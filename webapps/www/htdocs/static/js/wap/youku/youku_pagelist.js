$(document).ready(function () {
    //获取id集合
    var obj = $("font[id^='archiveid_']");
    var archivesid = "";
    for (i = 0; i < obj.length; i++) {
        var id = $(obj[i]).attr("id");
        if (id != "") {
            archivesid = archivesid + "," + id.split("_")[1];
        }
    }
    if (typeof(archivesid) != 'undefined') {
        $.ajax({
            url: "http://api.joyme.com/joymeapp/gameclient/webview/marticle/getcheat?ykindex=true&archiveid="+archivesid,
            type: "get",
            dataType: "jsonp",
            jsonpCallback: "getcheatcallback",
            success: function(req) {
                var resMsg = req[0];
                if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    for (i = 0; i < result.length; i++) {
                        $("#archiveid_" + result[i].archivesid).html("<b>" + result[i].readnum + "</b>人看过");
                    }
                }
            }
        });
    }
});