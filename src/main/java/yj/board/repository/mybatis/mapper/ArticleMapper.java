package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import yj.board.domain.article.dto.ArticleDetailDto;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;

import java.util.ArrayList;

@Mapper
public interface ArticleMapper {

    ArrayList<ArticleDto> findAll();
    Integer selectArticleCount(String category);
    ArrayList<ArticleDto> selectArticleList(RowBounds rowBounds, String category);
    Long selectNewAtcNum();
    void insertArticle(ArticleWriteDto articleDto);
    void updateArticle(ArticleWriteDto articleDto);
    ArticleDetailDto findOne(String atcNum);
    void deleteArticle(String atcNum);
}
