<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h3 class="con_hn">${group.resourceName}</h3>
<div class="stab_hd clearfix">
    <ul class="stablist">
        <c:set var="hasBaike" value="false"/>
         <c:forEach items="${gameNavigationList}" var="gamenav" varStatus="st">

                 <c:choose>
                     <c:when test="${gamenav.type=='cover'}">
                           <li>
                               <a <c:if test="${gamenav.type==currentTab}">class="on"</c:if>
                                  href="${URL_WWW}/game/${group.gameCode}">
                                   <span>封面</span>
                               </a>
                            </li>
                     </c:when>
                     <c:when test="${gamenav.type=='talk'}">
                            <li>
                               <a <c:if test="${gamenav.type==currentTab}">class="on"</c:if> href="${URL_WWW}/game/${group.gameCode}/talk">
                                   <span>讨论区</span>
                               </a>
                            </li>
                     </c:when>
                      <c:when test="${gamenav.type=='ess'}">
                            <li>
                               <a <c:if test="${gamenav.type==currentTab}">class="on"</c:if> href="${URL_WWW}/game/${group.gameCode}/ess">
                                   <span>精华</span>
                               </a>
                            </li>
                     </c:when>
                     <c:when test="${gamenav.type=='baike'}">
                          <c:set var="hasBaike" value="true"/>
                            <li>
                               <a <c:if test="${gamenav.type==currentTab}">class="on"</c:if> href="${URL_WWW}/baike/${group.gameCode}">
                                   <span>攻略百科</span>
                               </a>
                            </li>
                     </c:when>
                     <c:when test="${gamenav.type=='link'}">
                           <li>
                               <a href="${gamenav.value}">
                                   <span>${gamenav.name}</span>
                               </a>
                            </li>
                     </c:when>
                 </c:choose>
         </c:forEach>
        <c:if test="${baikePrivacy && !hasBaike}">
            <li>
                <a
                        <c:if test="${gamenav.type==currentTab}">class="on"</c:if>
                        href="${URL_WWW}/baike/${group.gameCode}/create">
                    <span>+攻略百科</span>
                </a>
            </li>
        </c:if>
    </ul>
    <c:if test="${viewCategory.locationCode == 'talk'}">
        <a href="javascript:void(0)" id="postTalk" class="send">发帖</a>
    </c:if>
</div>