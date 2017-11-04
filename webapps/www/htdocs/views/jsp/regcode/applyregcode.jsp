<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/views/jsp/common/taglibs.jsp"%>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>申请注册码 ${jmh_title}</title>
    <style>
    body{background:url(${URL_LIB}/static/default/img/loginbg2.jpg) top center no-repeat; height: 100%; }
    </style>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/login.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/mask.css?${version}"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/googleStatistics.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery.form.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common.js?${version}"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/Tips.js?${version}"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            //申请验证
            $("#introduce").keydown(function() {
                checkInputLength(300, 'introduce', 'introduce_num');
            });
            $("#introduce").keyup(function() {
                checkInputLength(300, 'introduce', 'introduce_num');
            });
            $('#form_apply').validate({
                        rules:{
                            email:{ required:true,email:true,maxlength:128},
                            introduce:{verifyWord:''}
                        },
                        messages:{
                            email:{ required:'<fmt:message key="regcode.apply.email.not.null" bundle="${userProps}"/>',email:'<fmt:message key="regcode.apply.email.illegal" bundle="${userProps}"/>',maxlength:'<fmt:message key="regcode.email.maxlength" bundle="${userProps}"/>'},
                            introduce:{}
                        },
                        showErrors: showErrors
                    });
            $("#apply_submit").click(function() {
                if ($('#introduce').val() == '<fmt:message  key="regcode.apply.introduce.tips" bundle="${userProps}"/>') {
                    $('#introduce').val('');
                }
            })
            $('#form_apply').ajaxForm(function(data) {
                var result = eval('(' + data + ')');
                if (result.status_code == '1') {
                    if (result.msg != null && result.msg.length != 0) {
                        $('#mask_info').html(result.msg);
                    }

                    $("#mask_success").fadeIn();
                    $("#form_apply")[0].reset();
                    setTimeout('hideMask()', 1000);
                    return false;
                }
                return false;
            });
        });


        $.validator.addMethod('verifyWord', function(value, element, params) {
            var result = true;
            $.ajax({
                        url:'${ctx}/json/validate/postword',
                        type:'post',
                        data:{word:value},
                        async:false,
                        success: function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });
            return result;
        }, '<fmt:message key="user.word.illegl" bundle="${userProps}"/>');

        //自定义提示方法
        function showErrors() {
            //验证失败处理
            for (var i = 0; this.errorList[i]; i++) {
                var error = this.errorList[i];
                var elename = this.idOrName(error.element);
                $("#" + elename + "_tips").html('<span><em>' + error.message + '</em></span>');
                $("#" + elename + "_ok").css({"display":"none"});
            }
            //验证成功处理
            for (var i = 0; this.successList[i]; i++) {
                var suc = this.successList[i];
                $("#" + suc.id + "_tips").html('');
                $("#" + suc.id + "_ok").css({"display":"inline"});
            }
        }

        function hideMask() {
            $("#mask_success").fadeOut(1000, function() {
                window.location.href = "${ctx}/login";
            });
        }
    </script>
</head>
<body>

	<div id="login">
    <!-- 遮罩 -->
    <div id="mask_success" style="position: absolute;  z-index: 1200; left: 380px; top: 350px;display:none">
        <table border="0" cellpadding="0" cellspacing="0" class="div_warp">
            <tbody>
            <tr>
                <td class="top_l"></td>
                <td class="top_c"></td>
                <td class="top_r"></td>
            </tr>
            <tr>

                <td class="mid_l"></td>
                <td class="mid_c">
                    <div class="div_cont3" style="">

                        <p id="mask_info" class="p1" style="color:#333">申请成功</p>
                    </div>
                </td>
                <td class="mid_r"></td>
            </tr>

            <tr>
                <td class="bottom_l"></td>
                <td class="bottom_c"></td>
                <td class="bottom_r"></td>
            </tr>
            </tbody>
        </table>
    </div>
    <!-- 遮罩 -->
        <form id="form_apply" action="${ctx}/regcode/apply" method="post">
        <div class="reg_zm" >
        	<div class="reg_logo"></div>
            <div class="login_tab"><a href="${ctx}/login" class="log"></a></div>
            <div class="reg_cont"style=" margin-top:10px;">
            <h3>本站目前正在封闭测试中，如果您想参与，请留下您的Email。</h3>
            	<div class="login_bg1" style="margin-left:40px; display:inline;">
                	<div class="login_l login_lbg">
                    	<input id="email" name="email" type="text" class="mailtext" onblur="if(this.value==''){this.value='邮箱';this.style.color='#b9b5b5';}" onclick="if(this.value=='邮箱'){this.value='';this.style.color='#555';} " value="邮箱" />
                    </div>
                     <div id="email_ok" class="text_true" style="display:none"></div>
                    <div id="email_tips" class="login_tips">
                    </div>
                </div>

                <div class="invite_text">
                    <textarea name="introduce" cols="" rows="" class="text" id="introduce"
                          onblur="if(this.value==''){this.value='<fmt:message key="regcode.apply.introduce.tips" bundle="${userProps}"/>';this.style.color='#999';}"
                          onclick="if(this.value=='<fmt:message key="regcode.apply.introduce.tips" bundle="${userProps}"/>'){this.value='';this.style.color='#999';} "><fmt:message
                        key="regcode.apply.introduce.tips" bundle="${userProps}"  style="font-family:Tahoma, '宋体';"></textarea><span id="introduce_num">限<b>300</b>字内</span>
                    <div id="introduce_tips" class="login_tips">
                    </div>
                </div>
                 <a href="javascript:void(0)" class="invite" onclick="javascript:$('#form_apply').submit()">申请注册码</a>
            </div>
        </div>
        </form>
	</div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
<script type="text/javascript" src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(1);
</script>
<div style="width:0px; height:0px; overflow:hidden;">
    <script src="http://s19.cnzz.com/stat.php?id=3214014&web_id=3214014&show=pic" language="JavaScript"></script>
</div>
</body>
</html>