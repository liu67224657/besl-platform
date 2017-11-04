<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、评论审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript">
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 社交端管理 >> 社交端活动详情</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>活动详情</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">活动ID</td>
                    <td>${activity.activityId}</td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">标题</td>
                    <td>${activity.title}</td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">描述</td>
                    <td><textarea readonly="readonly"
                                  style="height: 50px;width: 300px">${activity.description}</textarea></td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">角标属性</td>
                    <td><fmt:message key="subscript.type.${activity.subscript.type}" bundle="${def}"/>&nbsp;
                        ${activity.subscript.startDate}&nbsp;${activity.subscript.endDate}
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">使用</td>
                    <td>${activity.useSum}</td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">评论</td>
                    <td>${activity.replySum}</td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">点赞</td>
                    <td>${activity.agreeSum}</td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">礼物</td>
                    <td>${activity.giftSum}</td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">创建信息</td>
                    <td><fmt:formatDate value="${activity.createDate}"
                                        pattern="yyyy-MM-dd"/>&nbsp;${activity.createIp}&nbsp;${activity.createUserId}</td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">修改信息</td>
                    <td><fmt:formatDate value="${activity.lastModifyDate}"
                                        pattern="yyyy-MM-dd"/>&nbsp;${activity.lastModifyIp}&nbsp;${activity.lastModifyUserId}</td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">分享</td>
                    <td>${activity.shareId}</td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">排序值</td>
                    <td>${activity.displayOrder}</td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">IOS水印弹层图</td>
                    <td><img src="${activity.iosIcon}"/></td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">Android水印弹层图</td>
                    <td><img src="${activity.androidIcon}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">IOS广场小图</td>
                    <td><img src="${activity.iosSmallPic}"/></td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">Android广场小图</td>
                    <td><img src="${activity.androidSmallPic}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">IOS广场大图</td>
                    <td><img height="100" width="100" src="${activity.iosBigPic}"/></td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">Android广场大图</td>
                    <td><img height="100" width="100" src="${activity.androidBigPic}"/></td>
                </tr>
                <c:if test="${! empty activity.awardSet.awardSet}">
                    <tr>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">奖品</td>
                        <td colspan="3">
                            <table width="50%" bgcolor="#e0ffff" cellspacing="0" cellpadding="0">
                                <c:forEach items="${activity.awardSet.awardSet}" var="award" varStatus="st">
                                    <tr>
                                        <td width="80" align="right">名称：</td>
                                        <td>${award.name}</td>
                                        <td width="80" align="right">等级：</td>
                                        <td>${award.level}</td>
                                        <td width="80" align="right">总数：</td>
                                        <td>${award.total}</td>
                                    </tr>
                                    <tr>
                                        <td height="1" colspan="15" class="default_line_td"></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">状态</td>
                    <td colspan="3" <c:choose>
                        <c:when test="${activity.removeStatus.code eq 'valid'}">style="color: #008000;" </c:when>
                        <c:when test="${activity.removeStatus.code eq 'invalid'}">style="color: #ffff00;" </c:when>
                        <c:otherwise>style="color: #ff0000;"</c:otherwise>
                    </c:choose>>
                        <fmt:message key="social.activity.status.${activity.removeStatus.code}" bundle="${def}"/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">文章总数</td>
                    <td>${activity.totals}</td>
                    <td>跳转</td>
                    <td>${activity.redirectUrl}</td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr align="center">
                    <td colspan="3">
                        <%--<input name="Submit" type="submit" class="default_button" value="提交">--%>
                        <input name="Reset" type="button" class="default_button" value="返回"
                               onclick="javascript:window.history.go(-1);">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>