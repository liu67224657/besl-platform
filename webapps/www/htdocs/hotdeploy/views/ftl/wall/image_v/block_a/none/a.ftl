<div class="bankuai fl">
    <div class="apcon clearfix">
        <div class="s_picon mr_l">
            <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
               cid="${block.discoveryWallContent.contentId}">
                <div class="s_pimg">
                    <p><img src="${block.discoveryWallContent.thumbImgLink}" width="${block.discoveryWallContent.width}"
                            height="${block.discoveryWallContent.height}"></p>
                    <div class="zhezhao"></div>
                </div>
            </a>
        </div>
        <div class="byauthor">
            作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
            <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
        </div>
    </div>
</div>