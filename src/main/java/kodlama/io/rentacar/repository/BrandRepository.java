package kodlama.io.rentacar.repository;

import kodlama.io.rentacar.enttities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD Operations
public interface BrandRepository extends JpaRepository<Brand,Integer> {


}