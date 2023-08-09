package yj.board.service;

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
}