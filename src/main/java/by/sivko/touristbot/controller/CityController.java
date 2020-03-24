package by.sivko.touristbot.controller;

import by.sivko.touristbot.dto.CityDto;
import by.sivko.touristbot.entity.City;
import by.sivko.touristbot.service.CityService;
import by.sivko.touristbot.validation.OnCreate;
import by.sivko.touristbot.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
@Validated
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable long id) {
        final Optional<City> optionalCity = this.cityService.findById(id);
        return optionalCity
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(this.cityService.findAll());
    }

    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody @Valid CityDto cityDto) {
        return new ResponseEntity<>(this.cityService.create(cityDto), HttpStatus.CREATED);
    }

    @Validated(OnUpdate.class)
    @PutMapping
    public ResponseEntity<City> updateCity(@RequestBody @Valid CityDto cityDto) {
        return ResponseEntity.ok(this.cityService.update(cityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable long id) {
        this.cityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
