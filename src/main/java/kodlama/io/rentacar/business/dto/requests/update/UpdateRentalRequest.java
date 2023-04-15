package kodlama.io.rentacar.business.dto.requests.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRentalRequest {
    private int carId;
    private int rentedForDays;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
