<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加游戏标签</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="http://static.joyme.com/js/qiniu.js"></script>
    <script>
        $(document).ready(function () {
            $('#form').submit(function () {
                var nick = $('#nick').val();
                var icon = $('#icon').val();
                var sex = $("input[name='sex']:checked").val();
                var description = $('#description').val();

                var isError = false;
                if (nick.length == 0) {
                    $('#msg_nick').html('<span style="color:red">昵称不能为空</font>')
                    isError = true;
                }

                if (icon.length == 0) {
                    $('#msg_icon').html('<span style="color:red">头像不能为空</font>')
                    isError = true;
                }

                if (sex === undefined) {
                    $('#msg_sex').html('<span style="color:red">请选择性别</font>')
                    isError = true;
                }

                if (description.length == 0) {
                    $('#msg_description').html('<span style="color:red">请填写描述</font>')
                    isError = true;
                }

                if (isError) {
                    return false;
                }
            });
            upload();
        });

        function upload() {
            var QiniuPic = new QiniuJsSDK();
            var picUploader = QiniuPic.uploader({
                runtimes: 'html5,flash,html4',    //上传模式,依次退化
                browse_button: 'pickfiles',       //上传选择的点选按钮，**必需**
                uptoken: '${uploadtoken}',
                unique_names: false,
                save_key: false,
                domain: 'http://joymepic.joyme.com/',
                container: 'container',           //上传区域DOM ID，默认是browser_button的父元素，
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
                            var sourceLink = domain + res.key + "?imageMogr2/auto-orient"; //获取上传成功后的文件的Url
                            $('#imageUrl').attr("src", sourceLink);
                            $('#icon').val(sourceLink);
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

                        return "/headicon/" + Year + "/" + Month + "/" + timestamp + "/" + uuid() + extName;
                    }
                }

            });
            picUploader.stop();

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
        }

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 添加虚拟用户</td>
    </tr>
    <tr>

        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加虚拟用户</td>
                </tr>
                <tr>
                    <td>
                        <form action="/apiwiki/vertualprofile/create" method="post" id="form">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_header_td"></td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">昵称</td>
                                    <td class="edit_table_value_td">
                                        <input type="text" id="nick" name="nick" class=""
                                               placeholder="请输入昵称" maxlength="32" size="32"/>
                                    </td>
                                    <td id="msg_nick" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">头像</td>
                                    <td class="edit_table_value_td">
                                        <div id="container">
                                            <img id="imageUrl" src=""/>
                                            <input type="hidden" name="icon" value="" id="icon">
                                            <a class="" id="pickfiles" href="#"><i>上传头像</i></a>
                                        </div>
                                    </td>
                                    <td id="msg_icon" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">性别</td>
                                    </td >
                                    <td class="edit_table_value_td">
                                        <input type="radio" value="1" name="sex">男
                                        <input type="radio" value="0" name="sex">女
                                    </td>
                                    <td id="msg_sex" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">描述信息</td>
                                    </td>
                                    <td class="edit_table_value_td">
                                        <textarea id="description" name="description" rows="8" cols="50"></textarea>
                                    </td>
                                    <td id="msg_description" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_value_td" align="center"><input
                                            type="submit" name="button" id="sub" class="default_button" value="提交"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>