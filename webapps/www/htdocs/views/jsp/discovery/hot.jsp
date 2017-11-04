<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="游戏,圣境传说,`战地3,使命召唤8,爱丽丝,神秘海域,最新游戏,热门游戏,最热游戏,话题,活动,热点,好玩,着迷,着迷网,joyme,joyme.com,游戏社区">
    <meta name="description"
          content="着迷网的热门游戏专区，展示最新、最热门游戏的全部资源！找攻略、看热点、找达人，来着迷热门专区。,游戏,圣境传说,战地3,使命召唤8,爱丽丝,神秘海域,最新游戏,热门游戏,最热游戏,话题,活动,热点,好玩,着迷,着迷网,joyme,joyme.com,游戏社区"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${jmh_title} 看热门</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>

    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
</head>

<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<%@ include file="/hotdeploy/views/jsp/hot/hot-games1.jsp" %>

<!--中间部分开始-->
<div class="content clearfix">
    <!--左侧开始-->
    <div class="conleft">
        <div class="latest">最新热门
            <div class="hoticon"></div>
        </div>

        <%@ include file="/views/jsp/content/content-previewlist.jsp" %>

            <input type="hidden" id="hidden_totalRows" value="${page.totalRows}"/>
    <input type="hidden" id="hidden_scrollNo" value="${screenNo}"/>
    <c:set var="pageurl" value="${URL_WWW}/discovery/hot"/>
    <%@ include file="/views/jsp/page/pagenoend.jsp" %>
    </div>
    <!--左侧结束-->

    <!--右侧开始-->
     <%@ include file="/hotdeploy/views/jsp/hot/hot-contright-new.jsp" %>
    </div>
    <!--话题结束-->
</div>
<!--右侧结束-->
</div>
<!--中间部分结束-->
<!--页尾开始-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/hot-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
