<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/include/js/stringutil.js"></script>
<script type="text/javascript">
function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        this.debug(ex);
    }
}
function uploadStart(file) {
    $('#loading').css('display', '');
}
function uploadComplete(file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $('#loading').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function fileQueueError(file, errorCode, message) {
    try {
        var imageName = "error.gif";
        var errorName = "";
        if (errorCode === SWFUpload.errorCode_QUEUE_LIMIT_EXCEEDED) {
            errorName = "You have attempted to queue too many files.";
        }

        if (errorName !== "") {
            alert(errorName + "fileQueueError");
            return;
        }

        switch (errorCode) {
            case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                alert("图片容量为0");
                break;
            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                imageName = "toobig.gif";
                alert("图片太大了");
                break;
            case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
            default:
                alert("超出一次最多上传图片张数");
                break;
        }
    } catch (ex) {
        this.debug(ex);
    }

}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadStart(file) {

    $('#loading').css('display', '');
}

function uploadProgress(file, bytesLoaded) {
    try {
        var percent = Math.ceil((bytesLoaded / file.size) * 100);
        var progress = new FileProgress(file, this.customSettings.upload_target);
        progress.setProgress(percent);

    } catch (ex) {
        this.debug(ex);
    }
}
function uploadSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);

            $('#menu_pic').attr('src', largeLogoSrc);
            $('#input_menu_pic').val(largeLogoSrc);


            alert('上传成功');
            $('#menu_pic').Jcrop({
                bgFade: true,
                onChange: showCoords,
                onSelect: showCoords,
                onRelease: clearCoords,
                bgOpacity: .3,
                setSelect: [ 0, 0, 172, 127 ]
            }, function () {
                window.jcrop_api = this;
            });
            setTimeout(function () {
                window.jcrop_api.setImage(largeLogoSrc);
            }, 200)
//            $('#input_menu_pic').val(jsonData.result[0]);
            //   alert('上传成功');
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if (jsonData.msg == '') {
                    alert('上传失败');
                } else {
                    alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete(file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $('#loading').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadError(file, errorCode, message) {
    try {
        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                this.cancelQueue();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                this.cancelQueue();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                this.cancelQueue();
                break;
            case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                this.cancelQueue();
                break;
            default:
                alert(message);
                break;
        }
    } catch (ex3) {
        this.debug(ex3);
    }
}


/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
    this.fileProgressID = targetID;
    this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
    if (!this.fileProgressWrapper) {
        this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
    }

    this.height = this.fileProgressWrapper.offsetHeight;
}


FileProgress.prototype.setStatus = function (status) {

};


function showCoords(c) {
    $('#x1').val(c.x);
    $('#y1').val(c.y);
    $('#x2').val(c.x2);
    $('#y2').val(c.y2);
    $('#w').val(c.w);
    $('#h').val(c.h);
}
function clearCoords() {
    $('#coords input').val('');
}
String.prototype.startWith = function (str) {
    var reg = new RegExp("^" + str);
    return reg.test(this);
}

function createNewImg() {
    var imgNew = $('#input_menu_pic').val();
    // if(httpImgNew.start)
    var httpImgNew = imgNew;
    if (!httpImgNew.startWith("http://")) {
        httpImgNew = "http://" + imgNew.substring(1, 5) + ".${domain}" + imgNew;
    }

    if (imgNew.startWith("http://")) {
        imgNew = imgNew.replace("http://" + imgNew.substring(7, 11) + ".${domain}", "");
    }

    if ($('#x1').val() == '' && $('#y1').val() == '' && $('#w').val() == '' && $('#h').val() == '') {
        alert("请选择一个区域");
        $('#loading2').css('display', 'none');
        return false;
    }

    var w = $("#w").val();
    var h = $("#h").val();
    $.ajax({
        url: "/json/upload/imgcropper",
        type: 'post',
        data: {filename: imgNew, x1: $("#x1").val(), y1: $("#y1").val(), w: $("#w").val(), h: $("#h").val(), at: '${at}', rw: $('#aw').val(), rh: $('#ah').val(), rtype: 'image'},
        success: function (data) {

            var jsonObj = eval('(' + data + ')');
            if (jsonObj.rs == '1') {
                $('#menu_pic').attr('src', jsonObj.url);
                jcrop_api.setImage(jsonObj.url);
                $('#input_menu_pic').val(jsonObj.url);

                $('#w').val(w);
                $('#h').val(h);
            } else {
                alert('切割失败!');
            }
            $('#loading2').css('display', 'none');
        }
    });
}
</script>
<script type="text/javascript" src="/static/include/js/jquery.Jcrop.js"></script>
<link type="text/css" rel="stylesheet" href="/static/include/css/jquery.Jcrop.css"/>
<script>


    function isNotNull() {
        var menuName = $("#input_text_menuName").val();
        var menuType = $("#menuType").val();
        if (menuName == '') {
            alert("菜单名称不能为空!");
            return false;
        }

        if (menuType == '') {
            alert("请选择菜单类型。");
            return false;
        }

        if ($('#input_statusdesc').length > 0) {
            var status = $('#input_statusdesc').val();
            if (status.length == 0) {
                alert("请填写状态描述");
                return false;
            }
        }

        if ($('#input_recomstar').length > 0) {
            var star = $('#input_recomstar').val();
            if (star.length == 0) {
                alert("请填写推荐星级");
                return false;
            }
        }

        var statusdesc = $("#statusdesc").val();
        if (statusdesc != '' && jmz.GetLength(statusdesc) > 6) {
            alert("请检查内容类型字数");
            return false;
        }


    }
    $().ready(function () {

        window.jcrop_api;
        $('#menu_pic').Jcrop({
            bgFade: true,
            onChange: showCoords,
            onSelect: showCoords,
            onRelease: clearCoords,
            bgOpacity: 0.3,
            setSelect: [ 0, 0, 170, 127 ]
        }, function () {
            jcrop_api = this;
        });

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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP菜单管理</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">APP二级菜单添加</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form action="/joymeapp/menu/createsub" method="post" id="form_submit" onsubmit="return isNotNull();">
<input type="hidden" name="appkey" value="${appkey}"/>
<input type="hidden" name="pid" value="${pid}"/>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="3" class="default_line_td"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            菜单名称:
        </td>
        <td height="1" class="">
            <input id="input_text_menuName" type="text" name="menuname" size="80" value=""/>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <c:if test="${displaytype==0}">
    <tr style="display: none">
        </c:if>
        <c:if test="${displaytype!=0}">
    <tr style="">
        </c:if>
        <td height="1" class="default_line_td">
            菜单描述:
        </td>
        <td height="1" class="">
            <textarea id="input_menuDesc" type="text" name="menudesc"
                      style="height: 100px;width: 600px"></textarea><span
                style="color:red">限40字以内</span>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            菜单URL:
        </td>
        <td height="1" class="">
            <input id="input_text_url" type="text" name="url" size="80" value=""/>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            菜单类型:
        </td>
        <td height="1" class="">
            <select name="menuType" id="menuType">
                <option selected="selected" value="">请选择</option>
                <option value="0"><fmt:message key="joymeapp.menu.type.0" bundle="${def}"/></option>
                <option value="1"><fmt:message key="joymeapp.menu.type.1" bundle="${def}"/></option>
            </select>

            <%--<input id="input_text_menutype" type="text" name="menutype" size="32" value="${joymeAppMenu.menuType}"/>--%>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <tr <c:if test="${displaytype == 0 || displaytype == 3}"> style="display: none"</c:if>>
        <td height="1" class="default_line_td">
            图片:
        </td>
        <td height="1" class="">
            <img id="menu_pic" src="/static/images/default.jpg"
                 class="img_pic" width="200px" height="200px"/>
            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic" type="hidden" name="pic_url" value="">
            <span style="color:red">1.6版通版170*127,1.6精版(顶部大图560*150,长条小图120*120,图文区图片120*120),1.7通版(图文列表330*224,图片、标签列表548*368,图标列表238*228)</span>
            <a href="javascript:void(0)" onclick="createNewImg()">裁图并保存</a>

            <div id="coords">
                <input type="hidden" size="4" id="x1" name="x1"/>
                <input type="hidden" size="4" id="y1" name="y1"/>
                当前截取的宽度：<input type="text" size="4" id="w" name="w" disabled="true"/>
                当前截取的长度：<input type="text" size="4" id="h" name="h" disabled="true"/>
            </div>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            列表展示类型:
        </td>
        <td height="1" class="">
            <select name="displayType" id="displayType">
                <c:forEach var="type" items="${displayTypes}">
                    <option value="${type.code}"
                            <c:if test="${type.code == displaytype}">selected="selected"</c:if>>
                        <fmt:message key="joymeapp.menu.displaytype.${type.code}" bundle="${def}"/>
                    </option>
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
                <c:forEach items="${contentTypes}" var="ctype">
                    <option value="${ctype.code}"><fmt:message key="app.menu.content.type.${ctype.code}"
                                                               bundle="${def}"/>
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
        <td height="1" class="">
            <select name="isNew">
                <option value="true">true</option>
                <OPTION value="false" selected="selected">false</OPTION>
            </select>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <tr <c:choose>
        <c:when test="${displaytype==1}">style="display: none" </c:when>
        <c:otherwise>style=""</c:otherwise>
    </c:choose>>
        <td height="1" class="default_line_td">
            是否是hot:
        </td>
        <td height="1" class="">
            <select name="isHot">
                <option value="true"> true</option>
                <OPTION value="false" selected="selected">false</OPTION>
            </select>
        </td>
        <td height="1" class=>
        </td>
    </tr>
    <c:choose>
        <c:when test="${appMenu.moduleType.code ==1}">
            <tr>
                <td height="1" class="default_line_td">
                    状态描述:
                </td>
                <td height="1" class="">
                    <input type="text" name="statusdesc" id="input_statusdesc" value=""/>*如：“即将开启/进行中...”
                </td>
                <td height="1" class=>
                </td>
            </tr>
        </c:when>
        <c:when test="${appMenu.moduleType.code ==3}">
            <tr>
                <td height="1" class="default_line_td">
                    推荐星级:
                </td>
                <td height="1" class="">
                    <input type="text" name="recomstar" id="input_recomstar" value=""/>*1 -- 5之间的数字
                </td>
                <td height="1" class=>
                </td>
            </tr>
        </c:when>
    </c:choose>
    <tr>
        <td height="1" class="default_line_td">
            更新时间:
        </td>
        <td height="1">
            <input id="lastmodifydate" name="lastmodifydate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                   readonly></input>
        </td>
        <td height="1">
        </td>
    </tr>
    <c:choose>
        <c:when test="${appMenu.moduleType.code ==7}">
            <tr>
                <td height="1" class="default_line_td">
                    内容类型:
                </td>
                <td height="1" class="">
                    <input type="text" name="statusdesc" id="statusdesc"/>
                </td>
                <td height="1" class=>
                </td>
            </tr>
        </c:when>
    </c:choose>
    <tr align="center">
        <td colspan="3">
            <input name="Submit" type="submit" class="default_button" value="添加">
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