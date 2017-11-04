<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>Simple jsp page</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/goodshandler.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function(){
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {
                var goodsId = $('#goodsid').val();
                if(goodsId == ''){
                    alert("填写商品ID");
                    return false;
                }
                var startTime = $('#starttime').val();
                if(startTime == ''){
                    alert("填写开始时间");
                    return false;
                }
                var endTime = $('#endtime').val();
                if(endTime == ''){
                    alert("填写结束时间");
                    return false;
                }
                var total = $('#total').val();
                if(total == ''){
                    alert("填写秒杀库存");
                    return false;
                }
                var beforetips = $('#beforetips').val();
                if(beforetips == ''){
                    alert("填写开始提示");
                    return false;
                }
                var intips = $('#intips').val();
                if(intips == ''){
                    alert("填写进行中提示");
                    return false;
                }
                var aftertips = $('#aftertips').val();
                if(aftertips == ''){
                    alert("填写结束提示");
                    return false;
                }
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
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 秒杀批次管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加游戏</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                            <c:if test="${restsum != null}">：${restsum}</c:if>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/point/goodsseckill/create" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td>
                            <input type="hidden" name="qtitle" value="${qTitle}"/>
                            <input type="hidden" name="qgid" value="${qGid}"/>
                            <input type="hidden" name="qgat" value="${qGat}"/>
                            <input type="hidden" name="qstart" value="${qStart}"/>
                            <input type="hidden" name="qend" value="${qEnd}"/>
                            <input type="hidden" name="qstatus" value="${qStatus}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            商品ID：
                        </td>
                        <td height="1" class="">
                            <input id="goodsid" type="text" name="goodsid" value="${goodsId}">
                            <span style="color: red;">*必填</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题：
                        </td>
                        <td height="1" class="">
                            <input id="title" type="text" name="title" value="${title}">
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            简介：
                        </td>
                        <td height="1" class="">
                            <textarea id="desc" name="desc" rows="3" cols="20">${desc}</textarea>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            图片 ：
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${fn:length(pic)>0}">
                                    <img id="img_pic" src="${pic}"/>
                                </c:when>
                                <c:otherwise>
                                    <img id="img_pic" src="/static/images/default.jpg"/>
                                </c:otherwise>
                            </c:choose>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_hid_goodsPic" type="hidden" name="pic" value="${pic}">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            开始时间：
                        </td>
                        <td height="1" class="">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="starttime" id="starttime"
                                   value="${startTime}"/>
                            <span style="color: red;">*必填</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            结束时间：
                        </td>
                        <td height="1" class="">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="endtime" id="endtime"
                                   value="${endTime}"/>
                            <span style="color: red;">*必填</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            秒杀库存总数：
                        </td>
                        <td height="1" class="">
                            <input id="total" type="text" name="total" value="${total}">
                            <span style="color: red;">*必填</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            提示：
                        </td>
                        <td height="1" class="">
                            开始前：<input id="beforetips" type="text" name="beforetips" value="${beforeTips}"><span style="color: red;">*必填</span><br/>
                            进行中：<input id="intips" type="text" name="intips" value="${inTips}"><span style="color: red;">*必填</span><br/>
                            结束后：<input id="aftertips" type="text" name="aftertips" value="${afterTips}"><span style="color: red;">*必填</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
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