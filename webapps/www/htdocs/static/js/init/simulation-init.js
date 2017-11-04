/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-6-9
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */
define(function(require) {
    var $ = require('../common/jquery-1.5.2')
    var header = require('../page/header.js');
    var simulationArray = {
        city:'',
        sex:'',
        age:'',
        bodytype:''
    };
    var fleeArray = {
        robber:'',
        banditlair:''
    }
    $().ready(function() {
        header.searchReTopInit();
        $("#tym>ul>li,#flee>ul>li").live('click', function() {
            var olLine = $(this).children('ol');
            if (olLine.is(":hidden")) {
                olLine.show();
                return;
            } else {
                olLine.hide();
                return;
            }
        })
        $("#tym>ul>li>ol li").live('click', function() {
            simulationArray[$(this).find('a').attr('type')] = $(this).index();
            $(this).parent().parent().find('span').show().text($(this).find('a').text());
        });
        $("#flee>ul>li:eq(0)>ol li").live('click', function() {
            fleeArray[$(this).find('a').attr('type')] = $(this).index();
            $(this).parent().parent().find('span').show().text($(this).find('a').text());
            $("#flee>ul>li:eq(2)").find('span').hide();
            fleeArray['banditlair'] = "";
            var fleeLi = $("#flee>ul>li:eq(2)>ol li");
            fleeLi.css('display', 'none');
            fleeLi.eq($(this).index() * 2).css("display", "block");
            fleeLi.eq($(this).index() * 2).next().css("display", "block");
        })
        $("#flee>ul>li:eq(2)>ol li").live('click', function() {
            fleeArray[$(this).find('a').attr('type')] = $(this).index();
            $(this).parent().parent().find('span').show().text($(this).find('a').text());
        })

        $("#clue").bind('click', function() {
            var simNum = '';
            $("#pic1").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/waitflee.jpg')
            if (eachObj(simulationArray)) {
                for (var i in simulationArray) {
                    if (simulationArray[i] + "" == "" && i == 'city') {
                        $("#pic1").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullCity.jpg')
                        return;
                    }
                    if (simulationArray[i] + "" == "" && i == 'sex') {
                        $("#pic1").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullSex.jpg')
                        return;
                    }
                    if (simulationArray[i] + "" == "" && i == 'age') {
                        $("#pic1").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullAge.jpg')
                        return;
                    }
                    if (simulationArray[i] + "" == "" && i == 'bodytype') {
                        $("#pic1").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullBodyType.jpg')
                        return;
                    }
                }
            } else {
                setTimeout(function() {
                    simNum = simulationArray.city + "" + simulationArray.sex + "" + simulationArray.age + "" + simulationArray.bodytype;
                    $("#pic1").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/' + simNum + '.jpg');
                    $("#pic1").find('img').error(function() {
                        $(this).attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/null.jpg');
                    })
                    //simulationArray = eachObjNull(simulationArray); //清空查询结果
                }, 3000)
            }
        })
        $("#robberSub").bind('click', function() {
            var simNum = '';
            $("#pic2").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/waitrobber.jpg')
            if (eachObj(fleeArray)) {
                for (var i in fleeArray) {
                    if (fleeArray[i] + "" == "" && i == 'robber') {
                        $("#pic2").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullrobberName.jpg')
                        return;
                    }
                    if (fleeArray[i] + "" == "" && i == 'banditlair') {
                        $("#pic2").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullrobberMap.jpg')
                        return;
                    }
                }
            } else {
                setTimeout(function() {
                    simNum = fleeArray.robber + "" + fleeArray.banditlair;
                    $("#pic2").find('img').attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/' + simNum + '.jpg');
                    $("#pic2").find('img').error(function() {
                        $(this).attr('src', joyconfig.URL_LIB + '/static/img/tysimulation/nullrobber.jpg');
                    })
                    //fleeArray = eachObjNull(fleeArray);
                }, 3000)
            }
        })
    })
    function eachObj(obj) {
        var flag = false;
        for (var i in obj) {
            if (obj[i].toString() == '') {
                flag = true;
                break;
            }
        }
        return flag;
    }

    function eachObjNull(obj) {
        for (var i in obj) {
            obj[i] == '';
        }
        return obj;
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm');
})