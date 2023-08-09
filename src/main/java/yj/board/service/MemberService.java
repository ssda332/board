package yj.board.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.member.Member;
import yj.board.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public int save(Member member) {
        int result = 0;
        log.info("MemberService.save init");
        Optional<Member> byLoginId = memberRepository.findByLoginId(member.getLoginId());
        log.info("MemberService.findByLoginId end");
        memberRepository.save(member);
        log.info("MemberService.save end");
        return result;
    }

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
