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

        function edit2(resourceId) {
            window.location.href = "/wiki/editpage?resourceId=" + resourceId;
        }

        function back2() {
            window.location.href = "/wiki/list";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td"> >><a href="/wiki/list">条目维护</a> >>条目详情</td>
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
                        <p:privilege name="/wiki/list">
                            <input name="Submit" type="submit" class="default_button" value="返回条目维护" onClick="back2();">
                        </p:privilege>
                        <p:privilege name="/wiki/editpage">
                            <input name="Submit" type="submit" class="default_button" value="修改条目"
                                   onClick="edit2('${wiki.resourceId}');">
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
                        ${wiki.wikiName}
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">WIKI编码：</td>
                    <td nowrap class="edit_table_value_td">${wiki.wikiCode}</td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI地址：</td>
                    <td class="edit_table_value_td" colspan="3">
                        ${wiki.wikiUrl}
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">条目主图：</td>
                    <td nowrap class="edit_table_value_td">
                        <img id="img_game_logo" src="${uf:parseBFace(wiki.icon)}"/>
                    </td>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">条目附图：</td>
                    <td nowrap class="edit_table_value_td">
                        <img id="img_game_thumimg" src="${uf:parseBFace(icon.thumbIcon)}"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">WIKI简介：</td>
                    <td colspan="3" class="edit_table_value_td" style="word-break:break-all">
                        ${wiki.wikiDesc}
                    </td>
                </tr>
                <tr>
                    <td nowrap class="edit_table_defaulttitle_td" align="right">统计数据：</td>
                    <td colspan="3" class="edit_table_value_td" style="word-break:break-all">
                        总页面数：${wiki.totalPageNum} &nbsp;&nbsp;|&nbsp;&nbsp;
                        上周跟新的页面：${wiki.lastWeekUpdatePageNum}&nbsp;&nbsp;|&nbsp;&nbsp;
                        最近更新日期：<fmt:formatDate value="${wiki.lastUpdatePageDate}" pattern="yyyy年MM月dd日"/>
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
</table>
</td>
</tr>
</table>
</body>
</html>
