package com.mymarket.store;

import com.mymarket.membership.member.Member;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Store {

    private Long id;

    @Size(min = 1, max = 300, message = "{error.store.name}")
    private String name;

    private Member member;
}
