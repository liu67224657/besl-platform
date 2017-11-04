<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>海贼迷节目列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#focusBatch').bind("submit", function() {
                if($("#tv_id").val() != "") {
                    if(isNaN($("#tv_id").val())){
                        alert("视频ID需要填写数字。");
                        $("#tv_id").focus();
                        return false;
                    }
                }
            });

            $("#showurl").focusout(function() {
                var phpurl="http://hezuo.joyme.com/api/spider.php";
                var userurl=$("#showurl").val();
                $.ajax({
                    url:"/joymeapp/anime/tv/get",
                    type:'post',
                    data:{'url':phpurl+"?url="+userurl},
                    async:false,
                    dataType: "json",
                    success:function (data) {
                        try {
                            var jsonStr = JSON.parse(data);
                            $("#url").val(jsonStr.result.url);
                        } catch (e) {
                        }
                    }
                });
            });
        });

        function del(tv_id,removestaus) {
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
            window.location.href = "/joymeapp/anime/tv/modify?remove=remove&tv_id="+tv_id+"&removestaus="+removestaus+"&pager.offset=${page.startRowIdx}";
        }


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫视频列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="list_table_header_td">视频列表</td>
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
                <form action="/joymeapp/anime/tv/list" method="post" id="focusBatch">
                    <input type="hidden" name="search" class="default_button" size="30" value="search" />
                    <table width="100%">
                        <tr>
                            <td height="1" class="default_line_td">
                                视频ID:
                            </td>
                            <td height="1">
                                <input type="text" name="tv_id" class="default_button" size="30" id="tv_id"/>
                            </td>
                            <td height="1" class="default_line_td">
                                视频名:
                            </td>
                            <td height="1">
                                <input type="text" name="tv_name" class="default_button" size="30"/>
                            </td>

                            <td height="1" class="default_line_td">
                                URL:
                            </td>
                            <td height="1">
                                <input type="text"  id="showurl" name="showurl" class="default_button" size="35"/>
                                <input type="hidden"  id="url" name="url" class="default_button" size="35"/>
                            </td>
                            <td>
                                是否删除：<select name="removestaus" id="removestaus">
                                <option value="" selected="selected">全部</option>
                                <option value="invalid" >预发布</option>
                                <option value="valid">发布</option>
                                <option value="removed">删除</option>
                            </select>
                            <td>
                                视频来源：<select name="domain" id="domain">
                                        <option value="" selected="selected">全部</option>
                                        <c:forEach var="stype" items="${animeTVDomain}">
                                            <option value="${stype.code}"><fmt:message key="anime.tv.domain.${stype.code}" bundle="${def}"/></option>
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
                            <form action="/joymeapp/anime/tv/createpage" method="post">
                                <input type="submit" name="button" class="default_button" value="新增视频"/>
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
            <td nowrap align="center" width="50">视频ID</td>
            <td nowrap align="center" width="150">视频名</td>
            <td nowrap align="center" width="150">采集url地址</td>
            <td nowrap align="center" width="80">来源</td>
            <td nowrap align="center" width="60">是否删除</td>
            <td nowrap align="center" width="120">创建时间</td>
            <td nowrap align="center" width="120">更新时间</td>
            <td nowrap align="center" width="100">创建人</td>
            <td nowrap align="center" width="80">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="10" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="dto" varStatus="st">
                    <tr id="socialHotContent_${dto.tv_id}"
                        class="<c:choose><c:when test="
                    ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td nowrap align="center">${dto.tv_id}</td>
                        <td nowrap align="left">${dto.tv_name}</td>
                        <td nowrap>${dto.url}</td>
                        <td nowrap><fmt:message key="anime.tv.domain.${dto.domain.code}" bundle="${def}"/></td>
                        <td nowrap><fmt:message key="anime.remove.status.${dto.remove_status.code}" bundle="${def}"/></td>
                        <td nowrap>${dto.create_date}</td>
                        <td nowrap>${dto.update_date}</td>
                        <td nowrap>${dto.create_user}</td>
                        <td nowrap>
                            <a href="/joymeapp/anime/tv/modifypage?tv_id=${dto.tv_id}&pager.offset=${page.startRowIdx}">编辑</a>
                            <c:if test="${dto.remove_status.code=='invalid'}">
                                <a href="javascript:del('${dto.tv_id}','valid')">发布</a>|
                                <a href="javascript:del('${dto.tv_id}','removed')">删除</a>
                            </c:if>
                            <c:if test="${dto.remove_status.code=='valid'}">
                                <a href="javascript:del('${dto.tv_id}','removed')">删除</a>
                            </c:if>
                            <c:if test="${dto.remove_status.code=='removed'}">
                                <a href="javascript:del('${dto.tv_id}','invalid')">激活</a>
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
            <td colspan="10" height="1" class="default_line_td"></td>
        </tr>
        <c:if test="${page.maxPage > 1}">
            <tr class="list_table_opp_tr">
                <td colspan="10">
                    <pg:pager url="/joymeapp/anime/tv/list"
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