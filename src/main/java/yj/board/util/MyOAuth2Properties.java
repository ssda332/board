package yj.board.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.security.oauth2")
@RequiredArgsConstructor
@Getter
@Setter
public class MyOAuth2Properties {

    private String password;

}