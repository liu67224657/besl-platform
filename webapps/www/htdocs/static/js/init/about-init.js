/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 12-2-1
 * Time: 下午2:24
 */
define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    $(".headt").css("height", "57px")
    $(document).ready(function () {
        var urlStr = window.location.href;
        var text = urlStr.split('/');
        var hrefText = text[text.length - 1];
        if (hrefText) {
            if (hrefText == "aboutus") {
                $("#aboutus").html('<b>' + $("#aboutus").html() + '</b>');
                $("#aboutus").addClass("about-nav-active");
            }
            if (hrefText == "service") {
                $("#service").html('<b>' + $("#service").html() + '</b>');
                $("#service").addClass("about-nav-active");
            }
            if (hrefText == "zhaopin") {
                $("#zhaopin").html('<b>' + $("#zhaopin").html() + '</b>');
                $("#zhaopin").addClass("about-nav-active");
            }
            if (hrefText == "contactus") {
                $("#contactus").html('<b>' + $("#contactus").html() + '</b>');
                $("#contactus").addClass("about-nav-active");
            }
            if (hrefText == "help") {
                $("#help").html('<b>' + $("#help").html() + '</b>');
                $("#help").addClass("about-nav-active");
            }
            if (hrefText == "apply") {
                $("#apply").html('<b>' + $("#apply").html() + '</b>');
                $("#apply").addClass("about-nav-active");
            }
            if (hrefText == "ambassador") {
                $("#ambassador").html('<b>' + $("#ambassador").html() + '</b>');
                $("#ambassador").addClass("about-nav-active");
            }
            if (hrefText == "press") {
                $("#press").html('<b>' + $("#press").html() + '</b>');
                $("#press").addClass("about-nav-active");
            }

        }
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
})
        ;