package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.ModelService;
import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetModelResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Model;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CarManager implements CarService {
    private final CarRepository repository;
    private final ModelMapper mapper;
    private final ModelService modelService;
    @Override
    public List<GetAllCarsResponse> getAll(boolean displayMaintenance) {
        List<Car> cars = repository.findAll();
        if (!displayMaintenance){
            cars = checkIfCarState(cars);
        }
        List<GetAllCarsResponse> responses = cars
                .stream()
                .map(car -> mapper.map(car, GetAllCarsResponse.class))
                .toList();

        return responses;
    }

    @Override
    public GetCarResponse getById(int id) {
        checkIfCarExist(id);
        Car car = repository.findById(id).orElseThrow();
        GetCarResponse response = mapper.map(car, GetCarResponse.class);
        return response;
    }

    @Override
    public CreateCarResponse add(CreateCarRequest request) {
        Car car = mapper.map(request, Car.class);
        car.setId(0);
        GetModelResponse getModelResponse = modelService.getById(car.getModel().getId());
        Model model = mapper.map(getModelResponse, Model.class);
        car.setModel(model);
        repository.save(car);
        CreateCarResponse response = mapper.map(car, CreateCarResponse.class);
        return response;
    }

    @Override
    public UpdateCarResponse update(int id, UpdateCarRequest request) {
        checkIfCarExist(id);
        Car car = mapper.map(request, Car.class);
        car.setId(id);
        GetModelResponse getModelResponse = modelService.getById(car.getModel().getId());
        Model model = mapper.map(getModelResponse, Model.class);
        car.setModel(model);
        Car createdCar = repository.save(car);
        UpdateCarResponse response = mapper.map(createdCar, UpdateCarResponse.class);
        return response;
    }

    @Override
    public void delete(int id) {
        checkIfCarExist(id);
        repository.deleteById(id);
    }

    @Override
    public void changeCarStatus(int id, State state) {
        Car car = repository.findById(id).orElseThrow();
        car.setState(state);
        repository.save(car);
    }

    // Business Rules
    private void checkIfCarExist(int id) {
        if (!repository.existsById(id)) throw new RuntimeException("Car Id does not exist!");
    }

    private List<Car> checkIfCarState(List<Car> cars){
        List<Car> newCars = new ArrayList<>();
        for (Car car:cars) {
            if (car.getState() != State.MAINTENANCE) {
                newCars.add(car);
            }
        }
        return newCars;
    }
}
