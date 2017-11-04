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
    <meta name="Keywords" content="着迷、着迷网、游戏社区、游戏攻略、游戏交友">
    <meta name="description" content="来着迷小组寻找推荐游戏、实用攻略、向游戏达人提问、结交志同道合的朋友。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>着迷分享_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/share.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<div id="wiki">
    <div class="login_logo"></div>
    <div id="wiki-share" class="clearfix">
        <div class="wiki-succeed">
            <h2>推荐成功</h2>

            <p>希望你的朋友会喜欢你推荐的东西~</p>
            <a class="graybtn" onclick="closeWindow();"><span>关闭窗口</span></a>
        </div>
    </div>
</div>
<script>
    function closeWindow() {
        window.opener = null;
        window.open('', '_self');
        window.close();
    }
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
