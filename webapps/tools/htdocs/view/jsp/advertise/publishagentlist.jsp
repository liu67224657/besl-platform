<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,广告商—广告位列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {
            $('#add_button').click(function() {
                var agentId='${agentId}';
                window.location.href = "/advertise/publish/addpage?agentid=" + agentId+"&source=${source}";
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
<form name="publishagentform" action="/advertise/publish/publishlistbyagent" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> 广告位管理</td>
    </tr>
<tr>
    <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">查询条件</td>
            </tr>
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="agentid" value="${agentId}"/>
                    <table width="100%" border="0" cellspacing="1" cellpadding="0" style="table-layout: fixed;">
                        <tr>
                            <td width="3%" align="center" rowspan="2">搜索条件</td>
                            <td align="right" class="edit_table_defaulttitle_td" width="4%">广告发布名称：</td>
                            <td class="edit_table_value_td" width="10%">
                                <input name="publishname" class="default_input_singleline" type="text" value="${publishName}"/>
                            </td>
                            <td align="right" class="edit_table_defaulttitle_td" width="4%">广告项目名称：</td>
                            <td class="edit_table_value_td" width="10%">
                                <%--<input class="default_input_singleline" type="text" name="projectname" value="${projectName}"/>--%>
                                <select name="projectid" style="width: 160px">
                                    <option value="">全部</option>
                                    <c:forEach items="${projectList}" var="project">
                                        <option value="${project.projectId}" <c:if test="${projectId == project.projectId}">selected</c:if>>${project.projectName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="4%" align="center" rowspan="2">
                                <input name="Submit" type="submit" class="default_button" value="搜索">
                                <input name="Reset" type="button" class="default_button" value="重置">
                            </td>
                        </tr>
                        <tr>
                            <td align="right" class="edit_table_defaulttitle_td">是否有效：</td>
                            <td class="edit_table_value_td" colspan="3">
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
                    <input type="button" value="添加广告发布" class="default_button" id="add_button"/>
                    <%--<a style="text-decoration: none" href="/advertise/publish/addpage?agentid=${agentId}&source=agent">--%>
                    <%--添加广告发布</a>--%>
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
                <td nowrap align="left" width="8%">广告项目名称</td>
                <td nowrap align="left" width="12%">广告发布链接</td>
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
                                    <a href="/advertise/publish/detail?publishid=${publish.publishId}&source=agent&projectid=${publish.projectId}&agentid=${publish.agentId}" <c:if test="${publish.validStatus.code!='removed'}">target="incFrame"</c:if>>
                                            ${publish.publishName}
                                    </a>
                                </div>
                            </td>
                            <td align="left">
                                <div title="${publish.publishDesc}">${publish.publishDesc}</div>
                            </td>
                            <td><div title="${publish.advertiseProject.projectName}"
                                     <c:if test="${publish.advertiseProject.validStatus.code=='valid'}">style="color: green;" </c:if>
                                    <c:if test="${publish.advertiseProject.validStatus.code=='invalid'}"> style="color:#808080;"</c:if>
                                     <c:if test="${publish.advertiseProject.validStatus.code=='removed'}">style="color: red;"</c:if> >
                                    ${publish.advertiseProject.projectName}
                            </div></td>
                            <td align="left">
                                    ${URL_WWW}/click/${publish.publishId}
                            </td>
                            <td align="left">
                                <div title="${publish.redirectUrl}">${publish.redirectUrl}</div>
                            </td>
                            <td align="center">
                                <fmt:message key="def.validstatus.${publish.validStatus.code}.name" bundle="${def}"/>
                            </td>
                            <td nowrap align="center">
                                <a <c:if test="${publish.validStatus.code=='removed'}">style="color: #808080"</c:if> href="/advertise/publish/modifypage?publishid=${publish.publishId}&source=agent&modifyagentid=${publish.agentId}&modifyprojectid=${publish.projectId}">编辑</a>
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
                        <pg:pager url="/advertise/publish/listbyagent"
                                  items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber"
                                  scope="request">
                            <pg:param name="maxPageItems" value="${page.pageSize}"/>
                            <pg:param name="items" value="${page.totalRows}"/>
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