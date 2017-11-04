define(function(require, exports, module) {
    require('../common/tips');
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    require('../common/tips');
    var tag = require('../page/tag');
    var tagCallback = require('../page/tagcallback');

    $(document).ready(function() {
        $("#tags_input_add").live("keyup", function(event) {
            tag.checkTagssplit("add", event, "form_tag");
        });

        $("#savetags").bind("click", function() {
            tag.saveTag('form_tag');
            var title = $('#liketag_title').text();
            if (title == tipsText.tag.add_like_tag_empty) {
                $('#liketag_title').text(tipsText.tag.add_like_tag_title)
            }
        });

        $("a[name=tagLink]").live("click", function() {
            common.clickTagFavorite($(this).html());
        });


        $("a[name=tagdel]").live("click", function() {
            tag.delTag($(this).attr("id"), $(this).attr("tag"), tagCallback.customizeDelTagCallback);
            if ($('#favoritetag>li').length == 0) {
                $('#liketag_title').text(tipsText.tag.add_like_tag_empty)
            }
        });
        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});