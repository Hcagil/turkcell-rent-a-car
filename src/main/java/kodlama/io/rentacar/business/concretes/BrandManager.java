package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.business.dto.requests.create.CreateBrandRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateBrandRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateBrandResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllBrandsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetBrandResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateBrandResponse;
import kodlama.io.rentacar.entities.Brand;
import kodlama.io.rentacar.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class BrandManager implements BrandService {
    private final BrandRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<GetAllBrandsResponse> getAll() {
        List<Brand> brands = repository.findAll();
        List<GetAllBrandsResponse> responses = brands
                .stream()
                .map(brand -> mapper.map(brand,GetAllBrandsResponse.class))
                .toList();
        return responses;
    }

    @Override
    public GetBrandResponse getById(int id) {
        checkIfBrandExist(id);
        Brand brand = repository.findById(id).orElseThrow();
        GetBrandResponse response = mapper.map(brand, GetBrandResponse.class);
        return response;
    }


    @Override
    public CreateBrandResponse add(CreateBrandRequest request) {
//        Brand brand = new Brand();
//        brand.setName(request.getName());
//        repository.save(brand);
//
//        CreateBrandResponse response = new CreateBrandResponse();
//        response.setId(brand.getId());
//        response.setName(brand.getName());
//
//        return response;
        Brand brand = mapper.map(request,Brand.class);
        brand.setId(0);
        Brand createdBrand = repository.save(brand);
        CreateBrandResponse response = mapper.map(createdBrand,CreateBrandResponse.class);
        return response;
    }

    @Override
    public UpdateBrandResponse update(int id, UpdateBrandRequest request) {
        checkIfBrandExist(id);
        Brand brand = mapper.map(request,Brand.class);
        brand.setId(id);
        repository.save(brand);
        UpdateBrandResponse response = mapper.map(brand, UpdateBrandResponse.class);

        return response;
    }

    @Override
    public void delete(int id) {
        checkIfBrandExist(id);
        repository.deleteById(id);
    }

    // Business rules
    private void checkIfBrandExist(int id) {
        if (!repository.existsById(id)) throw new RuntimeException("Brand Id does not exist!");
    }

}
