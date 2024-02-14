package yj.board.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageInfoDto {
    private int articleCount;
    private int commentCount;
    private MemberInfoDto memberInfoDto;
}
