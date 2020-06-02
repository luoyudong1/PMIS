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
                // 入库方式（1：采购入库、2：生产入库、3：调拨入库、0:手动添加）
                const entryTypes = ['手动添加', '采购入库', '生产入库', '调拨入库'];
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
                var sourceStoreHouseName = '';// 保留点击的源仓库
                var sourceStoreHouseId = '';// 保留点击的源仓库
                var sheetTrData = '';// 保留点击的单号信息
                var sheetDetailTable;// 保留点击的单号信息
                var sheetDetailParams = "";
                // 初始化时间查询框
                var date = new Date();
                date.setDate("1");
                date.setMonth(date.getMonth() - 1)
                CMethod.initDatetimepicker('queryTimeBegin', date);
                CMethod.initDatetimepicker('queryTimeEnd', new Date());
                CMethod.initDatetimepicker('queryTime', date);
                CMethod.initDatetimepicker('queryTime2', new Date());
                CMethod.initDatetimepicker("faultInfoDateAdd", new Date());
                CMethod.initDatetimepicker("faultInfoDateModify", new Date());
                // 初始化仓库查询项
                $
                    .ajax({
                        async: false,
                        url: config.basePath
                            + "/repairManage/repair/getStorehouse",
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
                                $("#sourceStoreHouse")
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
                                + '/repairManage/repair/getAllSheets',
                            type: "get",
                            data: function (d) {
                                d.sheetType = 2;
                                d.sheetId = "%"
                                    + $('#sheet_id').val()
                                    + "%";
                                d.sourceStoreHouseId = $(
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
                                data: 'objectStorehouseName'
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
                        ordering: false,
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
                 * 添加单据
                 */
                $('#addSheetModal')
                    .on(
                        'show.bs.modal',
                        function () {
                            // 单据ID为单据类型+部门id+序列号
                            if (deptId < 10) {
                                var sheetId = "04-0" + deptId
                                    + "-";
                            } else {
                                var sheetId = "04-" + deptId
                                    + "-";
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

                /**
                 * 提交新增单据事件
                 *
                 */
                $("#btnAddSheetOk")
                    .on(
                        'click',
                        function (e) {
                            e.preventDefault();
                            var params = JSON.stringify({
                                sheetId: $('#sheetIdAdd')
                                    .val(),
                                sourceStoreHouseId: $(
                                    '#sourceStoreHouse')
                                    .val(),
                                addDate: $('#sheetDateAdd')
                                    .val(),
                                sendOperatorId: user_id,
                                sendOperatorName: $(
                                    '#sendOperatorNameAdd')
                                    .val(),
                                sendRemark: $('#sendRemarkAdd')
                                    .val(),
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repair/createSheetInfo',
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
                                                    '<span style="color:green;text-align:center"><strong>单据信息添加成功！</strong></span>');
                                            $("#infoAlert")
                                                .show();
                                            hideTimeout(
                                                "infoAlert",
                                                2000);
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
                                sheetTrData = sheetTable
                                    .row(tr).data();
                                sheet_id2 = sheetTrData.sheetId;
                                sendVerifyFlag = sheetTrData.sendVerifyFlag;
                                sourceStoreHouseName = sheetTrData.sourceStorehouseName;
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

                                if (sourceStoreHouseName == '机辆检测所新购库') {

                                    sheetDetailTable = sheetDetailTable1;
                                    $('#sheetDetailTableDiv1')
                                        .show();
                                    $('#sheetDetailTableDiv2')
                                        .hide();
                                    $('#sheetDetailTableDiv3')
                                        .hide();

                                } else if (sourceStoreHouseName == '机辆检测所送修库') {
                                    // loadSheetDetail2(params).column(0).search('');
                                    sheetDetailTable = sheetDetailTable2;

                                    $('#sheetDetailTableDiv2')
                                        .show();
                                    $('#sheetDetailTableDiv1')
                                        .hide();
                                    $('#sheetDetailTableDiv3')
                                        .hide();

                                } else if (sourceStoreHouseName == '机辆检测所修返库') {
                                    // loadSheetDetail3(params).column(0).search('');

                                    sheetDetailTable = sheetDetailTable3;
                                    $('#sheetDetailTableDiv3')
                                        .show();
                                    $('#sheetDetailTableDiv1')
                                        .hide();
                                    $('#sheetDetailTableDiv2')
                                        .hide();

                                }
                                sheetDetailTable.ajax
                                    .url(
                                        config.basePath
                                        + '/repairManage/repair/getAllSheetDetails?'
                                        + sheetDetailParams)
                                    .load();

                            }
                        });

                /**
                 * 获取新购库送修单据配件详情表
                 */
                var sheetDetailTable1 = dataTable(
                    'sheetDetailTable1',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'POST',
                            data: "",
                            url: config.basePath
                                + '/repairManage/repair/getAllSheetDetails'

                        },
                        columns: [{
                            data: null,
                            fnDrawCallback: function () {
                                var api = this.api();
                                var startIndex = api.context[0]._iDisplayStart;//获取本页开始的条数
                                api.column(0).nodes().each(function (cell, i) {
                                    cell.innerHTML = startIndex + i + 1;
                                })
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
                            data: 'assetAttributesName'
                        }, {
                            data: 'remark'
                        }],
                        columnDefs: [{
                            targets: 8,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {

                                    str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetail1Modal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                        + '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popPartsModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
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
                 * 获取送修库送修单据配件详情表
                 */

                var sheetDetailTable2 = dataTable(
                    'sheetDetailTable2',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'POST',
                            data: "",
                            url: config.basePath
                                + '/repairManage/repair/getAllSheetDetails'

                        },
                        columns: [
                            {
                                data: null
                            },
                            {
                                data: 'partId'
                            },
                            {
                                data: 'partCode'
                            },
                            {
                                data: 'devicePartsName'
                            },
                            {
                                data: 'deviceTypeName'
                            },
                            {
                                data: 'deviceModelName'
                            },
                            {
                                data: 'assetAttributesName'
                            },
                            {
                                data: 'usedStationName'
                            },
                            {
                                data: 'faultDate'
                            }, {
                                data: 'faultInfo'
                            }, {
                                data: 'remark'
                            }],
                        columnDefs: [{
                            targets: 11,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                    str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetail2Modal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                        + '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popPartsModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
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
                 * 获取修返库送修单据配件详情表
                 */
                var sheetDetailTable3 = dataTable(
                    'sheetDetailTable3',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'POST',
                            data: "",
                            url: config.basePath
                                + '/repairManage/repair/getAllSheetDetails'

                        },
                        columns: [
                            {
                                data: null
                            },
                            {
                                data: 'partId'
                            },
                            {
                                data: 'partCode'
                            },
                            {
                                data: 'devicePartsName'
                            },
                            {
                                data: 'deviceTypeName'
                            },
                            {
                                data: 'deviceModelName'
                            },
                            {
                                data: 'assetAttributesName'
                            },
                            {
                                data: 'factoryReplaceComponent'
                            },
                            {
                                data: 'factoryReplaceCount'
                            },
                            {
                                data: 'factoryRepairedPrice'
                            }

                            ,
                            {
                                data: 'factoryRepairedDate'
                            }, {
                                data: 'repaireState',
                                render: function (data) {
                                    if (data == 0) {
                                        return "否";
                                    } else if (data == 1) {
                                        return "是";
                                    } else {
                                        return "-";
                                    }
                                }

                            }],
                        columnDefs: [{
                            targets: 12,
                            data: function (row) {
                                var str = '';
                                if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                    str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetail3Modal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                        + '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popPartsModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
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

                $('#addRepairPartsModal')
                    .on(
                        'show.bs.modal',
                        function (e) {
                            $('#addModal')
                                .text(
                                    $(
                                        "#storehouse_id_sel option:selected")
                                        .text());
                        });
                $('#btnPopPartsOk')
                    .on(
                        'click',
                        function (e) {
                            batchCreateSheetDetail(1);

                        });

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
                            var trData = tblParts.row($(this).closest("tr")).data();
                            var object = createSheetDetail(trData);


                            json.push(object)
                        })
                    }


                    var list = JSON.stringify(json)
                    $.ajax({
                        url: config.basePath + '/repairManage/repair/sheetDetailBatchCreate',
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
                                sheetDetailTable.ajax.url(config.basePath + '/repairManage/repair/getAllSheetDetails?' + json).load();
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
                    o.factoryRepairedPrice = trData.factoryRepairedPrice;
                    o.factoryRepairedDate = trData.factoryRepairedDate;
                    o.factoryReplaceComponent = trData.factoryReplaceComponent;
                    o.repaireState = trData.repaireState;
                    return o;
                }

                // 审核
                $('#btnVerify').on('click', function () {
                    if (sel_a2.length > 0) {
                        $('#popVerifyModal').modal('show');
                    } else {
                        alert("未选择任何配件");
                        $('#popVerifyModal').modal('hide');
                    }
                })
                $('#btnPopVerifyOk')
                    .on(
                        'click',
                        function (e) {
                            clearTimeout(timer);
                            e.preventDefault();
                            var params = JSON.stringify({
                                sheetId: sel_a2,
                                sendVerifyFlag: 1
                            });
                            $
                                .ajax({
                                    url: config.basePath
                                        + '/repairManage/repair/modifyVerify',
                                    type: 'POST',
                                    traditional: true,
                                    contentType: "application/json",
                                    data: params,
                                    dataType: "json",
                                    success: function (
                                        result) {
                                        if (result.code != 0) {
                                            alert(result.msg);
                                        } else {
                                            $('#add_sheetDetail').hide()
                                            $("#alertMsg")
                                                .html(
                                                    '<span style="color:green;text-align:center"><strong>提交成功！</strong></span>');
                                            $("#infoAlert")
                                                .show();
                                            CMethod
                                                .hideTimeout(
                                                    "alertMsg",
                                                    "infoAlert",
                                                    2000);
                                        }
                                    }
                                });
                        });

                // 添加信息
                $('#addRepairPartsModal').on('show.bs.modal',
                    function (e) {
                        tblPartsInfo.ajax.reload();
                    });
                // 跟据条件查询
                $('#btnQueryparts').on('click', function (e) {
                    tblPartsInfo.ajax.reload();
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
                                        + '/repairManage/repair/modify',
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
                                        + '/repairManage/repair/modify',
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
                                                    + '/repairManage/repair/getAllSheetDetails?'
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
                                sheetId: $('#deleteSheetId').val()
                            });
                            $.ajax({
                                url: config.basePath
                                    + '/repairManage/repair/delete',
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

                /**
                 * 初始化配件表格
                 */
                var tblPartsInfo = dataTable(
                    'tblPartsInfo',
                    {
                        bAutoWidth: false,
                        ajax: {
                            url: config.basePath
                                + '/repairManage/repair/getAllPartsByStock',
                            type: 'get',
                            data: function (d) {
                                d.storeHouseId = $('#sourceStoreHouse :contains("' + sourceStoreHouseName + '")').val()
                                d.partCode = '%'
                                    + $("#query_part_code").val()
                                    + '%';
                                d.sheetId=$("#query_sheet_id").val()
                                d.partId = '%'
                                    + $("#query_part_id").val()
                                    + '%';
                                d.devicePartsName = '%'
                                    + $("#query_part_name")
                                        .val() + '%';
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
                            data: 'sheetId'
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
                                if (data == 1) {
                                    return "是"
                                } else if (data == 0) {
                                    return "否";
                                } else {
                                    return "-";
                                }
                            }
                        }, {
                            data: 'factoryReplaceComponent'
                        }, {
                            data: 'factoryRepairedPrice'
                        }, {
                            data: 'factoryRepairedDate'
                        }, {
                            data: 'repaireState',
                            render: function (data) {
                                if (data == 0) {
                                    return "否";
                                } else if (data == 1) {
                                    return "是";
                                } else {
                                    return "-";
                                }
                            }
                        }, {
                            data: 'remark'
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
                 * 配件点击事件
                 */
                $('#tblPartsInfo tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前单号
                            if (tblPartsInfo.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                partsTrData = tblPartsInfo.row(
                                    tr).data();
                                partCode = partsTrData.partCode;
                                partId = partsTrData.partId
                                var params = JSON.stringify({
                                    sheetId: sheet_id2,
                                    sourceSheetId: partsTrData.sheetId,
                                    partCode: partCode,
                                    partId: partId,
                                    storeHouseId: $('#sourceStoreHouse :contains("' + sourceStoreHouseName + '")').val()
                                });

                            }
                            ;
                        });
                /**
                 * 点击送修库送修配件修改事件
                 */
                $("#modifySheetDetail2Modal").on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        $("#part_nameModify").val(data.devicePartsName);
                        $("#deviceModelNameModify").val(data.deviceModelName);
                        $("#partCodeModify").val(data.partCode);
                        $("#warrantyModify").val(data.warranty);
                        $("#part_idModify").val(data.partId);
                        $("#usedStationModify").val(data.usedStationName);
                        $("#faultInfoDateModify").val(data.faultDate);
                        $("#faultInfoModify").val(data.faultInfo);
                        $("#sheetDetailRemarkModify").val(data.remark);
                    });
                $("#btnModifySheetDetail2Ok").click(function () {
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partCode: $("#partCodeModify").val(),
                        faultDate: $('#faultInfoDateModify').val(),
                        faultInfo: $('#faultInfoModify').val(),
                        usedStationName: $('#usedStationModify').val(),
                        warranty: $('#warrantyModify').val(),
                        remark: $('#sheetDetailRemarkModify').val()
                    });
                    $.ajax({
                        url: config.basePath + '/repairManage/repair/SheetDetailModify',
                        type: "post",
                        data: params,
                        contentType: 'application/json',
                        dataType: "json",
                        success: function (result) {
                            if (result.code != 0) {
                                 alert(result.msg);
                            } else {
                                var json = $.param({
                                    sheetId: sheet_id2
                                });
                                sheetDetailTable.ajax.url(config.basePath + '/repairManage/repair/getAllSheetDetails?' + json).load();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
                                $("#infoAlert").show();
                                hideTimeout("infoAlert", 2000);


                            }
                        }
                    });
                })
                /**
                 * 删除配件
                 */
                $('#popPartsModal').on(
                    'show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        $('#warningPartsText').text(
                            '确定要删除（单据为' + sheet_id2 + '，编号为'
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
                                partCode: $('#deletePartId').val()
                            });
                            $.ajax({
                                url: config.basePath
                                    + '/repairManage/repair/SheetDetailDeleteByCode',
                                type: 'POST',
                                data: params,
                                contentType: 'application/json',
                                dataType: 'json',
                                success: function (
                                    result) {
                                    if (result.code != 0) {
                                        // alert(result.msg);
                                    } else {
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
                $("#add_sheetDetail").click(function () {

                    $("#addSheetDetailModal").show();
                    tblPartsInfo.ajax.reload();
                })
                /** 导出配件详情信息 */
                $("#sheetTable tbody").on('click', '#exportExcel',
                    function () {
                        var tr = $(this).closest('tr');
                        sheetTrData = sheetTable.row(tr).data();
                        sheet_id2 = sheetTrData.sheetId;
                        var params = $.param({
                            sheetId: sheet_id2,
                            fileName:"所内送修入库单"
                        });
                        window.location.href = config.basePath + '/exportExcel/export?' + params;
                    });
                /** *************** **/
                function hideTimeout(id, ms) {
                    var time = setTimeout(function () {
                        $("#" + id).hide();
                    }, ms)
                }

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
            })
    })