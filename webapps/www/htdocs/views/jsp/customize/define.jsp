<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>互动设置 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <div class="set_title">
            <h3>互动设置</h3>
        </div>
        <!--设置-互动设置-->
        <div class="set_int_text clearfix">
            <p>消息设置</p>

            <p>勾选后将收到对应的提醒信息，显示为以下形式</p>
            <ul id="checkbox_select" class="clearfix">
                <li>
                    <label><input type="checkbox" name="hintmyfeedback" id="hintmyfeedback"
                                  <c:if test="${userSession.userDefine.hintmyfeedback.getCode() == 'y'}">checked="checked"</c:if>/>
                        有新评论</label>
                </li>
                <li>
                    <label><input type="checkbox" name="hintmyfans" id="hintmyfans"
                                  <c:if test="${userSession.userDefine.hintmyfans.getCode() == 'y'}">checked="checked"</c:if>/>
                        有新粉丝</label>
                </li>
                <li>
                    <label><input type="checkbox" name="hintmyletter" id="hintmyletter"
                                  <c:if test="${userSession.userDefine.hintmyletter.getCode() == 'y'}">checked="checked"</c:if>/>
                        有新私信</label>
                </li>
                <li>
                    <label><input type="checkbox" name="hintmynotice" id="hintmynotice"
                                  <c:if test="${userSession.userDefine.hintmynotice.getCode() == 'y'}">checked="checked"</c:if>/>
                        有新通知</label>
                </li>
                <li>
                    <c:choose>
                        <c:when test="${userSession.userDefine.atSet.getIsNotice()}">
                            <label><input type="checkbox" name="hintat" id="hintat" checked="checked"/>
                                @到我的信息<em class="ait" id="atEm"></em></label>
                        </c:when>
                        <c:otherwise>
                            <label><input type="checkbox" name="hintat" id="hintat"/>
                                @到我的信息<em class="aithover"></em></label>
                        </c:otherwise>
                    </c:choose>
                     <div class="ait_text clearfix" id="ait_text"
                     <c:if test="${!userSession.userDefine.atSet.getIsNotice()}">style="dispaly:none;"</c:if>>
                    <p>设置哪些@提到我的文章/评论计入@提醒数字中</p>

                    <p>文章/评论的作者是：
                        <label><input type="radio" id="allRelation" name="authorRelation"
                                      <c:if test="${userSession.userDefine.atSet.getAuthorRelation() == 'all'}">checked="checked"</c:if>
                                      value="all"> 所有人</label>
                        <label><input type="radio" id="focusRelation" name="authorRelation"
                                      <c:if test="${userSession.userDefine.atSet.getAuthorRelation() == 'focus'}">checked="checked"</c:if>
                                      value="focus"> 关注的人</label>
                    </p>

                    <p>文章的类型是：
                        <label><input type="radio" id="allblog" name="contentType" value="all"
                                      <c:if test="${userSession.userDefine.atSet.getContentType() == 'all'}">checked="checked"</c:if>>
                            所有博客</label>
                        <label><input type="radio" id="oriblog" name="contentType" value="org"
                                      <c:if test="${userSession.userDefine.atSet.getContentType() == 'org'}">checked="checked"</c:if>>
                            原创博客</label>
                    </p>
                </div>
                </li>
               
            </ul>
            <c:if test="${userSession.userDefine.hintmyfeedback.getCode() == 'y' ||
                            userSession.userDefine.hintmyfans.getCode() == 'y' ||
                            userSession.userDefine.hintmyletter.getCode() == 'y' ||
                            userSession.userDefine.hintmynotice.getCode() == 'y' ||
                            userSession.userDefine.atSet.getIsNotice()}">
                <ol id="notice_tips">
                    <li id="p_hintmyfeedback">
                        <c:if test="${userSession.userDefine.hintmyfeedback.getCode() == 'y'}">
                            1条新评论，<b>查看评论</b>
                        </c:if>
                    </li>
                    <li id="p_hintmyfans">
                        <c:if test="${userSession.userDefine.hintmyfans.getCode() == 'y'}">
                            1条新粉丝，<b>查看我的粉丝</b>
                        </c:if>
                    </li>
                    <li id="p_hintmyletter">
                        <c:if test="${userSession.userDefine.hintmyletter.getCode() == 'y'}">
                            1条私信，<b>查看收件箱</b>
                        </c:if>
                    </li>
                    <li id="p_hintmynotice">
                        <c:if test="${userSession.userDefine.hintmynotice.getCode() == 'y'}">
                            1条新通知，<b>查看收件箱</b>
                        </c:if>
                    </li>
                    <li id="p_hintat">
                        <c:if test="${userSession.userDefine.atSet.getIsNotice()}">
                            1条@提到我，<b>查看@到我的</b>
                        </c:if>
                    </li>
                </ol>
            </c:if>

        </div>
        <div class="set_int_text clearfix">
            <p>评论设置</p>

            <p>以下用户可以评论我的文章</p>
            <ul class="clearfix">
                <li>
                    <label><input name="allowreplay" value="A" type="radio" id="user_all"
                                  <c:if test="${userSession.userDefine.allowContentReplyType.getCode()=='a'}">checked="checked"</c:if> />
                        所有人</label>
                </li>
                <li>
                    <label><input name="allowreplay" value="C" type="radio" id="user_att"
                                  <c:if test="${userSession.userDefine.allowContentReplyType.getCode()=='c'}">checked="checked"</c:if>  />
                        我关注的人</label>
                </li>
                <li>
                    <label><input name="allowreplay" value="N" type="radio" id="user_any"
                                  <c:if test="${userSession.userDefine.allowContentReplyType.getCode()=='n'}">checked="checked"</c:if> />
                        不允许任何人</label>
                </li>
            </ul>
        </div>
        <div class="set_int_text clearfix">
            <p>私信设置</p>

            <p>以下用户可以向我发送私信</p>
            <ul class="clearfix">
                <li>
                    <label><input name="allowletter" value="A" type="radio" id="s_all"
                                  <c:if test="${userSession.userDefine.allowLetterType.getCode()=='a'}">checked="checked"</c:if> />
                        所有人</label>
                </li>
                <li>
                    <label><input name="allowletter" value="C" type="radio" id="s_att"
                                  <c:if test="${userSession.userDefine.allowLetterType.getCode()=='c'}">checked="checked"</c:if> />
                        我关注的人</label>
                </li>
                <li>
                    <label><input name="allowletter" value="N" type="radio" id="s_any"
                                  <c:if test="${userSession.userDefine.allowLetterType.getCode()=='n'}">checked="checked"</c:if> />
                        不允许任何人</label>
                </li>
            </ul>
        </div>
        <div class="set_int_text unline clearfix">
            <p><a class="submitbtn"><span>保 存</span></a></p>
        </div>

        <!--设置-互动设置 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-define-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
