package yj.board.exception.article.exhandler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yj.board.domain.error.ErrorCode;
import yj.board.domain.error.ErrorResponse;
import yj.board.exception.article.ArticleAccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ArticleExceptionHandler {

    /**
     * AU_002
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     * 403
     */
    @ExceptionHandler(ArticleAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handleArticleAccessDeniedException(ArticleAccessDeniedException e) {
//        final List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        return buildFieldErrors(ErrorCode.ACCESS_DENIED, null);
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
