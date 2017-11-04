<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <title>添加任务组</title>>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">

        $(function () {
            $("#select_type").val('${taskgroup.type.code}');


            $("#select_type").on("change", function () {
                if ($("#select_type").val() == 'common') {
                    $(".data-task-center").show();
                } else {
                    $(".data-task-center").hide();
                }
            });
            $("#select_type").change();

            $('#form_submit').submit(function () {

                var input_text_name = $.trim($("#input_text_name").val());
                if (input_text_name == '') {
                    alert("任务组名称不能为空");
                    return false;
                }

                $('#form_submit').submit();

            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <c:if test="${taskgroup.appKey == '17yfn24TFexGybOF0PqjdY'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸任务管理</td>
        </c:if>
        <c:if test="${taskgroup.appKey == '08pkvrWvx5ArJNvhYf19kN'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> app任务管理 >> 优酷合作任务管理</td>
        </c:if>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加任务组</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/task/group/modify" method="post" id="form_submit">
                <input type="hidden" name="displayOrder" value="${taskgroup.displayOrder}"/>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="groupid" value="${taskgroup.taskGroupId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            组ID：
                        </td>
                        <td height="1" class="" width="10">
                            ${taskgroup.taskGroupId}
                        </td>
                        <td height="1" align="left">*不能修改</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="name" value="${taskgroup.taskGroupName}" id="input_text_name"/>
                        </td>
                        <td height="1" align="left">*</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            任务组类型:
                        </td>
                        <td height="1" class="">
                            <select name="type" id="select_type">
                                <c:forEach items="${types}" var="item">
                                    <option value="${item.code}"><fmt:message key="task.taskgroup.type.${item.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">*可以修改<span style="color:red"> 连续性任务是指任务之间存在某种关系,如签到任务; 非连续性任务是指新手任务，日常任务等，任务之间不存在相互关系  </span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台:
                        </td>
                        <td height="1" class="">
                            <fmt:message key="joymeapp.platform.${taskgroup.appPlatform.code}" bundle="${def}"/>
                        </td>
                        <td height="1">*不能修改</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            appkey:
                        </td>
                        <td height="1" class="">
                            ${taskgroup.appKey}
                        </td>
                        <td height="1">*不能修改</td>
                    </tr>
                    <tr class="data-task-center">
                        <td height="1" class="default_line_td">
                            任务都完成后分组在任务中心的显示情况
                        </td>
                        <td height="1">
                            <select name="showtype" id="showtype">
                                <c:forEach items="${showtypes}" var="st">
                                    <option value="${st.code}" <c:if test="${st.code==taskgroup.showtype.code}">selected</c:if>>
                                        <fmt:message key="task.taskgroup.showtype.${st.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1"><span style="color:red">*(新手任务可以选择后两个'不显示',其他任务一般情况下请选择'显示')</span></td>
                    </tr>
                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>