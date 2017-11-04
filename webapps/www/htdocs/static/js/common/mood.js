/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-7-7
 * Time: 下午7:05
 * 表情相关的JS
 */

var cursorPosition = {
    get: function (textarea) {
        var rangeData = {text: "", start: 0, end: 0 };

        if (textarea.setSelectionRange) { // W3C
            textarea.focus();
            rangeData.start = textarea.selectionStart;
            rangeData.end = textarea.selectionEnd;
            rangeData.text = (rangeData.start != rangeData.end) ? textarea.value.substring(rangeData.start, rangeData.end) : "";
        } else if (document.selection) { // IE
            textarea.focus();
            var i,
                    oS = document.selection.createRange(),
                // Don't: oR = textarea.createTextRange()
                    oR = document.body.createTextRange();
            oR.moveToElementText(textarea);

            rangeData.text = oS.text;
            rangeData.bookmark = oS.getBookmark();

            // object.moveStart(sUnit [, iCount])
            // Return Value: Integer that returns the number of units moved.
            for (i = 0; oR.compareEndPoints('StartToStart', oS) < 0 && oS.moveStart("character", -1) !== 0; i ++) {
                // Why? You can alert(textarea.value.length)
                if (textarea.value.charAt(i) == '\r') {
                    i ++;
                }
            }
            rangeData.start = i;
            rangeData.end = rangeData.text.length + rangeData.start;
        }

        return rangeData;
    },
    set: function (textarea, rangeData) {
        var oR, start, end;
        textarea.focus();
        if (!rangeData) {
            alert("You must get cursor position first.")
        }
        if (textarea.setSelectionRange) { // W3C
            textarea.setSelectionRange(rangeData.start, rangeData.end);
        } else if (textarea.createTextRange) { // IE
            oR = textarea.createTextRange();

            // Fixbug : ues moveToBookmark()
            // In IE, if cursor position at the end of textarea, the set function don't work
            if (textarea.value.length === rangeData.start) {
                //alert('hello')
                oR.collapse(false);
                oR.select();
            } else {
                oR.moveToBookmark(rangeData.bookmark);
                oR.select();
            }
        }
    },
    add: function (textarea, rangeData, text) {
        var oValue, nValue, oR, sR, nStart, nEnd, st;
        this.set(textarea, rangeData);

        if (textarea.setSelectionRange) { // W3C
            oValue = textarea.value;
            nValue = oValue.substring(0, rangeData.start) + text + oValue.substring(rangeData.end);
            nStart = nEnd = rangeData.start + text.length;
            st = textarea.scrollTop;
            textarea.value = nValue;
            // Fixbug:
            // After textarea.values = nValue, scrollTop value to 0
            if (textarea.scrollTop != st) {
                textarea.scrollTop = st;
            }
            textarea.setSelectionRange(nStart, nEnd);
        } else if (textarea.createTextRange) { // IE
            sR = document.selection.createRange();
            sR.text = text;
            sR.setEndPoint('StartToEnd', sR);
            sR.select();
        }
    }
}
var rangeJoyData;
//展示表情层方法,
//textarea的ID(用来记录range),要显示的face层
function toggleMessageMood(textareaId, faceshow) {
    rangeJoyData = cursorPosition.get(document.getElementById(textareaId));
    $("#" + faceshow).slideToggle("fast");
}
function toggleMessageHome() {
    rangeJoyData = cursorPosition.get(document.getElementById('chat_content'));
    $(".reldiv").css("display", "none");
    $(".rel_face").toggle();
}

function toggleChatMood() {
    $("#chat_face").slideToggle("fast");
}

function toggleTxtMood() {
    $("#txt_face").slideToggle("fast");
}

function toggleMoodById(id) {
    $("#" + id).slideToggle("fast");
}

function fillMood(fillAreaId, moody) {
    var s = cursorPosition.get(document.getElementById(fillAreaId)).start;
    var str = $('#' + fillAreaId).val().substring(0, s);
    str += '[' + moody + ']';
    str += $('#' + fillAreaId).val().substring(s);
    $('#' + fillAreaId).val(str);
    $("#chat_face").slideToggle("fast");
}
function fillMoodClass(fillAreaId, moody) {
    var s = cursorPosition.get(document.getElementById(fillAreaId)).start;
    var str = $('#' + fillAreaId).val().substring(0, s);
    str += '[' + moody + ']';
    str += $('#' + fillAreaId).val().substring(s);
    $('#' + fillAreaId).val(str);
    $(".face_div").slideToggle("fast");
}
function fillMoodByDiv(moodDiv, fillAreaId, moody) {
    rangeJoyData = cursorPosition.get(document.getElementById(fillAreaId));
    var str = '[' + moody + ']';
    cursorPosition.add(document.getElementById(fillAreaId), rangeJoyData, str);
    // $('#' + fillAreaId).val(str);
    //$("#" + moodDiv).slideToggle("fast");
}

function fillMoodToCkeditor(ckeditor, moody) {
    var str = '[' + moody + ']';
    ckeditor.insertHtml(str);
    //$("#face_div").slideToggle("fast");
}

function fillMoodToComment(feedbackid, moody) {
    var s = cursorPosition.get(document.getElementById('textarea_' + feedbackid)).start;
    var str = $('#textarea_' + feedbackid).val().substring(0, s);
    str += '[' + moody + ']';
    str += $('#textarea_' + feedbackid).val().substring(s);
    $('#textarea_' + feedbackid).val(str);
    $("#face_div_" + feedbackid).slideToggle("fast");
}
/*
 * 显示表情层方法(参数一:表情显示按钮,参数二:要填充表情的输入框的ID)
 * */
function displayFaceMethod(container, textareaID) {
    $(".reldiv").hide();
    var faceDivOffset = $(container).offset();
    var faceStr = '';
    if ($("#face_div").length > 0) {
        $("#face_div").css({top:faceDivOffset.top + 24 + "px",left:faceDivOffset.left + "px"});
        $("#face_div").show();
    } else {
        faceStr = '<div id="face_div" class="rel_face reldiv"><table border="0" cellpadding="0" cellspacing="0" class="div_warp" >' +
                '<tbody><tr><td class="top_l"></td><td class="top_c"></td><td class="top_r"></td></tr>' +
                '<tr><td class="mid_l"></td><td class="mid_c"><div class="p_face" id="face_box">' +
                '<div class="cont_t" id="face_title_box"><span id="face_title">潘斯特表情</span><a href="javascript:void(0)" id="faceHide" class="x" onclick="$(\'#face_div\').hide();"></a></div>' +
                '<div class="p_face_cont clearfix"><div class="face_con clearfix" id="face_button"><img src="../static/theme/default/img/wait.gif" style="border: none; margin: 30px auto;" />' +
                '</div><div class="p_summuray" style="clear:both;width:100%;" id="face_footer">潘斯特是个充满弹性的小外星人，耳朵其实是触手。</div></div></div></td><td class="mid_r"></td></tr>' +
                '<tr><td class="bottom_l"></td><td class="bottom_c"></td><td class="bottom_r"></td></tr>' +
                '</tbody></table></div>';//构建表情层
        var face_container = "";//构建表情按钮
        $.ajax({
                    type: "POST",
                    url: "/json/mood",
                    success:function(data) {
                        var jsonObj = eval('(' + data + ')');
                        if (jsonObj.status_code == "1") {
                            var list = jsonObj.result;
                            $.each(list, function(i, val) {
                                //face_container += '<a href="javascript:void(0)" title="' + val.code + '" onclick="fillMoodByDiv(\'' + $("#" + textareaID).attr("id") + '\',\'' + $("#" + container).attr("id") + '\', \'' + val.code + '\');"><img src="' + 1 + '" /></a>';
                                face_container += '<a href="javascript:void(0)" title="' + val.code + '"><img src="' + URL_LIB + val.imgUrl + '" /></a>';
                            })
                            //code,imgUrl,imgClass
                        } else {
                            face_container = "网络忙,暂时找不到表情!";
                        }
                        $("#face_button").html(face_container);
                    }
                })

        $("body").append(faceStr);
        $("#face_div").css({top:faceDivOffset.top + 24 + "px",left:faceDivOffset.left + "px"});
        $("#face_div").show();
    }

}
function docFaceBind(showBtn, textareaID) {
    if (!$("#face_div").is(":hidden")) {
        $("#face_div").hide();
    }
    $(document).bind('click', function(e) {
        var e = e || window.event;
        var target = e.target || e.srcElement;
        if (target.id === $(showBtn).attr("id")) {
            displayFaceMethod(showBtn, textareaID);
        } else if (target.id !== "face_box" && target.id !== "face_box" && target.id !== "face_title_box" && target.id !== "face_title" && target.id !== "face_footer" && target.id !== "face_button") {
            if (!$("#face_div").is(":hidden")) {
                $("#face_div").hide();
                $(this).unbind('click');
            } else {
                $("#face_div").hide();

            }
        }
        $("#face_button a").die();//每次都先清除表情点击方法,
        $("#face_button a").live('click', function() {//然后再注册上
            fillMoodByDiv('face_div', textareaID, $(this).attr("title"))
            $("#face_button a").die();
        })
    });
}