package com.kuehnenagel.cities.service.impl;

import com.kuehnenagel.cities.dto.CityDto;
import com.kuehnenagel.cities.entity.City;
import com.kuehnenagel.cities.exception.ApplicationException;
import com.kuehnenagel.cities.exception.ResourceNotFoundException;
import com.kuehnenagel.cities.mapper.GenericMapper;
import com.kuehnenagel.cities.repository.CityRepository;
import com.kuehnenagel.cities.service.CityService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {


    private final CityRepository cityRepository;

    private final GenericMapper genericMapper;

    private final ModelMapper modelMapper;

    @Override
    public void readAndSaveCsvFile(String fileName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<City> cities = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                City City = new City();
                City.setId(Long.valueOf(record.get("id")));
                City.setName(record.get("name"));
                City.setPhoto(record.get("photo"));
                cities.add(City);
            }

            cityRepository.saveAll(cities);
            csvParser.close();

        } catch (IOException e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @Override
    public Page<CityDto> getCities(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<City> all = cityRepository.findAll(pageable);
        return genericMapper.mapPage(all, CityDto.class);

    }

    @Override
    public Page<CityDto> findCitiesByName(int pageNo, int pageSize, String sortBy, String sortDir,String name) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<City> cities = cityRepository.findByNameContainingIgnoreCase(name, pageable);
        return genericMapper.mapPage(cities,CityDto.class);

    }

    @Override
    public CityDto updateCity(CityDto dto) {
        City city = cityRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("City", "Id", dto.getId()));
        city.setName(dto.getName());
        city.setPhoto(dto.getPhoto());
        City updatedCity = cityRepository.save(city);
        return modelMapper.map(updatedCity,CityDto.class);
    }
}
