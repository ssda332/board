package yj.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yj.board.domain.member.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
