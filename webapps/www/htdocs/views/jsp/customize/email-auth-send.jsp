<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>邮箱管理 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <!--设置title-->
        <div class="set_title">
            <h3>邮箱管理</h3>
        </div>
        <c:if test="${errorInfo==null}">
        <div class="yz_list">
            <p><b>验证邮件已发送到你的邮箱：</b><em>${userid}</em></p>

            <p>点击邮件里的确认链接完成验证</p>

            <p><a href="javascript:void(0);" class="submitbtn" id="goto_email" data-email="${userid}"><span>立即查看邮箱</span></a></p>
        </div>
        <!--yz_now-->
        <div class="yz_tips">
            <p> 没有收到验证邮件？<br>
                1. 到广告邮件、垃圾邮件目录里找找看
                <br>2. 真没收到？<a title="重新发送验证邮件" href="${URL_WWW}/security/email/auth/send">重新发送验证邮件</a>
                <br>3. 不小心填错了Email？<a href="${ctx}/profile/customize/emailmodify">返回修改</a>
            </p>
        </div>
        </c:if>
        <c:if test="${errorInfo!=null}">
        <div class="yz_fail">
            <em></em>
            <p>
            <b><fmt:message key="${errorInfo}" bundle="${userProps}"/></b>
                <br>
                2小时内不能重复发送认证邮件。
                <br>
                验证邮箱邮件的有效期为48小时，如果您没能在有效期内完成安全邮箱验证，请
                <a href="${URL_WWW}/security/email/auth/send">重新发送验证邮件</a>
            </p>
        </div>
        </c:if>
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-email-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>