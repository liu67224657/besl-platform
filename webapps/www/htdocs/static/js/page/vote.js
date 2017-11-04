define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    require('../common/tips');
    var joymealert = require('../common/joymealert');
    var joymeform = require('../common/joymeform');

    window.postVoteLock = false;

    var vote = {
        voteEventLive:function() {
            $('a[name=prePartVote]').die().live('click', function() {
                vote.prePartVote($(this));
            });

            $('a[name=closePoll]').die().live('click', function() {
                vote.closePoll($(this));
            });

            $('a[name=submit_poll]').die().live('click', function() {
                vote.partVote($(this));
            });

            $('li[name=licurrent]').live('mouseenter',
                    function() {
                        $(this).addClass('current');
                    }).live('mouseleave', function() {
                        $(this).removeClass('current');
                    });

        },
        voteEventLiveOnBlog:function() {
            $('a[name=submit_poll]').die().live('click', function() {
                vote.parVoteOnBlog($(this));
            });

            $('li[name=licurrent]').live('mouseenter',
                    function() {
                        $(this).addClass('current');
                    }).live('mouseleave', function() {
                        $(this).removeClass('current');
                    });
        },
        checkVoteInputLength:function (inputObj, num) {
            var inputNum = common.getInputLength($.trim(inputObj.val()));
            inputNum = !inputNum ? 0 : inputNum;
            if (inputNum > num) {
                inputObj.parent().find(".error").remove();
                inputObj.parent().append('<div class="error">已超出' + (Math.ceil(inputNum) - num) + '个字，最多可输入' + num + '个字</div>');
            } else {
                inputObj.parent().find(".error").remove();
            }
            //init option select
            vote.initOptionSelect();
        },
        appendOptionClose:function () {
            if ($("input[name=option]").length > tipsText.vote.vote_option_limit_size) {
                $("a[name=removeOption]").show();
            } else {
                $("a[name=removeOption]").hide();
            }
        },
        setExpiredDate:function(AddDayCount) {
            var dd = new Date();
            dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
            var y = dd.getFullYear();
            var m = (dd.getMonth() + 1) < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;//获取当前月份的日期
            var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
            var h = dd.getHours() < 10 ? "0" + dd.getHours() : dd.getHours();
            var min = dd.getMinutes() < 10 ? "0" + dd.getMinutes() : dd.getMinutes();

            $("#dateinput").val(y + "-" + m + "-" + d);

            $("select[name=expiredhour] option").each(function() {
                if ($(this).val() == h) {
                    $(this).attr("selected", true);
                }
            });

            $("select[name=expiredminite] option").each(function() {
                if ($(this).val() == min) {
                    $(this).attr("selected", true);
                }
            });
        },
        postVote:function () {
            if (checkSubject() && checkDescription() && checkOptions() && checkExpiredDate()) {
                postVote();
            }
        },
        prePartVote:function (jqObj) {
            loadPreviewVote(jqObj.attr("data-sid"), jqObj.attr("data-did"), jqObj.attr("data-duno"), jqObj.attr("data-domid"), beforePreviewVote, loadPreviewVoteCallback);
        },
        closePoll:function(jqObj) {
            jqObj.parent().remove();
            $('#' + jqObj.attr("data-domid")).show();
        },
        partVote:function(jqObj) {
            var sync = false;
            if (jqObj.next().find('input').is(':checked')) {
                sync = true;
            }
            partVoteSubmit(jqObj.attr("data-domid"), jqObj.attr("data-did"), jqObj.attr("data-duno"), jqObj.attr("data-sid"), jqObj.attr("data-choicenum"), sync, 4, beforePartVoteBiz, partVoteBizCallback);
        },
        parVoteOnBlog:function(jqObj) {
            var sync = false;
            if (jqObj.next().find('input').is(':checked')) {
                sync = true;
            }
            partVoteSubmit(jqObj.attr("data-domid"), jqObj.attr("data-did"), jqObj.attr("data-duno"), jqObj.attr("data-sid"), jqObj.attr("data-choicenum"), sync, -1, beforePartVoteBizOnBlog, partVoteBizCallbackOnBlog);
        },
        initOptionSelect:function() {
            var index = 0;
            $("input[name=option]").each(function (i, val) {
                if ($.trim(val.value).length > 0) {
                    index++;
                }
            });
            var optionStr = '<option value="1">单选</option>';
            for (var i = 1; i < index; i++) {
                optionStr += '<option value="' + (i + 1) + '">最多选' + (i + 1) + '项</option>';
            }
            $("#choicenum").html(optionStr);
        }

    }

    var checkSubject = function () {
        if ($.trim($("#subject").val()).length == 0) {
            if ($("#subject").parent().find(".error").length > 0) {
                $("#subject").parent().find(".error").html(tipsText.vote.vote_subject_empty);
            } else {
                $("#subject").parent().append('<div class="error">' + tipsText.vote.vote_subject_empty + '</div>');
            }

            return false
        }
        if (common.getInputLength($.trim($("#subject").val())) > tipsText.vote.vote_subject_input_length) {
            if ($("#subject").parent().find(".error").length > 0) {
                $("#subject").parent().find(".error").html(tipsText.vote.vote_subject_max_length);
            } else {
                $("#subject").parent().append('<div class="error">' + tipsText.vote.vote_subject_max_length + '</div>');
            }
            return false
        }

        var result = ajaxverify.verifySysWord($("#subject").val());
        if (!result) {
            if ($("#subject").parent().find(".error").length > 0) {
                $("#subject").parent().find(".error").html(tipsText.vote.vote_subject_illegal);
            } else {
                $("#subject").parent().append('<div class="error">' + tipsText.vote.vote_subject_illegal + '</div>');
            }
            return result;
        }
        return true;
    }

    var checkDescription = function () {
        if ($.trim($("#description").val()).length > 0) {
            if (common.getInputLength($.trim($("#description").val())) > tipsText.vote.vote_description_input_length) {
                if ($("#des_li").is(":hidden")) {
                    $("#addDescription").trigger('click');
                }
                if ($("#description").parent().find(".error").length > 0) {
                    $("#description").parent().find(".error").html(tipsText.vote.vote_description_max_length);
                } else {
                    $("#description").parent().append('<div class="error">' + tipsText.vote.vote_description_max_length + '</div>');
                }
                return false
            }

            var result = ajaxverify.verifySysWord($("#description").val());
            if (!result) {
                if ($("#des_li").is(":hidden")) {
                    $("#addDescription").trigger('click');
                }
                if ($("#description").parent().find(".error").length > 0) {
                    $("#description").parent().find(".error").html(tipsText.vote.vote_description_illegal);
                } else {
                    $("#description").parent().append('<div class="error">' + tipsText.vote.vote_description_illegal + '</div>');
                }
                return result;
            }
        }
        return true;
    }

    var checkOptions = function () {
        var optionNum = 0;
        var flag = true;
        $("input[name=option]").each(function (i, val) {
            if ($.trim(val.value).length > 0) {
                optionNum++;
                var result = ajaxverify.verifySysWord(val.value);
                if (!result) {
                    if ($(this).parent().find(".error").length > 0) {
                        $(this).parent().find(".error").html(tipsText.vote.vote_option_option_illegal);
                    } else {
                        $(this).parent().append('<div class="error">' + tipsText.vote.vote_option_option_illegal + '</div>');
                    }
                    flag = result;
                }

                if (common.strLen(val.value) > tipsText.vote.vote_option_input_length) {
                    if ($(this).parent().find(".error").length > 0) {
                        $(this).parent().find(".error").html(tipsText.vote.vote_option_max_length);
                    } else {
                        $(this).parent().append('<div class="error">' + tipsText.vote.vote_option_max_length + '</div>');
                    }
                    flag = false;
                }
            }
        });
        if (optionNum < tipsText.vote.vote_option_limit_size) {
            if ($("#addOption").parent().prev().find(".error").length > 0) {
                $("#addOption").parent().prev().find(".error").html(tipsText.vote.vote_option_limit_size_tips);
            } else {
                $("#addOption").parent().prev().append('<div class="error">' + tipsText.vote.vote_option_limit_size_tips + '</div>');
            }

            flag = false;
        }

        return flag;
    }

    var checkExpiredDate = function() {
        if ($("#expiredselect").val() == 'def') {
            if (common.isValidDate($("#dateinput").val())) {
                if (common.comptime(common.date2str(new Date()), $("#dateinput").val() + ' ' + $("#expiredhour").val() + ':' + $("#expiredminite").val() + ':00') < 0) {
                    var alertOption = {text:tipsText.vote.vote_expired_date_before_ill, tipLayer:true, textClass:"tipstext"};
                    joymealert.alert(alertOption);
                    return false;
                }

            } else {
                //
                var alertOption = {text:tipsText.vote.vote_expired_date_illegal, tipLayer:true, textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            }
        }
        return true;
    }

    var postVote = function () {
        var formOption = {
            formid:'voteForm',
            beforeSend:function () {
                if (window.postVoteLock) {
                    return false;
                }
                window.postVoteLock = true;
                loadingVote();
                return true;
            },
            success:function (req, paramObj) {
                var jsonObj = eval('(' + req + ')');
                //弹出层提示
                if (jsonObj.status_code == "1") {
                    //成功
                    if ($("#gamecode").length == 0) {
                        window.location.href = "/home";
                    } else {
                        window.location.href = "/group/" + $("#gamecode").val() + "/talk";
                    }

                } else {
                    //失败 <div class="error">已超出28个字，最多可输入20个字</div>
                    $("#postError").show().html(jsonObj.msg);
                    window.postVoteLock = false;
                    recoverVote();
                }
            }
        };

        joymeform.form(formOption);
    };

    var loadingVote = function () {
        var loginBut = $("#postvote");
        loginBut.attr('class', 'loadbtn').html('<span><em class="loadings"></em>发布中...</span>');
    };
    var recoverVote = function () {
        var loginBut = $("#postvote");
        loginBut.attr('class', 'submitbtn').html('<span>发起</span>');
    };

    var loadPreviewVote = function (subjectId, directId, directUno, domId, loadingCallback, callback) {
        $.ajax({
                    type:"POST",
                    url:"/json/vote/view",
                    data:{subjectid:subjectId, directid:directId,directUno:directUno},
                    success:function (req) {
                        var resultMsg = eval('(' + req + ')')
                        callback(subjectId, directId, directUno, domId, resultMsg);
                    },
                    beforeSend:function () {
                        if (loadingCallback != null) {
                            loadingCallback(subjectId, directId, directUno, domId);
                        }
                    }
                });
    };

    var beforePreviewVote = function (subjectId, directId, directUno, domId) {
        $('a[data-domid=' + domId + ']').removeClass('submit_poll');
        $('a[data-domid=' + domId + ']').addClass('loadbtn').html('<span><em class="loadings"></em>加载中...</span>');
        $('a[data-domid=' + domId + ']').attr('name', '');

    };

    var reverPreviewVote = function (domId) {
        $('a[data-domid=' + domId + ']').removeClass('loadbtn');
        $('a[data-domid=' + domId + ']').addClass('submit_poll').html('<span>投 票</span>');
        $('a[data-domid=' + domId + ']').attr('name', 'prePartVote');
    };

    var loadPreviewVoteCallback = function (subjectId, directId, directUno, domId, resultMsg) {
        reverPreviewVote(domId);
        if (resultMsg.status_code == "1") {
            var voteHtml = generatorVoteHtml(subjectId, directId, directUno, domId, resultMsg);
            $("#" + domId).hide();
            $("#" + domId).after(voteHtml);
        } else {
            var alertOption = {text:'加载失败', tipLayer:true, textClass:"tipstext"};
            joymealert.alert(alertOption);
        }
    };

    //加载vote html
    var generatorVoteHtml = function (subjectId, directId, directUno, domId, resultMsg) {
        var voteDto = resultMsg.result[0];
        var subjectStr = "";
        var voteNumStr = "";
        var optionStr = "";
        var pollInfo = "";

        var selectStr = "";
        if (voteDto.vote.voteSubject.choiceNum > 1) {
            selectStr = '最多选' + (voteDto.vote.voteSubject.choiceNum) + '项';
        } else {
            selectStr = "单选";
        }

        //参与过 or 已过期
        if (voteDto.voteUserRecord != null || voteDto.vote.expired) {
            subjectStr = '<h3>' + voteDto.vote.voteSubject.subject + '（' + selectStr + '）</h3>' +
                    '<p>' + voteDto.vote.voteSubject.direction + '</p>';
            voteNumStr = '<div class="poll_hits_box clearfix"><span class="poll_hits">' + (voteDto.vote.voteSubject.voteNum) + '人参与投票</span></div>';

            optionStr = generatorVotedExpiredOptionStr(voteDto);
        } else if (voteDto.voteUserRecord == null && voteDto.vote.voteSubject.voteVisible.code == 'voted' && !voteDto.vote.expired) {
            //没参与过 && 投票后可见
            subjectStr = '<h3>' + voteDto.vote.voteSubject.subject + '（' + selectStr + '）</h3>' +
                    '<p>' + voteDto.vote.voteSubject.direction + '</p>';

            optionStr = generatorVotedOptionStr(voteDto, domId, subjectId, directId, directUno);

        } else if (voteDto.voteUserRecord == null && voteDto.vote.voteSubject.voteVisible.code == 'all' && !voteDto.vote.expired) {
            //没参与过 && 任何人可见
            subjectStr = '<h3>' + voteDto.vote.voteSubject.subject + '（' + selectStr + '）</h3>' +
                    '<p>' + voteDto.vote.voteSubject.direction + '</p>';

            voteNumStr = '<div class="poll_hits_box clearfix"><span class="poll_hits">' + (voteDto.vote.voteSubject.voteNum) + '人参与投票</span></div>';

            optionStr = generatorAllOptionStr(voteDto, domId, subjectId, directId, directUno);
        }

        pollInfo = generatorPollInfoStr(voteDto);

        var voteImage = generatorPollImage(voteDto);

        var voteHtml = '<div class="poll_box_all" id="' + domId + '_submit">' +
                '<a class="closepoll" name="closePoll" href="javascript:void(0)" data-domid="' + domId + '">收起</a>' +
                '<div class="poll_box">' +
                '<div class="poll_content">' +
                subjectStr +
                voteImage +
                voteNumStr +
                optionStr +
                pollInfo +
                '<span class="poll_radius poll_radius_lt"></span>' +
                '<span class="poll_radius poll_radius_rt"></span>' +
                '<span class="poll_radius poll_radius_bl"></span>' +
                '<span class="poll_radius poll_radius_br"></span>' +
                '</div>' +
                '<div class="blank10"></div>' +
                '</div>';

        return voteHtml;
    };

    var generatorVoteHtmlOnBlog = function (subjectId, directId, directUno, domId, resultMsg) {
        var voteDto = resultMsg.result[0];
        var subjectStr = "";
        var voteNumStr = "";
        var optionStr = "";
        var pollInfo = "";

        var selectStr = "";
        if (voteDto.vote.voteSubject.choiceNum > 1) {
            selectStr = '最多选' + (voteDto.vote.voteSubject.choiceNum) + '项';
        } else {
            selectStr = "单选";
        }

        //参与过 or 已过期
        if (voteDto.voteUserRecord != null || voteDto.vote.expired) {
            subjectStr = '<h3>' + voteDto.vote.voteSubject.subject + '（' + selectStr + '）</h3>' +
                    '<p>' + voteDto.vote.voteSubject.direction + '</p>';

            voteNumStr = '<div class="poll_hits_box clearfix"><span class="poll_hits">' + (voteDto.vote.voteSubject.voteNum) + '人参与投票</span></div>';

            optionStr = generatorVotedExpiredOptionStr(voteDto);
        } else if (voteDto.voteUserRecord == null && voteDto.vote.voteSubject.voteVisible.code == 'voted' && !voteDto.vote.expired) {
            //没参与过 && 投票后可见
            subjectStr = '<h3>' + voteDto.vote.voteSubject.subject + '（' + selectStr + '）</h3>' +
                    '<p>' + voteDto.vote.voteSubject.direction + '</p>';

            optionStr = generatorVotedOptionStr(voteDto, domId, subjectId, directId, directUno);

        } else if (voteDto.voteUserRecord == null && voteDto.vote.voteSubject.voteVisible.code == 'all' && !voteDto.vote.expired) {
            //没参与过 && 任何人可见
            subjectStr = '<h3>' + voteDto.vote.voteSubject.subject + '（' + selectStr + '）</h3>' +
                    '<p>' + voteDto.vote.voteSubject.direction + '</p>';
            voteNumStr = '<div class="poll_hits_box clearfix"><span class="poll_hits">' + (voteDto.vote.voteSubject.voteNum) + '人参与投票</span></div>';
            optionStr = generatorAllOptionStr(voteDto, domId, subjectId, directId, directUno);
        }

        pollInfo = generatorPollInfoStrOnBlog(voteDto);
        var voteImage = generatorPollImage(voteDto);

        var voteHtml = '<div class="poll_content">' +
                subjectStr +
                voteImage +
                voteNumStr +
                optionStr +
                pollInfo +
                '</div>';

        return voteHtml;
    };

    //参与过 or expired
    var generatorVotedExpiredOptionStr = function(voteDto) {
        var liStr = '';
        $.each(voteDto.vote.voteOptionMap, function (i, val) {
            var str_percent = 0;
            if (voteDto.vote.voteNum != 0) {
                str_percent = (val.optionNum / voteDto.vote.voteNum * 100).toFixed(2);
            }

            var processBg = (i % 5) + 1;
            if (val.checked) {
                liStr = liStr + '<li class="poll_selected"  name="licurrent">' +
                        '<label>' + val.description + '</label>' +
                        '<div class="processBar">' +
                        '<span><em style="width:' + str_percent + '%" class="processBg_' + processBg + '"></em></span>' + (val.optionNum) + '票(' + str_percent + '%)' +
                        '</div>' +
                        '</li>';
            } else {
                var labelStr = '';
                if (voteDto.vote.voteSubject.choiceNum > 1) {
                    labelStr = '<label><input type="checkbox" name="optionid" disabled="disabled" max-option="' + voteDto.vote.voteSubject.choiceNum + '">&nbsp;' + val.description + '</label>';
                } else {
                    labelStr = '<label><input type="radio" name="optionid" disabled="disabled" max-option="' + voteDto.vote.voteSubject.choiceNum + '">&nbsp;' + val.description + '</label>';
                }

                liStr = liStr + '<li name="licurrent">' + labelStr +
                        '<div class="processBar">' +
                        '<span><em style="width:' + str_percent + '%" class="processBg_' + processBg + '"></em></span>' + (val.optionNum) + '票(' + str_percent + '%)' +
                        '</div>' +
                        '</li>';
            }
        });

        var pollStr = '已投票';
        if (voteDto.vote.expired) {
            pollStr = '已结束';
        }

        var optionStr = '<ul>' + liStr +
                '<li>' +
                '<p class="next">' +
                '<a class="disable_poll"><span>' + pollStr + '</span></a>' +
                '</p>' +
                '</li>' +
                '</ul>';
        return optionStr;
    };

    //没参与，all 可见
    var generatorAllOptionStr = function(voteDto, domId, subjectId, directId, directUno) {
        var liStr = '';
        $.each(voteDto.vote.voteOptionMap, function (i, val) {
            var str_percent = 0;
            if (voteDto.vote.voteNum != 0) {
                str_percent = (val.optionNum / voteDto.vote.voteNum * 100).toFixed(2);
            }
            var processBg = (i % 5) + 1;
            var labelStr = "";
            if (voteDto.vote.voteSubject.choiceNum > 1) {
                labelStr = '<label><input type="checkbox" name="optionId" value="' + val.optionId + '">&nbsp;' + val.description + '</label>';
            } else {
                labelStr = '<label><input type="radio" name="optionId" value="' + val.optionId + '">&nbsp;' + val.description + '</label>';
            }
            liStr = liStr + '<li name="licurrent">' + labelStr +
                    '<div class="processBar">' +
                    '<span><em style="width:' + str_percent + '%" class="processBg_' + processBg + '"></em></span>' + (val.optionNum) + '票(' + str_percent + '%)' +
                    '</div>' +
                    '</li>';
        });

        var syncStr = "";
        if (joyconfig.syncFlag) {
            syncStr = '<span class="y_zhuanfa">' +
                    '<input type="checkbox" name="syncContent" checked="checked" class="publish_s">' +
                    '<span>同步</span>' +
                    '<em class="install"></em>' +
                    '</span>';
        } else {
            syncStr = '<span class="y_zhuanfa">' +
                    '<input type="checkbox" name="syncContent" disabled="disabled" class="publish_s">' +
                    '<span>同步</span>' +
                    '<em class="install"></em>' +
                    '</span>';
        }

        var optionStr = '<ul>' + liStr +
                '<li>' +
                '<p class="next">' +
                '<a class="submit_poll" name="submit_poll" data-domid="' + domId + '" data-sid="' + subjectId + '" data-did="' + directId + '" data-duno="' + directUno + '" data-choicenum="' + voteDto.vote.voteSubject.choiceNum + '"><span>投 票</span></a>' +
                syncStr +
                '</p>' +
                '</li>' +
                '</ul>';
        return optionStr;
    };

    //没参与，voted可见
    var generatorVotedOptionStr = function(voteDto, domId, subjectId, directId, directUno) {
        var liStr = '';
        $.each(voteDto.vote.voteOptionMap, function (i, val) {
            var labelStr = "";
            if (voteDto.vote.voteSubject.choiceNum > 1) {
                labelStr = '<label><input type="checkbox" name="optionId" value="' + val.optionId + '">&nbsp;' + val.description + '</label>';
            } else {
                labelStr = '<label><input type="radio" name="optionId" value="' + val.optionId + '">&nbsp;' + val.description + '</label>';
            }
            liStr = liStr + '<li  name="licurrent">' + labelStr +
                    '</li>';
        });

        var syncStr = "";
        if (joyconfig.syncFlag) {
            syncStr = '<span class="y_zhuanfa">' +
                    '<input type="checkbox" name="syncContent" checked="checked" class="publish_s">' +
                    '<span>同步</span>' +
                    '<em class="install"></em>' +
                    '</span>';
        } else {
            syncStr = '<span class="y_zhuanfa">' +
                    '<input type="checkbox" name="syncContent" disabled="disabled" class="publish_s">' +
                    '<span>同步</span>' +
                    '<em class="install"></em>' +
                    '</span>';
        }

        var optionStr = '<ul>' + liStr +
                '<li>' +
                '<p class="next">' +
                '<a class="submit_poll" name="submit_poll" data-domid="' + domId + '" data-sid="' + subjectId + '" data-did="' + directId + '" data-duno="' + directUno + '" data-choicenum="' + voteDto.vote.voteSubject.choiceNum + '"><span>投 票</span></a>' +
                syncStr +
                '</p>' +
                '</li>' +
                '</ul>';
        return optionStr;
    };

    var generatorPollInfoStrOnBlog = function(voteDto) {
        var pollInfo = "";
        if (voteDto.userRecordDTOList != null && voteDto.userRecordDTOList.length > 0) {
            $.each(voteDto.userRecordDTOList, function(i, val) {
                var optionDesStr = generatorOptionDesStrOnBlog(val.voteUserRecord);
                pollInfo = pollInfo + '<p><a href="javascript:void(0)" name="atLink" title="' + val.blog.screenName + '" href="' + joyconfig.URL_WWW + '/people/' + val.blog.domain + '">' + val.blog.screenName + '</a>' +
                        '<span>投票给</span>' + optionDesStr + '<span>' + val.voteUserRecord.dateStr + '</span></p>';
            });
        }

        return '<div class="poll_info">' + pollInfo + '</div>';
    };

    var generatorPollImage = function(voteDto) {
        var imageStr = '';
        if (voteDto.vote.voteSubject.imageSet != null && voteDto.vote.voteSubject.imageSet.images.length > 0) {
            $.each(voteDto.vote.voteSubject.imageSet.images, function(i, val) {
                imageStr += '<p class="img"><img src="' + common.parseMimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/></p>';
            });
        }

        return imageStr;
    };

    var generatorPollInfoStr = function(voteDto) {
        var pollInfo = "";
        if (voteDto.userRecordDTOList != null && voteDto.userRecordDTOList.length > 0) {
            $.each(voteDto.userRecordDTOList, function(i, val) {
                if (i < 3) {
                    var optionDesStr = generatorOptionDesStr(val.voteUserRecord);
                    pollInfo = pollInfo + '<p><a href="javascript:void(0)" name="atLink" title="' + val.blog.screenName + '" href="'+joyconfig.URL_WWW + '/people/' + val.blog.domain  + '">' + val.blog.screenName + '</a>' +
                            '<span>投票给</span>' + optionDesStr + '<span>' + val.voteUserRecord.dateStr + '</span></p>';
                }
            });
            if (voteDto.userRecordDTOList.length > 3) {
                if (voteDto.voteContent != null && voteDto.voteBlog != null) {
                    pollInfo += '<p><a href="' + joyconfig.URL_WWW + '/note/' + voteDto.voteContent.contentId + '" class="show_all_poll" target="_blank">查看全部&gt;&gt;</a></p>';
                }
            }
        }

        return '<div class="poll_info">' + pollInfo + '</div>';
    };

    var generatorOptionDesStr = function(voteUserRecord) {
        var optionDesStr = "";
        $.each(voteUserRecord.recordSet.records, function(i, val) {
            if (i < 2) {
                optionDesStr = optionDesStr + val.optionValue;
                if (i != 1 && voteUserRecord.recordSet.records.length >= 2) {
                    optionDesStr = optionDesStr + "、";
                }
            }
        });
        if (voteUserRecord.recordSet.records.length > 2) {
            optionDesStr = optionDesStr + "等";
        }

        return optionDesStr;
    };

    var generatorOptionDesStrOnBlog = function(voteUserRecord) {
        var optionDesStr = "";
        $.each(voteUserRecord.recordSet.records, function(i, val) {
            optionDesStr = optionDesStr + val.optionValue;
            if (i < voteUserRecord.recordSet.records.length) {
                optionDesStr = optionDesStr + "、";
            }
        });

        return optionDesStr;
    };

    var partVoteSubmit = function(domId, directId, directUno, subjectId, choiceNum, sync, size, beforePartVoteBiz, partVoteBizCallback) {
        //验证
        if (choiceNum > 1) {
            var index = 0;
            var optionStr = "";
            $("#" + domId + "_submit").find("input[name=optionId]").each(function(i, val) {
                if (val.checked) {
                    index++;
                    optionStr = optionStr + val.value + ",";
                }
            });
            if (index > choiceNum) {
                var alertOption = {text:'最可选' + choiceNum + '项', tipLayer:true, textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            } else {
                //
                partVoteBiz(subjectId, directId, directUno, optionStr, domId, sync, size, beforePartVoteBiz, partVoteBizCallback);
            }
        } else {
            var optionStr = "";
            $("#" + domId + "_submit").find("input[name=optionId]").each(function(i, val) {
                if (val.checked) {
                    optionStr = val.value;
                }
            });
            if (optionStr == '') {
                var alertOption = {text:'至少选一项', tipLayer:true, textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            } else {
                partVoteBiz(subjectId, directId, directUno, optionStr, domId, sync, size, beforePartVoteBiz, partVoteBizCallback);
            }
        }
    };
    var partVoteBiz = function (subjectId, directId, directUno, optionid, domId, sync, size, loadingCallback, callback) {
        $.ajax({
                    type:"POST",
                    url:"/json/vote/part",
                    data:{cid:directId,cuno:directUno,subjectid:subjectId,optionid:optionid, sync:sync, size:size},
                    success:function (req) {
                        var resultMsg = eval('(' + req + ')')
                        callback(subjectId, directId, directUno, optionid, domId, resultMsg);
                    },
                    beforeSend:function () {
                        if (loadingCallback != null) {
                            loadingCallback(subjectId, directId, directUno, optionid, domId);
                        }
                    }
                });
    };

    var beforePartVoteBiz = function(subjectId, directId, directUno, optionid, domId) {
        $("#" + domId + "_submit").find('.poll_box').find('a[data-domid=' + domId + ']').removeClass('submit_poll');
        $("#" + domId + "_submit").find('.poll_box').find('a[data-domid=' + domId + ']').addClass('loadbtn').html('<span><em class="loadings"></em>投票中...</span>');
        $("#" + domId + "_submit").find('.poll_box').find('a[data-domid=' + domId + ']').attr('name', '');
    }

    var reverPartVoteBiz = function(domId) {
        $("#" + domId + "_submit").find('.poll_box').find('a[data-domid=' + domId + ']').removeClass('loadbtn');
        $("#" + domId + "_submit").find('.poll_box').find('a[data-domid=' + domId + ']').addClass('submit_poll').html('<span>投票</span>');
        $("#" + domId + "_submit").find('.poll_box').find('a[data-domid=' + domId + ']').attr('name', 'submit_poll');
    }

    var beforePartVoteBizOnBlog = function(subjectId, directId, directUno, optionid, domId) {
        $("#" + domId + "_submit").find('a[data-domid=' + domId + ']').removeClass('submit_poll');
        $("#" + domId + "_submit").find('a[data-domid=' + domId + ']').addClass('loadbtn').html('<span><em class="loadings"></em>投票中...</span>');
        $("#" + domId + "_submit").find('a[data-domid=' + domId + ']').attr('name', '');
    }

    var reverPartVoteBizOnBlog = function(domId) {
        $("#" + domId + "_submit").find('a[data-domid=' + domId + ']').removeClass('loadbtn');
        $("#" + domId + "_submit").find('a[data-domid=' + domId + ']').addClass('submit_poll').html('<span>投票</span>');
        $("#" + domId + "_submit").find('a[data-domid=' + domId + ']').attr('name', 'submit_poll');
    }

    var partVoteBizCallback = function(subjectId, directId, directUno, optionid, domId, resultMsg) {
        reverPartVoteBiz(domId);
        if (resultMsg.status_code == "1") {
            var voteHtml = generatorVoteHtml(subjectId, directId, directUno, domId, resultMsg);
            $("#" + domId + "_submit").replaceWith(voteHtml);
        } else {
            var alertOption = {text:resultMsg.msg, tipLayer:true, textClass:"tipstext"};
            joymealert.alert(alertOption);
        }

    }

    var partVoteBizCallbackOnBlog = function(subjectId, directId, directUno, optionid, domId, resultMsg) {
        reverPartVoteBizOnBlog(domId);
        if (resultMsg.status_code == "1") {
            var voteHtml = generatorVoteHtmlOnBlog(subjectId, directId, directUno, domId, resultMsg);
            $("#" + domId + "_submit").replaceWith(voteHtml);
        } else {
            var alertOption = {text:resultMsg.msg, tipLayer:true, textClass:"tipstext"};
            joymealert.alert(alertOption);
        }

    }

    return vote;
})