/**
 * 采购单审核
 */
require(['../config'], function (config) {

    require(['datetimepicker'], function () {
        var date = new Date();
        date.setDate("1")
        date.setMonth(date.getMonth() - 1)
        initDatetimepicker("queryTime", date);
        initDatetimepicker("queryTime2", new Date());

        /**
         * 初始化时间框
         */
        function initDatetimepicker(id, date) {
            $.datetimepicker.setLocale('ch');
            $('#' + id).datetimepicker({
                value: date,
                format: 'Y-m-d',
                timepicker: false, //关闭时间选项
                todayButton: true //关闭选择今天按钮
            });
        }
    });

    require(['jquery', 'bootstrap', 'common/dt', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin'], function ($, bootstrap, dataTable) {


        var newDate = new Date();
        var verifyFlag = '';
        var sheet_id = '';
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var sheetTrData = '';//保留点击的单号信息

        /**
         * 查询
         */
        $('#btn-search').click(function (e) {
            sheetTable.ajax.reload();
            sheetDetailTable.column(0).search('');
            sheetDetailTable.clear().draw();
            sheet_id = '';
        });

        /**
         * 重置
         */
        $('#btn-reset').click(function (e) {
            $('#sheet_id').val('');
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
                url: config.basePath + '/entryAndOut/preparePurchase/getAllSheets',
                type: "get",
                data: function (d) {
                    d.sheetId = "%" + $('#sheet_id').val() + "%";
                    d.sendVerifyFlag = $('#verify_flag').val();
                    d.queryTime = $("#queryTime").val();
                    d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                }
            },
            columns: [
                {data: null},
                {data: 'sheetId'},
                {data: 'trackingNumber'},
                {data: 'supplierName'},
                {data: 'objectStorehouseName'},
                {
                    data: 'addDate',
                    render: function (data) {
                        if (data > 0) {
                            return formatDateBy(data, 'yyyy-MM-dd');
                        } else {
                            return '-';
                        }
                    }
                },
                {data: 'sendRemark'},
                {data: 'sendOperatorName'},
                {data: 'sendVerifyName'},
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
                            return formatDateBy(data, 'yyyy-MM-dd');
                        } else {
                            return '-';
                        }
                    }
                }
            ],
            columnDefs: [
                {
                    targets: 11,
                    data: function (row) {
                        var str = '';
                        if (row.sendVerifyFlag == 1) {
                            str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#SheetVerifyOkModal" title="审核通过" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#SheetVerifyBackModal" title="审核不通过"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                        } else {
                            str += '';
                        }
                        str += '<button id="exportExcel" type="button" class="btn btn-success btn-xs" title="导出"><span class="glyphicon glyphicon-download-alt"></span></button>';
                        return str;
                    }
                }
            ],
            ordering: false,
            paging: true,
            pageLength: 5,
            serverSide: false,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function (cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });
        /**
         * 初始化配件详情
         */
        var sheetDetailTable = dataTable('sheetDetailTable', {
            bAutoWidth: false,
            ajax: {
                type: 'post',
                data: "",
                url: config.basePath + '/entryAndOut/preparePurchase/getAllSheetDetails'
            },
            columns: [
                {data: null},
                {data: 'partId'},
                {data: 'devicePartsName'},
                {data: 'deviceModelName'},
                {data: 'partCode'},
                {data: 'deviceTypeName'},
                {data: 'unitPrice'},
                {data: 'supplierName'},
                {
                    data: 'createTime',
                    render: function (data) {
                        if (data > 0) {
                            return formatDateBy(data, 'yyyy-MM-dd');
                        } else {
                            return '-';
                        }
                    }
                },
                {
                    data: 'updateTime',
                    render: function (data) {
                        if (data > 0) {
                            return formatDateBy(data, 'yyyy-MM-dd');
                        } else {
                            return '-';
                        }
                    }
                },
                {data: 'remark'}
            ],
            ordering: true,
            order: [8, 'desc'],
            pageLength: 5,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                api.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });

        $('#sheetTable tbody').on('click', 'tr', function (e) {//获取当前单号
            if (sheetTable.data().any()) {
                $(this).addClass('selected').siblings().removeClass('selected');
                var tr = $(this).closest('tr');
                sheetTrData = sheetTable.row(tr).data();
                sheet_id = sheetTrData.sheetId;
                sendVerifyFlag = sheetTrData.sendVerifyFlag;
                var params = $.param({
                    sheetId: sheet_id
                });
                sheetDetailTable.column(0).search('');
                sheetDetailTable.ajax.url(config.basePath + '/entryAndOut/preparePurchase/getAllSheetDetails?' + params).load();
            }
        });

        /**
         * 入库单据通过审核提示
         */
        $('#SheetVerifyOkModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#warningSheetOkText').text('确定入库单号为' + data.sheetId + '的单据通过审核（通过后将无法操作该单据）？');
            $('#SheetVerifyOkId').val(data.sheetId);
        });

        /**
         * 单据审核通过
         */
        $("#btnPopSheetVerifyOk").on('click', function (e) {
            e.preventDefault();
            var date = new Date();
            var params = JSON.stringify({
                sendVerifyId: user_id,
                sendVerifyName: user_name,
                sheetId: $('#SheetVerifyOkId').val(),
                sendVerifyFlag: 2,
                sendVerifyDate: date
            });
            $.ajax({
                url: config.basePath + '/entryAndOut/preparePurchase/modify',
                type: "post",
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        sheetTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据审核通过！</strong></span>');
                        $("#infoAlert").show();
                        hideTimeout("infoAlert", 2000);
                    }
                }
            });
            $('#sheetDetailTable').DataTable().ajax.reload();
        });

        /**
         * 入库单据未通过审核提示
         */
        $('#SheetVerifyBackModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#warningSheetText').text('确定回退入库单号为' + data.sheetId + '的单据？');
            $('#SheetVerifyId').val(data.sheetId);
        });

        /**
         * 单据审核未通过
         */
        $("#btnPopSheetVerify").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                sendVerifyId: user_id,
                sendVerifyName: user_name,
                sheetId: sheet_id,
                sendVerifyFlag: 3,
                sendVerifyDate: new Date()
            });
            $.ajax({
                url: config.basePath + '/entryAndOut/preparePurchase/modify',
                type: "post",
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        sheetTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据审核未通过！</strong></span>');
                        $("#infoAlert").show();
                        hideTimeout("infoAlert", 2000);
                    }
                }
            });
            $('#sheetDetailTable').DataTable().ajax.reload();
        });
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
         * 时间加载
         */
        function loadDate(id, date) {
            $.datetimepicker.setLocale('ch');
            $('#' + id).datetimepicker({
                value: date,
                format: 'Y-m-d',
                timepicker: false, // 关闭时间选项
                todayButton: true// 关闭选择今天按钮
            });
        }

        /**
         * 定时隐藏alert框
         */
        function hideTimeout(id, ms) {
            var time = setTimeout(function () {
                $("#" + id).hide();
            }, ms)
        }


    });
});