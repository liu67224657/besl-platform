var isLoading = false,
    fnMenu = {
        'loadTime': null,
        'curpage': 1,
        // 动态获取滚动区域元素宽度 （例：活动嘉宾区域宽度）
        getWidth: function (config) {
            var widthBox = config.widthBox,
                widthEle = config.widthEle,
                OneW = widthEle.outerWidth(true),
                len = widthEle.length,
                factW = OneW * len;
            widthBox.width(factW);
        },
        loadComment: function (config) {
            var pid = $("input[name='pid']").val();
            var qid = $("input[name='qid']").val();
            var askpid = $("input[name='askpid']").val();
            var accepaid = $("input[name='accepaid']").val();
            var fisrtaid = $("input[name='fisrtaid']").val();


            var page = fnMenu.curpage + 1;
            $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/wanba/api/ask/answer/answerlist",
                data: {qid: qid, pid: pid, pnum: page},
                timeout: 5000,
                dataType: "json",
                type: "POST",
                success: function (data) {
                    if (data.rs == 1) {
                        var result = data.result;
                        fnMenu.curpage = page;
                        var share = $("input[name='share']").val();    //判断是不是分享页面

                        $("input[name='maxpage']").val();
                        var html = "";
                        for (var i = 0; i < result.answerDetailDTOList.length; i++) {
                            var answer = result.answerDetailDTOList[i];
                            if (answer.answerid == accepaid || fisrtaid == answer.fisrtaid) {
                                continue;
                            }

                            html += "<div class='join-ans-item border-bot'><div class='join-ans-cont'><div class='ans-user fn-clear'>";
                            if (typeof(share) == "undefined") {
                                html += "<a href='javascript:jump(\"51\",\"" + answer.profileid + "\");' class='ans-user-l'>";
                            } else {
                                html += "<a href='jmwanba://jt=52&ji="+qid+"' class='ans-user-l'>";
                            }

                            if (result.profileMap[answer.profileid].icon == '') {
                                html += "<cite><img src='http://lib." + joyconfig.DOMAIN + "/hotdeploy/static/img/wanba_default.png'></cite>"
                            } else {
                                html += "<cite><img src='" + result.profileMap[answer.profileid].icon + "'></cite>";
                            }
                            html += "<span> <font class='ans-user-name fn-clear'><em class='limit'>" + result.profileMap[answer.profileid].nick + "</em>";
                            if (typeof(result.wanbaProfileMap[answer.profileid]) != "undefined") {
                                if (result.wanbaProfileMap[answer.profileid].verifyType > 0) {
                                    html += "<i></i>";
                                }
                            }
                            html += "</font>" +
                                "<b class='ans-user-tme'>" + answer.answertimeString + "</b></span></a>"
                            if (typeof(result.wanbaProfileMap[answer.profileid]) != "undefined" && answer.profileid != pid) {
                                if (typeof(share) == "undefined") {
                                    html += " <a href='javascript:;' class='ans-user-r '><span onclick='jump(" + 56 + ",pid=" + answer.profileid + "&point=" + result.wanbaProfileMap[answer.profileid].askPoint + "&name=" + result.wanbaProfileMap[answer.profileid].nick + ")'>问TA</span></a>";
                                } else {
                                    html += "<a href='jmwanba://jt=52&ji="+qid+"' class='ans-user-r '>";
                                }
                            }
                            html += "</div>";
                            if (answer.voice != null && answer.voice != '' && answer.voice.url != '') {
                                var time = answer.voice.time / 1000;
                                time = parseInt(time);
                                if (time < 1) {
                                    time = 1;
                                }
                                html += "<div class='audio-box'><audio src='" + answer.voice.url + "'></audio><i></i><b>" + time + "</b></div>";
                            }
                            if (answer.richbody != '') {
                                if (typeof(share) == "undefined") {
                                    html += "<a href='http://api." + joyconfig.DOMAIN + "/wanba/webview/answer/detail?aid=" + answer.answerid + "&pid=" + answer.profileid + "'><p class='limit-line'>" + answer.body.text + "</p></a>";
                                } else {
                                    html += "<a href='http://api." + joyconfig.DOMAIN + "/wanba/webview/answer/sharedetail?aid=" + answer.answerid + "'>" + answer.richbody + "</a>";
                                }
                            }
                            html += "</div> <div class='suspend fn-clear'>";
                            if (typeof(share) == "undefined") {
                                if (answer.agreestatus == 1) {
                                    html += " <a href='javascript:onagree();' class='zan-count on'><i></i>";
                                } else {
                                    html += " <a href=javascript:agree('" + answer.answerid + "'); id=agreeClass" + answer.answerid + "' class='zan-count'><i></i>";
                                }
                            } else {
                                html += "<a href='jmwanba://jt=52&ji="+qid+"' class='zan-count'><i></i>";
                            }


                            if (answer.agreesum == 0) {
                                html += " <em id='agree" + answer.answerid + "'>点赞</em>";
                            } else {
                                html += "<em id='agree" + answer.answerid + "'>" + answer.agreesum + "</em>"
                            }
                            html += "</a>";
                            if (typeof(share) == "undefined") {
                                html += "<a href=\"javascript:jump('55','" + answer.answerid + "');\" class='mess-count'><i></i>";
                            } else {
                                html += "<a href='jmwanba://jt=52&ji="+qid+"' class='mess-count'><i></i>";
                            }


                            if (answer.replysum == 0) {
                                html += "评论";
                            } else {
                                html += answer.replysum;
                            }
                            html += "</a>";
                            if (pid == askpid && accepaid == 0) {
                                html += " <div class='adopt-best'><span onclick='accepaid(" + answer.answerid + ")'><i></i>采纳为最佳</span></div>";
                            }
                            html += "</div></div>";
                        }
                        $(".join-ans-list").append(html);
                        isLoading = false;
                        $('.loading').remove();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("请求失败");
                    isLoading = false;
                    return;
                }
            });

            clearTimeout(fnMenu.loadTime);
            /*新增end*/
        },
        //滚动加载
        scroll_load: function (parentBox, className) {
            var className = className, parentBox = parentBox;
            $("#" + parentBox).scroll(function (ev) {
                ev.stopPropagation();
                ev.preventDefault();
                var maxPage = $("input[name='maxpage']").val();
                if (fnMenu.curpage >= maxPage) {
                    return;
                }
                var sTop = $("#" + parentBox)[0].scrollTop + 5;
                var sHeight = $("#" + parentBox)[0].scrollHeight;
                var sMainHeight = $("#" + parentBox).height();
                var sNum = sHeight - sMainHeight;
                var loadTips = '<div class="loading"><span><i></i>正在加载...</span></div>';
                if (sTop >= sNum && !isLoading) {
                    isLoading = true;
                    $('.' + className).append(loadTips);
                    fnMenu.loadComment(className);
                }
                ;
            });
        },
        // 倒计时
        timeOut: function (parent, time) {
            var parents = parent;
            var timeNum = time;
            var times = null;
            var hourbox = parents.find('.hour'),
                minutebox = parents.find('.minute');
            secondbox = parents.find('.second');
            function zero(num) {
                // console.log(num)
                if (num < 10) {
                    return '0' + num;
                } else if (num >= 10) {
                    return '' + num;
                }
            };
            times = setInterval(function () {
                checkTime();
            }, 1000);
            function checkTime() {
                var future = Date.parse(timeNum);
                var now = new Date();
                var nowTime = now.getTime();
                var mistiming = (future - nowTime) / 1000,
                    h = zero(parseInt((mistiming) / 60 / 60 % 24)),
                    f = zero(parseInt((mistiming / 60) % 60)),
                    m = zero(parseInt(mistiming % 60));

                getTimes(h, f, m);
                if (future < nowTime) {
                    clearInterval(times);
                    parents.find('code').html('00');

                    setInterval(function () {
                        location.reload();
                    }, 3000);
                    return false;
                }
                ;
            };

            function getTimes(h, f, m) {
                hourbox.html(h);
                minutebox.html(f);
                secondbox.html(m)
            };
        },
        oAudio: function () {//音频
            var audioBox = $('.audio-box'),
                audioEle = audioBox.find('audio');
            inta = true;
            audioBox.click(function () {
                var $this = $(this),
                    actAudio = $this.find('audio')[0];
                audioEle.parent('.audio-box').find('i').removeClass('on');
                audioEle.each(function () {
                    var eventTester = function (e) {
                        actAudio.addEventListener(e, function () {
                            $(actAudio).parent('.audio-box').find('i').removeClass('on');
                        }, false);
                    }
                    eventTester("ended");
                    if (this == actAudio) {
                        if (this.paused) {
                            actAudio.load();
                            this.play();
                            $(actAudio).parent('.audio-box').find('i').addClass('on');
                        } else {
                            audioEle.parent('.audio-box').find('i').removeClass('on');
                            this.pause();
                        }
                    } else {
                        this.pause();
                    }
                });
            });
        }, oHint: function (config) {//弹窗
            var oMask = config.mask,
                oHintBox = config.hintBox,
                oHintChild = oHintBox.children(),
                activing = config.activing,
                clickEle = config.clickEle;
            clickEle.click(function () {
                oMask.addClass(activing);
                oHintBox.addClass(activing);
            });
            oHintChild.click(function () {
                oMask.removeClass(activing);
                oHintBox.removeClass(activing);
            });
        }, otherPause: function (config) {
            var oFiring = config.firing,
                pausEle = config.pausEle;
            oFiring.click(function () {
                pausEle.each(function () {
                    var thisAu = $(this).find('audio');
                    thisAu[0].pause();
                });
            })
        },

        int: function (parent, time) {
            fnMenu.getWidth({widthBox: $('.guest-list '), widthEle: $('.guest-list a')});
            window.onload = function () {
                fnMenu.scroll_load('wrapper', 'list-item');
            };
            fnMenu.oAudio();
            fnMenu.oHint({mask: $('.mask'), hintBox: $('.hint'), activing: 'on', clickEle: $('.restart a')});
            fnMenu.otherPause({firing: $('.ans-user-r span,.mess-count'), pausEle: $('.audio-box')});
        }
    }
fnMenu.int();