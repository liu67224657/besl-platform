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
    <meta name="Keywords" content="手机游戏排行榜,热门手机游戏,好玩的手机游戏"/>
    <meta name="description" content="着迷手机游戏排行榜,众多玩家玩家一起推荐的手机游戏."/>
    <title>评分</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/mobildgamestyle.css">
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body style="background:#fff">
<!-- topbar -->
<div class="topbar imgbox">
    <a class="cancel-btn" href="javascript:history.go(-1)">取消</a>
    <a class="fb-btn" href="javascript:void(0);" id="tc-formtable">发布</a>
    <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
</div>

<!-- 评分框 -->
<div class="set-score" id="set-score">
    <span></span>
    <span></span>
    <span></span>
    <span></span>
    <span></span>
</div>

<!-- 吐槽输入框 -->
<div class="tc-form tc-form2" id="tc-form">
    <div class="tc-form-box">
        <textarea id="tc-textarea" placeholder="发表你对${gameName}的短评"></textarea>
        <span id="char-num">140</span>
    </div>
</div>

<div id="tc-share" class="tc-layer">
    <div style="background:#eceef0; border-top:1px solid transparent; padding-bottom:10px;">
        <div class="share-title">您的评论闪耀着智慧的光芒！</div>

        <!-- 1分==>show-score-1， 2分==> show-score-2  以此类推 -->
        <div class="set-score " id="scoreId">
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
        </div>

        <!-- 显示评论内容 -->
        <div class="show-pl">
            <div id="print">
            </div>
        </div>
    </div>
    <div class="share-to">
        <div class="h2">分享到</div>
        <div id="share-to-box">
            <!-- Baidu Button BEGIN -->
            <div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare"
                 data="{'url':'${URL_WWW}/mobilegame/comment/${gameId}?lineid=${lineId}&contentid=${contentid}'}">
                <a class="bds_qzone">QQ空间</a>
                <a class="bds_tsina">新浪微博</a>
                <a class="bds_tqq">腾讯微博</a>
                <a class="bds_tqf">腾讯朋友</a>
            </div>
            <script type="text/javascript" id="bdshare_js" data="type=tools&mini=1"></script>
            <script type="text/javascript" id="bdshell_js"></script>
            <script type="text/javascript">
                try {
                    window.bds_config = {
                        'bdDes': '（分享自 @着迷网）',//您的自定义分享摘要
                        'bdText': '#${gameName}#（分享自 @着迷网）',//您的自定义分享内容
                        'bdUrl': '${URL_WWW}/mobilegame/comment/${gameId}?lineid=${lineId}&contentid=${contentid}',
                        'snsKey': {'qzone': '', 'tsina': '1245341962', 'tqq': '801517256', 'tqf': ''}
                    };
                } catch (e) {
                }
            </script>
            <!-- Baidu Button END -->
        </div>
        <div id="close-share">以后再说</div>
    </div>
</div>
<script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
<script>
    set_score();// 打分js
    tc_input() // 限定吐槽字数
    $(document).ready(function () {

        $("#tc-formtable").click(function (e) {
            //分享
            var body = $("#tc-textarea").val();
            <%--if ($.trim(body) == "" || body == "发表你对${gameName}的评论") {--%>
            <%--alert("评论不能为空哦");--%>
            <%--return false;--%>
            <%--}--%>
            var userScore = window.score;
            if (userScore != null) {
                userScore = (userScore + 1) * 2;
            } else {
                userScore = 0;
            }
            if (userScore == 0 && $.trim(body) == "" || body == "发表你对${gameName}的评论") {
                alert("评论或者评分至少选择一项");
                return false;
            }
            $.ajax({
                url: '${URL_WWW}/json/comment/mobile/post',
                type: 'post',
                async: false,
                data: {'cid': "${contentid}", 'body': body, 'type': '3', score: userScore},
                dataType: "json",
                success: function (data) {
                    if (data.rs == 11014) {
                        alert("时间间隔太短了，请稍后评论！");
                        $("#opinionResult").val("false");
                    } else if (data.rs == -305) {
                        alert("评论中含有不适当的内容！");
                        $("#opinionResult").val("false");
                    } else if (data.rs == 1) {
                        var msg = data.result.msg;
                        var scope = data.result.scope;
                        //先清空cookie
                        $.cookie("msg${gameId}", null, {expires: 0});
                        $.cookie("scope${gameId}", null, {expires: 0});
                        //在添加
                        $.cookie("msg${gameId}", msg, {expires: 10});
                        $.cookie("scope${gameId}", scope, {expires: 10});
                        if (msg != "" && msg != null) {
                            $("#tc-share").css("display", "block");
                            bds_config = {
                                'bdDes': '（分享自 @着迷网）',//您的自定义分享摘要
                                'bdText': '分享我的观点:' + msg + '（分享自 @着迷网）',//您的自定义分享内容
                                'bdUrl': '${URL_WWW}/mobilegame/comment/${gameId}?lineid=${lineId}&contentid=${contentid}',
                                'snsKey': {'qzone': '', 'tsina': '1245341962', 'tqq': '801517256', 'tqf': ''}
                            };
                            document.getElementById('bdshell_js').src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000);

                            $("#shareId").val(msg);
                            if (scope != 0) {
                                $("#scoreId").addClass("get-score-" + (scope / 2));
                            } else {
                                $("#scoreId").css("display", "none");
                            }
                            $("#print").html(msg);
                        } else {
                            window.location = "${URL_WWW}/mobilegame/comment/${gameId}?lineid=${lineId}&contentid=${contentid}";
                        }

                    } else {
                        alert("发布失败");
                    }
                }
            });
        })
    });
    $("#close-share").click(function () {
        window.location = "${URL_WWW}/mobilegame/comment/${gameId}?lineid=${lineId}&contentid=${contentid}";
    });
</script>
</body>
</html>