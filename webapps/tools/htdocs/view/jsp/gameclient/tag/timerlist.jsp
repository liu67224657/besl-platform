<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>定时发布列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function() {
            $('a[id^="a_update_timer_"]').click(function(){
                var idx = $(this).attr('id').replace('a_update_timer_', '');
                var timer = $('#timerdate_'+idx).val();
                if(timer.length <= 0){
                    alert("请选择时间");
                    return false;
                }
                var now = new Date();
                var date = new Date(timer.replace("-", "/").replace("-", "/"));
                if(date.getTime() < (now.getTime() + 30*60*1000)){
                    alert("延迟时间最少30分钟");
                    return false;
                }
                var tagid = "${tagid}";
                var tdid = $(this).attr('data-tdid');
                window.location.href="/gameclient/tag/dede/batchtimer?tagid="+tagid+"&ids="+tdid+"&pulishtime="+timer+"&pageStartIndex=${page.startRowIdx}";
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 标签管理</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">定时发布列表</td>
    </tr>
</table>
    <table>
        <tr>
            <td><a href="/gameclient/tag/dede/list?tagid=${tagid}"><input type="button" class="default_button" name="button" value="全部列表"/></a></td>
            <td><a href="/gameclient/tag/dede/timerlist?tagid=${tagid}"><input type="button" class="default_button" name="button" value="定时发布列表"/></a></td>
        </tr>
    </table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="12" class="error_msg_td">
                    ${errorMsg}
            </td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
    <%--<table width="100%" border="0" cellspacing="0" cellpadding="0" >--%>
        <%--<tr class="toolbar_tr">--%>
            <%--<td width="28%">--%>
                <%--<input type="checkbox" name="checkall" id="checkall">全选--%>
                <%--&nbsp;&nbsp;--%>
                <%--<input type="text" class="Wdate"--%>
                       <%--onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:00'})"--%>
                       <%--readonly="readonly" name="starttime" id="starttime"/>--%>
                <%--<input type="button" id="batchtimer" value="定时发布" class="default_button"/>--%>
            <%--</td>--%>
            <%--<td width="4%">--%>

            <%--</td>--%>
        <%--</tr>--%>
    <%--</table>--%>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="11" class="default_line_td"></td>
</tr>
<tr class="list_table_title_tr">
    <%--<td nowrap align="center"></td>--%>
    <td nowrap align="center">文章ID</td>
    <td nowrap align="center">定时时间</td>
    <td nowrap align="center">标题</td>
    <td nowrap align="center">发布时间</td>
    <td nowrap align="center">类型</td>
    <td nowrap align="center">阅读数</td>
    <td nowrap align="center">赞数</td>
    <td nowrap align="center">平台</td>
    <td nowrap align="center">操作</td>
</tr>
<tr>
    <td height="1" colspan="11" class="default_line_td"></td>
</tr>
<c:choose>
    <c:when test="${list.size() > 0}">
        <c:forEach items="${list}" var="dto" varStatus="st">
            <tr id="socialHotContent_${dto.id}"
                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <%--<td align="center">--%>
                    <%--<input type="checkbox" name="tdid" value="${dto.id}_${dto.dede_archives_id}" id="tdid_${st.index}">--%>
                <%--</td>--%>
                <td nowrap align="center">${dto.dede_archives_id}</td>
                <td nowrap align="center">
                        ${dto.timerDate}
                </td>
                <td nowrap align="left">
                    <c:if test="${dto.archiveContentType.code==0}">
                        ${dto.dede_archives_title}
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <p style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;width: 250px;">
                                    <c:out value="${bean.value.description}"/></p>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </td>
                <td nowrap align="center">
                    <c:if test="${dto.archiveContentType.code==0}">
                        ${dto.dede_archives_pubdate_str}
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.createTime}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </td>
                <td nowrap align="center">
                    <fmt:message key="gameclient.tagdedearchives.type.${dto.tagDisplyType.code}" bundle="${def}"/>
                </td>
                <td nowrap align="center" name="read_num">
                    <c:if test="${dto.archiveContentType.code==0}">
                        <c:forEach items="${cheatMap}" var="cheat"><c:if
                                test="${cheat.key==dto.dede_archives_id}"><c:out
                                value="${cheat.value.read_num}"/></c:if></c:forEach>
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.longCommentSum}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </td>
                <td nowrap align="center">
                    <c:if test="${dto.archiveContentType.code==0}">
                        <c:forEach items="${cheatMap}" var="cheat"><c:if
                                test="${cheat.key==dto.dede_archives_id}"><c:out
                                value="${cheat.value.agree_num}"/></c:if></c:forEach>
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.scoreCommentSum}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </td>
                <td nowrap align="center">
                    <c:if test="${dto.dede_archives_showios==1 && dto.dede_archives_showandroid==1}">
                        全部
                    </c:if>
                    <c:if test="${dto.dede_archives_showios!=1 && dto.dede_archives_showandroid!=1}">
                    </c:if>
                    <c:if test="${dto.dede_archives_showandroid==1 && dto.dede_archives_showios!=1}">
                        Android
                    </c:if>
                    <c:if test="${dto.dede_archives_showios==1 && dto.dede_archives_showandroid!=1 }">
                        IOS
                    </c:if>
                </td>
                <td>
                    <input type="text" class="Wdate" onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:00'})" readonly="readonly" id="timerdate_${st.index}"/>
                    <a href="javascript:void (0)" id="a_update_timer_${st.index}" data-tdid="${dto.id}_${dto.dede_archives_id}">修改定时任务</a>
                    <a href="/gameclient/tag/dede/deltimer?tagid=${tagid}&ids=${dto.id}&timerdate=${dto.timerDate}&pageStartIndex=${page.startRowIdx}" onclick="return confirm('确定要删除吗？')">删除定时任务</a>
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
            <pg:pager url="/gameclient/tag/dede/list"
                      items="${page.totalRows}" isOffset="true"
                      maxPageItems="${page.pageSize}"
                      export="offset, currentPageNumber=pageNumber" scope="request">
                <pg:param name="tagid" value="${tagid}"/>
                <pg:param name="platform" value="${platform}"/>
                <pg:param name="title" value="${title}"/>
                <pg:param name="removestaus" value="${removestaus}"/>
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