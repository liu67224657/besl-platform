<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

        });
    </script>
    <style>
        .name {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .target {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 游戏标签列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form action="/gamev2/tag/list" method="post">
                            标签名称
                            <input type="text" name="tagName" class="default_button" value="${tagName}"
                                   size="20"/>
                            状态：<select name="validStatus">
                            <option value="" <c:if test="${validStatus==''}">selected</c:if>>全部</option>
                            <option value="valid" <c:if test="${validStatus=='valid'}">selected</c:if>>有用</option>
                            <option value="invalid" <c:if test="${validStatus=='invalid'}">selected</c:if>>禁用</option>
                        </select>

                            <input type="submit" name="button" class="default_button" value="搜索"/>
                        </form>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        <form action="/gamev2/tag/createpage" method="post">
                            <input type="submit" name="button" class="default_button" id="default_button2"
                                   value="添加游戏标签"/>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">标签ID</td>
                    <td nowrap align="center" width="">标签名称</td>
                    <td nowrap align="center" width="">对应游戏</td>
                    <td nowrap align="center" width="">是否推荐</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.id}</td>
                                <td nowrap class="name" title="${dto.tagName}">${dto.tagName}</td>

                                <td nowrap align="center">
                                    <c:if test="${dto.gameSum>0}">
                                        <a href="/gamev2/tag/gamelist?id=${dto.id}">${dto.gameSum}</a>
                                    </c:if>
                                    <c:if test="${dto.gameSum<=0}">
                                        ${dto.gameSum}
                                    </c:if>
                                </td>
                                <td nowrap align="center">
                                    <c:if test="${dto.recommendStatus==0}">否</c:if>
                                    <c:if test="${dto.recommendStatus==1}">
                                        <div style="color: red">是</div>
                                    </c:if>
                                </td>
                                <td nowrap align="center">
                                    <fmt:message key="gametag.validstatus.${dto.validStatus}" bundle="${toolsProps}"/>
                                </td>


                                <td nowrap align="center">
                                    <a href="/gamev2/tag/modifypage?id=${dto.id}">编辑</a>&nbsp;&nbsp;&nbsp;
                                    |
                                    <c:if test="${dto.validStatus=='VALID'}">
                                        <a href="/gamev2/tag/modifystatus?id=${dto.id}&valid=invalid">禁用</a>
                                    </c:if>
                                    <c:if test="${dto.validStatus=='UNVALID'}">
                                        <a href="/gamev2/tag/modifystatus?id=${dto.id}&valid=valid">启用</a>
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
                            <pg:pager url="/gamev2/tag/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="tagName" value="${tagName}"/>
                                <pg:param name="validStatus" value="${validStatus}"/>
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