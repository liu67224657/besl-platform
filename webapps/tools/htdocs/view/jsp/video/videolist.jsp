<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>


    <script>
        function modifyvideo(modifyId, status) {
            if (status == 'y') {
                alert("请先下线视频再进行操作");
                return;
            }

            var type = document.getElementById('sel_type').value;
            var gamesdk = document.getElementById('sel_gamesdk').value;
            var actstatus = document.getElementById('sel_actstatus').value;

            window.location.href = "/video/modifypage?id=" + modifyId + "&type=" + type + "&gamesdk=" + gamesdk + "&actstatus=" + actstatus;
        }


        function modifyStatus(modifyId, status) {
            if (status == 'y') {
                alert("请先下线视频再进行操作");
                return;
            }
            if (confirm('确认要删除视频么？该操作不可逆')) {
                var type = document.getElementById('sel_type').value;
                var gamesdk = document.getElementById('sel_gamesdk').value;
                var actstatus = document.getElementById('sel_actstatus').value;

                window.location.href = "/video/modifystatus?id=" + modifyId + "&status=n" + "&type=" + type + "&gamesdk=" + gamesdk + "&actstatus=" + actstatus;
            }
        }

        function sendHot() {

            var gamekey = '${gamesdk}';
            if (gamekey == '') {
                alert("请先按游戏查询再发布到热门");
                return;
            }

            var sendHot = $("input[name='sendhot']:checked");
            if (sendHot.size() == 0) {
                alert("你还没有选择视频");
                return false;
            }
            if (sendHot.size() > 6) {
                alert("最多只能发布6个热门视频");
                return false;
            }
            var result = new Array();
            $("input[name='sendhot']:checked").each(function () {
                result.push(this.value);
            });
            result = result.join('@');
            $.ajax({
                url: '/video/sendhot',
                data: { "commentids": result, gamekey: gamekey },
                dataType: "json",
                type: "POST",
                success: function (req) {
                    if (req.rs == '-2') {
                        alert("发布失败，热门视频已满");
                        return;
                    } else if (req.rs == '-1') {
                        alert("冲突，只能发布" + req.result + "个视频");
                        return;
                    } else if (req.rs == '1') {
                        alert("发布成功");
                        window.location.href = "/video/list?title=" + '${title}' + "&type=" + '${type}' + "&gamesdk=" + '${gamesdk}' + "&actstatus=" + '${actstatus}';
                        return;
                    }
                }
            });

        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 视频管理 >>视频管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">着迷视频列表管理</td>
    </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>

        <td class="list_table_header_td">
            <form method="post" action="javascript:void(0);">
                <input type="submit" name="button" onclick="sendHot();" value="发布到热门"/>
            </form>
        </td>
        <td class="list_table_header_td" width="65">
            <form method="post" action="/hot/video/list">
                <input type="submit" name="button" value="热门视频列表"/>
            </form>

        </td>
        <td class="list_table_header_td" width="65">
            <form method="post" action="/video/createpage">
                <input type="submit" name="button" value="上传视频"/>
            </form>

        </td>


    </tr>
    <tr style="padding-right: 10px;">
        <form method="post" action="/video/list">

            <td class="list_table_header_td" width="210">视频标题:<input type="text" name="title" value="${title}"/>
            </td>
            <td width="100px;">
                <select name="type" id="sel_type">
                    <option value="">视频来源</option>
                    <option value="0" <c:if test="${type eq 0}">selected </c:if>>用户上传</option>
                    <option value="1" <c:if test="${type eq 1}">selected </c:if>>后台上传</option>
                </select>

            </td>
            <td width="100px;">
                <select name="gamesdk" id="sel_gamesdk">
                    <option value="">请选择游戏</option>
                    <c:forEach items="${gamelist}" var="game">

                        <option value="${game.appId}"
                                <c:if test="${game.appId eq gamesdk}">selected </c:if>>${game.appName}</option>
                    </c:forEach>
                </select>

            </td>
            <td>
                <select name="actstatus" id="sel_actstatus">
                    <option value="">请选择状态</option>
                    <option value="ing" <c:if test="${actstatus eq 'ing'}">selected </c:if>>未上线</option>
                    <option value="y" <c:if test="${actstatus eq 'y'}">selected </c:if>>已上线</option>
                    <option value="n" <c:if test="${actstatus eq 'n'}">selected </c:if>>已删除</option>
                </select>
                <input type="submit" name="button" value="查询"/>

            </td>

        </form>


    </tr>

</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="9" class="error_msg_td">
                <fmt:message key="${errorMsg}" bundle="${error}"/>
            </td>
        </tr>
    </c:if>

    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="9" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="center" width="30"> ID</td>
        <td nowrap align="center" width="200">视频标题</td>
        <td nowrap align="center" width="60">视频来源</td>
        <td nowrap align="center" width="60">所属游戏</td>
        <td nowrap align="center" width="60">上传时间</td>
        <%--<td nowrap align="center" width="100">APP描述</td>--%>
        <%--<td nowrap align="center">我的模块</td>--%>
        <td nowrap align="center" width="60">操作人</td>
        <td nowrap align="center" width="120">视频状态</td>
        <td nowrap align="center" width="80">操作</td>
    </tr>
    <tr>
        <td height="1" colspan="9" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="video" varStatus="st">
                <tr class="<c:choose><c:when test="
                            ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap align="center" width="20px;">
                        <c:if test="${video.actStatus.code eq 'ing'}">
                            <input type="checkbox" name="sendhot" value="${video.commentVideoId}"/>
                        </c:if>
                            ${video.commentVideoId}
                    </td>
                    <td nowrap align="center">
                            ${video.videoTitle}
                    </td>

                    <td nowrap align="center">
                        <fmt:message key="video.comment.type.${video.commentVideoType.code}"
                                     bundle="${def}"/>
                    </td>

                    <td nowrap align="center">
                        <c:forEach items="${gamelist}" var="game">
                            <c:if test="${game.appId eq video.sdk_key}"> ${game.appName} </c:if>
                        </c:forEach>
                    </td>
                    <td nowrap align="center" width="180px">

                        <fmt:formatDate value="${video.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>

                    </td>
                    <td nowrap align="center">
                        <c:choose>
                            <c:when test="${video.commentVideoType.code eq 1}">
                                管理员
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${profiles}" var="profile">
                                    <c:if test="${profile.profileId eq video.profileid}">
                                        ${profile.nick}
                                    </c:if>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td nowrap align="center">
                        <c:choose>
                            <c:when test="${video.actStatus.code eq 'y'}">
                                            <span style="color: red;">
                                                <fmt:message key="video.status.type.${video.actStatus.code}"
                                                             bundle="${def}"/>
                                            </span>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="video.status.type.${video.actStatus.code}"
                                             bundle="${def}"/>
                            </c:otherwise>
                        </c:choose>


                    </td>
                    <td nowrap align="center">
                        <a href="javascript:modifyvideo('${video.commentVideoId}','${video.actStatus.code}');">编辑</a>
                        <c:if test="${video.actStatus.code ne 'n'}">
                            <a href="javascript:modifyStatus('${video.commentVideoId}','${video.actStatus.code}')">删除</a>
                        </c:if>


                            <%--<c:choose>--%>
                            <%--<c:when test="${app.actStatus.code!='n'}">--%>
                            <%--<a href="/video/game/delete?appid=${video.commetVideoId}&status=valid">恢复</a>--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                            <%--<a href="/video/game/delete?appid=${video.commetVideoId}&status=invalid">删除</a>--%>
                            <%--</c:otherwise>--%>
                            <%--</c:choose>--%>

                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="9" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="9" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="9" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="9">
                <pg:pager url="/video/list"
                          items="${page.totalRows}" isOffset="true"
                          maxPageItems="${page.pageSize}"
                          export="offset, currentPageNumber=pageNumber" scope="request">
                    <pg:param name="resourceName" value="${resourceName}"/>
                    <pg:param name="removeStatusCode" value="${removeStatusCode}"/>
                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                    <pg:param name="items" value="${page.totalRows}"/>
                    <%--<pg:param name="appname" value="${appName}"/>--%>
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