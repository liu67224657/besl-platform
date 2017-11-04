<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇配置列表</td>
    </tr>
    <tr>
        <td valign="top"><br/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>摇一摇业务管理</td>
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
                        <form action="/shake/type/createpage" method="post">
                            <input type="hidden" name="configid" value="${config.configId}"/>
                            <input type="submit" class="default_button" value="创建摇一摇业务"/>
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
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">名称</td>
                    <td nowrap align="center">描述</td>
                    <td nowrap align="center">类型</td>
                    <td nowrap align="center">单人的次数限制</td>
                    <td nowrap align="center">是否有时间限制</td>
                    <td nowrap align="center">开始时间</td>
                    <td nowrap align="center">结束时间</td>
                    <td nowrap align="center">权重</td>
                    <td nowrap align="center">标签</td>
                    <td nowrap align="center">标签开始时间</td>
                    <td nowrap align="center">标签结束时间</td>
                    <td nowrap align="center">操作</td>
                </tr>

                <c:forEach var="cfg" items="${config.info.shakeconfigs}" varStatus="st">
                    <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td nowrap align="center">${cfg.value.buttontext}</td>
                        <td nowrap align="center">${cfg.value.title}</td>
                        <td nowrap align="center"><fmt:message key="shake.type.${cfg.value.shakeType}"
                                                               bundle="${def}"/></td>
                        <td nowrap align="center">${cfg.value.shaketimes}</td>
                        <td nowrap align="center">${cfg.value.timelimit}</td>
                        <td nowrap align="center">
                            <fmt:formatDate value="${date:long2date(cfg.value.begintime)}"
                                            pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td nowrap align="center"><fmt:formatDate value="${date:long2date(cfg.value.endtime)}"
                                                                  pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td nowrap align="center">${cfg.value.shakeRange.min}~${cfg.value.shakeRange.max}</td>
                        <td nowrap align="center">${cfg.value.tag}</td>
                        <td nowrap align="center"><fmt:formatDate value="${date:long2date(cfg.value.tagbegintime)}"
                                                                  pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td nowrap align="center"><fmt:formatDate value="${date:long2date(cfg.value.tagendtime)}"
                                                                  pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td nowrap align="center">
                            <a href="/shake/type/remove?configid=${config.configId}&type=${cfg.key}">删除</a>
                            <a href="/shake/pool/list?configid=${config.configId}&type=${cfg.key}">摇一摇内容管理</a>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>

</table>

</body>
</html>