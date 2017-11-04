<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title> ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/change.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">

    </script>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="content clearfix" style="height:600px; min-height:600px;">
    <div class="invitefriend">
        <div class="if_title">
            <h3>完善帐号信息
                <p> 你将可以使用输入的邮箱和密码登录着迷，并自由绑定与解绑合作平台的帐号</p>
            </h3>

        </div>
        <div class="reg_list">
            <form action="${URL_WWW}/profile/customize/full/account" method="post" id="perForm">
                <input id="rurl" type="hidden" name="rurl" value='${rurl}'/>
                <ul class="clearfix">
                    <li>
                        <span class="rs_1">登录邮箱</span>
                            <span class="rs_2">
                                <input id="userid" name="userid" type="text" class="text" value="" maxlength="64">
                            </span>
                        <span class="rs_7" id="useridTips" style="display:none">
                            <fmt:message key="user.email.wrong" bundle="${userProps}"/>
                            <fmt:message key="user.email.exists" bundle="${userProps}"/>
                        </span>
                    </li>
                    <li>
                        <span class="rs_1">创建密码</span>
                            <span class="rs_2">
                                <input id="userpwd" name="userpwd" type="password" class="text" maxlength="18">
                            </span>
                        <span class="rs_7" id="userpwdTips" style="display:none">
                            <fmt:message key="userset.userpwd.error" bundle="${userProps}"/>
                        </span>
                    </li>
                </ul>
                <p class="next"><a class="submitbtn" id="link_upload_audio"><span>保 存</span></a> <a id="skip"  href="<c:choose><c:when test="${rurl ne null}">${rurl}</c:when><c:otherwise>${URL_WWW}</c:otherwise></c:choose>" class="tiaoguo">暂不完善，跳过</a></p>
            </form>
        </div>

    </div>
    <!--yaoqing-->
</div>
<!--content-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/fullaccount-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>
