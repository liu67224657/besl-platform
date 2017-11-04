<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>分享管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>

        $().ready(function() {
            doOnLoad();
            $('#form_submit').bind('submit', function() {
                var shareKey = $('#input_text_sharekey').val();
                if (shareKey.length == 0) {
                    alert('请填写分享的key');
                    return false;
                }

                var displayStyle = $('#input_text_displaystyle').val();
                if (displayStyle.length == 0) {
                    alert('请填写按钮样式');
                    return false;
                }


                var sourceUrl = $('#input_text_sharesource').val();
                if (sourceUrl.length == 0) {
                    alert('请填写分享的url');
                    return false;
                }

                var typeVal = $('#select_share_type').val();
                if (typeVal.length == 0) {
                    alert('请选择分享类型');
                    return false;
                }
            });
        });
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["input_expiredate"]);
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷分享 >> 分享信息</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加分享信息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/sync/shareinfo/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class=""></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            分享的KEY:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="shareKey" size="20" id="input_text_sharekey" onblur="check_key()"/>
                        </td>
                        <td height="1">*必填项</td>

                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(keyerror)>0}">
                                <span style="color: red" id="key_error"><fmt:message key="${keyerror}"
                                                                                     bundle="${def}"/></span>
                            </c:if>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            分享的URL:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="shareSource" style="width: 100%;" id="input_text_sharesource"/>
                        </td>
                        <td height="1" class=>*必填项</td>
                    </tr>
                     <tr>
                        <td height="1" class="default_line_td">
                            对应的ID:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="directId" style="width: 100%;" id="input_text_directid"/>
                        </td>
                        <td height="1" class=>*如果是分享活动要填写文章的ID</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            分享的按钮样式:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="displayStyle" style="width: 100%;" id="input_text_displaystyle"/>
                        </td>
                        <td height="1" class=>*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            过期时间:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="expiredate" size="20" id="input_expiredate"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            选择分享类型:
                        </td>
                        <td height="1" class="">
                            <select name="sharetype" id="select_share_type">
                                <option value="">请选择</option>
                                <c:forEach var="sharetype" items="${shareTypeCollection}">
                                    <option value="${sharetype.code}"
                                            <c:if test="${sharetype.code==shareTypeCode}">selected</c:if>><fmt:message
                                            key="share.type.${sharetype.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            选择奖励类型:
                        </td>
                        <td>
                            <select name="shareRewardType">
                                <c:forEach var="sharerewardtype" items="${sharerewardtypelist}">
                                    <option value="${sharerewardtype.value}"><fmt:message
                                            key="share.reward.type.${sharerewardtype.value}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            奖励积分:
                        </td>
                        <td>
                            <input type="text" name="shareRewardPoint" value="60"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            奖励ID:
                        </td>
                        <td>
                            <input type="text" name="shareRewardId"/>
                        </td>
                        <td height="1" class=></td>
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