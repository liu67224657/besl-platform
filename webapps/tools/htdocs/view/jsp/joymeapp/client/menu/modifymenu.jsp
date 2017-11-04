<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>APP菜单添加页面</title>
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
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);

            $('#input_delete_pic').click(function () {
                $('#menu_pic').attr('src', '/static/images/default.jpg');
                $('#input_menu_pic').val('');
            });

            $('#input_delete_pic2').click(function () {
                $('#menu_pic2').attr('src', '/static/images/default.jpg');
                $('#input_menu_pic2').val('');
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
        var coustomImageSettings2 = {
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

            file_dialog_complete_handler: fileDialogComplete2,
            upload_start_handler: uploadStart2,
            upload_success_handler: uploadSuccess2,
            upload_complete_handler: uploadComplete2,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button2",
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 2.0版小端菜单管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加菜单</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/client/menu/modify" method="post" id="form_submit">
                <input type="hidden" name="appkey" value="${appKey}"/>
                <input type="hidden" name="pid" value="${pid}"/>
                <input type="hidden" name="menuid" value="${joymeAppMenu.menuId}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${pid == 0}">
                            <tr>
                                <td height="1" class="default_line_td">
                                    菜单模块:
                                </td>
                                <td height="1">
                                    <select name="module" id="select_module">
                                        <c:forEach var="modu" items="${moduleSet}">
                                            <option value="${modu.code}"
                                                    <c:if test="${modu.code == module}">selected="selected"</c:if>>
                                                <fmt:message key="joymeapp.menu.module.${modu.code}" bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td height="1" class=>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td height="1" class="default_line_td">
                                    菜单模块:
                                </td>
                                <td height="1">
                                    <select name="module" id="select_module">
                                        <c:forEach var="modu" items="${moduleSet}">
                                            <c:if test="${modu.code == module}">
                                                <option value="${modu.code}">
                                                    <fmt:message key="joymeapp.menu.module.${modu.code}"
                                                                 bundle="${def}"/>
                                                </option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td height="1" class=>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td height="1" class="default_line_td">
                            菜单标题:
                        </td>
                        <td height="1">
                            <input id="input_text_menuName" type="text" name="menuname" size="32" value="${joymeAppMenu.menuName}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <c:if test="${pid > 0}">
                        <tr>
                            <td height="1" class="default_line_td">
                                菜单描述:
                            </td>
                            <td height="1">
                                <textarea cols="30" rows="5" id="textarea_desc" name="menudesc">${joymeAppMenu.menuDesc}</textarea>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类型:
                        </td>
                        <td height="1">
                            <select name="jt" id="select_jt">
                                <option value="-1">不跳转</option>
                                <c:forEach items="${jtSet}" var="jt">
                                    <option value="${jt}" <c:if test="${jt == joymeAppMenu.menuType}">selected="selected" </c:if>>
                                        <fmt:message key="joymeapp.menu.type.${jt}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转参数:
                        </td>
                        <td height="1">
                            <input id="input_text_ji" type="text" name="ji" size="32" value="${joymeAppMenu.url}"/>
                            <br/>如果是webview跳转，请填写完整的http://连接
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <c:if test="${pid > 0 && module != 9 && module != 10}">
                        <tr>
                            <td height="1" class="default_line_td">
                                图片(IOS):
                            </td>
                            <td>
                                <img id="menu_pic" src="${joymeAppMenu.pic.iosPic}"/>
                                <span id="upload_button">上传</span>
                                <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                                <input id="input_menu_pic" type="hidden" name="picios" value="">
                                <input id="input_delete_pic" type="button" value="重置"/>
                                <span style="color:red"></span>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                图片(安卓):
                            </td>
                            <td>
                                <img id="menu_pic2" src="${joymeAppMenu.pic.androidPic}"/>
                                <span id="upload_button2">上传</span>
                                <span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>
                                <input id="input_menu_pic2" type="hidden" name="picandroid" value="">
                                <input id="input_delete_pic2" type="button" value="重置"/>
                                <span style="color:red"></span>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${pid > 0}">
                        <tr>
                            <td height="1" class="default_line_td">
                                展示类型:
                            </td>
                            <td height="1">
                                <select name="displaytype" id="displayTypes">
                                    <c:forEach var="display" items="${displaySet}">
                                        <option value="${display.code}" <c:if test="${display.code == joymeAppMenu.displayType.code}">selected="selected" </c:if>>
                                            <fmt:message key="joymeapp.menu.displaytype.${display.code}" bundle="${def}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                内容分类:
                            </td>
                            <td height="1" class="">
                                <select name="contenttype">
                                    <option value="0">请选择</option>
                                    <c:forEach items="${contentSet}" var="content">
                                        <option value="${content.code}" <c:if test="${content.code == joymeAppMenu.contentType.code}">selected="selected" </c:if>>
                                            <fmt:message key="app.menu.content.type.${content.code}" bundle="${def}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                是否new:
                            </td>
                            <td height="1">
                                <select name="isnew" id="select_isnew">
                                    <option value="true" <c:if test="${joymeAppMenu.isNew()}">selected="selected" </c:if>>是</option>
                                    <OPTION value="false" <c:if test="${!joymeAppMenu.isNew()}">selected="selected" </c:if>>否</OPTION>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                是否hot:
                            </td>
                            <td height="1">
                                <select name="ishot" id="select_ishot">
                                    <option value="true" <c:if test="${joymeAppMenu.isHot()}">selected="selected" </c:if>>是</option>
                                    <OPTION value="false" <c:if test="${!joymeAppMenu.isHot()}">selected="selected" </c:if>>否</OPTION>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <c:if test="${module != 9 && module != 10}">
                            <tr>
                                <td height="1" class="default_line_td">
                                    拓展描述:
                                </td>
                                <td height="1">
                                    <textarea cols="30" rows="5" id="textarea_expdesc" name="expdesc">${joymeAppMenu.expField.expDesc}</textarea>
                                </td>
                                <td height="1" class=>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td">
                                    作者:
                                </td>
                                <td height="1">
                                    <input type="text" name="author" id="input_author" value="${joymeAppMenu.expField.author}"/>
                                </td>
                                <td height="1" class=>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td">
                                    推荐星级:
                                </td>
                                <td height="1">
                                    <input type="text" name="star" id="input_star" value="${joymeAppMenu.expField.star}"/>
                                </td>
                                <td height="1" class=>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" class="default_line_td">
                                    发布时间:
                                </td>
                                <td height="1">
                                    <input id="input_publishdate" name="publishdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true})"
                                           readonly value="${joymeAppMenu.expField.publishDate}"/>
                                </td>
                                <td height="1" class=>
                                </td>
                            </tr>
                        </c:if>
                    </c:if>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="修改">
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