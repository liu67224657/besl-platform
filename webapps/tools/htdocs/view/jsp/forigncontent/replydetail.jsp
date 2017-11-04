<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>评论详情</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 客户服务 >> 内容审核 >> 评论审核</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">评论详情</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" colspan="3" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/forign/content/reply/batchupdate" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td>
                            <input type="hidden" name="screenname" value="${screenname}">
                            <input type="hidden" name="unikey" value="${unikey}">
                            <input type="hidden" name="subkey" value="${subKey}">
                            <input type="hidden" name="startdate" value="${startdate}">
                            <input type="hidden" name="enddate" value="${enddate}">
                            <input type="hidden" name="statuscode" value="${statuscode}">
                            <input type="hidden" name="domain" value="${domain}">
                            <input type="hidden" name="replyids" id="input_hidden_replyids"
                                   value="${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            评论ID:
                        </td>
                        <td height="1">
                            ${reply.replyId}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="10%">
                            评论帐号:
                        </td>
                        <td height="1">
                            ${profile.nick}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            文章信息:
                        </td>
                        <td height="1">
                            <table>
                                <tr>
                                    <td class="default_line_td">ID</td>
                                    <td>${comment.commentId}</td>
                                </tr>
                                <tr>
                                    <td class="default_line_td">unikey</td>
                                    <td>${comment.uniqueKey}</td>
                                </tr>
                                <tr>
                                    <td class="default_line_td">uri</td>
                                    <td>${comment.uri}</td>
                                </tr>
                            </table>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            上级评论ID:
                        </td>
                        <td height="1">
                            ${reply.parentId}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            主评论ID:
                        </td>
                        <td height="1">
                            ${reply.rootId}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            回复数:
                        </td>
                        <td height="1">
                            ${reply.subReplySum}<c:if test="${reply.subReplySum > 0}"><a href="/forign/content/reply/sublist?commentid=${reply.commentId}&rootid=${reply.replyId}" target="_blank">查看回复</a></c:if>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            支持数:
                        </td>
                        <td height="1">
                            ${reply.agreeSum}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            内容:
                        </td>
                        <td height="1">
                            <textarea readonly="readonly"
                                      style="height: 100px;width: 400px;">${reply.body.text}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--图片:--%>
                    <%--</td>--%>
                    <%--<td height="1">--%>
                    <%--<img src="${reply.body.pic}"/>--%>
                    <%--</td>--%>
                    <%--<td height="1">--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            创建时间:
                        </td>
                        <td height="1"><fmt:formatDate value="${reply.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            创建IP:
                        </td>
                        <td height="1">
                            ${reply.createIp}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            状态:
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${reply.removeStatus.code=='n'}">
                                    未审核
                                </c:when>
                                <c:when test="${reply.removeStatus.code=='ing'}">
                                    已审核
                                </c:when>
                                <c:otherwise>
                                    已删除
                                </c:otherwise>
                            </c:choose>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            修改状态：
                            <select name="updatestatuscode" id="select_update_status">
                                <option value="n">通过</option>
                                <option value="y">屏蔽</option>
                            </select>
                            <input type="submit" value="提交" name="submit" class="default_button"/>
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