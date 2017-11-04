define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    require('../common/jquery.form');
    var customizebiz = require('../biz/customize-biz');
    var ajaxverify = require('../biz/ajaxverify');
    var mail = require('./getmail');
    var veifyPost = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');

    var customize = {
        setCity:function (joycityid) {
            var pid = $("#provinceId").val();
            if (pid != -1) {
                $.post("/json/profile/customize/getsubregionbypid", {regionid:pid}, function (data) {
                    var jsonObj = eval('(' + data + ')');
                    common.locationLoginByJsonObj(jsonObj);

                    if (jsonObj.status_code == '1') {
                        $("#cityid").html('<option value="-1" id="cityid_init">请选择</option>');

                        $.each(jsonObj.result, function (i, val) {
                            var str = '<option value="' + val.regionId + '">' + val.regionName + '</option>';
                            if (val.regionId == joycityid) {
                                str = '<option value="' + val.regionId + '" selected="selected">' + val.regionName + '</option>';
                            }
                            $("#cityid_init").after(str);
                        });
                        $("#cityid").next('div').remove();
                        $("#cityid").ylDropDownList();
                    }
                });
            }
        },
        showValidateTips:function () {
            for (var i = 0; this.errorList[i]; i++) {
                var error = this.errorList[i];
                var elename = this.idOrName(error.element);
                $("#" + elename + "tips").html(error.message);
                onsubmit: false;
            }
            //验证成功处理
            for (var i = 0; this.successList[i]; i++) {
                var suc = this.successList[i];
                $("#" + suc.id + "tips").html('');
            }

        },
        saveBaseInfo:function () {
            var inputNum = common.getInputLength($("#description").val())
            inputNum = !inputNum ? 0 : inputNum;

            if (inputNum <= 140) {
                $("#form_baseinfo").ajaxForm(function (data) {
                    var jsonObj = eval('(' + data + ')');
                    common.locationLoginByJsonObj(jsonObj);
                    var alertOption = {};
                    if (jsonObj.status_code == "1") {
                        alertOption = {text:jsonObj.msg, tipLayer:true};
                    } else {
                        alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                    }
                    joymealert.alert(alertOption);
                });

                $("#form_baseinfo").submit();
            }

        },
        saveDomainInfo:function () {
            $("#form_domaininfo").ajaxForm(function (data) {
                var jsonObj = eval('(' + data + ')');
                common.locationLoginByJsonObj(jsonObj);
                if (jsonObj.status_code == "1") {
                    //弹出层提示
                    $("#userLink").attr("href", joyconfig.URL_WWW + '/people/' + $.trim($("#blogdomain").val()));
                    $("#blogLink").attr("href", joyconfig.URL_WWW + '/people/' + $.trim($("#blogdomain").val()) );

                    var alertOption = {text:"保存成功", tipLayer:true};
                    joymealert.alert(alertOption);
                }
            });
            $("#form_domaininfo").submit();
        },
        setUserDefine:function () {
            var hintmyfeedback = '';
            var hintmyfans = '';
            var hintmyletter = '';
            var hintmynotice = '';

            if ($("#hintmyfeedback")[0].checked) {
                hintmyfeedback = "y";
            } else {
                hintmyfeedback = "rj";
            }
            if ($("#hintmyfans")[0].checked) {
                hintmyfans = "y";
            } else {
                hintmyfans = "rj";
            }
            if ($("#hintmyletter")[0].checked) {
                hintmyletter = "y";
            } else {
                hintmyletter = "rj";
            }
            if ($("#hintmynotice")[0].checked) {
                hintmynotice = "y";
            } else {
                hintmynotice = "rj";
            }

            var hintat = false;
            var authorRelation = $("input[name=authorRelation]:checked").val();
            var contentType = $("input[name=contentType]:checked").val();
            if ($("#hintat")[0].checked) {
                hintat = true;
            }

            var allowreplay = $("input[name=allowreplay]:checked").val();
            var allowletter = $("input[name=allowletter]:checked").val();
            customizebiz.saveUserDefine(hintmyfeedback, hintmyfans, hintmyletter, hintmynotice, allowreplay, allowletter, hintat, authorRelation, contentType, this.setUserDefineCallback);

        },
        setUserDefineCallback:function (jsonObj) {
            common.locationLoginByJsonObj(jsonObj);
            var alertOption = {};
            if (jsonObj.status_code == "1") {
                alertOption = {text:jsonObj.msg, tipLayer:true};
            } else {
                alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
            }
            joymealert.alert(alertOption);
        },
        //重新设置邮箱，即登录密码
        resetuserid:function () {
            $("#form_resetuserid").ajaxForm(function (data) {
                var jsonObj = eval('(' + data + ')');
                common.locationLoginByJsonObj(jsonObj);
                //弹出层提示
                if (jsonObj.status_code == "1") {
                    var newuserid = $("#newuserid").val();
                    $("#olduserid")[0].value = "";
                    $("#password")[0].value = "";
                    $("#newuserid")[0].value = "";

                    $("#email_href").html(newuserid).bind('click', function () {
                        mail.hrefMail(newuserid);
                    });

                    $('#link_go_mail').bind('click', function () {
                        mail.hrefMail(newuserid);
                    });

                    customize.showstep();
                } else {
                    var alertOption = {};
                    if (jsonObj.status_code == "1") {
                        alertOption = {text:jsonObj.msg, tipLayer:true};
                    } else {
                        alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                    }
                    joymealert.alert(alertOption);
                }
            });
            $("#form_resetuserid").submit();
        },
        showstep:function () {
            $("#resetemail").css('display', 'none');
            $('#suc').css('display', 'block');
        },
        resetPwd:function () {
            $("#form_resetpwd").ajaxForm(function (data) {
                var jsonObj = eval('(' + data + ')');
                common.locationLoginByJsonObj(jsonObj);
                //弹出层提示
                if (jsonObj.status_code == "1") {
                    $("#form_resetpwd")[0].reset();
                }
                var alertOption = {};
                if (jsonObj.status_code == "1") {
                    alertOption = {text:jsonObj.msg, tipLayer:true};
                } else {
                    alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                }
                joymealert.alert(alertOption);
            });
            $("#form_resetpwd").submit();
        },


        addSchool:function () {
            var school = '<div class="set_adduserdata_line" id="schoolDiv">' +
                '<form method="post" id="form_school" action="/json/profile/experience/saveschools">' +
                '<div class="set_adduserdata">' +
                '<p>学校</p>' +
                '<div class="set_adduserdata_list">' +
                '<ul id="scLi">' +
                '<li>' +
                '<input type="text" class="settext" name="schools" maxlength="40">' +
                '<a class="close" href="javascript:void(0)" name="removeSchoolItem"></a>' +
                '<em></em></li>' +
                '</ul>' +
                '</div>' +

                '<div class="set_adduserdata_fun">' +
                '<p><a class="add_button" href="javascript:void(0)" id="addSchoolItem">+</a></p>' +
                '<p>以上信息' +
                '<label><input type="radio" name="allowType" value="a" checked="checked">所有人可见</label>' +
                '<label><input type="radio" name="allowType" value="c">仅关注我的人可见</label>' +
                '</p>' +
                '<p><a href="javascript:void(0)" class="submitbtn" id="saveSchool"><span>保 存</span></a>' +
                '<a class="graybtn" id="cancelSchool" flag=false><span>取 消</span></a></p>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>';
            $("#addschoolbtn").hide();
            $("#addSchool").append(school);
        },
        addSchoolItem:function (text) {
            var itemStr = '<li>' +
                '<input type="text" class="settext" name="schools" maxlength="40" value="' + text + '">' +
                '<a class="close" href="javascript:void(0)" name="removeSchoolItem"></a>' +
                '<em></em></li>';

            $("#scLi").append(itemStr);
            this.checkSchoolItemLen();
        },

        removeSchoolItem:function (jqObj) {
            if ($("#scLi li").length > 1) {
                jqObj.parent().remove();
                this.checkSchoolItemLen();
            } else {
                jqObj.prev().val('');
            }

        },

        checkSchoolItemLen:function () {
            if ($("#scLi li").length >= 10) {
                $("#addSchoolItem").css("display", "none");
            } else {
                $("#addSchoolItem").css("display", "block");
            }
        },

        checkSchool:function () {
            var returnValue = true;
            $("#scLi li input").each(function (i) {
                if ($.trim($(this).val()).length == 0) {
                    $(this).parent().remove();
                } else {
                    if (common.strLen($(this).val()) > 20) {
                        returnValue = false;
                        $(this).next().next().html(tipsText.userSet.user_experience_school_length);
                    } else {
                        //敏感字过滤

                        if (!ajaxverify.verifySysWord($(this).val())) {
                            returnValue = false;
                            $(this).next().next().html(tipsText.userSet.user_word_illegl);
                        }
                    }
                }
            })
            return returnValue;
        },

        //保存学校
        saveSchool:function () {
            if (this.checkSchool()) {
                if ($("#scLi li").size() == 0) {
                    $('#form_school').ajaxForm(function (data) {
                        var jsonObj = eval('(' + data + ')');
                        common.locationLoginByJsonObj(jsonObj);
                        var alertOption = {};
                        if (jsonObj.status_code == "1") {
                            alertOption = {text:jsonObj.msg, tipLayer:true};
                        } else {
                            alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                        }
                        joymealert.alert(alertOption);
                    });
                    $('#form_school').submit();
                    $("#schoolDiv").remove();
                    $("#addschoolbtn").show();
                } else {
                    var schoolArray = []
                    $("#scLi li input").each(function (i) {
                        schoolArray[i] = $(this).val();
                    })
                    var schoolStatus = $("input:checked[name=allowType]").val();
                    $('#form_school').ajaxForm(function (data) {
                        var jsonObj = eval('(' + data + ')');
                        common.locationLoginByJsonObj(jsonObj);
                        var alertOption = {};
                        if (jsonObj.status_code == "1") {
                            $("#schoolDiv").remove();
                            customize.showSchool(schoolArray, schoolStatus);
                            alertOption = {text:jsonObj.msg, tipLayer:true};
                        } else {
                            alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                        }
                        joymealert.alert(alertOption);
                    });
                    $('#form_school').submit(); //表单提交。
                }
            }
        },
        showSchool:function (schoolArray, schoolStatus) {
            var statusText = "";
            if (schoolStatus == "a") {
                statusText = "(对所有人可见)";
            } else if (schoolStatus == "c") {
                statusText = "(仅我关注的人可见)";
            }
            var scLi = '';
            if (schoolArray.length != 0) {
                for (var i = 0; i < schoolArray.length; i++) {
                    scLi += '<li>' + schoolArray[i] + '</li>';
                }
            }

            var school = '<div class="set_adduserdata_line" id="school">' +
                '<div class="set_adduserdata">' +
                '<p>学校</p>' +
                '<div class="set_adduserdata_list">' +
                '<ul id="scLi">' +
                '<div>' + statusText + '<a href="javascript:void(0)" name="updateSchool" id="' + schoolStatus + '" class="schcool_change">修改</a></div>' + scLi +
                '</ul>' +
                '</div>' +
                '</div>' +
                '</div>';

            $("#addSchool").append(school);
        },
        updateSchool:function (allowType) {
            var schoolArray = []
            $("#scLi li").each(function (i) {
                schoolArray[i] = ($(this).text());
            })
            $("#addSchool").data("scho", $("#school").html());
            $("#school").remove();
            var inputStr = '';
            if (allowType == 'a') {
                inputStr = '<label><input type="radio" name="allowType" value="a" checked="checked">对所有人可见</label>' +
                    '<label> <input type="radio" name="allowType" value="c">仅我关注的人可见</label>';
            } else {
                inputStr = '<label><input type="radio" name="allowType" value="a">对所有人可见</label>' +
                    '<label> <input type="radio" name="allowType" value="c"  checked="checked">仅我关注的人可见</label>';
            }

            var schoolDiv = '<div class="set_adduserdata_line" id="schoolDiv">' +
                '<form method="post" id="form_school" action="/json/profile/experience/saveschools">' +
                '<div class="set_adduserdata">' +
                '<p>学校</p>' +
                '<div class="set_adduserdata_list">' +
                '<ul id="scLi">' +
                '</ul>' +
                '</div>' +
                '<div class="set_adduserdata_fun">' +
                '<p><a class="add_button" href="javascript:void(0)" id="addSchoolItem">+</a></p>' +
                '<p>以上信息' + inputStr +
                '</p>' +
                '<p><a href="javascript:void(0)" class="submitbtn" id="saveSchool"><span>保 存</span></a>' +
                '<a class="graybtn" id="cancelSchool" flag=true><span>取 消</span></a></p>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>';
            $("#addSchool").append(schoolDiv);
            for (var i = 0; i < schoolArray.length; i++) {
                this.addSchoolItem(schoolArray[i]);
            }
        },
        cancelSchool:function (flag) {
            if (flag == "true") {
                $("#schoolDiv").remove();
                $("#addSchool").html('<div id="school" class="schcool">' + $("#addSchool").data("scho") + '</div>');
            } else {
                $("#schoolDiv").remove();
                $("#addschoolbtn").show();
            }
        },

        addCompany:function () {
            var company = '<div class="set_adduserdata_line unline" id="companyDiv">' +
                '<form method="post" id="form_company" action="/json/profile/experience/savecompanies">' +
                '<div class="set_adduserdata">' +
                '<p>公司</p>' +
                '<div class="set_adduserdata_list">' +
                '<ul id="comLi">' +
                '<li>' +
                '<input type="text" class="settext" name="companies" maxlength="40">' +
                '<a class="close" href="javascript:void(0)" name="removeCompanyItem"></a>' +
                '<em></em></li>' +
                '</ul>' +
                '</div>' +

                '<div class="set_adduserdata_fun">' +
                '<p><a class="add_button" href="javascript:void(0)" id="addCompanyItem">+</a></p>' +
                '<p>以上信息' +
                '<label><input type="radio" name="allowType" value="a" checked="checked">所有人可见</label>' +
                '<label><input type="radio" name="allowType" value="c">仅关注我的人可见</label>' +
                '</p>' +
                '<p><a href="javascript:void(0)" class="submitbtn" id="saveCompany"><span>保 存</span></a>' +
                '<a class="graybtn" id="cancelCompany" flag=false><span>取 消</span></a></p>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>';
            $("#addCompanyBtn").hide();
            $("#addCompany").append(company);
        },
        addCompanyItem:function (text) {
            var itemStr = '<li>' +
                '<input type="text" class="settext" name="companies" maxlength="40" value="' + text + '">' +
                '<a class="close" href="javascript:void(0)" name="removeCompanyItem"></a>' +
                '<em></em></li>';

            $("#comLi").append(itemStr);
            this.checkCompanyItemLen();
        },

        removeCompanyItem:function (jqObj) {
            if ($("#comLi li").length > 1) {
                jqObj.parent().remove();
                this.checkCompanyItemLen();
            } else {
                jqObj.prev().val('');
            }

        },

        checkCompanyItemLen:function () {
            if ($("#comLi li").length >= 10) {
                $("#addCompanyItem").css("display", "none");
            } else {
                $("#addCompanyItem").css("display", "block");
            }
        },

        checkCompany:function () {
            var returnValue = true;
            $("#comLi li input").each(function (i) {
                if ($.trim($(this).val()).length == 0) {
                    $(this).parent().remove();
                } else {
                    if (common.strLen($(this).val()) > 20) {
                        returnValue = false;
                        $(this).next().next().html(tipsText.userSet.user_experience_company_length);
                    } else {
                        //敏感字过滤

                        if (!ajaxverify.verifySysWord($(this).val())) {
                            returnValue = false;
                            $(this).next().next().html(tipsText.userSet.user_word_illegl);
                        }
                    }
                }
            })
            return returnValue;
        },

        //保存公司
        saveCompany:function () {
            if (this.checkCompany()) {
                if ($("#comLi li").size() == 0) {
                    $('#form_company').ajaxForm(function (data) {
                        var jsonObj = eval('(' + data + ')');
                        common.locationLoginByJsonObj(jsonObj);
                        var alertOption = {};
                        if (jsonObj.status_code == "1") {
                            alertOption = {text:jsonObj.msg, tipLayer:true};
                        } else {
                            alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                        }
                        joymealert.alert(alertOption);
                    });
                    $('#form_company').submit();
                    $("#companyDiv").remove();
                    $("#addCompanyBtn").show();
                } else {
                    var companyArray = []
                    $("#comLi li input").each(function (i) {
                        companyArray[i] = $(this).val();
                    })
                    var companyStatus = $("input:checked[name=allowType]").val();
                    $('#form_company').ajaxForm(function (data) {
                        var jsonObj = eval('(' + data + ')');
                        common.locationLoginByJsonObj(jsonObj);
                        var alertOption = {};
                        if (jsonObj.status_code == "1") {
                            $("#companyDiv").remove();
                            customize.showCompany(companyArray, companyStatus);
                            alertOption = {text:jsonObj.msg, tipLayer:true};
                        } else {
                            alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                        }
                        joymealert.alert(alertOption);
                    });
                    $('#form_company').submit(); //表单提交。
                }
            }
        },
        showCompany:function (companyArray, companyStatus) {
            var statusText = "";
            if (companyStatus == "a") {
                statusText = "(对所有人可见)";
            } else if (companyStatus == "c") {
                statusText = "(仅我关注的人可见)";
            }
            var comLi = '';
            if (companyArray.length != 0) {
                for (var i = 0; i < companyArray.length; i++) {
                    comLi += '<li>' + companyArray[i] + '</li>';
                }
            }

            var company = '<div class="set_adduserdata_line unline" id="company">' +
                '<div class="set_adduserdata">' +
                '<p>公司</p>' +
                '<div class="set_adduserdata_list">' +
                '<ul id="comLi">' +
                '<div>' + statusText + '<a href="javascript:void(0)" name="updateCompany" id="' + companyStatus + '" class="schcool_change">修改</a></div>'
                + comLi +
                '</ul>' +
                '</div>' +
                '</div>' +
                '</div>';

            $("#addCompany").append(company);
        },
        updateCompany:function (allowType) {
            var companyArray = []
            $("#comLi li").each(function (i) {
                companyArray[i] = ($(this).text());
            })
            $("#addCompany").data("comp", $("#company").html());
            $("#company").remove();
            var inputStr = '';
            if (allowType == 'a') {
                inputStr = '<span><input type="radio" class="checkbtn" name="allowType" value="a" checked="checked">对所有人可见</span>' +
                    '<span> <input type="radio" class="checkbtn" name="allowType" value="c">仅我关注的人可见</span></p>';
            } else {
                inputStr = '<span><input type="radio" class="checkbtn" name="allowType" value="a">对所有人可见</span>' +
                    '<span> <input type="radio" class="checkbtn" name="allowType" value="c"  checked="checked">仅我关注的人可见</span></p>';
            }

            var companyDiv = '<div class="set_adduserdata_line unline" id="companyDiv">' +
                '<form method="post" id="form_company" action="/json/profile/experience/savecompanies">' +
                '<div class="set_adduserdata">' +
                '<p>公司</p>' +
                '<div class="set_adduserdata_list">' +
                '<ul id="comLi">' +
                '</ul>' +
                '</div>' +
                '<div class="set_adduserdata_fun">' +
                '<p><a class="add_button" href="javascript:void(0)" id="addCompanyItem">+</a></p>' +
                '<p>以上信息' + inputStr +
                '</p>' +
                '<p><a href="javascript:void(0)" class="submitbtn" id="saveCompany"><span>保 存</span></a>' +
                '<a class="graybtn" id="cancelCompany" flag=true><span>取 消</span></a></p>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>';
            $("#addCompany").append(companyDiv);
            for (var i = 0; i < companyArray.length; i++) {
                this.addCompanyItem(companyArray[i]);
            }
        },
        cancelCompany:function (flag) {
            if (flag == "true") {
                $("#companyDiv").remove();
                $("#addCompany").html('<div id="company" class="schcool">' + $("#addCompany").data("comp") + '</div>');
            } else {
                $("#companyDiv").remove();
                $("#addCompanyBtn").show();
            }
        },
        addGame:function (getsrt) {
            if ($("#gamesrt").length > 0) {
                $("#gamesrt").remove();
            }
            var gameDiv = '<div id="gamesrt" style="clear:both;">' +
                '<form method="post" id="form_game" action="/json/profile/palyedgame/save">' +
                '<div class="addgamelist">' +
                '<dl>' +
                '<dt>' +
                '<input type="text" class="settext" name="gameName" value="游戏名称"><span id="gamesrterror"></span>' +
                '</dt>' +
                '<div id="gameRoles"></div>' +
                '</dl>' +
                '</div>' +
                '<div class="addgame">' +
                '<a href="javascript:void(0)" id="addRole">+ 添加游戏中的角色信息</a>' +
                '</div>' +
                '<div class="addgamesave" id="gameBtn">' +
                '<a href="javascript:void(0)" class="submitbtn" id="saveGame"><span>保 存</span></a>' +
                '<a class="graybtn" id="cancelGame"><span>取 消</span></a>' +
                '</div>' +
                '</form>' +
                '</div>';


            $("#addGame").before(gameDiv);
            this.addRole(getsrt);
            $("#addGame").hide();
        },
        addRole:function (getsrt) {
            var roleStr = "";
            roleStr = '<dt>' +
                '<input type="text" class="setaddusertext" name="serverAreas" value="' + getsrt.serverAreas + '"><b>‾</b>' +
                '<input type="text" class="setaddusertext" name="roleNames" value="' + getsrt.roleNames + '"><b>‾</b>' +
                '<input type="text" class="setaddusertext" name="guilds" value="' + getsrt.guilds + '"> ' +
                '<a class="close" href="javascript:void(0)" id="removeGameTag"></a>' +
                '</dt>' +
                '<dd>' +
                '<span class="e1"></span>' +
                '<span class="e2"></span>' +
                '<span class="e3"></span>' +
                '</dd>';
            $("#gameRoles").append(roleStr);
            if ($("#gameRoles dt").length >= 20) {
                //判断是否超过20个，超过隐藏添加按钮
                $("#addRole").toggle();
            }
        },
        //保存新游戏
        saveGame:function (getsrt) {
            if (this.checkGame(getsrt)) {
                var gameArray = getArrayForInput(getsrt);
                $('#form_game').ajaxForm(function (data) {
                    var jsonObj = eval('(' + data + ')');
                    common.locationLoginByJsonObj(jsonObj);
                    var alertOption = {};
                    if (jsonObj.status_code == "1") {
                        gameTagID = jsonObj.result[0].playedGameId;
                        var gameData = {gameName:$("input[name=gameName]").val(), gameProperty:gameArray, id:gameTagID};
                        customize.showGame(gameData);
                        alertOption = {text:jsonObj.msg, tipLayer:true};
                    } else {
                        alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                    }
                    joymealert.alert(alertOption);
                });
                $('#form_game').submit(); //表单提交。
            } else {
                return;
            }
        },
        //取消添加游戏
        cancelGame:function (getsrt) {
            $("#gamesrt").remove();
            if ($(".addgameshowlist").size() < 50) {
                $("#addGame").show();
            }
        },
        cancelUpGame:function (tagId) {
            $("#gamesrt").remove();
            $("#gameText_" + tagId).show();
            if ($(".addgameshowlist").size() <= 50) {
                $("#addGame").show();
            }
        },
        //修改游戏
        updateGame:function (tagId, getsrt) {
            if ($("#gamesrt").length > 0) {
                //$("#gamesrt").parent().find("table").show();
                $("#gamesrt").remove();
            }
            $("#addGame").hide();
            var gameArray = getArrayForTable(tagId, getsrt);
            var gameLine = $("#getDate_" + tagId).find("h3").text();
            var gameData = {gameName:gameLine, gameProperty:gameArray, id:tagId};
            var addGameDiv = '<div id="gamesrt" style="clear:both;">' +
                '<form method="post" id="form_game" action="/json/profile/palyedgame/update"><div class="addgamelist"><input type="hidden" name="playedGameId" value="' + gameData.id + '" />' +
                '<input type="hidden" name="gameName" value="' + gameLine + '" /><dl><dt><h3>' + gameLine + '</h3>&nbsp;<a href="javascript:void(0)" id="delGameTag" class="delGameTag" style="font-size: 12px;" title="' + gameData.id + '">删除</a></dt></dl>' +
                '<div id="gameRoles"></div></form>' +
                '</div>';
            if (gameArray.length < 20) {
                addGameDiv += '<div class="addgame"><a id="addRole" href="javascript:void(0)">+ 添加游戏中的角色信息</a></div>';
            }
            addGameDiv += '<div class="addgamesave" id="gameBtn"><a id="uploadGame" class="submitbtn" href="javascript:void(0)"><span id="' + tagId + '">保 存</span></a><a id="cancelUpGame" class="graybtn"><span id="' + tagId + '">取 消</span></a></div>';
            $("#getDate_" + tagId + " #gameText_" + tagId).hide();
            $("#getDate_" + tagId).append(addGameDiv);
            updataRole(gameData, getsrt);
        },
        //显示新添加游戏
        showGame:function (gameData) {
            var gameTrStr = '';
            $.each(gameData.gameProperty, function (i, val) {
                gameTrStr += '<li><p>' + val[0] + '</p><p class="pcenter">' + val[1] + '</p><p class="pcenter">' + val[2] + '</p></li>'
            });
            var gameLineStr = '<h3>' + gameData.gameName + '</h3> &nbsp; <span><a href="javascript:void(0)" class="schcool_change" name="updateGame" id="' + gameData.id + '">修改</a></span><ul> ' + gameTrStr + '</ul>';
            if ($("#getDate_" + gameData.id).length > 0) {
                $("#getDate_" + gameData.id).find("ul").append(gameTrStr);
                $("#gamesrt").remove();
                if ($(".addgameshowlist").size() < 50) {
                    $("#addGame").show();
                }
            } else {
                $("<div class='addgameshowlist' id='getDate_" + gameData.id + "'></div>").insertBefore("#addGame");
                $("#getDate_" + gameData.id).html(gameLineStr);
                $("#gamesrt").remove();
                if ($(".addgameshowlist").size() < 50) {
                    $("#addGame").show();
                }
            }
        },
        //删除游戏角色信息
        removeGameTag:function (tagDom) {
            if ($("#gameRoles dt").size() == 1) {
                $(tagDom).parent().children("input").val("");
            } else if ($("#gameRoles dt").size() <= 20) {
                $("#addRole").show();
                $(tagDom).parent().next().remove();
                $(tagDom).parent().remove();
            } else {
                $(tagDom).parent().next().remove();
                $(tagDom).parent().remove();
            }
        },
        //验证内容
        checkGame:function (getsrt) {
            var gameName = $("input:text[name=gameName]").val();
            var gamelineTrNum = 0;
            var errorSwitch = 0;
            if (gameName != undefined) {
                if (gameName != "" && gameName != getsrt.gameName) {
                    var gameNum = $("#gameRoles dt").size();
                    $(".addgameshowlist h3").each(function () {
                        if ($(this).text() == gameName) {
                            var trNum = $(this).parent().find("ul li").size();
                            gamelineTrNum = gameNum + trNum;
                            if (gamelineTrNum > 2) {
                                if ($("#gameBtnErrText").length == 0) {
                                    $("#gameBtn").append("<div style='color:#FF8773; font-size:12px;' id='gameBtnErrText'>你已添加过该游戏的角色，每个游戏下的角色数不能超过20个,您已添加了" + trNum + "个</div>");
                                } else {
                                    $("#gameBtnErrText").text("你已添加过该游戏的角色，每个游戏下的角色数不能超过20个,您已添加了" + trNum + "个");
                                }
                                errorSwitch++;
                            } else {
                                $("#gameBtnErrText").remove();
                            }
                            return false;
                        }
                    })
                    //验证游戏名称
                    $("#gamesrterror span").text("");
                    if (gameName.length > 20) {
                        $("#gamesrterror").text('请控制在20字以内');
                        $("#gamesrterror").css({"color":"#FF8773"});
                        $("#gamesrterror").parent().find('input').focus();
                        errorSwitch++;
                    } else {
                        //敏感字过滤

                        if (!veifyPost.verifyPost(gameName)) {
                            $("#gamesrterror").text(tipsText.userSet.user_word_illegl);
                            $("#gamesrterror").css({"color":"#FF8773"});
                            $("#gamesrterror").parent().find('input').focus();
                            errorSwitch++;
                        } else {
                            $("#gamesrterror").text('');
                        }
                    }
                } else {
                    $("#gamesrterror").text(' 请输入一个游戏名称');
                    $("#gamesrterror").css({"color":"#FF8773", "font-size":"12px"});
                    $("#gamesrterror").parent().find('input').focus();
                    errorSwitch++;
                }
            }
            if (errorSwitch == 0) {
                $("#gameRoles dt").each(function (i) {
                    var serverAreas = $(this).find("input:eq(0)").val();
                    var roleNmaes = $(this).find("input:eq(1)").val();
                    var guilds = $(this).find("input:eq(2)").val();
                    if (serverAreas != "" && serverAreas != getsrt.serverAreas || roleNmaes != "" && roleNmaes != getsrt.roleNames || guilds != "" && guilds != getsrt.guilds) {//如果都不为空,才进行下一次判断
                        if (common.strLen(serverAreas) > 10) {
                            $(this).find("input:eq(0)").focus();
                            $(this).next('dd').find('.e1').html('请控制在10字以内');
                            errorSwitch++;
                        } else {
                            //敏感字过滤
                            if (!veifyPost.verifyPost(serverAreas)) {
                                $(this).find("input:eq(0)").focus();
                                $(this).next('dd').find('.e1').html(tipsText.userSet.user_word_illegl);
                                errorSwitch++;
                            } else {
                                $(this).next('dd').find('.e1').html('');
                            }
                        }
                        if (common.strLen(roleNmaes) > 16) {
                            $(this).find("input:eq(1)").focus();
                            $(this).next('dd').find('.e2').html('请控制在16字以内');
                            errorSwitch++;
                        } else {
                            //敏感字过滤
                            if (!veifyPost.verifyPost(roleNmaes)) {
                                $(this).find("input:eq(1)").focus();
                                $(this).next('dd').find('.e2').html(tipsText.userSet.user_word_illegl);
                                errorSwitch++;
                            } else {
                                $(this).next('dd').find(".e2").html('');
                            }
                        }
                        if (common.strLen(guilds) > 20) {
                            $(this).find("input:eq(2)").focus();
                            $(this).next('dd').find('.e3').html('请控制在20字以内');
                            errorSwitch++;
                        } else {
                            //敏感字过滤
                            if (!veifyPost.verifyPost(guilds)) {
                                $(this).find("input:eq(2)").focus();
                                $(this).next('dd').find('.e3').html(tipsText.userSet.user_word_illegl);
                                errorSwitch++;
                            } else {
                                $(this).next('dd').find(".e3").html('');
                            }
                        }
                        $("#gameBtnErrText").remove();
                    } else {
                        if (i != 0) {
                            $(this).remove();
                        } else {
                            if ($(this).next("li").size() == 0) {
                                $(this).find("input:eq(0)").focus();
                                if ($("#gameBtnErrText").length == 0) {
                                    $("#gameBtn").append("<div style='color:#FF8773; font-size:12px; line-height: 24px;' id='gameBtnErrText'>请至少输入一条角色信息</div>");
                                } else {
                                    $("#gameBtnErrText").text("请至少输入一条角色信息");
                                }
                                errorSwitch++;
                            } else {
                                $(this).remove();
                            }
                        }
                    }
                });
            }

            if (errorSwitch == 0) {
                return true;
            } else {
                return false;
            }
        },
        //更新游戏
        uploadGame:function (tagId, getsrt) {
            $("#getDate_" + tagId).find("table").remove()//当前容器
            if (this.checkGame(getsrt)) {
                var gameArray = getArrayForInput(getsrt);//获取input所有内容
                var gameData = {gameName:$("#gamesrt div dl dt h3").text(), gameProperty:gameArray, id:tagId};
                var _this = this;
                $('#form_game').ajaxForm(function (data) {
                    var jsonObj = eval('(' + data + ')');
                    common.locationLoginByJsonObj(jsonObj);
                    if (jsonObj.status_code == "1") {
                        _this.showUpdateGame(gameData);
                        if ($(".addgameshowlist").size() < 50) {
                            $("#addGame").show();
                        }
                    }
                    var alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                    joymealert.alert(alertOption);
                });
                $('#form_game').submit(); //表单提交。
            } else {
                return;
            }
        },
        //显示更新游戏
        showUpdateGame:function (gameData) {
            var gameTrStr = '';
            $.each(gameData.gameProperty, function (i, val) {
                gameTrStr += '<li><p>' + val[0] + '</p><p class="pcenter">' + val[1] + '</p><p class="pcenter">' + val[2] + '</p></li>'
            });
            var gameLineStr = ' <div id="gameText_' + gameData.id + '">' +
                '<h3>' + gameData.gameName + '</h3> <span><a name="updateGame" id="' + gameData.id + '" class="schcool_change">修改</a></span><ul>' + gameTrStr +
                '</ul></div>';
            $("#getDate_" + gameData.id).html(gameLineStr);
        },
        delGameTag:function (tagId) {
            $.ajax({
                url:"/json/profile/palyedgame/del",
                type:"post",
                data:"playedGameId=" + tagId,
                success:function (data) {
                    var jsonObj = eval('(' + data + ')');
                    var alertOption = {};
                    if (jsonObj.status_code == "1") {
                        $("#getDate_" + tagId).slideUp().remove();
                        $("#addGame").show();
                        alertOption = {text:jsonObj.msg, tipLayer:true};
                    } else {
                        alertOption = {text:jsonObj.msg, tipLayer:true, textClass:"tipstext"};
                    }
                    joymealert.alert(alertOption);
                }
            })
        },
        unBind:function (jqObj) {
            var verify = true;
            var verifyMsg = '';
            $.ajax({
                url:"/json/profile/sync/verifyunbind",
                type:"post",
                data:{apicode:jqObj.attr('data-apicode')},
                async:false,
                success:function (data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj.status_code != "1") {
                        verifyMsg = jsonObj.msg;
                        verify = false;
                    }

                }
            });
            if (!verify && verifyMsg.length > 0) {
                var alertOption  = {text:verifyMsg,tipLayer:true,textClass:"tipstext",alertFooter:true};
                joymealert.alert(alertOption);
                return false;
            }

            var offSet = jqObj.offset();
            var tips = '确定要取消绑定吗?'
            var confirmFunction = function () {
                window.location.href = jqObj.attr("data-href");
            }
            var confirmOption = {
                confirmid:"unbind",
                offset:"Custom",
                offsetlocation:[offSet.top, offSet.left],
                text:tips,
                width:229,
                submitButtonText:'确 定',
                submitFunction:confirmFunction,
                cancelButtonText:'取 消',
                cancelFunction:null};
            joymealert.confirm(confirmOption);
        }

    }
    var getArrayForInput = function (getsrt) {
        var gameArray = []
        $("#gameRoles dt").each(function (i) {
            var tdDom = $(this).find("input");
            gameArray[i] = [
                $(tdDom).eq(0).val() == getsrt.serverAreas ? eval('$(tdDom).eq(0).val("-").css("color","#555"); $(tdDom).eq(0).val()') : $(tdDom).eq(0).val(),
                $(tdDom).eq(1).val() == getsrt.roleNames ? eval('$(tdDom).eq(1).val("(无角色信息)").css("color","#555");$(tdDom).eq(1).val()') : $(tdDom).eq(1).val(),
                $(tdDom).eq(2).val() == getsrt.guilds ? eval('$(tdDom).eq(2).val("-").css("color","#555");$(tdDom).eq(2).val()') : $(tdDom).eq(2).val()
            ]
        });

        return gameArray;
    }
    var getArrayForTable = function (tagId, getsrt) {
        var gameArray = [];
        $("#getDate_" + tagId + " ul li").each(function (i) {
            var tdDom = $(this).find("p");
            gameArray[i] = [
                $(tdDom).eq(0).text() == "-" ? getsrt.serverAreas : $(tdDom).eq(0).text(),
                $(tdDom).eq(1).text() == "(无角色信息)" ? getsrt.roleNames : $(tdDom).eq(1).text(),
                $(tdDom).eq(2).text() == "-" ? getsrt.guilds : $(tdDom).eq(2).text()
            ];
        });
        return gameArray;
    }

    var updataRole = function (gameData, getsrt) {
        var gameArray = gameData.gameProperty;
        var roleStr = "";
        if (gameArray.length > 0) {
            $.each(gameArray, function (i, val) {
                var roleStyle = val[0] == getsrt.serverAreas ? "style=\"color:#cccccc\"" : "style=\"color:#5b5b5b\"";
                var serverStyle = val[1] == getsrt.roleNames ? "style=\"color:#cccccc\"" : "style=\"color:#5b5b5b\""
                var guildStyle = val[2] == getsrt.guilds ? "style=\"color:#cccccc\"" : "style=\"color:#5b5b5b\""
                roleStr += '<dt>' +
                    '<input type="text"  class="setaddusertext" ' + roleStyle + ' name="serverAreas" value="' + val[0] + '">' +
                    '<b>-</b>' +
                    '<input type="text" class="setaddusertext" ' + serverStyle + ' name="roleNames" value="' + val[1] + '">' +
                    '<b>‾</b>' +
                    '<input type="text" class="setaddusertext" ' + guildStyle + ' name="guilds" value="' + val[2] + '">' +
                    ' <a id="removeGameTag" class="close" href="javascript:void(0)"></a>' +
                    '</dt>' +
                    '<dd><span class="e1"></span><span class="e2"></span><span class="e3"></span></dd>';
            })
        }
        $("#gameRoles").append(roleStr);
    }
    return customize;
});
