<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
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
    <title>后台数据管理、添加新专题</title>

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

        function back() {
            window.location.href = "/viewspecial/listspecial";
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
                        url:"/viewspecial/createspecial",
                        data:"contentType=" + contentTypes,
                        success:function(data) {
                            var resultJson = eval('(' + data + ')');
                            if (resultJson.status_code == '1') {
                                alert("保存special成功！");
                            } else {
                                alert("保存special失败！");
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 多维度LINE >> 专题管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改专题</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <c:if test="${errorMsgMap['system']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['system']}" bundle="${error}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                </table>
            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/viewspecial/editspecial" method="POST">
                    <input type="hidden" name="specialId" value="${special.specialId}">
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">专题编码：</td>
                        <td class="edit_table_value_td">
                            <c:if test="${errorMsgMap['lineCspecialCodeode']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['specialCode']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="specialCode" type="text" class="default_input_singleline" id="specialCode"
                                   value="${special.specialCode}" size="32" maxlength="32" readonly="true"> * 一经创建，不能修改。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_notnulltitle_td">专题名称：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['specialName']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['specialName']}" bundle="${error}"/></span><br>
                            </c:if>
                            <input name="specialName" type="text" class="default_input_singleline" size="64"
                                   maxlength="64" value="${special.specialName}"
                                   id="specialName"> * 专题的中文名称。
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">专题类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="specialTypeCode" class="default_select_single">
                                <c:forEach items="${specialTypes}" var="specialType">
                                    <option value="${specialType.code}" <c:if test="${special.specialType.code == specialType.code}">selected="true"</c:if>>
                                        <fmt:message key="def.viewspecial.type.${specialType.code}.name" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">显示设置：</td>
                        <td nowrap class="edit_table_value_td">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="80" align="right" class="edit_table_defaulttitle_td">模板设置：</td>
                                    <td nowrap class="edit_table_value_td">
                                        <select name="templateCode" class="default_select_single">
                                            <c:forEach items="${specialTemplates}" var="specialTemplate">
                                                <option value="${specialTemplate.code}"
                                                        <c:if test="${special.displaySetting.templateCode == specialTemplate.code}">selected="true"</c:if>>
                                                        ${specialTemplate.name}
                                                </option>
                                            </c:forEach>
                                        </select> * 选定后，不能轻易改动，如需改动，注意专题元素的调整。
                                    </td>
                                </tr>
                                <tr>
                                    <td width="80" align="right" class="edit_table_defaulttitle_td">背景图片：</td>
                                    <td nowrap class="edit_table_value_td">
                                        <img id="img_game_logo" src="/static/images/default.jpg"/>
                                        <span id="uploadButton">上传</span>
                                        <input name="bgImageUrl" type="hidden" value="${picurl_b}" id="bgImageUrl">
                                    </td>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">SEO关键字：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="seoKeyWord" type="text" class="default_input_singleline" size="64"
                                   maxlength="128" value="${special.seoKeyWord}"
                                   id="seoKeyWord">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">SEO描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea rows="5" cols="100" name="seoDesc"
                                      class="default_input_singleline">${special.seoDesc}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">专题描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea rows="5" cols="100" name="specialDesc"
                                      class="default_input_singleline">${special.specialDesc}</textarea>
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
                            <input name="Reset" type="reset" class="default_button" value="返回"
                                   onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>