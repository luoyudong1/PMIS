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
                var dispatcher = window.parent.user.dispatcher // 所属部门id
                var id;
                var completeFlag;
                var format = 'Y-m-d H:i:s';
                var detectDeviceId;
                var type;
                var detectDevice;
                var sheetData;
                let deviceType=''
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
                    $.ajax({
                        async: false,
                        url: config.basePath + "/faultHandle/faultReport/finished",
                        type: 'get',
                        data: {},
                        dataType: 'json',
                        success: function (result) {
                            if (result.code != 0) {
                                alert(result.msg);
                            } else {
                                sheetTable.ajax.reload();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障归档成功！</strong></span>');
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
                 * 获取故障现像
                 */
                function getFaultInfo(data) {
                    var ret = '';
                    $.ajax({
                        async: false,
                        url: config.basePath + "/faultHandle/faultReport/getFaultInfo",
                        type: 'post',
                        data: {"deviceType": data},
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
                 *故障现像修改初始化
                 */
                function initFaultInfoModify(type){
                    var result=getFaultInfo(type)
                    $("#faultInfoModify").empty()
                    $("#faultInfoModify").append('<option></option>')
                    for (var i = 0; i < result.length; i++) {
                        $("#faultInfoModify").append('<option value="' + result[i].id + '">' + result[i].faultInfo + '</option>');
                    }
                }
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
                $('#detectDeviceModify').change(function (e) {
                    if ($('#detectDeviceModify').val() != '') {
                        let data = $('#detectDeviceModify').val()
                        let responsibleDepot = getResponsibleDepot(data)
                        let responsibleSegment = getResponsibleSegment(data)
                        let result = getDetectRepairPerson(data)
                        $('#repairUserModify').empty()
                        $('#noticeUserModify').empty()
                        $('#responsibleDepotModify').val(responsibleDepot)
                        $('#segmentDepotModify').val(responsibleSegment)
                        for (var i = 0; i < result.length; i++) {
                            $("#repairUserModify").append('<option></option>');
                            $("#repairUserModify").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                            $("#noticeUserModify").append('<option></option>');
                            $("#noticeUserModify").append('<option value="' + result[i].id + '">' + result[i].name + '</option>');
                        }

                    }
                })


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

                            d.finished = 0
                            d.dispatcher = $('#dispatcher', parent.document).val()
                            d.completeFlag = $('#verify_flag').val()
                            d.detectDeviceName = $('#detectDeviceName').val()
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
                        {
                            data: 'haultStartTime',
                            render: function (data, type, row, meta) {
                                var date = new Date()
                                var startTime = new Date(Date.parse(data))
                                var endTime = new Date(Date.parse(row.handleEndTime))

                                if (row.handleEndTime != '') {
                                    date = endTime
                                }
                                var diff = (date.getTime() - startTime.getTime()) / (3600 * 1000)
                                if (row.type == '设备故障') {
                                    if (row.faultLevelType == 'A' && diff > 48) {
                                        return '<span style="color:red;font-weight:bold;">超时</span>'
                                    } else if (row.faultLevelType == 'B') {
                                        if (startTime.getHours() > 8 && startTime.getHours() < 18 && diff > 6) {
                                            return '<span style="color:red;font-weight:bold;">超时</span>'
                                        } else if (diff > 12) {
                                            return '<span style="color:red;font-weight:bold;">超时</span>'
                                        }

                                    } else if (row.faultLevelType == 'C' && diff > 72) {
                                        return '<span style="color:red;font-weight:bold;">超时</span>'
                                    }
                                }

                                return '-'
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 16,
                        data: function (row) {
                            var str = '';

                            if (row.completeFlag == 2 || row.completeFlag == 4 || row.completeFlag == 6) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popBackSheetModal" title="回退单据"><span class="glyphicon glyphicon-backward"></span></a>&nbsp;&nbsp;';
                            } else {
                                str += '<a class="remarkSheet btn btn-info btn-xs" data-toggle="modal" href="#remarkSheetModal" title="查看及修改备注"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            }
                            if (row.completeFlag >= 2 && row.completeFlag <= 4) {
                                str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';

                            }
                            return str;
                        }
                    }],
                    ordering: false,
                    paging: true,
                    pageLength: 10,
                    serverSide: false,
                    fnCreatedRow: function (row, data) {
                        if (data.completeFlag == 2 || data.completeFlag == 4 || data.completeFlag == 6) {
                            $(row).css('color', 'red')
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
                                deviceType=sheetTrData.detectDeviceType
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
                    $('#verifyUser1').text(sheetTrData.verifyUser1)
                    $('#verifyUser2').text(sheetTrData.verifyUser2)
                    $('#verifyUser3').text(sheetTrData.verifyUser3)
                    $('#verifyUser4').text(sheetTrData.verifyUser4)
                    $('#verifyUser5').text(sheetTrData.verifyUser5)
                    $('#verifyDate1').text(sheetTrData.verifyDate1)
                    $('#verifyDate2').text(sheetTrData.verifyDate2)
                    $('#verifyDate3').text(sheetTrData.verifyDate3)
                    $('#verifyDate4').text(sheetTrData.verifyDate4)
                    $('#verifyDate5').text(sheetTrData.verifyDate5)
                    if (completeFlag == 7) {
                        $('#updateTime').text(sheetTrData.updateTime)
                    } else {
                        $('#updateTime').text('')
                    }
                    $('#submitUser').text(sheetTrData.submitUser)
                }

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
                 * 备注故障预报表
                 */
                $('#remarkSheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#lineRemarkModify').val(data.lineName)
                        $('#detectTypeRemarkModify').val(data.detectDeviceType)
                        $('#detectDeviceRemarkModify').val(data.detectDeviceName)
                        $('#remark').val(data.remark)
                    });

                /**
                 * 修改故障预报
                 */
                $("#btnRemarkSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: id,
                            remark: $('#remark').val()

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
                                    $('#remarkSheetModal').modal('hide')
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>提交成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });

                    });
                /**
                 * 修改故障预报表
                 */
                $('#modifySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#lineModify').append('<option>' + data.lineName + '</option>');
                        $('#detectTypeModify').append('<option>' + data.detectDeviceType + '</option>');
                        $('#detectDeviceModify').append('<option>' + data.detectDeviceName + '</option>');
                        $('#remarkModify').text(data.remark)
                        $('#checkInfoModify').text(data.checkInfo)
                        $('#detectDeviceModify').val(data.detectDeviceName)
                        $('#haultStartTimeModify').val(formatDateBy(data.haultStartTime, 'yyyy-MM-dd HH:mm:ss'))
                        $('#haultEndTimeModify').val(formatDateBy(data.haultEndTime, 'yyyy-MM-dd HH:mm:ss'))
                        $('#faultStopTimeModify').val(data.faultStopTime)
                        $('#faultLevelModify option:contains("' + data.faultLevelType + '")').prop("selected", true);
                        $('#reportTimeModify').val(data.forecastFaultTime)
                        initFaultInfoModify(data.detectDeviceType)
                        $('#faultInfoModify option:contains("' + data.faultInfo + '")').prop("selected", true);
                        $('#faultInfoDetailModify').val(data.faultInfoDetail)
                        $('#maintenanceTimeModify').val(data.maintenanceTime)
                        $('#segmentDutyUserModify').val(data.segmentDutyUser)
                        $('#segmentDepotModify').val(data.segmentDepot)
                        $('#handleInfoModify').text(data.handleInfo)
                        $('#faultHandleStartTimeModify').val(data.handleStartTime)
                        $('#faultHandleEndTimeModify').val(data.handleEndTime)
                        $('#responsibleUserModify').val(data.responsibleUser)
                        $('#responsibleDepotModify').val(data.responsibleDepot)
                        $('#typeModify option:contains("' + data.type + '")').prop("selected", true);
                        if (data.faultResponsible != null && data.faultResponsible != "") {
                            $('#faultResponsibleModify option:contains("' + data.faultResponsible + '")').prop("selected", true);
                        }
                        $('#telegraphNumberModify').val(data.telegraphNumber)
                        $('#noticeTimeModify').val(data.noticeTime)
                        $('#responsibleUnitModify').val(data.responsibleUnit)
                        $('#planOutageStartTimeModify').val(data.planOutageStartTime)
                        $('#planOutageEndTimeModify').val(data.planOutageEndTime)
                        if (data.faultType != ""&&data.faultType != null) {
                            $('#faultTypeModify option:contains("' + data.faultType + '")').prop("selected", true);
                        }else {
                            $('#faultTypeModify option:first').prop("selected", true);
                        }
                        initRepairUserModify(detectDeviceId)
                        if (data.repairPerson != null && data.repairPerson != '') {
                            $('#repairUserModify option:contains("' + data.repairPerson + '")').prop("selected", true);
                        }
                        if (data.noticeUser != null && data.noticeUser != '') {
                            $('#noticeUserModify option:contains("' + data.noticeUser + '")').prop("selected", true);
                        }
                        initModifyModal()
                    });

                /**
                 * 改变修改故障预报模态框
                 */
                function initModifyModal() {
                    let typeModify = $('#typeModify option:selected').text()
                    /**
                     * 隐藏非设备故障填写项
                     */
                    if (typeModify != '设备故障') {
                        $('.hideDiv').show()
                        $('.faultResponsible').show()
                    } else {
                        $('.hideDiv').hide()
                        $('.faultResponsible').hide()
                    }
                    /**
                     * 显示或隐藏非责任故障备注
                     */
                    displayFaultResponsibleRemark()
                    /**
                     * 隐藏通知责任人填写
                     */
                    $('#noticeUserModify').attr("disabled", true)

                }

                /**
                 * 显示或隐藏非责任故障备注
                 */
                function displayFaultResponsibleRemark() {
                    let faultResponsible = $('#faultResponsibleModify option:selected').text()
                    if (faultResponsible == "非责任故障") {
                        $('#faultResponsibleRemarkModify').show()
                    } else {
                        $('#faultResponsibleRemarkModify').hide()
                    }
                }

                /**
                 *故障日表类别改变事件
                 */
                $('#faultResponsibleModify').change(function (e) {
                    displayFaultResponsibleRemark()
                })


                /**
                 * 更新faultHandle
                 */


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
                    let noticeTimeModify = $('#noticeTimeModify').val()
                    let responsibleUserModify = $('#responsibleUserModify').val()
                    let checkInfoModify = $('#checkInfoModify').val()
                    let handleInfo = $('#handleInfoModify').val()
                    let repairUserModify = $('#repairUserModify option:selected').text()
                    let faultTypeModify = $('#faultTypeModify option:selected').text()
                    let faultLevelModify = $('#faultLevelModify option:selected').text()
                    let haultStartTime = $('#haultStartTimeModify').val()
                    let typeModify = $('#typeModify option:selected').text()
                    let date = new Date()
                    if (completeFlag >= 1 && haultStartTime == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障发生时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    if (completeFlag >= 1 && Date.parse(haultStartTime) > date) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障发生时间大于当前时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    if (completeFlag >= 3 && typeModify == '设备故障' && handleStartTime == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理开始时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    else if (completeFlag >= 3 && typeModify == '设备故障' && Date.parse(handleStartTime) > date) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理开始时间大于当前时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 3 && Date.parse(handleStartTime) < Date.parse(haultStartTime)) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理开始时间小于故障发生时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 5 && handleEndTime == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理结束时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    else if (completeFlag >= 5 && typeModify == '设备故障' && Date.parse(handleEndTime) < Date.parse(handleStartTime)) {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障处理结束时间小于开始时间！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 5 && typeModify == '设备故障' && handleInfo == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>处理情况为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 5 && typeModify == '设备故障' && faultLevelModify == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障级别为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    else if (completeFlag >= 5 && typeModify == '设备故障' && faultTypeModify == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>故障类别为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    }
                    else if (completeFlag >= 3 && typeModify == '设备故障' && repairUserModify == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>维修人员为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 3 && typeModify != '设备故障' && noticeTimeModify == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>通知时间为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 3 && typeModify != '设备故障' && responsibleUserModify == '') {
                        $("#alertMsgSheetModify").html("<font style='color:red'>责任人为空！</font>");
                        $("#alertMsgSheetModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                        return false;
                    } else if (completeFlag >= 3&& typeModify == '设备故障' && checkInfoModify == '') {
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
                        if (verifyParmasByCompleteFlag() == false) {
                            return false
                        }
                        var planOutageStartTime = verifyTime($('#planOutageStartTimeModify').val())
                        var planOutageEndTime = verifyTime($('#planOutageEndTimeModify').val())
                        var handleStartTime = verifyTime($('#faultHandleStartTimeModify').val())
                        var handleEndTime = verifyTime($('#faultHandleEndTimeModify').val())
                        var noticeTime = verifyTime($('#noticeTimeModify').val())
                        var maintenanceTime = verifyTime($('#maintenanceTimeModify').val())
                        let type = $('#typeModify option:selected').text()
                        let responsibleUnit = $('#responsibleUnitModify').val()
                        let faultResponsible = $('#faultResponsible option:selected').text()
                        let faultResponsibleRemark = $('#faultResponsibleRemarkModify').val()
                        if (type == "设备故障") {
                            responsibleUnit = ""
                        } else {
                            faultResponsible = "非责任故障"
                            faultResponsibleRemark = ""
                        }
                        var params = JSON.stringify({
                            id: id,
                            faultLevelType: $('#faultLevelModify option:selected').text(),
                            handleStartTime: handleStartTime,
                            handleEndTime: handleEndTime,
                            maintenanceTime: maintenanceTime,
                            checkInfo: $('#checkInfoModify').text(),
                            faultInfoDetail: $('#faultInfoDetailModify').val(),
                            faultInfo: $('#faultInfoModify option:selected').text(),
                            handleInfo: $('#handleInfoModify').text(),
                            repairPerson: $('#repairUserModify option:selected').text(),
                            noticeUser: $('#noticeUserModify option:selected').text(),
                            faultType: $('#faultTypeModify option:selected').text(),
                            faultResponsible: faultResponsible,
                            faultResponsibleRemark: faultResponsibleRemark,
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
                                    $('#modifySheetModal').modal('hide')
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
                            if (sheetData.completeFlag == 5 && sheetData.handleEndTime == '') {
                                $('#popSheetVerifyModal').modal('hide')
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>故障处理结束时间为空！</strong></span>');
                                $("#infoAlert")
                                    .show();
                                hideTimeout(
                                    "infoAlert",
                                    2000);
                                return false;
                            }
                            if (sheetData.completeFlag == 5 && sheetData.type == '设备故障' && sheetData.handleInfo == '') {
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
                            let verifyUser2;
                            let verifyUser4;
                            let verifyDate2;
                            let verifyDate4;
                            let date = formatDateBy(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss')
                            if (completeFlag == 2) {//提交人员1
                                verifyUser2 = user_id
                                verifyDate2 = date
                            } else if (completeFlag == 4) {//提交人员2
                                verifyUser4 = user_id
                                verifyDate4 = date
                            }
                            if (completeFlag == 6) {//提交人员3
                                submitUser = user_id
                            }
                            var params = JSON.stringify({
                                id: id,
                                completeFlag: completeFlag + 1,
                                verifyUser2: verifyUser2,
                                verifyUser4: verifyUser4,
                                submitUser: submitUser,
                                verifyDate2: verifyDate2,
                                verifyDate4: verifyDate4,
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
                 * 回退单据
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
                /** 导出配件详情信息*/
                $("#exportExcel").on('click', function () {
                    window.location.href = config.basePath + '/faultHandle/exportExcel/' + deptId;
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
    });