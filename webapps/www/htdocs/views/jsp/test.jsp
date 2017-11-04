<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html>

<head>
    <title>Insert title here</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="wrapper">
<div class="con">
    <div class="con_hd"></div>
    <div class="con_area  con_pd01 clearfix">
        <h3 class="con_hn">圣境传说</h3>

        <div class="stab_hd clearfix">
            <ul class="stablist">
                <li><a href="#"><span>封面</span></a></li>
                <li><a href="#"><span>小组</span></a></li>
                <li><a href="#"><span>精华区</span></a></li>
                <li><a href="#"><span>资料区</span></a></li>
                <li><a href="#" class="on"><span>攻略百科</span></a></li>
            </ul>
        </div>
        <!--stab_hd-->
        <div class="stab_bd">
            <div class="baike clearfix">
                <div class="tit_baike">
                    <h3>圣境传说攻略百科<a href="#" class="edit_baike">编辑百科</a></h3>
                </div>
                <div class="bk_btn" id="baike_nav">
                    <a href="javascript:void(0)" class="add_bk" id="add_baike_one">添加一级标题</a>
                    <a href="#" class="save_bk" id="save_baike">保存修改</a>
                </div>
                <%--<div class="bk_con  mt10">--%>
                    <%--<div class="bk_hd">--%>
                        <%--新手指南--%>
                 <%--<span class="operate_bk"> <a class="bk_delete" id="del_one_item" href="javascript:void(0)" title="删除"></a>--%>
                   <%--<a class="bk_editxt" href="#" title="编辑"></a></span>--%>
                   <%--<span class="dengji">--%>
                     <%--<a href="#" class="two_bk"><i></i>二级</a>--%>
                     <%--<a href="#" class="three_bk"><i></i>三级</a>--%>
                   <%--</span>--%>
                    <%--</div>--%>
                    <%--<div class="bk_bd bk_pd">--%>
                    <%--<span class="bk_list on"> <a href="#">技能系统</a> <span class="operate_bk"> <a class="bk_delete"--%>
                                                                                                <%--href="#" title="删除"></a>--%>
                   <%--<a class="bk_editxt" href="#" title="编辑"></a></span>    </span>--%>
                        <%--<span class="bk_list">  <a href="#">工匠系统</a> </span>--%>
                        <%--<span class="bk_list"> <a href="#">符文系统</a> </span>--%>
                        <%--<span class="bk_list"><a href="#">血球系统 </a></span>--%>
                        <%--<span class="bk_list"> <a href="#">遭遇系统</a></span>--%>

                    <%--</div>--%>
                    <%--<!--bk_bd-->--%>
                <%--</div>--%>
                <%--<!--bk_con-->--%>
                <%--<div class="bk_con  mt17">--%>
                    <%--<div class="bk_hd">--%>
                        <%--职业与技能--%>
                 <%--<span class="operate_bk"> <a class="bk_delete" href="#" title="删除"></a>--%>
                   <%--<a class="bk_editxt" href="#" title="编辑"></a></span>--%>
                   <%--<span class="dengji">--%>
                     <%--<a href="#" class="two_bk"><i></i>二级</a>--%>
                     <%--<a href="#" class="three_bk"><i></i>三级</a>--%>
                   <%--</span>--%>
                    <%--</div>--%>
                    <%--<div class="bk_bd bk_pd01">--%>
                        <%--<div class="bk_bd_hd">--%>

                            <%--野蛮人--%>
                 <%--<span class="operate_bk"> <a class="bk_delete" href="#" title="删除"></a>--%>
                   <%--<a class="bk_editxt" href="#" title="编辑"></a></span>--%>
                   <%--<span class="dengji">--%>
                     <%--<a href="#" class="three_bk"><i></i>三级</a>--%>
                   <%--</span>--%>

                        <%--</div>--%>
                        <%--<div class="bk_bd_bd">--%>
                        <%--<span class="bk_list"> <a href="#">技能系统</a> <span class="operate_bk"> <a class="bk_delete"--%>
                                                                                                 <%--href="#"--%>
                                                                                                 <%--title="删除"></a>--%>
                       <%--<a class="bk_editxt" href="#" title="编辑"></a></span>    </span>--%>
                            <%--<span class="bk_list">  <a href="#">工匠系统</a> </span>--%>
                            <%--<span class="bk_list"> <a href="#">符文系统</a> </span>--%>
                            <%--<span class="bk_list"><a href="#">血球系统 </a></span>--%>
                            <%--<span class="bk_list"> <a href="#">遭遇系统</a></span>--%>
                         <%--<span class="bk_list"> <a href="#">技能系统</a> <span class="operate_bk"> <a class="bk_delete"--%>
                                                                                                  <%--href="#"--%>
                                                                                                  <%--title="删除"></a>--%>
                       <%--<a class="bk_editxt" href="#" title="编辑"></a></span>    </span>--%>
                            <%--<span class="bk_list">  <a href="#">工匠系统</a> </span>--%>
                            <%--<span class="bk_list"> <a href="#">符文系统</a> </span>--%>
                            <%--<span class="bk_list"><a href="#">血球系统 </a></span>--%>
                            <%--<span class="bk_list"> <a href="#">遭遇系统</a></span>--%>
                        <%--</div>--%>
                        <%--<div class="bk_bd_hd">--%>

                            <%--巫医--%>
                 <%--<span class="operate_bk"> <a class="bk_delete" href="#" title="删除"></a>--%>
                   <%--<a class="bk_editxt" href="#" title="编辑"></a></span>--%>
                   <%--<span class="dengji">--%>
                     <%--<a href="#" class="three_bk"><i></i>三级</a>--%>
                   <%--</span>--%>

                        <%--</div>--%>
                        <%--<div class="bk_bd_bd">--%>
                        <%--<span class="bk_list"> <a href="#">技能系统</a> <span class="operate_bk"> <a class="bk_delete"--%>
                                                                                                 <%--href="#"--%>
                                                                                                 <%--title="删除"></a>--%>
                       <%--<a class="bk_editxt" href="#" title="编辑"></a></span>    </span>--%>
                            <%--<span class="bk_list">  <a href="#">工匠系统</a> </span>--%>
                            <%--<span class="bk_list"> <a href="#">符文系统</a> </span>--%>
                            <%--<span class="bk_list"><a href="#">血球系统 </a></span>--%>
                            <%--<span class="bk_list"> <a href="#">遭遇系统</a></span>--%>
                        <%--</div>--%>
                        <%--<div class="bk_bd_hd">--%>

                            <%--秘术师--%>
                 <%--<span class="operate_bk"> <a class="bk_delete" href="#" title="删除"></a>--%>
                   <%--<a class="bk_editxt" href="#" title="编辑"></a></span>--%>
                   <%--<span class="dengji">--%>
                     <%--<a href="#" class="three_bk"><i></i>三级</a>--%>
                   <%--</span>--%>

                        <%--</div>--%>
                        <%--<div class="bk_bd_bd">--%>
                        <%--<span class="bk_list"> <a href="#">技能系统</a> <span class="operate_bk"> <a class="bk_delete"--%>
                                                                                                 <%--href="#"--%>
                                                                                                 <%--title="删除"></a>--%>
                       <%--<a class="bk_editxt" href="#" title="编辑"></a></span>    </span>--%>
                            <%--<span class="bk_list">  <a href="#">工匠系统</a> </span>--%>
                            <%--<span class="bk_list"> <a href="#">符文系统</a> </span>--%>
                            <%--<span class="bk_list"><a href="#">血球系统 </a></span>--%>
                            <%--<span class="bk_list"> <a href="#">遭遇系统</a></span>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<!--bk_bd-->--%>
                    <%--<div class="bk_btn">--%>
                        <%--<a href="#" class="save_bk">保存修改</a>--%>
                    <%--</div>--%>
                    <%--<div class="bk_sth clearfix"><strong>最近编辑者：</strong>Yuee，影子熊，邓布利空，大幻秘--%>
                        <%--<br/><a href="#"> 攻略百科须知</a>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!--bk_con-->
            </div>
            <!--baike-->
        </div>
        <!--stab_bd-->
    </div>
    <!--con_area-->
    <div class="con_ft"></div>
</div>
<div class="sidebar">
    <div class="side_item">
        <div class="side_hd02"></div>
        <div class="side_bd side_pd02 clearfix">
            <div class="databd_con">
                <div class="data_bd">
                    <div class="side_game clearfix">
                        <div class="entryshadow clearfix">

                            <img src="http://r002.joyme.com/r002/game/2012/06/90/0D98D758AB71384271537405AC43E98D_GLL.jpg"
                                 width="150" height="200">

                        </div>


                        <a href="javascript:void(0)" name="followBoard" id="board_244" data-id="244"
                           class="concern clearfix">关注</a>


                    </div>

                    <div class="gameconwrapper">中文名：魔兽世界
                        <br style="">

                        <p>游戏平台：PC
                        </p>

                        <p>游戏类别：网络角色扮演
                        </p>

                        <p>发行商：网之易</p>


                        <p>开发商：暴雪</p>

                        <p style="display: none" data-ishide="hide" id="game_web">

                            官网：<a target="_blank" href="http://www.warcraftchina.com/">http://www.warcraftchina.com/</a>

                        </p>

                        <p style="display: none" data-ishide="hide" id="game_desc">
                            游戏简介：《魔兽世界》是著名的游戏公司暴雪娱乐（BlizzarD
                            Entertainment）所制作的一款大型多人在线角色扮演游戏（MMORPG），于2004年年中在北美公开测试。《魔兽世界》在中国大陆的前代理商为第九城市，2005年3月21日下午开始限量测试，2005年4月23日关闭限量测试，2005年4月26日开始公开测试，2005年6月6日正式商业化运营。2009年6月7日起中国地区运营商变更为网易。
                        </p>
                        <a class="pack_up" style="display: none" href="javascript:void(0);" id="gameviewup">收起</a>
                    </div>
                    <p></p>
                </div>
                <a href="javascript:void(0)" style="display: block" class="see_all" id="gameviewdown"></a>
            </div>
            <div class="item_pd01">
                <h3>版主：（3）</h3>


                <div class="dr_con clearfix">
                                <span class="commenfacecon">
                                    <a href="http://yangyulong.joyme.com" name="atLink" title="陪妳看煙火" target="_blank">
                                        <img width="33" height="33"
                                             src="http://r002.joyme.com/r002/headicon/2012/06/81/624FFE58CDEC0CE53D4C63B5C8041D24_S.jpg">
                                    </a>
                                </span>
                    <a href="http://yangyulong.joyme.com" target="_blank" name="atLink" title="陪妳看煙火">陪妳看煙火</a>
                </div>

                <div class="dr_con clearfix">
                                <span class="commenfacecon">
                                    <a href="http://samantha.joyme.com" name="atLink" title="囧妞" target="_blank">
                                        <img width="33" height="33"
                                             src="http://r001.joyme.com/r001/headicon/2012/06/22/7A3D008583942C2C0A668B358E75CE72_S.jpg">
                                    </a>
                                </span>
                    <a href="http://samantha.joyme.com" target="_blank" name="atLink" title="囧妞">囧妞</a>
                </div>

                <div class="dr_con clearfix">
                                <span class="commenfacecon">
                                    <a href="http://Creamapple.joyme.com" name="atLink" title="奶油小苹果" target="_blank">
                                        <img width="33" height="33"
                                             src="http://r001.joyme.com/r001/headicon/2012/05/97/EE5650C8EC0F065418C6E5DF710ECAC0_S.jpg">
                                    </a>
                                </span>
                    <a href="http://Creamapple.joyme.com" target="_blank" name="atLink" title="奶油小苹果">奶油小苹果</a>
                </div>


                <a class="ask_for" href="http://www.joyme.com/home/joyme/3NxDguvsRe6qYV8iWgRfek"
                   target="_blank">我要申请组长！</a>
            </div>
            <!--item_pd01-->


            <div class="item_pd01 w_line">
                <h3>魔兽世界相关链接</h3>
                <ul class="item_list02">

                    <li><a href="" target="_blank">我们都爱熊猫人QQ群：236018997</a></li>

                    <li><a href="http://html.joyme.com/zt/wow/panda.html" target="_blank">熊猫人之谜国服内测帐号发放活动</a></li>

                    <li><a href="http://xiaode.joyme.com/" target="_blank">记者团小德带大家走进熊猫人</a></li>

                    <li>
                        <a href="http://www.joyme.com/search/s/%E9%AD%94%E5%85%BD%E4%B8%96%E7%95%8C%E5%90%8C%E4%BA%BA%E4%BD%9C%E5%93%81/"
                           target="_blank">魔兽世界同人作品</a></li>

                    <li><a href="http://spikymy.joyme.com/" target="_blank">熊猫人之谜游记：《团团圆圆回家记》</a></li>

                </ul>
            </div>

            <div class="item_pd01 w_line">
                <h3>搜索<span>"<a href="http://www.joyme.com/search/s/魔兽世界">魔兽世界</a>"</span>相关的全部结果</h3>
            </div>
        </div>
        <!--side_bd-->
        <div class="side_ft"></div>
    </div>
</div>
</div>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/baike-init.js')
</script>
</body>
</html>