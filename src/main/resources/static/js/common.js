const ajaxCall = {
    mainCall:function(url, type, dataType, token, successCallBack){
        $.ajax({
            url:url,
            type:type,
            dataType:dataType,
            beforeSend:function(xhr){
                //csrf 토큰 추가시 활성화 필요
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success:function(response){
                location.href = "/";
                console.log(response);
                // alert("success");
                // if(!cmmnUtil.isEmpty(successCallBack)){
                //     alert("success2");
                //     successCallBack(response);
                // }
            },
            error:function(data) {
                console.log(data);
                alert("fail");
            }
        });
    }


}

// - 넘겨받은 정보 체크 함수
const cmmnUtil = {
    isEmpty:function(value){
        if(value=="" || value==null || value==undefined){
            return true;
        }else{
            return false;
        }
    }
}

function submitWithTokenAndRedirect(url, method, token, data) {
    // Create a hidden form element
    var form = document.createElement('form');
    form.setAttribute('method', method);
    form.setAttribute('action', url);

    // Add token as a hidden input field
    var tokenInput = document.createElement('input');
    tokenInput.setAttribute('type', 'hidden');
    tokenInput.setAttribute('name', 'Authorization');
    tokenInput.setAttribute('value', 'Bearer ' + token);
    form.appendChild(tokenInput);

    // Add other data as hidden input fields if needed
    if (data) {
        for (var key in data) {
            var input = document.createElement('input');
            input.setAttribute('type', 'hidden');
            input.setAttribute('name', key);
            input.setAttribute('value', data[key]);
            form.appendChild(input);
        }
    }

    // Append the form to the document body and submit it
    document.body.appendChild(form);
    // Set token in a custom header
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    // Submit the form
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // Handle success, if needed
                form.submit();
            } else {
                // Handle error, if needed
            }
        }
    };

    // Send the request
    xhr.send(new FormData(form));
}

