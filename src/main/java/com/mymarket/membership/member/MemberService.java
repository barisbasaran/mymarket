package com.mymarket.membership.member;


import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public Member getMember(Long memberId) {
        var member = memberRepository.findById(memberId)
            .map(memberMapper::toDomain)
            .orElseThrow(MemberNotFoundException::new);
        log.info("Member found {}", member);
        return member;
    }

    public void checkMemberActive(String email) {
        var memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);
        if (!memberEntity.isActive()) {
            throw new ApplicationException("email-not-verified");
        }
    }

    public Optional<Member> getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByEmail(authentication.getName())
            .map(memberMapper::toDomain);
    }

    public List<Member> getMembers() {
        var members = memberRepository.findAll().stream()
            .map(memberMapper::toDomain)
            .toList();
        log.info("Members found {}", members);
        return members;
    }

    public Member updateMember(Member member) {
        var memberEntity = memberRepository.findById(member.getId())
            .orElseThrow(MemberNotFoundException::new);

        memberEntity.setFirstName(member.getFirstName());
        memberEntity.setLastName(member.getLastName());
        memberEntity.setPhone(member.getPhone());

        var updatedMember = memberMapper.toDomain(memberRepository.save(memberEntity));

        log.info("member updated {}", updatedMember);
        return updatedMember;
    }
}
