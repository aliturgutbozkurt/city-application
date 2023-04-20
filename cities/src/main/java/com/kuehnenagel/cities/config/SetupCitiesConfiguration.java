package com.kuehnenagel.cities.config;

import com.kuehnenagel.cities.entity.City;
import com.kuehnenagel.cities.exception.ApplicationException;
import com.kuehnenagel.cities.repository.CityRepository;
import com.kuehnenagel.cities.service.CityService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SetupCitiesConfiguration {

    private final CityService cityService;

    @Bean
    public CommandLineRunner runOnStartup() {
        return args -> {
            String csvFileName = "cities.csv"; // src/main/resources altındaki CSV dosyasının adını girin
            cityService.readAndSaveCsvFile(csvFileName);
        };
    }
}
