<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>搜索</title>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        $(document).ready(function () {
            $('#submit_search_link,#search_link').click(function () {
                var text = $('#search_text_input').val();
                if (text != null && text.length > 0 && text != '搜一下游戏名就有') {
                    $('#search_form').submit();
                } else {
                    return false;
                }
            });

            $('#search_text_input').focus(function () {
                if ($('#search_text_input').val().length == 0 || $('#search_text_input').val() == '搜一下游戏名就有') {
                    $('#search_text_input').val('').attr('style', '');
                }
            }).blur(function () {
                if ($('#search_text_input').val().length == 0) {
                    $('#search_text_input').val('搜一下游戏名就有').attr('style', 'color:#b9b9b9');
                }
            });

            $("a[name=game_link]").click(function () {
                var gid = $(this).attr('data-gid');
                _jclient.jump('jt=24&ji=' + gid);
            });
        });

    </script>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <div class="soso so-top">
        <span>
            <a href="javascript:void(0);" id="submit_search_link"></a>
            <form id="search_form" action="http://api.${DOMAIN}/joymeapp/gameclient/webview/search/gamequery"
                  method="post">
                <input type="text" name="text" id="search_text_input"
                <c:choose>
                       <c:when test="${fn:length(text)>0}">value="${text}" </c:when>
                       <c:otherwise>value="搜一下游戏名就有" style="color:#b9b9b9"</c:otherwise>
                </c:choose>>
                <input type="hidden" name="token" value="${token}"/>
                <input type="hidden" name="uid" value="${uid}"/>
                <input type="hidden" name="uno" value="${uno}"/>
                <input type="hidden" name="appkey" value="${appkey}"/>
                <input type="hidden" name="platform" value="${platform}"/>
            </form>
        </span>
        <a href="javascript:void(0);" class="off fl" id="search_link">搜索</a>
    </div>
    <div class="look-for">
        <c:choose>
            <c:when test="${fn:length(searchList)==0}">
                <c:if test="${messagedisplay}">
                    <div class="no-attention"> 暂无您要找的游戏</div>
                </c:if>
                <div class="look-for-tit">
                    <div class="look-for-tit-l"><cite><img src="${URL_LIB}/static/theme/wap/images/djdz.png"
                                                           alt="">大家都在找</cite></div>
                    <div class="look-for-tit-r"></div>
                </div>
                <div class="look-main">
                    <c:forEach var="game" items="${gamelist}">
                        <div class="look-main-block fl">
                            <a href="javascript:void(0);" name="game_link" data-gid="${game.game.gameid}">
                                <cite><img src="${game.game.iconurl}" alt=""></cite>

                                <h2>${game.game.name}</h2>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="look-main">
                    <c:forEach var="game" items="${searchList}">
                        <div class="look-main-block fl">
                            <a href="javascript:void(0);" name="game_link" data-gid="${game.gameid}">
                                <cite><img src="${game.iconurl}" alt=""></cite>

                                <h2>${game.name}</h2>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<script src="${URL_LIB}/static/js/pwiki/wanba_game.js" type="text/javascript"></script>
</body>
</html>