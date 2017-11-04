<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>发起投票 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/third/ui/jquery.ui.theme.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="wraper">
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div class="wrapper clearfix">
        <div class="con">
            <div class="con_tit">
                <h2>发起投票</h2>
            </div>
            <!-- =======添加投票开始======== -->
            <div class="con_area con_l clearfix">
                <form action="${URL_WWW}/json/content/post/vote" method="post" id="voteForm">
                    <c:if test="${viewCategory!=null && group!=null}">
                        <input type="hidden" name="categoryid" value="${viewCategory.categoryId}"/>
                        <input type="hidden" name="gamecode" id="gamecode" value="${group.gameCode}"/>
                        <input type="hidden" name="resourceid" id="resourceid" value="${group.resourceId}"/>
                    </c:if>
                    <ul class="poll">
                        <li>
                            <p>投票主题</p>
                            <input type="text" class="input-text" name="subject" id="subject"/>
                        </li>
                        <li class="poll-moretitle"><a href="javascript:void(0)" id="addDescription">+ 投票说明</a></li>
                        <li class="hide" id="des_li">
                            <textarea name="description" id="description"></textarea>
                            <div class="related clearfix" style="height: 15px">
                                <div class="transmit_pic clearfix" id="reply_image_0nJHO17Qx0WbqDIPu0XxHq">
                                    <div class="t_pic" name="reply_image_icon">
                                        <a class="t_pic1" href="javascript:void(0)">图片</a>
                                    </div>
                                    <div style="display:none;" class="t_pic_more" name="reply_image_icon_more">
                                        <a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="reply_image_0nJHO17Qx0WbqDIPu0XxHq">图片</a>
                                        <a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="reply_image_0nJHO17Qx0WbqDIPu0XxHq">链接</a>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li>
                            <p>投票选项</p>
                            <input type="text" class="input-text" name="option">
                            <a class="close" href="javascript:void(0)"name="removeOption" style="display: none"></a>
                        </li>
                        <li>
                            <input type="text" class="input-text" name="option">
                            <a class="close" href="javascript:void(0)" name="removeOption" style="display: none"></a>
                        </li>
                        <li class="poll-moretitle"><a href="javascript:void(0)" id="addOption">+ 添加更多</a></li>
                        <li>
                            <span class="poll-item-title">单选/多选：</span>
                            <select name="choicenum" id="choicenum">
                                <option value="1">单选</option>
                            </select>
                        </li>
                        <li class="poll-moretitle2"><a class="advanced-btn" href="javascript:void(0)" id="advancedset">高级设置</a>
                        </li>
                        <ul class="hide" id="advancedul">
                            <li>
                                <span class="poll-item-title">截止时间：</span>
                                <select name="expiredselect" id="expiredselect">
                                    <option value="7">一周</option>
                                    <option value="30">一个月</option>
                                    <option value="def">自定义</option>
                                </select>&nbsp;
                                <span id="defExpired" style="display: none">
                                <input type="text" class="settime" name="expireddate" id="dateinput">&nbsp;
                                <select name="expiredhour" id="expiredhour">
                                    <option value="00">00</option>
                                    <option value="01">01</option>
                                    <option value="02">02</option>
                                    <option value="03">03</option>
                                    <option value="04">04</option>
                                    <option value="05">05</option>
                                    <option value="06">06</option>
                                    <option value="07">07</option>
                                    <option value="08">08</option>
                                    <option value="09">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                </select>&nbsp;时&nbsp;
                                <select name="expiredminite" id="expiredminite">
                                    <option value="00">00</option>
                                    <option value="01">01</option>
                                    <option value="02">02</option>
                                    <option value="03">03</option>
                                    <option value="04">04</option>
                                    <option value="05">05</option>
                                    <option value="06">06</option>
                                    <option value="07">07</option>
                                    <option value="08">08</option>
                                    <option value="09">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>&nbsp;分&nbsp;
                                </span>
                            </li>
                            <li>
                                <span class="poll-item-title">投票结果：</span>
                                <label><input type="radio" name="visible" value="all" checked="true"> 任何人可见</label>&nbsp;&nbsp;
                                <label><input type="radio" name="visible" value="voted"> 投票后可见</label>
                            </li>
                        </ul>
                        <li>
                            <p class="next">
                                <a class="submitbtn" id="postvote"><span>发起</span></a>
                                <span class="y_zhuanfa">
                                  <input type="checkbox" class="publish_s"
                                  <c:choose>
                                         <c:when test="${fn:length(syncProviderSet)>0}">checked="true"</c:when>
                                         <c:otherwise>disabled="true"</c:otherwise>
                                  </c:choose> name="sync">
                                  <span>同步</span>
                                  <em class="install"></em>
                                </span>
                            </p>
                            <div class="error" id="postError" style="display: none"></div>
                        </li>

                        <c:if test="${viewCategory.categoryId!=null && viewCategory.categoryId>0}">
                        <li>
                            <div class="" style="line-height:24px">
                                <span style="vertical-align:middle">验证码：</span>
                                <img src="/validate/imgcode" style="vertical-align:middle" id="img_validatecode"
                                     onclick="javascript:document.getElementById('img_validatecode').src='/validate/imgcode?'+Math.random();return false;"/>
                                <input id="valimg" name="valimg" type="text" class="" value="" style="width:50px; padding:4px 0; vertical-align:middle"/>
                                <a href="javascript:void(0)"
                                   onclick="javascript:document.getElementById('img_validatecode').src='/validate/imgcode?'+Math.random();return false;">换一张？</a>
                                <br/>
                                <span style="vertical-align:middle; color:#999; margin-left:50px; ">这是一个常用成语，请输入图片中“？”的文字</span>
                            </div>
                        </li>
                        </c:if>
                    </ul>
                </form>
            </div>
            <!-- ===========添加投票结束=========== -->
            <div class="con_ft"></div>
        </div>
        <!--右侧开始-->
        <c:choose>
          <c:when test="${group!=null}">
                <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
          </c:when>
          <c:otherwise>
              <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
          </c:otherwise>
        </c:choose>
        <!--右侧结束-->
    </div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/post-vote-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>
