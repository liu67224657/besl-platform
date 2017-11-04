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
<c:if test="${pid ne question.askprofileid&&timeout&&answertype eq 0}">
    <div id="_answer_status" style="display: none;">yes</div>
</c:if>
<div id="_questionid" style="display: none;">${question.questionid}</div>
<div id="_questiontitle" style="display: none;">${question.title}</div>

<div id="_share_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">${question.title}</div>
<div id="_desc" style="display: none;">
    <c:if test="${not empty  question.voice &&not empty question.voice.url}">[语音]</c:if>
    ${question.body.text}</div>
<div id="_pic" style="display: none;">http://huabao.joyme.com/wapimage/wanba_icon.jpg</div>
<div id="_share_url" style="display: none;">${share_url}</div>
<div id="_report_type" style="display: none;">1</div>
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
    <c:if test="${!timeout&&pid eq question.askprofileid && question.accepaid eq 0 &&question.reactivated eq 0}">
        <div class="restart suspend-ele">
            <a href="javascript:;">没有满意的答案？点击重新激活<i></i></a>
        </div>
    </c:if>
    <!-- 顶部悬浮 结束 -->
    <div id="wrapper">
        <div class="que-main border-bot">
            <div class="que-main-cont">
                <div class="ques-top fn-clear">
                    <span class="fn-l ques-vector"><i></i>${profileMap[question.askprofileid].nick}的提问</span>
                    <span class="fn-r ques-score"><i></i>${question.point}分</span>
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
            <c:if test="${timeout&&question.accepaid eq 0}">
                <div class="invite-icon">
                    <a href="javascript:jump('58','${question.questionid}');"><i></i>邀请回答</a>
                </div>
            </c:if>

        </div>

        <c:if test="${not empty gameAnimeTagMap[question.tagid]}">
            <div class="wiki-ans">
                <a href="javascript:jump('57','tagid=${question.tagid}&tagname=${gameAnimeTagMap[question.tagid].tag_name}');"
                   class="wiki-link fn-clear">
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
                                <a href="javascript:jump('51','${answer.profileid}');" class="ans-user-l ">
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
                                                                                    <c:if test="${question.fisrtaid eq answer.answerid&&answerpage.totalRows>1}">
                                                                                        <b class="zk">最快</b>
                                                                                    </c:if>
                                                         						</font>
                                                         						<b class="ans-user-tme">${dateutil:parseWanbaDate(answer.answertime)}</b>
                                                         					</span>
                                </a>
                                <a href="javascript:;" class="ans-user-r ">
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
                                    <c:when test="${answerpage.totalRows>1}"> <a
                                            href="http://api.${DOMAIN}/wanba/webview/answer/detail?aid=${answer.answerid}">
                                        <p class="limit-line">${answer.body.text}</p></a></c:when>
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
                                <a href="javascript:agree('${answer.answerid}');" id="agreeClass${answer.answerid}"
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
                                <a href="javascript:jump('55','${answer.answerid}');" class="mess-count"><i></i>
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
                                <a href="javascript:jump('51','${answer.profileid}');" class="ans-user-l ">
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
                                <a href="javascript:;" class="ans-user-r ">
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
                                    <c:when test="${answerpage.totalRows>1}"> <a
                                            href="http://api.${DOMAIN}/wanba/webview/answer/detail?aid=${answer.answerid}">
                                        <p class="limit-line">${answer.body.text}</p></a></c:when>
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
                                <a href="javascript:agree('${answer.answerid}');" id="agreeClass${answer.answerid}"
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
                                <a href="javascript:jump('55','${answer.answerid}');" class="mess-count"><i></i>
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
                                    <a href="javascript:jump('51','${answer.profileid}');" class="ans-user-l ">
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
                                    <a href="javascript:;" class="ans-user-r ">
                                        <c:if test="${not empty wanbaProfileMap[answer.profileid]&&answer.profileid ne pid}">
                                            <span onclick="jump('56','pid=${answer.profileid}&point=${wanbaProfileMap[answer.profileid].askPoint}&name=${wanbaProfileMap[answer.profileid].nick}')">问TA</span>
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
                                            <a href="http://api.${DOMAIN}/wanba/webview/answer/detail?aid=${answer.answerid}">
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
                                <a href="javascript:onagree();" class="zan-count on"><i></i>
                                    </c:when>
                                    <c:otherwise>
                                    <a href="javascript:agree('${answer.answerid}');" id="agreeClass${answer.answerid}"
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
                                    <a href="javascript:jump('55','${answer.answerid}');" class="mess-count"><i></i>
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
                    </c:forEach>


                </c:when>
                <c:otherwise>
                    <!-- 情况一 无人参与 -->
                    <div class="no-join-ans">还没有人回答哦</div>
                </c:otherwise>
            </c:choose>


        </div>

        <!-- 参与回答列表 结束 -->
        <div class="mask"></div>
        <div class="hint">
            <p>是否确认重新激活</p>
            <div class="hint-btn">
                <a href="javascript:;" class="cancle-btn">取消</a>
                <a href="javascript:;" class="sure-btn" onclick="reactivated();">确定</a>
            </div>
        </div>

    </div>
</div>
<div class="pop-up">
    <span></span>
</div>
<input type="hidden" name="pid" value="${pid}"/>
<input type="hidden" name="qid" value="${question.questionid}"/>
<input type="hidden" name="askpid" value="${question.askprofileid}"/>
<input type="hidden" name="accepaid" value="${question.accepaid}"/>
<input type="hidden" name="fisrtaid" value="${question.fisrtaid}"/>
<input type="hidden" name="logindomain" value="${logindomain}"/>
<input type="hidden" name="appkey" value="${appkey}"/>
<input type="hidden" name="blackprofile" value="${blackprofile}"/>
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
        })
        ;

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
    })
    ;
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
                    if (data.pointtext != "") {
                        $(".pop-up").find("span").text(data.pointtext);
                        $(".pop-up").addClass("active");
                        var timmer;
                        clearTimeout(timmer);
                        timmer = setTimeout(function () {
                            $('.pop-up').removeClass('active');
                        }, 3000);
                    }
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
        $(".pop-up").find("span").text("您已经点过赞了");
        $(".pop-up").addClass("active");
        var timmer;
        clearTimeout(timmer);
        timmer = setTimeout(function () {
            $('.pop-up').removeClass('active');
        }, 3000);
    }

    var accepLock = false
    function accepaid(aid) {
        accepLock = true;
        var pid = $("input[name='pid']").val();
        var appkey = $("input[name='appkey']").val();
        var logindomain = $("input[name='logindomain']").val();
        var qid = $("input[name='qid']").val();
        if (pid == '' || logindomain == '' || logindomain == 'client') {
            accepLock = false;
            _jclient.showLogin();
            return;
        }
        $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/wanba/api/ask/answer/accept",
            data: {aid: aid, pid: pid, qid: qid, appkey: appkey},
            timeout: 5000,
            dataType: "json",
            type: "POST",
            success: function (data) {
                accepLock = false;
                location.reload();

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $(".pop-up").find("span").text("无可用网络，请检查网络设置");
                $(".pop-up").addClass("active");
                var timmer;
                clearTimeout(timmer);
                timmer = setTimeout(function () {
                    $('.pop-up').removeClass('active');
                }, 3000);
                accepLock = false;
                return;
            }
        });
    }
    var relock = false;
    function reactivated() {

        relock = true;
        var pid = $("input[name='pid']").val();
        var appkey = $("input[name='appkey']").val();
        var logindomain = $("input[name='logindomain']").val();
        var qid = $("input[name='qid']").val();
        if (pid == '' || logindomain == '' || logindomain == 'client') {
            _jclient.showLogin();
            return;
        }
        $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/wanba/webview/ask/reactivated",
            data: {pid: pid, qid: qid},
            timeout: 5000,
            dataType: "json",
            type: "POST",
            success: function (data) {
                if (data.rs == 1) {
                    location.href = "http://api." + joyconfig.DOMAIN + "/wanba/webview/ask/qdetail?qid=" + data.qid + "&pid=" + pid + "&logindomain=" + logindomain + "&appkey=" + appkey;
                } else if (data.rs == -3) {
                    relock = false;
                    $(".pop-up").find("span").text("该问题已经激活过了");
                    $(".pop-up").addClass("active");
                    var timmer;
                    clearTimeout(timmer);
                    timmer = setTimeout(function () {
                        $('.pop-up').removeClass('active');
                    }, 3000);
                    return;
                } else {
                    relock = false;
                    $(".pop-up").find("span").text("激活问题失败");
                    $(".pop-up").addClass("active");
                    var timmer;
                    clearTimeout(timmer);
                    timmer = setTimeout(function () {
                        $('.pop-up').removeClass('active');
                    }, 3000);
                    return;
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                relock = false;
                $(".pop-up").find("span").text("无可用网络，请检查网络设置");
                $(".pop-up").addClass("active");
                var timmer;
                clearTimeout(timmer);
                timmer = setTimeout(function () {
                    $('.pop-up').removeClass('active');
                }, 3000);
                return;
                return;
            }
        });
    }

    function jump(jt, id) {
        var pid = $("input[name='pid']").val();
        var logindomain = $("input[name='logindomain']").val();
        if (jt == '56') {
            if (pid == '' || logindomain == '' || logindomain == 'client') {
                _jclient.showLogin();
                return;
            }
            var blackprofile = $("input[name='blackprofile']").val();
            if (blackprofile == '1') {
                $(".pop-up").find("span").text("您已被禁言，如有疑问请联系客服");
                $(".pop-up").addClass("active");
                var timmer;
                clearTimeout(timmer);
                timmer = setTimeout(function () {
                    $('.pop-up').removeClass('active');
                }, 3000);
                return;
            }
            _jclient.jump("jt=" + jt + "&ji=" + encodeURIComponent(id));
        } else if (jt == '58') {
            if (jt != '55') {
                if (pid == '' || logindomain == '' || logindomain == 'client') {
                    _jclient.showLogin();
                    return;
                }
            }
            var qid = $("input[name='qid']").val();
            $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/wanba/api/ask/question/checkask/acceptstatus",
                data: {qid: qid},
                timeout: 5000,
                dataType: "json",
                type: "POST",
                success: function (data) {
                    if (data.rs == -50112) {
                        $(".pop-up").find("span").text("问题已经被解决了");
                        $(".pop-up").addClass("active");
                        var timmer;
                        clearTimeout(timmer);
                        timmer = setTimeout(function () {
                            $('.pop-up').removeClass('active');
                        }, 3000);
                        return;
                    } else {
                        _jclient.jump("jt=" + jt + "&ji=" + encodeURIComponent(id));
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
                    return;
                }
            });
        } else {
            if (jt != '55' && jt != '51' && jt != '57') {
                if (pid == '' || logindomain == '' || logindomain == 'client') {
                    _jclient.showLogin();
                    return;
                }
            }
            _jclient.jump("jt=" + jt + "&ji=" + encodeURIComponent(id));
        }
    }
</script>
</body>
</html>