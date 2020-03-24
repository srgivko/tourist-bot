package by.sivko.touristbot.service;

import by.sivko.touristbot.dto.CityDto;
import by.sivko.touristbot.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    Optional<City> findById(long id);

    List<City> findAll();

    City create(CityDto cityDto);

    void delete(long id);

    City update(CityDto cityDto);

    Optional<City> findByName(String name);
}
