;


var hostname = window.location.hostname;
var wwwHost = 'http://www.joyme.' + hostname.substring(hostname.lastIndexOf('.') + 1, hostname.length) + '/';
var apiHost = 'http://api.joyme.' + hostname.substring(hostname.lastIndexOf('.') + 1, hostname.length) + '/';
var passportHost = 'http://passport.joyme.' + hostname.substring(hostname.lastIndexOf('.') + 1, hostname.length) + '/';
(function () {
    var mod = {
        init: function () {
            var html = '<div class="success-btn">注册成功</div><div class="loginbarBox"><div class="login-box login-bg"><h1>账号登录</h1><span class="close-icon">关闭</span><div class="login-con">' +
                '<div class="login-text"><label><span>手机号：</span><input type="text" id="l_phone"></label><i class="text-error on" placeholder="手机/邮箱" id="l_phone_i"></i></div>' +
                '<div class="login-text"><label><span>密&nbsp;码：</span><input type="password" id="l_password"></label><i class="text-error on" id="l_password_i"></i></div>' +
                '<div class="w-200 fn-clear"><label class="check-me"><input type="checkbox" name="check" checked="cheched" id="remember"><i></i>记住我</label><a href="javascript:void(0);" class="password fn-r">忘记密码</a></div>' +
                '<div class="login-btn" id="login">登录</div><div class="third-login"><span class="third-bg"></span><div>' +
                '<a href="' + passportHost + '/auth/thirdapi/qq/bind?reurl=' + escape(window.location.href) + '" class="qq-icon">QQ</a >' +
                '<a href="' + passportHost + '/auth/thirdapi/sinaweibo/bind?reurl=' + escape(window.location.href) + '" class="sina-icon">weibo</a ></div></div></div>' +
                '<a href="javascript:;" class="register register-mask mask-link fn-r">没有账号？去注册</a ></div><div class="register-box login-bg"><h1>手机注册</h1><span class="close-icon">关闭</span>' +
                '<div class="login-con"><div class="login-text"><label><span>手机号：</span><input type="text" placeholder="仅支持中国大陆" id="r_phone" maxlength="11"/></label>' +
                '<i class="text-error on" id="r_phone_i"></i></div>' +
                '<div class="login-text"><label class="code-text"><span>验证码：</span><input type="text" id="r_code" maxlength="5"><button class="code-btn" id="r_sendmobile">发送验证码</button></label>' +
                '<i class="text-error on" id="r_code_i"></i></div>' +
                '<div class="login-text"><label><span>密&nbsp;码：</span><input type="password" id="r_password"></label>' +
                '<i class="text-error on" id="r_password_i"></i></div><div class="login-text"><label><span>确认密码：</span><input type="password" id="r_confirm_password"></label><i class="text-error on" id="r_confirm_password_i"></i></div>' +
                '<div class="login-text"><label><span>昵&nbsp;称：</span><input type="text" id="r_nick"></label><i class="text-error on" id="r_nick_i"></i></div>' +
                '<div class="register-btn w-200" id="register">注册</div><div class="w-200" style="text-align: center;">' +
                '<label class="check-me"><input type="checkbox" name="check" id="agree" checked="checked"><i></i>我已同意<a href="http://www.joyme.com/help/law" target="_blank">《着迷注册服务协议》</a ></label></div></div>' +
                '<a href="javascript:;" class="login login-mask mask-link fn-r">已有账号？去登录</a ></div><div class="password-box login-bg" ><h1>找回密码</h1><span class="close-icon">关闭</span><div class="login-con">' +
                '<div class="login-text"><label><span>手机号：</span><input type="text" placeholder="仅支持中国大陆" id="f_phone"></label><i class="text-error on" id="f_phone_i"></i></div>' +
                '<div class="login-text"><label class="code-text"><span>验证码：</span><input type="text" id="f_code" maxlength="5"><button class="code-btn" id="f_sendmobile">获取验证码</button></label><i class="text-error on" id="f_code_i"></i></div>' +
                '<div class="login-text"><label><span>新密码：</span><input type="password" id="f_password" ></label><i class="text-error on" id="f_password_i"></i></div>' +
                '<div class="login-text"><label><span>确认密码：</span><input type="password" id="f_confirm_password" ></label><i class="text-error on" id="f_confirm_password_i"></i></div>' +
                '<div class="sure-btn w-200" id="forgot">确定</div></div><a href="javascript:;" class="login login-mask mask-link fn-r">已有账号？去登录</a ></div><div class="mask-box"></div></div>';

            $('body').append(html);
            this.pageInit();
            this.registerInit();//注册初始化
            this.loginInit();//登录初始化
            this.forgotPassword();//找回密码初始化
        }, pageInit: function () {

            var oMask = $('.mask-box'),
                loginBg = $('.login-bg'),
                wrap = $('.loginbarBox');

            function loginBox(config) {
                var oEle = $('.' + config),
                    oEleBox = $('.' + config + '-box');
                loginBg.hide();
                oEleBox.show();
                oMask.show();
                wrap.show();
                $(".login-box").find("input[type='text']").val("");
                $(".login-box").find("input[type='password']").val("");
                $(".password-box").find("input[type='text']").val("");
                $(".password-box").find("input[type='password']").val("");
                $(".register-box").find("input[type='text']").val("");
                $(".register-box").find("input[type='password']").val("");
                $(".text-error").text("");

            }

            $(document).click(function(e){
                var _con = $('.captcha-box'); // 设置目标区域
                if(!_con.is(e.target) && _con.has(e.target).length === 0){
                    _con.hide();
                }
            });

            $('.close-icon').click(function () {
                oMask.hide();
                loginBg.hide();
                wrap.hide();
                $('document').scrollTop(0);

                $(".login-box").find("input[type='text']").val("");
                $(".login-box").find("input[type='password']").val("");
                $(".password-box").find("input[type='text']").val("");
                $(".password-box").find("input[type='password']").val("");
                $(".register-box").find("input[type='text']").val("");
                $(".register-box").find("input[type='password']").val("");
                $(".text-error").text("");

            });
            $('.register-mask').click(function () {
                loginBox('register');
            });
            $('.login-mask').click(function () {
                loginBox('login');
            });
            $('.password').click(function () {
                loginBox('password');
            });

            $("#agree").click(function () {
                var agree = $("#agree").is(":checked");
                if (agree) {
                    $("#r_nick_i").text("");
                    return;
                }
            });


//            $(".login_box_comment").on("click", function () {

//            });

        }, loginInit: function () {
            $("#l_phone").focus(function () {
                $("#l_phone_i").text("");
            });
            $("#l_password").focus(function () {
                $("#l_password_i").text("");
            });

            var loginlock = false;
            $("#login").click(function () {
                var phone = $("#l_phone").val();
                var password = $("#l_password").val();
                if (phone == '') {
                    $("#l_phone_i").text("请输入账号");
                    return;
                }
                if (password == '') {
                    $("#l_password_i").text("请输入密码");
                    return;
                }


                if (!loginlock) {
                    loginlock = true;
                    var remember = $("#remember").is(":checked");

                    $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/platform/auth/login",
                        dataType: "jsonp",
                        jsonp: "callback",
                        jsonpCallback: "platformauthcallback",
                        data: {
                            loginkey: phone,
                            password: password,
                            remember: remember
                        },
                        success: function (res) {
                            var req = res[0];
                            loginlock = false;
                            if (req.rs == '1') {
                                location.reload();
                            } else if (req.rs == '-10126') {
                                window.location.href = "http://passport." + joyconfig.DOMAIN + "/auth/savenickpage?reurl=" + encodeURIComponent(window.location.href) + "&logindomain=" + req.logindomain;
                            } else {
                                $("#l_password_i").text("您输入的账号或密码有误");
                            }
                        }, error: function (req) {
                            loginlock = false;
                        }
                    });
                }
            });
            document.onkeydown = function (e) {
                var ev = document.all ? window.event : e;
                if (ev.keyCode == 13) {
                    $("#login").click();
                }
            }
        },
        registerInit: function () {
            var isRegister = true;
            $("#r_phone").blur(function () {
                mod.validateRPhone("r");//注册手机校验
            });
            $("#r_code").blur(function () {
                mod.validateRCode("r");//注册验证码校验
            });
            $("#r_password").blur(function () {
                mod.validateRPassword("r");//注册密码校验
            });
            $("#r_confirm_password").blur(function () {
                mod.validateRComfirmPass("r");//注册确认密码校验
            });
            $("#r_nick").blur(function () {
                mod.validateRNick();//注册昵称校验
            });


            $("#r_phone").focus(function () {
                $("#r_phone_i").text("");
            });
            $("#r_code").focus(function () {
                $("#r_code_i").text("");
            });
            $("#r_password").focus(function () {
                $("#r_password_i").text("");
            });
            $("#r_confirm_password").focus(function () {
                $("#r_confirm_password_i").text("");
            });
            $("#r_nick").focus(function () {
                $("#r_nick_i").text("");
            });
            mod.sendMobileCode("r");//发送验证码


            var registerlock = false;
            $("#register").click(function () {

                var valid1 = mod.validateRPhone("r");
                var valid2 = mod.validateRCode("r");
                var valid3 = mod.validateRPassword("r")
                var valid4 = mod.validateRComfirmPass("r");
                var valid5 = mod.validateRNick();
                if (!valid1 || !valid2 || !valid3 || !valid4 || !valid5) {
                    return;
                }
                var agree = $("#agree").is(":checked");
                if (!agree) {
                    $("#r_nick_i").text("您尚未同意《着迷注册服务协议》");
                    return;
                }


                if (!registerlock) {
                    var domain = $("#domain").val();
                    var phone = $("#r_phone").val();
                    var code = $("#r_code").val();
                    var password = $("#r_password").val();
                    var nick = $("#r_nick").val();

                    registerlock = true;
                    $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/platform/auth/register",
                        dataType: "jsonp",
                        jsonp: "callback",
                        jsonpCallback: "platformauthcallback",
                        data: {
                            loginkey: phone,
                            password: password,
                            nick: nick,
                            mobilecode: code,
                            logindomain: "mobile"
                        },
                        success: function (res) {
                            registerlock = false;
                            var req = res[0];
                            if (req.rs == '1') {
                                $(".success-btn").text("注册成功");
                                sucBtn();
                                setTimeout(function () {
                                    location.reload();
                                }, 1000);
                            } else if (req.rs == '-10126') {
                                $("#r_nick_i").text("不能输入敏感字符");
                                return;
                            } else if (req.rs == '-10112') {
                                $("#r_nick_i").text("昵称已存在");
                                return;
                            } else if (req.rs == '-10204') {
                                $("#r_code_i").text("您还没有发送验证码");
                                return;
                            } else if (req.rs == '-10206') {
                                $("#r_code_i").text("验证码错误");
                                return;
                            } else if (req.rs == '-10203') {
                                $("#r_phone_i").text("该手机号已经存在");
                                return;
                            } else if (req.rs == '-10103') {
                                $("#r_phone_i").text("该手机号已经存在");
                                return;
                            }
                            else {
                                alert("注册失败 错误编码：" + req.rs);
                            }
                        }
                    });
                }


            });
        }, forgotPassword: function () {
            $("#f_phone").blur(function () {
                mod.validateRPhone("f");//注册手机校验
            });
            $("#f_code").blur(function () {
                mod.validateRCode("f");
            });

            $("#f_password").blur(function () {
                mod.validateRPassword("f");//注册密码校验
            });
            $("#f_confirm_password").blur(function () {
                mod.validateRComfirmPass("f");//注册确认密码校验
            });

            var forgotlock = false;
            $("#forgot").click(function () {
                var valid1 = mod.validateRPhone("f");
                var valid2 = mod.validateRCode("f");
                var valid3 = mod.validateRPassword("f")
                var valid4 = mod.validateRComfirmPass("f");
                if (!valid1 || !valid2 || !valid3 || !valid4) {
                    return;
                }

                if (!forgotlock) {
                    var phone = $("#f_phone").val();
                    var code = $("#f_code").val();
                    var password = $("#f_password").val();
                    var comfrimPassword = $("#f_confirm_password").val();

                    forgotlock = true;
                    $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/platform/auth/recover/password",
                        dataType: "jsonp",
                        jsonp: "callback",
                        jsonpCallback: "platformauthcallback",
                        data: {
                            mobile: phone,
                            pwd: password,
                            mobilecode: code,
                            repeatpwd: comfrimPassword
                        },
                        success: function (res) {
                            forgotlock = false;
                            var req = res[0];
                            if (req.rs == '1') {
                                $(".success-btn").text("修改成功");
                                sucBtn();
                                $("#f_phone").val("");
                                $("#f_code").val("");
                                $("#f_password").val("");
                                $("#f_confirm_password").val("");
                                $(".close-icon").click();
                            } else if (req.rs == '-10121') {
                                $("#f_password_i").text("两次输入密码不一致");
                                return;
                            } else if (req.rs == '-10103') {
                                $("#f_phone_i").text("用户不存在");
                                return;
                            } else if (req.rs == '-10204') {
                                $("#f_code_i").text("您还没有发送验证码");
                                return;
                            } else if (req.rs == '-10206') {
                                $("#f_code_i").text("验证码错误");
                                return;
                            } else if (req.rs == '-10125') {
                                $("#f_password_i").text("新密码不能与之前一致");
                                return;
                            } else {
                                alert("注册失败 错误编码：" + req.rs);
                            }
                        }
                    });
                }

            });


            $("#f_phone").focus(function () {
                $("#f_phone_i").text("");
            });
            $("#f_password").focus(function () {
                $("#f_password_i").text("");
            });
            $("#f_confirm_password").focus(function () {
                $("#f_confirm_password_i").text("");
            });
            $("#f_code").focus(function () {
                $("#f_code_i").text("");
            });

            mod.sendMobileCode("f");
        }, sendMobileCode: function (id) {
            var wait = 60;
            var bool = false;
            $("#" + id + "_sendmobile").click(function () {
                var phone = $("#" + id + "_phone").val();
                if (phone == "") {
                    $("#" + id + "_phone_i").text("*您还没有输入手机号");
                    return;
                }
                var reg = /^0?(13|14|15|17|18)[0-9]{9}$/;
                if (!reg.test(phone)) {
                    $("#" + id + "_phone_i").text("*手机号格式不正确");
                    return;
                }

                if (!bool) {
                    bool = true;
                    $("#" + id + "_sendmobile").addClass("on");
                    $("#" + id + "_sendmobile").text("重新发送(" + wait + ")");

                    var miao = setInterval(function () {
                        if (wait == 0) {
                            clearInterval(miao);
                            $("#" + id + "_sendmobile").removeClass("on");
                            $("#" + id + "_sendmobile").text("发送验证码");
                            wait = 60;
                            bool = false;
                            return;
                        }
                        wait--;
                        $("#" + id + "_sendmobile").text("重新发送(" + wait + ")");
                    }, 1000);
                    var url = "http://api." + joyconfig.DOMAIN + "/platform/auth/mobile/sendcode";
                    if (id == 'f') {
                        url = "http://api." + joyconfig.DOMAIN + "/platform/auth/existsmobile/sendcode"
                    }
                    $.ajax({
                        url: url,
                        dataType: "jsonp",
                        jsonp: "callback",
                        jsonpCallback: "platformauthcallback",
                        data: {
                            mobile: phone
                        },
                        success: function (res) {
                            var req = res[0];
                            if (req.rs == '1') {

                            } else if (req.rs == '-10201') {
                                $("#" + id + "_phone_i").text("*发送超过次数上限");
                                bool = false;
                            } else if (req.rs == '-10207') {
                                $("#" + id + "_phone_i").text("*发送失败");
                                bool = false;
                            } else if (req.rs == '-10103') {
                                $("#" + id + "_phone_i").text("*用户不存在")
                                bool = false;
                            }
                        }, error: function (req) {
                            bool = false;
                        }
                    });

                }
            });
        }, validateRPhone: function (id) {
            var phone = $("#" + id + "_phone").val();
            if (phone == "") {
                $("#" + id + "_phone_i").text("*您还没有输入手机号");
                return false;
            }
            var reg = /^0?(13|14|15|17|18)[0-9]{9}$/;
            if (!reg.test(phone)) {
                $("#" + id + "_phone_i").text("*手机号格式不正确");
                return false;
            }

            return true;
        }, validateRCode: function (id) {
            var code = $("#" + id + "_code").val();
            if (code == "") {
                $("#" + id + "_code_i").text("*您还没有输入验证码");
                return false;
            }

            return true;
        }, validateRPassword: function (id) {
            var value = $("#" + id + "_password").val();
            if (value == "") {
                $("#" + id + "_password_i").text("*您还没有输入密码");
                return false;
            }
            var length = value.length;
            if (length < 6) {
                $("#" + id + "_password_i").text("*密码不能少于6位");
                return false;
            } else if (length > 16) {
                $("#" + id + "_password_i").text("*密码不能超过16位");
                return false;
            }
            var reg = /\s/;
            if (reg.test(value)) {
                $("#" + id + "_password_i").text("*密码不能包含空格");
                return false;
            }

            return true;
        }, validateRComfirmPass: function (id) {
            var value = $("#" + id + "_confirm_password").val();
            var value2 = $("#" + id + "_password").val();

            if (value == "") {
                $("#" + id + "_confirm_password_i").text("*您还没有输入确认密码");
                return false;

            }
            var length = value.length;
            if (length < 6) {
                $("#" + id + "_confirm_password_i").text("*密码不能少于6位");
                return false;
            } else if (length > 16) {
                $("#" + id + "_confirm_password_i").text("*密码不能超过16位");
                return false;
            }
            var reg = /\s/;
            if (reg.test(value)) {
                $("#" + id + "_confirm_password_i").text("*密码不能包含空格");
                return false;
            }
            if (value != value2) {
                $("#" + id + "_confirm_password_i").text("*两次输入密码不一致");
                return false;
            }

            return true;
        }, validateRNick: function () {
            var value = $("#r_nick").val();
            if (value == "") {
                $("#r_nick_i").text("*您还没有输入昵称");
                return false;
            }
            var reg = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/gi;
            if (!reg.test(value)) {
                $("#r_nick_i").text("*昵称不能含有特殊字符");
                return false;
            }

            var oValLength = 0;
            value.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = value.match(/[^ -~]/g) == null ? value.length : value.length + value.match(/[^ -~]/g).length;
            if (oValLength < 4) {
                $("#r_nick_i").text("*您输入的昵称过短");
                return false;
            } else if (oValLength > 20) {
                $("#r_nick_i").text("*您输入的昵称过长");
                return false;
            }
            return true;
        }
    };
    mod.init();
})();
function redirectLogin(url) {
    window.location.href = url + escape(window.location.href);
}

function loginDiv() {
    $('.login-box').show();
    $('.mask-box').show();
    $('.loginbarBox').show();
}

function registerDiv() {
    $('.register-box').show();
    $('.mask-box').show();
    $('.loginbarBox').show();
}

function sucBtn() {
    $(".success-btn").addClass("active");
    var timmer;
    clearTimeout(timmer);
    timmer = setTimeout(function () {
        $('.success-btn').removeClass('active');
    }, 1000);
}
