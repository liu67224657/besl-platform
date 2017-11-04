function getcheat() {
    var archiveid = $("#_pageid").html();
    if (typeof(archiveid) != 'undefined') {
        $.ajax({
            url: "http://api.joyme.com/joymeapp/gameclient/webview/marticle/getcheat?type=yk&archiveid=" + archiveid,
            type: "get",
            dataType: "jsonp",
            jsonpCallback: "getcheatcallback",
            success: function (req) {

            }
        });
    }
}
$(document).ready(function () {
    getcheat();
});