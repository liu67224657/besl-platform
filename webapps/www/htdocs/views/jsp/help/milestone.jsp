<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>着迷里程碑 ${jmh_title}</title>
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

<!-- 关于着迷 -->
<div class="about-bg clearfix">
	<!-- 左侧导航 -->
		<div class="about-nav">
			<ul>
				<li><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
                <li  class="current"><a href="${URL_WWW}/help/milestone">着迷里程碑</a><span></span></li>
				<li><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
				<li><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
				<li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
				<li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>
				<li><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
                <li><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
			</ul>
		</div>

    <!-- 着迷里程碑 -->
    <div class="milestone">
        <h2 class="about-title"><span class="about-title-text-6">着迷里程碑</span></h2>

        <!-- 2011年 -->
        <h3><span>2011年</span></h3>
        <table>
            <tr>
                <th>4月</th>
                <td>着迷公司正式成立</td>
            </tr>
            <tr>
                <th>11月</th>
                <td>着迷首款产品——着迷网（www.joyme.com）正式开放注册</td>
            </tr>
        </table>

        <!-- 2012年 -->
        <h3><span>2012年</span></h3>
        <table>
            <tr>
                <th>5月</th>
                <td>着迷网推出首个“应用”性质的服务——《圣境传说》职业技能模拟器</td>
            </tr>
        </table>

        <!-- 2013年 -->
        <h3><span>2013年</span></h3>
        <table>
            <tr>
                <th>1月</th>
                <td>着迷Wiki资料库首款产品《动物之森》上线，支持手机浏览，由专业团队和达人玩家共同建设</td>
            </tr>
            <tr>
                <th>4月</th>
                <td>着迷首批系列攻略App全面上线，支持离线浏览</td>
            </tr>
            <tr>
                <th>6月</th>
                <td>着迷网日浏览量突破百万</td>
            </tr>
            <tr>
                <th rowspan="2">8月</th>
                <td>着迷网成功转型为手游垂直媒体</td>
            </tr>
            <tr>
                <td>着迷率先实行游戏内嵌攻略资料库合作，合作厂商达15家</td>
            </tr>
            <tr>
                <th>9月</th>
                <td>着迷与360合作发布《植物大战僵尸2》攻略App，当日下载超过30万次</td>
            </tr>
            <tr>
                <th>10月</th>
                <td>着迷礼包中心正式上线，首周领取礼包人数达20万人</td>
            </tr>
            <tr>
                <th rowspan="3">11月</th>
                <td>着迷荣获“2013金玩奖优秀移动游戏媒体奖”</td>
            </tr>
            <tr>
                <td>着迷论坛正式上线</td>
            </tr>
            <tr>
                <td>着迷礼包中心“双十一”活动，当日参与人数超50万人</td>
            </tr>
        </table>

        <!-- 2014年 -->
        <h3><span>2014年</span></h3>
        <table>
            <tr>
                <th rowspan="2">1月</th>
                <td>着迷荣获“2013金翎奖最佳游戏网络媒体奖”</td>
            </tr>
            <tr>
                <td>着迷网和攻略App日浏览量突破300万</td>
            </tr>
            <tr>
                <th>2月</th>
                <td>着迷完成B轮1.3亿元融资</td>
            </tr>
            <tr>
                <th>3月</th>
                <td>着迷精品轻阅读App产品《手游画报》正式上线</td>
            </tr>
            <tr>
                <th>5月</th>
                <td>着迷荣获“第七届中国网页游戏&移动游戏高峰论坛”优秀移动游戏媒体奖</td>
            </tr>
            <tr>
                <th>6月</th>
                <td>着迷江浙皖区域总部——“芜湖着迷网络信息技术有限公司”正式成立</td>
            </tr>
            <tr>
                <th>7月</th>
                <td>着迷社交App产品咔哒（KADA）正式上线</td>
            </tr>
        </table>

    </div>

</div>
<!--content结束-->

<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>
