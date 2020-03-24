package by.sivko.touristbot.dto;

import by.sivko.touristbot.validation.OnCreate;
import by.sivko.touristbot.validation.OnUpdate;
import by.sivko.touristbot.validation.UniqueCityName;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@UniqueCityName(groups = OnUpdate.class)
public class CityDto {

    @Positive(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @UniqueCityName(groups = OnCreate.class)
    @NotBlank
    private String name;

    @NotBlank
    private String info;

    public CityDto(String name, String info) {
        this.name = name;
        this.info = info;
    }
}
