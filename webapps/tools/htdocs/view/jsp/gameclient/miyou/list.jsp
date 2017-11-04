<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理-玩覇查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#checkall").click(function() {
                var checkall = $("#checkall").attr("checked");
                if (checkall) {
                    $('input[name="box"]').attr("checked", true);
                } else {
                    $('input[name="box"]').attr("checked", false);
                }
            });
            $("#checkinverse").click(function() {
                $('input[name="box"]').each(function() {
                    this.checked = !this.checked;
                });
            });

            $('#form_submit').bind('submit', function () {
                var result = new Array();
                $("input[name='box']:checked").each(function() {
                    result.push(this.value);
                });
                if (result.length == 0) {
                    alert("至少要选择一条记录");
                    return false;
                }

                $('#input_hidden_ids2').val(result.join('@'));
            });

        });
        function sort(sort, itemid, lineid, validstatus) {
            if (validstatus == 'removed') {
                alert("请先恢复文章再进行排序");
                return false;
            }
            $.post("/gameclient/clientline/miyou/sort/" + sort, {itemid: itemid, lineid: lineid}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;

                    if (result == null) {

                    } else {
                        var itemid = result.itemid;
                        var sort = result.sort;
                        var returnid = result.returnitemid;
                        if (sort == 'up') {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
                            $("#clientitem_" + returnid).before(item);
                        } else {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
                            $("#clientitem_" + returnid).after(item);
                        }
                    }
                }
            });
        }

        function refreshCache() {
            if (confirm("是否确认重新生成缓存？")) {
                alert("缓存正在重新生成，期间请不要重复操作");
                $("#refreshCache").attr("disabled", "disabled");
                $.post("/gameclient/clientline/miyou/refresh", {}, function (req) {

                });

            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">运营维护 >> <a href="/clientline/iphone/news/list"> 着迷手游画报管理</a> >> 迷友管理</td>
</tr>
<tr>
    <td valign="top"><br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="detail_table_header_td">玩覇详细信息</td>
            </tr>
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr class="toolbar_tr">
                <td>
                </td>
            </tr>
        </table>
	  <span id=moduleView>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
              <td height="1" class="default_line_td"></td>
          </tr>
      </table>
      </span>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td">
                    <form action="/gameclient/clientline/miyou/page" mothed="post">
                        <%--<input type="hidden" name="maxPageItems" value="${page.pageSize}"/>--%>
                        <%--<input type="hidden" name="items" value="${page.totalRows}"/>--%>
                        <%--<input type="hidden" name="pager.offset" value="${page.startRowIdx}"/>--%>

                        <select name="status">
                            <option value="">所有状态</option>
                            <option value="y"
                            <c:if test="${status eq 'y'}">selected</c:if>><fmt:message
                                key="gameclient.commentbean.y" bundle="${def}"/></option>
                            <option value="n"
                            <c:if test="${status eq 'n'}">selected</c:if>><fmt:message
                                key="gameclient.commentbean.n" bundle="${def}"/></option>
                            <option value="ing"
                            <c:if test="${status eq 'ing'}">selected</c:if>><fmt:message
                                key="gameclient.commentbean.ing" bundle="${def}"/></option>
                        </select>
                        按昵称查询:<input type="text" name="nick" value="${nick}"/>
                        <input type="submit" value="查询"/> （模糊搜索）
                    </form>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                    <%--<p:privilege name="/gameclient/clientline/miyou/createpage">--%>
                    <form action="/gameclient/clientline/miyou/createpage" mothed="post">
                        <%--<input type="hidden" name="lineid" value="${line.lineId}"/>--%>
                        <input type="submit" value="创建玩覇图片"/>
                    </form>
                    <input type="button" onclick="refreshCache();" id="refreshCache" value="重新生成迷友图片缓存"/>
                    <%--</p:privilege>--%>


                </td>

            </tr>
            <tr>
                <form action="/gameclient/clientline/miyou/batchupdate" method="post" id="form_submit">
                    <td>
                        <input type="checkbox" name="all" id="checkall"/>全选
                        <input type="checkbox" name="inverse" id="checkinverse"/>反选
                        状态改成：
                        <select name="updatestatuscode" class="default_select_single">
                            <%--<option value="">--请选择--</option>--%>
                            <option value="ing">加入迷友</option>
                            <option value="y">删除</option>
                            <option value="n">恢复/从迷友移除</option>

                        </select>
                        <input type="hidden" value="" name="itemid" id="input_hidden_ids2"/>
                        <input type="hidden" value="${status}" name="status" id=""/>
                        <input name="button" type="submit" class="default_button" value="批量修改"/>
                    </td>

            </tr>
            </form>
            </tr>
        </table>
        <br>
    </td>
</tr>
<tr>
    <td height="100%" valign="top">
        <table width="100%" border="0" cellspacing="1" cellpadding="0">
            <c:if test="${fn:length(errorMsg)>0}">
                <tr>
                    <td height="1" colspan="13" class="error_msg_td">
                        <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                    </td>
                </tr>
            </c:if>
            <tr class="list_table_title_tr">
                <td nowrap align="left" width="30"></td>
                <td nowrap align="center">上传的用户昵称</td>
                <td nowrap align="left">图片</td>
                <%--<td nowrap align="left">排序</td>--%>
                <td nowrap align="left" width="80">图片ID</td>
                <td nowrap align="left">状态</td>
                <td nowrap align="left">创建信息</td>
                <td nowrap align="left">操作</td>
            </tr>
            <tr>
                <td height="1" colspan="13" class="default_line_td"></td>
            </tr>
            <c:choose>
                <c:when test="${list.size() > 0}">
                    <c:forEach items="${list}" var="dto" varStatus="st">
                        <tr id="clientitem_${dto.uniqueKey}"
                            class="<c:choose><c:when test='${st.index % 2 == 0}'>list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                            <td nowrap align="left"><input type="checkbox" name="box"
                                                           value="${dto.commentId}"/></td>
                            <td nowrap align="center">
                                <c:forEach items="${profiles}" var="item">
                                    <c:if test="${item.profileId==dto.uri}">${item.nick}</c:if>
                                </c:forEach>

                            </td>
                            <td nowrap align="center"><img src="${dto.pic}" height="100" width="100"/></td>
                            <%--<td nowrap align="center">--%>
                            <%--<a href="javascript:sort('up','${dto.id}','${dto.lid}','${validstatus}')"><img--%>
                            <%--src="/static/images/icon/up.gif"></a>--%>
                            <%--&nbsp;--%>
                            <%--<a href="javascript:sort('down','${dto.id}','${dto.lid}','${validstatus}')"><img--%>
                            <%--src="/static/images/icon/down.gif"></a>--%>
                            <%--</td>--%>
                            <td nowrap align="left">${dto.commentId}</td>
                            <td nowrap align="left"
                            <c:choose>
                                <c:when test="${dto.removeStatus.code != 'y'}">style="color: #008000;" </c:when>
                                <c:otherwise>style="color: #ff0000;" </c:otherwise>
                            </c:choose>>
                            <fmt:message key="gameclient.commentbean.${dto.removeStatus.code}"
                                         bundle="${def}"/>
                            <td nowrap align="left"><fmt:formatDate value="${dto.createTime}"
                                                                    pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td nowrap align="left">
                                <c:if test="${dto.removeStatus.code=='y'}">
                                    <a href="/gameclient/clientline/miyou/delete?id=${dto.commentId}&status=n&relstatus=${status}">恢复</a>
                                    <a href="/gameclient/clientline/miyou/delete?id=${dto.commentId}&status=ing&relstatus=${status}">加入迷友</a>
                                </c:if>
                                <c:if test="${dto.removeStatus.code=='n'}">
                                    <a href="/gameclient/clientline/miyou/delete?id=${dto.commentId}&status=y&relstatus=${status}">删除</a>
                                    <a href="/gameclient/clientline/miyou/delete?id=${dto.commentId}&status=ing&relstatus=${status}">加入迷友</a>
                                </c:if>
                                <%-- --%>
                                <c:if test="${dto.removeStatus.code=='ing'}">
                                    <a href="/gameclient/clientline/miyou/delete?id=${dto.commentId}&status=y&relstatus=${status}">删除</a>
                                    <a href="/gameclient/clientline/miyou/delete?id=${dto.commentId}&status=n&relstatus=${status}">从迷友移除</a>
                                </c:if>
                                <%--<c:choose>--%>
                                <%--<c:when test="${dto.status == 'valid'}">--%>
                                <%--<a href="/gameclient/clientline/miyou/delete?itemid=${dto.id}&lineid=${dto.lid}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&items=${page.totalRows}&status=${status}">删除</a>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                <%--<a href="/gameclient/clientline/miyou/recover?itemid=${dto.id}&lineid=${dto.lid}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&items=${page.totalRows}&status=${status}">激活</a>--%>
                                <%--</c:otherwise>--%>
                                <%--</c:choose>--%>

                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="13" height="1" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            <tr>
                <td colspan="13" height="1" class="default_line_td"></td>
            </tr>
            <c:if test="${page.maxPage > 1}">
                <tr class="list_table_opp_tr">
                    <td colspan="13">
                        <pg:pager url="/gameclient/clientline/miyou/page"
                                  items="${page.totalRows}" isOffset="true"
                                  maxPageItems="${page.pageSize}"
                                  export="offset, currentPageNumber=pageNumber" scope="request">
                            <%--<pg:param name="lineid" value="${line.lineId}"/>--%>
                            <pg:param name="maxPageItems" value="${page.pageSize}"/>
                            <pg:param name="items" value="${page.totalRows}"/>
                            <pg:param name="status" value="${status}"/>

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