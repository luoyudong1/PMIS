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
                 * 初始化入库单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/faultHandle/faultReport/search',
                        type: 'GET',
                        data: function (d) {
                            d.depotId=deptId
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