<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>彩票管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/sharehandler.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        function checkticket() {
            var awardName = $("#input_text_name").val();
            var awardDesc = $("#textarea_desc").val();
            var awardPic = $("#input_hid_awardpic").val();
            var awardLevel = $("#awardlevel").val();
            var awardCount = $("#awardcount").val();
            var currentCount = $("#currentcount").val();
            if (awardName == '') {
                alert("请填写奖品名称");
                return false;
            }
            if (awardDesc == '') {
                alert("请填写奖品描述");
                return false;
            }
            if (awardPic == '') {
                alert("请选择一张图片");
                return false;

            }
            if (awardLevel == '请选择') {
                alert('请选择奖品等级');
                return false;
            }
            if (awardCount == '') {
                alert('请填写奖品数量');
                return false;
            }
            if (currentCount == '') {
                alert("请填写剩余数量");
                return false;
            }
            if (currentCount > awardCount) {
                alert("剩余数量不能大于奖品总数");
                return false;
            }

        }

        $().ready(function () {
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
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 修改彩票奖品</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改彩票奖品</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/ticket/award/modify" method="post" id="form_submit"
                  onsubmit="return checkticket()">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">

                        </td>
                        <td height="1" class="" width="10">
                            <input type="hidden" value="${ticketAward.ticketId}" name="ticketId" size="20"
                                   id="input_text_ticketId"/>
                            <input type="hidden" value="${ticketAward.ticketAwardId}" name="ticketAwardId" size="20"
                                   id="input_text_ticketAwardId"/>
                        </td>

                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖品名称:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" value="${ticketAward.awardName}" name="awardName" size="20"
                                   id="input_text_name"/>
                        </td>

                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            奖品描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="awardDesc" id="textarea_desc" rows="5" cols="100"
                                      style="width: 100%;float: left"
                                      class="default_input_multiline">${ticketAward.awardDesc}</textarea>
                        </td>

                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖品图片：
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${fn:length(ticketAward.awardPic)>0}">
                                    <img id="img_pic" height="100" width="100" src="${ticketAward.awardPic}"/>
                                </c:when>
                                <c:otherwise>
                                    <img id="img_pic" height="100" width="100" src="/static/images/default.jpg"/>
                                </c:otherwise>

                            </c:choose>

                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_hid_awardpic" type="hidden" value="${ticketAward.awardPic}"
                                   name="awardPic">
                        </td>

                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖品等级：
                        </td>
                        <td height="1" class="" width="10">
                            <select name="awardLevel" id="awardlevel">
                                <option value="请选择">
                                    请选择
                                </option>
                                <c:forEach items="${awardLevel}" var="al">

                                    <option value="${al}"
                                    <c:if test="${ticketAward.awardLevel==al}">selected</c:if> ><fmt:message
                                        key="lottery.award.level.${al}"
                                        bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            奖品总数：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" value="${ticketAward.awardCount}"
                                   onkeyup="value = value.replace(/\D|^0/g, '');" name="awardCount"
                                   id="awardcount"/>

                        </td>

                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            剩余数量：
                        </td>
                        <td height="1" class="" width="10">

                            <input type="text" value="${ticketAward.currentCount}"
                                   onkeyup="value = value.replace(/\D|^0/g, '');" id="currentcount"
                                   name="currentCount"/>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(levelExist)>0}">
                                <span style="color: red" id="span_level_exist"><fmt:message key="${levelExist}"
                                                                                            bundle="${def}"/></span>
                            </c:if>
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