<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${title}</title>

    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        $(function () {
            var tabflag = '${retype}';
            if (tabflag == 'midou') {
                $('.tab-tit').find('a').eq(0).removeClass("cur");
                $('.tab-tit').find('a').eq(1).addClass("cur");
                $('.giftwrapper').removeClass('cur-box');
                $('.giftwrappers').addClass('cur-box');
            }

        });
    </script>
</head>
<body>
<div id="wrapper">
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>精选礼包</h1>
         <a href="#" class="close">关闭</a>
     </header> -->
    <div class="tab-tit">
        <a href="javascript:void(0);" class="cur fl">礼包</a><a href="javascript:void(0);" class="fr">宝贝</a>
    </div>
    <div class="tab-main">
        <div class="lb-box cur-box giftwrapper">
            <p class="lb-record"> 最近一周领取记录<b>
                <c:choose>
                    <c:when test="${not empty giftdto.page.totalRows}">
                        ${giftdto.page.totalRows}
                    </c:when>
                    <c:otherwise>
                        0
                    </c:otherwise>

                </c:choose></b></p>


            <div class="lb-main">
                <c:choose>
                    <c:when test="${not empty giftdto.rows}">
                        <c:forEach items="${giftdto.rows}" var="dto">
                            <dl>
                                <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/mygiftdetail?appkey=${appkey}&profileid=${profileid}&aid=${dto.aid}&uno=${uno}&lid=${dto.lid}&type=1">
                                    <dt class="fl">
                                    <p><img src="${dto.gipic}" alt=""></p></dt>
                                    <dd class="fl">
                                        <div class="fl">
                                            <h1 class="cut_out2">${dto.title}</h1>

                                            <p>有效期：<span>${dto.endTime}</span></p>
                                        </div>
                                        <span class="min_btn ch">查</span>

                                    </dd>
                                </a>
                            </dl>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="lb-main-none" style="display:block">
                            <p><img src="${URL_LIB}/static/theme/default/images/my/libao.png" alt=""></p>

                            <p>还没有您的领取记录～</p>
                        </div>
                    </c:otherwise>
                </c:choose>


            </div>
            <%--<c:if test="${giftdto.page.maxPage>1}">--%>
            <%--<div class="loading" id="syhbmygiftload"><a href="javascript:;"><i--%>
            <%--style="display:none"></i><b>点击加载更多</b></a></div>--%>
            <%--</c:if>--%>
            <input type="hidden" value="${giftdto.page.curPage}" id="giftWapCurPage"/>
            <input type="hidden" value="${giftdto.page.maxPage}" id="giftWapMaxPage"/>
        </div>


        <div class="baby-box giftwrappers">
            <c:choose>
                <c:when test="${not empty midoudto.rows}">
                    <c:forEach items="${midoudto.rows}" var="dto">
                        <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/mygiftdetail?appkey=${appkey}&profileid=${profileid}&aid=${dto.gid}&type=2&consumeorder=${dto.consumeOrder}">
                            <div>
                                <h1><p class="fl">订单号：<span>${dto.consumeOrder}</span></p>

                                    <p class="fr">${dto.exchangeTime}</p></h1>
                                <div class="baby-box-li">
                                        <span class="fl">
                                            <img src="${dto.gipic}">
                                        </span>

                                    <div class="baby-box-li-text fl">
                                        <h2 class="cut_out2">${dto.title}</h2>

                                        <p>数量：<span>1</span></p>

                                        <p class="color"><img src="${URL_LIB}/static/theme/wap/images/midou-min.png"
                                                              alt="">消耗<span>${dto.point}</span>迷豆</p>
                                    </div>
                                    <span class="fr"></span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="lb-main-none" style="display:block">
                        <p><img src="${URL_LIB}/static/theme/default/images/my/libao.png" alt=""></p>

                        <p>您还没有兑换过商品</p>
                    </div>
                </c:otherwise>
            </c:choose>
            <%--<c:if test="${midoudto.page.maxPage>1}">--%>
            <%--<div class="loading" id="mymidouload"><a href="javascript:;"><i--%>
            <%--style="display:none"></i><b>点击加载更多</b></a></div>--%>
            <%--</c:if>--%>
            <input type="hidden" value="${midoudto.page.curPage}" id="midouWapCurPage"/>
            <input type="hidden" value="${midoudto.page.maxPage}" id="midouWapMaxPage"/>
            <!-- <div class="loading" id="loading-btn"><a href="javascript:;"><i style="display:none"></i><b>加载更多</b></a></div> -->

        </div>


    </div>
</div>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        <%--window.history.replaceState(null, null, 'list?uno=${uno}&appkey=${appkey}');--%>
        <%--window.history.replaceState(null, null, 'mygift?uno=${uno}&appkey=${appkey}');--%>
        $('.tab-tit').on('touchstart', 'a', function () {
            var index = $(this).index();
            $(this).addClass('cur').siblings().removeClass('cur');
            $('.tab-main>div').eq(index).addClass('cur-box').siblings().removeClass('cur-box')
        });
//        window.onpopstate = function (e) {
//            $("#midouWapCurPage").val("1");
//            $("#giftWapCurPage").val("1");
//        };
//        var loading = document.getElementById('syhbmygiftload');
//        loading.onclick = function () {
//            loading.getElementsByTagName('i')[0].style.display = 'inline-block';
//            loading.getElementsByTagName('b')[0].innerHTML = '加载中';
//        }
//
//        var loading2 = document.getElementById('mymidouload'),
//                icon = loading2.getElementsByTagName('i')[0],
//                txt = loading2.getElementsByTagName('b')[0];
//        loading2.onclick = function () {
//            icon.style.display = 'inline-block';
//            txt.innerHTML = '加载中';
//        }
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 222]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);

    })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=222" style="border:0;" alt=""/></p></noscript>


<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${uno}" name="uno"/>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
</body>
</html>
