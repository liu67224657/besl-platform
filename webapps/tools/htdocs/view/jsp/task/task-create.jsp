<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <title>添加任务</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>

    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
                    $("input[type='checkbox']").attr("checked", true);
                    $("#checkall").click(function() {
                        if ($("#checkall").is(':checked')) {
                            $("input[name='taskplatform']").attr("checked", true);
                        } else {
                            $("input[name='taskplatform']").attr("checked", false);
                        }
                    });


                    var groupid = '${groupid}';
                    var groupTypeCode = '${taskgroup.type.code}';
                    //只有一天一次时才可以选择日期
                    $("#tasktype").change(function () {
                        var tasktype = $(this).val();
                        if (tasktype == 1) {
                            $(".repeat-date").show();
                        } else {
                            $(".repeat-date").hide();
                        }
                    });

                    var overtimes = '${overtimes}';
                    if (overtimes != '') {
                        $("#input_text_overtimes").val(overtimes);
                    }

                    var awardvalue = '${awardvalue}';
                    if (awardvalue != '') {
                        $("#input_text_awardvalue").val(awardvalue);
                    }

                    $('#form_submit').submit(function () {

                        var input_text_taskid = $("#input_text_taskid").val().trim();
                        var input_text_name = $("#input_text_name").val().trim();
                        var awardtype = $("#awardtype").val().trim();
                        var input_text_awardvalue = $("#input_text_awardvalue").val().trim();
                        var input_text_overtimes = $("#input_text_overtimes").val().trim();


                        if (input_text_taskid == '') {
                            alert("任务id不能为空");
                            return false;
                        }
                        if (input_text_name == '') {
                            alert("任务名称不能为空");
                            return false;
                        }

                        if (groupTypeCode == 'common' && input_text_taskid.length > 8) {
                            alert("一般任务的id不能超过8个字符");
                            return false;
                        } else if (groupTypeCode == 'sign' && input_text_taskid.length > 14) {
                            alert("签到任务id不能超过14个字符");
                            return false;
                        }

                        if (input_text_overtimes.length <= 0 || !isNumericString(input_text_overtimes)) {
                            alert("请正确填写完成次数");
                            return false;
                        }

                        if (awardtype == 0) {
                            if (!isNumericString(input_text_awardvalue)) {
                                alert("当奖励类型是虚拟货币时，请填写正确的奖励值");
                                return false;
                            }
                        }

                        var taskType = $('#tasktype').val();

                        var taskaction = $('#select_action').val();
                        if (taskaction.length == 0) {
                            $('#error_action').text('动作类型不能为空！！！！').attr("class", "fontcolor_dsp_hint");
                            return false;
                        }

                        //不是签到任务  才校验
                        if (groupTypeCode == 'common') {

                            var beginTime = $('#beginTime').val();
                            var endTime = $('#endTime').val();
                            if (beginTime == '') {
                                alert("请设置任务的开始时间,然后再点击提交!");
                                return false;
                            } else if (endTime == '') {
                                alert("请设置任务的结束时间,然后再点击提交!");
                                return false;
                            }

                            var sTime = new Date(beginTime.replace("-", "/").replace("-", "/")).getTime();
                            var eTime = new Date(endTime.replace("-", "/").replace("-", "/")).getTime();
                            if (eTime - sTime < 0) {
                                alert("结束时间需要大于开始时间");
                                return false;
                            }
                            //每天一次时，差值要小于24小时
                            if (taskType == '1' && eTime - sTime > 24 * 3600 * 1000) {
                                alert("一天一次时，结束时间不能超过开始时间24小时以上");
                                return false;

                            }


                        }

                        // 不是签到任务， 并且 是一天一次时 才校验
                        if (groupTypeCode == 'common' && $("#tasktype").val() == 1) {
                            var beginDate = $('#beginDate').val();
                            var endDate = $('#endDate').val();
                            if (beginDate == '') {
                                alert("请设置任务的[首次开始日期],然后再点击提交!");
                                return false;
                            }
                            if (endDate == '') {
                                alert("请设置任务的[末次开始日期],然后再点击提交!");
                                return false;
                            }

                            var sDate = new Date(beginDate.replace("-", "/").replace("-", "/")).getTime();
                            var eDate = new Date(endDate.replace("-", "/").replace("-", "/")).getTime();

                            if (eDate - sDate < 0) {
                                alert("[末次开始日期]需要大于等于[首次开始日期]");
                                return false;
                            }
                            if (beginTime.substr(0, 10) != beginDate) {
                                alert("首次开始日期和开始时间需要设置在同一天");
                                return false;
                            }
                            if (eDate - sDate > 1000 * 3600 * 24 * 31) {
                                alert("最多一次设置31天的数据");
                                return false;
                            }
                        }
                    });
                }
        );
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <c:if test="${taskgroup.appKey == '17yfn24TFexGybOF0PqjdY'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸任务管理</td>
        </c:if>
        <c:if test="${taskgroup.appKey == '08pkvrWvx5ArJNvhYf19kN'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> app任务管理 >> 优酷合作任务管理</td>
        </c:if>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加任务</span>
                    </td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/task/create" method="post" id="form_submit">
                <input type="hidden" name="groupid" value="${taskgroup.taskGroupId}"/>
                <input type="hidden" name="dateFilter" value="${dateFilter}"/>
                <input type="hidden" name="dateEndFilter" value="${dateEndFilter}"/>
                <input type="hidden" name="allFilter" value="${allFilter}"/>
                <input type="hidden" name="typeFilter" value="${typeFilter}"/>
                <table border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            所属任务组:
                        </td>
                        <td height="1" class="">
                            ${taskgroup.taskGroupName}
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台:
                        </td>
                        <td height="1" class="">
                            <input type="checkbox" id="checkall"/>全选&nbsp;
                            <input type="checkbox" name="taskplatform" value="0"/>iOS&nbsp;
                            <input type="checkbox" name="taskplatform" value="1"/>Android&nbsp;
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            所属appkey:
                        </td>
                        <td height="1" class="">
                            ${taskgroup.appKey}
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            任务ID：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="taskid" size="40" id="input_text_taskid" value="${taskid}"/> <span
                                style="color:red;">*必填 生成的格式是groupId.taskId ,一般任务的id不能超过8个字符，签到任务的id不能超过14个字符</span>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="taskname" size="40" id="input_text_name" value="${taskname}"/>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            任务动作：
                        </td>
                        <td height="1" class="" width="10">
                            <select name="action" id="select_action">
                                <option value="">请选择</option>
                                <c:forEach var="a" items="${actions}">
                                    <c:choose>
                                        <c:when test="${a.taskGroupType.code==taskgroup.type.code}">
                                            <option value="${a.code}"  <c:if
                                                    test="${a.code == action}"> selected="selected" </c:if> >
                                                <fmt:message key="task.action.type.${a.code}"
                                                             bundle="${def}"/></option>
                                        </c:when>
                                        <c:otherwise></c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select> *必填 动作类型
                        </td>
                        <td height="1" align="left" id="error_action"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            对象ID：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="directid" size="40" id="input_text_directid" value="${directid}"/>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            简介:
                        </td>
                        <td height="1" class="">
                            <textarea name="taskdesc" rows="8" cols="40" >${taskdesc}</textarea>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            分类:
                        </td>
                        <td height="1">
                            <select name="tasktype" id="tasktype">
                                <c:forEach items="${tasktypes}" var="type">
                                    <option value="${type.code}" <c:if test="${type.code == tasktype}"> selected="selected"</c:if>>
                                        <fmt:message key="joyme.task.type.${type.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            完成任务需要的次数:
                        </td>
                        <td height="1">
                            <input type="text" name="overtimes" id="input_text_overtimes" value="1"/>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖励类型:
                        </td>
                        <td height="1" class="">
                            <select name="awardtype" id="awardtype">
                                <c:forEach items="${awardtypes}" var="type">
                                    <option value="${type.code}" <c:if
                                            test="${type.code==awardtype}"> selected="selected"</c:if> >
                                        <fmt:message key="task.award.type.${type.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            奖励的VALUE:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="awardvalue" id="input_text_awardvalue" value="20"/> * 迷豆数量
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类型:
                        </td>
                        <td height="1" class="">
                            <select name="redirecttype" id="input_text_redir">
                                <c:forEach items="${redirectypes}" var="type">
                                    <option value="${type.code}" <c:if
                                            test="${type.code==redirecttype}"> selected="selected" </c:if>  >
                                        <fmt:message key="client.item.redirect.${type.code}"
                                                     bundle="${def}"/></option>
                                </c:forEach>
                            </select>* 任务列表中点击跳转到对应页面的类型
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转信息:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="redirecturi" id="input_text_redirecturi" value="${redirecturi}"/>
                            *任务列表中点击跳转到对应页面的类型
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否自动发送奖励:
                        </td>
                        <td height="1" class="">
                            <select name="autosendaward" id="select_autosendaward">
                                <option value="false">否</option>
                                <option value="true" <c:if test="${autosendaward}">selected="selected"</c:if>>是</option>
                            </select> * 默认为否。（签到任务等应该选择是）
                        </td>
                        <td height="1"></td>
                    </tr>
                    <%-- 只有签到任务没有开始时间和结束时间  --%>
                    <c:if test="${taskgroup.type.code =='common'}">
                        <tr>
                            <td height="1" class="default_line_td">
                                任务开始时间:
                            </td>
                            <td height="1" class="">
                                <input type="text" class="Wdate" onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" id="beginTime" name="beginTime" value="${beginTime}"/>*时间可精确到分钟
                            </td>
                            <td height="1"></td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td">
                                任务结束时间:
                            </td>
                            <td height="1" class="">
                                <input type="text" class="Wdate" onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" id="endTime" name="endTime" value="${endTime}"/>*时间可精确到分钟
                            </td>
                            <td height="1"></td>
                        </tr>
                        <tr class="repeat-date">
                            <td height="1" class="default_line_td">
                                首次任务的开始日期:
                            </td>
                            <td height="1" class="">
                                <input type="text" class="Wdate" onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" id="beginDate" name="beginDate" value="${beginDate}"/>
                                <span style="color:red">一天生成一条记录，包括[首次任务的开始日期]和[末次任务的开始日期];如果只想生成一条记录，请把两个日期设在同一天,最多设置离现在一个月的数据</span>
                            </td>
                            <td height="1"></td>
                        </tr>
                        <tr class="repeat-date">
                            <td height="1" class="default_line_td">
                                末次任务的开始日期:
                            </td>
                            <td height="1" class="">
                                <input type="text" class="Wdate" onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" id="endDate" name="endDate" value="${endDate}"/>
                                <span style="color:red">一天生成一条记录，包括[首次任务的开始日期]和[末次任务的开始日期];如果只想生成一条记录，请把两个日期设在同一天,最多设置离现在一个月的数据</span>
                            </td>
                            <td height="1"></td>
                        </tr>

                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td">
                            任务完成验证:
                        </td>
                        <td height="1" class="">
                            <select name="taskverityid">
                                <option value="0"
                                        <c:if test="${taskverityid==0}">selected="selected"</c:if> >按用户
                                </option>
                                <option value="1"
                                        <c:if test="${taskverityid==1}">selected="selected"</c:if> >按设备
                                </option>

                            </select> <span style="color:red">玩霸一般设成按用户即可,按设备是为优酷游戏中心准备的</span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            显示类型:
                        </td>
                        <td height="1" class="">
                            <select name="taskdisplaytype">
                                <option value="0"
                                        <c:if test="${taskdisplaytype==0}">selected="selected"</c:if> >默认
                                </option>
                                <option value="1"
                                        <c:if test="${taskdisplaytype==1}">selected="selected"</c:if> >新
                                </option>
                            </select> 任务列表的任务显示类型
                        </td>
                        <td height="1"></td>
                    </tr>
                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input type="submit" class="default_button" value="提交">
                            <input type="button" class="default_button" value="返回"
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