<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、资源查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">

        function add() {
            window.location.href = "/privilege/res/createrespage";
        }
    </script>
</head>

<body>


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">系统管理 >> 权限管理 >> 操作权限管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>资源查询列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/privilege/res/reslist" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td><table width="100%"  border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">资源名字：</td>
                                <td class="edit_table_value_td"><input name="rsname" id="rsname" type="text" class="default_input_singleline" size="16" maxlength="32" value="${rsname}">
                                    [可模糊搜索]</td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">资源类型：</td>
                                <td class="edit_table_value_td"><select name="rstype" id="all" class="default_select_single">
                                    <option value=""
                                            <c:if test="${entity.rstype.code == null || rstype == ''}"> selected="selected" </c:if>>--全部--</option>
                                    <option value="1"
                                            <c:if test="${entity.rstype.code eq 1 || rstype eq 1}"> selected="selected" </c:if>>菜单</option>
                                    <option value="2"
                                            <c:if test="${entity.rstype.code eq 2 || rstype eq 2}"> selected="selected" </c:if>>动作</option>
                                </select></td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">状态：</td>
                                <td class="edit_table_value_td"><select name="status" id="all2" class="default_select_single">
                                    <option value=""
                                            <c:if test="${entity.status.code == '' || status ==''}"> selected="selected" </c:if>>--全部--</option>
                                    <option value="y" id="qy"
                                            <c:if test="${entity.status.code == 'y' || status =='y'}"> selected="selected" </c:if>>启用</option>
                                    <option value="n" id="ty"
                                            <c:if test="${entity.status.code == 'n' || status =='n'}"> selected="selected" </c:if>>停用</option>
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
                        <p:privilege name="/privilege/res/createrespage">
                            <input type="button" value="新增资源" onclick="add();" class="default_button"/>
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
                    <td align="left">资源ID</td>
                    <td align="left">资源名字</td>
                    <td align="left">资源地址</td>
                    <td align="center">资源类别</td>
                    <td align="center">资源级别</td>
                    <td align="center">状态</td>
                    <td align="center" width="60">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <form action="/privilege/res/batchupdate" method="POST" name="batchform">
                    <input name="rsname" type="hidden" value="${rsname}"/>
                    <input name="rstype" type="hidden" value="${rstype}"/>
                    <input name="status" type="hidden" value="${status}"/>
                    <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                    <input name="items" type="hidden" value="${page.totalRows}"/>
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <c:forEach items="${rows}" var="ct" varStatus="st">
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
                                        <input type="checkbox" name="rsids" value="${ct.rsid}">
                                    </td>
                                    <td align="left">
                                            ${ct.rsid}
                                    </td>
                                    <td align="left">
                                            ${ct.rsname}
                                    </td>
                                    <td align="left">
                                            ${ct.rsurl}
                                    </td>
                                    <td align="center">
                                        <c:choose>
                                            <c:when test="${ct.rstype.code == 1}">
                                                菜单
                                            </c:when>
                                            <c:otherwise>
                                                动作
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td align="center">
                                            ${ct.rslevel.code}
                                    </td>
                                    <td align="center">
                                        <c:choose>
                                            <c:when test="${ct.status.code =='y'}">
                                                <font color="green">启用</font>
                                            </c:when>
                                            <c:otherwise>
                                                <font color="red">停用</font>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td align="center">
                                        <a href="${ctx}/privilege/res/preeditrespage?rsid=${ct.rsid}">修改</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="8" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="8">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].rsids, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].rsids)'>反选
                                    将选中记录用户状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <option value="y" >停用</option>
                                        <option value="n" >启用</option>
                                    </select>
                                    <p:privilege name="/privilege/res/batchupdate">
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
                                    <pg:pager url="/privilege/res/reslist"
                                              items="${page.totalRows}" isOffset="true"
                                              maxPageItems="${page.pageSize}"
                                              export="offset, currentPageNumber=pageNumber" scope="request">

                                        <pg:param name="rsname" value="${rsname}"/>
                                        <pg:param name="rstype" value="${rstype}"/>
                                        <pg:param name="status" value="${status}"/>

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