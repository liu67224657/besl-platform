<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="游戏礼包,游戏周边,游戏兑换码,游戏手办">
    <meta name="description"
          content="着迷网双十一活动为广泛游戏玩家提供各种游戏礼包,激活码,特权码,测试礼包,新手礼包,兑换码等免费领取,还有多种游戏周边礼品等您拿，双十一给力游戏礼包就在着迷网."/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
    <title>着迷网双十一_游戏礼包_游戏周边_兑换码_手办_玩偶_着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/event/shuang11/css/shuang11.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="parts-1">
    <div class="box">
        <span class="sp-1"></span>
        <span class="sp-2"></span>
        <span class="sp-3"></span>
        <span class="sp-4"></span>
        <span class="sp-5"></span>
        <span class="sp-6"></span>
        <c:choose>
            <c:when test="${count!=null}">
                <c:forEach items="${count}" var="dto" varStatus="st">
                    <em class="txt-${st.index+1}">${dto}</em>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <em class="txt-1">0</em>
                <em class="txt-2">0</em>
                <em class="txt-3">0</em>
                <em class="txt-4">0</em>
                <em class="txt-5">0</em>
            </c:otherwise>
        </c:choose>


    </div>
</div>
<!-- 新增加 -->
<div class="parts-7"></div>
<!-- 合作伙伴 -->
<div class="parts-2"></div>

<!-- 游戏礼包 -->
<div class="parts-3" id="gamegift">
    <div class="box clearfix">

        <h2><a href="${URL_WWW}/gift">更多游戏礼包</a></h2>
        <ul class="clearfix">
            <c:choose>
                <c:when test="${giftPage!=null}">
                    <c:forEach items="${giftPage}" var="dto" varStatus="st">
                        <li class="color-${st.index+1}">
                            <a href="${URL_WWW}/gift/${dto.gid}">
                                <img class="img-1"
                                     src="${URL_LIB}/static/event/shuang11/images/game-pic-${st.index+1}.jpg"
                                     width="192"
                                     height="236"/>
                                <img class="img-2" src="${dto.gipic}" width="102"
                                     height="102"/>
                                <span class="sp-1">${dto.title}</span>
                                <span class="sp-2">${dto.rn}人领取&nbsp;&nbsp;剩余${dto.sn}</span>
                                         <span class="sp-3">
                                             <c:choose>
                                                 <c:when test="${dto.sn>0}">
                                                     领号
                                                 </c:when>
                                                 <c:otherwise>
                                                     淘号
                                                 </c:otherwise>
                                             </c:choose>

                                         </span>
                                <span class="sp-4"></span>
                            </a>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li>暂时没有礼包</li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</div>

<div class="parts-4"><a target="_blank" href="http://www.joyme.com/click/xxzfq6-xxzfol-xxzfqv"></a></div>

<!-- 积分商品 -->
<div class="parts-5" id="pointGift">
    <div class="box clearfix">
        <h2><a href="${URL_WWW}/coin">更多积分商品</a></h2>
        <ul class="clearfix">
            <c:forEach items="${pointPage}" var="dto" varStatus="st">
                <li class="color-${st.index+1}">
                    <a href="${URL_WWW}/coin/${dto.gid}">
                        <c:choose>
                            <c:when test="${dto.shuang11Pic!=null}">
                                <img src=" ${uf:parseOrgImg(dto.shuang11Pic) }" width="190" height="190"/>
                            </c:when>
                            <c:otherwise>
                                <img src="${dto.gipic}" width="190" height="190"/>
                            </c:otherwise>
                        </c:choose>
                        <span class="sp-1">${dto.title}</span>
                        <span class="sp-2">已有${dto.cn-dto.sn}人领取</span>
                        <span class="sp-3"><del>兑换价： ${dto.shuang11Point}积分</del></span>
                        <span class="sp-4">双11价：${dto.point}积分</span>
                        <span class="sp-5">立即兑换</span>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<div class="parts-6"></div>
<!-- 页脚 -->
<div class="footercon clearfix">
    <div class="footer">
        <%@ include file="/hotdeploy/views/jsp/tiles/all-rights-reserved.jsp" %>
        <span> 京ICP备11029291号</span>
        <a rel="nofollow" target="_blank" href="http://www.joyme.com/help/aboutus">关于着迷</a> |
        <a rel="nofollow" target="_blank" href="http://www.joyme.com/about/job/zhaopin">工作在着迷</a> |
        <a rel="nofollow" target="_blank" href="http://www.joyme.com/about/contactus">商务合作</a>|
        <a target="_blank" href="http://www.joyme.com/sitemap.htm">网站地图</a>
    </div>
</div>

<!-- 返回顶部 -->
<div id="returnTo">
    <a href="javascript:void(0)" id="returnGift" class="links-1"><em>礼包领取</em><span></span><i></i></a>
    <a href="javascript:void(0)" id="returnPoint" class="links-2"><em>积分兑换</em><span></span><i></i></a>
    <a href="javascript:void(0)" id="returnTop" class="links-3"><em>返回顶部</em><span></span><i></i></a>
</div>

<script type="text/javascript">

    document.getElementById('returnTop').onclick = function () {
        document.documentElement.scrollTop = 0;
        document.body.scrollTop = 0;
        document.getElementById('returntop').style.display = 'none';
    }
    document.getElementById('returnGift').onclick = function() {
        document.documentElement.scrollTop = document.getElementById('gamegift').offsetTop;
        document.body.scrollTop = document.getElementById('gamegift').offsetTop;
    }
    document.getElementById('returnPoint').onclick = function() {
        document.documentElement.scrollTop = document.getElementById('pointGift').offsetTop;
        document.body.scrollTop = document.getElementById('pointGift').offsetTop;
    }
</script>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/giftmarket-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>