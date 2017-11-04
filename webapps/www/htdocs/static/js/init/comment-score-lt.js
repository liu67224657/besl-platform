$(function () {
    var wap_wrap = document.getElementById('wap_wrap');
    var wap_float = document.getElementById('wap_float');
    var wap_center = document.getElementById('wx_center');
    var wap_floatBtn = document.getElementById('wap_floatBtn');
    var wap_bg = document.getElementById('wap_bg');
    $('#wap_floatBtn').click(function () {
        if (wap_center.className == 'wx_center' || wap_center.className == '') {
            wap_center.className = 'wx_center icon_open ';
            this.className = 'deg';
            wap_bg.className = 'opacity';
            wap_bg.style.zIndex = 96;
            wap_bg.style.display = 'block';
        } else {
            wap_center.className = '';
            this.className = '';
            wap_bg.className = '';
            wap_bg.style.zIndex = 0;
            wap_bg.style.display = 'none';
        }
    });
    grade();
});
//20141111新增评分页面
function grade() {
    var grade = true;
    var plNum = $('.pf_user b').text();
    $('.grade_icon cite').click(function () {
        $(this).addClass('on').prevAll('cite').addClass('on');
        $(this).nextAll('cite').removeClass('on');
        if (!$('.grade_icon cite:first-child').hasClass('on')) {
            $('.grade_icon cite:first-child').removeClass('on');
        } else {
            $('.grade_icon cite:first-child').addClass('on');
        }
    });
    $('.pf_btn').click(function () {
        //			plNum=parseInt(plNum)+1;
        //			$('.pf_user b').text(plNum);
        //			if(grade){
        //			   $('.pf_btn').text("修改");
        //			   $('#opacity_bg,.pf_tips').show();
        //			   $('#opacity_bg').css({
        //				   'z-index':'20'
        //				   });
        //			   $('.pf_tips').css({
        //				   'z-index':'21'
        //				   })
        //			   grade=false;
        //			}else{
        //			   $('.pf_btn').text("评分");
        //			   grade=true;
        //			};
    });
    $('.pf_close').click(function () {
        $('#opacity_bg,.pf_tips').hide();
        $('#opacity_bg,.pf_tips').css({
            'z-index': '9'
        });
    });
    var varNum = 0;
    $('.pic_comment span').click(function () {
        var countNum = $(this).children('code').text();
        varNum = parseInt(countNum) + 1
        $(this).children('code').text(varNum);
    })
}


