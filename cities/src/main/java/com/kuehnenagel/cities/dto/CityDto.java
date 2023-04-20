package com.kuehnenagel.cities.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CityDto {

    private Long id;

    private String name;

    private String photo;
}
