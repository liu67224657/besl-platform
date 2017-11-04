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
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="about-bg clearfix">
    <!-- 左侧导航 -->
    <div class="about-nav">
        <ul>
            <li><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
            <li><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
            <li class="current"><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
            <li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
            <li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>
            <li><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
            <li><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
        </ul>
    </div>

    <!-- 职位详情 -->
    <div class="job-detail">
        <h2 class="about-title"><span class="about-title-text-5">工作在着迷</span></h2>

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;视频编辑
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>视频编辑</h3>

                    <p>• 工作地点：北京 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责视频文案的编写，修改。</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                负责视频素材的收集，录制。
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                负责视频相关的剪辑合成工作。
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>
                                负责日常视频所需图片素材的设计编辑工作。
                            </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>
                                负责视频后期的特效设计制作。
                            </dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>
                                负责视频相关的音效处理工作。
                            </dd>
                        </dl>
                        <dl>
                            <dt>7.</dt>
                            <dd>
                                负责定期将以往的各类素材整理入库工作。
                            </dd>
                        </dl>
                        <dl>
                            <dt>8.</dt>
                            <dd>
                                视频选题相关游戏的体验。
                            </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                能熟练使用PR,会声会影、sony vegas其中一种。
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                对剪辑中常用效果熟练掌握至少3种。
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                对画面有较好的掌控能力，能配合文案剪辑出配套画面。
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                对画面明暗，色彩搭配有较好的理解，并能熟练调控。
                            </dd>
                            <dt>5.</dt>
                            <dd>对BGM有一定理解，能根据不同文案，不同画面配上合适的BGM。</dd>
                            <dt>6.</dt>
                            <dd>能进行一些简单的音频剪辑处理。</dd>
                            <dt>7.</dt>
                            <dd>有一定剪辑视频作品并能稳定工作者优先。</dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <br><br>

            <div class="sendresume">
                <span><img width="16" height="16" src="http://lib.joyme.com/static/theme/default/img/zlzp.jpg"><a target="_blank" href="http://special.zhaopin.com/sh/2014/zlzp042150/">去智联招聘投简历</a></span>
            <span>
            <img width="11" height="16" src="http://lib.joyme.com/static/theme/default/img/51job.jpg">
            <a target="_blank" href="http://search.51job.com/list/co,c,2581201,000000,10,1.html">去51job投简历</a></span>
            </div>
        </div>
    </div>

</div>
<!--content结束-->
<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>
