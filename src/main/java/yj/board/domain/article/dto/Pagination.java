package yj.board.domain.article.dto;

public class Pagination {
    // PageInfo 객체를 리턴하는 static 메소드
    public static PageInfo getPageInfo(int currentPage, int listCount) {
        PageInfo pi = null;	// 페이지 정보를 담아 줄 PageInfo 참조변수 선언

        int pageLimit = 3;	// 한 페이지에 보여질 페이징바의 개수
        int boardLimit = 3;	// 한 페이지 보여질 게시글 개수

        int maxPage = (int)Math.ceil((double)listCount / boardLimit);
        int startPage = (currentPage - 1) / pageLimit * pageLimit + 1; // 현재 페이지(currentPage 참조해서 결정)
        int endPage = startPage + pageLimit - 1;

        // 마지막 페이지가 총 페이지 수보다 클 경우
        if(maxPage < endPage) {
            endPage = maxPage != 0 ? maxPage : 1;
        }

        pi = new PageInfo(currentPage, listCount, pageLimit, maxPage, startPage, endPage, boardLimit);

        return pi;
    }
}
