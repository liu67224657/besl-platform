<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 新游戏预告管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">游戏详情</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameresource/newrelease/verify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="2" class="default_line_td">
                            <input type="hidden" name="infoid" value="${dto.newRelease.newReleaseId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="10%">
                            游戏名称:
                        </td>
                        <td height="1">
                            ${dto.newRelease.newGameName}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏图标:
                        </td>
                        <td height="1">
                            <img src="${dto.newRelease.newGameIcon}" height="100" width="100">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏简介:
                        </td>
                        <td height="1">
                            ${dto.newRelease.newGameDesc}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏标签:
                        </td>
                        <td height="1">
                            <c:forEach items="${dto.newReleaseTagList}" var="tag">${tag.tagName}&nbsp;&nbsp;</c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏截图:
                        </td>
                        <td height="1">
                            <c:forEach items="${dto.newRelease.newGamePicSet.picSet}" var="pic">
                                <img src="${pic.picUrl}" height="100" width="100">&nbsp;&nbsp;
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            团体名称:
                        </td>
                        <td height="1">
                            ${dto.newRelease.companyName}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            团体人数:
                        </td>
                        <td height="1">
                            <fmt:message key="gameres.newgame.peoplenumtype.${dto.newRelease.peopleNumType.value}"
                                         bundle="${def}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            合作类型:
                        </td>
                        <td height="1">
                            <c:if test="${dto.newRelease.cooprateType.hasExclusive()}">独代&nbsp;</c:if>
                            <c:if test="${dto.newRelease.cooprateType.hasBenefit()}">分成&nbsp;</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            所在城市:
                        </td>
                        <td height="1">
                            ${dto.city.cityName}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            联系人:
                        </td>
                        <td height="1">
                            ${dto.newRelease.contacts}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            邮箱:
                        </td>
                        <td height="1">
                            ${dto.newRelease.email}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            电话:
                        </td>
                        <td height="1">
                            ${dto.newRelease.phone}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            QQ:
                        </td>
                        <td height="1">
                            ${dto.newRelease.qq}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            发行时间:
                        </td>
                        <td height="1">
                            <fmt:formatDate value="${dto.newRelease.publishDate}" pattern="yyyy年MM月"/>上线
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            发行地区:
                        </td>
                        <td height="1">
                            <c:if test="${dto.newRelease.publishArea.hasInternal()}">国内&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasAsia()}">亚洲&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasSouthEastAsia()}">东南亚&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasJapanKorea()}">日韩&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasNorthAmerica()}">北美&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasSouthAmerica()}">南美&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasWestAsia()}">西亚&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasAfrican()}">非洲&nbsp;</c:if>
                            <c:if test="${dto.newRelease.publishArea.hasEurope()}">欧洲</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            提交时间:
                        </td>
                        <td height="1">
                            <fmt:formatDate value="${dto.newRelease.createDate}" pattern="yyyy年MM月dd日 HH:mm:ss"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            提交人IP:
                        </td>
                        <td height="1">
                            ${dto.newRelease.createIp}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            审核状态:
                        </td>
                        <td height="1"
                            <c:choose>
                                <c:when test="${dto.newRelease.validStatus.code eq 'valid'}">
                                    style="color: #008000;"
                                </c:when>
                                <c:otherwise>
                                    style="color:red"
                                </c:otherwise>
                            </c:choose>>
                            <fmt:message key="gameres.newgame.validstatus.${dto.newRelease.validStatus.code}" bundle="${def}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            审核时间:
                        </td>
                        <td height="1">
                            <fmt:formatDate value="${dto.newRelease.verifyDate}" pattern="yyyy年MM月dd日 HH:mm:ss"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            最后修改时间:
                        </td>
                        <td height="1">
                            <fmt:formatDate value="${dto.newRelease.lastModifyDate}" pattern="yyyy年MM月dd日 HH:mm:ss"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            最后修改人IP:
                        </td>
                        <td height="1">
                            ${dto.newRelease.lastModifyIp}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            最后修改类型:
                        </td>
                        <td height="1">
                            <c:if test="${dto.newRelease.lastModifyType.code != null}">
                            <fmt:message key="gameres.newgame.verify.type.${dto.newRelease.lastModifyType.code}" bundle="${def}"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            关注度:
                        </td>
                        <td height="1">
                            ${dto.newRelease.focusNum}
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class=""></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            关联分享:
                        </td>
                        <td height="1">
                            ${dto.shareBaseInfo.shareKey}
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class=""></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <c:if test="${dto.newRelease.validStatus.code eq 'invalid'}">
                            <input type="submit" value="通过审核" name="submit" class="default_button"/>
                            </c:if>
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>