package com.mymarket.membership.login;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePassword {

    @Size(min = 10, max = 50, message = "{error.member.change-password.password1}")
    private String password1;

    @Size(min = 10, max = 50, message = "{error.member.change-password.password2}")
    private String password2;

    @AssertTrue(message = "{error.member.change-password.password-equal}")
    private boolean isPasswordsEqual() {
        return password1.equals(password2);
    }
}
