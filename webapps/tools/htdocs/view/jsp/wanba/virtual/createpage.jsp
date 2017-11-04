<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新增虚拟用户</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/appadvertise.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var checkNick = false;

            $('#form_submit').bind('submit', function () {
                var nick = $("input[name='nick']").val();
                var desc = $("#desc").val();
                var pic = $("input[name='pic']").val();
                if(checkNick){
                    alert("请检查昵称!");
                    return false;
                }
                if (nick.trim() == "") {
                    alert("请填写昵称");
                    return false;
                }

                if(desc.trim().length>15){
                    alert("最多输入15个字符");
                    return false;
                }
                if (pic.trim() == "") {
                    alert("请检查图片是否上传");
                    return false;
                }

            });

            $("#check").blur(function () {
                var nick = $("input[name='nick']").val();
                if (nick.trim() == '') {
                    alert("请填写昵称");
                    checkNick=true;
                    return;
                }
                $.ajax({
                    url: "/wanba/profile/virtualchecknick",
                    data: {nick: nick},
                    timeout: 5000,
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.rs == '-1') {
                            //alert("昵称已在虚拟用户,请修改!");
                            checkNick=false;
                        }else{
                            checkNick=true;
                        }
                    }
                });
            });


            var checkRealNick = false;
            $('#form_submit2').bind('submit', function () {

                var realnick = $("input[name='realnick']").val();
                if (realnick.trim() == '') {
                    alert("请填写昵称!");
                    return;
                }
                if(checkRealNick){
                    alert("请检查昵称");
                    return false;
                }
            });
            $("#realnick").blur(function () {
                var realnick = $("input[name='realnick']").val();
                if (realnick.trim() == '') {
                    alert("请填写昵称");
                    checkNick=true;
                    return;
                }
                $.ajax({
                    url: "/wanba/profile/virtualchecknick",
                    data: {nick: realnick},
                    timeout: 5000,
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.rs == '-1') {
                           // alert("昵称不存在,请修改!");
                            checkRealNick=true;
                            return false;
                        }else if (data.rs == '-3') {
                          //  alert("昵称已在虚拟用户,请修改!");
                            checkRealNick=true;
                            return false;
                        }else{
                            checkRealNick=false;
                        }
                    }
                });
            });

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
            debug: false
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 新增虚拟用户</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增虚拟用户</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                                ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/wanba/virtual/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            用户昵称:
                        </td>
                        <td height="1">
                            <input type="text" name="nick" size="50" id="check"/>
                            <span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            性别:
                        </td>
                        <td height="1">
                            <select name="sex">
                               <option value="1">男</option>
                                <option value="0">女</option>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            简介:
                        </td>
                        <td height="1">
                            <textarea type="text" name="desc" id="desc"  cols="50" rows="5" ></textarea><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            头像:
                        </td>
                        <td>
                            <img id="picurl1_src" src="/static/images/default.jpg"
                                 class="img_pic" width="200px" height="200px"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="pic" value=""><span style="color:red">头像最大尺寸：110*110</span>
                        </td>
                        <td height="1">
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

<br/><br/><br/><br/>
<hr color="blue">
<span style="color:red;">绑定真实用户，跟上面表单没关系!!!!!</span>  :
<form action="/wanba/virtual/realcreate" method="post" id="form_submit2">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="3" class="default_line_td"></td>
        </tr>
        <tr>
            <td height="1" class="default_line_td td_cent" width="100">
                用户昵称:
            </td>
            <td height="1">
                <input type="text" name="realnick" size="50" id="realnick"/>

                <span></span>
                <span>*必填</span>
            </td>
            <td height="1" class=>
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
</body>
</html>