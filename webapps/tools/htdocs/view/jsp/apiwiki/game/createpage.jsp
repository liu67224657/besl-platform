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
                var gameradio = $("input[name='_gameid']:checked").val();
                if (gameradio == null) {
                    alert("请选择一个游戏");
                    return false;
                }
                $("#gameDbId").val(gameradio);

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
    <style>
        .name {
            max-width: 100px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 添加推荐游戏</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加推荐游戏</td>
                </tr>
                <tr>
                    <td>
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td>
                                    <form action="/apiwiki/game/createpage" method="post">
                                        <select name="type">
                                            <option value="1" <c:if test="${type=='1'}">selected</c:if>>游戏ID</option>
                                            <option value="2" <c:if test="${type=='2'}">selected</c:if>>游戏名称</option>
                                        </select>
                                        <input type="text" name="gameid" class="default_button" value=""/>
                                        <input type="submit" name="button" class="default_button" value="添加推荐游戏"/>
                                    </form>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <form action="/apiwiki/game/create" method="post" id="form_submit">
                <table width="80%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="5" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="center" width="50px">选择</td>
                        <td nowrap align="center" width="50px">游戏ID</td>
                        <td nowrap align="center" width="">游戏名称</td>
                        <td nowrap align="center" width="">别名</td>
                        <td nowrap align="center" width="70px">标签</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="5" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td align="center">
                                        <input type="radio" id="radio_${dto.gameDbId}" name="_gameid"
                                               value="${dto.gameDbId}" <c:if test="${ischeck==true}">checked</c:if>/>
                                    </td>
                                    <td nowrap align="center">${dto.gameDbId}</td>

                                    <td nowrap class="name" title="${dto.gameName}">${dto.gameName}</td>
                                    <td nowrap class="name" title="${dto.anotherName}">${dto.anotherName}</td>
                                    <td nowrap class="name" title="${dto.gameTag}">${dto.gameTag}</td>
                                </tr>
                            </c:forEach>


                            <input type="hidden" id="gameDbId" name="gameDbId" class="default_button"/>
                            <tr>
                                <td height="1" colspan="5" class="default_line_td"></td>
                            </tr>
                            <tr>
                                <td height="1" colspan="5" class="">
                                    <br/> 一句话推荐:<br/>
                                    <textarea maxlength="128" name="recommend" id="recommend" cols="50" rows="5"
                                    >${recommend}</textarea>
                                </td>

                            </tr>
                            <tr>
                                <td height="1" colspan="5" class="">
                                    撰写人:<br/>
                                    <input type="text" id="recommendAuth" name="recommendAuth" class="default_button"
                                           value="${recommendAuth}" size="52" maxlength="10"/>
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="5" height="1" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="5">
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
<script>
    $(document).ready(function () {
        $("input[id^='radio_']").click(function () {   //点击id为1开头但不为1Checkbox2的选择框触发事件
            var gameid = $(this).val();

            $.post("/apiwiki/game/json", {id: gameid}, function (req) {
                var rsobj = eval('(' + req + ')');
                if (rsobj.rs == 1) {
                    if (rsobj.result != null && rsobj.result != "") {
                        $("#recommendAuth").val(rsobj.result.recommendAuth);
                        $("#recommend").val(rsobj.result.recommend);
                    } else {
                        $("#recommendAuth").val("");
                        $("#recommend").val("");
                    }
                } else {
                    $("#recommendAuth").val("");
                    $("#recommend").val("");
                }
            });
        });
    });

</script>
</html>