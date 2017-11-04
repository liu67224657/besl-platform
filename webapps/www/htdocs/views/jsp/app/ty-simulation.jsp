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
    <meta name="Keywords" content="桃园，桃园ol，官府，通缉，悬赏，悬赏任务，官府任务，查询 查找 ">
    <meta name="description" content="《桃园》官府悬赏任务查询器，方便查找官府悬赏通缉任务的强盗及强盗逃跑路线。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>

    <title>桃园悬赏查询器 – 着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/css/ty-simulation.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<div id="body_bg">
    <div id="body_bg_mid">
        <div class="LinkHref">
            <a href="${URL_WWW}/home">返回首页</a>
            <a href="${URL_WWW}/game/taoyuan">桃园讨论区</a>
            <a href="http://html.joyme.com/zt/ty/zlk.html">桃园资料库</a>
        </div>
        <div id="content">
            <div class="cont_mid">
                <div class="model_1" id="tym">
                    <h2>请选择查询线索</h2>
                    <ul>
                        <li class="m1"><span></span>
                            <ol>
                                <li><a href="javascript:void(0)" type="city">东城区</a></li>
                                <li><a href="javascript:void(0)" type="city">西城区</a></li>
                            </ol>
                        </li>
                        <li class="m7"></li>
                        <li class="m2"><span></span>
                            <ol>
                                <li><a href="javascript:void(0)" type="sex">男性</a></li>
                                <li><a href="javascript:void(0)" type="sex">女性</a></li>
                            </ol>
                        </li>
                        <li class="m7"></li>
                        <li class="m3"><span></span>
                            <ol>
                                <li><a href="javascript:void(0)" type="age">年轻</a></li>
                                <li><a href="javascript:void(0)" type="age">中年</a></li>
                                <li><a href="javascript:void(0)" type="age">老年</a></li>
                            </ol>
                        </li>
                        <li class="m7"></li>
                        <li class="m4"><span></span>
                            <ol>
                                <li><a href="javascript:void(0)" type="bodytype">清瘦</a></li>
                                <li><a href="javascript:void(0)" type="bodytype">魁梧</a></li>
                                <li><a href="javascript:void(0)" type="bodytype">肥胖</a></li>
                            </ol>
                        </li>
                        <li class="m7"></li>
                        <li><input name="" type="button" class="but_1" id="clue"/></li>
                    </ul>
                    <h3>查询结果</h3>

                    <div class="m_pic" id="pic1">
                        <img src="${URL_LIB}/static/img/tysimulation/0.jpg" width="687" height="571"/>
                    </div>
                </div>


                <div class="model_2" id="flee">
                    <h2>强盗跑了？快去抓住他</h2>
                    <ul>
                        <li class="m5"><span></span>
                            <ol>
                                <li><a href="javascript:void(0)" type="robber">海贼王</a></li>
                                <li><a href="javascript:void(0)" type="robber">鬼谷妖道</a></li>
                                <li><a href="javascript:void(0)" type="robber">冷血无情</a></li>
                                <li><a href="javascript:void(0)" type="robber">千里独行</a></li>
                                <li><a href="javascript:void(0)" type="robber">圣手书生</a></li>
                            </ol>
                        </li>
                        <li class="m7"></li>
                        <li class="m6"><span></span>
                            <ol>
                                <li><a href="javascript:void(0)" type="banditlair">奇礁阵</a></li>
                                <li><a href="javascript:void(0)" type="banditlair">清波渡</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">荒芜之地</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">夜叉岭</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">月食窟1层</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">转镜1层</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">冰原</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">雪地</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">世外孤林</a></li>
                                <li style="display:none"><a href="javascript:void(0)" type="banditlair">桃花林</a></li>

                            </ol>
                        </li>
                        <li class="m7"></li>
                        <li><input name="" type="button" class="but_1" id="robberSub"/></li>
                    </ul>
                    <h3>查询结果</h3>

                    <div class="m_pic" id="pic2">
                        <img src="${URL_LIB}/static/img/tysimulation/1.jpg" width="700" height="540"/>
                    </div>
                </div>
            </div>

        </div>
        <%@ include file="/views/jsp/tiles/footer.jsp" %>
    </div>
</div>
<script src="${URL_LIB}/static/js/common/seajs.js?${version}"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/simulation-init.js')
</script>
</body>
</html>