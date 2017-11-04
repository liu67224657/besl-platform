/**
 * Created by renhuimin on 2016/11/21.
 */
var load = function (){
    var mod = {
        init: function () {
            this.pageInit();
            this.registerInit();//注册初始化
            this.loginInit();//登录初始化
            this.forgotPassword();//找回密码初始化
            this.captcha = false;
        }, pageInit: function () {
            var a = $('.l-captcha').html();
            $('#words').append( $('.l-captcha').html());

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
                $('.captcha-box').hide();
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
                mod.captcha = false;
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
                                if (type == '1') {
                                    location.href = "http://m." + joyconfig.DOMAIN + "/mygift/m";
                                } else {
                                    location.reload();
                                }
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
                mod.validateRPhone("r")//注册手机校验
            });
            $("#r_code").blur(function () {
                mod.validateRCode("r");//注册验证码校验
            });
            $("#r_password").blur(function () {
                mod.validateRPassword("r");//注册密码校验
            });

            $("#r_realname").blur(function () {
                mod.validateRRealname("r");//真实姓名校验
            });
            $("#r_id").blur(function () {
                mod.validateRId("r");//身份证号校验
            });
            //$("#r_confirm_password").blur(function () {
            //    mod.validateRComfirmPass("r");//注册确认密码校验
            //});
            $("#r_nick").blur(function () {
                mod.validateRNick();//注册昵称校验
            });


            $("#r_phone").focus(function () {
                $("#r_phone_i").text("");
            });
            $("#r_realname").focus(function () {
                $("#r_realname_i").text("");
            });
            $("#r_id").focus(function () {
                $("#r_id_i").text("");
            });
            $("#r_code").focus(function () {
                $("#r_code_i").text("");
            });
            $("#r_password").focus(function () {
                $("#r_password_i").text("");
            });
            //$("#r_confirm_password").focus(function () {
            //    $("#r_confirm_password_i").text("");
            //});
            $("#r_nick").focus(function () {
                $("#r_nick_i").text("");
            });
            mod.sendMobileCode("r");//发送验证码


            var registerlock = false;
            $("#register").click(function () {

                var valid1 = mod.validateRPhone("r");
                var valid2 = mod.validateRCode("r");
                var valid3 = mod.validateRPassword("r");
                var valid6 = mod.validateRRealname("r");
                var valid7 = mod.validateRId("r");
                //var valid4 = mod.validateRComfirmPass("r");
                var valid5 = mod.validateRNick();
                if (!valid1 || !valid2 || !valid3  || !valid5 || !valid6 || !valid7) {
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
                    var userRealname = $("#r_realname").val();
                    var userIdcard = $("#r_id").val();

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
                            logindomain: "mobile",
                            userIdcard:userIdcard,
                            userRealname:userRealname
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
            //$("#f_confirm_password").blur(function () {
            //    mod.validateRComfirmPass("f");//注册确认密码校验
            //});

            var forgotlock = false;
            $("#forgot").click(function () {
                var valid1 = mod.validateRPhone("f");
                var valid2 = mod.validateRCode("f");
                var valid3 = mod.validateRPassword("f")
                //var valid4 = mod.validateRComfirmPass("f");
                if (!valid1 || !valid2 || !valid3) {
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
                // alert("aaaaaaaaaaaaaa"+mod.captcha)
                if(!mod.validateRPhone(id)) return false;//注册手机校验
                if(!mod.captcha){
                    $('.captcha-box').show();
                    return false;
                }


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
                            mobile: phone,
                            checkmobile:"true",
                            luotestresponse:$("#luotest_response_mima").val()
                        },
                        success: function (res) {
                            mod.captcha=false;
                            var req = res[0];

                            if (req.rs == '-10124') {
                                $("#" + id + "_phone_i").text("该手机号已存在");
                                bool = false;
                                LUOCAPTCHA.reset();
                                $("#luotest_response_mima").val("");
                                return;
                            }

                            if (req.rs == '-10103') {
                                $("#" + id + "_phone_i").text("*用户不存在")
                                bool = false;
                                return;
                            }

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

                            if (req.rs == '1') {

                            } else if (req.rs == '-10201') {
                                $("#" + id + "_phone_i").text("*发送超过次数上限");
                                bool = false;
                            } else if (req.rs == '-10207') {
                                $("#" + id + "_phone_i").text("*发送失败");
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
        }, validateRRealname:function(id){
            var name = $("#" + id + "_realname").val();
            if (name == "") {
                $("#" + id + "_realname_i").text("*您还没有输入真实姓名");
                return false;
            }
            if(!/^[\u4e00-\u9fa5]+$/.test(name)){
                $("#" + id + "_realname_i").text("*请输入中文名字");
                return false;
            }
            if(name.length<2){
                $("#" + id + "_realname_i").text("*名字不少于2个字");
                return false;
            }

            return true;
        }, validateRId:function(id){
            var userId = $("#" + id + "_id").val();
            if (userId == "") {
                $("#" + id + "_id_i").text("*您还没有输入身份证号");
                return false;
            }
            if (!(/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/.test(userId))) {
                $("#" + id + "_id_i").text("*您输入身份证号不正确");
                return false;
            }

            return true;
        },
        validateRPassword: function (id) {
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
        },getResponse:function(reg){
            if(!reg) return false;
            mod.captcha =true;

            $("#luotest_response_mima").val(reg);
            $('.captcha-box').hide();
            LUOCAPTCHA.reset();
            //if(!mod.validateRPhone("r"))return false;
            //$('#r_sendmobile').addClass('code-btn').removeClass('code-not');


        }
    };
    mod.init();
    return { mod : mod }
}();

var type = '';
function redirectLogin(url) {
    window.location.href = url + escape(window.location.href);
}

function loginDiv() {
    $('.login-box').show();
    $('.mask-box').show();
    $('.loginbarBox').show();
    type = "";
}

function mygiftLoginDiv() {
    loginDiv();
    type = 1;
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