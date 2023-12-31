package yj.board.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import yj.board.domain.member.Authority;
import yj.board.domain.member.Member;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        //given
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .loginId("repoTest13")
                .password(null)
                .nickname("repoTest13")
                .authorities(Collections.singleton(authority))
                .activated(true)
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();

        memberRepository.save(member);
    }


    @Test
    @DisplayName("회원 정보 DB에 저장하기")
    void saveMember() {
        //given
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yy-mm-dd");
        String format = simpleDate.format(new Date());


        Member member = Member.builder()
                .loginId("repoTest12")
                .password(null)
                .nickname("repoTest12")
                .authorities(Collections.singleton(authority))
                .activated(true)
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();

        //when
        Member savedMember = memberRepository.save(member);

//        System.out.println(savedMember.getRegDate());
        //then
        assertThat(member.getLoginId()).isSameAs(savedMember.getLoginId());
//        assertThat(member.getRegDate()).isSameAs(savedMember.getRegDate());

    }

    @Test
    @DisplayName("회원 아이디로 단건 조회하기")
    void selectMemberByLoginId() {
        //given
        String loginId = "repoTest13";

        //when
        Optional<Member> member = memberRepository.findOneWithAuthoritiesByLoginId(loginId);

        //then
        assertThat(member).isNotNull();
        assertThat(member.isPresent()).isEqualTo(true);
        assertThat(member.get().getLoginId()).isEqualTo(loginId);
    }
}
