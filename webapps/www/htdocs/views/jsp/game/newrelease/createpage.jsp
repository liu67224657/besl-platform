<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta name="Keywords" content="着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人">
    <meta name="description"
          content="着迷网（Joyme.com）是一个以游戏为主题的游戏玩家社区，记录你的游戏生活和情感 ，相遇结交志同道合的朋友，互动属于自己的游戏文化 ，有趣、新鲜的游戏话题，每天等你来讨论!,着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon">
    <title>着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<!--header-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<!-- 手游预告开始 -->
<div class="post-game-box clearfix">
<!-- left -->
<div class="posted-game clearfix">
    <!-- 表单 -->
    <div class="post-game-form">
        <h1>提交游戏报告</h1>

        <form action="/newrelease/create" method="post" id="form_submit_create">
            <h2>游戏信息</h2>
            <br>
            <table>
                <tbody>
                <tr>
                    <td class="labels">游戏名称：</td>
                    <td><input type="text" class="inputs" style="width:310px;" name="newgamename"
                               id="input_text_game_name"></td>
                </tr>
                <tr>
                </tr>
                <tr>
                    <td class="labels" valign="top">游戏图标：</td>
                    <td>
                        <div class="fl">
                            <img id="img_icon" src="${URL_LIB}/static/theme/default/img/default.jpg"/>
                            <input id="input_icon" type="hidden" name="newgameicon">
                        </div>
                        <div class="fl" style="padding-left:20px;">
                            <p>
                                <a id="upload_button_icon" name="upload_button"></a>&nbsp;&nbsp;
                            </p>

                            <p><br><span id="loading" class="span_loading" style="display:none"><img
                                    src="${URL_LIB}/static/theme/default/img/loading.gif"/></span></p>

                            <p><br><span style="color:#aa6462">格式支持gif jpg png&nbsp;&nbsp;&nbsp;大小80X80最佳</span></p>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="labels" valign="top">游戏标签：</td>
                    <td>
                        <div class="choose-game-tag">
                            <c:forEach items="${tagList}" var="tag">
                                <label><input type="checkbox" name="tag" value="${tag.newReleaseTagId}">${tag.tagName}
                                </label>
                            </c:forEach>
                        </div>
                        <input type="hidden" name="tagidstr" id="input_hidden_tag" value="">
                    </td>
                </tr>
                </tbody>
            </table>
            <br>

            <h2>商务信息</h2>
            <br>
            <table>
                <tbody>
                <tr>
                    <td class="labels">企业/团队名称：</td>
                    <td><input type="text" class="inputs" style="width:310px;" name="companyname"
                               id="input_text_company_name"></td>
                </tr>
                <tr>
                    <td class="labels">企业/团队人数：</td>
                    <td>
                        <select name="peoplenumtype" id="select_people_num_type">
                            <option value="">请选择</option>
                            <c:forEach items="${peopleNumTypeCollection}" var="type">
                                <option value="${type.value}"><fmt:message
                                        key="gameres.newgame.peoplenumtype.${type.value}"
                                        bundle="${userProps}"/></option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="labels">所在城市：</td>
                    <td id="td_city">
                        <select name="cityid" id="select_city">
                            <option value="">请选择所在城市</option>
                            <c:forEach items="${cityList}" var="city">
                                <option value="${city.cityId}">${city.cityName}</option>
                            </c:forEach>
                            <option value="0">其他</option>
                        </select>
                        <input class="inputs" id="input_text_city" type="text" name="cityname">
                    </td>
                </tr>
                <tr>
                    <td class="labels">合作方式：</td>
                    <td>
                        <label><input type="checkbox" name="coopratetype" value="1">独代</label>&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="coopratetype" value="2">分成</label>&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                </tbody>
            </table>
            <table>
                <tbody>
                <tr>
                    <td class="labels">联系人：</td>
                    <td><input type="text" class="inputs" style="width:203px;" name="contacts" id="input_contacts"></td>
                    <td class="labels">联系邮箱：</td>
                    <td><input type="text" class="inputs" style="width:203px;" name="email" id="input_email"></td>
                </tr>
                <tr>
                    <td class="labels">联系电话：</td>
                    <td><input type="text" class="inputs" style="width:203px;" name="phone" id="input_phone"></td>
                    <td class="labels">联系QQ：</td>
                    <td><input type="text" class="inputs" style="width:203px;" name="qq" id="input_qq"></td>
                </tr>
                </tbody>
            </table>
            <table>
                <tbody>
                <tr>
                    <td class="labels">预计上市时间：</td>
                    <td><input type="text" class="inputs" style="width:30px;" id="input_yyyy">&nbsp;&nbsp;年&nbsp;&nbsp;
                        <input type="text" class="inputs" style="width:30px;" id="input_MM">&nbsp;&nbsp;月
                        <input type="hidden" name="publishdate" id="input_hidden_pub_date" value=""/>
                    </td>
                </tr>
                <tr>
                    <td class="labels">发行范围：</td>
                    <td>
                        <label><input type="checkbox" name="publisharea" value="1">国内</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="2">亚洲</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="4">东南亚</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="8">日韩</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="16">北美</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="32">南美</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="64">西亚</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="128">非洲</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="checkbox" name="publisharea" value="256">欧洲</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                </tbody>
            </table>
            <br>

            <h2>游戏截图：</h2>
            <br>
            <table width="100%">
                <tbody>
                <tr>
                    <td style="padding-left: 50px">
                        <div class="fl" id="div_img">
                            <img id="0" name="imgpic" src="${URL_LIB}/static/theme/default/img/default.jpg"/>
                        </div>
                        <input id="input_pic_set" type="hidden" name="newgamepicset" value="">

                        <div class="fl" style="padding-left:20px;">
                            <p>
                                <a id="upload_button_pic_set"></a>&nbsp;&nbsp;
                            </p>

                            <p><br><span id="loading_pic_set" style="display:none"><img
                                    src="${URL_LIB}/static/theme/default/img/loading.gif"/></span></p>
                            <p><br><span style="color:#aa6462"><a id="a_delete_pic" href="javascript:void(0);">删除</a></span></p>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <br><br>

            <h2>游戏介绍</h2>
            <br>
            <table width="100%">
                <tbody>
                <tr>
                    <td><textarea
                            style="width:620px; height:100px; margin:0 auto; display:block; border:1px solid #e1e1e1"
                            name="newgamedesc" id="textarea_game_desc"></textarea>
                    </td>
                </tr>
                <tr>
                    <td align="center"><input style="border:none" type="submit" value="" class="publishon" name="">
                    </td>
                </tr>
                </tbody>
            </table>
            <br/><br/><br/>
        </form>

    </div>

</div>
<!-- right -->
<div class="posted-game-right">
    <a href="/newrelease/createpage" class="post-game-btn" id="a_button">提交游戏报告</a>

    <!-- 热门标签 -->
    <div class="posted-game-item">
        <h2>热门<span>标签</span></h2>

        <div class="post-game-hot-tag">
            <c:forEach items="${newGameTagHotList}" var="tag" varStatus="st">
                <a href="#" <c:choose>
                    <c:when test="${st.index%6==0}">
                        style="background:#68a2d0"
                    </c:when>
                    <c:when test="${st.index%6==1}">
                        style="background:#9c82cc"
                    </c:when>
                    <c:when test="${st.index%6==2}">
                        style="background:#fb8466"
                    </c:when>
                    <c:when test="${st.index%6==3}">
                        style="background:#81cda6"
                    </c:when>
                    <c:when test="${st.index%6==4}">
                        style="background:#68a2d0"
                    </c:when>
                    <c:when test="${st.index%6==5}">
                        style="background:#68a2d0"
                    </c:when>
                </c:choose>>${tag.tagName}</a>
            </c:forEach>
        </div>
    </div>

    <!-- 所在城市 -->
    <div class="posted-game-item">
        <h2>所在<span>城市</span></h2>

        <div class="post-game-city">
            <ul>
                <c:forEach items="${cityList}" var="city">
                    <li><a href="#">${city.cityName}</a></li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/newrelease-create-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>

</html>
