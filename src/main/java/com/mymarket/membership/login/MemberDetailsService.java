package com.mymarket.membership.login;

import com.mymarket.membership.member.MemberNotFoundException;
import com.mymarket.membership.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var memberEntity = memberRepository.findByEmail(username)
            .orElseThrow(MemberNotFoundException::new);

        var rolesArray = memberEntity.getRoles().stream()
            .map(Objects::toString)
            .toList().toArray(new String[0]);
        return User.builder()
            .username(username)
            .password(memberEntity.getPassword())
            .roles(rolesArray)
            .build();
    }
}
