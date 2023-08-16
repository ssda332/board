package yj.board.service;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yj.board.controller.argumentresolver.LoginMemberArgumentResolver;
import yj.board.repository.JpaMemberRepository;
import yj.board.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final EntityManager em;

    public WebConfig(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    //쿠키에 특수문자 허용해주는 프로세서로 바꿈
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (serverFactory) -> serverFactory.addContextCustomizers(
                (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}