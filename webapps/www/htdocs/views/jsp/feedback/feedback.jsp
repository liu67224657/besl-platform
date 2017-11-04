<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>意见反馈 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/feedback-init.js')
    </script>
</head>
<body>
<%@ include file="/views/jsp/tiles/header-only-icon.jsp" %>

<div class="content about-bg clearfix">
    <%@ include file="/views/jsp/help/left.jsp" %>
    <div class="about-right">
        <div class="about-title"><span>意见反馈</span></div>
        <div class="feedback-content">
            <form action="${ctx}/json/ops/feedback/send" method="post" id="feedbackForm" name="feedbackForm">
                <span class="feedback-intro">欢迎您向我们提出意见或建议，请将您的描述控制在500字以内，简要的描述将有助于让我们尽快领会您的建议。</span>
                <textarea class="feedback-textarea" style="font-family:Tahoma, '宋体';" rows="" cols="" name="feedbackBody" id="feedbackBody"></textarea>

                <div class="feedback-validate">
                    <span id="tips" class="feedback-val"></span>

                    <div class="feedback-submit">

                        <a href="javascript:void(0)" onclick="javascript:sendReply()"
                           class="submitbtn"><span>提交建议</span></a>
                    </div>
                </div>
            </form>
        </div>
        <!--feedback-content结束-->
    </div>
    <!--about-right结束-->
</div>
<!--content结束-->
<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>