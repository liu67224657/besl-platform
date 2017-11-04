function is_weixn() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger" || ua.match(/WeiBo/i) == "weibo" || ua.match(/QQ/i) == "qq") {
        return true;
    } else {
        return false;
    }
}

$(function () {
    function adverH() {
        var adverImgH = $('.advertising img').height(),
            adverTop = $('.adver-top'),
            suspendEle = $('.suspend-ele'),
            suspendEleH = suspendEle.height();
        if (suspendEleH) {
            var newH = adverImgH + suspendEleH;
            adverTop.css({'paddingBottom': newH});
            suspendEle.css({'bottom': adverImgH});
        } else {
            adverTop.css({'paddingBottom': adverImgH});
        }
    }

    adverH();
    function downLoadSh() {

        //如果在微信端打开点击下载
        $(".adver-con img").click(function (e) {
            // alert(is_weixn())
            //if (is_weixn()) {
            //    $(".open-in-browser").addClass("on");
            //} else {
            //    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.enjoyf.wanba";
            //}
            e.preventDefault();
            window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.enjoyf.wanba";
        });
        $(".open-in-browser").click(function () {
            $(this).removeClass("on");
        });
    }

    downLoadSh();


})

$("a[href^='jmwanba://']").on('click', function (e) {
    e.preventDefault();
    var href = $(this).attr("href");
    //alert(href)
    //此操作会调起app并阻止接下来的js执行
    //$('body').append("<iframe src='" + href + "' style='display:none' target='_parent' ></iframe>");
    ////没有安装应用会执行下面的语句
    if (is_weixn()) {
        $(".open-in-browser").addClass("on");
    } else {
        window.location = href;
        setTimeout(function () {
            window.location = 'http://wanba.joyme.com/'
        }, 3000);
    }
});