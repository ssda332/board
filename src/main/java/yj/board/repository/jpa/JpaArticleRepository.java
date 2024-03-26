package yj.board.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import yj.board.domain.article.Article;

public interface JpaArticleRepository extends JpaRepository<Article, Long> {

}
