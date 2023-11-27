/**
 *  레이아웃 카테고리 렌더링
 */
function category_hireachy(categoryList) {
    // JSON 데이터 반복
    let html = "";
    categoryList.forEach(function(category) {

        // ctgHierachy가 1인 경우
        if (category.ctgHierachy === 1) {
            html += '<hr class="sidebar-divider">';
            html += '<div class="sidebar-heading">' + category.ctgTitle + '</div>';
        }

        // ctgHierachy가 2인 경우
        if (category.ctgHierachy === 2) {
            var childCategories = categoryList.filter(function(childCategory) {
                return childCategory.ctgPrtId === category.ctgId;
            });

            if (childCategories.length > 0) {
                html += '<li class="nav-item">';
                html += '<a class="nav-link collapsed" href="/' + category.ctgId + '" data-toggle="collapse" data-target="#collapsePages' + category.ctgId + '"';
                html += ' aria-expanded="true" aria-controls="collapsePages' + category.ctgId + '">';
                html += '<i class="fas fa-fw fa-folder"></i>';
                html += '<span>' + category.ctgTitle + '</span>';
                html += '</a>';

                html += '<div id="collapsePages' + category.ctgId + '" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">';
                html += '<div class="bg-white py-2 collapse-inner rounded">';

                childCategories.forEach(function(childCategory) {
                    html += '<a class="collapse-item" href="/article?category=' + childCategory.ctgId + '">' + childCategory.ctgTitle + '</a>';
                });

                html += '</div></div></li>';
            } else {
                html += '<li class="nav-item">';
                html += '<a class="nav-link" href="/article?category=' + category.ctgId + '">';
                html += '<i class="fas fa-fw fa-table"></i>';
                html += '<span>' + category.ctgTitle + '</span></a></li>';
            }
        }

    });

    return html;
}

/**
 *  카테고리 편집기 렌더링 
 */
function renderEditCategory(data, ulElement) {
    ulElement.innerHTML = ''; // ul 비우기

    // JSON 데이터를 순회하며 li 요소를 동적으로 생성 및 추가
    data.forEach((item, index) => {

        const liElement = createCategoryListItem(item.ctgTitle, item.ctgPrtTitle, item.ctgId, item.ctgPrtId, "", item.ctgSort, item.ctgHierachy);

        if (item.ctgPrtId !== null) {
            // ctgPrtId가 null이 아닌 경우 해당 부모 div 아래에 추가
            const wrapperDiv = ulElement.querySelector(`div#wrapper_${item.ctgPrtId}`);
            wrapperDiv.appendChild(liElement);
        } else {
            // ctgPrtId가 null인 경우 ul에 추가
            ulElement.appendChild(liElement);
        }

    });
}

// 카테고리 편집 -> 새로운 카테고리 요소를 생성하는 함수
function createCategoryListItem(ctgTitle, ctgPrtTitle, ctgId, ctgPrtId, status, sort, ctgHierachy) {
    const divWrapper = document.createElement("div"); // li 요소를 감실 div 엘리먼트
    divWrapper.className = "category-wrapper";
    divWrapper.id = `wrapper_${ctgId}`; // ul 내 li 요소의 개수를 세어 순서를 매김

    const liElement = document.createElement("li");
    liElement.className = "list-group-item d-flex justify-content-between align-items-center";
    liElement.id = `category_li_${ulElement.childElementCount + 1}`; // ul 내 li 요소의 개수를 세어 순서를 매김

    const divContainer = document.createElement("div");
    divContainer.className = "action-buttons d-flex align-items-center";

    // ctgId
    const ctgIdDiv = document.createElement("div");
    ctgIdDiv.id = `ctgId_${ulElement.childElementCount + 1}`; // 중복되지 않는 ID 설정
    ctgIdDiv.style.display = "none";
    ctgIdDiv.textContent = ctgId; // 새로운 li의 ctgId 초기값

    // ctgPrtId
    const ctgPrtIdDiv = document.createElement("div");
    ctgPrtIdDiv.id = `ctgPrtId_${ulElement.childElementCount + 1}`; // 중복되지 않는 ID 설정
    ctgPrtIdDiv.style.display = "none";
    ctgPrtIdDiv.textContent = ctgPrtId; // 새로운 li의 ctgPrtId 초기값

    // status
    const statusDiv = document.createElement("div");
    statusDiv.id = `status_${ulElement.childElementCount + 1}`; // 중복되지 않는 ID 설정
    statusDiv.style.display = "none";
    statusDiv.textContent = status; // li 상태 표시 (변경 : u, 추가 : c, 삭제 : d)

    // status
    const sortDiv = document.createElement("div");
    sortDiv.id = `sort_${ulElement.childElementCount + 1}`; // 중복되지 않는 ID 설정
    sortDiv.style.display = "none";
    sortDiv.textContent = sort;

    // status
    const hierachyDiv = document.createElement("div");
    hierachyDiv.id = `hierachy_${ulElement.childElementCount + 1}`; // 중복되지 않는 ID 설정
    hierachyDiv.style.display = "none";
    hierachyDiv.textContent = ctgHierachy;

    // ctgPrtTitle
    if (ctgPrtTitle) {
        const ctgPrtTitleBadge = document.createElement("div");
        ctgPrtTitleBadge.className = "badge bg-primary mr-2 text-white";
        ctgPrtTitleBadge.textContent = ctgPrtTitle;

        // Calculate and add margin based on ctgHierachy
        if (ctgHierachy >= 2) {
            const marginPx = ctgHierachy * 20 - 20; // Adjust the multiplier as needed
            ctgPrtTitleBadge.style.marginLeft = `${marginPx}px`;
        }

        divContainer.appendChild(ctgPrtTitleBadge);
    }

    // ctgTitle
    const ctgTitleDiv = document.createElement("div");
    ctgTitleDiv.id = `ctgTitle_${ulElement.childElementCount + 1}`; // 중복되지 않는 ID 설정
    ctgTitleDiv.textContent = ctgTitle;

    const actionButtonsDiv = document.createElement("div");
    actionButtonsDiv.className = "action-buttons";

    // "추가" 버튼 (계층이 3일때는 제거)
    if (ctgHierachy != 3) {
        const addButton = document.createElement("button");
        addButton.type = "button";
        addButton.className = "btn btn-outline-dark mx-2";
        addButton.textContent = "추가";

        // "추가" 버튼에 클릭 이벤트 리스너 추가
        addButton.addEventListener("click", function () {
            // 알림창을 사용자에게 표시하여 카테고리명 입력 받음
            const newCategoryTitle = prompt("새로운 카테고리명을 입력하세요:", "기본값");

            // 사용자가 취소 버튼을 누르지 않았고, 카테고리명이 비어 있지 않다면
            if (newCategoryTitle !== null && newCategoryTitle.trim() !== "") {
                // 이벤트 핸들러 함수에서 현재 "추가" 버튼이 속한 <li> 요소를 찾습니다.
                const liElement = addButton.closest("li");
                const divElement = liElement.closest("div");

                // liElement를 기반으로 객체를 만듭니다.
                const categoryObject = {
                    ctgId: liElement.querySelector("[id^=ctgId_]").textContent,
                    ctgPrtId: liElement.querySelector("[id^=ctgPrtId_]").textContent,
                    ctgPrtTitle : liElement.querySelector("[id^=ctgTitle_]").textContent,
                    childDivCount : divElement.children.length
                };

                const newSubCategory = createCategoryListItem(newCategoryTitle, categoryObject.ctgPrtTitle, countNewCtgId(), categoryObject.ctgId, "c", categoryObject.childDivCount, ctgHierachy + 1);
                divElement.appendChild(newSubCategory);
            }
        });

        actionButtonsDiv.appendChild(addButton);

    }

    // "삭제" 버튼
    const deleteButton = document.createElement("button");
    deleteButton.type = "button";
    deleteButton.className = "btn btn-outline-danger mx-2";
    deleteButton.textContent = "삭제";
    deleteButton.addEventListener("click", function () {
        // "c"이면 해당 li 컴포넌트 삭제
        let liElement = deleteButton.closest("li");
        let statusDiv = liElement.querySelector("[id^=status_]");
        let status = liElement.querySelector("[id^=status_]").textContent;
        const ctgId = liElement.querySelector("[id^=ctgId_]").textContent;

        // ul 안의 li 컴포넌트를 반복
        const liElements = ulElement.querySelectorAll("li");
        let canDelete = true; // 삭제 가능 여부

        for (const otherLiElement of liElements) {
            if (otherLiElement !== liElement) { // 현재 li 컴포넌트는 제외
                const ctgPrtId = otherLiElement.querySelector("[id^=ctgPrtId_]").textContent;

                if (ctgPrtId === ctgId) {
                    // 삭제를 막고 알림을 표시
                    alert("다른 카테고리가 해당 카테고리에 속해 있어 삭제할 수 없습니다.");
                    canDelete = false;
                    break;
                }
            }
        }

        if (!canDelete) return;

        if (status === "c") {
            liElement.remove();
        } else if (status === "u"){
            alert("변경중인 카테고리는 삭제할 수 없습니다.");
        } else {
            deleteButton.classList.add("active");
            statusDiv.textContent = "d";
        }
    });


    // 요소들을 구조에 추가
    divContainer.appendChild(ctgIdDiv);
    divContainer.appendChild(ctgPrtIdDiv);
    divContainer.appendChild(statusDiv);
    divContainer.appendChild(sortDiv);
    divContainer.appendChild(hierachyDiv);

    divContainer.appendChild(ctgTitleDiv);

    // 추가 버튼 추가는 위에있음
    actionButtonsDiv.appendChild(deleteButton);

    divWrapper.appendChild(liElement); // divWrapper를 liElement에 추가

    liElement.appendChild(divContainer);
    liElement.appendChild(actionButtonsDiv);

    return divWrapper;
}

function countNewCtgId() {
    // ul 안의 li 컴포넌트를 가져옵니다
    const liElements = ulElement.querySelectorAll("li");

    // "new_"로 시작하는 ctgId를 가진 li 컴포넌트 개수를 세기 위한 변수
    let count = 0;

    // li 컴포넌트를 반복하면서 "new_"로 시작하는 ctgId를 찾습니다
    for (const liElement of liElements) {
        const ctgId = liElement.querySelector("[id^=ctgId_]").textContent;

        if (ctgId.startsWith("new_")) {
            count++;
        }
    }

    // "new_"와 개수를 결합하여 반환합니다
    return "new_" + count;
}

function changeCategory(type, moveCategory, targetCategory) {
    const ulElement = currentLiElement.closest("ul");

    // 순서 변경할 카테고리 요소
    const ctgId = currentLiElement.querySelector("[id^=ctgId_]").textContent;

    // 현재 위치에서 제거
    currentLiElement.remove();

    // 새 위치에 삽입
    if (newPosition === "before") {
        ulElement.insertBefore(currentLiElement, newParentLiElement);
    } else {
        ulElement.insertBefore(currentLiElement, newParentLiElement.nextSibling);
    }

    // 이제 ctgId를 사용하여 서버 또는 데이터 모델에서 순서를 업데이트하는 작업 수행


}

function ctgMoveUp(clickedLi) {
    // 위 버튼을 클릭했을 때 실행할 동작을 정의
    // 여기에 위로 이동하는 동작을 구현합니다.
    if (clickedLi) {
        // clickedLi의 ctgSort와 ctgHierachy 값 가져오기
        const clickDivElement = clickedLi.closest('div');

        const clickedSortDiv = clickedLi.querySelector("[id^=sort_]");
        const clickedStatusDiv = clickedLi.querySelector("[id^=status_]");
        const clickedCtgSort = Number(clickedLi.querySelector("[id^=sort_]").textContent);
        const clickedCtgHierachy = Number(clickedLi.querySelector("[id^=hierachy_]").textContent);
        const clickedCtgPrtId = clickedLi.querySelector("[id^=ctgPrtId_]").textContent;

        // 상위에 있는 li 요소 찾기
        let prevLiDiv = clickDivElement.previousElementSibling;

        while (prevLiDiv && prevLiDiv.tagName === 'DIV') {
            // prevLi의 ctgSort와 ctgHierachy 값 가져오기
            const prevLi = prevLiDiv.querySelector("li");

            const prevSortDiv = prevLi.querySelector("[id^=sort_]");
            const prevStatusDiv = prevLi.querySelector("[id^=status_]");
            const prevCtgSort = Number(prevLi.querySelector("[id^=sort_]").textContent);
            const prevCtgHierachy = Number(prevLi.querySelector("[id^=hierachy_]").textContent);
            const prevCtgPrtId = prevLi.querySelector("[id^=ctgPrtId_]").textContent;

            if (prevCtgPrtId === clickedCtgPrtId && prevCtgHierachy === clickedCtgHierachy && prevCtgSort < clickedCtgSort) {
                // 위치 변경 (DOM 요소 교환)
                clickDivElement.parentNode.insertBefore(clickDivElement, prevLiDiv);

                // Sort 값을 교환
                clickedSortDiv.textContent = prevCtgSort;
                prevSortDiv.textContent = clickedCtgSort;
                if (clickedStatusDiv.textContent != "c" && clickedStatusDiv.textContent != "d") {
                    clickedStatusDiv.textContent = "u";
                }
                if (prevStatusDiv.textContent != "c" && prevStatusDiv.textContent != "d") {
                    prevStatusDiv.textContent = "u";
                }

                break;
            }

            prevLiDiv = prevLiDiv.previousElementSibling;
        }
    }
}

function ctgMoveDown(clickedLi) {
    // 아래 버튼을 클릭했을 때 실행할 동작을 정의
    // 여기에 아래로 이동하는 동작을 구현합니다.
    if (clickedLi) {
        // clickedLi의 ctgSort와 ctgHierachy 값 가져오기
        const clickDivElement = clickedLi.closest('div');

        const clickedSortDiv = clickedLi.querySelector("[id^=sort_]");
        const clickedStatusDiv = clickedLi.querySelector("[id^=status_]");
        const clickedCtgSort = Number(clickedLi.querySelector("[id^=sort_]").textContent);
        const clickedCtgHierachy = Number(clickedLi.querySelector("[id^=hierachy_]").textContent);
        const clickedCtgPrtId = clickedLi.querySelector("[id^=ctgPrtId_]").textContent;

        // 상위에 있는 li 요소 찾기
        let nextLiDiv = clickDivElement.nextElementSibling;

        while (nextLiDiv) {
            // prevLi의 ctgSort와 ctgHierachy 값 가져오기
            const nextLi = nextLiDiv.querySelector("li");

            const nextSortDiv = nextLi.querySelector("[id^=sort_]");
            const nextStatusDiv = nextLi.querySelector("[id^=status_]");
            const nextCtgSort = Number(nextLi.querySelector("[id^=sort_]").textContent);
            const nextCtgHierachy = Number(nextLi.querySelector("[id^=hierachy_]").textContent);
            const nextCtgPrtId = nextLi.querySelector("[id^=ctgPrtId_]").textContent;

            if (nextCtgPrtId === clickedCtgPrtId && nextCtgHierachy === clickedCtgHierachy && nextCtgSort > clickedCtgSort) {
                // 위치 변경 (DOM 요소 교환)
                insertAfter(clickDivElement, nextLiDiv);

                // Sort 값을 교환
                clickedSortDiv.textContent = nextCtgSort;
                nextSortDiv.textContent = clickedCtgSort;
                if (clickedStatusDiv.textContent != "c" && clickedStatusDiv.textContent != "d") {
                    clickedStatusDiv.textContent = "u";
                }
                if (nextStatusDiv.textContent != "c" && nextStatusDiv.textContent != "d") {
                    nextStatusDiv.textContent = "u";
                }

                break;
            }

            nextLiDiv = nextLiDiv.nextElementSibling;
        }
    }
}

function insertAfter(newNode, referenceNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}

// 제목 입력 / 수정
function writeTitle(ulElement) {
    const activeLiElement = ulElement.querySelector(".list-group-item.active");
    if (activeLiElement) { // 선택된 li가 있으면

        // "status_"로 시작하는 컴포넌트의 textContent 첫 글자가 "d"가 아닌 경우
        const statusElement = activeLiElement.querySelector("[id^=status_]");
        if (statusElement && !statusElement.textContent.startsWith("d") && !statusElement.textContent.startsWith("c")) {
            statusElement.textContent = "u";
        }

        const ctgTitle = inputTitleElement.value;
        activeLiElement.querySelector("[id^=ctgTitle_]").textContent = ctgTitle;

        // ctgId 및 ctgPrtId 가져오기
        const ctgId = activeLiElement.querySelector("[id^=ctgId_]").textContent;
        const ctgPrtId = activeLiElement.querySelector("[id^=ctgPrtId_]").textContent;

        // 다른 li 요소들 중에서 ctgPrtId가 일치하는 요소의 badge 클래스 업데이트
        ulElement.querySelectorAll("li").forEach(function (liElement) {
            const liCtgPrtId = liElement.querySelector("[id^=ctgPrtId_]").textContent;
            if (liCtgPrtId === ctgId) {
                liElement.querySelector(".badge.bg-primary.mr-2.text-white").textContent = ctgTitle;
            }
        });
    }
}

function selectLi(event, ulElement) {
    const clickedElement = event.target;
    if (!clickedElement.contains(clickedElement.querySelector("button"))) {
        return;
    }

    // 클릭된 요소의 부모 노드(<li>)를 찾아서 해당 <li> 요소에 대해 처리
    clickedLi = clickedElement.closest("li");
    console.log(clickedLi);

    if (clickedLi) {
        // 다른 모든 <li>에서 활성화 스타일 제거
        const allLiElements = ulElement.getElementsByClassName("list-group-item");
        for (const element of allLiElements) {
            element.classList.remove("active");
        }
        clickedLi.classList.add("active");

        // 클릭한 <li>의 ctgTitle 값을 input 요소에 설정
        inputTitleElement.value = clickedLi.querySelector("[id^=ctgTitle_]").textContent;
    }
}

// 새로운 최상위 카테고리 생성
function insertNewCategory(ulElement) {
    // 사용자가 원하는 초기 ctgTitle 값을 입력하도록 프롬프트를 표시
    const ctgTitle = prompt("새로운 카테고리명을 입력하세요:", "기본값");

    if (ctgTitle !== null) {
        // 새로운 li 요소 생성 (두번째 인자로 사용자가 입력한 ctgTitle 값을 전달)
        // const size = ulElement.children.length;
        // ulElement의 가장 마지막 자식 li 요소 선택
        const lastLi = ulElement.lastElementChild;
        // lastLi 요소에서 id가 "sort_"로 시작하는 요소 선택
        const sortElement = lastLi.querySelector("[id^=sort_]");
        // sortElement 요소의 텍스트 가져오기
        const sortText = parseInt(sortElement.textContent, 10) + 1;
        const newLi = createCategoryListItem(ctgTitle, null, countNewCtgId(), null, "c", sortText, 1);

        // ul에 새로운 li 요소 추가
        ulElement.appendChild(newLi);
    }
}

/**
 * 게시글 작성 가능한 카테고리 렌더링
 */
function category_canWrite(categoryList) {
    // JSON 데이터 반복
    let html = "";
    categoryList.forEach(function(category) {
        html += '<option value="' + category.ctgId + '">' + category.ctgTitle + '</option>';
    });

    return html;
}