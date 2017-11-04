<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/newgames.css?${version}"/>
</head>
</head>
<body>
<!--头部开始-->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>
<!--头部结束-->
<div class="newgames-content clearfix">
    <h2 class="newgames-title">机构帐号登录</h2>

    <form action="${ctx}/logindev" method="post" id="loginForm" name="loginForm">
        <table width="100%" class="post-newgame" style="border:none">
            <tbody>
            <tr>
                <input type="hidden" value="${URL_WWW}/developers/home" name="reurl"/>
                <input type="hidden" value="${cauth}" name="cauth"/>
                <th style="padding-top:6px;" valign="top" width="90">登录邮箱：</th>
                <td>
                    <input type="text" value="" class="inputs-1" name="userid" id="userid">

                    <div class="error" style="padding-top:4px;" id="error_userid">
                        <c:if test="${message != null}">
                            <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
                        </c:if>
                    </div>
                </td>
            </tr>
            <tr>
                <th style="padding-top:6px;" valign="top" width="90">登录密码：</th>
                <td>
                    <input type="password" value="" class="inputs-1" name="password" id="password">

                    <div id="error_password" class="error" style="padding-top:4px;"></div>
                </td>
            </tr>
            <tr>
                <th>&nbsp;</th>
                <td>
                    <a class="submitbtn" href="javascript:void(0)" id="submit_login"><span>登 录</span></a>
                    <a href="${ctx}/security/pwd/forgot" style="display:inline-block; vertical-align:middle; padding:4px 0 0 10px;">忘记密码</a>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</div>
<!--content-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/developer-login-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>