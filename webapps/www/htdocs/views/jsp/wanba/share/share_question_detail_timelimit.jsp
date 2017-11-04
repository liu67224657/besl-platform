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

<div class="container <c:if test='${(timeout&&question.accepaid eq 0)|| (pid eq question.askprofileid&& question.accepaid eq 0&&question.reactivated eq 0)}'>suction-top</c:if>">
    <!-- 注释：无倒计时时去掉‘suction-top’类名，反之添加 -->
    <!-- 顶部悬浮 开始 -->
    <!-- 倒计时 -->
    <c:if test="${timeout&&question.accepaid eq 0}">
        <div class=" count-down suspend-ele">
            <span><code class="hour"></code>:<code class="minute"></code>:<code class="second"></code></span><i>后结束</i>
        </div>
    </c:if>
    <!-- 重新激活 -->
    <c:if test="${!timeout&&pid eq question.askprofileid && question.accepaid eq 0}">
        <div class="restart suspend-ele">
            <a href="javascript:reactivated();">没有满意的答案？点击重新激活<i></i></a>
        </div>
    </c:if>
    <!-- 顶部悬浮 结束 -->
    <div id="wrapper">
        <div class="que-main border-bot">
            <div class="que-main-cont">
                <div class="ques-top fn-clear">
                    <span class="fn-l ques-vector"><i></i>${profileMap[question.askprofileid].nick} 在  ${gameAnimeTagMap[question.tagid].tag_name}的提问</span>
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
            <c:if test="${timeout}">
                <div class="invite-icon">
                    <a href="jmwanba://jt=52&ji=${question.questionid}"><i></i>邀请回答</a>
                </div>
            </c:if>

        </div>

        <c:if test="${not empty gameAnimeTagMap[question.tagid]}">
            <div class="wiki-ans">
                <a href="jmwanba://jt=52&ji=${question.questionid}" class="wiki-link fn-clear">
                    <div>

                        <cite><img src="${gameAnimeTagMap[question.tagid].picjson.ios}"></cite>
                        <span>
        							<font>${gameAnimeTagMap[question.tagid].tag_name}</font>
        							<b>还有
        							<c:choose>
                                        <c:when test="${not empty questionPage}">
                                            ${ questionPage.totalRows}
                                        </c:when>
                                        <c:otherwise>
                                            0
                                        </c:otherwise>
                                    </c:choose>
        							个问答</b>
        						</span>
                    </div>
                    <i></i>
                </a>
            </div>
        </c:if>

        <!-- 参与回答列表 开始 -->
        <div class="join-ans-list list-item">
            <c:if test="${not empty answerpage.totalRows}">
                <h2>共${answerpage.totalRows}人参与回答</h2>
            </c:if>

            <!--------------------------------------------------------------最佳-------------------------------------------------------------------------->
            <c:if test="${question.accepaid ne 0 || question.fisrtaid ne 0}">
                <c:if test="${not empty topAnserMap[question.accepaid]}">
                    <c:set var="answer" value="${topAnserMap[question.accepaid]}"/>
                    <div class="join-ans-item border-bot">
                        <div class="join-ans-cont">

                            <div class="ans-user fn-clear">
                                <a href="jmwanba://jt=52&ji=${question.questionid}" class="ans-user-l ">
                                    <c:choose>
                                        <c:when test="${not empty profileMap[answer.profileid].icon}">
                                            <cite><img src="${profileMap[answer.profileid].icon}"></cite>
                                        </c:when>
                                        <c:otherwise>
                                            <cite><img
                                                    src="${URL_LIB}/hotdeploy/static/img/wanba_default.png"></cite>
                                        </c:otherwise>
                                    </c:choose>
                                    <span>
                                                         						<font class="ans-user-name fn-clear">
                                                         							<em class="limit">${profileMap[answer.profileid].nick}</em>
                                                                                    <c:if test="${not empty wanbaProfileMap[answer.profileid]}">
                                                                                        <c:if test="${wanbaProfileMap[answer.profileid].verifyType>0}">
                                                                                            <i></i></c:if>
                                                                                    </c:if>
                                                         							<b class="zj">最佳</b>
                                                                                    <c:if test="${question.fisrtaid eq answer.answerid}">
                                                                                        <b class="zk">最快</b>
                                                                                    </c:if>
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
                                <c:choose>
                                    <c:when test="${answerpage.totalRows>1}">
                                        <a href="http://api.${DOMAIN}/wanba/webview/answer/sharedetail?aid=${answer.answerid}&jt=59&ji=${answer.answerid}">
                                            <p class="limit-line">${answer.body.text}</p></a>
                                    </c:when>
                                    <c:otherwise>
                                        ${answer.richbody}
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </div>
                        <div class="suspend fn-clear">
                            <c:choose>
                            <c:when test="${answer.agreestatus eq 1}">
                            <a href="jmwanba://jt=52&ji=${question.questionid}" class="zan-count on"><i></i>
                                </c:when>
                                <c:otherwise>
                                <a href="jmwanba://jt=52&ji=${question.questionid}" id="agreeClass${answer.answerid}"
                                   class="zan-count"><i></i>
                                    </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${answer.agreesum eq 0}">
                                            <em id="agree${answer.answerid}">点赞</em>
                                        </c:when>
                                        <c:otherwise><em
                                                id="agree${answer.answerid}">${answer.agreesum}</em></c:otherwise>
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
                    </div>
                </c:if>

                <!--------------------------------------------------------------最佳结束-------------------------------------------------------------------------->
                <!-------------------------------------------------------------------------最快-------------------------------------------------->
                <c:if test="${not empty topAnserMap[question.fisrtaid]&&question.fisrtaid ne question.accepaid}">
                    <c:set var="answer" value="${topAnserMap[question.fisrtaid]}"/>
                    <div class="join-ans-item border-bot">
                        <div class="join-ans-cont">

                            <div class="ans-user fn-clear">
                                <a href="jmwanba://jt=52&ji=${question.questionid}" class="ans-user-l ">
                                    <c:choose>
                                        <c:when test="${not empty profileMap[answer.profileid].icon}">
                                            <cite><img src="${profileMap[answer.profileid].icon}"></cite>
                                        </c:when>
                                        <c:otherwise>
                                            <cite><img
                                                    src="${URL_LIB}/hotdeploy/static/img/wanba_default.png"></cite>
                                        </c:otherwise>
                                    </c:choose>
                                    <span>
                                                         						<font class="ans-user-name fn-clear">
                                                         							<em class="limit">${profileMap[answer.profileid].nick}</em>
                                                                                    <c:if test="${not empty wanbaProfileMap[answer.profileid]}">
                                                                                        <c:if test="${wanbaProfileMap[answer.profileid].verifyType>0}">
                                                                                            <i></i></c:if>
                                                                                    </c:if>
                                                         							<b class="zk">最快</b>
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
                                <c:choose>
                                    <c:when test="${answerpage.totalRows>1}">
                                        <a href="http://api.${DOMAIN}/wanba/webview/answer/sharedetail?aid=${answer.answerid}">
                                            <p class="limit-line">${answer.body.text}</p></a>
                                    </c:when>
                                    <c:otherwise>
                                        ${answer.richbody}
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </div>
                        <div class="suspend fn-clear">
                            <c:choose>
                            <c:when test="${answer.agreestatus eq 1}">
                            <a href="jmwanba://jt=52&ji=${question.questionid}" class="zan-count on"><i></i>
                                </c:when>
                                <c:otherwise>
                                <a href="jmwanba://jt=52&ji=${question.questionid}" id="agreeClass${answer.answerid}"
                                   class="zan-count"><i></i>
                                    </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${answer.agreesum eq 0}">
                                            <em id="agree${answer.answerid}">点赞</em>
                                        </c:when>
                                        <c:otherwise><em
                                                id="agree${answer.answerid}">${answer.agreesum}</em></c:otherwise>
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
                        <c:if test="${pid eq question.askprofileid&&question.accepaid eq  0}">
                            <div class="adopt-best">
                                <span onclick="accepaid('${answer.answerid}')"><i></i>采纳为最佳</span>
                            </div>
                        </c:if>
                    </div>
                </c:if>
                <!-------------------------------------------------------------------------最快结束-------------------------------------------------->
            </c:if>

            <c:choose>
                <c:when test="${answerpage.totalRows>0}">
                    <!-- 情况二 有参与人 -->


                    <c:forEach items="${answerDetailDTOList}" var="answer">
                        <div class="join-ans-item border-bot">
                            <div class="join-ans-cont">

                                <div class="ans-user fn-clear">
                                    <a href="jmwanba://jt=52&ji=${question.questionid}" class="ans-user-l ">
                                        <c:choose>
                                            <c:when test="${not empty profileMap[answer.profileid].icon}">
                                                <cite><img src="${profileMap[answer.profileid].icon}"></cite>
                                            </c:when>
                                            <c:otherwise>
                                                <cite><img
                                                        src="${URL_LIB}/hotdeploy/static/img/wanba_default.png"></cite>
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
                                <c:if test="${not empty  answer.voice&&not empty answer.voice.url}">
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
                                    <c:choose>
                                        <c:when test="${answerpage.totalRows>1}">
                                            <a href="http://api.${DOMAIN}/wanba/webview/answer/sharedetail?aid=${answer.answerid}&jt=59&ji=${answer.answerid}">
                                                <p class="limit-line">${answer.body.text}</p></a>
                                        </c:when>
                                        <c:otherwise>
                                            ${answer.richbody}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </div>
                            <div class="suspend fn-clear">
                                <c:choose>
                                <c:when test="${answer.agreestatus eq 1}">
                                <a href="jmwanba://jt=52&ji=${question.questionid}" class="zan-count on"><i></i>
                                    </c:when>
                                    <c:otherwise>
                                    <a href="jmwanba://jt=52&ji=${question.questionid}" id="agreeClass${answer.answerid}"
                                       class="zan-count"><i></i>
                                        </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${answer.agreesum eq 0}">
                                                <em id="agree${answer.answerid}">点赞</em>
                                            </c:when>
                                            <c:otherwise><em
                                                    id="agree${answer.answerid}">${answer.agreesum}</em></c:otherwise>
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

                        </div>
                    </c:forEach>


                </c:when>
                <c:otherwise>
                    <!-- 情况一 无人参与 -->
                    <div class="no-join-ans">还没有人回答哦</div>
                </c:otherwise>
            </c:choose>


        </div>

        <!-- 参与回答列表 结束 -->


    </div>
</div>
<div class="pop-up">
    <span>您已点赞过啦！</span>
</div>
<input type="hidden" name="share" value="yes"/>
<input type="hidden" name="pid" value="${pid}"/>
<input type="hidden" name="qid" value="${question.questionid}"/>
<input type="hidden" name="askpid" value="${question.askprofileid}"/>
<input type="hidden" name="accepaid" value="${question.accepaid}"/>
<input type="hidden" name="fisrtaid" value="${question.fisrtaid}"/>
<input type="hidden" name="logindomain" value="${logindomain}"/>
<input type="hidden" name="appkey" value="${appkey}"/>
<input type="hidden" name="curpage" value="${answerpage.curPage}"/>
<input type="hidden" name="maxpage" value="${answerpage.maxPage}"/>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
        src="${URL_LIB}/static/js/init/question_action.js"></script>
<script>
    $(document).ready(function () {
        var time = '${question.timelimit}';
        var timeDate = new Date(parseInt(time));
        var timeout = '${timeout}';
        if (timeout == 'true') {
            fnMenu.timeOut($('.count-down'), timeDate);
        }
    });

</script>
<script type="text/javascript" src="${URL_LIB}/static/js/wap/wanba/share.js"></script>
</body>
</html>