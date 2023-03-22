package kodlama.io.rentacar.repository.concretes;

import kodlama.io.rentacar.enttities.concretes.Brand;
import kodlama.io.rentacar.repository.abstracts.BrandRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryBrandRepository implements BrandRepository {

    List<Brand> brands;

    public InMemoryBrandRepository() {
        brands = new ArrayList<>();
        brands.add(new Brand(1, "Mercedes",10,100000,"Mercede S500"));
        brands.add(new Brand(2, "BMW",20,120000,"BMW M3"));
        brands.add(new Brand(3, "Audi",25,110000,"Audi A6"));
        brands.add(new Brand(4, "Volvo",15,90000,"Volvo S90"));
        brands.add(new Brand(5, "Renault",50,65000,"Renault Megane RS"));
    }

    @Override
    public List<Brand> getAll() {
        return brands;
    }

    @Override
    public Brand getById(int id) {
        return brands.get(id-1);
    }

    @Override
    public Brand add(Brand brand) {
        brands.add(brand);
        return brand;
    }

    @Override
    public Brand update(int id, Brand brand) {
        return brands.set(id-1,brand);
    }

    @Override
    public void delete(int id) {
        brands.remove(id);
    }
}
