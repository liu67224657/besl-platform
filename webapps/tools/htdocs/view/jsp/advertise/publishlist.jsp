<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,广告项目-广告位列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            $('#add_button').click(function() {
                var agentId='${agent.agentId}';
                var projectId='${project.projectId}';
                window.location.href = "${ctx}/advertise/publish/addpage?source=publish";
            });

            $('input[type=button][name=edit_button]').click(function() {
                var publishid = $(this).attr('data-publishid');
                window.location.href = "" + publishid;
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
                $.post("/json/advertise/modifyCheck",{publishids:vaildState,modifyvalidstatus:modifyValidStatus}, function(data){
                    var jsonobj = eval("("+data+")");
                    var jsonPublishId = jsonobj.result;
                    $.each(jsonPublishId,function(i){
                        var str = jsonPublishId[i];
                        if(modifyValidStatus=="valid"){
                            $("#"+str).siblings().eq(5).text("有效");
                            $("#"+str).siblings().eq(5).css("color","green");
                            $("#"+str).siblings().eq(6).children(0).eq(0).css("color","blue");
                        }
                        if(modifyValidStatus=="invalid"){
                            $("#"+str).siblings().eq(5).text("无效");
                            $("#"+str).siblings().eq(5).css("color","#808080");
                            $("#"+str).siblings().eq(6).children(0).eq(0).css("color","blue");
                        }
                        if(modifyValidStatus=="removed"){
                            $("#"+str).siblings().eq(5).text("删除");
                            $("#"+str).siblings().eq(5).css("color","red");
                            $("#"+str).siblings().eq(6).children(0).eq(0).css("color","#808080");
                        }
                    })
                    $("input[type='checkbox']").each(function(){
                        $(this).attr("checked","");
                    })
                });

            })
        })
    </script>
    <style type="text/css">
        td,td div{
            overflow: hidden;
            text-overflow:ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;
        }
        td div{
            width: 160px;
        }
    </style>
</head>

<body>
<form action="/advertise/publish/list" method="post">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 广告管理 >> 广告位管理
    </td>
</tr>
<tr>
    <td>
        <br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">广告位查询</td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
            <tr><td>
            <table width="100%" border="0" cellspacing="1" cellpadding="0" style="table-layout: fixed;">
                <tr>
                    <td width="3%" align="center" rowspan="2">搜索条件</td>
                    <td align="right" class="edit_table_defaulttitle_td" width="4%">广告发布名称：</td>
                    <td class="edit_table_value_td" width="10%">
                        <input class="default_input_singleline" type="text" name="publishname" value="${publishName}"/>
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td" width="4%">广告商名称：</td>
                    <td class="edit_table_value_td" width="10%">
                        <%--<input class="default_input_singleline" type="text" name="agentname" value="${agentName}"/>--%>
                        <select name="agentid" style="width: 160px">
                            <option value="">全部</option>
                            <c:forEach items="${agentList}" var="agent">
                                <option <c:if test="${agentId==agent.agentId}">selected</c:if> value="${agent.agentId}">${agent.agentName}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td  width="4%" align="center" rowspan="2">
                        <input name="Submit" type="submit" class="default_button" value="搜索">
                        <input id="reset" name="Reset" type="button" class="default_button" value="重置">
                    </td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td" width="4%">广告项目名称：</td>
                    <td class="edit_table_value_td" width="10%">
                        <%--<input class="default_input_singleline" type="text" name="publishname" value="${publishName}"/>--%>
                            <select name="projectid" style="width: 160px">
                                <option value="">全部</option>
                                <c:forEach items="${projectList}" var="project">
                                    <option <c:if test="${projectId==project.projectId}">selected</c:if> value="${project.projectId}">${project.projectName}</option>
                                </c:forEach>
                            </select>
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td">是否有效：</td>
                    <td class="edit_table_value_td" >
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
    <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr class="list_table_header_td">
                <td class="list_table_header_td">广告位列表</td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td height="100%" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td" colspan="2"></td>
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
                <td width="4%" class="list_table_header_td">
                    <input class="default_button" type="button" id="add_button" value="添加广告发布">
                    <%--<a type="button" href="/advertise/publish/addpage?source=publish">添加广告发布</a>--%>
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="1" cellpadding="0">
            <tr>
                <td height="1" colspan="8" class="default_line_td"></td>
            </tr>
            <c:if test="${errorMsgMap['error']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
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
            <tr class="list_table_title_tr">
                <td nowrap align="center" width="2%">选择</td>
                <td nowrap align="left" width="8%">广告发布名称</td>
                <td nowrap align="left" width="8%">广告发布描述</td>
                <td nowrap align="left" width="8%">广告商名称</td>
                <td nowrap align="left" width="12%">广告项目名称</td>
                <td nowrap align="left" width="8%">跳转地址</td>
                <td nowrap align="center" width="3%">是否有效</td>
                <td nowrap align="center" width="3%">操作</td>
            </tr>
            <tr>
                <td height="1" colspan="11" class="default_line_td"></td>
            </tr>
            <c:choose>

                <c:when test="${publishList.size() > 0}">
                    <c:forEach items="${publishList}" var="publish" varStatus="st">
                        <tr class="
              <c:choose>
              <c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when>
              <c:otherwise>list_table_even_tr</c:otherwise>
              </c:choose>">
                            <td align="center" id="${publish.publishId}">
                                <input type="checkbox" name="" value="${publish.publishId}">
                            </td>
                            <td align="left">
                                <div title="${publish.publishName}">
                                    <a href="/advertise/publish/detail?publishid=${publish.publishId}&source=publish&projectid=${publish.projectId}&agentid=${publish.agentId}">
                                    ${publish.publishName}
                                    </a>
                                </div>
                            </td>
                            <td align="left">
                                <div title="${publish.publishDesc}">${publish.publishDesc}</div>
                            </td>
                            <td align="left">
                                    <%--${URL_WWW}/click/${publish.publishId}--%>
                                         <div title="${publish.advertiseAgent.agentName}"
                                              <c:if test="${publish.advertiseAgent.validStatus.code=='valid'}">style="color: green;" </c:if>
                                              <c:if test="${publish.advertiseAgent.validStatus.code=='invalid'}"> style="color:#808080;"</c:if>
                                              <c:if test="${publish.advertiseAgent.validStatus.code=='removed'}">style="color: red;"</c:if> >
                                              ${publish.advertiseAgent.agentName}
                                         </div>
                            </td>
                            <td>
                                <div title="${publish.advertiseProject.projectName}"
                                    <c:if test="${publish.advertiseProject.validStatus.code=='valid'}">style="color: green;" </c:if>
                                    <c:if test="${publish.advertiseProject.validStatus.code=='invalid'}"> style="color:#808080;"</c:if>
                                    <c:if test="${publish.advertiseProject.validStatus.code=='removed'}">style="color: red;"</c:if> >
                                     ${publish.advertiseProject.projectName}
                                </div>
                            </td>
                            <td align="left">
                                <div title="${publish.redirectUrl}">${publish.redirectUrl}</div>
                            </td>
                            <td align="center">
                                <fmt:message key="def.validstatus.${publish.validStatus.code}.name" bundle="${def}"/>
                            </td>
                            <td nowrap align="center">
                                <p:privilege name="/advertise/publish/modifypage">
                                    <a <c:if test="${publish.validStatus.code=='removed'}">style="color: #808080"</c:if> href="/advertise/publish/modifypage?publishid=${publish.publishId}&source=publish">编辑</a>
                                </p:privilege>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="8" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td  align="center" class="default_line_td">
                    <LABEL>
                        <pg:pager url="/advertise/publish/list" items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}"
                                  export="offset, currentPageNumber=pageNumber" scope="request">
                            <pg:param name="maxPageItems" value="${page.pageSize}"/>
                            <pg:param name="items" value="${page.totalRows}"/>
                            <pg:param name="publishname" value="${publishName}"/>
                            <pg:param name="validstatus" value="${validStatus}"/>
                            <pg:param name="projectid" value="${projectId}"/>
                            <pg:param name="agentid" value="${agentId}"/>
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