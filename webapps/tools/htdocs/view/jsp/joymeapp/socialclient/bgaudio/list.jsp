<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、评论审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            doOnLoad();
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
        }
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 社交端管理 >> 社交端背景音乐管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">>背景音乐列表</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="50%" border="0" cellspacing="0" cellpadding="0">
    <form action="/joymeapp/social/bgaudio/list" method="post" id="form_submit_search">
        <tr>
            <td width="80" align="center">搜索条件</td>
            <td>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">歌名：</td>
                        <td>
                            <input name="qname" type="text" size="48" value="${queryName}"/>
                        </td>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                        <td>
                            <input id="startDate" name="qstarttime" type="text"
                                   class="default_input_singleline" size="16" maxlength="10"
                                   value="<fmt:formatDate value="${queryStartTime}" pattern="yyyy-MM-dd"/>">
                            -
                            <input id="endDate" name="qendtime" type="text" class="default_input_singleline"
                                   size="16" maxlength="10"
                                   value="<fmt:formatDate value="${queryEndTime}" pattern="yyyy-MM-dd"/>">
                        </td>
                    </tr>
                    <tr>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">创建人：</td>
                        <td>
                            <input type="text" name="qcreateuserid" value="${queryCreateUserId}"/>
                        </td>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">状态：</td>
                        <td>
                            <select name="qstatus">
                                <option value="">请选择</option>
                                <option value="valid" <c:if test="${queryStatus == 'valid'}">selected="selected"</c:if>>
                                    使用中
                                </option>
                                <option value="invalid"
                                        <c:if test="${queryStatus == 'invalid'}">selected="selected"</c:if>>预发布
                                </option>
                                <option value="removed"
                                        <c:if test="${queryStatus == 'removed'}">selected="selected"</c:if>>删除
                                </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">排序规则：</td>
                        <td>
                            <input type="radio" name="qordertype" value=""
                                   <c:if test="${queryOrderType == null}">checked="true"</c:if>/>无&nbsp;
                            <input type="radio" name="qordertype" value="1"
                                   <c:if test="${queryOrderType == 1}">checked="true"</c:if>/>使用数&nbsp;
                        </td>
                    </tr>
                </table>
            </td>
            <td width="80" align="center">
                <input name="Button" type="submit" class="default_button" value=" 搜索 ">
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <span style="color: #ff0000;">仅当状态为“使用中”且没有其他搜索条件时，才支持排序</span>
            </td>
        </tr>
    </form>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <form method="post" action="/joymeapp/social/bgaudio/createpage">
                <table>
                    <tr>
                        <td>
                            <p:privilege name="/joymeapp/social/bgaudio/createpage">
                            <input type="submit" name="button" class="default_button" value="添加背景音乐"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="center" width="50">音乐ID</td>
        <td nowrap align="center" width="200">歌名</td>
        <td nowrap align="center" width="200">图片</td>
        <td nowrap align="center" width="250">描述</td>
        <td nowrap align="center" width="100">歌手</td>
        <td nowrap align="center" width="100">mp3</td>
        <td nowrap align="center" width="100">wav</td>
        <td nowrap align="center" width="100">角标</td>
        <td nowrap align="center" width="50">排序</td>
        <td nowrap align="center" width="100">使用数</td>
        <td nowrap align="center" width="50">状态</td>
        <td nowrap align="center" width="100">操作</td>
        <td nowrap align="center" width="150">创建信息</td>
        <td nowrap align="center" width="50">修改信息</td>
    </tr>
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="bgaudio" varStatus="st">
                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap>${bgaudio.audioId}</td>
                    <td nowrap>${bgaudio.audioName}</td>
                    <td nowrap><img src="${bgaudio.audioPic}" height="100" width="100"/></td>
                    <td nowrap><textarea readonly="readonly"
                                         style="height: 100%;width: 100%;">${bgaudio.audioDescription}</textarea></td>
                    <td nowrap>${bgaudio.singer}</td>
                    <td nowrap>
                        <audio controls="controls">
                            <source src="${bgaudio.mp3Src}" type="audio/mpeg">
                            <embed id="embed_audio_src" src="${bgaudio.mp3Src}" controls="console" align="middle"
                               loop="false" autostart="false"
                               width="300"
                               height="30"></embed>
                        </audio>
                    </td>
                    <td nowrap>
                        <audio controls="controls">
                            <source src="${bgaudio.wavSrc}" type="audio/mpeg">
                            <embed id="embed_audio_src" src="${bgaudio.wavSrc}" controls="console" align="middle"
                               loop="false" autostart="false"
                               width="300"
                               height="30"></embed>
                        </audio>
                    </td>
                    <td nowrap><fmt:message key="subscript.type.${bgaudio.subscript.type}" bundle="${def}"/>
                        <br/>${bgaudio.subscript.startDate}
                        <br/>${bgaudio.subscript.endDate}
                    </td>
                    <td nowrap align="center">
                        <c:if test="${fn:length(queryName) <= 0 && fn:length(queryCreateUserId) <= 0 && queryStatus == 'valid' && queryOrderType == null}">
                            <a href="/joymeapp/social/bgaudio/sort/up?bgaid=${bgaudio.audioId}&qname=${queryName}&qstarttime=${queryStartTime}&qendtime=${queryEndTime}&qcreateuserid=${queryCreateUserId}&qstatus=${queryStatus}&qordertype=${queryOrderType}&pager.offset=${page.startRowIdx}">
                                <img src="/static/images/icon/up.gif"></a>
                            <a href="/joymeapp/social/bgaudio/sort/down?bgaid=${bgaudio.audioId}&qname=${queryName}&qstarttime=${queryStartTime}&qendtime=${queryEndTime}&qcreateuserid=${queryCreateUserId}&qstatus=${queryStatus}&qordertype=${queryOrderType}&pager.offset=${page.startRowIdx}">
                                <img src="/static/images/icon/down.gif"></a>
                        </c:if>
                    </td>
                    <td nowrap>${bgaudio.useSum}</td>
                    <td nowrap
                            <c:choose>
                                <c:when test="${bgaudio.removeStatus.code eq 'valid'}">style="color: #008000;" </c:when>
                                <%--<c:when test="${bgaudio.removeStatus.code eq 'invalid'}">style="color: #ffff00;" </c:when>--%>
                                <c:otherwise>style="color: #ff0000;"</c:otherwise>
                            </c:choose>>
                        <fmt:message key="social.bgaudio.status.${bgaudio.removeStatus.code}" bundle="${def}"/>
                    </td>
                    <td nowrap>
                        <c:choose>
                            <c:when test="${bgaudio.removeStatus.code eq 'valid'}">
                                <p:privilege name="/joymeapp/social/bgaudio/remove">
                                    <a href="/joymeapp/social/bgaudio/remove?bgaid=${bgaudio.audioId}&qname=${queryName}&qstarttime=${queryStartTime}&qendtime=${queryEndTime}&qcreateuserid=${queryCreateUserId}&qstatus=${queryStatus}&qordertype=${queryOrderType}&pager.offset=${page.startRowIdx}">删除</a>
                                </p:privilege>
                            </c:when>
                            <c:when test="${bgaudio.removeStatus.code eq 'invalid'}">
                                <p:privilege name="/joymeapp/social/bgaudio/publish">
                                    <a href="/joymeapp/social/bgaudio/publish?bgaid=${bgaudio.audioId}&qname=${queryName}&qstarttime=${queryStartTime}&qendtime=${queryEndTime}&qcreateuserid=${queryCreateUserId}&qstatus=${queryStatus}&qordertype=${queryOrderType}&pager.offset=${page.startRowIdx}">发布</a>
                                </p:privilege>
                            </c:when>
                            <c:otherwise>
                                <p:privilege name="/joymeapp/social/bgaudio/recover">
                                    <a href="/joymeapp/social/bgaudio/recover?bgaid=${bgaudio.audioId}&qname=${queryName}&qstarttime=${queryStartTime}&qendtime=${queryEndTime}&qcreateuserid=${queryCreateUserId}&qstatus=${queryStatus}&qordertype=${queryOrderType}&pager.offset=${page.startRowIdx}">恢复</a>
                                </p:privilege>
                            </c:otherwise>
                        </c:choose>
                        &nbsp;
                        <p:privilege name="/joymeapp/social/bgaudio/modifypage">
                            <a href="/joymeapp/social/bgaudio/modifypage?bgaid=${bgaudio.audioId}&qname=${queryName}&qstarttime=${queryStartTime}&qendtime=${queryEndTime}&qcreateuserid=${queryCreateUserId}&qstatus=${queryStatus}&qordertype=${queryOrderType}&pager.offset=${page.startRowIdx}">编辑</a>
                        </p:privilege>
                    </td>
                    <td nowrap><fmt:formatDate value="${bgaudio.createDate}"
                                               pattern="yyyy-MM-dd"/><br/>${bgaudio.createIp}<br/>${bgaudio.createUserId}
                    </td>
                    <td nowrap><fmt:formatDate value="${bgaudio.modifyDate}"
                                               pattern="yyyy-MM-dd"/><br/>${bgaudio.modifyIp}<br/>${bgaudio.modifyUserId}
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="14" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="14" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="14" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="14">
                <c:set var="startDateStr">
                    <fmt:formatDate value="${queryStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </c:set>
                <c:set var="endDateStr">
                    <fmt:formatDate value="${queryEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </c:set>
                <LABEL>
                    <pg:pager url="/joymeapp/social/bgaudio/list"
                              items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber" scope="request">
                        <pg:param name="qname" value="${queryName}"/>
                        <pg:param name="qstarttime" value="${startDateStr}"/>
                        <pg:param name="qendtime" value="${endDateStr}"/>
                        <pg:param name="qcreateuserid" value="${queryCreateUserId}"/>
                        <pg:param name="qstatus" value="${queryStatus}"/>
                        <pg:param name="qordertype" value="${queryOrderType}"/>
                        <pg:param name="currentPageNumber" value="${page.curPage}"/>
                        <pg:param name="maxPageItems" value="${page.pageSize}"/>
                        <pg:param name="items" value="${page.totalRows}"/>
                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                    </pg:pager>
                </LABEL>
            </td>
        </tr>
    </c:if>
</table>
</td>
</tr>
</table>
</body>
</html>