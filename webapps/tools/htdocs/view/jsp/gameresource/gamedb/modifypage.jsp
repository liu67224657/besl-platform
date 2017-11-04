<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>建立资料库</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link href="/static/include/css/Jcrop-0.9.12/jquery.Jcrop.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/ajaxfileupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script src="/static/include/js/Jcrop-0.9.12/jquery.Jcrop.js"></script>
    <style>
        .show {
            display: table-row
        }

        .hide {
            display: none
        }

        * {
            margin: 0;
            padding: 0;
        }

        /*.wrap{*/
        /*margin: 100px auto;*/
        /*width: 600px;*/
        /*height: 50px;*/
        /*}*/
        #inputword { /*width: 500px;*/
            height: 100%;
            display: block;
            float: left;
        }

        #search {
            width: 90px;
            height: 100%;
            display: block;
        }

        ul, li {
            list-style: none;
        }

        .show-history {
            min-height: 30px;
        }

        .show-history li {
            position: relative;
            display: inline-block;
            border: 1px solid #95B8E7;
            border-radius: 5px;
            padding: 5px 10px;
        }

        .show-history li .close {
            position: absolute;
            width: 10px;
            height: 10px;
            right: -5px;
            top: 0;
            cursor: pointer;
            background-color: red;
            font-size: 12px;
            line-height: 10px;
            text-align: center;
            color: #fff;
            border-radius: 50%;
        }

        .append-div > li {
            padding: 5px;

        }

        .append-div > li:hover {
            border: 1px solid #b7d2ff;
            color: #000000;
            background: #eaf2ff;
        }
    </style>
    <script>
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
            var coustomSwfu4 = new SWFUpload(coustomImageSettings4);
            var coustomSwfu5 = new SWFUpload(coustomImageSettings5);

            $('input[id^=input_checkbox_]').on('click', function () {
                var idx = $(this).attr('id').replace('input_checkbox_', '');
                if ($(this).is(':checked')) {
                    $('#table_' + idx).show();
                } else {
                    $('#table_' + idx).hide();
                }
            })

            var platformHtml = '';
            <c:forEach var="mplat" items="${mPlatforms}">
            platformHtml += '<option value="${mplat.code}" >${mplat.desc}</option>';
            </c:forEach>

            var channelHtml = '';
            <c:forEach items="${channelList}" var="channel">
            channelHtml += '<option value="${channel.channelId}" id="channel_${channel.channelId}">${channel.channelName}</option>';
            </c:forEach>

            $('#insertline').on('click', function () {
                var idx = $(this).attr('data-idx');

                var html = '<tr><td><table id="table_qudao' + idx + '">' +
                    '<tr>' +
                    '<td height="1" class="default_line_td">平台：</td>' +
                    '<td height="1"><select id="platform' + idx + '"><option value="">请选择</option>' + platformHtml + '</select></td>' +
                    '<td height="1" class=></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td height="1" class="default_line_td">渠道：</td>' +
                    '<td height="1"><select id="channel' + idx + '"><option value="">请选择</option>' + channelHtml + '</select></td>' +
                    '<td height="1" class=></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td height="1" class="default_line_td">下载地址：</td>' +
                    '<td height="1"><input type="text" id="download' + idx + '" value=""/></td>' +
                    '<td height="1" class=></td>' +
                    '</tr>' +
                    '<tr><td height="1" class="default_line_td">游戏版本：</td>' +
                    '<td height="1"><input type="text" id="version' + idx + '" value=""/></td>' +
                    '<td height="1" class=></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td height="1" class="default_line_td">游戏大小：</td>' +
                    '<td height="1"><input type="text" id="size' + idx + '" value=""/></td>' +
                    '<td height="1" class=></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td height="1" class="default_line_td">系统版本：</td>' +
                    '<td height="1"><input type="text" id="system' + idx + '" value=""/></td>' +
                    '<td height="1" class=></td>' +
                    '</tr>' +
                    '<tr><td colspan="3" height="1"><input type="button" id="remove' + idx + '" value="删除" onclick="removeChannelDownload(this)"/></td></tr>' +
                    '<tr><td colspan="3" height="1" class="default_line_td"></td></tr>' +
                    '</table></td></tr>';
                $('#tr_before').before(html);
            });

            //新游开测及游戏海报设置开始
            var showType = $("#posterShowTypeIos").val();
            if (showType == 1) {
                $("#posterShowContentTrIos").hide();
            } else if (showType == 2) {
                $("#posterShowContentTrIos").show();
            }
            $("#posterShowTypeIos").on("change", function () {
                var showType = $("#posterShowTypeIos").val();
                if (showType == 1) {
                    $("#posterShowContentTrIos").hide();
                } else if (showType == 2) {
                    $("#posterShowContentTrIos").show();
                }
            });

            var showType = $("#posterShowTypeAndroid").val();
            if (showType == 1) {
                $("#posterShowContentTrAndroid").hide();
            } else if (showType == 2) {
                $("#posterShowContentTrAndroid").show();
            }
            $("#posterShowTypeAndroid").on("change", function () {
                var showType = $("#posterShowTypeAndroid").val();
                if (showType == 1) {
                    $("#posterShowContentTrAndroid").hide();
                } else if (showType == 2) {
                    $("#posterShowContentTrAndroid").show();
                }
            });

            $('#form_submit').on('submit', function () {
                if ($("#input_menu_pic").val() == '') {
                    alert("请上传游戏图标");
                    return false;
                }

                var gamename = $("#gamename").val().replace(/^\s+|\s+$/g, '');
                if (getLength(gamename) <= 0 || getLength(gamename) > 20) {
                    alert('游戏名称在1~20个字符长度');
                    return false;
                }

                var anothername = $.trim($("#anothername").val());
                if (anothername == "") {
                    alert('请填写别名');
                    return false;
                }

                var gamePlatformType = $('input[name=gameplatformtype]').val();
                if (gamePlatformType == '1') {
                    if ($('input[name=mplatform]:checked').size() <= 0) {
                        alert('请选择平台！');
                        return false;
                    }
                } else {
                    if ($('input[name=pcplatform]:checked').size() <= 0 && $('input[name=pspplatform]:checked').size() <= 0 && $('input[name=tvplatform]:checked').size() <= 0) {
                        alert('请选择平台！');
                        return false;
                    }
                }

                var gamepublictime = $.trim($("#gamepublictime").val());
                if (gamepublictime == "") {
                    alert('请填写上市时间');
                    return false;
                }

                var officialwebsite = $.trim($("#officialwebsite").val());
                if (officialwebsite == "") {
                    alert('请填写官网地址');
                    return false;
                }

                if ($('input[name=category]:checked').size() <= 0) {
                    alert("请选择类型");
                    return false;
                }

                var developer = $.trim($("#developer").val());
                if (developer == "") {
                    alert('请填写开发商');
                    return false;
                }

                var gamepic = '';
                $("img[name='gamepicurl']").each(function () {
                    var src = $(this).attr("src");
                    if (src != '/static/images/default.jpg') {
                        gamepic += src + ",";
                    }
                });
                gamepic = gamepic.substring(0, gamepic.length - 1);
                $("#input_menu_pic2").val(gamepic);


                if ($("#value1").val().length <= 0 || $("#value2").val().length <= 0 || $("#value3").val().length <= 0 || $("#value4").val().length <= 0 || $("#value5").val().length <= 0) {
                    alert("请填写玩霸评分");
                    return false;
                }

                if ($("#coverAgreeNum").val() != "") {
                    if (isNaN($("#coverAgreeNum").val())) {
                        alert("玩霸点赞数需要是数字！");
                        return false;
                    }
                }

                var financing = 0;
                $("input[name='financing-checkbox']:checked").each(function () {
                    financing += Number($(this).val());
                });
                $("#financing").val(financing);

                var area = 0;
                $("input[name='area-checkbox']:checked").each(function () {
                    area += Number($(this).val());
                });
                $("#area").val(area);

                var i = Number(1);
                var result = new Array();
                var bool = true;
                $('table[id^=table_qudao]').each(function () {
                    var platform = $("#platform" + i).val();
                    var channel = $("#channel" + i).val();
                    var download = $("#download" + i).val();
                    var version = $("#version" + i).val();
                    var size = $("#size" + i).val();
                    var system = $("#system" + i).val();
                    var channelName = $('#channel_' + channel).text();

                    if (platform.length > 0 || channel.length > 0 || download.length > 0 || version.length > 0 || size.length > 0 || system.length > 0) {
                        if (platform == '') {
                            alert("请选择第" + i + "个渠道下载信息的平台");
                            bool = false;
                            return false;
                        }
                        if (channel == '') {
                            alert("请选择第" + i + "个渠道下载信息的渠道");
                            bool = false;
                            return false;
                        }
                        if (download == '') {
                            alert("请填写第" + i + "个渠道下载信息的下载地址");
                            bool = false;
                            return false;
                        }
                        var channelDownloadInfo = {
                            'download': download,
                            'gameversion': version,
                            'gamesize': size,
                            'systemversion': system,
                            'channelName': channelName
                        };
                        var channelInfo = {
                            'device': platform,
                            'channel_id': channel,
                            'channelDownloadInfo': channelDownloadInfo
                        };
                        //var jsonChannel = JSON.stringify(channelInfo);
                        result.push(channelInfo);
                    }
                    i = i + 1;
                });
                $('#input_download_info').val(JSON.stringify(result));


                var commentGamePicurl = '';
                $("img[name='commentGamePic']").each(function () {
                    var src = $(this).attr("src");
                    if (src != '/static/images/default.jpg') {
                        commentGamePicurl += src + ",";
                    }
                });

                commentGamePicurl = commentGamePicurl.substring(0, commentGamePicurl.length - 1);

                $("#input_menu_pic5").val(commentGamePicurl);

                if ($("#gameTag").val() == '') {
                    alert("请选择游戏标签");
                    return false;
                } else {
                    var gameTag = $("#gameTag").val();
                    if (gameTag.substr(0, 1) == ',') {
                        $("#gameTag").val(gameTag.substr(1));
                    }
                }

                return bool;
            })


        });
        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif;*.jpeg",
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
            file_types: "*.jpg;*.png;*.gif;*.jpeg",
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
            file_types: "*.jpg;*.png;*.gif;*.jpeg",
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
            } else {
                this.startUpload();
            }

        }
        window.imgLocaL = 0;
        function uploadSuccess2(file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {

                    var imgSrc = genImgDomain(jsonData.result[0], DOMAIN);

                    if (imgLocaL > 5) {

                    } else {
                        $("#menu_pic2").css("display", "none");
                        var img = $(' <span style="padding-left:15px;"  id="gamepic' + imgLocaL + '" onclick="deletePic(this)"><img src="' + imgSrc + '" name="gamepicurl" width="115" height="209"/></span>');
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

        function deletePic(pic) {
            if (confirm("是否删除该图片？")) {
                var id = pic.id;
                $("#" + id).remove();
            }
        }

        function getLength(str) {
            if (str == null || str.length == 0) {
                return 0;
            }
            var len = str.length;
            var reLen = 0;
            for (var i = 0; i < len; i++) {
                if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) {
                    // 全角
                    reLen += 1;
                } else {
                    reLen += 0.5;
                }
            }
            return Math.ceil(reLen);
        }

        function removeChannelDownload(dom) {
            var idx = $(dom).attr('id').replace('remove', '');
            $('#table_qudao' + idx).remove();
        }


        var coustomImageSettings4 = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif;*.jpeg",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete4,
            upload_start_handler: uploadStart4,
            upload_success_handler: uploadSuccess4,
            upload_complete_handler: uploadComplete4,
            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button4",
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

        var coustomImageSettings5 = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif;*.jpeg",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete5,
            upload_start_handler: uploadStart5,
            upload_success_handler: uploadSuccess5,
            upload_complete_handler: uploadComplete5,
            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button5",
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
        function uploadStart5() {
            $('#loading5').css('display', '');
        }
        function fileDialogComplete5(numFilesSelected, numFilesQueued) {
            var piclength = $("img[name='commentGamePic']").size();
            if (piclength > 4) {
                alert('最多只能上传5张图片');
                return;
            } else {
                this.startUpload();
            }

        }
        window.commentGamePic = 0;
        function uploadSuccess5(file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {

                    var imgSrc = genImgDomain(jsonData.result[0], DOMAIN);

                    if (commentGamePic > 5) {

                    } else {
                        $("#menu_pic5").css("display", "none");
                        var img = $(' <span style="padding-left:15px;"  id="commentGamePic' + commentGamePic + '" onclick="deletePic(this)"><img src="' + imgSrc + '" name="commentGamePic" width="115" height="209"/></span>');
                        $("#upload5").before(img);
                        $("#loading5").css("display", "none");
                    }
                    commentGamePic++;
                }
            } catch (ex) {
                this.debug(ex);
            }
        }

        function uploadComplete5(file) {
            try {
                if (this.getStats().files_queued <= 0) {
                    $('#loading5').css('display', 'none');
                }
            } catch (ex) {
                this.debug(ex);
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
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gamedb/update" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="gamedbid" value="${gameDb.gameDbId}"/>
                            <input type="hidden" name="searchname" value="${searchname}"/>
                            <input type="hidden" name="pageStartIndex" value="${pageStartIndex}"/>
                            <input type="hidden" name="validstatus" value="${validstatus}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏图标 ：
                        </td>
                        <td height="1">
                            <img id="menu_pic" src="${gameDb.gameIcon}" height="120px" width="120px"/>
                            <input id="input_menu_pic" type="hidden" name="gameicon" value="${gameDb.gameIcon}">
                            <span style="color:red">*必填项</span>
                            <input type="file" name="Filedata" id="Filedata" onchange="preImg(this.id);"/>


                            <!--弹出层时背景层DIV-->
                            <div id="fade" class="black_overlay"
                                 style="display: none;position: absolute;top: 0%;left: 0%;width: 100%;height: 100%;background-color: black;z-index:1001;-moz-opacity: 0.8;opacity:.80;filter: alpha(opacity=80);"></div>
                            <div id="MyDiv" class="white_content"
                                 style="display: none;position: absolute;top: 30%;left: 25%;width: 45%;background-color: white;z-index:1002;overflow: auto;">
                                <div id="element_id_min_div" align="center"
                                     style="float:left;width:180px;height:300px;background-color:#f2f2f2;">
                                    <div style="width: 150px;height: 150px; overflow: hidden;">
                                        <img id="element_id_min" src="" style="display: block;" class="jcrop-preview"
                                             alt="Preview"/>
                                    </div>
                                    <br>
                                    <input type="hidden" name="x" id="x"/>
                                    <input type="hidden" name="y" id="y"/>
                                    <input type="hidden" name="width" id="width"/>
                                    <input type="hidden" name="height" id="height"/>
                                    <input type="button" onclick="javascript:imageCrop();" value="保存"/>&nbsp;&nbsp;
                                    <input type="button" value="取消" onclick="javascript:CloseDiv('MyDiv','fade')"/>
                                </div>
                                <div id="element_id_div" align="center" style="float:left;background-color:#f2f2f2;">
                                    <img id="element_id" src="" width="300" class="jcrop-preview" alt="Preview"/>
                                    <br>
                                    图片宽度：<input id="crop_w" style="width:50px"/> &nbsp;&nbsp;&nbsp;图片高度：<input
                                        id="crop_h" style="width:50px"/>
                                </div>
                            </div>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            点评背景图 ：
                        </td>
                        <td height="1">
                            <img id="menu_pic4" src="${gameDb.backpic}" class="img_pic" height="120px"
                                 width="120px"/>
                            <a href="javascript:void(0)" id="upload4">
                                <span id="upload_button4" class="upload_button">上传</span>
                            </a>
                            <span id="loading4" style="display:none" class="loading">
                                <img src="/static/images/loading.gif"/>
                            </span>
                            <input id="input_menu_pic4" type="hidden" name="backpic"
                                   value="${gameDb.backpic}">
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏名称 ：
                        </td>
                        <td height="1">
                            <input type="text" name="gamename" size="32" id="gamename" value="${gameDb.gameName}"/>
                            <span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            别名 ：
                        </td>
                        <td height="1">
                            <input type="text" name="anothername" size="32" id="anothername"
                                   value="${gameDb.anotherName}"/>多个别名请用","隔开<span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            英文名 ：
                        </td>
                        <td height="1">
                            <input type="text" name="englishName" size="32" id="englishName"
                                   value="${gameDb.englishName}" maxlength="200"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏大小 ：
                        </td>
                        <td height="1">
                            <input type="text" name="gamesize" size="32" id="gamesize" value="${gameDb.gameSize}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            一句话描述（游戏库） ：
                        </td>
                        <td height="1">
                            <input type="text" name="gamedesc" size="50" id="gamedesc" value="${gameDb.gameDesc}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台 ：
                        </td>
                        <td height="1">
                            <c:if test="${gamePlatformType==1}">
                                手机游戏: <input type="hidden" name="gameplatformtype" value="1"/>
                                <c:forEach var="mplat" items="${mPlatforms}">
                                    <c:if test="${mplat.code!=2}">
                                        <input type="checkbox" value="${mplat.code}" name="mplatform"
                                                <c:forEach items="${gameDb.platformMap['1']}" var="checkPlatform">
                                                    <c:if test="${checkPlatform.code == mplat.code}">checked="checked"</c:if>
                                                </c:forEach>/>${mplat.desc}
                                    </c:if>
                                </c:forEach><br/>
                            </c:if>
                            <c:if test="${gamePlatformType!=1}">
                                电脑游戏： <input type="hidden" name="gameplatformtype" value="0"/>
                                <c:forEach var="pcplat" items="${pcPlatforms}">
                                    <input type="checkbox" value="${pcplat.code}" name="pcplatform"
                                           onchange="showPcConfigTr()"
                                            <c:forEach items="${gameDb.platformMap['2']}" var="checkPlatform">
                                                <c:if test="${checkPlatform.code == pcplat.code}">checked="checked"</c:if>
                                            </c:forEach>/>${pcplat.desc}
                                </c:forEach><br/>
                                掌机游戏：
                                <c:forEach var="pspplat" items="${pspPlatforms}">
                                    <input type="checkbox" value="${pspplat.code}" name="pspplatform"
                                            <c:forEach items="${gameDb.platformMap['3']}" var="checkPlatform">
                                                <c:if test="${checkPlatform.code == pspplat.code}">checked="checked"</c:if>
                                            </c:forEach>/>${pspplat.desc}
                                </c:forEach><br/>
                                电视游戏：
                                <c:forEach var="tvplat" items="${tvPlatforms}">
                                    <input type="checkbox" value="${tvplat.code}" name="tvplatform"
                                            <c:forEach items="${gameDb.platformMap['4']}" var="checkPlatform">
                                                <c:if test="${checkPlatform.code == tvplat.code}">checked="checked"</c:if>
                                            </c:forEach>/>${tvplat.desc}
                                </c:forEach>
                            </c:if>
                            <span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <c:if test="${gamePlatformType==1}">
                        <tr>
                            <td height="1" class="default_line_td">
                                iOS下载 ：
                            </td>
                            <td height="1">
                                <input type="text" name="iosdownload" id="iosdownload" value="${gameDb.iosDownload}"/>
                            </td>
                            <td height="1" class=></td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                Android下载 ：
                            </td>
                            <td height="1">
                                <input type="text" name="androiddownload" id="androiddownload"
                                       value="${gameDb.androidDownload}"/>
                            </td>
                            <td height="1" class=></td>
                        </tr>
                    </c:if>
                    <c:if test="${gamePlatformType!=1}">
                        <tr>
                            <td height="1" class="default_line_td">
                                最低配置 ：
                            </td>
                            <td height="1">
                                <textarea name="pcconfigurationinfo1" id="pcconfigurationinfo1" cols="50"
                                          rows="8">${pcConfigurationInfo1}</textarea>
                            </td>
                            <td height="1" class=></td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                推荐配置 ：
                            </td>
                            <td height="1">

                                <textarea name="pcconfigurationinfo2" id="pcconfigurationinfo2" cols="50"
                                          rows="8">${pcConfigurationInfo2}</textarea>
                            </td>
                            <td height="1" class=></td>
                        </tr>
                    </c:if>


                    <tr id="pc_config_3" <c:if test="${gamePlatformType==1}">class="hide"</c:if>>
                        <td height="1" class="default_line_td">
                            PC下载地址 ：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="pc_download" id="pc_download"
                                   value="${gameDb.pcDownload}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr id="ps4Download_config" <c:if test="${gamePlatformType==1}">class="hide"</c:if>>
                        <td height="1" class="default_line_td">
                            PS4下载地址 ：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="ps4Download" id="ps4Download"
                                   value="${gameDb.ps4Download}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr id="xboxoneDownload_config" <c:if test="${gamePlatformType==1}">class="hide"</c:if>>
                        <td height="1" class="default_line_td">
                            Xboxone下载地址 ：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="xboxoneDownload" id="xboxoneDownload"
                                   value="${gameDb.xboxoneDownload}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            上市时间 ：
                        </td>
                        <td height="1">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true, dateFmt:'yyyy-MM-dd'})"
                                   readonly="readonly" name="publictime" id="gamepublictime"
                                   value="<fmt:formatDate value='${gameDb.gamePublicTime}' pattern='yyyy-MM-dd' type='both'/>"/>
                            <span style="color:red">*必填项</span>
                            <br/>
                            开启提醒：<input type="radio" name="publichtips" value="true"
                                        <c:if test="${gameDb.publicTips}">checked="checked"</c:if>/>是&nbsp;
                            <input type="radio" name="publichtips" value="false"/>否
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <%--<c:if test="${gamePlatformType != 1}">--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            官网地址 ：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="officialwebsite" id="officialwebsite"
                                   value="${gameDb.officialWebsite}"/><span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <%--</c:if>--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            联网：
                        </td>
                        <td height="1">
                            <c:forEach var="net" items="${netTypes}">
                                <input type="radio" name="nettype" value="${net.code}"
                                       <c:if test="${gameDb.gameNetType.code == net.code}">checked="checked"</c:if>/>${net.name}&nbsp;
                            </c:forEach>
                            <%--<span style="color:red">*必填项</span>--%>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            语言：
                        </td>
                        <td height="1">
                            <c:forEach var="language" items="${languageTypes}">
                                <input type="checkbox" name="languagetype" value="${language.code}"
                                        <c:forEach items="${gameDb.languageTypeSet}" var="checkLanguage">
                                            <c:if test="${checkLanguage.code == language.code}">checked="checked"</c:if>
                                        </c:forEach>/>${language.name}&nbsp;
                            </c:forEach>
                            <%--<span style="color:red">*必填项</span>--%>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            类型 ：
                        </td>
                        <td height="1">
                            <c:forEach items="${categoryTypes}" var="category" varStatus="c">
                                <input type="checkbox" value="${category.code}" name="category"
                                        <c:forEach items="${gameDb.categoryTypeSet}" var="checkCategory">
                                            <c:if test="${checkCategory.code == category.code}">checked="checked"</c:if>
                                        </c:forEach>/>${category.value}&nbsp;
                                <c:if test="${c.index%8==0&&c.index!=0}"><br/></c:if>
                            </c:forEach>
                            <span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            题材：
                        </td>
                        <td height="1">
                            <c:forEach var="theme" items="${themeTypes}" varStatus="c">
                                <input type="checkbox" value="${theme.code}" name="themetype"
                                        <c:forEach items="${gameDb.themeTypeSet}" var="checkTheme">
                                            <c:if test="${checkTheme.code == theme.code}">checked="checked"</c:if>
                                        </c:forEach>/>${theme.name}&nbsp;
                                <c:if test="${c.index%8==0&&c.index!=0}"><br/></c:if>
                            </c:forEach>
                            <%--<span style="color:red">*必填项</span>--%>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            开发商 ：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="developer" id="developer"
                                   value="${gameDb.gameDeveloper}"/>
                            <span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            发行商 ：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="publishers" id="publishers"
                                   value="${gameDb.gamePublishers}"/>
                            <%--<span style="color:red">*必填项</span>--%>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏介绍 ：
                        </td>
                        <td height="1">
                            <textarea name="gameprofile" id="gameprofile" cols="50"
                                      rows="8">${gameDb.gameProfile}</textarea>
                            <%--<span style="color:red">*必填项</span>--%>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            视频 ：
                        </td>
                        <td height="1">
                            <input name="gamevideo" value="${gameDb.gameVideo}" size="100"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏截图 ：
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${not empty gamePicList}">
                                    <c:forEach items="${gamePicList}" var="pic" varStatus="picindex">
                                        <span style="padding-left:15px;" id="gamepic${picindex.index+1}"
                                              onclick="deletePic(this);">
                                            <img src="${pic}" name="gamepicurl" width="120"/>
                                        </span>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <img id="menu_pic2" src="/static/images/default.jpg"/>
                                </c:otherwise>
                            </c:choose>
                            <a href="javascript:void(0)" id="upload">
                                <span id="upload_button2" class="upload_button">上传</span>
                            </a>
                            <span>*点击图片可删除</span>
                            <span id="loading2" style="display:none" class="loading">
                                <img src="/static/images/loading.gif"/>
                            </span>
                            <input id="input_menu_pic2" type="hidden" name="gamepics" value="">
                            <br/>
                            <select name="pictype">
                                <c:forEach items="${picTypes}" var="pct">
                                    <option value="${pct.code}"
                                            <c:if test="${gameDb.gamePicType.code == pct.code}">selected="selected"</c:if>>${pct.name}</option>
                                </c:forEach>
                            </select>
                            <span style="color:red">*横图：顺时针旋转，270度。竖图：不变</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            点评游戏宣传图 ：
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${not empty commentGamePicList}">
                                    <c:forEach items="${commentGamePicList}" var="pic" varStatus="picindex">
                                        <span style="padding-left:15px;" id="commentGamePic${picindex.index+1}"
                                              onclick="deletePic(this);">
                                            <img src="${pic}" name="commentGamePic" width="115" height="209"/>
                                        </span>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <img id="menu_pic5" src="/static/images/default.jpg"/>
                                </c:otherwise>
                            </c:choose>
                            <a href="javascript:void(0)" id="upload5">
                                <span id="upload_button5" class="upload_button5">上传</span>
                            </a>
                            <span>*点击图片可删除</span>
                            <span id="loading5" style="display:none" class="loading">
                                <img src="/static/images/loading.gif"/>
                            </span>
                            <input id="input_menu_pic5" type="hidden" name="commentGamePic" value="">
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            WIKI：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="wikiurl" value="${gameDb.wikiUrl}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            专区：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="cmsurl" value="${gameDb.cmsUrl}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏库评分：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="gamerate" id="gamerate" value="${gameDb.gameRate}"/>
                        </td>
                        <td height="1" class=></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            网页游戏地址：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="webpageDownload" value="${gameDb.webpageDownload}">
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否关卡游戏：
                        </td>
                        <td height="1">
                            <input type="radio" name="levelGame" value="false"
                                   <c:if test="${gameDb.levelGame==false}">checked</c:if>>否
                            <input type="radio" name="levelGame" value="true"
                                   <c:if test="${gameDb.levelGame==true}">checked</c:if>>是
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏版号：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="isbn" value="${gameDb.isbn}"
                                   placeholder="ISB号，如：123-4-1234-6789-0">
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            App store价格：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="appstorePrice" value="${gameDb.appstorePrice}">
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            宣传视频：
                        </td>
                        <td height="1">
                            <input size="100" type="text" name="video" value="${gameDb.video}">
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            需要VPN支持：
                        </td>
                        <td height="1">
                            <input type="radio" name="vpn" value="false"
                                   <c:if test="${gameDb.vpn==false}">checked</c:if>>否
                            <input type="radio" name="vpn" value="true" <c:if test="${gameDb.vpn==true}">checked</c:if>>是
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏标签 ：
                        </td>
                        <td height="1">
                            <div class="wrap">


                                <ul class="show-history">
                                    <c:forEach items="${gameTagList}" var="tag" varStatus="picindex">
                                        <li><span data-id="${tag.id}">${tag.tagName}</span><span class='close'>X</span>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <input type="text" id="inputword" size="50" autocomplete="off"/>
                                <input type="hidden" id="gameTag" name="gameTag" size="50" value="${gameDb.gameTag}"/>
                            </div>
                            <p class="show">
                            </p>
                            <span style="color:red">*必填项</span>&nbsp;&nbsp;<a target="_blank"
                                                                              href="/apiwiki/gametag/createpage">新增标签</a>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            点评评分：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="commentScore" value="${gameDb.commentScore}" readonly>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            点评评分人数：
                        </td>
                        <td height="1">
                            <input size="50" type="text" name="commentSum" value="${gameDb.commentSum}" readonly>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                </table>

                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="list_table_header_td">
                            <input type="checkbox" id="input_checkbox_wanba"/>玩霸相关信息
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0" id="table_wanba" style="display: none">
                    <tr>
                        <td class="list_table_header_td" colspan="3">摇一摇</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            摇一摇推荐理由1 ：
                        </td>
                        <td height="1">
                            <textarea name="reason" cols="50" rows="8">${gameDb.recommendReason}</textarea>
                            <span style="color:red">*用于摇一摇显示 20个字左右</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            摇一摇推荐理由2 ：
                        </td>
                        <td height="1">
                            <textarea name="reason2" cols="50" rows="8">${gameDb.recommendReason2}</textarea>
                            <span style="color:red">* 用于摇一摇显示 20个字左右</span>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            版本简介：
                        </td>
                        <td height="1">
                            <textarea name="versionprofile" rows="8" cols="50"
                                      id="versionprofile">${gameDb.versionProfile}</textarea>
                        </td>
                        <td height="1" class=></td>
                    </tr>
                    <tr>
                        <td class="list_table_header_td" colspan="3">游戏封面</td>
                    </tr>
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
                            <a href="javascript:void(0)" id="upload3">
                                <span id="upload_button3" class="upload_button">上传</span>
                            </a>
                            <span id="loading3" style="display:none" class="loading">
                                <img src="/static/images/loading.gif"/>
                            </span>
                            <input id="input_menu_pic3" type="hidden" name="coverPicUrl"
                                   value="${gameDb.gameDBCover.coverPicUrl}">
                        </td>
                        <td height="1" class=></td>
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
                            评分：<select name="value1" id="value1">
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
                        </select><span style="color:red">*必填项</span>
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
                            评分：<select name="value2" id="value2">
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
                        </select><span style="color:red">*必填项</span>
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
                            评分：<select name="value3" id="value3">
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
                        </select><span style="color:red">*必填项</span>
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
                            评分：<select name="value4" id="value4">
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
                        </select><span style="color:red">*必填项</span>
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
                            评分：<select name="value5" id="value5">
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
                        </select><span style="color:red">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            点赞数：
                        </td>
                        <td height="1">
                            <input type="text" id="coverAgreeNum" name="coverAgreeNum" size="80"
                                   value="${gameDb.gameDBCover.coverAgreeNum}"/>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            iOS游戏下载开关：
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
                    <tr>
                        <td class="list_table_header_td" colspan="3">新游开测与游戏海报-ios</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            选择开测时间显示方式:
                        </td>
                        <td height="1">
                            <select name="posterShowTypeIos" id="posterShowTypeIos">
                                <option value="1"
                                        <c:if test="${gameDb.gameDBCover.posterShowTypeIos == '1'}">selected="selected"</c:if>>
                                    精确时间
                                </option>
                                <option value="2"
                                        <c:if test="${gameDb.gameDBCover.posterShowTypeIos == '2'}">selected="selected"</c:if>>
                                    自定义显示的文字
                                </option>
                            </select> *必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr id="posterShowContentTrIos">
                        <td height="1" class="default_line_td">
                            自定义显示文字内容:
                        </td>
                        <td height="1">
                            <input type="text" name="posterShowContentIos" id="posterShowContentIos" size="50"
                                   maxlength="50" value="${gameDb.gameDBCover.posterShowContentIos}"/> *必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            上市时间:
                        </td>
                        <td height="1">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="posterGamePublicTimeIos" id="posterGamePublicTimeIos"
                                   value="${gameDb.gameDBCover.posterGamePublicTimeIos}"/>*必填项
                        </td>
                    </tr>
                    <tr>
                        <td class="list_table_header_td" colspan="3">新游开测与游戏海报-Android</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            选择开测时间显示方式:
                        </td>
                        <td height="1">
                            <select name="posterShowTypeAndroid" id="posterShowTypeAndroid">
                                <option value="1"
                                        <c:if test="${gameDb.gameDBCover.posterShowTypeAndroid == '1'}">selected="selected"</c:if>>
                                    精确时间
                                </option>
                                <option value="2"
                                        <c:if test="${gameDb.gameDBCover.posterShowTypeAndroid == '2'}">selected="selected"</c:if>>
                                    自定义显示的文字
                                </option>
                            </select> *必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr id="posterShowContentTrAndroid">
                        <td height="1" class="default_line_td">
                            自定义显示文字内容:
                        </td>
                        <td height="1">
                            <input type="text" name="posterShowContentAndroid" id="posterShowContentAndroid" size="50"
                                   maxlength="50" value="${gameDb.gameDBCover.posterShowContentAndroid}"/> *必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            上市时间:
                        </td>
                        <td height="1">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="posterGamePublicTimeAndroid"
                                   id="posterGamePublicTimeAndroid"
                                   value="${gameDb.gameDBCover.posterGamePublicTimeAndroid}"/>*必填项
                        </td>
                    </tr>
                </table>

                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="list_table_header_td">
                            <input type="checkbox" id="input_checkbox_shouye"/>PC主站首页“大家对游戏的看法”
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0" id="table_shouye" style="display: none">
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            看法1:
                        </td>
                        <td height="1">
                            <input type="text" name="comment1" value="${gameDb.commentAndAgree.comment1}"/> 点赞数:<input
                                type="text" name="agree1" value="${gameDb.commentAndAgree.agree1}"
                                onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            看法2 :
                        </td>
                        <td height="1">
                            <input type="text" name="comment2" value="${gameDb.commentAndAgree.comment2}"/> 点赞数:<input
                                type="text" name="agree2" value="${gameDb.commentAndAgree.agree2}"
                                onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            看法3:
                        </td>
                        <td height="1">
                            <input type="text" name="comment3" value="${gameDb.commentAndAgree.comment3}"/> 点赞数:<input
                                type="text" name="agree3" value="${gameDb.commentAndAgree.agree3}"
                                onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                </table>

                <c:if test="${gamePlatformType == 1}">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td class="list_table_header_td">
                                <input type="checkbox" id="input_checkbox_chanel_download"/>渠道下载信息
                            </td>
                        </tr>
                    </table>
                    <table width="100%" border="0" cellspacing="1" cellpadding="0" id="table_chanel_download"
                           style="display: none">
                        <c:choose>
                            <c:when test="${gameDb.channelInfoSet.size() > 0}">
                                <c:forEach items="${gameDb.channelInfoSet}" var="channelInfo" varStatus="st">
                                    <tr>
                                        <td colspan="3">
                                            <table id="table_qudao${st.index + 1}">
                                                <tr>
                                                    <td height="1" class="default_line_td">
                                                        平台：
                                                    </td>
                                                    <td height="1">
                                                        <select id="platform${st.index + 1}">
                                                            <option value="">请选择</option>
                                                            <c:forEach var="mplat" items="${mPlatforms}">
                                                                <option value="${mplat.code}"
                                                                        <c:if test="${''+ mplat.code == channelInfo.device}">selected="selected"</c:if>>${mplat.desc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                    <td height="1" class=>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td height="1" class="default_line_td">
                                                        渠道：
                                                    </td>
                                                    <td height="1">
                                                        <select id="channel${st.index + 1}">
                                                            <option value="">请选择</option>
                                                            <c:forEach items="${channelList}" var="channel">
                                                                <option value="${channel.channelId}"
                                                                        id="channel_${channel.channelId}"
                                                                        <c:if test="${'' + channel.channelId == channelInfo.channel_id}">selected="selected"</c:if>>${channel.channelName}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                    <td height="1" class=>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td height="1" class="default_line_td">
                                                        下载地址：
                                                    </td>
                                                    <td height="1">
                                                        <input type="text" id="download${st.index + 1}"
                                                               value="${channelInfo.channelDownloadInfo.download}"/>
                                                    </td>
                                                    <td height="1" class=>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td height="1" class="default_line_td">
                                                        游戏版本：
                                                    </td>
                                                    <td height="1">
                                                        <input type="text" id="version${st.index + 1}"
                                                               value="${channelInfo.channelDownloadInfo.gameversion}"/>
                                                    </td>
                                                    <td height="1" class=>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td height="1" class="default_line_td">
                                                        游戏大小：
                                                    </td>
                                                    <td height="1">
                                                        <input type="text" id="size${st.index + 1}"
                                                               value="${channelInfo.channelDownloadInfo.gamesize}"/>
                                                    </td>
                                                    <td height="1" class=>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td height="1" class="default_line_td">
                                                        系统版本：
                                                    </td>
                                                    <td height="1">
                                                        <input type="text" id="system${st.index + 1}"
                                                               value="${channelInfo.channelDownloadInfo.systemversion}"/>
                                                    </td>
                                                    <td height="1" class=>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="3">
                                                        <input type="button" id="remove${st.index + 1}" value="删除"
                                                               onclick="removeChannelDownload(this)"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="3" height="1" class="default_line_td"></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3">
                                        <table id="table_qudao1">
                                            <tr>
                                                <td height="1" class="default_line_td">
                                                    平台：
                                                </td>
                                                <td height="1">
                                                    <select id="platform1">
                                                        <option value="">请选择</option>
                                                        <c:forEach var="mplat" items="${mPlatforms}">
                                                            <option value="${mplat.code}">${mplat.desc}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="1" class="default_line_td">
                                                    渠道：
                                                </td>
                                                <td height="1">
                                                    <select id="channel1">
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
                                                    下载地址：
                                                </td>
                                                <td height="1">
                                                    <input type="text" id="download1" value=""/>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="1" class="default_line_td">
                                                    游戏版本：
                                                </td>
                                                <td height="1">
                                                    <input type="text" id="version1" value=""/>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="1" class="default_line_td">
                                                    游戏大小：
                                                </td>
                                                <td height="1">
                                                    <input type="text" id="size1" value=""/>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="1" class="default_line_td">
                                                    系统版本：
                                                </td>
                                                <td height="1">
                                                    <input type="text" id="system1" value=""/>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <input type="button" id="remove1" value="删除"
                                                           onclick="removeChannelDownload(this)"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" height="1" class="default_line_td"></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        <tr id="tr_before">
                            <td colspan="3">
                                <input type="button" value="增加下载信息" id="insertline"
                                       data-idx="${gameDb.channelInfoSet.size() +1}"/>
                            </td>
                        </tr>
                        <input type="hidden" name="channeldownloadinfo" id="input_download_info"/>
                    </table>
                </c:if>

                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="list_table_header_td">
                            <input type="checkbox" id="input_checkbox_shangwu"/>商务信息
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0" id="table_shangwu" style="display: none">
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
                            <input type="radio" name="teamnum" value="1"
                                   <c:if test="${gameDb.teamNum == 1}">checked="checked"</c:if>/>1-10人&nbsp;&nbsp;
                            <input type="radio" name="teamnum" value="2"
                                   <c:if test="${gameDb.teamNum == 2}">checked="checked"</c:if>/>11-20人&nbsp;&nbsp;
                            <input type="radio" name="teamnum" value="3"
                                   <c:if test="${gameDb.teamNum == 3}">checked="checked"</c:if>/>21-50人&nbsp;&nbsp;
                            <input type="radio" name="teamnum" value="4"
                                   <c:if test="${gameDb.teamNum == 4}">checked="checked"</c:if>/>51-100人&nbsp;&nbsp;
                            <br/>
                            <input type="radio" name="teamnum" value="5"
                                   <c:if test="${gameDb.teamNum == 5}">checked="checked"</c:if>/>101-200人&nbsp;&nbsp;
                            <input type="radio" name="teamnum" value="6"
                                   <c:if test="${gameDb.teamNum == 6}">checked="checked"</c:if>/>201人以上&nbsp;&nbsp;
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            所在城市：
                        </td>
                        <td height="1">
                            <input type="radio" name="city" value="北京"
                                   <c:if test="${gameDb.city == '北京'}">checked="checked"</c:if>/>北京&nbsp;&nbsp;
                            <input type="radio" name="city" value="广州"
                                   <c:if test="${gameDb.city == '广州'}">checked="checked"</c:if>/>广州&nbsp;&nbsp;
                            <input type="radio" name="city" value="上海"
                                   <c:if test="${gameDb.city == '上海'}">checked="checked"</c:if>/>上海&nbsp;&nbsp;
                            <input type="radio" name="city" value="福州"
                                   <c:if test="${gameDb.city == '福州'}">checked="checked"</c:if>/>福州&nbsp;&nbsp;
                            <br/>
                            <input type="radio" name="city" value="杭州"
                                   <c:if test="${gameDb.city == '杭州'}">checked="checked"</c:if>/>杭州&nbsp;&nbsp;
                            <input type="radio" name="city" value="成都"
                                   <c:if test="${gameDb.city == '成都'}">checked="checked"</c:if>/>成都&nbsp;&nbsp;
                            <input type="radio" name="city" value="深圳"
                                   <c:if test="${gameDb.city == '深圳'}">checked="checked"</c:if>/>深圳&nbsp;&nbsp;
                            <input type="radio" name="city" value="大连"
                                   <c:if test="${gameDb.city == '大连'}">checked="checked"</c:if>/>大连&nbsp;&nbsp;
                            <br/>
                            <input type="radio" name="city" value="其他"
                                   <c:if test="${gameDb.city == '其他'}">checked="checked"</c:if>/>其它
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--预计上市时间：--%>
                    <%--</td>--%>
                    <%--<td height="1">--%>
                    <%--<input type="text" class="Wdate"--%>
                    <%--onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"--%>
                    <%--name="publictime" id="publictime"--%>
                    <%--value="<fmt:formatDate value='${gameDb.publicTime}' pattern='yyyy-MM-dd' type='both'/>"/>--%>
                    <%--</td>--%>
                    <%--<td height="1" class=>--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            融资方式：
                        </td>
                        <td height="1">
                            <input type="checkbox" name="financing-checkbox" value="1"
                                   <c:if test="${gameDb.financing == 1 || gameDb.financing == 3}">checked="checked"</c:if>/>独代
                            <input type="checkbox" name="financing-checkbox" value="2"
                                   <c:if test="${gameDb.financing == 2 || gameDb.financing == 3}">checked="checked"</c:if>/>分成
                            <input type="hidden" name="financing" value="" id="financing"/>
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
                                <option value="1" <c:if test="${gameDb.displayIcon == 1}">selected="selected"</c:if>>
                                    NEW
                                </option>
                                <option value="2" <c:if test="${gameDb.displayIcon == 2}">selected="selected"</c:if>>
                                    HOT
                                </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            发行范围：
                        </td>
                        <td height="1">
                            <input type="checkbox" value="1" name="area-checkbox"/>国内&nbsp;&nbsp;
                            <input type="checkbox" value="2" name="area-checkbox"/>北美&nbsp;&nbsp;
                            <input type="checkbox" value="4" name="area-checkbox"/>南美&nbsp;&nbsp;
                            <input type="checkbox" value="8" name="area-checkbox"/>东南亚&nbsp;&nbsp;
                            <br/>
                            <input type="checkbox" value="16" name="area-checkbox"/>日韩&nbsp;&nbsp;
                            <input type="checkbox" value="32" name="area-checkbox"/>港台&nbsp;&nbsp;
                            <input type="checkbox" value="64" name="area-checkbox"/>西亚&nbsp;&nbsp;
                            <input type="checkbox" value="128" name="area-checkbox"/>非洲&nbsp;&nbsp;
                            <br/>
                            <input type="checkbox" value="256" name="area-checkbox"/>欧洲&nbsp;&nbsp;
                            <input type="checkbox" value="512" name="area-checkbox"/>其它&nbsp;&nbsp;
                            <input type="hidden" name="area" value="" id="area"/>
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
<script type="text/javascript">
    /**
     * 从 file 域获取 本地图片 url
     */

    function getFileUrl(sourceId) {
        var url;
        if (navigator.userAgent.indexOf("MSIE") >= 1) { // IE
            url = document.getElementById(sourceId).value;
        } else if (navigator.userAgent.indexOf("Firefox") > 0) { // Firefox
            url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
        } else if (navigator.userAgent.indexOf("Chrome") > 0) { // Chrome
            url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
        }
        return url;
    }


    var jcrop_api;

    function initJcrop() {
        $('#element_id').Jcrop({
                onSelect: showCoords,
                onChange: showCoords,
                allowSelect: true,
                allowResize: true,
                maxSize: 150,
                minSize: 150,
                aspectRatio: 1
            }, function () {
                jcrop_api = this;
                jcrop_api.animateTo([0, 0, 150, 150]);
            }
        );

    }

    function showCoords(c) {
        var x = c.x;
        var y = c.y;
        var w = c.w;
        var h = c.h;

        $('#x').attr("value", x);
        $('#y').attr("value", y);
        $('#width').attr("value", w);
        $('#height').attr("value", h);

        $('#crop_w').attr("value", w);
        $('#crop_h').attr("value", h);

        if (parseInt(c.w) > 0) {
            var rx = 150 / w;
            var ry = 150 / h;
            var bounds = jcrop_api.getBounds();
            boundx = bounds[0];
            boundy = bounds[1];
            $('#element_id_min').css({
                width: Math.round(rx * boundx) + 'px',
                height: Math.round(ry * boundy) + 'px',
                marginLeft: '-' + Math.round(rx * x) + 'px',
                marginTop: '-' + Math.round(ry * y) + 'px'
            });
        }
    }

    /**
     * 将本地图片 显示到浏览器上
     */
    function preImg(sourceId) {
        var url = getFileUrl(sourceId);
        $('#element_id_min').attr("src", url);
        $('#element_id').attr("src", url);

        initJcrop();
        ShowDiv('MyDiv', 'fade')

    }

    //弹出隐藏层
    function ShowDiv(show_div, bg_div) {
        document.getElementById(show_div).style.display = 'block';
        document.getElementById(bg_div).style.display = 'block';
        var bgdiv = document.getElementById(bg_div);
        bgdiv.style.width = document.body.scrollWidth;
        // bgdiv.style.height = $(document).height();
        $("#" + bg_div).height($(document).height());
    };

    //关闭弹出层
    function CloseDiv(show_div, bg_div) {
        document.getElementById(show_div).style.display = 'none';
        document.getElementById(bg_div).style.display = 'none';

        //$('#element_id').replaceWith('<img id="element_id" src=""  width="300px" class="jcrop-preview" alt="Preview" />');
        $('#Filedata').remove();
        $('#input_menu_pic').after('<input type="file" name="Filedata" id="Filedata" onchange ="preImg(this.id);" />')
        jcrop_api.destroy()
    };

    function imageCrop() {
        $.ajaxFileUpload({
            url: '/json/upload/crop',//需要链接到服务器地址
            type: 'post',
            fileElementId: "Filedata",//文件选择框的id属性
            dataType: 'JSON',   //json
            data: {
                x: $('#x').val(),
                y: $('#y').val(),
                width: $('#width').val(),
                height: $('#height').val(),
                w: $('#element_id').attr('width')
            },
            success: function (data) {
                CloseDiv('MyDiv', 'fade');
                var status = data.status_code
                if (status == 1) {
                    $('#menu_pic').attr('src', data.result);
                    $('#menu_pic').attr('width', $('#width').val() + 'px');
                    $('#menu_pic').attr('height', $('#height').val() + 'px');
                    $('#input_menu_pic').attr('value', data.result);

                } else {
                    alert(data.msg)
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert('上传失败！');
            }
        });
    }


    $(function () {
        var tagidArr = [];
        var _str = $("#gameTag").val();
        var strArry = _str.split(',');
        for (var i = 0; i < strArry.length; i++) {
            tagidArr.push(strArry[i]);
        }
        console.log(tagidArr)
        $("#inputword").keyup(function () {
            var that = this;
            var hei = $(this).outerHeight();
            var content = $("#inputword").val();
            if ($.trim(content) == "") {
                return;
            }
            $.ajax({
                type: "GET",
                url: "/apiwiki/gametag/json?tagName=" + content,
                dataType: 'JSON',   //json
                success: function (data) {
                    $('.append-div').remove();
                    var html = "<div class='append-div' style='position:absolute; margin-top:" + hei + "px;background-color: #fafafa; border-color: #ddd; color: #444; width:400px; overflow:hidden; max-height:300px;padding: 2px;border-width: 1px;border-style: solid; border-radius5px;'>";
                    if (data.total > 0) {
                        for (var i = 0; i < data.rows.length; i++) {
                            html += "<li data-id=" + data.rows[i].id + ">" + data.rows[i].tagName + "</li>";
                        }
                    }
                    html += '<div>'
                    $(html).insertAfter(that);
                }
            });
        });

        $(document).on('click', '.append-div li', function () {
            var text = $(this).text();
            var id = $(this).attr("data-id");
            if ($.inArray(id, tagidArr) == -1) {
                tagidArr.push(id);
                $("#gameTag").val(tagidArr.join(","));
                $("<li><span data-id=" + id + ">" + text + "</span><span class='close'>X</span></li>").appendTo('.show-history')
            }

        })

        $(document).on('click', '.show-history li .close', function () {
            tagidArr.splice(jQuery.inArray($(this).parent('li').find("span:first").attr("data-id"), tagidArr), 1);
            $("#gameTag").val(tagidArr.join(","));
            $(this).parent('li').remove();
        });


        $(document).mouseup(function (e) {
            var _con = $('.append-div');
            if (!_con.is(e.target) && _con.has(e.target).length === 0) {
                _con.remove();
            }
        });

    });


</script>
</html>