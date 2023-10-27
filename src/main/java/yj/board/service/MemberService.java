package yj.board.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.member.Authority;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.MemberUpdateDto;
import yj.board.exception.DuplicateMemberException;
import yj.board.exception.MemberUpdateException;
import yj.board.repository.MemberRepository;
import yj.board.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /*public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }*/

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findOneWithAuthoritiesByLoginId(memberDto.getLoginId()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member user = Member.builder()
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();

        MemberDto from = MemberDto.from(memberRepository.save(user));

        return from;
    }

    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String username) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByLoginId(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByLoginId).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities() {
        return MemberDto.from(SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByLoginId).orElse(null));
    }

    @Transactional
    public void updateMember(MemberUpdateDto memberUpdateDto) {
        Member member = SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByLoginId).orElse(null);
        if (member == null) {
            throw new MemberUpdateException();
        }
        // 닉네임 변경
        member.setNickname(memberUpdateDto.getNickname());
    }
}