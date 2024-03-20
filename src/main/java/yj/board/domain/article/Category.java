package yj.board.domain.article;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_ARTICLE_CTG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name="CTG_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="CTG_ID_SEQ", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)
public class Category {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CTG_SEQ_GEN")
    @Column(name="CTG_ID")
    private String ctgId;

    @Column(name="CTG_TITLE")
    private String ctgTitle;
/*
    @Column(name="CTG_HIERACHY")
    private Long ctgHierachy;*/

    @Column(name = "CTG_REG_DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime ctgRegDate; // 데이터 타입 변경

    @Column(name = "CTG_UPT_DATE")
    @LastModifiedDate
    private LocalDateTime ctgUptDate; // 데이터 타입 변경

    @Column(name="CTG_SORT")
    private Long ctgSort;

    // 셀프조인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CTG_PRT_ID")
    private Category parents;

    @OneToMany(mappedBy = "parents")
    private List<Category> child = new ArrayList<>();

}
