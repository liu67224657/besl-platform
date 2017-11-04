<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<!--头部开始-->

<!--header-->
<header>
    <div class="joyme-header">
        <div class="joyme-header-top">
            <div class="joyme-nav fn-ma">
                <div class="joyme-logo fn-left fn-ovf">
                    <h1><a href="${URL_WWW}/" title="着迷网"><img
                            src="http://static.joyme.com/pc/cms/jmsy/images/joyme-logo.png" alt="着迷网" title="着迷网"></a>
                    </h1>
                </div>
                <ul class="joyme-nav-ul">
                    <li><a target="_blank" href="http://wiki.joyme.com/">WIKI</a></li>
                    <li><a target="_blank" href="http://www.joyme.com/news/#areaid=1010">资讯</a>

                        <div style="width:1000px;">
                            <a target="_blank" href="http://www.joyme.com/xinwen/#areaid=1016">新游资讯</a>
                            <a target="_blank" href="http://www.joyme.com/news/official/#areaid=1018">业界新闻</a>
                            <a target="_blank" href="http://www.joyme.com/news/ztqh/#areaid=1016">原创专栏</a>
                            <a target="_blank" href="http://www.joyme.com/news/reviews/#areaid=1314">游戏评测</a>
                            <a target="_blank" href="http://www.joyme.com/news/gameguide/#areaid=1011">游戏攻略</a>
                            <a target="_blank" href="http://www.joyme.com/news/rwzf/#areaid=1015">人物专访</a>
                            <a target="_blank" href="http://www.joyme.com/news/newpicture/#areaid=1012">美图囧图</a>
                            <a target="_blank" href="http://www.joyme.com/news/asiangames/#areaid=1017">热讯聚焦</a>
                            <a target="_blank" href="http://www.joyme.com/news/blue/#areaid=1013">精品推荐</a>
                        </div>
                    </li>
                    <li><a target="_blank" href="${URL_WWW}/collection/">游戏库</a></li>
                    <li><a target="_blank" href="${URL_WWW}/gift">礼包</a></li>
                    <li><a target="_blank" href="http://bbs.joyme.com/">论坛</a></li>
                    <li><a target="_blank" href="${URL_WWW}/vr/">VR</a></li>
                    <!--小绿点提示为cite标签-->
                </ul>
                <div class="joyme-search-bar fn-clear fn-ovf" style="width: 200px;">
                    <script type="text/javascript">
                        (function () {
                            document.write(unescape('%3Cdiv id="bdcs"%3E%3C/div%3E'));
                            var bdcs = document.createElement('script');
                            bdcs.type = 'text/javascript';
                            bdcs.async = true;
                            bdcs.src = 'http://znsv.baidu.com/customer_search/api/js?sid=6135004928527906484' + '&plate_url=' + encodeURIComponent(window.location.href) + '&t=' + Math.ceil(new Date() / 3600000);
                            var s = document.getElementsByTagName('script')[0];
                            s.parentNode.insertBefore(bdcs, s);
                        })();
                    </script>
                    <div id="bdcs">
                        <div class="bdcs-container">
                            <meta http-equiv="x-ua-compatible" content="IE=9">
                            <!-- 嵌入式 -->
                            <div class="bdcs-main bdcs-clearfix" id="default-searchbox">
                                <div class="bdcs-search bdcs-clearfix" id="bdcs-search-inline">
                                    <form action="http://zhannei.baidu.com/cse/search" method="get" target="_blank"
                                          class="bdcs-search-form" autocomplete="off" id="bdcs-search-form"><input
                                            type="hidden" name="s" value="6135004928527906484"> <input type="hidden"
                                                                                                       name="entry"
                                                                                                       value="1"> <input
                                            type="text" name="q" class="bdcs-search-form-input"
                                            id="bdcs-search-form-input" placeholder="请输入关键词"
                                            style="height: 28px; line-height: 28px;"> <input type="submit"
                                                                                             class="bdcs-search-form-submit "
                                                                                             id="bdcs-search-form-submit"
                                                                                             value="搜索"></form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="joyme-loginbar fn-right">
                    <script type="text/javascript">
                        document.write('<script src="http://passport.${DOMAIN}/auth/header/userinfo?t=index&' + Math.random() + '"><\/script>');</script>
                </div>
            </div>
        </div>
    </div>
    <style>
        #ujian_BtnDiv {
            display: none;
        }

    </style>
</header>
<script>
    var uri = window.location.href.replace('${URL_WWW}', '');
    if (uri.indexOf('/gift') == 0) {
        $('#gift').addClass('on');
    }
</script>
</header>