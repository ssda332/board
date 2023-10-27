package yj.board.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public interface JwtProperties {
//    String SECRET = "loose"; // 서버만 알고 있는 개인키
//    int EXPIRATION_TIME = 1000 * 60 * 10; // 10분 (1/1000초)
//    int EXPIRATION_TIME_REFRESH = 1000 * 60 * 60 * 24 * 7; // 일주일 (1/1000초)

    /**
     * 테스트용 EXPIRATION_TIME
     */
    int EXPIRATION_TIME = 1000 * 10 ; // 10초 (1/1000초)
    int EXPIRATION_TIME_REFRESH = 1000 * 60 * 60 * 24; // 24시간 (1/1000초)

    String SECRET = "SomeSecretForJWTGeneration";

    String TOKEN_PREFIX = "Bearer ";
    String REFRESH_HEADER_STRING = "RefreshToken";
    String HEADER_STRING = "Authorization";

    //Auth url
    String AUTH_URL = "/token";

    // OAuth2 공통 비밀번호
    String oauth2Password = "youngjin123";
}