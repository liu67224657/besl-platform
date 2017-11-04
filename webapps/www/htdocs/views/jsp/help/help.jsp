<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>帮助说明 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
</head>
<body>
<%@ include file="/views/jsp/tiles/header-only-icon.jsp" %>

<div class="content about-bg clearfix">
    <%@ include file="/views/jsp/help/left.jsp" %>
    <div class="about-right">
        <div class="about-title"><span>帮助说明</span></div>
        <div class="help-content">
            <p>
                如果您已经厌倦了微博的碎碎念，博客的孤芳自赏，想找到一帮与你同样热爱游戏的人，共同探索、制造快乐---那么欢迎来到着迷网。着迷网是好玩，好看，能很好的展示自己，并结识有趣的人的游戏主题社区,这里可以相遇和结交许多新的志同道合和有趣的人，他们将和你互动，原创属于自己的游戏文化，让更多的人认识你关注你，不管将来是否还玩着同样的游戏，你们的关系始终在这里不会褪色。</p>

            <div class="help-q">初次进入我能做什么？你们能带给我什么？</div>
            <div class="help-a">在这里你能找到很多与你有着共同游戏经历的朋友，你可以从这里找到你感兴趣的话题，参与讨论，转发分享。也可以发起话题</div>
            <div class="help-q">如何寻找自己感兴趣的话题？</div>
            <div class="help-a">点击上方导航栏的"随便看"，看看有什么您感兴趣的内容没有，别忘分享哦
             <!--   <img src="${URL_LIB}/static/theme/default/img/helpic1.jpg" />-->
            </div>

            <div class="help-q">看到喜欢的文章我该如何收藏？</div>
            <div class="help-a">您可以点击文章右下方的"心"形按钮，就可以收藏了它，并把这篇你喜欢的文章推荐给了你的好友。
               <!-- <img src="${URL_LIB}/static/theme/default/img/helpic2.jpg" />-->
            </div>

            <div class="help-q">标签是什么？为什么要添加标签？</div>
            <div class="help-a">标签是自定义描述自己职业、兴趣爱好的关键词。将文章通过标签分类后，可以查看其他人与你相同标签下都有哪些内容。
               <!-- <img src="${URL_LIB}/static/theme/default/img/helpic3.jpg" />-->
                
            </div>

            <div class="help-q">如何修改头像、密码、个性域名</div>
            <div class="help-a">登录后，在首页右上角昵称右侧的下拉按钮，进入账户设置页面，可以修改更新您的头像、密码、个性域名等
              <!-- <img src="${URL_LIB}/static/theme/default/img/helpic4.jpg"/>-->
            </div>
        </div>
        <!--help-content结束-->
    </div>
    <!--about-right结束-->
</div>
<!--content结束-->
<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
