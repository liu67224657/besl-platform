<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>标签游戏列表</title>
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 标签游戏列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">游戏ID</td>
                    <td nowrap align="center" width="">游戏名称</td>
                    <td nowrap align="center" width="">开发商</td>
                    <td nowrap align="center" width="70px">WIKIKEY</td>
                </tr>
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.gameDbId}</td>
                                <td nowrap class="target"><a
                                        href="/gamedb/updatepage?gamedbid=${dto.gameDbId}">${dto.gameName}</a></td>
                                <td nowrap class="target">${dto.gameDeveloper}</td>
                                <td nowrap class="target">${dto.wikiKey}</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="4" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="4" height="1" class="default_line_td"></td>
                </tr>
                <tr class="list_table_opp_tr">
                    <td colspan="6">
                        <c:if test="${next!=''}">
                            <a href="${next}">下一页</a>
                        </c:if>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
</body>
</html>