
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <title>虚拟用户-wikiapp列表</title>
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
            <form action="/apiwiki/pushmessage/list" method="post">
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        推送类型：
                    </td>
                    <td>
                        <select name="pushType">
                            <option value="">请选择</option>
                            <option value="TAG" <c:if test="${pushType=='TAG'}">selected</c:if>><fmt:message key="pushmessage.type.TAG" bundle="${toolsProps}"/></option>
                            <option value="DEVICE"  <c:if test="${pushType=='DEVICE'}">selected</c:if>><fmt:message key="pushmessage.type.DEVICE" bundle="${toolsProps}"/></option>
                        </select>
                    </td>
                    <td>
                        发送类型：
                    </td>
                    <td>
                        <select name="sendType">
                            <option value="">请选择</option>
                            <option value="now" <c:if test="${sendType=='now'}">selected</c:if> ><fmt:message key="pushmessage.sendtype.now" bundle="${toolsProps}"/></option>
                            <option value="deplayed" <c:if test="${sendType=='deplayed'}">selected</c:if> ><fmt:message key="pushmessage.sendtype.delayed" bundle="${toolsProps}"/></option>
                        </select>
                    </td>
                    <td>
                        发送状态：
                    </td>
                    <td>
                        <select name="sendStatus">
                            <option value="">请选择</option>
                            <option value="INIT" <c:if test="${sendStatus=='INIT'}">selected</c:if> ><fmt:message key="pushmessage.sendstatus.INIT" bundle="${toolsProps}"/></option>
                            <option value="VALID" <c:if test="${sendStatus=='VALID'}">selected</c:if> ><fmt:message key="pushmessage.sendstatus.VALID" bundle="${toolsProps}"/></option>
                        </select>
                    </td>
                    <td>
                      <input type="submit" value="查询"/>
                    </td>
                </tr>
            </table>
            </form>
            <table  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        <form action="/apiwiki/pushmessage/createpage">
                            <input type="submit" value="推送消息"/>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${errorMsg != null}">
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
                    <td nowrap align="center" width="50px">发送类型</td>
                    <td nowrap align="center" width="120px">内容</td>
                    <td nowrap align="center" width="120px">标签</td>
                    <td nowrap align="center" width="120px">跳转</td></td>
                    <td nowrap align="center" width="80px">发送类型</td>
                    <td nowrap align="center" width="">发送时间</td>
                    <td nowrap align="center" width="80px">发送状态</td>
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
                                <td><fmt:message key="pushmessage.type.${dto.pushType}" bundle="${toolsProps}"/></td>
                                <td>${dto.title}</td>
                                <td>${dto.tags}</td>
                                <td>JT:${dto.jt}<br/>JI:${dto.ji}</td>
                                <td><fmt:message key="pushmessage.sendtype.${dto.sendType}" bundle="${toolsProps}"/></td>
                                <td  <c:if test="${dto.sendType=='delayed'}">class="fontcolor_dsp_hint"</c:if> >
                                    <fmt:formatDate value="${dto.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </td>
                                <td <c:if test="${dto.sendStatus=='INIT'}">class="fontcolor_dsp_hint"</c:if> >
                                    <fmt:message key="pushmessage.sendstatus.${dto.sendStatus}" bundle="${toolsProps}"/></td>
                                <td>
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
