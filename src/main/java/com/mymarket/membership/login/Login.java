package com.mymarket.membership.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Login {

    @Email(message = "{error.email.format}")
    @Size(min = 3, max=100, message = "{error.email}")
    private String email;

    @Size(min = 10, max=50, message = "{error.member.password}")
    private String password;
}
