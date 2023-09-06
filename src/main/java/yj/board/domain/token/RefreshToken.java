package yj.board.domain.token;

import lombok.*;
import yj.board.domain.member.Member;

import javax.persistence.*;

@Entity
@Table(name = "TB_REFRESH_TOKEN")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*@SequenceGenerator(
        name="TOKEN_SEQ_GEN",
        sequenceName="TOKEN_ID_SEQ",
        initialValue=1,
        allocationSize=1
)*/
public class RefreshToken {
    /*@Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TOKEN_SEQ_GEN")
    @GeneratedValue
    @Column(name="TOKEN_ID")
    private Long id;*/
    @Id
//    @Column(name = "MEM_ID", nullable = false, unique = true)
    @Column(name="MEM_ID")
    private Long memId;

    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "MEM_ID", referencedColumnName = "MEM_ID")
    private Member member;

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

/*    @Builder
    public RefreshToken(Long memId, String refreshToken) {
        this.memId = memId;
        this.refreshToken = refreshToken;
    }*/

    @Builder
    public RefreshToken(Long memId, String refreshToken, Member member) {
        this.memId = memId;
        this.refreshToken = refreshToken;
        this.member = member;
    }
}
