<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/appadvertise.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var nick = $("input[name='nick']").val();
                var hiddenick = $("input[name='hiddenick']").val();
                var point = $("input[name='point']").val();
                var verifydesc = $("[name='verifydesc']").val();
                var appkey = $("[name='appkey']").val();

                if (nick.trim() == "") {
                    alert("请填写昵称");
                    return false;
                }
                if (nick != hiddenick) {
                    alert("请先检测用户是否存在");
                    return false;
                }

                if (appkey == '') {
                    alert("请选择来源");
                    return false;
                }

                //if (appkey == '3iiv7VWfx84pmHgCUqRwun') { //玩霸appkey
                    if (point == '') {
                        alert("请填写积分");
                        return false;
                    }
                    if (point < 10 || point > 500) {
                        alert("积分值请填写10-500");
                        return false;
                    }
                //}
                if (verifydesc.trim() == "") {
                    alert("请填写认证信息");
                    return false;
                }
                if (verifydesc.trim().length > 15) {
                    alert("最多输入15个字符");
                    return false;
                }

            });

            $("#check").click(function () {
                var nick = $("input[name='nick']").val();
                if (nick.trim() == '') {
                    alert("请填写昵称");
                    return;
                }
                $.ajax({
                    url: "/wanba/profile/checknick",
                    data: {nick: nick},
                    timeout: 5000,
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.rs == '1') {
                            alert("检测成功");
                            $("input[name='hiddenick']").val(nick);
                            return;
                        } else if (data.rs == '-1') {
                            alert("该用户不存在");
                            return;
                        } else if (data.rs == '-2') {
                            alert("该用户已经存在达人列表，请直接去修改");
                            return;
                        }
                    }
                });
            });

        });


    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 新增认证用户</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新认证用户</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                                ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/wanba/profile/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            用户昵称:
                        </td>
                        <td height="1">
                            <input id="tag_name" type="text" name="nick" size="20"/>
                            <input type="hidden" name="hiddenick" size="20"/>
                            <input type="button" value="检测" id="check"/>
                            <span></span>

                            <span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            来源:
                        </td>
                        <td height="1">
                            <select name="appkey">
                                <c:forEach items="${appkeyList}" var="key">
                                    <option value="${key}"
                                            <c:if test="${key eq appkey}">selected</c:if> >
                                        <fmt:message key="verify.source.appkey.${key}"
                                                     bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            认证类型:
                        </td>
                        <td height="1">
                            <select name="verifytype">
                                <c:forEach items="${wanbaVerifyList}" var="verify">
                                    <option value="${verify.verifyId}">${verify.verifyName}</option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            提问积分:
                        </td>
                        <td height="1">
                            <input type="text" name="point" size="48" onkeyup="value=value.replace(/[^\d]/g,'')"
                                   placeholder="10-500"/> <span style="color:red">*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            认证信息:
                        </td>
                        <td height="1">
                            <textarea rows="5" cols="35" name="verifydesc" placeholder="认证信息，15字以内"></textarea>
                            <span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            游戏绑定:
                        </td>
                        <td height="1">
                            <c:forEach items="${animeTagList}" var="tag">
                                <input type="checkbox" name="tagid" value="${tag.tag_id}"/> ${tag.tag_name}
                            </c:forEach>
                            <span style="color:red">*玩霸专用</span>
                        </td>
                        <td height="1" class=>

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