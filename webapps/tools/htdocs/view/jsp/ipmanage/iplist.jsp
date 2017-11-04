<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@taglib prefix="ip" uri="/ip" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台IP管理、IP查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">

        var myCalendar;
        function doOnLoad(startDate, endDate) {
            myCalendar = new dhtmlXCalendarObject([startDate, endDate]);
        }

        function add() {
            window.location.href = "/cs/ipmanage/preincreaseip";
        }
    </script>
</head>

<body onload="doOnLoad('createdate','');">


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> IP管理 >> 查询屏蔽IP列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>查询屏蔽IP列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/cs/ipmanage/iplist" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td><table width="100%"  border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">IP：</td>
                                <td class="edit_table_value_td"><input name="ip" id="ip" type="text" class="default_input_singleline" size="16" maxlength="32" value="${ip}"></td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                                <td class="edit_table_value_td"><input id="createdate" name="createdate" type="text" class="default_input_singleline" size="10" maxlength="10" value="${createdate}">[格式为：2011-11-20]</td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">状态：</td>
                                <td class="edit_table_value_td"><select name="validstatus" id="all" class="default_select_single">
                                    <option value=""
                                            <c:if test="${validstatus ==''}"> selected="selected" </c:if>>--全部--</option>
                                    <option value="valid"
                                            <c:if test="${validstatus =='valid'}"> selected="selected" </c:if>>已经屏蔽</option>
                                    <option value="removed"
                                            <c:if test="${validstatus =='removed'}"> selected="selected" </c:if>>取消屏蔽</option>
                                </select></td>
                            </tr>
                        </table></td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/cs/ipmanage/preincreaseip">
                            <input type="button" value="新增被屏蔽的IP" onclick="add();" class="default_button"/>
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td align="center" width="40">选择</td>
                    <td align="left">起始IP</td>
                    <td align="left">结束IP</td>
                    <td align="center">到期时间</td>
                    <td align="center">描述</td>
                    <td align="center">状态</td>
                    <td align="left">屏蔽时间</td>
                    <td align="center">操作者</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <form action="/cs/ipmanage/batchupdate" method="POST" name="batchform">
                    <input name="ip" type="hidden" value="${ip}"/>
                    <input name="createdate" type="hidden" value="${createdate}"/>
                    <input name="validstatus" type="hidden" value="${validstatus}"/>
                    <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                    <input name="items" type="hidden" value="${page.totalRows}"/>
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <c:forEach items="${rows}" var="entity" varStatus="st">
                                <tr class="
                                    <c:choose>
                                    <c:when test="${st.index % 2 == 0}">
                                       list_table_opp_tr
                                    </c:when>
                                    <c:otherwise>
                                        list_table_even_tr
                                    </c:otherwise>
                                    </c:choose>">
                                    <td align="center">
                                        <input type="checkbox" name="ipids" value="${entity.ipId}">
                                    </td>
                                    <td align="left">
                                            ${ip:convert(entity.startIP)}
                                    </td>
                                    <td align="left">
                                            ${ip:convert(entity.endIp)}
                                    </td>
                                    <td align="center">
                                            ${entity.utillDate}
                                    </td>
                                    <td align="center">
                                            ${entity.description}
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.validstatus.${entity.status.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="left">
                                            ${entity.createDate}
                                    </td>
                                    <td align="center">
                                            ${entity.createUserid}
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="8" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="8">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].ipids, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].ipids)'>反选
                                    将选中记录删除状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <option value="valid" >启用</option>
                                        <option value="removed" >停用</option>
                                    </select>
                                    <p:privilege name="/cs/ipmanage/batchupdate">
                                        <input name="update" type="submit" class="default_button" value="批量修改">
                                    </p:privilege>
                                </td>
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
                        <tr>
                            <td colspan="8" height="1" class="list_table_opp_tr">
                                <LABEL>
                                    <pg:pager url="/cs/ipmanage/iplist"
                                              items="${page.totalRows}" isOffset="true"
                                              maxPageItems="${page.pageSize}"
                                              export="offset, currentPageNumber=pageNumber" scope="request">

                                        <pg:param name="ip" value="${ip}"/>
                                        <pg:param name="createdate" value="${createdate}"/>
                                        <pg:param name="validstatus" value="${validstatus}"/>

                                        <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                        <pg:param name="items" value="${page.totalRows}"/>
                                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                    </pg:pager>
                                </LABEL>
                            </td>
                        </tr>
                    </c:if>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>