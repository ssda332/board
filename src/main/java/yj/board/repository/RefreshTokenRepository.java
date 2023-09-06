package yj.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yj.board.domain.token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findById(Long memId);
}
