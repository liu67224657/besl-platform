define(function(require, exports, module) {
    //一句话
    var PHRASE = 1;
    //是否有图片
    var IMAGE = 2;
    //是否有音乐
    var AUDIO = 4;
    //是否有视频
    var VIDEO = 8;
    //是否是长文
    var TEXT = 16;
    //APP
    var APP = 32;

    //全部
    var ALL = 63;
    var contentType = {
        hasPhrase:function(typeValue) {
            return (typeValue & PHRASE)>0;
        },
        hasText:function(typeValue) {
            return (typeValue & TEXT)>0;
        },
        hasImage:function(typeValue) {
            return (typeValue & IMAGE)>0;
        },
        hasAudio:function(typeValue) {
            return (typeValue & AUDIO)>0;
        },
        hasVideo:function(typeValue) {
            return (typeValue & VIDEO)>0;
        },
        hasApp:function(typeValue) {
            return (typeValue & APP)>0;
        },
        onlyImage:function(typeValue) {
            return typeValue == TEXT + IMAGE || typeValue == PHRASE + IMAGE;
        },
        onlyApp:function(typeValue) {
            return typeValue == TEXT + APP || typeValue == PHRASE + APP;
        },onlyAudio:function(typeValue) {
            return typeValue == TEXT + AUDIO || typeValue == PHRASE + AUDIO;
        },onlyVideo:function(typeValue) {
            return typeValue == TEXT + VIDEO || typeValue == PHRASE + VIDEO;
        }
    }
    return contentType;
});