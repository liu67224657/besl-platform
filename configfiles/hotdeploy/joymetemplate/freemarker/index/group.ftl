<#if groupList?? && groupList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
<h2 class="index-2013-title">推荐小组<a href="${URL_WWW}/group" target="_blank" class="normalLink">&gt;更多小组</a></h2>
<ul class="recommend-group-2013">
    <#list groupList as group>
        <li class="<#if !group_has_next> noborder</#if>">
            <div class="group-title"><a href="${URL_WWW}/group/${group.groupDTO.code}" target="_blank" title="${group.groupDTO.title}">${group.groupDTO.title}</a>
                <span>&nbsp;&nbsp;|&nbsp;&nbsp;帖子：${group.groupDTO.postTimes+group.groupDTO.replyTimes}</span></div>
            <a class="group-pic" href="${URL_WWW}/group/${group.groupDTO.code}" target="_blank" >
                <#assign thumbimg = parseOrgImg(group.topContent.thumbimg)/>
                <p><img src="${thumbimg}" alt="${group.groupDTO.title}"></p>
            </a>

            <div class="group-article">
                <h2>
                    <a href="${URL_WWW}/note/${group.topContent.elementId}" title="${group.topContent.title?html}" target="_blank">${joymeSubString(group.topContent.title,28,"…")?html}</a>
                </h2>
                <a class="user" href="${URL_WWW}/people/${group.topContent.domain}"  target="_blank">${joymeSubString(group.topContent.screenName,12,"…")}</a>
                <#list group.contentList as content>
                    <p><a target="_blank"  href="${URL_WWW}/note/${content.elementId}" title="${content.title?html}" >${joymeSubString(content.title,14,"…")?html}</a>
                    </p>
                </#list>
            </div>
        </li>
    </#list>
</ul>
</#if>
