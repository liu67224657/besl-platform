<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>家长监护 着迷网Joyme.com</title>
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
<div class="about-bg clearfix">
    <!-- 左侧导航 -->
    <div class="about-nav">
			<ul>
				<li><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
                <li><a href="${URL_WWW}/help/milestone">着迷里程碑</a><span></span></li>
				<li><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
				<li><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
				<li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
				<li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>
                <li><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
				<li class="current"><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
			</ul>
		</div>

    <!-- 家长监护 -->
    <div class="about-parentProtect">
        <img src="${URL_LIB}/static/theme/default/img/about-parentProtect.jpg" width="688">
        <br/><br/><br/>
        <div class="about-parentProtect-title apt-1">
            <h3>工程介绍</h3>
        </div>
        <div>
            <p>“家长监护工程”是一项有中华人民共和国文化部指导，旨在加强家长对未成年人参与网络游戏的监护，引导未成年人健康、绿色参与网络游戏，和谐家庭关系的社会性公益行动。它提供了一种切实可行的方法，一种家长实施监控的管道，使家长纠正部分未成年子女沉迷游戏的行为成为可能。该项社会公益行动充分反映了中国网络游戏行业高度的社会责任感，对未成年玩家合法权益的关注以及对用实际行动营造和谐社会的愿望。</p>
            <p>通过我们竭诚的服务，我们将为焦虑的家长提供监控孩子游戏的途径，解决孩子沉迷问题。并引导家长如何管理孩子健康游戏，保护孩子身心健康。</p>
            <br /><br />
        </div>

        <div class="about-parentProtect-title apt-2">
            <h3>未成年人健康参与网络游戏提示</h3>
        </div>
        <div>
            <p>随着网络在青少年中的普及，未成年人接触网络游戏已经成为普遍现象。为保护未成年人健康参与游戏，在政府进一步加强行业管理的前提下，家长也应当加强监护引导。为此，我们为未成年人参与网络游戏提供以下意见：</p>
            <p style="text-indent:0em;">一、主动控制游戏时间。游戏只是学习、生活的调剂，要积极参与线下的各类活动，并让父母了解自己在网络游戏中的行为和体验</p>
            <p style="text-indent:0em;">二、不参与可能耗费较多时间的游戏设置。不玩大型角色扮演类游戏，不玩有PK类设置的游戏。<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在校学生每周玩游戏不超过2小时，每月在游戏中的花费不超过10元。</p>
            <p style="text-indent:0em;">三、不要将游戏当作精神寄托。尤其在现实生活中遇到压力和挫折时，应多与家人朋友交流倾诉，不要只依靠游戏来缓解压力。</p>
            <p style="text-indent:0em;">四、养成积极健康的游戏心态。克服攀比、炫耀、仇恨和报复等心理，避免形成欺凌弱小、抢劫他人等不良网络行为习惯。</p>
            <p style="text-indent:0em;">五、注意保护个人信息。包括个人家庭、朋友身份信息，家庭、学校、单位地址，电话号码等，防范网络陷阱和网络犯罪。</p>
            <p style="text-indent:0em;"><br />文化部网络游戏内容审查专家委员会 <br />中国教育学会中小学信息技术教育委员会<br />中国青少年网络协会<br />二〇一〇年五月二十八日</p>
            <br /><br />
        </div>

        <div class="about-parentProtect-title apt-3">
            <h3>申请条件</h3>
        </div>
        <div>
            <p style="text-indent:0em;">1.  申请人需为被监护未成年人的法定监护人；</p>
            <p style="text-indent:0em;">2.  申请人的被监护人年龄小于18周岁；</p>
            <p style="text-indent:0em;">3.  申请人需为大陆公民，不含港、澳、台人士。</p>
            <p style="text-indent:0em;">4.  申请需提交材料</p>
            <br />
            <img src="${URL_LIB}/static/theme/default/img/jzjh.png" width="688">
            <br /><br />
            <p style="text-indent:0em;">申请人需通过邮寄方式向我司提交<a target="_blank" href="http://html.joyme.com/doc/shenqing.zip">《家长监护服务申请书》</a>及其中所提及需提供的附件：</p>
            <p style="text-indent:0em;">附件1：申请人的身份证和户口本（复印件）</p>
            <p style="text-indent:0em;">附件2：被申请人的身份证和户口本（复印件）</p>
            <p style="text-indent:0em;">附件3：申请人与被申请人的监护法律关系证明（原件）</p>
            <br /><br />
        </div>

        <div class="about-parentProtect-title apt-4">
            <h3>其他要求</h3>
        </div>
        <div>
            <p style="text-indent:0em;">1.  着迷网目前尚未涉及游戏运营业务，能且只能在有相关的业务开展之情况下，方可以申请家长监护服务。</p>
            <p style="text-indent:0em;">2.  申请人应提交较完备的申请材料，对未提供的信息要及时补充；可请熟知电脑、互联网、游戏的人员进行协助，<br />&nbsp;&nbsp;&nbsp;&nbsp;以便提供详实资料；</p>
            <p style="text-indent:0em;">3.  申请人应保证提交的信息真实有效；对于提供虚假信息或伪造证件，我公司将保留进一步追究法律责任的权利。</p>
            <br /><br />
        </div>

        <div class="about-parentProtect-title apt-5">
            <h3>声明</h3>
        </div>
        <div style="border:none">
            <p style="text-indent:0em;">着迷网目前尚未涉及任何直接运营游戏业务，能且只能在有相关的业务开展之情况下，可以申请家长监护服务。</p>
            <p style="text-indent:0em;">着迷网对目前网站推荐的任何网络游戏不承担运营责任。<br />二〇一四年八月一日</p>
            <br /><br />
        </div>

        <br><br><br><br>
    </div>
  </div>
<div class="piccon"></div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>


