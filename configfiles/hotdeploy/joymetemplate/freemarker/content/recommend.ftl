<#if recommendList?? && recommendList?size gt 0>
    <#assign joymeSubString= "com.enjoyf.platform.webapps.common.html.freemarktag.SubStringChinese"?new()>
<h2 class="index-2013-title">热门活动</h2>
<ul class="index-2013-hot-news">
    <#list recommendList as recommend>
        <li><a href="${recommend.link}" target="_blank">${joymeSubString(recommend.title,9)}</a>
            <#if recommend.extField1?? && (recommend.extField1=='hot' || recommend.extField1=='new')>
                <em class="${recommend.extField1}"></em>
            </#if>
        </li>
    </#list>
</ul>
</#if>