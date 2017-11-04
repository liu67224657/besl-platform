<div class="bbankuai fl">
    <div class="bcon clearfix">
        <div class="v_icon">
            <div class="v_img">
                <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
                   cid="${block.discoveryWallContent.contentId}">
                    <img src="${block.discoveryWallContent.thumbImgLink}" width="${block.styleConfig.width}" height="${block.styleConfig.height}">
                </a>
                <a title="播放" class="video_btn" href="javascript:void(0)" name="preview"
                   cuno="${block.discoveryWallContent.contentUno}"
                   cid="${block.discoveryWallContent.contentId}"></a>
            </div>
        </div>
        <div class="atit fl pt20 clearfix">
            <h3>
                <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
                   cid="${block.discoveryWallContent.contentId}">
                ${block.discoveryWallContent.wallSubject}
                </a>
            </h3>

            <div class="byauthor byposition">
                作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
                <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
            </div>
        </div>
    </div>
</div>