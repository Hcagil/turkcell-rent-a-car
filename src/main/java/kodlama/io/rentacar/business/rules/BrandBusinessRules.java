package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.core.exceptions.BusinessException;
import kodlama.io.rentacar.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BrandBusinessRules {

    private final BrandRepository repository;

    public void checkIfBrandExist(int id) {
        if (!repository.existsById(id)) throw new BusinessException("Brand Id does not exist!");
    }
}
