package yj.board.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yj.board.domain.member.Member;

import java.util.List;
import java.util.Optional;

//@Repository
public interface MemberRepository {

    List<Member> findAll();
    Optional<Member> findByLoginId(String loginId);

    Member save(@Param("member") Member member);


}
