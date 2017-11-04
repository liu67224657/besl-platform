<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动轮播图</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        function del(id, removestaus, archiveId, tag_id) {
            var message = "你真的确定要删除吗?";
            if (removestaus == "valid") {
                message = "你真的发布吗?";
            } else if (removestaus == "invalid") {
                message = "你真的预发布吗?";
            }
            var gnl = confirm(message);
            if (!gnl) {
                return false;
            }
            window.location.href = "/comment/bean/delete?tag_id=" + tag_id + "&remove_status=" + removestaus + "&id=" + id + "&archiveId=" + archiveId + "&pager.offset=${page.startRowIdx}";
        }

        function modifyquanzi(id, commentid) {
            var text = $("#tagid" + id).find("option:selected").text();
            var val = $("#tagid" + id).find("option:selected").val();
            if (confirm("是否要将该帖子移到" + text + "圈子？")) {
                window.location = '/comment/bean/modifygroup?commentid=' + commentid + '&groupid=' + val;
            }
        }
        $(document).ready(function() {
            $("#all_remove_form").submit(function() {
                var end_time = $("#input_text_enddate").val();
                var start_time = $("#input_text_startdate").val();
                if (start_time != "" && end_time != "") {
                    if (start_time >= end_time) {
                        alert("开始时间不能大于结束时间");
                        return false;
                    }
                } else if (start_time != "" && end_time == "") {
                    alert("请选择结束时间");
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸迷友圈主贴</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="11" class="error_msg_td">
                <fmt:message key="${errorMsg}" bundle="${error}"/>

            </td>
        </tr>
    </c:if>
    <tr>
    </tr>
</table>
<form action="/comment/bean/list" method="post" id="all_remove_form">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td height="1" class="default_line_td" colspan="2"></td>
        </tr>
        <tr class="toolbar_tr">
            <td width="28%">
                圈子： <select name="quanzi">
                <option value="" <c:if test="${remove_status==''}">selected</c:if>>全部</option>
                <c:if test="${not  empty  animetags}">
                    <c:forEach items="${animetags}" var="item">
                        <option value="${item.tag_id}"
                                <c:if test="${quanzi eq item.tag_id}">selected</c:if>>${item.tag_name}</option>
                    </c:forEach>
                </c:if>
            </select>
                状态： <select name="remove_status">
                <option value="" <c:if test="${remove_status==''}">selected</c:if>>全部</option>
                <option value="valid" <c:if test="${remove_status=='valid'}">selected</c:if>>发布</option>
                <option value="invalid" <c:if test="${remove_status=='invalid'}">selected</c:if>>预发布
                </option>
                <option value="removed" <c:if test="${remove_status=='removed'}">selected</c:if>>删除
                </option>
            </select>
                小号发帖： <select name="virtual_user">
                <option value="" <c:if test="${virtualUser==''}">selected</c:if>>全部</option>
                <option value="3" <c:if test="${virtualUser=='3'}">selected</c:if>>是</option>
                <option value="2" <c:if test="${virtualUser=='2'}">selected</c:if>>否
                </option>
            </select>

                用户昵称： <input type="text" value="${nick}" name="profilenick"/>


                起始时间: <input type="text" class="Wdate"
                             onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00',vel:'startdate',autoPickDate:true})"
                             readonly="readonly" name="startdate" id="input_text_startdate"
                             value="<fmt:formatDate value="${startdate}" pattern="yyyy-MM-dd HH:00:00"/>"/>
                结束时间: <input type="text" class="Wdate"
                             onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00',vel:'enddate',autoPickDate:true})"
                             readonly="readonly" name="enddate" id="input_text_enddate"
                             value="<fmt:formatDate value="${enddate}" pattern="yyyy-MM-dd HH:00:00"/>"/>

                <input type="submit" name="modifyCheck" id="modifyCheck" value="查询"/>

            </td>
            <td width="4%">
            </td>
        </tr>
    </table>
</form>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td" colspan="2"></td>
    </tr>
    <tr class="toolbar_tr">
        <td width="28%">
            <form action="/comment/bean/createpage" method="post">
                <input type="submit" value="虚拟用户发帖"/>
            </form>
            共${usernum}人，发布了${page.totalRows}帖子
        </td>
        <td width="4%">

        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="center">图片</td>
        <td nowrap align="center">文章评论</td>
        <td nowrap align="center"
            style="width:250px;overflow:hidden;white-space:nowrap;text-overflow: ellipsis;">标题
        </td>
        <td nowrap align="center">所属圈子</td>
        <td nowrap align="center">发布人</td>
        <td nowrap align="center">小号发帖</td>
        <td nowrap align="center">排序操作</td>
        <td nowrap align="center" width="130px">发布时间</td>
        <td nowrap align="center">阅读数(真实)</td>
        <td nowrap align="center">赞数(真实)</td>
        <td nowrap align="center">评论数</td>
        <td nowrap align="center">是否显示推荐标签</td>
        <td nowrap align="center" width="70px">是否删除</td>
        <td nowrap align="center">操作</td>
    </tr>
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="dto" varStatus="st">
                <tr id="socialHotContent_${dto.id}"
                    class="<c:choose><c:when
                                test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap align="center" width="100px"><c:forEach items="${commentBeanMap}" var="bean">
                        <c:if test="${bean.key==dto.dede_archives_id}">
                            <img src="${bean.value.pic}" width="100" height="100"/>
                        </c:if>
                    </c:forEach></td>
                    <td align="left">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <a href="/forign/content/reply/list?domain=7&unikey=${bean.value.uniqueKey}&statuscode="
                                   target="_blank">查看评论</a>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td align="left">
                        <p style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;width: 300px;">
                            <c:forEach items="${commentBeanMap}" var="bean">
                                <c:if test="${bean.key==dto.dede_archives_id}">
                                    <c:out value="${bean.value.description}"/>
                                </c:if>
                            </c:forEach>
                        </p>
                    </td>
                    <td nowrap align="center" width="130px">

                        <c:forEach items="${animetags}" var="anime">
                            <c:if test="${dto.display_tag==anime.tag_id}">
                                ${anime.tag_name}
                            </c:if>
                        </c:forEach>
                        <br/>
                        <a href="javascript:modifyquanzi('${st.index}','${dto.dede_archives_id}')">移动到</a>
                        <br/>
                        <select id="tagid${st.index}">
                            <c:forEach items="${animetags}" var="anime">
                                <c:if test="${dto.display_tag!=anime.tag_id}">
                                    <option value="${anime.tag_id}">${anime.tag_name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </td>

                    <td nowrap align="center" width="130px">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:forEach items="${profileMap}" var="profile">
                                <c:if test="${dto.dede_archives_id==bean.key&&profile.key==bean.value.uri}">
                                    ${profile.value.nick}
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </td>
                    <td nowrap align="center" width="130px">
                        <c:choose>
                            <c:when test="${dto.archiveContentType.code == '3'}">
                                是
                            </c:when>
                            <c:otherwise>
                                否
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td nowrap align="left">
                            ${dto.display_order}
                        <form action="/comment/bean/sort" method="post">
                            <input type="text" name="display_order" value="" size="8"/>
                            <input type="hidden" name="id" value="${dto.id}"/>
                            <input type="hidden" name="tagid" value="${dto.tagid}"/>
                            <input type="hidden" name="dede_archives_id" value="${dto.dede_archives_id}"/>
                            <input type="hidden" name="remove_status" value="${dto.remove_status.code}"/>
                            <input type="hidden" name="quanzi" value="${quanzi}"/>
                            <input type="submit" name="button" class="default_button" value="修改"/>
                        </form>
                    </td>

                    <td nowrap align="center" width="130px">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.createTime}"/>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td nowrap align="center">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.longCommentSum}"/>
                                (${bean.value.fiveUserSum})
                            </c:if>
                        </c:forEach>
                    </td>
                    <td nowrap align="center">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.scoreCommentSum}"/>
                                (${bean.value.fourUserSum})
                            </c:if>
                        </c:forEach>
                    </td>
                    <td nowrap align="center">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:out value="${bean.value.commentSum}"/>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td nowrap align="center">
                        <c:forEach items="${commentBeanMap}" var="bean">
                            <c:if test="${bean.key==dto.dede_archives_id}">
                                <c:if test="${bean.value.oneUserSum==-1}">
                                    <span style="color: red;">是</span>
                                </c:if>
                                <c:if test="${bean.value.oneUserSum!=-1}">
                                    否
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td nowrap align="center" width="70px"><fmt:message
                            key="anime.remove.status.${dto.remove_status.code}" bundle="${def}"/></td>

                    <td nowrap align="center">
                        <a href="/comment/bean/modifypage?id=${dto.id}&pageofferset=${page.startRowIdx}">编辑</a>|

                        <c:if test="${dto.remove_status.code=='valid'}">
                            <a href="javascript:del('${dto.id}','removed','${dto.dede_archives_id}','${dto.tagid}')">删除</a>
                        </c:if>
                        <c:if test="${dto.remove_status.code=='removed'}">
                            <a href="javascript:del('${dto.id}','valid','${dto.dede_archives_id}','${dto.tagid}')">激活</a>
                        </c:if>
                        <a href="/comment/bean/createreplypage?commentid=${dto.dede_archives_id}">使用虚拟用户评论</a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="14" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="14" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="10" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="14">
                <pg:pager url="/comment/bean/list"
                          items="${page.totalRows}" isOffset="true"
                          maxPageItems="${page.pageSize}"
                          export="offset, currentPageNumber=pageNumber" scope="request">
                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                    <pg:param name="items" value="${page.totalRows}"/>
                    <pg:param name="remove_status" value="${remove_status}"/>
                    <pg:param name="profilenick" value="${nick}"/>
                    <pg:param name="quanzi" value="${quanzi}"/>
                    <pg:param name="virtual_user" value="${virtualUser}"/>
                    <pg:param name="startdate" value="${startdate}"/>
                    <pg:param name="enddate" value="${enddate}"/>
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