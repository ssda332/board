package yj.board.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
//@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_MEM")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name="ID_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="MEM_ID_SEQ", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "MEM_ID")
public class Member {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ_GEN")
    @Column(name="MEM_ID")
    private Long id;

    //    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "ID는 숫자와 영문 대,소문자 조합으로 4자 이상 12자 이하입니다.")
    @Column(name="MEM_USERID")
    private String loginId;

    //    @Pattern(regexp = "^[A-Za-z0-9]{8,12}$", message = "비밀번호는 영문 대,소문자와 숫자 8자 이상 12자 이하입니다.")
    @Column(name="MEM_PASSWORD")
    private String password;

    @Column(name = "MEM_NICKNAME")
    private String nickname;

    @Column(name = "MEM_ACTIVATED")
    private boolean activated;

    @Column(name = "MEM_REG_DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime regDate; // 데이터 타입 변경

    @Column(name = "MEM_UPT_DATE")
    @LastModifiedDate
    private LocalDateTime uptDate; // 데이터 타입 변경

    @ManyToMany
    @JoinTable(
            name = "TB_MEM_AUTH",
            joinColumns = {@JoinColumn(name = "MEM_ID", referencedColumnName = "MEM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTH_NAME", referencedColumnName = "AUTH_NAME")})
    private Set<Authority> authorities;

}

