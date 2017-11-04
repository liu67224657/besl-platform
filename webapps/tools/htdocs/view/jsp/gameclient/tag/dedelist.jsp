<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<%@page import="com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>文章列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function() {
            $("td[name='read_num']").each(function() {
                var read_num = $(this).html();
                if (read_num < 3000) {
                    $(this).css("color", "red");
                }
            });

            $("#checkall").click(function() {
                if ($("#checkall").attr("checked") == false) {
                    $("input[name='tdid']").each(function() {
                        $(this).attr("checked", "");
                    })
                } else {
                    $("input[name='tdid']").each(function() {
                        $(this).attr("checked", "checked");
                    })
                }
            });

            $('#batchpublish').click(function(){
                if($("input[name='tdid']:checked").length <= 0){
                    alert("至少选择一个");
                    return false;
                }

                if(!confirm("确定要批量发布吗？")){
                    return false;
                }

                var block = false;
                var idArr = new Array();
                $("input[name='tdid']:checked").each(function() {
                    var idx = $(this).attr('id').replace('tdid_', '');
                    if($('#removestatus_'+idx).val() != 'valid'){
                        idArr.push($(this).val());
                    }else{
                        block = true;
                        return false;
                    }
                });
                if(block){
                    alert("已发布的文章不能再次发布");
                    return false;
                }
                var tagid = "${tagid}";
                var platform = "${platform}";
                var title = "${title}";
                var removestaus="${removestaus}";
                window.location.href="/gameclient/tag/dede/batchpublish?tagid="+tagid+"&platform="+platform+"&title="+title+"&removestaus="+removestaus+"&ids="+idArr.join(",")+"&updatestaus=valid&pageStartIndex=${page.startRowIdx}";
            });

            $('#batchremove').click(function(){
                if($("input[name='tdid']:checked").length <= 0){
                    alert("至少选择一个");
                    return false;
                }
                if(!confirm("确定要批量删除吗？")){
                    return false;
                }

                var block = false;
                var idArr = new Array();
                $("input[name='tdid']:checked").each(function() {
                    var idx = $(this).attr('id').replace('tdid_', '');
                    if($('#removestatus_'+idx).val() != 'remove'){
                        idArr.push($(this).val());
                    }else{
                        block = true;
                        return false;
                    }
                });
                if(block){
                    alert("已删除的文章不能重复删除");
                    return false;
                }
                var tagid = "${tagid}";
                var platform = "${platform}";
                var title = "${title}";
                var removestaus="${removestaus}";
                window.location.href="/gameclient/tag/dede/batchpublish?tagid="+tagid+"&platform="+platform+"&title="+title+"&removestaus="+removestaus+"&ids="+idArr.join(",")+"&updatestaus=removed&pageStartIndex=${page.startRowIdx}";
            });

            $('#batchtimer').click(function(){
                if($("input[name='tdid']:checked").length <= 0){
                    alert("至少选择一个");
                    return false;
                }
                var pulishtime = $('#starttime').val();
                if(pulishtime.length <= 0){
                    alert("请选择定时时间");
                    return false;
                }
                var now = new Date();
                var date = new Date(pulishtime.replace("-", "/").replace("-", "/"));
                if(date.getTime() < (now.getTime() + 30*60*1000)){
                    alert("延迟时间最少30分钟");
                    return false;
                }

                var block = false;
                var idArr = new Array();
                $("input[name='tdid']:checked").each(function() {
                    var idx = $(this).attr('id').replace('tdid_', '');
                    if($('#removestatus_'+idx).val() == 'invalid'){
                        idArr.push($(this).val());
                    }else{
                        block = true;
                        return false;
                    }
                });
                if(block){
                    alert("已删除或已发布的文章不能定时发布");
                    return false;
                }
                var tagid = "${tagid}";
                var platform = "${platform}";
                var title = "${title}";
                var removestaus="${removestaus}";

                window.location.href="/gameclient/tag/dede/batchtimer?tagid="+tagid+"&platform="+platform+"&title="+title+"&removestaus="+removestaus+"&ids="+idArr.join(",")+"&pulishtime="+pulishtime;
            });
        });


        function del(id, updatestatus, archiveId, contenttype) {
            var message = "你真的确定要删除吗?";
            if (updatestatus == "valid") {
                message = "你真的发布吗?";
            } else if (updatestatus == "invalid") {
                message = "你真的预发布吗?";
            }
            var gnl = confirm(message);
            if (!gnl) {
                return false;
            }
            window.location.href = "/gameclient/tag/dede/delete?platform=${platform}&title=${title}&tagid=${tagid}&removestaus=${removestaus}&updatestatus=" + updatestatus + "&id=" + id + "&archiveId=" + archiveId + "&contenttype=" + contenttype;
        }

        function sort(title,startRowIdx,sort,id,tagid,archiveId,platform,removestaus){
            $.post("/gameclient/tag/dede/sort", {
                title: title,
                startRowIdx: startRowIdx,
                desc: sort,
                id:id,
                tagid:tagid,
                archiveId:archiveId,
                platform:platform,
                removestaus:removestaus
            }, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return;
                } else {
                    var result = resMsg.result;
                    if(result == null){
                        return;
                    }
                    if(sort == 'up'){
                        var item = $('#tr_'+result.oneid).clone();
                        $('#tr_'+result.oneid).remove();
                        $('#tr_'+result.twoid).before(item);
                    }else{
                        var item = $('#tr_'+result.oneid).clone();
                        $('#tr_'+result.oneid).remove();
                        $('#tr_'+result.twoid).after(item);
                    }
                }
            });
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 标签管理</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">文章列表</td>
    </tr>
</table>
    <table>
        <tr>
            <td><a href="/gameclient/tag/dede/list?tagid=${tagid}"><input type="button" class="default_button" name="button" value="全部列表"/></a></td>
            <td><a href="/gameclient/tag/dede/timerlist?tagid=${tagid}"><input type="button" class="default_button" name="button" value="定时发布列表"/></a></td>
        </tr>
    </table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="12" class="error_msg_td">
                    ${errorMsg}
            </td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form action="/gameclient/tag/dede/list" method="post">
                <input type="hidden" name="tagid" value="${tagid}" id="input_tagid"/>
                <table width="100%">
                    <tr>
                        <td height="1" class="default_line_td">
                            标签名称:
                        </td>
                        <td height="1">
                            <input type="text" name="title" class="default_button" size="30" value="${title}"/>
                        </td>
                        <td>
                            平台：<select name="platform" id="platform">
                            <option value="" <c:if test="${empty platform}">selected="selected"</c:if>>全部</option>
                            <option value="0"
                                    <c:if test="${platform=='0'}">selected="selected"</c:if> >IOS
                            </option>
                            <option value="1" <c:if test="${platform=='1'}">selected="selected"</c:if>>Android</option>
                        </select>
                        </td>
                        <td>
                            是否删除：<select name="removestaus" id="removestaus">
                            <option value=""
                                    <c:if test="${removestaus==''}">selected="selected"</c:if> >默认
                            </option>
                            <option value="invalid" <c:if test="${removestaus=='invalid'}">selected="selected"</c:if>>
                                预发布
                            </option>
                            <option value="valid" <c:if test="${removestaus=='valid'}">selected="selected"</c:if>>发布
                            </option>
                            <option value="removed" <c:if test="${removestaus=='removed'}">selected="selected"</c:if>>删除
                            </option>
                        </select>
                        </td>
                        <td>
                            <input type="submit" name="button" class="default_button" value="查询"/>
                        </td>
                        <td>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" >
        <tr class="toolbar_tr">
            <td width="28%">
                <input type="checkbox" name="checkall" id="checkall">全选
                <input type="button" id="batchpublish" value="批量发布" class="default_button"/>
                <input type="button" id="batchremove" value="批量删除" class="default_button"/>
                &nbsp;&nbsp;
                <input type="text" class="Wdate"
                       onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:00'})"
                       readonly="readonly" name="starttime" id="starttime"/>
                <input type="button" id="batchtimer" value="定时发布" class="default_button"/>
            </td>
            <td width="4%">

            </td>
        </tr>
    </table>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="11" class="default_line_td"></td>
</tr>
<tr class="list_table_title_tr">
    <td nowrap align="center"></td>
    <td nowrap align="center">文章ID</td>
    <td nowrap align="center">查看评论</td>
    <td nowrap align="center">标题</td>
    <td nowrap align="center">排序操作</td>
    <td nowrap align="center">快速排序</td>
    <td nowrap align="center">发布时间</td>
    <td nowrap align="center">类型</td>
    <td nowrap align="center">阅读数</td>
    <td nowrap align="center">赞数</td>
    <td nowrap align="center">平台</td>
    <td nowrap align="center">是否删除</td>

    <td nowrap align="center">操作</td>
</tr>
<tr>
    <td height="1" colspan="11" class="default_line_td"></td>
</tr>
<c:choose>
    <c:when test="${list.size() > 0}">
        <c:forEach items="${list}" var="dto" varStatus="st">
            <tr id="tr_${dto.id}"
                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td align="center">
                    <input type="checkbox" name="tdid" value="${dto.id}_${dto.dede_archives_id}" id="tdid_${st.index}">
                </td>
                <td nowrap align="center">${dto.dede_archives_id}</td>
                <td nowrap align="center">

                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <a href="/forign/content/reply/list?domain=7&unikey=${bean.value.uniqueKey}&statuscode="
                                   target="_blank">查看评论</a>
                            </c:if>
                        </c:forEach>
                    </c:if>
                    <c:if test="${dto.archiveContentType.code!=2}">
                        <a href="/forign/content/reply/list?domain=2&unikey=${dto.dede_archives_id}&statuscode="
                           target="_blank">查看评论</a>
                    </c:if>
                </td>
                <td nowrap align="left">
                    <c:if test="${dto.archiveContentType.code==0}">
                        ${dto.dede_archives_title}
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <p style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;width: 250px;">
                                    <c:out value="${bean.value.description}"/></p>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </td>
                <td nowrap align="center">
                    <a href="javascript:void(0)" onclick="sort('${title}','${page.startRowIdx}','up','${dto.id}','${tagid}','${dto.dede_archives_id}','${platform}','${removestaus}')"><img src="/static/images/icon/up.gif"></a>

                    <a href="javascript:void(0)" onclick="sort('${title}','${page.startRowIdx}','down','${dto.id}','${tagid}','${dto.dede_archives_id}','${platform}','${removestaus}')"><img src="/static/images/icon/down.gif"></a>

                </td>
                <td nowrap align="left">
                        ${dto.display_order}
                    <form action="/gameclient/tag/dede/modifysort" method="post">
                        <input type="text" name="display_order" value="" size="10"/>
                        <input type="hidden" name="id" value="${dto.id}"/>
                        <input type="hidden" name="tagid" value="${tagid}"/>
                        <input type="hidden" name="title" value="${title}"/>
                        <input type="hidden" name="startRowIdx" value="${page.startRowIdx}"/>
                        <input type="hidden" name="removestaus" value="${removestaus}"/>
                        <input type="hidden" name="platform" value="${platform}"/>
                        <input type="hidden" name="archiveId" value="${dto.dede_archives_id}"/>
                        <input type="submit" name="button" class="default_button" value="修改"/>
                    </form>
                </td>
                <td nowrap align="center">
                    <c:if test="${dto.archiveContentType.code==0}">
                        ${dto.dede_archives_pubdate_str}
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.createTime}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>

                </td>
                <td nowrap align="center">
                    <fmt:message key="gameclient.tagdedearchives.type.${dto.tagDisplyType.code}" bundle="${def}"/>
                </td>

                <td nowrap align="center" name="read_num">
                    <c:if test="${dto.archiveContentType.code==0}">
                        <c:forEach items="${cheatMap}" var="cheat"><c:if
                                test="${cheat.key==dto.dede_archives_id}"><c:out
                                value="${cheat.value.read_num}"/></c:if></c:forEach>
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.longCommentSum}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>

                </td>
                <td nowrap align="center">
                    <c:if test="${dto.archiveContentType.code==0}">
                        <c:forEach items="${cheatMap}" var="cheat"><c:if
                                test="${cheat.key==dto.dede_archives_id}"><c:out
                                value="${cheat.value.agree_num}"/></c:if></c:forEach>
                    </c:if>
                    <c:if test="${dto.archiveContentType.code==2}">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.scoreCommentSum}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>

                </td>


                <td nowrap align="center">
                    <c:if test="${dto.dede_archives_showios==1 && dto.dede_archives_showandroid==1}">
                        全部
                    </c:if>
                    <c:if test="${dto.dede_archives_showios!=1 && dto.dede_archives_showandroid!=1}">

                    </c:if>
                    <c:if test="${dto.dede_archives_showandroid==1 && dto.dede_archives_showios!=1}">
                        Android
                    </c:if>
                    <c:if test="${dto.dede_archives_showios==1 && dto.dede_archives_showandroid!=1 }">
                        IOS
                    </c:if>

                </td>


                <td nowrap align="center"><fmt:message key="anime.remove.status.${dto.remove_status.code}"
                                                       bundle="${def}"/><input type="hidden" value="${dto.remove_status.code}" id="removestatus_${st.index}"/></td>


                <td nowrap align="center">
                    <c:if test="${dto.archiveContentType.code==0}">
                        <a href="/gameclient/tag/dede/modifypage?id=${dto.id}&tagid=${tagid}&title=${title}&startRowIdx=${page.startRowIdx}&removestaus=${removestaus}&platform=${platform}">编辑</a>
                    </c:if>
                    <c:if test="${dto.remove_status.code=='invalid'}">
                        <a href="javascript:void(0)" onclick="del('${dto.id}','valid','${dto.dede_archives_id}','${dto.archiveContentType.code}')">发布</a>|
                        <a href="javascript:void(0)" onclick="del('${dto.id}','removed','${dto.dede_archives_id}','${dto.archiveContentType.code}')">删除</a>
                    </c:if>
                    <c:if test="${dto.remove_status.code=='valid'}">
                        <a href="javascript:void(0)" onclick="del('${dto.id}','removed','${dto.dede_archives_id}','${dto.archiveContentType.code}')">删除</a>
                    </c:if>
                    <c:if test="${dto.remove_status.code=='removed'}">
                        <a href="javascript:void(0)" onclick="del('${dto.id}','invalid','${dto.dede_archives_id}','${dto.archiveContentType.code}')">激活</a>
                    </c:if>
                    <c:if test="${dto.archiveContentType.code!=2&&dto.dede_redirect_type.code==23}">
                        <a href="/gameclient/tag/dede/createreplypage?unikey=${dto.dede_archives_id}&tagid=${tagid}">虚拟用户回复</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td height="1" colspan="12" class="default_line_td"></td>
        </tr>
    </c:when>
    <c:otherwise>
        <tr>
            <td colspan="12" class="error_msg_td">暂无数据!</td>
        </tr>
    </c:otherwise>
</c:choose>
<tr>
    <td colspan="12" height="1" class="default_line_td"></td>
</tr>
<c:if test="${page.maxPage > 1}">
    <tr class="list_table_opp_tr">
        <td colspan="12">
            <pg:pager url="/gameclient/tag/dede/list"
                      items="${page.totalRows}" isOffset="true"
                      maxPageItems="${page.pageSize}"
                      export="offset, currentPageNumber=pageNumber" scope="request">
                <pg:param name="tagid" value="${tagid}"/>
                <pg:param name="platform" value="${platform}"/>
                <pg:param name="title" value="${title}"/>
                <pg:param name="removestaus" value="${removestaus}"/>
                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                <pg:param name="items" value="${page.totalRows}"/>
                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
            </pg:pager>
        </td>
    </tr>
</c:if>
</table>
</td>
</tr>
</table>
</body>
</html>