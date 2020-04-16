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

                var sheet_id = ''; // 查询的单号
                var verify_flag = ''; // 查询的审核状态

                let user_id = window.parent.user.userId; // 登录人ID
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
                var sheetData;
                const lineSet = new Set();
                const deviceTypeSet = new Set();
                CMethod.initDatetimepickerWithSecond("haultStartTimeAdd", null, format);
                CMethod.initDatetimepickerWithSecond("haultEndTimeAdd", null, format);
                CMethod.initDatetimepickerWithSecond("faultHandleStartTimeAdd", null, format);
                CMethod.initDatetimepickerWithSecond("faultHandleEndTimeAdd", null, format);
                CMethod.initDatetimepickerWithSecond("haultStartTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("haultEndTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("faultHandleStartTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("faultHandleEndTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("reportTimeAdd", new Date(), format);
                CMethod.initDatetimepickerWithSecond("noticeTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("planOutageStartTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("planOutageEndTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("maintenanceTimeModify", null, format);
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
                        $('#lineModify').append('<option>' + line + '</option>')
                    }
                }

                /**
                 * 显示探测站类型
                 */
                function initDeviceTypeAdd() {
                    for (let deviceType of deviceTypeSet) {
                        $('#detectTypeAdd').append('<option>' + deviceType + '</option>')
                        $('#detectTypeModify').append('<option>' + deviceType + '</option>')
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
                        depotId: deptId,
                        faultEnable:1
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

                /**
                 * 获取责任单位
                 */
                function getResponsibleUnit(detectDeviceId) {
                    $.ajax({
                        async: false,
                        url: config.basePath + "/faultHandle/faultReport/getResponsibleUnit",
                        type: 'get',
                        data: {detectDeviceId: detectDeviceId},
                        dataType: 'json',
                        success: function (result) {
                            detectDevice = result
                        },
                        error: function (result) {
                            console.log(result);
                        }

                    });
                    return detectDevice
                }

                /**
                 * 显示故障类别
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/faultHandle/faultReport/getFaultType",
                    type: 'get',
                    dataType: 'json',
                    success: function (result) {
                        for (var i = 0; i < result.length; i++) {
                            $("#faultTypeAdd").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                            $("#faultTypeModify").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                /**
                 * 获取探测站维修人员和责任部门
                 */
                $('#detectDeviceAdd').change(function (e) {
                    if ($('#detectDeviceAdd').val() != '') {
                        let data = $('#detectDeviceAdd').val()
                        let responsibleDepot = getResponsibleDepot(data)
                        let responsibleSegment = getResponsibleSegment(data)
                        let result = getDetectRepairPerson(data)
                        $('#repairUserAdd').empty()
                        $('#noticeUserAdd').empty()
                        $('#responsibleDepotAdd').val(responsibleDepot)
                        $('#segmentDepotAdd').val(responsibleSegment)
                        for (var i = 0; i < result.length; i++) {
                            $("#repairUserAdd").append('<option></option>');
                            $("#repairUserAdd").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                            $("#noticeUserAdd").append('<option></option>');
                            $("#noticeUserAdd").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                        }

                    }
                })
                /**
                 * 线别点击事件
                 */
                $("#lineAdd").change(function () {
                    $("#detectTypeAdd option:first").prop("selected", true);
                })
                /**
                 * 线别修改点击事件
                 */
                $("#lineModify").change(function () {
                    $("#detectTypeModify option:first").prop("selected", true);
                })
                /**
                 * 探测站类型新增点击事件
                 */
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
                 * 探测站类型修改点击事件
                 */
                $("#detectTypeModify").change(function () {
                    initDetectModify()

                })

                function initDetectModify() {
                    var lineName = $("#lineModify option:selected").text();
                    var detectType = $("#detectTypeModify option:selected").text();

                    $("#detectDeviceModify").empty()
                    $("#detectDeviceModify").append('<option></option>')
                    for (let i = 0; i < listDetect.length; i++) {
                        if (listDetect[i].lineName == lineName && listDetect[i].deviceTypeName == detectType) {
                            $("#detectDeviceModify").append('<option value="' + listDetect[i].detectDeviceId + '" deviceTypeName="' + listDetect[i].deviceTypeName + '">' + listDetect[i].detectDeviceName + '</option>');
                        }
                    }
                }
                /**
                 * 初始化维修人员
                 * @param detectDeviceId
                 */
                function initRepairUserModify(detectDeviceId) {
                    if (detectDeviceId != '') {
                        var data = detectDeviceId
                        var result = getDetectRepairPerson(data)
                        $('#repairUserModify').empty()
                        $('#noticeUserModify').empty()
                        for (var i = 0; i < result.length; i++) {
                            $("#repairUserModify").append('<option></option>');
                            $("#repairUserModify").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                            $("#noticeUserModify").append('<option></option>');
                            $("#noticeUserModify").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                        }
                    }
                    var result = getResponsibleUnit(data)
                    if (type == "电力故障") {
                        $('#responsibleUnitModify').val(result.electricName)
                    } else {
                        $('#responsibleUnitModify').val(result.netConnectName)
                    }
                }

                /**
                 * 获取维修人员
                 * @param data
                 * @returns {string}
                 */
                function getDetectRepairPerson(data) {
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
                 * 获取责任部门
                 * @param data
                 * @returns {string}
                 */
                function getResponsibleDepot(data) {
                    let ret = '';
                    $.ajax({
                        async: false,
                        url: config.basePath + "/faultHandle/faultReport/getResponsibleDepot",
                        type: 'get',
                        data: {detectDeviceId: data},
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
                 * 获取责任单位
                 */
                function getResponsibleSegment(detectDeviceId) {
                    $.ajax({
                        async: false,
                        url: config.basePath + "/faultHandle/faultReport/getResponsibleSegment",
                        type: 'get',
                        data: {detectDeviceId: detectDeviceId},
                        success: function (result) {
                            detectDevice = result
                        },
                        error: function (result) {
                            console.log(result);
                        }

                    });
                    return detectDevice
                }
                /**
                 * 初始化入库单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/faultHandle/faultReport/byEightNextDay',
                        type: 'GET',
                        data: function (d) {
                                d.depotId = deptId;
                                d.queryTime = ($("#queryTime").val() == '' ? '' : $("#queryTime2").val() + " 00:00:01");
                                d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                        }
                    },
                    columns: [{
                        data: null, bVisible: false
                    },
                        {
                            data: 'id'
                        },

                        {
                            data: 'detectDeviceName'
                        }, {
                            data: 'detectDeviceType'
                        },
                        {
                            data: 'faultInfo'
                        }, {
                            data: 'haultStartTime'
                        },
                        {
                            data: 'forecastFaultTime'
                        },
                        {
                            data: 'segmentDutyUser'
                        },
                        {
                            data: 'handleStartTime'
                        }, {
                            data: 'handleEndTime'
                        }, {
                            data: 'type'
                        },
                        {
                            data: 'faultLevelType'
                        }, {
                            data: 'faultType'
                        },
                        {
                            data: 'submitUser'
                        },
                        {
                            data: 'completeFlag',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '<span style="color:red;font-weight:bold;">新建</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:blue;font-weight:bold;">预报待审核</span>';
                                } else if (data == 3) {
                                    str = '<span style="color:blue;font-weight:bold;">故障待处理</span>';
                                }
                                else if (data == 4) {
                                    str = '<span style="color:blue;font-weight:bold;">故障处理开始待审核</span>';
                                }
                                else if (data == 5) {
                                    str = '<span style="color:blue;font-weight:bold;">故障处理开始</span>';
                                } else if (data == 6) {
                                    str = '<span style="color:blue;font-weight:bold;">故障处理结束待审核</span>';
                                } else if (data == 7) {
                                    str = '<span style="color:black;font-weight:bold;">故障处理完成</span>';
                                }
                                return str;
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 15,
                        data: function (row) {
                            var str = '';
                            if (row.completeFlag == 1 || row.completeFlag == 3|| row.completeFlag == 5) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                            }
                            if ( row.completeFlag == 1) {
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';

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
                 * 新增故障预报
                 */
                $("#btnAddSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let haultStartTime = $("#haultStartTimeAdd").val()
                        let faultLevel = $("#faultLevelAdd").val()
                        let faultInfo = $("#faultInfoAdd").val()
                        let noticeUser = $("#noticeUserAdd").val()
                        let date = new Date()
                        if ($("#detectDeviceAdd").val() == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>探测站为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (haultStartTime == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>停机开始时间为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (Date.parse(haultStartTime) >= date) {
                            $("#alertMsgAdd").html("<font style='color:red'>停机开始时间大于当前时间！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (faultLevel == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>故障级别为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (faultInfo == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>故障现象为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (noticeUser == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>通知的责任人为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        var params = JSON.stringify({
                            id: $('#idAdd').val(),
                            detectDeviceId: $('#detectDeviceAdd').val(),
                            detectDeviceName: $('#detectDeviceAdd option:selected').text(),
                            detectDeviceType: $('#detectDeviceAdd option:selected').attr("deviceTypeName"),
                            haultStartTime: $('#haultStartTimeAdd').val(),
                            lineName:$('#lineAdd option:selected').text(),
                            noticeUser: $('#noticeUserAdd option:selected').text(),
                            faultInfo: $('#faultInfoAdd').val(),
                            segmentDutyUser: $('#segmentDutyUserAdd').val(),
                            segmentDepot: $('#segmentDepotAdd').val(),
                            type: $('#typeAdd option:selected').text(),
                            responsibleDepot: $('#responsibleDepotAdd').val(),
                            segmentDepot: $('#segmentDepotAdd').val(),
                            faultInfoDetail: $('#faultInfoDetailAdd').val(),
                            depotId: deptId
                        });
                        $.ajax({
                            url: config.basePath + '/faultHandle/faultReport/add',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
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
                                let tr = $(this).closest('tr');
                                let sheetTrData = sheetTable
                                    .row(tr).data();
                                id = sheetTrData.id
                                detectDeviceId = sheetTrData.detectDeviceId
                                completeFlag = sheetTrData.completeFlag
                                type = sheetTrData.type;
                                sheetData = sheetTrData
                                drawSheetDetail(sheetTrData)
                            }

                        });

                /**
                 * 重绘故障预报详情
                 */
                function drawSheetDetail(sheetTrData) {

                    $('#faultInfoDetail').text(sheetTrData.faultInfoDetail)
                    $('#maintenanceTime').text(sheetTrData.maintenanceTime)
                    $('#checkInfo').text(sheetTrData.checkInfo)
                    $('#noticeUser').text(sheetTrData.noticeUser)
                    $('#segmentDutyUser').text(sheetTrData.segmentDutyUser)
                    $('#segmentDepot').text(sheetTrData.segmentDepot)
                    $('#responsibleUser').text(sheetTrData.responsibleUser)
                    $('#responsibleDepot').text(sheetTrData.responsibleDepot)
                    $('#handleInfo').text(sheetTrData.handleInfo)
                    $('#repairPerson').text(sheetTrData.repairPerson)
                    $('#telegraphNumber').text(sheetTrData.telegraphNumber)
                    $('#planOutageStartTime').text(sheetTrData.planOutageStartTime)
                    $('#planOutageEndTime').text(sheetTrData.planOutageEndTime)
                    $('#responsibleUnit').text(sheetTrData.responsibleUnit)
                    $('#noticeTime').text(sheetTrData.noticeTime)
                }
                /**
                 * 新增模态框打开按钮事件
                 */
                $('#addSheetModal').on('show.bs.modal', function (e) {
                    $('#segmentDutyUserAdd').val(user_id)
                    $('#faultStopTimeAdd').val('')
                    $('#haultStartTimeAdd').val('')
                    $('#haultEndTimeAdd').val('')
                    $('#faultHandleStartTimeAdd').val('')
                    $('#faultHandleEndTimeAdd').val('')
                    $('#faultInfoDetailAdd').val('')
                    $('#faultInfoAdd').val('')
                    $('#handleInfoAdd').val('')
                    $('#repairUserAdd').val('')
                    $('#responsibleDepotAdd').val('')
                    $('#faultTypeAdd option:first').prop("selected", true)
                    $('#faultLevelAdd option:first').prop("selected", true)
                    $('#detectDeviceAdd option:first').prop("selected", true)
                    $('#lineAdd option:first').prop("selected", true)
                    $('#detectTypeAdd option:first').prop("selected", true)
                    // 单据ID为单据类型+部门id+序列号
                    let sheetId;
                    if (deptId < 10) {
                        sheetId = "GZ-0" + deptId + "-";
                    } else {
                        sheetId = "GZ-" + deptId + "-";
                    }
                    $.ajax({
                        async: false,
                        url: config.basePath + '/faultHandle/faultReport/getMaxSheetId',
                        type: "get",
                        data: {
                            sheet_id: sheetId
                        },
                        contentType: 'application/json',
                        dataType: "text",
                        success: function (result) {
                            $('#idAdd').val(result);
                        },

                    });
                })
                /**
                 * 修改故障预报表
                 */
                $('#modifySheetModal').on('show.bs.modal',
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
                        $('#faultInfoDetailModify').val(data.faultInfoDetail)
                        $('#maintenanceTimeModify').val(data.maintenanceTime)
                        $('#segmentDutyUserModify').val(data.segmentDutyUser)
                        $('#segmentDepotModify').val(data.segmentDepot)
                        $('#handleInfoModify').val(data.handleInfo)
                        $('#faultHandleStartTimeModify').val(data.handleStartTime)
                        $('#faultHandleEndTimeModify').val(data.handleEndTime)
                        $('#responsibleUserModify').val(data.responsibleUser)
                        $('#responsibleDepotModify').val(data.responsibleDepot)
                        $('#typeModify option:contains("' + data.type + '")').prop("selected", true);
                        $('#lineModify option:contains("' + data.lineName + '")').prop("selected", true);
                        $('#detectTypeModify option:contains("' + data.detectDeviceType + '")').prop("selected", true);
                        initDetectModify()
                        $('#detectDeviceModify option:contains("' + data.detectDeviceName + '")').prop("selected", true);
                        $('#telegraphNumberModify').val(data.telegraphNumber)
                        $('#noticeTimeModify').val(data.noticeTime)
                        $('#responsibleUnitModify').val(data.responsibleUnit)
                        $('#planOutageStartTimeModify').val(data.planOutageStartTime)
                        $('#planOutageEndTimeModify').val(data.planOutageEndTime)
                        $('#faultTypeModify option:contains("' + data.faultType + '")').prop("selected", true);
                        initRepairUserModify(detectDeviceId)
                        if (data.repairPerson != null && data.repairPerson != '') {
                            $('#repairUserModify option:contains("' + data.repairPerson + '")').prop("selected", true);
                        }
                        if (data.noticeUser != null && data.noticeUser != '') {
                            $('#noticeUserModify option:contains("' + data.noticeUser + '")').prop("selected", true);
                        }
                        initModifyModal()
                    });


                function initModifyModal() {
                    if (completeFlag == 1 || completeFlag == 2) {
                        $('.handleStart').each(function () {
                            $(this).hide()
                        })
                        $('.handleEnd').each(function () {
                            $(this).hide()
                        })
                    } else if (completeFlag == 3 || completeFlag == 4) {
                        $('.handleEnd').each(function () {
                            $(this).hide()
                        })
                    }
                    if ($('#typeModify option:selected').text() != '设备故障' && completeFlag > 2) {
                        $('.hideDiv').show()
                    } else {
                        $('.hideDiv').hide()
                    }
                    if (completeFlag > 1) {

                        $('#noticeUserModify').attr("disabled", true)
                    } if (completeFlag > 2) {

                        $('#haultStartTimeModify').attr("readOnly", true)
                        $('#faultInfoModify').attr("readOnly", true)
                        $('#faultInfoDetailModify').attr("readOnly", true)
                        $('#lineModify').attr("disabled", true)
                        $('#detectTypeModify').attr("disabled", true)
                        $('#detectDeviceModify').attr("disabled", true)
                    }
                    if(completeFlag >3){
                        $('#maintenanceTimeModify').attr("readOnly", true)
                        $('#maintenanceTimeModify').attr("readOnly", true)
                        $('#checkInfoModify').attr("readOnly", true)
                        $('#faultHandleStartTimeModify').attr("readOnly", true)
                        $('#repairUserModify').attr("disabled", true)
                    }
                    if(completeFlag>5){
                        $('#faultHandleEndTimeModify').attr("readOnly", true)
                        $('#handleInfoModify').attr("readOnly", true)
                        $('#telegraphNumberModify').attr("readOnly", true)
                        $('#planOutageStartTimeModify').attr("readOnly", true)
                        $('#planOutageEndTimeModify').attr("readOnly", true)
                        $('#noticeTimeModify').attr("readOnly", true)
                        $('#responsibleUnitModify').attr("readOnly", true)
                        $('#responsibleUserModify').attr("readOnly", true)
                        $('#typeModify').attr("disabled", true)
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

                /**
                 * 校验参数
                 */
                function verifyParmasByCompleteFlag() {
                    let handleStartTime = $('#faultHandleStartTimeModify').val()
                    let handleEndTime = $('#faultHandleEndTimeModify').val()
                    let checkInfoModify = $('#checkInfoModify').val()
                    let maintenanceTime = $('#maintenanceTimeModify').val()
                    let handleInfo = $('#handleInfoModify').val()
                    let repairUserModify = $('#repairUserModify option:selected').text()
                    let haultStartTime=$('#haultStartTimeModify').val()
                    let date = new Date()
                    if (completeFlag>=1&&haultStartTime=='') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障发生时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }if (completeFlag>=1&&Date.parse(haultStartTime) >date) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障发生时间大于当前时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    if (completeFlag>=3&&handleStartTime=='') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理开始时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    else if (completeFlag>=3&&Date.parse(handleStartTime) >date) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理开始时间大于当前时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }  else if (completeFlag>=3&&Date.parse(handleStartTime) <Date.parse(haultStartTime)) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理开始时间小于故障发生时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag>=5&&handleEndTime=='') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理结束时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    else if (completeFlag>=5&&Date.parse(handleEndTime) < Date.parse(handleStartTime)) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理结束时间小于开始时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag>=5&&handleInfo=='') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>处理情况为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }else if(maintenanceTime!=''&&Date.parse(maintenanceTime) < date){
                        $("#alertMsgSheetModify").html("<font style='color:red'>维修天窗小于当前时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }else if (completeFlag>=3&&repairUserModify=='') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>维修人员为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }else if (completeFlag>=3&&checkInfoModify=='') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>现场设备诊断情况为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    return true
                }

                /**
                 * 修改故障预报
                 */
                $("#btnModifySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if(verifyParmasByCompleteFlag()==false){
                            return false
                        }
                        var stopTime = verifyFaultStopTime()
                        var planOutageStartTime = verifyTime($('#planOutageStartTimeModify').val())
                        var planOutageEndTime = verifyTime($('#planOutageEndTimeModify').val())
                        var handleStartTime = verifyTime($('#faultHandleStartTimeModify').val())
                        var handleEndTime = verifyTime($('#faultHandleEndTimeModify').val())
                        var noticeTime = verifyTime($('#noticeTimeModify').val())
                        var maintenanceTime = verifyTime($('#maintenanceTimeModify').val())
                        let type=$('#typeModify option:selected').text()
                        let responsibleUnit=$('#responsibleUnitModify').val()
                        if(type=="设备故障"){
                            responsibleUnit=""
                        }
                        var params = JSON.stringify({
                            id: id,
                            faultLevelType: $('#faultLevelModify option:selected').text(),
                            handleStartTime: handleStartTime,
                            handleEndTime: handleEndTime,
                            maintenanceTime: maintenanceTime,
                            checkInfo: $('#checkInfoModify').val(),
                            faultInfoDetail: $('#faultInfoDetailModify').val(),
                            faultInfo: $('#faultInfoModify').val(),
                            handleInfo: $('#handleInfoModify').val(),
                            repairPerson: $('#repairUserModify option:selected').text(),
                            noticeUser: $('#noticeUserModify option:selected').text(),
                            faultType: $('#faultTypeModify option:selected').text(),
                            telegraphNumber: $('#telegraphNumberModify').val(),
                            noticeTime: noticeTime,
                            type: type,
                            responsibleUnit: responsibleUnit,
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
                            if (sheetData.completeFlag == 3 && sheetData.handleStartTime == '') {
                                $('#popSheetVerifyModal').modal('hide')
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障处理开始时间为空！</strong></span>');
                                $("#infoAlert")
                                    .show();
                                hideTimeout(
                                    "infoAlert",
                                    2000);
                                return false;
                            }
                            if (sheetData.completeFlag == 3 && sheetData.handleEndTime == '') {
                                $('#popSheetVerifyModal').modal('hide')
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障处理结束时间为空！</strong></span>');
                                $("#infoAlert")
                                    .show();
                                hideTimeout(
                                    "infoAlert",
                                    2000);
                                return false;
                            }
                            if (sheetData.completeFlag == 5 && sheetData.handleInfo == '') {
                                $('#popSheetVerifyModal').modal('hide')
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障处理情况为空！</strong></span>');
                                $("#infoAlert")
                                    .show();
                                hideTimeout(
                                    "infoAlert",
                                    2000);
                                return false;
                            }
                            let submitUser;
                            if (completeFlag == 5) {//提交人员
                                submitUser = user_id
                            }
                            var params = JSON.stringify({
                                id: id,
                                completeFlag: completeFlag + 1,
                                submitUser: submitUser
                            });
                            $.ajax({
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
                                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据已提交！</strong></span>');
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
                            url: config.basePath + '/faultHandle/faultReport/delete',
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
                 * 点击回退确定按钮
                 */
                $('#btnPopBackSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: $('#backSheetId').val(),
                            completeFlag: completeFlag - 1
                        });
                        $.ajax({
                            url: config.basePath + '/faultHandle/faultReport/update',
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