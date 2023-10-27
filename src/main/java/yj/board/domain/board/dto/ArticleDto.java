package yj.board.domain.board.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleDto {
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
    private long atcNun;
    private String atcTitle;
    private String atcContent;
    private String atcWriter;
    private String atcRegDate;
    private String atcUptDate;
}
