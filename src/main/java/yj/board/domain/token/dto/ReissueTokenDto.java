package yj.board.domain.token.dto;

import lombok.*;
import yj.board.domain.member.dto.MemberDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReissueTokenDto {

    private String accessToken;
    private String refreshToken;
    private MemberDto memberDto;

}
