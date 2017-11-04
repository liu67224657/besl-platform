<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device"content="pc">
    <meta name="mobile-agent"content="format=xhtml;url=http://m.joyme.com/">
    <meta name="mobile-agent" content="format=html5;url=http://m.joyme.com">

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="安卓游戏免费下载,iPhone游戏免费下载,免费手机游戏">
    <meta name="description"
          content="着迷网手机游戏库推荐好玩的手机游戏,提供手机游戏排行榜,并有安卓游戏及iPhone游戏免费下载地址,扫二维码直接下载,大家赶紧来到这里选择自己喜欢的游戏吧."/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>安卓游戏免费下载_iPhone游戏免费下载_免费手机游戏_着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/header.css?${version}" rel="stylesheet" type="text/css"/>     
    <link href="${URL_STATIC}/pc/cms/jmsy/yxk/css/newgame-collection.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script>
        var bs = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    WPhone: u.indexOf('Windows Phone') > -1,//windows phone
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }


        var domain = window.location.hostname;
        var env = domain.substring(domain.lastIndexOf('.'), domain.length);
        var jumpUrl = 'http://m.joyme'+env+window.location.pathname;

        if (bs.versions.mobile) {
            if (bs.versions.android || bs.versions.iPhone || bs.versions.iPad || bs.versions.ios || bs.versions.WPhone) {
                if(window.location.hash.indexOf('wappc') <= 0){
                    var flag = getCookie('jumpflag');
                    if(flag == '' || flag == null || flag == undefined){
                        window.location.href = jumpUrl;
                    }
                }else{
                    var timeOutDate = new Date();
                    timeOutDate.setTime(timeOutDate.getTime() + (24*60*60*1000));
                    setCookie('jumpflag', 'wappc', timeOutDate, env);
                }
            }
        }

        function getCookie(objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName && temp[1] != '\'\'' && temp[1] != "\"\"") {
                    return unescape(temp[1]);
                }
            }
            return null;
        }

        function setCookie(key, value, exDate, env) {
            var cookie = "";
            if (!!key)
                cookie += key + "=" + escape(value) + ";path=/;domain=.joyme"+env+";expires="+exDate.toUTCString();
            document.cookie = cookie;
        }
    </script>
</head>
<body style="background: none;">
<!-- header -->
<c:import url="/views/jsp/passport/new-header.jsp"/>
<!--header==end-->
<!--body-->
<div id="joyme-wrapper">
    <div class="joyme-bt fn-nav">
        <span><b>当前位置：</b><a href="<c:out value="${URL_WWW}"/>">着迷网</a> &gt; <a href="<c:out value="${URL_WWW}/collection"/>">游戏库</a></span>
    </div>
    <!--joyme-center-->
    <div class="joyme-center fn-clear joyme-center-list">
        <!--games-l-->
        <div class="games-c">
            <!--游戏分类-->
            <div class="games-fl">
                <h2 class="joyme-title fn-clear">
                    <span class="fn-left">游戏分类</span>
                </h2>
                <div class="games-fl-list">
                    <dl>
                        <dt>平台：</dt>
                        <dd>
                            <a href="javascript:void(0);" id="genre_allplatform" class="active">不限</a>
                            <c:forEach items="${platformTypeSet}" var="platformType" varStatus="st">
                                <span class="terrace-btn" data-tab="<c:out value="${st.index + 1}"/>"
                                      name="genre_platformtype" data-platformtype="<c:out value="${platformType.code}-"/>"><c:out value="${platformType.desc}"/><em></em>
                                </span>
                            </c:forEach>
                            <div class="terrace-box">
                                <c:forEach items="${platformTypeSet}" varStatus="st" var="platformType">
                                    <c:set var="platformTypeIndex" ><c:out value="${platformType.code}"/></c:set>
                                    <c:if test="${platformMap[platformTypeIndex] != null}">
                                        <p class="terrace" id="data-tab${st.index + 1}">
                                            <c:forEach items="${platformMap[platformTypeIndex]}" var="platform">
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-${platform.code}"/>">
                                                    <c:out value="${platform.desc}"/>   
                                                </a>
                                            </c:forEach>
                                        </p>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt>联网：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_net" class="active" data-net="">不限</a>
                            <c:forEach items="${netTypeSet}" var="net">
                                <a href="javascript:void(0);" name="genre_net" data-net="${net.code}">${net.name}</a>
                            </c:forEach>
                        </dd>
                    </dl>
                    <dl>
                        <dt>语言：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_language" class="active" data-language="">不限</a>
                            <c:forEach items="${languageTypeSet}" var="language">
                                <a href="javascript:void(0);" name="genre_language"
                                   data-language="${language.code}">${language.name}</a>
                            </c:forEach>
                        </dd>
                    </dl>
                    <dl class="type">
                        <dt>类型：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_category" class="active" data-category="">不限</a>
                            <c:forEach items="${categorySet}" var="category" varStatus="c">
                                <a href="javascript:void(0);" name="genre_category"
                                   data-category="${category.code}">${category.value}</a>
                            </c:forEach>                         
                        </dd>
                      <!--   <span class="type-more">更多<i></i></span> -->
                    </dl>
                    <dl>
                        <dt>题材：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_theme" class="active" data-theme="">不限</a>
                            <c:forEach items="${themeTypeSet}" var="theme">
                                <a href="javascript:void(0);" name="genre_theme"
                                   data-theme="${theme.code}">${theme.name}</a>
                            </c:forEach>
                        </dd>
                    </dl>
                    <dl class="search-box">
                        <dt>搜索：</dt>
                        <dd>
                            <p><input type="text" id="search-name" value="请输入要查找的游戏名称"><span class="search-btn" id="search-btn"></span></p>
                        </dd>
                    </dl>
                </div>
            </div>
            <!--游戏分类==end-->
            <!--着迷推荐-->
            <div class="games-rm fn-clear">
                <div class="games-tj-top">
                    <div class="joyme-tj fn-clear w-1048">
                        <h2 class="joyme-title fn-clear">
                            <span class="fn-left">着迷<b>推荐</b></span>
                            <c:if test="${recommendList.size()>=3}"><a target="_blank" href="${URL_WWW}/collection/genre?title=着迷推荐" class="joyme-title-more fn-right">更多&gt;&gt;</a> </c:if>
                        </h2>
                    
                        <c:forEach items="${recommendList}" var="game" varStatus="st">
                            <dl class="fn-clear">
                                <dt>
                                    <a href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" target="_blank">
                                        <img src="" alt="<c:out value="${game.gameName}"/>" height="120" width="120" data-url="<c:out value="${game.gameIcon}"/>" class="lazy">
                                        <i <c:if test="${game.gameRate<1}">class="max1"</c:if>
                                           <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="max2"</c:if>
                                           <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="max3"</c:if>
                                           <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="max4"</c:if>
                                           <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="max5"</c:if>
                                           <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="max6"</c:if>
                                           <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="max7"</c:if>
                                           <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="max8"</c:if>
                                           <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="max9"</c:if>
                                           <c:if test="${game.gameRate==10}">class="max10"</c:if>><c:out value="${game.gameRate}"/>
                                       </i>                                              
                                    </a>
                                </dt>
                                <dd>
                                    <h3><a href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" target="_blank"><c:out value="${game.gameName}"/></a></h3>
                                    <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></span>
                                    <span>平台：
                                        <c:forEach items="${game.platformMap}" var="map">
                                            <c:forEach items="${game.platformMap[map.key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                                        </c:forEach>
                                    </span>
                                    <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                    <p>
                                        <c:if test="${game.giftSum > 0}">
                                            <a href="<c:out value="${URL_WWW}/giftmarket"/>" class="games-tj-lb" target="_blank">礼包</a>
                                        </c:if>
                                        <c:if test="${fn:length(game.wikiUrl) > 0}">
                                            <a href="<c:out value="${game.wikiUrl}"/>" class="games-tj-wiki" target="_blank">WIKI</a>
                                        </c:if>
                                        <a target="_blank" href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon">进入&gt;</a> 
                                    </p>
                                </dd>
                            </dl>
                        </c:forEach>
                    </div>
                    
                    <!--着迷推荐 end-->
                    <!--最新入库-->
                    <div class="joyme-news fn-clear w-1048">
                        <h2 class="joyme-title fn-clear">
                            <span class="fn-left">最新<b>入库</b></span>
                            <c:if test="${newList.size()>=3}"><a target="_blank" href="${URL_WWW}/collection/genre?title=最新入库" class="joyme-title-more fn-right">更多&gt;</a> </c:if>
                        </h2>
                        <c:forEach items="${newList}" var="game" varStatus="st">
                            <dl class="fn-clear">
                                <dt>
                                    <a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">
                                        <img src="" alt="${game.gameIcon}" height="120" width="120" data-url="${game.gameIcon}" class="lazy">
                                        <i <c:if test="${game.gameRate<1}">class="max1"</c:if>
                                           <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="max2"</c:if>
                                           <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="max3"</c:if>
                                           <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="max4"</c:if>
                                           <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="max5"</c:if>
                                           <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="max6"</c:if>
                                           <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="max7"</c:if>
                                           <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="max8"</c:if>
                                           <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="max9"</c:if>
                                           <c:if test="${game.gameRate==10}">class="max10"</c:if>><c:out value="${game.gameRate}"/>
                                       </i>  
                                   </a>
                            </dt>
                            <dd>
                                <h3><a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">${game.gameName}</a></h3>
                                <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st">${category.value}&nbsp;</c:forEach></span>
                                <span>平台：<c:forEach items="${game.platformMap}" var="map">
                                    <c:forEach items="${game.platformMap[map.key]}" var="platform">${platform.desc}&nbsp;</c:forEach>
                                </c:forEach>
                                </span>
                                <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                <p>
                                    <c:if test="${game.giftSum > 0}">
                                        <a href="${URL_WWW}/giftmarket" class="games-tj-lb" target="_blank">礼包</a>
                                    </c:if>
                                    <c:if test="${fn:length(game.wikiUrl) > 0}">
                                        <a href="${game.wikiUrl}" class="games-tj-wiki" target="_blank">WIKI</a>
                                    </c:if>
                                    <a target="_blank" href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon">进入&gt;</a> 
                                </p>
                            </dd>
                        </dl>
                    </c:forEach>
                </div>
                <!--最新入库 end-->
                <!--手机游戏-->
                <div class="joyme-wap fn-clear w-1048">
                    <h2 class="joyme-title fn-clear">
                        <span class="fn-left">手机<b>游戏</b></span>
                        <c:if test="${mobileList.size()>=3}"><a target="_blank" href="${URL_WWW}/collection/genre/p1-_page:1" class="joyme-title-more fn-right">更多&gt;</a> </c:if>
                    </h2>
                        <c:forEach items="${mobileList}" var="game" varStatus="st">
                            <dl class="fn-clear">
                                <dt>
                                    <a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">
                                        <img src="" alt="${game.gameIcon}" height="120" width="120" data-url="${game.gameIcon}" class="lazy">
                                        <i <c:if test="${game.gameRate<1}">class="max1"</c:if>
                                           <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="max2"</c:if>
                                           <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="max3"</c:if>
                                           <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="max4"</c:if>
                                           <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="max5"</c:if>
                                           <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="max6"</c:if>
                                           <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="max7"</c:if>
                                           <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="max8"</c:if>
                                           <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="max9"</c:if>
                                           <c:if test="${game.gameRate==10}">class="max10"</c:if>><c:out value="${game.gameRate}"/>
                                       </i> 
                                    </a>
                                 </dt>
                                <dd>
                                    <h3><a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">${game.gameName}</a></h3>
                                    <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st">${category.value}&nbsp;</c:forEach></span>
                                    <span>平台：<c:forEach items="${game.platformMap}" var="map">
                                        <c:forEach items="${game.platformMap[map.key]}" var="platform">${platform.desc}&nbsp;</c:forEach>
                                        </c:forEach>
                                    </span>
                                    <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                    <p>
                                        <c:if test="${game.giftSum > 0}">
                                            <a href="${URL_WWW}/giftmarket" class="games-tj-lb" target="_blank">礼包</a>
                                        </c:if>
                                        <c:if test="${fn:length(game.wikiUrl) > 0}">
                                            <a href="${game.wikiUrl}" class="games-tj-wiki" target="_blank">WIKI</a>
                                        </c:if>
                                        <a target="_blank" href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon">进入&gt;</a> 
                                    </p>
                                </dd>
                            </dl>
                        </c:forEach>
                    </div>
            <!--手机游戏 end-->
            <!--电脑游戏-->
            <div class="joyme-pc fn-clear w-1048">
                <h2 class="joyme-title fn-clear">
                    <span class="fn-left">电脑<b>游戏</b></span>
                    <c:if test="${pcList.size() >= 3}"><a target="_blank" href="${URL_WWW}/collection/genre/p2-_page:1" class="joyme-title-more fn-right">更多&gt;</a> </c:if>                  
                </h2>
                    <c:forEach items="${pcList}" var="game" varStatus="st">
                        <dl class="fn-clear">
                            <dt>
                                <a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">
                                    <img src="" alt="${game.gameIcon}" height="120" width="120" data-url="${game.gameIcon}" class="lazy">
                                        <i <c:if test="${game.gameRate<1}">class="max1"</c:if>
                                           <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="max2"</c:if>
                                           <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="max3"</c:if>
                                           <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="max4"</c:if>
                                           <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="max5"</c:if>
                                           <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="max6"</c:if>
                                           <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="max7"</c:if>
                                           <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="max8"</c:if>
                                           <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="max9"</c:if>
                                           <c:if test="${game.gameRate==10}">class="max10"</c:if>><c:out value="${game.gameRate}"/>
                                   </i> 
                                </a>
                            </dt>
                            <dd>
                                <h3><a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">${game.gameName}</a></h3>
                                <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st">${category.value}&nbsp;</c:forEach></span>
                                <span>平台：<c:forEach items="${game.platformMap}" var="map">
                                    <c:forEach items="${game.platformMap[map.key]}" var="platform">${platform.desc}&nbsp;</c:forEach>
                                </c:forEach>
                                </span>
                                <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                <p>
                                    <c:if test="${game.giftSum > 0}">
                                        <a href="${URL_WWW}/giftmarket" class="games-tj-lb" target="_blank">礼包</a>
                                    </c:if>
                                    <c:if test="${fn:length(game.wikiUrl) > 0}">
                                        <a href="${game.wikiUrl}" class="games-tj-wiki" target="_blank">WIKI</a>
                                    </c:if>
                                    <a target="_blank" href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon">进入&gt;</a> 
                                </p>
                            </dd>
                        </dl>
                    </c:forEach>
                </div>
            <!--电脑游戏 end-->
            <!--掌机游戏-->
            <div class="joyme-pc fn-clear w-1048">
                <h2 class="joyme-title fn-clear">
                    <span class="fn-left">掌机<b>游戏</b></span>
                    <c:if test="${pspList.size()>=3}"><a target="_blank" href="${URL_WWW}/collection/genre/p3-_page:1" class="joyme-title-more fn-right">更多&gt;&gt;</a></c:if>                   
                </h2>
                    <c:forEach items="${pspList}" var="game" varStatus="st">
                        <dl class="fn-clear">
                            <dt>
                                <a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">
                                    <img src="" alt="${game.gameIcon}" height="120" width="120" data-url="${game.gameIcon}" class="lazy">
                                        <i <c:if test="${game.gameRate<1}">class="max1"</c:if>
                                           <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="max2"</c:if>
                                           <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="max3"</c:if>
                                           <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="max4"</c:if>
                                           <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="max5"</c:if>
                                           <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="max6"</c:if>
                                           <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="max7"</c:if>
                                           <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="max8"</c:if>
                                           <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="max9"</c:if>
                                           <c:if test="${game.gameRate==10}">class="max10"</c:if>><c:out value="${game.gameRate}"/>
                                   </i> 
                                </a>
                            </dt>
                            <dd>
                                <h3><a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">${game.gameName}</a></h3>
                                <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st">${category.value}&nbsp;</c:forEach></span>
                                <span>平台：<c:forEach items="${game.platformMap}" var="map">
                                    <c:forEach items="${game.platformMap[map.key]}" var="platform">${platform.desc}&nbsp;</c:forEach>
                                </c:forEach>
                                </span>
                                <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                <p>
                                    <c:if test="${game.giftSum > 0}">
                                        <a href="${URL_WWW}/giftmarket" class="games-tj-lb" target="_blank">礼包</a>
                                    </c:if>
                                    <c:if test="${fn:length(game.wikiUrl) > 0}">
                                        <a href="${game.wikiUrl}" class="games-tj-wiki" target="_blank">WIKI</a>
                                    </c:if>
                                    <a target="_blank" href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon">进入&gt;</a> 
                                </p>
                            </dd>
                        </dl>
                    </c:forEach>
            </div>
            <!--掌机游戏 end-->
            <!--电视游戏-->
            <div class="joyme-pc fn-clear w-1048">
                <h2 class="joyme-title fn-clear">
                    <span class="fn-left">电视<b>游戏</b></span>
                    <c:if test="${tvList.size()>=3}"><a target="_blank" href="${URL_WWW}/collection/genre/p4-_page:1" class="joyme-title-more fn-right">更多&gt;&gt;</a></c:if>                 
                </h2>
                    <c:forEach items="${tvList}" var="game" varStatus="st">
                        <dl class="fn-clear">
                            <dt>
                                <a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">
                                    <img src="" alt="${game.gameIcon}" height="120" width="120" data-url="${game.gameIcon}" class="lazy">
                                        <i <c:if test="${game.gameRate<1}">class="max1"</c:if>
                                           <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="max2"</c:if>
                                           <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="max3"</c:if>
                                           <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="max4"</c:if>
                                           <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="max5"</c:if>
                                           <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="max6"</c:if>
                                           <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="max7"</c:if>
                                           <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="max8"</c:if>
                                           <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="max9"</c:if>
                                           <c:if test="${game.gameRate==10}">class="max10"</c:if>><c:out value="${game.gameRate}"/>
                                   </i> 
                                </a>
                            </dt>
                            <dd>
                                <h3><a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">${game.gameName}</a></h3>
                                <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st">${category.value}&nbsp;</c:forEach></span>
                                <span>平台：<c:forEach items="${game.platformMap}" var="map">
                                    <c:forEach items="${game.platformMap[map.key]}" var="platform">${platform.desc}&nbsp;</c:forEach>
                                </c:forEach></span>
                                <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                <p>
                                    <c:if test="${game.giftSum > 0}">
                                        <a href="${URL_WWW}/giftmarket" class="games-tj-lb" target="_blank">礼包</a>
                                    </c:if>
                                    <c:if test="${fn:length(game.wikiUrl) > 0}">
                                        <a href="${game.wikiUrl}" class="games-tj-wiki" target="_blank">WIKI</a>
                                    </c:if>
                                    <a target="_blank" href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon">进入&gt;</a> 
                                </p>
                                 
                            </dd>
                        </dl>
                    </c:forEach>
                </div>
            </div>
            </div>
            <!--电视游戏 end-->
        </div>
        <!--games-l==end-->
    </div>
    <!--joyme-center end-->
</div>
<!--body-->
<!-- footer -->
<%@ include file="/views/jsp/tiles/new-footer.jsp" %>
<!-- footer end-->
<div class="retuen-top" style="left: 1270.5px; display: none;">
    <span class="rt-btn">返回顶部</span>
</div>
<script type="text/javascript" src="${URL_STATIC}/pc/cms/jmsy/yxk/js/game-common.js?${version}"></script>
<script type="text/javascript">
    var btns=$('.terrace-box');
    btns.on('click',function(event){
        event.stopPropagation();
        btns.children('p').hide();
        $(this).children('p').show();
    });
    $(document).on('click',function(){
        btns.children('p').hide();
    })

    function focusInput(config) {
        var inp=$(config);
        var val=inp.val();
        inp.on('focus',function(){
            var vals=$(this).val();
            if(vals==val){
                $(this).val('');
            }else{
                $(this).val(vals);
            }
        });
        inp.on('blur',function(){
            var vals=$(this).val();
            if(vals==''){
                $(this).val(val);
            }else{
                $(this).val(vals);
            }
        });
    };
    focusInput('#search-name');
    
    function listMore(){
        var Omore = $('.type-more'),
            Otype = $('.type'),
            OtypeChildH = Otype.find('dd').height();

        Omore.click(function(){
            if($(this).hasClass('on')){
                $(this).removeClass('on');
                Otype.css({'height':'30px'});
            }else{
                $(this).addClass('on');
                Otype.css({'height':OtypeChildH});
            }
        });

    }
    listMore();
</script>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/game-collection-init.js');
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>

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
