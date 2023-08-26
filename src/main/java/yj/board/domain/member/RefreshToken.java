package yj.board.domain.member;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "TB_REFRESH_TOKEN")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name="TOKEN_SEQ_GEN",
        sequenceName="TOKEN_ID_SEQ",
        initialValue=1,
        allocationSize=1
)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TOKEN_SEQ_GEN")
    @Column(name="TOKEN_ID")
    private Long id;
    @Column(name = "MEM_ID", nullable = false, unique = true)
    private Long memId;
    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

    @Builder
    public RefreshToken(Long memId, String refreshToken) {
        this.memId = memId;
        this.refreshToken = refreshToken;
    }
}
