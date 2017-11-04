<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/youkutaglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<c:set var="url" value="/youku/webview/giftmarket/detail?aid=${activityDetailDTO.aid}"/>
<html>
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>${activityDetailDTO.title}</title>

    <!--手机style-->
    <link rel="stylesheet" media="screen and (max-width:768px)" href="${URL_LIB}/static/theme/youku/css/club.css">
    <!--平板style-->
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_common.css">
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_club.css">

    <script type="text/javascript" src="${URL_LIB}/static/js/page/geo.js"></script>

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body onload="setup();preselect('省份');">
<div id="wrapper" class="w-bg-color">
    <!--padding_box -->
    <div class="padding_box">
        <!--图片轮播-->
        <div id="pic-loop">
            <div class="pic-loop-box swiper-wrapper">
                <div class="swiper-slide"><img src="${activityDetailDTO.bgPic}"></div>
            </div>
        </div>
        <!--图片轮播-->
        <div class="sp-details">
            <div class="sp-details-top clearfix">
                <div class="sp-details-top-l fl">
                    <p class="Seckill">
                    <span>${activityDetailDTO.title}
                    <c:if test="${activityDetailDTO.seckillType==1}">
                        <b style="background:#f30;">秒杀</b>
                    </c:if>
                    </span>
                    </p>

                    <p>剩余：
                        <c:choose>
                            <c:when test="${goodsSecKill != null}">
                                <span><em class="color">${goodsSecKill.restsum}</em>/<cite>${goodsSecKill.totals}</cite></span>
                            </c:when>
                            <c:otherwise>
                                <span><em
                                        class="color">${activityDetailDTO.sn}</em>/<cite>${activityDetailDTO.cn}</cite></span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
            <div class="sp-details-bottom clearfix">
                <p>需酷豆：<span class="color">${activityDetailDTO.point}</span></p>
            </div>
        </div>
        <div class="sp-details-main exclusive clearfix">
            <h1 class="no-xian"><span>商品详情</span></h1>

            <c:if test="${not empty activityDetailDTO.textJsonItemsList}">
                <c:forEach items="${activityDetailDTO.textJsonItemsList}" var="dto">
                    <c:choose>
                        <c:when test="${dto.type=='1'}">
                            <p>${dto.item}</p>
                        </c:when>
                        <c:otherwise>
                            <p><img src="${dto.item}"/></p>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>

            <h1 class="appTo"><span>提示</span></h1>

            <p class="appTo">新疆、西藏、青海、港澳台以及海外不在邮寄范围内，如有疑问请联系我们</p>


            <c:choose>
                <c:when test="${not empty giftDto.rows}">
                    <h1 class="dj-title"><span>大家都在换</span></h1>

                    <div class="page-box1">
                        <c:forEach items="${giftDto.rows}" var="dto" varStatus="index">
                            <c:choose>
                                <c:when test="${index.index==0}">
                                    <div class="box1left">
                                        <a href="/youku/webview/giftmarket/detail?aid=${dto.gid}"><img
                                                src="${dto.gipic}" alt=""></a>
                                        <a href="/youku/webview/giftmarket/detail?aid=${dto.gid}">${dto.title}</a>
                                        <a href="/youku/webview/giftmarket/detail?aid=${dto.gid}"><span>${dto.desc}</span>
                                        </a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="box1right">
                                        <a href="/youku/webview/giftmarket/detail?aid=${dto.gid}"><img
                                                src="${dto.gipic}" alt=""></a>
                                        <a href="/youku/webview/giftmarket/detail?aid=${dto.gid}">${dto.title}</a>
                                        <a href="/youku/webview/giftmarket/detail?aid=${dto.gid}">
                                            <span>${dto.desc}</span> </a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <!--padding_box end-->
    <div class="details-btn">
        <c:choose>
            <c:when test="${goodsSecKill != null}">
                <c:choose>
                    <c:when test="${currentTime < goodsSecKill.startTime.getTime()}">
                        <a href="javascript:void(0);" class="details-butten greyBtn" id="exchange-btn" data-secid="${goodsSecKill.seckillId}">
                            <p id="p_sec_djs" data-start="${goodsSecKill.startTime.getTime()}">
                                <span id="span_djs_h"></span>:
                                <span id="span_djs_m"></span>:
                                <span id="span_djs_s"></span>
                                后开抢!
                            </p>
                        </a>
                    </c:when>
                    <c:when test="${currentTime >= goodsSecKill.endTime.getTime()}">
                        <a href="javascript:void(0);" class="details-butten greyBtn" data-secid="${goodsSecKill.seckillId}">已结束</a>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${goodsSecKill.restsum>0}">
                                <a href="${ykt:ykLogin(pageContext.request,url,'')}" class="details-butten bludBtn"
                                   id="exchange-btn" data-secid="${goodsSecKill.seckillId}">马上兑换</a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:void(0);" class="details-butten greyBtn" data-secid="${goodsSecKill.seckillId}">已售罄</a>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${activityDetailDTO.sn!=0}">
                        <c:choose>
                            <c:when test="${isBool}">
                                <a href="javascript:void(0);" class="details-butten greyBtn">商品已下架</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${ykt:ykLogin(pageContext.request,url,'')}" class="details-butten bludBtn"
                                   id="exchange-btn">马上兑换</a>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:void(0);" class="details-butten greyBtn">已售罄</a>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </div>
    <!--遮罩-->
    <div class="mark-box"></div>
    <div class="dialog opinion" id="cofirmshopdiv">
        <p>兑换此商品，共需要消耗<em>${activityDetailDTO.point}</em> 酷豆</p>

        <div class="dialog-btn">
            <a href="javascript:cancelAddress();" class="cancel">取消</a>
            <c:set var="url" value="/youku/webview/giftmarket/detail?aid=${activityDetailDTO.aid}"/>
            <a href="${ykt:ykLogin(pageContext.request,url,'')}" class="gain" id="getgoods">兑换</a>
        </div>
    </div>

    <div class="dialog" id="gopointwall">
        <h1>可用酷豆不足</h1>

        <p style="text-align:center">兑换此商品共需<em>${activityDetailDTO.point}</em>酷豆
            <br/>
            坚持每日签到可领取更多酷豆哦
            <br/>
            <a href="/youku/webview/task/signpage?platform=0" style="color: #54c2fb">点击这里去签到</a>
        </p>

        <div class="dialog-btn"><a href="javascript:void(0);" class="gain" onclick="cancelReserve();"
                                   style="border-left:none;">确定</a>

        </div>
    </div>


    <input type="hidden" value="${activityDetailDTO.aid}" name="aid"/>
    <input type="hidden" value="${profile.profileId}" name="profileId"/>

    <input type="hidden" value="${appkey}" name="appkey"/>
    <input type="hidden" value="${activityDetailDTO.gid}" name="gid"/>
    <input type="hidden" value="${activityDetailDTO.sn}" name="sn"/>
    <input type="hidden" value="${activityDetailDTO.point}" name="dtopoint"/>
    <input type="hidden" value="${userpoint}" name="userpoint"/>
    <input type="hidden" value="${allowExchangeStatus}" name="allowExchangeStatus"/>
    <input type="hidden" value="${exchangeStatus}" name="exchangeStatus"/>
    <input type="hidden" value="${goodstype}" name="goodstype"/>
    <input type="hidden" value="${endTime}" name="endTime"/>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/theme/youku/js/swiper.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/theme/youku/js/action.js"></script>

</div>
<div class="Threebox"><span></span></div>
<div id="exchange" class="pt-45" style="display:none;">
    <div class="lh-succeed lh-error">
        <span><img src="${URL_LIB}/static/theme/youku/images/ipad_succee.png" alt=""></span>

        <p>兑换成功</p>
    </div>
    <div class="win-message">
        <h3><em>卡号：</em>

            <p><span id="kahao"></span></p></h3>
        <h3 id="password" style="visibility: hidden;"><em>密码：</em>

            <p><span id="passwordspan"></span></p></h3>
    </div>
    <div class="done dn">
        <%--<p>已兑换的商品请在<span class="color">个人中心</span>我的宝贝页查看！</p>--%>
        <h4><a href="" id="a_to_use">如何使用</a><a href="/youku/webview/giftmarket/list">返回俱乐部</a></h4>
    </div>
</div>
<div class="new-address" style="display:none;">
    <ul>
        <li><input type="text" placeholder="收货人姓名" name="name"/></li>
        <li><input type="text" placeholder="手机号码" name="phone"/></li>
        <li><input type="text" placeholder="邮政编码" name="zipcode"/></li>
        <li style="width:100%;">
            <select class="select" name="province" id="s1">
                <option value=""></option>
            </select>

        </li>
        <li>
            <select class="select" name="city" id="s2">
                <option value=""></option>
            </select></li>
        <li>
            <select class="select" name="town" id="s3">
                <option value=""></option>
            </select></li>
        <li><input type="text" placeholder="详细地址" class="textarea" name="address"/></li>
    </ul>
    <div class="dialog-btn"
         style="text-align:center ;width: 100%;box-sizing: border-box;position: static;border-top:none;border-bottom: 1px solid #e5e5e5;">
        <a href="javascript:cancelAddress();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                      class="gain"
                                                                      id="submitAddress">提交</a>
    </div>
</div>
<script>
    $('#exchange-btn').on('touchstart', function () {

    });
    var wid = $(window).width() / 5;
    $('.Threebox').css('left', wid)

</script>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/youkuseajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/youkuwap-init.js');
</script>
<script type="text/javascript" src="http://static.joyme.com/app/youku/js/youkubtn.js"></script>
<script>
    var nowTime = new Date(${currentTime}).getTime();
    function timeOut1(start) {
        function zero(num) {
            if (num < 10) {
                return '0' + num;
            } else if (num >= 10) {
                return '' + num;
            }
        }

        var time = setInterval(function () {
            checkTime();
        }, 1000);

        function checkTime() {
            if (start < nowTime) {
                clearInterval(time);
                $('#exchange-btn').attr('href', '${ykt:ykLogin(pageContext.request,url,'')}');
                $('#exchange-btn').removeClass('greyBtn');
                $('#exchange-btn').addClass('bludBtn');
                $('#exchange-btn').html('马上兑换');
            } else {
                nowTime = nowTime + 1000;
                var mistiming = (start - nowTime) / 1000;
                var h = zero(parseInt((mistiming) / 3600));
                var m = zero(parseInt((mistiming % 86400 % 3600) / 60));
                var s = zero(parseInt(mistiming % 86400 % 360 % 60));
                $('#span_djs_h').text(h);
                $('#span_djs_m').text(m);
                $('#span_djs_s').text(s);
            }
        }
    }

    $(document).ready(function () {
        var dom = $('#p_sec_djs');
        if (dom != null && dom != undefined && dom.length > 0) {
            var startTimeLong = dom.attr('data-start');
            if (startTimeLong != null && startTimeLong != undefined && startTimeLong != '') {
                startTimeLong = Number(startTimeLong);
                timeOut1(startTimeLong);
            }
        }
    });
</script>
<script>
    seajs.use('${URL_LIB}/static/js/init/youkuwap-init.js');
</script>
<%@ include file="/views/jsp/youku_pwiki.jsp" %>
</body>
</html>