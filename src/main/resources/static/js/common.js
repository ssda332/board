// 인증 과정 거쳐 뷰 이동
function ajaxView(type, url, data, isToken, callbackSuccess, callbackError) {
    $.ajax({
        type: type,
        url: url,
        contentType: 'application/json; charset=utf-8',
        data: data,
        beforeSend: function (xhr) {
            if (isToken) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            }
        },
    }).done(function(data, status, xhr) {
        callbackSuccess(data, status, xhr);
    }).fail(function(xhr, status, error) {
        callbackError(xhr, status, error);
    });
}

// json 반환
function ajaxJson(type, url, data, isToken, callbackSuccess, callbackError) {
    $.ajax({
        type: type,
        url: url,
        contentType: 'application/json; charset=utf-8',
        data: data,
        beforeSend: function (xhr) {
            if (isToken) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            }
        },
    }).done(function(data, status, xhr) {
        callbackSuccess(data, status, xhr);
    }).fail(function(xhr, status, error) {
        callbackError(xhr, status, error);
    });
}

// 토큰 넘겨서 권한검증
function tokenToJson(type, url, data, isToken, callbackSuccess, callbackError) {
    $.ajax({
        type: type,
        url: url,
        contentType: 'application/json; charset=utf-8',
        data: data,
        beforeSend: function (xhr) {
            // OAuth2 로그인시 쿼리 문자열 값 있음(accessToken, refreshToken)
            let queryString = window.location.search;
            // console.log(queryString);

            if (queryString != "") {
                // 쿼리 문자열 파싱
                let queryParams = new URLSearchParams(queryString);
                // "name" 매개변수의 값을 가져오기

                let accessToken = queryParams.get("accessToken");
                let refreshToken = queryParams.get("refreshToken");

                if (accessToken != null) localStorage.setItem("Authorization", accessToken);
                if (refreshToken != null) localStorage.setItem("refreshToken", refreshToken);
            }


            if (isToken) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            }


        },
    }).done(function(data, status, xhr) {
        callbackSuccess(data, status, xhr);
    }).fail(function(xhr, status, error) {
        let jsonResponse = JSON.parse(xhr.responseText);
        let accessToken = localStorage.getItem("Authorization");

        if (jsonResponse.code == "AU_001" && accessToken != null) {
            // 401 시큐리티 인증 실패 에러
            reissue(callbackSuccess, callbackError);
        } else {
            callbackError(xhr, status, error);
        }
    });
}

function reissue(callbackSuccess, callbackError) {
    const accessToken = localStorage.getItem("Authorization");
    const refreshToken = localStorage.getItem("refreshToken");

    const data = {
        accessToken: accessToken,
        refreshToken: refreshToken
    };

    $.ajax({
        type: "PUT",
        url: "/token",
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type","application/json");
        },
    }).done(function(data, status, xhr) {
        const accessToken = data.accessToken;
        const refreshToken = data.refreshToken;

        localStorage.setItem("Authorization", accessToken);
        localStorage.setItem("refreshToken", refreshToken);

        callbackSuccess(data.memberDto, status, xhr);

    }).fail(function(xhr, status, error) {
        // alert("anonymous");
        let jsonResponse = JSON.parse(xhr.responseText);
        // console.log(jsonResponse);
        // console.log(jsonResponse.status);

        if (jsonResponse.code == "AU_003") {
            // 401 리프레쉬 토큰 에러
            alert("다시 로그인 해주세요");
            localStorage.clear();
            window.location.href="/";
        } else {
            callbackError(xhr, status, error);
        }
    });
}

function logout() {
    localStorage.clear();
    window.location.href="/";
}

function isTokenValid() {
    const accessToken = localStorage.getItem("Authorization"); // accessToken 가져오기
    if (!accessToken) {
        // accessToken이 없으면 유효하지 않다고 처리
        return false;
    }

    // accessToken을 디코딩하여 만료 시간(expiration time) 가져오기
    const tokenParts = accessToken.split(".");
    if (tokenParts.length !== 3) {
        // 올바른 JWT 형식이 아니면 유효하지 않다고 처리
        return false;
    }

    const payload = JSON.parse(atob(tokenParts[1]));
    const exp = payload.exp;

    if (!exp || typeof exp !== "number") {
        // exp 값이 없거나 숫자가 아니면 유효하지 않다고 처리
        return false;
    }

    // 현재 시간을 가져오기
    const currentTime = Math.floor(Date.now() / 1000); // 초 단위로 변환

    // 만료 시간(expiration time)에서 현재 시간을 빼서 남은 시간 계산
    const timeUntilExpiration = exp - currentTime;

    // 만료 시간이 3분(180초) 이하로 남았으면 false 반환, 그렇지 않으면 true 반환
    return timeUntilExpiration > 180;
}

// 토큰 넘겨서 권한검증
function tokenRequest(type, url, data, isToken, callbackSuccess, callbackError) {
    $.ajax({
        type: type,
        url: url,
        contentType: 'application/json; charset=utf-8',
        data: data,
        beforeSend: function (xhr) {
            // OAuth2 로그인시 쿼리 문자열 값 있음(accessToken, refreshToken)
            let queryString = window.location.search;
            // console.log(queryString);

            if (queryString != "") {
                // 쿼리 문자열 파싱
                let queryParams = new URLSearchParams(queryString);
                // "name" 매개변수의 값을 가져오기

                let accessToken = queryParams.get("accessToken");
                let refreshToken = queryParams.get("refreshToken");

                if (accessToken != null) localStorage.setItem("Authorization", accessToken);
                if (refreshToken != null) localStorage.setItem("refreshToken", refreshToken);
            }


            if (isToken) {
                xhr.setRequestHeader("Content-type","application/json");
                xhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            }


        },
    }).done(function(data, status, xhr) {
        callbackSuccess(data, status, xhr);
    }).fail(function(xhr, status, error) {
        let jsonResponse = JSON.parse(xhr.responseText);
        let accessToken = localStorage.getItem("Authorization");

        if (jsonResponse.code == "AU_001" && accessToken != null) {
            // 401 시큐리티 인증 실패 에러
            reissue(callbackSuccess, callbackError);
        } else {
            callbackError(xhr, status, error);
        }
    });
}

function hasRoleAdmin(authorities) {
    for (let i = 0; i < authorities.length; i++) {
        if (authorities[i].authorityName === 'ROLE_ADMIN') {
            return true;
        }
    }
    return false;
}

function addAdminMenu() {
    //user-information
    // 새로운 <a> 태그 생성
    const newLink = document.createElement('a');
    newLink.className = 'dropdown-item';
    newLink.id = 'category-edit';
    newLink.href = '/category';

    // <i> 태그 생성 및 속성 설정
    const newIcon = document.createElement('i');
    newIcon.className = 'fas fa-user fa-sm fa-fw mr-2 text-gray-400';

    // 텍스트 추가
    newLink.appendChild(newIcon);
    newLink.appendChild(document.createTextNode('Edit category'));

    // user-information에 새로운 <a> 태그 추가
    const userInformation = document.getElementById('user-information');
    userInformation.appendChild(newLink);

}