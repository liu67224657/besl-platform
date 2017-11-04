define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    $(document).ready(function() {

        $('#uploadBuuton').click(function(){
            $('#upload_file').click();
        })

        $('#upload_file').change(function(){
            $('#uploadform').submit();
        })
    })

});