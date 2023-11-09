package yj.board.exception.member;

public class AccessTokenNullException extends RuntimeException{
    public AccessTokenNullException() {
        super();
    }
    public AccessTokenNullException(String message, Throwable cause) {
        super(message, cause);
    }
    public AccessTokenNullException(String message) {
        super(message);
    }
    public AccessTokenNullException(Throwable cause) {
        super(cause);
    }
}
