/*
 Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
    // Define changes to default configuration here. For example:
    config.language = 'zh-cn';
    config.toolbarStartupExpanded = false;
    config.toolbarCanCollapse = false;
    config.resize_enabled = false;
    config.startupFocus = false;
    config.removePlugins = 'elementspath';
    config.enterMode = CKEDITOR.ENTER_BR;
//    config.forcePasteAsPlainText =true;
    config.ignoreEmptyParagraph = true;
    config.entities = false;
    config.disableObjectResizing = true;
    config.height = 176;
//    config.extraPlugins = "moody,joymevideo,joymeaudio,joymephoto,joymesplit,joymeimagelink,joymeat,joymeios";
    config.autoUpdateElement = true;
    config.pasteFromWordRemoveStyles = true;
    config.pasteFromWordPromptCleanup = true;
    config.pasteFromWordRemoveFontStyles = true;
    config.protectedSource.push(/(<img)+?(?![^<>]*?joymet[^<>]*?>).*?>/g);
    config.autoGrow_minHeight = 190;
//    config.menu_groups ='';
//    config.removeFormatTags='a';
//     config.protectedSource.push( /<[\s\S]*?p[\s\S]*?>/g );

};
