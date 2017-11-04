<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>标签问题列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#add_itemId").bind('click', function () {
                var destId = $("#destId").val();

                if ($.trim(destId) == "") {
                    alert("问题id不能为空");
                    return;
                } else if (destId.indexOf("，")>0) {
                    alert("分隔符请采用英文逗号");
                    return;
                } else {
                   var destIdArr =destId.split(",");
                    if(destIdArr.length>5){
                        alert("批量最大只能填写5个");
                        return;
                    }
                    $.post("/wanba/askrecommend/create", {destId: destId}, function (req) {
                        var rsobj = eval('(' + req + ')');
                        if (rsobj.rs == 1) {
                            window.location.href = "/wanba/askrecommend/list";
                        } else {
                            alert(rsobj.msg);
                            window.location.href = "/wanba/askrecommend/list";
                        }
                    });
                }
            });


            //置顶
            $('a[name=top]').click(function () {
                var type = $(this).attr('data-type');
                var destId = $(this).attr('data-destid');

                $.post("/wanba/askrecommend/top", {type: type,destId:destId}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        window.location.reload();
                    } else {
                        alert(rsobj.msg);
                    }
                });
            });
        });



        function valid(itemid){
            if (confirm('确定发布?')) {
                $.post("/wanba/askrecommend/release", {tagid:${tagid},itemid: itemid,status:'valid'}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        $('#td_rstatus_' + itemid).html('<font>发布</font>');
                        $('#td_action_' + itemid).html('<a  href="javascript:void(0);" onclick="del(\''+itemid+'\')">删除</a>');
                    }
                });
            }
        }

        function invalid(itemid) {
            if (confirm('确定恢复?')) {
                $.post("/wanba/askrecommend/release", {tagid:${tagid},itemid: itemid,status:'invalid'}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        $('#td_rstatus_' + itemid).html('<font>未发布</font>');
                        $('#td_action_' + itemid).html('<a href="javascript:void(0);" onclick="valid(\''+itemid+'\')">发布</a>');
                    }
                });
            }
        }

        //删除
        function del(itemid){
            if (confirm('确定删除?')) {
                $.post("/wanba/askrecommend/remove", {tagid:${tagid},itemid: itemid}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        $('#td_rstatus_' + itemid).html('<font color="red">已删除</font>');
                        $('#td_action_' + itemid).html('<a href="javascript:void(0);" onclick="invalid(\''+itemid+'\')">恢复</a>');
                    }
                });
            }
        }

    </script>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 标签问题列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">标签问题列表</td>
                </tr>
            </table>

            <c:if test="${tagid<0}">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="10" class="error_msg_td">
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
                        <td></td>
                        <td>
                            <table>
                                <tr>
                                    <td>
                                        <input type="text" name="destId" id="destId" size="50" placeholder="请输入问题ID,批量逗号分隔，如1,2,3"/>
                                        <input type="submit" name="button" class="default_button" value="新增"
                                               id="add_itemId"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tr>
                                    <td>

                                        <a href="/wanba/askrecommend/recommendprofile">推荐页推荐达人信息</a>

                                    </td>
                                </tr>
                            </table>

                        </td>
                    </tr>
                </table>


            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">问题ID</td>
                    <td nowrap align="center">问题标题</td>
                    <td nowrap align="center">问题状态</td>
                    <td nowrap align="center">创建时间</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.destId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap><a href="/wanba/ask/answer/list?qid=${dto.destId}">${dto.destId}</a></td>
                                <td nowrap> ${questionMap[dto.destId+""]==null?dto.destId:questionMap[dto.destId+""].title}</td>

                                <td nowrap id="td_rstatus_${dto.itemId}"><fmt:message
                                        key="wanba.removestatus.${dto.validStatus.code}"
                                        bundle="${toolsProps}"/></td>
                                <td nowrap><fmt:formatDate value="${dto.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap width="200px">
                                    <a href="/wanba/askrecommend/sort?desc=up&destId=${dto.destId}&tagid=${tagid}"><img
                                            src="/static/images/icon/up.gif"></a>

                                    <a href="/wanba/askrecommend/sort?desc=down&destId=${dto.destId}&tagid=${tagid}"><img
                                            src="/static/images/icon/down.gif"></a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${tagid<0}">
                                    <c:if test="${dto.score<nowscore}">
                                        <a href="javascript:void(0);" data-type="1" data-destid="${dto.destId}" name="top">置顶</a>
                                    </c:if>
                                    <c:if test="${dto.score>nowscore}">
                                        <a href="javascript:void(0);" data-type="2" data-destid="${dto.destId}" name="top">取消置顶</a>
                                    </c:if>
                                    </c:if>
                                </td>
                                <td nowrap id="td_action_${dto.itemId}">
                                    <c:if test="${dto.validStatus.code=='valid'}">
                                        <a href="javascript:void(0);" onclick="del('${dto.itemId}')">删除</a>
                                    </c:if>
                                    <c:if test="${dto.validStatus.code=='invalid'}">
                                        <a href="javascript:void(0);" onclick="valid('${dto.itemId}')">发布</a>
                                    </c:if>
                                    <c:if test="${dto.validStatus.code=='removed'}">
                                        <a href="javascript:void(0);" onclick="invalid('${dto.itemId}')">恢复</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="6" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="6" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="6">
                            <pg:pager url="/wanba/askrecommend/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="tagid" value="${tagid}"/>
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