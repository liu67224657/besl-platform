<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/resourcehandler.js"></script>
    <title>后台数据管理、添加游戏条目</title>
    <script language="JavaScript" type="text/JavaScript">

        $().ready(function () {
            var swfu = new SWFUpload(mainImageSettings);
            var coustomSwfu = new SWFUpload(coustomImageSettings);

            $('input[name=logoSize]').bind('click', function () {
                swfu.addPostParam("logoSize", $(this).val());
            });

            $('input[name=imageSize]').bind('click', function () {
                swfu.addPostParam("scale", $(this).val());
            });
        });
        var mainImageSettings = {
            upload_url: "${urlUpload}/json/upload/resource/logo",
            post_params: {
                "at": "${at}",
                "resourceDomain": "game",
                "scale": '3:4'
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadStart,
            upload_progress_handler: uploadProgress,
            upload_success_handler: uploadSuccess,
            upload_complete_handler: uploadComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "uploadButton",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {
                "scale": $('input[name=imageSize]:checked').val()
            },
            // Debug Settings
            debug: false}
        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadThumbimgStart,
            upload_progress_handler: uploadProgress,
            upload_success_handler: uploadThumimgSuccess,
            upload_complete_handler: uploadThumbimgComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "thumbimg_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false}

        function checkForm() {
            var resourceName = document.form1.resourceName.value;
            var picurl_s = document.form1.picurl_s.value;
            var synonyms = document.form1.synonyms.value;
            var resourceDesc = document.form1.resourceDesc.value;
            var gameCode = document.form1.gameCode.value;

            if (resourceName.length == 0 || resourceName == '') {
                alert('游戏名称不能为空');
                return false;
            }
            if (picurl_s.length == 0 || picurl_s == '') {
                alert('游戏主图不能为空');
                return false;
            }

            if (gameCode.length == 0 || gameCode == '') {
                alert('游戏编码不能为空');
                return false;
            }

//            if (synonyms.length != 0 || synonyms != '') {
//                checkSynonyms();
//            }
            document.form1.submit();
            return true;
        }
        function back() {
            window.location.href = "/gameresource/gameresourcelist";
        }
        function checkName() {
            var resourceName = $("#resourceName").val();
            var resourceDomain = $('input[name="resourceDomain"]:checked').val();
            var resourceId = $("#resourceId").val();

            var data = "resourceName=" + resourceName + "&resourceDomain=" + resourceDomain;
            if (resourceName == '') {
                alert("请输入游戏条目主名称！");
                return false;
            } else if (typeof(resourceId) != 'undefined') {
                data += "&resourceId=" + resourceId;
            }
            $.ajax({
                type: "POST",
                url: "/gameresource/checkname",
                data: data,
                success: function (data) {
                    var resultJson = eval('(' + data + ')');
                    if (resultJson.status_code == '1') {
                        alert("主名称已经存在[或可能是其他游戏条目同义词]，请重新输入！");
                    } else {
                        alert("该游戏条目主名称不存在,可以使用！");
                    }
                }
            });
        }
        function checkSynonyms(resourceId) {
            var synonyms = $("#synonyms").val();
            $.ajax({
                type: "POST",
                url: "/gameresource/checksynonyms",
                data: "synonyms=" + synonyms + "&resourceId=" + resourceId,
                success: function (data) {
                    var resultJson = eval('(' + data + ')');
                    if (resultJson.status_code == '1') {
                        if (resultJson.result[0].length > 0) {
                            alert(synonyms + (" 已经是 ") + resultJson.result[1] + " 的同义词!");
                        } else {
                            alert(synonyms + " 已经是一个条目名称！");
                        }
                    } else {
                        alert("该同义词不存在,可以使用！");
                    }
                }
            });
        }

    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> <a href="/gameresource/gameresourcelist">条目维护</a> >> 新建游戏条目
    </td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">新建游戏条目</td>
        <td align="left">
        </td>
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
<c:if test="${errorMsgMap['gamecode']!=null}">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" class="default_line_td"></td>
        </tr>
        <tr>
            <td class="error_msg_td">
                <fmt:message key="${errorMsgMap['gamecode']}" bundle="${error}"/>
            </td>
        </tr>
    </table>
</c:if>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<form name="form1" action="/gameresource/creategameresource" method="POST">
<tr>
    <td height="1" colspan="4" class="default_line_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏条目主名称：</td>
    <td class="edit_table_value_td">
        <c:if test="${errorMsgMap['resourceName']!=null}">
            <span class="error_msg_td"><fmt:message key="${errorMsgMap['resourceName']}" bundle="${error}"/></span><br>
        </c:if>
        <c:if test="${errorMsgMap['resourceNameNull']!=null}">
            <span class="error_msg_td"><fmt:message key="${errorMsgMap['resourceNameNull']}"
                                                    bundle="${error}"/></span><br>
        </c:if>
        <input name="resourceName" type="text" class="default_input_singleline" id="resourceName"
               value="${entity.resourceName}"
               size="32" maxlength="32">
        <input type="button" class="default_button" onclick="checkName()" value="检查此名称是否存在"/>
        <font color="red">*</font>
    </td>

    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏编码：</td>
    <td class="edit_table_value_td">
        <input name="gameCode" type="text" class="default_input_singleline" id="gameCode"
               value="${entity.gameCode}"
               size="32" maxlength="32">
        <font color="red">*此处为必填项，游戏编码不能重复</font>
    </td>

    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td" nowrap>主图类别：</td>
    <td class="edit_table_value_td">
        <input type="radio" name="resourceDomain" value="game" checked="true">
        普通游戏
        </input>
        <input type="radio" name="resourceDomain" value="ios">
        app游戏
        </input>
    </td>
    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">尺寸：</td>
    <td class="edit_table_value_td">
        <input type="radio" name="logoSize" value="3:4"
               checked="true"/>
        3:4
        <input type="radio" name="logoSize" value="4:3"/>
        4:3
        <input type="radio" name="logoSize" value="3:3"/>
        1:1
    </td>
    </td>
    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏平台：</td>
    <td nowrap class="edit_table_value_td"><c:forEach var="deviceMap" items="${deviceMap}" varStatus="st">
        <span style="width:100px">
            <input name="device" type="checkbox" maxlength="64" value="${deviceMap.value.code}" id="device" <c:if
                test="${entity.deviceSet.isChecked(deviceMap.value.code)}">checked</c:if>>
            ${deviceMap.value.code}
        </span>
        <c:if test="${(st.index + 1) % 6 == 0}"><br/></c:if>
    </c:forEach>
    </td>
    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">条目主图：</td>
    <td nowrap class="edit_table_value_td">
        <c:if test="${errorMsgMap['icon']!=null}">
            <span class="error_msg_td"><fmt:message key="${errorMsgMap['icon']}" bundle="${error}"/></span><br>
        </c:if>
        <img id="img_game_logo" src="/static/images/default.jpg"/>
        <span id="uploadButton">上传</span>
        <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
        <font color="red">*</font>
        <input name="picurl_s" type="hidden" value="${picurl_s}" id="picurl_s">
        <input name="picurl_m" type="hidden" value="${picurl_m}" id="picurl_m">
        <input name="picurl_b" type="hidden" value="${picurl_b}" id="picurl_b">
        <input name="picurl_ss" type="hidden" value="${picurl_ss}" id="picurl_ss">

        <input name="picurl_ll" type="hidden" value="${picurl_ll}" id="picurl_ll">
        <input name="picurl_sl" type="hidden" value="${picurl_sl}" id="picurl_sl">
    </td>
    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">条目附图：</td>
    <td nowrap class="edit_table_value_td">
        <c:if test="${errorMsgMap['thumimg']!=null}">
            <span class="error_msg_td"><fmt:message key="${errorMsgMap['thumimg']}" bundle="${error}"/></span><br>
        </c:if>
        <img id="img_game_thumimg" src="/static/images/default.jpg"/>
        <span id="thumbimg_button">上传</span>
        <span id="loading_thumimg" style="display:none"><img src="/static/images/loading.gif"/></span>
        <font color="red">*</font>
        <input name="thumbimg" type="hidden" value="${thumbimg}" id="thumbimg_url">
    </td>
    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏类型：</td>
    <td nowrap class="edit_table_value_td">
        <c:forEach var="categoryMap" items="${categoryMap}" varStatus="st">
            <span style="width:100px">
                <input name="resourceCategory" type="checkbox" value="${categoryMap.value.code}"
                       id="resourceCategory" <c:if test="${entity.categorySet.isChecked(categoryMap.value.code)}">checked</c:if>>
                    ${categoryMap.value.code}
            </span>
            <c:if test="${(st.index + 1) % 6 == 0}"></br></c:if>
        </c:forEach>
    </td>
    <td nowrap class="edit_table_value_td"></td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">SEO关键字：</td>
    <td class="edit_table_value_td">
        <input name="seoKeyWords" type="text" class="default_input_singleline"
               id="seoKeyWords" value="${entity.seoKeyWords}" size="100" maxlength="500">
    </td>
    <td nowrap class="edit_table_value_td">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">SEO描述：</td>
    <td class="edit_table_value_td">
        <textarea rows="5" cols="100" name="seoDescription"
                  class="default_input_multiline">${entity.seoDescription}</textarea>
    </td>
    <td nowrap class="edit_table_value_td">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏风格：</td>
    <td nowrap class="edit_table_value_td">
        <c:forEach var="styleMap" items="${styleMap}" varStatus="st">
            <span style="width:100px">
                <input name="resourceStyle" type="checkbox" maxlength="64" id="resourceStyle"
                       value="${styleMap.value.code}" <c:if test="${entity.styleSet.isChecked(styleMap.value.code)}">checked="true"</c:if>>
                    ${styleMap.value.code}
            </span>
            <c:if test="${(st.index + 1) % 6 == 0}"></br></c:if>
        </c:forEach>
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="resourceStyleValue" type="checkbox" size="24" id="resourceStyleValue">隐藏风格
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">发行商/运营商：</td>
    <td nowrap class="edit_table_value_td">
        <input name="publishCompany" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.publishCompany}" id="publishCompany">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="publishCompanyValue" type="checkbox" size="24"
               id="publishCompanyValue"> 隐藏发行商/运营商
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">开发商：</td>
    <td nowrap class="edit_table_value_td">
        <input name="develop" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.develop}" id="develop">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="developValue" type="checkbox" size="24"
               id="developValue"> 隐藏开发商
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏人数：</td>
    <td nowrap class="edit_table_value_td">
        <input name="playerNumber" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.playerNumber}" id="playerNumber">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="playerNumberValue" type="checkbox" size="24"
               id="playerNumberValue">隐藏游戏人数
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏大小：</td>
    <td nowrap class="edit_table_value_td">
        <input name="fileSize" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.fileSize}" id="fileSize">
        <select name="fileType" id="fileType">
            <option value="KB"
            <c:if test="${fileType == null || fileType =='KB' }">selected="true"</c:if>>KB</option>
            <option value="MB"
            <c:if test="${fileType =='MB' }">selected="true"</c:if>>MB</option>
            <option value="GB"
            <c:if test="${fileType =='GB' }">selected="true"</c:if>>GB</option>
        </select>
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="fileSizeValue" type="checkbox" size="24"
               id="fileSizeValue"> 隐藏游戏大小
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">发售价格：</td>
    <td nowrap class="edit_table_value_td">
        <input name="price" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.price}" id="price">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="priceValue" type="checkbox" size="24" id="priceValue"> 隐藏发售价格
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">发售日期：</td>
    <td nowrap class="edit_table_value_td">
        <input name="publishDate" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.publishDate}" id="publishDate">
        <font color="red">《时间格式为：yyyy-MM-dd》</font>
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="publishDateValue" type="checkbox" size="24" id="publishDateValue"> 隐藏发售日期
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">更新日期：</td>
    <td nowrap class="edit_table_value_td">
        <input name="lastUpdateDate" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="" id="lastUpdateDate">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="lastUpdateDateValue" type="checkbox" size="24" id="lastUpdateDateValue"> 隐藏更新日期
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">官网：</td>
    <td nowrap class="edit_table_value_td">
        <input name="resourceUrl" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.resourceUrl}" id="resourceUrl">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="resourceUrlValue" type="checkbox" size="24" id="resourceUrlValue">隐藏官网
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">获取渠道1：</td>
    <td nowrap class="edit_table_value_td" colspan="2">
        类型：<select name="channeltype1">
        <option value="">请选择</option>
        <c:forEach var="channel" items="${gameChannelList}">
            <option value="${channel.code}"><fmt:message key="game.channel.${channel.code}.code" bundle="${userProps}"/>
            </option>
        </c:forEach>
    </select>&nbsp;&nbsp;
        地址：<input name="channellink1" type="text" class="default_input_singleline" size="100" value="">
        价格：<input name="channelprice1" type="text" class="default_input_singleline" size="24" value="">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">获取渠道2：</td>
    <td nowrap class="edit_table_value_td" colspan="2">
        类型：<select name="channeltype2">
        <option value="">请选择</option>
        <c:forEach var="channel" items="${gameChannelList}">
            <option value="${channel.code}"><fmt:message key="game.channel.${channel.code}.code" bundle="${userProps}"/>
            </option>
        </c:forEach>
    </select>&nbsp;&nbsp;
        地址：<input name="channellink2" type="text" class="default_input_singleline" size="100" value="">
        价格：<input name="channelprice2" type="text" class="default_input_singleline" size="24" value="">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">获取渠道3：</td>
    <td nowrap class="edit_table_value_td" colspan="2">
        类型：<select name="channeltype3">
        <option value="">请选择</option>
        <c:forEach var="channel" items="${gameChannelList}">
            <option value="${channel.code}"><fmt:message key="game.channel.${channel.code}.code" bundle="${userProps}"/>
            </option>
        </c:forEach>
    </select>&nbsp;&nbsp;
        地址：<input name="channellink3" type="text" class="default_input_singleline" size="100" value="">
        价格：<input name="channelprice3" type="text" class="default_input_singleline" size="24" value="">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">获取渠道4：</td>
    <td nowrap class="edit_table_value_td" colspan="2">
        类型：<select name="channeltype4">
        <option value="">请选择</option>
        <c:forEach var="channel" items="${gameChannelList}">
            <option value="${channel.code}"><fmt:message key="game.channel.${channel.code}.code" bundle="${userProps}"/>
            </option>
        </c:forEach>
    </select>&nbsp;&nbsp;
        地址：<input name="channellink4" type="text" class="default_input_singleline" size="100" value="">
        价格：<input name="channelprice4" type="text" class="default_input_singleline" size="24" value="">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">获取渠道5：</td>
    <td nowrap class="edit_table_value_td" colspan="2">
        类型：<select name="channeltype5">
        <option value="">请选择</option>
        <c:forEach var="channel" items="${gameChannelList}">
            <option value="${channel.code}"><fmt:message key="game.channel.${channel.code}.code" bundle="${userProps}"/>
            </option>
        </c:forEach>
    </select>&nbsp;&nbsp;
        地址：<input name="channellink5" type="text" class="default_input_singleline" size="100" value="">
        价格：<input name="channelprice5" type="text" class="default_input_singleline" size="24" value="">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">语言：</td>
    <td nowrap class="edit_table_value_td">
        <input name="language" type="text" class="default_input_singleline" size="24"
               maxlength="64" value="${entity.language}" id="language">
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="languageValue" type="checkbox" size="24" id="languageValue"> 隐藏语言
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">同义词：</td>
    <td nowrap class="edit_table_value_td">
        <c:if test="${errorMsgMap['synonyms']!=null}">
            <span class="error_msg_td"><fmt:message key="${errorMsgMap['synonyms']}" bundle="${error}"/></span><br>
        </c:if>
        <input name="synonyms" type="text" class="default_input_singleline" size="64"
               maxlength="64" value="${entity.synonyms}" id="synonyms">
        <input type="button" class="default_button" onclick="checkSynonyms('0')" value="检查此同义词是否存在"/>
    </td>
    <td nowrap class="edit_table_value_td">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏简介：</td>
    <td nowrap class="edit_table_value_td">
        <textarea rows="5" cols="100" name="resourceDesc"
                  class="default_input_multiline">${entity.resourceDesc}</textarea>
    </td>
    <td nowrap class="edit_table_value_td">
        <input name="resourceDescValue" type="checkbox" size="24" id="resourceDescValue">隐藏游戏简介
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">更新信息：</td>
    <td nowrap class="edit_table_value_td">
        <textarea rows="5" cols="100" name="updateInfo" class="default_input_multiline">${updateInfo}</textarea>
    </td>
    <td nowrap class="edit_table_value_td">

    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">游戏活动：</td>
    <td nowrap class="edit_table_value_td">
        <textarea rows="5" cols="100" name="eventdesc" class="default_input_multiline">${entity.eventDesc}</textarea>
    </td>
    <td nowrap class="edit_table_value_td">
    </td>
</tr>
<tr>
    <td width="120" align="right" class="edit_table_defaulttitle_td">媒体评分：</td>
    <td nowrap class="edit_table_value_td">
        <table>
            <tr>
                <td>分数：<input type='text' class="default_input_singleline" name='score1'
                              value="${entity.gameMediaScoreSet.mediaScores[0].score}"></td>
                <td>来自媒体：<input type='text' class="default_input_singleline" name='from1'
                                value="${entity.gameMediaScoreSet.mediaScores[0].from}"></td>
                <td>评分描述：<input type='text' class="default_input_singleline" name='desc1'
                                value="${entity.gameMediaScoreSet.mediaScores[0].description}"></td>
            </tr>
            <tr>
                <td>分数：<input type='text' class="default_input_singleline" name='score2'
                              value="${entity.gameMediaScoreSet.mediaScores[1].score}"></td>
                <td>来自媒体：<input type='text' class="default_input_singleline" name='from2'
                                value="${entity.gameMediaScoreSet.mediaScores[1].from}"></td>
                <td>评分描述：<input type='text' class="default_input_singleline" name='desc2'
                                value="${entity.gameMediaScoreSet.mediaScores[1].description}"></td>
            </tr>
        </table>
    </td>
    <td nowrap class="edit_table_value_td">
        <span class="error_msg_td">*最多可选两项</span>
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
        <p:privilege name="/gameresource/creategameresource">
            <input name="Submit" type="button" class="default_button" value="提交"
                   onclick="javascript:return checkForm();">
            <input name="Reset" type="reset" class="default_button" value="重置">
        </p:privilege>
    </td>
</tr>
</form>
</table>
</td>
</tr>
</table>
</body>
<script language="JavaScript" type="text/JavaScript">

    var idx = ${jsidx};

    function addInputArea() {
        var tab = document.getElementById("tb");
        var row = tab.insertRow(-1);
        row.id = "row" + idx;

        var cell1 = row.insertCell(-1);
        var cell2 = row.insertCell(-1);
        var cell3 = row.insertCell(-1);
        var cell4 = row.insertCell(-1);

        cell1.innerHTML = "描述：<input type='text' class=\"default_input_singleline\" name='advertisement' size='35'>";
        cell2.innerHTML = "url：<input type='text' class=\"default_input_singleline\" name='url' size='35'>";
        cell3.innerHTML = "类型： <input type='checkbox' name=\"gameResourceTypes\" value=\"new.jpg_" + idx + "\">最新 <input type='checkbox' name=\"gameResourceTypes\" value=\"hot.jpg_" + idx + "\">热门 <input type='checkbox' name=\"gameResourceTypes\" value=\"ess.gif_" + idx + "\">精华"
        cell4.innerHTML = "<input type='button' onclick='del(" + idx + ")' value='删除' class='default_button'>";
        idx++;

    }
    function del(num) {

        var row = document.getElementById("row" + num);
        var tab = document.getElementById("tb");
        tab.deleteRow(row.rowIndex);

    }
</script>
</html>