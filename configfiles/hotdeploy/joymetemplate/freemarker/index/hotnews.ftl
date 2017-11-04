<#if customList?? && customList?size gt 0>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
<h2 class="index-2013-title">着迷热点<a href="${URL_WWW}/article/hotnews/index.html" target="_blank" class="normalLink">&gt;更多</a></h2>
<ul class="index-2013-hot-news">
    <#list customList as custom>
        <#if custom_index==0>
            <li class="<#if !custom_has_next> noborder</#if>"><a href="<#if custom.link??>${custom.link}</#if>"  title="${custom.title?html}" class="top" target="_blank">${joymeSubString(custom.title,17,"…")?html}</a></li>
            <#else>
                <li class="<#if !custom_has_next> noborder</#if>">
                    <a href="<#if custom.link??>${custom.link}</#if>" target="_blank" title="${custom.title?html}"><#if custom.extField1??>${custom.extField1}</#if>${joymeSubString(custom.title,16,"…")?html}</a>
                </li>
        </#if>
    </#list>
</ul>
</#if>
