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
    <meta name="Keywords" content="安卓游戏免费下载,iPhone游戏免费下载,免费手机游戏">
    <meta name="description"
          content="着迷网手机游戏库推荐好玩的手机游戏,提供手机游戏排行榜,并有安卓游戏及iPhone游戏免费下载地址,扫二维码直接下载,大家赶紧来到这里选择自己喜欢的游戏吧."/>
    <title>安卓游戏免费下载_iPhone游戏免费下载_免费手机游戏_着迷网Joyme.com</title>

    <link href="${URL_STATIC}/mobile/cms/jmsy/yxk/css/newyxk_common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_STATIC}/mobile/cms/jmsy/yxk/css/newindex.css?${version}" rel="stylesheet" type="text/css"/>
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
    <nav>
        <div class="menu-box border-b">
            <div class="mu-list" id="div-platform">
                <h3 id="h3-platform">平台</h3>
                <ul>
                    <li name="genre_platformtype" class="border-b" data-platformtype=""><a href="javascript:void (0)">全部</a></li>
                    <c:forEach items="${platformTypeSet}" var="platformType">
                        <li name="genre_platformtype" class="border-b" data-platformtype="<c:out value="${platformType.code}-"/>"><a href="javascript:void (0)"><c:out value="${platformType.desc}"/></a></li>
                    </c:forEach>
                </ul>
            </div>

            <div class="mu-list" id="div-category">
                <h3 id="h3-category">类型</h3>
                <ul>
                    <c:forEach items="${categorySet}" var="category">
                        <li class="border-b" name="genre_category" data-category="<c:out value="${category.code}"/>"><a href="javascript:void(0);"><c:out value="${category.value}"/></a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="mu-list">
                <h3 id="h3-publishtime" name="genre_publishtime"><a href="javascript:void(0)">时间</a></h3>          
            </div>
            <div class="mu-list">
                <h3 id="h3-rate" name="genre_rate" ><a href="javascript:void(0)">评分</a></h3>
            </div>             
           <%--  <div class="mu-list" id="div-device">
                <h3 id="h3-device">机型</h3>
                <c:forEach items="${platformMap}" var="map" varStatus="st">
                    <ul id="platform_<c:out value="${map.key}"/>" style="display: none">
                        <c:forEach items="${platformMap[map.key]}" var="platform">
                            <li name="genre_platform" data-platform="<c:out value="${map.key}-${platform.code}"/>"><a href="javascript:void (0)"><c:out value="${platform.desc}"/></a></li>
                        </c:forEach>
                    </ul>
                </c:forEach>
            </div>
            <div class="mu-list" id="div-net">
                <h3 id="h3-net">联网</h3>
                <ul>
                    <c:forEach items="${netTypeSet}" var="net">
                        <li name="genre_net" data-net="<c:out value="${net.code}"/>"><a href="javascript:void (0)"><c:out value="${net.name}"/></a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="menu-box border-b">
            <div class="mu-list" id="div-language">
                <h3 id="h3-language">语言</h3>
                <ul>
                    <c:forEach items="${languageTypeSet}" var="language">
                        <li name="genre_language" data-language="<c:out value="${language.code}"/>"><a href="javascript:void(0);"><c:out value="${language.name}"/></a></li>
                    </c:forEach>
                </ul>
            </div>      
            <div class="mu-list" id="div-theme">
                <h3 id="h3-theme">题材</h3>
                <ul>
                    <c:forEach items="${themeTypeSet}" var="theme">
                        <li name="genre_theme" data-theme="<c:out value="${theme.code}"/>"><a href="javascript:void(0);"><c:out value="${theme.name}"/></a></li>
                    </c:forEach>
                </ul>
            </div> --%>
              
        </div>
    </nav>
    <div class="game-list fn-ovf">
        <h3>着迷推荐</h3>
        <ul>
            <c:forEach items="${recommendList}" var="game" varStatus="st">
                <li>
                    <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>" class="border-b">
                        <i class="fn-left"><img class="lazy" src="" data-url="<c:out value="${game.gameIcon}"/>" alt="<c:out value="${game.gameName}"/>" title="<c:out value="${game.gameName}"/>"></i>
                        <div class="fn-left">
                            <h4><c:out value="${game.gameName}"/><span></span></h4>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></p>
                            <p>平台：<c:forEach items="${game.platformMap}" var="map">
                                <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                            </c:forEach></p>
                        </div>
                    </a>
                    <canvas class="process" width="168" height="168">${game.gameRate}</canvas>
                </li>
            </c:forEach>
        </ul>
        <h3>最新入库</h3>
        <ul>
            <c:forEach items="${newList}" var="game" varStatus="st">
                <li>
                    <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>" class="border-b">
                        <i class="fn-left"><img class="lazy" src="" data-url="<c:out value="${game.gameIcon}"/>" alt="<c:out value="${game.gameName}"/>" title="<c:out value="${game.gameName}"/>"></i>
                        <div class="fn-left">
                            <h4><c:out value="${game.gameName}"/><span></span></h4>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></p>
                            <p>平台：<c:forEach items="${game.platformMap}" var="map">
                                <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                            </c:forEach></p>
                        </div>
                    </a>
                    <canvas class="process" width="168" height="168">${game.gameRate}</canvas>
                </li>
            </c:forEach>
        </ul>
        <h3>手机游戏</h3>
        <ul>
            <c:forEach items="${mobileList}" var="game" varStatus="st">
                <li>
                    <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>" class="border-b">
                        <i class="fn-left"><img class="lazy" src="" data-url="<c:out value="${game.gameIcon}"/>" alt="<c:out value="${game.gameName}"/>" title="<c:out value="${game.gameName}"/>"></i>
                        <div class="fn-left">
                            <h4><c:out value="${game.gameName}"/><span></span></h4>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></p>
                            <p>平台：<c:forEach items="${game.platformMap}" var="map">
                                <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                            </c:forEach></p>
                        </div>
                    </a>
                    <canvas class="process" width="168" height="168">${game.gameRate}</canvas>
                </li>
            </c:forEach>
        </ul>
        <h3>电脑游戏</h3>
        <ul>
            <c:forEach items="${pcList}" var="game" varStatus="st">
                <li>
                    <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>" class="border-b">
                        <i class="fn-left"><img class="lazy" src="" data-url="<c:out value="${game.gameIcon}"/>" alt="<c:out value="${game.gameName}"/>" title="<c:out value="${game.gameName}"/>"></i>
                        <div class="fn-left">
                            <h4><c:out value="${game.gameName}"/><span></span></h4>
                           <%--  <p>开发商：<c:out value="${game.gameDeveloper}"/></p> --%>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></p>
                            <p>平台：<c:forEach items="${game.platformMap}" var="map">
                                <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                            </c:forEach></p>
                        </div>
                    </a>
                    <canvas class="process" width="168" height="168">${game.gameRate}</canvas>
                </li>
            </c:forEach>
        </ul>
        <h3>掌机游戏</h3>
        <ul>
            <c:forEach items="${pspList}" var="game" varStatus="st">
                <li>
                    <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>" class="border-b">
                        <i class="fn-left"><img class="lazy" src="" data-url="<c:out value="${game.gameIcon}"/>" alt="<c:out value="${game.gameName}"/>" title="<c:out value="${game.gameName}"/>"></i>
                        <div class="fn-left">
                            <h4><c:out value="${game.gameName}"/><span></span></h4>
                           <%--  <p>开发商：<c:out value="${game.gameDeveloper}"/></p> --%>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></p>
                            <p>平台：<c:forEach items="${game.platformMap}" var="map">
                                <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                            </c:forEach></p>
                        </div>
                    </a>
                    <canvas class="process" width="168" height="168">${game.gameRate}</canvas>
                </li>
            </c:forEach>
        </ul>
        <h3>电视游戏</h3>
        <ul>
            <c:forEach items="${tvList}" var="game" varStatus="st">
                <li>
                    <a href="<c:out value="${URL_M}/collection/${game.gameDbId}"/>" class="border-b">
                        <i class="fn-left"><img class="lazy" src="" data-url="<c:out value="${game.gameIcon}"/>" alt="<c:out value="${game.gameName}"/>" title="<c:out value="${game.gameName}"/>"></i>
                        <div class="fn-left">
                            <h4><c:out value="${game.gameName}"/><span></span></h4>
                            <%-- <p>开发商：<c:out value="${game.gameDeveloper}"/></p> --%>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></p>
                            <p>平台：<c:forEach items="${game.platformMap}" var="map">
                                <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                            </c:forEach></p>
                        </div>
                    </a>
                    <canvas class="process" width="168" height="168">${game.gameRate}</canvas>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<script>
    document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
</script>
<script src="http://static.joyme.com/mobile/cms/jmsy/js/lazyimg.js?${version}"></script>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/yxk/js/action.js"></script>

<script type="text/javascript">
    ;(function(){
        var mod={
            navFunc:function(){
                var navBtn=$('.mu-list');
                navBtn.each(function(){
                    var _this=$(this);
                    _this.on('click',function(e){
                        e.stopPropagation();
                        e.preventDefault();
                        if(!_this.hasClass('on')){
                            navBtn.removeClass('on');
                            _this.addClass('on');
                        }else{
                            _this.removeClass('on');
                        }
                    });
                });
            },
            searchFunc:function(){
                var searchBtn=$('.search-btn');
                var search=$('.search');
                searchBtn.on('click',function(){
                    $('.search-box').removeClass('hide');
                    if($('#search-text').val()==''){
                    	search.focus();
                    }
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
        mod.navFunc();
        $('.lazy').lazyImg();
    })();


    var host = window.location.host.substr(window.location.host.indexOf('.'));
    var mHost = 'http://m' + host + '/';
    var apiHost = 'http://api' + host + '/';
    var passportHost = 'http://passport' + host + "/";

    $(document).ready(function () {
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
        //平台
        $('li[name=genre_platformtype]').on('click', function(){
            var idx = $(this).attr('data-platformtype');
            var text = $(this).find('a').text();
            $('#h3-platform').text(text);
            $('ul[id^=platform_]').attr('style', 'display: none');
            $('#platform_'+idx).attr('style', '');
            var platform = $(this).attr('data-platformtype');
            if(platform == '' || platform == undefined){
                window.location.href = mHost + 'collection/genre';
            }else{
                window.location.href = mHost + 'collection/genre/p'+platform;
            }
        });
        //机型
        $('li[name = genre_platform]').on('click', function () {
            var text = $(this).find('a').text();
            $('#h3-device').text(text);
            var platform = $(this).attr('data-platform');
            if(platform == '' || platform == undefined){
                window.location.href = mHost + 'collection/genre';
            }else{
                window.location.href = mHost + 'collection/genre/p'+platform;
            }
        });
        //联网
        $('li[name = genre_net]').on('click', function () {
            var text = $(this).find('a').text();
            $('#h3-net').text(text);
            var net = $(this).attr('data-net');
            if(net == '' || net == undefined){
                window.location.href = mHost + "collection/genre";
            }else{
                window.location.href = mHost + "collection/genre/n" +net;
            }
        });
        //语言
        $('li[name = genre_language]').on('click', function () {
            var text = $(this).find('a').text();
            $('#h3-language').text(text);
            var language =  $(this).attr('data-language');
            if(language == '' || language == undefined){
                window.location.href = mHost + "collection/genre";
            }else{
                window.location.href = mHost + "collection/genre/l"+language;
            }
        });
        //类型
        $('li[name = genre_category]').on('click', function () {
            var text = $(this).find('a').text();
            $('#h3-category').text(text);
            var category = $(this).attr('data-category');
            if(category == '' || category == undefined){
                window.location.href = mHost + "collection/genre";
            }else{
                window.location.href = mHost + "collection/genre/c"+category;
            }
        });
        //题材
        $('li[name = genre_theme]').on('click', function () {
            var text = $(this).find('a').text();
            $('#h3-theme').text(text);
            var theme = $(this).attr('data-theme');
            if(theme == '' || theme == undefined){
                window.location.href = mHost + "collection/genre";
            }else{
                window.location.href = mHost + "collection/genre/t"+theme;
            }
        });
        //发布时间
        $('h3[name = genre_publishtime]').on('click', function () {
            var text = $(this).find('a').text();
            window.location.href = mHost + "collection/genre/s1";
        });
        //评分
        $('h3[name = genre_rate]').on('click', function () {
            var text = $(this).find('a').text();
            window.location.href = mHost + "collection/genre/s2";
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
