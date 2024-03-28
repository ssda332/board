package yj.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yj.board.domain.article.Category;

import java.util.ArrayList;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.parents IS NULL AND c.ctgActivated = 0L ORDER BY c.ctgSort")
    List<Category> findTopCategories();

    @Query("SELECT c FROM Category c WHERE c.parents.ctgId = :parentId ORDER BY c.ctgSort")
    List<Category> findByParentId(@Param("parentId") Long parentId);
}
