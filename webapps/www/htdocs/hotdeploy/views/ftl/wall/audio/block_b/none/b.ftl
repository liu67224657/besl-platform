<div class="bbankuai fl">
    <div class="bcon pt70 clearfix">
        <div class="music ml30  clearfix">
                <div class="music_con">
                    <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
                       cid="${block.discoveryWallContent.contentId}">
                        <img src="${block.discoveryWallContent.thumbImgLink}" width="${block.styleConfig.width}"
                             height="${block.styleConfig.height}">
                    </a>
                </div>
             <a class="music_btn" href="javascript:void(0)" url="${URL_WWW}/discovery/${block.discoveryWallContent.domainName}/${block.discoveryWallContent.contentId}"></a>
        </div>
        <div class="byauthor byposition">
            作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
            <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
        </div>
    </div>
</div>