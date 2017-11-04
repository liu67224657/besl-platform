<script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<%--
  Created by IntelliJ IDEA.
  User: zhitaoshi
  Date: 13-11-1
  Time: 下午2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:if test="${recentLogList != null && !empty recentLogList}">
    <div class="get-gift-state">
        <h2>大家正在领</h2>

        <div id="marquee_y">
            <ul class="ul" id="indexLinks">
                <c:forEach items="${recentLogList}" var="log" varStatus="st">
                    <li><span>${log.screenName}&nbsp;&nbsp;于${dateutil:parseDate(log.date)}&nbsp;&nbsp;领到</span>${log.goodsName}</li>
                </c:forEach>
            </ul>
        </div>
    </div>
</c:if>
<script type="text/javascript">
    jQuery(function($) {
        function ScrollAction(listObj, listElem, speed, isSeries) {    //listObj为需要滚动的列表，  speed为滚动速度
            var pos, top, aniTop, height;
            var id = '';  //记录setInterval的标记id

            pos = listObj.position();
            top = pos.top;			//列表的top
            aniTop = top;				//记录当前运动时的top
            height = listObj.height();	//列表的高度

            var scrollUp = function() {
                aniTop--;
                if (!isSeries) {    //isSeries变量控制是否连续滚动，false不连续，true连续
                    if (aniTop == -height) {    //不连续，滚动玩重新滚动
                        listObj.css({'top': top});
                        aniTop = top;
                    }
                } else {
                    if (aniTop == -listObj.children().eq(0).height()) {    //连续滚动
                        var firstItem = '<' + listElem + '>' + listObj.children().eq(0).html() + '</' + listElem + '>';
                        listObj.children().eq(0).remove();
                        listObj.append(firstItem);
                        aniTop = 4;
                    }
                }
                listObj.css({'top': aniTop + 'px'});
            };
            var hover = function(id) {
                listObj.hover(function() {
                    clearInterval(id);
                }, function() {
                    id = setInterval(scrollUp, speed);
                });
            };
            this.start = function() {
                id = setInterval(scrollUp, speed);
                hover(id);
            };
        }

        var sa = new ScrollAction($('.ul'), 'li', 30, true);
        sa.start();
    });
</script>


