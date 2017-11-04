/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-4
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var moodBiz = require('../biz/mood-biz');
    var common = require('../common/common')
    var moodPopConfig = {
        allowmultiple:true,
        isremovepop:true,
        isfocus:true
    };
    var mood = {
        bindMood:function(moodId, inputId, config) {
            var te = new TextareaEditor(document.getElementById(inputId));
            $('#' + moodId).die().live('click', function() {
                var config = {
                    allowmultiple:true,
                    isremovepop:true,
                    isfocus:false
                };
                moodBiz.docFaceBindOnReply($(this), inputId, config, te);
            });
        },

        outCloseMood:function() {
            //焦点失去隐藏判断
            $("body").live('mousedown', function() {
                if (window.isOut) {
                    moodBiz.hideFace();
                    window.isOut = true;
                }
            });
        }
    }

    return mood;

});