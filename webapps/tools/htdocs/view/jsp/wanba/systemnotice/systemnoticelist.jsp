<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>系统通知列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        function removeSystemNoticeId(systemNoticeId) {
            var msg = "您确定要删除,请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/wanba/systemnotice/remove?systemNoticeId=" + systemNoticeId;
            }
        }
    </script>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 系统通知管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">系统通知列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                                ${errorMsg}
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

                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <form action="/wanba/systemnotice/createpage" method="post">
                                        <input type="submit" name="button" class="default_button" value="新增"/>
                                    </form>
                                </td>

                                <td>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">系统ID</td>

                    <td nowrap align="center">通知标题</td>
                    <td nowrap align="center">通知文本</td>
                    <td nowrap align="center">平台</td>
                    <td nowrap align="center">版本</td>
                    <td nowrap align="center">appkey</td>
                    <td nowrap align="center">创建时间</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.systemNoticeId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.systemNoticeId}</td>
                                <td nowrap>${dto.title}</td>
                                <td nowrap>${dto.text}</td>
                                <td nowrap>
                                    <c:if test="${dto.platform==0}">
                                      IOS
                                    </c:if>
                                    <c:if test="${dto.platform==1}">
                                        Android
                                    </c:if>
                                </td>
                                <td nowrap>${dto.version}</td>
                                <td nowrap>${dto.appkey}</td>
                                <td nowrap><fmt:formatDate value="${dto.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td nowrap>
                                    <a href="/wanba/systemnotice/modifypage?systemNoticeId=${dto.systemNoticeId}">编辑</a>|
                                    <a  href='javascript:;' onclick='removeSystemNoticeId(${dto.systemNoticeId});' >删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="7" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/wanba/systemnotice/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>