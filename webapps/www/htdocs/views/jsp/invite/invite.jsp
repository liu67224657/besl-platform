<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>邀请 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script>
       var uno='${srcUno}';
   </script>
</head>

<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div class="content clearfix">
        <div class="invitefriend">
            <div class="if_title">
                <h3>邀请朋友一起来着迷吧！</h3>
                <p> 选择常用的邀请方式，邀请好友加入着迷。收到邀请的人注册着迷后，就会自动关注你</p>
            </div>

            <div class="invitelist">
                <h3>方法一：邀请链接</h3>
                <div class="invite_link">
                    <span>复制邀请链接</span>
                    <input id="ipt_invite_link" type="text" class="settext" readonly="true" data-id="${inviteInfo.inviteId}"
                           value="${URL_WWW}/invite/${inviteInfo.inviteId}"/>
                    <a href="javascript:void(0);" id="copy_invite_link" class="submitbtn"><span>复制链接</span></a>
                </div>
            </div>

            <div class="invitelist">
                <h3>方法二：发邮件邀请</h3>
                <form id="form_mail" action="${URL_WWW}/json/invite/mail" method="post">
                    <input id="inviteid" name="inviteid" value="${inviteInfo.inviteId}" type="hidden"/>
                    <div class="invite_mail">
                        <ul>
                            <li>
                                <span> 输入对方邮箱</span>
                                <input type="text" id="inviteMail" name="inviteMail" value="" class="settext">
                                <a href="javascript:void(0);" id="send_invite_mail" class="submitbtn"><span>发送邀请</span></a>
                                <a id="priviewMail" href="javascript:void(0);" class="pi_a">预览邀请信</a>
                            </li>
                            <li id="error_mail_invite" class="error" ></li>
                        </ul>

                        <div id="div_invite_mail" class="pi_style" style="display:none">
                            <div class="pi_title"><h4>邀请信</h4><span><a class="pack_up" href="javascript:void(0);" id="hideMail">收起</a></span></div>
                            <ul>
                                <li><span>Hi! 我是</span><input id="mailTitle"name="mailTitle" type="text" value="${userSession.blogwebsite.screenName}" class="useranme"></li>
                                <li><textarea class="pi_text" style="font-family:Tahoma, '宋体';" rows="1" cols="" id="mailBody" name="mailBody" wrap="physical">最近发现一个叫着迷网的游戏社区挺不错的，已经有咱的不少朋友都去那安家了，你也一起来吧！</textarea></li>
                                <li>点这个链接就可以和我成为好友：</li>
                                <li>${URL_WWW}/invite/${inviteInfo.inviteId}</li>
                            </ul>
                        </div>
                    </div>
                </form>
            </div>

            <div class="invitelist">
                <h3>方法三：邀请邮件联系人</h3>
                <form id="form_import_mail" action="${URL_WWW}/invite/importmail" method="post">
                     <input name="inviteid" value="${inviteInfo.inviteId}" type="hidden"/>
                    <div class="invite_mail_c">
                        <ul>
                            <p>请输入您的邮箱账号和密码，着迷网只会通过您的邮箱通讯录来邀请你的朋友，不会保存您的账号和密码。</p>
                            <li><span>邮箱地址</span>
                                <input id="importMail" type="text"  name="loginName" value="" class="settext">
                                <em class="at">@</em>
                                <select class="fm_sel" id="mailProvider" name="mailProvider">
                                    <option value="">请选择邮箱类型</option>
                                    <c:forEach var="provider" items="${mailProviderMap}">
                                        <option value="${provider.key}"><fmt:message key="invite.mail.${provider.key}.suffix" bundle="${userProps}"/></option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li class="mailpic">
                                <img src="${URL_LIB}/static/theme/default/img/sohu.jpg" width="90" height="26"/>
                                <img src="${URL_LIB}/static/theme/default/img/gmail.jpg" width="90" height="26"/>
                            </li>
                            <li>
                                <span>邮箱密码 </span>
                                <input id="importPwd" type="password" name="passwrod" class="settext" value="">
                            </li>
                            <li class="error" id="import_error">
                                <c:if test="${fn:length(importError)>0}">
                                    <fmt:message key="${importError}" bundle="${userProps}"/>
                                </c:if>
                            </li>
                            <li class="mailpic">
                                <a class="submitbtn" id="link_import_submit"><span>查找</span></a>
                            </li>
                            <li id="import_mail_tips" style="display:none" class="loading">正在查找，可能时间稍长，请耐心等待...</li>
                        </ul>
                    </div>
                </form>
                <!--sc_con-->
            </div>
            <!--search_con-->
        </div>
        <!--yaoqing-->
    </div>
    <!--content-->
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use("${URL_LIB}/static/js/init/invite_init.js");
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>