package yj.board.repository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import yj.board.domain.member.Authority;
import yj.board.domain.member.Member;
import yj.board.domain.token.RefreshToken;
import yj.board.jwt.JwtProperties;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("리프레쉬 토큰 저장하기")
    void saveToken() {
        //given
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .loginId("tokenTest")
                .password(null)
                .nickname("tokenTest")
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        Member savedMember = memberRepository.save(member);

        String refreshToken = JWT.create()
                .withSubject(savedMember.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME_REFRESH))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        RefreshToken token = RefreshToken.builder()
                .memId(savedMember.getId())
                .refreshToken(refreshToken)
                .build();

        //when
        RefreshToken savedToken = tokenRepository.save(token);

        //then
        assertThat(savedToken.getRefreshToken()).isSameAs(token.getRefreshToken());
        assertThat(savedToken.getMemId()).isSameAs(token.getMemId());
    }

}
