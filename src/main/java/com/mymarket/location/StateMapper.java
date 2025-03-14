package com.mymarket.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateMapper {

    public State toDomain(StateEntity stateEntity) {
        if (stateEntity == null) {
            return null;
        }
        return State.builder()
            .id(stateEntity.getId())
            .name(stateEntity.getName())
            .build();
    }
}
