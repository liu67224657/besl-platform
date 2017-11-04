define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.form');
    require('../common/tips');
    var common = require('../common/common');
    var tagbiz = require('../biz/tag-biz');
    var ajaxverify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');
    var opts = {};
    var showOption = {tid:'',isShow:true};

    var oldtagstr = '';//用于存储当前tags_input的值
//    var tagId = 0;
    var alertOption = {};
    var tag = {
        //检查标签输入是否为","号
        checkTagssplit:function (taginputid, event, postform) {
            if (!event.shiftKey) {
                switch (event.which) {
                    case 188:
                        var tagname = oldtagstr;
                        spliteTagByform(taginputid, tagname, postform);
                        break;
                    case 13:
                        var tagname = $("#tags_input_" + taginputid).val();
                        spliteTagByform(taginputid, tagname, postform);
                        break;
                    default:
                        oldtagstr = $("#tags_input_" + taginputid).val();
                        break;
                }
            }
            event.keyCode = 0;
            event.returnValue = false;
            event.cancelBubble = true;
        },
        //添加标签
        saveTag:function(form_tag) {
            //判断标签是否为空
            var flag = this.checkPostTags(form_tag);
            if (flag) {
                $('#form_tag').ajaxForm(function(data) {
                    var jsonObj = eval('(' + data + ')');
                    common.locationLoginByJsonObj(jsonObj);
                    if (jsonObj.status_code == "1") {
                        var str = '';
                        for (var i = 0; i < jsonObj.result.length; i++) {
                            str = '<li>' +
                                    '<a href="javascript:void(0)" name="tagLink">' + jsonObj.result[i].tag + '</a>' +
                                    '<a href="javascript:void(0)" title="删除标签" name="tagdel" id="' + jsonObj.result[i].tagId + '" tag="' + jsonObj.result[i].tag + '">×</a>' +
                                    '</li>';
                            $("#favoritetag").prepend(str);
                        }
                        $('#tags_editor_add span').remove();

                        $("input[type='hidden'][name='tags']").remove();
//                        $.each($("input[type='hidden'][name='tags']"), function(i, val) {
//                            this.remove();
//                        });

                        $("#tags_input_add").val('');
                    } else {
                        alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
                        joymealert.alert(alertOption);
                    }
                });
                $('#form_tag').submit(); //表单提交。
            }
        },
        //submit时验证标签输入
        checkPostTags:function (form_tag) {
            var taginputs = $("#" + form_tag).find("input[name=tags]");
            var flag = true;
            $.each(taginputs, function(i, val) {
                if (common.strLen(val.value) > tipsText.tag.max_length) {
                    alertOption = {text:tipsText.tag.max_length_error,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                    flag = false;
                    return false;
                }
                //非法词条
                var verify = ajaxverify.verifyPost(val.value);
                if (!verify) {
                    alertOption = {text:tipsText.tag.tag_illegl,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                    flag = false;
                    return false;
                }

            });
            if (flag) {
                return true;
            }
        },
        initTagItem:function(taginputid, tagId, tagname, postform) {
            tagname = tag.trimTag(tagname);
            var _this = this;
            var tagsinputs = $("#" + postform + " input[name=tags]");
            if (tagsinputs.length > tipsText.tag.max_num) {
                $("#tags_input_" + taginputid).val('');
                return;
            }

            //排重
            if (!this.matchTags(postform, tagname)) {
                var tagHidden = '<input name="tags" id="tag_' + tagId + '" type="hidden" value="' + tagname + '"/>';
                $("#" + postform).append(tagHidden);
            }

            if (!this.matchSpanTags(taginputid, tagname)) {
                var spanItem = '<span class="item" id="' + tagId + '">' + tagname +
                        '<a href="javascript:void(0)" id="close_tagid_' + tagId + '" title="删除">×</a></span>';
                $('#close_tagid_' + tagId).die().live('click', function() {
                    $(this).parent().remove();
                    _this.delSpanItem(tagId, taginputid);
                });
                $("#tags_input_" + taginputid).before(spanItem);
                oldtagstr = '';
            }

        },
        getTagFocus:function (val) {
            $('#tags_input_' + val).focus();
            this.showTagTips(val, opts, showOption);
        },
        hideTips: function (tagtipsid) {
            var tagOpt = getTagTips(tagtipsid, opts, showOption);
            setTimeout(function() {
                $("#tags_tips_" + tagOpt.tid).hide();
            }, 200);
        },
        delSpanItem:function (spanItemid, tags_editor_id) {
            $("#tags_editor_" + tags_editor_id + " span[id=" + spanItemid + "]").remove();
            resetTags_tips(tags_editor_id);
            $("#tags_input_" + tags_editor_id).focus();
            this.showTagTips(tags_editor_id, opts, showOption);
            $("#div_post_" + tags_editor_id + " input[id=tag_" + spanItemid + "]").remove();
            if($("#editforwardform").length>0){
                $("#editforwardform input[id=tag_" + spanItemid + "]").remove();
            }
        },

        clearTagItem:function(tags_editor_id,formid) {
            $("#tags_input_box_" + tags_editor_id + ">span").remove();
            $("#" + formid + " input[name=tags]").remove();
        },
        matchTags:function(formName, tagname) {
            var result = false;
            $.each($("#" + formName + " input[name=tags]"), function(i, val) {
                if (val.value != null && val.value.length != 0 && val.value == tagname) {
                    result = true;
                    return result;
                }
            })
            return result;
        },
        matchSpanTags:function(tags_editor_id, tagname) {
            var result = false;
            $.each($("#tags_editor_" + tags_editor_id + " .item"), function(i, val) {
                var tagText = val.childNodes[0].nodeValue;
                if (tagText == tagname) {
                    result = true;
                    return result;
                }
            })
            return result;
        },

        //过滤通配符
        trimTag:function(str) {
            str = this.trimStr(str);
            return str.replace(/[%'\\*\/_()#",:.]/g, "");
        },
        //过滤所有空格
        trimStr:function(str) {
            return str.replace(/\s+/g, "");
        },
        //删除标签：数据库
        delTag:function(tagid, tagname, callback) {
            tagbiz.delTag(tagid, tagname, callback);
        },
        //收藏标签
        likeTag:function(tagname, callback) {
            tagbiz.likeTag(tagname, callback);
        },
        showTagTips: function (tagtipsid, opts, showOption) {
            this.showTips(tagtipsid, opts, showOption);
        },
        showTips:function (tagtipsid, opts, showOption) {
            var flag = false;
            var postbiz = require('../biz/post-biz');
            if (!($("#tags_tips_" + tagtipsid + " div:eq(1) ul li").size() > 0)) {
                flag = true
            }
            postbiz.searchTag(showOption, tagtipsid, this.showintegTips, flag)

        } ,
        showintegTips:function(msg, tagtipsid, showOption) {
            var liStr = "";
            if (msg) {
                var list = msg.result;
                $.each(list, function(i, val) {
                    liStr += '<li id="' + val.tagId + '">' + val.tag + '</a>'
                })
            }
            $("#tags_tips_" + tagtipsid + " div:eq(1) ul").append(liStr);
            resetTags_tips(tagtipsid);
            $("#tags_tips_" + tagtipsid).toggle();
        },
        setCommonTags : function (tagSpanid, tagName, tags_editor_id, postform) {
            this.initTagItem(tags_editor_id, tagSpanid, tagName, postform)
            resetTags_tips(tags_editor_id);
            this.showTagTips(tags_editor_id, showOption);
        }

    }
    var resetTags_tips = function (tags_tips_id, dom) {
        $("#tags_tips_" + tags_tips_id).css({"top":  48 + $("#tags_input_box_" + tags_tips_id).height() + "px","left":"10px"})
    }
    var getTagTips = function(tagtipsid, opts, showOption) {
        if (opts[tagtipsid] == null) {
            var tagOption = $.extend({}, showOption, {tid:tagtipsid});
            opts[tagtipsid] = tagOption;
        }
        return opts[tagtipsid];
    }

    var spliteTagByform = function(taginputid, tagname, postform) {

        tagname = tag.trimTag(tagname);

        if (common.strLen(tagname) > tipsText.tag.max_length) {
            alertOption = {text:tipsText.tag.max_length_error,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return;
        }
        if (tagname.length > 0) {
            var tagsinputs = $("#"+postform+" input[name=tags]");
            if (tagsinputs.length >= tipsText.tag.max_num) {
                alertOption = {text:tipsText.tag.max_num_error,tipLayer:false,textClass:"tipstext"};
                joymealert.alert(alertOption);
                $("#tags_input_" + taginputid).val('');
                return;
            }

            if (tagname.length > 0) {
                var tagId = $('#' + postform + ' input[name=tags]').size();
                $("#tags_input_" + taginputid).val('');
                tag.initTagItem(taginputid, tagId, tagname, postform);
            } else {
                $("#tags_input_" + taginputid).val('');
            }
        } else {
            $("#tags_input_" + taginputid).val('');
            return;
        }
    }
    return tag;
});
