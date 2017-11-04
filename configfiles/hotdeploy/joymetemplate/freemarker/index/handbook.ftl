<#if customList?? && customList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
    <#assign joymeDate= "com.enjoyf.platform.util.html.freemarktag.JoymeDate"?new()>
<h2 class="index-2013-title">游戏专区<a href="${URL_WWW}/category/handbook" target="_blank" class="normalLink">&gt;更多</a></h2>
<div class="free-game">
<ul class="game-measured">
    <#list customList as custom>
        <#if custom.thumbimg??>
        <li class="<#if (custom_index%2==1)>bg</#if><#if !custom_has_next> noborder</#if>">
            <a href="${custom.link}" class="game-pic" target="_blank">
                <p>
                    <#assign thumbimg = parseOrgImg(custom.thumbimg)/>
                    <img src="${thumbimg}" alt="${custom.title?html}">
                </p>
                <#if custom.extField1??><span>${custom.extField1}</span></#if>
            </a>

            <div>
                <a href="${custom.link}" target="_blank" title="${custom.title?html}">
                ${joymeSubString(custom.title,24,"…")?html}
                </a>

                <p> <#if custom.desc??>${joymeSubString(custom.desc,30,"…")?html}</#if></p>
                <#--<span>${joymeDate(custom.createDate?string("yyyy-MM-dd HH:mm:ss"))}</span>-->
            </div>
        </li>
         </#if>
    </#list>
</ul>
</div>
</#if>

