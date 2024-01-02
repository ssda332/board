package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.repository.query.Param;
import yj.board.domain.article.dto.ArticleDetailDto;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.search.Search;

import java.util.ArrayList;

@Mapper
public interface ArticleMapper {

    ArrayList<ArticleDto> findAll();
    Integer selectArticleCount(String category, String searchCondition, String searchValue);
    ArrayList<ArticleDto> selectArticleList(RowBounds rowBounds, String category, String searchCondition, String searchValue);
    Long selectNewAtcNum();
    void insertArticle(ArticleWriteDto articleDto);
    void updateArticle(ArticleWriteDto articleDto);
    ArticleDetailDto findOne(String atcNum);
    void deleteArticle(String atcNum);
    void updateViews(String atcNum);
}
