package yj.board.repository;

import lombok.extern.slf4j.Slf4j;
import yj.board.domain.member.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        log.info("MemberRepository.findByLoginId init");
        Member member = /*em.find(Member.class, loginId);*/null; // find(class, pk) pk값 와야함
        log.info("MemberRepository.findByLoginId end");
        return Optional.ofNullable(member);
    }

    @Override
    public Member save(Member member) {
        /*member.setRole("test");*/
        em.persist(member);
        return member;
    }
}
