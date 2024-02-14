package yj.board.repository.mybatis;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import yj.board.domain.article.dto.ArticleDetailDto;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.search.Search;
import yj.board.repository.ArticleRepository;
import yj.board.repository.mybatis.mapper.ArticleMapper;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class MyBatisArticleRepository implements ArticleRepository {

    private final ArticleMapper articleMapper;

    @Override
    public ArrayList<ArticleDto> findAll() {
        return articleMapper.findAll();
    }

    @Override
    public ArrayList<ArticleDto> findArticlesByCategory(String seq) {
        return null;
    }

    @Override
    public Integer selectArticleCount(String seq, Search search) {
        return articleMapper.selectArticleCount(seq, search.getSearchCondition(), search.getSearchValue());
    }

    @Override
    public ArrayList<ArticleDto> selectArticleList(RowBounds rowBounds, String category, Search search) {

        return articleMapper.selectArticleList(rowBounds, category, search.getSearchCondition(), search.getSearchValue());
    }

    @Override
    public Long selectNewAtcNum() {
        return articleMapper.selectNewAtcNum();
    }

    @Override
    public void writeArticle(ArticleWriteDto articleDto) {
        articleMapper.insertArticle(articleDto);
    }

    @Override
    public void updateArticle(ArticleWriteDto articleDto) {
        articleMapper.updateArticle(articleDto);
    }

    @Override
    public ArticleDetailDto findArticle(String atcNum) {
        return articleMapper.findOne(atcNum);
    }

    @Override
    public void deleteArticle(String atcNum) {
        articleMapper.deleteArticle(atcNum);
    }

    @Override
    public void updateViews(String atcNum) {
        articleMapper.updateViews(atcNum);
    }

    @Override
    public Integer selectMemberArticleCount(Long id) {
        return articleMapper.selectMemberArticleCount(id);
    }
}
