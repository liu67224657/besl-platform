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
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }
         a {
            text-decoration: none;
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                if ($.trim($("#desc").val()) == "") {
                    $("#desc").focus();
                    alert('评论不能为空');
                    return false;
                }
                var strlen = isChinese($("#desc"));
                strlen = Math.ceil(strlen / 2);
                if (strlen <= 1) {
                    alert("最少要输入两个字");
                    return false;
                }
                if (strlen > 140) {
                    alert("最多只能输入140个字，现在字数" + strlen);
                    return false;
                }
                if ($.trim($("#nickname").val()) == "") {
                    alert("请填写虚拟用户昵称。");
                    return false;
                }
                var textare = $("#desc").val();
                textare = {text: textare};
                var text = JSON.stringify(textare);
                $("#text").val(text);

            });

        });

        function isChinese(str) { //判断是不是中文
            var oVal = str.val();
            var oValLength = 0;
            oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
            return oValLength;

        }
        function username(name) {
            $("#nickname").val(name);
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸迷友圈主贴</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">虚拟用户发表评论</td>
                </tr>
            </table>
            <form action="/comment/bean/createreply" method="post" id="form_submit">

                <table width="800" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" value="${comentBean.commentId}" name="commentid"/>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            主帖内容
                        </td>
                        <td height="1">
                            ${comentBean.description}
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            主帖图片
                        </td>
                        <td height="1">
                            <c:forEach items="${pics}" var="pic">
                                <img src="${pic}"/> <br/>
                            </c:forEach>
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            发帖人
                        </td>
                        <td height="1">
                            ${profile.nick}
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            评论内容
                        </td>
                        <td height="1">
                            <textarea id="desc" name="textare" cols="50" rows="10">${textare}</textarea> *必填
                            <input type="hidden" name="text" id="text"/>
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150" height="100">
                            虚拟用户名称:
                        </td>
                        <td height="1" style="width:150px;">
                            <input id="nickname" type="text" name="nick" value="${nick}" readonly="readonly" size="32"/>

                            <span style="color:red">
                                <c:if test="${not empty errorMsg}">
                                    <fmt:message
                                            key="${errorMsg}" bundle="${error}"/>
                                </c:if>
                            </span>
                        </td>
                        <td height="1" class="default_line_td" style="width:350px;">
                            <c:forEach items="${names}" var="name">
                                <a href="javascript:username('${name}')">${name}</a>&nbsp;&nbsp;&nbsp;
                            </c:forEach>
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