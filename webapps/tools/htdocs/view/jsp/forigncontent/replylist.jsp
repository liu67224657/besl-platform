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
<table width="60%" border="0" cellspacing="0" cellpadding="0">
    <form action="/forign/content/reply/allremove" method="post" id="all_remove_form">
        <tr>
            <td>
                <input type="hidden" name="screenname" value="${screenName}"/>
                <input type="hidden" name="startdate"
                       value="<fmt:formatDate value='${startDate}' pattern='yyyy-MM-dd'/> "/>
                <input type="hidden" name="enddate"
                       value="<fmt:formatDate value='${endDate}' pattern='yyyy-MM-dd'/>"/>
                <input type="hidden" name="unikey" value="${uniKey}"/>
                <input type="hidden" name="statuscode" value="${statusCode}"/>
                <input type="hidden" name="refreshStatus" value="${refreshStatus}"/>
                <input type="hidden" name="hide15Status" value="${hide15Status}"/>
                <input type="hidden" name="replyStatus" value="${replyStatus}"/>
            </td>
        </tr>
    </form>
    <form action="/forign/content/reply/list" method="post" id="form_submit_search">
        <tr>
            <td width="80" align="center">搜索条件</td>
            <td>
                <table border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td width="80" align="right" class="edit_table_defaulttitle_td">用户昵称：</td>
                        <td>
                            <input id="input_scrrenname" name="screenname" type="text"
                                   class="default_input_singleline"
                                   size="32"
                                   maxlength="32" value="${screenName}">
                            [暂不可模糊搜索]
                            <c:if test="${statusCode eq 'n' && not empty screenName && not empty list}">
                                <input type="button" name="button" value="全部删除" id="button_allremove"
                                       class="default_button"/>
                                <span style="color: #ff0000;">*谨慎操作</span>
                            </c:if>
                        </td>
                    </tr>

                    <tr>
                        <c:choose>
                            <c:when test="${domainCode!=9}">
                                <td width="80" align="right" class="edit_table_defaulttitle_td">唯一标识unikey：</td>
                                <td>
                                    <select name="domain" id="select_domain">
                                        <c:forEach items="${domainSet}" var="domain">
                                            <option value="${domain.code}"
                                                    <c:if test="${domainCode==domain.code}">selected="selected"</c:if>>
                                                <fmt:message key="comment.domain.${domain.code}"
                                                             bundle="${def}"/></option>
                                        </c:forEach>
                                        <input name="unikey" type="text" class="default_input_singleline" size="30"
                                               id="input_content_url" value="${uniKey}">
                                        <br/>
                                        <a target="_blank"
                                           href="http://wiki.enjoyf.com/index.php?title=%E8%AF%84%E8%AE%BA%E4%BA%A4%E4%BA%92%E7%B3%BB%E7%BB%9F#unikey--domain.E8.AF.B4.E6.98.8E">填写说明点此查看</a>
                                    </select>
                                </td>

                            </c:when>
                            <c:otherwise>
                                <td width="80" align="right" class="edit_table_defaulttitle_td">节目ID：</td>
                                <td>
                                    <select name="domain" id="select_domain1">
                                        <option value="${domainCode}"
                                                selected="selected">
                                            <fmt:message key="comment.domain.${domainCode}"
                                                         bundle="${def}"/></option>
                                    </select>
                                    <input name="commentid" type="text" class="default_input_singleline" size="30"
                                           value="${commentId}">
                                </td>

                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <c:if test="${domainCode !=9}">
                        <tr>
                            <td width="80" align="right" class="edit_table_defaulttitle_td">二级key：</td>
                            <td>
                                <input name="subkey" type="text" class="default_input_singleline" size="30"
                                       id="" value="${subKey}">如：op、dtcq、......
                            </td>
                        </tr>
                        <tr>
                            <td width="80" align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                            <td>
                                <input id="startDate" name="startdate" type="text"
                                       class="default_input_singleline" size="8" maxlength="10"
                                       value="<fmt:formatDate value='${startDate}' pattern='yyyy-MM-dd' />"/>
                                -
                                <input id="endDate" name="enddate" type="text" class="default_input_singleline"
                                       size="8" maxlength="10"
                                       value="<fmt:formatDate value='${endDate}' pattern='yyyy-MM-dd' /> "/>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td width="30" align="right" class="edit_table_defaulttitle_td">状态：</td>
                        <td>
                            <select name="statuscode" id="input_statuscode" class="default_select_single">
                                <option value="">全部</option>
                                <option value="n"
                                        <c:if test="${statusCode eq 'n'}">selected="selected"</c:if>>未审核
                                </option>
                                <option value="ing"
                                        <c:if test="${statusCode eq 'ing'}">selected="selected"</c:if>>已审核
                                </option>
                                <option value="y"
                                        <c:if test="${statusCode eq 'y'}">selected="selected"</c:if>>已删除
                                </option>
                            </select>
                        </td>
                    </tr>
                    <c:if test="${domainCode !=9}">
                        <tr>
                            <td width="30" align="right" class="edit_table_defaulttitle_td">评论回复状态：</td>
                            <td>
                                <select name="replyStatus" id="inputReplyStatus" class="default_select_single">
                                    <option value="">全部</option>
                                    <option value="noReply"
                                            <c:if test="${replyStatus == 'noReply'}">selected="selected"</c:if>>
                                        无回复
                                    </option>
                                    <option value="hasReply"
                                            <c:if test="${replyStatus=='hasReply'}">selected="selected"</c:if>>
                                        有回复
                                    </option>
                                    <option value="hasCsReply"
                                            <c:if test="${replyStatus == 'hasCsReply'}">selected="selected"</c:if>>
                                        有客服的回复
                                    </option>
                                    <option value="csReply"
                                            <c:if test="${replyStatus == 'csReply'}">selected="selected"</c:if>>
                                        来自客服的回复
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td width="100" align="right" class="edit_table_defaulttitle_td">每页显示条数：</td>
                            <td>
                                <select name="maxPageItems" class="default_select_single"
                                        id="inputMaxPageItems">
                                    <option value="50"
                                            <c:if test="${page.pageSize==50}">selected="selected"</c:if> >50
                                    </option>
                                    <option value="100"
                                            <c:if test="${page.pageSize==100}">selected="selected"</c:if> >100
                                    </option>
                                    <option value="150"
                                            <c:if test="${page.pageSize==150}">selected="selected"</c:if> >150
                                    </option>
                                    <option value="200"
                                            <c:if test="${page.pageSize==200}">selected="selected"</c:if> >200
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td width="100" align="right" class="edit_table_defaulttitle_td">评论内容：</td>
                            <td>
                                <input name="bodytext" type="text" value="${bodyText}">
                            </td>
                        </tr>
                    </c:if>
                </table>
            </td>
            <td width="80" align="center">
                <input type="submit" id="formSearch" class="default_button" value=" 搜索 ">
                <%--      <input id="refreshSearch" type="button" class="default_button" value=" 刷新 "> --%>
            </td>
        </tr>
    </form>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <form action="/forign/content/reply/batchupdate" method="post" id="form_submit">
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
                <input type="hidden" name="pager.offset" value="${page.startRowIdx}"/>
                <input type="hidden" name="domain" value="${domainCode}"/>
                <input type="hidden" name="screenname" value="${screenName}"/>
                <input type="hidden" name="unikey" value="${uniKey}"/>
                <input type="hidden" name="subkey" value="${subKey}"/>
                <input type="hidden" name="startdate"
                       value="<fmt:formatDate value='${startDate}' pattern='yyyy-MM-dd'/>"/>
                <input type="hidden" name="enddate"
                       value="<fmt:formatDate value='${endDate}' pattern='yyyy-MM-dd'/>"/>
                <input type="hidden" name="statuscode" value="${statusCode}"/>
                <input type="hidden" name="refreshStatus" value="${refreshStatus}"/>
                <input type="hidden" name="hide15Status" value="${hide15Status}"/>
                <input type="hidden" name="replyStatus" value="${replyStatus}"/>
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
                        <pg:pager url="/forign/content/reply/list"
                                  items="${page.totalRows}" isOffset="true"
                                  maxPageItems="${page.pageSize}"
                                  export="offset, currentPageNumber=pageNumber" scope="request">
                            <pg:param name="screenname" value="${screenName}"/>
                            <pg:param name="unikey" value="${uniKey}"/>
                            <pg:param name="refreshStatus" value="${refreshStatus}"/>
                            <pg:param name="replyStatus" value="${replyStatus}"/>
                            <pg:param name="startdate" value="${startDateStr}"/>
                            <pg:param name="enddate" value="${endDateStr}"/>
                            <pg:param name="statuscode" value="${statusCode}"/>
                            <pg:param name="domain" value="${domainCode}"/>
                            <pg:param name="subkey" value="${subKey}"/>
                            <pg:param name="bodytext" value="${bodyText}"/>
                            <pg:param name="currentPageNumber" value="${page.curPage}"/>
                            <pg:param name="maxPageItems" value="${page.pageSize}"/>
                            <pg:param name="items" value="${page.totalRows}"/>
                            <pg:param name="commentid" value="${commentId}"/>

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
        <td nowrap="nowrap" align="left" width="70">主评论楼层</td>
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
                    <td nowrap="nowrap">${reply.floorNum}</td>
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
                                   onclick='auditItemById("/forign/content/reply/batchupdate?subkey=${subKey}&refreshStatus=${refreshStatus}&hide15Status=${hide15Status}&replyStatus=${replyStatus}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=ing&screenname=${screenName}&unikey=${uniKey}&statuscode=${statusCode}&startdate=<fmt:formatDate value='${startDate}'
                                                                   pattern='yyyy-MM-dd'/>&enddate=<fmt:formatDate
                                                       value='${endDate}'
                                                       pattern='yyyy-MM-dd'/>&domain=${domainCode}&commentid=${commentId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>审核</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a href='javascript:;'
                                   onclick='deleteItemById("/forign/content/reply/batchupdate?subkey=${subKey}&refreshStatus=${refreshStatus}&hide15Status=${hide15Status}&replyStatus=${replyStatus}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=y&screenname=${screenName}&unikey=${uniKey}&statuscode=${statusCode}&startdate=<fmt:formatDate value='${startDate}'
                                                                   pattern='yyyy-MM-dd'/>&enddate=<fmt:formatDate
                                                       value='${endDate}'
                                                       pattern='yyyy-MM-dd'/>&domain=${domainCode}&commentid=${commentId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>删除</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="/forign/content/reply/replypage?replyid=${reply.replyId}">回复</a>
                            </c:when>
                            <c:when test="${reply.removeStatus.code=='ing'}">
                                <a href='javascript:;'
                                   onclick='deleteItemById("/forign/content/reply/batchupdate?subkey=${subKey}&refreshStatus=${refreshStatus}&hide15Status=${hide15Status}&replyStatus=${replyStatus}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=y&screenname=${screenName}&unikey=${uniKey}&statuscode=${statusCode}&startdate=<fmt:formatDate value='${startDate}'
                                                                   pattern='yyyy-MM-dd'/>&enddate=<fmt:formatDate
                                                       value='${endDate}'
                                                       pattern='yyyy-MM-dd'/>&domain=${domainCode}&commentid=${commentId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>删除</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="/forign/content/reply/replypage?replyid=${reply.replyId}">回复</a>
                            </c:when>
                            <c:otherwise>

                                <a href='javascript:;'
                                   onclick='auditItemById("/forign/content/reply/batchupdate?subkey=${subKey}&refreshStatus=${refreshStatus}&hide15Status=${hide15Status}&replyStatus=${replyStatus}&replyids=${reply.replyId}_${reply.rootId}_${reply.parentId}_${reply.commentId}_${reply.removeStatus.code}&updatestatuscode=ing&screenname=${screenName}&unikey=${uniKey}&statuscode=${statusCode}&startdate=<fmt:formatDate value='${startDate}'
                                                                   pattern='yyyy-MM-dd'/>&enddate=<fmt:formatDate
                                                       value='${endDate}'
                                                       pattern='yyyy-MM-dd'/>&domain=${domainCode}&commentid=${commentId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}","${reply.replyId}")'>审核</a>

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
        <form action="/forign/content/reply/batchupdate" method="post" id="form_submit2">
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
                    <input type="hidden" name="pager.offset" value="${page.startRowIdx}">
                    <input type="hidden" name="domain" value="${domainCode}">
                    <input type="hidden" name="screenname" value="${screenName}">
                    <input type="hidden" name="unikey" value="${uniKey}">
                    <input type="hidden" name="subkey" value="${subKey}">
                    <input type="hidden" name="startdate"
                           value="<fmt:formatDate value='${startDate}' pattern='yyyy-MM-dd' />"/>
                    <input type="hidden" name="enddate"
                           value="<fmt:formatDate value='${endDate}' pattern='yyyy-MM-dd' />"/>
                    <input type="hidden" name="statuscode" value="${statusCode}"/>
                    <input type="hidden" name="refreshStatus" value="${refreshStatus}"/>
                    <input type="hidden" name="hide15Status" value="${hide15Status}"/>
                    <input type="hidden" name="replyStatus" value="${replyStatus}"/>
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
                            <pg:pager url="/forign/content/reply/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="screenname" value="${screenName}"/>
                                <pg:param name="unikey" value="${uniKey}"/>
                                <pg:param name="refreshStatus" value="${refreshStatus}"/>
                                <pg:param name="replyStatus" value="${replyStatus}"/>
                                <pg:param name="startdate" value="${startDateStr}"/>
                                <pg:param name="enddate" value="${endDateStr}"/>
                                <pg:param name="statuscode" value="${statusCode}"/>
                                <pg:param name="domain" value="${domainCode}"/>
                                <pg:param name="subkey" value="${subKey}"/>
                                <pg:param name="bodytext" value="${bodyText}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
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