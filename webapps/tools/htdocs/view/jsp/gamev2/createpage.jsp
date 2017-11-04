<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新版游戏资料库-创建游戏</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link href="/static/include/css/gametag.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/js/gametag.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/js/qiniu.js"></script>

    <script>
        $(document).ready(function () {
            upload();
            uploadGamePic();
            $('a[id^=delpic]').click(function () {
                var idx = $(this).attr('data-button');
                $('#img_pic' + idx).attr("src", '');
                $('#hidden_pic' + idx).val('');
                $(this).hide();
            });

            WS({el: '#gametags'});
        });

        var QiniuPic = new QiniuJsSDK();
        function upload() {
            var QiniuPic = new QiniuJsSDK();
            var picUploader = QiniuPic.uploader({
                runtimes: 'html5,flash,html4',    //上传模式,依次退化
                browse_button: 'but_gameLogo',       //上传选择的点选按钮，**必需**
                uptoken: '${uploadtoken}',
                unique_names: false,
                save_key: false,
                domain: 'http://joymepic.joyme.com/',
                max_file_size: '2mb',           //最大文件体积限制
                flash_swf_url: 'http://lib.joyme.com/static/third/qiniuupload/plupload/Moxie.swf',  //引入flash,相对路径
                max_retries: 3,                   //上传失败最大重试次数
                dragdrop: true,                   //开启可拖曳上传
                chunk_size: '2mb',                //分块上传时，每片的体积
                auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
                init: {
                    'FilesAdded': function (up, files) {
                        var i = 1;
                        plupload.each(files, function (file) {
                            files.splice(i, 1);
                            // 文件添加进队列后,处理相关的事情
                        });
                    },
                    'BeforeUpload': function (up, file) {
                        var picType = file.name.substring(file.name.lastIndexOf('.'), file.name.length).toUpperCase();

                        if (picType != '.JPG' && picType != '.PNG' && picType != '.JPEG' && picType != '.BMP') {
                            this.trigger('Error', {
                                code: -222,
                                message: "请上传jpg、png、jpeg、bmp格式的图片",
                                file: file
                            });
                            return false;
                        }
                        if (file.size >= 1024 * 1024 * 2) {
                            mw.ugcwikiutil.msgDialog("请上传小于2M的图片");
                            return false;
                        }
                    },
                    'UploadProgress': function (up, file) {
                    },
                    'FileUploaded': function (up, file, info) {
                        try {
                            var domain = up.getOption('domain');
                            var res = JSON.parse(info);
                            var sourceLink = domain + res.key; //获取上传成功后的文件的Url
                            $('#img_gameLogo').attr("src", sourceLink + "?imageMogr2/auto-orient/thumbnail/120x120");
                            $('#hidden_gameLogo').val(sourceLink);
                        } catch (ex) {
                            alert(ex);
                        }
                    },
                    'Error': function (up, err, errTip) {
                        if (err.code == plupload.FILE_SIZE_ERROR) {
                            mw.ugcwikiutil.msgDialog("请上传小于2M的图片");
                        } else if (err.code == -222) {
                            mw.ugcwikiutil.msgDialog("请上传jpg、png、jpeg、bmp格式的图片");
                        } else {
                            mw.ugcwikiutil.msgDialog(errTip);
                        }
                    },
                    'UploadComplete': function () {

                    },
                    'Key': function (up, file) {
                        // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                        var day = new Date();
                        var Year = day.getFullYear();
                        var Month = day.getMonth() + 1;
                        if (Month < 10) {
                            Month = '0' + Month;
                        }
                        var timestamp = Math.ceil(day.getTime());
                        var extName = ".jpg";
                        if (file.type == 'image/jpg') {
                            extName = ".jpg"
                        } else if (file.type == 'image/jpeg') {
                            extName = ".jpg"
                        } else {
                            extName = ".png";
                        }

                        return "/gamelogo/" + Year + "/" + Month + "/" + timestamp + "/" + uuid() + extName;
                    }
                }

            });
            picUploader.stop();
        }

        function uuid() {
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            return uuid;
        }

        function uploadGamePic() {
            for (var butIdx = 1; butIdx <= 5; butIdx++) {
                var picUploader = QiniuPic.uploader({
                    runtimes: 'html5,flash,html4',    //上传模式,依次退化
                    browse_button: 'but_pic' + butIdx,       //上传选择的点选按钮，**必需**
                    butindex: butIdx,
                    uptoken: '${uploadtoken}',
                    unique_names: false,
                    save_key: false,
                    domain: 'http://joymepic.joyme.com/',
                    max_file_size: '2mb',           //最大文件体积限制
                    flash_swf_url: 'http://lib.joyme.com/static/third/qiniuupload/plupload/Moxie.swf',  //引入flash,相对路径
                    max_retries: 3,                   //上传失败最大重试次数
                    dragdrop: true,                   //开启可拖曳上传
                    chunk_size: '2mb',                //分块上传时，每片的体积
                    auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
                    init: {
                        'FilesAdded': function (up, files) {
                            var i = 1;
                            plupload.each(files, function (file) {
                                files.splice(i, 1);
                                // 文件添加进队列后,处理相关的事情
                            });
                        },
                        'BeforeUpload': function (up, file) {
                            var picType = file.name.substring(file.name.lastIndexOf('.'), file.name.length).toUpperCase();

                            if (picType != '.JPG' && picType != '.PNG' && picType != '.JPEG' && picType != '.BMP') {
                                this.trigger('Error', {
                                    code: -222,
                                    message: "请上传jpg、png、jpeg、bmp格式的图片",
                                    file: file
                                });
                                return false;
                            }
                            if (file.size >= 1024 * 1024 * 2) {
                                mw.ugcwikiutil.msgDialog("请上传小于2M的图片");
                                return false;
                            }
                        },
                        'UploadProgress': function (up, file) {
                        },
                        'FileUploaded': function (up, file, info) {
                            try {
                                var domain = up.getOption('domain');
                                var res = JSON.parse(info);
                                var sourceLink = domain + res.key; //获取上传成功后的文件的Url

                                var butId = up.getOption('butindex');
                                $('#img_pic' + butId).attr("src", sourceLink + "?imageMogr2/auto-orient/thumbnail/120x120");
                                $('#hidden_pic' + butId).val(sourceLink);
                                $('#delpic' + butId).show();
                            } catch (ex) {
                                alert(ex);
                            }
                        },
                        'Error': function (up, err, errTip) {
                            if (err.code == plupload.FILE_SIZE_ERROR) {
                                mw.ugcwikiutil.msgDialog("请上传小于2M的图片");
                            } else if (err.code == -222) {
                                mw.ugcwikiutil.msgDialog("请上传jpg、png、jpeg、bmp格式的图片");
                            } else {
                                mw.ugcwikiutil.msgDialog(errTip);
                            }
                        },
                        'UploadComplete': function () {

                        },
                        'Key': function (up, file) {
                            // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                            var day = new Date();
                            var Year = day.getFullYear();
                            var Month = day.getMonth() + 1;
                            if (Month < 10) {
                                Month = '0' + Month;
                            }
                            var timestamp = Math.ceil(day.getTime());
                            var extName = ".jpg";
                            if (file.type == 'image/jpg') {
                                extName = ".jpg"
                            } else if (file.type == 'image/jpeg') {
                                extName = ".jpg"
                            } else {
                                extName = ".png";
                            }

                            return "/gamelogo/" + Year + "/" + Month + "/" + timestamp + "/" + uuid() + extName;
                        }
                    }

                });
                picUploader.stop();
            }
        }

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
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 新版游戏资料库-创建游戏</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">创建游戏</td>
                </tr>

            </table>
            <form action="/gamev2/create" method="post" id="form_submit">
                <table width="80%" border="0" cellspacing="1" cellpadding="0">
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="1" colspan="3" class="edit_table_header_td"></td>
                        </tr>
                        <c:if test="${fn:length(errorMsg)>0}">
                            <tr>
                                <td height="1" colspan="3" class="fontcolor_input_hint">
                                    <fmt:message key="${errorMsg}" bundle="${errorProps}"/>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏名称</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="name" value="${name}" name="name" class=""
                                       placeholder="请输入游戏名称 *必填" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_name" class="detail_table_value_td">*必填</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏别名</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="aliasName" value="${aliasName}" name="aliasName" class=""
                                       placeholder="请输入游戏别名" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_aliasName" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏描述</td>
                            <td class="edit_table_value_td">
                                <textarea name="gameDesc" rows="8" cols="50"></textarea>
                            </td>
                            <td id="msg_gameDesc" class="detail_table_value_td">*游戏描述</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏LOGO</td>
                            <td class="edit_table_value_td" id="uplaod_logo_container">
                                <img src="" width="120px" height="120px" id="img_gameLogo"/>
                                <input type="hidden" name="gameLogo" value="" id="hidden_gameLogo">
                                <a href="#" id="but_gameLogo">上传游戏logo</a>
                            </td>
                            <td id="msg_gameLogo" class="detail_table_value_td">*必填</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏截图</td>
                            <td class="edit_table_value_td" colspan="2">
                                <table>
                                    <tr>
                                        <td>
                                            <img src="" width="120px" height="120px" id="img_pic1"/><br/>
                                            <input type="hidden" name="pic1" value="" id="hidden_pic1">
                                            <a href="#" id="but_pic1">上传游戏截图1</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:void(0);" style="display:none" id="delpic1"
                                               data-button="1">删除截图1</a>
                                        </td>
                                        <td>
                                            <img src="" width="120px" height="120px" id="img_pic2"/><br/>
                                            <input type="hidden" name="pic2" value="" id="hidden_pic2">
                                            <a href="#" id="but_pic2">上传游戏截图2</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:void(0);" style="display:none" id="delpic2"
                                               data-button="2">删除截图2</a>
                                        </td>
                                        <td>
                                            <img src="" width="120px" height="120px" id="img_pic3"/><br/>
                                            <input type="hidden" name="pic3" value="" id="hidden_pic3">
                                            <a href="#" id="but_pic3">上传游戏截图3</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:void(0);" style="display:none" id="delpic3"
                                               data-button="3">删除截图3</a>
                                        </td>
                                        <td>
                                            <img src="" width="120px" height="120px" id="img_pic4"/><br/>
                                            <input type="hidden" name="pic4" value="" id="hidden_pic4">
                                            <a href="#" id="but_pic4">上传游戏截图4</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:void(0);" style="display:none" id="delpic4"
                                               data-button="4">删除截图4</a>
                                        </td>
                                        <td>
                                            <img src="" width="120px" height="120px" id="img_pic5"/><br/>
                                            <input type="hidden" name="pic5" value="" id="hidden_pic5">
                                            <a href="#" id="but_pic5">上传游戏截图5</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:void(0);" style="display:none" id="delpic5"
                                               data-button="1">删除截图5</a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td id="msg_pic" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏标签</td>
                            <td class="edit_table_value_td" >
                                <div>
                                    <input type="hidden" id="gametags" name="gameTag" value="" style="display: none"/>
                                </div>
                            </td>
                            <td id="msg_gameTag" class="detail_table_value_td" >
                                *如果提示标签不存在请<a href="/gamev2/tag/createpage" target="_blank">点我!打开一个新窗口创建标签</a>
                            </td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏类型</td>
                            <td class="edit_table_value_td">
                                <select name="gameType">
                                    <option value="">请选择</option>
                                    <c:forEach var="gtype" items="${gameType}">
                                        <option value="${gtype}"
                                                <c:if test="${gtype=='UNKNOWN'}">selected</c:if> >${gtype}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td id="msg_gameType" class="detail_table_value_td">*必选</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏开发商</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="gameDeveloper" value="" name="gameDeveloper" class=""
                                       placeholder="请输入游戏开发商" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_gameDeveloper" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏发行商</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="gamePublisher" value="" name="gamePublisher" class=""
                                       placeholder="请输入游戏发行商" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_gamePublisher" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">运营状态</td>
                            <td class="edit_table_value_td">
                                <select name="operStatus">
                                    <option value="">请选择</option>
                                    <c:forEach var="ops" items="${operStatus}">
                                        <option value="${ops}"
                                                <c:if test="${ops=='UNKNOWN'}">selected</c:if> >${ops}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td id="msg_opsStatus" class="detail_table_value_td">*必选</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">视频地址</td>
                            <td class="edit_table_value_td">
                                <input type="text" name="video" value="">
                            </td>
                            <td id="msg_video" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">是否是网络游戏</td>
                            <td class="edit_table_value_td">
                                <input type="radio" name="isOnline" value="true">是
                                <input type="radio" name="isOnline" value="false">否
                            </td>
                            <td id="msg_isOnline" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏语言</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="language" value="" name="language" class=""
                                       placeholder="请输入语言" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_language" class="detail_table_value_td">多语言用"，"分割</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏大小</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="size" value="" name="size" class=""
                                       placeholder="请输入大小" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_size" class="detail_table_value_td"></td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">游戏价格</td>
                            <td class="edit_table_value_td">
                                <input type="text" id="price" value="" name="price" class=""
                                       placeholder="请输入价格" maxlength="32" size="32"/>
                            </td>
                            <td id="msg_price" class="detail_table_value_td">填写价格，免费为空</td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">选择平台</td>
                            <td class="edit_table_value_td" colspan="2">
                                <table>
                                    <tr>
                                        <td>是否是安卓：</td>
                                        <td>
                                            <input type="checkbox" value="true" name="android"/>
                                        </td>
                                        <td>下载地址：</td>
                                        <td>
                                            <input type="text" value="" name="androidDownload"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>是否是IOS：</td>
                                        <td>
                                            <input type="checkbox" value="true" name="ios"/>
                                        </td>
                                        <td>下载地址：</td>
                                        <td>
                                            <input type="text" value="" name="iosDownload"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>是否是PC：</td>
                                        <td>
                                            <input type="checkbox" value="true" name="pc"/>
                                        </td>
                                        <td>下载地址：</td>
                                        <td>
                                            <input type="text" value="" name="pcDownload"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="edit_table_defaulttitle_td" width="100" height="1">一句话推荐</td>
                            <td class="edit_table_value_td" colspan="2">
                                <table>
                                    <tr>
                                        <td>一句话推荐作者</td>
                                        <td><input name="recommendAuth" value="" placeholder="推荐作者"/></td>
                                        <td>一句话推荐</td>
                                        <td><input name="recommend" value="" placeholder="一句话推荐"/></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" colspan="3" class="edit_table_value_td" align="center"><input
                                    type="submit" name="button" id="sub" class="default_button" value="提交"/>
                            </td>
                        </tr>
                    </table>
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