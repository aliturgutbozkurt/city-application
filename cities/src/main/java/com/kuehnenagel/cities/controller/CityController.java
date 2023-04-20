package com.kuehnenagel.cities.controller;

import com.kuehnenagel.cities.dto.CityDto;
import com.kuehnenagel.cities.service.CityService;
import com.kuehnenagel.cities.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<CityDto>> getCities(
            @RequestParam(
                    value = "pageNo",
                    defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
                    required = false)
            int pageNo,
            @RequestParam(
                    value = "pageSize",
                    defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                    required = false)
            int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)
            String sortBy,
            @RequestParam(
                    value = "sortDir",
                    defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,
                    required = false)
            String sortDir) {
        return ResponseEntity.ok(cityService.getCities(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<CityDto>> getCitiesByName(@RequestParam(
            value = "pageNo",
            defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
            required = false)
                                                         int pageNo,
                                                         @RequestParam(
                                                                 value = "pageSize",
                                                                 defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                                                                 required = false)
                                                         int pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)
                                                         String sortBy,
                                                         @RequestParam(
                                                                 value = "sortDir",
                                                                 defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,
                                                                 required = false)
                                                         String sortDir, @RequestParam String name) {
        return ResponseEntity.ok(cityService.findCitiesByName(pageNo, pageSize, sortBy, sortDir,name));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ALLOW_EDIT')")
    public ResponseEntity<CityDto> updateCity(@RequestBody CityDto dto) {
        return ResponseEntity.ok(cityService.updateCity(dto));
    }

}
