<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、用户查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add() {
            window.location.href = '/privilege/user/createuserpage';
        }
        function changepwd() {
            window.location.href = "/privilege/user/modifypwd";
        }
        function selectAll(obj, itemName) {
            var boxes = document.getElementsByName(itemName);
            for (var i = 0; i < boxes.length; i++) {
                boxes[i].checked = obj.checked;
            }
        }

        function del(id) {
            if (window.confirm("确定要删除吗?")) {
                window.location.href = '${ctx}/privilege/user/deleteuser?uno=' + id;
            }
        }
        function chongzhi() {
            document.getElementById("box").value = "";
            document.getElementById("startDate").value = "";
            document.getElementById("endDate").value = "";
            document.getElementById("screenName").value = "";
            document.getElementById("totalRows").value = "";
        }

        var subWindowArr = new Array();
        function preView(url) {
            var style = "height=600, width=800, top=230, left=180, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no";
            var subWin = window.open(url, "_blank", style);
            subWindowArr[subWindowArr.length] = subWin;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">系统管理 >> 权限管理 >> 管理员管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">管理员用户列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/privilege/user/userlist" name="userform" method="POST">
            <tr>
              <td width="80" align="center">搜索条件</td>
              <td><table width="100%"  border="0" cellspacing="1" cellpadding="0">
                <tr>
                  <td width="100" align="right" class="edit_table_defaulttitle_td">用户ID/用户名字：</td>
                  <td class="edit_table_value_td"><input name="userid" id="userid" type="text" class="default_input_singleline" size="16" maxlength="32" value="${entity.userid}">[可模糊查询]</td>
                  <td width="100" align="right" class="edit_table_defaulttitle_td">选择状态：</td>
                  <td class="edit_table_value_td"><select name="ustatus" class="default_select_single">
                    <option <c:if test="${entity.ustatus.code == '' || ustatus ==''}">selected="selected"</c:if> value="">--所有--</option>
                    <option <c:if test="${entity.ustatus.code == 'y' || ustatus == 'y'}">selected="selected"</c:if> value="y">启用</option>
                    <option <c:if test="${entity.ustatus.code == 'n' || ustatus =='n'}">selected="selected"</c:if> value="n">停用</option>
                  </select></td>
                </tr>
              </table></td>
              <td width="80" align="center">
                <input name="Submit" size="20" type="submit" class="default_button" value=" 搜索 ">
              </td>
            </tr>
                </form>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/privilege/user/createuserpage">
                            <input type="button" value="新增用户" onclick="add();" class="default_button"/>
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td align="center" width="40">选择</td>
                    <td align="left">用户ID</td>
                    <td align="left">用户名称</td>
                    <td align="center" width="60">状态</td>
                    <td align="center" width="120">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="5" class="default_line_td"></td>
                </tr>
                <form action="/privilege/user/batchupdate" method="POST" name="batchform">
                    <input type="hidden" name="pager.offset"  value="${page.startRowIdx}">
                    <input type="hidden" name="userid"  value="${entity.userid}">
                    <input type="hidden" name="ustatus"  value="${entity.ustatus.code}">
                    <input type="hidden" name="items"  value="${page.totalRows}">
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
                                        <c:if test="${ct.userid != 'sysadmin'}">
                                            <input type="checkbox" name="unos" value="${ct.uno}">
                                        </c:if>
                                    </td>
                                    <td align="left">
                                            ${ct.userid}
                                    </td>
                                    <td align="left">
                                            ${ct.username}
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.actstatus.privilege.${ct.ustatus.code}.name" bundle="${def}"></fmt:message>
                                    </td>
                                    <td align="center">
                                        <c:if test="${ct.userid !='sysadmin'}">
                                            <p:privilege name="privilege/user/preedituserpage">
                                                <a href="${ctx}/privilege/user/preedituserpage?uno=${ct.uno}">修改</a>
                                            </p:privilege>
                                            <p:privilege name="privilege/user/assignuserroles">
                                                &nbsp;&nbsp;<a href="javascript:preView('${ctx}/privilege/user/assignuserroles?uno=${ct.uno}')">用户角色分配</a>
                                            </p:privilege>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="5" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="5">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].unos, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].unos)'>反选
                                    将选中记录用户状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <option value="y" >停用</option>
                                        <option value="n" >启用</option>
                                    </select>
                                    <p:privilege name="/privilege/user/batchupdate">
                                        <input name="update" type="submit" class="default_button" value="批量修改">
                                    </p:privilege>
                                    <p:privilege name="/privilege/user/batchupdate">
                                        <input name="delete" type="submit" class="default_button" value="批量删除">
                                    </p:privilege>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="5" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr>
                            <td colspan="5" height="1" class="list_table_opp_tr">
                                <LABEL>
                                    <pg:pager url="/privilege/user/userlist"
                                              items="${page.totalRows}" isOffset="true"
                                              maxPageItems="${page.pageSize}"
                                              export="offset, currentPageNumber=pageNumber" scope="request">

                                        <pg:param name="userid" value="${entity.userid}"/>
                                        <pg:param name="username" value="${entity.username}"/>
                                        <pg:param name="ustatus" value="${entity.ustatus.code}"/>

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