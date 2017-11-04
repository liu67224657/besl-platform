<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="圣境,圣境传说,职业知识,知识模拟器,职业知识模拟器,天赋模拟器,职业天赋,知识点,职业加点,圣境知识,职业配点">
    <meta name="description" content="《圣境传说》职业知识模拟器，可以自由配置任意职业的职业知识。还可以查看其它玩家的配点方案和高手达人的配点建议。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>

    <title>RO3圣境传说模拟器 – 着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/css/sjmn.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/sjcs-talentcalculatorv-init.js')
    </script>
</head>
<script type="text/javascript">
    var talentObj = {tclass:'zs',total:0,addTalent:{}};
    <c:if test="${talentClass!=null && fn:length(talentClass)>0}">
    talentObj.tclass = '${talentClass}';
    <c:forEach var="talent" items="${talent}">
    talentObj.addTalent['${talent.key}'] =${talent.value};
    </c:forEach>
    </c:if>
</script>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"
      style="background:url(http://lib.joyme.com/static/theme/default/img/bg.jpg) repeat">
<!--头部开始-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<!--头部结束-->
<div class="sbgcon">
<div class="wrapper">
<a href="http://html.joyme.com/zt/sjcs.html" class="ziliao" target="_blank">圣境传说资料库</a>
<a class="zhinan" href="http://html.joyme.com/zt/jdzn/0513.html" target="_blank">职业知识配点指南</a>

<div class="top">
    <a href="${URL_WWW}/game/sjcs">RO3圣境传说讨论区</a>
    <a href="${URL_WWW}/index">返回首页</a>
</div>
<div class="bannersj"></div>
<!--banner-->
<div class="scontent">
<div class="tab">
<ul id="ul_class" class="tab_hd clearfix">
    <li id="li_g1" data-gid="g1" class="s01 <c:if test="${group=='g1'}">active</c:if>">近战防御</li>
    <li id="li_g2" data-gid="g2" class="s02 <c:if test="${group=='g2'}">active</c:if>">近战伤害</li>
    <li id="li_g3" data-gid="g3" class="s03 <c:if test="${group=='g3'}">active</c:if>">远距伤害</li>
    <li id="li_g4" data-gid="g4" class="s04 <c:if test="${group=='g4'}">active</c:if>">治疗辅助</li>
    <li id="li_g5" data-gid="g5" class="s05 <c:if test="${group=='g5'}">active</c:if>">魔法伤害</li>
</ul>
<!--tab_hd-->
<ul class="tab_bd clearfix">
<!--近战防御开始-->
<li id="tab_g1"
    style="<c:choose><c:when test="${group=='g1'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
<div class="tab01">
<ul class="tab_hd01 clearfix">
    <li id="subtab_zs" class="sn1 <c:if test="${talentClass=='zs'}">active01</c:if>" data-cname="zs">战士</li>
    <li id="subtab_qs" class="sn2 <c:if test="${talentClass=='qs'}">active01</c:if>" data-cname="qs">骑士</li>
    <li id="subtab_sqs" class="sn14 <c:if test="${talentClass=='sqs'}">active01</c:if>" data-cname="sqs">圣骑士</li>
</ul>
<ul class="tab_bd01 clearfix subtalenttab">
<!--战士开始-->
<li id="tli_zs"
    style="<c:choose><c:when test="${talentClass=='zs'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_zs" type="text" value="" class="text">职业点数<em
                    id="talent_count_zs">0</em> 已投点数<em id="talent_add_zs">0</em>
                剩余点数<em id="talent_remain_zs">0</em></div>
            <!--about-->
            <!--战士天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/11.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t1" data-tid="zs.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/21.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t4" data-tid="zs.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/31.png) no-repeat;  background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t7" data-tid="zs.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/41.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t8" data-tid="zs.t8"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/61.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t12" data-tid="zs.t12"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/71.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t15" data-tid="zs.t15"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/81.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t16" data-tid="zs.t16"></a>
                    </div>
                    <!--icon-->
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/12.png) no-repeat;background-position:0px -34px"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t2" data-tid="zs.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/22.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t5" data-tid="zs.t5"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/42.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t9" data-tid="zs.t9"></a>
                    </div>
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/51.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t11" data-tid="zs.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/62.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t13" data-tid="zs.t13"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/82.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t17" data-tid="zs.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/13.png) no-repeat;background-position:0px -34px"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t3" data-tid="zs.t3"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/23.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t6" data-tid="zs.t6"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/43.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t10" data-tid="zs.t10"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/63.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t14" data-tid="zs.t14"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/zhanshi/83.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_zs_t18" data-tid="zs.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--战士天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-zs.png">
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    战士
                </h3>

                <div class="liebiao clearfix"><span>近战</span> <span> 物理</span> <span> 攻击</span><span> 防御</span></div>
                <p>
                    钻研不同的攻击技巧，并穿着厚实铠甲，优秀的近战、防御让战士肩负冲锋、防守的角色，活跃于各种战斗场合。</p>

                <p><a href="http://www.joyme.com/note/3GRmovsZFasp0IL8vWYvNG" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3Fcj9blkta_on9kmiSS6u5" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3Fcj9blkta_on9kmiSS6u5" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说综合型战士加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/2gFEbt9QddaagMhU3EpOzp" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2gFEbt9QddaagMhU3EpOzp"
                               target="_blank">永驻灰石山</a></h4>

                        <p>防御型战士加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1l1r4QQ3Zb2qFs3xa286HI" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1l1r4QQ3Zb2qFs3xa286HI"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说战士加点：大斧型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--战士结束-->
<!--骑士开始-->
<li id="tli_qs"
    style="<c:choose><c:when test="${talentClass=='qs'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_qs" type="text" value="" class="text">职业点数<em
                    id="talent_count_qs">0</em> 已投点数<em id="talent_add_qs">0</em>剩余点数<em id="talent_remain_qs">0</em>
            </div>
            <!--about-->
            <!--骑士天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/11.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t1" data-tid="qs.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/21.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t4" data-tid="qs.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/31.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t7" data-tid="qs.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t11" data-tid="qs.t11"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/71.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t15" data-tid="qs.t15"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/12.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t2" data-tid="qs.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/22.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t5" data-tid="qs.t5"></a>
                    </div>
                    <div class="smallrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/32.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t8" data-tid="qs.t8"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/41.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t9" data-tid="qs.t9"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t13" data-tid="qs.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/72.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t16" data-tid="qs.t16"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t17" data-tid="qs.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/13.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t3" data-tid="qs.t3"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/23.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t6" data-tid="qs.t6"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/42.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t10" data-tid="qs.t10"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/52.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t12" data-tid="qs.t12"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t14" data-tid="qs.t14"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/qishi/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_qs_t18" data-tid="qs.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--骑士天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-qs.png">
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    骑士
                </h3>

                <div class="liebiao clearfix"><span>近战</span> <span>物理</span> <span>灵敏</span> <span>格挡</span></div>
                <p>讲究优雅风格，原地防守不是骑士的本性，冷静的骑士会灵巧的闪避对手攻势，并给予敌人连续的致命伤害。</p>

                <p><a href="http://www.joyme.com/note/1oU8Z_EJVaTWEGZH7XZpDr" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3rxjDN0DJ9Iay_0Hn4ERUi" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3rxjDN0DJ9Iay_0Hn4ERUi" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说单打型骑士加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1hV92cGZNehpQtqAJkc8dX" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1hV92cGZNehpQtqAJkc8dX"
                               target="_blank">永驻灰石山</a></h4>

                        <p>攻击型骑士加点 </p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3m4jV2PBp1BE-67fnsN8ti" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3m4jV2PBp1BE-67fnsN8ti"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说骑士加点：肉盾型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--骑士结束-->
<!--圣骑士开始-->
<li id="tli_sqs"
    style="<c:choose><c:when test="${talentClass=='sqs'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_sqs" type="text" value="" class="text">职业点数<em
                    id="talent_count_sqs">0</em> 已投点数<em id="talent_add_sqs">0</em>剩余点数<em id="talent_remain_sqs">0</em>
            </div>
            <!--about-->
            <!--圣骑士天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/11.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t1" data-tid="sqs.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/21.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t4" data-tid="sqs.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/31.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t7" data-tid="sqs.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t11" data-tid="sqs.t11"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t12" data-tid="sqs.t12"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t16" data-tid="sqs.t16"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/12.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t2" data-tid="sqs.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/22.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t5" data-tid="sqs.t5"></a>
                    </div>
                    <div class="smallrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/32.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t8" data-tid="sqs.t8"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/42.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t9" data-tid="sqs.t9"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t13" data-tid="sqs.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/72.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t15" data-tid="sqs.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t17" data-tid="sqs.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/13.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t3" data-tid="sqs.t3"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/23.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t6" data-tid="sqs.t6"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/43.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t10" data-tid="sqs.t10"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/63.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t14" data-tid="sqs.t14"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/sqs/83.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sqs_t18" data-tid="sqs.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--骑士天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-sqs.png">
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    圣骑士
                </h3>

                <div class="liebiao clearfix"><span>近战</span> <span>防御</span> <span>格挡</span> <span>治疗</span></div>
                <p>强化心中信仰，崇尚圣光的圣骑士，用神圣力量，捍卫周围的一切，对污秽的邪恶，给予毫不留情的审判。</p>

                <p><a href="http://www.joyme.com/note/1s9cyEQ0RbXWWlB17aParD" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3rxjDN0DJ9Iay_0Hn4ERUi" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0EPrO7Bbhb8UNnTSPhbpy-" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说自挡自补型圣骑士加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1hV92cGZNehpQtqAJkc8dX" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1Z63zG16FcDrkG1wDWY_4s"
                               target="_blank">永驻灰石山</a></h4>

                        <p>圣境传说圣骑士加点方式：盾牌型</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3m4jV2PBp1BE-67fnsN8ti" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2p_oCFoZl4uULB1XA9cuBD"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说圣骑士加点方式：防御型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--圣骑士结束-->
</ul>
</div>
</li>
<!--近战防御结束-->
<!--近战伤害开始-->
<li id="tab_g2"
    style="<c:choose><c:when test="${group=='g2'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
<div class="tab01 ">
<ul class="tab_hd01 clearfix">
    <li id="subtab_dz" class="sn3 <c:if test="${talentClass=='dz'}">active01</c:if>" data-cname="dz">盗贼</li>
    <li id="subtab_wdj" class="sn4 <c:if test="${talentClass=='wdj'}">active01</c:if>" data-cname="wdj">武斗家</li>
    <li id="subtab_wz" class="sn5 <c:if test="${talentClass=='wz'}">active01</c:if>" data-cname="wz">舞者</li>
    <li id="subtab_ws" class="sn15 <c:if test="${talentClass=='ws'}">active01</c:if>" data-cname="ws">武士</li>
</ul>
<ul class="tab_bd01 clearfix">
<!--盗贼开始-->
<li id="tli_dz"
    style="<c:choose><c:when test="${talentClass=='dz'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_dz" type="text" value="" class="text">职业点数<em
                    id="talent_count_dz">0</em> 已投点数<em id="talent_add_dz">0</em>剩余点数<em id="talent_remain_dz">0</em>
            </div>
            <!--about-->
            <!--盗贼天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t1" data-tid="dz.t1"></a>
                    </div>
                    <!--icon-->

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t4" data-tid="dz.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/31.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t6" data-tid="dz.t6"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t10" data-tid="dz.t10"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t13" data-tid="dz.t13"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t16" data-tid="dz.t16"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/12.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t2" data-tid="dz.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/22.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t5" data-tid="dz.t5"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/32.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t7" data-tid="dz.t7"></a>
                    </div>

                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/52.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t11" data-tid="dz.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t14" data-tid="dz.t14"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t17" data-tid="dz.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/13.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t3" data-tid="dz.t3"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/33.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t8" data-tid="dz.t8"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/41.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t9" data-tid="dz.t9"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/53.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t12" data-tid="dz.t12"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/71.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t15" data-tid="dz.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/daozei/83.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_dz_t18" data-tid="dz.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--盗贼天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-dz.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    盗贼
                </h3>

                <div class="liebiao clearfix"><span>近战</span> <span>物理</span> <span>潜伏</span> <span>暴击</span></div>
                <p>敏捷身手，神出鬼没的暗杀技巧，盗贼讲究速度，能给予敌人迅速的解脱，潜伏暗处，等待机会一举击杀目标。</p>

                <p><a href="http://www.joyme.com/note/28hR9diw10yHTe3YKfmnfW" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1wHeXaCfN7Eob9jNx8nx2a" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1wHeXaCfN7Eob9jNx8nx2a" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说攻速型盗贼加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/39Ua2i8UF4irjktt1dM1Xo" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/39Ua2i8UF4irjktt1dM1Xo"
                               target="_blank">永驻灰石山</a></h4>

                        <p>攻闪型盗贼加点.</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3D2CX8yzheNVQhnkGNcJ-c" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3D2CX8yzheNVQhnkGNcJ-c"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说盗贼加点：暴击型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--盗贼结束-->
<!--武斗家开始-->
<li id="tli_wdj"
    style="<c:choose><c:when test="${talentClass=='wdj'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_wdj" type="text" value="" class="text">职业点数<em
                    id="talent_count_wdj">0</em> 已投点数<em id="talent_add_wdj">0</em>剩余点数<em id="talent_remain_wdj">0</em>
            </div>
            <!--about-->
            <!--武斗家天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/11.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t1" data-tid="wdj.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t4" data-tid="wdj.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/31.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t7" data-tid="wdj.t7"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t11" data-tid="wdj.t11"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t13" data-tid="wdj.t13"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t2" data-tid="wdj.t2"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/22.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t5" data-tid="wdj.t5"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/32.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t8" data-tid="wdj.t8"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/52.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t12" data-tid="wdj.t12"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t14" data-tid="wdj.t14"></a>
                    </div>

                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/71.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t15" data-tid="wdj.t15"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t17" data-tid="wdj.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t3" data-tid="wdj.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/23.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t6" data-tid="wdj.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/33.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t9" data-tid="wdj.t9"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/41.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t10" data-tid="wdj.t10"></a>
                    </div>
                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/72.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t16" data-tid="wdj.t16"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wudou/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wdj_t18" data-tid="wdj.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--武斗家天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-wdj.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    武斗家
                </h3>

                <div class="liebiao clearfix"><span>物理</span> <span>攻击</span> <span>体能</span> <span> 武道</span></div>
                <p>致力于躯体锻炼，将武术升华到极致，武斗家的拳头就是致命武器，在武斗家面前，任何防御都是毫无意义的。</p>

                <p><a href="http://www.joyme.com/note/2JC4bBW3l8XqNxtR6EcW1r" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3fF9ZCYAV32XPhfWdLu1mf" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3fF9ZCYAV32XPhfWdLu1mf" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说血伤型武斗家加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0g-NIYB0d69WX5AIfMjzse" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0g-NIYB0d69WX5AIfMjzse"
                               target="_blank">永驻灰石山</a></h4>

                        <p>伤害型武斗家加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3yIwHQ_c97jbnwMtJtghgj" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3yIwHQ_c97jbnwMtJtghgj"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说武斗家加点：暴击型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--武斗家结束-->
<!--舞者开始-->
<li id="tli_wz"
    style="<c:choose><c:when test="${talentClass=='wz'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_wz" type="text" value="" class="text">职业点数<em
                    id="talent_count_wz">0</em> 已投点数<em id="talent_add_wz">0</em>剩余点数<em id="talent_remain_wz">0</em>
            </div>
            <!--about-->
            <!--舞者天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/11.png) no-repeat;  background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t1" data-tid="wz.t1"></a>
                    </div>
                    <!--icon-->

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t4" data-tid="wz.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/31.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t7" data-tid="wz.t7"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/41.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t9" data-tid="wz.t9"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t11" data-tid="wz.t11"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/71.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t15" data-tid="wz.t15"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/12.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t2" data-tid="wz.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/22.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t5" data-tid="wz.t5"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/42.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t10" data-tid="wz.t10"></a>
                    </div>
                    <div class="smallrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/52.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t12" data-tid="wz.t12"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t14" data-tid="wz.t14"></a>
                    </div>
                    <div class="bigrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t17" data-tid="wz.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t3" data-tid="wz.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/23.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t6" data-tid="wz.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/32.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t8" data-tid="wz.t8"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/53.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t13" data-tid="wz.t13"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/72.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t16" data-tid="wz.t16"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuzhe/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wz_t18" data-tid="wz.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--舞者天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-wz.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    舞者
                </h3>

                <div class="liebiao clearfix"><span>物理</span> <span> 攻击</span> <span> 体能</span> <span> 舞蹈</span></div>
                <p>拥有曼妙身材的舞者，就算在残酷的战场上，也能舞出华丽的舞蹈。当所有人的情绪都被舞者的律动牵引时，舞者手中的剑，将神不知鬼不觉的夺去对手的生命。</p>

                <p><a href="http://www.joyme.com/note/3CM3d0w5R3hXqL07OdRDJT" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0B3GEd4Pl0kUWjeTSGKVD_" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0B3GEd4Pl0kUWjeTSGKVD_" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说攻闪型舞者加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3bqodRwuR9dqqjJTAce3Ub" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3bqodRwuR9dqqjJTAce3Ub"
                               target="_blank">永驻灰石山</a></h4>

                        <p>攻击型舞者加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0r9mCjPJ9eF8pqgT5KDm-d" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0r9mCjPJ9eF8pqgT5KDm-d"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说舞者加点：暴击型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--舞者结束-->
<!--武士开始-->
<li id="tli_ws"
    style="<c:choose><c:when test="${talentClass=='ws'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_ws" type="text" value="" class="text">职业点数<em
                    id="talent_count_ws">0</em> 已投点数<em id="talent_add_ws">0</em>剩余点数<em id="talent_remain_ws">0</em>
            </div>
            <!--about-->
            <!--武士天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/11.png) no-repeat;  background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t1" data-tid="ws.t1"></a>
                    </div>
                    <!--icon-->

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t4" data-tid="ws.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/41.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t9" data-tid="ws.t9"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t11" data-tid="ws.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t13" data-tid="ws.t13"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t16" data-tid="ws.t16"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/12.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t2" data-tid="ws.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/22.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t5" data-tid="ws.t5"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/31.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t7" data-tid="ws.t7"></a>
                    </div>
                    <div class="icon  mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/52.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t12" data-tid="ws.t12"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/71.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t15" data-tid="ws.t15"></a>
                    </div>
                    <div class="smallrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t17" data-tid="ws.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t3" data-tid="ws.t3"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/23.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t6" data-tid="ws.t6"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/32.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t8" data-tid="ws.t8"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/42.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t10" data-tid="ws.t10"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t14" data-tid="ws.t14"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wushi/83.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_ws_t18" data-tid="ws.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--武士天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-ws.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    武士
                </h3>

                <div class="liebiao clearfix"><span>近战</span> <span> 物理</span> <span> 攻击</span> <span> 爆击</span></div>
                <p>擅长使用太刀的职业，利用各种劈、斩对敌人造成强力攻击，不仅能够造成单点的强突破，还能进行线性攻击及范围攻击。</p>

                <p><a href="http://www.joyme.com/note/1KCDTeYehaGEN2Zh09iNOi" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3557euM0x4ZqxhHwMpi4zY" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3557euM0x4ZqxhHwMpi4zY" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说暴击型武士加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0n1SeZnu1bbpz0wkF20FKm" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0n1SeZnu1bbpz0wkF20FKm"
                               target="_blank">永驻灰石山</a></h4>

                        <p>攻速型武士加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/20rqrfFR57Q8H7gaoS3H-6" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/20rqrfFR57Q8H7gaoS3H-6"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说武士加点：范围攻击型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--武士结束-->
</ul>
</div>
</li>
<!--近战伤害结束-->

<!--远距伤害开始-->
<li id="tab_g3"
    style="<c:choose><c:when test="${group=='g3'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
<div class="tab01 ">
<ul class="tab_hd01 clearfix">
    <li id="subtab_lr" class="sn6 <c:if test="${talentClass=='lr'}">active01</c:if>" data-cname="lr">猎人</li>
    <li id="subtab_gcs" class="sn7 <c:if test="${talentClass=='gcs'}">active01</c:if>" data-cname="gcs">工程师</li>
    <li id="subtab_yx" class="sn16 <c:if test="${talentClass=='yx'}">active01</c:if>" data-cname="yx">游侠</li>
</ul>
<ul class="tab_bd01 clearfix">
<!--猎人开始-->
<li id="tli_lr"
    style="<c:choose><c:when test="${talentClass=='lr'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_lr" type="text" value="" class="text">职业点数<em
                    id="talent_count_lr">0</em> 已投点数<em id="talent_add_lr">0</em>剩余点数<em id="talent_remain_lr">0</em>
            </div>
            <!--about-->
            <!--猎人天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/11.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t1" data-tid="lr.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/21.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t4" data-tid="lr.t4"></a>
                    </div>


                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/41.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t9" data-tid="lr.t9"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/51.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t10" data-tid="lr.t10"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/61.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t12" data-tid="lr.t12"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/81.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t16" data-tid="lr.t16"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/12.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t2" data-tid="lr.t2"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/22.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t5" data-tid="lr.t5"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/31.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t7" data-tid="lr.t7"></a>
                    </div>

                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/52.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t11" data-tid="lr.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t13" data-tid="lr.t13"></a>
                    </div>


                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/71.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t15" data-tid="lr.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t17" data-tid="lr.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/13.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t3" data-tid="lr.t3"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/23.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t6" data-tid="lr.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/32.png) no-repeat; background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t8" data-tid="lr.t8"></a>
                    </div>
                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/63.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t14" data-tid="lr.t14"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/lieren/83.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_lr_t18" data-tid="lr.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--猎人天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-lr.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    猎人
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 攻击</span> <span> 召唤</span> <span> 暴击</span></div>
                <p>与猎虎合作无间的进行作战，猎人擅长牵制住对手后，再给予致命一击，被盯上的猎物只能等待死亡。</p>

                <p><a href="http://www.joyme.com/note/02pOQ1t7B7ibt34bpqSF6p" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0w48tSP-pe48DYVrfamzfM" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0w48tSP-pe48DYVrfamzfM" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说战斗型猎人加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1a-FAwrQF0MUYm9ThNYZIT" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1a-FAwrQF0MUYm9ThNYZIT"
                               target="_blank">永驻灰石山</a></h4>

                        <p>爆击型猎人加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1CNDMw_c57MEjJ1yNv1dzL" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1CNDMw_c57MEjJ1yNv1dzL"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说猎人加点：稳伤型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--猎人结束-->
<!--工程师开始-->
<li id="tli_gcs"
    style="<c:choose><c:when test="${talentClass=='gcs'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_gcs" type="text" value="" class="text">职业点数<em
                    id="talent_count_gcs">0</em> 已投点数<em id="talent_add_gcs">0</em>剩余点数<em id="talent_remain_gcs">0</em>
            </div>
            <!--about-->
            <!--战士天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t1" data-tid="gcs.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t4" data-tid="gcs.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/31.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t7" data-tid="gcs.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t8" data-tid="gcs.t8"></a>
                    </div>
                    <!--icon-->

                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t12" data-tid="gcs.t12"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t16" data-tid="gcs.t16"></a>
                    </div>
                    <!--icon-->
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t2" data-tid="gcs.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/22.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t5" data-tid="gcs.t5"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/42.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t9" data-tid="gcs.t9"></a>
                    </div>
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t11" data-tid="gcs.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/62.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t13" data-tid="gcs.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t15" data-tid="gcs.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t17" data-tid="gcs.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t3" data-tid="gcs.t3"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/23.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t6" data-tid="gcs.t6"></a>
                    </div>

                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/43.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t10" data-tid="gcs.t10"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/63.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t14" data-tid="gcs.t14"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/eng/83.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_gcs_t18" data-tid="gcs.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--战士天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-gcs.png">
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    工程师
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 攻击</span> <span> 变身</span> <span> 爆破</span></div>
                <p>工程师对科学有极度偏执，他会用炸弹一类的科学产物，展现骇人的战斗风格，在烟硝味过后，敌人将体无完肤。</p>

                <p><a href="http://www.joyme.com/note/3uIcdITox7ZqW95XiHEczz" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/2U4nJIHE94YpyEgVxl6_1e" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2U4nJIHE94YpyEgVxl6_1e" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说战斗型工程师加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1vJuhpyB1dWqXeC3qQw1qr" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1vJuhpyB1dWqXeC3qQw1qr"
                               target="_blank">永驻灰石山</a></h4>

                        <p>火炮型工程师加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3YbmnW8UF3fFJNpjAONDEz" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3YbmnW8UF3fFJNpjAONDEz"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说工程师加点：攻防一体</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--工程师结束-->
<!--游侠开始-->
<li id="tli_yx"
    style="<c:choose><c:when test="${talentClass=='yx'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_yx" type="text" value="" class="text">职业点数<em
                    id="talent_count_yx">0</em> 已投点数<em id="talent_add_yx">0</em>剩余点数<em id="talent_remain_yx">0</em>
            </div>
            <!--about-->
            <!--游侠天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t1" data-tid="yx.t1"></a>
                    </div>
                    <div class="smallrow"></div>
                    <!--icon-->
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t4" data-tid="yx.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/31.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t6" data-tid="yx.t6"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t10" data-tid="yx.t10"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t16" data-tid="yx.t16"></a>
                    </div>
                    <!--icon-->
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t2" data-tid="yx.t2"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/32.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t7" data-tid="yx.t7"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/42.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t9" data-tid="yx.t9"></a>
                    </div>
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/52.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t11" data-tid="yx.t11"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/62.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t13" data-tid="yx.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/72.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t15" data-tid="yx.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t17" data-tid="yx.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t3" data-tid="yx.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/23.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t5" data-tid="yx.t5"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/33.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t8" data-tid="yx.t8"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/53.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t12" data-tid="yx.t12"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/63.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t14" data-tid="yx.t14"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/yx/83.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yx_t18" data-tid="yx.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--游侠天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-yx.png">
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    游侠
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 物理</span> <span> 攻击</span> <span> 爆击</span></div>
                <p>为了维护正义，不惜挺身对抗邪恶的游侠。独来独往的他会用灵敏的身手，快速寻找射击位置，他的敌人将无处可逃。</p>

                <p><a href="http://www.joyme.com/note/17VaBtQD59OGi5D82uP3_X" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0BLIHmzhRdtWR3rNjuU_5e" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0BLIHmzhRdtWR3rNjuU_5e" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说战斗型工程师加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1p-7vfSnZ2C97SuiYUk4ed" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1p-7vfSnZ2C97SuiYUk4ed"
                               target="_blank">永驻灰石山</a></h4>

                        <p>火炮型工程师加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/2m0K3J_0t5vH5UtnmoLqBL" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2m0K3J_0t5vH5UtnmoLqBL"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说工程师加点：攻防一体</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--游侠结束-->
</ul>
</div>
</li>
<!--远距伤害结束-->

<!--治疗辅助系开始-->
<li id="tab_g4"
    style="<c:choose><c:when test="${group=='g4'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
<div class="tab01 ">
<ul class="tab_hd01 clearfix">
    <li id="subtab_sg" class="sn8 <c:if test="${talentClass=='sg'}">active01</c:if>" data-cname="sg">神官</li>
    <li id="subtab_yysr" class="sn9 <c:if test="${talentClass=='yysr'}">active01</c:if>" data-cname="yysr">吟游诗人</li>
    <li id="subtab_wy" class="sn10 <c:if test="${talentClass=='wy'}">active01</c:if>" data-cname="wy">巫医</li>
</ul>
<ul class="tab_bd01 clearfix">
<!--神官开始-->
<li id="tli_sg"
    style="<c:choose><c:when test="${talentClass=='sg'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_sg" type="text" value="" class="text">职业点数<em
                    id="talent_count_sg">0</em> 已投点数<em id="talent_add_sg">0</em>剩余点数<em id="talent_remain_sg">0</em>
            </div>
            <!--about-->
            <!--战士天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t1" data-tid="sg.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t4" data-tid="sg.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/31.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t7" data-tid="sg.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t8" data-tid="sg.t8"></a>
                    </div>
                    <!--icon-->

                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t12" data-tid="sg.t12"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t17" data-tid="sg.t17"></a>
                    </div>
                    <!--icon-->
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t2" data-tid="sg.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/22.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t5" data-tid="sg.t5"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/42.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t9" data-tid="sg.t9"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t10" data-tid="sg.t10"></a>
                    </div>
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/62.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t13" data-tid="sg.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t15" data-tid="sg.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t18" data-tid="sg.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t3" data-tid="sg.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/23.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t6" data-tid="sg.t6"></a>
                    </div>
                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/52.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t11" data-tid="sg.t11"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/63.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t14" data-tid="sg.t14"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shenguan/72.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_sg_t16" data-tid="sg.t16"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--战士天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-sg.png">
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    神官
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 魔法</span> <span> 净化</span> <span> 治疗</span></div>
                <p>信仰光明，神官运用神圣恩典，治愈抚慰一切生命，当邪秽意念横行，神官会给予这一切圣洁的净化与庇佑。</p>

                <p><a href="http://cleuse.joyme.com/note/1r5WQRDCN75UldV11WsDOe"
                      target="_blank">查看此职业的详细资料及玩家心得</a></p>
            </div>
            <!--list-->
        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1jpR9l7zZa0aXEB7RlSxJ7" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1jpR9l7zZa0aXEB7RlSxJ7" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说安全型神官加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3kV17E8sNfEW3DbMTtu0iT" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3kV17E8sNfEW3DbMTtu0iT"
                               target="_blank">永驻灰石山</a></h4>

                        <p>急速治疗型神官加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0FLduA8Y9fnazLS_AGjP84" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0FLduA8Y9fnazLS_AGjP84"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说神官加点：治疗型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--神官结束-->
<!--吟游诗人开始-->
<li id="tli_yysr"
    style="<c:choose><c:when test="${talentClass=='yysr'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_yysr" type="text" value="" class="text">职业点数<em
                    id="talent_count_yysr">0</em> 已投点数<em id="talent_add_yysr">0</em>剩余点数<em
                    id="talent_remain_yysr">0</em></div>
            <!--about-->
            <!--吟游诗人天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t1" data-tid="yysr.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t4" data-tid="yysr.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/31.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t7" data-tid="yysr.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t11" data-tid="yysr.t11"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t14" data-tid="yysr.t14"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t17" data-tid="yysr.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t2" data-tid="yysr.t2"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/22.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t5" data-tid="yysr.t5"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/32.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t8" data-tid="yysr.t8"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t9" data-tid="yysr.t9"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t12" data-tid="yysr.t12"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/72.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t15" data-tid="yysr.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t18" data-tid="yysr.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t3" data-tid="yysr.t3"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/23.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t6" data-tid="yysr.t6"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/42.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t10" data-tid="yysr.t10"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/62.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t13" data-tid="yysr.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/shiren/73.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_yysr_t16" data-tid="yysr.t16"></a>
                    </div>

                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--吟游诗人天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-yysr.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    吟游诗人
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 魔法</span> <span> 辅助</span> <span> 团队</span></div>
                <p>自由自在的吟游诗人，利用节奏感染周遭一切，随着音符起伏，一切情绪，都将影响战斗进行。</p>

                <p><a href="http://www.joyme.com/note/3n9IdXDup5HaP41oe4Z859" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk" target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/2EV9wQ5aF89HFB17h0-L9W" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2EV9wQ5aF89HFB17h0-L9W" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说平均型吟游诗人加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1OhO1qjd92XbOkWN6OIy1N" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/1OhO1qjd92XbOkWN6OIy1N"
                               target="_blank">永驻灰石山</a></h4>

                        <p>强攻型吟游诗人加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/39dlX2yqF7Wa_jIN6GBcwM" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/39dlX2yqF7Wa_jIN6GBcwM"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说吟游诗人加点：均衡型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--吟游诗人结束-->
<!--巫医开始-->
<li id="tli_wy"
    style="<c:choose><c:when test="${talentClass=='wy'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_wy" type="text" value="" class="text">职业点数<em
                    id="talent_count_wy">0</em> 已投点数<em id="talent_add_wy">0</em>剩余点数<em id="talent_remain_wy">0</em>
            </div>
            <!--about-->
            <!--巫医天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t1" data-tid="wy.t1"></a>
                    </div>
                    <!--icon-->

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t4" data-tid="wy.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/31.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t7" data-tid="wy.t7"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t11" data-tid="wy.t11"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t15" data-tid="wy.t15"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t2" data-tid="wy.t2"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/22.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t5" data-tid="wy.t5"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/32.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t8" data-tid="wy.t8"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t10" data-tid="wy.t10"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/52.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t12" data-tid="wy.t12"></a>
                    </div>
                    <div class="smallrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t14" data-tid="wy.t14"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/72.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t16" data-tid="wy.t16"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t17" data-tid="wy.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t3" data-tid="wy.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/23.png) no-repeat; background-position:0px -34px"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t6" data-tid="wy.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/33.png) no-repeat; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t9" data-tid="wy.t9"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/53.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t13" data-tid="wy.t13"></a>
                    </div>
                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/wuyi/82.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_wy_t18" data-tid="wy.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--巫医天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-wy.png" width="104" height="126"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    巫医
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 魔法</span> <span> 净化</span> <span> 治疗</span></div>
                <p>透过祈求仪式，巫医获得从祖灵而来的强大力量，让受伤的生命得以康复。当他吹响号角，强大的森林巨灵会响应他的呼唤，听从巫医的号令。</p>

                <p><a href="http://www.joyme.com/note/0jZD23GrpbyWpiqjNZwvGB" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk"  target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/192vBc36V6wXw-v3rOr1uD" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/192vBc36V6wXw-v3rOr1uD" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说均衡型巫医加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/04oJaXwfx4Lr2gCBN5bn31" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/04oJaXwfx4Lr2gCBN5bn31"
                               target="_blank">永驻灰石山</a></h4>

                        <p>伤害型巫医加点 </p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/2WZJ96A_laO9jIpAw85LHn" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2WZJ96A_laO9jIpAw85LHn"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说巫医加点：治疗型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--巫医结束-->
</ul>
</div>
</li>
<!--治疗辅助系结束-->

<!--魔法伤害开始-->
<li id="tab_g5"
    style="<c:choose><c:when test="${group=='g5'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
<div class="tab01 ">
<ul class="tab_hd01 clearfix">
    <li id="subtab_mss" class="sn11 <c:if test="${talentClass=='mss'}">active01</c:if>" data-cname="mss">魔术师</li>
    <li id="subtab_hss" class="sn12 <c:if test="${talentClass=='hss'}">active01</c:if>" data-cname="hss">幻术师</li>
    <li id="subtab_slfs" class="sn13 <c:if test="${talentClass=='slfs'}">active01</c:if>" data-cname="slfs">死灵法师</li>
</ul>
<ul class="tab_bd01 clearfix">
<!--魔术师开始-->
<li id="tli_mss"
    style="<c:choose><c:when test="${talentClass=='mss'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_mss" type="text" value="" class="text">职业点数<em
                    id="talent_count_mss">0</em> 已投点数<em id="talent_add_mss">0</em>剩余点数<em id="talent_remain_mss">0</em>
            </div>
            <!--about-->
            <!--舞者天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t1" data-tid="mss.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t4" data-tid="mss.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/31.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t7" data-tid="mss.t7"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t10" data-tid="mss.t10"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t15" data-tid="mss.t15"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t17" data-tid="mss.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/12.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t2" data-tid="mss.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/22.png) no-repeat;background-position:0px -34px;  "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t5" data-tid="mss.t5"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t9" data-tid="mss.t9"></a>
                    </div>
                    <div class="smallrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/52.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t11" data-tid="mss.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t13" data-tid="mss.t13"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t18" data-tid="mss.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t3" data-tid="mss.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/23.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t6" data-tid="mss.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/32.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t8" data-tid="mss.t8"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/53.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t12" data-tid="mss.t12"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/62.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t14" data-tid="mss.t14"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/moshu/72.png) no-repeat; background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_mss_t16" data-tid="mss.t16"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--舞者天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-mss.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    魔术师
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 魔法</span> <span> 元素</span> <span> 群攻</span></div>
                <p>魔术师善于使用本身的魔法能量，重新聚集四散的元素能量。无论是将敌人禁锢在寒冰之中，或是使用火焰烧尽眼前所有敌人，让魔术师成为战场上最令人畏惧的对手。</p>

                <p><a href="http://www.joyme.com/note/2TWt6zSEdavVAt73kf59jy" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk"  target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3rieI3wAN9wULNm035Zomb" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3rieI3wAN9wULNm035Zomb" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说施速型魔术师加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/15UUWFo9VdnqyHFpnZw8m5" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/15UUWFo9VdnqyHFpnZw8m5"
                               target="_blank">永驻灰石山</a></h4>

                        <p>强力输出型魔术师加点 </p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/1UTFsCp2dcjrtaUTaR6tmX" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="#http://www.joyme.com/note/1UTFsCp2dcjrtaUTaR6tmX"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说魔术师加点：稳定输出型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--魔术师结束-->
<!--幻术师开始-->
<li id="tli_hss"
    style="<c:choose><c:when test="${talentClass=='hss'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_hss" type="text" value="" class="text">职业点数<em
                    id="talent_count_hss">0</em> 已投点数<em id="talent_add_hss">0</em>剩余点数<em id="talent_remain_hss">0</em>
            </div>
            <!--about-->
            <!--幻术师天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/11.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t1" data-tid="hss.t1"></a>
                    </div>
                    <!--icon-->
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/21.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t4" data-tid="hss.t4"></a>
                    </div>
                    <!--icon-->
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/31.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t7" data-tid="hss.t7"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t9" data-tid="hss.t9"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t13" data-tid="hss.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t15" data-tid="hss.t15"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/12.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t2" data-tid="hss.t2"></a>
                    </div>

                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/22.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t5" data-tid="hss.t5"></a>
                    </div>
                    <div class="bigrow">
                    </div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/42.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t10" data-tid="hss.t10"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t11" data-tid="hss.t11"></a>
                    </div>
                    <div class="smallrow"></div>

                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/62.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t14" data-tid="hss.t14"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/72.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t16" data-tid="hss.t16"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t17" data-tid="hss.t17"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/13.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t3" data-tid="hss.t3"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/23.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t6" data-tid="hss.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/32.png) no-repeat;background-position:0px -34px;"></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t8" data-tid="hss.t8"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/52.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t12" data-tid="hss.t12"></a>
                    </div>

                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/huanshu/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_hss_t18" data-tid="hss.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--幻术师天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-hss.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    幻术师
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 魔法</span> <span> 催眠</span> <span> 控制</span></div>
                <p>利用催眠，激发潜能、幻惑敌人，这就是神秘的幻术师，他的奇幻之术，能让遗迹巨人，为捍卫他而苏醒。</p>

                <p><a href="http://www.joyme.com/note/3oKa-0ymR0REfUNoXIHRpK" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk"  target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/3qWQjbjxF6pUMinfwpBPDO" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/3qWQjbjxF6pUMinfwpBPDO" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说速度型幻术师加点方式</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/34rmmdmyd3xoSomicDMI9S" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/34rmmdmyd3xoSomicDMI9S"
                               target="_blank">永驻灰石山</a></h4>

                        <p>爆击型幻术师加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/2f6-OMPwl52FSAb5Ur9XJV" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/2f6-OMPwl52FSAb5Ur9XJV"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说幻术师加点：标准型 </p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--幻术师结束-->
<!--死灵法师开始-->
<li id="tli_slfs"
    style="<c:choose><c:when test="${talentClass=='slfs'}">display:block</c:when><c:otherwise>display:none</c:otherwise></c:choose>">
    <div class="leftcon clearfix">
        <div class="left clearfix">
            <div class="caozuo">
                <a href="javascript:void(0)" class="chongzhi" name="recover_talent">重置点数</a>
                <a href="javascript:void(0)" class="fuzhi" name="copy_talent">复制链接</a>
            </div>
            <!--caozuo-->
            <div class="about">职业等级<input id="level_input_slfs" type="text" value="" class="text">职业点数<em
                    id="talent_count_slfs">0</em> 已投点数<em id="talent_add_slfs">0</em>剩余点数<em
                    id="talent_remain_slfs">0</em></div>
            <!--about-->
            <!--死灵法师天赋树开始-->
            <div class="sn_tree">
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/11.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t1" data-tid="slfs.t1"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/21.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t4" data-tid="slfs.t4"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/31.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t7" data-tid="slfs.t7"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/41.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t9" data-tid="slfs.t9"></a>
                    </div>
                    <div class="icon mt122">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/71.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t15" data-tid="slfs.t15"></a>
                    </div>
                </div>
                <!--tree_left-->
                <div class="tree_left">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/12.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t2" data-tid="slfs.t2"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/22.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t5" data-tid="slfs.t5"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon ">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/42.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t10" data-tid="slfs.t10"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/51.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t11" data-tid="slfs.t11"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/61.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t13" data-tid="slfs.t13"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/72.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t16" data-tid="slfs.t16"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/81.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t17" data-tid="slfs.t17"></a>
                    </div>

                </div>
                <!--tree_left-->
                <div class="tree_left" style="margin-right:0px;">
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/13.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t3" data-tid="slfs.t3"></a>
                    </div>
                    <div class="smallrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/23.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t6" data-tid="slfs.t6"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/32.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t8" data-tid="slfs.t8"></a>
                    </div>
                    <div class="icon mt68">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/52.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t12" data-tid="slfs.t12"></a>
                    </div>
                    <div class="icon mt14">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/62.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t14" data-tid="slfs.t14"></a>
                    </div>
                    <div class="bigrow"></div>
                    <div class="icon">
                        <ins style="background:url(${URL_LIB}/static/img/sjcstalent/siling/82.png) no-repeat;background-position:0px -34px; "></ins>
                        <a href="javascript:void(0)" class="hover" id="link_talent_slfs_t18" data-tid="slfs.t18"></a>
                    </div>
                </div>
                <!--tree_left-->
            </div>
            <!--sn_tree-->
            <!--死灵法师天赋树结束-->
        </div>
        <div class="fenxiang clearfix">分享到：
            <a href="javascript:void(0);" class="sina" title="同步到新浪微博" name="share_sina">新浪微博</a>
            <a href="javascript:void(0);" class="qq" title="同步到腾讯微博" name="share_qq">腾讯微博</a>
            <a href="javascript:void(0);" name="share_joyme" class="zhaomi" title="同步到着迷网">着迷网</a>
        </div>
    </div>
    <div class="right">
        <div class="rightcon clearfix">
            <div class="renwu">
                <img src="${URL_LIB}/static/img/sjcstalent/headicon-slfs.png"/>
            </div>
            <!--renwu-->
            <div class="list">
                <h3 class="clearfix">
                    死灵法师
                </h3>

                <div class="liebiao clearfix"><span>远程</span> <span> 魔法</span> <span> 召唤</span> <span> 暴击</span></div>
                <p>借用黑暗力量,不畏亵渎亡者的罪名,签订禁忌契约的死灵法师,他会让敌人彻底了解来自深渊的恐怖。</p>

                <p><a href="http://www.joyme.com/note/19FvN7qeZ9pp4wqpLdU25j" target="_blank">查看此职业的详细资料及玩家心得</a>
                </p>
            </div>
            <!--list-->

        </div>
        <!--rightcon-->
        <div class="rightarea clearfix">
            <div class="right_bd">
                <a href="http://www.joyme.com/game/sjcs/talk"  target="_blank"
                   class="see_other">看看其他人都是怎么加的</a>
                <h4>看达人的知识加点教学</h4>

                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0SepO0IJlflofNf3s5GLe0" target="_blank"><img
                                src="http://r002.joyme.com/r002/headicon/2012/04/9/0B2A5DCABB903CCE8085EB3F5E8D6C10_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0SepO0IJlflofNf3s5GLe0" target="_blank">大宫毅</a>
                        </h4>

                        <p>圣境传说战斗型死灵法师加点方式 </p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0fuBHqKuR4f9PHi0VDm9Xr" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/47/C05684EB2E9CD445E50F2672FA626B71_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0fuBHqKuR4f9PHi0VDm9Xr"
                               target="_blank">永驻灰石山</a></h4>

                        <p>爆击型死灵法师加点</p>
                    </div>
                </div>
                <!--sjlist-->
                <div class="sjlist noborder clearfix">
                    <div class="sj_face01">
                        <a href="http://www.joyme.com/note/0Kxfh4GVZatGNUxstfhcVE" target="_blank">
                            <img src="http://r002.joyme.com/r002/headicon/2012/04/68/B2343D2B0DA788E130A59FEA66969CB5_S.jpg"></a>
                    </div>
                    <div class="sj_con">
                        <h4><a href="http://www.joyme.com/note/0Kxfh4GVZatGNUxstfhcVE"
                               target="_blank">圣辉者</a></h4>

                        <p>圣境传说死灵法师加点：均衡型</p>
                    </div>
                </div>
                <!--sjlist-->
            </div>
        </div>
        <!--rightarea-->
    </div>
    <!--right-->
</li>
<!--死灵法师结束-->


</ul>
</div>
</li>
<!--魔法伤害结束-->
</ul>
<!--tab_bd-->
</div>
<div class="sjmnfooter clearfix">
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
<!--sjmnfooter-->
</div>
<!--scontent-->
</div>
<!--wrapper-->
</div>
<!--sbgcon-->
</body>


</html>