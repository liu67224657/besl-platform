<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <title>用户举报-用户举报列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.deletelink').click(function () {
                if (!confirm('确定要删除该用户反馈么！该操作不可逆')) {
                    return false;
                }
                var id=$(this).attr('data-id');
                $.get("/apiwiki/feedback/json/delete?id="+id,null,function(req){
                    if(req=='success'){
                        $('#value_tr_'+id).remove();
                    }else{
                        alert(req);
                    }
                });
            })

            $('.feedback_valid').click(function () {
                if (!confirm('确定该用户反馈改为"处理"么？该操作不可逆')) {
                    return false;
                }
                var _this=$(this);
                var id=$(this).attr('data-id');
                $.get("/apiwiki/feedback/json/valid?id="+id,null,function(req){
                    if(req=='success'){
                        $('.modify_status_link[data-id='+id+']').remove();
                        $('#status_'+id).html('<font color="green">已处理</font>')
                    }else{
                        alert(req);
                    }
                });
            })

            $('.feedback_unvalid').click(function () {
                if (!confirm('确定该用户反馈改为"不做处理"么？该操作不可逆')) {
                    return false;
                }
                var id=$(this).attr('data-id');
                $.get("/apiwiki/feedback/json/unvalid?id="+id,null,function(req){
                    if(req=='success'){
                       $('.modify_status_link[data-id='+id+']').remove();
                        $('#status_'+id).html('<font color="red">不做处理</font>')
                    }else{
                        alert(req);
                    }
                });
            })
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 推荐用户</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <form action="/apiwiki/feedback/list" method="post">
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        举报类型：
                    </td>
                    <td>
                        <select name="feedbackType">
                            <option value="">请选择</option>
                            <option value="GAME" <c:if test="${feedbackType=='GAME'}">selected</c:if>><fmt:message key="feedback.type.GAME" bundle="${toolsProps}"/></option>
                            <option value="COMMENT"  <c:if test="${feedbackType=='COMMENT'}">selected</c:if>><fmt:message key="feedback.type.COMMENT" bundle="${toolsProps}"/></option>
                            <option value="USER" <c:if test="${feedbackType=='USER'}">selected</c:if>><fmt:message key="feedback.type.USER" bundle="${toolsProps}"/></option>
                            <option value="REPLY" <c:if test="${feedbackType=='REPLY'}">selected</c:if>><fmt:message key="feedback.type.REPLY" bundle="${toolsProps}"/></option>
                        </select>
                    </td>
                    <td>
                        举报状态：
                    </td>
                    <td>
                        <select name="status">
                            <option value="">请选择</option>
                            <option value="INIT" <c:if test="${status=='INIT'}">selected</c:if> ><fmt:message key="feedback.status.INIT" bundle="${toolsProps}"/></option>
                            <option value="VALID" <c:if test="${status=='VALID'}">selected</c:if> ><fmt:message key="feedback.status.VALID" bundle="${toolsProps}"/></option>
                            <option value="UNVALID" <c:if test="${status=='UNVALID'}">selected</c:if> ><fmt:message key="feedback.status.UNVALID" bundle="${toolsProps}"/></option>
                        </select>
                    </td>
                    <td>
                      <input type="submit" value="查询"/>
                    </td>
                </tr>
            </table>
            </form>
            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${error != null}">
                    <tr>
                        <td height="1" colspan="7" class="default_line_td">

                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">ID</td>
                    <td nowrap align="center" width="50px">举报类型</td>
                    <td nowrap align="center" width="">举报原因</td>
                    <td nowrap align="center" width="80px">举报人</td>
                    <td nowrap align="center" width="120px">举报对象</td>
                    <td nowrap align="center" width="80px">状态</td>
                    <%--<td nowrap align="center">关联游戏</td>--%>
                    <%--<td nowrap align="center" width="80px">被举报人</td>--%>
                    <td nowrap align="center" width="80px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="7" class="fontcolor_input_hint">
                            <fmt:message key="${errorMsg}" bundle="${errorProps}"/>
                        </td>
                    </tr>
                </c:if>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="value_tr_${dto.id}" class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td>${dto.id}</td>
                                <td><fmt:message key="feedback.type.${dto.feedbackType}" bundle="${toolsProps}"/></td>
                                <td>${dto.description}</td>
                                <td>${dto.uid}</td>
                                <td>
                                   <c:choose>
                                       <c:when test="${dto.feedbackType=='GAME'}">
                                           <c:choose>
                                               <c:when test="${gameMap[dto.destid]!=null}">
                                                  游戏名称： ${gameMap[dto.destid].name}
                                               </c:when>
                                               <c:otherwise>
                                                   ${dto.destid}
                                               </c:otherwise>
                                           </c:choose>
                                       </c:when>
                                       <c:when test="${dto.feedbackType=='USER'}">
                                           <c:choose>
                                               <c:when test="${userMap[dto.destid]!=null}">
                                                  用户昵称： ${userMap[dto.destid].nick}
                                               </c:when>
                                               <c:otherwise>
                                                   ${dto.destid}
                                               </c:otherwise>
                                           </c:choose>
                                       </c:when>
                                       <c:when test="${dto.feedbackType=='COMMENT'}">
                                           <c:choose>
                                               <c:when test="${commentMap[dto.destid]!=null}">
                                                  点评： ${commentMap[dto.destid].body}
                                               </c:when>
                                               <c:otherwise>
                                                   ${dto.destid}
                                               </c:otherwise>
                                           </c:choose>
                                       </c:when>
                                       <c:when test="${dto.feedbackType=='REPLY'}">
                                           评论内容：${dto.destBody}
                                       </c:when>
                                   </c:choose>
                                </td>
                                <td id="status_${dto.id}"><fmt:message key="feedback.status.${dto.status}" bundle="${toolsProps}"/> </td>
                                <td>
                                    <c:if test="${dto.status=='INIT'}">
                                        <a class="feedback_valid modify_status_link" data-id="${dto.id}" href="javascript:void(0);">已处理</a>
                                        <a class="feedback_unvalid modify_status_link" data-id="${dto.id}" href="javascript:void(0);">不做处理</a>
                                    </c:if>
                                    <a class="deletelink" data-id="${dto.id}" href="javascript:void(0);">删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="7" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="7" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="7">
                            <pg:pager url="/apiwiki/feedback/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
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
