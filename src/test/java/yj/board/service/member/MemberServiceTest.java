package yj.board.service.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import yj.board.domain.member.Authority;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.member.dto.MemberUpdateDto;
import yj.board.exception.DuplicateMemberException;
import yj.board.repository.MemberRepository;
import yj.board.service.MemberService;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member getMember() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority("ROLE_USER"));

        return Member.builder()
                .loginId("memberServiceTest")
                .password("memberServiceTest")
                .nickname("memberServiceTest")
                .authorities(authorities)
                .activated(true)
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp() {
        //given
        Member member = getMember();
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(passwordEncoder.encode(member.getPassword())).willReturn(member.getPassword());

        MemberDto memberDto = MemberDto.from(member);
        memberDto.setPassword(member.getPassword());
        //when
        MemberDto savedMember = memberService.signup(memberDto);
        //then
        assertThat(savedMember.getLoginId()).isSameAs(memberDto.getLoginId());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 회원")
    public void signup_DuplicateMemberException() {
        // given
        MemberDto memberDto = MemberDto.from(getMember());
        memberDto.setPassword(getMember().getPassword());

        given(memberRepository.findOneWithAuthoritiesByLoginId(any())).willReturn(Optional.of(getMember()));

        // then
        assertThrows(DuplicateMemberException.class, () -> memberService.signup(memberDto));
    }

    /*
    ** 더티 체킹 테스트는 스프링부트 단위의 테스트에서 수행되어야 함. (트랜잭션이 끝났을때 데이터 변경 유무 확인 필요)
    *
    @Test
    @DisplayName("회원 정보 수정")
    public void updateMember() {
        //given
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.fromUpdate(getMember());
        memberUpdateDto.setNickname("new");

        //when
        memberService.updateMember(memberUpdateDto);

        //then
        assertThat(updatedMember.getNickname()).isEqualTo(memberUpdateDto.getNickname());
    }*/
}
