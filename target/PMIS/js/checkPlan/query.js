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


                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                var roleName = window.parent.user.roleName; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var completeFlag;
                var status;
                var id;
                var date = new Date()

                let sheet_id = '';

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
                 * 初始化检修单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/checkPlan/sheet/getAllSheets',
                        type: 'GET',
                        data: function (d) {
                            d.depotId = deptId
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
                        url: config.basePath + '/checkPlan/checkPlan/getPlanBySheetId',
                        type: 'GET',
                        data: function (d) {
                            d.sheetId = sheet_id
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
                        },  {
                            data: 'detectDepotName'
                        }, {
                            data: 'detectDeviceType'
                        },
                        {
                            data: 'planTime'
                        },
                        {
                            data: 'oldPlanTime'
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
                                    str = '<span style="color:blue;font-weight:bold;">待检修</span>';
                                }
                                else if (data == 3) {
                                    str = '<span style="color:blue;font-weight:bold;">检修开始待审核</span>';
                                } else if (data == 4) {
                                    str = '<span style="color:blue;font-weight:bold;">检修结开始</span>';
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
                        targets: 14,
                        data: function (row) {
                            var str = '-';
                            if (row.status == 2 && roleName == '段值班员') {
                                str = '<a class="delaySheet btn btn-info btn-xs" data-toggle="modal" href="#delaySheetModal" title="延期"><span class="glyphicon glyphicon-time"></span></a>&nbsp;&nbsp;'
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
                                sheetDetailTable.ajax.reload()
                            }

                        });

                /*******************************************************
                 * 单据点击事件
                 ********************************************************/
                $('#sheetDetailTable tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前单号
                            if (sheetDetailTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                var sheetDetailTrData = sheetDetailTable
                                    .row(tr).data();
                                status = sheetDetailTrData.status
                                id = sheetDetailTrData.id
                            }

                        });

                /**
                 *延期检修计划模态框
                 */
                $('#delaySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        id = data.id;
                        $('#detectDeviceDelay').val(data.detectDeviceName)
                        $('#detectTypeDelay').val(data.detectDeviceType)
                        $('#planTypeDelay').val(data.planType)
                        $('#planTimeDelay').val(data.planTime)
                    });
                /**
                 * 延期按钮事件
                 */
                $("#btnDelaySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let date = new Date()
                        let planTime = $('#planTimeDelay').val()
                        if (planTime < date) {
                            $("#alertMsgDelay").html("<font style='color:red'>延期时间小于当前时间</font>");
                            $("#alertMsgDelay").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgDelay", 5000);
                        }
                        var params = JSON.stringify({
                            id: id,
                            sheetId: sheet_id,
                            planTime: $('#planTimeDelay').val(),
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/delay',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetDetailTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>延期成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });
                /** 导出配件详情信息 */
                $("#sheetTable tbody").on('click', '#exportExcel',
                    function () {
                        if(sheet_id!='')
                            window.location.href = config.basePath + '/checkPlan/sheet/export/' + sheet_id;
                    });
                /** *************** **/
                sheetTable.on('draw.dt', function () {
                    //给第一列编号
                    sheetTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });
                sheetDetailTable.on('draw.dt', function () {
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