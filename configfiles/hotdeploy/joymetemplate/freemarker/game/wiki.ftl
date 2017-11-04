<#if majorList?? && majorList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeDateField= "com.enjoyf.platform.util.html.freemarktag.JoymeDateField"?new()>
<h4 class="game-wiki-icon">游戏wiki</h4>
<ul class="tj-game-wiki clearfix">
    <#list majorList as wiki>
        <li>
        <a href="${wiki.link}" target="_blank" title="${wiki.title}">
             <#assign thumbimg = parseOrgImg(wiki.thumbimg)/>
            <p><img height="96" width="96"  src="${thumbimg}"></p>
            <p>${wiki.title}</p>
        </a>
        <#if wiki.intValue1??>
           <div>页面总数:<span>${wiki.intValue1}个</span></div>
        </#if>
        <#if wiki.dateValue??>
         <div>最后更新:<span>${joymeDateField(wiki.dateValue?string("yyyy-MM-dd HH:mm:ss"))}</span></div>
        </#if>
    </li>
    </#list>
</ul>
<ul class="tj-game-tag clearfix">
      <#if list?? && list?size gt 0>
        <ul class="tj-game-tag clearfix">
        <#list list as wiki>
            <li>
                <#if wiki.extField1??>
                    <span class="week-game-tag">${wiki.extField1}</span>
                </#if>
                <a href="${wiki.link}" target="_blank"  title="${wiki.title}"> ${wiki.title}</a>
            </li>
        </#list>
        </ul>
    </#if>
</ul>
</#if>