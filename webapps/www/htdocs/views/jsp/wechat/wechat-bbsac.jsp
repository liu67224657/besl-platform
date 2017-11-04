<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta content="着迷网活动中心，游戏礼包，兑换码，实物奖品天天送！" name="description">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title></title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
    <style type="text/css">
            *{margin: 0; padding:0;}
            #wrapper{/*margin: 0 15px; padding: 15px 0;*/ padding-bottom: 10px; overflow: hidden;}
            #pic-loop{overflow: hidden; position: relative;}
            #pic-loop-box{display: -webkit-box; display: -ms-box; display: -moz-box; -webkit-backface-visibility}
            #pic-loop-box img{width: 100%; display: block;}
            .pagination{position: absolute; width: 100%; left: 0; bottom: 4px; font-size: 0; text-align: right;}
            .pagination span{display: inline-block; width: 8px; height: 8px; -webkit-border-radius: 8px; -ms-border-radius: 8px; -moz-border-radius: 8px; border-radius: 8px; background: #99906c; margin-right: 4px;}
            .pagination span.swiper-active-switch{background: #fff;}
            .pagination span:last-child{margin-right: 15px;}
            .item{margin: 10px 10px 0;}
            .item a{display: block; position: relative;}
            .item a img{display: block; width: 100%;}
            .item a p{display: block; position: absolute; width: 100%; left: 0; bottom: 0; font-size: 14px; line-height: 18px; color: #fff; padding: 4px 10px; -webkit-box-sizing: border-box; -ms-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box; background: rgba(0,0,0,0.7);}
            .item a span{display: block; width: 40px; height: 19px; font-size: 12px; line-height: 19px; text-align: center; color: #fff; position: absolute; left: 0; top: 10px; background: url(${URL_LIB}/static/theme/default/images/wechat/adorn.png) no-repeat; background-size: 40px 40px;}
            .item a span.remen{background-position: 0 0}
            .item a span.zuixin{background-position: 0 -21px}
            .item section{border: 1px solid #d8d8d8; padding: 6px 0}
            .item section p{display:block; color: #333; font-size: 12px; line-height: 16px; margin: 0 10px;}
            .item section p span{color: #ff4e00}
        </style>
</head>
<body>
<div id="wrapper">
    <!-- 图片轮播 -->
    <article id="pic-loop">
        <div id="pic-loop-box" class="swiper-wrapper">
            <c:choose>
                <c:when test="${topList != null && topList.size() > 0}">
                    <c:forEach items="${topList}" var="topItem">
                        <div class="swiper-slide"><a href="${topItem.url}"><img src="${topItem.picUrl}"></a></div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="swiper-slide"><a href="#">暂时没有数据~</a></div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="pagination">
            <c:if test="${topList != null && topList.size() > 0}">
            <c:forEach items="${topList}" varStatus="st">
                <c:choose>
                    <c:when test="${st.index == 0}">
                        <span class="swiper-pagination-switch swiper-active-switch"></span>
                    </c:when>
                    <c:otherwise>
                        <span class="swiper-pagination-switch"></span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </c:if>
        </div>
    </article>
    <!-- 列表 -->

    <c:forEach items="${itemList}" var="item">
        <article class="item">
            <a href="${item.url}">
                <img src="${item.picUrl}">
                <p>${item.title}</p>
                <c:choose>
                    <c:when test="${item.param.isHot}"><span class="remen">热</span></c:when>
                    <c:when test="${item.param.isNew}"><span class="zuixin">新</span></c:when>
                </c:choose>
                </a>
                <section>
                    <p><span>活动时间：</span>${item.param.startDate} 至 ${item.param.endDate}</p>

                    <p><span>活动形式：</span>${fn:substring(item.desc,0,50)}${fn:length(item.desc)>50?'...':''}</p>
                </section>
            </article>
    </c:forEach>
</div>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="${URL_LIB}/static/js/init/wechat-bbsac-swiper.js"></script>
<script>
    if ($('.swiper-slide').length > 1) {
        var mySwiper = new Swiper('#pic-loop', {
            loop: true,
            pagination: '.pagination',
            paginationClickable: false,
            mode: 'horizontal',  //水平
            cssWidthAndHeight: true
        });
        timer = setInterval(function () {
            mySwiper.swipeNext()
        }, 3000)
    }
</script>
</body>
</html>