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
    <script type="text/javascript" src="/static/include/swfupload/joymeapphandler.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }
    </style>
    <script>
        $().ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {
                if($("#read_num").val() != "") {
                    if(isNaN($("#read_num").val())){
                        alert("阅读数需要是数字！");
                        $("#read_num").focus();
                        return false;
                    }
                }

                if($("#agree_percent").val() != "") {
                    if(isNaN($("#agree_percent").val())){
                        alert("赞百分比需要是数字！");
                        $("#agree_percent").focus();
                        return false;
                    }
                }

                if($("#agree_num").val() != "") {
                    if(isNaN($("#agree_num").val())){
                        alert("赞数需要是数字！");
                        $("#agree_num").focus();
                        return false;
                    }
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
            debug: false}

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 标签管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">标签文章</td>
                </tr>
            </table>
            <form action="/gameclient/tag/dede/modify" method="post" id="form_submit">
                <input type="hidden" name="id" size="48" value="${tagDedearchives.id}"/>
                <input type="hidden" name="tagid" size="48" value="${tagid}"/>
                <input type="hidden" name="removestaus" size="48" value="${removestaus}"/>
                <input type="hidden" name="platform" size="48" value="${platform}"/>
                <input type="hidden" name="startRowIdx" size="48" value="${startRowIdx}"/>
                <input type="hidden" name="title" size="48" value="${title}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            文章ID:
                        </td>
                        <td height="1">
                            <input  type="text"  size="48" value="${tagDedearchives.dede_archives_id}" readonly name="dede_archives_id"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            标题:
                        </td>
                        <td height="1">
                            <input  type="text"  size="48" name="dede_archives_title" value="${tagDedearchives.dede_archives_title}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100" color="red" backg>
                            描述:
                        </td>
                        <td height="1">
                            <textarea rows="10" cols="100" name="dede_archives_description">${tagDedearchives.dede_archives_description}</textarea>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100" color="red" backg>
                            IOS是否显示:
                        </td>
                        <td height="1">
                            <c:if test="${tagDedearchives.dede_archives_showios==1}">
                                <input type="radio" name="dede_archives_showios" value="1" checked="checked">是
                                <input type="radio" name="dede_archives_showios" value="0">否
                            </c:if>
                            <c:if test="${tagDedearchives.dede_archives_showios!=1}">
                                <input type="radio" name="dede_archives_showios" value="1">是
                                <input type="radio" name="dede_archives_showios" value="0" checked="checked">否
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100" color="red" backg>
                            android是否显示:
                        </td>
                        <td height="1">
                            <c:if test="${tagDedearchives.dede_archives_showandroid==1}">
                                <input type="radio" name="dede_archives_showandroid" value="1" checked="checked">是
                                <input type="radio" name="dede_archives_showandroid" value="0">否
                            </c:if>
                            <c:if test="${tagDedearchives.dede_archives_showandroid!=1}">
                                <input type="radio" name="dede_archives_showandroid" value="1">是
                                <input type="radio" name="dede_archives_showandroid" value="0" checked="checked">否
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            图片:
                        </td>
                        <td height="1">
                            <c:if test="${tagDedearchives.tagDisplyType.code==1}">
                            <img src="${tagDedearchives.dede_archives_litpic}" alt="">
                            </c:if>
                            <c:if test="${tagDedearchives.tagDisplyType.code!=1}">
                                <img src="${tagDedearchives.dede_archives_htlistimg}" alt="">
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            图片展示的类型:
                        </td>
                        <td height="1">
                            <c:if test="${tagDedearchives.tagDisplyType.code==1}">
                                <input type="radio" name="display_type" checked="true" value="1"/>缩列图&nbsp;
                                <input type="radio" name="display_type" value="2"/>通栏&nbsp;
                                <input type="radio" name="display_type" value="4"/>通栏无标题&nbsp;
                                <input type="radio" name="display_type" value="5"/>通栏有标题&nbsp;
                            </c:if>
                            <c:if test="${tagDedearchives.tagDisplyType.code==2}">
                                <input type="radio" name="display_type"  value="1"/>缩列图&nbsp;
                                <input type="radio" name="display_type" checked="true" value="2"/>通栏&nbsp;
                                <input type="radio" name="display_type" value="4"/>通栏无标题&nbsp;
                                <input type="radio" name="display_type" value="5"/>通栏有标题&nbsp;
                            </c:if>
                            <c:if test="${tagDedearchives.tagDisplyType.code==4}">
                                <input type="radio" name="display_type"  value="1"/>缩列图&nbsp;
                                <input type="radio" name="display_type"  value="2"/>通栏&nbsp;
                                <input type="radio" name="display_type"  checked="true" value="4"/>通栏无标题&nbsp;
                                <input type="radio" name="display_type" value="5"/>通栏有标题&nbsp;
                            </c:if>

                            <c:if test="${tagDedearchives.tagDisplyType.code==5}">
                                <input type="radio" name="display_type"  value="1"/>缩列图&nbsp;
                                <input type="radio" name="display_type" value="2"/>通栏&nbsp;
                                <input type="radio" name="display_type"  value="4"/>通栏无标题&nbsp;
                                <input type="radio" name="display_type" checked="true" value="5"/>通栏有标题&nbsp;
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            阅读数:
                        </td>
                        <td height="1">
                            <input  type="text"  size="48" value="${cheat.read_num}" id="read_num" name="read_num" />
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            赞数:
                        </td>
                        <td height="1">
                            <input  type="text"  size="48" value="${cheat.agree_num}" id="agree_num" name="agree_num" />
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            赞百分比:
                        </td>
                        <td height="1">
                            <input  type="text"  size="48" value="${cheat.agree_percent}" id="agree_percent" name="agree_percent"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            发布时间:
                        </td>
                        <td height="1">
                            <input id="dede_archives_pubdate" name="dede_archives_pubdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"  value="${tagDedearchives.dede_archives_pubdate_str}"/>
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