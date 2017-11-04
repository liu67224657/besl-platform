<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>长评-打开</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/mobildgamestyle.css">
    <script type="text/javascript">
    window.addEventListener('DOMContentLoaded', function () {
        document.addEventListener('touchstart', function () {return false}, true)
    }, true);
    </script>
</head>
<body>
    <div id="body-wrapper">
        <!-- topbar -->
        <div class="topbar imgbox">
            <a class="return-btn" href="javascript:history.go(-1)">神庙逃亡2</a>
            <a class="normal-btn" id="tg-btn">投稿</a>
            <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
        </div>

        <!-- 长评内容 -->
        <div class="pl-content">
            <div class="pl-content-title">
                <h1>保卫萝卜2：极地冒险攻略：游戏评测-游戏玩法</h1>
                <p>09-03  作者: 裤裤</p>
            </div>
            <div class="pl-txt">
                <p>游戏的界面左上方代表玩家目前拥有的萝卜数量，萝卜可以造防御工事，并且不同的防御工事消耗的萝卜数量不一样，中间则是显示当前关卡攻击多少波怪物以及当前进行到了多少波，旁边的闪电符号是游戏加速，最大可以调整为2倍速度，在右边则是暂停按钮，暂停按钮可以随时在游戏中使用，最后关卡中的系统选项。</p>
                <img src="http://sy.joyme.com/uploads/140903/12-140Z3103230961.jpg">
                <p>游戏的界面左上方代表玩家目前拥有的萝卜数量，萝卜可以造防御工事，并且不同的防御工事消耗的萝卜数量不一样，中间则是显示当前关卡攻击多少波怪物以及当前进行到了多少波，旁边的闪电符号是游戏加速，最大可以调整为2倍速度，在右边则是暂停按钮，暂停按钮可以随时在游戏中使用，最后关卡中的系统选项。</p>
            </div>
        </div>

        <!-- footer -->
        <div class="footer">
            <div>&copy;2011－2015 joyme.com, all rights reserved</div>
            <div>京ICP备11029291号京公网安备110108001706号</div>
        </div>
    </div>

    <!-- 返回顶部 -->
    <div id="returntop" onclick="returnTop();">返回顶部</div>
    
    <!-- 吐槽提示按钮 -->
    <div class="tc-place" id="tc-place">
        <div class="tc">
            <div class="tc-box">
                <!-- 赞同和反对 -->
                <div class="pl-opinions">
                    <div class="sel">1790</div> <!-- 赞同 -->
                    <div>7040</div> <!-- 反对 -->
                </div>
                <div id="tx-btn">分享按钮</div>
            </div>
        </div>
    </div>

    <!-- 分享提示框 -->
    <div id="tc-share" class="tc-layer">
        <div class="share-to">
            <div class="h2">分享到</div>
            <div class="share-to-box">
                <a href="#"><img src="${URL_LIB}/static/theme/default/images/mobilegame/share-weixin.png">微信好友</a>
                <a href="#"><img src="${URL_LIB}/static/theme/default/images/mobilegame/share-qzone.png">QQ空间</a>
                <a href="#"><img src="${URL_LIB}/static/theme/default/images/mobilegame/share-pengyouquan.png">朋友圈</a>
                <a href="#"><img src="${URL_LIB}/static/theme/default/images/mobilegame/share-sinaweibo.png">新浪微博</a>
                <a href="#"><img src="${URL_LIB}/static/theme/default/images/mobilegame/share-txweibo.png">腾讯微博</a>
            </div>
            <div id="close-share">取消</div>
        </div>
    </div>

    <!-- 投稿弹层  -->
    <div class="tc-layer" id="tg-layer">
        <div class="contribution">
            <div>欢迎投稿</div>
            <p>欢迎您将您的个性评论发送到：</p>
            <a href="mailto:zhenggao@staff.joyme.com">zhenggao@staff.joyme.com</a>
            <p>PS: 请将邮件标题写明为：</p>
            <p>游戏名称+征稿，并留下您的联系方式，</p>
            <p>以便我们能即时联系到您并进行致谢！</p>
            <span id="tg-close-btn">知道了</span>
        </div>
    </div>

    <script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
    <script>
        fx(); // 分享弹出层
        contribution() // 投稿
    </script>
</body>
</html>