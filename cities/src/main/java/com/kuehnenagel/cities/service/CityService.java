package com.kuehnenagel.cities.service;

import com.kuehnenagel.cities.dto.CityDto;
import org.springframework.data.domain.Page;

public interface CityService {
    void readAndSaveCsvFile(String csvFileName);

    Page<CityDto> getCities(int pageNo, int pageSize, String sortBy, String sortDir);

    Page<CityDto> findCitiesByName(int pageNo, int pageSize, String sortBy, String sortDir,String name);

    CityDto updateCity(CityDto dto);
}
