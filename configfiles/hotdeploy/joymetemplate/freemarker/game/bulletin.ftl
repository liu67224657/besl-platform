<#if firstButtelin?? && firstButtelin?length gt 0>
    <#assign dateList= "com.enjoyf.platform.util.html.freemarktag.JoymeDateList"?new()>
<h2><a href="<#if firstButtelin.link?? && firstButtelin.link?length gt 0>${firstButtelin.link}</#if>"  <#if firstButtelin.extField1?? && firstButtelin.extField1?length gt 0>style="color:${firstButtelin.extField1}"</#if> >${firstButtelin.title}</a></h2>

<#if list?? && list?size gt 0>
    <ul>
    <#list list as buttelin>
        <li>
            <span>
            <em style="<#if buttelin_index%7==1>color:#55779E
                <#elseif buttelin_index%7==2>color:#ff9e21
                <#elseif buttelin_index%7==3>color:#0a8ee3
                <#elseif buttelin_index%7==4>color:#97009b
                <#elseif buttelin_index%7==5>color:#e60201
                <#elseif buttelin_index%7==6>color:#ab4901
                <#else>color:#fc13ee
            </#if>">•</em>
                <a href="${buttelin.link}" <#if buttelin.extField1?? && buttelin.extField1?length gt 0>style="color:${buttelin.extField1}"</#if> title="${buttelin.title}">${buttelin.title}</a>
            </span>
            <em style="<#if dateList(buttelin.createDate?string("yyyy-MM-dd HH:mm:ss"))<7>color:#55779E</#if>">${buttelin.createDate?string("MM/dd")}</em>
        </li>
    </#list>
    </ul>
</#if>
</#if>