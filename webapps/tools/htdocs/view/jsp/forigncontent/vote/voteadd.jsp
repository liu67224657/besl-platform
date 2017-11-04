<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>wiki 投票管理---添加</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css">
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript">
        $(document).ready(function (e) {

            $("#maxchooseitems").hide();
            $("#prompttext").hide();

            var removeStatus = '${removeStatus}';
            if (removeStatus != '') {

                $("#removeStatus").val(removeStatus);
            }


            var uri = '${uri}';
            if (uri != '') {

                $("#uri").val(uri);
            }


            var description = '${description}';
            if (description != '') {
                var jsonObj = $.parseJSON(description);

                $("#choosetype").val(jsonObj.choosetype);
                if (jsonObj.choosetype == '1') {
                    $("#maxchooseitems").show();
                    $("#prompttext").show();
                    $("#maxchooseitems").val(jsonObj.maxchooseitems);
                } else {
                    $("#maxchooseitems").hide();
                    $("#prompttext").hide();

                }
                $("#restrict").val(jsonObj.restrict);
                $("#resultvisible").val(jsonObj.resultvisible);

                $('#starttime').datetimebox('setValue', jsonObj.starttime);
                $('#endtime').datetimebox('setValue', jsonObj.endtime);

            }

            $("#choosetype").click(function () {
                if ($("#choosetype").val() == "1") {
                    $("#maxchooseitems").show();
                    $("#prompttext").show();
                } else {

                    $("#maxchooseitems").hide();
                    $("#prompttext").hide();

                }
            });

            //是否永久复选框
            $("#endtimeforever").on("click", function () {
                if ($("#endtimeforever").prop("checked")) {
                    $('#endtime').datetimebox('setValue', '2037-12-31 23:59:59');
                } else {
                    var now = new Date();
                    var sevenDayLaterLong = now.getTime() + 1000 * 60 * 60 * 24 * 7;
                    var sevenDayLaterDate = new Date(sevenDayLaterLong);
                    var sevenDayLaterStr = sevenDayLaterDate.getFullYear() + '-' + (sevenDayLaterDate.getMonth() + 1) + '-' + sevenDayLaterDate.getDate();
                    sevenDayLaterStr += ' ' + sevenDayLaterDate.getHours() + ':' + sevenDayLaterDate.getMinutes() + ':' + sevenDayLaterDate.getSeconds();
                    $('#endtime').datetimebox('setValue', sevenDayLaterStr);
                }
            });


            $("#submit").click(function () {
                var title = $.trim($("#title").val());
                var starttime = $.trim($('#starttime').datetimebox('getValue'));
                var endtime = $.trim($('#endtime').datetimebox('getValue'));

                if (title == '') {
                    alert("请填写投票主题!");
                    return false;
                }


                if (starttime == '') {
                    alert("请投置投票开始时间！");
                    return false;

                }
                if (endtime == '') {
                    alert("请投置投票结束时间！");
                    return false;

                }

                var sTime = '';
                var eTime = '';
                var patternStart = /(\d{4})-(\d{1,2})-(\d{1,2})\s+(\d{1,2}):(\d{1,2}):(\d{1,2})/g;
                if (patternStart.test(starttime)) {
                    sTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3, RegExp.$4, RegExp.$5, RegExp.$6).getTime();
                }
                var patternEnd = /(\d{4})-(\d{1,2})-(\d{1,2})\s+(\d{1,2}):(\d{1,2}):(\d{1,2})/g;
                if (patternEnd.test(endtime)) {
                    eTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3, RegExp.$4, RegExp.$5, RegExp.$6).getTime();
                }
                if (sTime == '' || eTime == '' || eTime - sTime <= 0) {
                    alert("结束时间需要大于起始时间");
                    return false;

                }

                var minDate = new Date();
                minDate.setHours(0);
                minDate.setMinutes(0);
                minDate.setSeconds(0);
                minDate.setMilliseconds(0);

                minDateLong = minDate.getTime();
                var maxDate = new Date(2037, 11, 31, 23, 59, 59).getTime();
                if (sTime < minDateLong) {
                    alert("开始时间需要大于等于今天的0:00:00");
                    return false;
                }
                if (eTime > maxDate) {
                    alert("结束时间需要小于等于2037-12-31 23:59:59");
                    return false;
                }


                var jsonObj = new Object();
                jsonObj.choosetype = $("#choosetype").val();
                if (jsonObj.choosetype == '1') {
                    jsonObj.maxchooseitems = $("#maxchooseitems").val();
                } else {
                    jsonObj.maxchooseitems = '1';
                }

                jsonObj.restrict = $("#restrict").val();
                jsonObj.resultvisible = $("#resultvisible").val();

                jsonObj.starttime = starttime;
                jsonObj.endtime = endtime;

                var result = JSON.stringify(jsonObj);

                $("#description").val(result);
                $("#form_submit").submit();

            });

        });

    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 内容专题维护 >> wiki 投票管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">wiki 投票管理---添加</td>
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
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/comment/vote/wiki/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <input type="hidden" name="description" id="description"/>
                    <tr>
                        <td height="1" class="default_line_td">
                            创建者:
                        </td>
                        <td height="1">
                            <select name="uri" id="uri">
                                <option value="joyme_diandian">joyme_diandian</option>
                                <option value="joyme_ting">joyme_ting</option>
                                <option value="joyme_ht">joyme_ht</option>
                                <option value="joyme_sb8jyrus">joyme_sb8jyrus</option>
                            </select>
                            *必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            投票主题:
                        </td>
                        <td height="1">
                            <input type="text" name="title" id="title" size="50" maxlength="50" value="${title}"/>
                            *必填项(最长50个字符)
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="120">
                            选择模式:
                        </td>
                        <td height="1">
                            <select id="choosetype">
                                <option value="0">单选</option>
                                <option value="1">多选</option>
                            </select>
                            <label id="prompttext" for="maxchooseitems">最多可选项数:</label>
                            <select id="maxchooseitems">
                                <option value="2">最多两项</option>
                                <option value="3">最多三项</option>
                                <option value="4">最多四项</option>
                                <option value="5">最多五项</option>
                                <option value="6">最多六项</option>
                                <option value="7">最多七项</option>
                            </select>
                            *必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="120">
                            投票限制:
                        </td>
                        <td height="1">
                            <select id="restrict">
                                <option value="0">无限制</option>
                                <option value="1">一个IP每天限投一次</option>
                                <option value="2">一个用户限投一次</option>
                                <option value="3">一个浏览器限投一次</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="120">
                            投票结果可见性:
                        </td>
                        <td height="1">
                            <select id="resultvisible">
                                <option value="0">任何人可见</option>
                                <option value="1">投票后可见</option>
                                <option value="2">只有tools后台可见</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="120">
                            投票开始时间:
                        </td>
                        <td height="1">
                            <input type="text" class="easyui-datetimebox" editable="false" id="starttime"
                                   value="${starttime}"/>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="120">
                            投票结束时间:
                        </td>
                        <td height="1">
                            <input type="text" class="easyui-datetimebox" id="endtime" value="${endtime}"
                                   editable="false"/>*必填项 <input type="checkbox" id="endtimeforever"/> 是否永久
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            状态:
                        </td>
                        <td height="1">
                            <select name="removeStatus" id="removeStatus">
                                <option value="ing">已审核</option>
                                <option value="n">未审核</option>
                                <option value="y">已删除</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
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