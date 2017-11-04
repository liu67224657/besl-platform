<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>热门页游戏分类创建父类别</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(function () {

            var platform='${platform}';
            if(platform !=  ''){
               $("#platform").val(parseInt(platform));
            }

            $('#form_submit').on('submit', function () {
                var name = $.trim($('#input_text_name').val());
                if (name == '') {
                    alert("请填写名称");
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 热门页游戏分类管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">创建一条Line</span>
                    </td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
                                ${errorMsg}
                        </td>
                    </tr>
                </c:if>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/clientline/gamecategory/create" method="post" id="form_submit">
                <table width="60%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td" width="120px">
                            名称
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="lineName" size="20" id="input_text_name"
                                   value="${lineName}"/> *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="120px">
                            请选择所属平台
                        </td>
                        <td height="1" class="" width="10">
                         <select name="platform" id="platform" >
                             <option value="0">ios</option>
                             <option value="1">android</option>
                             <option value="2">双平台</option>      <%--加上这个option后，判断逻辑变复杂了--%>
                         </select>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                    <tr align="center">
                        <td colspan="3">
                            <input  type="submit" class="default_button" value="提交">
                            <input type="button" class="default_button" value="返回"
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