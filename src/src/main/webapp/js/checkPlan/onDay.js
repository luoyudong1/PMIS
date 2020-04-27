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
                initDatetimepicker("planTimeDelay", date);
                initDatetimepicker("planTimeModify", date);

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
                let sheet_id = ''
                var format = 'Y-m-d H:i:s';
                var status;
                CMethod.initDatetimepickerWithSecond("startTimeModify", null, format);
                CMethod.initDatetimepickerWithSecond("endTimeModify", null, format);
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
                 * 初始化检修单据详情
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/checkPlan/checkPlan/listOnDay',
                        type: 'GET',
                        data: function (d) {
                            d.depotId = deptId;
                            d.queryTime = ($("#queryTime").val() == '' ? '' : $("#queryTime").val() + " 00:00:01");
                            d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
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
                        }, {
                            data: 'detectDepotName'
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
                            if (row.status == 2) {
                                str = '<a class="delaySheet btn btn-info btn-xs" data-toggle="modal" href="#delaySheetModal" title="延期"><span class="glyphicon glyphicon-time"></span></a>&nbsp;&nbsp;'
                            }
                            if (row.status == 2 || row.status == 4) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
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
                        $('#remarkModify').val(data.remark)
                        initModifyModal()
                    });

                function initModifyModal() {
                    if (status == 2 || status == 3) {
                        $('.checkEnd').each(function () {
                            $(this).hide()
                        })
                    }
                    if (status > 3) {
                        $('#startTimeModify').attr("readOnly", true)
                    }

                }

                /**
                 *延期检修计划
                 */
                $('#delaySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#detectDeviceDelay').val(data.detectDeviceName)
                        $('#detectTypeDelay').val(data.detectDeviceType)
                        $('#planTypeDelay').val(data.planType)
                        $('#planTimeDelay').val(data.planTime)
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
                            $("#alertMsgModify").html("<font style='color:red'>检修开始时间为空！请检查输入是否正确</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                            return false;
                        }
                        if (Date.parse(startTime) > date) {
                            $("#alertMsgModify").html("<font style='color:red'>检修开始时间大于当前时间</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                            return false;
                        }
                        if (status >= 4 && endTime == '') {
                            $("#alertMsgModify").html("<font style='color:red'>检修结束时间为空！请检查输入是否正确</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                            return false;
                        }
                        if (status >= 4 && Date.parse(endTime) < Date.parse(startTime)) {
                            $("#alertMsgModify").html("<font style='color:red'>检修结束时间小于检修开始时间！请检查输入是否正确</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
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
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>延期成功！</strong></span>');
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
                            let verifyUser1;
                            let verifyUser3;
                            let verifyDate1 = null;
                            let verifyDate3 = null;
                            let date = formatDateBy(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss')
                            if (status == 2) {//提交人员1
                                verifyUser1 = user_id
                                verifyDate1 = date
                            } else if (status == 4) {//提交人员2
                                verifyUser3 = user_id
                                verifyDate3 = date
                            }
                            var params = JSON.stringify({
                                id: id,
                                status: status + 1,
                                verifyUser1, verifyUser1,
                                verifyDate1, verifyDate1,
                                verifyUser3, verifyUser3,
                                verifyDate3, verifyDate3,
                                sheetId: sheet_id
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
                 * 点击回退确定按钮
                 */
                $('#btnPopBackSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: id,
                            status: status - 1
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