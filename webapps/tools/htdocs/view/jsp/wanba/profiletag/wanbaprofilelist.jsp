<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>达人列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script>
        function profilesort(profileid, tagid) {
            var sort = $("#sort" + profileid).val();
            if (sort == '') {
                alert("请填写移动的数字");
                return false;
            }
            window.location.href = "/wanba/profiletag/profilesort?tagid=" + tagid + "&sort=" + sort + "&profileid=" + profileid;
        }

    </script>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 达人列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">达人列表</td>
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
                                    <form action="/wanba/profiletag/addprofilepage" method="post">
                                        <input type="hidden" name="tagid" value="${animeTag.tag_id}" size="48"/>
                                        ${animeTag.tag_name}&nbsp;
                                        <c:if test="${animeTag.tag_id ne -20000}"><input type="submit" name="button"
                                                                          class="default_button" value="新增达人"/>
                                        </c:if>
                                    </form>
                                </td>

                                <td>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="700px;" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">达人profileid</td>
                    <td nowrap align="center">昵称</td>
                    <td nowrap align="center">认证类型</td>
                    <td nowrap align="center">提问积分</td>
                    <td nowrap align="center">排序操作</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="
                                               ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center" width="100px;">${dto.profileId}</td>
                                <td nowrap>${profileMap[dto.profileId].nick}
                                </td>
                                <td nowrap>
                                    <c:forEach items="${wanbaVerifyList}" var="verify">
                                        <c:if test="${verify.verifyId eq dto.verifyType}">
                                            ${verify.verifyName}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td nowrap>${dto.askPoint}</td>
                                <td nowrap>
                                    <form action="/wanba/profiletag/profilesort" method="post">
                                        <input type="hidden" name="profileid" value="${dto.profileId}"/>
                                        <input type="hidden" name="tagid" value="${animeTag.tag_id}" size="48"/>
                                        移动到第<input type="text" size="4" name="sort" id="sort${dto.profileId}"
                                                   onkeyup="value=value.replace(/[^\d]/g,'')"/>位<input type="button"
                                                                                                       onclick="profilesort('${dto.profileId}','${animeTag.tag_id}')"
                                                                                                       value="确定"/>
                                    </form>

                                </td>
                                <td nowrap>
                                    <a href="/wanba/profiletag/deleteprofile?tagid=${animeTag.tag_id}&pid=${dto.profileId}">删除</a>
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
                            <pg:pager url="/wanba/profiletag/querybytagid"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">

                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="tagid" value="${animeTag.tag_id}"/>
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