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

    <meta name="Keywords" content="${detailDTO.title}">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${detailDTO.title}_兑换码_领取地址_着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/header.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<link href="${URL_LIB}/static/theme/default/css/new-giftcenter.css?${version}" rel="stylesheet" type="text/css"/>--%>
    <link href="${URL_STATIC}/pc/cms/jmsy/giftcenter/css/new-giftcenter.css?${version}"
          rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>

    <script type="text/javascript">
        function myGift() {
            var userSession = '${userSession}';
            if (userSession == null || userSession == '') {
                loginDiv();
                return;
            }
            window.location.href = '${URL_WWW}/mygift';
        }
    </script>

</head>
<body>
<c:import url="/views/jsp/passport/new-header.jsp"/>

<div class="joyme-bt fn-nav">
    <div>当前位置：<a href="${URL_WWW}">着迷网</a> &gt; <a href="${URL_WWW}/gift/">礼包中心</a>
        &gt; ${detailDTO.title}
    </div>
</div>
<div class="joyme-center">
    <div class="joyme-lb-main fn-clear pt">
        <!--joyme-lb-l-->
        <div class="joyme-lb-l fn-clear">
            <!--专属礼包-->
            <div class="zslb-lb">
                <dl class="zslb-lb-top fn-clear">
                    <dt><img src="${detailDTO.gipic}" width="120" height="120" alt="${detailDTO.title}"
                             title="${detailDTO.title}"/></dt>
                    <dd>
                        <span class="detail-text fn-clear">
                               <b>${detailDTO.title}</b>
                               <c:if test="${detailDTO.platform eq 0 || detailDTO.platform eq 4}">
                                   <em class="mobile-ios"></em>
                               </c:if>
                               <c:if test="${detailDTO.platform eq 1  || detailDTO.platform eq 4}">
                                   <em class="mobile-and"></em>
                               </c:if>
                        </span>

                        <p>剩余量：<em><span id="jdtStyle"
                                         style="width:${detailDTO.rn/detailDTO.cn*100}%"></span></em><code><font
                                id="resetNum2">${detailDTO.rn}</font>/<code id="countNum">${detailDTO.cn}</code>
                        </code></p>
                        <i>发布时间：${dateutil:dateToString(detailDTO.startTime, 'yyyy年MM月dd日 HH:mm')}</i>
                        <span>有效期：${fn:substring(detailDTO.endTime, 0, 19)}</span>

                        <div class="default-btn fn-clear">
                            <c:if test="${detailDTO.weixinExclusive==0}">
                                <c:if test="${detailDTO.rn>0}">
                                    <a class="zslb-lb-lh" href="javascript:void(0);" id="getCode">领号</a>
                                </c:if>
                                <c:if test="${taobtn eq 1}">
                                    <a class="zslb-lb-th" href="javascript:void(0);" id="taoCode">淘号</a>
                                </c:if>
                            </c:if>
                        </div>
                    </dd>
                </dl>
                <c:if test="${detailDTO.weixinExclusive==1}">
                    <dl class="zslb-lb-bottom fn-clear">
                        <dt>
                            <b>专属礼包</b>
                            <span>此礼包为微信独家礼包,请关注<strong>着迷网络</strong>领取</span>
                        <p>
                            <b>领取方法:</b>
                            <b>1、用手机扫描下方二维码；</b>
                            <b>2、公众号里点击领取礼包；</b>
                        </p>
                        </dt>
                        <dd>
                            <cite> <img src="${URL_LIB}/static/theme/default/images/joyme-special4.jpg"></cite>
                        </dd>
                    </dl>
                </c:if>
                <c:if test="${detailDTO.weixinExclusive==2}">
                    <dl class="zslb-lb-bottom fn-clear">
                        <dt>
                            <b>专属礼包</b>
                            <span>本礼包是着迷玩霸专属礼包，安装客户端后才能领取！</span>
                        <p>
                            <b>安装方法:</b>
                            <b>1、用手机扫描下方二维码；</b>
                            <b>2、点击下方按钮下载安装；</b>
                        </p>
                        </dt>
                        <dd>
                            <cite> <img src="http://joymepic.joyme.com/qiniu/original/2016/11/2/5e22c3880e4c1049c40911d01a0e11607462.jpg"></cite>
                        </dd>
                    </dl>
                </c:if>
                <div class='zslb-lb-bottom fn-clear th-lb-box' id="taocodeDiv" style="display:none;"><h2>恭喜您淘号成功</h2>
                    <span>您所淘的二手号可能之前已被其他玩家使用，如无法激活可多淘几次试试</span>

                    <span style="color:red;" id="taoCodeSetTime"><span
                            id="taoCodeMinute">15</span>秒钟后才可再次淘号哦</span>

                    <a href="javascript:void(0);" id="taoCode2" class="taoagain">再淘一次</a>

                </div>
                <div class="zslb-lb-bottom fn-clear th-lb-box lh-lb-box" id="getcodeDiv" style="display:none;">
                    <h2>恭喜您领号成功</h2>
                    <span>恭喜您获得了<em id="codeTitle"></em>您可以进入<a href="${URL_WWW}/mygift">我的礼包</a>查看</span>
                    <ul class="fn-clear">
                        <li>
                            <code id="codeValue">W000G7XF6CBE</code><cite id="copy-button">复制</cite><cite
                                class="sent-phone"
                                id="phone-button"
                                data-lid="">发送到手机上</cite>
                        </li>
                    </ul>
                    <span><strong>*</strong>已领取的礼包请在<span style="color:red;font-weight: bold;">1小时内</span>尽快使用，否则将会进入淘号库被其他用户使用哦！</span>
                </div>
            </div>
            <!--专属礼包==end-->
            <!--礼包内容-->
            <div class="lbnr-lb">
                <h2 class="joyme-title fn-clear"><span class="fn-left">礼包<b>内容</b></span></h2>

                <div class="lbnr-lb-cont fn-ovf">
                    <c:if test="${not empty detailDTO.desc}">
                        ${detailDTO.desc}
                    </c:if>
                    <%--<c:if test="${not empty detailDTO.textJsonItemsList}">--%>
                    <%--<c:forEach items="${detailDTO.textJsonItemsList}" var="dto">--%>
                    <%--<c:if test="${dto.type=='1'}">--%>
                    <%--<p>${dto.item}</p>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${dto.type=='2'}">--%>
                    <%--<img src="${dto.item}" style="display:block;margin:0 auto;" />--%>
                    <%--</c:if>--%>
                    <%--</c:forEach>--%>
                    <%--</c:if>--%>
                </div>
                <%--<div class="games-jt-cont">--%>
                <%--<ul class="fn-clear games-jt-cont-ul">--%>
                <%--<c:if test="${not empty detailDTO.textJsonItemsList}">--%>
                <%--<c:forEach items="${detailDTO.textJsonItemsList}" var="dto">--%>

                <%--</c:forEach>--%>
                <%--</c:if>--%>
                <%--</ul>--%>
                <%--</div>--%>
                <div class="popupImg">
                    <cite></cite>
                </div>
            </div>
            <!--礼包内容==end-->
            <c:if test="${not empty list}">
                <div class="new-lb lb-box">
                    <h2 class="joyme-title fn-clear"><span class="fn-left">推荐<b>礼包</b></span></h2>

                    <div class="new-lb-list">
                        <ul class="lb-ul fn-clear">
                            <c:forEach items="${list}" var="item" varStatus="index">
                                <c:if test="${(index.index+1)%5==1||index.index==0}">
                                    <li>
                                </c:if>
                                <a href="${URL_WWW}/gift/${item.gid}">
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
                                <c:if test="${(index.index+1)%5==0 ||(index.index+1)==list.size()}">
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
        <c:if test="${!empty detailDTO.gameIcon || !empty detailDTO.gameTitie  ||!empty detailDTO.gameTitie || detailDTO.gameType.getValue()!=0 || !empty detailDTO.gameDeveloper||!empty detailDTO.iosUrl|| !empty detailDTO.androidUrl}">
            <div class="joyme-lb-r">
                <div class="load-lb fn-clear">
                    <h3>${detailDTO.gameTitie}</h3>
                    <dl class="fn-clear">
                        <dt><a href="${URL_WWW}/collection/${detailDTO.gameDbId}" target="_blank"><img src="${detailDTO.gameIcon}"
                                                                                       width="96"
                                                                                       title="${detailDTO.gameTitie}"
                                                                                       height="96"
                                                                                       alt="${detailDTO.gameTitie}"/></a>
                        </dt>
                        <dd>
                            <span> <c:if test="${not empty detailDTO.categoryList}">游戏类型： </c:if>
                             <c:forEach items="${detailDTO.categoryList}" var="item">
                                 ${item}&nbsp;&nbsp;
                             </c:forEach>
                            </span>
                            <span>平台：
                                <c:if test="${not empty detailDTO.iosUrl}">
                                    IOS
                                </c:if>
                                <c:if test="${not empty detailDTO.iosUrl&&not empty detailDTO.androidUrl}">
                                    /
                                </c:if>
                                  <c:if test="${not empty detailDTO.androidUrl}">
                                      Android
                                  </c:if>
                                </span>
                            <c:if test="${!empty detailDTO.wikiUrl}"><a href="${detailDTO.wikiUrl}">WIKI</a></c:if>
                        </dd>
                    </dl>
                    <div class="load-lb-btn">
                        <c:if test="${not empty detailDTO.iosUrl}">
                            <a href="${detailDTO.iosUrl}" class="iosBtn"><cite></cite>iPhone 版下载<span><b><code><img
                                    src="${URL_WWW}/acitivty/qrcode/generator?url=${detailDTO.iosUrl}"
                                    alt=""/></code><font>手机扫描下载</font></b></span></a>
                        </c:if>
                        <c:if test="${not empty detailDTO.androidUrl}">
                            <a href="${detailDTO.androidUrl}" class="andBtn"><cite></cite>Android 版下载<span><b><code><img
                                    src="${URL_WWW}/acitivty/qrcode/generator?url=${detailDTO.androidUrl}"
                                    alt=""/></code><font>手机扫描下载</font></b></span></a>
                        </c:if>

                    </div>
                </div>
            </div>
        </c:if>
        <div class="joyme-lb-r fn-clear">
            <div class="my-lb">
                <a href="javascript:myGift();"><i class="lb-icon"></i><span>我的礼包</span></a>
            </div>
        </div>
        <%@ include file="/views/jsp/giftmarket/giftmarket-right.jsp" %>

        <!--joyme-lb-r==end-->
    </div>
</div>
<input type="hidden" id="resetNum" value="${detailDTO.rn}"/>
<input type="hidden" id="gid" value="${detailDTO.gid}"/>
<input type="hidden" id="aid" value="${detailDTO.aid}"/>
<input type="hidden" id="timeOut" value="${detailDTO.endTime}">

<!-- footer -->
<%@ include file="/views/jsp/tiles/new-footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/gift-init.js');
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftcenter.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>
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
