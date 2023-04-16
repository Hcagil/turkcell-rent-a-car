package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.core.exceptions.BusinessException;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MaintenanceBuinessRules {
    private final MaintenanceRepository repository;

    public void checkIfMaintenanceExist(int id) {
        if (!repository.existsById(id)) throw new BusinessException("Maintenance is not exist!");
    }

    public void checkIfCarCanBeSentToMaintenance(int carId, State state) {
        checkIfCarUnderMaintenance(carId);
        checkIfCarRented(state);
    }


    public void checkIfCarRented(State state) {
        if (state.equals(State.RENTED)) throw new BusinessException("Car is rented!");
    }


    public void checkIfCarIsNotUnderMaintenance(int carId) {
        if (!repository.existsByCarIdAndIsCompletedFalse(carId)) {
            throw new BusinessException("Car is not under maintenance");
        }
    }

    public void checkIfCarUnderMaintenance(int carId) {
        if (repository.existsByCarIdAndIsCompletedFalse(carId)) {
            throw new BusinessException("Car is under maintenance!");
        }
    }
}
