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
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="Keywords" content="手机游戏礼包领取,手游兑换码,手游激活码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>手机游戏礼包领取,手游兑换码,手游激活码_着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/header.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_STATIC}/pc/cms/jmsy/giftcenter/css/new-giftcenter.css?v=${version}"
          rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script>
        function searchText() {
            var text = $("[name='searchtext']").val().trim();
            if (text == '') {
                alert("请输入礼包名称");
                return false;
            }
        }
    </script>
</head>
<body>
<c:import url="/views/jsp/passport/new-header.jsp"/>
<div id="joyme-wrapper" class="fn-clear">
    <div class="joyme-bt fn-nav">
        <span><b>当前位置：</b><a href="${URL_WWW}">着迷网</a>&nbsp;&gt;&nbsp;<a href="${URL_WWW}/gift">礼包中心</a>&nbsp;&gt;&nbsp;搜索结果</span>
    </div>
    <div class="joyme-center">
        <div class="joyme-lb-main fn-clear pt">
            <div class="joyme-lb-l">
                <!-- 新增 开始 -->
                <div class="search-box search-big">
                    <form action="${URL_WWW}/gift/searchpage" onsubmit="return searchText();" method="post">
                        <input type="text" class="search-text" name="searchtext" placeholder="请输入礼包名称"
                               value="">
                        <input type="submit" class="search-icon">
                    </form>

                </div>
                <c:choose>
                    <c:when test="${not empty list}">
                        <h4 class="search-resu">关键词<font>“${searchtext}”</font>的礼包列表，共${page.totalRows}条搜索结果：</h4>
                        <div class="lb-box letter-box fn-ovf">
                            <ul class="lb-ul fn-clear">
                                <c:forEach items="${list}" var="item" varStatus="index">
                                    <c:if test="${(index.index+1)%5==1||index.index==0}">
                                        <li>
                                    </c:if>
                                    <a href="${URL_WWW}/gift/${item.gid}" target="_blank">
                                                <span>
                                                   <img data-url="${item.gipic}" width="70px" height="70px"
                                                        title="${item.title}" alt="${item.title}"
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
                                                                        <cite class="Android-icon"
                                                                              title="android"></cite>
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
                                    <c:if test="${(index.index+1)%5==0 ||(index.index+1)==list.size()}">
                                        </li>
                                    </c:if>
                                </c:forEach>

                            </ul>
                            <c:if test="${not empty page&& page.maxPage>1}">
                                <c:set var="pageurl" value="${URL_WWW}/gift/searchpage"/>
                                <c:set var="pageparam"
                                       value="searchtext=${searchtext}"/>
                                <%@ include file="/views/jsp/page/gift-page.jsp" %>
                            </c:if>
                        </div>

                    </c:when>
                    <c:otherwise>
                        <h4 class="search-resu">没有找到关键词<font>“${searchtext}”</font>的礼包列表，为您推荐：</h4>
                        <div class="lb-box letter-box fn-ovf" id="a">
                            <ul class="lb-ul fn-clear">
                                <c:forEach items="${exclusiveList}" var="item" varStatus="index">
                                <c:if test="${(index.index+1)%5==1||index.index==0}">
                                <li>
                                    </c:if>
                                    <a href="${URL_WWW}/gift/${item.activityGoodsId}" target="_blank">
                                   <span>
                                      <img data-url="${item.activityPicUrl}" width="70px" height="70px"
                                           title="${item.activitySubject}" alt="${item.activitySubject}"
                                           src="${URL_LIB}/static/theme/default/images/data-bg.gif"
                                           class="lazy"/>
                                       <c:if test="${item.platform.code!=3}">
                                           <b>
                                               <c:choose>
                                                   <c:when test="${item.platform.code==4}">
                                                       <cite class="ios-icon" title="ios"></cite>
                                                       <cite class="Android-icon" title="android"></cite>
                                                   </c:when>
                                                   <c:otherwise>
                                                       <c:if test="${item.platform.code==0}">
                                                           <cite class="ios-icon" title="ios"></cite>
                                                       </c:if>
                                                       <c:if test="${item.platform.code==1}">
                                                           <cite class="Android-icon" title="android"></cite>
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
                                    <c:if test="${(index.index+1)%5==0 ||(index.index+1)==exclusiveList.size()}">
                                </li>
                                </c:if>
                                </c:forEach>

                                <!-- 搜索无结果时 -->
                                <!-- 新增 结束 -->
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <%@ include file="/views/jsp/giftmarket/giftmarket-right.jsp" %>
        </div>
    </div>
    <!-- footer -->
    <%@ include file="/views/jsp/tiles/new-footer.jsp" %>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/giftcenter.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>
    <script type="text/javascript">

        var mod = {
            btn: $('#exclubtn'),
            getClass: 'active',
            int: function () {
                mod.btn.on('click', function () {

                });
            }
        }
        mod.int();
    </script>

    <!-- *******扫描微信****** -->
    <%--<div id="scan-weixin"></div>--%>
    <script>
        (function (G, D, s, c, p) {
            c = {//监测配置
                UA: "UA-joyme-000001", //客户项目编号,由系统生成
                NO_FLS: 0,
                WITH_REF: 1,
                URL: 'http://lib.joyme.com/static/js/iwt/iwt-min.js'
            };
            G._iwt ? G._iwt.track(c, p) : (G._iwtTQ = G._iwtTQ || []).push([c, p]), !G._iwtLoading && lo();
            function lo(t) {
                G._iwtLoading = 1;
                s = D.createElement("script");
                s.src = c.URL;
                t = D.getElementsByTagName("script");
                t = t[t.length - 1];
                t.parentNode.insertBefore(s, t);
            }
        })(this, document);
    </script>
</body>
</html>
