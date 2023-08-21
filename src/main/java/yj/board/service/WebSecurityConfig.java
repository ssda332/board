package yj.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import yj.board.jwt.*;
import yj.board.repository.MemberRepositoryV2;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private MemberRepositoryV2 memberRepository;

    @Autowired
    private PrincipalOauth2UserService principalDetailsService;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/xe/**", "/favicon.ico", "/error",
                        "/vendor/**", "/css/**", "/img/**", "/js/**", "/scss/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .addFilter(corsConfig.corsFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class) // 예외 처리 필터
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
                .authorizeRequests()
                /*.antMatchers("/")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")*/
                .antMatchers("/api/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/token/new")
                .loginProcessingUrl("/login")
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("Authorization")
                .and()
                // oauth2 방식 로그인
                .oauth2Login()
                .loginPage("/token/new")
                .successHandler(new OAuth2AuthenticationSuccessHandler())
                .failureHandler(new OAuth2AuthenticationFailureHandler())
                .defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(principalDetailsService);

                return http.build();
    }
}
