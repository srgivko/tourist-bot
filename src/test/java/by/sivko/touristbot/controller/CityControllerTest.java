package by.sivko.touristbot.controller;

import by.sivko.touristbot.dto.CityDto;
import by.sivko.touristbot.entity.City;
import by.sivko.touristbot.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CityController.class)
class CityControllerTest {

    private static final long EXIST_CITY_ID = 1;
    private static final long NOT_EXIST_CITY_ID = 100;
    private static final City ENTITY_CITY_1 = new City("test", "test");
    private static final City ENTITY_CITY_2 = new City("test2", "test2");
    private static final City[] ARRAYS_OF_CITIES = {ENTITY_CITY_1, ENTITY_CITY_2};

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService cityService;


    @BeforeEach
    void setMockOutput() {
    }

    @DisplayName("Test findAll()")
    @Test
    void testFindAll() throws Exception {
        when(this.cityService.findAll()).thenReturn(Arrays.asList(ARRAYS_OF_CITIES));
        this.mvc.perform(get("/cities")
                .contentType("application/json")
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(ENTITY_CITY_1.getName()));
        verify(this.cityService, times(1)).findAll();
    }

    @DisplayName("Test findById()")
    @Test
    void testFindById() throws Exception {
        when(this.cityService.findById(EXIST_CITY_ID)).thenReturn(Optional.of(ENTITY_CITY_1));
        final MvcResult mvcResult = this.mvc.perform(get("/cities/{id}", EXIST_CITY_ID)
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(ENTITY_CITY_1.getName()))
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(objectMapper.writeValueAsString(ENTITY_CITY_1)).isEqualToIgnoringWhitespace(actualResponseBody);
        verify(this.cityService, times(1)).findById(EXIST_CITY_ID);
    }

    @DisplayName("Test findById() if not exist id")
    @Test
    void testFindByIdNotExistId() throws Exception {
        when(this.cityService.findById(NOT_EXIST_CITY_ID)).thenReturn(Optional.empty());
        this.mvc.perform(get("/cities/{id}", NOT_EXIST_CITY_ID)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(this.cityService, times(1)).findById(NOT_EXIST_CITY_ID);
    }

    @DisplayName("Test create city")
    @Test
    void testCreateCity() throws Exception {
        CityDto cityDto = new CityDto("test", "test");
        City city = new City(cityDto.getName(), cityDto.getInfo());
        when(this.cityService.create(cityDto)).thenReturn(city);

        this.mvc.perform(post("/cities")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(cityDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(city.getName()));
        verify(this.cityService, times(1)).create(cityDto);
    }

    @DisplayName("Test create city with empty name")
    @Test
    void testCreateCityWithEmptyName() throws Exception {
        CityDto cityDto = new CityDto("", "test");
        this.mvc.perform(post("/cities")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(cityDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Test create city with empty info")
    @Test
    void testCreateCityWithEmptyInfo() throws Exception {
        CityDto cityDto = new CityDto("test", "");

        this.mvc.perform(post("/cities")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(cityDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(this.cityService, times(0)).create(any());
    }

    @DisplayName("Test create city with exist city name info")
    @Test
    void testDeleteCity() throws Exception {
        this.mvc.perform(delete("/cities/{id}", EXIST_CITY_ID))
                .andDo(print())
                .andExpect(status().isOk());
        verify(this.cityService, times(1)).delete(EXIST_CITY_ID);
    }

    @DisplayName("Test create city with exist city name info")
    @Test
    void testDeleteNotExistCity() throws Exception {
        this.mvc.perform(delete("/cities/{id}", NOT_EXIST_CITY_ID))
                .andDo(print())
                .andExpect(status().isOk());
        verify(this.cityService, times(1)).delete(NOT_EXIST_CITY_ID);
    }

    @DisplayName("Test change city")
    @Test
    void testChangeWithNameCity() throws Exception {
        CityDto cityDto = new CityDto(EXIST_CITY_ID, "test", "test");

        this.mvc.perform(put("/cities")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(cityDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(this.cityService, times(1)).update(cityDto);
    }

    @DisplayName("Test change city without city name")
    @Test
    void testChangeWithoutNameCity() throws Exception {
        CityDto cityDto = new CityDto(EXIST_CITY_ID, null, "test");

        this.mvc.perform(put("/cities")
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(cityDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(this.cityService, times(0)).update(cityDto);
    }
}