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
    <title>游戏标签</title>
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

<!-- topbar -->
<div class="topbar imgbox">
    <a class="return-btn" href="javascript:history.go(-1)"></a>

    <p class="t">${categoryName}</p>
    <img src="${URL_LIB}/static/theme/default/images/mobilegame/topbar.png">
</div>

<div id="listbox" class="listbox2">
    <!-- 列表 -->
    <c:forEach items="${dto.gamedtolist}" var="dtov">
        <div class="list">
            <!-- 游戏 -->
            <a class="game-info"
               href="${URL_WWW}/mobilegame/comment/${dtov.gamedbid}?lineid=${dtov.lineid}&contentid=${dtov.contentid}">
                <img src="${dtov.gameicon}">

                <div>
                    <div class="h2">${dtov.gamename}</div>
                    <p>
                        <code>${dtov.replynum}评</code>
                        <span class="score-1"><i style="width:${dtov.average_score/10*100}%"></i></span>
                        <em>${dtov.average_score}</em>
                    </p>
                </div>
            </a>

            <c:forEach items="${dtov.shortcommentlist}" var="dtov2">
                <div class="user-dp">
                    <img src="${dtov2.header}">

                    <div>${dtov2.msg}</div>
                </div>
            </c:forEach>
            <!-- 用户的点评 -->
                <%--<div class="user-dp">--%>
                <%--<img src="${URL_LIB}/static/theme/default/images/mobilegame/user.jpg">--%>
                <%--<div>这游戏的任务很坑爹，冲刺一次不是只能用一个吗？还有道具全用过了那任务还是没满格〜现在分数663W 感觉已经瓶颈了。</div>--%>
                <%--</div>--%>
        </div>
    </c:forEach>
</div>

<div class="loading" id="categoryLoadding"><a href="javascript:;"
                                              <c:if test="${page.maxPage<2}">style="display:none;"</c:if>><i
        style="display:none"></i><b>加载更多</b></a>
    <input type="hidden" value="${page.curPage}" id="curpage"/>
    <input type="hidden" value="${page.maxPage}" id="maxpage"/>
    <input type="hidden" id="categoryId" value="${categoryId}"/>

</div>

<!-- footer -->
<%@ include file="mobilegamefooter.jsp" %>

<!-- 返回顶部 -->
<div id="returntop" onclick="returnTop();">返回顶部</div>

<script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>
<script type="text/javascript">
    (function () {
        var loading = document.getElementById('categoryLoadding'),
                icon = loading.getElementsByTagName('i')[0],
                txt = loading.getElementsByTagName('b')[0];
        loading.onclick = function () {
            icon.style.display = 'inline-block';
            txt.innerHTML = '加载中';
        }

    })();
</script>
</body>
</html>