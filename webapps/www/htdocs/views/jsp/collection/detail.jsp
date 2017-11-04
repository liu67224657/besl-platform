<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="pc">
    <meta name="mobile-agent" content="format=xhtml;url=http://m.joyme.com/">
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
    <link rel="stylesheet" href="${URL_STATIC}/css/swiper-3.3.1.min.css?${version}"/>
    <link href="${URL_STATIC}/pc/cms/jmsy/yxk/css/newgame-collection.css?${version}" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <%--     <script type="text/javascript" src="${URL_STATIC}/js/swiper.min.js?${version}"></script> --%>
    <script type="text/javascript" src="http://static.joyme.com/js/swiper.min.js"></script>

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
        var jumpUrl = 'http://m.joyme' + env + window.location.pathname;

        if (bs.versions.mobile) {
            if (bs.versions.android || bs.versions.iPhone || bs.versions.iPad || bs.versions.ios || bs.versions.WPhone) {
                if (window.location.hash.indexOf('wappc') <= 0) {
                    var flag = getCookie('jumpflag');
                    if (flag == '' || flag == null || flag == undefined) {
                        window.location.href = jumpUrl;
                    }
                } else {
                    var timeOutDate = new Date();
                    timeOutDate.setTime(timeOutDate.getTime() + (24 * 60 * 60 * 1000));
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
                cookie += key + "=" + escape(value) + ";path=/;domain=.joyme" + env + ";expires=" + exDate.toUTCString();
            document.cookie = cookie;
        }
    </script>
</head>
<body>
<div id="joyme-wrapper">
    <!--header-->
    <c:import url="/views/jsp/passport/new-header.jsp"/>
    <!--header==end-->
    <!--joyme标题-->
    <div class="joyme-bt fn-nav">
        <span><b>当前位置：</b><a href="<c:out value="${URL_WWW}"/>">着迷网</a> &gt; <a
                href="<c:out value="${URL_WWW}/collection"/>">游戏库</a> &gt; <c:out value="${game.gameName}"/></span>
    </div>
    <!--joyme标题end-->
    <!--joyme-center-->
    <div class="joyme-center fn-clear joyme-center-list">
        <div class="games-l fn-left">
            <c:if test="${gamePlatformType.code == 1}">
                <div class="inwiki-box ">
                    <cite><img src="${game.gameIcon }" alt="" class="lazy"></cite>
                    <c:if test="${fn:length(game.wikiUrl) > 0}">
                        <a href="<c:out value="${game.wikiUrl}"/>" class="load-box-b" target="_blank"></a>
                    </c:if>
                </div>
                <div class="load-box">
                    <div class="load-box-t fn-clear">
                        <p>
                            <c:if test="${fn:length(game.iosDownload) > 0}"><a target="_blank"
                                                                               href="<c:out value="${game.iosDownload}"/>"><code></code>iOS下载</a></c:if>
                            <c:if test="${fn:length(game.androidDownload) > 0}"><a target="_blank"
                                                                                   href="<c:out value="${game.androidDownload}"/>"
                                                                                   class="az"><code></code>Android下载</a></c:if>
                            <c:if test="${game.assicatedGameId > 0}"><a target="_blank"
                                                                        href="<c:out value="${URL_WWW}/collection/${game.assicatedGameId}"/>"
                                                                        class="wp"><code></code>电脑版下载</a></c:if>
                        </p>
                        <cite>
                            <script type="text/javascript">
                                document.write('<img src="<c:out value="${URL_WWW}"/>/acitivty/qrcode/removal?height=200&width=200&url=<c:out value="${URL_M}"/>' + window.location.pathname + '" alt="">');
                            </script>
                            <c:choose>
                                <c:when test="${fn:length(game.iosDownload) > 0||fn:length(game.androidDownload) > 0}">
                                    <font>扫描二维码下载游戏</font>
                                </c:when>
                                <c:otherwise>
                                    <font>扫一扫访问移动版</font>
                                </c:otherwise>
                            </c:choose>
                        </cite>
                    </div>
                </div>
                <c:if test="${giftList != null && giftList.size() > 0}">
                    <div class="games-figt-box">
                        <c:forEach items="${giftList}" var="gift">
                            <c:if test="${gift.sn > 0}">
                                <a href="<c:out value="${URL_WWW}/gift/${gift.gid}"/>">
                                    <h6><c:out value="${gift.title}"/></h6>
                                    <font>剩余：<c:out value="${gift.sn}"/>/<c:out value="${gift.cn}"/></font>
                                    <em>领取</em>
                                </a>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:if>
            </c:if>

            <c:if test="${gamePlatformType.code != 1}">
                <div class="inwiki-box ">
                    <cite><img src="${game.gameIcon }" alt="" class="lazy"></cite>
                    <c:if test="${fn:length(game.wikiUrl) > 0}">
                        <a href="<c:out value="${game.wikiUrl}"/>" class="load-box-b" target="_blank"></a>
                    </c:if>
                    <c:if test="${gamePlatformType.code != 1}">
                        <c:if test="${game.assicatedGameId > 0}"><a class="load-phone" target="_blank"
                                                                    href="<c:out value="${URL_WWW}/collection/${game.assicatedGameId}"/>"><i></i>移动版下载</a></c:if>
                    </c:if>
                </div>
                <div class="int-sidebar">

                    <div class="int-mes">
                        <h2>基本信息</h2>
                        <c:if test="${game.categoryTypeSet.size()>0}">
                            <p>类型：<i><c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st">
                                <a target="_blank" style="color:blue"
                                   href="/collection/genre/c<c:out value="${category.code}"/>_s1_page:1"><c:out
                                        value="${category.value}"/></a>&nbsp;
                            </c:forEach></i></p>
                        </c:if>
                        <c:if test="${game.platformMap.keySet().size()>0}">
                            <p>平台：<i><c:forEach items="${game.platformMap.keySet()}" var="key">
                                <c:forEach items="${game.platformMap[key]}" var="platform">
                                    <a target="_blank" style="color:blue"
                                       href="/collection/genre/p<c:out value="${key}"/>-${platform.code}_s1_page:1"><c:out
                                            value="${platform.desc}"/></a>&nbsp;
                                </c:forEach>
                            </c:forEach>
                            </i>
                            </p>
                        </c:if>
                        <p>上市时间：<c:out value="${dateutil:dateToString(game.gamePublicTime, 'yyyy-MM-dd')}"/></p>
                        <c:if test="${fn:length(game.gameNetType.name) > 0}"><p>联网：<c:out
                                value="${game.gameNetType.name}"/></p></c:if>
                        <c:if test="${fn:length(game.gameDeveloper) > 0}"><p>开发商：<c:out
                                value="${game.gameDeveloper}"/></p></c:if>
                        <c:if test="${game.languageTypeSet.size()>0}">
                            <p>语言：<c:forEach items="${game.languageTypeSet}" var="language" varStatus="st"><c:out
                                    value="${language.name}"/>&nbsp;</c:forEach></p>
                        </c:if>
                        <c:if test="${fn:length(game.officialWebsite)>0}"><p>官网：<a target="_blank" style="color:blue"
                                                                                   href="<c:out value="${game.officialWebsite}"/>">点击进入</a>
                        </p></c:if>
                    </div>
                    <div id="deploy_div" class="deploy">
                        <input id="deploy" type="hidden" value="${pcConfigInfo1}"/>
                    </div>
                    <div id="groom_deploy_div" class="groom-deploy">
                        <input id="groom_deploy" type="hidden" value="${pcConfigInfo2}"/>
                    </div>
                    <script>
                        $(document).ready(function () {
                            var deploy_data = $('#deploy').val();
                            var groom_deploy_data = $('#groom_deploy').val();
                            if (deploy_data.indexOf('\r\n') > 0) {
                                deploy_data = deploy_data.split('\r\n');
                            } else if (deploy_data.indexOf('\n') > 0) {
                                deploy_data = deploy_data.split('\n')
                            }
                            var deploy_html = '<h2>最低配置</h2>';
                            for (i = 0; i < deploy_data.length; i++) {
                                deploy_html += '<p>' + deploy_data[i] + '</p>';
                            }
                            if (deploy_data.length > 0) {
                                $('#deploy_div').after(deploy_html);
                            }


                            if (groom_deploy_data.indexOf('\r\n') > 0) {
                                groom_deploy_data = groom_deploy_data.split('\r\n');
                            } else if (groom_deploy_data.indexOf('\n') > 0) {
                                groom_deploy_data = groom_deploy_data.split('\n')
                            }
                            var groom_deploy_html = '<h2>推荐配置</h2>';

                            for (i = 0; i < groom_deploy_data.length; i++) {
                                groom_deploy_html += '<p>' + groom_deploy_data[i] + '</p>';
                            }
                            if (groom_deploy_data.length > 0) {
                                $('#groom_deploy_div').after(groom_deploy_html);
                            }
                        });
                    </script>
                </div>
            </c:if>
        </div>
        <div class="games-r fn-left">
            <div class="yxpf-box fn-clear">
                <dl class="fn-left">
                    <dt>
                    <h2>${game.gameName}</h2>
                    <span>${game.anotherName}</span>
                    <p>${game.gameDesc}</p>
                    </dt>
                    <c:if test="${gamePlatformType.code == 1}">
                        <dd>
                            <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><a
                                    target="_blank" style="color:blue"
                                    href="/collection/genre/c<c:out value="${category.code}"/>_s1_page:1"><c:out
                                    value="${category.value}"/></a>&nbsp;</c:forEach>
                                <c:if test="${game.categoryTypeSet.size()<=0}">无</c:if></p>
                            <p>开发商：<c:out value="${game.gameDeveloper}"/><c:if
                                    test="${fn:length(game.gameDeveloper) <= 0}">无</c:if></p>
                            <p>平台：<c:forEach items="${game.platformMap.keySet()}" var="key">
                                <c:forEach items="${game.platformMap[key]}" var="platform">
                                    <a target="_blank" style="color:blue"
                                       href="/collection/genre/p<c:out value="${key}"/>-${platform.code}_s1_page:1"><c:out
                                            value="${platform.desc}"/></a>&nbsp;
                                </c:forEach>
                            </c:forEach>
                                <c:if test="${empty game.platformMap}">无</c:if>
                            </p>
                            <p>发行商：<c:out value="${game.gamePublishers}"/><c:if
                                    test="${fn:length(game.gamePublishers) <= 0}">无</c:if></p>
                            <p>上市时间：<c:out value="${dateutil:dateToString(game.gamePublicTime, 'yyyy-MM-dd')}"/></p>
                            <p>语言：<c:forEach items="${game.languageTypeSet}" var="language" varStatus="st"><c:out
                                    value="${language.name}"/>&nbsp;
                            </c:forEach>
                                <c:if test="${game.languageTypeSet.size()<=0}">无</c:if>
                            </p>
                            <p>联网：${game.gameNetType.name}<c:if
                                    test="${fn:length(game.gameNetType.name) <= 0}">无</c:if></p>
                            <p>大小：${game.gameSize}<c:if test="${fn:length(game.gameSize) <= 0}">无</c:if></p>
                        </dd>
                    </c:if>
                    <dd class="share-box">
                        <div class="bdsharebuttonbox bdshare-button-style0-16" data-bd-bind="1464338199004">
                            <a href="#" class="bds_tqq" data-cmd="weixin" title="分享到微信"><i></i></a>
                            <a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"><i></i></a>
                            <a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"><i></i></a>
                            <a class="bds_tieba" data-cmd="tieba" title="分享到百度贴吧"><i></i></a>
                        </div>
                        <script>
                            window._bd_share_config = {
                                "common": {
                                    "bdSnsKey": {},
                                    "bdText": "",
                                    "bdMini": "2",
                                    "bdPic": "",
                                    "bdStyle": "0",
                                    "bdSize": "16"
                                }, "share": {}
                            };
                            with (document)0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];
                        </script>
                    </dd>
                </dl>
                <div class="yxpf-fen fn-right">
                    <cite
                            <c:if test="${game.gameRate<1}">class="one"</c:if>
                            <c:if test="${game.gameRate>=1 && game.gameRate<2}">class="two"</c:if>
                            <c:if test="${game.gameRate>=2 && game.gameRate<3}">class="three"</c:if>
                            <c:if test="${game.gameRate>=3 && game.gameRate<4}">class="four"</c:if>
                            <c:if test="${game.gameRate>=4 && game.gameRate<5}">class="five"</c:if>
                            <c:if test="${game.gameRate>=5 && game.gameRate<6}">class="six"</c:if>
                            <c:if test="${game.gameRate>=6 && game.gameRate<7}">class="seven"</c:if>
                            <c:if test="${game.gameRate>=7 && game.gameRate<8}">class="eight"</c:if>
                            <c:if test="${game.gameRate>=8 && game.gameRate<10}">class="nine"</c:if>
                            <c:if test="${game.gameRate==10}">class="ten"</c:if>><c:out value="${game.gameRate}"/>
                    </cite>
                    <p>
                            <span id="praise-btn" class="praise-btn" data-game="${game.gameDbId}">
                                                            想玩&nbsp;<font id="favor-sum"><c:out
                                    value="${game.favorSum}"/></font><cite id="favor-cite">+1</cite>
                            </span>
                        <span id="bad-btn" class="bad-btn" data-game="${game.gameDbId}">
                                                            玩过&nbsp;<font id="unfavor-sum"><c:out
                                value="${game.unFavorSum}"/></font><cite id="unfavor-cite">+1</cite>
                            </span>
                    </p>
                </div>
            </div>

            <!--yxpf-box==end-->
            <div class="yxjs-box">
                <h2 class="joyme-title fn-clear">
                    <span class="fn-left">游戏<b>介绍</b></span>
                </h2>
                <div class="yxjs-text"><c:if test="${fn:length(game.gameProfile) <= 0}"><p class="yxjs-text-no">
                    暂无游戏介绍</p></c:if><c:if test="${fn:length(game.gameProfile) > 0}"><p>${game.gameProfile}</p></c:if>
                </div>
                <span class="slid-btn"><font>展开</font></span>
                <c:if test="${fn:length(game.gameVideoPic) > 0 || game.gamePicSet.size() > 0}">
                    <div class="swiper-container">
                        <div class="swiper-wrapper">
                            <c:if test="${fn:length(game.gameVideoPic) > 0}">
                                <div class="swiper-slide">
                                    <a href="<c:out value="${game.gameVideo}"/>">
                                        <cite></cite>
                                        <img src="<c:out value="${game.gameVideoPic}"/>"
                                             data-url="<c:out value="${game.gameVideoPic}"/>" class="lazy"/>
                                    </a>
                                </div>
                            </c:if>
                            <c:forEach items="${game.gamePicSet}" var="pic" varStatus="st">
                                <div class="swiper-slide"><img src="<c:out value="${pic}"/>"/></div>
                            </c:forEach>
                        </div>
                        <div class="swiper-scrollbar">
                            <div class="swiper-scrollbar-drag"></div>
                        </div>
                    </div>
                </c:if>
                <!--yxjs-pics==end-->
            </div>
            <!--yxjs-box==end-->
            <div class="yxzx-box fn-clear">
                <div class="yxzx-box-l fn-left">
                    <h2 class="joyme-title fn-clear">
                        <span class="fn-left">游戏<b>资讯</b></span>
                        <!-- <a class="joyme-title-more fn-right"></a> -->
                        <c:if test="${newsList.size() > 4}">
                            <a target="_blank" href="${URL_WWW}/collection/${game.gameDbId}/news"
                               class="joyme-title-more fn-right">更多&gt;&gt;</a>
                        </c:if>
                    </h2>
                    <c:if test="${newsList == null || newsList.size() <= 0}">
                        <div class="none-cont">暂无游戏资讯</div>
                    </c:if>
                    <c:if test="${newsList != null && newsList.size() > 0}">
                        <ul class="yxzx-ul">
                            <c:forEach items="${newsList}" var="archive" end="3">
                                <li><a href="${archive.dede_archives_url}">${archive.dede_archives_title}</a></li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
                <div class="yxzx-box-r fn-left">
                    <h2 class="joyme-title fn-clear">
                        <span class="fn-left">游戏<b>攻略</b></span>
                        <c:if test="${guideList.size() > 4}">
                            <a target="_blank" href="${URL_WWW}/collection/${game.gameDbId}/guides"
                               class="joyme-title-more fn-right">更多&gt;&gt;</a>
                        </c:if>
                    </h2>
                    <c:if test="${guideList == null || guideList.size() <= 0}">
                        <div class="none-cont">暂无游戏攻略</div>
                    </c:if>
                    <c:if test="${guideList != null && guideList.size() > 0}">
                        <ul class="yxzx-ul">
                            <c:forEach items="${guideList}" var="archive" end="3">
                                <li>
                                    <a href="<c:out value="${archive.dede_archives_url}"/>">${archive.dede_archives_title}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>
            <!--yxzx-box==end-->
            <div class="yxsp-box">
                <h2 class="joyme-title fn-clear">
                    <span class="fn-left">游戏<b>视频</b></span>
                    <c:if test="${videoList.size() > 3}">
                        <a target="_blank" href="${URL_WWW}/collection/${game.gameDbId}/videos"
                           class="joyme-title-more fn-right">更多&gt;&gt;</a>
                    </c:if>
                </h2>
                <c:if test="${videoList == null || videoList.size() <= 0}">
                    <div class="none-cont">暂无游戏视频</div>
                </c:if>
                <c:if test="${videoList != null && videoList.size() > 0}">
                    <div class="joyme-video-ul fn-clear">
                        <ul class="com-tab">
                            <c:forEach items="${videoList}" var="archive" end="2">
                                <li><a href="${archive.dede_archives_url}"><img class="lazy"
                                                                                src="${archive.dede_archives_litpic}"
                                                                                alt="${archive.dede_archives_title}"><b
                                        class="video-begin"></b><span
                                        class="fn-hide">${archive.dede_archives_title}</span></a></li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
                <c:if test="${categoryList != null && categoryList.size() > 0}">
                    <div class="cnxh-box-box">
                        <h2 class="joyme-title fn-clear">
                            <span class="fn-left">猜你<b>喜欢</b></span>
                        </h2>
                        <div class="cnxh-list">
                            <c:forEach items="${categoryList}" var="categoryGame">
                                <a href="<c:out value="${URL_WWW}/collection/${categoryGame.gameDbId}"/>">
                                    <cite><img class="lazy" data-url="<c:out value="${categoryGame.gameIcon}"/>"
                                               title="<c:out value="${categoryGame.gameName}"/>"
                                               alt="<c:out value="${categoryGame.gameName}"/>" src=""></cite>
                                    <span><c:out value="${categoryGame.gameName}"/></span>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>

            <div id="comment_area"></div>


        </div>
        <script src="${URL_LIB}/static/js/comment/comment_game.js"></script>
    </div>
    <!--joyme-center==end-->
    <!-- footer -->
    <%@ include file="/views/jsp/tiles/new-footer.jsp" %>
    <!--返回顶部-->

    <div class="popupImg">
        <cite></cite>
    </div>
    <div class="retuen-top">
        <span class="rt-btn">返回顶部</span>
    </div>
    <script src="${URL_STATIC}/pc/cms/jmsy/page/js/joyme-common.js?${version}"></script>
    <script type="text/javascript" src="${URL_STATIC}/pc/cms/jmsy/yxk/js/game-common.js?${version}"></script>
    <script type="text/javascript">
        (function () {
            var drawingArc = function () {
                var canvas = document.getElementById('canvas');
                var ctx = canvas.getContext('2d');
                var ball = {
                    x: 53,
                    y: 53,
                    radius: 53,
                    color: 'blue',
                    draw: function () {
                        ctx.beginPath();
                        ctx.arc(this.x, this.y, this.radius, Math.PI * 2 / 4, -Math.PI * 2 / 4, Math.PI * 2, false);
                        //ctx.closePath();
                        ctx.fillStyle = this.color;
                        ctx.stroke();
                    }
                };
                ball.draw();
            };
            // drawingArc();
        })()
    </script>

    <script>
        $(document).ready(function () {
            var browser = navigator.appName;
            if (browser == "Microsoft Internet Explorer") {
                h();
            } else {
                var swiper = new Swiper('.swiper-container', {
                    /*                     pagination: '.swiper-pagination',
                     paginationClickable: '.swiper-pagination',
                     slidesPerView: 3.5,
                     paginationClickable: true,
                     nextButton: '.swiper-button-next',
                     prevButton: '.swiper-button-prev',
                     spaceBetween: 10 */
                    scrollbar: '.swiper-scrollbar',
                    scrollbarHide: false,
                    slidesPerView: 'auto',
                    spaceBetween: 10
                });
            }
            ;
            function h() {
                $('.swiper-container div').css({'float': 'left', 'margin-right': '10px'});
                var ws = 0;
                $('.swiper-wrapper').children('div').each(function () {
                    ws += $(this).outerWidth(true);
                })
                $('.swiper-wrapper').width(ws + 10);
                $('.swiper-container').css('overflow-x', 'scroll');
            };
        });
    </script>

    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/game-collection-detail-init.js');
    </script>

</div>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>

<script>
    (function (G, D, s, c, p) {
        c = {//监测配置
            UA: "UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS: 0,
            WITH_REF: 1,
            URL: 'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt ? G._iwt.track(c, p) : (G._iwtTQ = G._iwtTQ || []).push([c, p]), !G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading = 1;
            s = D.createElement("script");
            s.src = c.URL;
            t = D.getElementsByTagName("script");
            t = t[t.length - 1];
            t.parentNode.insertBefore(s, t);
        }
    })(this, document);
</script>
<script type="text/javascript">
    $('.process').each(function () {
        var $this = $(this),
                ctx = this.getContext('2d');
        var W = this.width,
                H = this.height,
                min = Math.min(W, H),
                w = 10,//linewidth
                rad = (min - w) / 2,//半径
                deg = 0,
                new_deg = $this.text(),//百分数
                dif = 0,
                loop, re_loop, text, text_w;

        function init() {
            ctx.clearRect(0, 0, W, H);
            ctx.beginPath();
            ctx.strokeStyle = "#aaa";
            ctx.lineWidth = w;
            ctx.arc(W / 2, H / 2, rad, 0, Math.PI * 2, false);
            ctx.stroke();

            var r = (360 * deg / 100) * Math.PI / 180;
            ctx.beginPath();
            ctx.strokeStyle = "#3acb24";
            ctx.lineWidth = w;
            ctx.arc(W / 2, H / 2, rad, 0 - 90 * Math.PI / 180, r - 90 * Math.PI / 180, false);
            ctx.stroke();

            ctx.fillStyle = "#444";
            ctx.font = "50px arial";
            text = deg / 10;
            text_w = ctx.measureText(text).width;
            ctx.fillText(text, W / 2 - text_w / 2, H / 2 + 16);
        }

        function draw() {
            dif = new_deg - deg;
            loop = setInterval(to, 1000 / dif);
        }

        function to() {
            if (deg == new_deg) {
                return clearInterval(loop);
            }
            deg++;
            init(ctx);
        }

        draw();
    });

</script>

</body>
</html>
