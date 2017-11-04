<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>我的信箱 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">
            <div class="conleft_title">
                <h3>我的信箱</h3>
            </div>
            <div class="mycomment_tags">
                <a href="javascript:void(0)" class="up">私信</a>
                <a href="${ctx}/message/notice/list">通知</a>
            </div>
            <div class="p_back"><a href="${ctx}/message/private/list">返回收件箱</a></div>
            <div class="messages_list clearfix">
                <dl>
                    <dt class="personface">
                        <a name="atLink" title="<c:out value='${userSession.blogwebsite.screenName}'/>"
                           href="${URL_WWW}/people/<c:out value="${userSession.blogwebsite.domain}"/>">
                            <img src="<c:out value='${uf:parseFacesInclude(userSession.blogwebsite.headIconSet,userSession.userDetailinfo.sex,"s",true,0,1)[0]}'/>"
                                 width="58"
                                 height="58"/>
                        </a>
                    </dt>
                    <dd class="messages">
                        <div class="messages_l"></div>
                        <ul>
                            <li>回复：${screenName}<input type="hidden" id="receivename" name="receivename" class="pd_text"
                                                       value="${screenName}" readonly="true"></li>
                            <%--<li><input type="hidden" id="receivename" name="receivename" class="pd_text" value="${screenName}" readonly="true"></li>--%>
                            <li class="mt10">
                                <span class="left">私信内容：</span><span class="right" id="message_num"></span>
                            </li>
                            <li>
                                <textarea class="pd_textarea" style="font-family:Tahoma, '宋体';" rows="" cols=""
                                          id="messagebody" name="messagebody"></textarea>
                            </li>
                            <li>
                                <span class="left"><div class="commenface" id="moodFace"></div></span>
                                <span class="right"><a class="submitbtn" name="savemsg"><span>发 送</span></a></span>
                            </li>
                        </ul>
                    </dd>
                </dl>
            </div>
            <!--列表-->
            <c:forEach items="${recelist}" var="message" varStatus="st">
                <div class="messages_list clearfix" id="msg_${message.message.msgId}">
                    <dl>
                        <dt class="personface">
                            <a name="atLink" title="<c:out value='${message.profile.blog.screenName}'/>"
                               href="javascript:void(0)">
                                <img width="58" height="58"
                                     src='${uf:parseFacesInclude(message.profile.blog.headIconSet,message.profile.detail.sex,"s",true,0,1)[0]}'>
                            </a>
                        </dt>
                        <dd class="messages">
                            <div class="messages_l"></div>
                            <p class="p_text">
                                <a href="${URL_WWW}/people/<c:out value="${message.profile.blog.domain}"/>"
                                   name="atLink"
                                   title="<c:out value='${message.profile.blog.screenName}'/>">${message.profile.blog.screenName}</a>：${message.message.body}
                            </p>

                            <p class="delmycomment">
                                <a href="javascript:void(0)" name="removeMsgReply" title="删除"
                                   id="${message.message.msgId}">×</a>
                            </p>

                            <p class="p_tools">
                                <span>${dateutil:parseDate(message.message.sendDate)}</span>
                            </p>
                        </dd>
                    </dl>
                </div>
            </c:forEach>
            <!--列表-->
            <!--翻页开始-->
            <c:set var="pageurl" value="${ctx}/message/private/reply"/>
            <c:set var="pageparam" value="senduno=${sendUno}"/>
            <%@ include file="/views/jsp/page/page.jsp" %>
            <!--翻页结束-->
        </div>
        <div class="con_ft"></div>
    </div>
    <!--右侧开始-->
    <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    <!--右侧结束-->
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use("${URL_LIB}/static/js/init/message-init.js");
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(20);
</script>
</body>
</html>