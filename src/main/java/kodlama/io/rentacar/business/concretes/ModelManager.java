package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.ModelService;
import kodlama.io.rentacar.business.dto.requests.create.CreateModelRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateModelRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateModelResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllModelsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetModelResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateModelResponse;
import kodlama.io.rentacar.enttities.Brand;
import kodlama.io.rentacar.enttities.Model;
import kodlama.io.rentacar.repository.BrandRepository;
import kodlama.io.rentacar.repository.ModelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ModelManager implements ModelService {
    private final ModelRepository repository;
    private final ModelMapper mapper;
    private final BrandRepository brandRepository;

    @Override
    public List<GetAllModelsResponse> getAll() {
        List<Model> models = repository.findAll();
        List<GetAllModelsResponse> responses = models
                .stream()
                .map(model -> mapper.map(model, GetAllModelsResponse.class))
                .toList();
        return responses;
    }

    @Override
    public GetModelResponse getById(int id) {
        checkIfBrandExist(id);
        Model model = repository.findById(id).orElseThrow();
        GetModelResponse response = mapper.map(model, GetModelResponse.class);
        return response;
    }

    @Override
    public CreateModelResponse add(CreateModelRequest request) {
        Model model = mapper.map(request,Model.class);
        model.setId(0);
        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow();
        model.setBrand(brand);
        repository.save(model);
        CreateModelResponse response = mapper.map(model, CreateModelResponse.class);
        return response;
    }

    @Override
    public UpdateModelResponse update(int id, UpdateModelRequest request) {
        checkIfBrandExist(id);
        Model model = mapper.map(request,Model.class);
        model.setId(id);
        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow();
        model.setBrand(brand);
        repository.save(model);
        UpdateModelResponse response = mapper.map(model, UpdateModelResponse.class);
        return response;
    }

    // Business Rules
    private void checkIfBrandExist(int id){
        if (!repository.existsById(id)) throw new RuntimeException("Model Id does not exist!");
    }
}
