/**
 * 信息故障
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



                var roleName = window.parent.user.roleName; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
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
                                d.completeFlag = 5;
                                d.type = "信息故障";
                            } else {
                                d.completeFlag = 5;
                                d.type = "信息故障";
                            }
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
                            data: 'faultStopTime'
                        },
                        {
                            data: 'faultInfo'
                        },
                        {
                            data: 'faultLevelType'
                        }, {
                            data: 'telegraphNumber'
                        }, {
                            data: 'forecastFaultTime'
                        }, {
                            data: 'segmentDutyUser'
                        }, {
                            data: 'noticeTime'
                        }, {
                            data: 'responsibleUnit'
                        }, {
                            data: 'remark'
                        }, {
                            data: 'planOutageStartTime'
                        }, {
                            data: 'planOutageEndTime'
                        },
                        {
                            data: 'completeFlag',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '<span style="color:red;font-weight:bold;">新建</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:blue;font-weight:bold;">审核中</span>';
                                } else if (data == 3) {
                                    str = '<span style="color:blue;font-weight:bold;">故障处理中</span>';
                                }
                                else if (data == 4) {
                                    str = '<span style="color:blue;font-weight:bold;">故障处理确认中</span>';
                                }
                                else if (data == 5) {
                                    str = '<span style="color:black;font-weight:bold;">已完成</span>';
                                }
                                return str;
                            }
                        },
                    ],
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

            });
    });