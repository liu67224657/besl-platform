<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>着迷精选</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_style.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/reload.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false;
            }, true);
        }, true);
    </script>
    <script type="text/javascript">

        var _paq = _paq || [];
        _paq.push(['trackPageView']);
        _paq.push(['enableLinkTracking']);
        (function () {
            window.history.replaceState(null, null, 'list?uno=${uno}&appkey=${appkey}&retype=${tabflag}');
            window.onpopstate = function (e) {
                $("#midouWapCurPage").val("1");
                $("#giftWapCurPage").val("1");
            };

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
        var tools = {
            tab: function () {
                var $tabspan = $('.tab-tit').find('a');
                $tabspan.each(function (i) {
                    $(this).on('click', function () {
                        $(this).addClass('cur').siblings('a').removeClass('cur');
                        $($('.loadBox')[i]).addClass('show').siblings('.loadBox').removeClass('show');
                    })
                });
            },
            loadImg: function () {
                var imgs = document.getElementsByClassName('shop-img');
                var len = imgs.length;
                var t = false;
                var timer = null;
            }
        }

        $(function () {
            tools.loadImg();
            tools.tab();
            var tabflag = '${tabflag}';
            if (tabflag == 'midou') {
                $('.tab-tit').find('a').eq(1).click();
            }

        });
    </script>
</head>
<body>
<div id="wrapper">

    <div class="tab-tit">
        <a href="javascript:void(0);" class="cur fl">礼包</a><a href="javascript:void(0);" class="fr">迷豆商城</a>
    </div>
    <div id="libao-wrap" class="loadBox show">
        <%--<div id="scroller">--%>
        <%--<div id="libao-pullDown" style="margin-top:36px;">--%>
        <%--<span class="pullDownIcon"></span>--%>
        <%--<span class="pullDownLabel">快使劲拉我O(∩_∩)O</span>--%>
        <%--</div>--%>
        <div id="thelist">
            <div class="tab-main">
                <div class="lb-box cur-box wrapper">
                    <div class="lb-main" id="div-Main">
                        <c:if test="${not empty hotDto}">
                            <div class="lb-main-classify lb-main-rm" id="hot-Main">
                                <p>热门</p>
                                <c:choose>
                                    <c:when test="${not empty hotDto.rows}">
                                        <c:forEach items="${hotDto.rows}" var="dto">
                                            <dl>
                                                <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=${appkey}&profileid=${profile.profileId}&aid=${dto.gid}&type=1">
                                                    <dt class="fl">
                                                        <c:choose>
                                                        <c:when test="${dto.reserveType==1}">
                                                        <cite class="yyz">预约中</cite>
                                                        </c:when>
                                                        <c:otherwise>
                                                        <c:if test="${dto.weixinExclusive==2}">
                                                        <cite class="dj">独家</cite>
                                                        </c:if>
                                                        </c:otherwise>
                                                        </c:choose>
                                                    <p><img src="${dto.gipic}" alt=""></p></dt>
                                                    <dd class="fl">
                                                        <div class="fl">
                                                            <h1 class="cut_out2">${dto.title}</h1>

                                                            <p><span class="cut_out2">${dto.desc}<b>&nbsp;</b></span>
                                                            </p>

                                                            <c:if test="${dto.reserveType!=1}">
                                                                <p>
                                                                    <em class="lb-mian-surplus">剩余：<b>${dto.sn}/${dto.cn}</b></em>
                                                                    <c:if test="${dto.point ne 0}">
                                                                        <em class="lb-mian-md">迷豆：<b>${dto.point}</b></em>
                                                                    </c:if>
                                                                </p>
                                                            </c:if>
                                                        </div>
                                                        <c:if test="${dto.reserveType==0}">
                                                            <c:choose>
                                                                <c:when test="${dto.sn==0}">
                                                                    <span class="min_btn th">淘</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="min_btn lh">领</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${dto.reserveType==1}">
                                                            <span class="min_btn yy">约</span>
                                                        </c:if>
                                                    </dd>
                                                </a>
                                            </dl>

                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="lb-main-none" style="display:block">
                                            <p><img src="${URL_LIB}/static/theme/default/images/my/libao.png" alt="">
                                            </p>

                                            <p>还没有上架礼包哦</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                        </c:if>

                        <c:if test="${not empty newDto.rows}">
                            <div class="lb-main-classify lb-main-zx" id="libao-Main">
                                <p>最新</p>
                                <c:choose>
                                    <c:when test="${not empty newDto.rows}">
                                        <c:forEach items="${newDto.rows}" var="dto">
                                            <dl>
                                                <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=${appkey}&profileid=${profile.profileId}&aid=${dto.gid}&type=1">
                                                    <dt class="fl">
                                                        <c:choose>
                                                        <c:when test="${dto.reserveType==1}">
                                                        <cite class="yyz">预约中</cite>
                                                        </c:when>
                                                        <c:otherwise>
                                                        <c:if test="${dto.weixinExclusive==2}">
                                                        <cite class="dj">独家</cite>
                                                        </c:if>
                                                        </c:otherwise>
                                                        </c:choose>
                                                    <p><img src="${dto.gipic}" alt=""></p></dt>
                                                    <dd class="fl">
                                                        <div class="fl">
                                                            <h1 class="cut_out2">${dto.title}</h1>

                                                            <p><span class="cut_out2">${dto.desc}</span></p>
                                                            <c:if test="${dto.reserveType!=1}">
                                                                <p><em
                                                                        class="lb-mian-surplus">剩余：<b>${dto.sn}/${dto.cn}</b></em>
                                                                    <c:if test="${dto.point ne 0}">
                                                                        <em class="lb-mian-md">迷豆：<b>${dto.point}</b></em>
                                                                    </c:if>
                                                                </p>
                                                            </c:if>
                                                        </div>
                                                        <c:if test="${dto.reserveType==0}">
                                                            <c:choose>
                                                                <c:when test="${dto.sn==0}">
                                                                    <span class="min_btn th">淘</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="min_btn lh">领</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${dto.reserveType==1}">
                                                            <span class="min_btn yy">约</span>
                                                        </c:if>
                                                    </dd>
                                                </a>
                                            </dl>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="lb-main-none" style="display:block">
                                            <p><img src="${URL_LIB}/static/theme/default/images/my/libao.png" alt="">
                                            </p>

                                            <p>还没有上架礼包哦</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </c:if>
                    </div>
                    <%--<c:if test="${page.maxPage>1}">--%>
                    <%--<div class="loading" id="giftload"><a href="javascript:;"><i--%>
                    <%--style="display:none"></i><b>点击加载更多</b></a></div>--%>
                    <%--</c:if>--%>
                    <input type="hidden" value="${hotDto.page.curPage}" id="hotWapCurPage"/>
                    <input type="hidden" value="${hotDto.page.maxPage}" id="hotWapMaxPage"/>
                    <input type="hidden" value="${newDto.page.curPage}" id="newWapCurPage"/>
                    <input type="hidden" value="${newDto.page.maxPage}" id="newWapMaxPage"/>
                </div>
            </div>
        </div>
        <%--<div id="libao-pullUp">--%>
        <%--<span class="pullUpIcon"></span>--%>
        <%--<span class="pullUpLabel">快使劲拉我O(∩_∩)O</span>--%>
        <%--</div>--%>
        <%--</div>--%>
    </div>
    <div id="midou-wrap" class="loadBox">
        <div id="thelist">
            <div class="tab-main">
                <c:choose>
                    <c:when test="${not empty midouDto}">
                        <div class="midou-shop  wrappers"
                             id="midou-shop">
                            <div class="shop" id="midou-main">
                                <c:forEach items="${midouDto.rows}" var="dto">
                                    <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=${appkey}&profileid=${profile.profileId}&aid=${dto.gid}&type=2">
                                        <div class="shop-box">
                                            <cite><img src="${dto.gipic}" class="shop-img"></cite>

                                            <div class="shop-box-text"><h2 class="cut_out2">${dto.title}</h2>

                                                <h3><span>${dto.point}</span>迷豆</h3></div>
                                            <c:choose>
                                                <c:when test="${dto.sn==0}">
                                                    <div class="shop-btn s2">已售罄</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="shop-btn s1">奖品有限，速来参加</div>
                                                </c:otherwise>
                                            </c:choose>

                                        </div>
                                    </a>
                                </c:forEach>

                                <c:if test="${midouDto.page.maxPage>1}">
                                    <%--<div class="loading" id="midouload"><a href="javascript:;"><i--%>
                                            <%--style="display:none"></i><b>点击加载更多</b></a></div>--%>
                                </c:if>
                                <input type="hidden" value="${midouDto.page.curPage}" id="midouWapCurPage"/>
                                <input type="hidden" value="${midouDto.page.maxPage}" id="midouWapMaxPage"/>
                                <!-- <div class="loading" id="loading-btn"><a href="javascript:;"><i style="display:none"></i><b>加载更多</b></a></div> -->
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="lb-main-none" style="display:block">
                            <p><img src="${URL_LIB}/static/theme/default/images/my/libao.png" alt=""></p>

                            <p>没有商品上架</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>

</div>


<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=222" style="border:0;" alt=""/></p></noscript>


<input type="hidden" value="${profile.profileId}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${uno}" name="uno"/>
<input type="hidden" value="${type}" name="type"/>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
</body>
</html>
