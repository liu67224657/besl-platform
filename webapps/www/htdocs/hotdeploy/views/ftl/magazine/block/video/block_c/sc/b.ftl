<div class="cbankuai fl">
    <div class="ccon clearfix">
        <div class="cv_icon">
            <div class="cv_img">
                <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
                   cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
                    <img src="${displayContent.thumbImgLink}" width="${styleConfig.width}" height="${styleConfig.height}">
                </a>
                <a title="播放" class="video_btn" href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
                   cid="${displayContent.contentId}"></a>
            </div>
        </div>
        <h3 class="mt10">
            <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
               cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
            ${displayContent.wallSubject}
            </a>
        </h3>
    </div>
    <div class="byauthor byposition01">
        作者：<a href="${URL_WWW}/people/${displayContent.domainName}" target="_blank">${displayContent.screenName}</a>
        <span class="bytime"><a href="${URL_WWW}/note/${displayContent.contentId}" target="_blank">${displayContent.dateStr}</a></span>
    </div>
</div>