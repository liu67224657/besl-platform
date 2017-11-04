<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/lottery.css?${version}" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <title>着迷 </title>
    <script type="text/javascript" src="http://lib.joyme.com/static/js/common/google-statistics-noseajs.js"></script>
</head>
<body>
<div id="get-gift-user">
    <ul class="ranklist">
        <li>
            <c:forEach var="userLog" items="${userLogList}" varStatus="st">
            <span class="sp1"> ${userLog.screenName}</span>
            <span class="sp2">成功抽到</span>
            <span class="sp3">${userLog.lotteryAwardName}</span>
            <span class="sp4"><fmt:formatDate value="${userLog.lotteryDate}" pattern="HH:mm"/></span>
            <c:if test="${st.index%2==1}">
        </li>
        <li>
            </c:if>
            </c:forEach>
        </li>
    </ul>
</div>
</body>
<script type="text/javascript">
    (function($) {
        $.fn.myScroll = function(options) {
            //默认配置
            var defaults = {
                speed:40,  //滚动速度,值越大速度越慢
                rowHeight:3 //每行的高度
            };

            var opts = $.extend({}, defaults, options),intId = [];

            function marquee(obj, step) {

                obj.find("ul").animate({
                            marginTop: '-=1'
                        }, 0, function() {
                    var s = Math.abs(parseInt($(this).css("margin-top")));
                    if (s >= step) {
                        $(this).find("li").slice(0, 1).appendTo($(this));
                        $(this).css("margin-top", 0);
                    }
                });
            }

            this.each(function(i) {
                var sh = opts["rowHeight"],speed = opts["speed"],_this = $(this);
                intId[i] = setInterval(function() {
//                    if(_this.find("ul").height()<=_this.height()){
//                        clearInterval(intId[i]);
//                    }else{
                    marquee(_this, sh);
//                    }
                }, speed);

                _this.hover(function() {
                    clearInterval(intId[i]);
                }, function() {
                    intId[i] = setInterval(function() {
//                        if(_this.find("ul").height()<=_this.height()){
//                            clearInterval(intId[i]);
//                        }else{
                        marquee(_this, sh);
//                        }
                    }, speed);
                });

            });

        }

    })(jQuery);
    $(function() {
        $("#get-gift-user").myScroll({
                    speed:40,
                    rowHeight:24
                });

    });

</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</html>