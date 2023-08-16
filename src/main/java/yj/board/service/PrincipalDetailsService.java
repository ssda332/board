package yj.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yj.board.auth.PrincipalDetails;
import yj.board.domain.member.Member;
import yj.board.repository.MemberRepositoryV2;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

    private final MemberRepositoryV2 memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findOneWithAuthoritiesByLoginId(username).get();

        return new PrincipalDetails(member);
    }


}