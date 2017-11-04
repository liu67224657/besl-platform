<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="${URL_LIB}/static/theme/default/css/wca.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <title>分享</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {return false}, true)
        }, true);

        window.onload = function () {
            setTimeout(scrollTo, 0, 0, 0)
        };
        document.addEventListener('DOMContentLoaded', function () {

            var $audio = document.getElementById('audio');
            var $playbtn = document.getElementById('playbtn');
            var $progress = document.getElementById('progress');

            $progress.style.width = '0px';

            document.addEventListener('touchstart', function (e) {
                document.getElementById('bottomLayer').style.position = 'fixed';
                return false;
            }, true);

            $playbtn.addEventListener('touchstart', function (e) {
                e.preventDefault();
                audioCtroll(this);
            }, true);

            //载入错误
            $audio.addEventListener('error', function (e) {
                window.errMsg = '载入媒体失败！';
            }, false);

            // 正在播放(当前播放时间正在改变)
            $audio.addEventListener('timeupdate', function (e) {
                var a = Math.floor(e.target.duration);
                var b = Math.floor(e.target.currentTime);
                var c = Math.floor((b / a) * 100);

                $progress.style.width = c + '%';
            }, false);

            // 开始播放
            $audio.addEventListener('play', function () {

                if ($playbtn.getAttribute('flag') === 'pause') {

                    $playbtn.setAttribute('flag', 'play');
                    $playbtn.className = $playbtn.className.replace('pause', 'play');

                } else {

                    $playbtn.setAttribute('flag', 'pause');
                    $playbtn.className = $playbtn.className.replace('play', 'pause');

                }
            }, false);

            // 暂停
            $audio.addEventListener('pause', function () {

                if ($playbtn.getAttribute('flag') === 'pause') {

                    $playbtn.setAttribute('flag', 'play');
                    $playbtn.className = $playbtn.className.replace('pause', 'play');

                } else {

                    $playbtn.setAttribute('flag', 'pause');
                    $playbtn.className = $playbtn.className.replace('play', 'pause');

                }
            }, false);

            // 播放完毕
            $audio.addEventListener('ended', function () {
                $progress.style.width = 0;
                $playbtn.className = $playbtn.className.replace('pause', 'play');
                $playbtn.setAttribute('flag', 'play');
            }, false);

        }, true);
        function audioCtroll(tag) {
            if (window.errMsg) {
                alert(window.errMsg);
                return;
            }

            var t = document.getElementById('audio');

            if (tag.getAttribute('flag') === 'pause') {
                t.pause();
            } else {
                t.play();
            }
        }

        var uno = '${uno}';
        var lock = false;
        $(document).ready(function(){
            $("a[name=agree]").click(function (e) {
                e.preventDefault();
                if (lock) {
                    return false;
                }

                lock = true;
                var div= $(this).find("div")[0];
                var cid = $(this).attr("data-cid");
                var hasAgree = $(div).hasClass("vv"); //已经赞过
                var action = (uno != null && uno.length > 0 && hasAgree) ? 0 : 1;

                $.ajax({
                    url: '/json/kada/agree',
                    type: 'post',
                    data: {'uno': uno, 'action': action,'cid':cid},
                    dataType: "json",
                    success: function (data) {
                        if (data.rs == "1") {
                            if (action == 1) {
                                div.className="z-btn-2 vv";
                            } else {
                                div.className="z-btn-2";
                            }
                        }
                    },
                    complete: function () {
                        lock = false;
                    }
                })
            });
        });
    </script>
</head>
<body>
<audio id="audio" style="display:none; height:0; background:transparent" preload="load" src="${dto.content.audio.mp3}"></audio>

<div class="record" id="record">
    <img src="${dto.content.pic.pic}">
    <c:if test="${not empty socialContent.audio.mp3}">
        <span class="pbtn play" id="playbtn" flag="play"></span>
    </c:if>
    <div class="progress"><span id="progress" style="width: 0px;"></span></div>
    <a href="#" name="agree" data-cid="${dto.content.cid}"><div class="z-btn-2 <c:if test="${dto.content.isagree}">vv</c:if>"></div></a>
</div>

<div class="record-info">
    <div>#${dto.content.activity.title}</div>
    <div><div><c:if test="${not empty socialContent.audio.mp3}">${socialContent.playNum}播放 | <span id="fen">${fen}</span>'<span id="miao">${miao}</span>''</c:if></div></div>
</div>

<div class="record-dis">${dto.content.body}</div>
<div style="height:50px"></div>

</body>
</html>