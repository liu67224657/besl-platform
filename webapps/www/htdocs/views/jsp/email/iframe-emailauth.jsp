<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/views/jsp/common/meta.jsp"%>
<title>邮件验证   ${jmh_title}</title>
<link href="${URL_LIB}/static/default/css/mask.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${URL_LIB}/static/js/getmail.js"></script>
</head>
<body style="background: none;">
<div style="position: absolute;  z-index: 1200; left: -225px; margin-left:50%; top: 130px; background:none">
    <table border="0" cellpadding="0" cellspacing="0" class="div_warp" >
        <tbody>
            <tr>
                <td class="top_l"></td>
                <td class="top_c"></td>
                <td class="top_r"></td>
            </tr>
            <tr>
                <td class="mid_l"></td>
                <td class="mid_c">
                    <div class="ver_mail">
                        <c:if test="${errorMsg==null}">
						<div class="ver_mail_top">
                            <div style="width:90%; float:left;">
                        	<h3>马上激活邮件，完成注册！</h3>
                            <p>注册确认邮件已经发送到你的邮箱 <a href="javascript:void(0)" onclick="javascript:hrefMail('${userid}');return false;">${userid}</a></p>
                            <p>点击邮件里的确认链接即可登录着迷</p>
                            </div>
                            <div style="width:10%; float:right; "><a href="javascript:void(0);" onclick="hideMask();" class="close">X</a></div>
                        </div>
                       <div class="ver_mail_bottom">
                       	<dl>
                        	<dt><input name="" type="button" class="mailbut" onclick="javascript:hrefMail('${userid}');"/></dt>
                            <dd>还没有收到确认邮件？你可以：</dd>
                            <dd>1. 尝试到广告邮件、垃圾邮件、订阅邮件目录里找找看；</dd>
                            <dd>2. 请点击这里，<a href="${ctx}/security/email/auth/send">重新发送确认邮件</a>；</dd>
                            <dd>3. 填错邮箱地址？很抱歉，您需要重新注册。</dd>
                        </dl>
                       </div>
                        </c:if>
                        <c:if test="${errorMsg!=null}">
						<div class="ver_mail_top">
                            <div style="width:90%; float:left;">
                        	<h3>认证邮件发送失败！</h3>

                            <p><fmt:message key="${errorMsg}" bundle="${userProps}"/></p>
                                </div>
                            <div style="width:10%; float:right; "><a href="javascript:void(0);" onclick="hideMask();" class="close">X</a></div>
                        </div>
                        </c:if>
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
</body>
</html>
