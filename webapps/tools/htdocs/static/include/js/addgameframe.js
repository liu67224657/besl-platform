frameOption = {
    resetFunc: function () {
    },
    buttonid: '',
    url: '/json/gameclient/clientline/toaddgame',
    selectFunc: function (item) {
    }
}

function gameframe(frameOption) {
    $("#" + frameOption.buttonid).click(function () {

        $("#reset").click(function () {
            $('.easyui-datetimebox').datetimebox('clear');
            frameOption.resetFunc();
            $("#form_submit :text").prop("value", '');
            $("#form_submit input[name='gameDbId']").prop("value", '');
        });

        $('#dgrules').datagrid({
            url: frameOption.url,
            pagination: true,
            rownumbers: false,
            singleSelect: true,
            width: 'auto',
            height: 'auto',
            striped: true,
            idField: 'appId',
            pageList: [10, 20, 30, 40],
            fitColumns: true,
            loadMsg: '数据加载中请稍后……',
            columns: [
                [
                    {field: 'gameDbId', title: 'game_db表的_id', width: 60},
                    {field: 'gameName', title: '游戏名称', width: 100},
                    {field: 'anotherName', title: '别名', width: 100},
                    {field: 'downloadRecommend', title: '下载推荐', width: 150},
                    {
                        field: 'gameIcon', title: '游戏主图', width: 60,
                        formatter: function (value, row, index) {
                            return "<img src=" + row.gameIcon + " width='40' height='40>'";

                        }
                    },
                    {
                        field: 'gamePublicTime', title: '上市时间', width: 100,
                        formatter: function (value, row, index) {
                            if (row.gamePublicTime != null && row.gamePublicTime.toString() != '') {
                                var time = new Date(row.gamePublicTime);
                                return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
                            }
                            else {
                                return '';
                            }

                        }
                    },
                    {
                        field: 'gameDevices', title: '游戏设备', width: 120,
                        formatter: function (value, row, index) {
                            var result = '';
                            for (var item in row.gameDBDevicesSet) {
                                if (row.gameDBDevicesSet[item].gameDbDeviceId == 1 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                    result += "iphone,"
                                } else if (row.gameDBDevicesSet[item].gameDbDeviceId == 2 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                    result += "ipad,"
                                } else if (row.gameDBDevicesSet[item].gameDbDeviceId == 3 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                    result += "android,"
                                }

                            }
                            //去掉最后的 逗号
                            if (result.length > 0 && result.charAt(result.length - 1) == ',') {
                                result = result.substr(0, result.length - 1)

                            }
                            return result;

                        }
                    },
                    {field: 'ck', checkbox: true}
                ]
            ]
        });

        $('#chooseWindow').window('open');
    });


//从gamedb 中读取的查询按钮
    $("#buttonInWindow").click(function () {
        var url = frameOption.url;
        if ($.trim($("#gameNameInWindow").val()) != '') {

            url += '&gameName=' + $.trim($("#gameNameInWindow").val());
        }
        $('#dgrules').datagrid('options').url = url;
        $("#dgrules").datagrid('reload');

    });


//弹窗中选择一个游戏后
    $("#selectone").click(function () {
        var item = $('#dgrules').datagrid('getSelected');
        frameOption.selectFunc(item);

        $('#chooseWindow').window('close');
    });
}


