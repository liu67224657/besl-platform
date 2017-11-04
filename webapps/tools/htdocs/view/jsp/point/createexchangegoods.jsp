<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>商品管理</title>
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

<script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
<script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
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
    $("#addnewgame").click(function () {
        $("#reset").click(function () {
            $('.easyui-datetimebox').datetimebox('clear');
            $("#form_submit :text").prop("value", '');
            $("#form_submit input[name='gameDbId']").prop("value", '');
        });

        $('#dgrules').datagrid({
                    url: '/json/gameclient/clientline/toaddgame',
                    pagination: true,
                    rownumbers: false,
                    singleSelect: true,
                    width: 'auto',
                    height: 'auto',
                    striped: true,
                    idField: 'appId',
                    pageList: [10, 20, 30, 40],
                    fitColumns: true,
                    loadMsg: '数据加载中请稍后……',
                    columns: [
                        [
                            {field: 'gameDbId', title: 'game_db表的_id', width: 60},
                            {field: 'gameName', title: '游戏名称', width: 100},
                            {field: 'anotherName', title: '别名', width: 100},
                            {field: 'downloadRecommend', title: '下载推荐', width: 150},
                            {
                                field: 'gameIcon', title: '游戏主图', width: 60,
                                formatter: function (value, row, index) {
                                    return "<img src=" + row.gameIcon + " width='40' height='40>'";

                                }
                            },
                            {
                                field: 'gamePublicTime', title: '上市时间', width: 100,
                                formatter: function (value, row, index) {
                                    if (row.gamePublicTime != null && row.gamePublicTime.toString() != '') {
                                        var time = new Date(row.gamePublicTime);
                                        return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
                                    }
                                    else {
                                        return '';
                                    }

                                }
                            },
                            {
                                field: 'gameDevices', title: '游戏设备', width: 120,
                                formatter: function (value, row, index) {
                                    var result = '';
                                    for (var item in row.gameDBDevicesSet) {
                                        if (row.gameDBDevicesSet[item].gameDbDeviceId == 1 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                            result += "iphone,"
                                        } else if (row.gameDBDevicesSet[item].gameDbDeviceId == 2 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                            result += "ipad,"
                                        } else if (row.gameDBDevicesSet[item].gameDbDeviceId == 3 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                            result += "android,"
                                        }

                                    }
                                    //去掉最后的 逗号
                                    if (result.length > 0 && result.charAt(result.length - 1) == ',') {
                                        result = result.substr(0, result.length - 1)

                                    }
                                    return result;

                                }
                            },
                            {field: 'ck', checkbox: true}
                        ]
                    ]
                });

        $('#chooseWindow').window('open');
    });


    //从gamedb 中读取的查询按钮
    $("#buttonInWindow").click(function () {

        var url = '/json/gameclient/clientline/toaddgame';
        if ($.trim($("#gameNameInWindow").val()) != '') {

            url += '?gameName=' + $.trim($("#gameNameInWindow").val());
        }
        $('#dgrules').datagrid('options').url = url;
        $("#dgrules").datagrid('reload');

    });

    //弹窗中选择一个游戏后
    $("#selectone").click(function () {

        var item = $('#dgrules').datagrid('getSelected');
        $("#form_submit input[name='gamedbid']").val(item.gameDbId);

        $('#chooseWindow').window('close');
    });
});


$().ready(function () {
    $('#form_submit').bind('submit', function () {
        var platform = $("input[name=pbox]:checked").length;

        var subject = $("#activitySubject").val();
        var pic = $("#input_menu_pic").val();
        var starttime = $("#starttime").val();
        var jsonDesc = $("#word_surplus").text();
        var endtime = $("#endtime").val();
        var goodsamount = $("[name='goodsamount']").val();
        var timetype = $("[name='timetype']").val();
        var activitygoodtype = $("[name='activitygoodtype']").val();
        var goodsconsumepoint = $("[name='goodsconsumepoint']").val();
        var seckilltype = $("[name='seckilltype']").val();
        var shoptype = $("[name='shoptype']").val();


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
        if (platform == 2) {
            $("[name='platform']").val(4);
        } else {
            platform = $("input[name=pbox]:checked").val();
            $("[name='platform']").val(platform);
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
        if (activitygoodtype == '') {
            alert('请选择商品分类');
            return false;
        }
        if (goodsamount == '') {
            alert("请填写总量");
            return false;
        }

        if (goodsconsumepoint == '') {
            alert("请填写消费积分");
            return false;
        }
        if (timetype == '') {
            alert("请选择领取类型");
            return false;
        }
        var hotactivity = $("[name='hotactivity']").val();
        if (hotactivity == 2) {
            var channltype = $("[name='channltype']").val();
            if (channltype != 1) {
                alert("只有上架渠道为【微信】的礼包才能选择其他");
                return false;
            }
        }

        var yktitle = $("[name='yktitle']").val();
        var ykdesc = $("[name='ykdesc']").val();
//                var picurl2 = $("[name='picurl2']").val();
//                var ipadpic = $("[name='ipadpic']").val();

        if (seckilltype == '0' && shoptype == '12') {
            if (yktitle == '') {
                alert("请填写优酷首页标题");
                return false;
            }
            if (ykdesc == '') {
                alert("请填写优酷首页描述");
                return false;
            }
//                        if (picurl2 == '') {
//                            alert("请填写优酷首页图片");
//                            return false;
//                        }
//                        if (ipadpic == '') {
//                            alert("请上传优酷ipad图片");
//                            return false;
//                        }
        }
    });
    var coustomSwfu = new SWFUpload(coustomImageSettings);
    var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
    var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
    var coustomSwfu4 = new SWFUpload(coustomImageSettings4);
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
    debug: false
}

var coustomImageSettings4 = {
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

    file_dialog_complete_handler: fileDialogComplete4,
    upload_start_handler: uploadStart4,
    upload_success_handler: uploadSuccess4,
    upload_complete_handler: uploadComplete4,

    // Button Settings
    button_image_url: "/static/images/uploadbutton.png",
    button_placeholder_id: "upload_button4",
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

function secKill() {
    var seckilltype = $("[name='seckilltype']").val();
    if (seckilltype == '1') {
        $("#yktitle").css("display", "table-row");
        $("#ykpic").css("display", "table-row");
        $("#ykpic2").css("display", "table-row");
        $("#ykdesc").css("display", "table-row");


    } else {
        $("#yktitle").css("display", "none");
        $("#ykpic").css("display", "none");
        $("#ykpic2").css("display", "none");
        $("#ykdesc").css("display", "none");

    }
}
</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 活动信息管理</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">添加活动信息</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>

<form action="/point/exchangegoods/create" method="post" id="form_submit">
<table width="90%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <input type="hidden" name="activitytype" value="${activitType}"/>
    <input type="hidden" name="seckilltype" value="${seckillType}"/>
    <td height="1" class="default_line_td">
        活动标题
    </td>
    <td height="1" class="" width="10">
        <input type="text" name="activitysubject" size="32" maxlength="30" id="activitySubject"/>
        *必填项，最好不要超过10个字
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
        <img id="menu_pic" with="320" height="160" src="/static/images/default.jpg"/>
        <span id="upload_button">上传</span>
        <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
        <input id="input_menu_pic" type="hidden" name="picurl1" value="">
        *必填项
    </td>
    <td height="1"></td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        活动详情页背景图:
    </td>
    <td height="1" class="">
        <img id="menu_pic3" with="320" height="160" src="/static/images/default.jpg"/>
        <span id="upload_button3">上传</span>
        <span id="loading3" style="display:none"><img src="/static/images/loading.gif"/></span>
        <input id="input_menu_pic3" type="hidden" name="picurl3" value="">

    </td>
    <td height="1"></td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        活动简介:
    </td>
    <td height="1">
        <textarea name="content" id="editor_id"></textarea>
        <input type="hidden" id="jsonDesc" name="jsondesc"/>

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
        小标题描述:
    </td>
    <td height="1">
        <textarea name="subdesc" id="subdesc" cols="45" rows="8"></textarea>
    </td>
</tr>

<tr>
    <td height="1" class="default_line_td" width="200px">
        活动开始时间:
    </td>
    <td height="1">
        <input type="text" class="Wdate"
               onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
               readonly="readonly" name="starttime" id="starttime"/> <span style="color:red">*必选 如果该商品加入<b
            style="font-size:15px">秒杀</b> 则 <b>活动开始时间就是秒杀开始时间</b></span>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        活动结束时间:
    </td>
    <td height="1" class="">
        <input type="text" class="Wdate"
               onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
               readonly="readonly" name="endtime" id="endtime"/> <span style="color:red">*必选</span>
    </td>

</tr>
<c:choose>
    <c:when test="${activitType==0}">
        <tr>
            <td height="1" class="default_line_td" width="200px">
                商城上架渠道
            </td>
            <td height="1">
                <select name="shoptype">
                    <c:forEach items="${shopTypes}" var="item">
                        <option value="${item.code}">${item.name}</option>
                    </c:forEach>

                </select>
            </td>
            <td height="1"></td>
        </tr>
        <tr>
            <td height="1" class="default_line_td" width="200px">
                商品分类：
            </td>
            <td height="1">
                <select name="activitygoodtype">
                    <option value="">请选择</option>
                    <option value="0">实物商品</option>
                    <option value="1">虚拟商品</option>
                </select>
                <span style="color:red">*必选</span>
            </td>
            <td height="1"></td>
        </tr>
    </c:when>
    <c:otherwise>
        <tr>
            <td height="1" class="default_line_td">
                平台
            </td>
            <td height="1" class="">
                <input type="checkbox" name="pbox" value="0"/>IOS&nbsp;&nbsp;
                <input type="checkbox" name="pbox" value="1"/>Android&nbsp;&nbsp;
                <input type="hidden" name="platform"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                关联的游戏资料库ID
            </td>
            <td height="1" class="">
                <input type="text" name="gamedbid" readonly="readonly"/> <input type="button"
                                                                                id="addnewgame"
                                                                                name="button"
                                                                                value="请选择要填加的游戏"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                礼包中心上架渠道:
            </td>
            <td height="1" class="">
                <select name="channltype">
                    <option value="0">默认</option>
                    <option value="1">微信</option>
                    <option value="2">手游画报</option>
                </select>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                是否加入热门活动:
            </td>
            <td height="1" class="">
                <select name="hotactivity">
                    <option value="0">否</option>
                    <option value="1">是</option>
                    <option value="2">其他</option>
                </select>
            </td>
        </tr>

    </c:otherwise>
</c:choose>
<tr>
    <td height="1" class="default_line_td">
        开放权限:
    </td>
    <td height="1" class="">
        <select name="reserve">
            <option value="0">可兑换</option>
            <option value="1">可预订</option>
        </select>
    </td>
</tr>

<tr>
    <td height="1" class="default_line_td">
        总量:
    </td>
    <td height="1" class="">
        <input type="text" name="goodsamount" onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
               size="20"/> <span style="color:red">*必选</span>
    </td>
    <td height="1"></td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        消费积分:
    </td>
    <td height="1">
        <input type="text" name="goodsconsumepoint" size="20"/>
        <span style="color:red">*必填，不需要积分填0</span>

    </td>
    <td height="1"></td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        市场价格:
    </td>
    <td height="1">
        <input type="text" name="price" size="20"/>
    </td>
    <td height="1"></td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="200px">
        领取类型:
    </td>
    <td height="1">
        <select name="timetype">
            <option value="">请选择</option>
            <c:forEach var="consumetimestype" items="${consumetimestypecollection}">
                <option value="${consumetimestype.code}">
                    <fmt:message key="consumetimes.type.${consumetimestype.code}"
                                 bundle="${def}"/></option>
            </c:forEach>

        </select>
        <span style="color:red">*必选</span>
    </td>
    <td height="1"></td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="200px">
        hot或new:
    </td>
    <td height="1">
        <select name="displaytype">
            <option value="0">无</option>
            <option value="1">是new</option>
            <option value="2">是hot</option>
            <option value="3">是new和hot</option>

        </select>
    </td>
    <td height="1"></td>
</tr>
<c:if test="${seckillType == 0}">
    <tr id="yktitle">
        <td height="1" class="default_line_td" width="200px">
            优酷首页标题:
        </td>
        <td height="1">
            <input type="text" name="yktitle"/><span style="color:red">*优酷商城的必填(没有秒杀显示兑换时使用)</span>
        </td>
        <td height="1"></td>
    </tr>
    <tr id="ykdesc">
        <td height="1" class="default_line_td" width="200px">
            优酷首页描述:
        </td>
        <td height="1">
            <textarea name="ykdesc" cols="45" rows="8"></textarea>
            <span style="color:red">*优酷商城的必填(没有秒杀显示兑换时使用)</span>
        </td>
        <td height="1"></td>
    </tr>
    <%--<tr id="ykpic">--%>
    <%--<td height="1" class="default_line_td" width="200px">--%>
    <%--优酷首页广告图片:--%>
    <%--</td>--%>
    <%--<td height="1">--%>
    <%--<img id="menu_pic2" with="320" height="160" src="/static/images/default.jpg"/>--%>
    <%--<span id="upload_button2">上传</span>--%>
    <%--<span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>--%>
    <%--<input id="input_menu_pic2" type="hidden" name="picurl2" value="">--%>
    <%--<span style="color:red">优酷商城的必传</span>--%>
    <%--</td>--%>
    <%--<td height="1"></td>--%>
    <%--</tr>--%>
    <%--<tr style="display:none;" id="ykpic2">--%>
    <%--<td height="1" class="default_line_td" width="200px">--%>
    <%--优酷首页广告IPAD图片:--%>
    <%--</td>--%>
    <%--<td height="1">--%>
    <%--<img id="menu_pic4" with="320" height="160" src="/static/images/default.jpg"/>--%>
    <%--<span id="upload_button4">上传</span>--%>
    <%--<span id="loading4" style="display:none"><img src="/static/images/loading.gif"/></span>--%>
    <%--<input id="input_menu_pic4" type="hidden" name="ipadpic" value="">--%>
    <%--<span style="color:red">优酷商城的必传</span>--%>
    <%--</td>--%>
    <%--<td height="1"></td>--%>
    <%--</tr>--%>
</c:if>
<tr id="subscript">
    <td height="1" class="default_line_td" width="200px">
        角标文字:
    </td>
    <td height="1">
        <input type="text" name="subscript"/> <span style="color:red">暂时只有优酷商城用到 非必填</span>
    </td>
    <td height="1"></td>
</tr>
<tr id="subcolor">
    <td height="1" class="default_line_td" width="200px">
        角标颜色:
    </td>
    <td height="1">
        <input type="text" name="subcolor"/>*色值 如：#FF0000 <span
            style="color:red">暂时只有优酷商城用到 非必填</span>
    </td>
    <td height="1"></td>
</tr>
<tr align="center">
    <td colspan="3">
        <input name="Submit" type="submit" class="default_button" value="提交">
        <input name="Reset" type="button" class="default_button" value="返回"
               onclick="javascipt:window.history.go(-1);">
    </td>
</tr>
</td>
</tr>
</table>
</form>
</table>

<div id="chooseWindow" class="easyui-window" title="选择要填加的app"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false" style="width:750px;">
    游戏名称&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"
                                                                                        id="gameNameInWindow"
                                                                                        size="10"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" id="buttonInWindow" value="查询"/>
    <table id="dgrules" data-options="
              rownumbers:false,
              autoRowHeight:true,
              pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;">
            <input type="button" onclick="$('#chooseWindow').window('close');" value="取消"/>
            <input type="button" value="选择" id="selectone" value="保存"/></div>
    </div>
</div>
</body>
</html>