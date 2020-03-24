package by.sivko.touristbot.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "cities")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_generator")
    @SequenceGenerator(name="city_generator", sequenceName = "cities_id_seq", allocationSize=1)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "City name cannot be empty")
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "City info cannot be empty")
    private String info;

    public City(String name, String info) {
        this.name = name;
        this.info = info;
    }
}
