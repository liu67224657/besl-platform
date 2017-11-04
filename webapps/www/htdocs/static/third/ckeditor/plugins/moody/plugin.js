/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-7-25
 * Time: 下午10:15
 * add jmh mood js
 */
(function() {
    //Section 1 : 按下自定义按钮时执行的代码
    var a = {
        exec:function(editor) {
            displayCkFaceMethod(editor);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'moody';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('moody', {
                                title:'着迷表情',
                                icon: this.path + '/face_icon.jpg',
                                command:b
                            });
                }
            });
})();

function displayCkFaceMethod(editor) {
    faceDivOffset = $(".cke_icon").eq(2).offset();
    $(".reldiv").hide();
    var faceStr = '';
    if ($("#face_div_ck").length > 0) {
        $("#face_div_ck").css({top:faceDivOffset.top + 24 + "px",left:faceDivOffset.left + "px"});
        $("#face_div_ck").show();
    } else {
        faceStr = '<div id="face_div_ck" class="rel_face reldiv"><table border="0" cellpadding="0" cellspacing="0" class="div_warp" >' +
                '<tbody><tr><td class="top_l"></td><td class="top_c"></td><td class="top_r"></td></tr>' +
                '<tr><td class="mid_l"></td><td class="mid_c"><div class="p_face" id="face_box_ck">' +
                '<div class="cont_t" id="face_title_box_ck"><span id="face_title_ck">潘斯特表情</span><a href="javascript:void(0)" id="faceHide_ck" class="x" onclick="$(\'#face_div_ck\').hide();"></a></div>' +
                '<div class="p_face_cont clearfix"><div class="face_con clearfix" id="face_button_ck"><img src="../static/default/img/wait.gif" style="border: none; margin: 30px auto;" />' +
                '</div><div class="p_summuray" style="clear:both;width:100%;" id="face_footer_ck">潘斯特是个充满弹性的小外星人，耳朵其实是触手。</div></div></div></td><td class="mid_r"></td></tr>' +
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
                                face_container += '<a href="javascript:void(0)" title="' + val.code + '"><img src="' + URL_LIB + val.imgUrl + '" /></a>';
                            })
                        } else {
                            face_container = "网络忙,暂时找不到表情!";
                        }
                        $("#face_button_ck").html(face_container);
                    }
                })

        $("body").append(faceStr);
        $("#face_div_ck").css({top:faceDivOffset.top + 24 + "px",left:faceDivOffset.left + "px"});
        $("#face_div_ck").show();
    }
    $("#face_button_ck a").die();
    $("#face_button_ck a").live('click', function() {//然后再注册上
        fillMoodToCkeditor(editor, $(this).attr("title"))
        $("#face_button_ck a").die();
    })
    $(document).bind('click', function(e) {
        var e = e || window.event;
        var target = e.target || e.srcElement;
        if (target.className === "cke_icon") {

        } else {
            if ( target.id !== "face_box_ck" && target.id !== "face_box_ck" && target.id !== "face_title_box_ck" && target.id !== "face_title_ck" && target.id !== "face_footer_ck" && target.id !== "face_button_ck") {
                if (!$("#face_div_ck").is(":hidden")) {
                    $("#face_div_ck").hide();
                   // $(this).unbind('click');
                }else{
                    $("#face_div_ck").hide();
                }
            }
        }
    });
}
