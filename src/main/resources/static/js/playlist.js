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

    $("#showArtistsButton").click(function() {
        $.get("/api/saved-artists", function (data) {
            data.forEach(e => $("#artistsList")
                .append(`<li>${e.first}: ${e.second}</li>`))
        })
    })

    $("#createPlaylistButton").click(function() {
        let artistId = $("#artistIdInput").val()
        $.post(`/api/saved-artists/${artistId}/playlist`, function () {
            alert("Playlist was created!")
        })
    })
})