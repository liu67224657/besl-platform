<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>Simple jsp page</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        $().ready(function() {
            $('#form_submit').bind('submit', function() {

            });
        });

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 排行榜长评维护列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/mobilegame/article/create" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="title" size="32" id="input_text_name"/> *必填
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="desc" cols="55" rows="20"></textarea><span style="color:red">必填 </span>
                        </td>
                        <td height="1"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            文章链接地址:
                        </td>
                        <td height="1" class="">
                           <input type="text" size="50" name="url" /><span style="color:red">必填 </span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            作者名称：
                        </td>
                        <td>
                            <input id="input_menu_pic" type="text" name="author">
                            <span style="color:red">必填 </span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            所属游戏库ID：
                        </td>
                        <td>
                            <input id="gameDbId" type="text" name="gamedbid">
                            <c:if test="${fn:length(gamedbisnull)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${gamedbisnull}" bundle="${def}"/></span>
                            </c:if>
                            <span style="color:red">必填 </span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(codeExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${codeExist}" bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>

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