package yj.board.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yj.board.domain.article.Category;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.parents IS NULL ORDER BY c.ctgSort")
    List<Category> findTopCategories();

    @Query("SELECT c FROM Category c WHERE c.parents.ctgId = :parentId ORDER BY c.ctgSort")
    List<Category> findByParentId(@Param("parentId") Long parentId);
}
