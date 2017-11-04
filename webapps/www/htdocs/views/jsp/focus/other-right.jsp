<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script type="text/javascript" src="${URL_LIB}/static/js/getmail.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/dialog.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/right.js"></script>
<div id="cont_right">
    <div class="user_cont userattention">
        <img src="<c:out value='${uf:parseFacesInclude(profile.blog.headIcon,profile.detail.sex,"m",true,0,1)[0]}'/>" class="user" width="58px" height="58px"/>
        <p class="mtp"><a href="${URL_WWW}/people/${profile.blog.domain}">${profile.blog.screenName}</a>
            <c:if test="${profile.detail.verifyType !=null && profile.detail.verifyType.code!= 'n'}">
                <a href="${URL_WWW}/people/${profile.blog.domain}" class="${profile.detail.verifyType.code}vip" title="<fmt:message key="verify.profile.${profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
            </c:if>
        </p>
        <b><a href="${URL_WWW}/people/${profile.blog.domain}">${URL_WWW}/people/${profile.blog.domain}</a></b>
        <b>
            <div class="u_fans_crr f_fans_crr">
                <ul>

                        <c:choose>
                            <c:when test="${userSession.blogwebsite.uno eq profile.blog.uno}">
                            </c:when>
                            <c:when test="${socialRelation.srcStatus.code eq 'y' && socialRelation.destStatus.code eq 'n'}">
                                <li class="attention_crr" id="relation_${profile.blog.uno}">
                               <span class="pr">已关注</span><a href="javascript:void(0)" onclick="showDesUnfocusMask('${profile.blog.uno}'); return false;">取消</a>
                               </li>
                            </c:when>
                            <c:when test="${socialRelation.srcStatus.code eq 'y' && socialRelation.destStatus.code eq 'y'}">
                               <li class="attention_crr" id="relation_${profile.blog.uno}">
                              <span class="pr">相互关注</span><a href="javascript:void(0)" onclick="showDesUnfocusMask('${profile.blog.uno}'); return false;">取消</a>
                               </li>
                            </c:when>
                             <c:when test="${socialRelation.srcStatus.code eq 'n' && socialRelation.destStatus.code eq 'y'}">
                               <li id="relation_${profile.blog.uno}">
                               <a href="javascript:void(0)" class="jfl" onclick="javascript:ajaxFocus('${userSession.blogwebsite.uno}','${profile.blog.uno}',focusOnOtherRightCallBack); return false;">加关注</a>
                               </li>
                            </c:when>
                            <c:otherwise>
                                <li id="relation_${profile.blog.uno}">
                               <a href="javascript:void(0)" class="fl" onclick="javascript:ajaxFocus('${userSession.blogwebsite.uno}','${profile.blog.uno}',focusOnOtherRightCallBack); return false;">加关注</a>
                               </li>
                            </c:otherwise>
                        </c:choose>
                        <li style="position: absolute;">
                        <div id="des_unf_mask_<c:out value="${profile.blog.uno}"/>" style="position: absolute; z-index: 9200; left: 0pt; top: 25px; width: 200px; display:none;">
                            <table border="0" cellpadding="0" cellspacing="0">
                            <tbody>
                            <tr>
                                <td class="top_l"></td>
                                <td class="top_c"></td>
                                <td class="top_r"></td>
                            </tr>
                            <tr>
                                <td class="mid_l"></td>
                                <td class="mid_c">
                                    <div  class="del_fans clearfix">
                                        <ul class="clearfix">
                                            <li>确定要移除<c:choose>
                                                <c:when test="${fn:length(profile.blog.screenName)<10}">
                                                     ${profile.blog.screenName}
                                                </c:when>
                                                 <c:otherwise>
                                                      ${fn:substring(profile.blog.screenName,0,7)}...?
                                                 </c:otherwise>
                                            </c:choose>?</li>
                                            <li style="clear:left;width:100%">
                                                <a href="javascript:void(0);" onclick="javascript:unFocus('<c:out value="${profile.blog.uno}"/>',unFocusOnOtherRightCallBack); return false;" >确定</a>
                                                <a href="javascript:void(0);" onclick="javascript:$('#des_unf_mask_<c:out value="${profile.blog.uno}"/>').slideToggle();; return false;" >取消</a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                                <td class="mid_r"></td>
                            </tr>
                            <tr>
                                <td class="bottom_l"></td>
                                <td class="bottom_c"></td>
                                <td class="bottom_r"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    </li>
                </ul>
            </div>
		</b>
    </div>
    <div class="user_follow">
        <ul>
            <li>
                <a href="${ctx}/social/focus/list/${profile.blog.uno}" >
                    <span>关注</span>
                    <p id="r_focus_num">${profile.sum.focusSum}</p>
                </a>
            </li>
            <li class="libg"></li>
            <li>
                <a href="${ctx}/social/fans/list/${profile.blog.uno}" >
                    <span>粉丝</span>
                    <p id="r_fans_num">${profile.sum.fansSum}</p>
                </a>
            </li>
            <li class="libg"></li>
            <li>
                <a href="<c:out value="${URL_WWW}/people/${profile.blog.domain}"/>" >
                    <span>文章</span>
                    <p id="r_blog_num">${profile.sum.blogSum}</p>
                </a>
            </li>
        </ul>
    </div>
    <div class="user_tags">
    	<ol><h3 class="left">他关注的标签</h3></ol>
        <ul id="user_tags_ul">
            <c:forEach var="tag" items="${tagList}" varStatus="st">
                <c:if test="${st.index<10}">
                <%--<li id="li_tag_${tag.value.tagId}"><a href="javascript:void(0);" onclick="window.location.href=getSearchTaghref('${tag.value.tag}','');">--%>
                <li id="li_tag_${tag.tagId}">
                <a href="${ctx}/search/content/${tag.tag}/?srid=a">${tag.tag}</a>
                </c:if>
            </c:forEach>
        </ul>
    </div>
    <%@ include file="/hotdeploy/views/jsp/topic/topic-right.jsp" %>
</div>