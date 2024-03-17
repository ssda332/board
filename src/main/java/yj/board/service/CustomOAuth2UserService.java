package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yj.board.domain.member.OAuth2Attribute;
import yj.board.domain.member.dto.MemberDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // OAuth2Attribute의 속성값들을 Map으로 반환 받는다.
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        Collection<? extends GrantedAuthority> roles = getRoles(memberAttribute);

        return new DefaultOAuth2User(
                roles,
                memberAttribute, "email");

    }

    /**
     *  1. 이미 가입된 사용자인지 체크
     *  2-1. 가입되었을 경우 가입된 아이디 권한 넘겨줌
     *  2-2. 신규 가입인 경우 ROLE_USER(일반 사용자 권한) 넘겨줌
     */
    public Collection<? extends GrantedAuthority> getRoles(Map<String, Object> memberAttribute) {
        // 사용자 email(또는 id) 정보를 가져온다.
        String email = (String) memberAttribute.get("email");
        // 이메일로 가입된 회원인지 조회한다.
        MemberDto findMember = memberService.getUserWithAuthorities(email);

        if (findMember == null) {
            // 회원이 존재하지 않을경우, memberAttribute의 exist 값을 false로 넣어준다.
            // 회원의 권한(회원이 존재하지 않으므로 기본권한인 ROLE_USER를 넣어준다)
            memberAttribute.put("exist", false);
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            // 회원이 존재할경우, memberAttribute의 exist 값을 true로 넣어준다.
            // 가지고있는 권한 반환해준다.
            memberAttribute.put("exist", true);
            return findMember.getAuthorityDtoSet().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                    .collect(Collectors.toList());
        }
    }
}