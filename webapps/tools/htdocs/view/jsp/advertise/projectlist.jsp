<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,运营维护,分类管理,广告项目管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>

    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            doOnLoad();
            $('#add_button').click(function() {
                window.location.href = "/advertise/project/addpage"
            });
            $("#reset").click(function(){
                $(".default_input_singleline").val("");
                $(".edit_table_value_td select").val("");

            })
            $("#selectall").click(function(){
                if($("#selectall").attr("checked")==false){
                    $("input[type='checkbox']").each(function(){
                        $(this).attr("checked","");
                    })
                }else{
                    $("input[type='checkbox']").each(function(){
                        $(this).attr("checked","checked");
                    })
                    $("#uncheck").attr("checked","");
                }
            })
            $("#uncheck").click(function(){
                $("input[type='checkbox']").each(function(){
                    if($(this).attr("checked")==true){
                        $(this).attr("checked","");
                    }else{
                        $(this).attr("checked","checked");
                    }
                })
                if($(this).attr("checked")==true){
                    $(this).attr("checked","");
                }else{
                    $(this).attr("checked","checked");
                }
                $("#selectall").attr("checked","");
            })
            $("#modifyCheck").click(function(){
                var vaildState="";
                $("input[type='checkbox']").each(function(){
                    if($(this).attr("checked")==true){
                        vaildState=vaildState+$(this).val()+",";
                    }
                });
                var modifyValidStatus=$("#modifyvalidstatus").val();
                $.post("/json/advertise/modifyCheck",{projectids:vaildState,modifyvalidstatus:modifyValidStatus}, function(data){
                    var jsonobj = eval("("+data+")");
                    var jsonProjectId = jsonobj.result;
                    $.each(jsonProjectId,function(i){
                        var str = jsonProjectId[i];
                        if(modifyValidStatus=="valid"){
                            $("#"+str).siblings().eq(4).text("有效");
                            $("#"+str).siblings().eq(4).css("color","green");
                            $("#"+str).siblings().eq(5).children(0).eq(0).css("color","blue");
                        }
                        if(modifyValidStatus=="invalid"){
                            $("#"+str).siblings().eq(4).text("无效");
                            $("#"+str).siblings().eq(4).css("color","#808080");
                            $("#"+str).siblings().eq(5).children(0).eq(0).css("color","blue");
                        }
                        if(modifyValidStatus=="removed"){
                            $("#"+str).siblings().eq(4).text("删除");
                            $("#"+str).siblings().eq(4).css("color","red");
                            $("#"+str).siblings().eq(5).children(0).eq(0).css("color","#808080");
                        }
                    })
                        $("input[type='checkbox']").each(function(){
                            $(this).attr("checked","");
                        })
                });

            })

        })
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate","statEndDate"]);
        }
    </script>
    <style type="text/css">
        td,td div{
            overflow: hidden;
            text-overflow:ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;
        }
        td div{
            width: 250px;
        }
    </style>
</head>

<body>
<form id="projectlistForm" name="projectlistForm" action="/advertise/project/list" method="post">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 广告管理 >> 广告项目管理</td>
</tr>
<tr>
    <td>
        <br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">广告项目查询</td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
            <tr>
                <td>
                    <table width="100%" border="0" cellspacing="1" cellpadding="0" style="table-layout: fixed;">
                        <tr>
                            <td width="3%" align="center" rowspan="2">搜索条件</td>
                            <td align="right" class="edit_table_defaulttitle_td" width="4%">广告项目名称：</td>
                            <td class="edit_table_value_td" width="10%">
                                <input name="projectname" class="default_input_singleline" type="text" value="${projectName}"/>
                            </td>
                            <td align="right" class="edit_table_defaulttitle_td" width="4%">统计有效日期：</td>
                            <td name="" class="edit_table_value_td" width="10%">
                                <input  name="statenddate" type="text" class="default_input_singleline" size="8" value="${statEndDate}" id="statEndDate">
                            </td>
                            <td width="4%" align="center" rowspan="2">
                                <input name="Submit" type="submit" class="default_button" value="搜索">
                                <input id="reset" name="Reset" type="button" class="default_button" value="重置">
                            </td>
                        </tr>
                        <tr>
                            <td align="right" class="edit_table_defaulttitle_td">有效日期：</td>
                            <td class="edit_table_value_td">
                                <input id="startDate" name="startdate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${startDate}" >
                                -
                                <input id="endDate" name="enddate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${endDate}" >
                            </td>
                            <td align="right" class="edit_table_defaulttitle_td">有效状态：</td>
                            <td class="edit_table_value_td">
                                <select name="validstatus">
                                    <option value="" <c:if test="${validStatus eq ''}">selected</c:if>>全部</option>
                                    <option value="valid" <c:if test="${validStatus eq 'valid'}">selected</c:if>>有效</option>
                                    <option value="invalid" <c:if test="${validStatus eq 'invalid'}">selected</c:if>>无效</option>
                                    <option value="removed" <c:if test="${validStatus eq 'removed'}">selected</c:if>>删除</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" colspan="6" class="default_line_td"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td height="100%" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">广告项目列表</td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" width="100%" class="default_line_td" colspan="2"></td>
            </tr>
            <tr class="toolbar_tr">
                <td width="28%">
                    <input type="checkbox" name="selectall" id="selectall" value="1">全选
                    <input  type="checkbox" name="uncheck" id="uncheck" value="1" >反选
                    <select name="modifyvalidstatus" id="modifyvalidstatus">
                        <option value="valid" <c:if test="${validStatus eq 'valid'}">selected</c:if>>有效</option>
                        <option value="invalid" <c:if test="${validStatus eq 'invalid'}">selected</c:if>>无效</option>
                        <option value="removed" <c:if test="${validStatus eq 'removed'}">selected</c:if>>删除</option>
                    </select>
                    <input type="button" name="modifyCheck" id="modifyCheck" value="修改状态"/>
                </td>
                <td width="4%">
                    <p:privilege name="/advertise/project/addpage">
                        <input type="button" value="添加广告项目" class="default_button" id="add_button"/>
                    </p:privilege>
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="1" cellpadding="0" >
            <tr>
                <td height="1" colspan="7" class="default_line_td"></td>
            </tr>
            <c:if test="${errorMsgMap['error']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0" style="table-layout: fixed;">
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['error']}" bundle="${error}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                </table>
            </c:if>
            <tr class="list_table_title_tr ">
                <td nowrap align="center" width="1%">选择</td>
                <td nowrap align="left" width="7%">广告项目名称</td>
                <td nowrap align="left" width="7%">广告项目描述</td>
                <td nowrap align="center" width="4%">有效日期</td>
                <td nowrap align="center" width="3%">统计有效日期</td>
                <td nowrap align="center" width="2%">是否有效</td>
                <td nowrap align="center" width="2%">操作</td>
            </tr>
            <tr>
                <td height="1" colspan="9" class="default_line_td"></td>
            </tr>
            <c:choose>
                <c:when test="${projectList.size() > 0}">
                    <%--<form action="/viewline/categorybatchstatus" method="POST" name="batchform">--%>
                    <c:forEach items="${projectList}" var="project" varStatus="st">
                        <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when>
                        <c:otherwise>list_table_even_tr</c:otherwise>
                        </c:choose>">
                            <td align="center" id="${project.projectId}">
                                <input type="checkbox" name="" value="${project.projectId}">
                            </td>
                            <td align="left">
                                <div title="${project.projectName}">
                                    <a href="/advertise/publish/listbyproject?projectid=${project.projectId}">${project.projectName}</a>
                                </div>
                            </td>
                            <td align="left">
                                <div title="${project.projectDesc}">${project.projectDesc}</div>
                            </td>
                            <td align="center">
                                <!--from：--><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd"/>
                                <!--<br/>&nbsp;&nbsp;&nbsp;&nbsp;
                                to：-->
                                <c:if test="${project.startDate!=null}">
                                    至
                                    <c:if test="${project.endDate==null}">
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    </c:if>
                                </c:if>
                                <fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td align="center">
                                <!--
                                        from：<fmt:formatDate value="${project.statStartDate}" pattern="yyyy-MM-dd"/>
                                        <br/>&nbsp;&nbsp;&nbsp;&nbsp;
                                        to：--><fmt:formatDate value="${project.statEndDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td align="center">
                                <fmt:message key="def.validstatus.${project.validStatus.code}.name"  bundle="${def}"/>
                            </td>
                            <td nowrap align="center">
                                <p:privilege name="/advertise/project/modifypage">
                                    <a <c:if test="${project.validStatus.code=='removed'}">style="color: #808080"</c:if> href="/advertise/project/modifypage?projectid=${project.projectId}">编辑</a>
                                </p:privilege>
                                <p:privilege name="/advertise/publish/listbyproject">
                                    <a href="/advertise/publish/listbyproject?projectid=${project.projectId}">详情</a>
                                </p:privilege>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td align="center" class="default_line_td">
                    <LABEL>
                    <pg:pager url="/advertise/project/list" items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber" scope="request">
                        <pg:param name="maxPageItems" value="${page.pageSize}"/>
                        <pg:param name="items" value="${page.totalRows}"/>
                        <pg:param name="validstatus" value="${validStatus}"/>
                        <pg:param name="projectname" value="${projectName}"/>
                        <pg:param name="statenddate" value="${statEndDate}"/>
                        <pg:param name="startdate" value="${startDate}"/>
                        <pg:param name="enddate" value="${endDate}"/>
                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                    </pg:pager>
                    </LABEL>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</form>
</body>
</html>