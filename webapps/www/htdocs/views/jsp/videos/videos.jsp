<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <title>着迷网移动版_iphone游戏_安卓游戏_好玩的手机游戏推荐网站</title>
    <meta name="Keywords" content="iphone游戏,安卓游戏,好玩的手机游戏,手机游戏推荐" />
    <meta name="description" content="着迷网为手机游戏爱好者提供好玩的iPhone游戏及安卓游戏下载,最实用的游戏攻略,最真实的手游评测,还有大量热门游戏礼包,玩手游就来着迷网." />
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/video/common.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        ;(function(doc,win) {
            var size=750;
            var int=100;
            var docEle = doc.documentElement,
                    evt = 'onorientationchange' in window ? 'orientationchange' : 'resize',
                    fn = function() {
                        var width = docEle.clientWidth;
                        if(width>size){
                            docEle.style.fontSize=int+'px';
                        }else{
                            width && (docEle.style.fontSize = width/(size/int) + 'px');
                        }
                        //doc.addEventListener('touchstart',function () {return false}, true);
                    };
            //横屏判断
            win.addEventListener(evt, fn, false);
            doc.addEventListener('DOMContentLoaded', fn, false);
        }(document,window));
    </script>

</head>
<body>

    <div class="tab-view">
        <div class="tab-menu">
            <p>
                <span class="js-on">热门视频</span>
                <span>我的视频</span>
            </p>
        </div>
        <div class="tab-wrap">
            <div class="tab-box js-show">
                <c:choose>
                    <c:when test="${not empty hotCommentVideos}">
                        <ul class="fn-ovf">
                            <c:forEach items="${hotCommentVideos}" var="videos">

                                <li>
                                     <span class="video-btn">
                            	<i><img src="${videos.jsonUrl.qnurl}?vframe/jpg/offset/1" alt="" title=""></i>
                                <font>${videos.videoTitle}</font>
                            	<video preload="meta" src="${videos.jsonUrl.qnurl}"></video>
                            </span>

                                    <%--<a href="#">--%>
                                        <%--<i><img src="${videos.videoPic}" alt="" title=""/></i>--%>
                                        <%--<b></b>--%>
                                    <%--</a>--%>
                                </li>
                            </c:forEach>
                        </ul>

                    </c:when>
                    <c:otherwise>
                        <div class="not-info">暂时还没有热门视频！</div>
                    </c:otherwise>
                </c:choose>

            </div>
            <div class="tab-box">
                <c:choose>
                    <c:when test="${not empty myVideoList}">
                        <ul class="fn-ovf">
                            <c:forEach items="${myVideoList}" var="videos">
                                <li>
                                                                         <span class="video-btn">
                            	<i><img src="${videos.jsonUrl.qnurl}?vframe/jpg/offset/1" alt="" title=""></i>
                                <font>${videos.videoTitle}</font>
                            	<video preload="meta" src="${videos.jsonUrl.qnurl}"></video>
                            </span>
                                    <%--<a href="#">--%>
                                        <%--<i><img src="${videos.videoPic}" alt="" title=""/></i>--%>
                                        <%--<b>${videos.videoTitle}</b>--%>
                                    <%--</a>--%>
                                </li>
                            </c:forEach>
                        </ul>

                    </c:when>
                    <c:otherwise>
                        <div class="not-info">还没有上传过视频呢！</div>
                    </c:otherwise>
                </c:choose>


            </div>
        </div>
    </div>

<script>
    ;(function(){
        var mod={
            tabView:function(){
                var menu=document.querySelector('.tab-menu');
                var btn=menu.querySelectorAll('span');
                var wrap=document.querySelector('.tab-wrap');
                var box=wrap.querySelectorAll('.tab-box');
                var videoBtn=document.querySelectorAll('.video-btn');
                for(var i=0;i<btn.length;i++){
                    btn[i].index=i;
                    btn[i].addEventListener('touchstart',function(){
                        for(var j=0;j<btn.length;j++){
                            btn[j].classList.remove('js-on');
                            this.classList.add('js-on');
                            box[j].classList.remove('js-show');
                            box[this.index].classList.add('js-show');
                        }
                    },false);
                };
                for(var k=0;k<videoBtn.length;k++){
                    videoBtn[k].addEventListener('touchstart',function(){
                        this.querySelector('video').play();
                    },false);
                }
            }
        }
        mod.tabView();
    }());
</script>
</body>
</html>
