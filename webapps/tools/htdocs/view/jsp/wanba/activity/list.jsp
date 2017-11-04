<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            //置顶
            $('a[name=top]').click(function () {
                var type = $(this).attr('data-type');
                var tag_id = $(this).attr('data-tagid');

                $.post("/wanba/activity/top", {type: type,tag_id:tag_id}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        window.location.reload();
                    } else {
                        alert(rsobj.msg);
                    }
                });
            });
        });
    </script>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 活动管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">活动列表</td>
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
                                    <form action="/wanba/activity/createpage" method="post">
                                        <input type="hidden" name="apptype" class="default_button" value="${apptype}"/>
                                        <input type="submit" name="button" class="default_button" value="新增活动"/>
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
                    <td nowrap align="center">标签ID</td>
                    <td nowrap align="center">标签名称</td>
                    <td nowrap align="center">是否删除</td>
                    <td nowrap align="center">创建时间</td>
                    <td nowrap align="center">更新时间</td>
                    <td nowrap align="center">创建人</td>
                    <td nowrap align="center" width="150px">排序操作</td>
                    <td nowrap align="center" width="150px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.tag_id}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.tag_id}</td>
                                <td nowrap>${dto.tag_name}</td>
                                <td nowrap><fmt:message key="anime.remove.status.${dto.remove_status.code}"  bundle="${def}"/></td>
                                <td nowrap>${dto.create_date}</td>
                                <td nowrap>${dto.update_date}</td>
                                <td nowrap>${dto.create_user}</td>
                                <td nowrap>
                                    <a href="/wanba/activity/sort?desc=down&tag_id=${dto.tag_id}"><img
                                            src="/static/images/icon/up.gif"></a>
                                    <a href="/wanba/activity/sort?desc=up&tag_id=${dto.tag_id}"><img
                                            src="/static/images/icon/down.gif"></a>


                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${dto.display_order>0}">
                                        <a href="javascript:void(0);" data-type="1" data-tagid="${dto.tag_id}" name="top">置顶</a>
                                    </c:if>
                                    <c:if test="${dto.display_order<0}">
                                        <a href="javascript:void(0);" data-type="2" data-tagid="${dto.tag_id}" name="top">取消置顶</a>
                                    </c:if>
                                </td>
                                <td nowrap>

                                    <a href="/wanba/activity/modifypage?apptype=${apptype}&tag_id=${dto.tag_id}">编辑</a>

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
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/wanba/activity/list"
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