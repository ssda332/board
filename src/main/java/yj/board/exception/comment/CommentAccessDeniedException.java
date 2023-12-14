package yj.board.exception.comment;

public class CommentAccessDeniedException extends RuntimeException{
    public CommentAccessDeniedException() {
        super();
    }
    public CommentAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
    public CommentAccessDeniedException(String message) {
        super(message);
    }
    public CommentAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
