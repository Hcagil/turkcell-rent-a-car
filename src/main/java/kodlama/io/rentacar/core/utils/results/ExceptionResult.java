package kodlama.io.rentacar.core.utils.results;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ExceptionResult<T extends Exception> {
    private LocalDateTime timestamp;
    private String type;
    private String messsage;

    public ExceptionResult(Class<T> type, String messsage) {
        this.timestamp = LocalDateTime.now();
        this.type = type.getSimpleName();
        this.messsage = messsage;
    }
}
