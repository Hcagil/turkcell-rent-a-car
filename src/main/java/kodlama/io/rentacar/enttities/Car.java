package kodlama.io.rentacar.enttities;

import jakarta.persistence.*;
import kodlama.io.rentacar.enttities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int modelYear;
    private String plate;
    private double dailyPrice;
    @Enumerated(EnumType.STRING)
    private State state; // Available, Rented, Maintance
    @ManyToOne
//    @JsonManagedReference
    private Model model;
    @OneToOne
    private Maintenance maintenance;
}
