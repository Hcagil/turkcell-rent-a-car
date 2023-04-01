package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.responses.get.GetAllCarsResponse;

import java.util.List;

public interface CarService {

    List<GetAllCarsResponse> getAll();
}
