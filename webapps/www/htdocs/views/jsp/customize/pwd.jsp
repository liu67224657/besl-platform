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
    <title>修改密码 ${jmh_title}</title>
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
        <div class="set_title">
            <h3>修改密码</h3>
        </div>
        <!--设置-修改密码-->
        <form id="form_resetpwd" action="${ctx}/json/profile/customize/resetpwd" method="post">
        <div class="set_basic">
            <dl>
                <dt><em>*</em>当前密码：</dt>
                <dd>
                    <input type="password" class="settext" name="oldpwd" id="oldpwd" maxlength="18"
                       autocomplete="off"/>
                    <em id="oldpwdtips"></em>
                </dd>
                <dt><em>*</em>新密码：</dt>
                <dd>
                    <input type="password" class="settext" name="newpwd" id="newpwd" maxlength="18"
                       autocomplete="off"/>
                    <em id="newpwdtips"></em>
                </dd>
                <dt><em>*</em>确认密码：</dt>
                <dd>
                    <input type="password" class="settext" name="repeatpwd" id="repeatpwd" maxlength="18"
                       autocomplete="off"/>
                    <em id="repeatpwdtips"></em>
                </dd>
                <dt></dt>
                <dd><a class="submitbtn"><span>保 存</span></a></dd>
            </dl>
        </div>
        </form>
        <!--设置-修改密码 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-pwd-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
