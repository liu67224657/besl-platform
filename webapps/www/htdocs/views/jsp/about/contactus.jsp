<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>商务合作 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use("${URL_LIB}/static/js/init/common-init.js")
    </script>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="about-bg clearfix">
    <!-- 左侧导航 -->
    <div class="about-nav">
        <ul>
            <li><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
            <li><a href="${URL_WWW}/help/milestone">着迷里程碑</a><span></span></li>
            <li class="current"><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
            <li><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
            <li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
            <li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>
            <li><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
            <li><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
        </ul>
    </div>
    <!-- 职位详情 -->
    <div class="about-business">
        <h2 class="about-title"><span class="about-title-text-2">商务合作</span></h2>

        <p><br>着迷网(Joyme.com)，移动游戏攻略第一站。拥有专业的编辑团队，独特的wiki资料库真正做到热门游戏全覆盖，优质内容实时更新，同时支持PC、WAP、APP，以满足不同玩家的需求，因此而聚集了大量的玩家。
        </p>

        <p>着迷网因其在玩家心中“专业、权威”的形象及在业内“专业、独特”的地位吸引了诸多运营平台通过着迷网的内容来判断游戏的厚度，成为业内评判游戏生命力的标杆，着迷的内容已经成为诸多运营平台挑选游戏的标准之一。</p>

        <p>
            着迷网聚集核心玩家参与内容贡献，从玩家的最基本需求入手，注重产品的可持续发展和长久生命力，为玩家提供精彩丰富的游戏攻略和体验。致力为玩家提供“值得信赖的”、“快乐的”和“专业的”互动娱乐体验，上百万核心玩家狂赞“给力”!</p>

        <p>着迷网最新版本的移动游戏攻略APP更是全面支持离线浏览和实时更新，让玩家用手机直接查找资料，为数百万核心玩家带来手机浏览攻略不一样的畅快体验! </p>

        <p>如果您有新游戏需要提前发布预告信息，如果希望您的游戏获得推荐机会，如果已在运营的游戏希望和我们达成更深度的合作，请联系我们。</p>

        <br>

        <h3>销售联系人</h3>
        <ul class="clearfix">
            <li>
                <div class="n">联系人：王向东</div>
                <div class="e">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：<a href="mailto:tonywang@staff.joyme.com">tonywang@staff.joyme.com</a>
                </div>
                <div class="q">Q&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Q：1301336986</div>
            </li>
        </ul>

        <br>

        <%--<h3 style="border:none">广告销售</h3>--%>
        <%--<ul class="clearfix">--%>
            <%--<li>--%>
                <%--<div class="n">联系人：王向东</div>--%>
                <%--<div class="e">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：<a href="mailto:tonywang@staff.joyme.com">tonywang@staff.joyme.com</a>--%>
                <%--</div>--%>
                <%--<div class="q">Q&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Q：1301336986</div>--%>
            <%--</li>--%>
        <%--</ul>--%>

        <%--<br>--%>

        <h3 style="border:none">友情链接</h3>
        <ul class="clearfix">
            <li>
                <div class="n">联系人：彭晨保</div>
                <div class="e">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：<a href="mailto:chenbaopeng@staff.joyme.com">chenbaopeng@staff.joyme.com</a>
                </div>
                <div class="q">Q&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Q：1545090552</div>
            </li>
        </ul>
        <br><br>

        <h3>&nbsp;</h3>

        <h4>联系方式</h4>

        <p style="padding-bottom: 20px;">地址：北京市海淀区知春路27号量子芯座11层<br>总机：010 - 5129 2727</p>

        <h4>资料库示例</h4>

        <p>齐天大圣3Dwiki：<a href="http://qtds.joyme.com/">http://qtds.joyme.com/</a></p>

        <p>神魔大陆wiki：<a href="http://smdl.joyme.com/">http://smdl.joyme.com/</a></p>

        <p>圣火英雄传wiki：<a href="http://shyxz.joyme.com/">http://shyxz.joyme.com/</a></p>

        <br>
        <h4>专区示例</h4>

        <p>糖果传奇：<a href="http://www.joyme.com/games/tgcq">http://www.joyme.com/games/tgcq</a>
        </p>

        <p>密室逃脱2：<a href="http://www.joyme.com/games/mstt2">http://www.joyme.com/games/mstt2</a>
        </p>

        <p>愤怒的小鸟英雄传：<a href="http://www.joyme.com/games/birdheros">http://www.joyme.com/games/birdheros</a>
        </p>

        <br>
        <h4>评测示例</h4>

        <p>《海盗掠夺战》评测：比COC精致，比BB更有趣：<a href="http://www.joyme.com/news/reviews/201406/1840948.html">http://www.joyme.com/news/reviews/201406/1840948.html</a>
        </p>

        <p>《飞天小女警》评测：献给童心未泯的成年人：<a href="http://www.joyme.com/news/reviews/201406/2441566.html">http://www.joyme.com/news/reviews/201406/2441566.html</a>
        </p>

        <p>《迷你前锋》评测：你有虐心之理，我有创意之道：<a href="http://www.joyme.com/news/reviews/201406/2041288.html">http://www.joyme.com/news/reviews/201406/2041288.html</a>
        </p>

        <p>《愤怒的小鸟英雄传》评测 ：乐趣依旧不减当年：<a href="http://www.joyme.com/news/reviews/201406/1340237.html">http://www.joyme.com/news/reviews/201406/1340237.html</a>
        </p>
        <br>
        <br>
        <br>
        <br>
    </div>
</div>
<div class="piccon"></div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>


