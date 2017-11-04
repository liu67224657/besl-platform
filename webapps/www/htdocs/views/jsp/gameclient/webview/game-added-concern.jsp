<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>新增游戏关注</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common_xykc.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/wap_style_xykc.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/wap_xykc_loading.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/gameclient-common.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false;
            }, true);
        }, true);
    </script>
    <script type="text/javascript">
        $(document).ready(function () {

            var uid = '${uid}';
            var idarray = new Array();
            <c:forEach items="${gameIdList}" var="game">
            idarray.push('${game.objectId}');
            </c:forEach>

            $('#interest-tit-num').html(idarray.length);

            $("a[name=link_like]").on("click", function () {
                $('#tip').html('').removeClass('show');
                var gameid = $(this).attr('data-gameid');
                var idx = idarray.indexOf(gameid);
                if (idx > -1) {
                    if (idarray.length == 1) {
                        showTips('亲，请至少关注1个游戏', 2000);
                        return;
                    }
                    idarray.splice(idx, 1);
                    $('#span_like_' + gameid).attr('class', 'att1').find("em").html("关注");
                } else {
                    if (idarray.length >= 6) {
                        showTips('亲，请先关注6个游戏，稍后随时可以修改', 2000);
                        return;
                    }
                    idarray.push(gameid);
                    $('#span_like_' + gameid).attr('class', 'att2').find("em").html("已关注");
                }
                $('#interest-tit-num').html(idarray.length);
            });

            window.postlock = false;
            $('#link_submit').on("click", function () {

                if (postlock) {
                    return;
                }
                if (idarray.length == 0) {
                    showTips('亲，请至少关注1个游戏', 2000);
                    return;
                }
                postlock = true;
                $('#loading').css('display', '');
                var t = setTimeout(function () {
                    $('#loading').css('display', 'none');
                    postlock = false;
                    //   clearTimeout(t);
                }, 2000);
                $.ajax({
                    url: 'http://api.' + joyconfig.DOMAIN + "/joymeapp/gameclient/json/game/batchlike",
                    data: {uid: uid, gids: idarray.join(","), appkey: '${appkey}', platform: '${platform}'},
                    type: "POST",
                    success: function (request) {
                        $('#loading').css('display', 'none');
                        postlock = false;
                        _jclient.jump('jt=30');
                    },
                    complete: function (request) {
                        $('#loading').css('display', 'none');
                        postlock = false;
                    }
                });
            });

            var searchFlag = '${searchFlag}';
            if (searchFlag == '1') {
                $("#toSearch").html("取消");
                $("#toSearch").attr("style", "color: #2F8FED;");
            }

            $("#toSearch").on("click", function () {
                var text = $.trim($("#searchtext").val());
                if (text == undefined || text == '' || text == '请输入游戏名称') {
                    showTips('亲,请先输入搜索条件', 2000);
                    return false;
                }
                if (searchFlag == '1') {
                    window.location.href = "/joymeapp/gameclient/webview/game/newaddedconcern?uid=${uid}&platform=${platform}&appkey=${appkey}";
                } else {
                    window.location.href = "/joymeapp/gameclient/webview/search/playinggame?uid=${uid}&platform=${platform}&appkey=${appkey}&text=" + text;
                }
            });

            var searchtext = '${text}';
            if (searchtext != '') {
                $("#searchtext").val(searchtext);
            }

        $("#searchtext").on("focus", function () {
                var text = $("#searchtext").val();
                if (text == '请输入游戏名称') {
                    $("#searchtext").val('');
                }
                if (searchFlag == '1') {
                    $("#toSearch").html("搜索");
                    $("#toSearch").removeAttr("style");
                    $("#toSearch").off("click");
                    $("#toSearch").on("click", function () {
                        var text = $.trim($("#searchtext").val());
                        if (text == undefined || text == '' || text == '请输入游戏名称') {
                            showTips('亲,请先输入搜索条件', 2000);
                            return false;
                        }
                        window.location.href = "/joymeapp/gameclient/webview/search/playinggame?uid=${uid}&platform=${platform}&appkey=${appkey}&text=" + text;
                    });
                }
            }).on("blur", function () {
                if ( $("#searchtext").val() == '') {
                    $("#searchtext").val('请输入游戏名称');
                }
            });
        });

    </script>



<body>
<div id="wrapper" class="wrapper">
    <div class="interest-tit">
        <h1>选择您感兴趣的游戏（<span class="interest-tit-num" id="interest-tit-num">0</span>/6）</h1>
    </div>
    <div class="soso so-top"><span><a href="#"></a>
        <input id="searchtext" type="text" style="color:#b9b9b9" value="请输入游戏名称"/></span>
        <a href="javascript:void(0);" id="toSearch" class="off fl search">搜索</a></div>

    <c:if test="${empty gamelistsearch&&searchFlag!=null&&searchFlag==1}">
        <div style="padding: 20px 0 0 10px;text-align: left;color: #9e9e9e;">
            暂无您要找的游戏
        </div>
    </c:if>
    <div class="look-main clearfix">
        <c:forEach items="${gamelistsearch}" var="game">
            <div class="look-main-block add-attention-block fl">
                <a href="javascript:void(0);" name="link_like" data-gameid="${game.game.gameid}">
                    <cite>
                        <c:choose>
                            <c:when test="${game.game.dottype==1}">
                                <small class="new"></small>
                            </c:when>
                            <c:when test="${game.game.dottype==2}">
                                <small class="hot"></small>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <img src="${game.game.iconurl}" alt="">
                        <c:choose>
                            <c:when test="${game.relationstatus==1}">
                                <span class="att2" id="span_like_${game.game.gameid}"><em>已关注</em></span>
                            </c:when>
                            <c:otherwise>
                                <span class="att1" id="span_like_${game.game.gameid}"><em>关注</em></span>
                            </c:otherwise>
                        </c:choose>
                    </cite>

                    <h2>${game.game.name}</h2>
                </a>
            </div>
        </c:forEach>

        <c:forEach items="${gamelist}" var="game">
            <div class="look-main-block add-attention-block fl">
                <a href="javascript:void(0);" name="link_like" data-gameid="${game.game.gameid}">
                    <cite>
                        <c:choose>
                            <c:when test="${game.game.dottype==1}">
                                <small class="new"></small>
                            </c:when>
                            <c:when test="${game.game.dottype==2}">
                                <small class="hot"></small>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <img src="${game.game.iconurl}" alt="">
                        <c:choose>
                            <c:when test="${game.relationstatus==1}">
                                <span class="att2" id="span_like_${game.game.gameid}"><em>已关注</em></span>
                            </c:when>
                            <c:otherwise>
                                <span class="att1" id="span_like_${game.game.gameid}"><em>关注</em></span>
                            </c:otherwise>
                        </c:choose>
                    </cite>

                    <h2>${game.game.name}</h2>
                </a>
            </div>
        </c:forEach>
    </div>
    <div class="tip" id="tip"></div>
    <!--黑色提示框  -->
    <div class="spinnerbox" id="loading" style="display: none">
        <div class="spinner">
            <p>努力加载中</p>

            <div class="bounce1"></div>
            <div class="bounce2"></div>
            <div class="bounce3"></div>
        </div>
    </div>
</div>
<div class="finishBox">
    <div class="finishBtn">
        <a href="javascript:void(0);" id="link_submit">完成</a>
    </div>
</div>





</body>
</html>