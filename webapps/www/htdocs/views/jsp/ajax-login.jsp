<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
    $(function() {
        $("#loginForm").validate({
                    rules: {
                        account: {required:true,email:true},
                        password: {required: true,rangelength: [6,18]}
                    },
                    messages: {
                        account: {required:"请输入你的邮箱", email:"请输入正确的邮箱地址"},
                        password:{required:tipsText.userSet.user_userpwd_notnull,rangelength:tipsText.userSet.user_userpwd_length}
                    },
                    showErrors: showErrors//自定义错误提示
                });

    })
    document.onkeydown = function(e) {
        if (window.event) {
            e = window.event;
        }
        var int_keycode = e.charCode || e.keyCode;
        if (int_keycode == '13') {
            ajaxlogin();
//                return false;
        }
    };
    function showErrors() {
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            var elename = this.idOrName(error.element);
            if (elename == "account") {
                if ($("#account").parent().find('span').length == 0) {
                    $("#account").parent().append('<span style="color:#7D0707; display:block; float:left;">' + error.message + '</span>');
                } else {
                    $("#account").parent().find('span').text(error.message);
                }
            }
            if (elename == "password") {
                if ($("#password").parent().find('span').length == 0) {
                    $("#password").parent().append('<span style="color:#7D0707; display:block; float:left;">' + error.message + '</span>');
                } else {
                    $("#password").parent().find('span').text(error.message);
                }
            }
        }
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            if (suc.id == "account") {
                $("#account").parent().find('span').remove();
            }
            if (suc.id == "password") {
                $("#password").parent().find('span').remove();
            }
        }
    }
    function ajaxlogin() {
        $("#loginForm").ajaxForm(function(req) {
            var jsonObj = eval('(' + req + ')');
            //弹出层提示
            if (jsonObj.status_code == "1") {
                logincallback(callbackOption);
                window.location.reload();
            }else{
                if ($("#password").parent().find('span').length == 0) {
                    $("#password").parent().append('<span style="color:#7D0707; display:block; float:left;">' + jsonObj.msg + '</span>');
                } else {
                    $("#password").parent().find('span').text(jsonObj.msg);
                }
            }
        });
        $("#loginForm").submit();
    }
</script>

<div style="position: absolute;  z-index: 1200; left: 0; top:0;">
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
                <div class="div_login">
                    <div class="div_login_t">

                        <a href="#" class="close" style="display:block;float:right; margin-top:0px;"
                           onclick="hideMask();"></a>

                        <h3 style="display:block; width:160px;">请先登录</h3>
                    </div>
                    <form action="${ctx}/json/login" method="post" id="loginForm">
                        <div class="div_login_m">
                            <ul>
                                <li>
                                    <span class="left">邮　箱：</span>
                                    <span class="right">
                                        <input id="account" name="account" type="text" tabindex="1" class="text"/>
                                    </span>
                                    <b id="mltips"></b>
                                </li>
                                <li>
                                    <span class="left">密　码：</span>
                                    <span class="right">
                                        <input id="password" name="password" type="password" tabindex="2" class="text"
                                               maxlength="16"/>
                                    </span>
                                </li>
                                <div id="pwtips"></div>
                                <li class="reset">
                                    <span class="left">&nbsp;</span>
                                <span class="right">
                                <span class="fl">
                    <input type="checkbox" checked="checked" id="ls" name="persistent" value="1">
                    <label for="ls">记住我的登录状态</label>
                </span> <span class="fr"><a href="${ctx}/security/pwd/forgot" target="_parent">忘记密码?</a></span>
                                </span>

                                </li>
                                <li>
                                    <span class="left">&nbsp;</span>
                                    <span class="right">
                                        <input name="" id="ajaxLogin" type="button" value="登　录" class="but_n1"
                                               onclick="ajaxlogin();"/>

                                    </span>
                                </li>
                                <li><span class="left">&nbsp;</span><span class="right"><a href="${ctx}/registerpage">还没有账号，现在注册</a></span>
                            </ul>
                        </div>
                    </form>
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
