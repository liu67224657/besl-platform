/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-7-12
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var baike = require('../page/baike')
    baike.baike();
    require('../common/jquery.lazyload')($);
    var card = require('../page/card');
    var header = require('../page/header');
    var contentlist = require('../page/contentlist');
    var followCallback = require('../page/followcallback');
    var game = require('../page/game');
    require('../../third/ui/jquery.ui.min')($);
    $().ready(function () {
        $(".lazy").scrollLoading()
        card.bindCardUnClick(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.talkBoardListBind();
        header.noticeSearchReTopInit();
        game.followGroup();
        $("#sortable").sortable({items:'div[id^=baike_one_title_]',cancel:".bk_bd",update:function() {
                    isModified = true;
                }});
        $("div[name=baike_box_13]").sortable({item:"span.three_item",update:function() {
                    isModified = true;
                }})
        $("div[name=baike_box_23]").sortable({items:"div[name=sortableTwo]",cancel:'.bk_bd_bd',update:function() {
                    isModified = true;
                }});
        $("div[name=three_item_box]").sortable({items:"span.bk_list",update:function() {
                    isModified = true;
                }});
    });
    function js() {
        var baikeObjStr = '[{name:"333333",url:"",itemId:"",icon:"",color:"",nodeType:"branch",children:[{name:"4444444",url:"",icon:"",color:"",nodeType:"branch",children:[]}]},{name:"2222222222",url:"",itemId:"",icon:"",color:"",nodeType:"branch",children:[{name:"333333333",url:"",icon:"",color:"",nodeType:"branch",children:[{name:"4444",url:"44444",icon:"",color:"#333333",nodeType:"leaf"}]},{name:"222222",url:"",icon:"",color:"",nodeType:"branch",children:[{name:"4444",url:"4444",icon:"",color:"#333333",nodeType:"leaf"}]}]},{name:"111",url:"",itemId:"",icon:"",color:"",nodeType:"branch",children:[{name:"333",url:"333",itemId:"",icon:"",color:"#333333",nodeType:"leaf"},{name:"33333",url:"3333333",itemId:"",icon:"",color:"#333333",nodeType:"leaf"}]}]'
        var baikeObj = eval('(' + baikeObjStr + ")");
        var oneItemId = 0;
        var twoItemId = 0;
        var threeitemId = 0;
        for (var i = 0; i < baikeObj.length; i++) {
            var oneItemDom = '<div class="bk_con mt10" id="baike_one_title_p' + oneItemId + '" disLevel="1"><div class="bk_hd"><h3><b>' + baikeObj[i].name + '</b> <span class="operate_bk"> ' +
                    '<a class="bk_delete" name="del_one_item" title="删除" href="javascript:void(0)"></a> ' +
                    '<a class="bk_editxt" name="edit_item" title="编辑" href="javascript:void(0)"></a>' +
                    '</span></h3><span class="dengji">' +
                    ' <a class="two_bk" name="add_two_item" href="javascript:void(0)"><i></i>二级</a>' +
                    ' <a class="three_bk" name="add_three_item" href="javascript:void(0)"><i></i>三级</a> ' +
                    '</span></div><div class="bk_bd"></div></div>'
            if (oneItemId == 0) {
                $("#baike_nav").after(oneItemDom);
            } else {
                $("#baike_one_title_p" + (oneItemId - 1)).after(oneItemDom);
            }
            var oneObj = baikeObj[i].children;
            if (oneObj.length > 0) {
                var itemBox = $("#baike_one_title_p" + oneItemId).children().last();
                for (var j = 0; j < oneObj.length; j++) {
                    if (oneObj[j].nodeType == "branch") { //二级
                        itemBox.prev().attr('name', 'baike_box_23')
                        itemBox.append('<div name="sortableTwo"><div class="bk_bd_hd" disLevel="2" id="baike_two_title_p' + twoItemId + '"><b>' + oneObj[j].name +
                                '</b><span class="operate_bk"> <a title="删除" href="javascript:void(0)" name="del_two_item" class="bk_delete"></a><a title="编辑" name="edit_item" href="javascript:void(0)" class="bk_editxt"></a></span>' +
                                '<span class="dengji"><a class="three_bk" name="add_twoToThree_item" href="javascript:void(0)"><i></i>三级</a></span>' +
                                '</div><div class="bk_bd_bd" name="three_item_box"></div></div>');
                        var threeObj = oneObj[j].children;
                        if (threeObj.length > 0) {
                            for (var k = 0; k < threeObj.length; k++) {
                                var itemColorClass = ""
                                if (threeObj[k].color == "#333333" || threeObj[k].color == "") {
                                    itemColorClass = "bk_list";
                                } else {
                                    itemColorClass = 'bk_list on';
                                }
                                $('#baike_two_title_p' + twoItemId).next().append('<span id="baike_three_title_p' + threeitemId + '" class="' + itemColorClass + '" disLevel="3"> <b>' +
                                        '<a href="' + threeObj[k].url + '">' + threeObj[k].name + '</a></b> ' +
                                        '<span class="operate_bk"> <a title="删除" href="javascript:void(0)" name="del_twoToThree_item" class="bk_delete"></a>' +
                                        '<a title="编辑" name="edit_item" href="javascript:void(0)" class="bk_editxt"></a></span></span>');
                            }
                        }
                        twoItemId++;
                    } else if (oneObj[j].nodeType == "leaf") {
                        var itemColorClass = ""
                        if (oneObj[j].color == "#333333" || oneObj[j].color == "") {
                            itemColorClass = "bk_list";
                        } else {
                            itemColorClass = 'bk_list on';
                        }
                        itemBox.append('<span id="baike_three_title_p' + threeitemId + '" disLevel="3" class="' + itemColorClass + '"> <b><a href="' + oneObj[j].url + '">' + oneObj[j].name + '</a></b> <span class="operate_bk"> <a title="删除" name="del_three_item" href="javascript:void(0)" class="bk_delete"></a>' +
                                '<a title="编辑" name="edit_item" href="javascript:void(0)" class="bk_editxt"></a></span></span>');
                    }
                }
            }
            oneItemId++;
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
})