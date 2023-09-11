package yj.board.exception;

public class CAccessDeniedException extends RuntimeException{
    public CAccessDeniedException() {
        super();
    }
    public CAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
    public CAccessDeniedException(String message) {
        super(message);
    }
    public CAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
