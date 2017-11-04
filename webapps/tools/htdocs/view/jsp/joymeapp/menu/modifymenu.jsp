<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
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

            $('#input_delete_pic').click(function () {
                $('#menu_pic').attr('src', '/static/images/default.jpg');
                $('#input_menu_pic').val('');
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


        function isNotNull() {
            var menuName = $("#input_text_menuName").val();
            var menuType = $("#menuType").val();
            if (menuName == '') {
                alert("菜单名称不能为空!");
                return false;
            }
            if (menuType == '请选择') {
                alert("请选择菜单类型。");
                return false;
            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP菜单管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP菜单编辑</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/menu/modify" method="post" id="form_submit" onsubmit="return isNotNull();">
                <input type="hidden" name="menuid" value="${joymeAppMenu.menuId}"/>
                <input type="hidden" name="appkey" value="${appkey}"/>
                <input type="hidden" name="pid" value="${joymeAppMenu.parentId}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            菜单名称:
                        </td>
                        <td height="1">
                            <input id="input_text_menuName" type="text" name="menuname" size="32"
                                   value="${joymeAppMenu.menuName}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            菜单URL:
                        </td>
                        <td height="1">
                            <input id="input_text_url" type="text" name="url" size="32" value="${joymeAppMenu.url}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            图片地址:
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${fn:length(joymeAppMenu.picUrl)>0}">
                                    <img id="menu_pic" src="${joymeAppMenu.picUrl}"/></c:when>
                                <c:otherwise><img id="menu_pic" src="/static/images/default.jpg"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value="${joymeAppMenu.picUrl}">
                            <input id="input_delete_pic" type="button" value="重置"/>
                            <span style="color:red">1.6通版120*120,1.7通版(左一大图210*220,右四小图130*120,底部小图150*110)</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            菜单类型:
                        </td>
                        <td height="1">
                            <select name="menutype" id="menuType">
                                <option value="请选择">请选择</option>
                                <c:forEach var="menuType" items="${menuTypes}">
                                    <option value="${menuType}"
                                    <c:if test="${menuType==joymeAppMenu.menuType}">selected</c:if>>
                                    <fmt:message key="joymeapp.menu.type.${menuType}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>

                            <%--<input id="input_text_menutype" type="text" name="menutype" size="32" value="${joymeAppMenu.menuType}"/>--%>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            列表展示类型:
                        </td>
                        <td height="1">
                            <select name="displaytype" id="displayTypes">
                                <c:forEach var="type" items="${displayTypes}">
                                    <option value="${type.code}"
                                    <c:if test="${type.code==joymeAppMenu.displayType.code}">selected</c:if>
                                    ><fmt:message key="joymeapp.menu.displaytype.${type.code}"
                                                  bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            菜单位置:
                        </td>
                        <td height="1">
                            <select name="category" id="category">
                                <c:forEach var="category" items="${menuCategorys}">
                                    <option value="${category.code}"
                                    <c:if test="${category.code == joymeAppMenu.moduleType.code}">selected="selected"</c:if>>
                                    <fmt:message key="joymeapp.menu.category.${category.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否是new:
                        </td>
                        <td height="1">
                            <select name="isnew" id="input_text_isnew">
                                <option value="true"
                                <c:if test="${joymeAppMenu.isNew()==true}">selected</c:if>>true
                                </option>
                                <option value="false"
                                <c:if test="${joymeAppMenu.isNew()==false}">selected</c:if>>false
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否是hot:
                        </td>
                        <td height="1">
                            <select name="ishot" id="input_text_ishot">
                                <option value="true"
                                <c:if test="${joymeAppMenu.isHot()==true}">selected</c:if> >true
                                </option>
                                <option value="false"
                                <c:if test="${joymeAppMenu.isHot()==false}">selected</c:if>>
                                false
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            更新时间:
                        </td>
                        <td height="1">
                            <input id="lastmodifydate" name="lastmodifydate"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly
                                   value="<fmt:formatDate value="${joymeAppMenu.lastModifyDate}" pattern="yyyy-MM-dd
                            HH:mm:ss"/>" ></input>
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
</body>
</html>