package com.mymarket.membership.member;

import com.mymarket.membership.address.AddressEntity;
import jakarta.persistence.*;
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
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ElementCollection(
        fetch = FetchType.EAGER
    )
    private List<MemberRole> roles;
}
