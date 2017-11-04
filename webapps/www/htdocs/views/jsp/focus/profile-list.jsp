<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="friendsbox clearfix">
<ul class="friends">
<c:forEach var="socialProfile" items="${list}" varStatus="st">
<input type="hidden" id="hid_nickname_${socialProfile.profile.blog.uno}"
       value="${socialProfile.profile.blog.screenName}"/>
<li class="friend-box" id="socialprofile_${socialProfile.profile.blog.uno}">
<input type="hidden" id="social_${socialProfile.profile.blog.uno}"
       value="${socialProfile.profile.blog.uno}"/>

<div class="friend-head">
    <a name="atLink" class="tag_cl_left" title="${socialProfile.profile.blog.screenName}"
       href="${URL_WWW}/people/<c:out value='${socialProfile.profile.blog.domain}'/>"><img
            src="<c:out value='${uf:parseFacesInclude(socialProfile.profile.blog.headIconSet,socialProfile.profile.detail.sex,"s" , true,0,1)[0]}'/>"
            width="58" height="58"/></a></div>
<div class="friend-content">
    <div class="friend-name">
        <a name="atLink" title="${socialProfile.profile.blog.screenName}"
           href="${URL_WWW}/people/<c:out value='${socialProfile.profile.blog.domain}'/>">
            <c:out value="${socialProfile.profile.blog.screenName}"/>
        </a>
        <c:if test="${socialProfile.profile.detail.verifyType !=null && socialProfile.profile.detail.verifyType.code!= 'n'}">
            <a href="${URL_WWW}/people/${socialProfile.profile.blog.domain}"
               class="${socialProfile.profile.detail.verifyType.code}vip"
               title="<fmt:message key="verify.profile.${socialProfile.profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
        </c:if>
        <c:choose>
            <c:when test="${location!=null && location eq 'focus'}">
                <input type="hidden" id="fdesc_${socialProfile.profile.blog.uno}"
                       value="${socialProfile.relation.description}"/>
                <c:choose>
                    <c:when test="${socialProfile.relation.description!=null && fn:length(socialProfile.relation.description)>0}">
                                        <span id="set_desc_${socialProfile.profile.blog.uno}" style="display:none;">(<a
                                                class="shezhi" id="link_setdesc_${socialProfile.profile.blog.uno}"
                                                href="javascript:void(0);"><c:out
                                                value="${socialProfile.relation.description}"/></a>)</span>
                    </c:when>
                    <c:otherwise>
                                        <span id="set_desc_${socialProfile.profile.blog.uno}" style="display:none;">(<a
                                                class="shezhi" id="link_setdesc_${socialProfile.profile.blog.uno}"
                                                href="javascript:void(0);">+设置备注</a>)</span>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:if test="${socialProfile.relation.description!=null && fn:length(socialProfile.relation.description)>0}">
                                    <span id="set_desc_${socialProfile.profile.blog.uno}" style="display:none;">(<a
                                            class="shezhi" href="javascript:void(0);"><c:out
                                            value="${socialProfile.relation.description}"/></a>)</span>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="friend-info">
        <c:if test="${socialProfile.profile.detail.sex=='0'}"><b class="nv"></b></c:if><c:if
            test="${socialProfile.profile.detail.sex=='1'}"><b class="nan"></b></c:if>
        <a href="${URL_WWW}/social/follow/list/${socialProfile.profile.blog.uno}">关注<span>${socialProfile.profile.sum.focusSum}</span></a>
        <a href="${URL_WWW}/social/fans/list/${socialProfile.profile.blog.uno}">粉丝<span>${socialProfile.profile.sum.fansSum}</span></a>
        <a href="${URL_WWW}/people/<c:out value='${socialProfile.profile.blog.domain}'/>">帖子<span>${socialProfile.profile.sum.blogSum}</span></a>
    </div>
    <div class="friend-tag"><c:if test="${fn:length(socialProfile.bardList)>0}">加入的小组：</c:if>
        <c:forEach var="board" items="${socialProfile.bardList}" varStatus="st">
            <a href="${URL_WWW}/group/${board.gameCode}" target="_blank">${board.gameName}</a>
        </c:forEach>
    </div>
    <div class="friend-recent">
        <c:if test="${socialProfile.content != null}">
            <a href="${URL_WWW}/note/${socialProfile.content.contentId}" target="_blank">
                <c:choose>
                    <c:when test="${socialProfile.content.subject!=null && fn:length(socialProfile.content.subject)!=0 }">
                        <c:choose>
                            <c:when test="${fn:length(socialProfile.content.subject)>30}">
                                <c:out value="${fn:substring(socialProfile.content.subject,0,30)}"/>...
                            </c:when>
                            <c:otherwise>
                                <c:out value="${socialProfile.content.subject}"/>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${socialProfile.content.content!=null && fn:length(socialProfile.content.content)!=0 }">
                        ${socialProfile.content.content}
                    </c:when>
                    <c:when test="${socialProfile.content.contentType.hasApp()}">
                        分享苹果应用
                    </c:when>
                    <c:when test="${socialProfile.content.contentType.value ==3 || socialProfile.content.contentType.value==18}">
                        分享图片
                    </c:when>
                    <c:when test="${socialProfile.content.contentType.value ==5 || socialProfile.content.contentType.value==20}">
                        分享音乐
                    </c:when>
                    <c:when test="${socialProfile.content.contentType.value ==9 || socialProfile.content.contentType.value ==25}">
                        分享视频
                    </c:when>
                    <c:otherwise>
                        分享
                    </c:otherwise>
                </c:choose>
            </a>
        </c:if>
    </div>
</div>
<div class="friend-status" id="relation_${socialProfile.profile.blog.uno}">
    <c:choose>
        <c:when test="${userSession.blogwebsite.uno eq socialProfile.profile.blog.uno}">
        </c:when>
        <c:when test="${socialProfile.relation.srcStatus.code eq 'y' && socialProfile.relation.destStatus.code eq 'n'}">
            <p><a href="javascript:void(0)" class="attentioned_nocancel" title="已关注"></a></p>
        </c:when>
        <c:when test="${socialProfile.relation.srcStatus.code eq 'y' && socialProfile.relation.destStatus.code eq 'y'}">
            <p><span class="attentionclosely" title="相互关注"></span></p>
        </c:when>
        <c:when test="${socialProfile.relation.srcStatus.code eq 'n' && socialProfile.relation.destStatus.code eq 'y'}">
            <p><a id="follow_${socialProfile.profile.blog.uno}" href="javascript:void(0)"
                  class="add_attention_ok"></a></p>
        </c:when>
        <c:otherwise>
            <p><a id="follow_${socialProfile.profile.blog.uno}" href="javascript:void(0)"
                  class="add_attention" title="加关注"></a></p>
        </c:otherwise>
    </c:choose>
    <div class="fcancel-box" id="cancel_${socialProfile.profile.blog.uno}">
        <c:choose>
            <c:when test="${userSession.blogwebsite.uno eq socialProfile.profile.blog.uno}">
            </c:when>
            <c:when test="${socialProfile.relation.srcStatus.code eq 'y' && (location==null || location != 'fans')}">
                <a href="javascript:void(0)" style="display:none"
                   id="remove_follow_${socialProfile.profile.blog.uno}"><span>取消关注</span></a>
            </c:when>
            <c:when test="${socialProfile.relation.destStatus.code eq 'y' && location!=null && location eq 'fans'}">
                <a href="javascript:void(0)" style="display:none"
                   id="remove_fans_${socialProfile.profile.blog.uno}"><span>移除粉丝</span></a>
            </c:when>
            <c:otherwise>
            </c:otherwise>
        </c:choose>
    </div>
    <c:if test="${location!=null && location=='focus'}">
        <div class="fenzu">
            <a href="javascript:void(0);" class="fenzubtn"
               id="follow_usercate_${socialProfile.profile.blog.uno}"><span>
                                <c:choose>
                                    <c:when test="${fn:length(socialProfile.categoryMap)!=0}">
                                        <c:forEach var="categ" items="${socialProfile.categoryMap}" varStatus="st"
                                                   begin="0" end="3">
                                            <c:choose>
                                                <c:when test="${st.first}">
                                                    <c:set var="cateName" value="${categ.value.cateName}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="cateName" value="${cateName},${categ.value.cateName}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <c:choose>
                                            <c:when test="${fn:length(cateName)>5}">
                                                ${fn:substring(cateName,0,5)}…
                                            </c:when>
                                            <c:otherwise>
                                                ${cateName}
                                            </c:otherwise>
                                        </c:choose>

                                    </c:when>
                                    <c:otherwise>未分组</c:otherwise>
                                </c:choose>
                            </span></a>

            <div id="usercate_select_${socialProfile.profile.blog.uno}" class="fenzu-select"
                 style="display:none">
                <div class="fenzu-groups">
                    <ul>
                        <c:forEach var="cate" items="${cateList}" varStatus="st">
                            <li><input
                                    id="chktype_<c:out value="${socialProfile.profile.blog.uno}"/>_<c:out value="${cate.value.cateId}"/>"
                                    name="typeid" type="checkbox" class="fenzuinput"
                                    value="<c:out value="${cate.value.cateId}"/>"
                                    <c:if test="${socialProfile.categoryMap[cate.key]!=null}">checked="true" </c:if>/>
                                <label for="chktype_<c:out value="${socialProfile.profile.blog.uno}"/>_<c:out value="${cate.value.cateId}"/>"><c:out
                                        value="${cate.value.cateName}"/></label>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="fenzu-new">
                    <a id="list_createusercate_${socialProfile.profile.blog.uno}" class="fenzu-add"
                       href="javascript:void(0);">创建新分组</a>

                    <div class="fenzu-new" style="display:none"
                         id="div_form_${socialProfile.profile.blog.uno}">
                        <form id="form_createtype_<c:out value="${socialProfile.profile.blog.uno}"/>"
                              action="${ctx}/profile/usertype/adduserandtype" method="post">
                            <input name="destUno" type="hidden"
                                   value="<c:out value="${socialProfile.profile.blog.uno}"/>"/>
                            <input id="cateame_${socialProfile.profile.blog.uno}" class="fenzuaddinput"
                                   type="text" name="cateName"/>

                            <div class="fenzu-wrong"
                                 id="error_type_${socialProfile.profile.blog.uno}"></div>
                            <div class="fenzubtnbox">
                                <div class="fenzu-addbtn"><a
                                        id="submit_lcreatecate_${socialProfile.profile.blog.uno}"
                                        class="submitbtn sbumitlistcreatecate"
                                        href="javascript:void(0);"><span>保存</span></a></div>
                                <div class="fenzu-cancelbtn"><a
                                        id="cancel_lcreatecate_${socialProfile.profile.blog.uno}"
                                        class="graybtn canclelistcreatecate" href="javascript:void(0);"><span>取消</span></a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
</li>
</c:forEach>
</ul>
</div>
