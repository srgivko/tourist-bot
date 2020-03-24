package by.sivko.touristbot.service;

import by.sivko.touristbot.dto.CityDto;
import by.sivko.touristbot.entity.City;
import by.sivko.touristbot.exception.EntityNotFoundException;
import by.sivko.touristbot.repository.CityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Optional<City> findById(long id) {
        return this.cityRepository.findById(id);
    }

    @Override
    public List<City> findAll() {
        return this.cityRepository.findAll();
    }

    @Transactional
    @Override
    public City create(CityDto cityDto) {
        return this.cityRepository.save(new City(cityDto.getName(), cityDto.getName()));
    }

    @Transactional
    @Override
    public void delete(long id) {
        this.cityRepository.deleteById(id);
    }

    @Transactional
    @Override
    public City update(CityDto cityDto) {
        City persistedCity = this.findById(cityDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(City.class.getSimpleName(), "id", String.valueOf(cityDto.getId())));
        BeanUtils.copyProperties(cityDto, persistedCity , "id");
        return persistedCity;
    }

    @Override
    public Optional<City> findByName(String name) {
        return this.cityRepository.findByNameIgnoreCase(name);
    }
}
