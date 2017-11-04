/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../../third/ui/jquery.ui.min')($);
    var header = require('../page/header');
    var recommend = require('../page/recommenduser');
    var ajaxverify = require('../biz/ajaxverify');
    var vote = require('../page/vote');
    var joymealert = require('../common/joymealert');
    var contentoperate = require('../page/content-operate');
    var game = require('../page/game');
    require('../../third/ui/jquery.ui.datepicker')($);

    window.memoContent = true;
    var windowTop = document.body.scrollTop + document.documentElement.scrollTop;

    $().ready(function() { //lazylaod在谷歌下跳位置的处理
        header.noticeSearchReTopInit();
        game.followGroup();
        $("#set_tx_div").live('mouseenter mouseleave', function (event) {
            if (event.type == "mouseenter") {
                $("#txbg").show();
                $("#set_tx").show();
            } else if (event.type == "mouseleave") {
                $("#txbg").hide();
                $("#set_tx").hide();
            }
        });
        $("#set_tx,#txbg").click(function() {
            window.location.href = "/profile/customize/headicon";
        })

        var chrome = window.navigator.userAgent.indexOf("Chrome") >= 0 ? true : false;
        if (chrome) {
            window.scrollTo(0, windowTop);
        }

        //subject
        $('#subject').live('keyup keydown', function(event) {
            vote.checkVoteInputLength($(this),tipsText.vote.vote_subject_input_length);
        });

        //description
        $('#description').live('keyup keydown', function(event) {
            vote.checkVoteInputLength($(this),tipsText.vote.vote_description_input_length);
        });

        //option
        $('input[name=option]').die().live('keyup keydown', function(event) {
            vote.checkVoteInputLength($(this),tipsText.vote.vote_option_input_length);
        });

        //.install image
        contentoperate.postVoteLive();

        // add option
        $("#addOption").click(function(){
            if($("input[name=option]").length<tipsText.vote.vote_option_max_size){
                $("#addOption").parent().before('<li name="optionli"><input type="text" class="input-text" name="option"><a class="close" href="javascript:void(0)" name="removeOption" style="display: none"></a></li>');
                vote.appendOptionClose();
            }
            if($("input[name=option]").length>=tipsText.vote.vote_option_max_size) {
                $("#addOption").parent().hide();
            }
        });

        $("a[name=removeOption]").live("click",function(){
            if($("input[name=option]").length>tipsText.vote.vote_option_limit_size){
                $(this).parent().remove();
            }

            if($("input[name=option]").length<tipsText.vote.vote_option_max_size){
                $("#addOption").parent().show();
            }
            vote.appendOptionClose();
            vote.initOptionSelect();
        });

        $("#addDescription").click(function(){
            if($("#des_li").is(":hidden")){
                $(this).text('-投票说明');
                $("#des_li").show();
            }else{
                $(this).text('+投票说明');
                $("#des_li").hide();
            }
        });

        //高级选项
        $("#advancedset").click(function(){
            if($("#advancedul").is(":hidden")){
                $("#advancedul").show();
            } else {
                $("#advancedul").hide();
            }
        });

        $("#expiredselect").change(function(){
            if($(this).children('option:selected').val()=='def'){
                $("#defExpired").show();
                vote.setExpiredDate(1);
            }else{
                $("#defExpired").hide();
            }
        });

        //时间控件
        $('#dateinput').datepicker();

        $("#postvote").live("click",function() {
            vote.postVote();
        });
    });

    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../common/google-statistics');
    require.async('../common/bdhm');

});
