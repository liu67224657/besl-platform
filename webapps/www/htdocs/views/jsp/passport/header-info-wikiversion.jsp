<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jslibs.jsp" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
document.writeln('<link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/cms/jmsy/logincont/login201611.css">');
<c:choose>
    <c:when test="${not empty userCenterSession}">

        document.writeln('<div class="lading"> ');
        document.writeln('<a href="${URL_UC}/usercenter/home" class="user-icon"><img src="${icon:parseIcon(userCenterSession.icon,userCenterSession.sex,"m")}"></a> ');
        document.writeln('<div class="dropdown setting-box pull-down dropdown-nor web-hide"> ');
        document.writeln('<a data-toggle="dropdown" class="dropdown-toggle" role="button">');
        document.writeln('<b class="caret setting-caret web-hide"></b></a>');
        document.writeln('<ul class="dropdown-menu" role="menu"> ');
        <%--document.writeln('<li><a href="${URL_UC}/usercenter/mypoint/list"> 我的积分</a></li>'); //todo 用戶激励体系删除--%>
        <%--document.writeln('<li><a href="http://wiki.${DOMAIN}/home/特殊:收藏列表"> 我的收藏</a></li>');--%>
        document.writeln('<li><a href="${URL_UC}/usercenter/account/safe"> 设置 </a></li>');
        document.writeln('<li><a href="http://passport.${DOMAIN}/auth/logout"> 退出 </a></li>');
        document.writeln('</ul></div>');
        document.writeln('<div class="dropdown notice-box pull-down dropdown-nor"> ');
        document.writeln('<a data-toggle="dropdown" class="dropdown-toggle" role="button"> ');
        document.writeln('<i class="fa fa-bell-o col-xs-show" aria-hidden="true"></i> 提醒');
        <c:if test="${not empty noticeSumMap}">
            document.writeln('<b class="caret"></b>');
        </c:if>
        document.writeln('</a>');
        document.writeln('<ul class="dropdown-menu notice-list" aria-labelledby="提醒" role="menu"> ');
        document.writeln('<li><a href="${URL_UC}/usercenter/notice/at"> @我的 ');
        <c:if test="${not empty noticeSumMap && not empty noticeSumMap['at']}">
            document.writeln('<b> ${ noticeSumMap['at'].value}</b> ');
        </c:if>
        document.writeln('</a></li>');
        document.writeln('<li><a href="${URL_UC}/usercenter/notice/reply">评论');
        <c:if test="${not empty noticeSumMap && not empty noticeSumMap['reply']}">
            document.writeln('<b> ${ noticeSumMap['reply'].value}</b> ');
        </c:if>
        document.writeln('</a></li>');
        document.writeln('<li><a href="${URL_UC}/usercenter/notice/agree">点赞 ');
        <c:if test="${not empty noticeSumMap && not empty noticeSumMap['agree']}">
            document.writeln('<b> ${ noticeSumMap['agree'].value}</b> ');
        </c:if>
        document.writeln('</a></li>');
        document.writeln('<li><a href="${URL_UC}/usercenter/notice/follow">关注');
        <c:if test="${not empty noticeSumMap && not empty noticeSumMap['follow']}">
            document.writeln('<b> ${ noticeSumMap['follow'].value}</b> ');
        </c:if>
        document.writeln('</a></li>');
        document.writeln('<li><a href="${URL_UC}/usercenter/notice/sys">系统');
        <c:if test="${not empty noticeSumMap && not empty noticeSumMap['sys']}">
            document.writeln('<b> ${ noticeSumMap['sys'].value}</b> ');
        </c:if>
        document.writeln('</a></li>');
        document.writeln('</ul></div> ');

        document.writeln('<a href="http://wiki.${DOMAIN}/home/特殊:私信列表" class="letter" id="boardtotal_a">');
        <c:if test="${not empty messageNum}">
            document.writeln('<b id="boardtotal">${messageNum}</b>');
        </c:if>
        document.writeln('<i class="fa fa-envelope-o" aria-hidden="true"></i>私信</a>');
        document.writeln('<div class="dropdown setting-box pull-down dropdown-nor web-show"> ');
        document.writeln('<a data-toggle="dropdown" class="dropdown-toggle" role="button" aria-expanded="true"> ');
        document.writeln('<i class="fa fa-cog fa-fw" aria-hidden="true">');
        document.writeln('</i></a>');
        document.writeln('<ul class="dropdown-menu" role="menu"> ');
        <%--document.writeln('<li><a href="${URL_UC}/usercenter/mypoint/list"> 我的积分</a></li>');//todo 用戶激励体系删除--%>
        <%--document.writeln('<li><a href="http://wiki.${DOMAIN}/home/特殊:收藏列表">我的收藏</a></li>');--%>
        document.writeln('<li><a href="${URL_UC}/usercenter/account/safe">设置</a></li>');
        document.writeln('<li><a href="http://passport.${DOMAIN}/auth/logout">退出</a></li></ul>');
        document.writeln('</div></div>');


    </c:when>
    <c:otherwise>
        document.writeln('<script src="${URL_STATIC}/tools/luosimao/api.js"></script>');
        document.writeln('<span class="fn-clear">');
        document.writeln('<div class="loginbar bg-f fn-r">');
        document.writeln('<a href="javascript:;" class="login login-mask">登录</a>');
        document.writeln('<a href="javascript:;" class="register register-mask ">注册</a>');
        document.writeln('</div>');
        document.writeln('<div class="success-btn">注册成功</div>');

        <!-- 登陆 开始 -->
        document.writeln('<div class="login-box login-bg ">');
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
        document.writeln('</span>');

        document.writeln('<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>');

        document.writeln('<script type="text/javascript" src="${URL_LIB}/static/js/init/login_common.js"></script>');

    </c:otherwise>
</c:choose>
