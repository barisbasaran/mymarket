package com.mymarket.membership.login;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPassword {

    @Size(min = 10, max = 50, message = "{error.member.reset-password.password1}")
    private String password1;

    @Size(min = 10, max = 50, message = "{error.member.reset-password.password2}")
    private String password2;

    @NotBlank(message = "{error.member.reset-password.token}")
    private String token;

    @AssertTrue(message = "{error.member.reset-password.password-equal}")
    private boolean isPasswordsEqual() {
        return password1.equals(password2);
    }
}
