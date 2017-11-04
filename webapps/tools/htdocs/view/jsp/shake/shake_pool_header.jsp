<%@ page contentType="text/html;charset=utf-8" language="java" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇配置列表</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <form action="/shake/pool/putpage" method="post">
                <input type="hidden" name="configid" value="${config.configId}"/>
                <input type="hidden" name="type" value="${type}"/>
                <input type="submit" class="default_button"
                       value="添加摇一摇<fmt:message key="shake.type.${type}" bundle="${def}"/>内容"/>
            </form>
        </td>
        <td>
            <form action="/shake/pool/remove" method="post">
                <input type="hidden" name="configid" value="${config.configId}"/>
                <input type="hidden" name="type" value="${type}"/>
                要移除的值：<input type="text" name="directid" value=""/>
                要移除的数量：<input type="text" name="weight" value=""/>
                <input type="submit" class="default_button"
                       value="移除摇一摇<fmt:message key="shake.type.${type}" bundle="${def}"/>内容"/>
            </form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="">
            应用: ${app.appName}(${app.appId})&nbsp;&nbsp;&nbsp;&nbsp;
            平台: <fmt:message key="joymeapp.platform.${config.platform.code}" bundle="${def}"/>&nbsp;&nbsp;&nbsp;&nbsp;
            版本: ${config.version}&nbsp;&nbsp;&nbsp;&nbsp;
            渠道: <fmt:message key="joymeapp.channel.type.${config.channel}" bundle="${def}"/>&nbsp;&nbsp;&nbsp;&nbsp;
            是否是企业版:<c:choose> <c:when test="${config.enterpriseType.code == 1}">否</c:when> <c:when
                test="${config.enterpriseType.code == 2}">是</c:when> <c:otherwise></c:otherwise></c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
            摇一摇开关:${config.info.shake_open}
            类型：<fmt:message key="shake.type.${type}" bundle="${def}"/>
        </td>
    </tr>
</table>