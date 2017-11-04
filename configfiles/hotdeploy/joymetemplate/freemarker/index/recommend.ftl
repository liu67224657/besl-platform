<#if recommendList?? && recommendList?size gt 0>
<#assign joymeSubString= "com.enjoyf.platform.webapps.common.html.freemarktag.SubStringChinese"?new()>
<ul class="recommend-article">
    <#list recommendList as recommend>
    <li><span>•</span><a href="${recommend.link}" target="_blank">${joymeSubString(recommend.title,9)?html}</a>
        <#if recommend.extField1?? && (recommend.extField1=='hot' || recommend.extField1=='new')>
            <em class="${recommend.extField1?html}"></em>
        </#if>
    </li>
    </#list>
</ul>
</#if>