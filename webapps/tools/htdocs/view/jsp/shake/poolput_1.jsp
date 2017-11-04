<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/include/js/addgameframe.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            frameOption.resetFunc = function () {

            };
            frameOption.buttonid = 'addnewgame';

            frameOption.selectFunc = function (item) {
                $("#txt_gamedbid").val(item.gameDbId);
                $("#txt_gamename").text(item.gameName);
            }

            gameframe(frameOption);
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸摇一摇配置列表</td>
    </tr>
    <tr>
        <td valign="top"><br/>
            <input type="button" id="addnewgame" name="button" value="请选择要填加的game"/><br/>
            <%@ include file="shake_pool_header.jsp" %>
            <form action="/shake/pool/put" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="configid" value="${config.configId}"/>
                            <input type="hidden" name="type" value="${type}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                        游戏ID:
                        </td>
                        <td>

                            <input type="text" name="directid" value="" id="txt_gamedbid"/><span id="txt_gamename"
                                                                                                 style="color: green"></span>
                        </td>
                        <td height="1" class="default_line_td" width="100">
                            权重值:
                        </td>
                        <td>
                            <input type="text" name="weight" value="1"/>
                        </td>
                        <td height="1">
                            <span style="color: #ff0000">数字</span>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>

</table>


<%@ include file="/view/jsp/gameframe.jsp" %>
</body>
</html>