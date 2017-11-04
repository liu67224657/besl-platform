<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>吐槽列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#focusBatch').bind("submit", function() {
//                var srcScreenname = $.trim($('#srcScreenname').val());
//                if (srcScreenname.length == 0) {
//                    alert("用户昵称");
//                    return false;
//                }


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 排行榜长评维护列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
<td class="list_table_header_td">${forignContent.contentTitle}:<c:if test="${type eq 'gag'}">吐槽</c:if><c:if test="${type ne 'gag'}">短评</c:if>评论列表</td>
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
                        <form action="/joymeapp/mobilegame/reply/list" method="post">
                            <input type="hidden" name="id" value="${id}"/>
                            <input type="hidden" name="type" value="${type}"/>
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                       内容:
                                    </td>
                                    <td height="1">
                                        <input type="text" name="word" class="default_button" size="30"/>
                                    </td>
                                    <td height="1">
                                        <select name="removeStaus" id="select_channel">
                                            <option value="">全部</option>
                                            <option value="n" selected>可用</option>
                                            <option value="y">已删除</option>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="查询"/>
                                    </td>

                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <form action="/joymeapp/mobilegame/reply/createpage" method="post">
                                        <input type="hidden" name="id" value="${id}"/>
                                        <input type="hidden" name="type" value="${type}"/>
                                        <input type="hidden" name="contentid" value="${contentid}"/>
                                        <c:if test="${type eq 'gag'}"><input type="submit" name="button" class="default_button" value="新增吐槽"/></c:if>
                                        <c:if test="${type ne 'gag'}"><input type="submit" name="button" class="default_button" value="新增短评"/></c:if>

                                    </form>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="100">ID</td>
                    <td nowrap align="center" width="300">内容</td>
                    <td nowrap align="center" width="150">用户昵称</td>
                    <td nowrap align="center" width="100">是否删除</td>
                    <td nowrap align="center" width="150">创建IP</td>
                    <td nowrap align="center" width="150">创建时间</td>
                    <td nowrap align="center" width="100">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.replyId}"
                                class="<c:choose><c:when test="
                    ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${dto.replyId}</td>
                                <td nowrap width="300">${dto.body}</td>
                                <td nowrap>${dto.replyUno}</td>
                                <td nowrap><fmt:message key="joymeapp.version.status.${dto.removeStatus.code}" bundle="${def}"/></td>
                                <td nowrap>${dto.createIp}</td>
                                <td nowrap>${dto.createTime}</td>
                                <td nowrap>
                                    <c:if test="${dto.removeStatus.code=='n'}">
                                            <a href="/joymeapp/mobilegame/reply/modify?del=y&replyid=${dto.replyId}&id=${id}&pager.offset=${page.startRowIdx}&contentid=${contentid}&listtype=${type}">删除</a>
                                    </c:if>
                                    <c:if test="${dto.removeStatus.code=='y'}">
                                            <a href="/joymeapp/mobilegame/reply/modify?del=n&replyid=${dto.replyId}&id=${id}&pager.offset=${page.startRowIdx}&contentid=${contentid}&listtype=${type}">恢复</a>
                                    </c:if>
                                    <c:if test="${dto.display_order>=0}">
                                        <a href="/joymeapp/mobilegame/reply/modify?replyid=${dto.replyId}&id=${id}&pager.offset=${page.startRowIdx}&type=up&contentid=${contentid}&listtype=${type}">置顶</a>
                                    </c:if>
                                    <c:if test="${dto.display_order<0}">
                                        <a href="/joymeapp/mobilegame/reply/modify?replyid=${dto.replyId}&id=${id}&pager.offset=${page.startRowIdx}&type=down&contentid=${contentid}&listtype=${type}">取消置顶</a>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="10" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="9" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="9">
                            <pg:pager url="/joymeapp/mobilegame/reply/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="id" value="${id}"/>
                                <pg:param name="type" value="${type}"/>
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