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
        <c:choose>
            <c:when test="${!authStatus}">
                <div class="yanzheng">
                    <em></em>

                    <p class="yz_icon">登录还未验证邮箱<br>
                        <span>${userid}</span> <a href="${URL_WWW}/security/email/auth/send">立即验证</a>
                        <br><a href="${URL_WWW}/profile/customize/email/modifypage">修改登录邮箱</a>
                    </p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="yz_ok">
                    <div class="yzlcon">
                        <div class="sucessful"></div>
                        <p>登录邮箱已认证<br>
                            <span>请使用此邮箱登录着迷网</span><br>
                            <em>${userid}</em><br>
                            <a href="${URL_WWW}/profile/customize/email/modifypage">修改登录邮箱</a>
                        </p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
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