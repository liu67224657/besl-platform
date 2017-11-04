<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="wap-font-scale" content="no">
    <title>答案详情</title>
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/common.css?v=20161222">
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

<div class="container suction-bot">
    <!-- 内容区域 开始 -->
    <div id="wrapper">
        <div class="art-title border-bot">
            <h1 class="limit-line ">${question.title}</h1>
        </div>
        <div class="art-main">
            <div class="ans-user fn-clear">
                <a href="jmwanba://jt=59&ji=${answer.answerId}" class="ans-user-l ">
                    <c:choose>
                        <c:when test="${not empty wanbaProfileDTO.icon}">
                            <cite><img src="${wanbaProfileDTO.icon}"></cite>
                        </c:when>
                        <c:otherwise>
                            <cite><img src="${URL_LIB}/hotdeploy/static/img/wanba_default.png"></cite>
                        </c:otherwise>
                    </c:choose>
						<span>
							<font class="ans-user-name fn-clear">
                                <em class="limit">${wanbaProfileDTO.nick}</em>
                                <c:if test="${wanbaProfileDTO.vtype>0}">
                                    <i></i>
                                </c:if>
                                <c:if test="${answer.isAccept==true && question.type.code==2}">
                                    <b class="zj">最佳</b>
                                </c:if>
                                <c:if test="${answer.answerId==question.firstAnswerId}">
                                    <b class="zk">最快</b>
                                </c:if>
                            </font>
							<b class="ans-user-tme">${dateutil:parseWanbaDate(answer.answerTime.getTime())}</b>
						</span>
                </a>
                <c:if test="${wanbaProfileDTO.vtype>0}">
                    <a href="jmwanba://jt=59&ji=${answer.answerId}" class="ans-user-r "><span>问TA</span></a>
                </c:if>
            </div>

            <c:if test="${not empty  answer.askVoice && not empty answer.askVoice.url}">
                <div class="audio-box">
                    <audio src="${answer.askVoice.url}"></audio>
                    <i></i>
                    <b>
                        <c:set var="time" value="${answer.askVoice.time/1000}"/>
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
            ${answer.richText}
        </div>
    </div>
    <!-- 内容区域 结束 -->
    <!-- 赞/评论 开始-->

    <div class="suspend fn-clear">
        <a href="jmwanba://jt=59&ji=${answer.answerId}" class="zan-count"><i></i>${answersum.agreeSum==0?"点赞":answersum.agreeSum}</a>
        <a href="jmwanba://jt=59&ji=${answer.answerId}" class="mess-count"><i></i>${answersum.replySum==0?"评论":answersum.replySum}</a>
    </div>
    <!-- 赞/评论 结束-->
</div>


</body>
<script src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script>
    fnMenu ={
        oAudio:function(){//音频
            var audioBox = $('.audio-box'),
                    audioEle = audioBox.find('audio');
            inta = true;
            audioBox.click(function(){
                var $this = $(this),
                        actAudio = $this.find('audio')[0];
                audioEle.parent('.audio-box').find('i').removeClass('on');
                audioEle.each(function(){
                    var eventTester = function(e){
                        actAudio.addEventListener(e,function(){
                            $(actAudio).parent('.audio-box').find('i').removeClass('on');
                        },false);
                    }
                    eventTester("ended");
                    if(this == actAudio){
                        if(this.paused){
                            actAudio.load();
                            this.play();
                            $(actAudio).parent('.audio-box').find('i').addClass('on');
                        }else{
                            audioEle.parent('.audio-box').find('i').removeClass('on');
                            this.pause();
                        }
                    }else{
                        this.pause();
                    }
                });
            });
        },

        int:function(parent,time){
            fnMenu.oAudio();
        }
    }
    fnMenu.int();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/wap/wanba/share.js"></script>
</html>
