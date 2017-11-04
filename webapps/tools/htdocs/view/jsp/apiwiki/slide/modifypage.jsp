<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>轮播</title>
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
            $('#form_submit').bind('submit', function () {
                var title = $.trim($('#title').val());
                if (title.length == 0) {
                    alert("请填写名称");
                    return false;
                }
                var starttime = $("input[name='starttime']").val();
                var endtime = $("input[name='endtime']").val();

                if(starttime==''||endtime==''){
                    alert('开始时间或结束时间不能为空');
                    return false;
                }

                var stimelong = convertDate(starttime).getTime();
                var endtimelong = convertDate(endtime).getTime();
                if (stimelong > endtimelong) {
                    alert("开始时间不能大于结束时间");
                    return false;
                }
                var date = new Date();
                if (date.getTime() >= endtimelong) {
                    alert("当前时间不能大于结束时间");
                    return false;
                }

                var type = $.trim($('#type').val());
                if (type == -1) {
                    alert("请选择跳转类别");
                    return false;
                }


                var target = $.trim($('#target').val());
                if (target.length == 0) {
                    alert("请填写链接");
                    return false;
                }

                if (target.indexOf("#") != -1) {
                    alert("请不要填写包含#的URL");
                    return false;
                }


                var picUrl = $('#input_menu_pic').val();
                if (picUrl.length == 0) {
                    alert("请上传图片");
                    return false;
                }

            });
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
            debug: false
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 推荐轮播图</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑推荐轮播图
                        <span style="color:red">
                        <c:if test="${not empty errorMsg}">
                            ${errorMsg}
                        </c:if>
                    </span></td>
                </tr>
            </table>
            <form action="/apiwiki/slide/modify" method="post" id="form_submit">
                <input type="hidden" name="id" size="48" value="${advertise.id}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            名称:
                        </td>
                        <td height="1">
                            <input id="title" type="text" name="title" size="80" value="${advertise.title}"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <input type="hidden" name="platform" value="${advertise.platform}"/>
                            <input type="radio" name="platform_hidden" value="-1" disabled/>双端
                            <input type="radio" name="platform_hidden" value="0" disabled
                                   <c:if test="${advertise.platform==0}">checked</c:if>/>IOS
                            <input type="radio" name="platform_hidden" value="1" disabled
                                   <c:if test="${advertise.platform==1}">checked</c:if>/>Android
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            上架时间:
                        </td>
                        <td height="1">
                            <input type="text" class="Wdate"
                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="starttime" value="${startTime}" id="starttime"/>
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            下架时间:
                        </td>
                        <td height="1">
                            <input type="text" class="Wdate"
                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="endtime" value="${endTime}" id="endtime"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类别:
                        </td>
                        <td height="1">
                            <select name="type" id="type">
                                <option value="-1">请选择</option>
                                <c:forEach items="${joymeWikiJt}" var="type">
                                    <option value="${type.code}"
                                            <c:if test="${advertise.type==type.code}">selected</c:if> >${type.name}</option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            链接:
                        </td>
                        <td height="1">
                            <input id="target" type="text" name="target" size="80" value="${advertise.target}"
                                   placeholder="链接过长请采用短链形式"/>*必选项
                            <span style="color:red">请不要填写包含#的URL</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            链接是否采用短链:
                        </td>
                        <td height="1">
                            是<input type="radio" name="shortlinkurl" value="1"/>
                            &nbsp;&nbsp;&nbsp;&nbsp;否<input checked="true" type="radio" name="shortlinkurl" value="0"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片:
                        </td>
                        <td>
                            <img id="menu_pic" src="${advertise.pic}" class="img_pic" width="200px" height="200px"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="pic" value="${advertise.pic}">*必填项<span
                                style="color:red">尺寸：750*300</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
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