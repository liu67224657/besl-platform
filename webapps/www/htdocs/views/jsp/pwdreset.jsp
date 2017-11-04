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
        seajs.use('${URL_LIB}/static/js/init/pwdreset-init')
    </script>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="icontent clearfix">
    <c:choose>
        <c:when test="${message == null || (message !='user.pwd.reset.success' && message !='user.pwd.reset.wrong' && message !='user.pwd.reset.url.illegl') }">
            <div class="set_all">
                <div class="set_title">
                    <h3>重设密码</h3>
                </div>
                <form class="reg-form" name="resetPwdForm" id="resetPwdForm" action="${ctx}/security/pwd/reset"
                      method="post">
                    <input type="hidden" name="uno" value="${uno}"/>
                    <input type="hidden" name="k" value="${k}"/>
                    <!--设置-忘记密码-->
                    <div class="set_basic">
                        <dl>
                            <dt><em>*</em>新密码：</dt>
                            <dd>
                                <input name="password" id="password" type="password" class="settext"/>
                                <em id="passwordtips"></em>
                            </dd>
                            <dt><em>*</em>确认密码：</dt>
                            <dd>
                                <input name="repassword" id="repassword" type="password" class="settext"/>
                                <em id="repasswordtips"></em>
                            </dd>
                            <dt></dt>
                            <dd><a id="resetSubmit" class="submitbtn"><span>保 存</span></a></dd>
                        </dl>
                    </div>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${message != null && message =='user.pwd.reset.success'}">
                <div class="set_all">
                    <div class="set_domain_text">
                        <h3>修改成功！</h3>
                        <ul>
                            <fmt:message key="${message}" bundle="${userProps}">
                                <fmt:param value="${account}"/>
                            </fmt:message>
                        </ul>
                    </div>
                </div>
            </c:if>
            <c:if test="${message != null &&  message !='user.pwd.reset.success'}">
                <div class="set_all">
                    <div class="set_domain_text">
                        <h3>参数错误</h3>
                        <ul>
                            <fmt:message key="${message}" bundle="${userProps}">
                                <fmt:param value="${account}"/>
                            </fmt:message>
                        </ul>
                    </div>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>