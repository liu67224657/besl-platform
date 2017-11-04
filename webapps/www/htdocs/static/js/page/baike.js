/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-7-16
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var joymealert = require('../common/joymealert');
    var common = require('../common/common')
    var firstId = 0;
    var secondId = 0;
    var thirdId = 0;
    window.isModified = false;
    exports.baike = function() {
        $('.stab_bd a').live('click', function() {
            return false;
        })
        if ($("div[id^=baike_one_title_]").size() > 0) {
            $("a[name=save_baike]").show();
        } else {
            $("a[name=save_baike]").hide();
        }
        $('#add_baike_one').bind('click', function() { //增加一级导航
            var confirmOption = {
                title:'添加一级标题',
                text:'',
                html:'<div class="bk_tc">' +
                        '<div class="bk_p clearfix">' +
                        '<span>标题名称</span><input type="text" class="bk_txt" id="one_title" /></div>' +
                        '<div class="tipstext" id="addTips" style="display:none;">一级标题支持1~15汉字</div></div>',
                submitFunction:function() {
                    var onetitle = $("#one_title").val();
                    if (onetitle.length == 0) {
                        $("#addTips").text('请输入标题').show();
                        $("#one_title").focus();
                        return true;
                    }
                    if (common.strLen(onetitle) > 15) {
                        $("#addTips").text('一级标题支持1~15汉字').show();
                        $("#one_title").focus();
                        return true;
                    }
                    $("#baiketree_bottom").before('<div class="bk_con mt10" id="baike_one_title_p' + firstId + '">' +
                            '<div class="bk_hd" disLevel="1"><h3><b>' + $("#one_title").val() + '</b> <span class="operate_bk"> ' +
                            '<a class="bk_delete" name="del_one_item" title="删除" href="javascript:void(0)"></a> ' +
                            '<a class="bk_editxt" name="edit_item" title="编辑" href="javascript:void(0)"></a>' +
                            '</span></h3><span class="dengji">' +
                            ' <a class="two_bk" name="add_two_item" href="javascript:void(0)"><i></i>二级</a>' +
                            ' <a class="three_bk" name="add_three_item" href="javascript:void(0)"><i></i>三级</a> ' +
                            '</span></div><div class="bk_bd" name="baike_box"></div></div>');
                    firstId++;
                    if ($("div[id^=baike_one_title_]").size() > 0) {
                        $("a[name=save_baike]").show();
                    }
                    isModified = true;
                    return false;
                }
            }
            joymealert.prompt(confirmOption);
        });
        $("a[name=del_one_item]").live('click', function() {//删除一级导航
            var baike_one_item = $(this).parent().parent().parent().parent();
            var text = "确定要删除该标题吗？"
            if (baike_one_item.children('div:last').children("div").size() != 0 || baike_one_item.children('div:last').children("span").size() != 0) {
                text = '确定要删除该标题吗?<br /><span style="color:#ff4200">(该操作将会删除所有子标题)</span>'
            }
            var confirmOption = {
                title:'提示信息',
                text:text,
                submitFunction:function() {
                    baike_one_item.remove();
                    if ($("div[id^=baike_one_title_]").size() == 0) {
                        $("a[name=save_baike]").hide();
                    }
                    isModified = true;
                }
            }
            joymealert.confirm(confirmOption);
        });
        $("a[name=add_two_item]").live('click', function() {//增加二级导航
            var _this = $(this)
            var baike_two_item = $(this).parent().parent().next();
            var confirmOption = {
                title:'添加二级标题',
                text:'',
                html:'<div class="bk_tc">' +
                        '<div class="bk_p clearfix"><span>标题名称</span><input type="text" id="two_title" class="bk_txt">' +
                        '</div>' +
                        '<div class="tipstext" id="addTips" style="display:none;"></div>' +
                        '</div>',
                submitFunction:function() {
                    var twotitle = $("#two_title").val();
                    if (twotitle.length == 0) {
                        $("#addTips").text('请输入二级标题').show();
                        $("#two_title").focus();
                        return true;
                    }
                    if (common.strLen(twotitle) > 15) {
                        $("#addTips").text('二级标题支持1~15汉字').show();
                        $("#two_title").focus();
                        return true;
                    }
                    baike_two_item.addClass('bk_pd01').attr('name', "baike_box_23");//为容器添加直接二级样式
                    baike_two_item.append('<div name="sortableTwo"><div class="bk_bd_hd" disLevel="2" id="baike_two_title_p' + secondId + '"><h3><b>' + twotitle +
                            '</b><span class="operate_bk"> <a title="删除" href="javascript:void(0)" name="del_two_item" class="bk_delete"></a><a title="编辑" name="edit_item" href="javascript:void(0)" class="bk_editxt"></a></span></h3>' +
                            '<span class="dengji"><a class="three_bk" name="add_twoToThree_item" href="javascript:void(0)"><i></i>三级</a></span>' +
                            '</div><div class="bk_bd_bd" name="three_item_box"></div></div>');
                    $("div[name=baike_box_23]").sortable({items:"div[name=sortableTwo]",cancel:'.bk_bd_bd'});
                    _this.next().hide();
                    secondId++;
                    isModified = true;
                    return false;
                }
            }
            joymealert.prompt(confirmOption);
        });
        $("a[name=del_two_item]").live('click', function() {//删除二级导航
            var baike_one_item = $(this).parent().parent().parent();
            var baike_two_box = $(this).parent().parent().parent().parent();
            var text = "确定要删除该标题吗？";
            if (baike_one_item.next().children('span').size() > 0) {
                text = '确定要删除该标题吗?<br /><span style="color:#ff4200">(该操作将会删除所有子标题)</span>'
            }
            var confirmOption = {
                title:'提示信息',
                text:text,
                submitFunction:function() {
                    if (baike_one_item.next().hasClass('bk_bd_bd')) {//判断二级分类后有没有三级分类
                        baike_one_item.next().remove();//有三级把三级也删除
                    }
                    baike_one_item.parent().remove();//删除二级
                    if (baike_two_box.children('div').size() == 0) {//如果该框下没有内容
                        baike_two_box.removeClass().addClass('bk_bd');//去掉样式
                        baike_two_box.prev().find('span:last a').show();
                    }
                    isModified = true;
                }
            }
            joymealert.confirm(confirmOption);
        });
        $("a[name=add_three_item]").live('click', function() {//直接添加三级模式
            var _this = $(this)
            var baike_item_box = $(this).parent().parent().next();
            var confirmOption = {
                title:'添加三级标题',
                text:'',
                html:'<div class="bk_tc">' +
                        '<div class="bk_p clearfix"><span>标题名称</span><input type="text" id="three_title" class="bk_txt">' +
                        '</div>' +
                        '<div class="bk_p mt12 clearfix"><span>标题链接</span><input type="text" id="three_url" class="bk_txt">' +
                        '</div>' +
                        '<div class="tipstext" id="addTips" style="display:none">二级标题支持1~15汉字</div>' +
                        '<div class="bk_p mt12 clearfix"><span>标题标红</span><div class="bk_radio"> <input name="itemColor" type="radio" value="false" checked="checked"><span>否</span>  <input name="itemColor" type="radio" class="bk_ml" value="true"><span>是</span></div>' +
                        '</div>' +
                        '</div>',
                submitFunction:function() {
                    var threetitle = $("#three_title").val();
                    if (threetitle.length == 0) {
                        $("#addTips").text('请输入标题').show();
                        $("#three_title").focus();
                        return true;
                    }
                    if (common.strLen(threetitle) > 15) {
                        $("#addTips").text('三级标题支持1~15汉字').show();
                        $("#three_title").focus();
                        return true;
                    }
                    var threeUrl = $("#three_url").val();
                    if (threeUrl.length == 0) {
                        $("#addTips").text('请输入超链接').show();
                        $("#three_url").focus();
                        return true;
                    }
                    var itemColorClass = 'bk_list';
                    if ($('input[name=itemColor]:checked').val() == 'true') {
                        itemColorClass = 'bk_list on';
                    }
                    baike_item_box.attr('name', "baike_box_13");//为容器添加直接三级样式
                    baike_item_box.append('<span id="baike_three_title_p' + thirdId + '" disLevel="3" class="' + itemColorClass + '" name="three_item"> <b><a href="' + threeUrl + '">' + threetitle + '</a></b> <span class="operate_bk"> <a title="删除" name="del_three_item" href="javascript:void(0)" class="bk_delete"></a>' +
                            '<a title="编辑" name="edit_item" href="javascript:void(0)" class="bk_editxt"></a></span></span>');
                    $("div[name=baike_box_13]").sortable({item:"span.three_item"})
                    _this.prev().hide();
                    thirdId++;
                    isModified = true;
                    return false;
                }
            }
            joymealert.prompt(confirmOption);
        });
        $("a[name=del_three_item]").live('click', function() {//删除直接添加三级标题
            var baike_three_item = $(this).parent().parent();
            var bakke_box = baike_three_item.parent()
            var confirmOption = {
                title:'删除三级标题',
                text:'确定要删除该标题吗？',
                submitFunction:function() {
                    baike_three_item.remove();
                    if (bakke_box.find('span').size() == 0) {
                        bakke_box.prev().find('span:last a').show();
                    }
                    isModified = true;
                }
            }
            joymealert.confirm(confirmOption);
        });
        $("a[name=add_twoToThree_item]").live('click', function() {
            var thisLine = $(this).parent().parent();
            var confirmOption = {
                title:'添加三级标题',
                text:'',
                html:'<div class="bk_tc">' +
                        '<div class="bk_p clearfix"><span>标题名称</span><input type="text" id="three_title" class="bk_txt">' +
                        '</div>' +
                        '<div class="bk_p mt12 clearfix"><span>标题链接</span><input type="text" id="three_url" class="bk_txt">' +
                        '</div>' +
                        '<div class="tipstext" id="addTips" style="display:none;">二级标题支持1~15汉字</div>' +
                        '<div class="bk_p mt12 clearfix"><span>标题标红</span><div class="bk_radio"> <input name="itemColor" type="radio" checked="checked" value="false"><span>否</span>  <input name="itemColor" type="radio" class="bk_ml" value="true"><span>是</span></div></div>' +
                        '</div>',
                submitFunction:function() {
                    var threetitle = $("#three_title").val();
                    if (threetitle.length == 0) {
                        $("#addTips").text('请输入标题').show();
                        $("#three_title").focus();
                        return true;
                    }
                    if (common.strLen(threetitle) > 15) {
                        $("#addTips").text('三级标题支持1~15汉字').show();
                        $("#three_title").focus();
                        return true;
                    }
                    var threeUrl = $("#three_url").val();
                    if (threeUrl.length == 0) {
                        $("#addTips").text('请输入超链接').show();
                        $("#three_url").focus();
                        return true;
                    }
                    if (!thisLine.next().hasClass('bk_bd_bd')) {
                        thisLine.after('<div class="bk_bd_bd"></div>')
                    }
                    var thisItem = thisLine.next();
                    var itemColorClass = 'bk_list';
                    if ($('input[name=itemColor]:checked').val() == 'true') {
                        itemColorClass = 'bk_list on';
                    }
                    thisItem.append('<span id="baike_three_title_p' + thirdId + '" class="' + itemColorClass + '" disLevel="3"> <b>' +
                            '<a href="' + $("#three_url").val() + '">' + $('#three_title').val() + '</a></b> ' +
                            '<span class="operate_bk"> <a title="删除" href="javascript:void(0)" name="del_twoToThree_item" class="bk_delete"></a>' +
                            '<a title="编辑" name="edit_item" href="javascript:void(0)" class="bk_editxt"></a></span></span>');
                    $("div[name=three_item_box]").sortable({items:"span.bk_list"});
                    thirdId++;
                    isModified = true;
                    return false;
                }
            }
            joymealert.prompt(confirmOption);
        });
        $("a[name=del_twoToThree_item]").live('click', function() {
            var thisLine = $(this).parent().parent();//本标题
            var thisLineBox = thisLine.parent();//标题容器
            var confirmOption = {
                title:'删除三级标题',
                text:'确定要删除该标题吗？',
                submitFunction:function() {
                    thisLine.remove();
                    if (thisLineBox.find('span').size() == 0) {
                        thisLineBox.remove();
                    }
                    isModified = true;
                }
            }
            joymealert.confirm(confirmOption);
        })
        $("a[name=edit_item]").live('click', function() {
            var thisLine = $(this).parent().parent();
            var isUrl = false;
            var isColor = false;
            var level = thisLine.attr('dislevel');
            var html = '<div class="bk_tc">' +
                    '<div class="bk_p clearfix"><span>标题名称</span><input type="text" id="edit_title" class="bk_txt" value="' + thisLine.children('b').text() + '">' +
                    '</div>';
            if (thisLine.children('b').find('a').size() != 0) {//是否有超链
                isUrl = true;
                html += '<div class="bk_p mt12 clearfix"><span>标题链接</span>' +
                        '<input type="text" id="edit_url" class="bk_txt" value="' + thisLine.children('b').find('a').attr('href') + '">' +
                        '</div>'
            }
            html += '<div class="tipstext" id="addTips" style="display:none;"></div>'
            if (level == 3) {
                isColor = true;
                var colorCheckedfalse = thisLine.hasClass('bk_list on') ? '' : 'checked="checked"';
                var colorCheckedtrue = thisLine.hasClass('bk_list on') ? 'checked="checked"' : '';
                html += '<div class="bk_p mt12 clearfix"><span>标题标红</span>' +
                        '<div class="bk_radio"> ' +
                        '<input name="editItemColor" type="radio" checked="checked" value="false" ' + colorCheckedfalse + '><span>否</span>  ' +
                        '<input name="editItemColor" type="radio" class="bk_ml" value="true" ' + colorCheckedtrue + '><span>是</span></div></div>'
            }
            html += '</div>'
            var title = "";
            if (level == 1) {
                title = "一级";
            }
            else if (level == 2) {
                title = "二级";
            } else {
                title = "三级";
            }
            var confirmOption = {
                title:'修改' + title + '标题',
                text:'',
                html:html,
                submitFunction:function() {
                    var edittitle = $("#edit_title").val();
                    if (edittitle.length == 0) {
                        $("#addTips").text('请输入标题').show();
                        $("#three_title").focus();
                        return true;
                    }
                    if (level == 1) {
                        if (common.strLen(edittitle) > 15) {
                            $("#addTips").text('一级标题支持1~15汉字').show();
                            $("#edit_title").focus();
                            return true;
                        }
                    } else if (level == 2) {
                        if (common.strLen(edittitle) > 15) {
                            $("#addTips").text('二级标题支持1~15汉字').show();
                            $("#edit_title").focus();
                            return true;
                        }
                    } else {
                        if (common.strLen(edittitle) > 15) {
                            $("#addTips").text('三级标题支持1~15汉字').show();
                            $("#edit_title").focus();
                            return true;
                        }
                    }
                    var titleFlag = true;
                    if (isUrl) {
                        var editurl = $("#edit_url").val();
                        if (editurl.length == 0) {
                            $("#addTips").text('请输入超链接').show();
                            $("#edit_url").focus();
                            return true;
                        } else {
                            thisLine.children('b').find('a').attr('href', editurl)
                        }
                    }
                    if (titleFlag) {
                        if (thisLine.children('b').children('a').size() > 0) {
                            thisLine.children('b').children('a').text(edittitle);
                        } else {
                            thisLine.children('b').text(edittitle)
                        }
                    }
                    if (isColor) {
                        var edititemColor = $('input:checked[name=editItemColor]').val();
                        if (edititemColor == "true") {
                            thisLine.removeClass().addClass('bk_list on');
                        } else {
                            thisLine.removeClass().addClass('bk_list');
                        }
                    }
                    isModified = true;
                    return false;
                }
            }
            joymealert.prompt(confirmOption);
        });
        var isSubmiting=false;
        $("a[name=save_baike]").bind('click', function() {
            if(isSubmiting){
               return;
            }
            if (isModified) {
                isSubmiting=true;
                var baikeObj = []
                $("div[id^=baike_one_title_]").each(function(i, val) { //遍历每个一级标题
                    var oneItemDom = $(this).children('div:eq(0)');
                    var oneItem = i;
                    baikeObj[oneItem] = {}
                    baikeObj[oneItem].name = oneItemDom.find('b:first').text(); //一级分类名称
                    baikeObj[oneItem].url = "";
                    baikeObj[oneItem].itemId = "";
                    baikeObj[oneItem].icon = "";
                    baikeObj[oneItem].color = "";
                    baikeObj[oneItem].nodeType = 'branch'
                    baikeObj[oneItem]["children"] = []
                    if (oneItemDom.next().children('span').size() != 0) {
                        oneItemDom.next().children('span').each(function(i, val) {
                            var threeItem = i;
                            baikeObj[oneItem]["children"][threeItem] = {}
                            baikeObj[oneItem]["children"][threeItem].name = $(this).children('b').text();
                            baikeObj[oneItem]["children"][threeItem].url = $(this).children('b').find('a').attr('href');
                            baikeObj[oneItem]["children"][threeItem].itemId = "";
                            baikeObj[oneItem]["children"][threeItem].icon = ""
                            if (common.trimStr($(this).attr('class')) == "bk_list") {
                                baikeObj[oneItem]["children"][threeItem].color = "";
                            } else {
                                baikeObj[oneItem]["children"][threeItem].color = "#cc0000"
                            }
                            baikeObj[oneItem]["children"][threeItem].nodeType = "leaf"
                        })
                    } else {
                        oneItemDom.next().children('div').each(function(i, val) {
                            var twoItem = i;
                            baikeObj[oneItem]["children"][twoItem] = {}
                            baikeObj[oneItem]["children"][twoItem].name = $(this).children('div:eq(0)').children('h3').children('b').text();
                            baikeObj[oneItem]["children"][twoItem].url = ""
                            baikeObj[oneItem]["children"][twoItem].icon = ""
                            baikeObj[oneItem]["children"][twoItem].color = ""
                            baikeObj[oneItem]["children"][twoItem].nodeType = "branch"
                            baikeObj[oneItem]["children"][twoItem]["children"] = []
                            if ($(this).children('div:eq(1)').children('span').size() > 0) {
                                $(this).children('div:eq(1)').children('span').each(function(i, val) {
                                    var threeItem = i;
                                    baikeObj[oneItem]["children"][twoItem]["children"][threeItem] = {};
                                    baikeObj[oneItem]["children"][twoItem]["children"][threeItem].name = $(this).children('b').text();
                                    baikeObj[oneItem]["children"][twoItem]["children"][threeItem].url = $(this).children('b').find('a').attr('href');
                                    baikeObj[oneItem]["children"][twoItem]["children"][threeItem].icon = "";
                                    if (common.trimStr($(this).attr('class')) == "bk_list") {
                                        baikeObj[oneItem]["children"][twoItem]["children"][threeItem].color = "";
                                    } else {
                                        baikeObj[oneItem]["children"][twoItem]["children"][threeItem].color = "#cc0000"
                                    }
                                    baikeObj[oneItem]["children"][twoItem]["children"][threeItem].nodeType = "leaf"
                                })
                            }
                        })
                    }
                })
                $("#baikeObj").val(common.obj2Str(baikeObj));
                $("#form_edit").submit();
            } else {
                var confirmOption = {
                    title:'提示信息',
                    text:'没有修改,无法保存!'
                }
                joymealert.alert(confirmOption);
            }
        });
    };
})