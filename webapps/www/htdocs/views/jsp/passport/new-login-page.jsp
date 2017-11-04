<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="loginbarBox">
    <div class="login-box login-bg">
        <h1>账号登录</h1>
        <span class="close-icon">关闭</span>

        <div class="login-con">
            <div class="login-text">
                <label>
                    <span>账&nbsp;号：</span>
                    <input type="text" id="l_phone"></label>
                <i class="text-error on" id="l_phone_i"></i>
            </div>

            <div class="login-text">
                <label>
                    <span>密&nbsp;码：</span>
                    <input type="password" id="l_password"></label>
                <i class="text-error on" id="l_password_i"></i>
            </div>

            <div class="w-200 fn-clear">
                <label class="check-me">
                    <input type="checkbox" name="check" checked="cheched"
                           id="remember">
                    <i></i>
                    记住我</label>
                <a
                        href="javascript:;" class="password fn-r">忘记密码</a>
            </div>

            <div class="login-btn" id="login">登录</div>
            <div class="third-login">
                <span class="third-bg"></span>

                <div>
                    <a href="javascript:redirectLogin('http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=');"
                       class="qq-icon">QQ</a>
                    <a href="javascript:redirectLogin('http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=');"
                       class="sina-icon">weibo</a>
                </div>
            </div>
        </div>
        <a href="javascript:;" class="register register-mask mask-link fn-r">没有账号？去注册</a>
    </div>
    <div class="register-box login-bg">
        <h1>手机注册</h1>
        <span class="close-icon">关闭</span>

        <div class="login-con">
            <div class="login-text">
                <label>
                    <span>手机号：</span>
                    <input type="text" placeholder="仅支持中国大陆" id="r_phone"
                           maxlength="11"/>
                </label>
                <i class="text-error on" id="r_phone_i"></i>
            </div>

            <div class="login-text">
                <label class="code-text">
                    <span>验证码：</span>
                    <input type="text" id="r_code"
                           maxlength="5">
                    <button class="code-btn" id="r_sendmobile">发送验证码</button>
                </label>
                <i class="text-error on" id="r_code_i"></i>
            </div>

            <div class="login-text">
                <label>
                    <span>密&nbsp;码：</span>
                    <input type="password" id="r_password"></label>
                <i
                        class="text-error on" id="r_password_i"></i>
            </div>
            <div class="login-text">
                <label>
                    <span>确认密码：</span>
                    <input type="password" id="r_confirm_password"></label>
                <i
                        class="text-error on" id="r_confirm_password_i"></i>
            </div>

            <div class="login-text">
                <label>
                    <span>昵&nbsp;称：</span>
                    <input type="text" id="r_nick"></label>
                <i
                        class="text-error on" id="r_nick_i"></i>
            </div>

            <div class="register-btn w-200" id="register">注册</div>
            <div class="w-200" style="text-align: center;">
                <label class="check-me">
                    <input type="checkbox" name="check"
                           id="agree"
                           checked="checked">
                    <i></i>
                    我已同意
                    <a
                            href="http://www.joyme.com/help/law" target="_blank">《着迷注册服务协议》</a>
                </label>
            </div>
        </div>
        <a href="javascript:;" class="login login-mask mask-link fn-r">已有账号？去登录</a>
    </div>
    <div class="password-box login-bg">
        <h1>找回密码</h1>
        <span class="close-icon">关闭</span>

        <div class="login-con">
            <div class="login-text">
                <label>
                    <span>手机号：</span>
                    <input type="text" placeholder="仅支持中国大陆"
                           id="f_phone"></label>
                <i class="text-error on"
                   id="f_phone_i"></i>
            </div>

            <div class="login-text">
                <label class="code-text">
                    <span>验证码：</span>
                    <input type="text" id="f_code"
                           maxlength="5">
                    <button class="code-btn" id="f_sendmobile">获取验证码</button>
                </label>
                <i class="text-error on" id="f_code_i"></i>
            </div>

            <div class="login-text">
                <label>
                    <span>新密码：</span>
                    <input type="password" id="f_password"></label>
                <i
                        class="text-error on" id="f_password_i"></i>
            </div>

            <div class="login-text">
                <label>
                    <span>确认密码：</span>
                    <input type="password" id="f_confirm_password"></label>
                <i
                        class="text-error on" id="f_confirm_password_i"></i>
            </div>

            <div class="sure-btn w-200" id="forgot">确定</div>
        </div>
        <a href="javascript:;" class="login login-mask mask-link fn-r">已有账号？去登录</a>
    </div>
    <div class="mask-box"></div>
</div>
