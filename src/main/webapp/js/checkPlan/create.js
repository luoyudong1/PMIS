/**
 * 故障预报
 */
require(['../config'],
    function (config) {

        require(['datetimepicker'],
            function () {
                var date = new Date();
                initDatetimepicker("queryTime", date);
                initDatetimepicker("queryTime2", date);

                /**
                 * 初始化时间框
                 */
                function initDatetimepicker(id, date) {
                    $.datetimepicker.setLocale('ch');
                    $('#' + id).datetimepicker({
                        value: date,
                        format: 'Y-m-d',
                        timepicker: false,
                        // 关闭时间选项
                        todayButton: true
                        // 关闭选择今天按钮
                    });
                }
            });

        require(['jquery', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin'],
            function ($, bootstrap, dataTable, CMethod) {


                var verify_flag = ''; // 查询的审核状态

                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                var roleName = window.parent.user.roleName; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var id;
                var completeFlag;
                var format = 'Y-m-d H:i:s';
                var detectDeviceId;
                var type;
                var detectDevice;
                var listDetect;
                let lineSet = new Set();
                let deviceTypeSet = new Set();
                let sheet_id = '';
                CMethod.initDatetimepickerWithSecond("planTimeAdd", null, format);
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
                    $('#sheet_id').val('');
                    $('#verify_flag').val('');
                    var now = new Date();
                    now.setDate(1);
                    $('#queryTime').val(formatDateBy(now.getTime(), 'yyyy-MM-dd'));
                    $('#queryTime2').val(formatDateBy(new Date(), 'yyyy-MM-dd'));
                });

                /**
                 * 显示线别
                 */
                function initLineAdd() {
                    for (let line of lineSet) {
                        $('#lineAdd').append('<option>' + line + '</option>')
                    }
                }

                /**
                 * 显示年份
                 */
                function initYearAdd() {
                    let date = new Date()
                    let year = parseInt(date.getFullYear().toString())
                    $('#yearAdd').append('<option>' + year + '</option>')
                    $('#yearAdd').append('<option>' + year + 1 + '</option>')

                }

                /**
                 * 显示年份
                 */
                function initDepotNameAdd() {
                    $.ajax({
                        async: false,
                        url: config.basePath + "/checkPlan/sheet/getDepotName",
                        type: 'get',
                        data: {
                            "depotId": deptId
                        },
                        dataType: 'json',
                        success: function (result) {

                            $('#depotNameAdd').val(result.depotName)

                        },
                        error: function (result) {
                            console.log(result);
                        }
                    });

                }

                /**
                 * 显示探测站类型
                 */
                function initDeviceTypeAdd() {
                    for (let deviceType of deviceTypeSet) {
                        $('#detectTypeAdd').append('<option>' + deviceType + '</option>')
                    }
                }

                /**
                 * 显示探测站
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/detectManage/detectManage/listDetect",
                    type: 'get',
                    data: {
                        "depotId": deptId
                    },
                    dataType: 'json',
                    success: function (result) {
                        listDetect = result.data
                        for (var i = 0; i < result.data.length; i++) {
                            // $("#detectDeviceAdd").append('<option value="' + result.data[i].detectDeviceId + '" deviceModelName="' + result.data[i].deviceModelName + '">' + result.data[i].detectDeviceName + '</option>');
                            lineSet.add(result.data[i].lineName)
                            deviceTypeSet.add(result.data[i].deviceTypeName)
                        }
                        initLineAdd()
                        initDeviceTypeAdd()
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                //初始化年份选择下拉框
                initYearAdd()
                //初始化部门名称
                initDepotNameAdd()
                $("#lineAdd").change(function () {
                    $("#detectTypeAdd option:first").prop("selected", true);
                })
                $("#detectTypeAdd").change(function () {
                    var lineName = $("#lineAdd option:selected").text();
                    var detectType = $("#detectTypeAdd option:selected").text();

                    $("#detectDeviceAdd").empty()
                    $("#detectDeviceAdd").append('<option></option>')
                    for (let i = 0; i < listDetect.length; i++) {
                        if (listDetect[i].lineName == lineName && listDetect[i].deviceTypeName == detectType) {
                            $("#detectDeviceAdd").append('<option value="' + listDetect[i].detectDeviceId + '" deviceTypeName="' + listDetect[i].deviceTypeName + '">' + listDetect[i].detectDeviceName + '</option>');
                        }
                    }

                })
                /**
                 * 初始化检修单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/checkPlan/sheet/getAllSheets',
                        type: 'GET',
                        data: function (d) {

                        }
                    },
                    columns: [{
                        data: null
                    },
                        {
                            data: 'sheetId'
                        },
                        {
                            data: 'year'
                        },
                        {
                            data: 'month'
                        },
                        {
                            data: 'depotName'
                        },
                        {
                            data: 'createTime'
                        }, {
                            data: 'createUser'
                        },
                        {
                            data: 'verifyUser'
                        },
                        {
                            data: 'updateTime'
                        },
                        {
                            data: 'remark'
                        },
                        {
                            data: 'flag',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '<span style="color:red;font-weight:bold;">新建</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:blue;font-weight:bold;">审核中</span>';
                                } else if (data == 3) {
                                    str = '<span style="color:black;font-weight:bold;">已完成</span>';
                                }
                                else if (data == 4) {
                                    str = '<span style="color:red;font-weight:bold;">审核不通过</span>';
                                }
                                return str;
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 11,
                        data: function (row) {
                            var str = '';
                            if (row.flag == 1 || row.flag == 4) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;' + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;' + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                            } else {
                                str += '';
                            }
                            str += '<button id="exportExcel" type="button" class="btn btn-success btn-xs" title="导出"><span class="glyphicon glyphicon-download-alt"></span></button>';
                            return str;
                        }
                    }],
                    ordering: false,
                    paging: true,
                    pageLength: 5,
                    serverSide: false,
                    drawCallback: function (settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function (cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });

                    },
                });
                /**
                 * 初始化检修单据详情
                 */
                var sheetDetailTable = dataTable('sheetDetailTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/checkPlan/checkPlan/list',
                        type: 'GET',
                        data: function (d) {
                            // // if (roleName.indexOf("集团调度员") == -1) {
                            // //     d.depotId = deptId;
                            //     d.queryTime = $("#queryTime").val();
                            //     d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                            // // }
                            d.sheetId=sheet_id
                        }
                    },
                    columns: [{
                        data: null
                    }, {
                        data: 'id', bVisible: false
                    },
                        {
                            data: 'detectDeviceId', bVisible: false
                        },
                        {
                            data: 'detectDeviceName'
                        }, {
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
                        }, {
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
                                    str = '<span style="color:blue;font-weight:bold;">计划审核中</span>';
                                } else if (data == 3) {
                                    str = '<span style="color:blue;font-weight:bold;">待检修中</span>';
                                }
                                else if (data == 4) {
                                    str = '<span style="color:blue;font-weight:bold;">检修结束中</span>';
                                }
                                else if (data == 5) {
                                    str = '<span style="color:black;font-weight:bold;">检修完成</span>';
                                }
                                return str;
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 12,
                        data: function (row) {
                            var str = '';
                            if (roleName == "段调度员" && (row.completeFlag == 1 || row.completeFlag == 3)) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            }
                            if (roleName == "集团调度员" && (row.completeFlag == 2 || row.completeFlag == 4)) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            }
                            if (roleName == "段调度员" && (row.completeFlag == 1 || row.completeFlag == 3)) {
                                str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                            } else if (roleName == "集团调度员" && (row.completeFlag == 2 || row.completeFlag == 4)) {
                                str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popBackSheetModal" title="回退单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                            }
                            if (roleName == "段调度员" && row.completeFlag == 1) {
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';

                            }
                            if (roleName == "段调度员" && row.completeFlag == 3) {
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popBackSheetModal" title="回退单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';

                            }

                            // str += '<button id="exportExcel" type="button" class="btn btn-success btn-xs" title="导出"><span class="glyphicon glyphicon-download-alt"></span></button>';
                            return str;
                        }
                    }],
                    ordering: false,
                    paging: true,
                    pageLength: 10,
                    serverSide: false,
                    drawCallback: function (settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function (cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
                    },
                });
                /**
                 * 添加单据
                 */
                $('#addSheetModal').on('show.bs.modal',
                    function () {
                        // 单据ID为单据类型+部门id+序列号
                        if (deptId < 10) {
                            var sheetId = "JX-0" + deptId + "-";
                        } else {
                            var sheetId = "JX-" + deptId + "-";
                        }
                        $.ajax({
                            url: config.basePath + '/checkPlan/sheet/getMaxSheetId',
                            type: "get",
                            data: {
                                sheet_id: sheetId
                            },
                            contentType: 'application/json',
                            dataType: "text",
                            success: function (result) {
                                // alert(result)
                                $('#sheetIdAdd').val(result);
                            },

                        });
                        $('#createUserAdd').val(user_id)
                    });

                $("#btnAddSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            sheetId: $('#sheetIdAdd').val(),
                            depotName: $('#depotNameAdd').val(),
                            createUser: $('#createUserAdd').val(),
                            year: $('#yearAdd').val(),
                            month: $('#monthAdd').val(),
                            depotId: deptId,
                            remark: $('#remarkAdd').val()
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/sheet/add',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据信息添加成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });

                /**
                 * 新增检修计划详情
                 */
                $("#btnAddSheetDetailOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if ($("#detectDeviceAdd").val() == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>探测站为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if ($("#haultStartTimeAdd").val() == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>停机开始时间为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        var params = JSON.stringify({
                            detectDeviceId: $('#detectDeviceAdd').val(),
                            detectDeviceName: $('#detectDeviceAdd option:selected').text(),
                            detectDeviceType: $('#detectDeviceAdd option:selected').attr("deviceTypeName"),
                            planTime: $('#planTimeAdd').val(),
                            // createUser: user_id,
                            planType: $('#planTypeAdd option:selected').text(),
                            depotId: deptId,
                            sheetId:sheet_id
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/add',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetDetailTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障预报添加成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
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
                                completeFlag = sheetTrData.flag
                                sheet_id = sheetTrData.sheetId
                                if (completeFlag == 2) { // 已审核不能添加
                                    $("#add_sheetDetail").hide();
                                } else {
                                    $("#add_sheetDetail").show()
                                }
                                sheetDetailTable.ajax.reload()
                            }

                        });
                $('#addSheetDetailModal').on('show.bs.modal', function (e) {
                    $('#planTimeAdd').val('')

                    $('#planTypeAdd option:first').prop("selected", true)
                    $('#createUserAdd').val(user_id)
                    $('#detectDeviceAdd option:first').prop("selected", true)
                    $('#lineAdd option:first').prop("selected", true)
                })
                /**
                 * 修改故障预报表
                 */
                $('#modifySheetDetailModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#remarkModify').val(data.remark)
                        $('#detectDeviceModify').val(data.detectDeviceName)
                        $('#haultStartTimeModify').val(formatDateBy(data.haultStartTime, 'yyyy-MM-dd HH:mm:ss'))
                        $('#haultEndTimeModify').val(formatDateBy(data.haultEndTime, 'yyyy-MM-dd HH:mm:ss'))
                        $('#faultStopTimeModify').val(data.faultStopTime)
                        $('#faultLevelModify option:contains("' + data.faultLevelType + '")').prop("selected", true);
                        $('#reportTimeModify').val(data.forecastFaultTime)
                        $('#faultInfoModify').val(data.faultInfo)
                        $('#segmentDutyUserModify').val(data.segmentDutyUser)
                        $('#handleInfoModify').val(data.handleInfo)
                        $('#faultHandleStartTimeModify').val(data.handleStartTime)
                        $('#faultHandleEndTimeModify').val(data.handleEndTime)
                        $('#responsibleUserModify').val(data.responsibleUser)
                        $('#remarkModify').val(data.remark)
                        $('#typeModify option:contains("' + data.type + '")').prop("selected", true);
                        $('#telegraphNumberModify').val(data.telegraphNumber)
                        $('#noticeTimeModify').val(data.noticeTime)
                        $('#responsibleUnitModify').val(data.responsibleUnit)
                        $('#planOutageStartTimeModify').val(data.planOutageStartTime)
                        $('#planOutageEndTimeModify').val(data.planOutageEndTime)
                        $('#faultTypeModify option:contains("' + data.faultType + '")').prop("selected", true);
                        if (data.repairPerson != null && data.repairPerson != '') {
                            $('#repairUserModify option:contains("' + data.repairPerson + '")').prop("selected", true);
                        }
                        if (data.noticeUser != null && data.noticeUser != '') {
                            $('#noticeUserModify option:contains("' + data.noticeUser + '")').prop("selected", true);
                        }
                        initModifyModal()
                    });


                function initModifyModal() {
                    if (completeFlag <= 2) {
                        $('#handleInfoDiv').hide()
                        $('#handleStartTimeDiv').hide()
                        $('#handleEndTimeDiv').hide()
                    }
                    if ($('#typeModify option:selected').text() != '设备故障' && completeFlag > 2) {
                        $('.hideDiv').show()
                    } else {
                        $('.hideDiv').hide()
                    }

                }

                $('#typeModify').change(function (e) {
                    initModifyModal()
                    var typeModify = $('#typeModify option:selected').text()
                    if (typeModify == "电力故障") {
                        $('#responsibleUnitModify').val(detectDevice.electricName)
                    } else {
                        $('#responsibleUnitModify').val(detectDevice.netConnectName)
                    }
                })

                /**
                 * 更新faultHandle
                 */
                function verifyFaultStopTime() {
                    var haultStartTime = $('#haultStartTimeModify').val()
                    var handleEndTime = $('#faultHandleEndTimeModify').val()
                    var stopTime = "-"
                    if (haultStartTime != null && haultStartTime != '' && handleEndTime != null && handleEndTime != '') {
                        stopTime = getInervalHour(stringToDate(haultStartTime), stringToDate(handleEndTime))
                    }
                    return stopTime;
                }

                function verifyTime(date) {
                    if (date == '' || date == null) {
                        return null
                    }
                    else return date
                }

                $("#btnModifyDetailSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var stopTime = verifyFaultStopTime()
                        var planOutageStartTime = verifyTime($('#planOutageStartTimeModify').val())
                        var planOutageEndTime = verifyTime($('#planOutageEndTimeModify').val())
                        var handleStartTime = verifyTime($('#faultHandleStartTimeModify').val())
                        var handleEndTime = verifyTime($('#faultHandleEndTimeModify').val())
                        var noticeTime = verifyTime($('#noticeTimeModify').val())
                        var params = JSON.stringify({
                            id: id,
                            faultStopTime: stopTime,
                            faultLevelType: $('#faultLevelModify option:selected').text(),
                            handleStartTime: handleStartTime,
                            handleEndTime: handleEndTime,
                            faultInfo: $('#faultInfoModify').val(),
                            handleInfo: $('#handleInfoModify').val(),
                            repairPerson: $('#repairUserModify option:selected').text(),
                            noticeUser: $('#noticeUserModify option:selected').text(),
                            faultType: $('#faultTypeModify option:selected').text(),
                            telegraphNumber: $('#telegraphNumberModify').val(),
                            noticeTime: noticeTime,
                            type: $('#typeModify option:selected').text(),
                            responsibleUnit: $('#responsibleUnitModify').val(),
                            responsibleUser: $('#responsibleUserModify').val(),
                            remark: $('#remarkModify').val(),
                            telegraphNumber: $('#telegraphNumberModify').val(),
                            planOutageStartTime: planOutageStartTime,
                            planOutageEndTime: planOutageEndTime,

                        });
                        $.ajax({
                            url: config.basePath + '/faultHandle/faultReport/update',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据信息修改成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });

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
                                '确定提交单据ID为:' + data.id
                                + '的单据吗？');
                            $('#btnPopSheetVerifyOk').val(
                                data.id);
                        });

                /**
                 * 单据审核
                 */
                $("#btnPopSheetVerifyOk")
                    .on(
                        'click',
                        function (e) {
                            var tr = $(e.relatedTarget).parents('tr');
                            var data = sheetTable.row(tr).data();
                            var params = JSON.stringify({
                                id: id,
                                completeFlag: completeFlag + 1
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/faultHandle/faultReport/update',
                                    type: "post",
                                    data: params,
                                    contentType: 'application/json',
                                    dataType: "json",
                                    success: function (
                                        result) {
                                        if (result.code != 0) {
                                            alert(result.msg);
                                        } else {
                                            $('#add_sheetDetail').hide()
                                            sheetTable.ajax
                                                .reload();
                                            $("#alertMsg")
                                                .html(
                                                    '<span style="color:green;text-align:center"><strong>单据已提交！</strong></span>');
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
                 * 删除单据
                 */
                $('#popSheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        $('#warningSheetText').text('确定要删除该故障预报单据（' + data.id + '）？');
                        $('#deleteSheetId').val(data.id);
                    });
                /**
                 * 点击删除确定按钮
                 */
                $('#btnPopSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: $('#deleteSheetId').val()
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/delete',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障预报单据删除成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });

                    });
                /**
                 * 删除单据
                 */
                $('#popBackSheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        $('#backSheetText').text('确定要回退该故障预报单据（' + data.id + '）？');
                        $('#backSheetId').val(data.id);
                    });
                /**
                 * 点击删除确定按钮
                 */
                $('#btnPopBackSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: $('#backSheetId').val(),
                            status: completeFlag - 1
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
                sheetTable.on('draw.dt', function () {
                    //给第一列编号
                    sheetDetailTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
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