package com.mymarket.membership.member;

import com.mymarket.membership.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Member {

    private Long id;

    private boolean active;

    @Size(min = 1, max = 100, message = "{error.first-name}")
    private String firstName;

    @Size(min = 1, max = 100, message = "{error.last-name}")
    private String lastName;

    @Email(message = "{error.email.format}")
    @Size(min = 3, max = 100, message = "{error.email}")
    private String email;

    private String phone;

    private Address address;

    private List<MemberRole> roles;

    public boolean isAdmin() {
        return roles.contains(MemberRole.ADMIN);
    }

    public boolean isStoreOwner() {
        return roles.contains(MemberRole.STORE_OWNER);
    }

    public String getName() {
        var sb = new StringBuilder();
        if (firstName != null) sb.append(firstName).append(" ");
        if (lastName != null) sb.append(lastName);
        return sb.toString();
    }
}
