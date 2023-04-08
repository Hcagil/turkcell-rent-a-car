package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository repository;
    private final ModelMapper mapper;
    private final CarService carService;
    @Override
    public List<GetAllMaintenanceResponse> getAll() {
        List<Maintenance> maintenances = repository.findAll();
        List<GetAllMaintenanceResponse> responses = maintenances
                .stream()
                .map(maintenance -> mapper.map(maintenance, GetAllMaintenanceResponse.class))
                .toList();

        return responses;
    }

    @Override
    public GetMaintenanceResponse getById(int id) {
        checkIfMaintenanceExist(id);
        Maintenance maintenance = repository.findById(id).orElseThrow();
        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);

        return response;
    }

    @Override
    public GetMaintenanceResponse returnCarFromMaintenance(int carId) {
        Maintenance maintenance = repository.findMaintenanceByCarIdAndIsCompletedFalse(carId);
        maintenance.setCompleted(true);
        maintenance.setEndDate(LocalDateTime.now());
        repository.save(maintenance);
        carService.changeState(carId,State.AVAILABLE);
        GetMaintenanceResponse response = mapper.map(maintenance, GetMaintenanceResponse.class);

        return response;
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        checkIfCarCanBeSentToMaintenance(request.getCarId());

        Maintenance maintenance = mapper.map(request,Maintenance.class);
        maintenance.setId(0);
        maintenance.setCompleted(false);
        maintenance.setStartDate(LocalDateTime.now());
        maintenance.setEndDate(null);

        int carId = request.getCarId();
        checkIfCarCanBeSentToMaintenance(carId);

        repository.save(maintenance);
        carService.changeState(carId,State.MAINTENANCE);

        CreateMaintenanceResponse response = mapper.map(maintenance, CreateMaintenanceResponse.class);
        return response;
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) {
        checkIfMaintenanceExist(id);
        Maintenance beforeMaintenance = repository.findById(id).orElseThrow();
        carService.changeState(beforeMaintenance.getCar().getId(),State.AVAILABLE);

        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(id);

        carService.changeState(maintenance.getCar().getId(),State.MAINTENANCE);
        Maintenance createdMaintenance = repository.save(maintenance);
        UpdateMaintenanceResponse response = mapper.map(createdMaintenance, UpdateMaintenanceResponse.class);
        return response;

//        Maintenance maintenance = mapper.map(request, Maintenance.class);

    }

    @Override
    public void delete(int id) {
        checkIfMaintenanceExist(id);
        Maintenance maintenance = repository.findById(id).orElseThrow();
        carService.changeState(maintenance.getCar().getId(),State.AVAILABLE);
        repository.deleteById(id);
    }

    // Business rules
    private void checkIfMaintenanceExist(int id) {
        if (!repository.existsById(id)) throw new RuntimeException("Car Id does not exist!");
    }

    private void checkIfCarCanBeSentToMaintenance(int carId){
        checkIfCarIsNotUnderMaintenance(carId);
        checkIfCarIsNotUnderMaintenance(carId);
//        checkIfCarInMaintenance(carId);
//        checkIfCarRented(carId);
    }

    private void checkIfCarInMaintenance(int carId){
        if(carService.getById(carId).getState().equals(State.MAINTENANCE)) throw new RuntimeException("Car is already in maintenance!");

    }
    private void checkIfCarRented(int carId){
        if(carService.getById(carId).getState().equals(State.RENTED)) throw new RuntimeException("Car is rented!");
    }

    private void checkIfCarIsNotUnderMaintenance(int carId){
        if(!repository.existsByCarIdAndIsCompletedFalse(carId)){
            throw new RuntimeException("Car is not under maintenance");
        }
    }

}
