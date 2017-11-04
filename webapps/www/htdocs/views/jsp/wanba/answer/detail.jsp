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
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/common.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>


<div class="container suction-bot">
    <!-- 内容区域 开始 -->
    <div id="wrapper">
        <div class="art-title border-bot">
            <h1 class="limit-line ">${question.title}</h1>
        </div>
        <div class="art-main">
            <div class="ans-user fn-clear">
                <a href="javascript:jump('51','${wanbaProfileDTO.pid}');" class="ans-user-l ">
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
                <c:if test="${pid!=wanbaProfileDTO.pid && wanbaProfileDTO.vtype>0}">
                     <a href="javascript:;" class="ans-user-r"><span>问TA</span></a>
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
        <c:if test="${askUserAction!=null}">
            <a href="javascript:;" class="zan-count on"><i></i><span id="_agreeSum">${answersum.agreeSum==0?"点赞":answersum.agreeSum}</span></a>
        </c:if>
        <c:if test="${askUserAction==null}">
            <a href="javascript:;" class="zan-count"><i></i><span id="_agreeSum">${answersum.agreeSum==0?"点赞":answersum.agreeSum}</span></a>
        </c:if>
        <a href="javascript:;" class="mess-count"><i></i>${answersum.replySum==0?"评论":answersum.replySum}</a>
    </div>
    <div class="pop-up">
        <span>您已点赞过了！</span>
    </div>
    <!-- 赞/评论 结束-->
</div>


<!--收藏按钮-->
<c:if test="${ not empty logindomain && logindomain!='client'}">
    <div id="_favorite_status" style="display: none;">yes</div>
    <div id="_shoucang_status" style="display: none;">${favoriteAskUserAction}</div>
</c:if>

<!--问题ID答案ID-->
<div id="_questionid" style="display: none;">${question.questionId}</div>
<div id="_answerid" style="display: none;" value="${answer.answerId}">${answer.answerId}</div>


<!--分享按钮及信息-->
<div id="_share_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">${question.title}</div>
<div id="_desc" style="display: none;"> ${sharedesc}</div>
<div id="_pic" style="display: none;">http://huabao.joyme.com/wapimage/wanba_icon.jpg</div>
<div id="_share_url" style="display: none;">${shareurl}</div>

<div id="_report_type" style="display: none;">2</div>
<input type="hidden" value="${pid}" id="_pid"/>
</body>
<script src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var logindomain = "${logindomain}";
        //跳转到评论native
        $(".mess-count").on("click", function () {
            try {
               _jclient.jump('jt=55&ji=' + $("#_answerid").html());
            } catch (e) {
                console.log("mess-count");
            }
        });


        //问他
        $(".ans-user-r").on("click", function () {
            try {
                if (logindomain == '' || logindomain == 'client') {
                    _jclient.showLogin();
                } else {
                    _jclient.jump('jt=56&ji='+encodeURIComponent('pid=${wanbaProfileDTO.pid}&point=${wanbaProfileDTO.point}&name=${wanbaProfileDTO.nick}'));
                }
            } catch (e) {
                console.log("ans-user-r");
            }
        });

        var timer = ""
        $(".zan-count").on("click", function () {
            if (logindomain == '' || logindomain == 'client') {
                try {
                    _jclient.showLogin();
                } catch (e) {
                    console.log('zan');
                }
                return;
            }

            if ($(this).hasClass("on")) {
                $(".pop-up").addClass("active");
                clearTimeout(timer);
                timer = setTimeout(function () {
                    $(".pop-up").removeClass("active");
                }, 2000);
            } else {
                $.ajax({
                    url: 'http://api.' + joyconfig.DOMAIN + '/wanba/api/ask/answer/agree',
                    data: {pid: $("#_pid").val(), aid: $("#_answerid").html(),appkey:"3iiv7VWfx84pmHgCUqRwunA"},
                    type: 'POST',
                    dataType: "json",
                    success: function (req) {
                        if (req.rs == 1) {
                            $(".zan-count").addClass("on");
                            var agree =$("#_agreeSum").html();
                            if(req.pointtext != ""){
                                $(".pop-up").find("span").text(req.pointtext);
                                $(".pop-up").addClass("active");
                                clearTimeout(timer);
                                timer = setTimeout(function () {
                                    $(".pop-up").find("span").html("您已点赞过了！");
                                    $(".pop-up").removeClass("active");
                                }, 2000);
                            }
                            if(isNaN(agree)){
                                $("#_agreeSum").html(1);
                            }else{
                                $("#_agreeSum").html(parseInt(agree) + 1);
                            }
                        } else if (req.rs == -50107) {
                            $(".pop-up").addClass("active");
                            clearTimeout(timer);
                            timer = setTimeout(function () {
                                $(".pop-up").removeClass("active");
                            }, 2000);
                        }
                    }
                });
            }
        });


    });
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
</html>
