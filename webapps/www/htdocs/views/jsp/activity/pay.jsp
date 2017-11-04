<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>基本信息 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <style>
        .pay{ text-align:left; }
        .pay ul{ padding:30px 0 0 300px; overflow:hidden; }
        .pay ul li{ font-size:0; padding:10px 0; text-align:left; }
        .pay ul li span{ font-size:14px; }
        .pay ul li p{ vertical-align:middle; overflow:hidden; padding:20px 0 0; }
        .pay ul li p input{ vertical-align:15px; margin-right:5px; }
        .inp-pay,.sel-pay{ border:1px solid #ccc; height:22px; line-height:22px; }
        .inp-pay{ width:100px; text-indent:3px; font-size:14px; margin-right:4px; }
        .sel-pay{ width:60px; height:22px; }
        .pay-box{ text-align:center; overflow:hidden; padding:5px 0; }
        .pay-box .pay-btn{ width:120px; height:32px; line-height:32px; display:inline-block; *display:inline; *zoom:1; background:#169BD5; color:#fff; font-size:14px; text-decoration:none; }
        .pay-popup,.popup-bg{ display:none; }
        .pay-popup{ width:350px; height:50px; line-height:50px; background:rgba(0,0,0,0.6); *background:#000; *opacity:0.5; position:fixed; top:50%; left:50%; margin:-25px 0 0 -125px; font-family:"微软雅黑"; -webkit-border-radius:3px; -moz-border-radius:3px; border-radius:3px; z-index:999; }
        .pay-popup h2{ text-align:center; font-weight:normal; font-size:18px; color:#fff; }
        .yx-icon{display:inline-block;width:100px;}
        .yx-icon img{width:100%;}
        .pay-tips{ color:#ccc; padding-left:68px; }
        p{font-size:14px;line-height:24px}
    </style>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="/views/jsp/customize/leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <!--设置title-->
        <div class="set_title">
            <h3>充值</h3>
        </div>
        <div class="pay">
            <ul>
                <li>
                    <span>游戏：</span>
                    <p><label><input type="radio" checked='checked' name="游戏" value="游戏" /><i class="yx-icon"><img src="${URL_LIB}/static/theme/default/images/activity/yx-icon.jpg" alt="游戏" title="游戏" /></i></label></p>
                </li>
                <li>
                    <span>充值金额：</span>
                    <span>
                    	<select class="sel-pay">
                            <option>10</option>
                            <option>50</option>
                            <option>100</option>
                        </select>
                        元
                    </span>
                </li>
                <li><span>其他金额：</span><span><input type="text" class="inp-pay" name="" value="" />元</span></li>
                <li><span class="pay-tips">1元=10着迷币</span></li>
                <li>
                    <span>充值方式：</span>
                    <p><label><input type="radio" checked='checked' name="支付宝" value="支付宝" /><i><img src="${URL_LIB}/static/theme/default/images/activity/zfb-icon.png" alt="支付宝" title="支付宝" /></i></label></p>
                </li>
            </ul>
            <div class="pay-box"><a class="pay-btn" href="#">立即充值</a></div>
        </div>
        <div class="pay-popup">
            <h2>对不起，着迷充值服务正在维护中~</h2>
        </div>
        <div class="set_title">
            <h3>退款</h3>
        </div>
        <p>我公司根据游戏玩家具体情况，针对玩家账户内未使用着迷币，将按照充值价格时的比例向玩家返还相应款项，网银在线支付的将于7日内将相关返款委托网银在线返还到玩家账户。同时，游戏向用户提供相应的申诉渠道，并根据相应的申诉退款处理程序处理用户的申诉。退款流程图如下： </p>
        <img width=100% src="${URL_LIB}/static/theme/default/images/activity/tklc.png" alt="退款流程图" title="退款流程图"/>
        <p>玩家若想申请退款，请通过以下途径申请退款：</p>
        <p>1、 用户提出申请退款；</p>
        <p>2、 退款申请反馈至客服邮箱；</p>
        <p>3、 用户提供充值相关信息；</p>
        <p>4、 客服人员审核用户充值信息真实有效性：</p>
        <p>I、 审核通过，则进行退款操作，退款结束。</p>
        <p>II、 若审核不通过，返回步骤3，直至退款结束。</p>
        <br>
        <p>请按照如下格式提交退款资料：</p>
        <p>游戏名称:</p>
        <p>游戏所在平台:</p>
        <p>游戏昵称:</p>
        <p>充值时间:</p>
        <p>提交充值订单号:</p>
        <p>充值账单截图等支付记录:</p>
        <p>问题详细描述:</p>
        <p>用户填写完资料后提交，并等待审核。(2到3个工作日) QQ:188012707 邮箱：service@staff.joyme.com</p>
        <p>客服收到玩家反馈，对提供的资料进行审核判定：--资料提供完整并审核通过，将充值金额退还用户，流程结束。--资料不完整审核未通过时，回复用户重新提供资料。</p>
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-basic-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
    (function(){
        $('.pay-btn').on('click',function(){
            var r=confirm("确定前往充值吗?")
            if (r==true){
                $('.pay-popup').show();
                var t=setTimeout(function(){
                    $('.pay-popup').hide();
                },3000);
            }else{
                $('.pay-popup').hide();
            }
        });
    })();
</script>
</body>
</html>
