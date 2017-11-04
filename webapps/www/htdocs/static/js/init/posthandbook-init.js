define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var postText = require('../page/post-text');
    var postOption = require('../page/post-option');
    var common = require('../common/common');
    window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
    window.imgLocaL = 0;
    $().ready(function () {
        header.noticeSearchReTopInit();

        if (POSTCONFIG != null) {
            if (POSTCONFIG.initTag != null && POSTCONFIG.initTag.length > 0) {
                postOption.handbookOption.initTag = POSTCONFIG.initTag;
            }
            if (POSTCONFIG.formula != null && POSTCONFIG.formula.length > 0) {
                postOption.handbookOption.formula = POSTCONFIG.formula;
            }
        }


        postText.posttextinit(postOption.handbookOption);
        common.jmPostConfirm(null);
    });
    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});