String.prototype.replaceAll = function(s1,s2) {
    return this.replace(new RegExp(s1,"gm"),s2);
}
//这个用于单页或者首页,获取优酷视频的播放量，填写规则 <font id="archiveid_XMTI5MzE1NTQyMA=="><b></b></font>
function getcheat() {
    //获取id集合
    var obj = $("font[id^='archiveid_']");
    var archivesid = "";
    for (i = 0; i < obj.length; i++) {
        var id = $(obj[i]).attr("id");
        if (id != "") {
            if (id.split("_")[1] != "") {
                archivesid = archivesid + "," + id.split("_")[1];
            }
        }
    }
    if (archivesid != "") {
        $.ajax({
            url: "https://openapi.youku.com/v2/videos/show_batch.json?client_id=41d1c28bee0b6f7e&video_ids=" + archivesid,
            type: "get",
            dataType: "json",
            success: function (req) {
                for (i = 0; i < req.videos.length; i++) {
                    var video = req.videos[i];
                    var videoid=video.id.replaceAll("=", "\\=");
                    $("#archiveid_" + videoid).html("<b>" + video.view_count + "</b>人看过");
                    var m = parseInt(video.duration / 60) ;
                    var s = parseInt(video.duration % 60) ;
                    if (s < 10) {
                        s = '0' + s;
                    }
                    $('font[data-duration=archiveid_' + videoid + ']').html('<i></i>' + m + ':' + s);
                }

            }
        });
    }
}
$(document).ready(function () {
    getcheat();
});

