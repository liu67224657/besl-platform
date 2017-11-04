<%@ taglib prefix="sql_rt" uri="http://java.sun.com/jstl/sql_rt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/include/js/jquery.js"></script>
<script type="text/javascript" src="/static/include/js/default.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/static/include/js/jquery.Jcrop.js"></script>
<link type="text/css" rel="stylesheet" href="/static/include/css/jquery.Jcrop.css"/>
<script type="text/javascript" src="/static/include/js/viewline.js"></script>
<title>后台数据管理-添加Line元素</title>
<script language="JavaScript" type="text/JavaScript">
    var at = "${at}";
    var urlUpload = '${urlUpload}';
</script>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <form action="/viewline/addlineitem" method="post" name="addlineitemform" id="addlineitemform">
        <input name="lineId" type="hidden" value="${line.lineId}"/>
        <input name="ItemId" type="hidden" value="${lineItem.itemId}"/>

        <input type="hidden" name="itemDesc" value="${lineItem.itemDesc}">
        <input type="hidden" name="srcId1" value="${srcId1}">
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">[选填]：</td>
            <td class="edit_table_value_td">当有自定义需求时，可填下面内容</td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">自定义URL：</td>
            <td class="edit_table_value_td">
                <c:if test="${errorMsgMap['itemDesc'] != null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemDesc']}"
                                                            bundle="${error}"/></span><br>
                </c:if>
                <input name="customUrl" type="text" class="default_input_singleline" size="60" maxlength="256"
                       value="${lineItem.displayInfo.getLinkUrl()}">
                * 此处文字内容、数量，根据页面显示需要自由定义。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">自定义标题：</td>
            <td class="edit_table_value_td">
                <c:if test="${errorMsgMap['itemDesc'] != null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemDesc']}"
                                                            bundle="${error}"/></span><br>
                </c:if>
                <input id="subject" name="customSubject" type="text" class="default_input_singleline" size="60"
                       maxlength="256" value="${lineItem.displayInfo.getSubject()}"
                       onfocus="ss=setInterval(sp,600,'subject')" onblur="clearInterval(ss)">
                * 此处文字内容、数量，根据页面显示需要自由定义。你已经输入了<span id="subjectspan">0</span>字
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">图片列表：</td>
            <td class="edit_table_value_td">
                <div>
                    <c:choose>
                        <c:when test="${content.images ne null}">
                            <ul style="list-style-type:none">
                                <c:forEach items="${content.images.images}" var="img">
                                    <c:if test="${img.validStatus}">
                                        <li style="float:left;">
                                            <p><img src="<c:out value="${uf:parseMFace(img.m)}"/>" width="100"
                                                    height="100"></p>

                                            <p align="center"><input type="radio" name="img" value="${img.m}"></p>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            本文不含图片
                        </c:otherwise>
                    </c:choose>
                </div>
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">自定义图标1：</td>
            <td class="edit_table_value_td" colspan="3">
                <c:if test="${errorMsgMap['itemDesc'] != null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemDesc']}"
                                                            bundle="${error}"/></span><br>
                </c:if>
                <div>
                    <img id="img_game_logo"
                            <c:choose>
                                <c:when test="${img eq null || img eq ''}">src="/static/images/default.jpg" </c:when>
                                <c:otherwise>src="${uf:parseOrgImg(img)}"</c:otherwise>
                            </c:choose> />
                </div>
                <div>
                    <div id="coords1">
                        <input type="hidden" size="4" id="hidden_x1" name="hidden_x1"/>
                        <input type="hidden" size="4" id="hidden_y1" name="hidden_y1"/>
                        当前截取的宽度：<input type="text" size="4" id="img1_w" name="img1_w" disabled="true"/>
                        当前截取的长度：<input type="text" size="4" id="img1_h" name="img1_h" disabled="true"/>
                    </div>
                </div>
                <input type="hidden" id="hiddenValue1"
                       value="<c:choose><c:when test="${img eq null || img eq ''}">/static/images/default.jpg</c:when><c:otherwise>${img}</c:otherwise></c:choose>">

                <div>
                    <span id="uploadButton1">上传图片</span>
                    <span id="loading1" style="display:none"><img src="/static/images/loading.gif"/></span>
                </div>
                <div>
                    <label style="color:blue">生成图片的长宽</label>
                    <label>宽度: <input type="text" size="4" id="img1_aw" name="img1_aw"
                                      class="default_input_singleline"/></label>
                    <label>长度: <input type="text" size="4" id="img1_ah" name="img1_ah"
                                      class="default_input_singleline"/></label>
                    <input type="button" id="createNewImg1" value="截取并保存图片">
                    <span id="loading_corp1" style="display:none"><img src="/static/images/loading.gif"/></span>
                </div>


                <div id="interface" style="margin: 1em 0;"></div>
                <input type="hidden" name="customIcon" class="default_input_singleline" size="64" id="iconURL"
                       value="${lineItem.displayInfo.getIconUrl()}">
                * 此处文字内容、数量，根据页面显示需要自由定义。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">自定义图标2：</td>
            <td class="edit_table_value_td">
                <div>
                    <img id="img_logo2"
                            <c:choose>
                                <c:when test="${img2 eq null || img2 eq ''}">src="/static/images/default.jpg" </c:when>
                                <c:otherwise>src="${uf:parseOrgImg(img2)}"</c:otherwise>
                            </c:choose> />
                </div>
                <div>
                    <div id="coords2">
                        <input type="hidden" size="4" id="hidden_x2" name="hidden_x2"/>
                        <input type="hidden" size="4" id="hidden_y2" name="hidden_y2"/>
                        当前截取的宽度：<input type="text" size="4" id="img2_w" name="img1_w" disabled="true"/>
                        当前截取的长度：<input type="text" size="4" id="img2_h" name="img1_h" disabled="true"/>
                    </div>
                </div>
                <input type="hidden" id="hiddenValue2"
                       value="<c:choose><c:when test="${img eq null || img eq ''}">/static/images/default.jpg</c:when><c:otherwise>${img}</c:otherwise></c:choose>">

                <div>
                    <span id="uploadButton2">上传图片</span>
                    <span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>
                </div>
                <div>
                    <label style="color:blue">生成图片的长宽</label>
                    <label>宽度: <input type="text" size="4" id="img2_aw" name="img1_aw"
                                      class="default_input_singleline"/></label>
                    <label>长度: <input type="text" size="4" id="img2_ah" name="img1_ah"
                                      class="default_input_singleline"/></label>
                    <input type="button" id="createNewImg2" value="截取并保存图片">
                    <span id="loading_corp2" style="display:none"><img src="/static/images/loading.gif"/></span>
                </div>


                <div id="interface2" style="margin: 1em 0;"></div>
                <input type="hidden" name="customIcon2" class="default_input_singleline" size="64" id="iconURL2"
                       value="${lineItem.displayInfo.getIconUrl()}">
                * 此处文字内容、数量，根据页面显示需要自由定义。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">自定义描述：</td>
            <td class="edit_table_value_td">
                <c:if test="${errorMsgMap['itemDesc'] != null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemDesc']}"
                                                            bundle="${error}"/></span><br>
                </c:if>
                <textarea rows="3" cols="100" name="customDesc"
                          class="default_input_singleline">${lineItem.displayInfo.getDesc()}</textarea>
                * 此处文字内容、数量，根据页面显示需要自由定义。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息1：</td>
            <td nowrap="" class="edit_table_value_td">
                <input type="text" value="<c:out value="${lineItem.displayInfo.extraField1}"/>" size="64"
                       class="default_input_singleline" name="extrafield1">
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息2：</td>
            <td nowrap="" class="edit_table_value_td">
                <input type="text" value="<c:out value="${lineItem.displayInfo.extraField2}"/>" size="64"
                       class="default_input_singleline" name="extrafield2">
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息3：</td>
            <td nowrap="" class="edit_table_value_td">
                <input type="text" value="<c:out value="${lineItem.displayInfo.extraField3}"/>" size="64"
                       class="default_input_singleline" name="extrafield3">
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息4：</td>
            <td nowrap="" class="edit_table_value_td">
                <input type="text" value="<c:out value="${lineItem.displayInfo.extraField4}"/>" size="64"
                       class="default_input_singleline" name="extrafield4">
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息5：</td>
            <td nowrap="" class="edit_table_value_td">
                <input name="extrafield5" type="text" class="default_input_singleline"
                       value="${lineItem.displayInfo.extraField5}">
            </td>
        </tr>
        <tr>
            <td height="1" colspan="2" class="default_line_td"></td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr align="center">
            <td colspan="2">
                <input name="Submit" type="submit" class="default_button" value="提交">
                <input name="Reset" type="button" class="default_button" value="返回"
                       onclick="javascript:window.history.go(-1);">
            </td>
        </tr>
    </form>
</table>
<script language="JavaScript" type="text/JavaScript">


    $(window).load(function() {
        var corpObj = $.Jcrop('#img_game_logo', {
                    bgFade:     true,
                    onChange:   function(c) {
                        $('#hidden_x1').val(c.x);
                        $('#hidden_y1').val(c.y);
                        $('#img1_aw').val(c.xMax);
                        $('#img1_ah').val(c.yMax);
                        $('#img1_w').val(c.w);
                        $('#img1_h').val(c.h);
                    },
                    onSelect:   function(c) {
                        $('#hidden_x1').val(c.x);
                        $('#hidden_y1').val(c.y);
                        $('#img1_aw').val(c.xMax);
                        $('#img1_ah').val(c.yMax);
                        $('#img1_w').val(c.w);
                        $('#img1_h').val(c.h);
                    },
                    onRelease:  function() {
                        $('#coords1 input').val('');
                    },
                    bgOpacity: 0.3,
                    setSelect: [ 0, 0, 70, 70 ]
                });
        var viewLineOption = {
            buttonId:'uploadButton1',
            imgId:'img_game_logo',
            corpObj:corpObj,
            fileDialogComplete:fileDialogComplete,
            uploadStart:uploadStart,
            uploadSuccess:uploadSuccess,
            uploadComplete:uploadComplete
        };
        var swfu = createSwfUploadObj(viewLineOption);

        var option = {
            areaId :'interface',
            sectionid:'anim_buttons',
            corpclickHandler:function(v, corpObj) {
                corpObj.setSelect(v);
                var ratioValue = (v[2] - v[0]) / (v[3] - v[1]);
                corpObj.setOptions({ aspectRatio: ratioValue });
                $('#img1_aw').val(v[2] - v[0]);
                $('#img1_ah').val(v[3] - v[1]);
                $('#img1_w').val(v[2] - v[0]);
                $('#img1_h').val(v[3] - v[1]);
                return false;
            },
            corpObj:corpObj
        }
        createJCropArea(option);
        $("#createNewImg1").click(function() {
            $('#loading_corp1').css('display', '');
            var imgNew = $('#hiddenValue1').val();
            var httpImgNew = "http://" + imgNew.substring(1, 5) + ".${DOMAIN}" + imgNew;
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
                        url: "/json/viewline/imgcropper",
                        type:'post',
                        data:{filename:imgNew,x1:xAxias,y1:yAxias,w:width,h:height,at:at,rw:$('#img1_aw').val(),rh:$('#img1_ah').val()},
                        success:function(data) {

                            var jsonObj = eval('(' + data + ')');
                            if (jsonObj.status_code == '1') {
                                $('#img_game_logo').attr('src', genImgDomain(jsonObj.result[0], '${DOMAIN}'));
                                corpObj.setImage(genImgDomain(jsonObj.result[0], '${DOMAIN}'));
                                $('#iconURL').val(jsonObj.result[0]);
                                $('#hiddenValue1').val(jsonObj.result[0]);
                            } else {
                                alert('切割失败!');
                            }
                            $('#loading_corp1').css('display', 'none');
                        }
                    });
        });
        $("input[name='img']").click(function() {
            var imgurl = $("input[name='img']:checked").val();
            $("#img_game_logo").attr('src', genImgDomain(imgurl, '${DOMAIN}'));

            //假如所修改的图片不存在，则重新new一个
            corpObj.setImage(genImgDomain(imgurl, '${DOMAIN}'));
            $('#iconURL').val(imgurl);

            $('#hiddenValue1').val(imgurl);

        });

            var corpObj2 = $.Jcrop('#img_logo2', {
            bgFade:     true,
            onChange:   function(c) {
                $('#hidden_x2').val(parseInt(c.x));
                $('#hidden_y2').val(parseInt(c.y));
                $('#img2_aw').val(c.xMax);
                $('#img2_ah').val(c.yMax);
                $('#img2_w').val(parseInt(c.w));
                $('#img2_h').val(parseInt(c.h));
            },
            onSelect:   function(c) {
                $('#hidden_x2').val(parseInt(c.x));
                $('#hidden_y2').val(parseInt(c.y));
                $('#img2_aw').val(c.xMax);
                $('#img2_ah').val(c.yMax);
                $('#img2_w').val(parseInt(c.w));
                $('#img2_h').val(parseInt(c.h));
            },
            onRelease:  function() {
                $('#coords2 input').val('');
            },
            bgOpacity: 0.3,
            setSelect: [ 0, 0, 180, 180 ]
        });
    var viewLineOption2 = {
        buttonId:'uploadButton2',
        imgId:'img_logo2',
        corpObj:corpObj2,
        fileDialogComplete:fileDialogComplete,
        uploadStart:uploadStart2,
        uploadSuccess:uploadSuccess2,
        uploadComplete:uploadComplete2
    };
    var swfu2 = createSwfUploadObj(viewLineOption2);
    var option2 = {
        areaId :'interface2',
        sectionid:'anim_buttons2',
        corpclickHandler:function(v, corpObj) {
            corpObj.setSelect(v);
            var ratioValue = (v[2] - v[0]) / (v[3] - v[1]);
            corpObj.setOptions({ aspectRatio: ratioValue });
            $('#img2_aw').val(v[2] - v[0]);
            $('#img2_ah').val(v[3] - v[1]);
            $('#img2_w').val(v[2] - v[0]);
            $('#img2_h').val(v[3] - v[1]);
            return false;
        },
        corpObj:corpObj2
    }
    createJCropArea(option2);
    $("#createNewImg2").click(function() {
        $('#loading_corp2').css('display', '');
        var imgNew = $('#hiddenValue2').val();
        var httpImgNew = "http://" + imgNew.substring(1, 5) + ".${DOMAIN}" + imgNew;
        var xAxias = $('#hidden_x2').val();
        var yAxias = $('#hidden_y2').val();
        var width = $('#img2_w').val();
        var height = $('#img2_h').val();

        if (xAxias == '' && yAxias == '' && width == '' && height == '') {
            alert("请选择一个区域");
            $('#loading_corp2').css('display', 'none');
            return false;
        }

        $.ajax({
                    url: "/json/viewline/imgcropper",
                    type:'post',
                    data:{filename:imgNew,x1:xAxias,y1:yAxias,w:width,h:height,at:at,rw:$('#img2_aw').val(),rh:$('#img2_ah').val()},
                    success:function(data) {

                        var jsonObj = eval('(' + data + ')');
                        if (jsonObj.status_code == '1') {
                            $('#img_logo2').attr('src', genImgDomain(jsonObj.result[0], '${DOMAIN}'));
                            corpObj2.setImage(genImgDomain(jsonObj.result[0], '${DOMAIN}'));
                            $('#iconURL2').val(jsonObj.result[0]);
                            $('#hiddenValue2').val(jsonObj.result[0]);
                        } else {
                            alert('切割失败!');
                        }
                        $('#loading_corp2').css('display', 'none');
                    }
                });
    });
    });

    var fileDialogComplete = function(numFilesSelected, numFilesQueued) {
        try {
            this.startUpload();
        } catch (ex) {
            this.debug(ex);
        }
    }
    var uploadStart = function (file) {
        $('#loading1').css('display', '');
    }
    var uploadSuccess = function (file, serverData) {
        try {
            var jsonData = eval('(' + serverData + ')');
            if (jsonData.status_code == "1") {
                var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
                $('#' + this.customSettings.imgId).attr('src', largeLogoSrc);

                var corpObj = this.customSettings.corpObj;
                setTimeout(function() {
                    corpObj.setImage(largeLogoSrc);
                }, 200)
                $('#iconURL').val(jsonData.result[0]);
                $('#hiddenValue1').val(jsonData.result[0]);
                alert('上传成功');
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
    var uploadComplete = function (file) {
        try {
            if (this.getStats().files_queued <= 0) {
                $('#loading1').css('display', 'none');
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    var uploadStart2 = function (file) {
    $('#loading2').css('display', '');
}
    var uploadSuccess2 = function (file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#' + this.customSettings.imgId).attr('src', largeLogoSrc);

            var corpObj = this.customSettings.corpObj;
            setTimeout(function() {
                corpObj.setImage(largeLogoSrc);
            }, 200)
            $('#iconURL2').val(jsonData.result[0]);
            $('#hiddenValue2').val(jsonData.result[0]);
            alert('上传成功');
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if(jsonData.msg==''){
                    alert('上传失败');
                }else{
                   alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}
    var uploadComplete2 = function (file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $('#loading2').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}

    function sp() {
        var tex = document.getElementById('subject').value;
        var nun = tex.length;
        var spa = document.getElementById('subjectspan');
        spa.innerHTML = nun;
    }

</script>