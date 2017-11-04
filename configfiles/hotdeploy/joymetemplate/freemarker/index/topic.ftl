<#if contentList?? && contentList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
    <#assign joymeDate= "com.enjoyf.platform.util.html.freemarktag.JoymeDate"?new()>
<h2 class="index-2013-title">专题推荐<a href="${URL_WWW}/category/recommend" target="_blank" class="normalLink">&gt;更多</a></h2>
<div class="free-game">
<ul class="game-measured">
    <#list contentList as content>
        <#if content.thumbimg??>
        <li class="<#if (content_index%2==1)>bg</#if><#if !content_has_next> noborder</#if>">
            <a href="${URL_WWW}/note/${content.elementId}" class="game-pic" target="_blank">
                <p>
                    <#assign thumbimg = parseOrgImg(content.thumbimg)/>
                    <img src="${thumbimg}" alt="${content.title?html}">
                </p>
                 <#if content.extField1??><span>${joymeSubString(content.extField1,5,'…')}</span></#if>
            </a>

            <div>
                <a href="${URL_WWW}/note/${content.elementId}" target="_blank" title="${content.title?html}">
                ${joymeSubString(content.title,24,"…")?html}
                </a>

                <p> ${joymeSubString(content.desc,30,"…")?html}</p>
                <span>${joymeDate(content.createDate?string("yyyy-MM-dd HH:mm:ss"))}</span>
            </div>
        </li>
        </#if>
    </#list>
</ul>
</div>
</#if>
