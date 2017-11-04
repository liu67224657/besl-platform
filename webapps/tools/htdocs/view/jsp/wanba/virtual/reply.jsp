<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>评论页</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#Submit').bind('click', function () {
                var profilieid = $("#profilieid").val();
                if (profilieid.trim() == "") {
                    alert("请选择一个用户");
                    return false;
                }
                var body = $("#body").val();
                if (body.trim() == "") {
                    alert("请填写内容");
                    return false;
                }
                $.ajax({
                    type: "post",
                    url: "/wanba/virtual/postreply",
                    data: {answerid: ${answerid},profilieid:profilieid,body:body,parentid:${parentid}},
                    dataType: "json",
                    success: function (req) {
                        if (req.rs == '1') {
                            alert("评论成功");
                            $("#body").val("");
                        }else if(req.rs=='-40022'){
                            alert("15秒只能评论一次");
                        }else if(req.rs=='-40020'){
                            alert("1分钟不能有重复内容");
                        }else if(req.rs=='-40019'){
                            alert("用户被加入黑名单禁止评论");
                        }else if(req.rs=='-40017'){
                            alert("内容含有敏感词");
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 评论页</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">评论页</td>
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
                <input name="answerid" type="hidden" class="default_button" value="${answerid}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            用户:
                        </td>
                        <td height="1">
                            <select name="profilieid" id="profilieid">
                                <c:forEach items="${list}" var="profile" varStatus="st">
                                    <option value="${profile.profileid}"><c:if test="${profileMap[profile.profileid]!=null}">${profileMap[profile.profileid].nick}</c:if></option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            内容:
                        </td>
                        <td height="1">
                            <textarea type="text" name="body" id="body"  cols="80" rows="20" ></textarea><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" id="Submit" type="button" class="default_button" value="提交">
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