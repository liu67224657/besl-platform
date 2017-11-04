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
                var point = $("input[name='point']").val();
                var verifydesc = $("[name='verifydesc']").val();
                var appkey = $("[name='appkey']").val();


                //if (appkey == '3iiv7VWfx84pmHgCUqRwun') { //玩霸appkey
                    if (point == '') {
                        alert("请填写积分");
                        return false;
                    }
                    if (point < 10 || point > 500) {
                        alert("积分值请填写10-500");
                        return false;
                    }
               // }
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

            });

        });


    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 修改玩霸认证用户</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改玩霸认证用户</td>
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
            <form action="/wanba/profile/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" value="${wanbaprofile.profileId}" name="profileid"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            用户昵称:
                        </td>
                        <td height="1">
                            ${wanbaprofile.nick}
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
                                    <option value="${verify.verifyId}"
                                            <c:if test="${wanbaprofile.verifyType eq verify.verifyId}">selected</c:if> >${verify.verifyName}</option>
                                </c:forEach>
                            </select>
                            <span></span>
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
                                            <c:if test="${key eq wanbaprofile.appkey}">selected</c:if> >
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
                            提问积分:
                        </td>
                        <td height="1">
                            <input type="text" name="point" size="48" onkeyup="value=value.replace(/[^\d]/g,'')"
                                   value="${wanbaprofile.askPoint}"
                                   placeholder="10-500"/><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            认证信息:
                        </td>
                        <td height="1">
                            <textarea rows="5" cols="35" name="verifydesc"
                                      placeholder="认证信息，15字以内">${wanbaprofile.description}</textarea>
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
                                <input type="checkbox" name="tagid" value="${tag.tag_id}"
                                       <c:forEach var="entry" items="${pidSet}">
                                       <c:if test="${entry==tag.tag_id}">checked</c:if>
                                </c:forEach> /> ${tag.tag_name}
                            </c:forEach>
                        </td>
                        <td height="1" class=>
                            <input type="hidden" value="${oldtagidSet}" name="oldtagidSet"/>

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