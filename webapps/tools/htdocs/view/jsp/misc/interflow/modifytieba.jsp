<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台消息推送列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 贴吧/QQ帐号管理 >> 贴吧帐号列表</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table width="30%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">编辑贴吧帐号</td>
        <td class="">
            <form></form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form action="/misc/tieba/modify" method="post" id="form_submit">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td">
        <input type="hidden" name="ifid" value="${interFlow.interflowId}" id="input_appid"/>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        QQ群名称:
    </td>
    <td height="1">
        <input type="text" name="ifname" id="input_ifname" value="${interFlow.name}">
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        QQ群号:
    </td>
    <td height="1">
        <input id="input_account" type="text" name="ifaccount" size="100"
               value="${interFlow.account}">
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        群主:
    </td>
    <td height="1">
        <input id="input_lord" type="text" name="iflord" width="500" value="${interFlow.lord}">
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        负责人:
    </td>
    <td height="1">
        <input id="input_ifduty" type="text" name="ifduty" value="${interFlow.duty}">
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        关注数:
    </td>
    <td height="1">
        <select name="ifusernumber" id="input_ifusernumber">
            <option value="" selected>请选择</option>
            <option value="1-99"
                    <c:if test="${interFlow.userNumber eq '1-99'}">selected="selected"</c:if>>1-99
            </option>
            <option value="100-499"
                    <c:if test="${interFlow.userNumber eq '100-499'}">selected="selected"</c:if>>100-499
            </option>
            <option value="500-999"
                    <c:if test="${interFlow.userNumber eq '500-999'}">selected="selected"</c:if>>500-999
            </option>
            <option value="1000-1999"
                    <c:if test="${interFlow.userNumber eq '1000-1999'}">selected="selected"</c:if>>1000-1999
            </option>
            <option value="2000-4999"
                    <c:if test="${interFlow.userNumber eq '2000-4999'}">selected="selected"</c:if>>2000-4999
            </option>
            <option value="5000-9999"
                    <c:if test="${interFlow.userNumber eq '5000-9999'}">selected="selected"</c:if>>5000-9999
            </option>
            <option value="万级"
                    <c:if test="${interFlow.userNumber eq '万级'}">selected="selected"</c:if>>万级
            </option>
            <option value="十万级"
                    <c:if test="${interFlow.userNumber eq '十万级'}">selected="selected"</c:if>>十万级
            </option>
            <option value="百万级"
                    <c:if test="${interFlow.userNumber eq '百万级'}">selected="selected"</c:if>>百万级
            </option>
        </select>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        等级:
    </td>
    <td height="1">
        <select id="input_iflevel" name="iflevel">
            <option value="" selected>请选择</option>
            <option value="初出茅庐"
                    <c:if test="${interFlow.level eq '初出茅庐'}">selected="selected"</c:if>>初出茅庐
            </option>
            <option value="广邀名贴"
                    <c:if test="${interFlow.level eq '广邀名贴'}">selected="selected"</c:if>>广邀名贴
            </option>
            <option value="小有所成"
                    <c:if test="${interFlow.level eq '小有所成'}">selected="selected"</c:if>>小有所成
            </option>
            <option value="不同凡响"
                    <c:if test="${interFlow.level eq '不同凡响'}">selected="selected"</c:if>>不同凡响
            </option>
            <option value="卓有成效"
                    <c:if test="${interFlow.level eq '卓有成效'}">selected="selected"</c:if>>卓有成效
            </option>
            <option value="自成一派"
                    <c:if test="${interFlow.level eq '自成一派'}">selected="selected"</c:if>>自成一派
            </option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        厂商:
    </td>
    <td height="1">
        <input id="input_ifmanufacturer" type="text" name="ifmanufacturer"
               value="${interFlow.manufacturer}"/>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        游戏名:
    </td>
    <td height="1">
        <input id="input_ifgamename" type="text" name="ifgamename" value="${interFlow.gameName}"/>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        游戏类型:
    </td>
    <td height="1">
        <select name="ifgamecategory" id="input_ifgamecategory">
            <option value="" selected>请选择</option>
            <option value="ACT 动作游戏" <c:if test="${interFlow.gameCategory eq 'ACT 动作游戏'}">selected="selected"</c:if>>ACT
                动作游戏
            </option>
            <option value="ARPG动作角色扮演"
                    <c:if test="${interFlow.gameCategory eq 'ARPG动作角色扮演'}">selected="selected"</c:if>>ARPG动作角色扮演
            </option>
            <option value="AVG 冒险游戏" <c:if test="${interFlow.gameCategory eq 'AVG 冒险游戏'}">selected="selected"</c:if>>AVG
                冒险游戏
            </option>
            <option value="AAVG动作冒险游戏"
                    <c:if test="${interFlow.gameCategory eq 'AAVG动作冒险游戏'}">selected="selected"</c:if>>AAVG动作冒险游戏
            </option>
            <option value="FPS第一人称射击" <c:if test="${interFlow.gameCategory eq 'FPS第一人称射击'}">selected="selected"</c:if>>
                FPS第一人称射击
            </option>
            <option value="FTG 格斗游戏" <c:if test="${interFlow.gameCategory eq 'FTG 格斗游戏'}">selected="selected"</c:if>>FTG
                格斗游戏
            </option>
            <option value="MUG 音乐游戏" <c:if test="${interFlow.gameCategory eq 'MUG 音乐游戏'}">selected="selected"</c:if>>MUG
                音乐游戏
            </option>
            <option value="PUZ 益智类游戏" <c:if test="${interFlow.gameCategory eq 'PUZ 益智类游戏'}">selected="selected"</c:if>>
                PUZ 益智类游戏
            </option>
            <option value="RAC 赛车游戏" <c:if test="${interFlow.gameCategory eq 'RAC 赛车游戏'}">selected="selected"</c:if>>RAC
                赛车游戏
            </option>
            <option value="RPG 角色扮演游戏"
                    <c:if test="${interFlow.gameCategory eq 'RPG 角色扮演游戏'}">selected="selected"</c:if>>RPG 角色扮演游戏
            </option>
            <option value="RTS 即时战略游戏"
                    <c:if test="${interFlow.gameCategory eq 'RTS 即时战略游戏'}">selected="selected"</c:if>>RTS 即时战略游戏
            </option>
            <option value="SLG 战棋游戏" <c:if test="${interFlow.gameCategory eq 'SLG 战棋游戏'}">selected="selected"</c:if>>SLG
                战棋游戏
            </option>
            <option value="SPG 体育运动游戏"
                    <c:if test="${interFlow.gameCategory eq 'SPG 体育运动游戏'}">selected="selected"</c:if>>SPG 体育运动游戏
            </option>
            <option value="STG 射击游戏" <c:if test="${interFlow.gameCategory eq 'STG 射击游戏'}">selected="selected"</c:if>>STG
                射击游戏
            </option>
            <option value="AVG恋爱游戏" <c:if test="${interFlow.gameCategory eq 'AVG恋爱游戏'}">selected="selected"</c:if>>
                AVG恋爱游戏
            </option>
            <option value="FLY 模拟飞行" <c:if test="${interFlow.gameCategory eq 'FLY 模拟飞行'}">selected="selected"</c:if>>FLY
                模拟飞行
            </option>
            <option value="SIM 模拟经营" <c:if test="${interFlow.gameCategory eq 'SIM 模拟经营'}">selected="selected"</c:if>>SIM
                模拟经营
            </option>
            <option value="卡牌类RPG" <c:if test="${interFlow.gameCategory eq '卡牌类RPG'}">selected="selected"</c:if>>
                卡牌类RPG
            </option>
            <option value="其他">其他</option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        网游/单机/其它：
    </td>
    <td height="1">
        <select name="ifgametype" id="input_ifgametype">
            <option value="" selected>请选择</option>
            <option value="网游" <c:if test="${interFlow.gameType eq '网游'}">selected="selected"</c:if>>网游</option>
            <option value="单机" <c:if test="${interFlow.gameType eq '单机'}">selected="selected"</c:if>>单机</option>
            <option value="其它" <c:if test="${interFlow.gameType eq '其它'}">selected="selected"</c:if>>其它</option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        平台：
    </td>
    <td height="1">
        <select name="ifplatform" id="input_ifplatform">
            <option value="" selected>请选择</option>
            <option value="IOS" <c:if test="${interFlow.platform eq 'IOS'}">selected="selected"</c:if>>IOS</option>
            <option value="Android" <c:if test="${interFlow.platform eq 'Android'}">selected="selected"</c:if>>Android
            </option>
            <option value="I/A双平台" <c:if test="${interFlow.platform eq 'I/A双平台'}">selected="selected"</c:if>>I/A双平台
            </option>
            <option value="PC" <c:if test="${interFlow.platform eq 'PC'}">selected="selected"</c:if>>PC</option>
            <option value="网页" <c:if test="${interFlow.platform eq '网页'}">selected="selected"</c:if>>网页</option>
            <option value="家用机" <c:if test="${interFlow.platform eq '家用机'}">selected="selected"</c:if>>家用机</option>
            <option value="掌机" <c:if test="${interFlow.platform eq '掌机'}">selected="selected"</c:if>>掌机</option>
            <option value="其他" <c:if test="${interFlow.platform eq '其他'}">selected="selected"</c:if>>其他</option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        题材：
    </td>
    <td height="1">
        <select name="iftheme" id="input_iftheme">
            <option value="" selected>请选择</option>
            <option value="动漫" <c:if test="${interFlow.theme eq '动漫'}">selected="selected"</c:if>>动漫</option>
            <option value="游戏" <c:if test="${interFlow.theme eq '游戏'}">selected="selected"</c:if>>游戏</option>
            <option value="影视" <c:if test="${interFlow.theme eq '影视'}">selected="selected"</c:if>>影视</option>
            <option value="其他" <c:if test="${interFlow.theme eq '其他'}">selected="selected"</c:if>>其他</option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        发布地区：
    </td>
    <td height="1">
        <select name="ifarea" id="input_ifarea">
            <option value="" selected>请选择</option>
            <option value="中国大陆" <c:if test="${interFlow.publishArea eq '中国大陆'}">selected="selected"</c:if>>中国大陆
            </option>
            <option value="中国台湾" <c:if test="${interFlow.publishArea eq '中国台湾'}">selected="selected"</c:if>>中国台湾
            </option>
            <option value="中国香港" <c:if test="${interFlow.publishArea eq '中国香港'}">selected="selected"</c:if>>中国香港
            </option>
            <option value="欧美" <c:if test="${interFlow.publishArea eq '欧美'}">selected="selected"</c:if>>欧美</option>
            <option value="日韩" <c:if test="${interFlow.publishArea eq '日韩'}">selected="selected"</c:if>>日韩</option>
            <option value="其他" <c:if test="${interFlow.publishArea eq '其他'}">selected="selected"</c:if>>其他</option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        最后发帖时间：
    </td>
    <td height="1">
        <select name="lastpost" id="input_lastpost">
            <option value="" selected>请选择</option>
            <option value="当天"
                    <c:if test="${interFlow.lastPostDate eq '当天'}">selected="selected"</c:if>>当天
            </option>
            <option value="三天内"
                    <c:if test="${interFlow.lastPostDate eq '三天内'}">selected="selected"</c:if>>三天内
            </option>
            <option value="一周内"
                    <c:if test="${interFlow.lastPostDate eq '一周内'}">selected="selected"</c:if>>一周内
            </option>
            <option value="一个月内"
                    <c:if test="${interFlow.lastPostDate eq '一个月内'}">selected="selected"</c:if>>一个月内
            </option>
            <option value="一个月以上"
                    <c:if test="${interFlow.lastPostDate eq '一个月以上'}">selected="selected"</c:if>>一个月以上
            </option>
        </select>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
</tr>
<tr align="center">
    <td colspan="3">
        <input name="Submit" type="submit" class="default_button" value="提交">
        <input name="Reset" type="button" class="default_button" value="返回"
               onclick="javascipt:window.history.go(-1);">
    </td>
</tr>
</table>
</form>
</td>
</tr>
</table>
</body>
</html>