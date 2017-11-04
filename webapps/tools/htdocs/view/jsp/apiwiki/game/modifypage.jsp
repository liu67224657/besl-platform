<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加推荐游戏</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var recommend = $.trim($('#recommend').val());
                if (recommend.length == 0) {
                    alert("请填写一句话推荐");
                    return false;
                }
                var recommendAuth = $.trim($('#recommendAuth').val());
                if (recommendAuth.length == 0) {
                    alert("请填写撰写人");
                    return false;
                }

            });
        });

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 添加推荐游戏</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <form action="/apiwiki/game/modify" method="post" id="form_submit">
                <table width="80%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="center" width="">游戏ID</td>
                        <td nowrap align="center" width="">游戏名称</td>
                        <td nowrap align="center" width="">别名</td>
                        <td nowrap align="center" width="">标签</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr class="">
                        <td nowrap align="center">${game.id}</td>
                        <input type="hidden" id="id" name="gameDbId" class="default_button" value="${game.id}"/>
                        <td nowrap class="name" title="${game.name}">${game.name}</td>
                        <td nowrap class="name" title="${game.aliasName}">${game.aliasName}</td>
                        <td nowrap class="name" title="${game.gameTag}">${game.gameTag}</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="">
                            <br/> 一句话推荐:<br/>
                            <textarea maxlength="128"
                                      name="recommend" id="recommend" cols="50"
                                      rows="5">${game.extjson.recommend}</textarea>
                        </td>

                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="">
                            撰写人:<br/>
                            <input type="text" id="recommendAuth" name="recommendAuth" class="default_button"
                                   maxlength="10" value="${game.extjson.recommendAuth}" size="52"/>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="4" height="1" class="default_line_td"></td>
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