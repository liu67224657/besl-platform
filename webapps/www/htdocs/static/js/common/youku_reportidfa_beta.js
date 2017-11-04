$(document).ready(function () {
    reportidfa();
});

function reportidfa() {
    var userAgentInfo = navigator.userAgent.toLowerCase();
    $('a[name=idfa_downloadlink]').click(function (e) {
        if (e && e.preventDefault) {
            e.preventDefault();
        }else {
            window.event.returnValue = false;
        }

        var _this = $(this);
        var aid = _this.attr("data-aid");
        //端内
        if (userAgentInfo.indexOf('youku hd') != -1 || userAgentInfo.indexOf('youku') != -1) {
            $.ajax({
                type: "POST",
                url: "http://joymebeta.youku.com/youku/api/report/idfa",
                async: true,
                data: {
                    aid: aid
                },
                dataType: "jsonp",
                jsonpCallback: "callback",
                success: function (req) {
                    window.location.href = _this.attr('href');
                }
            });
        }else{
            window.location.href = _this.attr('href');
        }

    });
}