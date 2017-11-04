<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!--右侧开始-->

<div class="article-column-right">
    <%@ include file="/hotdeploy/views/jsp/blog/b01/rightad-top.jsp" %>
    <c:choose>
        <c:when test="${userSession!=null && fn:length(gamePrivacyMap)>0}">
            <h2 class="index-2013-title">小组组长操作</h2>

            <div class="group-admin-ctrl">
                <select id="select_game_privacy">
                    <option value="">请选择</option>
                    <c:forEach var="gameRes" items="${gamePrivacyMap}">
                        <option value="${gameRes.value.gameRelationSet.boardRelation.relationValue}">${gameRes.value.resourceName}</option>
                    </c:forEach>
                </select>
                <ul id="privacy_operate">
                </ul>
            </div>
            <div class="dotted-line"></div>
        </c:when>
        <c:when test="${supergameprivacy}">
            <h2 class="index-2013-title">超级小组组长操作</h2>

            <div class="group-admin-ctrl">
                <input type="text" style="color:#989898" id="input_search_word" value="输入小组名称"
                       onblur="if(this.value==''){this.value='输入小组名称'; this.style.color='#989898';}"
                       onfocus="if(this.value=='输入小组名称'){this.value=''; this.style.color='#666';}"/>

                <div id="search_result_list"></div>

                <ul id="privacy_operate"></ul>
            </div>
            <div class="dotted-line"></div>
        </c:when>
    </c:choose>

    <c:if test="${increacePotinPrivacy}">
        <h2 class="index-2013-title">组长加积分</h2>
        <input type="hidden" value="${blogContent.content.relationSet.groupRelation.relationId}" id="adjust_group_id"/>

        <div class="headman-operation" style="width:185px;margin-left:25px;">
            <div>
                <select id="adjust_point">
                    <option value="200">200分</option>
                    <option value="400">400分</option>
                    <option value="600">600分</option>
                    <option value="800">800分</option>
                    <option value="1000">1000分</option>
                </select>
            </div>
            <div>
                <textarea rows="4" maxlength="100" onfocus="pointReasonFocus()" onblur="pointReasonBlur()"
                          id="point_reason">请输入加分理由...</textarea>
            </div>
            <p>
                <a href="#" id="but_adjust_point" class="submitbtn"><span>给作者加分</span></a>
            </p>


        </div>
        <br/>

        <div class="dotted-line"></div>
    </c:if>

    <c:if test="${userSession!=null && fn:length(magazinePrivacyList)>0}">
        <h2 class="index-2013-title">杂志版主操作</h2>

        <div class="group-admin-ctrl">
            <select id="select_magazine_privacy">
                <option value="0">选择相关操作</option>
                <c:forEach var="category" items="${magazinePrivacyList}">
                    <option value="${category.categoryId}">${category.categoryName}</option>
                </c:forEach>
            </select>
            <ul id="privacy_magazine_operate">
            </ul>
        </div>
        <div class="dotted-line"></div>
    </c:if>

    <!-- 相关游戏条目 -->
    <c:if test="${gamemenu!=null}">
        <h2 class="index-2013-title">相关游戏条目</h2>
        <ul class="tj-game-2th">
            <li>
                <div>
                    <c:if test="${fn:length(gamemenu.icon)>0}">
                        <a href="${URL_WWW}/game/${gamemenu.gameCode}"><img src="${uf:parseOrgImg(gamemenu.icon)}"
                                                                            width="78"/></a>
                    </c:if>
                </div>
                <h2><a href="${URL_WWW}/game/${gamemenu.gameCode}">${gamemenu.gameName}</a></h2>
            </li>
        </ul>
        <div class="dotted-line"></div>
    </c:if>


    <!-- 攻略 -->
    <%@ include file="/views/jsp/blog/b01/ajax-blog-right-baike.jsp" %>

    <!-- 着迷热点 -->
    ${hotNewsHtml}

    ${recommendHtml}

    <!-- 你可能还喜欢 -->
    <%--<%@ include file="/views/jsp/blog/b01/blog-bfd-recommend.jsp" %>--%>
    <%@ include file="/hotdeploy/views/jsp/blog/b01/rightad-bottom.jsp" %>
</div>


