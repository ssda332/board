package yj.board.repository;

import org.apache.ibatis.session.RowBounds;
import yj.board.domain.article.dto.ArticleDetailDto;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.search.Search;

import java.util.ArrayList;

public interface ArticleRepository {

    ArrayList<ArticleDto> findAll();
    ArrayList<ArticleDto> findArticlesByCategory(String seq);
    Integer selectArticleCount(String seq, Search search);
    ArrayList<ArticleDto> selectArticleList(RowBounds rowBounds, String category, Search search);
    Long selectNewAtcNum();
    void writeArticle(ArticleWriteDto articleDto);
    void updateArticle(ArticleWriteDto articleDto);
    ArticleDetailDto findArticle(String atcNum);
    void deleteArticle(String atcNum);
    void updateViews(String atcNum);
    Integer selectMemberArticleCount(Long id);

}
