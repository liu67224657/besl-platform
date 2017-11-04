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
    <meta name="Keywords" content='<c:if test="${checkThemeType != null}"><c:out value="${checkThemeType.name}"/> </c:if><c:if test="${checkCategoryType != null}"><c:out value="${checkCategoryType.value}"/> </c:if><c:if test="${checkLanguageType != null}"><c:out value="${checkLanguageType.name}"/> </c:if><c:if test="${checkNetType != null}"><c:out value="${checkNetType.name}"/> </c:if><c:if test="${checkPlatformType != null}"><c:out value="${checkPlatformType.desc}"/> </c:if>类游戏下载'>
    <meta name="description"
          content="着迷网游戏库推荐好玩的游戏,其中包括手机游戏、电脑游戏，掌机游戏和电视游戏,大家赶紧来到这里选择自己喜欢的游戏吧。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title><c:if test="${checkThemeType != null}"><c:out value="${checkThemeType.name}"/> </c:if><c:if test="${checkCategoryType != null}"><c:out value="${checkCategoryType.value}"/> </c:if><c:if test="${checkLanguageType != null}"><c:out value="${checkLanguageType.name}"/> </c:if><c:if test="${checkNetType != null}"><c:out value="${checkNetType.name}"/> </c:if><c:if test="${checkPlatformType != null}"><c:out value="${checkPlatformType.desc}"/> </c:if>类游戏下载_着迷网Joyme.com</title>
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
<!--header end-->
<!--body-->
<div id="joyme-wrapper">
    <!--joyme-center-->
    <div class="joyme-bt fn-nav">
        <span><b>当前位置：</b><a href="<c:out value="${URL_WWW}"/>">着迷网</a> &gt; <a href="<c:out value="${URL_WWW}/collection"/>">游戏库</a>  <c:if test="${fn:length(title) <= 0}"> &gt;搜索结果</c:if></span>
    </div>
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
                            <a href="javascript:void(0);" id="genre_allplatform" <c:if test="${platformTypeCode == null || platformTypeCode == ''}">class="active"</c:if>>不限</a>
                            <c:forEach items="${platformTypeSet}" var="platformType" varStatus="st">
                                <span class="terrace-btn <c:if test="${platformTypeCode == platformType.code}"> check</c:if>" data-tab="<c:out value="${st.index + 1}"/>"
                                        <%--<c:if test="${existPlatformTypeMap[platformType.code] == null}"> data-click="false" style="cursor:default;color:#e5e5e5" </c:if>--%>
                                      name="genre_platformtype" data-platformtype="<c:out value="${platformType.code}-"/>"><c:out value="${platformType.desc}"/><em></em>
                                </span>
                            </c:forEach>

                            <div class="terrace-box">
                                <c:forEach items="${platformTypeSet}" varStatus="st" var="platformType">
                                    <c:set var="platformTypeIndex" ><c:out value="${platformType.code}"/></c:set>
                                    <c:if test="${platformMap[platformTypeIndex] != null}">
                                        <p class="terrace" id="data-tab${st.index + 1}" <c:if test="${platformTypeCode == platformType.code}"> style="display: inline-block; margin-left: ${(st.index+1)*65}px;" </c:if>>
<%--                                             <c:if test="${platformTypeIndex!=3 && platformTypeIndex!=4}">
                                            <c:forEach items="${platformMap[platformTypeIndex]}" var="platform">
                                                <c:set var="existPlatform" ><c:out value="${platformType.code}-${platform.code}"/></c:set>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-${platform.code}"/>"
                                                        <c:if test="${existPlatformMap != null && existPlatformMap[existPlatform] == null}"> style="background-color: #e5e5e5" </c:if>
                                                   <c:if test="${platformCode == platform.code}"> class="active" </c:if>><c:out value="${platform.desc}"/>
                                                </a>
                                            </c:forEach>
                                            </c:if> --%>
                                            <c:if test="${platformTypeIndex==1}">
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-0"/>"
                                                   <c:if test="${platformCode == 0}"> class="active" </c:if>>iPhone
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-1"/>"
                                                   <c:if test="${platformCode == 1}"> class="active" </c:if>>Android
                                                </a>
                                            </c:if>
                                            <c:if test="${platformTypeIndex==3}">
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-3"/>"
                                                   <c:if test="${platformCode == 3}"> class="active" </c:if>>3DS
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-1"/>"
                                                   <c:if test="${platformCode == 1}"> class="active" </c:if>>PSV
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-2"/>"
                                                   <c:if test="${platformCode == 2}"> class="active" </c:if>>NDS
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-0"/>"
                                                   <c:if test="${platformCode == 0}"> class="active" </c:if>>PSP
                                                </a>
                                            </c:if>
                                            <c:if test="${platformTypeIndex==4}">
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-2"/>"
                                                   <c:if test="${platformCode == 2}"> class="active" </c:if>>PS4
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-5"/>"
                                                   <c:if test="${platformCode == 1}"> class="active" </c:if>>XBOXONE
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-4"/>"
                                                   <c:if test="${platformCode == 4}"> class="active" </c:if>>WiiU
                                                </a>
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-1"/>"
                                                   <c:if test="${platformCode == 1}"> class="active" </c:if>>PS3
                                                </a>  
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-0"/>"
                                                   <c:if test="${platformCode == 0}"> class="active" </c:if>>XBOX360
                                                </a>     
                                                <a href="javascript:void(0);" name="genre_platform" data-platform="<c:out value="${platformType.code}-3"/>"
                                                   <c:if test="${platformCode == 3}"> class="active" </c:if>>Wii
                                                </a>                                                                                                
                                            </c:if>
                                        </p>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt>联网：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_net" <c:if test="${netCode == null}">class="active"</c:if> data-net="">不限</a>
                            <c:forEach items="${netTypeSet}" var="net">
                                <a href="javascript:void(0);" name="genre_net" data-net="<c:out value="${net.code}"/>"
                                        <%--<c:if test="${existNetMap[net.code] == null}"> data-click="false" style="cursor:default;color:#e5e5e5" </c:if>--%>
                                   <c:if test="${netCode == net.code}">class="active"</c:if>><c:out value="${net.name}"/></a>
                            </c:forEach>
                        </dd>
                    </dl>
                    <dl>
                        <dt>语言：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_language" <c:if test="${languageCode == null}">class="active"</c:if> data-language="">不限</a>
                            <c:forEach items="${languageTypeSet}" var="language">
                                <a href="javascript:void(0);" name="genre_language" data-language="${language.code}"
                                        <%--<c:if test="${existLanguageMap[language.code] == null}"> data-click="false" style="cursor:default;color:#e5e5e5" </c:if>--%>
                                   <c:if test="${languageCode == language.code}">class="active"</c:if>><c:out value="${language.name}"/></a>
                            </c:forEach>
                        </dd>
                    </dl>
                    <dl class="type">
                        <dt>类型：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_category" <c:if test="${categoryCode == null}">class="active"</c:if> data-category="">不限</a>
                            <c:forEach items="${categorySet}" var="category" varStatus="c">
                                <a href="javascript:void(0);" name="genre_category" data-category="${category.code}"
                                   <%--<c:if test="${existCategoryMap[category.code] == null}"> data-click="false" style="cursor:default;color:#e5e5e5" </c:if>--%>
                                   <c:if test="${categoryCode == category.code}">class="active"</c:if>><c:out value="${category.value}"/></a>
                            </c:forEach>
                        </dd>
                      <!--   <span class="type-more">更多<i></i></span> -->
                    </dl>
                    <dl>
                        <dt>题材：</dt>
                        <dd>
                            <a href="javascript:void(0);" name="genre_theme" <c:if test="${themeCode == null}">class="active"</c:if> data-theme="">不限</a>
                            <c:forEach items="${themeTypeSet}" var="theme">
                                <a href="javascript:void(0);" name="genre_theme" data-theme="${theme.code}"
                                        <%--<c:if test="${existThemeMap[theme.code] == null}"> data-click="false" style="cursor:default;color:#e5e5e5" </c:if>--%>
                                   <c:if test="${themeCode == theme.code}">class="active"</c:if>><c:out value="${theme.name}"/></a>
                            </c:forEach>
                        </dd>
                    </dl>
                    <dl class="search-box">
                        <dt>搜索：</dt>
                        <dd>
                            <p><input type="text" id="search-name" placeholder="" <c:if test="${fn:length(name) > 0}">value="<c:out value="${name}"/>"</c:if><c:if test="${fn:length(name) <= 0}">value="请输入要查找的游戏名称"</c:if>><span class="search-btn" id="search-btn"></span></p>
                        </dd>
                    </dl>
                </div>
            </div>
            <!--游戏分类==end-->
            <div class="search-tit">
                <c:choose>
                    <c:when test="${fn:length(title) > 0}">${title}</c:when>
                    <c:otherwise> 搜索结果，共<font><c:out value="${page.totalRows > 0 ? page.totalRows : 0}"/></font>条搜索结果 <a href="javascript:void(0)" class="time-btn <c:if test="${sort == '1'}">active</c:if>" name="genre_sort" data-sort="1"></a><a href="javascript:void(0)" class="order-btn <c:if test="${sort == '2'}">active</c:if>" name="genre_sort" data-sort="2"></a></c:otherwise>
                </c:choose>
            </div>
            <!--热门大作-->
            <div class="games-rm fn-clear">
                <div class="games-tj-top">
                    <c:if test="${list != null && list.size() > 0}">
                        <c:forEach items="${list}" var="game" varStatus="st">
                            <dl class="fn-clear">
                                <dt>
                                    <a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank">
                                        <img src="" alt="${game.gameName}" height="120" width="120" data-url="${game.gameIcon}" class="lazy"/>
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
                                    <h3><a href="${URL_WWW}/collection/${game.gameDbId}" target="_blank"><c:out value="${game.gameName}"/></a></h3>
                                    <span>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out value="${category.value}"/>&nbsp;</c:forEach></span>
                                    <span>平台：<c:forEach items="${game.platformMap.keySet()}" var="key">
                                        <c:forEach items="${game.platformMap[key]}" var="platform"><c:out value="${platform.desc}"/>&nbsp;</c:forEach>
                                    </c:forEach></span>
                                    <span>上市时间：<fmt:formatDate value="${game.gamePublicTime}" pattern="yyyy年MM月dd日"/></span>
                                    <p>
                                        <c:if test="${game.giftSum > 0}">
                                            <a href="${URL_WWW}/giftmarket" class="games-tj-lb" target="_blank">礼包</a>
                                        </c:if>
                                        <c:if test="${fn:length(game.wikiUrl) > 0}">
                                            <a href="${game.wikiUrl}" class="games-tj-wiki" target="_blank">WIKI</a>
                                        </c:if>
                                        <a href="<c:out value="${URL_WWW}/collection/${game.gameDbId}"/>" class="join-icon" target="_blank">进入&gt;&gt;</a>
                                    </p>
                                </dd>
                            </dl>
                        </c:forEach>
                    </c:if>
                </div>
                <!-- 分页 -->
                <div class="joyme-paging-box" id="get_page_html">
                    <%@ include file="/views/jsp/page/collection-page.jsp" %>
                </div>
            </div>
            <!--热门大作==end-->
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
    function focusInput(config) {
        var inp=$(config);
        var val='请输入要查找的游戏名称';
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
</script>
<script src="http://lib.joyme.com/static/js/common/seajs.js"></script>
<script src="http://lib.joyme.com/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/game-collection-genre-init.js');
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
