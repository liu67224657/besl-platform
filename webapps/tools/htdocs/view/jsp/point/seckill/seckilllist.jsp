<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>秒杀批次管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
            });
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 秒杀批次管理</td>
    </tr>
    <tr>
        <td height="1" valign="top">
            <br/>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">批次列表</td>
                </tr>
            </table>
            <table>
                <tr>
                    <td height="1">
                        <form action="/point/goodsseckill/list" method="post">
                            <table>
                                <tr>
                                    <td>
                                        按条件查询:
                                    </td>
                                    <td>
                                        <table align="left">
                                            <tr>
                                                <td>标题：</td>
                                                <td colspan="3">
                                                    <input type="text" name="qtitle" value="${qTitle}"/>*模糊查询
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>商品ID：</td>
                                                <td>
                                                    <input type="text" name="qgid" value="${qGid}"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>商城类型：</td>
                                                <td colspan="2">
                                                    <select name="qgat">
                                                        <option value="">请选择</option>
                                                        <c:forEach items="${actionTypes}" var="act">
                                                            <option value="${act.code}" <c:if test="${qGat == act.code}">selected="selected"</c:if>>${act.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>时间范围：</td>
                                                <td colspan="3">
                                                    <input type="text" class="Wdate"
                                                           onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd'})"
                                                           readonly="readonly" name="qstart" id="starttime"
                                                           value="${qStart}"/>
                                                    --
                                                    <input type="text" class="Wdate"
                                                           onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd'})"
                                                           readonly="readonly" name="qend" id="endtime"
                                                           value="${qEnd}"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>状态：</td>
                                                <td colspan="3">
                                                    <select name="qstatus">
                                                        <option value="1" <c:if test="${qStatus == 1}">selected="selected"</c:if>>可用</option>
                                                        <option value="0" <c:if test="${qStatus == 0}">selected="selected"</c:if>>删除</option>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <input type="submit" class="default_button" value="查询"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td height="1">
                        <form action="/point/goodsseckill/createpage" method="post">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="qtitle" value="${qTitle}"/>
                                        <input type="hidden" name="qgid" value="${qGid}"/>
                                        <input type="hidden" name="qgat" value="${qGat}"/>
                                        <input type="hidden" name="qstart" value="${qStart}"/>
                                        <input type="hidden" name="qend" value="${qEnd}"/>
                                        <input type="hidden" name="qstatus" value="${qStatus}"/>
                                        <p:privilege name="/point/goodsseckill/createpage">
                                            <input type="submit" name="create_button" class="default_button"
                                                   value="添加批次"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="left">序号</td>
                    <td nowrap align="left">ID</td>
                    <td nowrap align="left">商品</td>
                    <td nowrap align="left">标题</td>
                    <td nowrap align="left">开始时间</td>
                    <td nowrap align="left">结束时间</td>
                    <td nowrap align="left">简介</td>
                    <td nowrap align="left">图片</td>
                    <td nowrap align="left">提示</td>
                    <td nowrap align="left">操作</td>
                    <td nowrap align="left">状态</td>
                    <td nowrap align="left">创建信息</td>
                    <td nowrap align="left">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="seckill" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${st.index + 1}</td>
                                <td nowrap align="left">${seckill.seckillId}</td>
                                <td nowrap align="left">${seckill.goodsId}</td>
                                <td nowrap align="left">${seckill.seckillTitle}</td>
                                <td nowrap align="left">
                                    <fmt:formatDate value="${seckill.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap align="left">
                                    <fmt:formatDate value="${seckill.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap align="left"><textarea rows="3" cols="20">${seckill.seckillDesc}</textarea></td>
                                <td nowrap align="left"><img src="${seckill.seckillPic}" width="100" height="100"/></td>
                                    <script>
                                        var nowTime = new Date();
                                        var start = new Date('${seckill.startTime}'.replace(/-/g,"/"));
                                        var end = new Date('${seckill.endTime}'.replace(/-/g,"/"));
                                        var html = '';
                                        if (nowTime.getTime() < start.getTime()) {
                                            html = '<td nowrap align="left">${seckill.seckillTips.beforeTips}</td>';
                                        } else if (nowTime.getTime() > end.getTime()) {
                                            html = '<td nowrap align="left" style="color: #ff0000;">${seckill.seckillTips.afterTips}</td>';
                                        } else {
                                            html = '<td nowrap align="left" style="color: #008000;">${seckill.seckillTips.inTips}</td>';
                                        }
                                        document.write(html)
                                    </script>
                                <td nowrap align="left">
                                    <a href="/point/goodsseckill/modifypage?seckillid=${seckill.seckillId}&qtitle=${qTitle}&qgid=${qGid}&qgat=${qGat}&qstart=${qStart}&qend=${qEnd}&qstatus=${qStatus}">编辑</a>
                                    <c:choose>
                                        <c:when test="${seckill.removeStatus.code == 1}">
                                            <a href="/point/goodsseckill/remove?seckillid=${seckill.seckillId}&qtitle=${qTitle}&qgid=${qGid}&qgat=${qGat}&qstart=${qStart}&qend=${qEnd}&qstatus=${qStatus}">删除</a>
                                        </c:when>
                                        <c:when test="${seckill.removeStatus.code == 0}">
                                            <a href="/point/goodsseckill/recover?seckillid=${seckill.seckillId}&qtitle=${qTitle}&qgid=${qGid}&qgat=${qGat}&qstart=${qStart}&qend=${qEnd}&qstatus=${qStatus}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="left"
                                        <c:choose>
                                            <c:when test="${seckill.removeStatus.code == 1}">style="color: #008000;" </c:when>
                                            <c:when test="${seckill.removeStatus.code == 0}">style="color: #ff0000;" </c:when>
                                            <c:otherwise></c:otherwise>
                                        </c:choose>>
                                    <c:choose>
                                        <c:when test="${seckill.removeStatus.code == 1}">可用</c:when>
                                        <c:when test="${seckill.removeStatus.code == 0}">删除</c:when>
                                        <c:otherwise></c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="left">
                                        ${seckill.createUser}<br/>
                                    <fmt:formatDate value="${seckill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap align="left">
                                        ${seckill.modifyUser}<br/>
                                    <fmt:formatDate value="${seckill.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
                            <pg:pager url="/point/goodsseckill/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="qtitle" value="${qTitle}"/>
                                <pg:param name="qgid" value="${qGid}"/>
                                <pg:param name="qgat" value="${qGat}"/>
                                <pg:param name="qstart" value="${qStart}"/>
                                <pg:param name="qend" value="${qEnd}"/>
                                <pg:param name="qstatus" value="${qStatus}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
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