<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>新游开测榜--修改item</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {

            <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
            $("#isHot").change(function () {
                if ($("#isHot").val() == 0) {
                    $(".newgame_block").hide();
                } else {
                    $(".newgame_block").show();
                }
                $("#category").change();
            });

            $("#category").change(function () {
                if ($("#category").val() == 0 || $("#isHot").val() == 0) {
                    $(".newgame_block_start").hide();
                    $(".newgame_block_end").hide();
                } else {
                    $(".newgame_block_start").show();
                    $(".newgame_block_end").show();
                }
            });
            $("#isHot").change();

            $("input[name=categoryColor][type=radio]").change(function () {
                        var name = $("input:radio[name=categoryColor]:checked").val();
                        if (name == 'gamecustom') {
                            $(".gamecustom").show();
                        } else {
                            $(".gamecustom").hide();
                        }
                    }
            );
            var categoryColor = '${item.categoryColor}';
            if (categoryColor != '') {
                $("input:radio[name=categoryColor][value=${item.categoryColor}]").prop("checked", true);

                if (categoryColor == 'gamecustom') {

                    $("#redirectType").val(${item.redirectType.code});
                    $("#url").val('${item.url}');
                }
            }
            $("input:radio[name=categoryColor][value=gamenone]").change();

            $("#customShowContentTr").hide();
            $("#showType").on("change", function () {
                var showType = $("#showType").val();
                if (showType == 1) {
                    $("#customShowContentTr").hide();
                } else if (showType == 2) {
                    $("#customShowContentTr").show();
                }
            });

            var showType = '${item.rate}';
            if (showType != '') {
                $("#showType").val(showType);
                $("#showType").change();
                $("#customShowContent").val('<c:out value="${item.author}" escapeXml="true" />');
            }
            </c:if>


            var lineCode = '${lineCode}';

            if (lineCode.indexOf('gc_newgame') > -1) {
                var isHot = '${item.displayType}';
                if (isHot != '') {
                    $("#isHot").val(isHot);
                }

                var category = '${item.category}';
                if (category != '') {
                    $("#category").val(category);
                }

                $("#isHot").change();
                if (isHot == '1' && category == '1') {
                    var time = new Date('${item.stateDate}');
                    var sTime = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();
                    $('#startTime').datebox('setValue', sTime);

                    var endTime = new Date(time.getTime() + ${item.contentid} * 1000);
                    var dTime = endTime.getFullYear() + '-' + (endTime.getMonth() + 1) + '-' + endTime.getDate();

                    $('#endTime').datebox('setValue', dTime);
                }
            }


            $("#submit").click(function () {
                var gamePublicTime = $('#gamePublicTime').datetimebox('getValue');
                var lineCode = '${lineCode}';

                //针对新游开测榜的单独验证
                if (lineCode.indexOf('gc_newgame') > -1) {
                    var isHot = $.trim($("#isHot").val());
                    var category = $.trim($("#category").val());
                    var startTime = $('#startTime').datebox('getValue');
                    var endTime = $('#endTime').datebox('getValue');
                    var categoryColor = $("input:radio[name=categoryColor]:checked").val();
                    var url = $.trim($("#url").val());
                    var showType = $.trim($("#showType").val());
                    var customShowContent = $.trim($("#customShowContent").val());

                    if (category == '1' && isHot == '1') {
                        if (startTime == '') {
                            alert("请填写起始时间!");
                            return false;
                        } else if (endTime == '') {
                            alert("请填写结束时间!");
                            return false;
                        }
                        var sTime = '';
                        var eTime = '';
                        var patternStart = /(\d{4})-(\d{1,2})-(\d{1,2})\s*/g;
                        if (patternStart.test(startTime)) {
                            sTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3).getTime();
                        }
                        var patternEnd = /(\d{4})-(\d{1,2})-(\d{1,2})\s*/g;
                        if (patternEnd.test(endTime)) {
                            eTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3).getTime();
                        }
                        if (sTime == '' || eTime == '' || eTime - sTime < 0) {
                            alert("结束时间需要大于起始时间");
                            return false;
                        }
                    }
                    if (categoryColor == 'gamecustom' && url == '') {
                        alert("请填写合适的链接");
                        return false;
                    }

                    if (showType == 2 && customShowContent == '') {
                        alert("请填写合适的自定义显示文字内容");
                        return false;
                    }

                }
                var displayOrder = $.trim($("#displayOrder").val());
                if (displayOrder == '' || !$.isNumeric(displayOrder) || displayOrder == 0) {
                    alert("请填写合适的排序值，且不能为0");
                    return false;
                }

                if (gamePublicTime == '') {
                    alert("请设置游戏的上市时间,然后再点击提交!");
                    return false;
                } else {
                    $("#form_submit").submit();
                }
            });

        });
    </script>


</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 新游开测,大家正在玩,热门的clientline</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:13px; ">新手游画报--${lineName}(${lineCode})修改一条记录</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/clientline/game/itemmodify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="itemId" value="${item.itemId}"/>
                            <input type="hidden" name="lineId" value="${item.lineId}"/>
                            <input type="hidden" name="lineName" value="${lineName}"/>
                            <input type="hidden" name="lineCode" value="${lineCode}"/>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            client_line_item表的id：
                        </td>
                        <td height="1" class="" width="10" width="200">
                            <input type="text" size="50" value="${item.itemId}" disabled="disabled"/>*
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            game_db表的_id：
                        </td>
                        <td height="1" class="" width="10" width="200">
                            <input type="text" size="50" id="gameDbId" value="${item.directId}" disabled="disabled"/>*
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            游戏名称：
                        </td>
                        <td height="1" class="" width="10" width="200">
                            <input type="text" size="50" value="${name}" disabled="disabled"/>*
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
                        <tr class="newgame">
                            <td height="1" class="default_line_td" width="150">
                                是否出现在热门页:
                            </td>
                            <td height="1">
                                <select id="isHot" name="isHot">
                                    <option value="0">不出现</option>
                                    <option value="1">出现</option>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr class="newgame_block">
                            <td height="1" class="default_line_td" width="150">
                                是否出现在热门页中的热门块:
                            </td>
                            <td height="1">
                                <select name="category" id="category">
                                    <option value="1">出现</option>
                                    <option value="0">不出现</option>
                                </select>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr class="newgame_block_start">
                            <td height="1" class="default_line_td" width="150">
                                出现在热门块中的开始时间(以天为单位):
                            </td>
                            <td height="1">
                                <input type="text" class="easyui-datebox" editable="false" id="startTime"
                                       name="startTime" value="${startTime}"/>*必填项 <span style="color: red;">条目在热门块中的排序按照"排序值" 由小到大的顺序排序</span>
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                        <tr class="newgame_block_end">
                            <td height="1" class="default_line_td" width="150">
                                出现在热门块中的结束时间(以天为单位):
                            </td>
                            <td height="1">
                                <input type="text" name="endTime" class="easyui-datebox" id="endTime"
                                       editable="false" value="${endTime}"/>*必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td" width="240">
                                选择开测时间显示方式:
                            </td>
                            <td height="1" width="240px">
                                <select name="showType" id="showType">
                                    <option value="1" selected="selected">精确时间</option>
                                    <option value="2">自定义显示的文字</option>
                                </select> *必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                        <tr id="customShowContentTr">
                            <td height="1" class="default_line_td" width="240">
                                自定义显示文字内容:
                            </td>
                            <td height="1" width="240px">
                                <input type="text" name="customShowContent" id="customShowContent" value=""/> *必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td">
                            排序值:
                        </td>
                        <td height="1">
                            <input type="text" name="displayOrder" size="32" id="displayOrder"
                                   value="<c:out value='${item.displayOrder}' escapeXml='true' />"/>*必填项
                            <c:if test="${fn:contains(lineCode, 'gc_newgame')}"> <span style="color: red;">此排序值仅适用于热门页中热门块的排序</span>  </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            上市时间:
                        </td>
                        <td height="1">
                            <input type="text" class="easyui-datetimebox" editable="false" id="gamePublicTime"
                                   value="<fmt:formatDate value='${item.itemCreateDate}'   pattern='yyyy-MM-dd HH:mm:ss' type='both'  />  "
                                   name="gamePublicTime"/>*必填项
                        </td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
                        <tr>
                            <td height="1" class="default_line_td" width="240">
                                链接事件:
                            </td>
                            <td height="1">
                                <input type="radio" checked="checked" name="categoryColor" value="gamenone"/> 无链接 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gamecover"/> 游戏封面 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gameposter"/> 宣传海报 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gamedetail"/> 游戏详情 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gamecustom"/> 自定义链接 &nbsp;&nbsp;
                            </td>
                        </tr>

                        <tr class="gamecustom">
                            <td height="1" class="default_line_td" width="150">
                                跳转类型 :
                            </td>
                            <td height="1" width="200">
                                <select id="redirectType" name="redirectType">
                                    <c:forEach items="${types}" var="item">
                                        <option value='${item.key}'>${item.key}__<fmt:message
                                                key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                    </c:forEach>
                                </select> <a target="view_window"
                                             href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                            </td>
                        </tr>
                        <tr class="gamecustom">
                            <td height="1" class="default_line_td" width="240">
                                链接:
                            </td>
                            <td height="1">
                                <input type="text" name="url" id="url" size="80"/>
                            </td>
                        </tr>
                    </c:if>


                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input type="submit" id="submit" class="default_button" value="提交">
                            <input type="button" class="default_button" value="返回"
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