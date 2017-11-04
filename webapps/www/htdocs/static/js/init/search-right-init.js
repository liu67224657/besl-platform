define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var followBiz = require('../biz/follow-biz');

    var tag = require('../page/tag');
    var tagcallback = require('../page/tagcallback');

    $().ready(function () {
        //加载标签管理
        $(".user_tool li").hover(function () {
            $(this).addClass("hover");
        }, function () {
            $(this).removeClass("hover");
        });

        $("#myblog").click(function () {
            $.post("/home", {pageNo:1, t:'myblog'}, function (data) {
                $("#blog_list").append(data);
            });
        });

        $(".ls3").click(function () {
            var pageNo = parseInt($("#hidden_pageNo").val());
            $("#hidden_pageNo").val(++pageNo);
            $.post("/home", {pageNo:1, t:'favorite'}, function (data) {
                $("#blog_list").append(data);
            });
        });

        //标签删除效果
        $("li[id^=li_tag_]").hover(function () {
            $(this).children()[1].className = 'dtag';
        }, function () {
            $(this).children()[1].className = 'del_tags';
        });

        //判断是否需要展示活跃用户
        if ($("#is_active_display").length > 0) {
            rightBiz.loadActive($("#is_active_display").attr("key"), searchRightCallback.loadActiveCallback);
        }

        //绑定 添加关注事件
        $("a[name = addactivefoucs]").live("click", function () {
            var focusuno = $(this).attr("focusuno");

            followBiz.ajaxFocus(joyconfig.joyuserno, focusuno, searchRightCallback.rightCallback);
        });

        //绑定 保存标签事件
        $("a[name = a_save_tag]").live("click", function () {
            var key = $(this).attr("key");

            tag.saveTag(key,searchRightCallback.saveTagCallback);
        });

        //绑定 删除标签事件
        $("a[name = a_del_tag]").live("click", function () {
            var tagid = $(this).attr("tagid");
            var tag_name = $(this).attr("tag_name");

            tag.delTag(tagid,tag_name,tagcallback.searchTagDelTagCallback);
        });


    });
});