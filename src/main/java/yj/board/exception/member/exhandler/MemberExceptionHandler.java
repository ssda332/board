package yj.board.exception.member.exhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import yj.board.domain.error.ErrorCode;
import yj.board.domain.error.ErrorResponse;
import yj.board.exception.member.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MemberExceptionHandler {

    @Autowired
    private yj.board.util.JwtProperties jwtProperties;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        return buildFieldErrors(ErrorCode.INPUT_VALUE_INVALID, fieldErrors);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBindException(org.springframework.validation.BindException e) {
        final List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        return buildFieldErrors(ErrorCode.INPUT_VALUE_INVALID, fieldErrors);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handleUserAlreadyExistsException(DuplicateMemberException e) {
        return buildFieldErrors(ErrorCode.CONFLICT_USER_ALREADY_EXISTS, null);
    }

    @ExceptionHandler(LoginFailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleLoginFailException(LoginFailException e) {
        return buildFieldErrors(ErrorCode.LOGIN_FAIL, null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return buildFieldErrors(ErrorCode.USER_NOT_FOUND, null);
    }

    @ExceptionHandler(MemberUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMemberUpdateException(UserNotFoundException e) {
        return buildFieldErrors(ErrorCode.UPDATE_MEMBER_FAIL, null);
    }

    /**
     * AU_001
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     * 401
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleAuthenticationEntrypointException(CAuthenticationEntryPointException e) {
        return buildFieldErrors(ErrorCode.AUTH_ENTRY_POINT, null);
    }

    /**
     * AU_002
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     * 403
     */
    @ExceptionHandler(CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handleAccessDeniedException(CAccessDeniedException e) {
        return buildFieldErrors(ErrorCode.ACCESS_DENIED, null);
    }

    /**
     * AU_003
     * 리프레쉬 토큰이 만료되었을 때 발생 시키는 예외
     * 401
     */
    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleRefreshTokenException(RefreshTokenException e, HttpServletResponse response) {
        // "RefreshToken" 쿠키를 만들어 만료시킵니다.
        Cookie refreshTokenCookie = new Cookie(jwtProperties.getRefreshTokenHeader(), null); // 쿠키 값으로 null을 설정
        refreshTokenCookie.setMaxAge(0); // 쿠키의 만료 시간을 0으로 설정하여 즉시 만료
        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가

        return buildFieldErrors(ErrorCode.REFRESH_TOKEN_EXPIRED, null);

    }

    /**
     * AU_004
     * 액세스 토큰이 NULL일 때 발생 시키는 예외
     * 401
     */
    @ExceptionHandler(AccessTokenNullException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleAccessTokenNullException(AccessTokenNullException e) {
        return buildFieldErrors(ErrorCode.ACCESS_TOKEN_NULL, null);
    }

    private List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        return errors.parallelStream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .reason(error.getDefaultMessage())
                        .field(error.getField())
                        .value((String) error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());
    }

    private ErrorResponse buildFieldErrors(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();
    }
}
