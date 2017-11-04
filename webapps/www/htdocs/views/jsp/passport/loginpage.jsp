<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<head>
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
</head>
</head>
<body>
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<!--头部结束-->
<div class="content">
    <div class="w_logincon mt45">
        <h3>登录着迷</h3>

        <div class="blog_login wlogin_l w_pd">
            <form action="http://passport.${DOMAIN}/auth/login" method="post" id="loginForm" name="loginForm">
                <input type="hidden" value="${reurl}" name="reurl"/>

                <p>邮箱：<input type="text" class="inputtextbtn" name="loginkey" id="loginkey"/></p>

                <p class="error" id="error_userid">
                    <c:if test="${message != null}">
                        <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
                    </c:if>
                </p>

                <p>密码：<input name="password" id="password" type="password" class="inputtextbtn"/></p>

                <p id="error_password" class="error"></p>

                <p class="logintop">
                    <span class="right"><a href="${ctx}/security/pwd/forgot">忘记密码</a></span>
                </p>

                <div class="blog_ft"><a class="graybtn" id="submit_login"
                                        href="javascript:void(0);"><span>登 录</span></a></div>
            </form>
        </div>
    </div>
    <div class="w_login_r">
        <p>还没有着迷账号？ <a href="http://passport.${DOMAIN}/auth/registerpage">立即注册</a></p>

        <p>你还可以用以下方式直接登录：

        <div class="cooper clearfix">
            <a class="qq" title="qq"
               href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=${reurl}"></a>
            <a class="weibo" title="新浪微博"
               href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=${reurl}"></a>
        </div>
        </p>
    </div>
    <!--w_login_r-->
</div>
<!--content-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>