<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>海贼迷通知管理</title>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/static/include/swfupload/joymeapphandler.js"></script>
<script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript" type="text/JavaScript">
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
    debug: false}
$(document).ready(function () {
    $('#add_version').hide();
    $('#add_channel').hide();

    $('#form_submit').bind('submit', function () {
        var msg = $('#input_sortmsg').val();
        if (msg.length == 0) {
            alert('请填写消息简述');
            return false;
        }

        var rtype = $('#select_rtype').val();
        if (rtype.length == 0) {
            alert("请选择跳转类型");
            return false;
        }

        var info = $('#input_info').val();

        var platform = $("#select_platform").val();
        if (platform.length == 0) {
            alert("请选择平台");
            return false;
        }

        var pushListType = $('#input_pushlisttype').val();
        if (pushListType == 0) {
            var version = '';
            $('input[name=check_version]:checkbox').each(function () {
                if ($(this).is(":checked")) {
                    version += ($(this).val() + "|");
                }
            });
            if (version.trim().length == 0) {
                alert("请选择版本");
                return false;
            }
            $('#input_version').val(version);

            var channel = '';
            if ($('#all_channel').prop('checked')) {
                channel = '';
            } else {
                var bool = true;
                $('input[name=check_channel]:checkbox').each(function () {
                    if ($(this).is(":checked")) {
                        channel += ($(this).val() + "|");
                    } else {
                        bool = false;
                    }
                });
                if (bool) {
                    channel = '';
                }
            }
            $('#input_channel').val(channel);

            var sendDate = $('#senddate').val();
            if (sendDate != null) {
                var sendTime = Date.parse(sendDate.replace(/-/g, "/"));
                var date = new Date().getTime();
                if ((date + 1000 * 60 * 30) > sendTime) {
                    alert("延迟时间最少30分钟");
                    return false;
                }
            }
        }
    });
});

function getVersion() {
    var pushListType = $('#input_pushlisttype').val();
    if (pushListType == 0 || pushListType == 3 || pushListType == 4) {
        var appId = $('#input_appid').val();
        var platform = $("#select_platform").find("option:selected").val();
        if (platform.length == 0) {
            alert("请选择平台");
            return false;
        }
        $.post("/joymeapp/push/getversion", {platform: platform, appid: appId, pushlisttype: 0}, function (req) {
            var resMsg = eval('(' + req + ')');
            if (resMsg.rs == 0) {
                return false;
            } else {
                if (resMsg.result == null) {
                    return;
                }
                var radioHtml = '<input type="checkbox" id="all_version" onclick="checkAllVersion($(this))"/>全选<input type="checkbox" id="inverse_version" onclick="checkInverseVersion()"/>反选<br/>';
                if (resMsg.result.versionList != null && resMsg.result.versionList.length > 0) {
                    var versionList = resMsg.result.versionList;
                    for (var i = 0; i < versionList.length; i++) {
                        radioHtml += '<input type="checkbox" name="check_version" value="' + versionList[i] + '"/>' + versionList[i] + '&nbsp;'
                        if (i % 6 == 5) {
                            radioHtml += '<br/>';
                        }
                    }
                }
                radioHtml += '<br/><span style="color: red">*必选项</span>';
                $('#td_version').html(radioHtml);

                var radioHtml = '<input type="checkbox" id="all_channel" onclick="checkAllChannel($(this))"/>全选<input type="checkbox" id="inverse_channel" onclick="checkInverseChannel()"/>反选<br/>';
                if (resMsg.result.channelList != null && resMsg.result.channelList.length > 0) {
                    var channelList = resMsg.result.channelList;
                    for (var i = 0; i < channelList.length; i++) {
                        radioHtml += '<input type="checkbox" name="check_channel" value="' + channelList[i].code + '"/>' + channelList[i].name + '&nbsp;'
                        if (i % 6 == 5) {
                            radioHtml += '<br/>';
                        }
                    }
                }
                radioHtml += '<br/><span style="color: red">*必选项，默认为全部渠道（除非需要指定特定渠道，否则一律为全部)</span>';
                $('#td_channel').html(radioHtml);
            }
        });

        if (platform == 1) {
            $('#span_title').html("*必填");
        } else if (platform == 0 || platform == 2) {
            $('#span_title').html("*可不填");
        }
    }
}

function addPush() {
    var addPush = $('#select_apppush').val();
    if (addPush == 'true') {
        $('#add_version').show();
        $('#add_channel').show();

        var appId = $('#input_appid').val();
        var platform = $("#select_platform").find("option:selected").val();
        if (platform.length == 0) {
            alert("请选择平台");
            return false;
        }
        var radioHtml = '';
        $.post("/joymeapp/push/getversion", {platform: platform, appid: appId}, function (req) {
            var resMsg = eval('(' + req + ')');
            if (resMsg.rs == '0') {
                return false;
            } else {
                var versionList = resMsg.result;
                radioHtml += '<input type="checkbox" id="all_version" onclick="checkAllVersion($(this))"/>全选<input type="checkbox" id="inverse_version" onclick="checkInverseVersion()"/>反选<br/>';
                for (var i = 0; i < versionList.length; i++) {
                    radioHtml += '<input type="checkbox" name="check_version" value="' + versionList[i] + '"/>' + versionList[i] + '&nbsp;'
                    if (i % 6 == 5) {
                        radioHtml += '<br/>';
                    }
                }
                $('#td_add_version').html(radioHtml);
            }
        });
    } else {
        $('#add_version').hide();
        $('#add_channel').hide();
    }
}

function checkAllChannel(dom) {
    if (dom.prop('checked')) {
        $('input[name=check_channel]:checkbox').each(function () {
            $(this).prop('checked', true);
        });
    } else {
        $('input[name=check_channel]:checkbox').each(function () {
            $(this).prop('checked', false);
        });
    }
}

function checkInverseChannel() {
    $('input[name=check_channel]:checkbox').each(function () {
        if ($(this).prop('checked')) {
            $(this).prop('checked', false);
        } else {
            $(this).prop('checked', true);
        }
    });
}

function checkAllVersion(dom) {
    if (dom.prop('checked')) {
        $('input[name=check_version]:checkbox').each(function () {
            $(this).prop('checked', true);
        });
    } else {
        $('input[name=check_version]:checkbox').each(function () {
            $(this).prop('checked', false);
        });
    }
}

function checkInverseVersion() {
    $('input[name=check_version]:checkbox').each(function () {
        if ($(this).prop('checked')) {
            $(this).prop('checked', false);
        } else {
            $(this).prop('checked', true);
        }
    });
}
</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫消息中心列表</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table width="30%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td"><c:if test="${pushListType == 0}">创建推送消息</c:if><c:if
                test="${pushListType == 1}">创建系统通知</c:if></td>
        <td class="">
            <form></form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form action="">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td">
        <input type="hidden" name="appid" value="${appId}" id="input_appid"/>
        <input type="hidden" name="msgid" value="${msgDTO.pushMessage.pushMsgId}" id="input_msgid"/>
        <input type="hidden" name="pushlisttype" value="${pushListType}" id="input_pushlisttype"/>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        选择平台:
    </td>
    <td height="1">
        <select name="platform" id="select_platform" onchange="getVersion()" disabled="disabled">
            <option value="">请选择</option>
            <c:choose>
                <c:when test="${pushListType ==0}">
                    <option value="0"
                            <c:if test="${msgDTO.pushMessage.appPlatform.code == 0}">selected="selected"</c:if>>
                        IOS-appstore
                    </option>
                    <option value="1"
                            <c:if test="${msgDTO.pushMessage.appPlatform.code == 1}">selected="selected"</c:if>>安卓
                    </option>
                    <option value="2"
                            <c:if test="${msgDTO.pushMessage.appPlatform.code == 2}">selected="selected"</c:if>>IOS-企业版
                    </option>
                </c:when>
                <c:when test="${pushListType ==1}">
                    <option value="0"
                            <c:if test="${msgDTO.pushMessage.appPlatform.code == 0}">selected="selected"</c:if>>IOS
                    </option>
                    <option value="1"
                            <c:if test="${msgDTO.pushMessage.appPlatform.code == 1}">selected="selected"</c:if>>安卓
                    </option>
                </c:when>
            </c:choose>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<c:if test="${pushListType == 1}">
    <tr>
        <td height="1" class="default_line_td">
            ICON:
        </td>
        <td height="1">
            <img id="img_icon" src="${msgDTO.pushMessage.msgIcon}" height="24" width="24"/>
                <%--<span id="upload_button">上传</span>--%>
                <%--<span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>--%>
                <%--<input id="msg_icon" type="hidden" name="icon">--%>
        </td>
        <td height="1">
        </td>
    </tr>
</c:if>
<tr>
    <td height="1" class="default_line_td">
        标题:
    </td>
    <td height="1">
        <input id="msg_subject" type="text" name="subject" size="64" value="${msgDTO.pushMessage.msgSubject}"
               disabled="disabled"/>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        内容简述:
    </td>
    <td height="1">
        <textarea id="input_sortmsg" name="shortmessage" cols="40"
                  rows="5" disabled="disabled">${msgDTO.pushMessage.shortMessage}</textarea>
        <%--*<span style="color: red">必填项</span>跳转链接为短链接时，为38个字。--%>
    </td>
    <td height="1">
    </td>
</tr>
<c:if test="${pushListType == 0}">
    <tr>
        <td height="1" class="default_line_td">
            角标:
        </td>
        <td height="1">
            <input id="input_badge" type="text" name="badge" value="${msgDTO.pushMessage.badge}" disabled="disabled">*应用右上角的消息数目，通常为0或1
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            声音:
        </td>
        <td height="1">
            <input type="radio" name="sound" value="default"
                   <c:if test="${msgDTO.pushMessage.sound == 'default'}">checked="checked"</c:if> disabled="disabled">系统默认&nbsp;
            <input type="radio" name="sound" value=""
                   <c:if test="${msgDTO.pushMessage.sound == ''}">checked="checked"</c:if> disabled="disabled">无&nbsp;
        </td>
        <td height="1">
        </td>
    </tr>
</c:if>
<tr>
    <td height="1" class="default_line_td">
        跳转类型:
    </td>
    <td height="1" class="edit_table_defaulttitle_td">
        <select name="rtype" id="select_rtype" disabled="disabled">
            <option value="0"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 0}">selected="selected"</c:if>>打开应用
            </option>
            <option value="1"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 1}">selected="selected"</c:if>>专题视频列表
            </option>
            <option value="2"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 2}">selected="selected"</c:if>>专题图文列表
            </option>
            <option value="3"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 3}">selected="selected"</c:if>>wap直达
            </option>
            <option value="4"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 4}">selected="selected"</c:if>>专题文字列表
            </option>
            <option value="5"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 5}">selected="selected"</c:if>>海湾首页
            </option>
            <option value="6"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 6}">selected="selected"</c:if>>追番首页
            </option>
            <option value="7"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 7}">selected="selected"</c:if>>追番详情页
            </option>
            <option value="8"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 8}">selected="selected"</c:if>>事典wap打开
            </option>
            <option value="9"
                    <c:if test="${msgDTO.pushMessage.options.list.get(0).type == 9}">selected="selected"</c:if>>消息中心小红点
            </option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        跳转链接:
    </td>
    <td height="1">
        <input id="input_info" type="text" name="info" size="64"
               value="${msgDTO.pushMessage.options.list.get(0).info}" disabled="disabled"/><a
            href="http://www.surl.sinaapp.com/"
            target="_blank">短链接工具</a>
    </td>
    <td height="1">
    </td>
</tr>
<c:if test="${pushListType == 0}">
    <tr>
        <td height="1" class="default_line_td">
            版本:
            <input type="hidden" id="input_version" name="version" value=""/>
        </td>
        <td height="1" id="td_version">
            <input type="checkbox" id="all_version" onclick="checkAllVersion($(this))" disabled="disabled"/>全选
            <input type="checkbox" id="inverse_version" onclick="checkInverseVersion()" disabled="disabled"/>反选<br/>
            <c:forEach var="version" items="${versionList}" varStatus="st">
                <input type="checkbox" name="check_version" value="${version}"
                        <c:forEach var="hasver" items="${versionSet}">
                            <c:if test="${hasver == version}">checked="checked"</c:if></c:forEach>
                       disabled="disabled"/>${version}&nbsp;
                <c:if test="${st.index % 6 ==5}"><br/></c:if>
            </c:forEach>
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            渠道:
            <input type="hidden" id="input_channel" name="channel" value=""/>
        </td>
        <td height="1">
            <input type="checkbox" id="all_channel" onclick="checkAllChannel($(this))" disabled="disabled"/>全选
            <input type="checkbox" id="inverse_channel" onclick="checkInverseChannel()" disabled="disabled"/>反选<br/>
            <c:forEach var="channel" items="${channelList}" varStatus="st">
                <input type="checkbox" name="check_channel" value="${channel.code}"
                        <c:forEach var="haschannel" items="${channelSet}">
                            <c:if test="${haschannel.code == channel.code}">checked="checked"</c:if></c:forEach>/>${channel.name}&nbsp;
                <c:if test="${st.index % 6 ==5}"><br/></c:if>
            </c:forEach>
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            指定用户昵称：
        </td>
        <td height="1">
            <textarea id="textarea_nemas" name="screennames" cols="60" rows="10"
                      disabled="disabled">${screenNames}</textarea>
            *多个用户昵称之间用空格" "隔开
        </td>
        <td height="1">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            延迟推送时间:
        </td>
        <td height="1">
            <input id="senddate" name="senddate"
                   value='<fmt:formatDate value="${msgDTO.pushMessage.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>'
                   disabled="disabled"/>
        </td>
        <td height="1">
        </td>
    </tr>
</c:if>
<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
</tr>
<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
</tr>
<tr align="center">
    <td colspan="3">
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