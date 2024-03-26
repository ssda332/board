package yj.board.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import yj.board.domain.member.Member;

import javax.persistence.*;

@Entity
@Table(name = "TB_ARTICLE")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name="ATC_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="ATC_ID_SEQ", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)
public class Article {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ATC_SEQ_GEN")
    @Column(name="ATC_NUM")
    private String id;

    @Column(name = "ATC_TITLE")
    private String title;

    @Column(name = "ATC_CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ATC_WRITER", referencedColumnName = "MEM_ID")
    private Member member;

    @Column(name = "ATC_REG_DATE", nullable = true, updatable = false)
    @CreatedDate
    private String regDate;

    @Column(name = "ATC_UPT_DATE")
    @LastModifiedDate
    private String uptDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CTG_ID")
    private Category category;

    @Column(name = "ATC_VIEWS")
    private Integer views;
}
