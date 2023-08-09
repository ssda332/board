package yj.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.member.Member;
import yj.board.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceIntergrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원_가입() {
        //given
        Member member = new Member();
        member.setLoginId("test1");
        member.setPassword("Testtest1");

        //when
        int save = memberService.save(member);

        //then
        assertThat(save).isEqualTo(1);
    }

}
