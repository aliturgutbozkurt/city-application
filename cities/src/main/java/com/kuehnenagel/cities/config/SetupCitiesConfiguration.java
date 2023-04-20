package com.kuehnenagel.cities.config;

import com.kuehnenagel.cities.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
