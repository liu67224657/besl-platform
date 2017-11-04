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
        var add_lottery_number = 1;
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                if ($.trim($('#input_text_name').val()) == "") {
                    alert('请填写奖品名称');
                    return false;
                }
                if ($.trim($('#textarea_desc').val()) == "") {
                    alert('请填写奖品描述');
                    return false;
                }

                if ($.trim($('#select_award_level').val()) == "") {
                    alert('请选择奖品的级别');
                    return false;
                }

                //活动的类型：随机数、时间戳、按次数
                var lottery_lotteryruleactiontype = $('#lottery_lotteryruleactiontype').val();
                var input_text_min = $("#input_text_min").val();
                var input_text_max = $("#input_text_max").val();
                if (lottery_lotteryruleactiontype == 1) {
                    if ($.trim(input_text_min) == "" || $.trim(input_text_max) == "") {
                        alert('请检查中奖数字范围');
                        return false;
                    }
                    if (Number(input_text_min) > Number(input_text_max)) {
                        alert("最大值不能小于最小值");
                        return false;
                    }
                } else if (lottery_lotteryruleactiontype == 2) {
                    if ($.trim(input_text_min) == "" || $.trim(input_text_max) == "") {
                        alert('请检查中奖时间范围');
                        return false;
                    }
                    var sTime = new Date(input_text_min.replace("-", "/").replace("-", "/")).getTime();
                    var eTime = new Date(input_text_max.replace("-", "/").replace("-", "/")).getTime();
                    if (eTime - sTime < 0) {
                        alert("请检查中奖时间范围是否正常");
                        return false;
                    }
                } else if (lottery_lotteryruleactiontype == 3) {
                    if ($.trim(input_text_min) == "") {
                        alert('请检查中奖次数');
                        return false;
                    }
                }


                var typeVal = $('#select_award_type').val();
                if ($.trim(typeVal) == "") {
                    alert('请选择奖品的类型');
                    return false;
                }

                if (typeVal == 2) {
                    var num = /^\d*$/;
                    if (!num.test(descVal)) {
                        alert("“积分奖品”的“奖品描述”只能填入数字（数值为奖励的积分值）");
                        return false;
                    }
                }

                var amountVal = $('#input_text_amount').val();
                if ($.trim(amountVal) == "") {
                    alert('请填写奖品的总数');
                    return false;
                }


                if ($.trim($('#createdate').val()) == "") {
                    alert('请填写开始时间');
                    return false;
                }
                if ($.trim($('#lastmodifydate').val()) == "") {
                    alert('请填写结束时间');
                    return false;
                }


                //奖品次数类型
                var lotteryAwardRuleCountType = $("#lotteryAwardRuleCountType").val();
                if (lotteryAwardRuleCountType == 1 || lotteryAwardRuleCountType == 2) {
                    if ($.trim($('#lottery_award_count_number').val()) == "") {
                        alert('请填写奖品次数限制');
                        return false;
                    }
                } else {
                    $("#add_lottery_number").val(add_lottery_number);
                }
            });

            add_lottery_number=$("input[name^='startTime']").length;

        });

        function addTr() {
            add_lottery_number++;
            var trHtml = '开始时间' + add_lottery_number + '：<input id="startTime' + add_lottery_number + '" name="startTime' + add_lottery_number + '" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" readonly="readonly" size="15" />';
            trHtml += ' 结束时间' + add_lottery_number + '：<input id="endTime' + add_lottery_number + '" name="endTime' + add_lottery_number + '" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" readonly="readonly" size="15"/>';
            trHtml += ' 抽中次数' + add_lottery_number + '：<input type="text" name="times' + add_lottery_number + '" size="10" id="times' + add_lottery_number + '"/><br/>';
            $("#add_text").append(trHtml);
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷抽奖 >> 奖品管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加商品</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/lottery/award/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            抽奖信息:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="hidden" name="lotteryid" value="${lottery.lotteryId}" size="20"/>
                            <input type="hidden" id="lottery_lotteryruleactiontype" name="lottery_lotteryruleactiontype"
                                   value="${lottery.lotteryRule.lotteryRuleActionType.code}" size="20"/>
                            <input type="text" name="lotteryname" value="${lottery.lotteryName}" size="20"
                                   readonly="readonly"/>
                            <input type="hidden" name="add_lottery_number" size="20"
                                   readonly="readonly" id="add_lottery_number"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖品名称:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="hidden" name="lotteryAwardId" value="${lotteryAward.lotteryAwardId}">
                            <input type="text" name="awardname" size="20" id="input_text_name"
                                   value="${lotteryAward.lotteryAwardName}"/>
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
                            奖品描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="awarddesc" id="textarea_desc" rows="5" cols="100"
                                      style="width: 100%;float: left"
                                      class="default_input_multiline">${lotteryAward.lotteryAwardDesc}</textarea>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            选择奖品级别:
                        </td>
                        <td height="1">
                            <select name="awardlevel" id="select_award_level">
                                <option value="">请选择</option>
                                <c:forEach var="level" items="${levelList}">
                                    <option value="${level.code}"
                                    <c:if test="${level.code==lotteryAward.lotteryAwardLevel}">selected="selected" </c:if>>
                                    <fmt:message key="lottery.award.level.${level.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(levelExist)>0}">
                                <span style="color: red" id="span_level_exist"><fmt:message key="${levelExist}"
                                                                                            bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>

                        <c:if test="${lottery.lotteryRule.lotteryRuleActionType.code==1}">
                            <td height="1" class="default_line_td">
                                中奖数字范围:
                            </td>
                            <td height="1" class="" width="10">
                                <input type="text" name="minrate" size="20" id="input_text_min"
                                       value="${lotteryAward.awardRule.min}"/>
                                &nbsp;——&nbsp;
                                <input type="text" name="maxrate" size="20" id="input_text_max"
                                       value="${lotteryAward.awardRule.max}"/>
                            </td>
                            <td height="1">*必填项</td>
                        </c:if>
                        <c:if test="${lottery.lotteryRule.lotteryRuleActionType.code==2}">
                            <td height="1" class="default_line_td">
                                请填写中奖时间范围:
                            </td>
                            <td height="1" class="" width="10">
                                <input type="text" name="minrate" size="40" id="input_text_min"
                                       onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/>
                                &nbsp;——&nbsp;
                                <input type="text" name="maxrate" size="40" id="input_text_max"
                                       onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/>
                            </td>
                            <td height="1">*必填项</td>
                        </c:if>
                        <c:if test="${lottery.lotteryRule.lotteryRuleActionType.code==3}">
                            <td height="1" class="default_line_td">
                                请填写中奖次数，用英文","分割:
                            </td>
                            <td height="1" class="" width="10">
                                 <textarea name="minrate" id="input_text_min" rows="5" cols="100"
                                           style="width: 100%;float: left" class="default_input_multiline">${lotteryAward.awardRule.min}</textarea>
                            </td>
                            <td height="1">*必填项</td>
                        </c:if>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(minRateError)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${minRateError}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            选择奖品类型:
                        </td>
                        <td height="1">
                            <select name="awardtype" id="select_award_type">
                                <option value="">请选择</option>

                                <c:forEach var="awardtype" items="${awardtypecollection}">
                                    <option value="${awardtype.code}"
                                    <c:if test="${awardtype.code==lotteryAward.lotteryAwardType.code}">selected="selected" </c:if>>
                                    <fmt:message key="lottery.award.type.${awardtype.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖品总数:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="awardamount" size="20" id="input_text_amount"
                                   value="${lotteryAward.lotteryAwardAmount}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            开始时间:
                        </td>
                        <td height="1">
                            <input id="createdate" name="createdate"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"
                                   value="${lotteryAward.createDate}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            结束时间:
                        </td>
                        <td height="1">
                            <input id="lastmodifydate" name="lastmodifydate"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"
                                   value="${lotteryAward.lastModifyDate}"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            奖品次数类型:
                        </td>
                        <td height="1" class="" width="10">
                            <select name="lotteryAwardRuleCountType" id="lotteryAwardRuleCountType">
                                <option value="">请选择</option>

                                <c:forEach items="${lotteryAwardRuleCountType}" var="type">
                                    <option value="${type.code}"
                                    <c:if test="${type.code==lotteryAward.awardRule.lotteryAwardRuleCountType.code}">selected="selected" </c:if>>
                                    <fmt:message key="lottery.lotteryAwardRuleCountType.${type.code}"
                                                 bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖品次数限制（奖品次数类型为按天或者按小时有效）:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="lottery_award_count_number" size="20" id="lottery_award_count_number"
                                   value="${lotteryAward.awardRule.text}"/>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            奖品次数限制（奖品次数类型为自定义时间段有效）:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="button" value="新增一行" onclick="addTr()">
                            </br>

                            <c:if test="${fn:length(lotteryAward.awardRule.countRule)==0}">
                                开始时间1：<input id="startTime1" name="startTime1"
                                             onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"
                                             size="15"/>
                                结束时间1：<input id="endTime1" name="endTime1"
                                             onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"
                                             size="15"/>
                                抽中次数1：<input type="text" name="times1" size="10" id="times1"/>
                            </c:if>

                            <c:forEach items="${lotteryAward.awardRule.countRule}" var="countRule" varStatus="status">
                                开始时间${status.index+1}：<input id="startTime${status.index+1}"
                                                             name="startTime${status.index+1}"
                                                             onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                             readonly="readonly"
                                                             value="${countRule.startTime}"
                                                             size="15"/>
                                结束时间${status.index+1}：<input id="endTime${status.index+1}"
                                                             name="endTime${status.index+1}"
                                                             onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                                             readonly="readonly"
                                                             value="${countRule.endTime}"
                                                             size="15"/>
                                抽中次数${status.index+1}：<input type="text" value="${countRule.times}"
                                                             name="times${status.index+1}" size="10"
                                                             id="times${status.index+1}"/>
                                <br/>
                            </c:forEach>

                            </br>
                            <div id="add_text">

                            </div>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            选择状态:
                        </td>
                        <td height="1">
                            <select name="validstatus" id="select_award_status">
                                <c:forEach var="status" items="${statuscollection}">
                                    <option value="${status.code}"
                                    <c:if test="${status.code==lotteryAward.validStatus.code}">selected="selected" </c:if>>
                                    <fmt:message key="lottery.award.validstatus.${status.code}"
                                                 bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">*必填项</td>
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