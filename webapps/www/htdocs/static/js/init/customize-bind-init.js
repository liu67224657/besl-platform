define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var customize = require('../page/customize');
    var header = require('../page/header');
    require('../common/tips');
    var joymealert = require('../common/joymealert');

    $(document).ready(function () {
        $(".publish_setcon li").live("mouseenter",
            function () {
                if ($(this).find("p[class=set_way_on]").size() > 0) {
                    $(this).find("p[class=set_way_on]").html('<a id="unbind_link" data-href="/profile/sync/' + $(this).attr("data-provider") + '/unbind" data-apicode="' + $(this).attr("data-provider") + '" href="javascript:void(0)">取消绑定</a>');
                    $('#unbind_link').die().live('click', function () {
                        customize.unBind($(this));
                    });
                }
            }).live("mouseleave", function () {
                if ($(this).find("p[class=set_way_on]").size() > 0) {
                    $(this).find("p[class=set_way_on]").html('已绑定');
                }
            });

        $('#but_alert').live('click', function () {
            $('#joymealert_bind').remove();
        });
        if (typeof errorcode != 'undefined' && errorcode != null && errorcode.length > 0) {
            joymealert.alert({text:errorcode,tipLayer:true,textClass:"tipstext",width:300});
        }

    });
    header.noticeSearchReTopInit();
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});