package com.mymarket.membership.login;

import com.mymarket.membership.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verify_email", uniqueConstraints = {@UniqueConstraint(columnNames = {"token"})})
public class VerifyEmailEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String token;
 
    @OneToOne(targetEntity = MemberEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "member_id")
    private MemberEntity member;
}
