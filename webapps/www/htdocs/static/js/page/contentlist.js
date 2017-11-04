define(function (require, exports, module) {

    var delBlog = require('./delblog');
    var contentEffective = require('./content-effective');
    var category = require('./category');
    var contentOper = require('./content-operate');

    var surl = require('./surl');
    var contentlist = {
        initBind:function() {
            delBlog.bindDel();
            contentEffective.togglePreviewVideo();
            contentEffective.togglePreviewPic();
            contentEffective.toggleAllContent();
            contentOper.homeLiveHot();
            surl.surlBind();
//            category.bindSortSelect();
//            category.bindSortOrder();
        },
        otherBind:function() {
            delBlog.bindDel();
            contentEffective.togglePreviewVideo();
            contentEffective.togglePreviewPic();
            contentEffective.toggleAllContent();
            contentOper.homeLiveHot();
            surl.surlBind();
//            category.bindSortSelect();
//            category.bindSortOrder();
        },
        bloglistBind:function() {
            delBlog.bindDel();
            contentOper.blogListHot();
            contentEffective.togglePreviewVideo();
            contentEffective.togglePreviewPic();
            contentEffective.toggleAllContentByBlog();
            surl.surlBind();
//            category.bindSortSelect();
//            category.bindSortOrder();
        },
        blogfavlistBind:function() {
            delBlog.bindDel();
            contentOper.blogfavListHot();
            contentEffective.togglePreviewVideo();
            contentEffective.togglePreviewPic();
            contentEffective.toggleAllContentByBlog();
            surl.surlBind();
//            category.bindSortSelect();
//            category.bindSortOrder();
        },
        searchListBind:function() {
            contentEffective.togglePreviewPicOnSearch();
            surl.surlBind();
        },
        talkBoardListBind:function() {
            contentEffective.postTalk();
            surl.surlBind();
        },
        essBoardListBind:function() {
            contentEffective.togglePreviewPicOnSearch();
            surl.surlBind();
        },
        atmeBind:function() {
            delBlog.bindDel();
            contentEffective.togglePreviewVideo();
            contentEffective.togglePreviewPic();
            contentEffective.toggleAllContent();
            contentOper.homeLiveHot();
            contentOper.atmeListBind();
            surl.surlBind();
        }

    }
    return contentlist;
});