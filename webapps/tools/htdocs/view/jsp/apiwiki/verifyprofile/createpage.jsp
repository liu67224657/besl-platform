<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加游戏标签</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/js/qiniu.js"></script>
    <script>
        $(document).ready(function () {
            $('#form').submit(function () {
                var nick = $('#nick').val();
                var verifyInfo = $('#verifyInfo').val();

                var isError = false;
                if (nick.length == 0) {
                    $('#msg_uid').html('<span style="color:red">昵称不能为空</font>')
                    isError = true;
                }

                if (verifyInfo.length == 0) {
                    $('#msg_verifyInfo').html('<span style="color:red">请填写认证信息</font>')
                    isError = true;
                }

                if (isError) {
                    return false;
                }
            });
        });

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 认证用户</td>
    </tr>
    <tr>

        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">认证用户</td>
                </tr>
                <tr>
                    <td>
                        <form action="/apiwiki/verifyprofile/create" method="post" id="form">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_header_td"></td>
                                </tr>
                                <c:if test="${fn:length(errorMsg)>0}">
                                    <tr>
                                        <td height="1" colspan="3" class="fontcolor_input_hint">
                                            <fmt:message key="${errorMsg}" bundle="${errorProps}"/>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">昵称</td>
                                    <td class="edit_table_value_td">
                                        <input type="text" id="nick"  value="${nick}" name="nick" class="" placeholder="请输入用户的昵称" maxlength="32" size="32"/>
                                    </td>
                                    <td id="msg_nick" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">QQ</td>
                                    </td>
                                    <td class="edit_table_value_td">
                                        <input type="text" id="qq" name="qq"  value="${qq}" class="" placeholder="请输入QQ号" maxlength="32" size="32"/>
                                    </td>
                                    <td id="msg_qq" class="detail_table_value_td"></td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">mobile</td>
                                    </td>
                                    <td class="edit_table_value_td">
                                        <input type="text" id="mobile" value="${mobile}" name="mobile" class="" placeholder="请输入手机号" maxlength="32" size="32"/>
                                    </td>
                                    <td id="msg_mobile" class="detail_table_value_td"></td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">微信</td>
                                    </td>
                                    <td class="edit_table_value_td">
                                        <input type="text" id="microMsg" value="${microMsg}"  name="microMsg" class="" placeholder="请输入微信号" maxlength="32" size="32"/>
                                    </td>
                                    <td id="msg_microMsg" class="detail_table_value_td"></td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">认证信息</td>
                                    </td>
                                    <td class="edit_table_value_td">
                                        <textarea id="verifyInfo"   name="verifyInfo" rows="8" cols="50">${verifyInfo}</textarea>
                                    </td>
                                    <td id="msg_verifyInfo" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_value_td" align="center"><input
                                            type="submit" name="button" id="sub" class="default_button" value="提交"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>