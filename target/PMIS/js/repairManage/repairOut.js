/**
 * 所内返修
 */
require(
    ['../config'],
    function (config) {
        //
        require(
            ['jquery', 'popper', 'bootstrap', 'common/dt',
                'common/commonMethod', 'common/dateFormat',
                'metisMenu', 'slimscroll', 'pace', 'inspinia'],
            function ($, Popper, Bootstrap, dataTable, CMethod) {
                const entryTypes = ['手动添加', '采购入库', '生产入库', '调拨入库'];
                const sheetTypesName = ['不合格', '合格', '报废'];
                const sheetTypesCode = ['05', '06', '07'];
                const storeHouseName = ['机辆检测所返厂库', '机辆检测所配送库',
                    '机辆检测所报废库'];
                let screenHeight = screen.height * 0.5 + 'px';
                let timer = 0;
                var sheet_id = '';// 查询的单号
                var sheetId = '';// 查询的单号
                var verify_flag = '';// 查询的审核状态
                var newDate = new Date();
                var sendVerifyFlag = '';
                var sheet_id2 = '';
                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                var deptId = window.parent.user.deptId; // 登录人部门id
                var sheetTrData = '';// 保留点击的单号信息
                var objectStoreHouseName = '';// 保留点击的目的仓库名称信息
                var sheetTrData = '';// 保留点击的单号信息
                var sheetDetailTable;// 保留点击的单号信息
                var sheetDetailParams = "";
                var objectStoreHouseSelected = "";
                var sheetTypeSelected = "";
                var repaireState = "";//修复状态0.不合格1：合格 2：报废
                // 初始化时间查询框
                var date = new Date();
                var newDate = new Date();
                date.setDate("1");
                date.setMonth(date.getMonth() - 1)
                CMethod.initDatetimepicker('queryTimeBegin', date);
                CMethod.initDatetimepicker('queryTimeEnd', newDate);
                CMethod.initDatetimepicker('queryTime', date);
                CMethod.initDatetimepicker('queryTime2', newDate);
                CMethod.initDatetimepicker('factory_checkedDate', newDate);
                CMethod.initDatetimepicker('delivery_checkedDate', newDate);
                CMethod.initDatetimepicker('scrap_checkedDate', newDate);
                // 初始化仓库查询项
                $
                    .ajax({
                        async: false,
                        url: config.basePath
                            + "/repairManage/repairOut/getStorehouseOfRepairOut",
                        data: {
                            "action": "all"
                        },
                        dataType: 'json',
                        success: function (result) {
                            for (var i = 0; i < result.data.length; i++) {
                                $("#storehouse_id_sel")
                                    .append(
                                        '<option value="'
                                        + result.data[i].storehouse_id
                                        + '">'
                                        + result.data[i].storehouse_name
                                        + '</option>');
                                $("#objectStoreHouse")
                                    .append(
                                        '<option value="'
                                        + result.data[i].storehouse_id
                                        + '">'
                                        + result.data[i].storehouse_name
                                        + '</option>');
                            }
                        },
                        error: function (result) {
                            console.log(result);
                        }
                    });
                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                    sheetDetailTable.column(0).search('');
                    sheetDetailTable.clear().draw();
                    sheetId = '';
                    verifyFlag = '';
                });

                /**
                 * 时间加载
                 */
                function loadDate(id, date) {
                    $.datetimepicker.setLocale('ch');
                    $('#' + id).datetimepicker({
                        value: date,
                        format: 'Y-m-d',
                        timepicker: false, // 关闭时间选项
                        todayButton: true
                        // 关闭选择今天按钮
                    });
                }

                /**
                 * 初始化入库单据
                 */

                var sheetTable = dataTable(
                    'sheetTable',
                    {
                        bAutoWidth: false,
                        ajax: {
                            url: config.basePath
                                + '/repairManage/repairOut/getAllSheets',
                            type: "get",
                            data: function (d) {
                                d.sheetType = 2;
                                d.sheetId = "%"
                                    + $('#sheet_id').val()
                                    + "%";
                                d.objectStoreHouseId = $(
                                    '#storehouse_id_sel').val();
                                d.sendVerifyFlag = $('#verify_flag')
                                    .val();
                                d.queryTime = $("#queryTime").val();
                                d.queryTime2 = ($("#queryTime2")
                                    .val() == '' ? '' : $(
                                    "#queryTime2").val()
                                    + " 23:59:59");
                            }
                        },
                        columns: [
                            {
                                data: null
                            },
                            {
                                data: 'sheetId'
                            },
                            {
                                data: 'sourceStorehouseName'
                            },
                            {
                                data: 'objectStorehouseName',
                            },
                            {
                                data: 'addDate',
                                render: function (data) {
                                    if (data > 0) {
                                        return formatDateBy(
                                            data,
                                            'yyyy-MM-dd');
                                    } else {
                                        return '-';
                                    }
                                }
                            },
                            {
                                data: 'sendRemark'
                            },
                            {
                                data: 'sendOperatorName'
                            },
                            {
                                data: 'sendVerifyName'
                            },
                            {
                                data: 'sendVerifyFlag',
                                render: function (data) {
                                    var str = "-";
                                    if (data == 0) {
                                        str = '<span style="color:red;font-weight:bold;">新建单据</span>';
                                    } else if (data == 1) {
                                        str = '<span style="color:blue;font-weight:bold;">审核中</span>';
                                    } else if (data == 2) {
                                        str = '<span style="color:black;font-weight:bold;">已审核</span>';
                                    }
                                    else if (data == 3) {
                                        str = '<span style="color:red;font-weight:bold;">审核不通过</span>';
                                    }
                                    return str;
                                }
                            },
                            {
                                data: 'sendVerifyDate',
                                render: function (data) {
                                    if (data > 0) {
                                        return formatDateBy(
                                            data,
                                            'yyyy-MM-dd');
                                    } else {
                                        return '-';
                                    }
                                }
                            }],
                        columnDefs: [{
                            targets: 10,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                    str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                        + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                        + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                                }
                                str += '<button id="exportExcel" type="button" class="btn btn-success btn-xs" title="导出"><span class="glyphicon glyphicon-download-alt"></span></button>';
                                return str;
                            }
                        }],
                        ordering: true,
                        paging: true,
                        pageLength: 5,
                        serverSide: false,
                        drawCallback: function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart;// 获取到本页开始的条数
                            api.column(0).nodes().each(
                                function (cell, i) {
                                    cell.innerHTML = startIndex
                                        + i + 1;
                                });
                        },
                    });
                /**
                 * 目的仓库改变事件
                 */
                $("#objectStoreHouse")
                    .change(
                        function () {
                            objectStoreHouseSelected = $(
                                "#objectStoreHouse option:selected")
                                .text()
                            for (j = 0; j < storeHouseName.length; j++) {
                                if (storeHouseName[j] == objectStoreHouseSelected) {
                                    sheetTypeSelected = sheetTypesCode[j]

                                    break;
                                } else {
                                    sheetTypeSelected = "";
                                }
                            }
                            /**
                             * 添加单据
                             */
                            // 单据ID为单据类型+部门id+序列号
                            if (sheetTypeSelected == "") {
                                $('#sheetIdAdd').val("");
                                return;
                            }
                            if (deptId < 10) {
                                var sheetId = sheetTypeSelected
                                    + "-0" + deptId + "-";
                            } else {
                                var sheetId = sheetTypeSelected
                                    + "-" + deptId + "-";
                            }
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/entryAndOut/purchaseParts/getMaxSheetId',
                                    type: "get",
                                    data: {
                                        sheet_id: sheetId
                                    },
                                    contentType: 'application/json',
                                    dataType: "text",
                                    success: function (
                                        result) {
                                        // alert(result)
                                        $('#sheetIdAdd')
                                            .val(result);
                                    },

                                });

                            loadDate("sheetDateAdd", new Date());
                            $('#sendOperatorNameAdd').val(
                                user_name);
                            $('#sendRemarkAdd').val('');
                        });

                $("#btnAddSheetOk")
                    .on(
                        'click',
                        function (e) {
                            e.preventDefault();
                            if ($('#sheetIdAdd').val() == null || $('#sheetIdAdd').val() == "") {
                                $("#alertSheetMsgAdd").html("<font style='color:red'>请选择目的仓库</font>");
                                $("#alertSheetMsgAdd").css('display', 'inline-block')
                                timer = CMethod.hideTimeout("alertSheetMsgAdd", "alertSheetMsgAdd", 5000);

                                return false;
                            }
                            var params = JSON
                                .stringify({
                                    sheetId: $(
                                        '#sheetIdAdd')
                                        .val(),
                                    objectStoreHouseId: $(
                                        "#objectStoreHouse")
                                        .val(),
                                    supplierName: $(
                                        '#supplierNameAdd option:selected')
                                        .text(),
                                    addDate: $(
                                        '#sheetDateAdd')
                                        .val(),
                                    sendOperatorId: user_id,
                                    sendOperatorName: $(
                                        '#sendOperatorNameAdd')
                                        .val(),
                                    sendRemark: $(
                                        '#sendRemarkAdd')
                                        .val(),
                                });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repairOut/createSheetInfo',
                                    type: "post",
                                    data: params,
                                    contentType: 'application/json',
                                    dataType: "json",
                                    success: function (
                                        result) {
                                        if (result.code != 0) {
                                            // alert(result.msg);
                                        } else {
                                            sheetTable.ajax
                                                .reload();
                                            $("#alertMsg")
                                                .html(
                                                    '<span style="color:green;text-align:center"><strong>单据信息添加成功！</strong></span>');
                                            $("#infoAlert")
                                                .show();
                                            hideTimeout(
                                                "infoAlert",
                                                2000);
                                            $('#addSheetModal').modal('hide')

                                        }
                                    }
                                });
                        });

                /*******************************************************
                 *
                 */
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
                                sheetTrData = sheetTable
                                    .row(tr).data();
                                sheet_id2 = sheetTrData.sheetId;
                                sendVerifyFlag = sheetTrData.sendVerifyFlag;
                                objectStoreHouseName = sheetTrData.objectStorehouseName;
                                sheetDetailParams = $.param({
                                    sheetId: sheet_id2
                                });
                                if (sendVerifyFlag == 0 || sendVerifyFlag == 3) {// 已审核不能添加
                                    $("#add_sheetDetail")
                                        .show();
                                } else {
                                    $("#add_sheetDetail")
                                        .hide();
                                }

                                if (objectStoreHouseName == '机辆检测所返厂库') {
                                    repaireState = 0
                                    sheetDetailTable = sheetDetailTable1;
                                    $('#sheetDetailTableDiv1')
                                        .show();
                                    $('#sheetDetailTableDiv2')
                                        .hide();
                                    $('#sheetDetailTableDiv3')
                                        .hide();

                                } else if (objectStoreHouseName == '机辆检测所配送库') {
                                    // loadSheetDetail2(params).column(0).search('');
                                    sheetDetailTable = sheetDetailTable2;
                                    repaireState = 1
                                    $('#sheetDetailTableDiv2')
                                        .show();
                                    $('#sheetDetailTableDiv1')
                                        .hide();
                                    $('#sheetDetailTableDiv3')
                                        .hide();

                                } else if (objectStoreHouseName == '机辆检测所报废库') {
                                    // loadSheetDetail3(params).column(0).search('');
                                    repaireState = 2
                                    sheetDetailTable = sheetDetailTable3;
                                    $('#sheetDetailTableDiv3')
                                        .show();
                                    $('#sheetDetailTableDiv1')
                                        .hide();
                                    $('#sheetDetailTableDiv2')
                                        .hide();

                                }
                                tblPartsInfo.ajax.reload();
                                sheetDetailTable.ajax
                                    .url(
                                        config.basePath
                                        + '/repairManage/repairOut/getAllSheetDetails?'
                                        + sheetDetailParams)
                                    .load();

                            }
                        });

                /**
                 * 获取返厂库送修单据配件详情表
                 */
                var sheetDetailTable1 = dataTable(
                    'sheetDetailTable1',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'POST',
                            data: "",
                            url: config.basePath
                                + '/repairManage/repairOut/getAllSheetDetails'

                        },
                        columns: [{
                            data: null
                        }, {
                            data: 'partId'
                        }, {
                            data: 'partCode'
                        }, {
                            data: 'devicePartsName'
                        }, {
                            data: 'deviceTypeName'
                        }, {
                            data: 'deviceModelName'
                        }, {
                            data: 'assetAttributesName'
                        }, {
                            data: 'partState',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '新购';
                                } else if (data == 2) {
                                    str = '送修';
                                } else if (data == 3) {
                                    str = '初始化在探测站';
                                } else if (data == 4) {
                                    str = '初始化在备品库';
                                } else if (data == 5) {
                                    str = '初始化在送修库';
                                }
                                return str;
                            }
                        }, {
                            data: 'faultInfo'
                        }, {
                            data: 'warranty',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '否';
                                } else if (data == 1) {
                                    str = '是';
                                } else {
                                    str = '-';
                                }
                                return str;
                            }
                        }, {
                            data: 'checkedDate'
                        }, {
                            data: 'checkedUserName'
                        }, {
                            data: 'checkedRemark'
                        }],
                        columnDefs: [{
                            targets: 13,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                    str += '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popPartsModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                                } else {
                                    str += '';
                                }
                                return str;
                            }
                        }],
                        ordering: false,
                        paging: true,
                        pageLength: 5,
                        drawCallback: function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                            api.column(0).nodes().each(
                                function (cell, i) {
                                    cell.innerHTML = startIndex
                                        + i + 1;
                                });
                        }
                    });

                /**
                 * 获取配送出库单据配件详情表
                 */

                var sheetDetailTable2 = dataTable(
                    'sheetDetailTable2',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'POST',
                            data: "",
                            url: config.basePath
                                + '/repairManage/repairOut/getAllSheetDetails'

                        },
                        columns: [{
                            data: null
                        }, {
                            data: 'partId'
                        }, {
                            data: 'partCode'
                        }, {
                            data: 'devicePartsName'
                        }, {
                            data: 'deviceTypeName'
                        }, {
                            data: 'deviceModelName'
                        }, {
                            data: 'assetAttributesName'
                        }, {
                            data: 'partState',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '新购';
                                } else if (data == 2) {
                                    str = '送修';
                                } else if (data == 3) {
                                    str = '初始化在探测站';
                                } else if (data == 4) {
                                    str = '初始化在备品库';
                                } else if (data == 5) {
                                    str = '初始化在送修库';
                                }
                                return str;
                            }
                        }, {
                            data: 'warranty',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '否';
                                } else if (data == 1) {
                                    str = '是';
                                } else {
                                    str = '-';
                                }
                                return str;
                            }
                        }, {
                            data: 'replaceComponentCheck'

                        }, {
                            data: 'checkedPrice'

                        }, {
                            data: 'checkedDate'
                        }, {
                            data: 'checkedUserName'
                        }, {
                            data: 'checkedRemark'
                        }],
                        columnDefs: [{
                            targets: 14,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                    str += '<a id="btnModifyDeliveryModal" class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifyDeliveryModal" title="修改检修记录"><span class="glyphicon glyphicon-edit"></span></a>';
                                    str += '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popPartsModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                                } else {
                                    str += '';
                                }
                                return str;
                            }
                        }],
                        ordering: false,
                        paging: true,
                        pageLength: 5,
                        drawCallback: function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                            api.column(0).nodes().each(
                                function (cell, i) {
                                    cell.innerHTML = startIndex
                                        + i + 1;
                                });
                        }
                    });

                /**
                 * 获取报废库送修单据配件详情表
                 */
                var sheetDetailTable3 = dataTable(
                    'sheetDetailTable3',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'POST',
                            data: "",
                            url: config.basePath
                                + '/repairManage/repairOut/getAllSheetDetails'

                        },
                        columns: [{
                            data: null
                        }, {
                            data: 'partId'
                        }, {
                            data: 'partCode'
                        }, {
                            data: 'devicePartsName'
                        }, {
                            data: 'deviceTypeName'
                        }, {
                            data: 'deviceModelName'
                        }, {
                            data: 'assetAttributesName'
                        }, {
                            data: 'partState',
                            render: function (data) {
                                var str = "-";
                                if (data == 1) {
                                    str = '新购';
                                } else if (data == 2) {
                                    str = '送修';
                                } else if (data == 3) {
                                    str = '初始化在探测站';
                                } else if (data == 4) {
                                    str = '初始化在备品库';
                                } else if (data == 5) {
                                    str = '初始化在送修库';
                                }
                                return str;

                            }

                        }, {
                            data: 'warranty',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '否';
                                } else if (data == 1) {
                                    str = '是';
                                } else {
                                    str = '-';
                                }
                                return str;
                            }
                        }, {
                            data: 'faultInfo'
                        }, {
                            data: 'checkedDate'
                        }, {
                            data: 'checkedUserName'
                        }, {
                            data: 'scrapReason'

                        }],
                        columnDefs: [{
                            targets: 13,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                    str += '<a id="btnModifyScrapModal" class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifyScrapModal" title="修改检修记录"><span class="glyphicon glyphicon-edit"></span></a>';
                                    str += '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popPartsModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                                } else {
                                    str += '';
                                }
                                return str;
                            }
                        }],
                        ordering: false,
                        paging: true,
                        pageLength: 5,
                        drawCallback: function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                            api.column(0).nodes().each(
                                function (cell, i) {
                                    cell.innerHTML = startIndex
                                        + i + 1;
                                });
                        }
                    });

                // 定义一个变量用于存储选中复选框的值
                var sel_a2 = [];

                // 响应刷新按钮
                $('#btnRefresh').click(function () {
                    $("#part_id_sel").val("");
                    $("#part_name_sel").val("");
                    location.reload();
                });
                /**
                 * 修改单据
                 */
                $('#modifySheetModal').on('show.bs.modal', function (e) {
                    var tr = $(e.relatedTarget).parents('tr');
                    var data = sheetTable.row(tr).data();
                    $('#sheetIdModify').val(data.sheetId);
                    loadDate("sheetDateModify", new Date());
                    $('#remarkModify').val(data.sendRemark);
                });

                $("#btnModifySheetOk")
                    .on(
                        'click',
                        function (e) {
                            e.preventDefault();
                            var params = JSON.stringify({
                                sheetId: $('#sheetIdModify')
                                    .val(),
                                addDate: $('#sheetDateModify')
                                    .val(),
                                sendRemark: $('#remarkModify')
                                    .val(),

                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repairOut/modify',
                                    type: 'POST',
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
                                                    '<span style="color:green;text-align:center"><strong>单据信息修改成功！</strong></span>');
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
                            e.preventDefault();
                            if (sheetDetailTable.data().length < 1) {
                                alert("配件为空，请先添加配件！")
                                return;
                            }
                            var date = new Date();
                            var params = JSON.stringify({
                                sendVerifyFlag: 1,
                                sheetId: $(
                                    '#btnPopSheetVerifyOk')
                                    .val(),
                                sendOperatorId: user_id,
                                sendOperatorName: user_name
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repairOut/modify',
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
                                            sheetDetailTable.ajax
                                                .url(
                                                    config.basePath
                                                    + '/repairManage/repairOut/getAllSheetDetails?'
                                                    + sheetDetailParams)
                                                .load();
                                            $("#alertMsg")
                                                .html(
                                                    '<span style="color:green;text-align:center"><strong>单据审核中！</strong></span>');
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
                $('#popSheetModal').on(
                    'show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        $('#warningSheetText').text(
                            '确定要删除该单据（' + data.sheetId + '）？');
                        $('#deleteSheetId').val(data.sheetId);
                    });

                $('#btnPopSheetOk')
                    .on(
                        'click',
                        function (e) {
                            e.preventDefault();
                            var params = JSON.stringify({
                                sheetId: $('#deleteSheetId')
                                    .val()
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repairOut/delete',
                                    type: 'POST',
                                    data: params,
                                    contentType: 'application/json',
                                    dataType: 'json',
                                    success: function (
                                        result) {
                                        if (result.code != 0) {
                                            alert(result.msg);
                                        } else {
                                            $("#add_sheetDetail")
                                                .hide();
                                            sheetTable.ajax
                                                .reload();
                                            sheetDetailTable.ajax
                                                .reload();
                                            tblPartsInfo.ajax.reload()
                                            $("#alertMsg")
                                                .html(
                                                    '<span style="color:green;text-align:center"><strong>单据删除成功！</strong></span>');
                                            $("#infoAlert")
                                                .show();
                                            hideTimeout(
                                                "infoAlert",
                                                2000);
                                        }
                                    }
                                });

                        })
                $('#btnQueryparts').click(function () {
                    tblPartsInfo.ajax.reload()
                })
                /**
                 * 初始化配件表格
                 */
                var tblPartsInfo = dataTable(
                    'tblPartsInfo',
                    {
                        bAutoWidth: false,
                        ajax: {
                            url: config.basePath
                                + '/repairManage/repairOut/getAllPartsByStock',
                            type: 'get',
                            data: function (d) {
                                d.partCode = '%'
                                    + $("#query_part_code").val()
                                    + '%';
                                d.partId = '%'
                                    + $("#query_part_id").val()
                                    + '%';
                                d.sheetId = $("#query_sheet_id").val()
                                    ;
                                d.devicePartsName = '%'
                                    + $("#query_part_name")
                                        .val() + '%';
                                d.repaireState = repaireState;
                                if (!sheetTrData.supplierName) {
                                } else {
                                    d.supplierName = '%'
                                        + sheetTrData.supplierName
                                        + '%';
                                }

                            }
                        },
                        columns: [{
                            data: null
                        }, {
                            data: 'partId',
                            render: function (data) {

                                return '<input type="checkbox" class="chk" />'
                            }
                        }, {
                            data: 'partId'
                        }, {
                            data: 'partCode'
                        }, {
                            data: 'devicePartsName'
                        }, {
                            data: 'deviceTypeName'
                        }, {
                            data: 'deviceModelName'
                        }, {
                            data: 'purchasePrice'
                        }, {
                            data: 'supplierName'
                        }, {
                            data: 'assetAttributesName'
                        }, {
                            data: 'usedStationName'
                        }, {
                            data: 'faultInfo'
                        }, {
                            data: 'faultDate',
                            render: function (data) {
                                if (data > 0) {
                                    return formatDateBy(
                                        data,
                                        'yyyy-MM-dd');
                                } else {
                                    return '-';
                                }
                            }
                        }, {
                            data: 'warranty',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '否';
                                } else if (data == 1) {
                                    str = '是';
                                } else {
                                    str = '-';
                                }
                                return str;
                            }
                        }, {
                            data: 'replaceComponentCheck'
                        }, {
                            data: 'checkedPrice'
                        }, {
                            data: 'checkedDate'
                        }, {
                            data: 'checkedUserName'
                        }, {
                            data: 'repaireState',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '不合格';
                                } else if (data == 1) {
                                    str = '合格';
                                } else if (data == 2) {
                                    str = '报废';
                                }
                                return str;
                            }
                        }, {
                            data: 'scrapReason'
                        },
                            {
                                data: 'checkedRemark'

                            }],
                        ordering: false,
                        paging: true,
                        pageLength: 10,
                        serverSide: false,
                        drawCallback: function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                            api.column(0).nodes().each(
                                function (cell, i) {
                                    cell.innerHTML = startIndex
                                        + i + 1;
                                });
                        },
                    });

                /**
                 * 删除配件
                 */
                $('#popPartsModal').on(
                    'show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        $('#warningPartsText').text(
                            '确定要删除（单据为' + sheet_id2 + '，配件出厂编码为'
                            + data.partCode + '配件）？');
                        $('#deletePartId').val(data.partCode);

                    });

                $('#btnPopPartsDeleteOk')
                    .on(
                        'click',
                        function (e) {
                            e.preventDefault();
                            var params = JSON.stringify({
                                sheetId: sheet_id2,
                                partCode: $('#deletePartId')
                                    .val()
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repairOut/SheetDetailDeleteByCode',
                                    type: 'POST',
                                    data: params,
                                    contentType: 'application/json',
                                    dataType: 'json',
                                    success: function (
                                        result) {
                                        if (result.code != 0) {
                                            alert(result.msg);
                                        } else {
                                            tblPartsInfo.ajax.reload()
                                            sheetDetailTable.ajax
                                                .reload();
                                            $("#alertMsg")
                                                .html(
                                                    '<span style="color:green;text-align:center"><strong>配件删除成功！</strong></span>');
                                            $("#infoAlert")
                                                .show();
                                            hideTimeout(
                                                "infoAlert",
                                                2000);
                                        }
                                    }
                                });
                        })
                /**
                 * 批量增加sheetDetail
                 */
                $("#btnPopPartsOk").click(function () {
                    batchCreateSheetDetail(1);
                })

                /**
                 * 点击当前配件 获取配件
                 */
                function batchCreateSheetDetail(condition) {
                    var json = []

                    if (condition == 1) {
                        var list = $('.chk:checked')

                        list.each(function () {
                            $(this).closest("tr").addClass('success').siblings().removeClass('success');
                            var trData = tblPartsInfo.row($(this).closest("tr")).data();
                            var object = createSheetDetail(trData);
                            json.push(object)
                        })
                    } else {
                        var list = $('.chkb:checked')
                        list.each(function () {
                            $(this).closest("tr").addClass('success').siblings().removeClass('success');
                            var trData = tblPartsInfo.row($(this).closest("tr")).data();
                            var object = createSheetDetail(trData);


                            json.push(object)
                        })
                    }


                    var list = JSON.stringify(json)
                    $.ajax({
                        url: config.basePath + '/repairManage/repairOut/sheetDetailBatchCreate',
                        type: "post",
                        data: list,
                        contentType: 'application/json',
                        dataType: "json",
                        success: function (result) {
                            if (result.code != 0) {
                                alert(result.msg);
                            } else {
                                var json = $.param({
                                    sheetId: sheet_id2
                                });
                                tblPartsInfo.ajax.reload()
                                sheetDetailTable.ajax.url(config.basePath + '/repairManage/repairOut/getAllSheetDetails?' + json).load();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
                                $("#infoAlert").show();
                                hideTimeout("infoAlert", 2000);


                            }
                        }
                    });
                }

                /**
                 * 创建对象
                 */
                function createSheetDetail(trData) {
                    var o = new Object();
                    o.sheetId = sheet_id2;
                    o.partCode = trData.partCode;
                    o.partId = trData.partId;
                    o.faultInfo = trData.faultInfo;
                    o.usedStationName = trData.usedStationName;
                    o.warranty = trData.warranty;
                    o.partState = trData.partState;
                    o.assetAttributesId = trData.assetAttributesId;
                    o.remark = trData.remark;
                    o.faultDate = trData.faultDate;
                    return o;
                }

                //sheetDetail点击事件
                $('#sheetDetailTable1 tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前配件检修记录信息
                            if (sheetDetailTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                sheetDetailTableTrData = sheetDetailTable
                                    .row(tr).data();
                                $("#factory_devicePartsName")
                                    .val(
                                        sheetDetailTableTrData.devicePartsName)
                                $("#factory_deviceModelName")
                                    .val(
                                        sheetDetailTableTrData.deviceModelName)
                                $("#factory_partCode")
                                    .val(
                                        sheetDetailTableTrData.partCode)
                                $("#factory_partId")
                                    .val(
                                        sheetDetailTableTrData.partId)
                                $("#factory_faultInfo")
                                    .val(
                                        sheetDetailTableTrData.faultInfo)
                                $("#factory_warranty")
                                    .val(
                                        sheetDetailTableTrData.warranty)
                                $("#factory_checkedDate")
                                    .val(
                                        sheetDetailTableTrData.checkedDate)
                                $("#factory_checkedName")
                                    .val(
                                        sheetDetailTableTrData.checkedUserName)
                                $("#factory_checkedRemark")
                                    .val(
                                        sheetDetailTableTrData.checkedRemark)

                            }
                        })
                $('#sheetDetailTable2 tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前配件检修记录信息
                            if (sheetDetailTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                sheetDetailTableTrData = sheetDetailTable
                                    .row(tr).data();
                                $("#delivery_devicePartsName")
                                    .val(
                                        sheetDetailTableTrData.devicePartsName)
                                $("#delivery_deviceModelName")
                                    .val(
                                        sheetDetailTableTrData.deviceModelName)
                                $("#delivery_partCode")
                                    .val(
                                        sheetDetailTableTrData.partCode)
                                $("#delivery_partId")
                                    .val(
                                        sheetDetailTableTrData.partId)
                                $(
                                    "#delivery_replaceComponentCheck")
                                    .val(
                                        sheetDetailTableTrData.replaceComponentCheck)
                                $("#delivery_warranty")
                                    .val(
                                        sheetDetailTableTrData.warranty)
                                $("#delivery_checkedPrice")
                                    .val(
                                        sheetDetailTableTrData.checkedPrice)
                                $("#delivery_checkedDate")
                                    .val(
                                        sheetDetailTableTrData.checkedDate)
                                $("#delivery_checkedName")
                                    .val(
                                        sheetDetailTableTrData.checkedUserName)
                                $("#delivery_checkedRemark")
                                    .val(
                                        sheetDetailTableTrData.checkedRemark)

                            }
                        })
                $('#sheetDetailTable3 tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前配件检修记录信息
                            if (sheetDetailTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                sheetDetailTableTrData = sheetDetailTable
                                    .row(tr).data();
                                $("#scrap_devicePartsName")
                                    .val(
                                        sheetDetailTableTrData.devicePartsName)
                                $("#scrap_deviceModelName")
                                    .val(
                                        sheetDetailTableTrData.deviceModelName)
                                $("#scrap_partCode")
                                    .val(
                                        sheetDetailTableTrData.partCode)
                                $("#scrap_partId")
                                    .val(
                                        sheetDetailTableTrData.partId)
                                $("#scrap_faultInfo")
                                    .val(
                                        sheetDetailTableTrData.faultInfo)
                                $("#scrap_warranty")
                                    .val(
                                        sheetDetailTableTrData.warranty)
                                $("#scrap_checkedDate")
                                    .val(
                                        sheetDetailTableTrData.checkedDate)
                                $("#scrap_checkedName")
                                    .val(
                                        sheetDetailTableTrData.checkedUserName)
                                $("#scrap_scrapReason")
                                    .val(
                                        sheetDetailTableTrData.scrapReason)

                            }
                        })
                $('#factory_btnSheetDetailOk').click(function (e) {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partId: $("#factory_partId").val(),
                        partCode: $("#factory_partCode").val(),
                        faultInfo: $("#factory_faultInfo").val(),
                        warranty: $("#factory_warranty").val(),
                        checkedDate: $("#factory_checkedDate").val(),
                        checkedUserName: $("#factory_checkedName").val(),
                        checkedRemark: $("#factory_checkedRemark").val(),
                    })
                    modifySheetDetail(params)


                })
                $('#delivery_btnSheetDetailOk').click(function (e) {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partId: $("#delivery_partId").val(),
                        partCode: $("#delivery_partCode").val(),
                        replaceComponentCheck: $("#delivery_replaceComponentCheck").val(),
                        checkedDate: $("#delivery_checkedDate").val(),
                        checkedPrice: $("#delivery_checkedPrice").val(),
                        checkedUserName: $("#delivery_checkedName").val(),
                        checkedRemark: $("#delivery_checkedRemark").val(),
                    })
                    modifySheetDetail(params)


                })
                $('#scrap_btnSheetDetailOk').click(function (e) {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partId: $("#scrap_partId").val(),
                        partCode: $("#scrap_partCode").val(),
                        faultInfo: $("#scrap_faultInfo").val(),
                        scrapReason: $("#scrap_checkedRemark").val(),
                        repaireState: $("#scrap_repaireState").val(),
                        checkedDate: $("#delivery_checkedDate").val(),
                        checkedUserName: $("#delivery_checkedName").val(),
                        checkedRemark: $("#delivery_checkedRemark").val(),
                    })
                    modifySheetDetail(params)


                })
                $('#add_factory_btnSheetDetailOk').click(function (e) {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partId: $("#add_factory_partId").val(),
                        partCode: $("#add_factory_partCode").val(),
                        faultInfo: $("#add_factory_faultInfo").val(),
                        warranty: $("#add_factory_warranty").val(),
                        checkedDate: $("#add_factory_checkedDate").val(),
                        checkedUserName: $("#add_factory_checkedName").val(),
                        checkedRemark: $("#add_factory_checkedRemark").val(),
                    })
                    addSheetDetail(params)
                    tblPartsInfo.ajax.reload()
                    clearAddFactory()

                })
                $('#add_delivery_btnSheetDetailOk').click(function (e) {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partId: $("#add_delivery_partId").val(),
                        partCode: $("#add_delivery_partCode").val(),
                        replaceComponentCheck: $("#add_delivery_replaceComponentCheck").val(),
                        checkedPrice: $("#add_delivery_checkedPrice").val(),
                        checkedDate: $("#add_factory_checkedDate").val(),
                        checkedUserName: $("#add_delivery_checkedName").val(),
                        checkedRemark: $("#add_delivery_checkedRemark").val(),
                        repaireState: $("#add_delivery_repaireState").val(),
                    })
                    addSheetDetail(params)
                    tblPartsInfo.ajax.reload()
                    clearAddDelivery()

                })
                $('#add_scrap_btnSheetDetailOk').click(function (e) {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partId: $("#add_scrap_partId").val(),
                        partCode: $("#add_scrap_partCode").val(),
                        faultInfo: $("#add_scrap_faultInfo").val(),
                        scrapReason: $("#add_scrap_scrapReason").val(),
                        checkedDate: $("#add_scrap_checkedDate").val(),
                        checkedUserName: $("#add_scrap_checkedName").val(),
                    })
                    addSheetDetail(params)
                    tblPartsInfo.ajax.reload()
                    clearAddScrap()

                })

                function addSheetDetail(params) {
                    $.ajax({
                        url: config.basePath + '/repairManage/repairOut/SheetDetailAdd',
                        type: 'POST',
                        data: params,
                        contentType: 'application/json',
                        dataType: "json",
                        success: function (result) {
                            if (result.code != 0) {
                                alert(result.msg);
                            } else {
                                tblPartsInfo.ajax.reload()
                                sheetDetailTable.ajax.url(config.basePath + '/repairManage/repairOut/getAllSheetDetails?' + sheetDetailParams).load();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息新增成功！</strong></span>');
                                $("#infoAlert").show();
                                hideTimeout("infoAlert", 2000);
                            }
                        }
                    });

                }

                function modifySheetDetail(params) {
                    $.ajax({
                        url: config.basePath + '/repairManage/repairOut/SheetDetailModify',
                        type: 'POST',
                        data: params,
                        contentType: 'application/json',
                        dataType: "json",
                        success: function (result) {
                            if (result.code != 0) {
                                alert(result.msg);
                            } else {
                                tblPartsInfo.ajax.reload()
                                sheetDetailTable.ajax.url(config.basePath + '/repairManage/repairOut/getAllSheetDetails?' + sheetDetailParams).load();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息修改成功！</strong></span>');
                                $("#infoAlert").show();
                                hideTimeout("infoAlert", 2000);
                            }
                        }
                    });

                }

                $("#add_factory_devicePartsName").on('click', function (e) {
                    $("#addSheetDetailModal").modal('show');
                })
                $("#add_delivery_devicePartsName").on('click', function (e) {
                    $("#addSheetDetailModal").modal('show');
                })
                $("#add_scrap_devicePartsName").on('click', function (e) {
                    $("#addSheetDetailModal").modal('show');
                })
                $("#add_sheetDetail").click(function () {
                    // if (objectStoreHouseName == storeHouseName[0]) {
                    //     clearAddFactory()
                    //     $("#addFactoryModal").modal({backdrop: true});
                    // } else if (objectStoreHouseName == storeHouseName[1]) {
                    //     clearAddDelivery()
                    //     $("#addDeliveryModal").modal({backdrop: true});
                    // } else {
                    //     clearAddScrap()
                    //     $("#addScrapModal").modal({backdrop: true});
                    // }
                    $("#addSheetDetailModal").modal({backdrop: true});
                })
                $("#add_sheetDetail").click(function () {

                })


                /**
                 * 清楚返厂出库新增模态框数据
                 */
                function clearAddFactory() {
                    $("#add_factory_partId").val("");
                    $("#add_factory_partCode").val("");
                    $("#add_factory_devicePartsName").val("");
                    $("#add_factory_deviceModelName").val("");
                    $("#add_factory_prepareCheck").val("");
                    $("#add_factory_machineCheck").val("");
                    $("#add_factory_replaceComponentCheck").val("");
                    $("#add_factory_copyMachineStartTime").val("");
                    $("#add_factory_copyMachineEndTime").val("");
                    $("#add_factory_copyMachineCheck").val("");
                    $("#add_factory_repaireState").val("");
                }

                /**
                 *清除配送出库新增模态框
                 */
                function clearAddDelivery() {
                    $("#add_delivery_partId").val("");
                    $("#add_delivery_partCode").val("");
                    $("#add_delivery_devicePartsName").val("");
                    $("#add_delivery_deviceModelName").val("");
                    $("#add_delivery_prepareCheck").val("");
                    $("#add_delivery_machineCheck").val("");
                    $("#add_delivery_replaceComponentCheck").val("");
                    $("#add_delivery_copyMachineStartTime").val("");
                    $("#add_delivery_copyMachineEndTime").val("");
                    $("#add_delivery_copyMachineCheck").val("");
                    $("#add_delivery_repaireState").val("");
                }

                function clearAddScrap() {
                    $("#add_scrap_partId").val("");
                    $("#add_scrap_partCode").val("");
                    $("#add_scrap_devicePartsName").val("");
                    $("#add_scrap_deviceModelName").val("");
                    $("#add_scrap_prepareCheck").val("");
                    $("#add_scrap_machineCheck").val("");
                    $("#add_scrap_replaceComponentCheck").val("");
                    $("#add_scrap_copyMachineStartTime").val("");
                    $("#add_scrap_copyMachineEndTime").val("");
                    $("#add_scrap_copyMachineCheck").val("");
                    $("#add_scrap_repaireState").val("");
                }
                /** 导出配件详情信息 */
                $("#sheetTable tbody").on('click', '#exportExcel',
                    function () {
                        var tr = $(this).closest('tr');
                        sheetTrData = sheetTable.row(tr).data();
                        sheet_id2 = sheetTrData.sheetId;
                        var params = $.param({
                            sheetId: sheet_id2,
                            fileName:"所内送修出库单"
                        });
                        window.location.href = config.basePath + '/exportExcel/export?' + params;
                    });
                /** *************** **/
                sheetTable.on('draw.dt', function () {
                    //给第一列编号
                    sheetTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });
                sheetDetailTable1.on('draw.dt', function () {
                    //给第一列编号
                    sheetDetailTable1.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });
                sheetDetailTable2.on('draw.dt', function () {
                    //给第一列编号
                    sheetDetailTable2.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });
                sheetDetailTable3.on('draw.dt', function () {
                    //给第一列编号
                    sheetDetailTable3.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });

                tblPartsInfo.on('draw.dt', function () {
                    //给第一列编号
                    tblPartsInfo.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                })

                function hideTimeout(id, ms) {
                    var time = setTimeout(function () {
                        $("#" + id).hide();
                    }, ms)
                }

            })

    });