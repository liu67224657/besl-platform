/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-8
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */

define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postbiz = require('../biz/post-biz');
    var rangeJoyData;
    var postat = {
        searchAtFocus:function (jqueryObj, divid) {
            var val = jqueryObj.val();
            if (val != null && val.length > 0) {
                jqueryObj.next().find('input').hide();
                jqueryObj.next().find('a').show();
            } else {
                jqueryObj.next().find('input').show();
                jqueryObj.next().find('a').hide();
            }
            if (val != null && val.length > 0) {
                this.ajaxInitAtDiv(val, divid);
            }
        },
        ajaxInitAtDiv : function (val, divid) {
            getFollowList(val, divid, null, intregpostat);
        },
        fillAtFocusToInput:function (inputid, screenName) {
            if ($('#' + inputid).val().indexOf("@" + screenName) == -1) {
                var common = require('../common/common');
                rangeJoyData = common.cursorPosition.get(document.getElementById(inputid));
                var str = "@" + screenName + " ";
                common.cursorPosition.add(document.getElementById(inputid), rangeJoyData, str);
//                checkModel(140, inputid);
            }
        },
        //点击要@的好友，插入editor
        fillAtFocusToEditor:function (ckeditor, screenName) {
            if (ckeditor.getData().indexOf('@' + screenName) == -1) {
                var str = '@' + screenName + ' ';
                ckeditor.focus();
                ckeditor.insertHtml(str);
            }

        },
        getAtFriendList :function(divid) {
            getFollowList('', divid, null, getAtFriendList)
        }
    }

    var getFollowList = function(searchWord, divid, loadingCallback, callback) {
        postbiz.searchAt(searchWord, divid, loadingCallback, callback);
    }


    var getAtFriendList = function(jsonObj, divid) {
        var atStr = '';
        if (jsonObj.status_code == '1') {
            atStr = '<div class="atsearchcon"><input id="at_input_'+divid+'" class="attext" type="text" value="" name="" style="color: rgb(91, 91, 91);">' +
                    '<span class="atbtn">' +
                    '<a id="at_input_remove_'+divid+'" class="close" href="javascript:void(0)" title="清空" style="display: none;"></a>' +
                    '</span>' +
                    '</div>' +
                    '<div id="'+divid+'Atul" class="atsearch"><ul>';
            var list = jsonObj.result;
            $.each(list, function(i, val) {
                var des = '';
                if (val.description != null && val.description.length > 1) {
                    des = '(' + val.description + ')';
                }
                atStr += '<li><a href="javascript:void(0)" title="' + val.screenName + '" name="atFocus' + divid + '">' + val.screenName + des + '</a></li>';
            });
            atStr += '<ul></div><p class="tipstext"></p></div>';
        } else {
            atStr += '<div class="friendloaderror">读取失败，请<a href="javascript:void(0)"  id="at_reload_' + divid + '">重试</a></div>';
            $('#at_reload_' + divid).die().live('click', function() {
                getFollowList('', divid,reloadCallback,getAtFriendList);
            });
        }

        $('#atfriend_area_' + divid).html(atStr);
    }

    var reloadCallback = function(divid) {
       $('#atfriend_area_' + divid).html('<div class="friendcon" id="atfriend_area_Text"><div class="friendload"><p>读取中，请稍候...</p></div></div>');
    }

    var intregpostat = function(jsonObj, divid) {
        if (jsonObj.status_code == '1') {
            $('#' + divid + 'Atul').html('');
            var list = jsonObj.result;
            var atStr = '<ul>';
            $.each(list, function(i, val) {
                var des = '';
                if (val.description != null && val.description.length > 1) {
                    des = '(' + val.description + ')';
                }
                atStr += '<li id="' + i + '"><a href="javascript:void(0)" title="' + val.screenName + '" name="atFocus' + divid + '">' + val.screenName + des + '</a></li>';
            });
            atStr += '</ul>'
            $('#' + divid + 'Atul').append(atStr)
        }
    }

    return postat;
});