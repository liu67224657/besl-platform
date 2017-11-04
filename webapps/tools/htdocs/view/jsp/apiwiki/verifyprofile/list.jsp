<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <title>虚拟用户-wikiapp列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#search_form').submit(function () {

            })

            $('.deletelink').click(function () {
                if (!confirm('确定要取消该用户的认证么！该操作不可逆')) {
                    return false;
                }
                var uid=$(this).attr('data-uid');
                $.get("/apiwiki/verifyprofile/json/delete?uid="+uid,null,function(req){
                   if(req=='success'){
                       $('#value_tr_'+uid).remove();
                   }else{
                       alert(req);
                   }
                });

            })
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 虚拟用户管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        <form action="/apiwiki/verifyprofile/createpage" method="post">
                            <input type="submit" name="button" class="default_button" id="default_button2"
                                   value="认证用户"/>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${errorMsg != null}">
                    <tr>
                        <td height="1" colspan="8" class="default_line_td">
                            <fmt:message key="${errorMsg}" bundle="${errorProps}" />
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">ID</td>
                    <td nowrap align="center" width="">昵称</td>
                    <td nowrap align="center">认证信息</td>
                    <%--<td nowrap align="center" width="80px">认证类型</td>--%>
                    <td nowrap align="center" width="80px">认证时间</td>
                    <td nowrap align="center" width="80px">QQ</td>
                    <td nowrap align="center" width="80px">微信</td>
                    <td nowrap align="center" width="80px">电话</td>
                    <td nowrap align="center" width="120px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="value_tr_${dto.id}" class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td>${dto.id}</td>
                                <td>${dto.nick}</td>
                                <td>${dto.verifyInfo}</td>
                                    <%--<td><fmt:message key="verifyprofile.type.${dto.verifyType}"  bundle="${toolsProps}"/></td>--%>
                                <td><fmt:formatDate value="${dto.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td>${dto.qq}</td>
                                <td>${dto.microMsg}</td>
                                <td>${dto.mobile}</td>
                                <td>
                                    <a href="/apiwiki/verifyprofile/updatepage?uid=${dto.id}">修改认证信息</a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a class="deletelink" data-uid="${dto.id}" href="javascript:void(0);">取消认证</a>
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
                        <td colspan="9">
                            <pg:pager url="/apiwiki/verifyprfile/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="type" value="${type}"/>
                                <pg:param name="searchText" value="${searchText}"/>
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
