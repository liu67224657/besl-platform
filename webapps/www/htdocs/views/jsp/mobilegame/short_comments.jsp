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
    <title>${gameDB.gameName}_短评列表</title>
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
<body>
<div id="body-wrapper">
    <!-- topbar -->
    <div class="topbar imgbox">
        <a class="return-btn"
           href="${URL_WWW}/mobilegame/comment/${gameid}?lineid=${lineid}&contentid=${contentid}">${gameDB.gameName}</a>
        <a class="normal-btn" href="javascript:;">${page.totalRows}短评</a>
        <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
    </div>
    <div style="height:15px;"></div>

    <!-- 短评列表 -->
    <div class="pl-list-box">
        <c:forEach items="${gameDTO.shortcommentlist}" var="dtov">
            <div class="pl-list">
                <div class="pl-show">
                    <img src="${dtov.header}">

                    <div>
                        <!-- 用户名和评分 -->
                        <div class="d1">
                            <strong>${dtov.name}</strong>
                            <span class="score-1"><i style="width:${dtov.scope/10*100}%"></i></span>
                        </div>
                        <!-- 评论内容 -->
                        <div class="d2">${dtov.msg}</div>
                    </div>
                </div>
                <!-- 赞同和反对 -->
                <div class="pl-opinions">
                    <div onclick="like('${dtov.replyId}','1','${URL_WWW}')"
                         id="like${dtov.replyId}">${dtov.agreeNum}</div>
                    <!-- 赞同 -->
                    <div onclick="like('${dtov.replyId}','2','${URL_WWW}')"
                         id="dislike${dtov.replyId}">${dtov.disagreeNum}</div>
                    <!-- 反对 -->
                </div>
            </div>
        </c:forEach>
    </div>

</div>
<!-- 加载更多 -->

<div class="loading" id="loading-btn"><a href="javascript:;"
                                         <c:if test="${page.maxPage<2}">style="display:none;"</c:if>><b>加载更多</b></a>
    <input type="hidden" value="${page.curPage}" id="curpage"/>
    <input type="hidden" value="${page.maxPage}" id="maxpage"/>
</div>


</div>

<!-- 吐槽提示按钮 -->
<%--<div class="tc-place" id="tc-place">--%>
    <%--<div class="tc">--%>
        <%--<div class="tc-box">--%>
            <%--<div id="tc-btn">发表短评</div>--%>
            <%--<div id="tx-btn">分享按钮</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<!-- 吐槽输入框 -->--%>
<%--<div id="tc-layer" class="tc-layer">--%>
    <%--<div class="tc-form" id="tc-form">--%>
        <%--<div class="tc-ctrl">--%>
            <%--<span id="tc-cancel">取消</span>--%>
            <%--<span id="tc-submit">提交</span>--%>
        <%--</div>--%>
        <%--<div class="tc-form-box">--%>
            <%--<textarea id="tc-textarea" placeholder="发表你对${gameDB.gameName}的评论"></textarea>--%>
            <%--<span id="char-num">140</span>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--<!-- 分享提示框 -->--%>
<%--<div id="tc-share" class="tc-layer">--%>
    <%--<div class="share-to">--%>
        <%--<div class="h2">分享到</div>--%>
        <%--<div id="share-to-box">--%>
            <%--<!-- Baidu Button BEGIN -->--%>
            <%--<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">--%>
                <%--<a class="bds_qzone">QQ空间</a>--%>
                <%--<a class="bds_tsina">新浪微博</a>--%>
                <%--<a class="bds_tqq">腾讯微博</a>--%>
                <%--<a class="bds_tqf">腾讯朋友</a>--%>
            <%--</div>--%>
            <%--<script type="text/javascript" id="bdshare_js" data="type=tools&mini=1"></script>--%>
            <%--<script type="text/javascript" id="bdshell_js"></script>--%>
            <%--<script type="text/javascript">--%>
                <%--try {--%>
                    <%--var bds_config = {--%>
                        <%--'bdText': '#${gameDB.gameName}#（分享自 @着迷网）',//您的自定义分享内容--%>
                        <%--'bdPopTitle': '${gameDB.gameName}',//您的自定义pop窗口标题--%>
                        <%--'bdPic': '${gameDB.gameIcon}',--%>
                        <%--'snsKey': {'qzone': '', 'tsina': '1245341962', 'tqq': '801517256', 'tqf': ''}--%>
                    <%--};--%>
                    <%--document.getElementById('bdshell_js').src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000);--%>
                <%--} catch (e) {--%>
                <%--}--%>
            <%--</script>--%>
            <%--<!-- Baidu Button END -->--%>
        <%--</div>--%>
        <%--<div id="close-share">取消</div>--%>
    <%--</div>--%>
<%--</div>--%>

<!-- 返回顶部 -->
<div id="returntop" onclick="returnTop();" style="bottom:5rem">返回顶部</div>

<script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
<script>

    $(document).ready(function () {

        $("#tc-submit").click(function (e) {
            var body = $("#tc-textarea").val();
            if ($.trim(body) == "" || body == "发表你对${gameName}的评论") {
                alert("评论不能为空哦");
                return false;
            }
            $.ajax({
                url: '${URL_WWW}/json/comment/mobile/post',
                type: 'post',
                async: false,
                data: {'cid': "${contentid}", 'body': body, 'type': '3'},
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
                        $.cookie("msg${gameid}", null, {expires: 0});
                        $.cookie("scope${gameid}", null, {expires: 0});
                        //在添加
                        $.cookie("msg${gameid}", msg, {expires: 10});
                        $.cookie("scope${gameid}", scope, {expires: 10});
                        history.go(0);

                    } else {
                        alert("发布失败");
                    }
                }
            });
        });

        $("#loading-btn").click(function () {
            var curNum = $("#curpage").val();
            var maxNum = $("#maxpage").val();

            if (parseInt(maxNum) <= parseInt(curNum)) {
                return;
            }
            curNum = parseInt(curNum) + parseInt(1);
            $.ajax({ type: "POST",
                url: joyconfig.URL_WWW + "/json/comment/mobile/shortcomments",
                data: {p: curNum, contentid:${contentid}},
                dataType: "json",
                success: function (data) {
                    var page = data.result.page;
                    var rows = data.result.rows;
                    if (page.curPage >= page.maxPage) {
                        $("#loading-btn").css("display", "none");
                    }

                    <%--<div onclick="like('${dtov.replyId}','1','${URL_WWW}')"--%>
                    <%--id="like${dtov.replyId}">${dtov.agreeNum}</div>--%>
                    for (var i = 0; i < rows.shortcommentlist.length; i++) {
                        var str = '<div class="pl-list">' +
                                '<div class="pl-show">' +
                                '<img src="' + rows.shortcommentlist[i].header + '">' +
                                '<div>' +
                                '<div class="d1">' +
                                '<strong>' + rows.shortcommentlist[i].name + '</strong>' +
                                '<span class="score-1"><i style="width:' + rows.shortcommentlist[i].scope / 10 * 100 + '%"></i></span>' +
                                '</div>' +
                                '<div class="d2">' + rows.shortcommentlist[i].msg + '</div>' +
                                '</div>' +
                                '</div>' +
                                '<div class="pl-opinions">' +
                                "<div onclick=like('" + rows.shortcommentlist[i].replyId + "','1','${URL_WWW}') id=like" + rows.shortcommentlist[i].replyId + "> "+ rows.shortcommentlist[i].agreeNum + " </div>" +
                                "<div onclick=like('" + rows.shortcommentlist[i].replyId + "','2','${URL_WWW}') id=dislike" + rows.shortcommentlist[i].replyId + "> "+ rows.shortcommentlist[i].disagreeNum + " </div>" +
                                '</div>' +
                                '</div>' +
                                '</div>';
                        $(".pl-list-box").append(str);
                    }
                    $("#curpage").val(page.curPage);
                    $("#maxpage").val(page.maxPage);

                }
            });
        });
    });
    tc();// 吐槽js
    fx(); // 分享弹出层
    tc_input() // 限定吐槽字数

</script>
</body>
</html>