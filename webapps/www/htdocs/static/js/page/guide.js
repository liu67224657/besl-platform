define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var joymealert = require('../common/joymealert');
    require('../common/tips');

    var guide = {
        closeRecommondInit:function() {
            $("#closeRecommend").live("click", function() {
                $(this).parent().parent().remove();
            });
        },
        guideFocusInit:function() {
            var _this = this;
            if ($("#guideFocus").length != 0) {
                $("#guideFocus").live("click", function() {
                    if (_this.checkSelect()) {
                        $("#guideForm").submit();
                    } else {
                        joymealert.alert({text:tipsText.joyfocus.focus_uno_not_null,tipLayer:true,textClass:"tipstext"});
                        return false;
                    }
                });
            }
        },
        checkSelect:function () {
            var flag = false;
            $("input[type=checkbox]").each(function(i, val) {
                if (val.checked) {
                    flag = true;
                }
            });
            return flag;
        }
    }
    return guide;
});