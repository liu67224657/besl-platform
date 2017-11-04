<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>分享话题</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷分享 >> 分享话题</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">话题列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="6" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>

                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form action="/sync/sharetopic/list" method="post">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择分享基本信息:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="shareid">
                                            <option value="" >请选择</option>
                                            <c:forEach var="baseInfo" items="${shareBaseInfoList}">
                                                <option value="${baseInfo.shareId}"
                                                        <c:if test="${baseInfo.shareId == info.shareId}">selected</c:if> >${baseInfo.shareKey}</option>
                                         </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <p:privilege name="/sync/sharetopic/list">
                                        <input  type="submit" name="button" value="查询" />
                                        </p:privilege>
                                    </td>
                                    <td height="1" class=>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <c:if test="${shareid>0}">
                            <table>
                                <tr>
                                    <td>
                                        <form action="/sync/sharetopic/createpage" method="post">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <input type="hidden" name="shareid" value="${info.shareId}"/>
                                                        <p:privilege name="/sync/sharetopic/createpage">
                                                        <input type="submit" name="button" value="添加分享话题"/>
                                                        </p:privilege>
                                                    </td>
                                                </tr>
                                            </table>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                     </td>
                </tr>
             </table>
            <form action="/sync/sharetopic/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="6" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="100">分享基本信息ID</td>
                        <td nowrap align="left" width="100">话题</td>
                        <td nowrap align="center" width="120">状态</td>
                        <td nowrap align="center" width="80">创建人ID</td>
                        <td nowrap align="center" width="60">创建时间</td>
                        <td nowrap align="center" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="6" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="topic" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td >${topic.shareId}</td>
                                    <td nowrap >${topic.shareTopic}</td>
                                    <td nowrap <c:if test="${topic.removeStatus.code eq 'y'}">class="error_msg_td"</c:if>
                                            <c:if test="${topic.removeStatus.code eq 'n'}">style="color: #008000;" </c:if>><fmt:message key="share.removestatus.${topic.removeStatus.code}" bundle="${def}"/></td>
                                    <td nowrap>${topic.createUserId}</td>
                                    <td nowrap>${topic.createDate}</td>
                                    <td nowrap>
                                        <c:if test="${topic.removeStatus.code eq 'n'}">
                                            <p:privilege name="/sync/sharetopic/modifypage">
                                                <a href="/sync/sharetopic/modifypage?sharetopicid=${topic.shareTopicId}&shareid=${topic.shareId}">编辑</a>
                                            </p:privilege>
                                            <p:privilege name="/sync/sharetopic/delete">
                                                <a href="/sync/sharetopic/delete?sharetopicid=${topic.shareTopicId}&shareid=${topic.shareId}">删除</a>
                                            </p:privilege>
                                        </c:if>
                                        <c:if test="${topic.removeStatus.code eq 'y'}">
                                            <p:privilege name="/sync/sharetopic/recover">
                                                <a href="/sync/sharetopic/recover?sharetopicid=${topic.shareTopicId}&shareid=${topic.shareId}">恢复</a>
                                            </p:privilege>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="6" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="6" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="6">
                                <pg:pager url="/sync/sharetopic/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="shareid" value="${info.shareId}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>