package kodlama.io.rentacar.business.dto.responses.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModelResponse {
    private String name;
    private String brandName;
}
