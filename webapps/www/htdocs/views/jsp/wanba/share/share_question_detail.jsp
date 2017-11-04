<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="wap-font-scale" content="no">
    <title>${question.title}</title>
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/common.css?v=20161222">
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/activity.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>

<div class="advertising">
    <div class="adver-con">
        <a href="javascript:;">
            <img src="http://static.joyme.alpha/app/wanba/point/images/wanba_bottom_download.png">
        </a>
    </div>
</div>
<div class="open-in-browser">
    <span class="browser-warn"></span>
</div>

<div id="wrapper">
    <div class="que-main border-bot">
        <div class="que-main-cont">
            <div class="ques-top fn-clear">
                <span class="fn-l ques-vector"><i></i>${profileMap[question.askprofileid].nick} 向 ${profileMap[question.inviteprofileid].nick}的提问</span>
                <span class="fn-r ques-score"><i></i>${question.point}</span>
            </div>
            <h2 class="ans-title">${question.title}</h2>
            <c:if test="${not empty  question.voice &&not empty question.voice.url}">
                <div class="audio-box">
                    <audio src="${question.voice.url}"></audio>
                    <i></i>
                    <b>
                        <c:set var="time" value="${question.voice.time/1000}"/>
                        <c:choose>
                            <c:when test="${time<1}">
                                1
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber value="${time-0.5}" type="number" pattern="#"/>
                            </c:otherwise>
                        </c:choose>
                    </b>
                </div>
            </c:if>

            <c:if test="${not empty  question.richbody}">
                ${question.richbody}
            </c:if>
        </div>
        <div class="follow-count fn-clear">
            <div class="follower-icon">
                <c:choose>
                    <c:when test="${not empty followInfo && not empty followInfo.rows}">
                        <div>
                            <c:forEach items="${followInfo.rows}" var="info">
                                <c:choose>
                                    <c:when test="${not empty profileMap[info.profileId].icon}">
                                        <cite><img src="${profileMap[info.profileId].icon}"></cite>
                                    </c:when>
                                    <c:otherwise>
                                        <cite><img src="${URL_LIB}/hotdeploy/static/img/wanba_default.png"></cite>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>

                        </div>
                        <span>${followInfo.page.totalRows}人关注</span>
                    </c:when>
                    <c:otherwise>
                        <span>0人关注</span>
                    </c:otherwise>
                </c:choose>


            </div>
            <c:choose>
                <c:when test="${question.followstatus eq 0}">
                    <a href="jmwanba://jt=52&ji=${question.questionid}">+关注</a>
                </c:when>
                <c:otherwise>
                    <a href="jmwanba://jt=52&ji=${question.questionid}">已关注</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:choose>
        <c:when test="${answerpage.totalRows>0}">
            <!-- 情况二 有回答时 -->

            <c:forEach items="${answerDetailDTOList}" var="answer">
                <div class="art-main">
                    <div class="ans-user fn-clear">
                        <a href="jmwanba://jt=52&ji=${question.questionid}" class="ans-user-l ">
                            <c:choose>
                                <c:when test="${not empty profileMap[answer.profileid].icon}">
                                    <cite><img src="${profileMap[answer.profileid].icon}"></cite>
                                </c:when>
                                <c:otherwise>
                                    <cite><img src="${URL_LIB}/hotdeploy/static/img/wanba_default.png"></cite>
                                </c:otherwise>
                            </c:choose>
                            <span>
               						<font class="ans-user-name fn-clear">
               							<em class="limit">${profileMap[answer.profileid].nick}</em>
                                        <c:if test="${not empty wanbaProfileMap[answer.profileid]}">
                                            <c:if test="${wanbaProfileMap[answer.profileid].verifyType>0}">
                                                <i></i></c:if>
                                        </c:if>
               							<%--<b class="zj">最佳</b>--%>
               						</font>
               						<b class="ans-user-tme">${dateutil:parseWanbaDate(answer.answertime)}</b>
               					</span>
                        </a>
                        <a href="jmwanba://jt=52&ji=${question.questionid}" class="ans-user-r ">
                            <c:if test="${not empty wanbaProfileMap[answer.profileid]}">
                                <span>问TA</span>
                            </c:if>
                        </a>
                    </div>
                    <c:if test="${not empty  answer.voice &&not empty answer.voice.url}">
                        <div class="audio-box">
                            <audio src="${answer.voice.url}"></audio>
                            <i></i>
                            <b>
                                <c:set var="time" value="${answer.voice.time/1000}"/>
                                <c:choose>
                                    <c:when test="${time<1}">
                                        1
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${time-0.5}" type="number" pattern="#"/>
                                    </c:otherwise>
                                </c:choose>
                            </b>
                        </div>
                    </c:if>
                    <c:if test="${not empty  answer.richbody}">
                        ${answer.richbody}

                    </c:if>
                </div>
                <div class="suspend fn-clear">
                    <c:choose>
                    <c:when test="${answer.agreestatus eq 1}">
                    <a href="jmwanba://jt=52&ji=${question.questionid}" class="zan-count on"><i></i>
                        </c:when>
                        <c:otherwise>
                        <a href="jmwanba://jt=52&ji=${question.questionid}" class="zan-count"
                           id="agreeClass${answer.answerid}"><i></i>
                            </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${answer.agreesum eq 0}">
                                    <em id="agree${answer.answerid}">点赞</em>
                                </c:when>
                                <c:otherwise><em id="agree${answer.answerid}">${answer.agreesum}</em></c:otherwise>
                            </c:choose>
                        </a>
                        <a href="jmwanba://jt=52&ji=${question.questionid}" class="mess-count"><i></i>
                            <c:choose>
                                <c:when test="${answer.replysum eq 0}">
                                    评论
                                </c:when>
                                <c:otherwise>${answer.replysum}</c:otherwise>
                            </c:choose>
                        </a>
                </div>
            </c:forEach>

        </c:when>
        <c:otherwise>
            <!-- 情况一 无回答时 -->
            <div class="no-join-ans">还没有人回答哦</div>
        </c:otherwise>
    </c:choose>


</div>
<input type="hidden" name="pid" value="${pid}"/>
<input type="hidden" name="qid" value="${question.questionid}"/>
<input type="hidden" name="logindomain" value="${logindomain}"/>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
        src="${URL_LIB}/static/js/init/question_action.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/wap/wanba/share.js"></script>
</body>
</html>