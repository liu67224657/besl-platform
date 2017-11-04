<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="pc">
    <meta name="mobile-agent" content="format=xhtml;url=http://m.joyme.com/">
    <meta name="mobile-agent" content="format=html5;url=http://m.joyme.com">

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="手机游戏礼包领取,手游兑换码,手游激活码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!
"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>手机游戏礼包领取,手游兑换码,手游激活码_着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/header.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<link href="${URL_LIB}/static/theme/default/css/new-giftcenter.css?${version}" rel="stylesheet" type="text/css"/>--%>
    <link href="${URL_STATIC}/pc/cms/jmsy/giftcenter/css/new-giftcenter.css?${version}"
          rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script>
        function choosePlatform(value) {
            $("#platform").val(value);
            $("#chooseGift").submit();

        }

        function chooseCooperation(value) {
            $("#cooperation").val(value);
            $("#chooseGift").submit();
        }

        function chooseFirst(value) {
            $("#firstletter").val(value);
            $("#chooseGift").submit();
        }

        function myGift() {
            var userSession = '${userSession}';
            if (userSession == null || userSession == '') {
                loginDiv();
                return;
            }
            window.location.href = '${URL_WWW}/mygift';
        }
        function searchText() {
            var text = $("[name='searchtext']").val().trim();
            if (text == '') {
                alert("请输入礼包名称");
                return false;
            }
        }

        $(document).ready(function () {
            //放送区轮播图
            var len = $("#headImage ul li").length;
            var maxpage = Math.ceil(len / 4);
            if (maxpage == 1) {
                $("#btn_gift_right").addClass("btn-gift-right-disable");
                $("#btn_gift_left").addClass("btn-gift-left-disable");
            } else {
                $("#btn_gift_left").addClass("btn-gift-left-disable");
            }
            var indexpage = 1;
            //切换图片
            $(".btn-gift-right").click(function () {
                $(".btn-gift-left").css("display", "block");
                if (indexpage < maxpage) {
                    $("#btn_gift_left").removeClass("btn-gift-left-disable");
                    indexpage++;
                    $("#headImage").animate({left: '-=100%'}, 500, function () {
                        if (indexpage == maxpage) {
                            $(".btn-gift-right").css("display", "none");
                        }
                    });
                }
            });

            $(".btn-gift-left").click(function () {
                if (indexpage > 1) {

                    $(".btn-gift-right").css("display", "block");
                    indexpage--;
                    $("#headImage").animate({left: '+=100%'}, 500, function () {
                        if (indexpage <= 1) {
                            $(".btn-gift-left").css("display", "none");
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<!-- header -->
<c:import url="/views/jsp/passport/new-header.jsp"/>
<div id="joyme-wrapper" class="fn-clear">
    <!--header-->

    <!--header==end-->
    <!--joyme-center-->
    <div class="joyme-center">
        <div class="carBox-toolBox fn-clear">
            <!--lb-slider-->
            <c:if test="${not empty  menupic}">
            <div class="carBox-toolBox fn-clear">
                <!--lb-slider-->
                <div class="joyme-pic-slid fn-left">
                    <span class="joyme-pic-slid-l hover"></span>
                    <span class="joyme-pic-slid-r hover"></span>
                    <ul class="joyme-pic-slid-box">
                        <c:forEach items="${menupic}" var="item" end="3">
                            <li>
                                <a href="${item.url}" target="_blank">
                                    <img class="lazy" src="${item.picUrl1}"
                                         data-url="${item.picUrl1}"
                                         alt="${item.menuName}"
                                         title="${item.menuName}"
                                         target="_blank"/>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                    <ol class="joyme-pic-slid-node"></ol>
                </div>
                </c:if>
                <div class="toolBox fn-left">
                    <div class="code-con fn-clear">
                        <cite class="fn-left">
                            <img src="http://joymepic.joyme.com/qiniu/original/2016/08/5/7af73b2b098b50408f08582011f39347bd48.jpg">
                            <font>扫码关注微信公众号</font>
                        </cite>
                        <cite class="fn-left">
                            <img src="http://joymepic.joyme.com/qiniu/original/2016/11/8/f17de3ab0f2da04fdd088410d2ffc726cf6d.png">
                            <font>扫码下载着迷玩霸</font>
                        </cite>
                    </div>
                    <div class="my-lb">
                        <a href="javascript:myGift();"><i class="lb-icon"></i><span>我的礼包</span></a>
                    </div>
                    <div class="search-box">
                        <form action="${URL_WWW}/gift/searchpage" onsubmit="return searchText();"  target="_blank" method="post">
                            <input type="text" class="search-text" value="${searchtext}" name="searchtext"
                                   placeholder="请输入礼包名称"/>
                            <input type="submit" class="search-icon"/>
                        </form>
                    </div>
                </div>
            </div>

            <!--lb-slider==end-->
            <div class="joyme-lb-main fn-clear">
                <!--joyme-lb-l-->
                <div class="joyme-lb-l">
                    <c:if test="${not empty exclusiveList}">
                        <div class="ele-lb">
                            <h2 class="joyme-title fn-clear"><span class="fn-left">独家<b>礼包</b></span></h2>

                            <div class="ele-lb-list">
                                <span class="ele-next">下一个</span>
                                <span class="ele-prev">上一个</span>
                                <!--ele-lb-slider-->
                                <div class="ele-lb-slider">
                                    <ul class="lb-ul fn-clear">
                                        <c:forEach items="${exclusiveList}" var="item" varStatus="index">
                                            <c:if test="${(index.index+1)%4==1||index.index==0}">
                                                <li>
                                            </c:if>
                                            <a href="${URL_WWW}/gift/${item.activityGoodsId}" target="_blank">
                                                    <span>
                                                        <img src="${item.activityPicUrl}"
                                                             title="${item.activitySubject}"
                                                             alt="${item.activitySubject}"/>
                                                          <c:if test="${item.platform.code!=3}">
                                                              <b></cite>
                                                                  <c:choose>
                                                                      <c:when test="${item.platform.code eq '4'}">
                                                                          <cite class="ios-icon" title="ios"></cite>
                                                                          <cite class="Android-icon"
                                                                                title="android"></cite>
                                                                      </c:when>
                                                                      <c:otherwise>
                                                                          <c:if test="${item.platform.code eq '0'}">
                                                                              <cite class="ios-icon" title="ios"></cite>
                                                                          </c:if>
                                                                          <c:if test="${item.platform.code eq '1'}">
                                                                              <cite class="Android-icon"
                                                                                    title="android"></cite>
                                                                          </c:if>
                                                                      </c:otherwise>
                                                                  </c:choose>
                                                              </b>
                                                          </c:if>
                                                    </span>

                                                <p>${item.activitySubject}</p>
                                                <c:choose>
                                                    <c:when test="${item.goodsResetAmount>0}">
                                                        <font class="lh-btn">领号</font>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <font class="th-btn">淘号</font>
                                                    </c:otherwise>
                                                </c:choose>
                                            </a>
                                            <c:if test="${(index.index+1)%4==0 ||(index.index+1)==exclusiveList.size()}">
                                                </li>
                                            </c:if>
                                        </c:forEach>

                                    </ul>
                                </div>
                                <!--ele-lb-slider==end-->
                            </div>
                        </div>
                    </c:if>
                    <!--广告位-->
                    <%@ include file="/hotdeploy/views/jsp/giftmarket/giftmarket-main-ad.jsp" %>
                    <c:if test="${not empty newRows}">
                        <div class="hot-lb lb-box">
                            <h2 class="joyme-title fn-clear"><span class="fn-left">最新<b>礼包</b></span>

                                    <%--<p class="fn-left">--%>
                                    <%--<a href="#">IOS</a><a href="#">安卓</a></p>--%>
                                <a href="${URL_WWW}/gift/more" class="joyme-title-more fn-right"
                                   target="_blank">查看全部</a>
                            </h2>

                            <div class="new-lb-list">
                                <ul class="lb-ul fn-clear">
                                    <c:forEach items="${newRows}" var="item" varStatus="index">
                                        <c:if test="${(index.index+1)%5==1||index.index==0}">
                                            <li>
                                        </c:if>
                                        <a href="${URL_WWW}/gift/${item.gid}" target="_blank">
                     <span>
                        <img data-url="${item.gipic}" width="70px" height="70px" title="${item.title}"
                             alt="${item.title}"
                             src="${URL_LIB}/static/theme/default/images/data-bg.gif"
                             class="lazy"/>
                         <c:if test="${item.platform!=3}">
                             <b>
                                 <c:choose>
                                     <c:when test="${item.platform==4}">
                                         <cite class="ios-icon" title="ios"></cite>
                                         <cite class="Android-icon" title="android"></cite>
                                     </c:when>
                                     <c:otherwise>
                                         <c:if test="${item.platform==0}">
                                             <cite class="ios-icon" title="ios"></cite>
                                         </c:if>
                                         <c:if test="${item.platform==1}">
                                             <cite class="Android-icon" title="android"></cite>
                                         </c:if>
                                     </c:otherwise>
                                 </c:choose>
                             </b>
                         </c:if>
                     </span>

                                            <p>${item.title}</p>
                                            <c:choose>
                                                <c:when test="${item.sn>0}">
                                                    <font class="lh-btn">领号</font>
                                                </c:when>
                                                <c:otherwise>
                                                    <font class="th-btn">淘号</font>
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                        <c:if test="${(index.index+1)%5==0 ||(index.index+1)==newRows.size()}">
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty hotRows}">
                        <div class="new-lb lb-box">
                            <a name="oranges"></a>

                            <h2 class="joyme-title fn-clear"><span class="fn-left">热门<b>礼包</b></span>

                                <p class="fn-left">

                                    <a href="${URL_WWW}/gift#oranges"
                                       <c:if test="${empty platform}">class="active"</c:if> >全部</a>
                                    <a href="${URL_WWW}/gift?p=0#oranges"
                                       <c:if test="${ platform eq 0}">class="active"</c:if>>IOS</a>
                                    <a href="${URL_WWW}/gift?p=1#oranges"
                                       <c:if test="${ platform eq 1}">class="active"</c:if>>安卓</a>

                                </p>
                                <a href="${URL_WWW}/gift/more" class="joyme-title-more fn-right"
                                   target="_blank">查看全部</a>
                            </h2>

                            <div class="new-lb-list">
                                <ul class="lb-ul fn-clear">
                                    <c:forEach items="${hotRows}" var="item" varStatus="index">
                                        <c:if test="${(index.index+1)%5==1||index.index==0}">
                                            <li>
                                        </c:if>
                                        <a href="${URL_WWW}/gift/${item.gid}" target="_blank">
                           <span>
                              <img data-url="${item.gipic}" width="70px" height="70px" title="${item.title}"
                                   alt="${item.title}"
                                   src="${URL_LIB}/static/theme/default/images/data-bg.gif"
                                   class="lazy"/>
                               <c:if test="${item.platform!=3}">
                                   <b>
                                       <c:choose>
                                           <c:when test="${item.platform==4}">
                                               <cite class="ios-icon" title="ios"></cite>
                                               <cite class="Android-icon" title="android"></cite>
                                           </c:when>
                                           <c:otherwise>
                                               <c:if test="${item.platform==0}">
                                                   <cite class="ios-icon" title="ios"></cite>
                                               </c:if>
                                               <c:if test="${item.platform==1}">
                                                   <cite class="Android-icon" title="android"></cite>
                                               </c:if>
                                           </c:otherwise>
                                       </c:choose>
                                   </b>
                               </c:if>

                           </span>

                                            <p>${item.title}</p>
                                            <c:choose>
                                                <c:when test="${item.sn>0}">
                                                    <font class="lh-btn">领号</font>
                                                </c:when>
                                                <c:otherwise>
                                                    <font class="th-btn">淘号</font>
                                                </c:otherwise>
                                            </c:choose>

                                        </a>
                                        <c:if test="${(index.index+1)%5==0 ||(index.index+1)==hotRows.size()}">
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </c:if>


                </div>
                <!--joyme-lb-l==end-->
                <!--joyme-lb-r-->
                <%@ include file="/views/jsp/giftmarket/giftmarket-right.jsp" %>

                <!--joyme-lb-r==end-->
            </div>
        </div>
        <!--joyme-center==end-->
    </div>

    <!-- footer -->
    <%@ include file="/hotdeploy/views/jsp/giftmarket/giftmarket-pos-ad.jsp" %>

    <%@ include file="/views/jsp/tiles/new-footer.jsp" %>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/giftcenter.js"></script>--%>
    <script type="text/javascript"
            src="${URL_STATIC}/pc/cms/jmsy/giftcenter/js/giftcenter.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/giftmarket-init.js');
    </script>
    <div class="retuen-top">
        <span class="rt-btn">返回顶部</span>
    </div>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/giftcenter.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>
    <%--<script>--%>
    <%--(function (G, D, s, c, p) {--%>
    <%--c = {//监测配置--%>
    <%--UA: "UA-joyme-000001", //客户项目编号,由系统生成--%>
    <%--NO_FLS: 0,--%>
    <%--WITH_REF: 1,--%>
    <%--URL: 'http://lib.joyme.com/static/js/iwt/iwt-min.js'--%>
    <%--};--%>
    <%--G._iwt ? G._iwt.track(c, p) : (G._iwtTQ = G._iwtTQ || []).push([c, p]), !G._iwtLoading && lo();--%>
    <%--function lo(t) {--%>
    <%--G._iwtLoading = 1;--%>
    <%--s = D.createElement("script");--%>
    <%--s.src = c.URL;--%>
    <%--t = D.getElementsByTagName("script");--%>
    <%--t = t[t.length - 1];--%>
    <%--t.parentNode.insertBefore(s, t);--%>
    <%--}--%>
    <%--})(this, document);--%>
    <%--</script>--%>
</body>
</html>
