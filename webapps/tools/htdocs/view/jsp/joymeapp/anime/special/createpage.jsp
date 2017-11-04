<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        $(document).ready(function() {
            $('#form_submit').bind('submit', function() {
                if ($("#input_text_menuname").val() == '') {
                    alert("专题名");
                    $("#input_text_menuname").focus();
                    return false;
                }
                if ($("#specialtype").val() == '') {
                    alert("专题分类");
                    $("#specialtype").focus();
                    return false;
                }

                if ($("#bgcolor").val() == '') {
                    alert("专题分类角标颜色");
                    $("#bgcolor").focus();
                    return false;
                }
                if ($("#specialattr").val() == '') {
                    alert("跳转类型");
                    $("#specialattr").focus();
                    return false;
                }
                if ($("#platform").val() == '') {
                    alert("所属平台");
                    $("#platform").focus();
                    return false;
                }

                if ($("#display_type").val() == '') {
                    alert("图片的展示方式");
                    $("#display_type").focus();
                    return false;
                }
            });
            $("#specialAttr").change(function() {
                var p1 = $(this).children('option:selected').val();
                if (p1 == '1') {
                    $("#div1").css("display", "block");
                    $("#div2").css("display", "none");
                    $("#div3").css("display", "none");
                } else if (p1 == '2') {
                    $("#div1").css("display", "none");
                    $("#div2").css("display", "block");
                    $("#div3").css("display", "none");
                } else if (p1 == '3') {
                    $("#div1").css("display", "none");
                    $("#div2").css("display", "none");
                    $("#div3").css("display", "block");
                } else {
                    $("#div1").css("display", "none");
                    $("#div2").css("display", "none");
                    $("#div3").css("display", "none");
                }
            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
        });
        var coustomImageSettings = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "2 MB",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete,
            upload_start_handler:  uploadStart,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}

        var coustomImageSettings2 = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "2 MB",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete2,
            upload_start_handler:  uploadStart2,
            upload_success_handler : uploadSuccess2,
            upload_complete_handler : uploadComplete2,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button2",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}
        var coustomImageSettings3 = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "2 MB",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete3,
            upload_start_handler:  uploadStart3,
            upload_success_handler : uploadSuccess3,
            upload_complete_handler : uploadComplete3,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button3",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}
        function insertTable(id) {
            if (id == 1) {
                alert("1");
            } else if (id == 2) {
                var url = $("#linkurl").val();
                var desc = $("#desc").val();
                var title = $("#title").val();
                var pic = $("#input_menu_pic3").val();
                if (pic == "") {
                    alert("请上传图片");
                    return;
                }
                var trHTML = "<tr><td>1</td><td>1</td><td>1</td>1<td>1</td><td>1</td></tr>"
                $("#listtable").append(trHTML);
            } else if (id == 3) {
                alert("3");
            }
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫专题维护</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加新专题</td>
                </tr>
            </table>
            <form action="/anime/special/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            APPKEY:
                        </td>
                        <td height="1">
                            <input type="text" name="appkey" size="48" value="${appkey}" readonly="readonly"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            专题名:
                        </td>
                        <td height="1">
                            <input id="input_text_menuname" type="text" name="specialname" size="48" value=""/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                     <tr>
                        <td height="1" class="default_line_td" width="100">
                            专题详情页标题:
                        </td>
                        <td height="1">
                            <input type="text" name="specialtitle" size="48" value=""/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            封面图:
                        </td>
                        <td>
                            <img id="menu_pic" src="/static/images/default.jpg" class="img_pic"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="coverpic" value=""><span style="color: red">必填项，图片尺寸：大图_710px * 236px 小图_180px * 135px</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            专题头图:
                        </td>
                        <td>
                            <img id="menu_pic2" src="/static/images/default.jpg" class="img_pic"/>
                            <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic2" type="hidden" name="specialpic" value=""><span style="color: red">必填项，图片尺寸：大图_710px * 236px 小图_180px * 135px</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            专题描述：
                        </td>
                        <td height="1">
                            <textarea rows="12" cols="52" name="specialdesc"></textarea>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            专题分类:
                        </td>
                        <td height="1">
                            <select name="specialtype" id="specialtype">
                                <option value="">请选择</option>
                                <c:forEach items="${specialType}" var="dto">
                                    <option value="${dto.code}"><fmt:message key="special.category.type.${dto.code}"
                                                                             bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            专题分类角标颜色:
                        </td>
                        <td height="1">
                            <select name="bgcolor" id="bgcolor">
                                <option value="">请选择</option>
                                <option value="1">红色</option>
                                <option value="2">绿色</option>
                                <option value="3">蓝色</option>
                                <option value="4">橙色</option>
                            </select>

                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            跳转类型:
                        </td>
                        <td height="1">
                            <select name="specialattr" id="specialattr">
                                <option value="">请选择</option>
                                <c:forEach items="${list}" var="dto">
                                    <option value="${dto}"><fmt:message key="anime.special.attr.${dto}"
                                                                        bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                      <tr>
                        <td height="1" class="default_line_td" width="100">
                            wap直达链接:
                        </td>
                        <td height="1">
                            <input type="text"  name="linkurl" value="" size="48"/>*只有专题属性为<b style="color:red">wap直达</b>的时候才需要填写链接
                            ,wap直达不需要加 <b style="color:red">?device=app</b>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            所属平台:
                        </td>
                        <td height="1">
                            <select name="platform" id="platform">
                                <option value="">请选择</option>
                                <option value="0">IOS</option>
                                <option value="1">安卓</option>
                                <option value="2">安卓+IOS</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            阅读数:
                        </td>
                        <td height="1">
                            <input type="text" name="read_num" size="48" value=""/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片的展示方式:
                        </td>
                        <td height="1">
                            <select name="display_type" id="display_type">
                                <option value="">请选择</option>
                                <c:forEach items="${specialDisplayType}" var="dto">
                                    <option value="${dto.code}"><fmt:message key="anime.special.display.type.${dto.code}"
                                                                        bundle="${def}"/></option>
                                </c:forEach>
                            </select>
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
        </td>
    </tr>
</table>
</body>
</html>