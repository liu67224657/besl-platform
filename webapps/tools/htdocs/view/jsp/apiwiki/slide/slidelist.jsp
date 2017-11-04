<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>轮播图</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('a[name=sort]').click(function () {
                var sort = $(this).attr('data-sort');
                var id = $(this).attr('data-id');
                var displayorder = $(this).attr('data-displayorder');

                var otherid = "";
                var otherdisplayorder = "";
                if (sort == "up") {
                    otherid = $(this).parent().parent().prev().find("td:first").html();
                } else {
                    otherid = $(this).parent().parent().next().find("td:first").html();
                }
                if (otherid != "") {
                    otherdisplayorder = $("#display_" + otherid).val();
                }
                if (otherdisplayorder == "") {
                    if (sort == "up") {
                        alert("已经是第一条记录");
                        return false;
                    } else {
                        alert("已经是最后一条记录");
                        return false;
                    }
                }
                //alert(otherid+"===="+otherdisplayorder)
                $.post("/apiwiki/slide/sort", {
                    id: id,
                    displayorder: displayorder,
                    otherid: otherid,
                    otherdisplayorder: otherdisplayorder,
                    sort: sort,
                    removestatus: '${removestatus}',
                    platform: '${platform}'
                }, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        window.location.reload();
                    } else {
                        alert(rsobj.msg);
                    }
                });
            });
        });
    </script>
    <style>
        .name {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .target {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 推荐轮播图</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form action="/apiwiki/slide/list" method="post">
                            状态：<select name="removestatus">
                            <option value="-1" <c:if test="${removestatus==''}">selected</c:if>>全部</option>
                            <option value="valid" <c:if test="${removestatus=='valid'}">selected</c:if>>可用</option>
                            <option value="invalid" <c:if test="${removestatus=='invalid'}">selected</c:if>>待上架</option>
                            <option value="removed" <c:if test="${removestatus=='removed'}">selected</c:if>>不可用</option>
                        </select>
                            平台：<select name="platform">
                            <option value="" <c:if test="${platform==''}">selected</c:if>>全部</option>
                            <option value="0" <c:if test="${platform=='0'}">selected</c:if>>IOS</option>
                            <option value="1" <c:if test="${platform=='1'}">selected</c:if>>Android</option>
                        </select>
                            <input type="submit" name="button" class="default_button" value="搜索"/>
                        </form>
                    </td>
                    <td>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        <form action="/apiwiki/slide/createpage" method="post">
                            <input type="submit" name="button" class="default_button" id="default_button2"
                                   value="添加轮播图"/>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">ID</td>
                    <td nowrap align="center" width="">名称</td>
                    <td nowrap align="center" width="">链接</td>
                    <td nowrap align="center" width="50px">平台</td>
                    <td nowrap align="center">上架时间</td>
                    <td nowrap align="center">下架时间</td>
                    <td nowrap align="center" width="80px">状态</td>
                    <td nowrap align="center" width="80px">排序操作</td>
                    <td nowrap align="center" width="80px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.id}</td>
                                <input type="hidden" id="display_${dto.id}" size="48" value="${dto.displayOrder}"/>
                                <td nowrap class="name" title="${dto.title}">${dto.title}</td>
                                <td nowrap class="target" title="${dto.target}">${dto.target}</td>
                                <td nowrap align="center">
                                    <c:if test="${dto.platform==0}">IOS</c:if>
                                    <c:if test="${dto.platform==1}">Android</c:if>
                                </td>
                                <td nowrap class="name">
                                    <fmt:formatDate value="${dto.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap class="name">
                                    <fmt:formatDate value="${dto.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap align="center">
                                    <c:if test="${dto.removeStatus.code=='valid'}">
                                        可用
                                    </c:if>
                                    <c:if test="${dto.removeStatus.code=='invalid'}">
                                        待上架
                                    </c:if>
                                    <c:if test="${dto.removeStatus.code=='removed'}">
                                        不可用
                                    </c:if>
                                </td>
                                <td nowrap align="center">
                                    <a href="javascript:void(0);" name="sort" data-id="${dto.id}" data-sort="up"
                                       data-displayorder="${dto.displayOrder}"><img
                                            src="/static/images/icon/up.gif"></a>
                                    <a href="javascript:void(0);" name="sort" data-id="${dto.id}" data-sort="down"
                                       data-displayorder="${dto.displayOrder}"><img src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap align="center">
                                    <a href="/apiwiki/slide/modifypage?id=${dto.id}">编辑</a>&nbsp;&nbsp;&nbsp;


                                    <c:choose>
                                        <c:when test="${dto.removeStatus.code=='valid' || dto.removeStatus.code=='invalid' }">
                                            <a href="/apiwiki/slide/updatestatus?id=${dto.id}&removestatus=${removestatus}&platform=${platform}&status=removed&pageStartIndex=${pageStartIndex}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${now<dto.endTime}">
                                                    <a href="/apiwiki/slide/updatestatus?id=${dto.id}&removestatus=${removestatus}&platform=${platform}&status=valid&pageStartIndex=${pageStartIndex}">恢复</a>
                                                </c:when>
                                                <c:otherwise>
                                                    不可用
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>


                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/apiwiki/slide/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="removestatus" value="${removestatus}"/>
                                <pg:param name="platform" value="${platform}"/>
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