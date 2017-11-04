<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:wml="http://www.wapforum.org/2001/wml">
<head>
    <meta name="applicable-device"content="mobile">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="手机游戏礼包,激活码,兑换码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <title>最新热门手机游戏礼包,激活码,兑换码_着迷网移动版</title>
    <link href="${URL_STATIC}/mobile/cms/jmsy/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_STATIC}/mobile/cms/jmsy/yxk/css/yxk_common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_STATIC}/mobile/cms/jmsy/yxk/css/page.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        ;(function(doc,win) {
            var size=750;
            var int=100;
            var docEle = doc.documentElement,
                    evt = 'onorientationchange' in window ? 'orientationchange' : 'resize',
                    fn = function() {
                        var width = docEle.clientWidth;
                        if(width>size){
                            docEle.style.fontSize=int+'px';
                        }else{
                            width && (docEle.style.fontSize = width/(size/int) + 'px');
                        }
                        doc.addEventListener('touchstart',function () {return false}, true);
                    };
            win.addEventListener(evt, fn, false);
            doc.addEventListener('DOMContentLoaded', fn, false);
        }(document,window));
    </script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<header>
    <div class="topbar-mod border-b fn-clear topbar">
        <h1 class="logo-img">
            <a href="<c:out value="${URL_M}/collection"/>">
            <i><img src="${URL_STATIC}/mobile/cms/jmsy/images/logo-icon2.png" alt="游戏" title="游戏"/></i>游戏
            </a>
        </h1>
        <div class="search-box hide" id="search-box">
            <span><input type="text" value="" placeholder="搜索" class="search" id="search-text"></span>
        </div>
        <div class="search-bar">
            <span class="search-btn" id="search-btn"></span>
        </div>
    </div>
</header>
<div id="wrapper">
    <div class="crumbs">
        <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>"><c:out value="${game.gameName}"/></a><span><c:if test="${contentType.code == 0}">资讯</c:if><c:if test="${contentType.code == 1}">视频</c:if><c:if test="${contentType.code == 4}">攻略</c:if></span>
    </div>
    <c:if test="${contentType.code == 0 || contentType.code == 4}">
        <div class="art-box">
            <ul class="list" id="archive-list">
                <c:forEach items="${list}" var="archive">
                    <li class="border-b">
                        <a href="<c:out value="${archive.dede_archives_url}"/>">
                            <i><img src="" class="lazy" data-url="<c:out value="${archive.dede_archives_litpic}"/>" alt="<c:out value="${archive.dede_archives_title}"/>"/></i>
                            <p>
                                <span><c:out value="${archive.dede_archives_title}"/></span>
                                <code><c:out value="${archive.dede_archives_pubdate_str}"/></code>
                            </p>
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <c:if test="${page != null && page.curPage < page.maxPage}">
                <div class="more-load" id="moreArticle">
                    <a href="javascript:void(0);">点击加载更多...</a>
                </div>
            </c:if>
        </div>
    </c:if>
    <c:if test="${contentType.code == 1}">
        <div class="art-video">
            <ul class="fn-clear border-b" id="archive-list">
                <c:forEach items="${list}" var="archive">
                    <li>
                        <a href="<c:out value="${archive.dede_archives_url}"/>">
                            <i class="video-icon"><img src="" class="lazy" data-url="<c:out value="${archive.dede_archives_litpic}"/>" alt="<c:out value="${archive.dede_archives_title}"/>"/></i>
                            <span><c:out value="${archive.dede_archives_title}"/></span>
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <c:if test="${page != null && page.curPage < page.maxPage}">
                <div class="more-load" id="moreArticle">
                    <a href="javascript:void(0);">点击加载更多...</a>
                </div>
            </c:if>
        </div>
    </c:if>
</div>
<script>
    document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
</script>
<script src="http://static.joyme.com/mobile/cms/jmsy/js/lazyimg.js?${version}"></script>
<script>
    ;(function(){
        var wh=$(window).height();
        var hh=$('header').innerHeight();
        var sH=wh-hh;
        $('#wrapper').css('min-height',sH);
        var mod={
            searchFunc:function(){
                var searchBtn=$('.search-btn');
                var search=$('.search');
                searchBtn.on('click',function(){
                    $('.search-box').removeClass('hide');
                    search.focus();
                });
                search.on('blur',function(){
                    var vals=$(this).val();
                    if(vals!=''){
                        $('.search-box').removeClass('hide');
                    }else{
                        $('.search-box').addClass('hide');
                    }
                })
            }
        }
        mod.searchFunc();
        $('.lazy').lazyImg();
    })();
    var block = false;
    var cp = 1;
    var host = window.location.host.substr(window.location.host.indexOf('.'));
    var mHost = 'http://m' + host + '/';
    var apiHost = 'http://api' + host + '/';

    var contentType = '${contentType.code}';
    var gameid = '${game.gameDbId}';
    $(document).ready(function(){
        $('#search-btn').on('click', function(){
            if(!$('#search-box').hasClass('hide')){
                var name = $('#search-text').val();
                if(name != undefined && name != ''){
                    window.location.href = mHost + 'collection/genre/name:'+name;
                }
            }
        });

        $('#search-text').keydown(function(e){
            if(e.keyCode == 13){
                if(!$('#search-box').hasClass('hide')){
                    var name = $(this).val();
                    if(name != undefined && name != ''){
                        window.location.href = mHost + 'collection/genre/name:'+name;
                    }
                }
            }
        });

        $('#moreArticle').on('touchstart', function(){
            if (block) {
                return;
            }
            block = true;
            cp += 1;
            $.ajax({
                url: apiHost + "collection/api/archivelist",
                type: "post",
                data: {gameid: gameid, p:cp, archivetype:contentType},
                dataType: "jsonp",
                jsonpCallback: "archivelistcallback",
                success: function (req) {
                    var resMsg = req[0];
                    if (resMsg.rs == '-1001') {
                        alert('参数错误，无法完成此操作');
                        return;
                    } else if (resMsg.rs == '1') {
                        var result = resMsg.result;
                        if(result != null && result != undefined){
                            var rows = result.rows;
                            var page = result.page;
                            if(rows != null && rows != undefined && page != null && page != undefined){
                                cp = page.curPage;
                                var html = '';
                                for(var i=0;i<rows.length;i++){
                                    if(contentType == '0' || contentType == '4'){
                                        html += '<li class="border-b">' +
                                        '<a href="'+rows[i].dede_archives_url+'">' +
                                        '<i><img src="" class="lazy" data-url="'+rows[i].dede_archives_litpic+'" alt="'+rows[i].dede_archives_title+'"/></i>' +
                                        '<p><span>'+rows[i].dede_archives_title+'</span><code>'+rows[i].dede_archives_pubdate_str+'</code></p></a>' +
                                        '</li>'
                                    }else if(contentType == '1'){
                                        html += '<li>' +
                                        '<a href="'+rows[i].dede_archives_url+'">' +
                                        '<i class="video-icon"><img src="" class="lazy" data-url="'+rows[i].dede_archives_litpic+'" alt="'+rows[i].dede_archives_title+'"/></i>' +
                                        '<span>'+rows[i].dede_archives_title+'</span>' +
                                        '</a>' +
                                        '</li>'
                                    }
                                }
                                $('#archive-list').append(html);
                                $('.lazy').lazyImg();
                                if(page.curPage >= page.maxPage){
                                    $('#moreArticle').hide();
                                }
                                block = false;
                            }
                        }
                    } else {
                        alert('系统错误');
                        block = false;
                        return;
                    }
                },
                complete: function () {
                    block = false;
                    return;
                },
                error: function () {
                    alert('获取失败，请刷新');
                    block = false;
                    return;
                }
            });
        });
    });
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/collection-tj.js"></script>

<script>
    (function (G,D,s,c,p) {
        c={//监测配置
            UA:"UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS:0,
            WITH_REF:1,
            URL:'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt?G._iwt.track(c,p):(G._iwtTQ=G._iwtTQ || []).push([c,p]),!G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading=1;s=D.createElement("script");s.src=c.URL;
            t=D.getElementsByTagName("script");t=t[t.length-1];
            t.parentNode.insertBefore(s,t);
        }
    })(this,document);
</script>
</body>
</html>
