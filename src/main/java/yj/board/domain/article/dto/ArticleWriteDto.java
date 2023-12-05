package yj.board.domain.article.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleWriteDto {
    private String atcNum;
    private String atcTitle;
    private String atcContent;
    private String atcWriter;
    private String ctgId;
    private String[] tempFiles;
}
