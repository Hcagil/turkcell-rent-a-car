package kodlama.io.rentacar.business.dto.responses.create;

import kodlama.io.rentacar.enttities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarResponse {
    private int id;
    private int modelYear;
    private String plate;
    private double dailyPrice;
    private State state;
    private String modelName;
    private String brandName;
}