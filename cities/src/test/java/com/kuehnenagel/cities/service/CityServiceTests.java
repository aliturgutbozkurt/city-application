package com.kuehnenagel.cities.service;

import com.kuehnenagel.cities.dto.CityDto;
import com.kuehnenagel.cities.entity.City;
import com.kuehnenagel.cities.exception.ResourceNotFoundException;
import com.kuehnenagel.cities.mapper.GenericMapper;
import com.kuehnenagel.cities.repository.CityRepository;
import com.kuehnenagel.cities.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests {

    @Mock
    CityRepository cityRepository;

    @Spy
    ModelMapper modelMapper;

    @Spy
    GenericMapper genericMapper;

    @InjectMocks
    CityServiceImpl cityService;


    private City city;

    @BeforeEach
    public void setup() {
        city = City.builder()
                .id(1L)
                .name("Gaziantep")
                .photo("http://cities.com/gaziantep.jpeg")
                .build();
    }

    @DisplayName("JUnit test for get Cities")
    @Test
    public void givenCities_whenGetAllCities_thenReturnCitiesObjects() {
        // given - precondition or setup
        City city1 = City.builder()
                .id(2L)
                .name("Istanbul")
                .photo("http://cities.com/istanbul.jpeg")
                .build();

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = Sort.Direction.ASC.name();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        given(cityRepository.findAll(pageable)).willReturn(new PageImpl<>(List.of(city, city1)));

        // when -  action or the behaviour that we are going test
        Page<CityDto> cities = cityService.getCities(pageNo, pageSize, sortBy, sortDir);

        // then - verify the output
        assertThat(cities).isNotNull();
        assertThat(cities.getNumberOfElements()).isGreaterThan(0);
    }

    @DisplayName("JUnit test for find cities by name")
    @Test
    public void givenCityObjectsandCityName_whenGetCitiesByName_thenReturnCityListObject() {
        // given - precondition or setup
        City city1 = City.builder()
                .id(2L)
                .name("Istanbul")
                .photo("http://cities.com/istanbul.jpeg")
                .build();

        String cityName = "gaziantep";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = Sort.Direction.ASC.name();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        given(cityRepository.findByNameContainingIgnoreCase(cityName, pageable)).willReturn(new PageImpl<>(List.of(city)));

        // when -  action or the behaviour that we are going test
        Page<CityDto> citiesByName = cityService.findCitiesByName(pageNo, pageSize, sortBy, sortDir, cityName);

        // then - verify the output
        assertThat(citiesByName).isNotNull();
        assertThat(citiesByName.getNumberOfElements()).isGreaterThan(0);
    }

    @DisplayName("JUnit test for update city")
    @Test
    public void givenCityObject_whenUpdateCity_thenReturnUpdatedCityObject() {
        // given - precondition or setup
        var willUpdateCity = City.builder()
                .id(1L)
                .name("Istanbul")
                .photo("http://cities.com/istanbul.jpeg")
                .build();


        given(cityRepository.findById(willUpdateCity.getId())).willReturn(Optional.of(city));

        CityDto willUpdateCityDto = new ModelMapper().map(willUpdateCity, CityDto.class);

        given(cityRepository.save(city)).willReturn(willUpdateCity);


        // when -  action or the behaviour that we are going test
        CityDto updatedCity = cityService.updateCity(willUpdateCityDto);

        // then - verify the output
        verify(cityRepository, times(1)).save(city);
        assertThat(updatedCity.getName()).isEqualTo("Istanbul");
        assertThat(updatedCity.getPhoto()).isEqualTo("http://cities.com/istanbul.jpeg");
    }

    @DisplayName("JUnit test for can not find city while update city")
    @Test
    public void givenCityObject_whenUpdateCity_thenThrowsException() {
        // given - precondition or setup
        var willUpdateCity = City.builder()
                .id(3L)
                .name("Istanbul")
                .photo("http://cities.com/istanbul.jpeg")
                .build();


        given(cityRepository.findById(willUpdateCity.getId())).willReturn(Optional.empty());
        CityDto willUpdateCityDto = new ModelMapper().map(willUpdateCity, CityDto.class);

        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            cityService.updateCity(willUpdateCityDto);
        });

        // then
        verify(cityRepository, never()).save(any(City.class));
    }

}
