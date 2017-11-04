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
        <div class="set_title">
            <h3>邮箱管理</h3>
        </div>
        <!--设置-修改邮箱-->
        <div id="resetemail">
        <div class="set_domain_text">
            <ul>
                <li>修改邮箱前，请您特别注意：</li>
                <li><em>1. 如果修改邮箱，再次登录着迷网时，需要用修改后的邮箱登录账号。</em></li>
                <li><em>2. 保存修改后，我们将向您的新邮箱发送一封邮件，请确保您的新邮箱真实可用。</em></li>
            </ul>
        </div>
        <form id="form_resetuserid" action="${ctx}/json/security/email/modify" method="post">
                <div class="set_basic">
                    <dl>
                        <dt>当前邮箱：</dt>
                        <dd>
                            <p>${email}</p>
                            <input class="shezhi_input" name="olduserid" id="olduserid" type="hidden" maxlength="64"
                                   value="${userLogin.loginKey}"/>
                        </dd>
                        <dt><em>*</em>登录密码：</dt>
                        <dd>
                            <input class="settext" name="password" id="password" type="password" maxlength="18" value=""
                                   AUTOCOMPLETE="OFF"/>
                            <em id="passwordtips"></em>
                        </dd>
                        <dt><em>*</em>新邮箱：</dt>
                        <dd>
                            <input class="settext" name="newuserid" id="newuserid" type="text" maxlength="64" value=""
                                   AUTOCOMPLETE="OFF"/>
                            <em id="newuseridtips"></em>
                        </dd>
                        <dt></dt>
                        <dd><a class="submitbtn" id="modifyemail"><span>保 存</span></a></dd>
                    </dl>
                </div>
            </form>
        </div>
        <div class="set_domain_text" id="suc" style="display:none;">
            <h3>修改邮箱确认邮件已成功发送！</h3>
            <ol>
                <li>修改邮箱确认邮件已经发送到你的邮箱  <b><a href="javascript:void(0);" id="email_href"></a></b></li>
                <li>点击邮件里的确认链接即可登录着迷网</li>
                <li><a href="javascript:void(0);" class="submitbtn" id="link_go_mail"><span>立即查看邮箱</span></a></li>
            </ol>
            <ul>
                <li>还没有收到确认邮件？你可以：</li>
                <li>1. 尝试到广告邮件、垃圾邮件、订阅邮件目录里找找看；</li>
                <li>2. 不小心填错了Email？<a href="${ctx}/profile/customize/email/modifypage">返回修改</a></li>
            </ul>
        </div>

        <!-- 发送成功 -->
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