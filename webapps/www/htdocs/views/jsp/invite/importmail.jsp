<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>邀请邮箱 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="content clearfix">
    <div class="invitefriend">
        <div class="if_title">
            <h3>邀请他们</h3>

            <p>以下邮箱联系人还没有加入着迷，请他们加入吧</p>
        </div>
        <form action="/json/invite/mails" method="post" id="form_invitemails">
             <input id="inviteid" name="inviteid" value="${inviteId}" type="hidden"/>
        <div class="mail_cont">
            <div class="mail_list">
                <ul>
                    <c:forEach items="${contactSet}" var="contact" varStatus="st">
                        <li><label>
                            <input id="inviteMails_${st.index}" name="inviteMails" value="${contact.email}"
                                   type="checkbox" checked="checked"/>${contact.email}</label>
                        </li>
                    </c:forEach>
                </ul>
                <div class="mail_tool">
                    <p class="left">
                        <label>
                            <input id="check_all" type="checkbox" checked="true">全选
                            <input id="uncheck_all" type="checkbox">反选
                        </label>
                    </p>

                    <p class="right">
                        <a href="javascript:void(0);" class="pi_a" id="priviewMail">预览邀请信</a>
                        <a id="link_import_submit" href="javascript:void(0);" class="submitbtn"> <span>确 定</span></a>
                    </p>
                     <p id="form_error" class="error">

                    </p>
                </div>


            </div>
            <div class="pi_mail_style" id="div_invite_mail" style="display:none">
                <div class="pi_mail_title">
                    <h4>邀请信</h4>
                    <span><a href="javascript:void(0)" id="hideMail">收起</a></span>
                </div>
                <ul>
                    <li>Hi! 我是<input id="mailTitle" name="mailTitle" type="text"
                                     value="${userSession.blogwebsite.screenName}" class="username"></li>
                    <li><textarea id="mailBody" style="font-family:Tahoma, '宋体';" name="mailBody" cols="" rows="2" class="ye_text clearfix">最近发现一个叫着迷网的游戏社区挺不错的，已经有咱的不少朋友都去那安家了，你也一起来吧！</textarea>
                    </li>
                    <li>点这个链接就可以和我成为好友：</li>
                    <li>${URL_WWW}/invite/${inviteId}</li>
                </ul>
            </div>
        </div>
        </form>
    </div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use("${URL_LIB}/static/js/init/invitemail_init.js");
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>