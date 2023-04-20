package com.kuehnenagel.cities.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehnenagel.cities.dto.CityDto;
import com.kuehnenagel.cities.entity.City;
import com.kuehnenagel.cities.service.impl.CityServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CityController.class

        // this disables loading up the WebSecurityConfig.java file, otherwise it fails on start up
        , useDefaultFilters = false

        // this one indicates the specific filter to be used, in this case
        // related to the CityController we want to test
        , includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = CityController.class
        )
})
public class CityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityServiceImpl cityService;

    @Autowired
    private ObjectMapper objectMapper;


    private City city;

    @BeforeEach
    public void setup() throws Exception {
        city = City.builder()
                .id(1L)
                .name("Gaziantep")
                .photo("http://cities.com/gaziantep.jpeg")
                .build();
    }


    @DisplayName("JUnit test for Get All cities REST API")
    @Test
    @WithMockUser
    public void givenListOfCities_whenGetAllCities_thenReturnCityPage() throws Exception {
        // given - precondition or setup
        var cityDto = new CityDto();
        cityDto.setId(1L);
        cityDto.setName("Gaziantep");
        cityDto.setPhoto("http://cities.com/gaziantep.jpeg");
        var cityDto1 = new CityDto();
        cityDto1.setId(2L);
        cityDto1.setName("Istanbul");
        cityDto1.setPhoto("http://cities.com/istanbul.jpeg");


        Page<CityDto> pageOfCities = new PageImpl<>(List.of(cityDto, cityDto1));

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = Sort.Direction.ASC.name();
        given(cityService.getCities(pageNo, pageSize, sortBy, sortDir)).willReturn(pageOfCities);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/cities"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("JUnit test for Get cities by name REST API")
    @Test
    @WithMockUser
    public void givenListOfCities_whenGetCitiesByName_thenReturnCityList() throws Exception {
        // given - precondition or setup
        var cityDto = new CityDto();
        cityDto.setId(1L);
        cityDto.setName("Gaziantep");
        cityDto.setPhoto("http://cities.com/gaziantep.jpeg");

        var cityName = "Gaziantep";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = Sort.Direction.ASC.name();
        given(cityService.findCitiesByName(pageNo, pageSize, sortBy, sortDir,cityName)).willReturn(new PageImpl<>(List.of(cityDto)));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/cities/search?name=" + cityName));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("JUnit test for update city vREST API")
    @Test
    @WithMockUser(authorities = "ROLE_ALLOW_EDIT")
    public void givenUpdateCityObject_whenUpdateCity_thenReturnUpdatedCityObject() throws Exception {
        // given - precondition or setup
        var willUpdateCityDto = new CityDto();
        willUpdateCityDto.setId(1L);
        willUpdateCityDto.setName("Istanbul");
        willUpdateCityDto.setPhoto("http://cities.com/istanbul.jpeg");

        given(cityService.updateCity(any(CityDto.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(willUpdateCityDto)).with(csrf()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is((willUpdateCityDto.getId().intValue()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(willUpdateCityDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.photo", CoreMatchers.is(willUpdateCityDto.getPhoto())));

    }

}
