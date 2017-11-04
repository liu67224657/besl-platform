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
<script type="text/javascript" src="/static/include/js/jquery.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>


<script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
<script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>

<script>

var uploaddomain = '${urlUpload}';
var at = '${at}';
var editor;
KindEditor.ready(function (K) {
    editor = K.create('textarea[name="content"]', {
        resizeType: 1,
        allowPreviewEmoticons: false,
        allowImageUpload: true,
        uploadJson: '/json/upload/image',
        filePostName: 'Filedata',
        extraParams: {'filetype': 'original', 'at': at},
        items: [
            'bold', 'italic', 'underline', 'image'],
        afterChange: function () {
            //  $('.word_count1').html(this.count()); //字数统计包含HTML代码
            $('.word_count2').html(this.count('text'));  //字数统计包含纯文本、IMG、EMBED，不包含换行符，IMG和EMBED算一个文字
            //////////
            //限制字数
            var limitNum = 5000;  //设定限制字数
            var pattern = '还可以输入' + limitNum + '字';
            $('.word_surplus').html(pattern); //输入显示
            if (this.count('text') > limitNum) {
                pattern = ('字数超过限制，请适当删除部分内容');
                //超过字数限制自动截取

            } else {
                //计算剩余字数
                var result = limitNum - this.count('text');
                pattern = '还可以输入' + result + '字';
            }
            $('#word_surplus').html(pattern); //输入显示
        }
    });
});
$().ready(function () {
    var coustomSwfu = new SWFUpload(coustomImageSettings);
    var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
    var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
    $('#form_submit').bind('submit', function () {
        var subject = $("#activitySubject").val();
        var pic = $("#input_menu_pic").val();
        var desc = $("#desc").val();
        var starttime = $("#starttime").val();

        var jsonDesc = $("#word_surplus").text();
        var endtime = $("#endtime").val();
        var gameName = $("#gameName").val();
        var pic2 = $("#input_menu_pic2").val();
        var developer = $("#developer").val();
        var gameUrl = $("#gameUrl").val();
        var iosUrl = $("#iosUrl").val();
        var iosSize = $("#iosSize").val();
        var androidUrl = $("#androidUrl").val();
        var androidSize = $("#androidSize").val();
        if (subject == '') {
            alert('请输入标题');
            return false;
        }
        if (pic == '') {
            alert('请上传活动大图');
            return false;
        }
        if (jsonDesc == '字数超过限制，请适当删除部分内容') {
            alert('字数超过限制，请适当删除部分内容');
            return false;
        }
        $("#jsonDesc").val(editor.html());
        if (starttime == '') {
            alert('请选择开始时间');
            return false;
        }
        if (endtime == '') {
            alert('请选择结束时间');
            return false;
        }
        if (gameName == '') {
            alert("请填写游戏名称");
            return false;
        }
        var caregory = Number(0);
        $("input[name='radio']").each(function () {
            if ($(this).attr("checked") == true) {
                caregory += parseInt($(this).val());
            }
        });
        $("#caregory").val(caregory);
        var num = $("input[name='radio']:checked").length;
        if (num > 3) {
            alert("最多只能选择3个");
            return false;
        }

        var platform = Number(0);
        $("input[name = pbox]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                platform += Number($(this).attr("value"));
            }
        });
        $('#input_hidden_platform').val(platform);

        var cooperation = Number(0);
        $("input[name = cbox]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                cooperation += Number($(this).attr("value"));
            }
        });
        $('#input_hidden_cooperation').val(cooperation);

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
    debug: false}
var coustomImageSettings3 = {
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

    file_dialog_complete_handler: fileDialogComplete3,
    upload_start_handler: uploadStart3,
    upload_success_handler: uploadSuccess3,
    upload_complete_handler: uploadComplete3,

    // Button Settings
    button_image_url: "/static/images/uploadbutton.png",
    button_placeholder_id: "upload_button3",
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
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 活动管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td"><span
                style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">修改活动</span>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form action="/activity/exchange/modify" method="post" id="form_submit">
<table width="90%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" class="">
            <c:if test="${errorMessage!=null}">
                <span style="color:red"><fmt:message key="${errorMessage}" bundle="${def}"/></span>
            </c:if>
        </td>
    </tr>
    <tr>
        <td height="1" class="">
            <input type="hidden" name="activityId" value="${activity.activityId}"/>
            <input type="hidden" name="code" value="${activity.qrUrl}"/>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动标题
        </td>
        <td height="1" class="" width="10">
            <input type="text" name="activitySubject" size="20" value="${activity.activitySubject}"
                   maxlength="30" id="activitySubject"/> *必填项，最好不要超过10个字
        </td>
        <td height="1" align="left"></td>
    </tr>
    <tr>
        <td>
            <c:if test="${fn:length(nameExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${nameExist}"
                                                                                           bundle="${def}"/></span>
            </c:if>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动主图:
        </td>
        <td height="1" class="">
            <c:choose>
                <c:when test="${fn:length(activity.activityPicUrl)>0}">
                    <img id="menu_pic" src="${activity.activityPicUrl}" with="320" height="160"/></c:when>
                <c:otherwise><img id="menu_pic" src="/static/images/default.jpg"/></c:otherwise>
            </c:choose>
            <span id="upload_button">上传</span>
            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic" type="hidden" name="picurl1" value="${activity.activityPicUrl}">
            *必填项，图片大小96X96
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动详情页背景图:
        </td>
        <td height="1" class="">
            <c:choose>
                <c:when test="${not empty activity.bgPic}">
                    <img id="menu_pic3" src="${activity.bgPic}" with="320" height="160"/>
                </c:when>
                <c:otherwise><img id="menu_pic3" src="/static/images/default.jpg"/>
                </c:otherwise>
            </c:choose>
            <span id="upload_button3">上传</span>
            <span id="loading3" style="display:none"><img src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic3" type="hidden" name="picurl3" value="${activity.bgPic}">
            *必填项。
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动简介:
        </td>
        <td height="1">
            <textarea name="content" id="editor_id">
                ${activity.activityDesc}
                <%--<c:forEach items="${activity.textJsonItemsList}"--%>
                <%--var="a"><c:if--%>
                <%--test="${a.type==1}"><p>${a.item}</p></c:if><c:if--%>
                <%--test="${a.type==2}"><p><img src="${a.item}"/></p></c:if></c:forEach>--%>
            </textarea>
            <input type="hidden" id="jsonDesc" name="jsonDesc"/>

            <p>
                <!--您当前输入了 <span class="word_count1">0</span> 个文字。（字数统计包含HTML代码。）<br/>  -->
                您当前输入了 <span class="word_count2">0</span>
                个文字。（字数统计包含纯文本、IMG、EMBED，包含换行符，IMG和EMBED算一个文字。）如果有部分功能问题，请换一个浏览器<br>
                <span id="word_surplus"></span>
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            积分兑换的小标题描述:
        </td>
        <td height="1">
            <textarea name="subdesc" id="subdesc" cols="45" rows="8">${activity.subDesc}</textarea>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            活动开始时间:
        </td>
        <td height="1">
            <input type="text" class="Wdate"
                   onClick="WdatePicker({autoPickDate:true})"
                   readonly="readonly" name="starttime" id="starttime" value="${activity.startTime}"/>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动结束时间:
        </td>
        <td height="1" class="">
            <input type="text" class="Wdate"
                   onClick="WdatePicker({autoPickDate:true})"
                   readonly="readonly" name="endtime" id="endtime" value="${activity.endTime}"/>
        </td>

    </tr>
    <tr>
        <td height="1" class="default_line_td">
            主动分享ID:
        </td>
        <td height="1">
            <select name="activitysid" id="select_share_base_info">
                <option value="0">请选择</option>
                <c:forEach var="baseinfo" items="${baseInfoList}">
                    <option value="${baseinfo.shareId}"
                    <c:if test="${baseinfo.shareId==activity.shareId}">selected="selected"</c:if>>${baseinfo.shareKey}</option>
                </c:forEach>
            </select>
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            关联的游戏资料库ID
        </td>
        <td height="1" class="">
            <input type="text" name="gamedbid" value="${activity.gameDbId}"/>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            上架渠道:
        </td>
        <td height="1" class="">
            <select name="weixinexclusive">
                <option value="0"
                <c:if test="${activity.getWeixinExclusive().code==0}">selected</c:if> >默认
                </option>
                <option value="1"
                <c:if test="${activity.getWeixinExclusive().code==1}">selected</c:if>>微信</option>
                <option value="2"
                <c:if test="${activity.getWeixinExclusive().code==2}">selected</c:if>>手游画报</option>
            </select>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            是否加入热门活动:
        </td>
        <td height="1" class="">
            <select name="hotactivity">
                <option value="0"
                <c:if test="${activity.hotActivity==0}">selected</c:if> >否
                </option>
                <option value="1"
                <c:if test="${activity.hotActivity==1}">selected</c:if>>是</option>
            </select>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            开放权限:
        </td>
        <td height="1" class="">
            <select name="reserve">
                <option value="0"
                <c:if test="${activity.reserveType==0}">selected</c:if>>可兑换</option>
                <option value="1"
                <c:if test="${activity.reserveType==1}">selected</c:if>>可预订</option>
            </select>
        </td>
    </tr>
</table>
<br/>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td><span
                style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">附加游戏信息</span>
        </td>
    </tr>
</table>
<br/>
<table width="90%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" class="">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏名称:
        </td>
        <td height="1" class="" width="10">
            <input type="text" name="gameName" size="20" maxlength="30" id="gameName"
                   value="${activity.gameName}"/>
        </td>
        <td height="1" align="left">*必填项</td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            平台:
        </td>
        <td height="1" class="" width="10">
            <input type="checkbox" name="pbox" value="1"
            <c:if test="${activity.activityPlatform.hasAndroid()}">checked="checked"</c:if>/>Android&nbsp;&nbsp;
            <input type="checkbox" name="pbox" value="2"
            <c:if test="${activity.activityPlatform.hasIos()}">checked="checked"</c:if>/>IOS&nbsp;&nbsp;
            <input type="checkbox" name="pbox" value="3"
            <c:if test="${activity.activityPlatform.hasAll()}">checked="checked"</c:if>/>全部
            <input type="hidden" name="platform" id="input_hidden_platform"/>
        </td>
        <td height="1" align="left"></td>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            合作专区:
        </td>
        <td height="1" class="" width="10">
            <input type="checkbox" name="cbox" value="1"
            <c:if test="${activity.cooperation.hasJiuYao()}">checked="checked"</c:if>/>91&nbsp;&nbsp;
            <input type="checkbox" name="cbox" value="2"
            <c:if test="${activity.cooperation.hasSanLiuLing()}">checked="checked"</c:if>/>360&nbsp;&nbsp;
            <input type="checkbox" name="cbox" value="3"
            <c:if test="${activity.cooperation.hasAll()}">checked="checked"</c:if>/>全部
            <input type="hidden" name="cooperation" id="input_hidden_cooperation"/>
        </td>
        <td height="1" align="left"></td>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏Icon:
        </td>
        <td height="1" class="">
            <c:choose>
                <c:when test="${fn:length(activity.gameIconUrl)>0}">
                    <img id="menu_pic2" src="${activity.gameIconUrl}" with="320" height="160"/></c:when>
                <c:otherwise><img id="menu_pic2" src="/static/images/default.jpg"/></c:otherwise>
            </c:choose>
            <span id="upload_button2">上传</span>
            <span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic2" type="hidden" name="picurl2" value="${activity.gameIconUrl}">
            图片请剪切成，96*96大小后上传，否则会出现样式错误。
        </td>
        <td height="1">*必填项</td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏类型:
        </td>
        <td height="1">
            <input type="checkbox" value="1" name="radio" id="radio1"
            <c:if test="${activity.category.hasKaiPai()}">checked="true" </c:if> />卡牌&nbsp;&nbsp;
            <input type="checkbox" value="2" name="radio" id="radio2"
            <c:if test="${activity.category.hasRpg()}">checked="true" </c:if>/>RPG&nbsp;&nbsp;
            <input type="checkbox" value="4" name="radio" id="radio3"
            <c:if test="${activity.category.hasCasual()}">checked="true" </c:if>/>休闲&nbsp;&nbsp;
            <input type="checkbox" value="8" name="radio" id="radio4"
            <c:if test="${activity.category.hasPuzzle()}">checked="true" </c:if>/>益智&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="16" name="radio" id="radio5"
            <c:if test="${activity.category.hasMotion()}">checked="true" </c:if>/>动作&nbsp;&nbsp;
            <input type="checkbox" value="32" name="radio" id="radio6"
            <c:if test="${activity.category.hasShoot()}">checked="true" </c:if>/>射击&nbsp;&nbsp;
            <input type="checkbox" value="64" name="radio" id="radio7"
            <c:if test="${activity.category.hasTactics()}">checked="true" </c:if>/>策略&nbsp;&nbsp;
            <input type="checkbox" value="128" name="radio" id="radio8"
            <c:if test="${activity.category.hasRole()}">checked="true" </c:if>/>角色扮演&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="256" name="radio" id="radio9"
            <c:if test="${activity.category.hasOperate()}">checked="true" </c:if>/>模拟经营&nbsp;&nbsp;
            <input type="checkbox" value="512" name="radio" id="radio10"
            <c:if test="${activity.category.hasPhysical()}">checked="true" </c:if>/>体育&nbsp;&nbsp;
            <input type="checkbox" value="1024" name="radio" id="radio11"
            <c:if test="${activity.category.hasSpeed()}">checked="true" </c:if>/>竞速&nbsp;&nbsp;
            <input type="checkbox" value="2048" name="radio" id="radio12"
            <c:if test="${activity.category.hasRelax()}">checked="true" </c:if>/>休闲&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="4096" name="radio" id="radio13"
            <c:if test="${activity.category.hasMusic()}">checked="true" </c:if>/>音乐&nbsp;&nbsp;
            <input type="checkbox" value="8192" name="radio" id="radio14"
            <c:if test="${activity.category.hasAdopt()}">checked="true" </c:if>/>养成&nbsp;&nbsp;
            <input type="checkbox" value="16384" name="radio" id="radio15"
            <c:if test="${activity.category.hasChess()}">checked="true" </c:if>/>棋牌&nbsp;&nbsp;
            <input type="checkbox" value="32768" name="radio" id="radio16"
            <c:if test="${activity.category.hasTriple()}">checked="true" </c:if>/>三消&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="65536" name="radio" id="radio17"
            <c:if test="${activity.category.hasDefence()}">checked="true" </c:if>/>塔防&nbsp;&nbsp;
            <input type="checkbox" value="131072" name="radio" id="radio18"
            <c:if test="${activity.category.hasRiddle()}">checked="true" </c:if>/>解谜&nbsp;&nbsp;
            <input type="checkbox" value="262144" name="radio" id="radio19"
            <c:if test="${activity.category.hasLove()}">checked="true" </c:if>/>恋爱&nbsp;&nbsp;
            <input type="hidden" value="" id="caregory" name="caregory"/>
        </td>
        <td height="1">*最多同时选择三个</td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            发行商/开发商:
        </td>
        <td height="1">
            <input type="text" name="developer" size="20" id="developer" value="${activity.gameProduct}"/>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏专区地址:
        </td>
        <td height="1" class="">
            <input type="text" name="gameUrl" size="20" id="gameUrl" value="${activity.gameUrl}"/>
            <span style="color:red;">*地址请已http://或者https://开头，谢谢</span>
        </td>

    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            游戏下载地址:
        </td>
        <td height="1">
            苹果下载地址:<input type="text" name="iosUrl" size="20" id="iosUrl" value="${activity.iosDownloadUrl}"/>
            <br/>
            安卓下载地址:<input type="text" name="androidUrl" size="20" id="androidUrl"
                          value="${activity.androidDownloadUrl}"/>
        </td>
        <td>
            <span style="color:red;">*地址请已http://或者https://开头，谢谢</span>
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