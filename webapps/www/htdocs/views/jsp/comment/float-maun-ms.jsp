<%--
  Created by IntelliJ IDEA.
  User: zhitaoshi
  Date: 13-11-1
  Time: 下午2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="float_menu">
    <div class="column_menu">
        <ul>
            <c:choose>
                <c:when test="${dto.score.menu_list != null && dto.score.menu_list.floatMenuList.size() > 0}">
                    <c:forEach items="${dto.score.menu_list.floatMenuList}" varStatus="st" var="menu">
                        <li><a href="${menu.uri}" class="a_${st.index + 1}">
                            <img width="60" height="58" src="${menu.pic}" alt="" title="">
                        </a>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li><a href="http://www.joyme.com/wxwiki/ms/86656.shtml" class="a_1"><img width="60" height="58"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_1.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="http://www.joyme.com/wxwiki/ms/85470.shtml" class="a_2"><img width="60" height="58"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_2.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="http://www.joyme.com/wxwiki/ms/86306.shtml" class="a_3"><img width="60" height="58"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_3.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="http://www.joyme.com/wxwiki/ms/75761.shtml" class="a_4"><img width="60" height="58"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_4.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="http://www.joyme.com/wxwiki/ms/64889.shtml" class="a_5"><img width="58" height="55"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_5.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="http://www.joyme.com/wxwiki/ms/85586.shtml" class="a_6"><img width="58" height="58"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_7.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="http://www.joyme.com/wxwiki/ms/86590.shtml" class="a_7"><img width="58" height="58"
                                                                                              src="${URL_LIB}/static/theme/default/images/score/menu_8.png"
                                                                                              alt="" title=""></a>
                    </li>
                    <li><a href="#" class="a_8"><img src="" alt="" title=""></a></li>
                    <li><a href="#" class="a_8"></a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</div>
<!--float_menu==end-->
<div id="float_menu_btn"><b>菜</b><b>单</b></div>
<div id="opacity_bg" style="height: 330px;"></div>
<!--悬浮菜单==end-->