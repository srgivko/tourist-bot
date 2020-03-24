package by.sivko.touristbot.validation;

import by.sivko.touristbot.dto.CityDto;
import by.sivko.touristbot.entity.City;
import by.sivko.touristbot.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class UniqueCityNameValidator implements ConstraintValidator<UniqueCityName, Object> {

    private final CityService cityService;

    @Autowired
    public UniqueCityNameValidator(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public void initialize(UniqueCityName constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object instanceof CityDto) {
            CityDto cityDto = (CityDto) object;
            final Optional<City> persistedCity = this.cityService.findByName(cityDto.getName());
            return !persistedCity.isPresent() || persistedCity.get().getId().equals(cityDto.getId());
        } else {
            String cityName = object.toString();
            return !this.cityService.findByName(cityName).isPresent();
        }
    }

}
