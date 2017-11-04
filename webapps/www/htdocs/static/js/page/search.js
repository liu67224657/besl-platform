define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var tag = require('./tag');
    var tagcallback = require('./tagcallback');
    var common = require('../common/common');
    var searchPage = {

        clearInitTag:function(postOption) {
            postOption.initTag = [];
            tag.clearTagItem('chat', 'postchat');
        },

        initHoldSearch:function() {
            $("#hold_txt_search").live("paste keydown keyup", function() {
                if (common.getInputLength($(this).val()) >= 20) {
                    $(this).val($(this).val().substr(0, 20));
                }
            });
            //搜索事件
            $("#hold_txt_search").keydown(function() {
                holdSearchTips($(this));
            });
            $("#hold_txt_search").focus(function() {
                holdSearchTips($(this));
            });
            window.setTipsFlag = false;
            $("#hold_txt_search").blur(function() {
                if (!window.setTipsFlag) {
                    $("#holdsearchtips").hide();
                    window.setTipsFlag = false;
                }
            });
            $("#holdsearchtips").mouseover(
                    function() {
                        window.setTipsFlag = true;
                    }).mouseout(function() {
                        window.setTipsFlag = false;
                    })
            $("#hold_txt_search").keyup(function(event) {
                holdSearchTips($(this));
                switch (event.keyCode) {
                    case 38:
                        if (!$("#holdsearchtips").is(":hidden")) {
                            var libg = $("#holdsearchtips>div.wsearc>ul>li[class=hover]");
                            if (libg.size() != 0) {
                                libg.removeClass("hover");
                            }
                            if (libg.prev().size() != 0) {
                                libg.prev().addClass("hover");
                                $("#holdstype").val(libg.prev()[0].id);
                            } else {
                                var hoverObj = $("#holdsearchtips>div.wsearc>ul>li").last();
                                hoverObj.addClass("hover");
                                $("#holdstype").val(hoverObj[0].id);
                            }
                        }
                        break;
                    case 40:
                        if (!$("#holdsearchtips").is(":hidden")) {
                            var libg = $("#holdsearchtips>div.wsearc>ul>li[class=hover]");
                            //清除.libg
                            if (libg.size() != 0) {
                                libg.removeClass("hover");
                            }
                            if (libg.next().size() != 0) {
                                libg.next().addClass("hover");
                                $("#holdstype").val(libg.next()[0].id);
                            } else {
                                var hoverObj = $("#holdsearchtips>div.wsearc>ul>li").first();
                                hoverObj.addClass("hover");
                                $("#holdstype").val(hoverObj[0].id);
                            }

                        }
                        break;
                    case 13:
                        holdSearch();
                        break;
                    default:
                        break;
                }
            });

            $("#holdsearchtips li").hover(function() {
                $(this).addClass("hover");
            }, function() {
                $(this).removeClass("hover");
            });
            $("#holdsearchtips li").click(function() {
                $("#holdstype")[0].value = $(this)[0].id;
                holdSearch();
            });

            //搜索提交
            $("#holdsearchbtn").bind("click", function() {
                holdSearch();
            });
        }

    }

    //搜索下拉框
    var holdSearchTips = function (jqueryObj) {
        var val = trimSearchStr(jqueryObj.val());
        if (val == null || val.length == 0 || val == '找找你感兴趣的') {
            $("#holdsearchtips").css("display", "none");
        } else {
            $("#holdsearchtips").css("display", "block");
            if ($.trim(val).length > 18) {
                val = val.substr(0, 18) + "...";
            }
            $("#holdsearchtips ul li b").text(val);
        }
    }

    //过滤搜索
    var trimSearchStr = function (str) {
        str = str.replace(/[%+'+\\+*+_+\/+(+)+"+.]/g, "");
        return $.trim(str);
    }
    var holdSearch = function() {
        var val = $("#hold_txt_search").val();
        val = trimSearchStr(val);
        if (val != null && val.length > 0 && val != '找找你感兴趣的') {
            var searchType = $("#holdstype")[0].value;
            if (searchType == 'group') {
                window.location.href = "/search/group/" + encodeURIComponent(val);
            } else if (searchType == 'profile') {
                window.location.href = "/search/profile/" + encodeURIComponent(val);
            } else if (searchType == 'game') {
                window.location.href = "/search/game/" + encodeURIComponent(val);
            } else {
                window.location.href = "/search/content/" + encodeURIComponent(val);
            }
        } else {
            var joymealert = require('../common/joymealert');
            var alertOption = {text:tipsText.search.search_word_error};
            joymealert.alert(alertOption);
        }
    }

    return searchPage;
});