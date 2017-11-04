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
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>上传我的游戏信息_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/newgames.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/third/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
    <script type="text/javascript">


    </script>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script>
    </script>
</head>
<body>
<!-- header -->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>
<!-- content -->
<div class="newgames-content clearfix">
<h2 class="newgames-title">上传我的游戏信息</h2>

<!-- 进度条 -->
<div class="process process-1" id="process"> <!-- 第一步class为“process-1”第二步class为“process-2”以此类推 -->
    <span class="step-1">填写基础信息</span>
    <span class="step-2">填写详细信息</span>
    <span class="step-3">填写商务信息</span>
    <span class="step-4">完成</span>
</div>
<!--基础信息 -->
<form action="${URL_WWW}/developers/home/creategame" method="post" id="newgame">
<div id="basicinfo">
    <table class="post-newgame" width="100%">
        <tr>
            <th width="90">游戏名称：</th>
            <td width="324"><input type="text" name="gamename"
                                   id="gamename" maxlength="20" class="inputs-1" value=""/>
            </td>
            <td>
                <div class="tips"><b>*</b>必填，今后可以根据要求修改名称，将通知到关注此游戏的其他人。</div>
            </td>
        </tr>
        <tr>
            <th>其他名称：</th>
            <td><input type="text" name="anothername" id="anothername" maxlength="20" class="inputs-1" value=""/></td>
            <td>
                <div class="tips"><b>*</b>选填，游戏的简称，诨名等。</div>
            </td>
        </tr>
        <tr>
            <th>游戏图标：</th>
            <td>
                <div class="showLoadedPic size-1 clearfix">
                    <ul>
                        <li><img src="${URL_LIB}/static/theme/default/img/default.jpg" id="gameicon"
                                 width="115" style="width:115px;"
                                 height="115"/></li>
                    </ul>

                </div>
                &nbsp;&nbsp;
                <a href="javascript:void(0)"><span id="upload_button"></span></a>

                <span id="loading" style="display:none"><img
                        src="${URL_LIB}/static/theme/default/img/loading.gif"/></span>
                <input type="hidden" id="picurl" name="picurl"/>
            </td>
            <td>
                <div class="tips"><b>*</b>必填，分辨率比例为1:1，文件不大于2M，支持jpg、png</div>
            </td>
        </tr>
        <tr>
            <th valign="top">游戏分类：</th>
            <td>
                <label> <input type="checkbox" value="1" name="category"/> 角色扮演 </label>
                <label> <input type="checkbox" value="2" name="category"/> 策略 </label>
                <label> <input type="checkbox" value="3" name="category"/> 模拟经营 </label>
                <label><input type="checkbox" value="4" name="category"/> 益智 </label>
                <label> <input type="checkbox" value="5" name="category"/> 休闲 </label>
                <label> <input type="checkbox" value="6" name="category"/> 棋牌</label>
                <label> <input type="checkbox" value="7" name="category"/> 动作</label>
                <label> <input type="checkbox" value="8" name="category"/> 格斗</label>
                <label> <input type="checkbox" value="9" name="category"/> 体育竞技</label>
                <label> <input type="checkbox" value="10" name="category"/> 音乐舞蹈</label>
                <label> <input type="checkbox" value="11" name="category"/> 模拟经营</label>
                <label> <input type="checkbox" value="12" name="category"/> 卡牌 </label>
                <label> <input type="checkbox" value="13" name="category"/> 网游 </label>
                <label><input type="checkbox" value="14" name="category"/> 射击 </label>
                <label> <input type="checkbox" value="15" name="category"/> 赛车竞速 </label>
                <label> <input type="checkbox" value="16" name="category"/> 冒险解谜 </label>
                <label> <input type="checkbox" value="17" name="category"/> 桌面游戏 </label>
                <%--<input type="hidden" name="gamecategory" id="gamecategory"/>--%>
            </td>
            <td>
                <div class="tips"><b>*</b>最少选择1个游戏分类，最多选择5个</div>
            </td>
        </tr>
        <tr>
            <th valign="top">游戏设备：</th>
            <td>
                <label> <input type="checkbox" value="1" name="gamesystem" id="iphone"/> iphone</label>
                <label> <input type="checkbox" value="2" name="gamesystem" id="ipad"/> ipad</label>
                <label> <input type="checkbox" value="3" name="gamesystem" id="android"/> 安卓</label>
                <%--<input type="hidden" name="gamedevice" id="gamedevice"/>--%>
            </td>
            <td>
                <div class="tips"><b>*</b>必填，选择游戏所在的系统环境。</div>
            </td>
        </tr>
        <tr>
            <th valign="top" style="padding-top: 6px;">开发商：</th>
            <td>
                <div class="multiinput"><input type="text" maxlength="20" class="inputs-1" name="developer"
                                               id="developer"/></div>
                <!-- 添加新开发商，只需复制这个div即可 -->


            </td>
            <td valign="top" style="padding-top: 6px;">
                <div class="tips"><b>*</b>必填</div>
            </td>
        </tr>
        <tr>
            <th valign="top" style="padding-top: 6px;">运营商：</th>
            <td>
                <div class="multiinput"><input type="text" class="inputs-1" maxlength="20" value="" name="publishers"
                                               id="publishers"/>
                </div>
            </td>
            <td valign="top" style="padding-top: 6px;">
                <%--<div class="tips"><b>*</b>必填</div>--%>
            </td>
        </tr>
        <tr>
            <th valign="top" style="padding-top: 6px;">上市时间：</th>
            <td>
                <div class="multiinput">
                    <input type="text" class="Wdate inputs-2"
                           onClick="WdatePicker({autoPickDate:true})"
                           readonly="readonly" name="gamepublictime" id="gamepublictime"/>&nbsp;&nbsp;
                    <%--<select name="">--%>
                    <%--<option value="">最近更新</option>--%>
                    <%--</select>--%>
                </div>

            </td>
            <td valign="top" style="padding-top: 6px;">
                <%--<div class="tips"><b>*</b>必填</div>--%>
            </td>
        </tr>
    </table>
    <div class="send-newgame-btn"><a href="javascript:void(0)" class="submitbtn" id="basicinfonext"><span>下一步</span></a>
    </div>
</div>
<!-- 详细信息 -->
<div id="detailinfo">
    <table class="post-newgame" width="100%" style="border:none" onsubmit="return validata();">
        <tr>
            <th width="90" valign="top">游戏截图：</th>
            <td>
                <img src="${URL_LIB}/static/theme/default/img/default.jpg" id="picdef" style="display:block;"
                     width="115"
                     height="115"/>

                <div class="showLoadedPic size-1 clearfix">
                    <ul id="upload">

                    </ul>
                </div>
                <a href="javascript:void(0)"><span id="uploadpic">上传截图</span></a>
                <span class="tips"
                      style="display:inline-block; vertical-align:middle; padding:4px 0 0 10px; margin-top:-20px;">最多可上传5张，建议尺寸为640*960</span>
                <span id="loading1" style="display:none">
                <img src="${URL_LIB}/static/theme/default/img/loading.gif"/></span>
                <input type="hidden" name="gamepicurl" id="gamepicurl"/>
            </td>

        </tr>
    </table>
    <table class="post-newgame" width="100%" style="margin-top:0">
        <tr>
            <th width="90" style="padding-bottom:10px;">游戏简介：</th>
            <td style="padding-bottom:10px;">
                <div class="tips">最多可以输入3000字，支持换行</div>
            </td>
        </tr>
        <tr>
            <td style="padding-left:30px;" colspan="2">
                <textarea class="txtarea" id="gameprofile" name="gameprofile"></textarea>
            </td>
        </tr>
        <tr>
            <th style="padding-bottom:10px;">版本简介：</th>
            <td style="padding-bottom:10px;">
                <div class="tips">最多可以输入3000字，支持换行</div>
            </td>
        </tr>
        <tr>
            <td style="padding-left:30px;" colspan="2">
                <textarea class="txtarea" id="versionprofile" name="versionprofile"></textarea>
            </td>
        </tr>
        <tr>
            <th style="padding-bottom:10px;">游戏下载：</th>
            <td style="padding-bottom:10px;">
                <div class="tips"></div>
            </td>
        </tr>
        <tr>
            <td style="padding-left:30px;" colspan="2">
                <div class="set-download-link" id="uploadLink1" name="downloadinfo"><!-- 新增下载地址，只要复制这个div即可 -->
                    <table width="100%">
                        <tr>
                            <td>下载地址：</td>
                        </tr>
                        <tr>
                            <td>
                                <select name="device" id="device1">
                                    <option value="0">选择游戏设备</option>
                                    <option value="1">iphone</option>
                                    <option value="2">ipad</option>
                                    <option value="3">android</option>
                                </select>
                                <select name="channel" id="channel1">
                                    <option value="0">选择下载渠道</option>
                                    <c:forEach items="${channelList}" var="channel">
                                        <option value="${channel.channelId}">${channel.channelName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="text" class="inputs-1" style="width:484px" id="download1" name='download'
                                       value="请输入下载地址"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="text" class="inputs-1" style="width:150px" id="gameversion1"
                                       name='gameversion'
                                       value="请输入游戏版本"/>
                                <input type="text" class="inputs-1" style="width:150px" id="gamesize1" name="gamesize"
                                       value="请输入游戏大小"/>
                                <input type="text" class="inputs-1" style="width:150px" id="systemversion1"
                                       name="systemversion"
                                       value="请输入支持的系统版本"/>
                            </td>
                        </tr>
                    </table>
                </div>

                <input type="button" id="createUploadInfo" value="新增一个下载地址"/>
                <input type="button" id="removeUploadInfo" value="删除一个下载地址"/>
                <input type="hidden" id="input_download_info" name="inputdownloadinfo"/>
            </td>
        </tr>
    </table>
    <div class="send-newgame-btn"><a href="javascript:void(0)" class="submitbtn"
                                     id="detailinfonext"><span>下一步</span></a></div>
</div>


<!-- 商务信息-->
<div id="business">
    <table class="post-newgame" width="100%">
        <tr>
            <th width="90">开发团队名称：</th>
            <td width="530"><input type="text" name="teamname" maxlength="20" class="inputs-1" value="" id="teamname"
                                   maxlength="20"/>
            </td>
            <td>
                <div class="tips"></div>
            </td>
        </tr>
        <tr>
            <th>开发团队人数：</th>
            <td><input type="radio" name="teamnum" value="1"/>1-10人&nbsp;&nbsp;
                <input type="radio" name="teamnum" value="2"/>11-20人&nbsp;&nbsp;
                <input type="radio" name="teamnum" value="3"/>21-50人&nbsp;&nbsp;
                <input type="radio" name="teamnum" value="4"/>51-100人&nbsp;&nbsp;
                <input type="radio" name="teamnum" value="5"/>101-200人&nbsp;&nbsp;
                <input type="radio" name="teamnum" value="6"/>201人以上&nbsp;&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <th>所在城市：</th>
            <td>
                <input type="radio" name="radio" value="北京"/>北京&nbsp;&nbsp;
                <input type="radio" name="radio" value="广州"/>广州&nbsp;&nbsp;
                <input type="radio" name="radio" value="上海"/>上海&nbsp;&nbsp;
                <input type="radio" name="radio" value="福州"/>福州&nbsp;&nbsp;

                <input type="radio" name="radio" value="杭州"/>杭州&nbsp;&nbsp;
                <input type="radio" name="radio" value="成都"/>成都&nbsp;&nbsp;

                <input type="radio" name="radio" value="深圳"/>深圳&nbsp;&nbsp;
                <input type="radio" name="radio" value="大连"/>大连&nbsp;&nbsp;
                <input type="radio" name="radio" value="其他"/>其它
                <input type="text" class="inputs-3" style="display:none;" name="city" id="city"/>
            </td>
            <td></td>
        </tr>
        <tr>
            <th>联系人：</th>
            <td><input type="text" class="inputs-3" name="contacts" maxlength="20" id="contacts" value=""/></td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <th>联系邮箱：</th>
            <td><input type="text" name="email" class="inputs-3" id="email" maxlength="40" value=""/></td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <th>联系电话：</th>
            <td><input type="text" name="phone" class="inputs-3" maxlength="20" id="phone" value=""/></td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <th>联系QQ：</th>
            <td><input type="text" name="qq" class="inputs-3" id="qq" maxlength="20" value=""/></td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <th>发行范围：</th>
            <td>
                <label> <input type="checkbox" value="1" name="checkbox" id="radio1"/> 国内</label>
                <label> <input type="checkbox" value="2" name="checkbox" id="radio2"/> 北美</label>
                <label> <input type="checkbox" value="4" name="checkbox" id="radio3"/> 南美</label>
                <label> <input type="checkbox" value="8" name="checkbox" id="radio4"/> 东南亚</label>
                <label> <input type="checkbox" value="16" name="checkbox" id="radio5"/> 日韩</label>
                <label> <input type="checkbox" value="32" name="checkbox" id="radio6"/> 港台</label>
                <label> <input type="checkbox" value="64" name="checkbox" id="radio7"/> 西亚</label>
                <label> <input type="checkbox" value="128" name="checkbox" id="radio8"/> 非洲</label>
                <label> <input type="checkbox" value="256" name="checkbox" id="radio9"/> 欧洲</label>
                <label> <input type="checkbox" value="512" name="checkbox" id="radio10"/> 其它</label>
                <input type="hidden" name="area" id="area"/>
            </td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <th>融资方式：</th>
            <td>
                <label><input type="checkbox" name="fincheckbox" value="1"/> 独代</label>
                <label><input type="checkbox" name="fincheckbox" value="2"/> 分成</label>
                <input type="hidden" name="financing" id="financing"/>
            </td>
            <td>&nbsp;</td>
        </tr>
    </table>
    <div class="send-newgame-btn"><a href="javascript:void(0)" class="submitbtn" id="businessinfo"><span>下一步</span></a>
        <input type="submit" style="display:none" id="submitformbtn"/>
    </div>
</div>

</form>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/gamedb-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
