<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jslibs.jsp" %>


<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
document.writeln('<link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/cms/jmsy/logincont/login201611.css">');
document.writeln('<script src="${URL_STATIC}/tools/luosimao/api.js"></script>');
document.writeln('<div class="joyme-loginbar fn-right">');
<c:choose>
    <c:when test="${userCenterSession==null}">
        <%--document.writeln('<a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl='+escape(window.location.href)+'" class="weibo-login" title="微博登录">微博登录</a>');--%>
        <%--document.writeln('<a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl='+escape(window.location.href)+'" class="qq-login" title="QQ登录">QQ登录</a>');--%>
        <%--document.writeln(' </div>');--%>
        document.writeln('<input type="hidden" value="${DOMAIN}" id="domain"/>');
        document.writeln('<div class="nav-box">');
        document.writeln('<div class="loginbar">');
        document.writeln('<a href="javascript:;" class="login login-mask">登录</a>');
        document.writeln('<a href="javascript:;" class="register register-mask ">注册</a>');
        document.writeln('</div>');
        document.writeln('</div>');
        document.writeln('<div class="success-btn">注册成功</div>');

        <!-- 登陆 开始 -->
        document.writeln('<div class="login-box login-bg">');
        document.writeln('<h1>账号登录</h1>');
        document.writeln('<span class="close-icon">关闭</span>');
        document.writeln('<div class="login-con">');
        document.writeln('<div class="login-text">');
        document.writeln('<label><span>账&nbsp;号：</span><input type="text" placeholder="手机/邮箱" id="l_phone"></label>');
        document.writeln('<i class="text-error on" id="l_phone_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="login-text">');
        document.writeln('<label ><span>密&nbsp;码：</span><input type="password" id="l_password"></label>');
        document.writeln('<i class="text-error on" id="l_password_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="w-200 fn-clear">');
        document.writeln('<label class="check-me"><input type="checkbox" name="check" checked="cheched" id="remember"><i></i>记住我</label>');
        document.writeln('<a href="javascript:;" class="password fn-r">忘记密码</a>');
        document.writeln('</div>');
        document.writeln('<div class="login-btn w-200" id="login">登录</div>');
        document.writeln('<div class="third-login">');
        document.writeln('<span class="third-bg"></span>');
        document.writeln('<div>');
        document.writeln('<a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl='+escape(window.location.href)+'" class="qq-icon">QQ</a>');
        document.writeln('<a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl='+escape(window.location.href)+'" class="sina-icon">weibo</a>');
        document.writeln('</div>');
        document.writeln('</div>');
        document.writeln('</div>');
        document.writeln('<a href="javascript:;" class="register register-mask mask-link fn-r">没有账号？去注册</a>');
        document.writeln('</div>');
        <!-- 登陆 结束 -->
        <!-- 注册 开始 -->
        document.writeln('<div class="register-box login-bg">');
        document.writeln('<h1>手机注册</h1>');
        document.writeln('<span class="close-icon">关闭</span>');
        document.writeln('<div class="login-con">');
        document.writeln('<div class="login-text">');
        document.writeln('<label><span>手机号：</span><input type="text" placeholder="仅支持中国大陆" id="r_phone" maxlength="11"/></label>');
        document.writeln('<i class="text-error on" id="r_phone_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="login-text">');
        document.writeln('<label class="code-text"><span>验证码：</span><input type="text" id="r_code" maxlength="5"><button class="code-btn" id="r_sendmobile">发送验证码</button></label>');
        document.writeln('<i class="text-error on" id="r_code_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="login-text">');
        document.writeln('<label><span>密&nbsp;码：</span><input type="password" id="r_password"></label>');
        document.writeln('<i class="text-error on" id="r_password_i"></i>');
        document.writeln('</div>');

        <%--document.writeln('<div class="login-text">');--%>
        <%--document.writeln('<label><span>确认密码：</span><input type="password" id="r_confirm_password"></label>');--%>
        <%--document.writeln('<i class="text-error on" id="r_confirm_password_i"></i>');--%>
        <%--document.writeln('</div>');--%>

        document.writeln('<div class="login-text">');
        document.writeln('<label><span>昵&nbsp;称：</span><input type="text" id="r_nick"></label>');
        document.writeln('<i class="text-error on" id="r_nick_i"></i>');
        document.writeln('</div>');

        document.writeln('<div class="login-text">');
        document.writeln('<label><span>真实姓名：</span><input type="text" id="r_realname"></label>');
        document.writeln('<i class="text-error on" id="r_realname_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="login-text">');
        document.writeln('<label><span>身份证号：</span><input type="text" id="r_id"></label>');
        document.writeln('<i class="text-error on" id="r_id_i"></i>');
        document.writeln('</div>');

        document.writeln('<div class="register-btn w-200" id="register">注册</div>');
        document.writeln('<div class="w-200" style="text-align: center;">');
        document.writeln('<label class="check-me"><input type="checkbox" name="check" id="agree" checked="checked"><i></i>我已同意<a href="http://www.joyme.com/help/law" target="_blank">《着迷注册服务协议》</a></label>');
        document.writeln('</div>');
        document.writeln('</div>');
        document.writeln('<a href="javascript:;" class="login-mask mask-link fn-r">已有账号？去登录</a>');
        document.writeln('</div>');
        <!-- 注册 结束 -->
        <!-- 找回密码 开始 -->
        document.writeln('<div class="password-box login-bg" >');
        document.writeln('<h1>找回密码</h1>');
        document.writeln('<span class="close-icon">关闭</span>');
        document.writeln('<div class="login-con">');
        document.writeln('<div class="login-text">');
        document.writeln('<label><span>手机号：</span><input type="text" placeholder="仅支持中国大陆" id="f_phone"></label>');
        document.writeln('<i class="text-error on" id="f_phone_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="login-text">');
        document.writeln('<label class="code-text"><span>验证码：</span><input type="text" id="f_code" maxlength="5"><button class="code-btn" id="f_sendmobile">获取验证码</button></label>');
        document.writeln('<i class="text-error on" id="f_code_i"></i>');
        document.writeln('</div>');
        document.writeln('<div class="login-text">');
        document.writeln('<label><span>新密码：</span><input type="password" id="f_password" ></label>');
        document.writeln('<i class="text-error on" id="f_password_i"></i>');
        document.writeln('</div>');

        <%--document.writeln('<div class="login-text">');--%>
        <%--document.writeln('<label><span>确认密码：</span><input type="password" id="f_confirm_password" ></label>');--%>
        <%--document.writeln('<i class="text-error on" id="f_confirm_password_i"></i>');--%>
        <%--document.writeln('</div>');--%>

        document.writeln('<input type="hidden" id="luotest_response_mima" name="luotest_response_mima" value="" >');
        document.writeln('<div class="sure-btn w-200" id="forgot">确定</div>');
        document.writeln('</div>');
        document.writeln('<a href="javascript:;" class="login-mask mask-link fn-r">已有账号？去登录</a>');
        document.writeln('</div>');
        <!-- 找回密码 结束 -->
        document.writeln('<div class="mask-box"></div>');

        document.writeln('<div class="captcha-box">');
        document.writeln('<div class="l-captcha" data-site-key="533a7e232fb9134c30928ceebad087ef" data-width="250" data-callback="load.mod.getResponse">');
        document.writeln('</div>');
        document.writeln('</div>');

        document.writeln('<script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>');


    </c:when>
    <c:otherwise>
        document.writeln('<div class="after">');
        document.writeln('<span>');
        <c:choose>
            <c:when test="${empty userCenterSession.icon}">
                document.writeln('<a href="${URL_UC}/usercenter/home" class="userhead"><img src="${icon:parseIcon(userCenterSession.icon, userCenterSession.sex, "")}"/></a>');
            </c:when>
            <c:otherwise>
                document.writeln('<a href="${URL_UC}/usercenter/home" class="userhead"><img src="${userCenterSession.icon}"/></a>');
            </c:otherwise>
        </c:choose>
        document.writeln('<cite>展开</cite>');
        document.writeln('<div>');
        document.writeln('<ul style="height: 50px;">');
        document.writeln('<li class="fit"><a href="${URL_WWW}/profile/customize/basic">设置</a></li>');
        <%--document.writeln('<li><a href="${URL_WWW}/activity/pay">充值</a></li>');--%>
        document.writeln('<li><a href="http://passport.${DOMAIN}/auth/logout?reurl='+escape(window.location.href)+'">退出</a></li>');
        document.writeln('</ul>');
        document.writeln('</div>');
        document.writeln('</span>');
        document.writeln('</div>');
    </c:otherwise>
</c:choose>

document.writeln('</div>');
document.writeln('<script type="text/javascript" src="${URL_LIB}/static/js/init/login_common.js"></script>');
