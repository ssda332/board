package yj.board.domain.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INPUT_VALUE_INVALID("CM_001", "입력값이 올바르지 않습니다.", 400),
    CONFLICT_USER_ALREADY_EXISTS("CM_002", "이미 가입된 사용자가 존재합니다.", 409),
    USER_NOT_FOUND("CM_003", "사용자를 찾을 수 없습니다.", 404),
    LOGIN_FAIL("CM_004", "로그인에 실패하였습니다.", 401),

    AUTH_ENTRY_POINT("AU_001", "인증에 실패하였습니다.", 401),
    ACCESS_DENIED("AU_002", "권한이 없습니다.", 403),
    REFRESH_TOKEN_EXPIRED("AU_003", "리프레쉬 토큰이 만료되었습니다.", 401),
    ACCESS_TOKEN_NULL("AU_004", "액세스 토큰이 없습니다.", 401);


    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status){
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
