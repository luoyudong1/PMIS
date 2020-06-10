/**
 * 故障预报
 */
require(['../config'],
    function (config) {

        require(['datetimepicker2'],
            function () {
                var date = new Date();
                $.fn.datetimepicker.dates['zh-CN'] = {
                    days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
                    daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
                    daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
                    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                    monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                    today: "今天",
                    suffix: [],
                    meridiem: ["上午", "下午"]
                };
                /**
                 * 初始化时间框
                 */
                function initDatetimepicker(id, date,format) {
                    $('#' + id).datetimepicker({
                        format: format,
                        // timepicker: false,
                        // forceParse:false,
                        // // 关闭时间选项
                        autoclose: true,
                        startView: 3, //这里就设置了默认视图为年视图
                        minView: 3, //设置最小视图为年视图
                        language:'zh-CN'

                    });
                }
                function getDay() {
                    var today = new Date();
                    var oYear = today.getFullYear();
                    var oMoth = (today.getMonth() + 1).toString();
                    if (oMoth.length <= 1)
                        oMoth = '0' + oMoth;
                    console.log(oYear + '-' + oMoth)
                    return oYear + '-' + oMoth;
                }
                initDatetimepicker("queryTime", date,'yyyy-mm');
                $('#queryTime').val(getDay())
            });

        require(['jquery', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin'],
            function ($, bootstrap, dataTable, CMethod) {


                var verify_flag = ''; // 查询的审核状态

                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                var roleName = window.parent.user.roleName; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var dispatcher = window.parent.user.dispatcher // 所属部门id
                var id;
                let sheet_id = ''
                var format = 'Y-m-d H:i:s';
                var status;
                let detectDeviceId='';
                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                });

                /**
                 * 重置
                 */
                $('#btn-reset').click(function (e) {
                    $.ajax({
                        async: false,
                        url: config.basePath + "/checkPlan/checkPlan/finished",
                        type: 'get',
                        data: {},
                        dataType: 'json',
                        success: function (result) {
                            if (result.code != 0) {
                                alert(result.msg);
                            } else {
                                sheetTable.ajax.reload();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检修计划归档成功！</strong></span>');
                                $("#infoAlert").show();
                                hideTimeout("infoAlert", 2000);
                            }
                        },
                        error: function (result) {
                            console.log(result);
                        }
                    });
                });


                /**
                 * 初始化检修单据详情
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/checkPlan/entry/listUnfulfilled',
                        type: 'GET',
                        data: function (d) {
                            d.dispatcher=$('#dispatcher', parent.document).val()
                            d.year=$('#queryTime').val().substring(0,4)
                            d.month=$('#queryTime').val().substring(5,7)
                            d.detectDeviceName = $('#detectDeviceName').val()
                            d.status = $('#verify_flag').val()
                        }
                    },
                    columns: [{
                        data: null
                    }, {
                        data: 'id', bVisible: false
                    },
                        {
                            data: 'sheetId', bVisible: false
                        },
                        {
                            data: 'detectDeviceId', bVisible: false
                        },
                        {
                            data: 'detectDeviceName'
                        },   {
                            data: 'detectDepotName'
                        },{
                            data: 'detectDeviceType'
                        },
                        {
                            data: 'planTime'
                        },
                        {
                            data: 'startTime'
                        }, {
                            data: 'endTime'
                        }, {
                            data: 'checkRecord'
                        },
                        {
                            data: 'repairUser'
                        },{
                            data: 'planType'
                        }, {
                            data: 'remark'
                        },
                        {
                            data: 'status',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '<span style="color:red;font-weight:bold;">新建</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:red;font-weight:bold;">未兑现</span>';
                                }
                                else if (data == 3) {
                                    str = '<span style="color:blue;font-weight:bold;">检修开始待审核</span>';
                                } else if (data == 4) {
                                    str = '<span style="color:blue;font-weight:bold;">检修开始</span>';
                                }
                                else if (data == 5) {
                                    str = '<span style="color:blue;font-weight:bold;">检修结束待审核</span>';
                                } else if (data == 6) {
                                    str = '<span style="color:black;font-weight:bold;">检修完成</span>';
                                }
                                return str;
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 15,
                        data: function (row) {
                            var str = '-';
                            if (row.status == 3 || row.status == 5) {
                                str = '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popBackSheetModal" title="回退"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                            }
                            if (row.status !=2) {
                                str += '<a class="btn btn-primary btn-xs undoSheet"data-toggle="modal" href="#popSheetUndoModal"  title="重置" > <span class="glyphicon glyphicon-repeat"></span></a>&nbsp;&nbsp;'
                            }
                            return str;
                        }
                    }],
                    ordering: false,
                    paging: true,
                    pageLength: 10,
                    serverSide: false,
                    fnCreatedRow: function (row, data) {
                        if (data.status== 5) {
                            $(row).css('color','red')
                        }
                    },
                    drawCallback: function (settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function (cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
                    },
                });
                /**
                 * 获取检修人员
                 */
                function getRepairUser(data) {
                    var ret = '';
                    $.ajax({
                        async: false,
                        url: config.basePath + "/faultHandle/faultReport/getRepairPerson",
                        type: 'get',
                        data: {detectDeviceId: data},
                        dataType: 'json',
                        success: function (result) {
                            ret = result;
                        },
                        error: function (result) {
                            console.log(result);
                        }
                    });
                    return ret;
                }

                /**
                 * 初始化检修人员下拉框
                 */
                function initRepairUser() {
                    if (detectDeviceId != null) {
                        let result = getRepairUser(detectDeviceId)
                        $("#repairUserModify").empty()
                        $("#repairUserModify").append('<option></option>')
                        for (var i = 0; i < result.length; i++) {
                            $("#repairUserModify").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                        }
                    }
                }
                /**
                 * 修改检修计划
                 */
                $('#modifySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#detectDeviceModify').val(data.detectDeviceName)
                        $('#detectTypeModify').val(data.detectDeviceType)
                        $('#planTypeModify').val(data.planType)
                        $('#planTimeModify').val(data.planTime)
                        $('#startTimeModify').val(data.startTime)
                        $('#endTimeModify').val(data.endTime)
                        $('#checkRecordModify').val(data.checkRecord)
                        initRepairUser()
                        if (data.repairUser != null) {//如果检修人员不为空
                            $('#repairUserModify option:contains("' + data.repairUser + '")').prop("selected", true);
                        }else{
                            $('#repairUserModify option:first').prop("selected", true);
                        }
                        $('#remarkModify').text(data.remark)
                    });



                /*******************************************************
                 * 单据点击事件
                 ********************************************************/
                $('#sheetTable tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前单号
                            if (sheetTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                var sheetTrData = sheetTable
                                    .row(tr).data();
                                status = sheetTrData.status
                                id = sheetTrData.id
                                sheet_id = sheetTrData.sheetId
                                detectDeviceId=sheetTrData.detectDeviceId
                            }

                        });


                /**
                 * 提交入库单据
                 */
                $('#popSheetVerifyModal')
                    .on(
                        'show.bs.modal',
                        function (e) {
                            var tr = $(e.relatedTarget)
                                .parents('tr');
                            var data = sheetTable.row(tr)
                                .data();
                            $('#warningSheetVerifyText').text(
                                '确定提交单据ID为:' + data.sheetId
                                + '的单据吗？');
                            $('#btnPopSheetVerifyOk').val(
                                data.sheetId);
                        });
                /**
                 * 修改按钮事件
                 */
                $("#btnModifySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let startTime = $('#startTimeModify').val()
                        let endTime = $('#endTimeModify').val()
                        let date = new Date()
                        if (startTime == '') {
                            $("#alertMsgAdd").html("<font style='color:red'>检修开始时间为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (Date.parse(startTime) > date) {
                            $("#alertMsgAdd").html("<font style='color:red'>检修开始时间大于当前时间</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (endTime == '') {
                            $("#alertMsgAdd").html("<font style='color:red'>检修结束时间为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if ( Date.parse(endTime) < Date.parse(startTime)) {
                            $("#alertMsgAdd").html("<font style='color:red'>检修结束时间小于检修开始时间！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        let params = JSON.stringify({
                            id: id,
                            sheetId: sheet_id,
                            planType: $('#planTypeModify').val(),
                            planTime: $('#planTimeModify').val(),
                            startTime: $('#startTimeModify').val(),
                            endTime: $('#endTimeModify').val(),
                            checkRecord: $('#checkRecordModify').val(),
                            remark: $('#remarkModify').val(),
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/update',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检修计划修改成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });

                /**
                 * 重置按钮事件
                 */
                $("#btnPopUndoSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: id,
                            sheetId: sheet_id,
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/undo',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>重置成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });
                /**
                 * 单据审核
                 */
                $("#btnPopSheetVerifyOk")
                    .on(
                        'click',
                        function (e) {
                            let verifyUser2;
                            let verifyUser4;
                            let verifyDate2 = null;
                            let verifyDate4 = null;
                            let date = formatDateBy(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss')
                            if (status == 3) {//提交人员1
                                verifyUser2 = user_id
                                verifyDate2 = date
                            } else if (status == 5) {//提交人员2
                                verifyUser4 = user_id
                                verifyDate4 = date
                            }
                            var params = JSON.stringify({
                                id: id,
                                status: status + 1,
                                verifyUser2, verifyUser2,
                                verifyDate2, verifyDate2,
                                verifyUser4, verifyUser4,
                                verifyDate4, verifyDate4,
                                sheetId: sheet_id
                            });
                            var params = JSON.stringify({
                                id: id,
                                status: 6,
                                sheetId: sheet_id,
                                finished:1
                            });
                            $.ajax({
                                url: config.basePath
                                    + '/checkPlan/checkPlan/update',
                                type: "post",
                                data: params,
                                contentType: 'application/json',
                                dataType: "json",
                                success: function (
                                    result) {
                                    if (result.code != 0) {
                                        alert(result.msg);
                                    } else {
                                        sheetTable.ajax
                                            .reload();
                                        $("#alertMsg")
                                            .html(
                                                '<span style="color:green;text-align:center"><strong>单据已提交,可在检修计划查询查看！</strong></span>');
                                        $("#infoAlert")
                                            .show();
                                        hideTimeout(
                                            "infoAlert",
                                            2000);
                                    }
                                }
                            });

                        });
                /**
                 * 点击回退确定按钮
                 */
                $('#btnPopBackSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: id,
                            status: 2
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/update',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障预报单据回退成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });

                    });
                /** *************** **/
                sheetTable.on('draw.dt', function () {
                    //给第一列编号
                    sheetTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });

                /**
                 * 定时隐藏alert框
                 */
                function hideTimeout(id, ms) {
                    var time = setTimeout(function () {
                            $("#" + id).hide();
                        },
                        ms)
                }

            });
    })
;