package yj.board.domain.search;

public class SearchQuery {

    public String selectSearchListCount(Search search) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) FROM TB_BOARD WHERE BD_DEL_YN = 'N'");
        if (search.getSearchCondition().equals("title")) {
            query.append(" AND BD_SUBJECT LIKE '%");
            query.append(search.getSearchValue());
            query.append("%'");
        }
        if (search.getSearchCondition().equals("content")) {
            query.append(" AND BD_CONTENT LIKE '%");
            query.append(search.getSearchValue());
            query.append("%'");
        }
        if (search.getSearchCondition().equals("writer")) {
            query.append(" AND BD_REG_MEM LIKE '%");
            query.append(search.getSearchValue());
            query.append("%'");
        }
        return query.toString();
    }

    public String selectSearchList(Search search) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT BD_SEQ, BD_REG_MEM, BD_SUBJECT, TO_CHAR(BD_REG_DATE, 'YYYY-MM-DD') REG_DATE, BD_VIEW_COUNT FROM TB_BOARD WHERE BD_DEL_YN = 'N'");
        if (search.getSearchCondition().equals("title")) {
            query.append(" AND BD_SUBJECT LIKE '%");
            query.append(search.getSearchValue());
            query.append("%'");
        }
        if (search.getSearchCondition().equals("content")) {
            query.append(" AND BD_CONTENT LIKE '%");
            query.append(search.getSearchValue());
            query.append("%'");
        }
        if (search.getSearchCondition().equals("writer")) {
            query.append(" AND BD_REG_MEM LIKE '%");
            query.append(search.getSearchValue());
            query.append("%'");
        }
        query.append(" ORDER BY BD_SEQ DESC");

        return query.toString();
    }
}
