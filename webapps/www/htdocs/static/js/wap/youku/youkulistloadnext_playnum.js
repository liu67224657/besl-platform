String.prototype.replaceAll = function(s1,s2) {
    return this.replace(new RegExp(s1,"gm"),s2);
}
//这个用于列表页,获取优酷视频的播放量，填写规则 <font id="archiveid_XMTI5MzE1NTQyMA=="><b></b></font>
function getReadNum() {
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

var locationhref = window.location.href;
var isload = false;
var loadToole = {
    'isLoading': false,
    'loadTime': null,
    'loadComment': function (config) {
        $.ajax({
            url: "http://cmsimage.joyme.com/article/page/nextpage.do",
            data: {
                href: locationhref
            },
            type: "post",
            dataType: "jsonp",
            async: false,
            jsonpCallback: "nextpage",
            success: function (req) {
                $('.loading').remove();
                clearTimeout(loadToole.loadTime);
                isload = false;
                loadToole.isLoading = false;
                if (req[0].rs == 1) {
                    locationhref = req[0].href;
                    $(".special-article").append(req[0].body);
                    getReadNum();
                } else if (req[0].rs == 2) {
                    $('.moreNo').show().delay(1000).hide(0);
                } else if (req[0].rs == 0) {
                    alert("系统繁忙");
                }
            },
            error: function (e) {
                $('.loading').remove();
                clearTimeout(loadToole.loadTime);
                isload = false;
                loadToole.isLoading = false;
            }
        });
    },
    //滚动加载
    'scroll_load': function (className, parentBox) {
        var className = className,
            parentBox = $("." + parentBox),
            oUl = parentBox.find('ul');
        $("." + className).scroll(function (ev) {
            ev.stopPropagation();
            ev.preventDefault();
            var sTop = $("." + className)[0].scrollTop;
            var sHeight = $("." + className)[0].scrollHeight;
            var sMainHeight = $("." + className).height();
            var sNum = sHeight - sMainHeight;
            var loadTips = '<div class="loading"><b><img src="http://joymepic.joyme.com/article/youkugamecenter/loading.png" alt=""></b><span>正在加载...</span></div>';
            if (sTop >= sNum && !loadToole.isLoading && !isload) {
                isload = true;
                loadToole.isLoading = true;
                $(".special-article").append(loadTips);
                loadToole.loadTime = setTimeout(function () {
                    loadToole.loadComment(oUl);
                }, 3000);
            }
        });
    }
};

$(document).ready(function () {
    getReadNum();
    loadToole.scroll_load('main', 'list-item');
});
