package kodlama.io.rentacar.business.dto.responses.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalResponse {
    private int id;
    private double carDailyPrice;
    private int rentedForDays;
    private double totalPrice; // readonly
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
