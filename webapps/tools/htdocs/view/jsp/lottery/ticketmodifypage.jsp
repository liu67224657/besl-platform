<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>彩票管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/sharehandler.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function() {
            if (${ticket.win_type==1}) {
                changetype();
            }
        });

        function checkticket() {
            var name = $("#input_text_name").val();
            var desc = $("#textarea_desc").val();
            var baserate = $("#input_type_baserate").val();
            var level = $("#input_type_awardlevelcount").val();
            var wintype = $("#input_text_wintype").val();

            var starttime = $("#starttime").val();
            var endtime = $("#endtime").val();

            if (name == '') {
                alert('彩票名称不能为空');
                return false;
            }

            if (desc == '') {
                alert('彩票描述不能为空');
                return false;
            }
            if (baserate == '') {
                alert('中奖几率不能为空');
                return false;
            }
            if (level == '') {
                alert('请输入一共有几等奖');
                return false;
            }
            var reg = /^[1-5]$/;
            if (!reg.test(level)) {
                alert("奖品等级请填写1-5");
                return false;
            }

            if (starttime == '') {
                alert('请选择开始时间');
                return false;
            }
            if (endtime == '') {
                alert('请选择结束时间');
                return false;
            }

            if (starttime > endtime) {
                alert('开始时间不能晚于结束时间');
                return false;
            }

            if (wintype == '请选择') {
                alert('请选择开奖类型');
                return false;
            }
            if (wintype == '1') {
                var wintime = $("#wintime").val();
                if (wintime == '') {
                    alert('请选择开奖时间');
                    return false;
                }

                if (wintime < endtime) {
                    alert('开奖时间不能早于结束时间');
                    return false;
                }


            }
        }

        function changetype() {
            var wintype = $("#input_text_wintype").val();
            if (wintype == '1') {
                $("#wintimediv").css({
                            'display':'block'
                        });
            } else {
                $("#wintimediv").css({
                            'display':'none'
                        });

                $("#wintime").val("");
            }
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 修改彩票</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改彩票</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/ticket/menu/modify" method="post" id="form_submit" onsubmit="return checkticket()">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">

                    <tr>
                        <td height="1" class="default_line_td">
                        </td>
                        <td height="1" class="" width="10">
                            <input type="hidden" name="ticketid" size="20" value="${ticket.ticketId}"
                                   id="input_text_id"/>
                        </td>

                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            彩票名称:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="ticketname" size="20" value="${ticket.ticketName}"
                                   id="input_text_name"/>
                        </td>

                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="ticketdesc" id="textarea_desc" rows="5" cols="100"
                                      style="width: 100%;float: left"
                                      class="default_input_multiline">${ticket.ticketDesc}</textarea>
                        </td>

                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            中奖概率：
                        </td>
                        <td height="1">
                            <input type="text" size="20" onkeyup="value = value.replace(/\D|^0/g, '');" maxlength="13"
                                   name="baserate" value="${ticket.base_rate}"
                                   id="input_type_baserate"/>
                        </td>

                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            总共几等奖：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" onkeyup="value = value.replace(/\D|^0/g, '');" maxlength="3"
                                   name="awardlevelcount" value="${ticket.awardLevelCount}"
                                   id="input_type_awardlevelcount"/>
                        </td>

                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            开始时间：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00',autoPickDate:true})"
                                   name="starttime"
                                   id="starttime"
                                   readonly="readonly" autoPickDate="true" value="${ticket.start_time}"/>
                        </td>

                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            结束时间：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" class="Wdate" value="${ticket.end_time}"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00',autoPickDate:true})"
                                   name="endtime" id="endtime"
                                   readonly="readonly" autoPickDate="true"/>
                        </td>

                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            开奖类型：
                        </td>
                        <td height="1" class="" width="10">
                            <select name="wintype" id="input_text_wintype" onchange="changetype();">
                                <option value="请选择">请选择</option>
                                <option value="0"
                                        <c:if test="${ticket.win_type==0}">selected</c:if> >手动开奖
                                </option>
                                <option value="1" <c:if test="${ticket.win_type==1}">selected="selected"</c:if>>自动开奖
                                </option>
                            </select>

                            <div style="display:none;" id="wintimediv">
                                请选择开奖日期：<input type="text" class="Wdate"
                                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00',autoPickDate:true})"
                                               value="${ticket.winCronexp}" name="wintime"
                                               id="wintime"
                                               readonly="readonly" autoPickDate="true"/>
                            </div>
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