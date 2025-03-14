package com.mymarket.membership.login;

import com.mymarket.membership.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyEmailRepository extends JpaRepository<VerifyEmailEntity, Long> {

    Optional<VerifyEmailEntity> findByToken(String token);

    Optional<VerifyEmailEntity> findByMember(MemberEntity member);
}
