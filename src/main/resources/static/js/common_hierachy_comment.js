/**
 * 댓글 생성
 */
function generateCommentHtml(comments, parentId = null) {
    let html = '';
    const payload = decodeToken(TOKEN_NAME);

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
                            
                            <div id="edit_${comment.cmtNum}" class="input-group" style="margin: 10px 0px 10px 0px; display: none;">
                                <textarea id="editText_${comment.cmtNum}" class="form-control">${comment.cmtContent}</textarea>
                                <div class="input-group-append">
                                    <button onclick="editComment({
                                                            cmtNum: ${comment.cmtNum},
                                                            cmtContent: document.getElementById('editText_${comment.cmtNum}').value,
                                                            atcNum: ${comment.atcNum},
                                                            memId: ${comment.memId}
                                                    })" class="btn btn-outline-secondary" type="button">작성
                                    </button>
                                </div>
                            </div>
                            
                            
                            <div class="mr-2 text-gray-800" style="white-space: nowrap; margin-top: 10px;">
                                <button onclick="toggleReplyArea(${comment.cmtNum})" type="button" class="btn btn-secondary btn-sm">답글</button>
                                ${isCurrentUserComment ? `
                                    <button type="button" onclick="toggleEditComment(${comment.cmtNum})" class="btn btn-info btn-sm">수정</button>
                                    <button type="button" onclick="deleteComment({
                                                            cmtNum: ${comment.cmtNum},
                                                            cmtContent: document.getElementById('editText_${comment.cmtNum}').value,
                                                            atcNum: ${comment.atcNum},
                                                            memId: ${comment.memId}
                                                    })" class="btn btn-danger btn-sm">삭제</button>
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
                                                    memId: payload.memId
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
function submitReply(comment) {
    // TODO: 답글 작성 로직 추가
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

function toggleEditComment(commentId) {
    const editArea = document.getElementById(`edit_${commentId}`);
    const commentArea = document.getElementById(`comment_${commentId}`);
    if (editArea) {
        editArea.style.display = editArea.style.display === 'none' ? '' : 'none';
        commentArea.style.display = commentArea.style.display === 'none' ? '' : 'none';
    }
}

function editComment(comment) {
    tokenToJson("PUT", "/comment", JSON.stringify(comment), true,
        function(data, status, xhr) {
            // data : 작성후 댓글 목록 반환
            renderComment(data);
        },

        function(xhr, status, error) {
            let errorText = JSON.parse(xhr.responseText);

            if (errorText.code == "AU_002") {
                alert("권한이 없습니다");
            } else {
                alert("댓글 수정에 실패하였습니다.");
            }

        }
    );
}

function deleteComment(comment) {
    tokenToJson("DELETE", "/comment", JSON.stringify(comment), true,
        function(data, status, xhr) {
            // data : 작성후 댓글 목록 반환
            alert("삭제되었습니다.");
            renderComment(data);
        },

        function(xhr, status, error) {
            let errorText = JSON.parse(xhr.responseText);

            if (errorText.code == "AU_002") {
                alert("권한이 없습니다");
            } else {
                alert("댓글 수정에 실패하였습니다.");
            }

        }
    );
}
