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
                var listDetect2;
                let lineSet = new Set();
                let lineSet2 = new Set();
                let deviceTypeSet = new Set();
                let deviceTypeSet2 = new Set();
                let sheet_id = '';
                let date = new Date()
                var detectDepotId=''
                let sheetData;
                let year='';
                let month='';
                /**
                 * 初始化时间框
                 */
                function initDatetimepicker(id, year,month,date) {
                    let startDate=year+'-'+(month-1)+'-'+'26';
                    let endDate=year+'-'+month+'-'+'25';
                    $.datetimepicker.setLocale('ch');
                    $('#' + id).datetimepicker({
                        format: 'Y-m-d',
                        value:date,
                        startDate:startDate,
                        endDate:endDate,
                        minDate: startDate,
                        maxDate: endDate,
                        timepicker:false,
                        todayButton: false
                        // 关闭时间选项
                    });
                }
                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                });
                $('#search_detect').click(function (e) {
                    sheetDetailTable.ajax.reload();
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
                 * 显示线别-临时检修
                 */
                function initLineAdd() {
                    $('#lineAdd').empty()
                    $('#lineAdd').append('<option></option>')
                    for (let line of lineSet) {
                        $('#lineAdd').append('<option>' + line + '</option>')
                    }
                }
                /**
                 * 显示线别-新增检修
                 */
                function initLineAdd2() {
                    $('#lineAdd2').empty()
                    $('#lineAdd2').append('<option></option>')
                    for (let line of lineSet2) {
                        $('#lineAdd2').append('<option>' + line + '</option>')
                    }
                }
                /**
                 * 显示探测站类型-临时检修
                 */
                function initDeviceTypeAdd() {
                    $('#detectTypeAdd').empty()
                    $('#detectTypeAdd').append('<option></option>')
                    for (let deviceType of deviceTypeSet) {
                        $('#detectTypeAdd').append('<option>' + deviceType + '</option>')
                    }
                }
                /**
                 * 显示探测站类型-新增检修
                 */
                function initDeviceTypeAdd2() {
                    $('#detectTypeAdd2').empty()
                    $('#detectTypeAdd2').append('<option></option>')
                    for (let deviceType of deviceTypeSet2) {
                        $('#detectTypeAdd2').append('<option>' + deviceType + '</option>')
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
                            depotId: detectDepotId,
                            faultEnable: 1
                        },
                        dataType: 'json',
                        success: function (result) {
                            listDetect = result.data
                            lineSet.clear()
                            deviceTypeSet.clear()
                            for (var i = 0; i < result.data.length; i++) {
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
                }

                //获取探测站
                getDetectDevice()

                /**
                 * 线别改变事件
                 */
                $("#lineAdd").change(function () {
                    $("#detectTypeAdd option:first").prop("selected", true);
                })
                /**
                 * 线别改变事件
                 */
                $("#lineAdd2").change(function () {
                    $("#detectTypeAdd2 option:first").prop("selected", true);
                })
                /**
                 * 探测站改变事件
                 */
                $("#detectDeviceAdd2").change(function () {
                    let planType = $("#detectDeviceAdd2 option:selected").attr("planCheckType");
                    $("#planTypeAdd2").val(planType)
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
                        if (listDetect[i].lineName == lineName && listDetect[i].deviceTypeName == detectType) {
                            $("#detectDeviceAdd").append('<option value="' + listDetect[i].detectDeviceId + '" deviceTypeName="' +
                                listDetect[i].deviceTypeName + '" planCheckType="' + listDetect[i].planCheckType + '" depotId="' +
                                listDetect[i].depotId + '" depotName="' + listDetect[i].depotName + '" dispatcher="' + listDetect[i].dispatcher + '">' + listDetect[i].detectDeviceName + '</option>');
                        }
                    }

                })
                /**
                 * 探测站类型改变事件
                 */
                $("#detectTypeAdd2").change(function () {
                    var lineName = $("#lineAdd2 option:selected").text();
                    var detectType = $("#detectTypeAdd2 option:selected").text();

                    $("#detectDeviceAdd2").empty()
                    $("#detectDeviceAdd2").append('<option></option>')
                    for (let i = 0; i < listDetect2.length; i++) {
                        if (listDetect2[i].lineName == lineName && listDetect2[i].deviceTypeName == detectType) {
                            $("#detectDeviceAdd2").append('<option value="' + listDetect2[i].detectDeviceId + '" deviceTypeName="' +
                                listDetect2[i].deviceTypeName + '" planCheckType="' + listDetect2[i].planCheckType + '" depotId="' +
                                listDetect2[i].depotId + '" depotName="' + listDetect2[i].depotName + '" dispatcher="' + listDetect2[i].dispatcher + '">' + listDetect2[i].detectDeviceName + '</option>');
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
                            d.flag = 3
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
                            data: 'total',
                            render:function (data, type, row, meta ) {
                                return row.completeCount+"/"+data
                            }
                        }, {
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
                        targets: 12,
                        data: function (row) {
                            var str = '';
                            if (row.flag == 1 || row.flag == 4) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;' + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;' + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
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
                        url: config.basePath + '/checkPlan/checkPlan/getPlanBySheetId',
                        type: 'GET',
                        data: function (d) {
                            d.sheetId = sheet_id
                            d.detectDeviceName = '%' + $('#detectDeviceName').val() + '%'
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
                                    str = '<span style="color:blue;font-weight:bold;">检修开始</span>';
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
                            if (row.status == 2) {
                                str += '<a class="delaySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="调整"><span class="glyphicon glyphicon-time"></span></a>&nbsp;&nbsp;'
                            }
                            if(row.status==2&&row.planType=='临时检修'){
                                str +='<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除"><span class="glyphicon glyphicon-remove"></span></a>'
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
                 * 新增临时检修计划详情
                 */
                $("#btnAddSheetDetailOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let date = new Date();
                        let planTime = $("#planTimeAdd").val()+" 23:59:59";
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
                            dispatcher: $('#detectDeviceAdd option:selected').attr("dispatcher"),
                            planTime: $('#planTimeAdd').val(),
                            createUser: user_id,
                            planType: $('#planTypeAdd').val(),
                            depotId: deptId,
                            sheetId: sheet_id,
                            status:2
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
                                    $('#detectDeviceAdd option:first').prop("selected", true)
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检修计划添加成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });
                /**
                 * 新增检修计划详情
                 */
                $("#btnAddSheetDetailOk2").on('click',
                    function (e) {
                        e.preventDefault();
                        let date = new Date();
                        let planTime = $("#planTimeAdd2").val()+" 23:59:59";
                        if ($("#detectDeviceAdd2").val() == "") {
                            $("#alertMsgAdd2").html("<font style='color:red'>探测站为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd2").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if ($("#planTimeAdd2").val() == "") {
                            $("#alertMsgAdd2").html("<font style='color:red'>检修时间为空！请检查输入是否正确</font>");
                            $("#alertMsgAdd2").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        } else if (Date.parse(planTime) <= date) {
                            $("#alertMsgAdd2").html("<font style='color:red'>检修时间小于当前时间！请检查输入是否正确</font>");
                            $("#alertMsgAdd2").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        var params = JSON.stringify({
                            detectDeviceId: $('#detectDeviceAdd2').val(),
                            detectDeviceName: $('#detectDeviceAdd2 option:selected').text(),
                            detectDeviceType: $('#detectDeviceAdd2 option:selected').attr("deviceTypeName"),
                            detectDepotId: $('#detectDeviceAdd2 option:selected').attr("depotId"),
                            detectDepotName: $('#detectDeviceAdd2 option:selected').attr("depotName"),
                            dispatcher: $('#detectDeviceAdd2 option:selected').attr("dispatcher"),
                            planTime: $('#planTimeAdd2').val(),
                            createUser: user_id,
                            planType: $('#planTypeAdd2').val(),
                            depotId: deptId,
                            sheetId: sheet_id,
                            status:2
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
                                    $('#detectTypeAdd2 option:first').prop("selected", true)
                                    $('#detectDeviceAdd2 option:first').prop("selected", true)
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>临时检修计划添加成功！</strong></span>');
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
                                sheetDetailTable.ajax.reload()

                                sheetData = sheetTrData
                                year = sheetTrData.year
                                month = sheetTrData.month
                                detectDepotId = sheetTrData.depotId
                            }
                        } );
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
                /**
                 * 新增检修计划详情-临时检修
                 */
                $('#addSheetDetailModal').on('show.bs.modal', function (e) {
                    $('#planTimeAdd').val('')
                    $('#createUserAdd').val(user_id)
                    $('#detectDeviceAdd option:first').prop("selected", true)
                    $('#lineAdd option:first').prop("selected", true)
                    initDatetimepicker('planTimeAdd',year,month,null)
                })
                /**
                 * 新增检修计划详情-新增检修
                 */
                $('#addModal').on('show.bs.modal', function (e) {
                    getDetect()
                    initLineAdd2()
                    initDeviceTypeAdd2()
                    $('#planTimeAdd2').val('')
                    $('#createUserAdd2').val(user_id)
                    $('#detectDeviceAdd2').empty()
                    $('#lineAdd2 option:first').prop("selected", true)
                    $('#detectTypeAdd2 option:first').prop("selected", true)
                    initDatetimepicker('planTimeAdd2',year,month,null)
                })
                /**
                 * 获取当前单据可增加的探测站
                 */
                function getDetect(){
                    $.ajax({
                        async:false,
                        url: config.basePath + '/checkPlan/sheet/getDetect',
                        type: "get",
                        data: {
                            depotId:deptId,
                            sheetId:sheet_id
                        },
                        contentType: 'application/json',
                        dataType: "json",
                        success: function (result) {

                            listDetect2=result.list;
                            lineSet2=result.lines;
                            deviceTypeSet2=result.detectDeviceType

                        }
                    });
                }
                /**
                 * 修改检修计划模态框
                 */
                $('#modifySheetDetailModal').on('show.bs.modal',
                    function (e) {
                        let tr = $(e.relatedTarget).parents('tr');
                        let data = sheetDetailTable.row(tr).data();
                        id = data.id;
                        initDatetimepicker('planTimeModify',year,month,data.planTime)
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
                 * 删除该探测站计划
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