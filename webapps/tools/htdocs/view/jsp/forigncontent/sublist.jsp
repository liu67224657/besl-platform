<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>后台数据管理、评论审核列表</title>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>

<link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
<link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
<script type="text/javascript" src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>

<script type="text/javascript" src="/static/include/js/default.js"></script>
<script type="text/javascript">
$(document).ready(function () {
    doOnLoad();
    $('#form_submit').bind('submit', function () {
        var result = new Array();
        $("[name = box]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                result.push($(this).prop("value"));
            }
        });
        if (result.length == 0) {
            alert("至少要选择一条评论");
            return false;
        }

        $('#input_hidden_ids').val(result.join('@'));
    });

    $('#form_submit2').bind('submit', function () {
        var result = new Array();
        $("[name = box]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                result.push($(this).prop("value"));
            }
        });
        if (result.length == 0) {
            alert("至少要选择一条评论");
            return false;
        }

        $('#input_hidden_ids2').val(result.join('@'));
    });

    $('#formSearch').bind('click', function () {
        var form = $("#form_submit_search");
        addFormNode('refreshStatus', form, 'refresh', '0');
        form.submit();

    });
    $('#refreshSearch').bind('click', function () {
        var form = $("<form>");
        form.attr('style', 'display:none');
        form.attr('target', '');
        form.attr('method', 'post');
        form.attr('action', "/forign/content/reply/list");

        addFormNode('screenname', form, 'input_scrrenname');
        addFormNode('domain', form, 'select_domain');
        addFormNode('unikey', form, "input_content_url");
        addFormNode('startdate', form, "startDate");
        addFormNode('enddate', form, "endDate");
        addFormNode('statuscode', form, 'input_statuscode');


        addFormNode('maxPageItems', form, 'inputMaxPageItems');
        addFormNode('refreshStatus', form, 'refresh', '1');


        form.submit();


    });

    $('#hideOrShow').bind('click', function () {
        var hideOrShow = $.trim($("#hideOrShow").val());

        var $lines = $(".item_list_jq_class");
        if ($lines.length <= 0) {

            alert("请先搜索.");
            return;
        }

        var $links = $("a[href*=gopagecom]");
        if (hideOrShow == "隐藏15字以下的评论") {
            $lines.each(function () {
                var $content = $(this).find(".item_td_jq_class >textarea").val();

                if ($content.length < 15) {
                    if ($(this).find("[name = box]:checkbox").prop("checked")) {
                        $(this).find("[name = box]:checkbox").prop("checked", false);
                    }
                    $(this).hide();
                }

            });


            $links.each(function () {
                var href = $(this).attr("href");
                href = href.replace(/\?(hide15Status=show&)?/, "\?hide15Status=hide&");

                $(this).attr("href", href);

            });


            $("#hideOrShow").val("显示15字以下的评论");
        } else {

            $lines.each(function () {
                if ($(this).is(":hidden")) {
                    $(this).show();
                }

            });

            $links.each(function () {
                var href = $(this).attr("href");
                href = href.replace(/\?(hide15Status=hide&)?/, "\?hide15Status=show&");

                $(this).attr("href", href);

            });

            $("#hideOrShow").val("隐藏15字以下的评论");
        }


    });

    $("#checkall").bind("click", function () {
        var $lines = $(".item_list_jq_class");

        if ($lines.length <= 0) {
            alert("请先搜索.");
            return;
        }

        if ($('#checkall').is(":checked")) {
            $("#checkall2").prop("checked", true);
            $lines.each(function () {
                if (!$(this).is(":hidden")) {
                    $(this).find("[name = box]:checkbox").prop("checked", true);
                }
            });
            //   $("[name = box]:checkbox").attr("checked", true);
        } else {
            $("#checkall2").prop("checked", false);
            $lines.each(function () {
                if (!$(this).is(":hidden")) {
                    $(this).find("[name = box]:checkbox").prop("checked", false);
                }
            });
            //   $("[name = box]:checkbox").attr("checked", false);
        }
    });

    $("#checkinverse").bind("click", function () {

        if ($("#checkinverse").prop("checked")) {
            $("#checkinverse2").prop("checked", true);
        } else {
            $("#checkinverse2").prop("checked", false);
        }

        var $lines = $(".item_list_jq_class");

        if ($lines.length <= 0) {
            alert("请先搜索.");
            return;
        }

        $lines.each(function () {
            if (!$(this).is(":hidden")) {
                if ($(this).find("[name = box]:checkbox").prop('checked')) {
                    $(this).find("[name = box]:checkbox").prop("checked", false);
                } else {
                    $(this).find("[name = box]:checkbox").prop("checked", true);

                }

            }
        });

        /*         $("[name = box]:checkbox").each(function () {
         $(this).prop("checked", !$(this).prop("checked"));
         });*/
    });

    $("#checkall2").bind("click", function () {

        $("#checkall").click();
        /*       if ($('#checkall2').is(":checked")) {
         $("[name = box]:checkbox").prop("checked", true);
         } else {
         $("[name = box]:checkbox").prop("checked", false);
         }*/
    });

    $("#checkinverse2").bind("click", function () {
        $("#checkinverse").click();
        /*        $("[name = box]:checkbox").each(function () {
         $(this).prop("checked", !$(this).prop("checked"));
         });*/
    });

    $('#button_allremove').bind("click", function () {
        if (confirm("确定要全部删除该用户的评论吗？")) {
            $('#all_remove_form').submit();
        }
    });

    <c:if test="${hide15Status=='hide'}" >
    $('#hideOrShow').click();
    </c:if>


});

function searchByNickName(nickName) {
    var form = $("#form_submit_search");
    $("#input_scrrenname").val(nickName);
    addFormNode('refreshStatus', form, 'refresh', '0');
    form.submit();


}


//用于页面的"刷新"按钮
function addFormNode(node, form, id, nodeValue) {
    var newNode = $('<input>');
    newNode.attr('type', 'hidden');
    newNode.attr('name', node);
    if (typeof(nodeValue) != "undefined") {
        newNode.attr('value', nodeValue);
    }
    else {
        var value = $("#" + id).val();
        newNode.attr('value', value);
        // alert(value);
    }

    form.append(newNode);
}


var myCalendar;
function doOnLoad() {
    myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
}

//删除
function deleteItemById(href, replyId) {
    var msg = "您确定要删除replyId为" + replyId + "的评论吗？\n\n请确认！";
    if (confirm(msg) == true) {
        window.location.href = href;


    }
}

//审核
function auditItemById(href, replyId) {
    var msg = "您确定要审核replyId为" + replyId + "的评论吗？\n\n请确认！";
    if (confirm(msg) == true) {
        window.location.href = href;


    }
}

//跳转到添加禁言用户的页面
function toforbiduser(nickName) {
    var href = "/forign/content/forbid/createpage?nickName=" + nickName;
    window.location.href = href;

}

</script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 客户服务 >> 内容审核 >> 评论审核</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">>评论查询列表</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="11" class="error_msg_td">${errorMsg}</td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <form action="/forign/content/reply/batchupdate?rurl=/forign/content/reply/sublist" method="post" id="form_submit">
        <tr>
            <td>
                <input type="checkbox" name="all" id="checkall"/>全选
            </td>
            <td>
                <input type="checkbox" name="inverse" id="checkinverse"/>反选
            </td>
            <td>
                状态改成：
                <select name="updatestatuscode" class="default_select_single">
                    <%--<option value="">--请选择--</option>--%>
                    <option value="ing">已审核</option>
                    <option value="y">已删除</option>
                </select>

                <input type="hidden" name="rootid" value="${rootId}"/>
                <input type="hidden" name="commentid" value="${commentId}"/>

                <input type="hidden" name="pager.offset" value="${page.startRowIdx}"/>
                <input type="hidden" name="replyids" id="input_hidden_ids"/>

                <input name="button" type="submit" class="default_button" value="批量修改"/>

                &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;
                <input id="hideOrShow" type="button" class="default_button" value="隐藏15字以下的评论"/>
                <c:if test="${domainCode==9}">
                    总评论数量：  ${page.totalRows}

                </c:if>
            </td>
            <c:if test="${page.maxPage > 1}">
                <td>
                    <c:set var="startDateStr">
                        <fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>
                    </c:set>
                    <c:set var="endDateStr">
                        <fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>
                    </c:set>
                    <LABEL>
                        <pg:pager url="/forign/content/reply/sublist"
                                  items="${page.totalRows}" isOffset="true"
                                  maxPageItems="${page.pageSize}"
                                  export="offset, currentPageNumber=pageNumber" scope="request">
                            <pg:param name="currentPageNumber" value="${page.curPage}"/>
                            <pg:param name="maxPageItems" value="${page.pageSize}"/>
                            <pg:param name="items" value="${page.totalRows}"/>
                            <pg:param name="commentid" value="${commentId}"/>
                            <pg:param name="rootid" value="${rootId}"/>
                            <%@ include file="/WEB-INF/jsp/toolspgwithnewversionjquery.jsp" %>
                        </pg:pager>
                    </LABEL>
                </td>
            </c:if>
        </tr>
    </form>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="11" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap="nowrap" align="left" width="30">序号</td>
        <td nowrap="nowrap" align="left" width="30"></td>
        <td nowrap="nowrap" align="left" width="70">评论ID</td>
        <td nowrap="nowrap" align="left" width="70">昵称</td>
        <td nowrap="nowrap" align="left" width="70">
            <c:choose>
                <c:when test="${domainCode!=9}">
                    文章unikey
                </c:when>
                <c:otherwise>
                    节目ID
                </c:otherwise>
            </c:choose>
        </td>
        <td nowrap="nowrap" align="left" width="70">来源</td>
        <td nowrap="nowrap" align="left" width="400">内容</td>
        <td nowrap="nowrap" align="left" width="150">操作</td>
        <%--<td nowrap="nowrap" align="left" width="100">图片</td>--%>
        <td nowrap="nowrap" align="left" width="50">状态</td>
        <c:if test="${statusCode eq 'y'}">
            <td nowrap align="left">操作人</td>
        </c:if>
        <td nowrap="nowrap" align="left" width="70">是否主评论</td>
        <td nowrap="nowrap" align="left" width="70">主评论ID</td>
        <td nowrap="nowrap" align="left" width="130">录入时间</td>

    </tr>
    <tr>
        <td height="1" colspan="11" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${not empty list}">
            <c:forEach items="${list}" var="reply" varStatus="st">
                <tr class="<c:choose><c:when test='${st.index % 2 == 0}'>list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose> item_list_jq_class">
                    <td nowrap="nowrap">${st.index + 1}</td>
                    <td nowrap="nowrap">
                        <input type="checkbox" name="box"
                               value="${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}"/>
                    </td>
                    <td nowrap="nowrap">${reply.replyId}</td>
                    <td nowrap="nowrap"><c:forEach items="${profileMap}" var="profile">
                        <c:if test="${profile.key == reply.replyProfileId}"><a href="javascript:;"
                                                                               onclick="searchByNickName('${profile.value.nick}');">${profile.value.nick}</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:if test="${domainCode!=9}">
                                <input type="button" name="toforbiduser"
                                       onclick="toforbiduser('${profile.value.nick}');"
                                       class="default_button" value="禁言"/>
                            </c:if>

                        </c:if></c:forEach>
                    </td>
                    <td nowrap="nowrap">
                        <c:choose>
                            <c:when test="${domainCode!=9}">
                                <c:forEach items="${commentBeanMap}" var="bean">
                                    <c:if test="${bean.key == reply.commentId}"><a target="_blank"
                                                                                   href="${bean.value.uri}">${bean.value.uniqueKey}</a></c:if></c:forEach>
                            </c:when>
                            <c:otherwise>
                                <a target="_blank"
                                   href="http://zozoka.${DOMAIN}/variety/show/showEdit?id=${reply.commentId}">${reply.commentId}</a>
                            </c:otherwise>
                        </c:choose>


                    </td>
                    <td nowrap="nowrap"><fmt:message key="comment.domain.${reply.domain.code}"
                                                     bundle="${def}"/></td>
                    <td nowrap="nowrap" class="item_td_jq_class"><textarea readonly="readonly"
                                                                           style="height: 100%;width: 100%;"
                                                                           disabled="disabled">${reply.body.text}</textarea>
                    <td nowrap="nowrap">

                        <a href="/forign/content/reply/detail?replyid=${reply.replyId}">详情</a>
                        &nbsp;&nbsp;
                        <c:choose>
                            <c:when test="${reply.removeStatus.code=='n'}">

                                <a href='javascript:;'
                                   onclick='auditItemById("/forign/content/reply/batchupdate?rurl=/forign/content/reply/sublist&rootid=${rootId}&commentid=${commentId}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=ing&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>审核</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a href='javascript:;'
                                   onclick='deleteItemById("/forign/content/reply/batchupdate?rurl=/forign/content/reply/sublist&rootid=${rootId}&commentid=${commentId}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=y&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>删除</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="/forign/content/reply/replypage?rurl=/forign/content/reply/sublist&rootid=${rootId}&commentid=${commentId}&replyid=${reply.replyId}">回复</a>
                            </c:when>
                            <c:when test="${reply.removeStatus.code=='ing'}">
                                <a href='javascript:;'
                                   onclick='deleteItemById("/forign/content/reply/batchupdate?rurl=/forign/content/reply/sublist&rootid=${rootId}&commentid=${commentId}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=y&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>删除</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="/forign/content/reply/replypage?rurl=/forign/content/reply/sublist&rootid=${rootId}&commentid=${commentId}&replyid=${reply.replyId}">回复</a>
                            </c:when>
                            <c:otherwise>

                                <a href='javascript:;'
                                   onclick='auditItemById("/forign/content/reply/batchupdate?rurl=/forign/content/reply/sublist&rootid=${rootId}&commentid=${commentId}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=ing&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>审核</a>
                            </c:otherwise>
                        </c:choose>


                    </td>
                    <td nowrap="nowrap"
                            <c:choose>
                                <c:when test="${reply.removeStatus.code!=y}">style="color: #008000;" </c:when>
                                <c:otherwise>style="color: #ff0000;"</c:otherwise>
                            </c:choose> >
                        <c:choose>
                            <c:when test="${reply.removeStatus.code=='n'}">
                                未审核
                            </c:when>
                            <c:when test="${reply.removeStatus.code=='ing'}">
                                已审核
                            </c:when>
                            <c:otherwise>
                                已删除
                            </c:otherwise>
                        </c:choose>

                    </td>
                    <c:if test="${statusCode eq 'y'}">
                        <td nowrap align="left">${reply.createIp}</td>
                    </c:if>
                    <td nowrap="nowrap"><c:choose><c:when
                            test="${reply.rootId == 0}">主楼评论</c:when><c:otherwise>楼中楼评论</c:otherwise></c:choose></td>
                    <td nowrap="nowrap">${reply.rootId}</td>
                    <td nowrap="nowrap"><fmt:formatDate value="${reply.createTime}"
                                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="11" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="11" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="11" height="1" class="default_line_td"></td>
    </tr>

</table>
<c:if test="${domainCode!=9}">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <form action="/forign/content/reply/batchupdate?rurl=/forign/content/reply/sublist" method="post" id="form_submit2">
            <tr>
                <td>
                    <input type="checkbox" name="all" id="checkall2"/>全选
                </td>
                <td>
                    <input type="checkbox" name="inverse" id="checkinverse2"/>反选
                </td>
                <td>
                    状态改成：
                    <select name="updatestatuscode" class="default_select_single">
                            <%--<option value="">--请选择--</option>--%>
                        <option value="ing">已审核</option>
                        <option value="y">已删除</option>
                    </select>
                    <input type="hidden" name="rootid" value="${rootId}"/>
                    <input type="hidden" name="commentid" value="${commentId}"/>

                    <input type="hidden" name="pager.offset" value="${page.startRowIdx}"/>
                    <input type="hidden" name="replyids" id="input_hidden_ids2">

                    <input name="button" type="submit" class="default_button" value="批量修改">

                </td>
                <c:if test="${page.maxPage > 1}">
                    <td>
                        <c:set var="startDateStr">
                            <fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>
                        </c:set>
                        <c:set var="endDateStr">
                            <fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>
                        </c:set>
                        <LABEL>
                            <pg:pager url="/forign/content/reply/sublist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="commentid" value="${commentId}"/>
                                <pg:param name="rootid" value="${rootId}"/>
                                <%--   <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>--%>
                                <%@ include file="/WEB-INF/jsp/toolspgwithnewversionjquery.jsp" %>
                            </pg:pager>
                        </LABEL>
                    </td>
                </c:if>
            </tr>
        </form>
    </table>
</c:if>
</td>
</tr>
</table>
</body>
</html>