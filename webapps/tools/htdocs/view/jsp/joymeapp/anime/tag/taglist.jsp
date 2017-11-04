<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>大动漫标签列表</title>
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

        function del(tag_id, tag_name,removestaus) {
            var message = "你真的确定要删除吗?";
            if(removestaus=="valid"){
                message = "你真的发布吗?";
            }else if(removestaus=="invalid"){
                message = "你真的预发布吗?";
            }
            var gnl = confirm(message);
            if (!gnl) {
                return false;
            }
            window.location.href = "/joymeapp/anime/tag/delete?tag_id="+tag_id+"&tag_name="+tag_name+"&removestaus="+removestaus;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫标签列表</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="list_table_header_td">标签列表</td>
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
                <form action="/joymeapp/anime/tag/list" method="post">
                    <table width="100%">
                        <tr>
                            <td height="1" class="default_line_td">
                                标签名称:
                            </td>
                            <td height="1">
                                <input type="text" name="search_tagname" class="default_button" size="30"/>
                            </td>
                            <td>
                                是否删除：<select name="removestaus" id="removestaus">
                                <option value="" selected="selected">全部</option>
                                <option value="invalid" >预发布</option>
                                <option value="valid">发布</option>
                                <option value="removed">删除</option>
                            </select>
                            </td>
                            <td>
                                搜索分类：<select name="search_type" id="search_type">
                                                <option value="" selected="selected">全部</option>
                                            <c:forEach var="stype" items="${animeTagSearchType}">
                                                <option value="${stype.code}"><fmt:message key="anime.tag.searchtype.${stype.code}" bundle="${def}"/></option>
                                            </c:forEach>
                                         </select>
                            </td>

                            <td>
                                <input type="submit" name="button" class="default_button" value="查询"/>
                            </td>
                            <td>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
            <td>
                <table>
                    <tr>
                        <td>
                            <form action="/joymeapp/anime/tag/createpage" method="post">
                                <input type="submit" name="button" class="default_button" value="新增标签"/>
                            </form>
                        </td>

                        <td>
                            <form action="/joymeapp/anime/tag/listtree" method="post">
                                <input type="submit" name="button" class="default_button" value="查看标签树"/>
                            </form>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="12" class="default_line_td"></td>
        </tr>
        <tr class="list_table_title_tr">
            <td nowrap align="center" width="50">标签ID</td>
            <td nowrap align="center" width="150">标签名称</td>
            <td nowrap align="center" width="90">子标签列表</td>
            <td nowrap align="center" width="150">中文名</td>
            <td nowrap align="center" width="100">英文名</td>
            <td nowrap align="center" width="60">分集模式</td>
            <td nowrap align="center" width="60">状态</td>
            <td nowrap align="center" width="60">是否删除</td>
            <td nowrap align="center" width="120">创建时间</td>
            <td nowrap align="center" width="120">更新时间</td>
            <td nowrap align="center" width="100">创建人</td>
            <td nowrap align="center" width="100">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="12" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="dto" varStatus="st">
                    <tr id="socialHotContent_${dto.tag_id}"
                        class="<c:choose><c:when test="
                    ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
        <td nowrap align="center">${dto.tag_id}</td>
        <td nowrap><a href="/joymeapp/anime/tv/list?tag_id=${dto.tag_id}">${dto.tag_name}</a></td>
                        <td nowrap><a href="/joymeapp/anime/tag/list?chapter=chapter&parent_tag_id=${dto.tag_id}&parent_tag_name=${dto.tag_name}">子标签列表</a></td>
        <td nowrap>${dto.ch_name}</td>
        <td nowrap>${dto.en_name}</td>
        <td nowrap align="center">
            <fmt:message key="anime.tag.model.${dto.animeTagModel.code}" bundle="${def}"/>
        </td>
        <td nowrap align="center">
            <fmt:message key="anime.tag.type.${dto.animeTagType.code}" bundle="${def}"/>
        </td>
                        <td nowrap><fmt:message key="anime.remove.status.${dto.remove_status.code}" bundle="${def}"/></td>
        <td nowrap>${dto.create_date}</td>
        <td nowrap>${dto.update_date}</td>
                        <td nowrap>${dto.create_user}</td>
        <td nowrap>
            <a href="/joymeapp/anime/tag/modifypage?tag_id=${dto.tag_id}&pager.offset=${page.startRowIdx}">编辑</a>

            <c:if test="${dto.remove_status.code=='invalid'}">
                <a href="javascript:del('${dto.tag_id}','${dto.tag_name}','valid')">发布</a>|
                <a href="javascript:del('${dto.tag_id}','${dto.tag_name}','removed')">删除</a>
            </c:if>
            <c:if test="${dto.remove_status.code=='valid'}">
                <a href="javascript:del('${dto.tag_id}','${dto.tag_name}','removed')">删除</a>
            </c:if>
            <c:if test="${dto.remove_status.code=='removed'}">
                <a href="javascript:del('${dto.tag_id}','${dto.tag_name}','invalid')">激活</a>
            </c:if>
        </td>
        </tr>
        </c:forEach>
        <tr>
            <td height="1" colspan="12" class="default_line_td"></td>
        </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="12" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
        </c:choose>
        <tr>
            <td colspan="12" height="1" class="default_line_td"></td>
        </tr>
        <c:if test="${page.maxPage > 1}">
            <tr class="list_table_opp_tr">
                <td colspan="12">
                    <pg:pager url="/joymeapp/anime/tag/list"
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