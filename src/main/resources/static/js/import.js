$(document).ready(function () {

    $.ajaxSetup({
        beforeSend : function(xhr, settings) {
            if (settings.type == 'POST' || settings.type == 'PUT'
                || settings.type == 'DELETE') {
                if (!(/^http:.*/.test(settings.url) || /^https:.*/
                    .test(settings.url))) {
                    // Only send the token to relative URLs i.e. locally.
                    xhr.setRequestHeader("X-XSRF-TOKEN",
                        Cookies.get('XSRF-TOKEN'));
                }
            }
        }
    });


    $("#greetButton").click(function() {
        $.ajax({
            url: '/api/greet',
            type: 'GET',
            contentType: false,
            processData: false,
            success: function(data){
                console.log(data)
            },
        });
    })

    $("#userButton").click(function() {
        $.get('/api/user', function(data){
                console.log(data)
        });
    })

    $("#importButton").click(function() {
        let formData = new FormData();
        let files = $('#songsFile')[0].files;
        if(files.length > 0 ){
            formData.append('file',files[0]);
            $.ajax({
                url: '/api/search',
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function(data){
                    console.log(data)
                },
                error: function (err) {
                    console.log(err)
                }
            });
        }else{
            alert("Please select a file.");
        }
    })
})



// $( document ).ready(function () {
//     $("#sendRefresh").click(function() {
//         let refreshToken = $("#refreshToken")[0].value
//         $.ajax({
//             type: "POST",
//             url: "https://platform.devtest.ringcentral.com/restapi/oauth/token",
//             data: {
//                 grant_type: "refresh_token",
//                 refresh_token: refreshToken,
//             },
//             success: function (data) {
//                 console.log(data)
//                 $("#accessToken")[0].value = data["access_token"]
//                 $("#refreshToken")[0].value = data["refresh_token"]
//                 $("#expiresIn")[0].value = data["expires_in"]
//                 $("#endpointId")[0].value = data["endpoint_id"]
//                 $("#rtei")[0].value = data["refresh_token_expires_in"]
//             },
//             error: function () {
//                 alert("Any error")
//             },
//             headers: {
//                 "Accept": "application/json",
//                 "Content-Type": "application/x-www-form-urlencoded",
//                 "Authorization": "Basic WXdYM2dBNDRRZmU5LTc2UE5zWUw3QTowUHVGMEZpU1RycU92dXQ5VFRfVDRBbFhZdk91WTNRY0dWMHpfLUJmcjRDQQ=="
//             },
//         });
//     })
//
//     $("#sendRevoke").click(function() {
//         let accessToken = $("#accessToken")[0].value
//         $.ajax({
//             type: "POST",
//             url: "https://platform.devtest.ringcentral.com/restapi/oauth/revoke",
//             data: {
//                 token: accessToken,
//             },
//             success: function () {
//                 alert("Access token was revoked")
//             },
//             error: function () {
//                 alert("Any error")
//             },
//             headers: {
//                 "Accept": "application/json",
//                 "Content-Type": "application/x-www-form-urlencoded",
//                 "Authorization": "Basic WXdYM2dBNDRRZmU5LTc2UE5zWUw3QTowUHVGMEZpU1RycU92dXQ5VFRfVDRBbFhZdk91WTNRY0dWMHpfLUJmcjRDQQ=="
//             },
//         });
//     })
// })