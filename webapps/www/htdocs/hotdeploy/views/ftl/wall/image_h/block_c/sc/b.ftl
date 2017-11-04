<div class="cbankuai fl">
    <div class="ccon clearfix">
        <div class="ch_picon">
            <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
               cid="${block.discoveryWallContent.contentId}">
                <div class="ch_img">
                    <p><img src="${block.discoveryWallContent.thumbImgLink}" width="${block.discoveryWallContent.width}"
                            height="${block.discoveryWallContent.height}"></p>
                    <div class="zhezhao"></div>
                </div>
            </a>
        </div>
        <h3 class="pt20">
            <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
               cid="${block.discoveryWallContent.contentId}">
            ${block.discoveryWallContent.wallSubject}
            </a>
        </h3>
    </div>
    <div class="byauthor byposition">
        作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
        <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
    </div>
</div>