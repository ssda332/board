package yj.board.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import yj.board.domain.member.dto.AuthorityDto;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.token.dto.TokenDto;
import yj.board.jwt.JwtProperties;
import yj.board.service.TokenService;
import yj.board.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //String provider = oAuth2User.getAttribute("provider"); //서비스 제공 플랫폼이 어디인지 가져옴
        // CustomOAuth2UserService에서 셋팅한 로그인한 회원 존재 여부를 가져온다.
        boolean isExist = oAuth2User.getAttribute("exist");
        // 인증된 사용자 정보를 memberDto로 파싱한다
        MemberDto memberDto = getMemberDto(oAuth2User);
        memberDto.setPassword(JwtProperties.oauth2Password);

        if (!isExist) {
            // 회원가입이 되어있지 않을 경우, 자동 회원가입 수행
            memberDto = memberService.signup(memberDto);
        }

        LoginDto loginDto = new LoginDto(memberDto.getLoginId(), JwtProperties.oauth2Password);
        TokenDto tokenDto = tokenService.login(loginDto);

        String targetUrl = createTargetUrl(tokenDto);

        // refresh token 쿠키로 보내기
        Cookie refreshToken = new Cookie(JwtProperties.REFRESH_HEADER_STRING, tokenDto.getRefreshToken());

        String serverDomain = System.getenv("SERVER_DOMAIN");
        if (!(serverDomain == null || serverDomain.isEmpty())) {
            // 개발서버일경우, secure설정
            refreshToken.setSecure(true);
        }

        refreshToken.setPath("/");
        refreshToken.setHttpOnly(true);
        response.addCookie(refreshToken);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    public MemberDto getMemberDto(OAuth2User oAuth2User) {
        // OAuth2User로 부터 Role을 얻어온다.
        String role = oAuth2User.getAuthorities().stream().
                findFirst() // 첫번째 Role을 찾아온다.
                .orElseThrow(IllegalAccessError::new) // 존재하지 않을 시 예외를 던진다.
                .getAuthority(); // Role을 가져온다.

        String[] roleArr = role.split(",");
        Arrays.stream(roleArr)
                .map(authority -> AuthorityDto.builder().authorityName(authority).build())
                .collect(Collectors.toSet());


        MemberDto memberDto = MemberDto.builder()
                .loginId(oAuth2User.getAttribute("email"))
                .nickname(oAuth2User.getAttribute("email"))
                .authorityDtoSet(Arrays.stream(roleArr)
                        .map(authority -> AuthorityDto.builder().authorityName(authority).build())
                        .collect(Collectors.toSet()))
                .build();

        return memberDto;
    }

    public String createTargetUrl(TokenDto tokenDto) {
        // 환경 변수에서 도메인 이름을 읽어오고, 설정되지 않았다면 기본값을 사용
        String serverDomain = System.getenv("SERVER_DOMAIN");
        if (serverDomain == null || serverDomain.isEmpty()) {
            serverDomain = "localhost:9091"; // 기본값 설정
        }

        // accessToken과 refreshToken을 쿼리스트링에 담는 URL 생성
        String targetUrl = UriComponentsBuilder.fromUriString("http://" + serverDomain + "/")
                .queryParam("accessToken", tokenDto.getAccessToken())
//                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return targetUrl;
    }
}
