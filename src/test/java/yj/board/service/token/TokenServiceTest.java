package yj.board.service.token;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.Authority;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.member.dto.MemberInfoDto;
import yj.board.domain.token.RefreshToken;
import yj.board.domain.token.dto.ReissueTokenDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.exception.member.LoginFailException;
import yj.board.exception.member.RefreshTokenException;
import yj.board.exception.member.UserNotFoundException;
import yj.board.jwt.TokenProvider;
import yj.board.repository.MemberRepository;
import yj.board.repository.RefreshTokenRepository;
import yj.board.service.TokenService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @InjectMocks
    private TokenService tokenService;
    @Mock
    private MemberRepository memberRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository tokenRepository;
    @Mock
    private TokenProvider tokenProvider;


    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        //given
        LoginDto loginDto = getLoginDto();
        Member member = getMember();
        TokenDto tokenDto = getTokenDto();
        RefreshToken refreshToken = getRefreshToken(member, tokenDto);

        given(memberRepository.findOneWithAuthoritiesByLoginId(any(String.class))).willReturn(Optional.of(member));
        given(tokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);
        given(tokenProvider.createToken(any(MemberInfoDto.class))).willReturn(tokenDto);

        //when
        TokenDto result = tokenService.login(loginDto);

        //then
        Assertions.assertThat(result.getAccessToken()).isEqualTo("testAccessToken");
        Assertions.assertThat(result.getRefreshToken()).isEqualTo("testRefreshToken");
    }

    @Test
    @DisplayName("로그인 실패 - 해당 회원 정보 없음")
    void loginFail_notMember() {
        //given
        LoginDto loginDto = getLoginDto();
        given(memberRepository.findOneWithAuthoritiesByLoginId(any(String.class))).willReturn(Optional.ofNullable(null));
        //when
        assertThrows(LoginFailException.class, () -> tokenService.login(loginDto));
    }

    @Test
    @DisplayName("로그인 실패 - 패스워드가 일치하지 않음")
    void loginFail_notMathchedPassword() {
        //given
        LoginDto loginDto = getLoginDto();
        Member member = getMember();
        member.setPassword("fail");
        given(memberRepository.findOneWithAuthoritiesByLoginId(any(String.class))).willReturn(Optional.of(member));
        //when
        assertThrows(LoginFailException.class, () -> tokenService.login(loginDto));
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueSuccess() {
        //given
        Member member = getMember();
        Authentication authentication = getAuthentication(member);
        TokenDto tokenDto = new TokenDto("testToken", "testToken");
        RefreshToken refreshToken = getRefreshToken(member, tokenDto);

        given(tokenProvider.resolveToken(any())).willReturn("testToken");
        given(tokenProvider.validationToken(any())).willReturn(true);
        given(tokenProvider.getAuthentication(any(String.class))).willReturn(authentication);
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(tokenRepository.findById(any())).willReturn(Optional.of(refreshToken));
        given(tokenProvider.createToken(any(MemberInfoDto.class))).willReturn(tokenDto);

        //when
        ReissueTokenDto reissue = tokenService.reissue(tokenDto);

        //then
        Assertions.assertThat(reissue.getAccessToken()).isEqualTo("testToken");
        Assertions.assertThat(reissue.getRefreshToken()).isEqualTo("testToken");

    }

    @Nested
    @DisplayName("토큰 재발급 실패")
    class testReissueFail {
        // given
        Member member = getMember();
        Authentication authentication = getAuthentication(member);
        TokenDto tokenDto = new TokenDto("testToken", "testToken");
        RefreshToken refreshToken = getRefreshToken(member, tokenDto);

        @Test
        @DisplayName("리프레쉬 토큰 만료 에러")
        void expiredRefreshToken() {
            given(tokenProvider.validationToken(any())).willReturn(false);

            //then
            assertThrows(RefreshTokenException.class, () -> tokenService.reissue(tokenDto));
        }


        @Test
        @DisplayName("user pk로 유저 검색 실패")
        void dbSelectFail() {
            given(tokenProvider.resolveToken(any())).willReturn("testToken");
            given(tokenProvider.validationToken(any())).willReturn(true);
            given(tokenProvider.getAuthentication(any(String.class))).willReturn(authentication);
            given(memberRepository.findById(any())).willReturn(Optional.ofNullable(null));

            //then
            assertThrows(UserNotFoundException.class, () -> tokenService.reissue(tokenDto));
        }

        @Test
        @DisplayName("repo 에 저장된 Refresh Token 이 없음")
        void dbSelectFail1() {
            given(tokenProvider.resolveToken(any())).willReturn("testToken");
            given(tokenProvider.validationToken(any())).willReturn(true);
            given(tokenProvider.getAuthentication(any(String.class))).willReturn(authentication);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(tokenRepository.findById(any())).willReturn(Optional.ofNullable(null));

            //then
            assertThrows(RefreshTokenException.class, () -> tokenService.reissue(tokenDto));
        }
    }


    private static Authentication getAuthentication(Member member) {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream("ROLE_USER".split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        new PrincipalDetails(member),
                        null, // 패스워드는 모르니까 null 처리, 인증 용도 x
                        authorities);

        return authentication;
    }

    private static LoginDto getLoginDto() {
        LoginDto loginDto = new LoginDto("loginTest", "Testtest1");
        return loginDto;
    }

    private static RefreshToken getRefreshToken(Member member, TokenDto tokenDto) {
        RefreshToken refreshToken = RefreshToken.builder()
                .memId(member.getId())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
        return refreshToken;
    }

    private static TokenDto getTokenDto() {
        TokenDto tokenDto = TokenDto.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        return tokenDto;
    }

    private static Member getMember() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority("ROLE_USER"));
        Member member = Member.builder()
                .id(1L)
                .loginId("loginTest")
                .password("$2a$10$rXhGqHZLFt1zqMU7lge6yeMgP/j.v7iagrJHDT4i09t2I6hUd99Z.")
                .nickname("loginTest")
                .authorities(authorities)
                .activated(true)
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();
        return member;
    }

}
