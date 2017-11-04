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
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/share.css?${version}" rel="stylesheet" type="text/css"/>
    <title>wiki弹窗-登录</title>
</head>
<body>
<div id="wiki">
    <div class="login_logo"></div>
    <div id="wiki-share" class="clearfix">
        <!-- 左侧表单 -->
        <div class="blog_login wlogin_l">
            <h3>登录着迷</h3>

            <form id="maskLoginForm" method="post" action="http://passport.${DOMAIN}/auth/login">
                <input type="hidden" value="${reurl}" name="reurl"/>

                <p>邮箱：<input type="text" name="loginkey" id="loginkey" class="inputtextbtn"></p>

                <p id="useridTips" class="error">
                    <c:if test="${message != null}">
                        <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
                    </c:if>
                </p>

                <p>密码：<input type="password" name="password" id="password" class="inputtextbtn"></p>

                <p id="passwordTips" class="error"></p>

                <p class="logintop">
                    <span class="blogright"><a href="${ctx}/security/pwd/forgot">忘记密码</a></span>
                </p>

                <div class="blog_ft">
                    <a id="log_in_dl" class="graybtn"><span>登 录</span></a>

                    <p>还没有账号，现在<a id="loginMaskReg" href="/registerpage">注册</a></p>
                </div>
            </form>
        </div>
        <!-- ### -->
        <div class="wlogin_r">
            <p>还没有着迷账号？ <a id="MaskReg" href="/registerpage">立即注册</a></p>

            <p>你还可以用以下方式直接登录：
            <span class="cooper clearfix">
            <a class="qq" title="qq"
               href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=${reurl}"></a>
            <a class="weibo" title="新浪微博"
               href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=${reurl}"></a>
            </span>
            </p>
        </div>
    </div>
</div>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/login-simple-init.js')
</script>
</body>
</html>