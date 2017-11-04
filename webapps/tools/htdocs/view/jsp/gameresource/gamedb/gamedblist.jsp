<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏资料库</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            <c:forEach items="${list}" var="gamedb" varStatus="st">
            var div = $("<div id='divStatus${st.index+1}'></div>");
            var select = $("<select name='validStatus${st.index+1}' id='validStatus${st.index+1}'></select>");
            var option1 = $("<option value='invalid' <c:if test="${gamedb.validStatus.code=='invalid'}">selected</c:if> >审核中</option>");
            var option2 = $("<option value='valid' <c:if test="${gamedb.validStatus.code=='valid'}">selected</c:if>>审核通过</option>");
            var option3 = $("<option value='notvalid' <c:if test="${gamedb.validStatus.code=='notvalid'}">selected</c:if>>审核未通过</option>");
            var option4 = $("<option value='removed' <c:if test="${gamedb.validStatus.code=='removed'}">selected</c:if>>删除</option>");
            select.append(option1);
            select.append(option2);
            select.append(option3);
            select.append(option4);
            var confirm = $('<input type="button" value="确认修改" onclick="submitStatus(${gamedb.gameDbId},${st.index+1})" />');
            var cancel = $('<input type="button" value="取消" onclick="cancel(${st.index+1});" />');
            div.append(select);
            div.append(confirm);
            div.append(cancel);
            $("#statusspan" + '${st.index+1}').before(div);
            $("#divStatus" + '${st.index+1}').css("display", "none");
            </c:forEach>

            $('select[id^=select_appkey_]').each(function () {
                var appKey = $(this).val();
                var index = $(this).attr('id').replace('select_appkey_', '');
                var typeTags = $('#select_type_tags_' + index).val();
                $('#a_push_createpage_' + index).attr('href', '/joymeapp/push/createpage?' + appKey + '&' + typeTags);
            });
        });
        function modifyStatus(status, index) {
            $("#divStatus" + index).css("display", "block");
            $("#statusspan" + index).css("display", "none");
        }

        function cancel(index) {
            $("#divStatus" + index).css("display", "none");
            $("#statusspan" + index).css("display", "block");
        }

        function submitStatus(id, index) {
            var gameid = id;
            var recoverstatus = $("#validStatus" + index).val();
            window.location = "/gamedb/recover?recoverstatus=" + recoverstatus + "&gamedbid=" + gameid + "&validstatus=${validstatus}&searchname=${searchname}&pageStartIndex=${page.startRowIdx}";
        }

        function addParam(idx) {
            var appKey = $('#select_appkey_' + idx).val();
            var typeTags = $('#select_type_tags_' + idx).val();
            $('#a_push_createpage_' + idx).attr('href', '/joymeapp/push/createpage?' + appKey + '&' + typeTags);
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 条目维护 >> 游戏资料库</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">游戏资料库</td>

                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td colspan="10">
                        <form method="post" action="/gamedb/createpage">
                            <input type="submit" name="button" class="default_button" value="添加新游戏"/>
                        </form>
                    <td>
                </tr>
                <tr>
                    <td>
                        <form method="post" action="/gamedb/list">
                            按状态查询：
                            <select name="validstatus">
                                <option value=""
                                        <c:if test="${validstatus==''}">selected</c:if> >请选择
                                </option>
                                <option value="invalid"
                                        <c:if test="${validstatus=='invalid'}">selected</c:if> >审核中
                                </option>
                                <option value="valid"
                                        <c:if test="${validstatus=='valid'}">selected</c:if>>审核通过
                                </option>
                                <option value="notvalid"
                                        <c:if test="${validstatus=='notvalid'}">selected</c:if>>
                                    审核未通过
                                </option>
                                <option value="removed"
                                        <c:if test="${validstatus=='removed'}">selected</c:if>>删除
                                </option>
                            </select>
                            <input type="submit" name="button" class="default_button" value="查询"/>
                        </form>
                    <td>
                <tr>
                <tr>
                    <td colspan="20">
                        <form method="post" action="/gamedb/list">
                            按名称搜索： <input type="text" name="searchname" value="${searchname}"/>
                            <input type="submit" name="button" class="default_button" value="搜索"/>
                        </form>
                    </td>
                </tr>

            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">ID</td>
                    <td nowrap align="center">游戏名称</td>
                    <td nowrap align="center">别名</td>
                    <td nowrap align="center">平台</td>
                    <td nowrap align="center">攻略</td>
                    <td nowrap align="center">资讯</td>
                    <td nowrap align="center">视频</td>
                    <td nowrap align="center">WIKI KEY</td>
                    <td nowrap align="center">是否点评</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="gamedb" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${gamedb.gameDbId}</td>
                                <td nowrap align="center">
                                        ${gamedb.gameName}
                                </td>
                                <td nowrap align="center">
                                        ${gamedb.anotherName}
                                </td>
                                <td nowrap align="center">
                                    <c:forEach items="${gamedb.platformTypeSet}" var="platformType">
                                        ${platformType.desc}
                                    </c:forEach>
                                </td>
                                <td nowrap align="center">
                                    <a href="/gamedb/archives?gamedbid=${gamedb.gameDbId}&contenttype=4">${gamedb.guideSum}</a>
                                </td>
                                <td nowrap align="center">
                                    <a href="/gamedb/archives?gamedbid=${gamedb.gameDbId}&contenttype=0">${gamedb.newsSum}</a>
                                </td>
                                <td nowrap align="center">
                                    <a href="/gamedb/archives?gamedbid=${gamedb.gameDbId}&contenttype=1">${gamedb.videoSum}</a>
                                </td>
                                <td nowrap align="center">
                                        ${gamedb.wikiKey}
                                </td>
                                <td nowrap align="center">
                                    <c:if test="${gamedb.comment==true}">是</c:if>
                                    <c:if test="${gamedb.comment==false}">否</c:if>
                                </td>
                                <td nowrap align="center"
                                    <c:if test="${gamedb.validStatus.code=='removed'}">style="color:red"</c:if>
                                    <c:if test="${gamedb.validStatus.code=='notvalid'}">style="color:red"</c:if>
                                    <c:if test="${gamedb.validStatus.code=='valid'}">style="color: #008000;"</c:if> >
                                    <span id="statusspan${st.index+1}">
                                        <fmt:message key="gamedb.validStatus.type.${gamedb.validStatus.code}"
                                                     bundle="${def}"/>
                                    </span>
                                </td>
                                <td nowrap align="center">
                                    <a href="/gamedb/updatepage?gamedbid=${gamedb.gameDbId}&validstatus=${validstatus}&searchname=${searchname}&pageStartIndex=${page.startRowIdx}">修改</a>
                                    &nbsp;&nbsp;
                                    <a href="/gamedb/delete?gamedbid=${gamedb.gameDbId}&validstatus=${validstatus}&searchname=${searchname}&pageStartIndex=${page.startRowIdx}">删除</a>
                                    &nbsp;&nbsp;
                                    <a href="/gamedb/recover?recoverstatus=valid&gamedbid=${gamedb.gameDbId}&validstatus=${validstatus}&searchname=${searchname}&pageStartIndex=${page.startRowIdx}">通过审核</a>
                                    &nbsp;&nbsp;
                                    <a href="javascript:modifyStatus('${gamedb.validStatus.code}','${st.index+1}')"
                                       id="aStatus${st.index+1}">状态修改</a>
                                    &nbsp;&nbsp;
                                    <a href="/gamedb/relation/page?gamedbid=${gamedb.gameDbId}">关联WIKI</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="13" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="13" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="13" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="13">
                            <pg:pager url="/gamedb/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="validstatus" value="${validstatus}"/>
                                <pg:param name="sort" value="${sort}"/>
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