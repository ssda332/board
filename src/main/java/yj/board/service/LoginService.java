package yj.board.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.Member;
import yj.board.domain.member.dto.LoginDto;
import yj.board.domain.member.dto.MemberDto;
import yj.board.domain.token.RefreshToken;
import yj.board.domain.token.dto.TokenDto;
import yj.board.exception.AccessTokenNullException;
import yj.board.exception.LoginFailException;
import yj.board.exception.RefreshTokenException;
import yj.board.exception.UserNotFoundException;
import yj.board.jwt.TokenProvider;
import yj.board.repository.MemberRepository;
import yj.board.repository.RefreshTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

        // 회원 정보 존재하는지 확인
        Member member = memberRepository.findOneWithAuthoritiesByLoginId(loginDto.getLoginId())
                .orElseThrow(LoginFailException::new);

        // 회원 패스워드 일치 여부 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword()))
            throw new LoginFailException();

        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = tokenProvider.createToken(MemberDto.from(member));

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .memId(member.getId())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        // 리프레쉬 토큰 DB저장
        tokenRepository.save(refreshToken);
        tokenProvider.responseTokenDto(tokenDto, request, response);
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenDto tokenDto, HttpServletRequest request, HttpServletResponse response) {
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        String accessToken = tokenProvider.resolveToken(tokenDto.getAccessToken());
        String refreshToken = tokenProvider.resolveToken(tokenDto.getRefreshToken());

        if (!tokenProvider.validationToken(refreshToken)) {
            throw new RefreshTokenException();
        }

        // AccessToken 에서 Username (pk) 가져오기
//        if (accessToken.equals("null")) throw new AccessTokenNullException();
        Authentication authentication = tokenProvider.getAuthentication(accessToken, response);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long id = principal.getMember().getId();
        Member member = memberRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        RefreshToken refreshTokenDB = tokenRepository.findById(id)
                .orElseThrow(RefreshTokenException::new);

        // 리프레시 토큰 불일치 에러
        if (!refreshTokenDB.getRefreshToken().equals(refreshToken))
            throw new RefreshTokenException();

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
//        TokenDto newCreatedToken = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        TokenDto newCreatedToken = tokenProvider.createToken(MemberDto.from(member));
        RefreshToken updateRefreshToken = refreshTokenDB.update(newCreatedToken.getRefreshToken());
        tokenRepository.save(updateRefreshToken);
        tokenProvider.responseTokenDto(newCreatedToken, request, response);

        return newCreatedToken;
    }
}
