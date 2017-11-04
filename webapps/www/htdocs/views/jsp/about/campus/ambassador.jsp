<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>校园招募 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
</head>
<body>
<%@ include file="/views/jsp/tiles/header-only-icon.jsp" %>
<div id="wraper" class="content about-bg clearfix">
    <%@ include file="/views/jsp/help/left.jsp" %>
    <div style="padding:22px 22px 8px" class="about-right">
        <!-- 校园招募开始 -->
        <div class="campusRecruiting">
            <h2><span></span>我们需要你有：</h2>

            <p><span>号召力，</span>能聚集游戏信徒，为分享快乐的信仰而前进；</p>

            <p><span>感染力，</span>让更多的人沉浸在着迷的乐趣中；</p>

            <p><span>执行力，</span>有效利用资源，保质保量达成目标。</p>
            <br>
            <br>

            <h2><span></span>你将得到：</h2>

            <p>1.着迷网提供的<span>优厚薪水</span>。</p>

            <p>2.专业的<span>岗前培训</span>。</p>

            <p>3.广阔的<span>提升空间</span>。</p>

            <p>4.<span>管理智慧</span>的启迪。</p>

            <p>5.<span>工作视野</span>的开拓。</p>

            <p>6.<span>人脉交际</span>的融入，认识更多能帮助你的朋友。</p>

            <p style="padding-top:6px;">成为着迷网的校园大使，为你的职业生涯奠定良好的基础。</p>
            <br>
            <br>

            <h2><span></span>校园大使工作职责：</h2>

            <p>负责着迷网兼职编辑的招募和管理。</p>

            <p>负责着迷网的在校园宣传和推广</p>
            <br>
            <br>

            <h2><span></span>申请方式：</h2>

            <p><a class="downFile" href="${URL_LIB}/static/download/campus-ambassador.docx">下载并填写申请表</a></p>

            <p>填写好申请表后，以附件形式提交申请表，提交格式：大学_姓名_着迷网校园大使申请表（如：北京交通大学_赵小米_着迷网校园大使申请表），发送至<a href="#">jobs@staff.joyme.com</a>
            </p>

            <div class="campusRecruiting-bottom"></div>
        </div>
        <!-- 校园招募结束 -->
    </div>
    <!--about-right结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<div class="piccon"></div>
</body>
</html>