/**
 * 댓글 생성
 */
function generateCommentHtml(comments, parentId = null) {
    let html = '';
    const payload = decodeToken("refreshToken");

    for (const comment of comments) {
        if (comment.cmtPrtNum === parentId) {
            const isCurrentUserComment = payload.memId == comment.memId;

            html += `
                <li class="list-group-item">
                    <p class="mb-1 text-muted" style="max-width: 30px; margin: 0; display: flex; align-items: center;">
                        <img class="img-profile rounded-circle" src="/img/undraw_profile.svg">
                        <span class="mr-2 text-gray-700" style="white-space: nowrap; margin: 0px 10px 0px 5px;">${comment.memNickname}</span>
                        <div class="d-flex justify-content-between" style="width: 100%;">
                            <div id="comment_${comment.cmtNum}" class="mr-2 text-gray-800 flex-grow-1" style="margin-top: 10px;">
                                ${comment.cmtContent}<br>
                            </div>
                            <div class="mr-2 text-gray-800" style="white-space: nowrap; margin-top: 10px;">
                                <button onclick="toggleReplyArea(${comment.cmtNum})" type="button" class="btn btn-secondary btn-sm">답글</button>
                                ${isCurrentUserComment ? `
                                    <button type="button" onclick="editComment(${comment.cmtNum})" class="btn btn-info btn-sm">수정</button>
                                    <button type="button" onclick="deleteComment(${comment.cmtNum})" class="btn btn-danger btn-sm">삭제</button>
                                ` : ''}
                            </div>
                        </div>
                    </p>
                    
                    <div id="reply_${comment.cmtNum}" class="input-group" style="margin: 10px 0px 10px 0px; display: none;">
                        <textarea id="replyText_${comment.cmtNum}" class="form-control"></textarea>
                        <div class="input-group-append">
                            <button onclick="submitReply({
                                                    cmtPrtNum: ${comment.cmtNum},
                                                    cmtContent: document.getElementById('replyText_${comment.cmtNum}').value,
                                                    cmtHierachy: ${comment.cmtHierachy} + 1,
                                                    atcNum: ${comment.atcNum},
                                                    memId: ''
                                            })" class="btn btn-outline-secondary" type="button">작성</button>
                        </div>
                    </div>
                    
                    ${generateCommentHtml(comments, comment.cmtNum)}
                </li>
            `;
        }
    }

    return html ? `<ul class="list-group">${html}</ul>` : '';
}

/**
 * 답글 textarea 열기
 */

// 답글 영역 토글 함수
function toggleReplyArea(commentId, cmtContent) {
    const replyArea = document.getElementById(`reply_${commentId}`);
    if (replyArea) {
        replyArea.style.display = replyArea.style.display === 'none' ? '' : 'none';
    }
}

// 답글 작성 함수
//${comment.cmtNum}, document.getElementById('replyText_${comment.cmtNum}').textContent, ${comment.cmtHierachy}, ${comment.actNum}, ${comment.memId}
//cmtNum, cmtContent, cmtHierachy, actNum, memId
function submitReply(comment) {
    // TODO: 답글 작성 로직 추가
    /*console.log(`Submit reply for comment ${cmtNum}`);
    // 작성 후 원하는 동작 수행
    toggleReplyArea(commentId);*/
    // 권한 검색
    const payload = decodeToken("refreshToken");
    comment.memId = payload.memId;

    console.log(comment);
    tokenToJson("POST", "/comment", JSON.stringify(comment), true,
        function(data, status, xhr) {
            // data : 작성후 댓글 목록 반환
            renderComment(data);
        },

        function(xhr, status, error) {
            alert("권한이 없습니다.");
            location.href = "/";
        }
    );

}

function renderComment(data) {
    let html = generateCommentHtml(data, null);
    const commentDiv = document.getElementById('commentDiv');
    commentDiv.innerHTML = html;
}

function editComment(cmtNum) {
    // id가 "comment_" + cmtNum 인 태그를 찾아서
    // 태그 안 text 그대로 유지한채 textarea 태그로 바꿔주는 코드
    // 댓글의 내용을 가져오기
    const commentText = document.getElementById(`comment_${cmtNum}`).innerText;

    // 댓글 내용을 표시하는 태그를 textarea로 교체
    const commentElement = document.getElementById(`comment_${cmtNum}`);
    const textarea = document.createElement('textarea');
    textarea.className = 'mr-2 text-gray-800 flex-grow-1';
    textarea.style.marginTop = '10px';
    textarea.value = commentText;

    // 원래의 댓글을 숨기고 textarea를 표시
    commentElement.style.display = 'none';
    commentElement.after(textarea);

    // textarea에 포커스 주기
    textarea.focus();

    // 편집 완료 시의 이벤트 처리 (예: 엔터 키를 누르면 저장)
    textarea.addEventListener('keydown', function (event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            saveEditedComment(cmtNum, textarea.value);
        }
    });
}