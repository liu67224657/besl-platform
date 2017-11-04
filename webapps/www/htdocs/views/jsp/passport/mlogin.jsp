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
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="Keywords" content="着迷、着迷网、游戏社区、游戏攻略、游戏交友">
    <meta name="description" content="来着迷小组寻找推荐游戏、实用攻略、向游戏达人提问、结交志同道合的朋友。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/login-init.js')
    </script>
    <link href="${URL_LIB}/static/theme/default/css/joymemobile.css?${version}" rel="stylesheet" type="text/css"/>
    <title>wiki弹窗-登录</title>
</head>
<body>
<div id="wrap-snsLogin">

    <div class="joymeMobile-title-bg">
        <h1 class="joymeMobile-title"><span>登录着迷</span></h1>
    </div>

    <div class="sns-choosePlatform">
        <h2>社交网络登录</h2>

        <div>
            <a href="${URL_WWW}/profile/sync/qq/bind<c:if test="${reurl!=null}">?reurl=${reurl}</c:if><c:if test="${icn!=null}">&icn=${icn}</c:if><c:if test="${inviteId!=null && inviteId>0}">&inviteid=${inviteId}</c:if><c:if test="${fn:length(gid)>0}">&gid=${gid}</c:if>"
               class="sns-qq">QQ</a>
            <a href="${URL_WWW}/profile/sync/sinaweibo/bind<c:if test="${reurl!=null}">?reurl=${reurl}</c:if><c:if test="${icn!=null}">&icn=${icn}</c:if><c:if test="${inviteId!=null && inviteId>0}">&inviteid=${inviteId}</c:if><c:if test="${fn:length(gid)>0}">&gid=${gid}</c:if>"
               title="新浪微博" class="sns-sina">SINA</a>
        </div>
    </div>
    <form action="${ctx}/mlogin" method="post" id="loginForm" name="loginForm">
        <input type="hidden" value="mobile" id="category"/>
        <input type="hidden" value="${reurl}" name="reurl"/>

        <div class="localLogin">
            <h2>老用户邮箱登录</h2>

            <div>
                <p><span></span><input type="email" placeholder="邮箱" name="userid" id="userid"/></p>

                <p><span></span><input type="password" placeholder="密码" name="password" id="password"/></p>
            </div>
            <div><span id="error_userid">
                  <c:if test="${message != null}">
                      <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
                  </c:if>

            </span><span id="error_password"></span><a href="${ctx}/security/pwd/forgot">忘记密码?</a></div>
        </div>

        <div class="btn-box">
            <a id="submit_login" href="javascript:void(0);" class="btn btn-1">登&nbsp;&nbsp;录</a>
        </div>
    </form>
</div>

<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>