package yj.board.domain.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INPUT_VALUE_INVALID("CM_001", "입력값이 올바르지 않습니다.", 400),
    CONFLICT_USER_ALREADY_EXISTS("CM_002", "이미 가입된 사용자가 존재합니다.", 409);

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status){
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
