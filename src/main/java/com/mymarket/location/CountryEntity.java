package com.mymarket.location;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "country", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "country",
        fetch = FetchType.EAGER,
        cascade = {CascadeType.ALL},
        orphanRemoval = true
    )
    private List<CityEntity> cities;

    private boolean hasState;

    @OneToMany(mappedBy = "country",
        fetch = FetchType.EAGER,
        cascade = {CascadeType.ALL},
        orphanRemoval = true
    )
    private List<StateEntity> states;

}
