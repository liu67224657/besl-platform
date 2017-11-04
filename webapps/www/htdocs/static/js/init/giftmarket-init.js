define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var joymealert = require('../common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    $(document).ready(function() {

        var index = Number(1);
        var total = $("#ad_list a").length;

        $("#ad_list a").hide();
        $("#ad_link" + index).show();
        $("#i_" + index).addClass("current");

        var t = 0;
        t = setInterval(function showAuto() {
            $("#ad_link" + index).hide();
            $("#i_" + index).removeClass("current");
            index = index >= total ? 1 : ++index;
            $("#ad_link" + index).show();
            $("#i_" + index).addClass("current");
        }, 3000);
        $("#ad_list").hover(function() {
            clearInterval(t)
        }, function() {
            t = setInterval(function showAuto() {
                $("#ad_link" + index).hide();
                $("#i_" + index).removeClass("current");
                index = index >= total ? 1 : ++index;
                $("#ad_link" + index).show();
                $("#i_" + index).addClass("current");
            }, 3000);
        });

        $("#category_tag a").mouseenter(function() {
            $("#category_tag a.current").removeClass("current");
            $(this).addClass("current");
            var category = $(this).attr("data-category");
            var goodsJqObj = $('#exchange-credits-' + category);
            var ajaxResult = $('#exchange-credits-' + category + ' li.ajax-result')
            if (goodsJqObj.length > 0 && ajaxResult.length > 0) {
                $("div.exchange-credits").hide();
                goodsJqObj.show();
                return;
            }
            biz.query(category, callback.queryCallback);
        });
        $("#logdingWap").click(function() {
            biz.waploadinglist(callback.waploadinglistCallBack);
        });
//        $("#logdingSearch").click(function() {
//            biz.searchResultList(callback.searchResultListCallBack);
//        });
//        $("#searchButton").click(function() {
//            var giftName = $("#searchGiftName").val().trim();
//            if (giftName == '' || giftName == '输入礼包名称') {
//                alertOption.text = '请输入礼包名称';
//                alertOption.title = '提示';
//                joymealert.alert(alertOption);
//                return;
//            }
//            window.open("http://www.baidu.com/s?wd=intitle:" + giftName + "礼包 着迷网 inurl:joyme", "_blank");
//        })
        var bizLock;
        var biz = {
            query: function (category, callback) {
                if (bizLock) {
                    return;
                }
                bizLock = true;
                $.ajax({type: "POST",
                            url: joyconfig.URL_WWW + "/json/coin",
                            data: {category: category},
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result, category);
                            },
                            complete: function () {
                                bizLock = false;
                            }
                        });

            },waploadinglist:function(callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                if (maxNum <= curNum) {
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({ type:"POST",
                            url:joyconfig.URL_WWW + "/joymeapp/activity/gift/list",
                            data:{count:20,type:1,pnum:curNum},
                            success:function(req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            }
                        });
            },searchResultList:function(callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                var cond = $("#cond").val();
                if (maxNum <= curNum) {
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({ type:"POST",
                            url:joyconfig.URL_WWW + "/gift/wap/jsonsearch",
                            data:{searchtext:cond,pnum:curNum},
                            success :function(req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            }
                        });
            }
        }

        var callback = {
            queryCallback: function (result, category) {
                var content = '';
                if (result.status_code == '0') {
                    content = '<li class="ajax-result-none"><p align="center">该目录下暂时没有活动内容</p></li>';
                } else if (result.status_code == '1') {
                    if (result.result == null || result.result.length == 0) {
                        content = '<li><p class="ajax-result-none" align="center">该目录下暂时没有活动内容</p></li>';
                    } else {
                        for (var index = 0; index < result.result.length; index++) {
                            content += "<li class='ajax-result'><div><a href='" + joyconfig.URL_WWW + "/coin/" + result.result[index].gid + "'>" +
//                                    " <span class='fold-7'>7折</span>" +
                                    "<img height='160' width='160' src='" + result.result[index].gipic + "'></a></div>" +
                                    "<h2><a href='" + joyconfig.URL_WWW + "/coin/" + result.result[index].gid + "'>" + result.result[index].title + "</a></h2>" +
                                    "<p>" + result.result[index].desc + "</p>" +
                                    "<p><span class='fl'>" + result.result[index].point + "积分/次</span>" +
                                    "<span class='fr'>剩余" + result.result[index].sn + "个</span></p></li>";
                        }
                    }
                }
                var html = "<div class='exchange-credits' style='display:none' id='exchange-credits-" + category + "'><ul class='clearfix' id='clearfix-" + category + "'>";
                html += content;
                html += "</ul></div>";

                $('#category_tag').after(html);
                $("div.exchange-credits").hide();
                $('#exchange-credits-' + category).show();
            },
            waploadinglistCallBack:function (result) {
                var uno = $("#uno").val();
                var content = "";
                for (var index = 0; index < result.result.length; index++) {
                    content += "<a href='/giftmarket/wap/giftdetail?aid=" + result.result[index].gid + "&uno=" + uno + "'><img src='" + result.result[index].gipic + "'><div>" +
                            " <p>" + result.result[index].title + "</p>" +
                            " <p><span>有效期：" + result.result[index].exDate + "</span> <span>剩余：" + result.result[index].sn + "/" + result.result[index].cn + "</span></p>" +
                            "</div></a>";
                }
                $("#wapgiftmarketlist").append(content);
                $("#wapCurPage").val(result.page.curPage);
                $("#wapMaxPage").val(result.page.maxPage);
                if (result.page.curPage >= result.page.maxPage) {
                    $("#logdingWap").css("display", "none");
                }
                var loading = document.getElementById('loading-btn'),
                        icon = loading.getElementsByTagName('i')[0],
                        txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '加载更多';
            },searchResultListCallBack:function(result) {
                var uno = $("#uno").val();
                var content = "";
                for (var index = 0; index < result.result.list.length; index++) {
                    content += "<a href='/giftmarket/wap/giftdetail?aid=" + result.result.list[index].gid + "&uno=" + uno + "'><img src='" + result.result.list[index].gipic + "'><div>" +
                            " <p>" + result.result.list[index].title + "</p>" +
                            " <p><span>有效期：" + result.result.list[index].exDate + "</span> <span>剩余：" + result.result.list[index].sn + "/" + result.result.list[index].cn + "</span></p>" +
                            "</div></a>";
                }
                $("#wapgiftmarketlist").append(content);
                $("#wapCurPage").val(result.result.page.curPage);
                $("#wapMaxPage").val(result.result.page.maxPage);
                if (result.result.page.curPage >= result.result.page.maxPage) {
                    $("#logdingSearch").css("display", "none");
                }
                var loading = document.getElementById('loading-btn'),
                        icon = loading.getElementsByTagName('i')[0],
                        txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '加载更多';
            }
        }
    });

});





