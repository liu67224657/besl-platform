define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var categoryBiz = require('../biz/category-biz');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var pop = require('../common/jmpopup');
    require('../common/tips');

    var category = {
        bindSortSelect:function() {
            $('a[name=category]').live('click', function() {
                if (loginBiz.checkLogin($(this))) {
                    categoryBiz.getCategoryPrivacy($(this), loadingCallback, getPrivacyCallback);
                }
            });
        },
        bindSortOrder:function() {
            $('a[name=categoryorder]').live('click', function() {
                if (loginBiz.checkLogin($(this))) {
                    if ($("a[name=canceltop]").length >= 2) {
                        var alertOption = {text:'最多允许置顶两篇文章',
                            tipLayer:true,
                            textClass:"tipstext"};
                        joymealert.alert(alertOption);
                    } else {
                        categoryBiz.categoryorder($(this), categoryorderCallback);
                    }

                }
            });

            //up down
            $('a[name=toporder]').live('click', function() {
                if (loginBiz.checkLogin($(this))) {
                    var paramObj = {};
                    if ($(this).attr("class") == 'downicon') {
                        var ordervalue = parseInt($(this).attr("data-ordervalue")) + 1;
                        var jqObj = $(".topicon");
                        paramObj = {
                            cid:jqObj.attr("data-cid"),
                            cuno:jqObj.attr("data-cuno"),
                            lineid:jqObj.attr("data-lineid"),
                            ordervalue:ordervalue
                        };
                    } else {
                        var jqObj = $(".downicon");
                        var ordervalue = parseInt(jqObj.attr("data-ordervalue")) + 1;

                        paramObj = {
                            cid:$(this).attr("data-cid"),
                            cuno:$(this).attr("data-cuno"),
                            lineid:$(this).attr("data-lineid"),
                            ordervalue:ordervalue
                        };
                    }

                    categoryBiz.toporder($(this), paramObj, toporderCallback);
                }
            });

            //cancel
            $('a[name=canceltop]').live('click', function() {
                if (loginBiz.checkLogin($(this))) {
                    categoryBiz.canceltop($(this), canceltopCallback);
                }
            });
        }

    }

    var getPrivacyCallback = function(jqObj, jsonObj) {
        if (jsonObj.status_code == '1') {
            categoryMaskInit(jqObj, jsonObj);
        } else if (jsonObj.status_code == '-1') {
            pop.hidepopById('category', true, true, null);
            loginBiz.maskLoginByJsonObj(jsonObj);
        } else if (jsonObj.status_code == '-5') {
            pop.hidepopById('category', true, true, null);
            var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                tipLayer:true,
                textClass:"tipstext"};
            joymealert.alert(alertOption);
        } else if (jsonObj.status_code == '0') {
            categoryMaskError(jqObj, jsonObj);
        }
    }

    var loadingCallback = function(jqObj) {
        var jqOffset = jqObj.offset();
        var categoryPopConfig = {
            pointerFlag : false,//是否有指针
            tipLayer : false,//是否遮罩
            containTitle : false,//包含title
            containFoot : false,//包含footer
            forclosed:true,
            offset:"Custom",
            offsetlocation:[jqOffset.top + 20,jqOffset.left],
            popwidth:150,
            popscroll:false,
            allowmultiple:false,
            isremovepop:true
        };
        var htmlObj = new Object();
        htmlObj['id'] = 'category'; //设置层ID
        htmlObj['html'] = '<div class="jing-select clearfix" style="top:0px">' +
                '<div class="jing-groups jinglist clearfix">' +
                '<div class="zhuantieload" style="height: 10px"></div>' +
                '</div> ' +
                '</div>';
        pop.popupInit(categoryPopConfig, htmlObj);
    }

    var categoryMaskInit = function(jqObj, jsonObj) {

        var jqOffset = jqObj.offset();
//        var distance = jqOffset.top - $(document).scrollTop();
        var categoryStr = parseChildrenCategory(jsonObj);
        if (categoryStr != '') {
            var html = '<div class="jing-select clearfix">' +
                    '<div class="jing-groups jinglist clearfix">' +
                    '分配文章到...' +
                    '<ul class="clearfix">' +
                    categoryStr +
                    '</ul>' +
                    '<div class="fenzu_icon">' +
                    '<a class="submitbtn" id="savecategory" data-cid="' + jqObj.attr("data-cid") + '" data-cuno="' + jqObj.attr("data-cuno") + '"><span>保存</span></a><a class="graybtn" id="hideCategory"><span>取 消</span></a></div>' +
                    '</div> ' +
                    '</div>';
            $('#category').html(html);
        } else {
            var html = '<div class="jing-select clearfix">' +
                    '<div class="jing-groups jinglist clearfix">' +
                    '您还没有可以管理的分类，请与管理员联系解决。' +
                    '<ul class="clearfix">' +
                    categoryStr +
                    '</ul>' +
                    '<div class="fenzu_icon">' +
                    '<a class="graybtn" id="hideCategory"><span>取 消</span></a></div>' +
                    '</div> ' +
                    '</div>';
            $('#category').html(html);
        }

        bindCategoryEvent();
    }

    var categoryMaskError = function(jqObj, jsonObj) {
        var jqOffset = jqObj.offset();
//        var distance = jqOffset.top - $(document).scrollTop();
        var categoryStr = parseChildrenCategory(jsonObj);
        var html = '<div class="jing-select clearfix">' +
                '<div class="jing-groups jinglist clearfix">' +
                '加载失败...请取消重试' +
                '<div class="fenzu_icon">' +
                '<a class="graybtn" id="hideCategory"><span>取 消</span></a></div>' +
                '</div> ' +
                '</div>';
        $('#category').html(html);
        bindCategoryEvent();
    }

    var parseChildrenCategory = function(jsonObj) {
        var returnStr = '';
        var allIds = '';
        if (jsonObj.result.length == 0) {
            return "";
        }
        $.each(jsonObj.result, function(i, val) {
            var categoryStr = '';
            var isChecked = false;

            $.each(val.childPrivacyCategories, function(j, category) {
                allIds = allIds + category.categoryId + ",";
                if (category.checked) {
                    isChecked = true;
                    categoryStr = categoryStr + '<p><input type="checkbox" class="checkbox" name="categoryids" checked="checked" value="' + category.categoryId + '">' + category.categoryName + '</p>';
                } else {
                    categoryStr = categoryStr + '<p><input type="checkbox" class="checkbox" name="categoryids" value="' + category.categoryId + '">' + category.categoryName + '</p>';
                }

            });

            var rootCategory = '';
            var displayStyle = 'none';
            if (isChecked) {
                rootCategory = '<em id="atEm" class="ait" name="categoryNode">' + val.categoryName + '</em>';
                displayStyle = 'block';
            } else {
                rootCategory = '<em id="atEm" class="aithover" name="categoryNode">' + val.categoryName + '</em>';
            }

            returnStr = returnStr + '<li class="clearfix">' +
                    rootCategory +
                    '<div class="radiolist clearfix"  style="display: ' + displayStyle + '">' +
                    categoryStr +
                    '</div>' +
                    '</li>';
        });
        returnStr = returnStr + '<input type="hidden" name="allIds" value="' + allIds + '" id="allIds"/>';
        return returnStr;
    }

    var bindCategoryEvent = function() {
        $("#hideCategory").bind("click", function() {
            pop.hidepopById('category', true, true, null);
        });
        $("em[name=categoryNode]").bind("click", function() {
            if ($(this).attr("class") == 'ait') {
                $(this).removeClass("ait").addClass('aithover');
            } else {
                $(this).removeClass("aithover").addClass('ait');
            }
            $(this).next().toggle();
        });
        $("#savecategory").bind("click", function() {
            gatherCategorySelect($(this));
        });
    }

    var gatherCategorySelect = function(jqObj) {
        var ids = '';
        $.each($("input[type=checkbox][name=categoryids]"), function(i, val) {
            if (val.checked) {
                ids = ids + val.value + ',';
            }
        });
        var paramObj = {ids:ids,cid:jqObj.attr("data-cid"),cuno:jqObj.attr("data-cuno"),allIds:$("#allIds").val()};
        categoryBiz.gatherCategory($("#savecategory"), paramObj, gatherLoadingCallback, gatherCallback);

    }

    var gatherLoadingCallback = function() {
        $("#savecategory").die();
        $("#savecategory").removeClass("submitbtn").addClass("graybtn");
        $("#savecategory").html('<span>保存中</span>');
    }

    var gatherCallback = function(jqObj, jsonObj) {
        $("#hideCategory").trigger('click');
    }

    var categoryorderCallback = function(jqObj, jsonObj) {
        var cid = jqObj.attr("data-cid");
        if (jsonObj.status_code == '1') {
            if ($("a[name=canceltop]").length > 0) {
                var jqObj = $("a[name=canceltop]");
                var str = '<a href="javascript:void(0)" name="toporder" data-cid="' + jqObj.attr("data-cid") + '" data-cuno="' + jqObj.attr("data-cuno") + '" data-lineid="' + jqObj.attr("data-lineid") + '" data-ordervalue="' + jqObj.attr("data-ordervalue") + '"  class="topicon"></a>';
                jqObj.before(str);
                addZhiDing(jqObj, jsonObj);
            } else {
                initZhiDing(jqObj, jsonObj);
            }
            $("#conent_" + cid).remove();
        } else if (jsonObj.status_code == '2') {
            var alertOption = {text:'最多允许置顶两篇文章',
                tipLayer:true,
                textClass:"tipstext"};
            joymealert.alert(alertOption);
        } else {
            var alertOption = {text:'操作成败',
                tipLayer:true,
                textClass:"tipstext"};
            joymealert.alert(alertOption);
        }
    }

    var canceltopCallback = function(jqObj, jsonObj) {
        if (jsonObj.status_code == '1') {
            $("#zhiding_" + jqObj.attr("data-cid")).remove();
            if ($(".zd_tit").length == 0) {
                $("#zd_line").remove();
            }
            if ($(".zd_tit").length == 1) {
                $("a[name=toporder]").remove();
            }
        }
    }

    var toporderCallback = function(jqObj, paramObj, jsonObj) {
        if (jsonObj.status_code == '1') {
            if (jqObj.attr("class") == 'downicon') {
                var topObj = $(".topicon");
                topObj.attr("data-ordervalue", paramObj.ordervalue);

                topObj.removeClass("topicon").addClass("downicon");
                jqObj.removeClass("downicon").addClass("topicon");

                var footDiv = $("#zhiding_" + topObj.attr("data-cid"));
                var topDiv = $("#zhiding_" + jqObj.attr("data-cid"));
                var divArr = [topDiv.html(),footDiv.html()];

                $("#zhiding_" + topObj.attr("data-cid")).html(divArr[0]);
                $("#zhiding_" + jqObj.attr("data-cid")).html(divArr[1]);
            } else {
                var downObj = $(".downicon");
                jqObj.attr("data-ordervalue", paramObj.ordervalue);

                downObj.removeClass("downicon").addClass("topicon");
                jqObj.removeClass("topicon").addClass("downicon");

                var footDiv = $("#zhiding_" + downObj.attr("data-cid"));
                var topDiv = $("#zhiding_" + jqObj.attr("data-cid"));
                var divArr = [topDiv.html(),footDiv.html()];

                $("#zhiding_" + downObj.attr("data-cid")).html(divArr[0]);
                $("#zhiding_" + paramObj.cid).html(divArr[1]);
            }
        } else {
            var alertOption = {text:'操作成败',
                tipLayer:true,
                textClass:"tipstext"};
            joymealert.alert(alertOption);
        }
    }

    var initZhiDing = function(jqObj, jsonObj) {
        var topContentDto = jsonObj.result[0];
        var multiStr = parseMulti(topContentDto);
        var htmlStr = '<div class="zd_tit" id="zhiding_' + topContentDto.content.contentId + '">' +
                '<a href="' + joyconfig.URL_WWW + '/note/' + topContentDto.content.contentId + '" target="_blank">' + topContentDto.content.subject + '</a>' +
                multiStr +
                '<div class="addsth">' +
                '<a href="javascript:void(0)" class="addt" name="canceltop" data-cid="' + topContentDto.content.contentId + '" data-cuno="' + topContentDto.content.uno + '" data-lineid="' + topContentDto.lineItem.lineId + '" data-ordervalue="' + topContentDto.lineItem.displayOrder + '">取消置顶</a>' +
                '</div>' +
                '</div>' +
                '<div class="zd_line" id="zd_line"></div>';
        $("#zhiding").append(htmlStr);
    }

    var addZhiDing = function(jqObj, jsonObj) {
        var topContentDto = jsonObj.result[0];
        var multiStr = parseMulti(topContentDto);
        var htmlStr = '<div class="zd_tit" id="zhiding_' + topContentDto.content.contentId + '">' +
                '<a href="' + joyconfig.URL_WWW + '/note/' + topContentDto.content.contentId + '" target="_blank">' + topContentDto.content.subject + '</a>' +
                multiStr +
                '<div class="addsth">' +
                '<a href="javascript:void(0)" name="toporder" data-cid="' + topContentDto.content.contentId + '" data-cuno="' + topContentDto.content.uno + '" data-lineid="' + topContentDto.lineItem.lineId + '" data-ordervalue="' + topContentDto.lineItem.displayOrder + '"  class="downicon"></a>' +
                '<a href="javascript:void(0)" class="addt" name="canceltop" data-cid="' + topContentDto.content.contentId + '" data-cuno="' + topContentDto.content.uno + '" data-lineid="' + topContentDto.lineItem.lineId + '" data-ordervalue="' + topContentDto.lineItem.displayOrder + '">取消置顶</a>' +
                '</div>' +
                '</div>';
        $("#zhiding").find('div').first().before(htmlStr);

    }

    var parseMulti = function(topContentDto) {
        var multiStr = '';
        if (topContentDto.hasApp) {
            multiStr = multiStr + '<span class="tapp"></span>';
        }
        if (topContentDto.hasImg) {
            multiStr = multiStr + '<span class="tpic"></span>';
        }
        if (topContentDto.hasVideo) {
            multiStr = multiStr + '<span class="tvideo"></span>';
        }
        if (topContentDto.hasAudio) {
            multiStr = multiStr + '<span class="tmusic"></span>';
        }
        return multiStr;
    }

    return category;
});