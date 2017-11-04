<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>忘记密码 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/pwdforgot-init')
    </script>
</head>

<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="icontent clearfix">
    <div id="content" class="set_all">
        <div class="set_title">
            <h3>找回密码</h3>
        </div>
        <c:choose>
            <c:when test="${message == null || message !='user.pwd.forgot.success'}">
                <form id="fm" name="requestResetPwd" action="${ctx}/security/pwd/forgot" method="post">
                    <div class="set_domain_text">
                        <ul>
                            <li>请输入你的登录邮箱</li>
                            <li>
                                <input type="text" name="userid" id="userid" class="settext" value=""/>
                                <em id="useridtips">
                                    <c:if test="${message != null}">
                                        <fmt:message key="${message}" bundle="${userProps}">
                                            <fmt:param value="${userid}"/></fmt:message>
                                    </c:if>
                                </em>
                            </li>
                            <li><a id="a_send" class="submitbtn"><span>发送</span></a></li>
                        </ul>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="set_domain_text">
                    <ol>
                        <li>邮件已经发送到你的邮箱 <b>${userid}</b></li>
                        <li>点击邮件里的链接修改密码</li>
                        <c:if test="${hrefMail!=null}">
                            <li><a title="立即查看邮箱" class="submitbtn" href="${hrefMail.server}"><span>立即查看邮箱</span></a>
                            </li>
                        </c:if>
                    </ol>
                    <ul>
                        <li>还没有收到确认邮件？你可以：</li>
                        <li>1. 尝试到广告邮件、垃圾邮件、订阅邮件目录里找找看；</li>
                        <li>2. <a href="${ctx}/security/pwd/forgot">请点击这里，重新发送确认邮件</a>。</li>
                    </ul>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>