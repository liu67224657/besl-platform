<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>正在维护中…</title>
    <script type="text/javascript">
        ;(function(doc,win) {
            var size=750;//设计稿的尺寸
            var int=100;  //初始字体值
            var docEle = doc.documentElement,
                    evt = 'onorientationchange' in window ? 'orientationchange' : 'resize',
                    fn = function() {
                        var width = docEle.clientWidth;
                        var w=document.getElementById('wrapper');
                        if(width>size){
                            w.className='';
                            docEle.style.fontSize=int+'px';
                        }else{
                            w.className='m';
                            width && (docEle.style.fontSize = width/(size/int) + 'px');
                        }
                        doc.addEventListener('touchstart',function () {return false}, true);
                    };
            win.addEventListener(evt, fn, false);
            doc.addEventListener('DOMContentLoaded', fn, false);
        }(document,window));
    </script>
</head>
<style>
    *{ padding:0; margin:0; }
    body{ background:url(http://static.joyme.com/pc/ugcwiki/images/weihu/bg.png) repeat 0 0; font:12px/1.5 arial, \5FAE\8F6F\96C5\9ED1, \5b8b\4f53, helvetica, sans-serif; }
    #wrapper{ width:780px; height:315px; position:absolute; left:50%; top:50%; margin:-162px 0 0 -390px; }
    .logo,.tips,.link{ width:780px; text-align:center; }
    .tips{ padding:90px 0 15px; font-size:40px; color:#595959; }
    .link dl dt,.link dl dd{ display:inline; }
    .link dl dt{ font-size:14px; color:#808080; }
    .link a{ padding:0 8px; font-size:14px; color:#808080; text-decoration:none;}
    .link a:hover{ color:#f35050; }
    #wrapper.m{ width:100%; max-width:750px; height:3.15rem; margin:0 auto; position:static; }
    .m .logo,.m .tips,.m .link{ width:100%; text-align:center; }
    .m .logo{ padding-top:2.7rem; }
    .m .logo img{ width:5.46rem; height:1.4rem; }
    .m .tips{ padding:3rem 0 0; font-size:.4rem; color:#595959; }
    .m .link { padding-top:1.66rem; }
    .m .link dl{ padding:0 .35rem; text-align:left; line-height:.4rem; }
    .m .link dl dt,.link dl dd{ display:inline; }
    .m .link dl dt{ font-size:.28rem; color:#808080; }
    .m .link a{ padding:0 .1rem; font-size:.28rem; color:#808080; text-decoration:none; }
    .m .link a:hover{ color:#f35050; }
    .m .link span{ display:block; text-align:center; }
</style>
<body>
<div id="wrapper">
    <div class="logo"><img src="http://static.joyme.com/pc/ugcwiki/images/weihu/logo.png" alt="着迷，才有乐趣" title="着迷，才有乐趣" /></div>
    <div class="tips">着迷游戏库正在维护中…</div>
    <div class="link">
        <dl>
            <dt>以下内容可正常访问：</dt>
            <dd>
                <a href="http://www.joyme.com/">着迷网</a>
                <a href="http://www.joyme.com/news/">着迷资讯</a>
                <a href="http://wiki.joyme.com/">着迷WIKI</a>
                <a href="http://v.joyme.com/">着迷视频</a>
                <a href="http://www.joyme.com/gift">礼包中心</a>
                <a href="http://bbs.joyme.com/">论坛</a>
            </dd>
        </dl>
    </div>
</div>
</body>
</html>