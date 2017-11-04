<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="囧闻,搞笑,游戏趣事,好玩,游戏资讯,美图,四格,原画,高手,达人,着迷,着迷网,joyme,joyme.com,游戏社区">
    <meta name="description"
          content="着迷网的游戏趣事集中营，不知道该看什么？那么就随便看看吧！这里有最新鲜的游戏介绍，最搞笑的视频，最精美的原画，最有趣的游戏囧事，以及最有爱的游戏达人！,囧闻,搞笑,游戏趣事,好玩,游戏资讯,美图,四格,原画,高手,达人,着迷,着迷网,joyme,joyme.com,游戏社区"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${viewCategory.categoryName}_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/see.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var magazineCode = "${magazineCode}";
        window.seeIndex = ${idx};
    </script>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}&cm=${cm}"/>
<c:forEach var="layout" items="${layoutList}" varStatus="status">
    <c:choose>
        <c:when test="${layout.layoutType.code eq 'layout1'}">
            <c:set var="layoutClass" value="scan_bs1"/>
        </c:when>
        <c:otherwise>
            <c:set var="layoutClass" value="scan_bs2"/>
        </c:otherwise>
    </c:choose>
    <div class="scan_con" id="block_${status.index}">
    ${layout.layoutHtml}
    </div>
    <div class="bkcon">
        <div class="stit"></div>
    </div>
</c:forEach>
<div class="bkcon" id="loading">
    <div class="see_loading"><p>正在载入更多...</p></div>
</div>
<div id="box" class="wallBox">
    <a id="toppage" class="gotop" href="javascript:void(0)" style="display:none;">返回顶部</a>
    <a id="upPage" class="roll_top" href="javascript:void(0)" style="display:none;">向上翻页</a>
    <a id="downPage" class="roll_down" href="javascript:void(0)">向下翻页</a>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/magazine-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>


</body>
</html>
