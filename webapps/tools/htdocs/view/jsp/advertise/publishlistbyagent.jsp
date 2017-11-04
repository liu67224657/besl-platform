<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,广告商—广告位列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
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
                        }
                        if(modifyValidStatus=="invalid"){
                            $("#"+str).siblings().eq(5).text("无效");
                            $("#"+str).siblings().eq(5).css("color","#808080");
                        }
                        if(modifyValidStatus=="removed"){
                            $("#"+str).siblings().eq(5).text("删除");
                            $("#"+str).siblings().eq(5).css("color","red");
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
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> 广告位管理</td>
    </tr>
    <tr>
        <td>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">广告商基本信息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0" style="table-layout: fixed;">
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td" width="4%">广告商名称：</td>
                    <td class="edit_table_value_td" width="10%">
                        ${agent.agentName}
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td" width="4%">是否有效：</td>
                    <td class="edit_table_value_td" width="10%">
                        <fmt:message key="def.validstatus.${agent.validStatus.code}.name" bundle="${def}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                    <td class="edit_table_value_td">
                        <fmt:formatDate value="${agent.createDate}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td align="right" class="edit_table_defaulttitle_td">更新时间：</td>
                    <td class="edit_table_value_td">
                        <fmt:formatDate value="${agent.updateDate}" pattern="yyyy-MM-dd"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" class="edit_table_defaulttitle_td">广告商描述：</td>
                    <td class="edit_table_value_td" colspan="3">
                        ${agent.agentDesc}
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table  width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <form action="" method="post" name="tabForm" target="tabMain" onSubmit="document.tabMain.location=this.action;return false;">
                    <tr>
                        <td height="22">
                            <input name="tab_1" type="submit" class="tab_noactive_button"
                                   value="广告位查询" title=""
                                   onClick="submitTab('tabForm','tab_',1,2,'/advertise/publish/publishlistbyagent?agentid=${agentId}');" />
                        </td>
                    </tr>
                </form>
                <tr>
                    <td height="2" class="default_line_td"></td>
                </tr>
                <tr>
                    <td style="height:100%" valign="top">
                        <iframe style="z-index: 1; width: 100%; height: 500px" name=tabMain src="" frameBorder=0 scrolling=auto ></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>
</body>
</html>
<script language="JavaScript">
    document.tabForm.tab_1.click();
</script>