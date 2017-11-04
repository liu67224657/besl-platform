<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>星际火线-圆盘抽奖</title>
    <meta name="Description" content=""/>
    <meta name="Keywords" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <meta name="format-detection" content="telephone=no"/>
    <link rel="stylesheet" href="http://api.joyme.com/static/css/activity/xinjihuoxian/style.css">
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function (e) {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
            document.getElementById('wrap').style.zoom = e.target.activeElement.clientWidth / 375;
        });

    </script>
    <script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<div id="wrap">
    <div class="main">
        <div class="dial">
            <div class="my">我的奖品</div>
            <div class="dial_box"></div>
            <div class="dial_btn"></div>
        </div>
        <div class="prize">
            <ul class="prize_box">
                <li>恭喜<span>love5217 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>zxl2616938 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>宝宝乖 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>緿縡の潴 抽中了</span>京东卡100元</li>
                <li>恭喜<span>nice_bin123 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>wanyu520815 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>丨嘟嘟丶 抽中了</span>京东卡100元</li>
                <li>恭喜<span>zjshhuxue 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>Gary_luo 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>雅妮づ妖 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>yiwei859 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>恋轻轻De回眸 抽中了</span>话费充值卡50元</li>
            </ul>
        </div>
        <div class="bg"><img src="http://api.joyme.com/static/img/activity/xinjihuoxian/bg.jpg" alt=""></div>
    </div>
    <div class="pop_box" style="display:none">
        <div class="pop pop1" style="display:none">
            <p></p>
            <cite class="sure_btn"></cite>
        </div>
        <div class="pop2" style="display:none">
            <p>
                <input type="text" class="iphone">
                <input type="text" class="qq">
                <input type="text" class="address">
            </p>
            <cite class="submit_btn"></cite>
        </div>
        <div class="pop pop3" style="display:none">
            <p>抱歉！您已经抽取过该礼包了！请继续关注“公众号”，更多福利等你来拿！</p>
            <cite class="close_btn"></cite>
        </div>
        <div class="my_box" style="display:none;">
            <cite class="close_btn"></cite>

            <div id="myLottery">

            </div>
        </div>
    </div>
    <div class="tip" style="display:none;">
        <span>请搜索“xjhx666”，关注星际火线官方微信公众号，参加活动</span>
    </div>
    <div class="hint" style="display:none;"></div>
</div>
<script language="javascript">
    $(document).ready(function(){
        var isWeiXin={
            int:function(){
                var ua = window.navigator.userAgent.toLowerCase();
                if(ua.match(/MicroMessenger/i) == 'micromessenger'){
                    return true;
                }else{
                    return false;
                }
            }
        }
        $('.tip').show();
    });
</script>
</body>
</html>
