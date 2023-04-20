package com.kuehnenagel.cities.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
@Builder
public class City {

    @Id
    private Long id;
    @Column(nullable = false)
    private String name;

    @Lob
    @Column( length = 5000 )
    private String photo;
}
