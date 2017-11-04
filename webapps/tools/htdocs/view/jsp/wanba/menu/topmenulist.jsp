<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>轮播图</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function () {
            $("#default_button2").bind('click',function(){
                if($("#wanbamenudomain_1").val()=="-1"){
                    alert("请选择一个标签添加")
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 标签轮播图</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">标签轮播图</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
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

                    </td>
                    <td>
                        <table>
                            <%--<tr>--%>
                                <%--<td>--%>
                                    <%--<form action="/wanba/menu/list?channelid=1" method="post">--%>
                                        <%--<input type="submit" name="button" class="default_button" value="热门轮播图列表"/>--%>
                                    <%--</form>--%>
                                <%--</td>--%>
                                <%--<td>--%>

                                    <%--<form action="/wanba/menu/createpage?wanbamenudomain=1" method="post">--%>
                                        <%--<input type="submit" name="button" class="default_button" value="添加热门轮播图"/>--%>
                                    <%--</form>--%>
                                <%--</td>--%>

                            <%--</tr>--%>
                            <tr>
                                <td>
                                    <form action="/wanba/menu/list" method="post">
                                        状态：
                                        <select name="validstatus">
                                            <option value="-1">全部</option>
                                            <option value="valid" <c:if test="${validstatus=='valid'}">selected</c:if>>可用</option>
                                            <option value="invalid" <c:if test="${validstatus=='invalid'}">selected</c:if>>不可用</option>
                                        </select>
                                        <select name="channelid" id="channelid_1">
                                            <option value="">请选择</option>
                                            <c:forEach items="${animeTagList}" var="tag">
                                                <option value="${tag.tag_id}" <c:if test="${tag.tag_id==channelid}">selected</c:if>>${tag.tag_name}</option>
                                            </c:forEach>
                                        </select>
                                        <input type="submit" name="button" class="default_button" id="default_button1" value="标签广告列表"/>
                                    </form>
                                </td>
                                <td>
                                    <form action="/wanba/menu/createpage" method="post">
                                        <select name="wanbamenudomain" id="wanbamenudomain_1">
                                            <option value="-1">请选择</option>
                                            <c:forEach items="${animeTagList}" var="tag">
                                                <option value="${tag.tag_id}">${tag.tag_name}</option>
                                            </c:forEach>
                                        </select>
                                        <input type="submit" name="button" class="default_button" id="default_button2" value="添加标签广告"/>
                                    </form>
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
                    <td nowrap align="center" width="80">ID</td>
                    <td nowrap align="center" width="">菜单名称</td>
                    <td nowrap align="center" width="">链接</td>
                    <td nowrap align="center" width="">平台</td>
                    <td nowrap align="center" width="">所属标签ID</td>
                    <td nowrap align="center" width="">排序操作</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>${dto.activityTopMenuId}</td>

                <td nowrap>${dto.menuName}</td>
                <td nowrap>${dto.linkUrl}</td>
                <td nowrap>
                    <c:if test="${dto.platform==0}">
                        IOS
                    </c:if>
                    <c:if test="${dto.platform==1}">
                        Android
                    </c:if>
                </td>
                <td nowrap>${dto.channelId}</td>
                <td nowrap>
                    <a href="/wanba/menu/sort?sort=up&activitytopmenuid=${dto.activityTopMenuId}&channelid=${dto.channelId}"><img
                            src="/static/images/icon/up.gif"></a>
                    <a href="/wanba/menu/sort?sort=down&activitytopmenuid=${dto.activityTopMenuId}&channelid=${dto.channelId}"><img
                            src="/static/images/icon/down.gif"></a>
                </td>
                <td nowrap>
                    <c:if test="${dto.validStatus.code=='valid'}">
                        可用
                    </c:if>
                    <c:if test="${dto.validStatus.code=='invalid'}">
                        不可用
                    </c:if>
                </td>

                <td nowrap>
                    <a href="/wanba/menu/modifypage?activitytopmenuid=${dto.activityTopMenuId}">编辑</a>&nbsp;|

                    <c:if test="${dto.validStatus.code=='valid'}">
                        <a href="/wanba/menu/remove?activitytopmenuid=${dto.activityTopMenuId}&validStatus=invalid&channelid=${channelid}&validstatus=${validstatus}">删除</a>
                    </c:if>
                    <c:if test="${dto.validStatus.code=='invalid'}">
                        <a href="/wanba/menu/remove?activitytopmenuid=${dto.activityTopMenuId}&validStatus=valid&channelid=${channelid}&validstatus=${validstatus}">恢复</a>
                    </c:if>
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
                        <td colspan="10">
                            <pg:pager url="/wanba/menu/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="channelid" value="${channelid}"/>
                                <pg:param name="validstatus" value="${validstatus}"/>
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