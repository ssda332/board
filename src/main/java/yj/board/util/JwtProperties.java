package yj.board.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Getter
@Setter
public class JwtProperties {
    private String header;
    private String refreshTokenHeader;
    private String secret;
    private Integer tokenValidityInSeconds;
    private Integer refreshTokenValidityInSeconds;

}
