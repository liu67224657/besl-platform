<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加模块</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var type = $('#select_type').val();
                if(type == '21'){
                    if ($('input[name=category]:checked').size() <= 0) {
                        alert("请选择游戏类型");
                        return false;
                    }
                    var name = $('input[name=category]:checked').attr('data-name');
                    $('#input_text_name').val(name);
                    var c = $('input[name=category]:checked').val();
                    $('#input_text_code').val('collection_game_category_' + c);
                }else{
                    var name = $('#input_text_name').val();
                    if(name.length <= 0){
                        alert("请填写名称");
                        return false;
                    }
                    var code = $('#input_text_code').val();
                    if(code.length <= 0){
                        alert("请填写编码");
                        return false;
                    }
                }
            });
        });

        function showCategory(dom) {
            var v = $(dom).val();
            if (v == '21') {
                $('#tr_linename').hide();
                $('#tr_linecode').hide();
                $('#tr_category').show();
            } else {
                $('#tr_linename').show();
                $('#tr_linecode').show();
                $('#tr_category').hide();
            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 网站-游戏库 >> 游戏库模块列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">
                        <span style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加一条Line</span>
                    </td>
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
            </table>
            <form action="/collection/game/clientline/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <%--<input type="hidden" name="linename" size="20" id="input_text_name"/>--%>
                            <%--<input id="input_text_code" type="hidden" size="20" name="linecode">--%>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            模块:
                        </td>
                        <td height="1">
                            <select name="itemtype" id="select_type" onchange="showCategory(this)">
                                <option value="">请选择</option>
                                <c:forEach items="${itemTypeCollection}" var="type">
                                    <%--<c:if test="${type.code == 21}">--%>
                                        <option value="${type.code}">
                                            <fmt:message key="client.item.type.${type.code}" bundle="${def}"/>
                                        </option>
                                    <%--</c:if>--%>
                                </c:forEach>
                            </select><span style="color:red">*必填项</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr id="tr_linename">
                        <td height="1" class="default_line_td">
                            名称:
                        </td>
                        <td height="1">
                            <input type="text" name="linename" size="20" id="input_text_name"/>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr id="tr_linecode">
                        <td height="1" class="default_line_td">
                            编码:
                        </td>
                        <td height="1">
                            <input id="input_text_code" type="text" size="20" name="linecode">
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(codeExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${codeExist}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr id="tr_category">
                        <td height="1" class="default_line_td">
                            游戏类型:
                        </td>
                        <td height="1">
                            <c:forEach items="${gameCateMap}" var="map" varStatus="c">
                                <input type="radio" value="${map.key}" name="category"
                                       data-name="${map.value}"/>${map.value}&nbsp;&nbsp;
                                <c:if test="${c.index%3==0&&c.index!=0}"><br/></c:if>
                            </c:forEach>
                            <input type="hidden" name="gamecategory" id="gamecategory"/>
                        </td>
                        <td height="1">
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