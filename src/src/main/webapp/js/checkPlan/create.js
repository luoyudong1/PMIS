/**
 * 故障预报
 */
require(['../config'],
    function (config) {

        require(['datetimepicker'],
            function () {
                let date = new Date();
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

                /**
                 * 初始化时间框
                 */
                function initDatetimepickerLimitDate(id, date, startDate, endDate) {
                    $.datetimepicker.setLocale('ch');
                    $('#' + id).datetimepicker({
                        value: date,
                        format: 'Y-m-d',
                        timepicker: false,
                        // 关闭时间选项
                        todayButton: true,
                        // 关闭选择今天按钮
                        startDate: startDate,
                        endDate: endDate
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
                var format = 'Y-m-d';
                var detectDeviceId;
                var type;
                var detectDevice;
                var listDetect;
                let lineSet = new Set();
                let deviceTypeSet = new Set();
                let sheet_id = '';
                let date = new Date()
                let sheetData;
                CMethod.initDatetimepicker('planTimeAdd', date)
                CMethod.initDatetimepicker('planTimeModify', date)
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
                    $('#lineAdd').empty()
                    $('#lineAdd').append('<option></option>')
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
                    $('#yearAdd').empty()
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

                function getDetectDevice() {
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
                            lineSet.clear()
                            deviceTypeSet.clear()
                            for (var i = 0; i < result.data.length; i++) {
                                if (result.data[i].planCheckEnable == 0 || (result.data[i].planCheckEnable == 1 && result.data[i].planCheckType == "半月检")) {
                                    lineSet.add(result.data[i].lineName)
                                    deviceTypeSet.add(result.data[i].deviceTypeName)
                                }
                            }
                            initLineAdd()
                            initDeviceTypeAdd()
                        },
                        error: function (result) {
                            console.log(result);
                        }
                    });
                }

                //获取探测站
                getDetectDevice()
                //初始化年份选择下拉框
                initYearAdd()
                //初始化部门名称
                initDepotNameAdd()
                /**
                 * 线别改变事件
                 */
                $("#lineAdd").change(function () {
                    $("#detectTypeAdd option:first").prop("selected", true);
                })
                /**
                 * 探测站改变事件
                 */
                $("#detectDeviceAdd").change(function () {
                    let planType = $("#detectDeviceAdd option:selected").attr("planCheckType");
                    $("#planTypeAdd").val(planType)
                })
                /**
                 * 探测站类型改变事件
                 */
                $("#detectTypeAdd").change(function () {
                    var lineName = $("#lineAdd option:selected").text();
                    var detectType = $("#detectTypeAdd option:selected").text();

                    $("#detectDeviceAdd").empty()
                    $("#detectDeviceAdd").append('<option></option>')
                    for (let i = 0; i < listDetect.length; i++) {
                        if (listDetect[i].lineName == lineName && listDetect[i].deviceTypeName == detectType && listDetect[i].planCheckEnable == 0) {
                            $("#detectDeviceAdd").append('<option value="' + listDetect[i].detectDeviceId + '" deviceTypeName="' +
                                listDetect[i].deviceTypeName + '" planCheckType="' + listDetect[i].planCheckType + '" depotId="' +
                                listDetect[i].depotId + '" depotName="' + listDetect[i].depotName +'" dispatcher="' + listDetect[i].dispatcher+ '">' + listDetect[i].detectDeviceName + '</option>');
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
                            if (row.flag == 1 || row.flag == 4) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;' + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;' + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                            } else {
                                str += '';
                            }
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
                        targets: 13,
                        data: function (row) {
                            var str = '';
                            if (row.status == 1 && completeFlag == 1) {
                                str += '<a class="modifySheetDetail btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除"><span class="glyphicon glyphicon-remove"></span></a>';
                            } else {
                                str += '-';
                            }
                            return str;


                        }
                    }
                    ],
                    ordering: false,
                    paging:
                        true,
                    pageLength:
                        10,
                    serverSide:
                        false,
                    drawCallback:

                        function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                            api.column(0).nodes().each(function (cell, i) {
                                cell.innerHTML = startIndex + i + 1;
                            });
                        }

                    ,
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
                        // let date=new Date(sheetData.year,sheetData.month,1)
                        // date.setMonth(date.getMonth())
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
                        let date = new Date();
                        let planTime = $("#planTimeAdd").val();
                        if ($("#detectDeviceAdd").val() == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>探测站为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if ($("#planTimeAdd").val() == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>检修时间为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        } else if (Date.parse(planTime) <= date) {
                            $("#alertMsgAdd").html("<font style='color:red'>检修时间小于当前时间！请检查输入是否正确</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        var params = JSON.stringify({
                            detectDeviceId: $('#detectDeviceAdd').val(),
                            detectDeviceName: $('#detectDeviceAdd option:selected').text(),
                            detectDeviceType: $('#detectDeviceAdd option:selected').attr("deviceTypeName"),
                            detectDepotId: $('#detectDeviceAdd option:selected').attr("depotId"),
                            detectDepotName: $('#detectDeviceAdd option:selected').attr("depotName"),
                            dispatch: $('#detectDeviceAdd option:selected').attr("dispatch"),
                            planTime: $('#planTimeAdd').val(),
                            createUser: user_id,
                            planType: $('#planTypeAdd').val(),
                            depotId: deptId,
                            sheetId: sheet_id
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
                                    getDetectDevice()
                                    sheetDetailTable.ajax.reload();
                                    $('#detectTypeAdd option:first').prop("selected", true)
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
                                if (completeFlag == 3) { // 已审核不能添加
                                    $("#add_sheetDetail").hide();
                                } else {
                                    $("#add_sheetDetail").show()
                                }
                                sheetDetailTable.ajax.reload()
                            }
                            sheetData = sheetTrData
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
                                id = sheetDetailTrData.id
                                detectDeviceId = sheetDetailTrData.detectDeviceId
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
                 * 修改检修计划模态框
                 */
                $('#modifySheetDetailModal').on('show.bs.modal',
                    function (e) {
                        let tr = $(e.relatedTarget).parents('tr');
                        let data = sheetDetailTable.row(tr).data();
                        id = data.id;
                        $('#detectDeviceModify').val(data.detectDeviceName)
                        $('#detectTypeModify').val(data.detectDeviceType)
                        $('#planTypeModify').val(data.planType)
                        $('#planTimeModify').val(data.planTime)

                    });


                /**
                 * 修改检修计划确定按钮
                 */
                $("#btnModifySheetDetailOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let planTime = $('#planTimeModify').val()
                        let date = new Date()
                        if (Date.parse(planTime) < date.toLocaleDateString()) {
                            $("#alertMsgSheetModify").html("<font style='color:red'>检修时间小于当前时间！请检查输入是否正确</font>");
                            $("#alertMsgSheetModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgSheetModify", "alertMsgSheetModify", 5000);
                            return false;
                        }
                        var params = JSON.stringify({
                            id: id,
                            planTime: $('#planTimeModify').val(),

                        });

                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/update',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    sheetDetailTable.ajax.reload()
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>修改检修计划成功！</strong></span>');
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
                                '确定提交单据ID为:' + data.sheetId
                                + '的单据吗？');
                            $('#btnPopSheetVerifyOk').val(
                                data.sheetId);
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
                                sheetId: sheet_id,
                                flag: completeFlag + 1
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/checkPlan/sheet/update',
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
                                            completeFlag = 2
                                            sheetDetailTable.ajax.reload()
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
                        $('#warningSheetText').text('确定要删除该检修单据（' + data.sheetId + '）？');
                    });
                /**
                 * 点击删除确定按钮
                 */
                $('#btnPopSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            sheetId: sheet_id
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/sheet/delete',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    sheetDetailTable.ajax.reload();
                                    getDetectDevice()
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检修单据删除成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });

                    });
                /**
                 * 删除该单据
                 */
                $('#popModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        $('#deleteText').text('确定要删除该探测站检修计划（' + data.id + '）？');
                    });
                /**
                 * 点击删除确定按钮
                 */
                $('#btnPopOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            id: id,
                            detectDeviceId: detectDeviceId
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
                                    sheetDetailTable.ajax.reload();
                                    getDetectDevice()
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检修计划删除成功！</strong></span>');
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