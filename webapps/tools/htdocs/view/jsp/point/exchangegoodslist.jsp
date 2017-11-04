<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>商品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('select[id^=select_appkey_]').each(function () {
                var appKey = $(this).val();
                var index = $(this).attr('id').replace('select_appkey_', '');
                var typeTags = $('#select_type_tags_' + index).val();
                $('#a_push_createpage_' + index).attr('href', '/joymeapp/push/createpage?' + appKey + '&' + typeTags);
            });
        });

        function modifySort(index) {

            $("#displayOrder" + index).removeAttr("disabled");
            $("#save" + index).css("display", "inline");
            $("#cancel" + index).css("display", "inline");
            $("#update" + index).css("display", "none");
        }

        function cancelSort(index, displayorder) {
            $("#displayOrder" + index).val(displayorder);
            $("#displayOrder" + index).attr("disabled", "true");
            $("#save" + index).css("display", "none");
            $("#cancel" + index).css("display", "none");
            $("#update" + index).css("display", "inline");
        }

        function submitSort(id, index) {
            var display = $("#displayOrder" + index).val();
            if (isNaN(display)) {
                alert("请填写数字");
                return false;
            }
            var name = $("#subjectName").val();
            var activityType = $("#activityType").val();
            var shopType = $("#shoptypes").val();
            var startIdx = $("#pageStartIndex").val();
            window.location = "/point/exchangegoods/sort?goodsid=" + id + "&displayorder=" + display + "&subject=" + name + "&activitytype=" + activityType+"&pageStartIndex="+startIdx+"&shoptypes="+shopType;
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 活动信息管理</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">激活码名称列表</td>
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
        <td height="1" class="default_line_td">
            <input type="hidden" id="subjectName" value="${subject}"/>
            <input type="hidden" id="activityType" value="${activityType}"/>
            <input type="hidden" id="shoptypes" value="${shop_types}"/>
            <input type="hidden" id="pageStartIndex" value="${page.startRowIdx}"/>

        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form method="post" action="/point/exchangegoods/createpage?activitytype=1">
                <p:privilege name="/point/exchangegoods/createpage">
                    <input type="submit" name="button" class="default_button" value="添加礼包信息"/>
                </p:privilege>
            </form>
            <form method="post" action="/point/exchangegoods/createpage?activitytype=0&seckilltype=0">
                <p:privilege name="/point/exchangegoods/createpage">
                    <input type="submit" name="button" class="default_button" value="添加兑换商城信息"/>
                </p:privilege>
            </form>
            <form method="post" action="/point/exchangegoods/createpage?activitytype=0&seckilltype=1">
                <p:privilege name="/point/exchangegoods/createpage">
                    <input type="submit" name="button" class="default_button" value="添加秒杀商城信息"/>
                </p:privilege>
            </form>

            <a href="/point/exchangegoods/exclusive" target="_blank">PC礼包中心独家礼包列表</a>
            <br/>
            <br/>
               <a href="/point/exchangegoods/exclusive?type=1" target="_blank">微信礼包搜索页面热词列表</a>
            <%--<form method="post" action="/point/exchangegoods/createpage?activitytype=0">--%>
                <%--<p:privilege name="/point/exchangegoods/createpage">--%>

                    <%--<input type="submit" name="button" class="default_button" value="重新生成PC列表缓存"/>--%>
                <%--</p:privilege>--%>
            <%--</form>--%>

        </td>

    </tr>
    <tr>
        <td>
            <form method="post" action="/point/exchangegoods/list">
                活动类型： <select name="activitytype">
                <option value="" selected>请选择</option>
                <option value="1"
                        <c:if test='${activityType eq "1"}'>selected</c:if>>礼包中心
                </option>
                <option value="0" <c:if test='${activityType eq "0"}'>selected</c:if>>积分商城</option>
            </select>

                商城类型：<select name="shoptypes">
                <option value="" selected>请选择</option>
                <c:forEach items="${shopTypes}" var="item">
                    <option value="${item.code}"
                            <c:if test="${shop_types eq item.code}">selected</c:if> >${item.name}</option>
                </c:forEach>
            </select>
                活动名称(模糊)：<input type="text" name="subject" size="32" value="${subject}"/>
                <input type="submit" name="button" class="default_button" value="查询"/>
            </form>
        </td>
    </tr>
</table>
<form action="/point/exchangegoods/list" method="post">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="14" class="default_line_td"></td>
        </tr>
        <c:if test="${fn:length(errorMsg)>0}">
            <tr>
                <td height="1" colspan="14" class="error_msg_td">
                    <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                </td>
            </tr>
        </c:if>
        <tr class="list_table_title_tr">
            <td nowrap align="left">ID</td>
            <td nowrap align="left">标题</td>
            <td nowrap align="left">图片</td>
            <td nowrap align="left" width="250">排序</td>
            <td nowrap align="left">开始时间</td>
            <td nowrap align="left">结束时间</td>
            <td nowrap align="left">商品分类</td>
            <td nowrap align="left">领取类型</td>
            <td nowrap align="left">渠道类型</td>
            <td nowrap align="left" width="60">秒杀商品</td>
            <td nowrap align="left" width="200">关注游戏推送</td>
            <td nowrap align="left">状态</td>
            <td nowrap align="left">创建人信息</td>
            <td nowrap align="left" width="">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="goods" varStatus="st">
                    <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td nowrap>
                                ${goods.activityGoodsId}
                        </td>
                        <td nowrap>
                                ${goods.activitySubject}
                        </td>
                        <td nowrap><img width="100" height="50" id="img_pic" src="${goods.activityPicUrl}"/>
                        </td>
                        <td nowrap="">
                            <input type="text" size="10" disabled="disabled"
                                   onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
                                   id="displayOrder${st.index}"
                                   name="displayorder"
                                   value="${goods.displayOrder}"/>
                            <input type="button" onclick="modifySort(${st.index});" id="update${st.index}"
                                   value="修改"/>
                            <input type="button" style="display:none;"
                                   onclick="submitSort(${goods.activityGoodsId},${st.index})"
                                   id="save${st.index}" value="保存"/>
                            <input type="button" style="display:none;"
                                   onclick="cancelSort(${st.index},${goods.displayOrder})"
                                   id="cancel${st.index}" value="取消"/>

                        </td>
                        <td nowrap> ${goods.startTime}</td>

                        <td nowrap><p
                                <c:if test="${nowdate>goods.endTime}">style='color:red;'</c:if>>${goods.endTime}</p>
                        </td>
                        <td nowrap>
                            <fmt:message key="goods.type.${goods.activitygoodsType.code}"
                                         bundle="${def}"/></td>

                        <td nowrap><fmt:message key="consumetimes.type.${goods.timeType.code}"
                                                bundle="${def}"/></td>
                        <td nowrap>
                            <c:choose>
                                <c:when test="${goods.activityType.code == 0}">${goods.goodsActionType.name}</c:when>
                                <c:otherwise>
                                    <fmt:message key="activity.goods.channel.type.${goods.channelType.code}"
                                                 bundle="${def}"/>
                                </c:otherwise>
                            </c:choose>


                        </td>
                        <td width="50"><fmt:message key="activity.seckilltype.type.${goods.seckilltype.code}"
                                                    bundle="${def}"/></td>
                        <td nowrap align="left">
                            <select id="select_appkey_${st.index}" onchange="addParam(${st.index})">
                                <option value="appid=25AQWaK997Po2x300CQeP0" selected="selected">着迷玩霸</option>
                            </select>
                            <select id="select_type_tags_${st.index}" onchange="addParam(${st.index})">
                                <option value="pushlisttype=4&tags=${goods.activityGoodsId}">预定礼包</option>
                                <c:if test="${goods.gameDbId > 0}">
                                    <option value="pushlisttype=3&tags=game${goods.gameDbId}">关注游戏</option>
                                </c:if>
                            </select>
                            <a href="#" target="_blank" id="a_push_createpage_${st.index}">去创建一条推送消息</a>
                        </td>
                        <td nowrap
                                <c:choose>
                                    <c:when test="${goods.actStatus.code eq 'y'}">
                                        style='color: #008000;'
                                    </c:when>
                                    <c:otherwise>
                                        class='error_msg_td'
                                    </c:otherwise>
                                </c:choose>>
                            <fmt:message key="activity.exchange.type.${goods.actStatus.code}"
                                         bundle="${def}"/>
                        </td>
                        <td>${goods.createUserId}<br/>${goods.createIp}
                            <br/><fmt:formatDate value="${goods.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>

                        <td nowrap>
                            <p:privilege name="/point/exchangegoods/modifypage">
                                <a href="/point/exchangegoods/modifypage?goodsid=${goods.activityGoodsId}&qatype=${activityType}&qsubject=${subject}&qstype=${shop_types}&pageStartIndex=${page.startRowIdx}">编辑</a>
                            </p:privilege>
                            <c:choose>
                                <c:when test="${goods.actStatus.code eq 'y'}">
                                    <p:privilege name="/point/exchangegoods/delete">
                                        &nbsp; <a
                                            href="/point/exchangegoods/delete?goodsid=${goods.activityGoodsId}&firstletter=${goods.firstLetter}&qatype=${activityType}&qsubject=${subject}&qstype=${shop_types}&pageStartIndex=${page.startRowIdx}" onclick="return confirm('确定要删除吗？')">删除</a>
                                    </p:privilege>
                                </c:when>
                                <c:otherwise>
                                    <p:privilege name="/point/exchangegoods/recover">
                                        &nbsp; <a
                                            href="/point/exchangegoods/recover?goodsid=${goods.activityGoodsId}&firstletter=${goods.firstLetter}&qatype=${activityType}&qsubject=${subject}&qstype=${shop_types}&pageStartIndex=${page.startRowIdx}">恢复</a>
                                    </p:privilege>
                                </c:otherwise>
                            </c:choose>
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
                    <pg:pager url="/point/exchangegoods/list"
                              items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber">
                        <pg:param name="maxPageItems" value="${page.pageSize}"/>
                        <pg:param name="activitytype" value="${activityType}"/>
                        <pg:param name="subject" value="${subject}"/>
                        <pg:param name="shoptypes" value="${shop_types}"/>
                        <pg:param name="items" value="${page.totalRows}"/>
                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                    </pg:pager>
                </td>
            </tr>
        </c:if>
    </table>
</form>
</td>
</tr>
</table>
</body>
</html>