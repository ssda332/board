package yj.board.domain.token.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}