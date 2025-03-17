package com.mymarket.membership.login;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponse {
    String token;
}
