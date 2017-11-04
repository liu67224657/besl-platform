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
define(function(require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
    var headicon = require('../../js/page/headicon');
    var joymealert = require('../../js/common/joymealert')
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    var handler = {

        preLoad:function() {
            if (!this.support.loading) {
                alert("You need the Flash Player 9.028 or above to use SWFUpload.");
                return false;
            }
        },

        loadFailed:function () {
            alert("Something went wrong while loading SWFUpload. If this were a real application we'd clean up and then give you an alternative");
        },

        fileDialogComplete:function () {
            this.startUpload();
        },

        uploadStart:function (file) {
            try {
                if (headicon.checkFileSize(file)) {
                    headicon.resetHeadIcon();
//                    updateDisplay.call(this, file);
                } else {
                    //jmAlert("<div style='text-align: center; margin: 10px 0;'>上传的图片尺寸过大</div>", false, 2000);
                    alertOption.text = '上传的图片尺寸过大';
                    alertOption.title = '提示';
                    joymealert.alert(alertOption);
                }
                this.customSettings.progressCount = 0;

            }
            catch (ex) {
                this.debug(ex);
            }

        },

        uploadStartRegOk:function (file) {
            try {
                if (headicon.checkFileSize(file)) {
                    headicon.resetHeadIconRegOk();
//                    updateDisplay.call(this, file);
                } else {
                    jmAlert("<div style='text-align: center; margin: 10px 0;'>上传的图片尺寸过大</div>", false, 2000);
                }
                this.customSettings.progressCount = 0;

            }
            catch (ex) {
                this.debug(ex);
            }

        },

        uploadProgress:function (file, bytesLoaded, bytesTotal) {
            try {
                this.customSettings.progressCount++;
//                updateDisplay.call(this, file);
            } catch (ex) {
                this.debug(ex);
            }

        },

//生成图片地址
//function genImgDomain(imgpath,r_p) {
//
//	if (imgpath.indexOf("/r") || imgpath.indexOf("\\r")) {
//		var rxxx = imgpath.substr(1, 4);
////		var vr = imgpath.substr(5);
//        var vr = imgpath;
//		return "http://" + rxxx + "\." + r_p + vr;
//	}
//}


        uploadSuccess:function (file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    headicon.initHeadIcon(jsonData);
                }
            } catch (ex) {
                this.debug(ex);
            }
        },
        uploadSuccessRegOk:function(file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    headicon.initHeadIconRegOk(jsonData);
                }
            } catch (ex) {
                this.debug(ex);
            }
        },

        uploadComplete:function (file) {
//    initJcrop();
            //this.customSettings.tdFilesUploaded.innerHTML = this.getStats().successful_uploads;
            //this.customSettings.tdErrors.innerHTML = this.getStats().upload_errors;

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

    return handler;
})
