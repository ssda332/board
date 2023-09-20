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
            let queryString = window.location.search;
            console.log(queryString);

            if (queryString != "") {
                // 쿼리 문자열 파싱
                let queryParams = new URLSearchParams(queryString);
                // "name" 매개변수의 값을 가져오기
                let accessToken = queryParams.get("accessToken");
                let refreshToken = queryParams.get("refreshToken");

                localStorage.setItem("Authorization", accessToken);
                localStorage.setItem("refreshToken", refreshToken);
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

        callbackSuccess(data, status, xhr);

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

function queryStringToLocalStoarge() {
    let queryString = window.location.search;
    console.log(queryString);

    // alert("처음: " + queryString);
    if (queryString != "") {
        // alert("냐냐");
        // 쿼리 문자열 파싱
        let queryParams = new URLSearchParams(queryString);
        // "name" 매개변수의 값을 가져오기
        let accessToken = queryParams.get("accessToken");
        let refreshToken = queryParams.get("refreshToken");

        localStorage.setItem("Authorization", accessToken);
        localStorage.setItem("refreshToken", refreshToken);
    }
}
