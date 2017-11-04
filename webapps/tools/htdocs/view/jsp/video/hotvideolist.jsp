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
                return false;
            }

            window.location.href = "/video/modifypage?id=" + modifyId;
        }

        function modifyStatus(modifyId, status) {
            if (status == 'y') {
                alert("请先下线视频再进行操作");
                return false;
            }

            window.location.href = "/video/modifystatus?id=" + modifyId + "&status=n";

        }

        function sendHot() {
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
                data: { "commentids": result },
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
        function updateHotList(gamesdk) {
            $.ajax({
                url: '/hot/video/updatehotlist',
                data: { "gamesdk": gamesdk },
                type: "POST",
                success: function (req) {
                    if (req == "success") {
                        alert("更新成功");
                    }else if(req=="error"){
                        alert("错误");
                    }
                }
            });
        }

        function sort(oldindex,gamekey) {
            var newindex = $("#newindex" + oldindex).val();
            var size = $("#listsize").val();
            var gamesdk = $("#gamesdk").val();
            var intnewIndex = parseInt(newindex);
            var intsize = parseInt(size);
            var intoldindex = parseInt(oldindex);
            if (intnewIndex > intsize) {
                alert("超出排序值范围");
                return ;
            }
            if (intnewIndex == 0) {
                alert("最少请填1");
                $("#newindex" + oldindex).val(intoldindex + 1);
                return ;
            }
            if (intnewIndex - 1 == intoldindex) {
                return ;
            }
            window.location.href = "/hot/video/sort?oldindex=" + oldindex + "&newindex=" + (intnewIndex - 1) + "&gamesdk=" + gamekey;
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
                    <form method="post" action="/hot/video/list">
                        <td>
                            <select name="gamesdk">
                                <c:forEach items="${gamelist}" var="game">

                                    <option value="${game.appId}"
                                            <c:if test="${game.appId eq gamesdk}">selected </c:if>>${game.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td width="100px;">
                            <input type="submit" value="查询"/>
                        </td>


                    </form>

                    <td width="100px;">
                        <input type="button" onclick="updateHotList('${gamesdk}');" value="更新"/>
                    </td>


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
                    <td nowrap align="center" width="60">上传时间</td>
                    <td nowrap align="center" width="120">视频状态</td>
                    <td nowrap align="center" width="80">排序</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td">

                    </td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">

                        <c:forEach items="${list}" var="video" varStatus="st">
                            <tr class="<c:choose><c:when test="
                            ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">
                                    <c:if test="${not empty gamesdk && video.actStatus.code eq 'ing'}">
                                        <input type="checkbox" name="sendhot" value="${video.commentVideoId}"/>
                                    </c:if>
                                        ${video.commentVideoId}
                                </td>
                                <td nowrap align="center">
                                        ${video.videoTitle}
                                    <input type="hidden" value="${list.size()}" id="listsize"/>
                                    <input type="hidden" value="${gamesdk}" id="gamesdk"/>
                                </td>

                                <td nowrap align="center" width="180px">

                                    <fmt:formatDate value="${video.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>

                                </td>
                                <td nowrap align="center">
                                    <a href="/video/modifystatus?id=${video.commentVideoId}&status=ing&url=hot&gamesdk=${gamesdk}">下线</a>
                                </td>
                                <td nowrap align="center" width="80">
                                    <a href="javascript:sort('${st.index}','${gamesdk}')">移动到第</a>
                                    <input type="text" size="2" id="newindex${st.index}" value="${st.index+1}"
                                           onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="2"/>位
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
            </table>
        </td>
    </tr>
</table>
</body>
</html>