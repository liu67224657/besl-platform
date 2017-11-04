<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
 <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <title>后台数据管理、添加新管理员</title>

    <script language="JavaScript" type="text/JavaScript">

        function back() {
            window.location.href = "/privilege/user/userlist";
        }
        $(document).ready(function(){
            $('#form_submit').bind('submit', function () {
                var userid = $("#userid").val();
                if (userid != '' && userid.indexOf("|")>=0) {
                    alert("用户ID包含特殊字符");
                    $("#userid").focus();
                    return false;
                }
                var username = $("#username").val();
                if (username != '' && username.indexOf("|")>=0) {
                    alert("用户姓名包含特殊字符");
                    $("#username").focus();
                    return false;
                }
                //return false;
            });
        });


    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">系统管理 >> 权限管理 >> 管理员管理</td>
    </tr>
    <c:if test="${errorMsgMap.containsKey('system')}">
        <fmt:message key="${errorMsgMap['system']}" bundle="${error}"></fmt:message>
    </c:if>

    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加用户</td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><div style="color: #008200">命名规则：用户ID（英文名）  用户姓名（部门_用户中文名）</div></td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><div style="color: #008200">重复则在后面加数字。</div></td>
                </tr>
                <tr>
                    <td class="list_table_header_td"><div style="color: #008200">用户ID和用户姓名均不的采用特殊字符 "|"</div></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/privilege/user/saveuserpage" method="POST" id="form_submit">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户ID：</td>
                        <td class="edit_table_value_td">
                            <input name="userid" type="text" class="default_input_singleline" id="userid"
                                   size="24" maxlength="32" value="${entity.userid}">
                            <c:if test="${errorMsgMap.containsKey('userid')}">
                                <fmt:message key="${errorMsgMap['userid']}" bundle="${error}"></fmt:message>
                            </c:if>
                            <c:if test="${errorMsgMap.containsKey('duplication')}">
                                <fmt:message key="${errorMsgMap['duplication']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>

                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户姓名：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="username" type="text" class="default_input_singleline" size="24" maxlength="64"
                                   id="username" value="${entity.username}">
                            <c:if test="${errorMsgMap.containsKey('username')}">
                                <fmt:message key="${errorMsgMap['username']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户密码：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="password" type="password" class="default_input_singleline" size="24"
                                   maxlength="64"
                                   id="password" value="${entity.password}">
                            <c:if test="${errorMsgMap.containsKey('password')}">
                                <fmt:message key="${errorMsgMap['password']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">用户状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="ustatus" id="ustatus">
                                <option value=""
                                        <c:if test="${entity.ustatus.code =='' || entity.ustatus.code == null}">selected="selected" </c:if>>--请选择--</option>
                                <option value="y"
                                        <c:if test="${entity.ustatus.code =='y'}">selected="selected" </c:if>>可用</option>
                                <option value="n"
                                        <c:if test="${entity.ustatus.code =='n'}">selected="selected" </c:if>>停用</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">仅限公司内网访问：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="limitLocation">
                                <option value=""
                                        <c:if test="${entity.limitLocation.code =='' || entity.ustatus.code == null}">selected="selected" </c:if>>--请选择--</option>
                                <option value="y"
                                        <c:if test="${entity.limitLocation.code =='y'}">selected="selected" </c:if>>是</option>
                                <option value="n"
                                        <c:if test="${entity.limitLocation.code =='n'}">selected="selected" </c:if>>否</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>
</table>
</body>
</html>