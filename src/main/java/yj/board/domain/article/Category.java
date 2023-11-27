package yj.board.domain.article;


import lombok.*;
import java.time.LocalDateTime;

/*@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_ARTICLE_CTG")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name="CTG_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="CTG_ID_SEQ", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)*/
@Data
public class Category {

    /*@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CTG_SEQ_GEN")
    @Column(name="CTG_ID")*/
    private Long ctgId;

//    @Column(name="CTG_TITLE")
    private String ctgTitle;

//    @Column(name="CTG_HIERACHY")
    private Long ctgHierachy;

//    @Column(name = "CTG_REG_DATE", nullable = false, updatable = false)
//    @CreatedDate
    private LocalDateTime ctgRegDate; // 데이터 타입 변경

//    @Column(name = "CTG_UPT_DATE")
//    @LastModifiedDate
    private LocalDateTime ctgUptDate; // 데이터 타입 변경

//    @Column(name="CTG_SORT")
    private Long ctgSort;

    // 셀프조인
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CTG_PRT_ID")
//    private Category parents;

//    @OneToMany(mappedBy = "parents")
//    private List<Category> child = new ArrayList<>();
    private Long ctgPrtId;

    public Category() {
    }

    public Category(Long ctgId, String ctgTitle, Long ctgHierachy, LocalDateTime ctgRegDate, LocalDateTime ctgUptDate, Long ctgSort, Long ctgPrtId) {
        this.ctgId = ctgId;
        this.ctgTitle = ctgTitle;
        this.ctgHierachy = ctgHierachy;
        this.ctgRegDate = ctgRegDate;
        this.ctgUptDate = ctgUptDate;
        this.ctgSort = ctgSort;
        this.ctgPrtId = ctgPrtId;
    }
}
