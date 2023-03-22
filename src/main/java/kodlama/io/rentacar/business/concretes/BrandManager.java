package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.enttities.concretes.Brand;
import kodlama.io.rentacar.repository.abstracts.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandManager implements BrandService {
    @Autowired
    private final BrandRepository repository;

    @Autowired
    public BrandManager(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Brand> getAll() {
        // is kurallari
        if (repository.getAll().size() == 0) throw new RuntimeException("Kayitli marka yok!!");
        return repository.getAll();
    }

    @Override
    public Brand getById(int id) {
        return repository.getById(id);
    }

    @Override
    public Brand add(Brand brand) {

        return repository.add(brand);
    }

    @Override
    public Brand update(int id, Brand brand) {
        return null;
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    private void validateBrand(Brand brand){

    }

    private  void checkIfPriceValid(Brand brand){
        if(brand.getPrice() <= 0) new IllegalArgumentException("Price cannot be less than or equal to zero.");
    }
    private void checkIfQuantityValid(Brand brand) {
        if (brand.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity cannot be less than or equal to zero.");
    }

    private void checkIfDescriptionLengthValid(Brand brand) {
        if (brand.getDescription().length() < 10 || brand.getDescription().length() > 50)
            throw new IllegalArgumentException("Description length must be between 10 and 50 characters.");
    }
}
