<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>建立资料库</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/json2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function () {
            var financing =${gameDb.financing};
            $("#removeUploadInfo").click(function () {
                var downloadinfo = $("div[name='downloadinfo']").size();
                if (downloadinfo <= 1) {
                    alert('最少需要一个下载信息');
                    return;
                }
                $("div[name='downloadinfo']:last").remove();
            });


            $("input[name='fincheckbox']").each(function () {
                var finchecbox = $(this).val();
                if (financing & finchecbox) {
                    $(this).attr("checked", true);
                }
            });
            var area =${gameDb.area};
            $("input[name='checkbox']").each(function () {
                if (area & $(this).val()) {
                    $(this).attr("checked", true);
                }
            });
            <c:if test="${not empty gameDb.gameRate}">
            var rate = ${gameDb.gameRate};
            $("#gamerate").children("option").each(function () {
                if (rate == $(this).val()) {
                    $(this).attr("selected", true);
                }
            });
            </c:if>

            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
            $("input[name=cityradio]").click(function () {
                var city = this.value;
                if (city == "其他") {
                    $("#city").val("");
                    $("#city").css("display", "block");
                } else {
                    $("#city").val(city);
                    $("#city").css("display", "none");
                }
            });
            <c:forEach items="${gameDb.gameDBCategorySet}" var="set">
            <c:if test="${set.gameDbCategoryStatus==true}">
            $("#category" +${set.gameDbCategoryId}).attr("checked", true);
            </c:if>
            </c:forEach>

            <c:forEach items="${gameDb.gameDBDevicesSet}" var="set">
            <c:if test="${set.gameDbDeviceStatus==true}">
            $("#gamesystem" +${set.gameDbDeviceId}).attr("checked", true);
            </c:if>
            </c:forEach>

            $("input[name='teamnum']").each(function () {
                if ($(this).val() ==${gameDb.teamNum}) {
                    $(this).attr("checked", true);
                }
            });
            //进来显示已有的渠道信息

            var device = '';
            <c:forEach items="${gameDb.channelInfoSet}" var ="channel" varStatus="index">
            device = device + "${channel.channel_id}" + "_" + ${channel.device} +"@";

            var title = $("</br><span style='font-weight: bold'>下载信息" + ${index.index+1} +"</span></br>");
            var div = $("<div width='200' name='downloadinfo'></div>");
            var devicespan = $("<span>游戏设备：</span>");
            var deviceselect = $("<select id='device" + ${index.index+1} +"'></select>");
            var channelspan = $("<br/><span>渠　　道：</span>");
            var channelselect = $("<select id='channel" + ${index.index+1} +"'></select>");
            var channeloption0 = $('<option value="0">请选择</option>');


            var download = $('<br/><span>下载地址：</span><input type="text" name="download" id="download' + ${index.index+1} +'" size="32" value="${channel.channelDownloadInfo.download}"/><span style="color:red">*游戏设备，渠道，下载地址为必填项</span> <br/>');
            var gameversion = $('<span>游戏版本：</span><input type="text" name="gameversion" id="gameversion' + ${index.index+1} +'" value="${channel.channelDownloadInfo.gameversion}" size="32"/> <br/>');
            var gamesize = $('<span>游戏大小：</span><input type="text" name="gamesize" id="gamesize' + ${index.index+1} +'" value="${channel.channelDownloadInfo.gamesize}" size="32"/> <br/>');
            var systemversion = $('<span>系统版本：</span><input type="text" name="systemversion" id="systemversion' + ${index.index+1} +'" value="${channel.channelDownloadInfo.systemversion}" size="32"/> <br/>');
            deviceselect.append('<option value="0">请选择</option>');
            deviceselect.append("<option value='1' <c:if test="${channel.device=='1'}">selected</c:if> >iphone</option>");
            deviceselect.append("<option value='2' <c:if test="${channel.device=='2'}">selected</c:if>>ipad</option>");
            deviceselect.append("<option value='3' <c:if test="${channel.device=='3'}">selected</c:if>>android</option>");

            channelselect.append(channeloption0);
            <c:forEach items="${channelList}" var="channelList">
            $(channelselect).append($('<option value="${channelList.channelId}" <c:if test="${channel.channel_id==channelList.channelId}">selected</c:if>>${channelList.channelName}</option>'));
            </c:forEach>

            $(div).append(title);
            $(div).append(devicespan);
            $(div).append(deviceselect);
            $(div).append(channelspan);
            $(div).append(channelselect);
            $(div).append(download);
            $(div).append(gameversion);
            $(div).append(gamesize);
            $(div).append(systemversion);
            $("#insertline").before(div);
            </c:forEach>
            //获得原有的设备和渠道信息 传到后台做清空操作
            device = device.substring(0, device.length - 1);
            $("#resetchannelinfo").val(device);
//    点击添加一个新的div
            var length = 0;
            <c:if test="${gameDb.channelInfoSet!=null}">
            length =${gameDb.channelInfoSet.size()};
            </c:if>
            // var index = length != 0 ? (Number(length) + 1) : 1;
            $("#insertline").click(function () {
                var index = $("div[name='downloadinfo']").size() + 1;
                var title = $("</br><span style='font-weight: bold'>下载信息" + index + "</span></br>");
                var div = $("<div width='200' name='downloadinfo'></div>");
                var devicespan = $("<span>游戏设备：</span>");
                var deviceselect = $("<select id='device" + index + "'></select>");
                var deviceoption0 = $('<option value="0">请选择</option>');
                var deviceoption1 = $("<option value='1'>iphone</option>");
                var deviceoption2 = $("<option value='2'>ipad</option>");
                var deviceoption3 = $("<option value='3'>android</option>");

                var channelspan = $("<br/><span>渠　　道：</span>");
                var channelselect = $("<select id='channel" + index + "'></select>");
                var channeloption0 = $('<option value="0">请选择</option>');
                var download = $('<br/><span>下载地址：</span><input type="text" name="download" id="download' + index + '" size="32"/><span style="color:red">*游戏设备，渠道，下载地址为必填项</span> <br/>');
                var gameversion = $('<span>游戏版本：</span><input type="text" name="gameversion" id="gameversion' + index + '" size="32"/> <br/>')
                var gamesize = $('<span>游戏大小：</span><input type="text" name="gamesize" id="gamesize' + index + '" size="32"/> <br/>');
                var systemversion = $('<span>系统版本：</span><input type="text" name="systemversion" id="systemversion' + index + '" size="32"/> <br/>')
                $(deviceselect).append(deviceoption0);
                $(deviceselect).append(deviceoption1);
                $(deviceselect).append(deviceoption2);
                $(deviceselect).append(deviceoption3);
                $(channelselect).append(channeloption0);
                <c:forEach items="${channelList}" var="channelList">
                $(channelselect).append($('<option value="${channelList.channelId}" >${channelList.channelName}</option>'));
                </c:forEach>
                $(div).append(title);
                $(div).append(devicespan);
                $(div).append(deviceselect);
                $(div).append(channelspan);
                $(div).append(channelselect);
                $(div).append(download);
                $(div).append(gameversion);
                $(div).append(gamesize);
                $(div).append(systemversion);

                index = index + 1;

                $("#insertline").before(div);
            });

            //新游开测及游戏海报设置开始
            $("#posterShowContentTrIos").hide();
            $("#posterShowTypeIos").on("change", function () {
                var showType = $("#posterShowTypeIos").val();
                if (showType == 1) {
                    $("#posterShowContentTrIos").hide();
                } else if (showType == 2) {
                    $("#posterShowContentTrIos").show();
                }
            });

            var posterShowTypeIos = '${gameDb.gameDBCover.posterShowTypeIos}';
            if (posterShowTypeIos != '') {
                $("#posterShowTypeIos").val(posterShowTypeIos);
                $("#posterShowTypeIos").change();
            }
            $("#posterShowContentIos").val('${gameDb.gameDBCover.posterShowContentIos}');
            var posterGamePublicTimeIos = '${gameDb.gameDBCover.posterGamePublicTimeIos}';
            if (posterGamePublicTimeIos != '') {
                var dateIos = new Date(Number(posterGamePublicTimeIos));
                var strIos = dateIos.getFullYear() + '-' + (dateIos.getMonth() + 1) + '-' + dateIos.getDate() + ' ' + dateIos.getHours() + ':' + dateIos.getMinutes() + ':' + dateIos.getSeconds();
                //    console.log("strIos-->"+strIos);
                $("#posterGamePublicTimeIos").datetimebox('setValue', strIos);
            }


            $("#posterShowContentTrAndroid").hide();
            $("#posterShowTypeAndroid").on("change", function () {
                var showType = $("#posterShowTypeAndroid").val();
                if (showType == 1) {
                    $("#posterShowContentTrAndroid").hide();
                } else if (showType == 2) {
                    $("#posterShowContentTrAndroid").show();
                }
            });

            var posterShowTypeAndroid = '${gameDb.gameDBCover.posterShowTypeAndroid}';
            if (posterShowTypeAndroid != '') {
                $("#posterShowTypeAndroid").val(posterShowTypeAndroid);
                $("#posterShowTypeAndroid").change();
            }
            $("#posterShowContentAndroid").val('${gameDb.gameDBCover.posterShowContentAndroid}');
            var posterGamePublicTimeAndroid = '${gameDb.gameDBCover.posterGamePublicTimeAndroid}';
            if (posterGamePublicTimeAndroid != '') {
                var dateAndroid = new Date(Number(posterGamePublicTimeAndroid));
                var strAndroid = dateAndroid.getFullYear() + '-' + (dateAndroid.getMonth() + 1) + '-' + dateAndroid.getDate() + ' ' + dateAndroid.getHours() + ':' + dateAndroid.getMinutes() + ':' + dateAndroid.getSeconds();
                //  console.log("strAndroid-->"+strAndroid);

                $("#posterGamePublicTimeAndroid").datetimebox('setValue', strAndroid);
            }

            //新游开测及游戏海报设置结束

        });
        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif,*.jpeg",
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
            debug: false
        }
        var coustomImageSettings2 = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif,*.jpeg",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete2,
            upload_start_handler: uploadStart2,
            upload_success_handler: uploadSuccess2,
            upload_complete_handler: uploadComplete2,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button2",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,


            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false
        }


        var coustomImageSettings3 = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif,*.jpeg",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete3,
            upload_start_handler: uploadStart3,
            upload_success_handler: uploadSuccess3,
            upload_complete_handler: uploadComplete3,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button3",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,


            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false
        }

        function uploadStart2() {
            $('#loading2').css('display', '');
        }

        function fileDialogComplete2(numFilesSelected, numFilesQueued) {
            var piclength = $("img[name='gamepicurl']").size();
            if (piclength > 4) {
                alert('最多只能上传5张图片');
                return;
            }
            if (numFilesSelected == 0) {

            } else if (numFilesSelected > 5) {

                alert('最多只能上传5张图片');
                return;
//                    joymealert.alert({text:"请不要选择超过40张图片"});
            } else {
                if (numFilesQueued > 0) {
                    this.startUpload();
                } else {
                    alert('最多只能上传5张图片');
                }
            }
        }
        window.imgLocaL = 0;
        function uploadSuccess2(file, serverData) {
            imgLocaL = $("img[name='gamepicurl']").size() + 1;
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    var imgSrc = genImgDomain(jsonData.result[0], DOMAIN);
                    if (imgLocaL > 5) {

                    } else {
                        $("#menu_pic2").css("display", "none");
                        var img = $(' <span style="padding-left:15px;" id="gamepic' + imgLocaL + '" onclick="deletePic(this)"><img src="' + imgSrc + '"  name="gamepicurl" width="115" height="209" /></span>');
                        //$("#gamepic").attr("src", imgSrc);
                        //$("#picurl").val(imgSrc);
                        $("#upload").before(img);
                        $("#loading2").css("display", "none");
                    }
                    imgLocaL++;
                }
            } catch (ex) {
                this.debug(ex);
            }
        }

        function checkSubmit() {
            if ($("#coverAgreeNum").val() != "") {
                if (isNaN($("#coverAgreeNum").val())) {
                    alert("点赞数需要是数字！");
                    $("#coverAgreeNum").focus();
                    return false;
                }
            }

            var teamnum = $("input[name='teamnum']:checked").length;
            if ($("#gamename").val().replace(/^\s+|\s+$/g, '') == '') {
                alert('请填写游戏名称。');
                return false;
            }
            if ($("#input_menu_pic").val() == '') {
                alert("请上传游戏主图");
                return false;
            }
            if ($("input[name='category']:checked").length < 1) {
                alert("请选择游戏分类");
                return false;
            }
            if ($("input[name='gamesystem']:checked").length < 1) {
                alert("请选择游戏设备");
                return false;
            }
            if ($("#developer").val().replace(/^\s+|\s+$/g, '') == '') {
                alert("请填写开发商信息");
                return false;
            }
            if ($("select[name='gamerate']").val() == '') {
                alert("请填写游戏评分");
                return false;
            }
            var platformwiki = $("#platformwiki").val().replace(/^\s+|\s+$/g, '');
            if (platformwiki != '') {
                if (platformwiki.indexOf("http://www.joyme.com/appwiki/") < 0) {
                    alert("请填写正确的WIKI页地址：" + "\n" + "http://www.joyme.com/appwiki/" + "\n" + "如：http://www.joyme.com/appwiki/wzzj/index.shtml 或 http://www.joyme.com/appwiki/wzzj/52739.shtml");
                    return false;
                }
            }


            var category = '';
            $("input[name='category']:checked").each(function () {
                category += $(this).val() + "、";
            });
            category = category.substring(0, category.length - 1);
            $("#gamecategory").val(category);

            var categoryall = '';
            $("input[name='category']").each(function () {
                categoryall += $(this).val() + "、";
            });
            categoryall = categoryall.substring(0, categoryall.length - 1);
            $("#gamecategoryall").val(categoryall);

            var financing = 0;
            $("input[name='fincheckbox']:checked").each(function () {
                financing += Number($(this).val());
            });
            $("#financing").val(financing);

            var area = 0;
            $("input[name='checkbox']:checked").each(function () {
                area += Number($(this).val());
            });
            $("#area").val(area);

            var gamesystem = '';
            $("input[name='gamesystem']:checked").each(function () {
                gamesystem += $(this).val() + "、";
            });
            gamesystem = gamesystem.substring(0, gamesystem.length - 1);
            $("#gamedevice").val(gamesystem);

            var gamesystemall = '';
            $("input[name='gamesystem']").each(function () {
                gamesystemall += $(this).val() + "、";
            });
            gamesystemall = gamesystemall.substring(0, gamesystemall.length - 1);
            $("#gamedeviceall").val(gamesystemall);

            var gamepic = '';
            $("img[name='gamepicurl']").each(function () {
                var src = $(this).attr("src");
                gamepic = gamepic + (src + "@");
            });
            gamepic = gamepic.substring(0, gamepic.length - 1);

            $("#input_menu_pic2").val(gamepic);
//
//    var emailreg = '^[\w\.-]+?@([\w\-]+\.){1,2}[a-zA-Z]{2,3}$ ';
//    if (!emailreg.test($("#email").val())) {
//        alert("联系邮箱格式不正确，请修改后再提交。");
//        return false;
//    }

            var i = Number(1);
            var result = new Array();
            var bool = true;
            $("div[name='downloadinfo']").each(function () {
                var device = $("#device" + i).val();
                var channel = $("#channel" + i).val();
                var download = $("#download" + i).val().replace(/^\s+|\s+$/g, '');
                var gameversion = $("#gameversion" + i).val();
                var gamesize = $("#gamesize" + i).val();
                var systemversion = $("#systemversion" + i).val();
                if (device == '0') {
                    alert("请选择下载信息" + i + "里的游戏设备。");
                    bool = false;
                    return false;
                }
                if (channel == '0') {
                    alert("请选择下载信息" + i + "里的渠道。");
                    bool = false;
                    return false;
                }
//        if (download == '') {
//            alert("请填写下载信息" + i + "里的下载链接。");
//            bool = false;
//            return false;
//        }
//        if (gamesize == '') {
//            alert("请填写下载信息" + i + "里的游戏大小。");
//            bool = false;
//            return false;
//        }
                var channelDownloadInfo = {
                    'download': download,
                    'gameversion': gameversion,
                    'gamesize': gamesize,
                    'systemversion': systemversion
                };
                var obj = {
                    'device': device,
                    'channel_id': channel,
                    'channelDownloadInfo': channelDownloadInfo
                };
                result.push(JSON.stringify(obj));
                i = i + 1;
            });
            $('#input_download_info').attr('value', result.join('@'));
            return bool;

        }

        function deletePic(pic) {
            if (confirm("是否删除该图片？")) {
                var id = pic.id;
                $("#" + id).remove();

            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 条目维护 >> 游戏资料库</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">基本信息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="3" class="default_line_td">
                        <input type="hidden" name="gameid" value="${gameDb.gameDbId}"/>
                        <input type="hidden" name="gamecategoryall" id="gamecategoryall"
                               value=""/>
                        <input type="hidden" name="gamedeviceall" id="gamedeviceall"
                               value=""/>
                        <input type="hidden" name="resetchannelinfo" id="resetchannelinfo" value=""/>
                    </td>
                </tr>

                <tr>
                    <td height="1" class="default_line_td">
                        游戏名称 ：
                    </td>
                    <td height="1">
                        <input type="text" name="gamename" size="32" id="gamename" value="${gameDb.gameName}"/><span
                            style='color:red'>*必填项</span>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        其他名称 ：
                    </td>
                    <td height="1">
                        <input type="text" name="anothername" size="32" id="anothername"
                               value="${gameDb.anotherName}"/>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        平台 ：
                    </td>
                    <td height="1">
                        <input type="checkbox" value="0" name="appplatform"/>IOS&nbsp;
                        <input type="checkbox" value="1" name="appplatform"/>安卓&nbsp;
                        <span style="color:red">*必填项</span>网站游戏库平台
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        一跳二下载地址 ：
                    </td>
                    <td height="1">
                        <input type="text" name="appclickdownload" value="" id="appclickdownload"/>
                        <span style="color:red">*必填项</span>网站游戏库一跳二下载地址
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        推荐理由1 ：
                    </td>
                    <td height="1">

                        <textarea name="reason" cols="50" rows="8">${gameDb.recommendReason}</textarea> <span
                            style="color:red">* 用于摇一摇显示 20个字左右</span>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        推荐理由2 ：
                    </td>
                    <td height="1">
                        <textarea name="reason2" cols="50" rows="8">${gameDb.recommendReason2}</textarea><span
                            style="color:red">* 用于摇一摇显示 20个字左右</span>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        游戏主图 ：
                    </td>
                    <td height="1">
                        <c:choose>
                            <c:when test="${fn:length(gameDb.gameIcon)>0}">
                                <img id="menu_pic" src="${gameDb.gameIcon}"/>
                            </c:when>
                            <c:otherwise>
                                <img id="menu_pic" src="/static/images/default.jpg"/>
                            </c:otherwise>
                        </c:choose>

                        <span id="upload_button">上传</span>
                        <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                        <input id="input_menu_pic" type="hidden" name="iconurl" value="${gameDb.gameIcon}"> <span
                            style='color:red'>*必填项</span>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        游戏大小 ：
                    </td>
                    <td height="1">
                        <input type="text" name="gamecontentsize" size="32" value="${gameDb.gameSize}"/>
                    </td>
                    <td height="1" class="">

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        游戏分类 ：
                    </td>
                    <td height="1">

                        <c:forEach items="${map}" var="map" varStatus="c">
                            <input type="checkbox" value="${map.key}" name="category"
                                   id="category${map.key}"/>${map.value}&nbsp;&nbsp;
                            <c:if test="${c.index%3==0&&c.index!=0}"><br/></c:if>
                        </c:forEach>
                        <%--<input type="checkbox" value="1" name="category" id="category1"/>扮演&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="2" name="category" id="category2"/>策略&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="3" name="category" id="category3"/>模拟&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="4" name="category" id="category4"/>益智&nbsp;&nbsp; <br/>--%>
                        <%--<input type="checkbox" value="5" name="category" id="category5"/>休闲&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="6" name="category" id="category6"/>棋牌&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="7" name="category" id="category7"/>动作&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="8" name="category" id="category8"/>格斗&nbsp;&nbsp; <br/>--%>
                        <%--<input type="checkbox" value="9" name="category" id="category9"/>体育竞技&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="10" name="category" id="category10"/>音乐舞蹈&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="11" name="category" id="category11"/>模拟经营&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="12" name="category" id="category12"/>卡牌&nbsp;&nbsp; <br/>--%>
                        <%--<input type="checkbox" value="13" name="category" id="category13"/>网游&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="14" name="category" id="category14"/>射击&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="15" name="category" id="category15"/>赛车竞速&nbsp;&nbsp;--%>
                        <%--<input type="checkbox" value="16" name="category" id="category16"/>冒险解谜&nbsp;&nbsp; <br/>--%>
                        <%--<input type="checkbox" value="17" name="category" id="category17"/>桌面游戏&nbsp;&nbsp;--%>
                        <input type="hidden" name="gamecategory" id="gamecategory"/>
                        <span style='color:red'>*必填项</span>
                        <%--<input type="hidden" name="game"--%>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        游戏设备 ：
                    </td>
                    <td height="1">

                        <input type="checkbox" value="1" name="gamesystem" id="gamesystem1"/>iphone&nbsp;&nbsp;
                        <input type="checkbox" value="2" name="gamesystem" id="gamesystem2"/>ipad&nbsp;&nbsp;
                        <input type="checkbox" value="3" name="gamesystem" id="gamesystem3"/>安卓&nbsp;&nbsp;

                        <input type="hidden" name="gamedevice" id="gamedevice"/>
                        <span style='color:red'>*必填项</span>
                    </td>
                    <td height="1" class=>

                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        开发商 ：
                    </td>
                    <td height="1">
                        <input type="text" name="developer" id="developer" value="${gameDb.gameDeveloper}"/> <span
                            style='color:red'>*必填项</span>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        发行商 ：
                    </td>
                    <td height="1">
                        <input type="text" name="publishers" id="publishers" value="${gameDb.gamePublishers}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        评分 ：
                    </td>
                    <td height="1">
                        <select name="gamerate" id="gamerate">
                            <option value="">请选择</option>
                            <option value="1"> 1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                        </select>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        WIKI 地址 ：
                    </td>
                    <td height="1">
                        <input type="text" name="wikiurl" id="wikiurl" size="32" value="${gameDb.wikiUrl}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        客户端WIKI 地址 ：
                    </td>
                    <td height="1">
                        <input type="text" name="platformwiki" id="platformwiki" size="32"
                               value="${gameDb.platformWiki}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        CMS 地址 ：
                    </td>
                    <td height="1">
                        <input type="text" name="cmsurl" id="cmsurl" size="32" value="${gameDb.cmsUrl}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        客户端CMS 地址 ：
                    </td>
                    <td height="1">
                        <input type="text" name="platfromcms" id="platformcms" size="32"
                               value="${gameDb.platformCms}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        上市时间 ：
                    </td>
                    <td height="1">
                        <input type="text" class="Wdate"
                               onClick="WdatePicker({autoPickDate:true})"
                               readonly="readonly" name="gamepublictime" id="gamepublictime"
                               value="<fmt:formatDate value='${gameDb.gamePublicTime}' pattern='yyyy-MM-dd' type='both'/>"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">详细信息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td">
                        游戏图片 ：
                    </td>
                    <td height="1">
                        <c:choose>
                            <c:when test="${not empty gamePicList}">
                                <c:forEach items="${gamePicList}" var="pic" varStatus="picindex">
                        <span style="padding-left:15px;" id="gamepic${picindex.index+1}" onclick="deletePic(this);"><img
                                src="${pic}"
                                name="gamepicurl" width="115" height="209"
                                /></span>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <img id="menu_pic2" src="/static/images/default.jpg"/>
                            </c:otherwise>
                        </c:choose>
                        <a href="javascript:void(0)" id="upload"> <span id="upload_button2"
                                                                        class="upload_button">上传</span>
                        </a><span style="color:red">*点击图片可删除</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                        <input id="input_menu_pic2" type="hidden" name="picurl2" value="${gameDb.gamePic}">
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        游戏简介：
                    </td>
                    <td height="1">
                            <textarea name="gameprofile" rows="8" cols="50"
                                      id="gameprofile">${gameDb.gameProfile}</textarea>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        版本简介：
                    </td>
                    <td height="1">
                            <textarea name="versionprofile" rows="8" cols="50"
                                      id="versionprofile">${gameDb.versionProfile}</textarea>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        品牌：
                    </td>
                    <td height="1">
                        <c:forEach var="gameBrand" items="${gameBrandList}">
                            <input type="radio" value="${gameBrand.id}" name="gamebrandid"
                                   <c:if test="${gameDb.gameBrandId == gameBrand.id}">checked="checked"</c:if>/><img
                                src="${gameBrand.icon}" height="56" width="56"/>&nbsp;
                        </c:forEach>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联网：
                    </td>
                    <td height="1">
                        <select id="nettype" name="nettype">
                            <option value="">请选择</option>
                            <c:forEach var="net" items="${netTypes}">
                                <option value="${net.code}"
                                        <c:if test="${gameDb.gameNetType.code == net.code}">selected="selected"</c:if>>${net.name}</option>
                            </c:forEach>
                        </select><span style="color:red">*必填项</span>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        语言：
                    </td>
                    <td height="1">
                        <select id="languagetype" name="languagetype">
                            <option value="">请选择</option>
                            <c:forEach var="language" items="${languageTypes}">
                                <option value="${language.code}"
                                        <c:if test="${gameDb.gameLanguageType.code == language.code}">selected="selected"</c:if>>${language.name}</option>
                            </c:forEach>
                        </select><span style="color:red">*必填项</span>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        题材：
                    </td>
                    <td height="1">
                        <select id="themetype" name="themetype">
                            <option value="">请选择</option>
                            <c:forEach var="theme" items="${themeTypes}">
                                <option value="${theme.code}"
                                        <c:if test="${gameDb.gameThemeType.code == theme.code}">selected="selected"</c:if>>${theme.name}</option>
                            </c:forEach>
                        </select><span style="color:red">*必填项</span>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        人气：
                    </td>
                    <td height="1">
                        <input type="text" name="popular" id="popular" value="${gameDb.popular}"/><span
                            style="color:red">*必填项</span>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="1" class="default_line_td"></td>
                </tr>
            </table>

            <input type="button" value="增加下载信息" id="insertline"/>
            <input type="button" id="removeUploadInfo" value="删除一个下载地址"/>
            <input type="hidden" name="inputdownloadinfo" id="input_download_info"/>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">游戏封面</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td">
                        下载推荐：
                    </td>
                    <td height="1">
                        <input type="text" name="downloadRecommend" size="80" value="${gameDb.downloadRecommend}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        游戏封面图 ：
                    </td>
                    <td height="1">
                        <img id="menu_pic3" src="${gameDb.gameDBCover.coverPicUrl}" class="img_pic" height="200px"
                             width="200px"/>
                        <a href="javascript:void(0)" id="upload3"> <span id="upload_button3"
                                                                         class="upload_button">上传</span></a>
                            <span id="loading3" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                        <input id="input_menu_pic3" type="hidden" name="coverPicUrl"
                               value="${gameDb.gameDBCover.coverPicUrl}">

                    </td>
                    <td height="1" class=>

                    </td>
                </tr>

                <tr>
                    <td height="1" class="default_line_td">
                        点评标题：
                    </td>
                    <td height="1">
                        <input type="text" name="coverTitle" size="80" value="${gameDb.gameDBCover.coverTitle}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        点评描述：
                    </td>
                    <td height="1">
                        <input type="text" name="coverComment" size="80"
                               value="${gameDb.gameDBCover.coverComment}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        点评详情：
                    </td>
                    <td height="1">
                        <textarea name="coverDesc" rows="8" cols="70">${gameDb.gameDBCover.coverDesc}</textarea>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        字段1：
                    </td>
                    <td height="1">
                        描述：<input type="text" name="key1" size="20" value="${gameDb.gameDBCoverFieldJson.key1}"/>
                        评分：<select name="value1">
                        <option value="1" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='1'}">selected</c:if>>
                            1
                        </option>
                        <option value="2" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='2'}">selected</c:if>>
                            2
                        </option>
                        <option value="3" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='3'}">selected</c:if>>
                            3
                        </option>
                        <option value="4" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='4'}">selected</c:if>>
                            4
                        </option>
                        <option value="5" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='5'}">selected</c:if>>
                            5
                        </option>
                        <option value="6" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='6'}">selected</c:if>>
                            6
                        </option>
                        <option value="7" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='7'}">selected</c:if>>
                            7
                        </option>
                        <option value="8" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='8'}">selected</c:if>>
                            8
                        </option>
                        <option value="9" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='9'}">selected</c:if>>
                            9
                        </option>
                        <option value="10" <c:if test="${gameDb.gameDBCoverFieldJson.value1=='10'}">selected</c:if>>
                            10
                        </option>
                    </select>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        字段2：
                    </td>
                    <td height="1">
                        描述：<input type="text" name="key2" size="20" value="${gameDb.gameDBCoverFieldJson.key2}"/>
                        评分：<select name="value2">
                        <option value="1" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='1'}">selected</c:if>>
                            1
                        </option>
                        <option value="2" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='2'}">selected</c:if>>
                            2
                        </option>
                        <option value="3" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='3'}">selected</c:if>>
                            3
                        </option>
                        <option value="4" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='4'}">selected</c:if>>
                            4
                        </option>
                        <option value="5" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='5'}">selected</c:if>>
                            5
                        </option>
                        <option value="6" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='6'}">selected</c:if>>
                            6
                        </option>
                        <option value="7" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='7'}">selected</c:if>>
                            7
                        </option>
                        <option value="8" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='8'}">selected</c:if>>
                            8
                        </option>
                        <option value="9" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='9'}">selected</c:if>>
                            9
                        </option>
                        <option value="10" <c:if test="${gameDb.gameDBCoverFieldJson.value2=='10'}">selected</c:if>>
                            10
                        </option>
                    </select>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        字段3：
                    </td>
                    <td height="1">
                        描述：<input type="text" name="key3" size="20" value="${gameDb.gameDBCoverFieldJson.key3}"/>
                        评分：<select name="value3">
                        <option value="1" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='1'}">selected</c:if>>
                            1
                        </option>
                        <option value="2" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='2'}">selected</c:if>>
                            2
                        </option>
                        <option value="3" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='3'}">selected</c:if>>
                            3
                        </option>
                        <option value="4" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='4'}">selected</c:if>>
                            4
                        </option>
                        <option value="5" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='5'}">selected</c:if>>
                            5
                        </option>
                        <option value="6" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='6'}">selected</c:if>>
                            6
                        </option>
                        <option value="7" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='7'}">selected</c:if>>
                            7
                        </option>
                        <option value="8" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='8'}">selected</c:if>>
                            8
                        </option>
                        <option value="9" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='9'}">selected</c:if>>
                            9
                        </option>
                        <option value="10" <c:if test="${gameDb.gameDBCoverFieldJson.value3=='10'}">selected</c:if>>
                            10
                        </option>
                    </select>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        字段4：
                    </td>
                    <td height="1">
                        描述：<input type="text" name="key4" size="20" value="${gameDb.gameDBCoverFieldJson.key4}"/>
                        评分：<select name="value4">
                        <option value="1" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='1'}">selected</c:if>>
                            1
                        </option>
                        <option value="2" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='2'}">selected</c:if>>
                            2
                        </option>
                        <option value="3" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='3'}">selected</c:if>>
                            3
                        </option>
                        <option value="4" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='4'}">selected</c:if>>
                            4
                        </option>
                        <option value="5" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='5'}">selected</c:if>>
                            5
                        </option>
                        <option value="6" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='6'}">selected</c:if>>
                            6
                        </option>
                        <option value="7" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='7'}">selected</c:if>>
                            7
                        </option>
                        <option value="8" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='8'}">selected</c:if>>
                            8
                        </option>
                        <option value="9" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='9'}">selected</c:if>>
                            9
                        </option>
                        <option value="10" <c:if test="${gameDb.gameDBCoverFieldJson.value4=='10'}">selected</c:if>>
                            10
                        </option>
                    </select>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        字段5：
                    </td>
                    <td height="1">
                        描述：<input type="text" name="key5" size="20" value="${gameDb.gameDBCoverFieldJson.key5}"/>
                        评分：<select name="value5">
                        <option value="1" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='1'}">selected</c:if>>
                            1
                        </option>
                        <option value="2" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='2'}">selected</c:if>>
                            2
                        </option>
                        <option value="3" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='3'}">selected</c:if>>
                            3
                        </option>
                        <option value="4" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='4'}">selected</c:if>>
                            4
                        </option>
                        <option value="5" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='5'}">selected</c:if>>
                            5
                        </option>
                        <option value="6" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='6'}">selected</c:if>>
                            6
                        </option>
                        <option value="7" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='7'}">selected</c:if>>
                            7
                        </option>
                        <option value="8" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='8'}">selected</c:if>>
                            8
                        </option>
                        <option value="9" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='9'}">selected</c:if>>
                            9
                        </option>
                        <option value="10" <c:if test="${gameDb.gameDBCoverFieldJson.value5=='10'}">selected</c:if>>
                            10
                        </option>
                    </select>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="1" class="default_line_td"></td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        点赞数：
                    </td>
                    <td height="1">
                        <input type="text" id="coverAgreeNum" name="coverAgreeNum" size="80"
                               value="${gameDb.gameDBCover.coverAgreeNum}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>

                <tr>
                    <td height="1" class="default_line_td">
                        IOS游戏下载开关：
                    </td>
                    <td height="1">
                        <input type="radio" name="coverDownload" value="true"
                               <c:if test="${gameDb.gameDBCover.coverDownload=='true'}">checked</c:if>/>开
                        <input type="radio" name="coverDownload" value="false"
                               <c:if test="${gameDb.gameDBCover.coverDownload=='false'}">checked</c:if>/>关
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        Android游戏下载开关：
                    </td>
                    <td height="1">
                        <input type="radio" name="coverDownloadAndroid" value="true"
                               <c:if test="${gameDb.gameDBCover.coverDownloadAndroid=='true'}">checked</c:if>/>开
                        <input type="radio" name="coverDownloadAndroid" value="false"
                               <c:if test="${gameDb.gameDBCover.coverDownloadAndroid=='false'}">checked</c:if>/>关
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新游开测与游戏海报-ios</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td" width="240">
                        选择开测时间显示方式:
                    </td>
                    <td height="1">
                        <select name="posterShowTypeIos" id="posterShowTypeIos">
                            <option value="1" selected="selected">精确时间</option>
                            <option value="2">自定义显示的文字</option>
                        </select> *必填项
                    </td>
                    <td height="1">
                    </td>
                </tr>
                <tr id="posterShowContentTrIos">
                    <td height="1" class="default_line_td" width="240">
                        自定义显示文字内容:
                    </td>
                    <td height="1">
                        <input type="text" name="posterShowContentIos" id="posterShowContentIos" size="50"
                               maxlength="50" value=""/> *必填项
                    </td>
                    <td height="1">
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        上市时间:
                    </td>
                    <td height="1">
                        <input type="text" class="easyui-datetimebox" editable="false" id="posterGamePublicTimeIos"
                               name="posterGamePublicTimeIos"/>*必填项
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新游开测与游戏海报-Android</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td" width="240">
                        选择开测时间显示方式:
                    </td>
                    <td height="1">
                        <select name="posterShowTypeAndroid" id="posterShowTypeAndroid">
                            <option value="1" selected="selected">精确时间</option>
                            <option value="2">自定义显示的文字</option>
                        </select> *必填项
                    </td>
                    <td height="1">
                    </td>
                </tr>
                <tr id="posterShowContentTrAndroid">
                    <td height="1" class="default_line_td" width="240">
                        自定义显示文字内容:
                    </td>
                    <td height="1">
                        <input type="text" name="posterShowContentAndroid" id="posterShowContentAndroid" size="50"
                               maxlength="50" value=""/> *必填项
                    </td>
                    <td height="1">
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        上市时间:
                    </td>
                    <td height="1">
                        <input type="text" class="easyui-datetimebox" editable="false"
                               id="posterGamePublicTimeAndroid"
                               name="posterGamePublicTimeAndroid"/>*必填项
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">商务信息</td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td">
                        企业/团队名称 ：
                    </td>
                    <td height="1">
                        <input type="text" name="teamname" size="32" id="teamname" value="${gameDb.teamName}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        企业/团队人数：
                    </td>
                    <td height="1">
                        <input type="radio" name="teamnum" value="1"/>1-10人&nbsp;&nbsp;
                        <input type="radio" name="teamnum" value="2"/>11-20人&nbsp;&nbsp;
                        <input type="radio" name="teamnum" value="3"/>21-50人&nbsp;&nbsp;
                        <input type="radio" name="teamnum" value="4"/>51-100人&nbsp;&nbsp;
                        <br/>
                        <input type="radio" name="teamnum" value="5"/>101-200人&nbsp;&nbsp;
                        <input type="radio" name="teamnum" value="6"/>201人以上&nbsp;&nbsp;
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>

                <tr>
                    <td height="1" class="default_line_td">
                        所在城市：
                    </td>
                    <td height="1">
                        <input type="radio" name="cityradio" value="北京"
                               <c:if test="${gameDb.city=='北京'}">checked</c:if> />北京&nbsp;&nbsp;
                        <input type="radio" name="cityradio" value="广州"
                               <c:if test="${gameDb.city=='广州'}">checked</c:if> />广州&nbsp;&nbsp;
                        <input type="radio" name="cityradio" value="上海"
                               <c:if test="${gameDb.city=='上海'}">checked</c:if> />上海&nbsp;&nbsp;
                        <input type="radio" name="cityradio" value="福州"
                               <c:if test="${gameDb.city=='福州'}">checked</c:if>/>福州&nbsp;&nbsp;
                        <br/>
                        <input type="radio" name="cityradio" value="杭州"
                               <c:if test="${gameDb.city=='杭州'}">checked</c:if>/>杭州&nbsp;&nbsp;
                        <input type="radio" name="cityradio" value="成都"
                               <c:if test="${gameDb.city=='成都'}">checked</c:if>/>成都&nbsp;&nbsp;
                        <input type="radio" name="cityradio" value="深圳"
                               <c:if test="${gameDb.city=='深圳'}">checked</c:if>/>深圳&nbsp;&nbsp;
                        <input type="radio" name="cityradio" value="大连"
                               <c:if test="${gameDb.city=='大连'}">checked</c:if>/>大连&nbsp;&nbsp;
                        <br/>
                        <input type="radio" name="cityradio" value="其他"
                               <c:if test="${gameDb.city!='北京'&&gameDb.city!='广州'&&gameDb.city!='上海'&&gameDb.city!='福州'&&gameDb.city!='杭州'&&gameDb.city!='成都'&&gameDb.city!='深圳'&&gameDb.city!='大连'}">checked</c:if>/>其它
                        <input type="text"
                               <c:if test="${gameDb.city=='北京'||gameDb.city=='广州'||gameDb.city=='上海'||gameDb.city=='福州'||gameDb.city=='杭州'||gameDb.city=='成都'||gameDb.city=='深圳'||gameDb.city=='大连'}">style="display:none;"</c:if>
                               name="city" id="city" value="${gameDb.city}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        预计上市时间：
                    </td>
                    <td height="1">
                        <input type="text" class="Wdate"
                               onClick="WdatePicker({autoPickDate:true})"
                               readonly="readonly" name="publictime" id="publictime"
                               value="<fmt:formatDate value='${gameDb.publicTime}' pattern='yyyy-MM-dd' type='both'/>"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        融资方式：
                    </td>
                    <td height="1">
                        <input type="checkbox" name="fincheckbox" value="1"
                                /> 独代
                        <input type="checkbox" name="fincheckbox" value="2"/> 分成
                        <input type="hidden" name="financing" id="financing" value="${gameDb.financing}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系人：
                    </td>
                    <td height="1">
                        <input type="text" name="contacts" id="contacts" value="${gameDb.contacts}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系邮箱：
                    </td>
                    <td height="1">
                        <input type="text" name="email" id="email" value="${gameDb.email}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系电话：
                    </td>
                    <td height="1">
                        <input type="text" id="phone" name="phone" value="${gameDb.phone}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系QQ：
                    </td>
                    <td height="1">
                        <input type="text" id="qq" name="qq" value="${gameDb.qq}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" width="150">
                        显示类型:
                    </td>
                    <td height="1">
                        <select name="displayicon">
                            <option value="">请选择</option>
                            <option value="1"
                                    <c:if test="${gameDb.displayIcon==1}">selected</c:if> >NEW
                            </option>
                            <option value="2" <c:if test="${gameDb.displayIcon==2}">selected</c:if>>HOT</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        发行范围：
                    </td>
                    <td height="1">
                        <input type="checkbox" value="1" name="checkbox" id="radio1"/>国内&nbsp;&nbsp;
                        <input type="checkbox" value="2" name="checkbox" id="radio2"/>北美&nbsp;&nbsp;
                        <input type="checkbox" value="4" name="checkbox" id="radio3"/>南美&nbsp;&nbsp;
                        <input type="checkbox" value="8" name="checkbox" id="radio4"/>东南亚&nbsp;&nbsp;
                        <br/>
                        <input type="checkbox" value="16" name="checkbox" id="radio5"/>日韩&nbsp;&nbsp;
                        <input type="checkbox" value="32" name="checkbox" id="radio6"/>港台&nbsp;&nbsp;
                        <input type="checkbox" value="64" name="checkbox" id="radio7"/>西亚&nbsp;&nbsp;
                        <input type="checkbox" value="128" name="checkbox" id="radio8"/>非洲&nbsp;&nbsp;
                        <br/>
                        <input type="checkbox" value="256" name="checkbox" id="radio9"/>欧洲&nbsp;&nbsp;
                        <input type="checkbox" value="512" name="checkbox" id="radio10"/>其它&nbsp;&nbsp;
                        <input type="hidden" name="area" id="area" value="${gameDb.area}"/>
                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="1" class="default_line_td"></td>
                </tr>

            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr align="center">
                    <td colspan="3">
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