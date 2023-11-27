package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.article.dto.ArticleDto;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.article.dto.PageInfo;
import yj.board.repository.ArticleRepository;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Qualifier("myBatisArticleRepository")
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ArrayList<ArticleDto> findAllArticle() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ArrayList<ArticleDto> findArticlesByCategory(String seq) {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Integer selectArticleCount(String seq) {
        return articleRepository.selectArticleCount(seq);
    }

    public ArrayList<ArticleDto> getArticleList(PageInfo pi, String Category) {
        // 몇 개의 게시글을 건너 뛸 것인지
        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

        return articleRepository.selectArticleList(rowBounds, Category);
    }

    @Transactional
    public void writeArticle(ArticleWriteDto articleDto) {
        String newAtcId = String.valueOf(articleRepository.selectNewAtcNum());
        articleDto.setAtcNum(newAtcId);

        articleRepository.writeArticle(articleDto);

    }

    @Transactional(readOnly = true)
    public ArticleDto findArticle(String atcNum) {
        return articleRepository.findArticle(atcNum);
    }
}
