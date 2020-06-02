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
                var part_code = '';
                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                var deptId = window.parent.user.deptId; // 登录人部门id
                var sourceStoreHouseName = '';// 保留点击的源仓库
                var sheetTrData = '';// 保留点击的单号信息
                var sheetDetailTable;// 保留点击的单号信息
                var sheetDetailParams = "";
                // 初始化时间查询框
                var date = new Date();
                date.setDate("1");
                date.setMonth(date.getMonth() - 1)
                CMethod.initDatetimepicker('queryTime', date);
                CMethod.initDatetimepicker('queryTime2', new Date());

                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                    sheetDetailTable.column(0).search('');
                    sheetDetailTable.clear().draw();
                    sheetId = '';
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
                            },{
                                data: 'sendVerifyFlag',
                                render: function (data) {
                                    var str = "-";
                                    if (data == 0) {
                                        str = '<span style="color:red;font-weight:bold;">新建</span>';
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
                           ],
                        columnDefs: [{
                            targets: 9,
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
                            var startIndex = api.context[0]._iDisplayStart;// 获取到本页开始的条数
                            api.column(0).nodes().each(
                                function (cell, i) {
                                    cell.innerHTML = startIndex
                                        + i + 1;
                                });
                        },
                    });
                /**
                 * 获取检修记录单
                 */
                var sheetDetailTable = dataTable(
                    'sheetDetailTable',
                    {
                        bAutoWidth: false,
                        ajax: {
                            type: 'post',
                            data: "",
                            url: config.basePath
                                +
                                '/repairManage/repair/getAllSheetDetails'

                        },
                        columns: [{
                            data: null
                        }, {
                            data: 'devicePartsName'
                        },{
                            data: 'partId'
                        },{
                            data: 'partCode'
                        }, {
                            data: 'deviceTypeName'
                        },{
                            data: 'deviceModelName'
                        },{
                            data: 'supplierName'
                        }, {
                            data: 'assetAttributesName'
                        }, {
                            data: 'partState',
                            render: function (data) {
                                if (data == 1) {
                                    return "新购"
                                } else if (data == 2) {
                                    return "送修";
                                } else if (data == 3) {
                                    return "初始化在探测站";
                                } else if (data == 4) {
                                    return "初始化在备品库";
                                } else if (data == 5) {
                                    return "初始化在送修库";
                                } else {
                                    return "-";
                                }
                            }
                        }, {
                            data: 'faultInfo'
                        }, {
                            data: 'prepareCheck',
                            render: function (data) {
                                if (data != null && data.length > 10) {
                                    return data.substring(0, 9) + "...."
                                } else {
                                    return data
                                }
                            }
                        }, {
                            data: 'machineCheck',
                            render: function (data) {
                                if (data != null && data.length > 10) {
                                    return data.substring(0, 9) + "...."
                                } else {
                                    return data
                                }
                            }
                        }, {
                            data: 'replaceComponentCheck',
                            render: function (data) {
                                if (data != null && data.length > 10) {
                                    return data.substring(0, 9) + "...."
                                } else {
                                    return data
                                }
                            }
                        }, {
                            data: 'copyMachineStartTime'
                        }, {
                            data: 'copyMachineEndTime'
                        }, {
                            data: 'copyMachineCheck',
                            render: function (data) {
                                if (data != null && data.length > 10) {
                                    return data.substring(0, 9) + "...."
                                } else {
                                    return data
                                }
                            }
                        }, {
                            data: 'checkedPrice'
                        }, {
                            data: 'scrapReason'
                        }, {
                            data: 'repaireState',
                            render: function (data) {
                                var str = '';
                                if (data == 0) {
                                    str = '不合格';
                                } else if (data == 1) {
                                    str = '合格';
                                } else if (data == 2) {
                                    str = '报废';
                                }
                                return str;
                            }
                        }],
                        columnDefs: [{
                            targets: 19,
                            data: function (row) {
                                var str = '';
                                str += '<button id="exportRecordExcel" type="button" class="btn btn-success btn-xs" title="导出"><span class="glyphicon glyphicon-download-alt"></span></button>';
                                return str;
                            }
                        }],
                        ordering: false,
                        paging: true,
                        pageLength: 5,
                        drawCallback: function (settings) {
                            var api = this.api();
                            var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                            api.column(0).nodes().each(function (cell, i) {
                                cell.innerHTML = startIndex + i + 1;
                            });
                        }
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

                                sheetDetailTable.ajax
                                    .url(
                                        config.basePath
                                        + '/repairManage/repair/getAllSheetDetails?'
                                        + sheetDetailParams)
                                    .load();

                            }
                        });



                /** 导出配件详情信息 */
                $("#sheetTable tbody").on('click', '#exportExcel',
                    function () {
                        var tr = $(this).closest('tr');
                        sheetTrData = sheetTable.row(tr).data();
                        sheet_id2 = sheetTrData.sheetId;
                        var params = $.param({
                            sheetId: sheet_id2,
                            fileName:"所内检修单"
                        });
                        window.location.href = config.basePath + '/exportExcel/exportCheckInfo?' + params;
                    });

                /** 导出配件详情信息 */
                $("#sheetDetailTable tbody").on('click', '#exportRecordExcel',
                    function () {
                        var tr = $(this).closest('tr');
                        var sheetDetail = sheetDetailTable.row(tr).data();
                        sheet_id =sheetDetail.sheetId;
                        part_code =sheetDetail.partCode;
                        window.location.href = config.basePath + '/repairManage/check/export/' + sheet_id+'/'+part_code;
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
            })
    })