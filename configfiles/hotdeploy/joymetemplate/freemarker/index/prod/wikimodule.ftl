<#if wikiList?? && wikiList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
    <#assign joymeDateField= "com.enjoyf.platform.util.html.freemarktag.JoymeDateField"?new()>
<h2 class="index-2013-title">WIKI资料大百科<a class="normalLink" target="_blank" href="${URL_WWW}/game#wiki">&gt;更多</a></h2>
<div class="game-wiki">
    <ul class="game-measured">
        <#list wikiList as wiki>
            <li>
                <a href="${wiki.link}" class="game-pic" target="_blank" title="${wiki.title}">
                    <p>
                        <#if wiki.thumbimg??>
                            <#assign thumbimg = parseOrgImg(wiki.thumbimg)/>
                            <img width="183" height="90" src="${thumbimg}"
                                 <#if wiki.title??>alt="${wiki.title}"</#if>>
                            <#else>
                                <img width="183" height="90"
                                     src="${URL_LIB}/static/theme/default/img/default.jpg">
                        </#if>
                    </p>
                </a>

                <div>
                    <a href="${wiki.link}" target="_blank" title="${wiki.title}">${wiki.title}
                        <#if wiki.extField1?? &&  wiki.extField1=='new'>
                            <img src="${URL_LIB}/static/theme/default/img/new.png">
                        </#if>
                        <#if wiki.extField1?? &&  wiki.extField1=='hot'>
                            <img src="${URL_LIB}/static/theme/default/img/hot.png">
                        </#if>
                    </a>

                    <p>
                         <#if wiki.dateValue??>
                        页面总数：<b>${wiki.intValue1}个</b>&nbsp;&nbsp;|
                        </#if>
                        <#if wiki.dateValue??>
                           &nbsp;&nbsp; 最后更新：<b>${joymeDateField(wiki.dateValue?string("yyyy-MM-dd HH:mm:ss"))}</b>
                        </#if>
                        <#if wiki.intValue2??>
                        <br>本周更新页面：<b>${wiki.intValue2}个</b>
                        </#if>
                        </p>
                    <#if wiki.extField2??>
                        <a href="${wiki.extField2}" class="down-wiki-app">攻略应用下载&nbsp;&gt;&gt;</a>
                        <#else>
                         <em class="down-wiki-app-none">攻略应用即将推出...</em>
                    </#if>
                </div>
            </li>
        </#list>
    </ul>
</div>
</#if>
