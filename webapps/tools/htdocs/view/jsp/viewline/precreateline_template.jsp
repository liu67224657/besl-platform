<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/resourcehandler.js"></script>
    <title>后台数据管理-编辑Line</title>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            var settings = {
                upload_url : "${urlUpload}/json/upload/resource/logo",
                post_params : {
                    "at" : "${at}",
                    "resourceDomain":"game",
                    "scale":'3:3'
                },

                // File Upload Settings
                file_size_limit : "8 MB",    // 2MB
                file_types : "*.jpg;*.png;*.gif",
                file_types_description : "请选择图片",
                file_queue_limit : 1,

                file_dialog_complete_handler : fileDialogComplete,
                upload_start_handler:  uploadStart,
                upload_progress_handler : uploadProgress,
                upload_success_handler : uploadSuccess,
                upload_complete_handler : uploadComplete,

                // Button Settings
//                button_image_url : "",
                button_placeholder_id : "uploadButton",
                button_width: 143,
                button_height: 28,
                button_text : '<span class="">上传</span>',
                button_text_style : '',
                button_text_top_padding: 0,
                button_text_left_padding: 18,
                button_window_mode: SWFUpload.WINDOW_MODE.OPAQUE,
                button_cursor: SWFUpload.CURSOR.HAND,

                // Flash Settings
                flash_url : "/static/include/swfupload/swfupload.swf",
                flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

                custom_settings : {
                    "scale":$('input[name=imageSize]:checked').val()
                },
                // Debug Settings
                debug: false}
            var swfu = new SWFUpload(settings);

            $('input[name=logoSize]').bind('click', function() {
                swfu.addPostParam("resourceDomain", $(this).val());
            });

            $('input[name=imageSize]').bind('click', function() {
                swfu.addPostParam("scale", $(this).val());
            });
        });

        function back(lineCode) {
            window.location.href = "/viewline/detailline?lineCode=" + lineCode;
        }

        function saveForm() {
            var uno = $("#uno").val();
            var contentTypes = "";

            $.each($("input[name='contentType']"), function(i, val) {
                if (val.checked) {
                    contentTypes = val.value + "," + contentTypes;
                }
            })
            $.ajax({
                        type:"POST",
                        url:"/viewline/createline",
                        data:"contentType=" + contentTypes,
                        success:function(data) {
                            var resultJson = eval('(' + data + ')');
                            if (resultJson.status_code == '1') {
                                alert("保存Line成功！");
                            } else {
                                alert("保存Line失败！");
                            }
                        }
                    }
            )
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 多维度LINE >> 分类管理 >> 添加ViewLine</td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加<fmt:message key="def.viewline.itemtype.${line.itemType.code}.name" bundle="${def}"/>Line</td>
                </tr>
            </table>
            <c:if test="${errorMsgMap['system']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['system']}" bundle="${error}"/>
                        </td>
                    </tr>
                </table>
            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/viewline/createline" method="POST">
                    <input type="hidden" name="itemTypeCode" value="${line.itemType.code}">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">Line编码：</td>
                        <td class="edit_table_value_td">
                            <c:if test="${errorMsgMap['locationCode']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['locationCode']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="locationCode" type="text" class="default_input_singleline" id="locationCode"
                                   value="${line.locationCode}" size="32" maxlength="32"> * 不能重复的英文或拼音短句，只能含有英文字母、数字或空格。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">Line名称：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['lineName']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['lineName']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="lineName" type="text" class="default_input_singleline" size="64"
                                   maxlength="64" value="${line.lineName}" id="lineName"> * Line的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">SEO关键字：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="seoKeyWord" type="text" class="default_input_singleline" size="64"
                                   maxlength="128" value="${line.seoKeyWord}"
                                   id="seoKeyWord">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">SEO描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea rows="3" cols="100" name="seoDesc"
                                      class="default_input_multiline">${line.seoDesc}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">Line类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="lineTypeCode" class="default_select_single">
                                <c:forEach items="${lineTypes}" var="lineType">
                                    <option value="${lineType.code}" <c:if test="${line.itemType.code eq lineType.code}">selected="true"</c:if>>
                                        <fmt:message key="def.viewline.itemtype.${lineType.code}.name" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select> * 一些line可以直接对外展示，一些只供其他页面嵌套使用。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">填充方式：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="autoFillTypeCode" class="default_select_single">
                                <c:forEach items="${autoFillTypes}" var="autoFillType">
                                    <option value="${autoFillType.code}" <c:if test="${line.autoFillType.code == autoFillType.code}">selected="true"</c:if>>
                                        <fmt:message key="def.viewline.autofill.type.${autoFillType.code}.name" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select> * 目前一些类型的元素并不适合自动填充，具体请跟技术沟通。
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">填充规则：</td>
                        <td nowrap class="edit_table_value_td">
                            <jsp:include page="/view/jsp/viewline/autofillrule_${line.itemType.code}.jsp"></jsp:include>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr>
                    <td width="120" align="right" class="edit_table_defaulttitle_td">页面设置：</td>
                    <td nowrap class="edit_table_value_td">
                    显示模板：<input name="templateId" type="input" class="default_input_singleline" size="24"
                    maxlength="64" value="${line.displaySetting.templateCode}"
                    id="templateId"><br>
                    背景图片：
                    <img id="img_game_logo" src="/static/images/default.jpg"/>
                    <input name="uploadButton" id="uploadButton" type="button" class="default_button" value="上传">
                    <input name="bgImageUrl" type="hidden" value="${picurl_b}" id="bgImageUrl">
                    </td>
                    </tr>
                    <tr>
                    <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">元素最小数：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="lineItemMixCount" type="input" class="default_input_singleline" size="4"
                                   maxlength="8" value="${line.itemMinCount}"
                                   id="lineItemMixCount">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">Line描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea rows="3" cols="100" name="lineDesc"
                                      class="default_input_singleline">${line.lineDesc}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="reset" class="default_button" value="重置">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>