<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>玩霸小红点列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            $('#focusBatch').bind("submit", function () {
                var wanbaMessageType = $.trim($('#wanbaMessageType').val());
                if (wanbaMessageType.length == 0) {
                    alert("位置");
                    return false;
                }
                var redmessagetype = $.trim($('#redmessagetype').val());
                if (redmessagetype.length == 0) {
                    alert("类型");
                    return false;
                }
                redmessagetype = parseInt(redmessagetype);
                if (redmessagetype == 2 && isNaN(redmessagetype)) {
                    alert("类型是数字。");
                    return false;
                }

                var text = $.trim($('#text').val());
                if (redmessagetype >= 4 && text.length > 0) {
                    alert("检查文本是否符合要求");
                    return false;
                }


            });

            $("#redmessagetype").change(function(){
                var redmessagetype = $("#redmessagetype").val();
                if(redmessagetype==7){
                    $("#redmessagetext").val("礼");
                }else if(redmessagetype==6){
                    $("#redmessagetext").val("活");
                }else if(redmessagetype==5){
                    $("#redmessagetext").val("新");
                }else if(redmessagetype==4){
                    $("#redmessagetext").val("攻");
                }
            });
        });


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 小红点修改</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>玩霸小红点列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/redmessage/list" method="post">
            <table width="20%" border="0" cellspacing="0" cellpadding="0">

                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <select name="platform" id="platform">
                                <c:if test="${platform==0}">
                                    <option value="0" selected>IOS</option>
                                    <option value="1">android</option>
                                </c:if>
                                <c:if test="${platform==1}">
                                    <option value="0">IOS</option>
                                    <option value="1" selected>android</option>
                                </c:if>
                            </select>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>

            </table>
            </form>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/redmessage/create" method="post" id="focusBatch">
                <input name="platform" value="${platform}" type="hidden">
                <table width="60%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td>
                            位置： <select name="wanbaMessageType" id="wanbaMessageType">
                            <option value="">请选择</option>
                            <c:forEach items="${wanbaMessageType}" var="type">
                                <option value="${type.name}">${type.chname}</option>
                            </c:forEach>
                        </select>
                        </td>

                        <td>
                            类型： <select name="redmessagetype" id="redmessagetype">
                            <option value="">请选择</option>
                            <c:forEach items="${redMessageType}" var="type">
                                <option value="${type.code}"><fmt:message
                                        key="gameclient.news.redmessagetype.${type.code}"
                                        bundle="${def}"/></option>
                            </c:forEach>
                        </select>
                        </td>
                        <td>
                            文本： <input type="text" name="text" id="redmessagetext"/>
                            <input type="submit" value="修改"/>
                        </td>
                    </tr>
                </table>
            </form>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="60%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="5" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="100">位置</td>
                    <td nowrap align="center" width="100">类型</td>
                    <td nowrap align="center" width="100">sid</td>
                    <td nowrap align="center" width="160">文本</td>
                    <td nowrap align="center" width="80">时间</td>
                </tr>

                <c:forEach items="${map}" var="item">
                    <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td nowrap align="center"><fmt:message key="gameclient.news.wanbamessagetype.${item.key}" bundle="${def}"/></td>
                        <td nowrap align="center">
                            <fmt:message key="gameclient.news.redmessagetype.${item.value.type}" bundle="${def}"/>
                        </td>
                        <td nowrap align="center">${item.value.sid}</td>
                        <td nowrap align="center">${item.value.text}</td>
                        <td nowrap align="center"><fmt:formatDate value="${date:long2date(item.value.messagetime)}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                </c:forEach>


            </table>
        </td>
    </tr>
</table>
</body>
</html>