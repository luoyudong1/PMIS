//公共方法
define(['jquery', 'datetimepicker'], function ($) {
    //正则表达式 - 验证数字
    const reg = new RegExp("^\\d+(\\.\\d+)?$");

    //初始化时间框
    let initDatetimepicker = function (id, date) {
        let date_now = new Date();
        if (date == null) {
            date = date_now
        }
        $.datetimepicker.setLocale('ch');
        $('#' + id).datetimepicker({
            value: date,
            format: 'Y-m-d',
            timepicker: false, //关闭时间选项
            todayButton: true //关闭选择今天按钮
        });
    }
    //初始化时间框
    let initDatetimepickerWithSecond = function (id, date, format) {
        let date_now = new Date();
        // if (date == null) {
        //     date = date_now
        // }
        $.datetimepicker.setLocale('ch');
        $('#' + id).datetimepicker({
            value: date,
            format: format,
            timepicker: true, //关闭时间选项
            todayButton: true,//关闭选择今天按钮
            minView: 0,
            minuteStep:1,
        });
    }
    //初始化仓库信息
    let initStorehouseInfo = function (config, ele_id) {
        $('#' + ele_id).find("option").remove();
        //初始化仓库查询项
        $.ajax({
            async: false,
            url: config.basePath + "/system/storehouse/getUserStorehouseByUserId",
            data: {},
            dataType: 'json',
            success: function (result) {
                if (result.length > 0) {
                    for (i = 0; i < result.length; i++) {
                        $("#" + ele_id).append('<option value="' + result[i].storehouse_id + '">' + result[i].storehouse_name + '</option>');
                    }
                } else {
                    $("#" + ele_id).append('<option value="-1">请选择</option>');
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    }

    //验证是否是数字
    let checkNumber = function (num) {
        if (reg.test(num)) {
            return true;
        } else {
            return false;
        }
    }
    //定时隐藏alert框
    let hideTimeout = function (id_html, id_hide, ms) {
        var time = setTimeout(function () {
            $("#" + id_html).html("");
            $("#" + id_hide).hide();
        }, ms)
        return time;
    }

    let checkIsNotBlank = function (value) {
        if (value) {
            return true;
        } else {
            return false;
        }
    }

    return {
        initDatetimepicker: initDatetimepicker,
        initDatetimepickerWithSecond: initDatetimepickerWithSecond,
        initStorehouseInfo: initStorehouseInfo,
        checkNumber: checkNumber,
        hideTimeout: hideTimeout,
        checkIsNotBlank: checkIsNotBlank
    };

});