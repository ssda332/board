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

