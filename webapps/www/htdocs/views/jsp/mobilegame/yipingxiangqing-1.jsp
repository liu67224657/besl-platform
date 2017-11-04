<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>${gameDB.gameName}好玩吗_着迷网Joyme.com</title>
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

    <div class="game-tag">
        <section>
            <h1 class="h1">${gameDB.gameName}</h1>
        </section>

        <div class="tag-and-pic">
            <div>
                <c:if test="${not empty gameDB.gameDBDevicesSet}">
                    <for:forEach items="${gameDB.gameDBDevicesSet}" var="device">
                        <c:if test="${device.gameDbDeviceId==1&&device.gameDbDeviceStatus==true}">
                            <a href="javascript:void(0);">IOS</a>
                        </c:if>
                        <c:if test="${device.gameDbDeviceId==3&&device.gameDbDeviceStatus==true}">
                            <a href="javascript:void(0);">Android</a>
                        </c:if>
                    </for:forEach>
                    <br/>
                </c:if>

                <for:forEach items="${gameDB.gameDBCategorySet}" var="category" varStatus="st">
                    <c:if test="${category.gameDbCategoryStatus==true}">

                        <c:forEach items="${map}" var="map" varStatus="c">
                            <c:if test="${map.key==category.gameDbCategoryId}">
                                <a href="${URL_WWW}/mobilegame/category?categoryId=${map.key}&categoryName=${map.value}">${map.value}</a>
                            </c:if>
                        </c:forEach>
                        <%--<c:if test="${(st.index+1)%2==0}"><br/></c:if>--%>
                    </c:if>

                </for:forEach>
                <br/>
                <c:if test="${not empty gameDB.gamePublishers}">
                    <a href="${URL_WWW}/mobilegame/publishersinfo?publishers=${gameDB.gamePublishers}&lineid=${lineid}&type=1">厂商：${gameDB.gamePublishers}&gt;</a>
                </c:if>
                <c:if test="${not empty gameDB.gameDeveloper}">
                    <a href="${URL_WWW}/mobilegame/publishersinfo?publishers=${gameDB.gameDeveloper}&lineid=${lineid}&type=2">开发商：${gameDB.gameDeveloper}&gt;</a>
                </c:if>

                <a href="${URL_WWW}/mobilegame/info/gameprofile/${gameDB.gameDbId}?contentid=${contentId}">游戏简介&gt;</a>
                <br/>
                <a href="${URL_WWW}/mobilegame/info/lookpic/${gameDB.gameDbId}?contentid=${contentId}">画面截图&gt;</a>

                <c:if test="${not empty gameDB.gameSize}">
                    <a href="javascript:void(0);">游戏大小：${gameDB.gameSize}</a>
                </c:if>
            </div>
            <img src="${gameDB.gameIcon}">
        </div>

        <div class="fx-btn">
            <a href="javascript:fq();" id="tx-btn">分享</a>
            <a href="${URL_WWW}/mobilegame/download_info?gameid=${gameDB.gameDbId}">下载</a>
        </div>
        <a href="${URL_WWW}/mobilegame/score?contentid=${contentId}&gameName=${gameDB.gameName}&lineid=${lineid}&gameid=${gameDB.gameDbId}"
           class="pf-btn">点击评分</a>

        <!-- 已评分 -->
        <a class="mypoint"
           href="${URL_WWW}/mobilegame/score?contentid=${contentId}&gameName=${gameDB.gameName}&lineid=${lineid}&gameid=${gameDB.gameDbId}">

            <%--<div>--%>
            <strong>我的观点</strong>
            <span class="score-1"><i style="width:0%" id="myscore"></i></span>

            <div id="myprint">欢迎发表评论</div>
            <%--</div>--%>
            <%--<div>章鱼开局，锁定大钱袋开打，城堡附近一定要开到绿瓶子火瓶子，手电，飞机也行，锁</div>--%>
            <%--<div>--%>
            <%--<strong>我的观点</strong>--%>
            <%--<span class="score-1"><i style="width:50%"></i></span>--%>
            <%--</div>--%>

        </a>
    </div>
    <!-- 总评分 -->
    <c:choose>

        <c:when test="${gameDTO.replynum>=10}">
            <div class="show-score">
                <div><span class="score-2"><i
                        style="width:${gameDTO.average_score/10*100}%"></i></span><em>${gameDTO.average_score}</em></div>
                <div>${gameDTO.replynum}评分</div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="show-score">
                <div><span class="score-2" ><i style="width:0;"></i></span><em></em></div>
                <div>尚未收到足够评分</div>
            </div>
        </c:otherwise>
    </c:choose>
    <c:if test="">

    </c:if>


    <!-- 短评 -->
    <div class="pl-type">
        <a href="${URL_WWW}/mobilegame/shortcomments?contentid=${contentId}&gameId=${gameDB.gameDbId}&lineid=${lineid}"><strong>短评</strong>${page.totalRows}<span>全部&gt;&gt;</span></a>
    </div>

    <div style="height:12px"></div>

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
                <div onclick="like('${dtov.replyId}','1','${URL_WWW}')" id="like${dtov.replyId}">${dtov.agreeNum}</div>
                <!-- 赞同 -->
                <div onclick="like('${dtov.replyId}','2','${URL_WWW}')"
                     id="dislike${dtov.replyId}">${dtov.disagreeNum}</div>
                <!-- 反对 -->
            </div>
        </div>

    </div>
    </c:forEach>


</div>

<!-- 长评 -->
<div class="pl-type pl-type2">
    <a href="${URL_WWW}/mobilegame/gamemobilearticle?gameId=${gameDB.gameDbId}&gamename=${gameDB.gameName}"><strong>长评</strong>${articlenum}<span>&gt;&gt;</span></a>
</div>

<!-- 排行榜 -->
<div class="pl-type" style="background:#fff">
    <c:choose>
        <c:when test="${not empty list}">
            <a href="${URL_WWW}/mobilegame/topview?gameid=${gameDB.gameDbId}&lineid=${lineid}&contentid=${contentId}"><strong>排行榜</strong>${list.size()}<span>&gt;&gt;</span></a>
        </c:when>
        <c:otherwise>
            <a href="javascript:void(0);"><strong>排行榜</strong>0<span>&gt;&gt;</span></a>

        </c:otherwise>
    </c:choose>
</div>

<!-- 返回顶部 -->
<div id="returntop" onclick="returnTop();">返回顶部</div>

<!-- footer -->
<%@ include file="mobilegamefooter.jsp" %>

</div>
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
                        'bdText': '#${gameDB.gameName}#（分享自 @着迷网）',//您的自定义分享内容
                        'bdPopTitle': '${gameDB.gameName}',//您的自定义pop窗口标题
                        'bdPic': '${gameDB.gameIcon}',
                        'snsKey': {'qzone': '', 'tsina': '1245341962', 'tqq': '801517256', 'tqf': ''}
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

<script type="text/javascript" src="${URL_LIB}/static/js/page/touch-0.2.14.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/page/action.js"></script>

<script>
    $(document).ready(function () {
        var msg = $.cookie("msg${gameDB.gameDbId}");
        var score = $.cookie("scope${gameDB.gameDbId}");
        if (msg != '' && msg != null || score != '' && score != null) {
            $(".pf-btn").css("display", "none");
            $("#myprint").html(msg);
        }

        if (score != '' && score != null) {
            $("#myscore").css("width", score + "0%");
        }
        if (msg == null || msg == '') {
            $("#myprint").html("尚未发表评论");
        }
        if (msg == null && score == null) {
            $(".mypoint").css("display", "none");
        }

    });
    function fq() {
        var b = document.getElementById('tx-btn');
        var c = document.getElementById('close-share');
        var d = document.getElementById('tc-share');
        var e = document.getElementById('body-wrapper');
        touch.on(b, 'tap', function () {
            e.style.display = 'none';
            d.style.display = 'block';
            try {
                document.getElementById('tc-print').style.display = 'none';
            } catch (e) {
            }
        });

        touch.on(c, 'tap', function () {
            e.style.display = 'block';
            d.style.display = 'none';
            try {
                document.getElementById('tc-print').style.display = 'block';
            } catch (e) {
            }
        });
    }


</script>
</body>
</html>