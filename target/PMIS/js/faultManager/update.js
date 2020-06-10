/**
 * 通信故障
 */
require(['../config'],
    function (config) {

        require(['datetimepicker'],
            function () {
                let date = new Date();
                let preMonth = new Date();
                preMonth.setMonth(date.getMonth() - 1)
                initDatetimepicker("queryTime", preMonth);
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
                var sheetData;
                var deviceType

                /**
                 * 初始化时间框
                 */
                function initDatetimepicker(id,startDate,endDate) {
                    $.datetimepicker.setLocale('ch');
                    $('#' + id).datetimepicker({
                        format: 'Y-m-d H:i',
                        startDate:startDate,
                        endDate:endDate,
                        minDate: startDate,
                        maxDate: endDate,
                        todayButton: false
                        // 关闭时间选项
                    });
                }
                // CMethod.initDatetimepickerWithSecond("handleEndTime2Update", null, format);
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
                    var date = new Date();
                    initDatetimepicker("queryTime", date);
                    initDatetimepicker("queryTime2", date);
                });

                /**
                 * 初始化入库单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/faultHandle/faultReport/list',
                        type: 'GET',
                        data: function (d) {
                            if (roleName.indexOf("集团调度员") == -1) {
                                d.depotId = deptId;
                                d.completeFlag = 7;
                                d.type = "设备故障";
                            } else {
                                d.completeFlag = 7;
                                d.type = "设备故障";
                            }
                            d.detectDeviceName = $("#detectDeviceName").val();
                            d.queryTime = $("#queryTime").val();
                            d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
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
                            data: 'haultStartTime'
                        },
                        {
                            data: 'handleEndTime'
                        },
                        {
                            data: 'faultStopTime'
                        },
                        {
                            data: 'faultLevelType'
                        }, {
                            data: 'handleInfo'
                        }, {
                            data: 'forecastFaultTime'
                        }, {
                            data: 'segmentDutyUser'
                        }, {
                            data: 'handleStartTime'
                        }, {
                            data: 'handleEndTime'
                        }, {
                            data: 'appealReason'
                        }, {
                            data: 'faultType'
                        },
                    ],
                    columnDefs: [{
                        targets: 16,
                        data: function (row) {
                            let str = '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="核准全部故障"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            str+= '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#updateSheetTypeModal" title="核准部分故障"><span class="glyphicon glyphicon-edit" style="color: red;"></span></a>&nbsp;&nbsp;'
                            return str;
                        }
                    }],
                    ordering: false,
                    paging: true,
                    pageLength: 10,
                    serverSide: false,
                    fnCreatedRow: function (row, data) {
                        if (data.adjustSubmitUser!=null) {
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
                            }

                        });

                /**
                 * 修改部分故障
                 */
                $('#updateSheetTypeModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#lineUpdate').val(data.lineName)
                        $('#detectTypeUpdate').val(data.detectDeviceType)
                        $('#detectDeviceUpdate').val(data.detectDeviceName)
                        $('#haultStartTimeUpdate').val(data.haultStartTime)
                        $('#handleEndTimeUpdate').val(data.handleEndTime)
                        $('#typeUpdate').val(data.type)
                        $('#handleEndTime2Update').val('')
                        initDatetimepicker('handleEndTime2Update',data.haultStartTime,data.handleEndTime)
                    });

                /**
                 * 修改部分故障
                 */
                $("#btnUpdateSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let haultStartTimeUpdate=$('#haultStartTimeUpdate').val()
                        let handleEndTimeUpdate=$('#handleEndTimeUpdate').val()
                        let handleEndTime2Update=$('#handleEndTime2Update').val()
                        let adjustReason=$('#adjustReason').val()
                        if (handleEndTime2Update=="") {
                            $("#alertMsgSheetTypeModify").html("<font style='color:red'>部分故障结束时间为空！</font>");
                            $("#alertMsgSheetTypeModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetTypeModify", "alertMsgSheetTypeModify", 5000);
                            return false
                        }else  if (handleEndTime2Update<haultStartTimeUpdate) {
                            $("#alertMsgSheetTypeModify").html("<font style='color:red'>部分故障结束时间小于故障开始时间！</font>");
                            $("#alertMsgSheetTypeModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetTypeModify", "alertMsgSheetTypeModify", 5000);
                            return false
                        }else  if (handleEndTime2Update>handleEndTimeUpdate) {
                            $("#alertMsgSheetTypeModify").html("<font style='color:red'>部分故障结束时间大于故障结束时间！</font>");
                            $("#alertMsgSheetTypeModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetTypeModify", "alertMsgSheetTypeModify", 5000);
                            return false
                        }else  if (adjustReason=='') {
                            $("#alertMsgSheetTypeModify").html("<font style='color:red'>调整理由为空！</font>");
                            $("#alertMsgSheetTypeModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetTypeModify", "alertMsgSheetTypeModify", 5000);
                            return false
                        }
                      $('#popSheetUpdateModal').modal('show')
                    });

                $("#btnPopSheetUpdateOk").on('click',
                    function (e) {
                        var params = JSON.stringify({
                            id: id,
                            type: $('#type2Update option:selected').text(),
                            handleEndTime: $('#handleEndTime2Update').val(),
                            adjustSubmitUser:user_id,
                            adjustReason:$('#adjustReason').val(),
                        });
                        $.ajax({
                            url: config.basePath + '/faultHandle/faultReport/updatePartOfFault',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    $('#updateSheetTypeModal').modal('hide')
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>该故障已分为两条故障，原故障发生时间和结束时间已被修改为对应故障的两段时间！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 5000);
                                }
                            }

                        })
                    })



                /**
                 * 修改故障预报
                 */
                $("#btnModifySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let type=$('#type option:selected').text()
                        let adjustReason=$('#adjustReason1').text()
                        if (type=='设备故障') {
                            $("#alertMsgSheetModify").html("<font style='color:red'>未改变故障类型，仍为设备故障！</font>");
                            $("#alertMsgSheetModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                            return false;
                        }else if(adjustReason=='') {
                            $("#alertMsgSheetTypeModify").html("<font style='color:red'>调整理由为空！</font>");
                            $("#alertMsgSheetTypeModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetTypeModify", "alertMsgSheetTypeModify", 5000);
                            return false
                        }

                        var params = JSON.stringify({
                            id: id,
                            type:type,
                            adjustSubmitUser:user_id,
                            adjustReason:adjustReason,

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
                 * 修改故障预报表
                 */
                $('#modifySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#lineModify').val(data.lineName)
                        $('#detectTypeModify').val(data.detectDeviceType)
                        $('#detectDeviceModify').val(data.detectDeviceName)
                        $('#haultStartTimeModify').val(data.haultStartTime)
                        $('#handleEndTimeUpdate2').val(data.handleEndTime)
                        $('#type option:contains("'+data.type+'")').prop("selected",true)

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