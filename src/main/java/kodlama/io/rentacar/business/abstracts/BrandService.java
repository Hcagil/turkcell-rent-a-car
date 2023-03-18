package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.enttities.concretes.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAll();
}
