package com.kuehnenagel.cities.repository;

import com.kuehnenagel.cities.entity.City;
import com.kuehnenagel.cities.utils.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CityRepositoryTests {

    @Autowired
    CityRepository cityRepository;

    private City city1;

    private City city2;

    @BeforeEach
    public void setup() {
        city1 = City.builder()
                .id(1L)
                .name("Gaziantep")
                .photo("http://cities.com/gaziantep.jpeg")
                .build();

        city2 = City.builder()
                .id(2L)
                .name("Istanbul")
                .photo("http://cities.com/istanbul.jpeg")
                .build();
    }

    @DisplayName("JUnit test for find city by name operation")
    @Test
    public void givenCities_whenFindByName_thenReturnCityObjects() throws Exception {
        // given - precondition or setup

        cityRepository.saveAll(List.of(city1,city2));
        var cityName = "gaziantep";
        Pageable pageable = PageRequest.of(Integer.valueOf(AppConstants.DEFAULT_PAGE_NUMBER),
                Integer.valueOf(AppConstants.DEFAULT_PAGE_SIZE), Sort.by(AppConstants.DEFAULT_SORT_BY).ascending());
        // when -  action or the behaviour that we are going test
        Page<City> cityPage = cityRepository.findByNameContainingIgnoreCase(cityName, pageable);

        // then - verify the output
        assertThat(cityPage.getNumberOfElements()).isGreaterThan(0) ;
    }


}
