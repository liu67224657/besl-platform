<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="game-group-right">

    <h2 class="noborder"><em>${game.resourceName}</em></h2>

    <div class="game-info">
        <c:choose>
            <c:when test="${game.logoSize=='3:3'}">
                <div class="game-info-pic equal-ratio">
                    <c:forEach items="${game.icon.images}" var="icon">
                        <img src="${uf:parseOrgImg(icon.ll)}" width="122" height="122"/><span></span>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="game-info-pic">
                    <c:forEach items="${game.icon.images}" var="icon">
                        <img src="${uf:parseOrgImg(icon.ll)}" width="122"/><span></span>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- 有购买链接 -->
        <c:choose>
            <c:when test="${game.gameProperties!=null && fn:length(game.gameProperties.channels)>0}">
                <div class="game-buy-link">
                    <c:forEach var="channel" items="${game.gameProperties.channels}">
                         <a href="${channel.value}" class="game-link-${channel.propertyType}" rel="nofollow" target="_blank"></a>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="game-buy-link-none"></div>
            </c:otherwise>
        </c:choose>

        <c:if test="${game.gameMediaScoreSet!=null}">
            <c:forEach var="mediaScore" items="${game.gameMediaScoreSet.mediaScores}" varStatus="status">
                <p>${mediaScore.from}：${mediaScore.score}</p>
            </c:forEach>
        </c:if>
    </div>

    <c:if test="${fn:length(talentList)>0 || fn:length(profileList)>0}">
    <h2 class="noborder">内容贡献者<span class="icon-1"></span></h2>
    <dl class="game-vendor">
        <c:if test="${fn:length(talentList)>0}">
            <dt>达&nbsp;&nbsp;&nbsp;&nbsp;人：</dt>
            <dd>
                <c:forEach var="profile" items="${talentList}" varStatus="st">
                    <a href="${URL_WWW}/people/${profile.domain}">${profile.screenName}</a><c:if test="${fn:length(profile.verifyType)>0 && profile.verifyType!='n'}"><a href="${URL_WWW}/people/${profile.domain}" class="${profile.verifyType}vip"></a></c:if><c:if test="${!st.last}">、</c:if>
                </c:forEach>
            </dd>
        </c:if>

         <c:if test="${fn:length(profileList)>0}">
            <dt>参与作者：</dt>
            <dd>
                <c:forEach var="profile" items="${profileList}" varStatus="st">
                    <a href="${URL_WWW}/people/${profile.domain}">${profile.screenName}</a><c:if test="${fn:length(profile.verifyType)>0 && profile.verifyType!='n'}"><a href="${URL_WWW}/people/${profile.domain}" class="${profile.verifyType}vip"></a></c:if><c:if test="${!st.last}">、</c:if>
                </c:forEach>
            </dd>
        </c:if>
    </dl>
    </c:if>

    <c:if test="${fn:length(downloadContentList)>0}">
        <h2 class="noborder">游戏相关下载<span class="icon-2"></span></h2>
        <ul class="xgdown">
            <c:forEach var="content" items="${downloadContentList}" varStatus="status">
                <li class="end">
                    <a href="${URL_WWW}/note/${content.elementId}" target="_blank"
                       title="${content.title}">${jstr:subStr(content.title, 13,'…')}</a>
                    <a class="dicon" href="${URL_WWW}/note/${content.elementId}"
                       target="_blank">下载</a>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${baikePrivacy}">
    <h2 class="noborder">游戏页管理<span class="icon-3"></span></h2>
    <div class="game-manager"><a href="${URL_WWW}/game/${game.gameCode}/edit/addmodulepage">添加新栏目</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${URL_WWW}/game/${game.gameCode}/edit/menupage">导航管理</a>
        &nbsp;&nbsp;&nbsp;&nbsp;<a href="${URL_WWW}/game/${game.gameCode}/edit/headimagepage">头图管理</a></div>
    </c:if>

    <c:if test="${fn:length(gameList)>0}">
    <h2>你可能感兴趣的其他游戏</h2>
    <ul class="game-interest">
        <c:forEach var="aboutGame" items="${gameList}">
        <li>
            <div>
                <a href="${URL_WWW}/game/${aboutGame.gameCode}">
                <c:forEach items="${aboutGame.icon.images}" var="icon">
                    <img src="${uf:parseOrgImg(icon.ll)}" width="78"/>
                </c:forEach>
                </a>
            </div>
            <a href="${URL_WWW}/game/${aboutGame.gameCode}">${aboutGame.resourceName}</a>
        </li>
       </c:forEach>
    </ul>
    </c:if>

    <h2>游戏基本信息</h2>

    <div class="game-info-2">
        <c:if test="${game.deviceSet!=null}">
        <p>平台：<c:forEach items="${game.deviceSet.deviceSet}" var="device" varStatus="st">${device.code}<c:if test="${!st.last}">/</c:if></c:forEach></p>
       </c:if>
        <p>游戏名称：${game.resourceName}</p>
        <c:if test="${game.resourceStatus.showPrice() && fn:length(game.price)>0}">
            <p>售价：${game.price}</p>
        </c:if>
        <c:if test="${game.resourceStatus.showFileSize() && fn:length(game.fileSize)>0}">
            <p>游戏大小：${game.fileSize}</p>
        </c:if>
        <c:if test="${game.categorySet!=null}">
        <p>类型：<c:forEach items="${game.categorySet.categorySet}" var="category" varStatus="st">${category.code}<c:if test="${!st.last}">/</c:if></c:forEach></p>
        </c:if>
        <c:if test="${game.resourceStatus.showResourceStyle() &&  game.styleSet!=null}">
            <p>风格：<c:forEach items="${game.styleSet.styleSet}" var="style" varStatus="st">${style.code}<c:if test="${!st.last}">/</c:if></c:forEach></p>
        </c:if>
        <c:if test="${game.resourceStatus.showPlayerNumber() && fn:length(game.playerNumber)>0}">
            <p>游戏人数：${game.playerNumber}</p>
        </c:if>
        <c:if test="${game.resourceStatus.showDevelop() && fn:length(game.develop)>0}">
            <p>开发商：${game.develop}</p>
        </c:if>
         <c:if test="${game.resourceStatus.showPublishCompany() && fn:length(game.publishCompany)>0}">
            <p>发行商：${game.publishCompany}</p>
        </c:if>
        <c:if test="${game.resourceStatus.showPublishDate() && fn:length(game.publishDate)>0}">
            <p>发售日期：${game.publishDate}</p>
        </c:if>
        <c:if test="${game.resourceStatus.showLastUpdateDate() && fn:length(game.lastUpdateDate)>0}">
            <p>更新时间：${game.lastUpdateDate}</p>
        </c:if>
        <c:if test="${game.resourceStatus.showLanguage() &&fn:length(game.language)>0}">
            <p>语言：${game.language}</p>
        </c:if>
        <c:if test="${game.resourceStatus.showResourceUrl() && fn:length(game.resourceUrl)>0}">
            <p>官网：<a href="${game.resourceUrl}" target="_blamk">${game.resourceUrl}</a></p>
        </c:if>
    </div>

    <div class="addNewItemBox">
        <h2>我来创建游戏条目...</h2>

        <p>
            <a class="addNewItemBtn addNewItemBtn-newItem" target="_blank"
               href="http://www.joyme.com/note/2BIoVTYU176Wz7cHDjLmHI">创建游戏条目</a>
        </p>
    </div>

</div>