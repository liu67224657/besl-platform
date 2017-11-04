<div class="bbankuai fl">
    <div class="bcon clearfix">
        <div class="atit">
            <h3>
                <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
                   cid="${block.discoveryWallContent.contentId}">
                ${block.discoveryWallContent.wallSubject}
                </a>
            </h3>

            <div class="byauthor">
                作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
                <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
            </div>
        </div>
        <!--atit-->
        <div class="bh_picon">
            <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
               cid="${block.discoveryWallContent.contentId}">
                <div class="bh_img">
                    <p><img src="${block.discoveryWallContent.thumbImgLink}" width="${block.discoveryWallContent.width}"
                            height="${block.discoveryWallContent.height}"></p>
                    <div class="zhezhao"></div>
                </div>
            </a></div>
    </div>
</div>