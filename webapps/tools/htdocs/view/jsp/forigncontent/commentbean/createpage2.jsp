<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.Jcrop.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/include/css/jquery.Jcrop.css"/>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }
    </style>
    <script>
        var urlUpload = '${urlUpload}';
        var corpObj;
        $(window).load(function () {
            $('#form_submit').bind('submit', function () {
                if ($.trim($("#desc").val()) == "") {
                    $("#desc").focus();
                    alert('请填写帖子内容');
                    return false;
                }
                var strlen = isChinese($("#desc"));
                strlen = Math.ceil(strlen / 2);
                if (strlen <= 1) {
                    alert("最少要输入两个字");
                    return false;
                }
                if (strlen > 140) {
                    alert("最多只能输入140个字，现在字数" + strlen);
                    return false;
                }
                if ($.trim($("#nickname").val()) == "") {
                    alert("请填写虚拟用户昵称。");
                    return false;
                }


                if ($("#input_menu_pic").val() == "") {
                    alert("请上传图片。");
                    return false;
                }
            });


            corpObj = $.Jcrop('#menu_pic', {
                bgFade: true,
                aspectRatio: 1,
                onChange: function (c) {
                    $('#hidden_x1').val(c.x);
                    $('#hidden_y1').val(c.y);
                    $('#img1_aw').val(c.xMax);
                    $('#img1_ah').val(c.yMax);
                    $('#img1_w').val(c.w);
                    $('#img1_h').val(c.h);
                },
                onSelect: function (c) {
                    $('#hidden_x1').val(c.x);
                    $('#hidden_y1').val(c.y);
                    $('#img1_aw').val(c.xMax);
                    $('#img1_ah').val(c.yMax);
                    $('#img1_w').val(c.w);
                    $('#img1_h').val(c.h);
                },
                onRelease: function () {
                    $('#coords1 input').val('');
                },
                bgOpacity: 0.3,
                setSelect: [ 0, 0, 115, 115 ]
            });

            var option = {
                areaId: 'interface',
                sectionid: 'anim_buttons',
                corpclickHandler: function (v, corpObj) {
                    corpObj.setSelect(v);
                    var ratioValue = (v[2] - v[0]) / (v[3] - v[1]);
                    corpObj.setOptions({ aspectRatio: ratioValue });
                    $('#img1_aw').val(v[2] - v[0]);
                    $('#img1_ah').val(v[3] - v[1]);
                    $('#img1_w').val(v[2] - v[0]);
                    $('#img1_h').val(v[3] - v[1]);
                    return false;
                },
                corpObj: corpObj
            }
            createJCropArea(option);

            var viewLineOption = {
                buttonId: 'upload_button',
                imgId: 'menu_pic',
                corpObj: corpObj,
                fileDialogComplete: fileDialogComplete,
                uploadStart: uploadStart,
                uploadSuccess: jietuSuccess,
                uploadComplete: uploadComplete,
                uploadError: uploadError
            };
            var swfu = createSwfUploadObj(viewLineOption);


            $("#createNewImg1").click(function () {

                var pic = $("#input_menu_pic").val();
                if (pic == '') {
                    alert("请上传图片再剪裁");
                    return;
                }
                $('#loading_corp1').css('display', '');
                var xAxias = $('#hidden_x1').val();
                var yAxias = $('#hidden_y1').val();
                var width = $('#img1_w').val();
                var height = $('#img1_h').val();


                if (xAxias == '' && yAxias == '' && width == '' && height == '') {
                    alert("请选择一个区域");
                    $('#loading_corp1').css('display', 'none');
                    return false;
                }
                $.ajax({
                    url: "/json/upload/imgcropper",
                    type: 'post',
                    data: {filename: pic, x1: xAxias, y1: yAxias, w: width, h: height, at: at, rw: $('#img1_aw').val(), rh: $('#img1_ah').val(), rtype: 'wanba'},
                    success: function (data) {

                        var jsonObj = eval('(' + data + ')');
                        if (jsonObj.rs == '1') {
                            $('#menu_pic').attr('src', jsonObj.url);
                            corpObj.setImage(jsonObj.url);
                            $('#input_menu_pic').val(jsonObj.url);
                        } else {
                            alert('切割失败!');
                        }
                        $('#loading_corp1').css('display', 'none');
                    }
                });
            });
        });

        function isChinese(str) { //判断是不是中文
            var oVal = str.val();
            var oValLength = 0;
            oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
            return oValLength;

        }

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸迷友圈主贴</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">玩霸迷友圈主贴</td>
                </tr>
            </table>
            <form action="/comment/bean/create" method="post" id="form_submit">

                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            帖子内容
                        </td>
                        <td height="1">
                            <textarea name="text" id="desc" cols="50" rows="10">${text}</textarea> *必填
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            虚拟用户名称:
                        </td>
                        <td height="1">
                            <input id="nickname" type="text" name="nick" value="${nick}" size="48"/> *必填
                            <span style="color:red">
                                <c:if test="${not empty errorMsg}">
                                    <fmt:message
                                            key="${errorMsg}" bundle="${error}"/>
                                </c:if>
                            </span>
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            图片:
                        </td>
                        <td>

                            <img id="menu_pic"  <c:choose>
                                <c:when test="${not empty pic}">
                                    src="${pic}"
                                </c:when>
                                <c:otherwise>
                                    src="/static/images/default.jpg"
                                </c:otherwise>
                            </c:choose> />

                            <div id="coords1">
                                <input type="hidden" size="4" id="hidden_x1" name="hidden_x1"/>
                                <input type="hidden" size="4" id="hidden_y1" name="hidden_y1"/>
                                当前截取的宽度：<input type="text" size="4" id="img1_w" name="img1_w" disabled="true"/>
                                当前截取的长度：<input type="text" size="4" id="img1_h" name="img1_h" disabled="true"/>
                            </div>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>

                            <div>
                                <label style="color:blue"></label>
                                <label> <input type="hidden" size="4" id="img1_aw" name="img1_aw"
                                               class="default_input_singleline"/></label>
                                <label><input type="hidden" size="4" id="img1_ah" name="img1_ah"
                                              class="default_input_singleline"/></label>
                                <input type="button" id="createNewImg1" value="截取并保存图片"> *不需要截图的图片直接提交
                                <span id="loading_corp1" style="display:none"><img
                                        src="/static/images/loading.gif"/></span>
                            </div>


                            <div id="interface" style="margin: 1em 0;"></div>
                            <input id="input_menu_pic" type="hidden" name="pic" value="${pic}">
                            <span style="color:red">*图片大小请压缩在1MB以下</span>
                        </td>
                        <td height="1" class=>
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