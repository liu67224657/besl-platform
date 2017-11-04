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
    <title>手机游戏排行榜_热门手机游戏_着迷网Joyme.com</title>
    <meta name="Keywords" content="手机游戏排行榜,热门手机游戏,好玩的手机游戏"/>
    <meta name="description" content="着迷手机游戏排行榜,众多玩家玩家一起推荐的手机游戏."/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/mobildgamestyle.css">
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
    <div class="topbar-place"> <!-- 注意： 这行只有首页会出现 -->
        <div class="topbar imgbox">
            <img src="${URL_LIB}/static/theme/default/images/mobilegame/logo.png" class="logo">
            <a id="tc-shut" href="javascript:void(0);"><span id="gagnum">${forignContentGag.replyNum}</span>吐槽<em></em></a>
            <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
        </div>
    </div>
    <!-- 注意： 这行只有首页会出现 -->

    <!-- focus -->
    <div class="focus imgbox">
        <img src="${dto.indexpic}">

        <div>${dto.linename}</div>
    </div>

    <div class="focus-txt">${dto.linedesc}</div>

    <div id="listbox">
        <!-- 列表 -->
        <c:forEach items="${dto.gamedtolist}" var="dtov" varStatus="st">
            <div class="list">
                <!-- 游戏 -->
                <a class="game-info"
                   href="${URL_WWW}/mobilegame/comment/${dtov.gamedbid}?lineid=${lineid}&contentid=${dtov.contentid}">
                    <img src="${dtov.gameicon}">

                    <div>
                        <div class="h2">${dtov.gamename}</div>
                        <p>
                            <code>${dtov.replynum}评</code>
                            <span class="score-1"><i style="width:${dtov.average_score_per}%"></i></span>
                            <em>${dtov.average_score}</em>
                        </p>
                    </div>
                </a>
                <!-- 用户的点评 -->
                <c:forEach items="${dtov.shortcommentlist}" var="dtov2">
                    <div class="user-dp">
                        <img src="${dtov2.header}">

                        <div>${dtov2.msg}</div>
                    </div>
                </c:forEach>

                <!-- 编号 -->
                <div class="list-num">${st.index+1}</div>
            </div>
        </c:forEach>


    </div>
    <c:if test="${maxPage>1}">
        <input type="hidden" id="curPage" value="${page}"/>
        <input type="hidden" id="maxPage" value="${maxPage}"/>

        <div class="loading" id="loading-btn"><a href="javascript:loadMore();"><i
                style="display:none"></i><b>加载更多</b></a></div>
    </c:if>
    <!-- footer -->
    <%@ include file="mobilegamefooter.jsp" %>

</div>

<!-- 吐槽提示按钮 -->
<div class="tc-place" id="tc-place">
    <div class="tc">
        <div class="tc-box">
            <div id="tc-btn">我要吐槽</div>
            <div id="tx-btn">分享按钮</div>
        </div>
    </div>
</div>

<!-- 吐槽输入框 -->
<div id="tc-layer" class="tc-layer">
    <div class="tc-form" id="tc-form">
        <div class="tc-ctrl">
            <span id="tc-cancel">取消</span>
            <span id="tc-submit">提交</span>
        </div>
        <div class="tc-form-box">
            <form id="tc-formtable">
                <input type="hidden" value="${gagContentId}" name="cid" id="cid">
                <textarea name="body" id="tc-textarea" placeholder="发表你对'${dto.linename}'的吐槽"></textarea>
                <input type="submit" value="" style="display:none">
            </form>
            <span id="char-num">70</span>
        </div>
    </div>
</div>

<!-- 分享提示框 -->
<div id="tc-share" class="tc-layer">
    <div class="share-to">
        <div class="h2">分享到</div>
        <div id="share-to-box">
            <!-- Baidu Button BEGIN -->
            <div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
                <a class="bds_qzone">QQ空间</a>
                <a class="bds_tsina">新浪微博</a>
                <a class="bds_tqq">腾讯微博</a>
                <a class="bds_tqf">腾讯朋友</a>
            </div>
            <script type="text/javascript" id="bdshare_js" data="type=tools&mini=1"></script>
            <script type="text/javascript" id="bdshell_js"></script>
            <script type="text/javascript">
                try {
                    var bds_config = {
                        'bdDes': '${dto.linedesc}（分享自 @着迷网）',//您的自定义分享摘要
                        'bdText': '集体吐槽#${dto.linename}#（分享自 @着迷网）',//您的自定义分享内容
                        'bdPopTitle': '${dto.linename}',//您的自定义pop窗口标题
                        'bdPic': '${dto.indexpic}',
                        'snsKey': {'qzone': '68ace50c15654ff38a0c850625c4c1f7', 'tsina': '1245341962', 'tqq': '100292513'}
                    };
                    document.getElementById('bdshell_js').src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000);
                } catch (e) {
                }
            </script>
            <!-- Baidu Button END -->
        </div>
        <div id="close-share">取消</div>
    </div>
</div>

<!-- 返回顶部 -->
<div id="returntop" onclick="returnTop();" style="bottom:5rem">返回顶部</div>

<script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
<script>
    // 首页吐槽Json
    var tcMsg = [
        <c:forEach items="${dto.gaglist}" var="dtov" varStatus="st">
        {
            'header': '${dtov.header}',
            'msg': '${dtov.msg}'
        }<c:if test="${!st.last}">,
        </c:if>
        </c:forEach>
    ]
    sytc(); // 吐槽弹出层
    fx(); // 分享弹出层
    tc_input() // 限定吐槽字数

    <c:if test="${dto.gaglist.size()>0}">
    tc_print(tcMsg) // 显示弹幕
    </c:if>

    function loadMore() {
            var appStr = "";
            var nextPage = parseInt($("#curPage").val()) + 1;
            $.ajax({
                url: '${URL_WWW}/mobilegame/indexloadmore/${lineid}',
                type: 'post',
                async: false,
                data: {'pnum': nextPage},
                dataType: "json",
                success: function (data) {
                    if (data.rs == 1) {
                        for (var i = 0; i < data.result.gamedtolist.length; i++) {
                            var gameDto = data.result.gamedtolist[i];
                            var str = '<div class="list">';
                            str = str + '<a class="game-info" href="${URL_WWW}/mobilegame/comment/' + gameDto.gamedbid + '?lineid=${lineid}&contentid=' + gameDto.contentid + '">';
                            str = str + '<img src="' + gameDto.gameicon + '">';
                            str = str + '<div>';
                            str = str + '<div class="h2">' + gameDto.gamename + '</div>';
                            str = str + '<p>';
                            str = str + '<code>' + gameDto.replynum + '评</code>';
                            str = str + '<span class="score-1"><i style="width:' + gameDto.average_score_per + '%"></i></span>';
                            str = str + '<em>' + gameDto.average_score + '</em>';
                            str = str + '</p>';
                            str = str + '</div>';
                            str = str + '</a>';
                            //显示2条短评
                            for (var j = 0; j < gameDto.shortcommentlist.length; j++) {
                                var shortDto = gameDto.shortcommentlist[j];
                                str = str + '<div class="user-dp">';
                                str = str + '<img src="' + shortDto.header + '">';
                                str = str + '<div>' + shortDto.msg + '</div>';
                                str = str + '</div>';
                            }
                            var index = (parseInt(data.result.curPage) - 1) * 10 + (i + 1);
                            str = str + '<div class="list-num">' + index + '</div>';
                            str = str + '</div>';
                            appStr = appStr + str;
                        }
                        if (data.result.curPage >= data.result.maxPage) {
                            $("#loading-btn").css("display", "none");
                        } else {
                            $("#loading-btn").css("display", "block");

                            $("#curPage").val(data.result.curPage);
                        }

                    } else {
                        alert("加载失败，请重新加载。")
                    }
                }
            });

            if (appStr != "") {
                $("#listbox").append(appStr);
            }
        }

    function sytc() {
        var a = document.getElementById('tc-place');
        var b = document.getElementById('tc-form');
        var c = document.getElementById('tc-btn');
        var d = document.getElementById('tc-textarea');
        var e = document.getElementById('tc-cancel');
        var f = document.getElementById('tc-submit');
        var g = document.getElementById('tc-layer');
        var h = document.getElementById('body-wrapper');

        touch.on(c, 'tap', function (ev) {
            a.style.display = 'none';
            b.style.display = 'block';
            h.style.display = 'none';
            g.style.display = 'block';
            d.setAttribute('autofocus', 'autofocus');
            try {
                document.getElementById('tc-print').style.display = 'none';
            } catch (e) {
            }
        });

        touch.on(e, 'tap', function (ev) {
            a.style.display = 'block';
            h.style.display = 'block';
            b.style.display = 'none';
            g.style.display = 'none';
            d.blur();
            try {
                document.getElementById('tc-print').style.display = 'block';
            } catch (e) {
            }
        });

        touch.on(f, 'tap', function (ev) {
            //document.getElementById('tc-formtable').submit();
            //发送吐槽内容
            var result = false;
            var body = $("#tc-textarea").val();
            var cid = $("#cid").val();
            if ($.trim(body) == "" || body == "发表你对'${dto.linename}'的吐槽") {
                alert("吐槽内容不能为空");
                return false;
            }
            $.ajax({
                url: '${URL_WWW}/json/comment/mobile/post',
                type: 'post',
                async: false,
                data: {'cid': '${gagContentId}', 'body': body, 'type': '4'},
                dataType: "json",
                success: function (data) {
                    if (data.rs == 11014) {
                        alert("时间间隔太短了，请稍后评论！");
                        $("#tc-textarea").focus();
                    } else if (data.rs == -305) {
                        alert("评论中含有不适当的内容！");
                        $("#tc-textarea").focus();
                    } else {
                        $("#tc-textarea").val("");
                        alert("吐槽成功！");
                        result = true;
                        var gagNum = $("#gagnum").html();
                        $("#gagnum").html(parseInt(gagNum) + 1);
                        reloadtc_print(data.result.header, data.result.msg);
                    }
                }
            });
            if (result) {
                h.style.display = 'block';
                b.style.display = 'none';
                a.style.display = 'block';
                g.style.display = 'none';
            }
            try {
                document.getElementById('tc-print').style.display = 'block';
            } catch (e) {
            }
        });
    }
</script>
</body>
</html>