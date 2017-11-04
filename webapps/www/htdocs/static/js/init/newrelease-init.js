define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    $(document).ready(function () {

        $('li[name = li_top_tag]').click(function () {
            $('li[name = li_hot_tag].current').removeClass('current');
            $('li[name = li_top_tag].current').removeClass('current');
            $(this).addClass('current');
            window.location.href = getQueryHref();
        });

        $('li[name = li_hot_tag]').click(function () {
            $('li[name = li_top_tag].current').removeClass('current');
            $('li[name = li_hot_tag].current').removeClass('current');
            $(this).addClass('current');
            window.location.href = getQueryHref();
        });

        $('a[name = li_city]').click(function () {
            $('a[name = li_city].current').removeClass('current');
            $(this).addClass('current');
            window.location.href = getQueryHref();
        });

        $('a[name = li_order]').click(function () {
            $('a[name = li_order].current').removeClass('current');
            $(this).addClass('current');
            window.location.href = getQueryHref();
        });

        $('input[name=coopratetype]:checkbox').click(function () {
            window.location.href = getQueryHref();
        });

        $('#a_button').click(function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
        });

        function getQueryHref() {
            var link = joyconfig.URL_WWW + "/newrelease";
            var unistop = $('#ul_tag_top li.current a').attr('unistop');
            if (unistop) {
                link = link + "?unistop=true";
            } else {
                link = link + "?unistop=false";
            }
            var coopType = Number(0);
            $("input[name = coopratetype]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    coopType += (Number($(this).attr("value")));
                }
            });
            if (coopType > 0) {
                link = link + "&coopratetype=" + coopType;
            }
            var topTagIdVal = $('#ul_tag_top li.current a').attr('tag_top_id');
            var hotTagIdVal = $('li[name = li_hot_tag].current a').attr('tag_hot_id');
            if (topTagIdVal != null) {
                link = link + "&tagid=" + topTagIdVal;
            } else if (hotTagIdVal != null) {
                link = link + "&tagid=" + hotTagIdVal;
            }
            var cityIdVal = $('a[name = li_city].current').attr('city_id');
            if (cityIdVal != null) {
                link = link + "&cityid=" + cityIdVal;
            }
            var order = $('a[name = li_order].current').attr('order');
            if (order != null) {
                link = link + "&ordertype=" + order;
            }
            return link;
        }

    });

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});





