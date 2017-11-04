function getReadNum() {
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
            url: "http://api.joyme.com/joymeapp/gameclient/webview/marticle/getcheat?ykindex=true&archiveid=" + archivesid,
            type: "get",
            dataType: "jsonp",
            jsonpCallback: "getcheatcallback",
            success: function (req) {
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




