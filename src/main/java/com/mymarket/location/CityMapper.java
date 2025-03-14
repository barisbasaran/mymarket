package com.mymarket.location;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityMapper {

    public City toDomain(CityEntity cityEntity) {
        if (cityEntity == null) {
            return null;
        }
        return City.builder()
            .id(cityEntity.getId())
            .name(cityEntity.getName())
            .build();
    }
}
