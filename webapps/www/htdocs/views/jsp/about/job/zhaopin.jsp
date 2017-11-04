<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>工作在着迷 ${jmh_title}</title>
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
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="about-bg clearfix" style="background:#fff">
    <div class="job-slogan"></div>

    <div class="job-item-1">
        <p><b>有竞争力的月薪+期权</b> — 在着迷，你有多优秀，你的薪资就有多丰厚。同时，对于特别优秀的你，我们将给予期权奖励！</p>

        <p><b>开放的职业平台 </b> — 着迷没有职业天花板，只要你有能力，够努力，对创业充满激情，你就可能成为NO.1！</p>

        <p><b>丰富的培训及员工分享</b> — 着迷为员工提供职业素养、工作技能、兴趣爱好等等各方面的培训。此外，着迷倡导、鼓励员工内部分享，在这里，大家会以一种开放和包容的心态，分享知识，传递技能！</p>

        <p><b>极富创意的工作内容</b> — 在着迷，你的工作极富创新！着迷从成立那天起，就一直在打破、创新，从创造一个全新的行业领域到一直引领向前，我们相信“打破陈旧，不断创新”早已成为这个时代的主流！</p>

        <p><b>健康保障</b> —
            “五险一金”是必须－不够的！这里还有人身意外保障、交通意外险、补充医疗保障、重大疾病保障……在着迷，你负责奋斗，我们负责健康！（吐槽：北京基本医疗保险的“1800起付，70%赔付”简直弱爆了，我们年经人用的就是“0起付，100%赔付”）。
        </p>

        <p><b>贴心照顾</b> — 午后煮上一杯浓香的咖啡，加上几块可口的点心，选一本感兴趣的书刊……休息，休息一下——如此温馨的画面，在着迷！</p>
    </div>

    <div class="job-item-2">
        <p>在着迷，我们不仅是同事，更是兄弟、是战友，我们为了共同的目标，为了同样的信仰，一起建造着快乐梦想之国。</p>

        <p>列车运行前方是知春路站，有前往“着迷国”的乘客，请您提前做好准备……着迷在这里，你在哪？</p>
    </div>


    <div class="job-item-3">
        <div id="job-tab" class="job-tab">
            <span class="city-beijing current">北京地区</span>
            <span class="city-wuhu">芜湖地区</span>
        </div>
        <!-- 北京招聘 -->
        <div id="city-beijing">
            <dl class="clearfix">
                <dt>职能类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/rlzyzz">人力资源总监</a>
                    <a href="${URL_WWW}/about/job/bj/pxjl">培训经理</a>
                    <a href="${URL_WWW}/about/job/bj/zpzg">招聘主管</a>
                    <a href="${URL_WWW}/about/job/bj/qt">前台</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>视频类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/spbj">视频编辑</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>运营类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/gjyyjl">高级运营经理</a>
                    <a href="${URL_WWW}/about/job/bj/zmwbyyzj">着迷玩霸运营总监</a>
                    <a href="${URL_WWW}/about/job/bj/zb">主编</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>市场类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/yxchjl">营销策划经理</a>
                    <a href="${URL_WWW}/about/job/bj/ggjl">公关经理 </a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>商务类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/swjl">商务经理</a>
                    <a href="${URL_WWW}/about/job/bj/xszxzy">销售执行专员</a>
                    <a href="${URL_WWW}/about/job/bj/ggxsjl">广告销售经理</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>设计类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/gui">GUI设计师</a>
                    <a href="${URL_WWW}/about/job/bj/ue">UE设计师</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>产品类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/ydcpjl">产品经理（移动方向）</a>
                    <a href="${URL_WWW}/about/job/bj/wzcpjl">产品经理（网站方向）</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>技术类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/bj/android">Android资深开发/经理</a>
                    <a href="${URL_WWW}/about/job/bj/php">PHP开发/经理</a>
                    <a href="${URL_WWW}/about/job/bj/qa">测试工程师</a>
                    <a href="${URL_WWW}/about/job/bj/html5">Html5开发工程师</a>
                </dd>
            </dl>
        </div>
        <!-- 芜湖招聘 -->
        <div id="city-wuhu" style="display: none;">
            <dl class="clearfix">
                <dt>视频类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/wh/spcb">视频采编</a>
                    <a href="${URL_WWW}/about/job/wh/spbj">视频编辑</a>
                    <a href="${URL_WWW}/about/job/wh/spzb">视频主播</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>运营类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/wh/seozg">SEO主管</a>
                    <a href="${URL_WWW}/about/job/wh/seozy">SEO专员</a>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>设计类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/wh/wy">网页设计师</a>
                    <a href="${URL_WWW}/about/job/wh/pm">平面设计师</a>
                    <a href="${URL_WWW}/about/job/wh/msbj">美术编辑</a>
                    <a href="${URL_WWW}/about/job/wh/msjl">美术设计部经理</a>
                </dd>

            </dl>
            <dl class="clearfix">
                <dt>技术类：</dt>
                <dd>
                    <a href="${URL_WWW}/about/job/wh/ios">ios开发工程师</a>
                    <a href="${URL_WWW}/about/job/wh/qa">测试工程师</a>
                    <a href="${URL_WWW}/about/job/wh/qd">前端开发工程师</a>
                    <a href="${URL_WWW}/about/job/wh/technical">技术部经理</a>
                </dd>
            </dl>
        </div>
    </div>


    <div class="job-item-4">
        <p>有这样如此令人"着迷"的机会，你还等什么！</p>

        <p>快快把简历E-mail至：<a href="mailto:jobs@staff.joyme.com">jobs@staff.joyme.com</a></p>

        <p>让我们认识你、 熟悉你吧。加入着迷网，做亿万玩家的代言人！</p>
    </div>

    <br><br><br><br>

</div>
<!--content结束-->

<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script type="text/javascript">
    var tab_city_btn = document.getElementById('job-tab').getElementsByTagName('span');
    var tab_city_beijing = document.getElementById('city-beijing');
    var tab_city_wuhu = document.getElementById('city-wuhu');
    tab_city_btn[0].onclick = function(){
        if (!/current/.test(this.className)){
            this.className += ' current';
            tab_city_beijing.style.display = 'block';
            tab_city_wuhu.style.display = 'none';
            tab_city_btn[1].className = 'city-wuhu';
        }
    }
    tab_city_btn[1].onclick = function(){
        if (!/current/.test(this.className)){
            this.className += ' current';
            tab_city_beijing.style.display = 'none';
            tab_city_wuhu.style.display = 'block';
            tab_city_btn[0].className = 'city-beijing';
        }
    }
</script>
</body>
</html>
