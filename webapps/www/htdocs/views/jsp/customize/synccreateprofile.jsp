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
    <title>创建博客 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/createprofile-init.js')
    </script>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="content">
    <div class="nicheng">
        <p>
        <c:choose>
            <c:when test="${fn:length(screenname)>0}">
                 <fmt:message key="${errorTitle}" bundle="${userProps}">
            <fmt:param value="${screenname}"/>
        </fmt:message>
            </c:when>
            <c:otherwise>
                <fmt:message key="sync.error.nickname.empty" bundle="${userProps}"></fmt:message>
            </c:otherwise>
        </c:choose>

        </p>
        <dl>
            <dt class="personface">
                <a class="tag_cl_left" href="javascript:void(0);">
                    <img src="${URL_LIB}/static/theme/default/img/default.jpg" width="58px" height="58px">
                </a>
            </dt>
            <dd>
                <form action="/profile/sync/${apiCode}/createprofile" id="createprofileform">
                    <input type="hidden" name="accountuno" value="${accountuno}"/>
                    <input type="hidden" name="inviteid" value="${inviteid}"/>
                    <input type="hidden" name="gid" value="${gid}"/>
                    <input type="hidden" name="rurl" value="${rurl}"/>
                    <input type="hidden" name="figurl" value="${figUrl}"/>
                    昵称 <input type="text" class="nichengtext" name="screenname" id="screenname">

                    <div id="tipstext" class="tipstext">
                        <c:choose>
                            <c:when test="${fn:length(screenname)>0}">
                                <fmt:message key="${errorMsg}" bundle="${userProps}"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="sync.error.nickname.mustfill" bundle="${userProps}"></fmt:message>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="reg_list">
                        <span class="rs_4"><a id="a_reg" href="javascript:void(0)">完成注册</a></span>
                    </div>
                </form>
            </dd>
        </dl>
    </div>
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<%--<script src="${URL_LIB}/static/js/common/seajs.js"></script>--%>
<%--<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>--%>
<%--<script>--%>
    <%--seajs.use('${URL_LIB}/static/js/page/customize-bind-init.js')--%>
<%--</script>--%>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>