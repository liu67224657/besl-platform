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
    <title>问题详情</title>
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/common.css?v=20161202">
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
<c:if test="${question.inviteprofileid eq pid&&answertype eq 0}">
    <div id="_answer_status" style="display: none;">yes</div>

</c:if>

<div id="_questionid" style="display: none;">${question.questionid}</div>
<div id="_questiontitle" style="display: none;">${question.title}</div>

<div id="_share_status" style="display: none;">no</div>

<div id="_title" style="display: none;">${question.title}</div>
<div id="_desc" style="display: none;"><c:if
        test="${not empty  question.voice &&not empty question.voice.url}">[语音]</c:if>
    ${question.body.text}</div>
<div id="_pic" style="display: none;">http://huabao.joyme.com/wapimage/wanba_icon.jpg</div>
<div id="_share_url" style="display: none;">${share_url}</div>
<div id="_report_type" style="display: none;">1</div>
<div id="wrapper">
    <div class="que-main border-bot">
        <div class="que-main-cont">
            <div class="ques-top fn-clear">
                <span class="fn-l ques-vector"><i></i>${profileMap[question.askprofileid].nick} 的提问</span>
                <span class="fn-r ques-score" style="display: none;"><i></i>${question.point}分</span>
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
        <div class="follow-count fn-clear" style="display: none;">
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
                        <span id="follownum">${followInfo.page.totalRows}人关注</span>
                        <input type="hidden" value="${followInfo.page.totalRows}" name="follownum"/>
                    </c:when>
                    <c:otherwise>
                        <span id="follownum">还没人关注</span>
                        <input type="hidden" value="0" name="follownum"/>
                    </c:otherwise>
                </c:choose>


            </div>

            <a href="javascript:;" id="follow" <c:if test="${question.followstatus eq 1}">style="display:none;"
               </c:if>　>+关注</a>
            <a href="javascript:;" id="unfollow"
               <c:if test="${question.followstatus eq 0}">style="display:none;"</c:if>>已关注</a>

        </div>
    </div>
    <c:choose>
        <c:when test="${answerpage.totalRows>0}">
            <!-- 情况二 有回答时 -->

            <c:forEach items="${answerDetailDTOList}" var="answer">
                <div class="art-main">
                    <div class="ans-user fn-clear">
                        <a href="javascript:void(0);" class="ans-user-l ">
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
               							<%--<i></i>--%>
               							<%--<b class="zj">最佳</b>--%>
               						</font>
               						<b class="ans-user-tme">${dateutil:parseWanbaDate(answer.answertime)}</b>
               					</span>
                        </a>
                        <a href="javascript:;" class="ans-user-r" style="display: none;">
                            <c:if test="${not empty wanbaProfileMap[answer.profileid]&&answer.profileid ne pid}">
                                <span onclick="jump('56','pid=${answer.profileid}&point=${wanbaProfileMap[answer.profileid].askPoint}&name=${wanbaProfileMap[answer.profileid].nick}')">问TA</span>
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
                            <c:when test="${answerDetailDTOList.size()>1}">
                                ${answer.richbody}
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
                    <a href="javascript:onagree();" class="zan-count on"><i></i>
                        </c:when>
                        <c:otherwise>
                        <a href="javascript:agree('${answer.answerid}');" class="zan-count"
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
                        <a href="javascript:jump('55','${answer.answerid}');" class="mess-count"><i></i>
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
<div class="pop-up">
    <span></span>
</div>
<input type="hidden" name="pid" value="${pid}"/>
<input type="hidden" name="qid" value="${question.questionid}"/>
<input type="hidden" name="logindomain" value="${logindomain}"/>
<input type="hidden" name="appkey" value="${appkey}"/>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
        src="${URL_LIB}/static/js/init/question_action.js"></script>
<script>
    $(document).ready(function () {
        var lock = false;
        $("#follow").click(function () {
            if (!lock) {
                lock = true;
                var pid = $("input[name='pid']").val();
                var qid = $("input[name='qid']").val();
                var logindomain = $("input[name='logindomain']").val();
                if (pid == '' || logindomain == '' || logindomain == 'client') {
                    lock = false;
                    _jclient.showLogin();
                    return;
                }

                $.ajax({
                    url: "http://api." + joyconfig.DOMAIN + "/wanba/api/ask/question/follow",
                    data: {qid: qid, pid: pid},
                    timeout: 5000,
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        lock = false;
                        var follownum = $("input[name='follownum']").val();
                        if (follownum == 0) {
                            follownum = 1;
                            $("#follownum").text("1人关注");
                        } else {
                            follownum = parseInt(follownum) + 1;
                            $("#follownum").text(follownum + "人关注");
                        }
                        $("#follow").css("display", "none");
                        $("#unfollow").css("display", "block");
                        $("input[name='follownum']").val(follownum);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        $(".pop-up").find("span").text("无可用网络，请检查网络设置");
                        $(".pop-up").addClass("active");
                        var timmer;
                        clearTimeout(timmer);
                        timmer = setTimeout(function () {
                            $('.pop-up').removeClass('active');
                        }, 3000);
                        lock = false;
                        return;
                    }
                });
            }
        });

        var unfollowLock = false;
        $("#unfollow").click(function () {
            if (!unfollowLock) {
                unfollowLock = true;
                var pid = $("input[name='pid']").val();
                var qid = $("input[name='qid']").val();
                var logindomain = $("input[name='logindomain']").val();
                if (pid == '' || logindomain == '' || logindomain == 'client') {
                    unfollowLock = false;
                    _jclient.showLogin();
                    return;
                }
                $.ajax({
                    url: "http://api." + joyconfig.DOMAIN + "/wanba/api/ask/question/unfollow",
                    data: {qid: qid, pid: pid},
                    timeout: 5000,
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        unfollowLock = false;
                        var follownum = $("input[name='follownum']").val();
                        if (follownum == 1) {
                            follownum = 0;
                            $("#follownum").text("还没人关注");
                        } else {
                            follownum = parseInt(follownum) - 1;
                            $("#follownum").text(follownum + "人关注");
                        }
                        $("#follow").css("display", "block");
                        $("#unfollow").css("display", "none");
                        $("input[name='follownum']").val(follownum);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        $(".pop-up").find("span").text("无可用网络，请检查网络设置");
                        $(".pop-up").addClass("active");
                        var timmer;
                        clearTimeout(timmer);
                        timmer = setTimeout(function () {
                            $('.pop-up').removeClass('active');
                        }, 3000);
                        unfollowLock = false;
                        return;
                    }
                });
            }
        });
    });
    var agreeLock = false
    function agree(aid) {
        agreeLock = true;
        var pid = $("input[name='pid']").val();
        var appkey = $("input[name='appkey']").val();
        var logindomain = $("input[name='logindomain']").val();
        if (pid == '' || logindomain == '' || logindomain == 'client') {
            agreeLock = false;
            _jclient.showLogin();
            return;
        }
        $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/wanba/api/ask/answer/agree",
            data: {aid: aid, pid: pid, appkey: appkey},
            timeout: 5000,
            dataType: "json",
            type: "POST",
            success: function (data) {
                agreeLock = false;
                var agreenum = $("#agree" + aid).text();
                $("#agreeClass" + aid).addClass("on");
                $("#agreeClass" + aid).attr("href", "javascript:onagree()");


                if (!isNaN(agreenum)) {
                    $("#agree" + aid).text(parseInt(agreenum) + 1);
                } else {
                    $("#agree" + aid).text(1);
                }

                if (data.rs == 1) {
//                    if (data.pointtext != "") {
//                        $(".pop-up").find("span").text(data.pointtext);
//                        $(".pop-up").addClass("active");
//                        var timmer;
//                        clearTimeout(timmer);
//                        timmer = setTimeout(function () {
//                            $('.pop-up').removeClass('active');
//                        }, 3000);
//                    }
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $(".pop-up").find("span").text("无可用网络，请检查网络设置");
                $(".pop-up").addClass("active");
                var timmer;
                clearTimeout(timmer);
                timmer = setTimeout(function () {
                    $('.pop-up').removeClass('active');
                }, 3000);
                agreeLock = false;
                return;
            }
        });
    }
    function onagree() {
//        $(".pop-up").find("span").text("您已经点过赞了");
//        $(".pop-up").addClass("active");
//        var timmer;
//        clearTimeout(timmer);
//        timmer = setTimeout(function () {
//            $('.pop-up').removeClass('active');
//        }, 3000);
    }

    function jump(jt, id) {
        if (jt != '55' && jt != '51') {
            var pid = $("input[name='pid']").val();
            var logindomain = $("input[name='logindomain']").val();
            if (pid == '' || logindomain == '' || logindomain == 'client') {
                _jclient.showLogin();
                return;
            }
        }
        _jclient.jump("jt=" + jt + "&ji=" + encodeURIComponent(id));
    }
</script>
</body>
</html>