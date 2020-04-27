/**
 * 所内送修出库
 */
require(['../config'], function (config) {
    require(['jquery', 'popper', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/dateFormat', 'metisMenu', 'slimscroll', 'pace', 'inspinia'], function ($, Popper, Bootstrap, dataTable, CMethod) {
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
        // 初始化时间查询框
        var date = new Date();
        date.setDate("1");
        date.setMonth(date.getMonth() - 1)
        CMethod.initDatetimepicker('queryTimeBegin', date);
        CMethod.initDatetimepicker('queryTimeEnd', new Date());
        CMethod.initDatetimepicker('queryTime', date);
        CMethod.initDatetimepicker('queryTime2', new Date());
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
                        if (row.sendVerifyFlag == 1) {
                            str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="审核通过" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="回退单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
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

                        if (objectStoreHouseName == '机辆检测所返厂库') {

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

                            $('#sheetDetailTableDiv2')
                                .show();
                            $('#sheetDetailTableDiv1')
                                .hide();
                            $('#sheetDetailTableDiv3')
                                .hide();

                        } else if (objectStoreHouseName == '机辆检测所报废库') {
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
                        if (data == 0) {
                            return "否";
                        } else if (data == 1) {
                            return "是";
                        } else {
                            return "-";
                        }
                    }
                }, {
                    data: 'checkedDate'
                }, {
                    data: 'checkedUserName'
                }, {
                    data: 'checkedRemark'
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
         * 获取配送单据配件详情表
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
                            if (data == 0) {
                                return "否";
                            } else if (data == 1) {
                                return "是";
                            } else {
                                return "-";
                            }
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
         * 获取报废库出库单据配件详情表
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
                    },
                    {
                        data: 'warranty',
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
                        data: 'faultInfo'
                    }, {
                        data: 'checkedDate'
                    }, {
                        data: 'checkedUserName'
                    },
                    {data: 'scrapReason'}
                ],
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
         * 单据审核
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
                        '确定审核通过单据ID为:' + data.sheetId
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
                    var date = new Date();
                    var params = JSON.stringify({
                        sendVerifyFlag: 2,
                        sheetId: $(
                            '#btnPopSheetVerifyOk')
                            .val(),
                        sendVerifyId: user_id,
                        sendVerifyName: user_name,
                        sendVerifyDate: new Date()
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
                                            '<span style="color:green;text-align:center"><strong>单据审核通过！</strong></span>');
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
         * 回退单据
         */
        $('#popSheetModal').on(
            'show.bs.modal',
            function (e) {
                var tr = $(e.relatedTarget).parents('tr');
                var data = sheetTable.row(tr).data();
                $('#warningSheetText').text(
                    '确定要回退该单据（' + data.sheetId + '）？');
                $('#deleteSheetId').val(data.sheetId);
            });

        $('#btnPopSheetOk')
            .on(
                'click',
                function (e) {
                    e.preventDefault();
                    var params = JSON.stringify({
                        sendVerifyId: user_id,
                        sendVerifyName: user_name,
                        sheetId: sheet_id2,
                        sendVerifyFlag: 3,
                        sendVerifyDate: new Date()
                    });
                    $
                        .ajax({
                            url: config.basePath
                                + '/repairManage/repairOut/modify',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (
                                result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax
                                        .reload();
                                    sheetDetailTable.ajax
                                        .reload();
                                    $("#alertMsg")
                                        .html(
                                            '<span style="color:green;text-align:center"><strong>单据回退成功！</strong></span>');
                                    $("#infoAlert")
                                        .show();
                                    hideTimeout(
                                        "infoAlert",
                                        2000);
                                }
                            }
                        });

                })
        /** 导出配件详情信息 */
        $("#sheetTable tbody").on('click', '#exportExcel',
            function () {
                var tr = $(this).closest('tr');
                sheetTrData = sheetTable.row(tr).data();
                sheet_id2 = sheetTrData.sheetId;
                var params = $.param({
                    sheetId: sheet_id2,
                    fileName: "所内送修出库单"
                });
                window.location.href = config.basePath + '/exportExcel/export?' + params;
            });

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


        function hideTimeout(id, ms) {
            var time = setTimeout(function () {
                $("#" + id).hide();
            }, ms)
        }

    })

});