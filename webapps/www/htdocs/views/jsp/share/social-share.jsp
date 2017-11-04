<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/social.css">
    <title>咔哒分享</title>

    <script type="text/javascript">
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

            // 载入元数据完成
//            $audio.addEventListener('loadedmetadata', function (e) {
//                var a = parseInt(e.target.duration / 60);
//                var b = parseInt(e.target.duration % 60);
//                document.getElementById('fen').innerHTML = a;
//                document.getElementById('miao').innerHTML = b;
//            }, false);

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
    </script>

</head>
<body>

<c:choose>
    <c:when test="${not empty socialContent}">
        <c:if test="${not empty socialContent.audio.mp3}">
            <audio id="audio" style="display:none; height:0; background:transparent" preload="metadata"
                   src="${socialContent.audio.mp3}"></audio>
        </c:if>

        <div class="tit">
            <img src="${uf:parseFacesInclude(profile.blog.headIconSet,profile.detail.sex,"s" , true,0,1)[0]}">
            <span>${profile.blog.screenName}</span>
        </div>

        <div class="record">
            <img src="${socialContent.pic.pic}">
            <c:if test="${not empty socialContent.audio.mp3}">
                <span class="pbtn play" id="playbtn" flag="play"></span>
            </c:if>
            <div class="progress"><span id="progress"></span></div>
        </div>

        <div class="record-info">
            <c:if test="${not empty socialActivity}">
                <div>#${socialActivity.title}#</div>
            </c:if>


            <div><c:if test="${not empty socialContent.audio.mp3}">${socialContent.playNum}播放 | <span
                    id="fen">${fen}</span>'<span id="miao">${miao}</span>'' </c:if></div>

        </div>

        <div class="record-dis">${socialContent.body}</div>
        <c:if test="${not empty profileList}">
            <div class="favorit">
                <div>
                    <c:forEach items="${profileList}" var="profile">
                        <img src="${uf:parseFacesInclude(profile.blog.headIconSet,profile.detail.sex,"s" , true,0,1)[0]}">
                    </c:forEach>

                </div>
                <a class="click" href="javascript:;">${socialContent.agreeNum}</a>
            </div>
        </c:if>
        <c:if test="${platform=='0'}">
            <div class="recommend-t">
                <h2>咔哒推荐</h2>

                <div>使用咔哒可以和她互动</div>
            </div>

            <%@ include file="/hotdeploy/views/jsp/social/social-share-recommend.jsp" %>
        </c:if>


        <div class="more">
            <div>更多精彩内容请下载 <strong>Kada</strong> 查看</div>
            <div class="weixin">微信公众账号：<strong>咔哒</strong></div>
            <div class="weibo">新浪微博公众账号：<strong>着迷咔哒_Kada</strong></div>
        </div>

        <c:if test="${platform=='0'}">
            <div id="bottomLayer" class="bottomLayer" style="position:static">
                <div><span>咔哒</span>有声有影，随心随性</div>
                <a href="http://www.joyme.com/click/xxzfjk-xxzfol-xxzf42">免费下载</a>
            </div>
        </c:if>


    </c:when>
    <c:otherwise>
        <div class="notfound"><img src="${URL_LIB}/static/theme/default/images/socialwap404.jpg"></div>
    </c:otherwise>
</c:choose>

</body>
</html>