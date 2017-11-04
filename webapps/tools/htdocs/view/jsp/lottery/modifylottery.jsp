<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>抽奖管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>

    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                if ($.trim($('#input_text_name').val()) == "") {
                    alert('请填写抽奖名称');
                    return false;
                }

                if ($.trim($('#textarea_desc').val()) == "") {
                    alert('请填写抽奖描述');
                    return false;
                }

                var input_text_baserate = $('#input_text_baserate').val()
                if ($.trim(input_text_baserate) == "") {
                    alert('请填写抽奖的基数');
                    return false;
                }
                if (isNaN(input_text_baserate)) {
                    alert("抽奖的基数需要填写数字。");
                    return false;
                }

                var level = $('#input_text_level').val();
                if ($.trim(level) == "") {
                    alert('请填写总共有几等奖');
                    return false;
                }
                if (isNaN(level)) {
                    alert("总共有几等奖需要填写数字。");
                    return false;
                }


                var start_date = $('#start_date').val();
                if (start_date.length == 0) {
                    alert('请填写开始时间');
                    return false;
                }
                var end_date = $('#end_date').val();
                if (end_date.length == 0) {
                    alert('请填写结束时间');
                    return false;
                }

                var sTime = new Date(start_date.replace("-", "/").replace("-", "/")).getTime();
                var eTime = new Date(end_date.replace("-", "/").replace("-", "/")).getTime();
                if (eTime - sTime < 0) {
                    alert("结束时间需要大于开始时间");
                    return false;
                }

                var lotteryRuleActionType = $('#lotteryRuleActionType').val();
                if($.trim(lotteryRuleActionType)==""){
                    alert("抽奖类型");
                    return false;
                }

                var initChance = $('#initChance').val();
                if (initChance.length == 0) {
                    alert('请填写初始抽奖机会');
                    return false;
                }
                if (isNaN(initChance)) {
                    alert("初始抽奖机会需要填写数字。");
                    return false;
                }


                var lotteryRuleChanceType = $('#lotteryRuleChanceType').val();
                if (lotteryRuleChanceType.length == 0) {
                    alert('请填写抽奖次数类型');
                    return false;
                }


                if (lotteryRuleChanceType == 1 || lotteryRuleChanceType == 2) {
                    var maxchance = $('#maxchance').val();
                    if ($.trim(maxchance) == "") {
                        alert('请填写最大抽奖数设置');
                        return false;
                    }

                    if (isNaN(maxchance)) {
                        alert("最大抽奖数需要填写数字。");
                        return false;
                    }
                    if (parseInt(initChance) >parseInt( maxchance)) {
                        alert("最大抽奖数需要大于等于初始抽奖数。");
                        return false;
                    }
                }


            });
        });

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷抽奖 >> 抽奖管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改抽奖</td>
                </tr>
                <tr>
                    <td class="list_table_header_td" style="color: red;">
                        抽奖类型说明：</br>
                        1、随机数(即概率)</br>
                        2、时间戳(即秒杀)</br>
                        3、次数：即第100次、200次、300次中某某奖。</br>
                        一个活动抽奖类型只能是其中一种。
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/lottery/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖名称:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="hidden" name="lotteryid" value="${lottery.lotteryId}"/>
                            <input type="text" name="lotteryname" size="20" id="input_text_name"
                                   value="${lottery.lotteryName}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(nameExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${nameExist}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="lotterydesc" id="textarea_desc" rows="5" cols="100"
                                      style="width: 100%;float: left"
                                      class="default_input_multiline">${lottery.lotteryDesc}</textarea>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖基数:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="baserate" size="20" id="input_text_baserate"
                                   value="${lottery.baseRate}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            总共几等奖:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="awardlevelcount" size="20" id="input_text_level"
                                   value="${lottery.awardLevelCount}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            开始时间:
                        </td>
                        <td height="1">
                            <input id="start_date" name="start_date"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"
                                   value="${lottery.startDate}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            结束时间:
                        </td>
                        <td height="1">
                            <input id="end_date" name="end_date" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" value="${lottery.endDate}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>



                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖类型:
                        </td>
                        <td height="1" class="" width="10">
                            <select name="lotteryRuleActionType" id="lotteryRuleActionType">
                                <option value="">请选择</option>
                                <c:forEach items="${lotteryRuleActionTypeCollection}" var="type">
                                    <c:if test="${type.code==lottery.lotteryRule.lotteryRuleActionType.code}">
                                    <option value="${type.code}" selected="selected">
                                    <fmt:message key="lottery.lotteryruleactiontype.${type.code}" bundle="${def}"/>
                                    </option>
                                    </c:if>

                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖次数类型:
                        </td>
                        <td height="1" class="" width="10">
                            <select name="lotteryRuleChanceType" id="lotteryRuleChanceType">
                                <option value="">请选择</option>
                                <c:forEach items="${lotteryRuleChanceTypeCollection}" var="type">
                                    <option value="${type.code}"
                                    <c:if test="${type.code==lottery.lotteryRule.ruleChanceType.code}">selected="selected" </c:if>>
                                    <fmt:message key="lottery.lotteryRuleChanceType.${type.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            初始抽奖机会:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="initChance" size="20" id="initChance"
                                   value="${lottery.lotteryRule.initChance}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            最大抽奖数:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="maxchance" size="20" id="maxchance"
                                   value="${lottery.lotteryRule.maxChance}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
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