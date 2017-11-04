<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $().ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });
        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },
            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadStart,
            upload_success_handler: uploadSuccess,
            upload_complete_handler: uploadComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,


            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false}
        function isNotNull() {
            var appname = $("#input_text_appName").val();
            var size = $("#size").val();
            var pkname = $("#input_text_packageName").val();
            var icon = $("#input_menu_pic").val();
            var platform = $("#platform").val();
            var tagname = $("#tagname").val();
            var link1 = $("#downloadlinkurl").val();
            var channel1 = $("#channelname").val();
            var link2 = $("#downloadlinkurl2").val();
            var channel2 = $("#channelname2").val();
            var link3 = $("#downloadlinkurl3").val();
            var channel3 = $("#channelname3").val();
            var desc = $("#desc").val();
            var caregory = 0;

            if (appname == '') {
                alert('请输入APP名称');
                return false;
            }
            if (pkname == '') {
                alert('请输入包名');
                return false;
            }
            if (size == '') {
                alert("请输入应用大小");
                return false;
            }
            if (icon == '') {
                alert('请上传图片');
                return false;
            }
            if (platform == '请选择') {
                alert('请选择平台');
                return false;
            }
            if (tagname == '') {
                alert("请输入标签");
                return false;
            }

            if (channel1 == '' && channel2 == '' && channel3 == '') {
                alert("最少填写一个下载链接");
                return false;
            }
            if (channel1 != '') {
                if (channel1 == channel2) {
                    alert("请选择不同的渠道");
                    return false;
                }
                if (link1 == '') {
                    alert("请填写下载地址1");
                    return false;
                }

            }
            if (channel2 != '') {
                if (channel2 == channel3) {
                    alert("请选择不同的渠道");
                    return false;
                }
                if (link2 == '') {
                    alert("请填写下载地址2");
                    return false;
                }

            }
            if (channel3 != '') {
                if (channel1 == channel3) {
                    alert("请选择不同的渠道");
                    return false;
                }
                if (link3 == '') {
                    alert("请填写下载地址3");
                    return false;
                }

            }
            $("input[name='radio']").each(function () {
                if ($(this).attr("checked") == true) {
                    caregory += parseInt($(this).val());
                }
            })
            $("#caregory").val(caregory);
            if (desc == '') {
                alert('小编点评不能为空');
                return false;
            }
        }

        //选择渠道后 才能输入下载地址
        function inputReadOnly(channel, link) {
            var down1 = $("#" + link);
            var channel1 = $("#" + channel).val();
            if (channel1 == '') {
                down1.attr("readonly", "readonly");
                down1.val("");
            } else {
                down1.removeAttr("readonly");
            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> APP游戏列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">添加新游戏</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form action="/joymeapp/resource/creategame" method="post" id="form_submit" onsubmit="return isNotNull();">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
</tr>

<tr>
    <td height="1" class="default_line_td">
        游戏名称:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input type="hidden" name="appid" value="${appId}"/>
        <input id="input_text_appName" type="text" name="appname" size="32" value=""/>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        APP包名:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input id="input_text_packageName" type="text" name="pkgname" size="32" value=""/>
    </td>
    <td height="1" class=>
        *请填写该APP对应的包名，用于标识用户是否下载了该APP
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        所属公司:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input id="input_text_company" type="text" name="company" size="32" value=""/>
    </td>
    <td height="1" class=>
    </td>
</tr>

<tr>
    <td height="1" class="default_line_td">
        应用大小:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input id="size" type="text" name="size" size="32" value=""/>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        游戏上架时间:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input type="text" class="Wdate"
               onClick="WdatePicker({dateFmt:'yyyy年MM月dd日',vel:'publishtime',autoPickDate:true})"
                />
        <input type="hidden" name="publishtime" id="publishtime">
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        上传图片
    </td>
    <td>
        <img id="menu_pic" src="/static/images/default.jpg"/>
        <span id="upload_button">上传</span>
        <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
        <input id="input_menu_pic" type="hidden" name="iconurl" value="">
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        所属平台:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">

        <select name="platform" id="platform">
            <option value="请选择" selected="selected">请选择</option>
            <option value="0">IOS</option>
            <option value="1">Android</option>
        </select>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        标签:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input id="tagname" type="text" name="tagname" size="32" value=""/><span
            style="color:red">如：卡牌、塔防等(标签中间请用“空格”隔开)</span>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td colspan="3">

        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">下载链接&nbsp;<span style="color:red">*(选择渠道后才能输入)</span></td>
            </tr>
        </table>
    </td>

</tr>
<tr>
    <td height="1" class="default_line_td">
        渠道名称1:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <select name="channelname" onchange="inputReadOnly('channelname','downloadlinkurl')" id="channelname">
            <option value="">请选择</option>
            <c:forEach items="${channelList}" var="channel">
                <option value="${channel.channelId}">${channel.channelName}</option>
            </c:forEach>
        </select>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        下载地址1:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input type="text" name="downloadlinkurl" readonly="" id="downloadlinkurl"/>
    </td>
    <td height="1" class=>

    </td>
</tr>
<tr>
    <td colspan="3">
        　
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        渠道名称2:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <select name="channelname2" onchange="inputReadOnly('channelname2','downloadlinkurl2')" id="channelname2">
            <option value="">请选择</option>
            <c:forEach items="${channelList}" var="channel">
                <option value="${channel.channelId}">${channel.channelName}</option>
            </c:forEach>
        </select>
    </td>
    <td height="1" class=>
    </td>
</tr>

<tr>
    <td height="1" class="default_line_td">
        下载地址2:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input type="text" name="downloadlinkurl2" readonly="" id="downloadlinkurl2"/>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td colspan="3">
        　
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        渠道名称3:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <select name="channelname3" onchange="inputReadOnly('channelname3','downloadlinkurl3')" id="channelname3">
            <option value="">请选择</option>
            <c:forEach items="${channelList}" var="channel">
                <option value="${channel.channelId}">${channel.channelName}</option>
            </c:forEach>
        </select>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        下载地址3:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input type="text" name="downloadlinkurl3" readonly="" id="downloadlinkurl3"/>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td colspan="3">

        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">游戏评价</td>
            </tr>
        </table>
    </td>

</tr>
<tr>
    <td height="1" class="default_line_td">
        推荐标签:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <input type="checkbox" value="1" name="radio" id="radio1"/>好评
        <input type="checkbox" value="2" name="radio" id="radio2"/>大作
        <input type="checkbox" value="4" name="radio" id="radio3"/>经典
        <input type="checkbox" value="8" name="radio" id="radio4"/>新作
        <input type="checkbox" value="16" name="radio" id="radio5"/>最热
        <input type="hidden" value="" id="caregory" name="caregory"/>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        小编点评:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <textarea name="desc" id="desc" rows="10" cols="35"></textarea>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        关联的JOYMEAPP:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <select name="appkey" id="appkey">
            <option value="" selected="selected">请选择</option>
            <c:forEach var="app" items="${applist}">
                <option value="${app.appId}">${app.appName}</option>
            </c:forEach>
        </select>
    </td>
    <td height="1" class=>*慎重填写用于统计该APP的猜你喜欢</td>
</tr>
<tr align="center">
    <td colspan="3">
        <input name="Submit" type="submit" class="default_button" value="添加">
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