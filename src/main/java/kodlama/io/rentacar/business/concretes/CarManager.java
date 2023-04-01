package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.enttities.Car;
import kodlama.io.rentacar.enttities.Model;
import kodlama.io.rentacar.repository.CarRepository;
import kodlama.io.rentacar.repository.ModelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarManager implements CarService {
    private final CarRepository repository;
    private final ModelMapper mapper;
    private final ModelRepository modelRepository;

    @Override
    public List<GetAllCarsResponse> getAll() {
        List<Car> cars = repository.findAll();
        List<GetAllCarsResponse> responses = cars
                .stream()
                .map(car -> mapper.map(car, GetAllCarsResponse.class))
                .toList();
        //! brandName = null
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
        Car car = mapper.map(request,Car.class);
        car.setId(0);
        Model model = modelRepository.findById(request.getModelId()).orElseThrow();
        car.setModel(model);
        repository.save(car);
        CreateCarResponse response = mapper.map(car, CreateCarResponse.class);
        response.setBrandName(model.getBrand().getName());
        return response;
    }

    // Business Rules
    private void checkIfCarExist(int id){
        if (!repository.existsById(id)) throw new RuntimeException("Car Id does not exist!");
    }
}
