package yj.board.domain.article.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleWriteDto {
    @NotNull
    private String atcNum;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{4,12}$", message = "제목을 4자 이상 12자 이하로 작성해주세요.")
    private String atcTitle;
    @NotNull
    @Size(max = 4000, message = "내용 길이 초과.")
    private String atcContent;
    @NotNull
    private String atcWriter;
    @NotNull
    private String ctgId;
    private String[] tempFiles;
}
