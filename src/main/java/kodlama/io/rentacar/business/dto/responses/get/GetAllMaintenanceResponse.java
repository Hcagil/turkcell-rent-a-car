package kodlama.io.rentacar.business.dto.responses.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllMaintenanceResponse {
    private int id;
    private Date returnDate;
    private Date sentDate;
    private double price;
    private int carId;
    private String carModelName;
    private String carModelBrandName;
}
