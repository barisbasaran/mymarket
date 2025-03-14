package com.mymarket.membership.login;

import com.mymarket.membership.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, Long> {

    Optional<PasswordResetEntity> findByToken(String token);

    Optional<PasswordResetEntity> findByMember(MemberEntity member);
}
