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
function preLoad() {
	if (!this.support.loading) {
		alert("You need the Flash Player 9.028 or above to use SWFUpload.");
		return false;
	}
}
function loadFailed() {
	alert("Something went wrong while loading SWFUpload. If this were a real application we'd clean up and then give you an alternative");
}

function fileQueued(file) {
	try {
		this.customSettings.tdFilesQueued.innerHTML = this.getStats().files_queued;
	} catch (ex) {
		this.debug(ex);
	}

}

function fileDialogComplete() {
	this.startUpload();
}

function uploadStart(file) {
	try {
		this.customSettings.progressCount = 0;
        if($('.touxiang_set_box').is(":hidden")){
            showadv();
        }else{
            resetAdv();
        }
		updateDisplay.call(this, file);
	}
	catch (ex) {
		this.debug(ex);
	}
	
}

function uploadProgress(file, bytesLoaded, bytesTotal) {
	try {
		this.customSettings.progressCount++;
		updateDisplay.call(this, file);
	} catch (ex) {
		this.debug(ex);
	}
	
}

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


function uploadSuccess(file, serverData) {
	try {
		var jsonData =  eval('(' + serverData + ')');
		$("#face_img_id").attr("src", genImgDomain(jsonData.result[0].b,this.customSettings.resource_path));
		$("#face_img_id_m").attr("src", genImgDomain(jsonData.result[0].m,this.customSettings.resource_path));
		$("#tumName").val(jsonData.result[0].b);
		$("#preview").attr("src", genImgDomain(jsonData.result[0].b,this.customSettings.resource_path));
		jcrop_api.destroy();
	
		//updateDisplay.call(this, file);
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
    initJcrop();
	//this.customSettings.tdFilesUploaded.innerHTML = this.getStats().successful_uploads;
	//this.customSettings.tdErrors.innerHTML = this.getStats().upload_errors;

}

function updateDisplay(file) {
	this.customSettings.tdCurrentSpeed.innerHTML = SWFUpload.speed.formatBPS(file.currentSpeed);
	this.customSettings.tdAverageSpeed.innerHTML = SWFUpload.speed.formatBPS(file.averageSpeed);
	this.customSettings.tdMovingAverageSpeed.innerHTML = SWFUpload.speed.formatBPS(file.movingAverageSpeed);
	this.customSettings.tdTimeRemaining.innerHTML = SWFUpload.speed.formatTime(file.timeRemaining);
	this.customSettings.tdTimeElapsed.innerHTML = SWFUpload.speed.formatTime(file.timeElapsed);
	this.customSettings.tdPercentUploaded.innerHTML = SWFUpload.speed.formatPercent(file.percentUploaded);
	this.customSettings.tdSizeUploaded.innerHTML = SWFUpload.speed.formatBytes(file.sizeUploaded);
	this.customSettings.tdProgressEventCount.innerHTML = this.customSettings.progressCount;

}