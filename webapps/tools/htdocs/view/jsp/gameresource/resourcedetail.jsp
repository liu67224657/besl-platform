<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>

<html>
<head>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <title>后台数据管理-运营管理-游戏条目管理</title>
    <script language="JavaScript" type="text/JavaScript">

        function edit(resourceId) {
            window.location.href = "/gameresource/preeditgameresource?resourceId=" + resourceId;
        }
        function edit2(resourceId) {
            window.location.href = "/gameresource/preeditgametopic?resourceId=" + resourceId;
        }

        function back() {
            window.location.href = "/gameresource/gameresourcelist";
        }
        function back2() {
            window.location.href = "/gameresource/gametopiclist";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>>
            <c:choose>
                <c:when test="${entity.resourceDomain eq 'game'}">
                    <a href="/gameresource/gameresourcelist">
                </c:when>
                <c:otherwise>
                    <a href="/gameresource/gametopiclist">
                </c:otherwise>
            </c:choose>
             条目维护</a> >>条目详情</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="detail_table_header_td">>查看条目详情</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/gameresource/gameresourcelist">
                            <input name="Submit" type="submit" class="default_button" value="返回条目维护"
                                <c:choose>
                                    <c:when test="${entity.resourceDomain.code eq 'game'}">
                                        onClick="back();"
                                    </c:when>
                                    <c:when test="${entity.resourceDomain.code eq 'group'}">
                                        onClick="back2();"
                                    </c:when>
                                    <c:when test="${entity.resourceDomain.code eq 'ios'}">
                                        onClick="back();"
                                    </c:when>
                                </c:choose>
                                   >
                        </p:privilege>
                        <p:privilege name="/gameresource/preeditgameresource">
                            <input name="Submit" type="submit" class="default_button" value="修改条目"
                                <c:choose>
                                    <c:when test="${entity.resourceDomain.code eq 'game'}">
                                        onClick="edit('${entity.resourceId}');"
                                    </c:when>
                                    <c:when test="${entity.resourceDomain.code eq 'group'}">
                                        onClick="edit2('${entity.resourceId}');"
                                    </c:when>
                                    <c:when test="${entity.resourceDomain.code eq 'ios'}">
                                        onClick="edit('${entity.resourceId}');"
                                    </c:when>
                                </c:choose>
                                   >
                        </p:privilege>
                        <input type="checkbox" name="checkbox" value="1"
                               onClick="hideSpan(document.getElementById('moduleView'),this.checked)">
                        [显示/隐藏]
                    </td>
                </tr>
            </table>
            <span id=moduleView>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏条目主名称：</td>
                    <td class="edit_table_value_td">
                        ${entity.resourceName}
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">游戏编码：</td>
                    <td nowrap class="edit_table_value_td">${entity.gameCode}</td>
                </tr>

                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏平台：</td>

                    <td class="edit_table_value_td">

                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">类型：</td>
                    <td nowrap class="edit_table_value_td"></td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">主图类别：</td>
                    <td class="edit_table_value_td">

                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">尺寸：</td>
                    <td nowrap class="edit_table_value_td"></td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">条目主图：</td>
                    <td nowrap class="edit_table_value_td">
                        <c:forEach var="image" items="${entity.icon.images}">
                            <img id="img_game_logo" src="${uf:parseBFace(image.url)}"/>
                        </c:forEach>

                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">条目附图：</td>
                    <td nowrap class="edit_table_value_td">
                         <c:forEach var="image" items="${entity.resourceThumbimg.images}">
                            <img id="img_game_thumimg" src="${uf:parseBFace(image.url)}"/>
                            <input name="thumbimg" type="hidden" value="${image.url}" id="thumbimg_url">
                        </c:forEach>
                    </td>
                </tr>

                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">SEO关键字：</td>
                    <td class="edit_table_value_td">
                          ${entity.seoKeyWords}
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">风格：</td>
                    <td nowrap class="edit_table_value_td">
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">SEO描述：</td>
                    <td class="edit_table_value_td">
                          ${entity.seoDescription}
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">语言：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.language}
                    </td>
                </tr>

                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">发行商/运营商：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.publishCompany}
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">开发商：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.develop}
                    </td>
                </tr>

                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏人数：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.playerNumber}
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">游戏大小：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.fileSize}
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">发售日期：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.publishDate}


                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">官网：</td>
                    <td nowrap class="edit_table_value_td">
                          ${entity.resourceUrl}
                    </td>
                </tr>
                <tr>
                    <td width="120"  align="right" class="edit_table_defaulttitle_td">更新日期：</td>
                    <td nowrap  class="edit_table_value_td">
                          ${entity.lastUpdateDate}
                    </td>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">同义词：</td>
                    <td nowrap class="edit_table_value_td">${test}
                          ${entity.synonyms}
                    </td>
                    <%--<td nowrap class="edit_table_defaulttitle_td" align="right">下载链接：</td>--%>
                    <%--<td nowrap class="edit_table_value_td">--%>
                          <%--${entity.buyLink}--%>
                    <%--</td>--%>
                </tr>
                 <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">获取渠道：</td>
                    <td nowrap colspan="3" class="edit_table_value_td">
                        <c:forEach var="channel" items="${entity.gameProperties.channels}" varStatus="st">
                            类型：<fmt:message key="game.channel.${channel.propertyType}.code" bundle="${userProps}"/>&nbsp;&nbsp;&nbsp;&nbsp;地址：${channel.value}&nbsp;&nbsp;&nbsp;&nbsp;价格：${channel.value2}
                            <c:if test="${!st.last}"><br/></c:if>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">游戏简介：</td>
                    <td colspan="3" class="edit_table_value_td" style="word-break:break-all">
                          ${entity.resourceDesc}
                    </td>
                </tr>
                 <tr>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">更新信息：</td>
                    <td colspan="3" class="edit_table_value_td" style="word-break:break-all">
                          ${entity.gameProperties.updateInfo.value}
                    </td>
                </tr>
            </table>
            </span>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <br>
        </td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <form action="" method="post" name="tabForm" target="tabMain"
                      onSubmit="document.tabMain.location=this.action;return false;">
                    <tr>
                        <td height="22">
                            <input name="tab_1" type="submit" class="tab_noactive_button"
                                   value="查看条目关联的其他资源" title="查看条目关联的其他资源"
                                   onClick="submitTab('tabForm','tab_',1,2,'/gameresource/relationlist?resid=${entity.resourceId}');">
                            <input name="tab_2" type="submit" class="tab_noactive_button"
                                   value="查看条目权限" title="查看条目权限"
                                   onClick="submitTab('tabForm','tab_',2,2,'/gameresource/privacy/list?resid=${entity.resourceId}');">
                       </td>
                    </tr>
                </form>
                <tr>
                    <td height="2" class="default_line_td"></td>
                </tr>
                <tr>
                    <td style="height:100%" valign="top">
                        <iframe style="z-index: 1; width: 100%; height: 100%" name=tabMain src="" frameBorder=0
                                scrolling=auto></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</td>
</tr>
</table>
</body>
</html>
<script language="JavaScript">
    document.tabForm.tab_1.click();
</script>