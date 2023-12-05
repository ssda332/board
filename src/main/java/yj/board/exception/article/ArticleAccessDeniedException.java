package yj.board.exception.article;

public class ArticleAccessDeniedException extends RuntimeException{
    public ArticleAccessDeniedException() {
        super();
    }
    public ArticleAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
    public ArticleAccessDeniedException(String message) {
        super(message);
    }
    public ArticleAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
