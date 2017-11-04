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

    <%--热部署seo--%>
    <%@ include file="/hotdeploy/views/jsp/talent/talent-seo.jsp" %>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${jmh_title} 话版</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="talk_area">
  ${talkPageDTO.recommend}

  <div class="talk_areacon clearfix">
    <div class="talk_area_left">
      <div class="talk_area_hd">话版直播</div>
      <div class="talk_area_bd">
          <%@ include file="/views/jsp/content/content-simplelist.jsp" %>
      </div><!--talk_area_bd-->
      <div class="talk_area_ft"></div>
    </div>
      <!--talk_area_left-->
    ${talkPageDTO.talkList}
      <!--talk_area_right-->
  </div><!--talk_areacon-->
</div>
<!--广场部分结束-->
<!--页尾开始-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/talk-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
