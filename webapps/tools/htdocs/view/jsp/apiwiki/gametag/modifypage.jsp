<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>编辑游戏标签</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            $('#sub').click(function () {
                var tagName = $("#tagName").val();
                if ($.trim(tagName) == "") {
                    alert("请输入游戏标签");
                    return false;
                }
                var tagId = $("#tagId").val();
                $.post("/apiwiki/gametag/modify", {tagName: tagName, id: tagId}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 0) {
                        alert("游戏标签已存在");
                    } else {
                        window.location.href = "/apiwiki/gametag/list";
                    }
                });
            });
        });

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 编辑游戏标签</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑游戏标签</td>
                </tr>
                <tr>
                    <td>
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td>
                                    <input type="hidden" name="id" id="tagId" class="default_button"
                                           value="${gameTag.id}"/>
                                    <input type="text" id="tagName" name="tagName" class="default_button"
                                           placeholder="请输入游戏标签，长度最多10个字符" maxlength="10" size="100"
                                           value="${gameTag.tagName}"/>
                                    <br/>
                                    <input type="button" name="button" id="sub" class="default_button" value="修改游戏标签"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>