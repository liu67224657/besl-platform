<#if favorTop?? && (favorTop?size>0)>
<div class="lovetit lover_noboder">
    <h3>着迷<span class="like">喜欢</span>榜</h3>
    <em>榜上有名，因为更多人喜欢！</em></div>
<ul class="awardlist clearfix">
    <#list favorTop as favor>
        <li><em
                <#if favor_index=0>class="first"</#if>
                <#if favor_index=1>class="two"</#if>
                <#if favor_index=2>class="three"</#if>>
                ${favor_index+1}</em>
            <a href="${URL_WWW}/note/${favor.elementId}" target="_blank">
                <#if favor.title?length gt 20 >
                                ${favor.title?substring(0,20)}
                            <#else>
                                ${favor.title}
                            </#if>
             </a>
        </li>
    </#list>
</ul>
</#if>