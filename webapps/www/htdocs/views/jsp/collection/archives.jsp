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
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <meta name="Keywords" content="${game.gameName},${game.gameName}安卓版下载,IOS版下载,ipad下载">
    <meta name="description"
          content="着迷网手机游戏库推荐好玩的手机游戏,提供手机游戏排行榜,并有安卓游戏及iPhone游戏免费下载地址,扫二维码直接下载,大家赶紧来到这里选择自己喜欢的游戏吧."/>
    <title>${game.gameName}_${game.gameName}安卓版_ios版_ipad下载_着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/header.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_STATIC}/pc/cms/jmsy/yxk/css/game-collection.css?${version}" rel="stylesheet" type="text/css"/>
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
<body>
<div id="joyme-wrapper">
    <!--header-->
    <c:import url="/views/jsp/passport/new-header.jsp"/>
    <!--header end-->
    <!--joyme标题-->
    <div class="joyme-bt fn-nav">
        <span><b>当前位置：</b><a href="<c:out value="${URL_WWW}"/>">着迷网</a> &gt; <a href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>"><c:out value="${game.gameName}"/></a> &gt; <c:if test="${contentType.code == 0}">资讯</c:if><c:if test="${contentType.code == 1}">视频</c:if><c:if test="${contentType.code == 4}">攻略</c:if></span>
    </div>
    <!--joyme标题end-->
    <!--joyme-center-->
    <div class="joyme-center joyme-center-list">
        <!--第四块-->
        <div class="joyme-box-four joyme-zzmj-box fn-clear">
            <!--joyme-zzmj-->
            <div class="joyme-video-list joyme-zzmj fn-left">
                <h2 class="joyme-title fn-clear">"<c:out value="${game.gameName}"/>"相关<c:if test="${contentType.code == 0}">资讯</c:if><c:if test="${contentType.code == 1}">视频</c:if><c:if test="${contentType.code == 4}">攻略</c:if></h2>
                <c:choose>
                    <c:when test="${contentType.code == 1}">
                        <div class="joyme-video-ul fn-clear">
                            <ul class="com-tab">
                                <c:forEach items="${list}" var="archive">
                                    <li><a href="<c:out value="${archive.dede_archives_url}"/>"><img class="lazy" data-url="<c:out value="${archive.dede_archives_litpic}"/>" src="" alt="<c:out value="${archive.dede_archives_title}"/>"><b class="video-begin"></b><span class="fn-hide"><c:out value="${archive.dede_archives_title}"/></span></a></li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="joyme-zxpd-dl games-zx-cont" id="get_archive_html">
                            <c:forEach items="${list}" var="archive">
                                <dl>
                                    <dt>
                                        <a href="<c:out value="${archive.dede_archives_url}"/>" target="_blank">
                                            <img src="" data-url="<c:out value="${archive.dede_archives_litpic}"/>" alt="<c:out value="${archive.dede_archives_title}"/>" class="lazy">
                                        </a>
                                    </dt>
                                    <dd>
                                        <div class="joyme-zxpd-dl-top">
                                            <h2><a href="<c:out value="${archive.dede_archives_url}"/>" target="_blank"><c:out value="${archive.dede_archives_title}"/></a>
                                            </h2>
                                        </div>
                                        <div>
                                            <span><c:out value="${archive.dede_archives_description}"/><a href="<c:out value="${archive.dede_archives_url}"/>" target="_blank">[详细]</a></span>
                                            <div class="joyme-zxpd-dl-num">
                                                <span><c:out value="${archive.dede_archives_pubdate_str}"/></span>
                                            </div>
                                        </div>
                                    </dd>
                                </dl>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="joyme-paging-box" id="get_page_html">
                    <%@ include file="/views/jsp/page/collection-page.jsp" %>
                </div>
            </div>
            <!--joyme-zzmj==end-->
            <!--右-->
            <div class="joyme-video-phb fn-left">
                <div class="games-rk">
                    <h2 class="joyme-title fn-clear">
                        <span class="fn-left">热门<b>攻略</b></span>
                    </h2>
                    <ul class="joyme-lbzx-box joyme-wzzz-box">
                        <c:forEach items="${guideList}" var="archive" varStatus="st">
                            <li class="<c:if test="${st.index == 0}">first-lb</c:if>">
                                <a target="_blank" href="<c:out value="${archive.dede_archives_url}"/>">
                                    <dl>
                                        <dt>
                                            <cite></cite>
                                            <span><c:out value="${archive.dede_archives_title}"/></span>
                                        </dt>
                                        <dd>
                                            <cite><img class="lazy" src="" width="70" height="70" data-url="<c:out value="${archive.dede_archives_litpic}"/>" alt="<c:out value="${archive.dede_archives_title}"/>"></cite>
                                            <p>
                                                <b><c:out value="${archive.dede_archives_title}"/></b>
                                            </p>
                                        </dd>
                                    </dl>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="games-rk t">
                    <h2 class="joyme-title fn-clear">
                        <span class="fn-left">热门<b>资讯</b></span>
                    </h2>
                    <ul class="joyme-lbzx-box joyme-wzzz-box">
                        <c:forEach items="${newsList}" var="archive" varStatus="st">
                            <li class="<c:if test="${st.index == 0}">first-lb</c:if>">
                                <a target="_blank" href="<c:out value="${archive.dede_archives_url}"/>">
                                    <dl>
                                        <dt>
                                            <cite></cite>
                                            <span><c:out value="${archive.dede_archives_title}"/></span>
                                        </dt>
                                        <dd>
                                            <cite><img class="lazy" src="" width="70" height="70" data-url="<c:out value="${archive.dede_archives_litpic}"/>" alt="<c:out value="${archive.dede_archives_title}"/>"></cite>
                                            <p>
                                                <b><c:out value="${archive.dede_archives_title}"/></b>
                                            </p>
                                        </dd>
                                    </dl>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="games-rmsp">
                    <h2 class="joyme-title fn-clear">
                        <span class="fn-left">热门<b>视频</b></span>
                    </h2>
                    <ul class="joyme-video-ul">
                        <c:forEach items="${videoList}" var="archive" varStatus="st">
                            <li><a href="<c:out value="${archive.dede_archives_url}"/>"><img class="lazy" src="" data-url="<c:out value="${archive.dede_archives_litpic}"/>" alt="<c:out value="${archive.dede_archives_title}"/>"><b class="video-begin"></b><span class="fn-hide"><c:out value="${archive.dede_archives_title}"/></span></a></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <!--右end-->
        </div>
        <!--第四块 end-->
    </div>
    <!-- footer Start -->
    <%@ include file="/views/jsp/tiles/new-footer.jsp" %>
    <!--返回顶部-->
    <div class="retuen-top" style="left: 1260.5px; display: none;">
        <span class="rt-btn">返回顶部</span>
    </div>
    <script src="${URL_STATIC}/pc/cms/jmsy/page/js/joyme-common.js?${version}"></script>
    <script type="text/javascript" src="${URL_STATIC}/pc/cms/jmsy/yxk/js/game-common.js?${version}"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $('li[name=li_page]').on('click', function () {
                var contentType = '${contentType.code}';
                var url = '${URL_WWW}/collection/${game.gameDbId}';
                if(contentType == '0'){
                    url += '/news';
                }else if(contentType == '1'){
                    url += '/videos';
                }else if(contentType == '4'){
                    url += '/guides';
                }
                var p = parseInt($(this).attr('data-page'));
                if(p != undefined && p != null && p!= ''){
                    window.location.href = url + "?p="+p;
                }
            });
        });
    </script>
</div>
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
