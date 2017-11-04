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
    <meta name="Keywords" content='<c:if test="${checkThemeType != null}"><c:out value="${checkThemeType.name}"/> </c:if><c:if test="${checkCategoryType != null}"><c:out value="${checkCategoryType.value}"/> </c:if><c:if test="${checkLanguageType != null}"><c:out value="${checkLanguageType.name}"/> </c:if><c:if test="${checkNetType != null}"><c:out value="${checkNetType.name}"/> </c:if><c:if test="${checkPlatformType != null}"><c:out value="${checkPlatformType.desc}"/> </c:if>类游戏下载'>
    <meta name="description"
          content="着迷网游戏库推荐好玩的游戏,其中包括手机游戏、电脑游戏，掌机游戏和电视游戏,大家赶紧来到这里选择自己喜欢的游戏吧。"/>
    <title><c:if test="${checkThemeType != null}"><c:out value="${checkThemeType.name}"/> </c:if><c:if test="${checkCategoryType != null}"><c:out value="${checkCategoryType.value}"/> </c:if><c:if test="${checkLanguageType != null}"><c:out value="${checkLanguageType.name}"/> </c:if><c:if test="${checkNetType != null}"><c:out value="${checkNetType.name}"/> </c:if><c:if test="${checkPlatformType != null}"><c:out value="${checkPlatformType.desc}"/> </c:if>类游戏下载_着迷网Joyme.com</title>
    <%-- <link href="${URL_STATIC}/mobile/cms/jmsy/css/common.css" rel="stylesheet" type="text/css"/> --%>
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
            <i><img src="${URL_STATIC}/mobile/cms/jmsy/images/logo-icon2.png" alt="游戏" title="游戏"></i>游戏
            </a>
        </h1>
        <div id="search-box" <c:if test="${isname}">class="search-box"</c:if><c:if test="${!isname}">class="search-box hide"</c:if>>
            <span><input id="search-text" type="text" value="<c:out value="${name}"/>" placeholder="搜索" class="search"></span>
        </div>
        <div class="search-bar">
            <span class="search-btn" id="search-btn"></span>
        </div>
    </div>
</header>
<div id="wrapper">
        <nav>
            <div class="menu-box border-b">
                <div class="mu-list">
                    <h3 id="h3-platform">平台</h3>
                    <ul>
                        <li name="genre_platformtype" <c:choose><c:when test="${platformTypeCode == null || platformTypeCode == ''}">class="border-b active"</c:when><c:otherwise>class="border-b"</c:otherwise></c:choose> data-platformtype=""><a <c:if test="${platformTypeCode == null || platformTypeCode == ''}">class="on"</c:if> href="javascript:void (0)">全部</a></li>
                        <c:forEach items="${platformTypeSet}" var="platformType">
                            <li name="genre_platformtype" <c:choose><c:when test="${platformType.code == platformTypeCode}">class="border-b active"</c:when><c:otherwise>class="border-b"</c:otherwise></c:choose> data-code="<c:out value="${platformType.code}"/>" data-platformtype="<c:out value="${platformType.code}-"/>"><a <c:if test="${platformType.code == platformTypeCode}">class="on"</c:if> href="javascript:void (0)"><c:out value="${platformType.desc}"/></a></li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="mu-list">
                    <h3 id="h3-category">类型</h3>
                    <ul>
                        <li name="genre_category" <c:if test="${categoryCode == null || categoryCode == ''}"> class="border-b active"</c:if> data-category=""><a <c:if test="${categoryCode == null || categoryCode == ''}"> class="on"</c:if> href="javascript:void(0);">全部</a></li>
                        <c:forEach items="${categorySet}" var="category">
                            <li name="genre_category"  <c:choose><c:when test="${categoryCode == category.code}"> class="border-b active"</c:when><c:otherwise>class="border-b"</c:otherwise></c:choose> data-category="<c:out value="${category.code}"/>"><a <c:if test="${categoryCode == category.code}"> class="on"</c:if> href="javascript:void(0);"><c:out value="${category.value}"/></a></li>
                        </c:forEach>
                    </ul>
                </div>  
                <div id="genre_publishtime" data-sort="1" class="mu-list <c:if test="${sort==1}">on</c:if>">
                    <h3 id="h3-publishtime" name="genre_publishtime"><a href="javascript:void(0)">时间</a></h3>          
                </div>
                <div id="genre_rate" data-sort="1" class="mu-list <c:if test="${sort==2}">on</c:if>">
                    <h3 id="h3-rate" name="genre_rate" ><a href="javascript:void(0)">评分</a></h3>
                </div>                               
                <%-- <div class="mu-list">
                    <h3 id="h3-device">机型</h3>
                    <c:forEach items="${platformMap}" var="map" varStatus="st">
                        <ul id="platform_<c:out value="${map.key}"/>" <c:if test="${platformTypeCode == null || platformTypeCode == '' || map.key != ''+platformTypeCode}">style="display: none" </c:if>>
                            <c:forEach items="${platformMap[map.key]}" var="platform">
                                <li name="genre_platform" <c:if test="${map.key == ('' + platformTypeCode) && platform.code == platformCode}"> class="active"</c:if> data-code="<c:out value="${platform.code}"/>" data-platform="<c:out value="${map.key}-${platform.code}"/>"><a <c:if test="${map.key == ('' + platformTypeCode) && platform.code == platformCode}"> class="on"</c:if> href="javascript:void (0)"><c:out value="${platform.desc}"/></a></li>
                            </c:forEach>
                        </ul>
                    </c:forEach>
                </div>
                <div class="mu-list">
                    <h3 id="h3-net">联网</h3>
                    <ul>
                        <li name="genre_net" <c:if test="${netCode == null || netCode == ''}"> class="active"</c:if> data-net=""><a <c:if test="${netCode == null || netCode == ''}"> class="on"</c:if> href="javascript:void (0)">全部</a></li>
                        <c:forEach items="${netTypeSet}" var="net">
                            <li name="genre_net" <c:if test="${netCode == net.code}"> class="active"</c:if> data-net="<c:out value="${net.code}"/>"><a <c:if test="${netCode == net.code}"> class="on"</c:if> href="javascript:void (0)"><c:out value="${net.name}"/></a></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="menu-box border-b">
                <div class="mu-list">
                    <h3 id="h3-language">语言</h3>
                    <ul>
                        <li name="genre_language" <c:if test="${languageCode == null || languageCode == ''}"> class="active"</c:if> data-language=""><a <c:if test="${languageCode == null || languageCode == ''}"> class="on"</c:if> href="javascript:void(0);">全部</a></li>
                        <c:forEach items="${languageTypeSet}" var="language">
                            <li name="genre_language" <c:if test="${languageCode == language.code}"> class="active"</c:if> data-language="<c:out value="${language.code}"/>"><a <c:if test="${languageCode == language.code}"> class="on"</c:if> href="javascript:void(0);"><c:out value="${language.name}"/></a></li>
                        </c:forEach>
                    </ul>
                </div> --%>

<%--                 <div class="mu-list">
                    <h3 id="h3-theme">题材</h3>
                    <ul>
                        <li name="genre_theme" <c:if test="${themeCode == null || themeCode == ''}">class="active"</c:if> data-theme=""><a <c:if test="${themeCode == null || themeCode == ''}"> class="on"</c:if> href="javascript:void(0);">全部</a></li>
                        <c:forEach items="${themeTypeSet}" var="theme">
                            <li name="genre_theme" <c:if test="${themeCode == theme.code}"> class="active"</c:if> data-theme="<c:out value="${theme.code}"/>"><a <c:if test="${themeCode == theme.code}"> class="on"</c:if> href="javascript:void(0);"><c:out value="${theme.name}"/></a></li>
                        </c:forEach>
                    </ul>
                </div> --%>

            </div>
        </nav>
    <div class="game-list fn-ovf">
        <h3><font><c:out value="${page.totalRows > 0 ? page.totalRows : 0}"/></font>条搜索结果</h3>
        <ul id="game-list">
            <c:if test="${list != null && list.size() > 0}">
                <c:forEach items="${list}" var="game">
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
            </c:if>
        </ul>
        <c:if test="${page != null && page.curPage < page.maxPage}">
            <div class="more-load" id="moreArticle">
                <a href="javascript:void(0);">点击加载更多...</a>
            </div>
        </c:if>
    </div>
</div>
<script>
    document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
</script>
<script src="${URL_STATIC}/mobile/cms/jmsy/js/lazyimg.js"></script>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/yxk/js/action.js"></script>
<script type="text/javascript">
    ;(function(){
        var wh=$(window).height();
        var hh=$('header').innerHeight();
        var sH=wh-hh;
        $('#wrapper').css('min-height',sH);

        var mod={
            navFunc:function(){
                var navBtn=$('.mu-list');
                navBtn.each(function(){
                    var _this=$(this);
                    _this.on('click',function(e){
                        e.stopPropagation();
                        e.preventDefault();
                        if( _this.attr('data-sort')!=1){
                            if(!_this.hasClass('on')){
                                navBtn.removeClass('on');
                                _this.addClass('on');
                            }else{
                                _this.removeClass('on');
                            }
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
    var cp = 1;
    var block = false;
    $(document).ready(function () {
        var h3platform = $('li[name = genre_platformtype].active').find('a').text();
        if(h3platform != '' && h3platform != undefined && h3platform != '全部'){
            $('#h3-platform').text(h3platform);
        }
        var h3device = $('li[name = genre_platform].active').find('a').text();
        if(h3device != '' && h3device != undefined && h3device != '全部'){
            $('#h3-device').text(h3device);
        }
        var h3net = $('li[name = genre_net].active').find('a').text();
        if(h3net != '' && h3net != undefined && h3net != '全部'){
            $('#h3-net').text(h3net);
        }
        var h3language = $('li[name = genre_language].active').find('a').text();
        if(h3language != '' && h3language != undefined && h3language != '全部'){
            $('#h3-language').text(h3language);
        }
        var h3category = $('li[name = genre_category].active').find('a').text();
        if(h3category != '' && h3category != undefined && h3category != '全部'){
            $('#h3-category').text(h3category);
        }
        var h3theme = $('li[name = genre_theme].active').find('a').text();
        if(h3theme != '' && h3theme != undefined && h3theme != '全部'){
            $('#h3-theme').text(h3theme);
        }

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

        $('li[name=genre_platformtype]').on('click', function(){
            var idx = $(this).attr('data-platformtype');
            $('ul[id^=platform_]').attr('style', 'display: none');
            $('#platform_'+idx).attr('style', '');
            $('li[name = genre_platformtype].active').removeClass('active');
            $('li[name = genre_platform].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $(this).attr('data-platformtype');
            var platform = $('li[name = genre_platform].active').attr('data-platform');
            var net = $('li[name = genre_net].active').attr('data-net');
            var language = $('li[name = genre_language].active').attr('data-language');
            var category = $('li[name = genre_category].active').attr('data-category');
            var theme = $('li[name = genre_theme].active').attr('data-theme');
            var sort = $('li[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = mHost + "collection/genre/"+param;
        });

        //平台
        $('li[name = genre_platform]').on('click', function () {
            $('li[name = genre_platform].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var platform = $(this).attr('data-platform');
            var net = $('li[name = genre_net].active').attr('data-net');
            var language = $('li[name = genre_language].active').attr('data-language');
            var category = $('li[name = genre_category].active').attr('data-category');
            var theme = $('li[name = genre_theme].active').attr('data-theme');
            var sort = $('li[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = mHost + "collection/genre/"+param;
        });
        //联网
        $('li[name = genre_net]').on('click', function () {
            $('li[name = genre_net].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var platform = $('li[name = genre_platform].active').attr('data-platform');
            var net = $(this).attr('data-net');
            var language = $('li[name = genre_language].active').attr('data-language');
            var category = $('li[name = genre_category].active').attr('data-category');
            var theme = $('li[name = genre_theme].active').attr('data-theme');
            var sort = $('li[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = mHost + "collection/genre/"+param;
        });
        //语言
        $('li[name = genre_language]').on('click', function () {
            $('li[name = genre_language].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var platform = $('li[name = genre_platform].active').attr('data-platform');
            var net = $('li[name = genre_net].active').attr('data-net');
            var language = $(this).attr('data-language');
            var category = $('li[name = genre_category].active').attr('data-category');
            var theme = $('li[name = genre_theme].active').attr('data-theme');
            var sort = $('li[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = mHost + "collection/genre/"+param;
        });
        //类型
        $('li[name = genre_category]').on('click', function () {
            $('li[name = genre_category].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var platform = $('li[name = genre_platform].active').attr('data-platform');
            var net = $('li[name = genre_net].active').attr('data-net');
            var language = $('li[name = genre_language].active').attr('data-language');
            var category = $(this).attr('data-category');
            var theme = $('li[name = genre_theme].active').attr('data-theme');
            var sort = $('li[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = mHost + "collection/genre/"+param;
        });
        //题材
        $('li[name = genre_theme]').on('click', function () {
            $('li[name = genre_theme].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var platform = $('li[name = genre_platform].active').attr('data-platform');
            var net = $('li[name = genre_net].active').attr('data-net');
            var language = $('li[name = genre_language].active').attr('data-language');
            var category = $('li[name = genre_category].active').attr('data-category');
            var theme = $(this).attr('data-theme');
            var sort = $('li[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = mHost + "collection/genre/"+param;
        });
        
        //发布时间
        $('h3[name = genre_publishtime]').on('click', function () {
            //var text = $(this).find('a').text();
            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var category = $('li[name = genre_category].active').attr('data-category');
            var name = $('#search-text').val();
            if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }

            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(name != '' && name != undefined){
                param += ("name:"+name+"_");
            }
            param += ("s1_page:"+1);
            if($('#genre_publishtime').hasClass('on')==false){
            	window.location.href = mHost + "collection/genre/"+param;
            }
            
        });
        //评分
        $('h3[name = genre_rate]').on('click', function () {
            var text = $(this).find('a').text();
            var param = "";
            var platformType = $('li[name = genre_platformtype].active').attr('data-platformtype');
            var category = $('li[name = genre_category].active').attr('data-category');

            var name = $('#search-text').val();
            if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }

            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            
            if(name != '' && name != undefined){
                param += ("name:"+name+"_");
            }
            
            param += ("s2_page:"+1);
            if($('#genre_rate').hasClass('on')==false){
            	window.location.href = mHost + "collection/genre/"+param;
            }
        });
        

        $('#moreArticle').on('click', function(){
            if (block) {
                return;
            }
            block = true;
            var name = $('#search-name').val();
            if(name != undefined){
                name = name.replace(/^\s+|\s+$/g, '');
            }else{
                name = '';
            }
            var platformType = $('li[name = genre_platformtype].active').attr('data-code');
            if(platformType == undefined){
                platformType = '';
            }
            var platform = $('li[name = genre_platform].active').attr('data-code');
            if(platform == undefined){
                platform = '';
            }
            var net = $('li[name = genre_net].active').attr('data-net');
            if(net == undefined){
                net = '';
            }
            var language = $('li[name = genre_language].active').attr('data-language');
            if(language == undefined){
                language = '';
            }
            var category = $('li[name = genre_category].active').attr('data-category');
            if(category == undefined){
                category = '';
            }
            var theme = $('li[name = genre_theme].active').attr('data-theme');
            if(theme == undefined){
                theme = '';
            }
            cp += 1;
            $.ajax({
                url: apiHost + "collection/api/gamelist",
                type: "post",
                data: {name: name, platformtype:platformType, platform:platform, nettype:net, languagetype:language, category:category, themetype:theme, p:cp},
                dataType: "jsonp",
                jsonpCallback: "gamelistcallback",
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
                                    var categoryHtml = '';
                                    for(var j=0;j<rows[i].categoryTypeSet.length;j++){
                                        categoryHtml += (rows[i].categoryTypeSet[j].value + ' ');
                                    }
                                    var platformHtml = '';
                                    $.each(rows[i].platformMap,function(key,values){
                                        $(values).each(function(){
                                            platformHtml += (this.desc + ' ');
                                        });
                                    });
                                    html += '<li>' +
                                    '<a href="'+mHost + '/collection/'+rows[i].gameDbId+'" class="border-b">'+
                                    '<i class="fn-left">' +
                                    '<img class="lazy" src="" data-url="'+rows[i].gameIcon+'" alt="'+rows[i].gameName+'" title="'+rows[i].gameName+'"/>' +
                                    '</i>' +
                                    '<div class="fn-left"><h4>'+rows[i].gameName+'<span></span></h4>' +
                                    '<p>类型：'+categoryHtml+'</p>' +
                                    '<p>平台：'+platformHtml+'</p>' +
                                    '</div>' +
                                    '</a>' +
                                    '<canvas class="process" width="168" height="168">'+rows[i].gameRate+'</canvas>'+
                                    '</li>';
                                }
                                $('#game-list').append(html);
                                $('.lazy').lazyImg();
                                if(page.curPage >= page.maxPage){
                                    $('#moreArticle').hide();
                                }
                                canvasFn();
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
