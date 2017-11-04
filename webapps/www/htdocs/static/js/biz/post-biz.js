/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-7
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */

define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postbiz = {
        searchAudio:function(audioCode, event, ctx, page, elementIds, callback2) {
            $.post(ctx + '/json/content/parseaudio', {audioStr:audioCode,page:page}, function(req) {
                $("#" + elementIds.songList).children().remove();
                $("#" + elementIds.songInfo).children().remove();
                var resultMsg = eval('(' + req + ')');

                callback2(audioCode, event, ctx, page, elementIds, resultMsg, callback2);
            });
        } ,
        searchVideo:function(url, ctx, callback, infoId, callback2, oEditorVideoDialog) {
            $.ajax({
                        url:ctx + '/json/content/parsevideo',
                        data:{url:url},
                        type:'post',
                        beforeSend:function() {
                            if (window.loginLock) {
                                return false;
                            }
                            window.loginLock = true;
                        },
                        success:function(data) {
                            var msg = eval('(' + data + ')');
                            if (msg.status_code == 1) {
                                var video = msg.result[0];
                                if (video == null) {
                                    $("#" + infoId).text('视频格式错误');
                                    return;
                                }
                                if (typeof callback2 == "function") {
                                    callback2(url, ctx, callback, infoId, msg);
                                } else {
                                    callback(msg.result[0], oEditorVideoDialog);
                                }
                            }
                        },
                        complete:function(req, paramObj) {
                            window.loginLock = false;
                        }
                    })
        } ,
        searchTag:function(showOption, tagtipsid, callback, flag) {

            if (flag) {
                $.post('/json/profile/tag/list/post', function(data) {
                    var msg = eval('(' + data + ')')
                    callback(msg, tagtipsid, showOption);
                })
            } else {
                callback(null, tagtipsid, showOption);
            }
        },
        searchAt:function(atval, divid, loadingCallback, callback) {
            $.post("/json/atme/search/focus", {nick:atval}, function(data) {
                var jsonObj = eval('(' + data + ')');
                callback(jsonObj, divid);
            });
            $.ajax({
                        url:'/json/atme/search/focus' ,
                        type:'POST',
                        data: {nick:atval},
                        success:callback,
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(divid);
                            }
                        }
                    });
        },
        uploadImgLink:function(linkUrl, beforeSendCallback, successCallback) {
            $.ajax({
                        url:'/json/upload/imagelink' ,
                        type:'POST',
                        data:{url:linkUrl},
                        success:successCallback,
                        beforeSend:beforeSendCallback
                    });
        },
        uploadReplyImgLink:function(linkUrl,uploadId, beforeSendCallback, successCallback) {
            $.ajax({
                url:'/json/upload/replyimagelink' ,
                type:'POST',
                data:{url:linkUrl},
                success:function(req) {
                    successCallback(req,uploadId);
                },
                beforeSend:beforeSendCallback(uploadId)
            });
        },
        verifyPost:function(value) {
            var result = true;
            $.ajax({
                        url: "/json/validate/sysword",
                        type:'post',
                        data:{word:value},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        },
        checkAtSize:function(context, callback1, callback2, successCallback, homeOption) {
            $.ajax({
                        url: "/json/atme/size",
                        type:'post',
                        data:{context:context},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                callback1(context, successCallback, homeOption);
                            } else {
                                callback2(context, successCallback, homeOption);
                            }
                        }
                    });
        }  ,
        checkAtNicks:function(context, callback, successCallback, homeOption) {
            $.ajax({
                        url: "/json/atme/nicks",
                        type:'post',
                        data:{context:context},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '1') {
                                callback(resMsg, homeOption);
                            } else {
                                successCallback(homeOption);
                            }
                        }
                    });
        },
        uploadios:function(url, callback) {
            $.ajax({
                        url: "/json/content/app/ios",
                        type:'post',
                        data:{url:url},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                $('#error_app').text(resMsg.msg);
                            }
                            callback(resMsg.result[0], url);
                        }
                    });
        },
        uploadIosInsertEditor :function(appObj, screenShot, deviceSet, iosContentDesc, url, uploadAppSuccess, uploadAppError) {
            $.ajax({
                        url: "/json/upload/app/ios",
                        type:'post',
                        data:{appid:appObj.appid,
                            appName:appObj.appName,
                            icon:appObj.icon,
                            price:appObj.price,
                            fileSize:appObj.fileSize,
                            appCategory:appObj.appCategory,
                            currentRating:appObj.currentRating,
                            currentRatingCount:appObj.currentRatingCount,
                            totalRating:appObj.totalRating,
                            totalRatingCount:appObj.totalRatingCount,
                            publishDate:appObj.publishDate,
                            appVersion:appObj.appVersion,
                            language:appObj.language,
                            develop:appObj.develop,
                            devices:deviceSet.join(','),
                            iosDesc:appObj.desc,
                            resourceUrl:appObj.resourceUrl,
                            screenShot:screenShot.join(',')},
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            if (resultMsg.status_code == '1') {
                                uploadAppSuccess(resultMsg, iosContentDesc, url, appObj);
                            } else {
                                uploadAppError(resultMsg);
                            }
                        }

                    });
        },
        insertCkimagePost:function(imageArray, insertimagecallback, i, aftertext, pasteObj) {
            $.ajax({
                        url:'/json/upload/imagelink' ,
                        type:'POST',
                        data:{url:imageArray[i]['srcKey']},
                        success:function(data) {
                            var resultMsg = eval('(' + data + ')');
                            if (resultMsg.status_code[0] != 0) {
                                ajaxData = resultMsg.result[0];
                                insertimagecallback(imageArray, insertimagecallback, ++i, aftertext, pasteObj, ajaxData);
                            } else {
                                ajaxData = resultMsg.result[0];
                                insertimagecallback(imageArray, insertimagecallback, ++i, aftertext, pasteObj, ajaxData);
                            }
                        },
                        beforeSend:function() {

                        }
                    });
        },
        getBindSync:function() {
            var resultMsg = ''
            $.ajax({
                        url:'/json/profile/sync/getprovider' ,
                        type:'POST',
                        async:false,
                        success:function(data) {
                            resultMsg = eval('(' + data + ')');
                        }
                    });
            return resultMsg;
        }
    }
    return postbiz;
})