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
                            data: 'repairPerson'
                        }, {
                            data: 'faultType'
                        },
                        {
                            data: 'appealFlag',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '<span style="color:blue;font-weight:bold;">申诉中</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:black;font-weight:bold;">申诉成功</span>';
                                } else {
                                    str = '-';
                                }
                                return str;
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 17,
                        data: function (row) {
                            var str = ''
                            if (row.appealFlag != 1) {
                                str = '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="申诉"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            }
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
                 * 修改故障预报
                 */
                $("#btnModifySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: id,
                            appealFlag: 1,
                            appealReason: $('#appealReasonModify').val(),

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
                        $('#lineModify').val(data.lineName)
                        $('#detectTypeModify').val(data.detectDeviceType)
                        $('#detectDeviceModify').val(data.detectDeviceName)
                        $('#haultStartTimeModify').val(data.haultStartTime)
                        $('#appealReasonModify').val(data.appealReason)
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