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
    <title>绑定社区 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <c:if test="${!empty errorcode}">
       <script type="text/javascript">
       var errorcode='<fmt:message key="${errorcode}" bundle="${userProps}"/>'
       </script>
    </c:if>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <!--设置title-->
        <div class="set_title">
            <h3>绑定社区</h3>
        </div>
        <!--设置title end-->
        <div class="set_avatar">
            <p class="band">绑定以下社区，可实现内容同步更新，也可以使用相应平台的账号登录着迷</p>

            <div class="publish_tab">
                <ul class="publish_setcon">
                    <c:set var="rurl" value="${URL_WWW}/profile/customize/bind"/>
                <c:forEach items="${syncProviderMap}" var="provider">
                    <c:if test="${provider.key != 'qweibo' && provider.key != 'renren'}">
                    <li data-provider="${provider.key}">
                        <p class="set${provider.key}"></p>
                        <p class="weibo"><fmt:message key="sync.provider.${provider.key}" bundle="${userProps}"/></p>
                        <c:choose>
                        <c:when test="${bindedSet.contains(provider.value)}">
                             <p class="set_way_on">已绑定</p>
                        </c:when>
                        <c:otherwise>
                            <p class="set_way"><a
                                    href="http://passport.${DOMAIN}/auth/thirdapi/${provider.key}/bind?reurl=${jstr:encodeUrl(rurl,'UTF-8')}&rl=true">设置绑定</a>
                            </p>
                        </c:otherwise>
                        </c:choose>
                        </p>
                    </li>
                    </c:if>
                </c:forEach>
                </ul>
            </div>

            <p style="clear: both; line-height: 20px; padding-top: 20px; color:#8f8f8f; width: 490px;">由于新浪微博的设置问题，每个用户的同步内容服务每隔7-10天需要重新授权。如果您绑定新浪微博超过7天，建议您取消并重新绑定，以免同步内容失败。</p>

        </div>
        <!--设置-设置头像 end-->
    </div>
    <!--设置内容结束-->
    <c:if test="${errorMask}">
    <div class="pop" id="joymealert_bind" style="position:absolute;_position:absolute; z-index:12000;left:554px;top:150px">
        <div class="hd clearfix"><em>提示信息</em></div>
        <div class="bd clearfix">
            <div class="publicuse">
                <p class="tipstext" id="p_alert_text">
            <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
            </p></div>
        </div>
        <div class="ft ftr">
            <a id="but_alert" class="submitbtn"><span>确定</span></a>
        </div>
    </div>
</c:if>
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>


<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-bind-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>