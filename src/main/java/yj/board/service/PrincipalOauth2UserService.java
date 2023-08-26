package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.dto.oauth2.GoogleUserInfo;
import yj.board.domain.dto.oauth2.OAuth2UserInfo;
import yj.board.domain.member.Authority;
import yj.board.domain.member.Member;
import yj.board.repository.MemberRepositoryV2;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private final MemberRepositoryV2 memberRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    //함수종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        //각 서비스에 맞게 정보를 가져옴
        //OAuth2UserInfo는 직접 만든 인터페이스 이고,
        //각 브랜드별로 구현체를 만듬

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else {
            return null;
        }

        /*else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("우리는 구글과 페이스 북만 지원해요");
            return null;
        }*/

        String provider = oAuth2UserInfo.getProvider(); // google
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";
        String nickname = oAuth2UserInfo.getName();

//        Member member = memberRepository.findOneWithAuthoritiesByLoginId(username).get();
        Optional<Member> membero = memberRepository.findOneWithAuthoritiesByLoginId(username);
        Member member = new Member();
        if (!membero.isPresent()) {

            System.out.println(provider + " 로그인이 최초입니다.");
            //강제 회원가입
            //회원 DB에 추가함
            //password 가 null 이기 때문에 일반적인 회원가입을 할 수가 없음
            Authority authority = Authority.builder()
                    .authorityName(role)
                    .build();

            member = Member.builder()
                    .loginId(username)
                    .nickname(nickname)
                    .authorities(Collections.singleton(authority))
                    .activated(true)
//                    .provider(provider)
//                    .providerId(providerId)
                    .build();
            memberRepository.save(member);
//            MemberDto.from(memberRepository.save(user));
        } else {
            System.out.println(provider + " 로그인을 이미 한 적이 있습니다.");
        }

        log.debug("return 타기전");

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
