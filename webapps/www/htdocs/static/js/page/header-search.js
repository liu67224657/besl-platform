define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    require('../common/tips');
    var search = {
        initSearch:function() {
            $("#txt_search").live("keydown", function(e) {
                if (e.keyCode == 13) {
                    search.search();
                }

//                if (common.getInputLength($(this).val()) >= 14) {
//                    $(this).val($(this).val().substr(0, 14));
//                }
            });
            //搜索事件
//            $("#txt_search").keydown(function() {
//                search.searchTips($(this));
//            });
            $("#txt_search").focus(function() {
                search.searchTips($(this));
            });
//            $("#txt_search").keyup(function(event) {
//                search.searchTips($(this));
//                switch (event.keyCode) {
//                    case 38:
//                        if (!$("#search_tips").is(":hidden")) {
//                            var libg = $("#search_tips>div.searchc>ul>li[class=hover]");
//                            if (libg.size() != 0) {
//                                libg.removeClass("hover");
//                            }
//                            if (libg.prev().size() != 0) {
//                                libg.prev().addClass("hover");
//                                $("#stype").val(libg.prev()[0].id);
//                            } else {
//                                var hoverObj = $("#search_tips>div.searchc>ul>li").last();
//                                hoverObj.addClass("hover");
//                                $("#stype").val(hoverObj[0].id);
//                            }
//                        }
//                        break;
//                    case 40:
//                        if (!$("#search_tips").is(":hidden")) {
//                            var libg = $("#search_tips>div.searchc>ul>li[class=hover]");
//                            //清除.libg
//                            if (libg.size() != 0) {
//                                libg.removeClass("hover");
//                            }
//                            if (libg.next().size() != 0) {
//                                libg.next().addClass("hover");
//                                $("#stype").val(libg.next()[0].id);
//                            } else {
//                                var hoverObj = $("#search_tips>div.searchc>ul>li").first();
//                                hoverObj.addClass("hover");
//                                $("#stype").val(hoverObj[0].id);
//                            }
//
//                        }
//                        break;
//                    case 13:
//                        search.search();
//                        break;
//                    default:
//                        break;
//                }
//            });
//            $("#txt_search").blur(function() {
//                $("#search_tips").slideUp();
//            });
//            $("#search_tips li").hover(function() {
//                $(this).addClass("hover");
//            }, function() {
//                $(this).removeClass("hover");
//            });
//            $("#search_tips li").click(function() {
//                $("#stype")[0].value = $(this)[0].id;
//                search.search();
//            });

            //搜索提交
            $("#sericon").bind("click", function() {
                search.search();
            });
        },
        //搜索下拉框
//        searchTips:function (jqueryObj) {
//            var val = search.trimSearch(jqueryObj.val());
//            if (val == null || val.length == 0 || val == '找找你感兴趣的') {
//                $("#search_tips").css("display", "none");
//                if ($("#memo_message>div.tipsc>p").size() > 0) {
//                    $('#memo_message').slideDown();
//                }
//            } else {
//                $("#search_tips").css({"display": "block","left": '-6px',top: '30px'});
//                if ($.trim(val).length > 8) {
//                    val = val.substr(0, 8) + "...";
//                }
//                $("#search_tips ul li b").text(val);
//                if (!$("#memo_message").is(":hidden")) {
//                    $("#memo_message").hide();
//                }
//            }
//        },

        //过滤搜索
        trimSearch:function (str) {
            str = str.replace(/[%+'+\\+*+_+\/+(+)+"+.]/g, "");
            return $.trim(str);
        },
        search:function() {
            var val = $("#txt_search").val();
            val = search.trimSearch(val);
            if (val != null && val.length > 0 && val != '找找你感兴趣的') {
//                var searchType = $("#stype")[0].value;
//                if (searchType == 'group') {
//                    window.location.href = joyconfig.URL_WWW+"/search/group/" + encodeURIComponent(val);
//                } else if (searchType == 'profile') {
//                    window.location.href =  joyconfig.URL_WWW+"/search/profile/" + encodeURIComponent(val);
//                } else if (searchType == 'game') {
//                    window.location.href =  joyconfig.URL_WWW+"/search/game/" + encodeURIComponent(val);
//                } else {
//                    window.location.href =  joyconfig.URL_WWW+"/search/content/" + encodeURIComponent(val);
//                }
                window.open("http://www.baidu.com/s?wd=intitle:" + val + " 着迷网 inurl:joyme", "_blank");
            } else {
                var joymealert = require('../common/joymealert');
                var alertOption = {text:tipsText.search.search_word_error};
                joymealert.alert(alertOption);
            }
        }
    }
    return search;
});

