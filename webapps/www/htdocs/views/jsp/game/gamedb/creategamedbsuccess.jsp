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
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>审核中_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/newgames.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script>
    </script>
</head>
<body>
<!-- header -->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>
<!-- content -->
<div class="newgames-content clearfix">
    <h2 class="newgames-title">上传我的游戏信息</h2>

    <!-- 进度条 -->
    <div class="process process-4"> <!-- 第一步class为“process-1”第二步class为“process-2”以此类推 -->
        <span class="step-1">填写基础信息</span>
        <span class="step-2">填写详细信息</span>
        <span class="step-3">填写商务信息</span>
        <span class="step-4">完成</span>
    </div>

    <div class="post-newgame-success">
        <div class="ischecking">您的游戏信息已经提交审核，<span>请耐心等待...</span></div>
    </div>

    <div class="send-newgame-btn"><a class="submitbtn" href="${URL_WWW}/developers/home"><span>返回机构用户中心</span></a></div>

</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/gamedb-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>
