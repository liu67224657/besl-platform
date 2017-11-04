/* Demo Note:  This demo uses a FileProgress class that handles the UI for displaying the file name and percent complete.
 The FileProgress class is not part of SWFUpload.
 */


/* **********************
 Event Handlers
 These are my custom event handlers to make my
 web application behave the way I went when SWFUpload
 completes different tasks.  These aren't part of the SWFUpload
 package.  They are part of my application.  Without these none
 of the actions SWFUpload makes will show up in my application.
 ********************** */
define(function (require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
    var joymealert = require('../../js/common/joymealert');
    var replyImage = require('../../js/page/reply-image');

    var handler = {
        fileQueueError:function (file, errorCode, message) {
            try {
                var errorName = "";
                if (errorCode === SWFUpload.errorCode_QUEUE_LIMIT_EXCEEDED) {
                    errorName = "You have attempted to queue too many files.";
                }
                if (errorName !== "") {
                    joymealert.alert({text:errorName});
                    return;
                }
                switch (errorCode) {
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                        break;
                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                        // too big
                        replyImage.showUploadErrorTips('您的图片超过3M，请重新选择');
                        this.cancelQueue();
                        break;
                    case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                        replyImage.showUploadErrorTips('只能上传1张图片');
                        this.cancelQueue();
                        break;
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    default:
                        //alert(message);
                        break;
                }

            } catch (ex) {
                this.debug(ex);
            }
        },
        preLoad:function () {
            if (!this.support.loading) {
                alert("You need the Flash Player 9.028 or above to use SWFUpload.");
                return false;
            }
        },

        loadFailed:function () {
            alert("Something went wrong while loading SWFUpload. If this were a real application we'd clean up and then give you an alternative");
        },

        fileDialogComplete:function () {
            if (checkUploadNum(this.customSettings.uploadId)) {
                this.startUpload();
            }
        },

        uploadStart:function (file) {
            try {
                // 上传中... init
                replyImage.replyUploadImageStart(this.customSettings.uploadId);
                this.customSettings.progressCount = 0;
            }
            catch (ex) {
                this.debug(ex);
            }
        },

        uploadSuccess:function (file, serverData) {
//            alert('uploadSuccess');
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    replyImage.replyUploadSuccess(file.name, jsonData.result[0],this.customSettings.uploadId);
                }
            } catch (ex) {
                this.debug(ex);
            }
        },

        uploadComplete:function (file) {
            try {
                if (window.replyswfu.getStats().files_queued > 0) {
                    window.replyswfu.cancelQueue();
                    window['imageUploadFlag'] = false;
                    return false;
                }
                window.replyswfu.customSettings.uploadCallback();
            } catch (ex) {
                alert(ex)
                replyswfu.debug(ex);
            }
        },

        updateDisplay:function (file) {
            this.customSettings.tdCurrentSpeed.innerHTML = SWFUpload.speed.formatBPS(file.currentSpeed);
            this.customSettings.tdAverageSpeed.innerHTML = SWFUpload.speed.formatBPS(file.averageSpeed);
            this.customSettings.tdMovingAverageSpeed.innerHTML = SWFUpload.speed.formatBPS(file.movingAverageSpeed);
            this.customSettings.tdTimeRemaining.innerHTML = SWFUpload.speed.formatTime(file.timeRemaining);
            this.customSettings.tdTimeElapsed.innerHTML = SWFUpload.speed.formatTime(file.timeElapsed);
            this.customSettings.tdPercentUploaded.innerHTML = SWFUpload.speed.formatPercent(file.percentUploaded);
            this.customSettings.tdSizeUploaded.innerHTML = SWFUpload.speed.formatBytes(file.sizeUploaded);
            this.customSettings.tdProgressEventCount.innerHTML = this.customSettings.progressCount;

        }
    }
    function checkUploadNum(objId) {
            if ($("#" + objId).find(".preview").length > 0) {
                showUploadErrorTips('只能上传1张图片');
                return false;
            } else {
                return true;
            }
        }
    function showUploadErrorTips(tips) {
            if ($("#" + replyUploadImageDialog).find(".piccontext").find(".tipstext").length > 0) {
                $("#" + replyUploadImageDialog).find(".piccontext").find(".tipstext").html(tips);
            } else {
                $("#" + replyUploadImageDialog).find(".piccontext").append('<p class="tipstext" id="">' + tips + '</p>');
            }
        }
    return handler;
})