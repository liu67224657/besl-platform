<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">
<meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta http-equiv="Expires" CONTENT="0">
<meta http-equiv="Cache-Control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache">
<meta name="Keywords" content="热门应用下载">
<meta name="description"
      content="着迷网热门应用下载中心."/>

<title><c:choose><c:when test="${not empty template && template==3}">推荐应用</c:when>
    <c:otherwise>精品应用</c:otherwise></c:choose></title>
<link href="${URL_LIB}/static/theme/default/css/wap_common_hotapp.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
<script type="text/javascript">

$(document).ready(function (e) {

    window.addEventListener('DOMContentLoaded', function () {
        document.addEventListener('touchstart', function () {
            return false
        }, true)
    }, true);


    window.template = '${template}';
    window.queryData = new Object();
    queryData.pageNo = 1;
    queryData.pageSize = 10;
    queryData.appkey = '${appkey}';
    queryData.profileid = '${profileid}';
    queryData.clientid = '${clientid}';
    queryData.platform = '${platform}';
    window.maxFlag = false;
    window.loadFlag = {};
    loadFlag.loadTime = null;
    loadFlag.isLoading = false;

    <c:if test="${fn:length(errorMessage)>0}">
    alert('${errorMessage}');
    </c:if>

    <c:if test="${not empty template && template==3}">
    window.userHasPoint =${userHasPoint};
    if (window.userHasPoint == null || window.userHasPoint == '') {
        window.userHasPoint = 0;
    }
    $("#userHasPoint").html(window.userHasPoint);
    </c:if>


    $("#loading-btn-a").bind('click', function () {
        $("#loading-btn-a i").css('display', 'inline-block');
        $("#loading-btn-a b").html('加载中');
        getMoreList();
    });

    getMoreList();
    $(window).scroll(toLoadMore);
});


function getMoreList() {
    $("#loading-btn-a i").css('display', 'inline-block');
    $("#loading-btn-a b").html('加载中');
    $applist = $("#applist");
    $.ajax({type:"post",
                url:"/json/my/hotapp/jsonmore",
                data:queryData,
                timeout:5000,
                dataType:"json",
                success:function(data) {
                    if (data.errorStatus == 1) {
                        $("#popup").text(data.errorMessage);
                        $("#popup").addClass("close");
                        var t = setInterval(function () {
                            $("#popup").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                        $("#loading-btn-a i").css('display', 'none');
                        $("#loading-btn-a b").html("加载更多");
                        return;
                    } else if (typeof(data.total ) == "undefined") {
                        $("#loading-btn-a b").html("木有了....(＞﹏＜)");
                            $("#loading-btn-a i").css('display', 'none');
                        $("#loading-btn-a").unbind();
                        return;
                    }

                    var toInsert = '';
                    $.each(data.list, function (index, item) {
                        toInsert += '<li id="' + item.wallAppId + '"> ';
                        if (item.hotStatus == 1) {
                            toInsert += ' <cite class="hot"><img src="${URL_LIB}/static/theme/default/images/my/hot.png" alt="" /></cite> ';
                        }
                        if (item.installStatus == 1 || item.installStatus == 3) {         //无论是下载超过5分钟还是在内都显示为"获取"
                            toInsert += '<a href="javascript:void(0);" >';
                        } else if (item.installStatus == 2) {
                            toInsert += '<a href="javascript:void(0);" > ';
                        } else {
                            toInsert += '<a href="javascript:void(0);" >';
                        }
                        toInsert += '<div class="main-list"><cite class="title-pic"> <img src="' + item.appIcon + '" alt="" /> </cite>';
                        toInsert += '<div class="title-text"><h1 class="cut_out2">' + item.appName + '</h1><p class="cut_out1">' + item.appDesc + '</p></div></div>';
                        if (window.template != '' && window.template == 3) {
                            toInsert += '<em class="zhuan">赚' + item.pointAmount + '${wallMoneyName}</em>';
                        }
                        if (item.installStatus == 1 || item.installStatus == 3) {
                            toInsert += '<span class="mian-btn download" onclick="toGetScore(' + item.wallAppId + ')" >领取</span> </a>  </li> ';
                        } else if (item.installStatus == 2) {
                            toInsert += '<span class="have">已领取</span> </a>  </li> ';
                        }
                        else {
                            toInsert += '<span class="mian-btn download"  onclick="toDownload(' + item.wallAppId + ')" >下载</span></a></li> ';
                        }

                    });

                    $("#loading-btn-a i").css('display', 'none');
                    $("#loading-btn-a b").html("加载更多");
                    $applist.append(toInsert);

                    if (data.maxPage <= queryData.pageNo) {
                        window.maxFlag = true;
                        $("#loading-btn-a b").html("木有了....(＞﹏＜)");
                        $("#loading-btn-a").unbind();
                    }

                    queryData.pageNo += 1;
                }, error:function(XMLHttpRequest, status) {
                    if (status == 'timeout') {
                        $("#loading-btn-a i").css('display', 'none');
                        $("#loading-btn-a b").html("点击加载更多");
                        toAlert("网络异常，请稍后重试");
                    }
                }

            });
}

function toDownload(wallAppId) {
    queryData.wallAppId = wallAppId;
    $("#" + wallAppId).find("a").removeAttr("onclick");
    $.ajax({type:"post",
                url:"/json/my/hotapp/download",
                data:queryData,
                timeout:5000,
                dataType:"json",
                success:function(data) {
                    if (data.errorStatus == 1) {
                        toAlert(data.errorMessage);
                        return;
                    } else {
                    <%--第一次在本页面执行下载--%>
                        if (typeof window.myIdArray == "undefined") {
                            window.myIdArray = new Array();
                            window.myTimeArray = new Array();
                            window.myFuncArray = new Array();
                        }

                        //第一次下载本app
                        if (window.myIdArray[wallAppId] === undefined) {
                            window.myIdArray[wallAppId] = "app" + wallAppId;
                            window.myTimeArray[wallAppId] = new Date().getTime() / 1000;  //存储的是秒
                            window.myFuncArray[wallAppId] = "";
                            //立即下载
                            window.location.href = data.downloadUrl;
                            //立即改成领取,如果template==3，把 那个赚删掉


                            //  $("#" + wallAppId).find("a>em[class=zhuan]").remove();     //2015-04-24   bug 9429
                            $("#" + wallAppId).find(" .mian-btn").attr("onclick", "toGetScore(" + wallAppId + ")");
                            $("#" + wallAppId).find(" .mian-btn").html("领取");       //2015-04-24   bug 9429

                        }

                    }
                }, error:function() {
                    toAlert("网络异常，请稍后重试");
                }

            });
<%--$.post("/json/my/hotapp/download", queryData, function (data, textStatus) {--%>

<%--if (data.errorStatus == 1) {--%>
<%--toAlert(data.errorMessage);--%>
<%--return;--%>
<%--} else {--%>
<%--&lt;%&ndash;第一次在本页面执行下载&ndash;%&gt;--%>
<%--if (typeof window.myIdArray == "undefined") {--%>
<%--window.myIdArray = new Array();--%>
<%--window.myTimeArray = new Array();--%>
<%--window.myFuncArray = new Array();--%>
<%--}--%>

<%--//第一次下载本app--%>
<%--if (window.myIdArray[wallAppId] === undefined) {--%>
<%--window.myIdArray[wallAppId] = "app" + wallAppId;--%>
<%--window.myTimeArray[wallAppId] = new Date().getTime() / 1000;  //存储的是秒--%>
<%--window.myFuncArray[wallAppId] = "";--%>
<%--//立即下载--%>
<%--window.location.href = data.downloadUrl;--%>
<%--//立即改成领取,如果template==3，把 那个赚删掉--%>


<%--//  $("#" + wallAppId).find("a>em[class=zhuan]").remove();     //2015-04-24   bug 9429--%>
<%--$("#" + wallAppId).find(" .mian-btn").attr("onclick", "toGetScore(" + wallAppId + ")");--%>
<%--$("#" + wallAppId).find(" .mian-btn").html("领取");       //2015-04-24   bug 9429--%>

<%--}--%>

<%--}--%>

<%--}, "json");--%>

}

function toAlert(content) {
    $("#popup").text(content);
    $("#popup").addClass("close");
    var t = setInterval(function () {
        $("#popup").removeClass("close");
        clearTimeout(t);
    }, 3000);

}
function toGetScore(wallAppId) {
    $("#" + wallAppId).find("a").removeAttr("onclick");
    queryData.wallAppId = wallAppId;
    $.ajax({type:"post",
                url:"/json/my/hotapp/getscore",
                data:queryData,
                timeout:5000,
                dataType:"json",
                success:function(data) {
                    if (data.errorStatus == 0) {
                        toAlert("领取" + data.pointAmount + "${wallMoneyName}");

                        $("#" + wallAppId).find("a").attr("href", "#").removeAttr("onclick");
                        $("#" + wallAppId).find(" .mian-btn").removeAttr("class").attr("class", "have").html("已领取");

                        var userHasPoint = $("#userHasPoint");
                        if (userHasPoint.length > 0) {
                            window.userHasPoint = parseInt(window.userHasPoint) + parseInt(data.pointAmount);
                            userHasPoint.html(window.userHasPoint);
                            // localStorage.setItem("userHasPoint", window.userHasPoint);
                        }
                        gid = new Date().getTime();
                        var stateObj = {
                            title: "title",
                            url: "url",
                            msg: "msg"
                        };

                    } else if (data.errorStatus == 3) {      //到5分钟再领取

                        var remainder = 5 - parseInt(data.timeGap / 60);
                        $("#dialog-second").text(remainder);
                        $("#dialog-url").attr("onclick", "downloadFromDialog('" + data.downloadUrl + "');");
                        $("#dialog-div").addClass("close");
                        $(".mark-box").show();
                        // $("#" + wallAppId).find("a").attr("onclick", "toGetScore(" + wallAppId + ")");
                    } else if (data.errorStatus == 4) {      //已经领取过

                        toAlert(data.errorMessage);
                        $("#" + wallAppId).find("a").attr("href", "#").removeAttr("onclick");
                        $("#" + wallAppId).find(" .mian-btn").removeAttr("class").attr("class", "have").html("已领取");
                    } else {
                        toAlert(data.errorMessage);
                        //  $("#" + wallAppId).find("a").attr("onclick", "toGetScore(" + wallAppId + ")");
                    }
                }, error:function() {
                    toAlert("网络异常，请稍后重试");
                }

            });
}

<%--从那个倒数的对话框点击ok--%>
function cancelDialog() {
    $('.dialog').removeClass('close');
    $(".mark-box").hide();
}

<%--从那个倒数的对话框点击下载--%>
function downloadFromDialog(url) {
    $('.dialog').removeClass('close');
    $(".mark-box").hide();
    window.location.href = url;
}


function toLoadMore() {
    if (window.maxFlag) {
        return;
    }

    var sTop = $(window).scrollTop();
    var sHeight = $(document).height();
    var sMainHeight = $(window).height();
    var sNum = sHeight - sMainHeight;

    //console.log("sTop-->" + sTop + "sHeight->" + sHeight + "sMainHeight-->" + sMainHeight + "sNum-->" + (sHeight - sMainHeight));

    if (sTop + 50 >= sNum && !loadFlag.isLoading) {
        loadFlag.isLoading = true;

        $("#loading-btn i").css('display', 'inline-block');
        $("#loading-btn b").html('加载中');
        getMoreList();

        loadFlag.loadTime = setTimeout(function () {
            loadFlag.isLoading = false;
        }, 1000);
    }
}
function toOpenUrl(obj) {

    gid = new Date().getTime();
    var stateObj = {
        title: "title",
        url: "url",
        msg: "msg"
    };

    window.location.href = $(obj).attr("data-href");

}

</script>
</head>
<body>
<div id="wrapper">
    <c:choose>
        <c:when test="${not empty template && template==3}">
            <div class="Personal_title">
                <p class="fl"><em>我有<span id="userHasPoint">${userHasPoint}</span>${wallMoneyName}</em> <c:if
                        test="${appkey=='119mpBeIV49bCDFJj5uSZ4'}"><em>客服QQ3135735548</em> </c:if></p>
                <a href="javascript:void(0);" data-href="${linkUrl}" onclick="toOpenUrl(this);" class="fr">实物兑换</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="banner">
                <div class="banner-text">
                    <p>赚${wallMoneyName}技术哪里强？</p>

                    <p>装应用，玩得爽，轻松赚${wallMoneyName}</p>
                </div>
                <img src="${URL_LIB}/static/theme/default/images/my/banner.jpg" alt="">
            </div>
        </c:otherwise>

    </c:choose>
    <div class="black-dialog" id="popup" style="z-index: 11;"></div>

    <!-- 列表 -->
    <div class="main">
        <ul id="applist">

        </ul>
    </div>

    <div class="loading" id="loading-btn-a"><a href="javascript:void(0);"><i style="display:none"></i><b>加载更多</b></a>
    </div>


</div>

<div class="dialog" id="dialog-div">
    <br/>

    <p class="confirm-text2">正在审核下载是否成功,成功后领取奖励,请稍等<span class="all" id="dialog-second"></span>分钟</p>

    <div class="dialog-btn"><a href="javascript:void(0);" onclick="cancelDialog();" class="cancel">OK</a><a
            href="javascript:void(0);" class="gain"
            id="dialog-url">重新下载</a></div>
</div>
<div class="mark-box"></div>

<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 106]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=106" style="border:0;" alt=""/></p></noscript>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>


</body>
</html>
