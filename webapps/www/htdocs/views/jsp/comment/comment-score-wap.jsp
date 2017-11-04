<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>评分_着迷网Joyme</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="http://lib.joyme.com/static/img/favicon.ico" type="image/x-icon">
    <c:choose>
        <c:when test="${key == 'ms'}">
            <link href="${URL_LIB}/static/theme/default/css/score-ms.css" rel="stylesheet" type="text/css">
            <link href="${URL_LIB}/static/theme/default/css/score-ms-common.css" rel="stylesheet" type="text/css">
        </c:when>
        <c:when test="${key == 'lt'}">
            <link href="${URL_LIB}/static/theme/default/css/score-lt.css" rel="stylesheet" type="text/css">
            <link href="${URL_LIB}/static/theme/default/css/score-lt-common.css" rel="stylesheet" type="text/css">
        </c:when>
        <c:when test="${key == 'mt2'}">
            <link href="${URL_LIB}/static/theme/default/css/score-mt2.css" rel="stylesheet" type="text/css">
            <link href="${URL_LIB}/static/theme/default/css/score-mt2-common.css" rel="stylesheet" type="text/css">
        </c:when>
        <c:otherwise>
            <link href="http://reswiki1.joyme.com/css/${key}/wxwiki/default/2/wiki.css" rel="stylesheet"
                  type="text/css">
            <link href="http://reswiki1.joyme.com/css/${key}/wxwiki/default/common.css" rel="stylesheet"
                  type="text/css">
        </c:otherwise>
    </c:choose>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js" type="text/javascript"></script>
</head>
<body>
<div id="wrap">
    <input type="hidden" id="input_hidden_unikey" value="${uniKey}"/>
    <input type="hidden" id="input_hidden_domain" value="${domain}"/>
    <input type="hidden" id="input_hidden_uri" value="${uri}"/>
    <!--pic_ban-->
    <c:if test="${dto != null && dto.score != null}">
        <div class="pic_ban pf_ban">
            <!--pic_ban_box-->
            <div class="pic_ban_box">
                <div class="pf_toolsbar">
                    <div class="pv_1">
                        <cite>★★★★★</cite>
                        <span style="width:30px;"></span>
                        <b>${dto.score.five_percent}</b>
                    </div>
                    <div class="pv_2">
                        <cite>★★★★</cite>
                        <span style="width:30px;"></span>
                        <b>${dto.score.four_percent}</b>
                    </div>
                    <div class="pv_3">
                        <cite>★★★</cite>
                        <span style="width:30px;"></span>
                        <b>${dto.score.three_percent}</b>
                    </div>
                    <div class="pv_4">
                        <cite>★★</cite>
                        <span style="width:30px;"></span>
                        <b>${dto.score.two_percent}</b>
                    </div>
                    <div class="pv_5">
                        <cite>★</cite>
                        <span style="width:30px;"></span>
                        <b>${dto.score.one_percent}</b>
                    </div>
                </div>
                <div class="pf_showbox">
                    <span><code>${dto.score.average_score}</code><b>分</b></span>
                    <span class="pf_user"><cite></cite><b>${dto.score.times_sum}</b></span>
                </div>
            </div>
            <!--pic_ban_box==end-->
            <!--pf_grade-->
            <div class="pf_grade">
            	<span class="grade_icon">
                    <cite></cite>
                    <cite></cite>
                    <cite></cite>
                    <cite></cite>
                    <cite></cite>
                </span>
                <span class="pf_btn">评分</span>
                <!--pf_tips-->
                <div class="pf_tips">
                    <strong>提示：</strong>
                    <span>评分成功，感谢您的参与！</span>
                    <span class="pf_close">关闭</span>
                </div>
                <!--pf_tips-->
            </div>
            <!--pf_grade==end-->
        </div>
    </c:if>
    <!--pic_ban==end-->
    <!--con_pet_data-->
    <div class="con_pet_data">
        <!--con_pet_menu-->
        <div class="con_pet_menu">
            <div class="con_pet_tc">全部吐槽</div>
        </div>
        <!--con_pet_menu==end-->
        <!--con_pet_main-->
        <div class="con_pet_main con_pet_pf">
            <!--pf_comment_bar-->
            <div class="pf_comment_bar">
                <div>
                    <input type="text" class="pf_comment_text" placeholder="请输入少于8个汉字的评论">
                    <a href="javascript:;" class="pf_comment_btn">评论</a>
                </div>
            </div>
            <!--pf_comment_bar-->
            <div class="con_pet_comment">
                <c:if test="${dto != null && dto.mainreplys != null && dto.mainreplys.rows.size() > 0}">
                    <c:forEach items="${dto.mainreplys.rows}" var="mainreply">
                        <div class="pf_comment_list">
                                <span>
                                    <b><img alt="${mainreply.reply.user.name}" src="${mainreply.reply.user.icon}"
                                            title=""></b>
                                    <em>${mainreply.reply.user.name}</em>
                                </span>
                            <em class="details_link fr"><cite
                                    id="agree_reply_${mainreply.reply.reply.rid}">赞+${mainreply.reply.reply.agree_sum}</cite></em>

                            <div>${mainreply.reply.reply.body.text}</div>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
        </div>
        <!--con_pet_main==end-->
    </div>
    <!--con_pet_data==end-->
    <!--悬浮菜单-->
    <!--float_menu-->
    <c:choose>
        <c:when test="${key == 'ms'}">
            <div id="float_menu">
                <div class="column_menu">
                    <ul>
                        <li><a href="http://hezuo.joyme.com/ms/" class="a_1"><img width="60" height="58"
                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_1.png"
                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/ms/85470.shtml" class="a_2"><img width="60" height="58"
                                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_2.png"
                                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/ms/86306.shtml" class="a_3"><img width="60" height="58"
                                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_3.png"
                                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/ms/75761.shtml" class="a_4"><img width="60" height="58"
                                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_4.png"
                                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/ms/64889.shtml" class="a_5"><img width="58" height="55"
                                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_5.png"
                                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/ms/85586.shtml" class="a_6"><img width="58" height="58"
                                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_7.png"
                                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/ms/86590.shtml" class="a_7"><img width="58" height="58"
                                                                                                  src="${URL_LIB}/static/theme/default/images/score/menu_8.png"
                                                                                                  alt="" title=""></a>
                        </li>
                        <li><a href="http://www.joyme.com/wxwiki/${key}/index.shtml" class="a_8"><img width="58"
                                                                                                      height="58"
                                                                                                      src="${URL_LIB}/static/theme/default/images/score/menu_11.png"
                                                                                                      alt=""
                                                                                                      title=""></a></li>
                        <li><a href="#" class="a_8"></a></li>
                    </ul>
                </div>
            </div>
            <!--float_menu==end-->
            <div id="float_menu_btn"><b>菜</b><b>单</b></div>
            <div id="opacity_bg" style="height: 330px;"></div>
            <!--悬浮菜单==end-->
        </c:when>
        <c:when test="${key == 'lt'}">
            <div id="wap_bg" class="" style="z-index: 0; display: none"></div>
            <div class="" id="wx_center">
                <div class="wx_game_icon" id="float_menu_div">
                    <a href="http://www.joyme.com/wxwiki/lt/60536.shtml" class="a_8"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/4/4b/Lt_icon_1.png">高手进阶</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/60528.shtml" class="a_7"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/d/d1/Lt_icon_2.png">新手专区</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/76663.shtml" class="a_9"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/9/95/Lt_icon_3.png">关卡攻略</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/76523.shtml" class="a_1"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/b/b6/Lt_icon_4.png">战机图鉴</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/77182.shtml" class="a_2"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/0/0c/Lt_icon_5.png">装甲图鉴</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/91694.shtml" class="a_6"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/b/b2/Lt_icon_6.png">其他图鉴</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/91575.shtml" class="a_5"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/e/e9/Lt_icon_7.png">关卡视频</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/91576.shtml" class="a_4"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/b/bb/Lt_icon_8.png">高玩视频</b></a>
                    <a href="http://www.joyme.com/wxwiki/lt/91597.shtml" class="a_3"><i></i><b
                            data-floatmenupic="http://lt.joyme.com/images/lt/9/97/Lt_icon_9.png">周边八卦</b></a>
                </div>
            </div>
            <div id="wap_float1">
                <span id="wap_floatBtn" class=""></span>
            </div>
            <footer>
                <div id="wx_footer">
                    以上内容由腾讯和着迷网联合制作
                </div>
            </footer>
        </c:when>
        <%--<c:when test="${key == 'ttfw'}">--%>
            <%--<div id="float_menu">--%>
                <%--<div class="column_menu">--%>
                    <%--<ul>--%>
                        <%--<li><a href="http://www.joyme.com/wxwiki/ttfw/78018.shtml" class="a_1">--%>
                            <%--<img width="60" height="58"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/a/a4/Icon-1.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                        <%--<li><a href="http://www.joyme.com/wxwiki/ttfw/77998.shtml" class="a_2">--%>
                            <%--<img width="60" height="58"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/9/98/Icon-2.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                        <%--<li><a href="http://www.joyme.com/wxwiki/ttfw/83419.shtml" class="a_3">--%>
                            <%--<img width="60" height="58"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/e/ec/Icon-3.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                        <%--<li><a href="http://www.joyme.com/wxwiki/ttfw/78005.shtml" class="a_4">--%>
                            <%--<img width="60" height="58"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/4/45/Icon-7.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                        <%--<li><a href="http://www.joyme.com/wxwiki/ttfw/81045.shtml" class="a_5">--%>
                            <%--<img width="58" height="55"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/8/85/Icon-8.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                        <%--<li><a href="http://www.joyme.com/wxwiki/ttfw/78057.shtml" class="a_6">--%>
                            <%--<img width="58" height="58"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/2/23/Icon-9.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                        <%--<li><a href="http://ttfw.joyme.com/wxwiki/87216.shtml" class="a_7">--%>
                            <%--<img width="58" height="58"--%>
                                 <%--src="http://ttfw.joyme.com/images/ttfw/5/51/Icon-10.png"--%>
                                 <%--alt="" title=""></a>--%>
                        <%--</li>--%>
                            <%--&lt;%&ndash;<li><a href="http://www.joyme.com/wxwiki/${key}/index.shtml" class="a_8">&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<img width="58"&ndash;%&gt;--%>
                            <%--&lt;%&ndash;height="58"&ndash;%&gt;--%>
                            <%--&lt;%&ndash;src="${URL_LIB}/static/theme/default/images/score/menu_11.png"&ndash;%&gt;--%>
                            <%--&lt;%&ndash;alt=""&ndash;%&gt;--%>
                            <%--&lt;%&ndash;title=""></a></li>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<li><a href="#" class="a_8"></a></li>&ndash;%&gt;--%>
                    <%--</ul>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<!--float_menu==end-->--%>
            <%--<div id="float_menu_btn"><b>菜</b><b>单</b></div>--%>
            <%--<div id="opacity_bg" style="height: 330px;"></div>--%>
            <%--<!--悬浮菜单==end-->--%>
        <%--</c:when>--%>
    </c:choose>
</div>
<c:choose>
    <c:when test="${key == 'ms' || key == 'lt' || key == 'mt2'}">
        <script type="text/javascript" src="${URL_LIB}/static/js/init/comment-score-${key}.js"></script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript" src="http://reswiki1.joyme.com:8000/css/${key}/wxwiki/js/action.js"></script>
    </c:otherwise>
</c:choose>
<script type="text/javascript" src="${URL_LIB}/static/js/init/comment-score-wap.js"></script>
<c:if test="${key == 'ms'}">
    <!-- Piwik -->
    <script type="text/javascript">
        var _paq = _paq || [];
        _paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
        _paq.push(['trackPageView']);
        _paq.push(['enableLinkTracking']);
        (function () {
            var u = "//stat.joyme.com/";
            _paq.push(['setTrackerUrl', u + 'piwik.php']);
            _paq.push(['setSiteId', 10]);
            var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
            g.type = 'text/javascript';
            g.async = true;
            g.defer = true;
            g.src = u + 'piwik.js';
            s.parentNode.insertBefore(g, s);
        })();
    </script>
    <noscript><p><img src="//stat.joyme.com/piwik.php?idsite=10" style="border:0;" alt=""/></p></noscript>
    <!-- End Piwik Code -->
</c:if>
</body>
</html>
