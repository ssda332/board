package yj.board.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public interface JwtProperties {
//    String SECRET = "loose"; // 서버만 알고 있는 개인키
    String SECRET = "SomeSecretForJWTGeneration";
    int EXPIRATION_TIME = 1000 * 60 * 10; // 30분 (1/1000초)
    int EXPIRATION_TIME_REFRESH = 1000 * 60 * 60; // 1시간 (1/1000초)
//    int EXPIRATION_TIME = 10000; // 10초 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}