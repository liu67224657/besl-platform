<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏资料库</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            var lock = false;
            $("#submit_relation").click(function () {
                var wikikey = $("input[name='wikikey']").val().trim();
                var gameid = $("input[name='gameid']").val();
//                if (wikikey == '') {
//                    alert("请填写wikikey");
//                    return;
//                }

                if (wikikey.length > 20) {
                    alert("20个字符以内 只能输入数字和字母");
                    return;
                }
                if (!lock) {
                    lock = true;
                    $.ajax({
                        url: "/json/gameresource/relation",
                        data: {wikikey: wikikey, gameid: gameid},
                        timeout: 5000,
                        dataType: "json",
                        type: "POST",
                        success: function (data) {
                            lock = false;
                            if (data.rs == -1) {
                                alert("该WIKI/圈子已关联游戏：" + data.gameName);
                            } else {
                                alert("更新成功");
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            lock = false;
                            alert("网络错误");
                            return;
                        }
                    });
                }

            });
        });


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 条目维护 >> 游戏资料库>>关联wiki</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">${gameDb.gameName}</td>

                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <input type="hidden" name="gameid" value="${gameDb.gameDbId}"/>
                        WIKI_KEY：<input type="text" name="wikikey" value="${gameDb.wikiKey}"
                                        onkeyup="this.value=this.value.replace(/[^0-9a-z]/g, '')"/>*必填 <br/>
                        <b style="color:red;">修改WIKI_KEY后【游戏管理>游戏列表】中的游戏将被禁用</b> <br/>
                        <b style="color:red;">请在【wiki列表】添加相关WIKI后重新启用游戏，启用后在app中游戏正常显示</b>
                    <td>
                <tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr align="center">
                    <td colspan="3">
                        <input name="Submit" type="button" id="submit_relation" class="default_button" value="提交">
                        <input name="Reset" type="button" class="default_button" value="返回"
                               onclick="javascipt:window.history.go(-1);">
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>