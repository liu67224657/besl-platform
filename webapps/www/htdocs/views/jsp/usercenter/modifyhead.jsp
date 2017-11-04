<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>上传头像</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/edit-select.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/joymedialog.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function (){
            document.addEventListener('touchstart', function (){return false}, true)
        }, true);

    </script>

</head>
<body>

<%@ include file="../usercenter/user-center-header.jsp" %>
<!-- 内容区域 开始 -->
<div class="container">
    <div class="row">
        <div class="setting-con">
            <%@ include file="../customize/customize-general-left.jsp" %>
            <div class="col-md-9 pag-hor-20">
                <div class="setting-r">
                        <input type="hidden" id="profileId" name="profileId" value="${profile.profileId}">
                        <input type="hidden" id="icon" name="icon" value="">
                    <h3 class="setting-tit web-hide">修改头像</h3>
                    <div class="acatar-con">
                        <div class="edit-acatar">
                            <img id="imageUrl" name="imageUrl" src="${icon:parseIcon( profile.getIcon(),  profile.getSex(),"m")}">
                        </div>
                        <div>
								<span class="acatar-btn">
                                    <div  id="container"><a class="" id="pickfiles" href="#"><i>上传头像</i></a></div>
								</span>
                            <p class="web-hide">支持jpg、png、jpeg、bmp，图片大小2M以内，图片宽高大于等于150*150</p>
                            <p class="web-hide">友情提示：最好上传规则图片，否则会影响头像显示效果</p>
                            <button class="web-hide btn-sure" onclick="saveUserPic()">保存</button>
                            <button class="web-show btn-sure" onclick="saveUserPic()">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../usercenter/user-center-footer.jsp" %>
<!-- 内容区域 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" src="http://static.joyme.com/js/plupload.full.min.js"></script>
<script type="text/javascript" src="http://static.joyme.com/js/qiniu.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>
<script>
    var apiHost = 'http://api.joyme.com/';
    var uploadToken="";
    $(document).ready(function () {
        uploadInit();
    });

    function uploadInit() {
        $.ajax({
            url: apiHost + "comment/bean/json/check",
            type: "post",
            data: {profileid: "c0b9fbf18884ac1c3b9be365025da686"},
            dataType: "jsonp",
            jsonpCallback: "checkcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '1') {
                    uploadToken=resMsg.uploadtoken;
                    upload();
                }
            }
        });
    }
        function saveUserPic() {
            var profileId = $('#profileId').val();
            var icon = $('#icon').val();
            if (icon != null && icon !=''){
                $.post("/servapi/profile/modify/icon", {profileid: profileId,icon:icon}, function (data) {
                    if (data != "") {
                        var sendResult = eval("(" + data + ")");
                        var rs = sendResult.rs;
                        if (rs=="1"){
                            //$('#profileIcon').attr('src',icon);
                            //mw.ugcwikiutil.msgDialog("修改成功");
                            mw.ugcwikiutil.ensureDialog("修改成功", function () {
                                window.location.href = "${URL_UC}/usercenter/customize/modifyhead"
                            });
                        }else if (rs=="-1001"){
                            mw.ugcwikiutil.msgDialog("参数不能为空");
                        }else if (rs=="-1000"){
                            mw.ugcwikiutil.msgDialog("系统错误");
                        }
                    }
                });
            }else {
                mw.ugcwikiutil.msgDialog("请选择上传图片");
            }
        }
    //图片传进度条
    function uploadPicProcessLine(uploadRate){
        $('#up_block_line').html('<span style="width:'+uploadRate+'%">'+uploadRate+'%</span>');
    }
    function upload(){
        var QiniuPic = new QiniuJsSDK();
        var picUploader = QiniuPic.uploader({
            runtimes: 'html5,flash,html4',    //上传模式,依次退化
            browse_button: 'pickfiles',       //上传选择的点选按钮，**必需**
            uptoken: uploadToken,
            unique_names: false ,
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
                        files.splice(i,1);
                        // 文件添加进队列后,处理相关的事情
                    });
                },
                'BeforeUpload': function (up, file) {
                    var picType = file.name.substring(file.name.lastIndexOf('.'),file.name.length).toUpperCase();

                    if(picType != '.JPG' && picType != '.PNG' && picType != '.JPEG' && picType != '.BMP'){
                        this.trigger('Error', {
                            code : -222,
                            message : "请上传jpg、png、jpeg、bmp格式的图片",
                            file:file
                        });
                        return false;
                    }
                    if (file.size >= 1024 * 1024 * 2) {
                        mw.ugcwikiutil.msgDialog("请上传小于2M的图片");
                        return false;
                    }
                },
                'UploadProgress': function (up, file) {
                    /*var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                    var uploadRate=Math.ceil(file.loaded*100/file.size);
                    uploadPicProcessLine(uploadRate);*/

                },
                'FileUploaded': function (up, file, info) {
                    try {
                        var domain = up.getOption('domain');
                        var res = JSON.parse(info);
                        var sourceLink = domain +res.key+"?imageMogr2/auto-orient"; //获取上传成功后的文件的Url
                        $('#imageUrl').attr("src",sourceLink);
                        $('#icon').val(sourceLink);
                    } catch (ex) {
                        alert(ex);
                    }
                },
                'Error': function (up, err, errTip) {
                    if(err.code==plupload.FILE_SIZE_ERROR){
                        mw.ugcwikiutil.msgDialog("请上传小于2M的图片");
                    }else if(err.code==-222){
                        mw.ugcwikiutil.msgDialog("请上传jpg、png、jpeg、bmp格式的图片");
                    }else{
                        mw.ugcwikiutil.msgDialog(errTip);
                    }
                },
                'UploadComplete': function () {

                },
                'Key': function (up, file) {
                    // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                    var picType = file.name.substring(file.name.lastIndexOf('.'),file.name.length).toLowerCase();
                    var day = new Date();
                    var Year= day.getFullYear();
                    var Month= day.getMonth()+1;
                    if(Month<10){
                        Month='0'+Month;
                    }
                    var Day = day.getDate();
                    var timestamp=Math.ceil(day.getTime());
                    var key = "live/pic/"+Year+Month+'/'+Day+timestamp+picType;

                    return key
                }
            }

        });
        picUploader.stop();

    }
</script>

</body>
</html>