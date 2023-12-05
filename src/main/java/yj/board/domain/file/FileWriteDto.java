package yj.board.domain.file;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileWriteDto {
    private String imgUrl;
    private String atcNum;
}
