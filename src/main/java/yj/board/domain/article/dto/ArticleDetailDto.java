package yj.board.domain.article.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleDetailDto {
    /*
    ATC_NUM
    ATC_TITLE
    ATC_CONTENT
    ATC_WRITER
    ATC_ACTIVATED
    CTG_ID
    ATC_REG_DATE
    ATC_UPT_DATE
    */
    private String atcNum;
    private String atcTitle;
    private String atcContent;
    private String atcWriter;
    private String memId;
    private String atcRegDate;
    private String atcUptDate;
    private String ctgId;
    private String ctgTitle;
}
