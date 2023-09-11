package yj.board.exception;

public class CAuthenticationEntryPointException extends RuntimeException{
    public CAuthenticationEntryPointException() {
        super();
    }
    public CAuthenticationEntryPointException(String message, Throwable cause) {
        super(message, cause);
    }
    public CAuthenticationEntryPointException(String message) {
        super(message);
    }
    public CAuthenticationEntryPointException(Throwable cause) {
        super(cause);
    }
}
