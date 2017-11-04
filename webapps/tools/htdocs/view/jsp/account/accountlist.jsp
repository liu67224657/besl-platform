<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、用户管理列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript">
        function selectAll(obj, itemName)
        {
            var boxes = document.getElementsByName(itemName);
            for(var i=0;i<boxes.length;i++){
                boxes[i].checked = obj.checked;
            }
            var unselected = document.getElementById("unchk");
            unselected.checked = false;
        }
        function unselected(obj, itemName)
        {
            var selected = document.getElementById("chkall");
            selected.checked = false;
            var boxes = document.getElementsByName(itemName);
            for(var i=0;i<boxes.length;i++){
                if(boxes[i].checked){
                    boxes[i].checked = false;
                }else {
                    boxes[i].checked = true;
                }
            }
        }


    </script>
</head>

<body>


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 用户管理 >> 用户信息管理</td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/audit/account/accountlist" method="POST">
                    <tr>
                        <td colspan="4" class="error_msg_td"></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">ACCOUNTUNO：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="accountuno" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   id="accountuno" value="${params.accountUno}">
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">注册时间：</td>
                        <td class="edit_table_value_td">

                            <c:set var="startDate"><fmt:formatDate value="${params.registerStartDate}" pattern="yyyy-MM-dd"/></c:set>
                            <c:set var="endDate"><fmt:formatDate value="${params.registerEndDate}" pattern="yyyy-MM-dd"/></c:set>
                            <input name="createstartdate" type="text" class="default_input_singleline" id="registerStartDate"
                                   value="${startDate}" size="24" maxlength="32">
                            -
                            <input name="createenddate" type="text" class="default_input_singleline" id="registerEndDate"
                                   value="${endDate}" size="24" maxlength="32">(格式为：2011-11-20)

                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">AUTHTOKEN：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="authtoken" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   id="authtoken" value="${params.authToken}">
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">修改时间：</td>
                        <td class="edit_table_value_td">

                            <c:set var="startUpDate"><fmt:formatDate value="${params.updateStartDate}" pattern="yyyy-MM-dd"/></c:set>
                            <c:set var="endUpDate"><fmt:formatDate value="${params.updateEndDate}" pattern="yyyy-MM-dd"/></c:set>
                            <input name="startdate" type="text" class="default_input_singleline" id="loginStartDate"
                                   value="${startUpDate}" size="24" maxlength="32">
                            -
                            <input name="enddate" type="text" class="default_input_singleline" id="loginEndDate"
                                   value="${endUpDate}" size="24" maxlength="32">(格式为：2011-11-20)

                        </td>

                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">范围${params.auditStatus.value}：</td>
                        <td nowrap class="edit_table_value_td">
                            <input type="radio" name="audit" value="0" id="unaudit"
                                    <c:if test='${!params.auditStatus.hasAudit()}'>
                                        checked="checked"
                                    </c:if>> 未审核</input>
                            <input type="radio" name="audit" value="1" id="audited"
                                    <c:if test='${params.auditStatus.hasAudit()}'>
                                        checked="checked"
                                    </c:if>> 已审核</input>
                        </td>

                        <td width="120" align="right" class="edit_table_defaulttitle_td">排序方式：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="sorttype" id="">
                                <option value="updatedatedesc"
                                        <c:if test='${params.sortType == "updatedatedesc"}'>
                                            selected="selected"
                                        </c:if>>用户最后更新时间倒序</option>
                                <option value="createdatedesc"
                                        <c:if test='${params.sortType == "createdatedesc"}'>
                                            selected="selected"
                                        </c:if>>用户创建博客时间倒序</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="default_line_td"></td>
                        <td colspan="4" class="default_line_td" >
                            <%--<p:privilege name="/audit/content/imglist?page=no">--%>
                                <input name="Submit" type="submit" class="default_button" value="提交">
                                <input name="Reset" type="reset" class="default_button" value="重置">
                            <%--</p:privilege>--%>
                        </td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>


    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">账号信息列表查询</td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td  align="center">
                        <nobr>选择</nobr>
                    </td>
                    <td  align="center">ACCOUNTUNO</td>
                    <td  align="center">邮箱</td>
                    <td  align="center">注册IP</td>
                    <td  align="center">最后登录IP</td>
                    <td  align="center">注册时间</td>
                    <td  align="center">最后登录时间</td>
                    <td  align="center">状态</td>
                    <td  align="center">来自</td>
                    <td  align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:forEach items="${rows}" var="account" varStatus="st">
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
                            <input type="checkbox" id="chk_${account.account.accountUno}" name="applyid"
                                   value="${account.account.accountUno}"/>
                        </td>
                        <td align="left" >
                                ${account.account.accountUno}
                        </td>
                        <td align="left" >
                                ${account.account.userid}
                        </td>

                        <td align="left" >
                                 ${account.account.registerIp}
                        </td>
                        <td align="left">
                               ${account.account.loginIp}
                        </td>
                        <td align="left" >
                              <fmt:formatDate value="${account.account.registerDate}" pattern="yyyy-MM-dd"></fmt:formatDate>
                        </td>
                        <td align="left">
                                <fmt:formatDate value="${account.account.updateDate}" pattern="yyyy-MM-dd"></fmt:formatDate>
                        </td>
                        <td align="left"  >
                                ${account.account.accountStatus.code}
                        </td>
                        <td align="left" >
                                人人
                        </td>
                        <td align="left">
                               操作
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td">
                        <nobr>全选<input type="checkbox" id="chkall" onclick="selectAll(this, 'applyid')"/></nobr>
                        <nobr>反选<input type="checkbox" id="unchk" onclick="unselected(this, 'applyid')"/></nobr>
                        <input name="Submit" type="submit" class="default_button" value="屏蔽">
                        <input name="Submit" type="submit" class="default_button" value="通过">

                    </td>
                    <td height="1" class="default_line_td">
                        <LABEL>
                            <pg:pager url="accountlist"
                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="pager.offset" value="${page.startRowIdx}"/>
                                <pg:param name="createstartdate" value="${startDate}"/>
                                <pg:param name="createenddate" value="${endDate}"/>
                                <pg:param name="startDate" value="${startUpDate}"/>
                                <pg:param name="endDate" value="${endUpDate}"/>
                                <pg:param name="accountuno" value="${params.accountUno}"/>
                                <pg:param name="authtoken" value="${params.authToken}"/>
                                <pg:param name="audit" value="${params.auditStatus.value}"/>
                                <pg:param name="sorttype" value="${params.sortType}"/>

                            <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </LABEL>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>