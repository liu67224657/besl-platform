define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../../third/swfupload/swfupload');
    var handler = require('../../third/swfupload/gamedbhandler');
    var joymealert = require('../common/joymealert');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');


    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    window.imgLocaL = 0;
    var swfu;
    var swfu1;
    $(document).ready(function() {
        $("#detailinfo").css("display", "none");
        $("#business").css("display", "none");
        $("#detailinfo").keyup(function(event) {
            if (event.keyCode == 13) {
                $("#detailinfonext").click();
            }
        });
        $("#basicinfo").keyup(function(event) {
            if (event.keyCode == 13) {
                $("#basicinfonext").click();

            }
        });
        $("#business").keyup(function(event) {
            if (event.keyCode == 13) {
                $("#businessinfo").click();
            }
        });
        $("#download1").bind("focus", tFocus);
        $("#download1").bind("blur", tBlur);
        $("#gameversion1").bind("focus", tFocus);
        $("#gameversion1").bind("blur", tBlur);
        $("#gamesize1").bind("focus", tFocus);
        $("#gamesize1").bind("blur", tBlur);
        $("#systemversion1").bind("focus", tFocus);
        $("#systemversion1").bind("blur", tBlur);
        $("#modifyinfo").click(function() {
            alertOption.text = '本功能暂未推出，敬请期待。';
            alertOption.title = '敬请期待';
            joymealert.alert(alertOption);
        });

        $("#managerproduct").click(function() {
            alertOption.text = '本功能暂未推出，敬请期待。';
            alertOption.title = '敬请期待';
            joymealert.alert(alertOption);
        });

        $("#createmygame").click(function() {
            window.location = '/developers/home/creategamedb';
        });
        var i = 2;
        $("#createUploadInfo").click(function() {
            var divTest = $("#uploadLink1");

            var newDiv = divTest.clone(true);
            newDiv.attr("name", "downloadinfo");
            newDiv.attr("id", "uploadLink" + i);
            newDiv.find("#download1").attr("id", "download" + i);
            newDiv.find("#download" + i).attr("value", "请输入下载地址");
            $("#download" + i).bind("focus", tFocus);
            $("#download" + i).bind("focus", tBlur);
            newDiv.find("#channel1").attr("id", "channel" + i);

            newDiv.find("#device1").attr("id", "device" + i);
            newDiv.find("#gameversion1").attr("id", "gameversion" + i);
            newDiv.find("#gameversion" + i).attr("value", "请输入游戏版本");
            $("#gameversion" + i).bind("focus", tFocus);
            $("#gameversion" + i).bind("focus", tBlur);
            newDiv.find("#gamesize1").attr("id", "gamesize" + i);
            newDiv.find("#gamesize" + i).attr("value", "请输入游戏大小");
            $("#gamesize" + i).bind("focus", tFocus);
            $("#gamesize" + i).bind("focus", tBlur);
            newDiv.find("#systemversion1").attr("id", "systemversion" + i);
            newDiv.find("#systemversion" + i).attr("value", "请输入支持的系统版本");
            $("#systemversion" + i).bind("focus", tFocus);
            $("#systemversion" + i).bind("focus", tBlur);
            $("#createUploadInfo").before(newDiv);
            i++;
        });

        $("#removeUploadInfo").click(function() {
            var downloadinfo = $("div[name='downloadinfo']").size();
            if (downloadinfo <= 1) {
                alertOption.text = '最少需要一个下载信息。';
                joymealert.alert(alertOption);
                return;
            }
            $("div[name='downloadinfo']:last").remove();
        })

        $("input[name=radio]").click(function() {
            var city = $(this).val();
            if (city == "其他") {
                $("#city").val("");
                $("#city").css("display", "inline");
            } else {
                $("#city").val(city);
                $("#city").css("display", "none");
            }
        });

        $("#basicinfonext").click(function() {
            var gamename = $("#gamename").val().replace(/^\s+|\s+$/g, '');
            var anothername = $("#anothername").val().replace(/^\s+|\s+$/g, '');
            if (gamename == '') {
                alertOption.text = '请填写游戏名称。';
                joymealert.alert(alertOption);
                return;
            }
            var bool = ajaxverify.verifyNickNameRegex(gamename);
            if (!bool) {
                alertOption.text = "游戏名称含有不适当内容。";
                joymealert.alert(alertOption);
                return;
            }
            if (anothername != '') {
                var bool = ajaxverify.verifyNickNameRegex(anothername);
                if (!bool) {
                    alertOption.text = "其他名称含有不适当内容。";
                    joymealert.alert(alertOption);
                    return;
                }
            }
            $("#anothername").val(anothername);
            var bool = true;
            $.ajax({type: "POST",
                        url: "/developers/home/getgamedbbyname",
                        async:false,
                        data: {name: gamename},
                        success: function (req) {
                            var result = eval('(' + req + ')');
                            bool = callback.getgamedbbyname(result);
                        }
                    });
            if (!bool) {
                return;
            }
            $("#gamename").val(gamename);
            if ($("#picurl").val() == '') {
                alertOption.text = '请上传游戏图标。';
                joymealert.alert(alertOption);
                return;
            }
            if ($("input[name='category']:checked").length < 1) {
                alertOption.text = '请至少选择一个游戏分类。';
                joymealert.alert(alertOption);
                return;
            }
            if ($("input[name='category']:checked").length > 5) {
                alertOption.text = '最多只能选择5个游戏分类。';
                joymealert.alert(alertOption);
                return;
            }
            if ($("input[name='gamesystem']:checked").length < 1) {
                alertOption.text = '请选择游戏设备。';
                joymealert.alert(alertOption);
                return;
            }
            var developer = $("#developer").val().replace(/^\s+|\s+$/g, '');
            if ($("#developer").val().replace(/^\s+|\s+$/g, '') == '') {
                alertOption.text = '请填写开发商信息。';
                joymealert.alert(alertOption);
                return;
            }

            var developerbool = ajaxverify.verifyNickNameRegex(developer);
            if (!developerbool) {
                alertOption.text = "开发商信息含有不适当内容。";
                joymealert.alert(alertOption);
                return;
            }
            var publishers = $("#publishers").val().replace(/^\s+|\s+$/g, '');
            if (publishers != '') {
                var publishersbool = ajaxverify.verifyNickNameRegex(publishers);
                if (!publishersbool) {
                    alertOption.text = "运营商信息含有不适当内容。";
                    joymealert.alert(alertOption);
                    return;
                }
            }

            $("#process").removeClass("process-1");
            $("#process").addClass("process-2");
            $("#basicinfo").css("display", "none");
            $("#detailinfo").css("display", "block");
        });
        $("#detailinfonext").click(function() {
            var gamepic = '';
            $("img[name='gamepicurl']").each(function() {
                var src = $(this).attr("src");
                gamepic = gamepic + (src + "@");
            });
            gamepic = gamepic.substring(0, gamepic.length - 1);

            $("#gamepicurl").val(gamepic);


            if ($("#gameprofile").val().length > 3000) {
                alertOption.text = '游戏简介字数过多，请删减。';
                joymealert.alert(alertOption);
                return;
            }
            if ($("#versionprofile").val().length > 3000) {
                alertOption.text = '版本简介字数过多，请删减。';
                joymealert.alert(alertOption);
                return;
            }


            var result = new Array();
            var bool = true;
            var i = Number(1);
            $("div[name='downloadinfo']").each(function() {

                var id = this.id;
                id = id.substring(10)
                var device = $("#device" + id).val();
                var channel = $("#channel" + id).val();
                var download = $("#download" + id).val().replace(/^\s+|\s+$/g, '');
                var gameversion = $("#gameversion" + id).val();
                var gamesize = $("#gamesize" + id).val();
                var systemversion = $("#systemversion" + id).val();
                if (device == '0') {
                    alertOption.text = '请选择下载信息 ' + i + ' 里的游戏设备。';
                    joymealert.alert(alertOption);
                    bool = false;
                    return;
                }
                if (channel == '0') {
                    alertOption.text = '请选择下载信息 ' + i + ' 里的渠道。';
                    joymealert.alert(alertOption);
                    bool = false;
                    return;
                }
                if (download == '' || download == '请输入下载地址') {
                    alertOption.text = "请填写下载信息" + i + "里的下载链接。";
                    joymealert.alert(alertOption);
                    bool = false;
                    return;
                }

                if (gamesize == '' || gamesize == '请输入游戏大小') {
                    alertOption.text = "请填写下载信息" + i + "里的游戏大小。";
                    joymealert.alert(alertOption);
                    bool = false;
                    return;
                }
                var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
                        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                        + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                        + "|" // 允许IP和DOMAIN（域名）
                        + "([0-9a-zA-Z_!~*'()-]+\.)*" // 域名- www.
                        + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\." // 二级域名
                        + "[a-zA-Z]{2,6})" // first level domain- .com or .museum
                        + "(:[0-9]{1,4})?" // 端口- :80
                        + "((/?)|" // a slash isn't required if there is no file name
                        + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
                var re = new RegExp(strRegex);
                //re.test()
                if (!re.test(download)) {
                    alertOption.text = "下载地址有误，请重新填写。";
                    joymealert.alert(alertOption);
                    bool = false;
                    return;
                }
                gameversion = gameversion == '请输入游戏版本' ? "" : gameversion;
                gamesize = gamesize == '请输入游戏大小' ? "" : gamesize;
                systemversion = systemversion == '请输入支持的系统版本' ? "" : systemversion;
                var channelDownloadInfo = {
                    'download': download,
                    'gameversion': gameversion,
                    'gamesize': gamesize,
                    'systemversion': systemversion
                };
                var obj = {
                    'device': device,
                    'channel_id':channel,
                    'channelDownloadInfo':channelDownloadInfo
                };
                var jsonChannel = JSON.stringify(obj);
                result.push(jsonChannel);
                i = i + 1;
            });
            if (!bool) {
                return;
            }
            $('#input_download_info').attr('value', result.join('@'));

            $("#process").removeClass("process-2");
            $("#process").addClass("process-3");
            $("#detailinfo").css("display", "none");
            $("#business").css("display", "block");
            return;
        });

        $("#businessinfo").click(function() {
            var teamname = $("#teamname").val().replace(/^\s+|\s+$/g, '');

            if (teamname == '') {
                alertOption.text = "请填写团队名称。";
                joymealert.alert(alertOption);
                return;
            }
            var reg = /^([\u4E00-\u9FA5a-zA-Z0-9])*$/;
            if (!teamname.match(reg)) {
                alertOption.text = "团队名称有误，不能带有特殊符号。";
                joymealert.alert(alertOption);
                return;
            }
            var bool = ajaxverify.verifyNickNameRegex(teamname);
            if (!bool) {
                alertOption.text = "团队名称含有不适当内容。";
                joymealert.alert(alertOption);
                return;
            }
            var teamnum = $("input[name='teamnum']:checked");
            if (teamnum.length < 1) {
                alertOption.text = "请选择团队人数。";
                joymealert.alert(alertOption);
                return;
            }
            var city = $("#city").val();
            if (city == '') {
                alertOption.text = "请填写您的所在的城市。";
                joymealert.alert(alertOption);
                return;
            }
            var contacts = $("#contacts").val().replace(/^\s+|\s+$/g, '');
            if (contacts == '') {
                alertOption.text = "请填写联系人信息。";
                joymealert.alert(alertOption);
                return;
            }
            var cantactsbool = ajaxverify.verifyNickNameRegex(contacts);
            if (!cantactsbool) {
                alertOption.text = "联系人含有不适当内容。";
                joymealert.alert(alertOption);
                return;
            }
            var reg = /^([\u4E00-\u9FA5a-zA-Z])*$/;
            if (!contacts.match(reg)) {
                alertOption.text = "联系人含有不适当内容。";
                joymealert.alert(alertOption);
                return;
            }
            var email = $("#email").val();
            if (email == '') {
                alertOption.text = "请填写邮箱。";
                joymealert.alert(alertOption);
                return;
            }
            var reg = /^\w+([-\.]\w+)*@\w+([\.-]\w+)*\.\w{2,4}$/;
            if (!email.match(reg)) {
                alertOption.text = "联系邮箱格式不正确，请修改后再提交。";
                joymealert.alert(alertOption);
                return;
            }
            var phone = $("#phone").val();
            if (phone == '') {
                alertOption.text = "请填写联系电话。";
                joymealert.alert(alertOption);
                return;
            }
            if (phone.length < 6) {
                alertOption.text = "请正确填写您的联系电话哦。";
                joymealert.alert(alertOption);
                return;
            }
            var reg = /^d{3}-d{8}|d{4}-d{7}|\d$/;
            if (!phone.match(reg)) {
                alertOption.text = "联系电话格式不正确，请修改后再提交。";
                joymealert.alert(alertOption);
                return;
            }
            var qq = $("#qq").val();
            if (qq == '') {
                alertOption.text = "请填写QQ号码。";
                joymealert.alert(alertOption);
                return;
            }
            var reg = /^[1-9][0-9]{4,}$/;
            if (!qq.match(reg)) {
                alertOption.text = "联系QQ格式不正确，请修改后再提交。";
                joymealert.alert(alertOption);
                return;
            }
            var area = 0;
            $("input[name='checkbox']:checked").each(function() {
                area += Number($(this).val());
            });
            if (area == 0) {
                alertOption.text = "请选择发行范围。";
                joymealert.alert(alertOption);
                return;
            }
            $("#area").val(area);
            var financing = 0;
            $("input[name='fincheckbox']:checked").each(function() {
                financing += Number($(this).val());
            });
            if (financing == 0) {
                alertOption.text = "请选择融资方式。";
                joymealert.alert(alertOption);
                return;
            }
            $("#financing").val(financing);

            $("#newgame").submit();
        });


        var settings = {
            flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
            flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",
            upload_url : joyconfig.urlUpload + "/json/upload/face",
            post_params : {
                "at" : joyconfig.token
            },
            file_size_limit : "2MB",
            file_types : "*.jpg;*.png;",
            file_types_description : "All Files",
            file_queue_limit :1,

            debug: false,

            // Button settings
            button_image_url:joyconfig.URL_LIB + '/static/theme/default/img/uploadgamepic.png',
            button_width: "94",
            button_height: "26",
            button_text : '',
            button_text_style : '',
            button_text_top_padding: 2,
            button_text_left_padding: 0,
            button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,
            button_window_mode: SWFUpload.WINDOW_MODE.OPAQUE,
            button_cursor: SWFUpload.CURSOR.HAND,
            button_placeholder_id: "uploadpic",

            moving_average_history_size: 40,

            // The event handler functions are defined in handlers.js
            file_dialog_complete_handler: handler.fileDialogComplete,
            upload_start_handler : handler.uploadStart,
            file_queue_error_handler :handler.uploadError,
            upload_progress_handler : handler.uploadProgress,
            upload_success_handler : handler.uploadSuccess,
            upload_complete_handler : handler.uploadComplete,



            custom_settings : {
                resource_path : joyconfig.DOMAIN,
                tdFilesUploaded : document.getElementById("tdFilesUploaded"),
                cancelButtonId : "btnCancel"
                //tdErrors : document.getElementById("tdErrors")
            }
        };

        swfu = new SWFUpload(settings);


        var settings1 = {
            flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
            flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",
            upload_url : joyconfig.urlUpload + "/json/upload/face",
            post_params : {
                "at" : joyconfig.token
            },
            file_size_limit : "2MB",
            file_types : "*.jpg;*.png;",
            file_types_description : "All Files",
            file_upload_limit :0,
            file_queue_limit :1,

            debug: false,

            // Button settings
            button_image_url:joyconfig.URL_LIB + '/static/theme/default/img/uploadgameicon.png',
            button_width: "94",
            button_height: "26",
            button_text : '',
            button_text_style : '',
            button_text_top_padding: 2,
            button_text_left_padding: 0,
            button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,
            button_window_mode: SWFUpload.WINDOW_MODE.OPAQUE,
            button_cursor: SWFUpload.CURSOR.HAND,
            button_placeholder_id: "upload_button",

            moving_average_history_size: 40,

            // The event handler functions are defined in handlers.js
            file_dialog_complete_handler: iconhanler.fileDialogComplete,
            upload_start_handler : iconhanler.uploadStart,
            file_queue_error_handler :iconhanler.uploadError,
            upload_progress_handler : iconhanler.uploadProgress,
            upload_success_handler : iconhanler.uploadSuccess,
            upload_complete_handler : iconhanler.uploadComplete,



            custom_settings : {
                resource_path : joyconfig.DOMAIN,
                tdFilesUploaded : document.getElementById("tdFilesUploaded"),
                cancelButtonId : "btnCancel"
                //tdErrors : document.getElementById("tdErrors")
            }
        };

        swfu1 = new SWFUpload(settings1);
    });

    function deletePic() {
        if (confirm("是否删除该图片？")) {
            var id = pic.id;
            $("#" + id).remove();
        }
    }

    var callback = {
        getgamedbbyname:function(result) {
            if (result.status_code == '0') {
                alertOption.text = result.msg;
                joymealert.alert(alertOption);
                return false;
            } else {
                return true;
            }
        }
    }
    var iconhanler =
    {
        uploadError :function (file, errorCode, message) {
            if (errorCode == SWFUpload.QUEUE_ERROR.INVALID_FILETYPE) {
                alertOption.text = '请确定您上传图片类型是否正确（只支持jpg、png）。';
                alertOption.title = '上传失败';
                joymealert.alert(alertOption);
                return;
            }
            if (errorCode == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {
                alertOption.text = '上传的文件过大，请上传小于2MB的文件。';
                alertOption.title = '上传失败';
                joymealert.alert(alertOption);
                return;
            }
        }
        ,
        fileDialogComplete:function (numFilesSelected, numFilesQueued) {
            this.startUpload();
        } ,
        uploadStart:function (file) {

            $("#loading").css("display", "inline");
        } ,
        uploadProgress:function (file, bytesLoaded, bytesTotal) {
            try {
                this.customSettings.progressCount++;
                updateDisplay.call(this, file);
            } catch (ex) {
                this.debug(ex);
            }

        }  ,
        uploadSuccess:function (file, serverData) {

            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    var imgSrc = common.genImgDomain(jsonData.result[0].b, joyconfig.DOMAIN);
                    $("#gameicon").attr("src", imgSrc);
                    $("#picurl").val(imgSrc);
                    $("#loading").css("display", "none");
                } else {
                    alertOption.text = '上传失败,请检查您的图片类型';
                    alertOption.title = '上传失败';
                    joymealert.alert(alertOption);
                    $("#loading").css("display", "none");
                    return;
                }
            } catch (ex) {
                this.debug(ex);
            }
        }
    }

    function tFocus() {
        var id = $(this).attr("id");
        var idObject = $("#" + id);
        if (id.substring(0, id.length - 1) == "download") {
            if (idObject.val() == '请输入下载地址') {
                idObject.val("");
            }
        }
        if (id.substring(0, id.length - 1) == "gameversion") {
            if (idObject.val() == '请输入游戏版本') {
                idObject.val("");
            }
        }
        if (id.substring(0, id.length - 1) == "gamesize") {
            if (idObject.val() == '请输入游戏大小') {
                idObject.val("");
            }
        }
        if (id.substring(0, id.length - 1) == "systemversion") {
            if (idObject.val() == '请输入支持的系统版本') {
                idObject.val("");
            }
        }

    }

    function tBlur() {
        var id = $(this).attr("id");
        var idObject = $("#" + id);
        if (id.substring(0, id.length - 1) == "download") {
            if (idObject.val() == '') {
                idObject.val("请输入下载地址");
            }
        }
        if (id.substring(0, id.length - 1) == "gameversion") {
            if (idObject.val() == '') {
                idObject.val("请输入游戏版本");
            }
        }
        if (id.substring(0, id.length - 1) == "gamesize") {
            if (idObject.val() == '') {
                idObject.val("请输入游戏大小");
            }
        }
        if (id.substring(0, id.length - 1) == "systemversion") {
            if (idObject.val() == '') {
                idObject.val("请输入支持的系统版本");
            }
        }
    }


});






