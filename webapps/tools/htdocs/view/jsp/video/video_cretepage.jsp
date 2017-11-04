<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>创建APP</title>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
<script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/static/include/js/jquery.form.js"></script>

<script type="text/javascript" src="/static/include/js/common.js"></script>
<script type="text/javascript" src="/static/include/js/plupload/moxie.js"></script>
<script type="text/javascript" src="/static/include/js/plupload/plupload.dev.js"></script>
<script type="text/javascript" src="/static/include/js/qiniu/qiniu.js"></script>
<style>
    #container {
        width: 226px;
        height: 25px;
        position: relative;
    }

    #filebtn {
        position: absolute;
        right: 167px;
    }

    #file {
        width: 70px;
        position: absolute;
        right: 0px;
        top: 0px;
        opacity: 0;
    }
</style>
<script>
    $(document).ready(function () {
        $('#form_submit').bind('submit', function () {
            var appNameVal = $('#input_text_appname').val();
            if (appNameVal.length == 0) {
                alert('请填写游戏名称');
                return false;
            }
        });


        var inputFile = $('#fileToUpload');
        var msg = $('#msg');
        var uploader = Qiniu.uploader({
            runtimes: 'html5,flash,html4',    //上传模式,依次退化
            browse_button: 'file',       //上传选择的点选按钮，**必需**
            //若未指定uptoken_url,则必须指定 uptoken ,uptoken由其他程序生成
            uptoken_url: '/video/gettoken',
            domain: 'http://joymepic.joyme.com/',
            container: 'container',//上传区域DOM ID，默认是browser_button的父元素，
            max_file_size: '1000mb',           //最大文件体积限制
            flash_swf_url: '/static/include/js/plupload/Moxie.swf',  //引入flash,相对路径
            max_retries: 3,                   //上传失败最大重试次数
            dragdrop: false,                   //开启可拖曳上传
            drop_element: 'container',        //拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
            chunk_size: '4mb',                //分块上传时，每片的体积
            auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
            get_new_uptoken: false,
            save_key:false,
            unique_names:true,

            init: {
                'FilesAdded': function (up, files) {
                    plupload.each(files, function (file) {
                        // 文件添加进队列后,处理相关的事情
                        //  msg.append(' FilesAdded 事件（文件添加进队列后,处理相关的事情）<br />');
                    });
                },
                'BeforeUpload': function (up, file) {
                    // 每个文件上传前,处理相关的事情
                    //    $("#file").val(file.name);
                    var d = file.name.length - ".mp4".length;
                    if (!(d >= 0 && file.name.lastIndexOf(".mp4") == d)) {
                        $("#file").val("");
                        alert("暂时只支持MP4格式的文件");
                        return false;
                    }
                    //  msg.append(' BeforeUpload 事件（每个文件上传前,处理相关的事情）<br />');
                },
                'UploadProgress': function (up, file) {
                    // 每个文件上传时,处理相关的事情
                    var percent = Math.round(file.loaded / file.size * 100);
                    msg.html('<span style="color:red">正在上传 请勿重复操作</span>  已上传：' + percent + '%<br />');
                },
                'FileUploaded': function (up, file, info) {
                    // 每个文件上传成功后,处理相关的事情
                    // 其中 info 是文件上传成功后，服务端返回的json，形式如
                    // {
                    //    "hash": "Fh8xVqod2MQ1mocfI4S4KpRL6D98",
                    //    "key": "gogopher.jpg"
                    //  }
                    // 参考http://developer.qiniu.com/docs/v6/api/overview/up/response/simple-response.html
                    // var domain = up.getOption('domain');
                    // var res = parseJSON(info);
                    // var sourceLink = domain + res.key; 获取上传成功后的文件的Url
                    //   msg.append(' FileUploaded 事件（每个文件上传成功后,处理相关的事）' + JSON.stringify(info) + '<br />');
                    var domain = up.getOption('domain');
                    var res = eval('(' + info + ')');
                    var sourceLink = domain + res.key; //获取上传成功后的文件的Url
                    //   msg.append(sourceLink);
                    $("#video").css("display", "inline");
                    $("#video").attr("src", sourceLink);
                    $("[name='videourl']").val(sourceLink);
                    $("#deleteVideoId").css("display", "inline");
                    // $("#loadingvideo").css("display", "none");
                },
                'Error': function (up, err, errTip) {
                    //上传出错时,处理相关的事情<br>
                    $("#errormsg").html('上传失败:' + errTip);
                },
                'UploadComplete': function () {
                    msg.html('<span style="color:red">上传完成</span><br />');
                    //队列文件处理完毕后,处理相关的事情
                    //  msg.append(' UploadComplete 事件（队列文件处理完毕后,处理相关的事情）<br />');
                }
//                'Key': function (up, file) {
//                    // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
//                    // 该配置必须要在 unique_names: false , save_key: false 时才生效
//                    var key = "123.mp4";
//                    var ext = Qiniu.getFileExtension(file.name);
//                    alert(ext);
//                    // do something with key here
//                    return key
//                }
            }
        });
        uploader.stop();
    });

//    function videoupload() {
//
////            var f = document.getElementById("file").files;
////            if (f[0].size / 1000 / 1000 > 20) {
////                alert("不能传超过20M的视频");
////                $("#file").val("");
////                return false;
////            }
//        if (a != "") {
//            alert("正在上传中");
//            return;
//        }
//        var file = $("#file").val();
//        if (file == "") {
//            alert("请先选择文件");
//            return;
//        }
//
//
//        var d = file.length - ".mp4".length;
//        if (!(d >= 0 && file.lastIndexOf(".mp4") == d)) {
//            $("#file").val("");
//            alert("暂时只支持MP4格式的文件");
//            return;
//        }
//        // $("#file").attr("disabled", "disabled");
//
//        $("#loadingvideo").css("display", "block");
//        a = $("#uploadvideo").ajaxSubmit({
//            type: "POST",
//            url: "/json/upload/video",
//            success: function (req) {
//                var result = eval('(' + req + ')');
//                alert("上传成功");
//                $("#video").css("display", "inline");
//                $("#video").attr("src", result.result[0]);
//                $("[name='videourl']").val(result.result[0]);
//                $("#deleteVideoId").css("display", "inline");
//
//                $("#loadingvideo").css("display", "none");
//                a = "";
//
//            }, error: function () {
//                $("#loadingvideo").css("display", "none");
//                alert("上传失败");
//                a = "";
//            }
//        });
//    }
    function deletevideo() {
        if (confirm("是否要删除已经上传的视频？")) {
            $("#video").css("display", "none");
            $("#video").attr("src", "");
            $("#deleteVideoId").css("display", "none");
            $("#file").val("");
            $("[name='videourl']").val("");
            $('#msg').html("");


        }
    }

    function createvideo() {
        var title = $("[name='title']").val();
        var gamesdk = $("[name='gamesdk']").val();
        var videourl = $("[name='videourl']").val();
        if (title == "") {
            alert("请填写视频标题");
            return false;
        }

        if (gamesdk == "") {
            alert("请选择所属游戏");
            return false;
        }

        if (videourl == "") {
            alert("请上传视频");
            return false;
        }
        $("#form_submit").submit();
    }


</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 视频管理 >> 视频列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新建视频</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/video/create" method="post" id="form_submit">

                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" value="" name="videourl"/>


                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            视频标题:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input type="text" value="" name="title" size="32" id="input_text_appname"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择游戏：
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="gamesdk">
                                <option value="">请选择游戏</option>
                                <c:forEach items="${gamelist}" var="game">
                                    <option value="${game.appId}">${game.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                </form>

                <tr>
                    <td height="1" class="default_line_td">
                        上传视频：
                    </td>
                    <td>

                        <video id="video"
                               src=""
                               width="400" style="display:none;" controls autobuffer preload="metadata">

                        </video>
                        <a href="javascript:deletevideo();" id="deleteVideoId" style="display:none">删除</a>
                        <%--</video>--%>
                        <div id="container">
                            <input id="filebtn" onclick="file.click()" type="button" value="选择视频"/>

                            <input name="Filedata" class="file" accept=".mp4" id="file"
                                   type="file"
                                   style="width:150px;"/>


                        </div>
                        <div id="errormsg"></div>
                        <div id="msg"></div>

                    </td>
                    <td height="1" class=>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="1" class="">*以上都是必填必选项</td>
                </tr>
                <tr align="center">
                    <td colspan="3">
                        <input name="Submit" type="button" onclick="createvideo()" class="default_button" value="提交">
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