<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,运营维护,分类管理,广告商管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>

    <script language="JavaScript" type="text/JavaScript">
        $().ready(function() {

            $('#add_button').click(function() {
                window.location.href = "/advertise/agent/addpage"
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
                $.post("/json/advertise/modifyCheck",{agentids:vaildState,modifyvalidstatus:modifyValidStatus}, function(data){
                    var jsonobj = eval("("+data+")");
                    var jsonAgentId = jsonobj.result;
                    $.each(jsonAgentId,function(i){
                        var str = jsonAgentId[i];
                        if(modifyValidStatus=="valid"){
                            $("#"+str).siblings().eq(2).text("有效");
                            $("#"+str).siblings().eq(2).css("color","green");
                            $("#"+str).siblings().eq(3).children(0).eq(0).css("color","blue");
                        }
                        if(modifyValidStatus=="invalid"){
                            $("#"+str).siblings().eq(2).text("无效");
                            $("#"+str).siblings().eq(2).css("color","#808080");
                            $("#"+str).siblings().eq(3).children(0).eq(0).css("color","blue");
                        }
                        if(modifyValidStatus=="removed"){
                            $("#"+str).siblings().eq(2).text("删除");
                            $("#"+str).siblings().eq(2).css("color","red");
                            $("#"+str).siblings().eq(3).children(0).eq(0).css("color","#808080");
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
            width: 370px;
        }
    </style>
</head>

<body >
<form id="form_agentList" name="form_agentList" action="/advertise/agent/list" method="post">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 广告管理 >> 广告商管理</td>
    </tr>
    <tr>
        <td>     <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">广告商查询</td>
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
                                    <td width="3%" align="center">搜索条件</td>
                                    <td align="right" class="edit_table_defaulttitle_td" width="4%">广告商名称：</td>
                                    <td class="edit_table_value_td" width="10%">
                                        <input name="agentname" class="default_input_singleline" type="text" value="${agentName}"/>
                                    </td>
                                    <td align="right" class="edit_table_defaulttitle_td" width="4%">有效状态：</td>
                                    <td class="edit_table_value_td" width="10%">
                                        <select name="validstatus">
                                            <option value="" <c:if test="${validStatus eq ''}">selected</c:if>>全部</option>
                                            <option value="valid" <c:if test="${validStatus eq 'valid'}">selected</c:if>>有效</option>
                                            <option value="invalid" <c:if test="${validStatus eq 'invalid'}">selected</c:if>>无效</option>
                                            <option value="removed" <c:if test="${validStatus eq 'removed'}">selected</c:if>>删除</option>
                                        </select>
                                    </td>
                                    <td width="4%" align="center">
                                        <input name="Submit" type="submit" class="default_button" value="搜索">
                                        <input id="reset" name="Reset" type="button" class="default_button" value="重置">
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
                    <td class="list_table_header_td">广告商列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
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
                    <td width="4%">
                        <p:privilege name="/advertise/agent/addpage">
                            <input class="default_button" type="button" value="添加广告商"  id="add_button"/>
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
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
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="1%">选择</td>
                    <td nowrap align="left" width="11%">广告商名称</td>
                    <td nowrap align="left" width="10%">广告商描述</td>
                    <td nowrap align="center" width="2%">是否有效</td>
                    <td nowrap align="center" width="2%" >操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${agentList.size() > 0}">
                        <%--<form action="/viewline/categorybatchstatus" method="POST" name="batchform">--%>
                        <c:forEach items="${agentList}" var="agent" varStatus="st">
                            <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when>
                        <c:otherwise>list_table_even_tr</c:otherwise>
                        </c:choose>">
                                <td id="${agent.agentId}" align="center">
                                    <input type="checkbox" name="" value="${agent.agentId}">
                                </td>
                                <td align="left">
                                    <div title=" ${agent.agentName}">
                                        <a href="/advertise/publish/listbyagent?agentid=${agent.agentId}">${agent.agentName}</a>
                                    </div>

                                </td>
                                <td align="left">
                                    <div title="${agent.agentDesc}">${agent.agentDesc} </div>
                                </td>
                                <td align="center">
                                    <fmt:message key="def.validstatus.${agent.validStatus.code}.name" bundle="${def}"/>
                                </td>
                                <td nowrap align="center">
                                    <p:privilege name="/advertise/agent/modifypage">
                                            <a href="/advertise/agent/modifypage?agentid=${agent.agentId}"
                                                    <c:if test="${agent.validStatus.code=='removed'}">style="color: #808080"</c:if>>编辑</a>
                                    </p:privilege>
                                    <p:privilege name="/advertise/publish/listbyagent">
                                        <a href="/advertise/publish/listbyagent?agentid=${agent.agentId}">详情</a>
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
                    <td align="center"  class="default_line_td">
                        <LABEL>
                            <pg:pager url="/advertise/agent/list"
                                      items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber"
                                      scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="agentname" value="${agentName}"/>
                                <pg:param name="validstatus" value="${validStatus}"/>
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