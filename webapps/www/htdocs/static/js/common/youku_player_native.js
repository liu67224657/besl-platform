$(document).ready(function () {
    var youkuPlayers = $('div[id ^=youkuplayer_]');
    if (youkuPlayers.length == 0) {
        return;
    }
    if (typeof(ykaid) == 'undefined') {
        ykaid = '41d1c28bee0b6f7e';
    }
    var userAgentInfo = navigator.userAgent.toLowerCase();
    $.each(youkuPlayers, function (i, val) {
        var div = $(val);
        var vid = div.attr("data-vid");
        var id = div.attr("id");
        var log = div.attr("logo");
        //兼容youku的链接
        if (vid.indexOf("youku.com") > 0) {
            vid = vid.substring(vid.lastIndexOf("/") + 1);
            vid = vid.replace("id_", "");
            if (vid.indexOf(".") > 0) {
                vid = vid.substring(0, vid.indexOf("."));
            }
            if (vid.indexOf("?") > 0) {
                vid = vid.substring(0, vid.indexOf("?"));
            }
            if (vid.indexOf("#") > 0) {
                vid = vid.substring(0, vid.lastIndexOf("#"));
            }
        }
        //ipad
        if (userAgentInfo.indexOf('youku hd') != -1) {
            var html = "<a id='youku_video' href='youkuhd://jsbplay?source=mplaypage&amp;vid=" + vid + "'>";
            html += "<img src='" + log + "'/></a>"
            $(val).replaceWith(html);
        } else if (userAgentInfo.indexOf('youku') != -1) {
            var html = "<a id='youku_video' href='youku://jsbplay?source=mplaypage&amp;vid=" + vid + "'>";
            html += "<img src='" + log + "'/></a>"
            $(val).replaceWith(html);
        } else {
            new YKU.Player(id, {
                styleid: '0',
                client_id: ykaid,
                vid: vid
            });
        }
    });
});