/**
 * 车间调拨到班组
 */
require(['../config'],
    function(config) {

        require(['datetimepicker'],
            function() {
                var date = new Date();
                date.setDate("1");
                date.setMonth(date.getMonth()-1)
                initDatetimepicker("queryTime", date);
                initDatetimepicker("queryTime2", new Date());
                initDatetimepicker("purchaseOrRepairedDateAdd", new Date());
                initDatetimepicker("purchaseOrRepairedDateModify", new Date());
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
            function($, bootstrap, dataTable, CMethod) {

                var sheet_id = ''; // 查询的单号
                var verify_flag = ''; // 查询的审核状态
                var newDate = new Date();
                var verifyFlag = '';
                var sheet_id2 = '';
                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                // var roles= window.parent.user.roles; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var sheetTrData = ''; // 保留点击的单号信息
                var partCode='';
                var partId='';
                var sourceStoreHouseId = ''; // 保留点击的原仓库信息
                /**
                 * 查询
                 */
                $('#btn-search').click(function(e) {
                    sheetTable.ajax.reload();
                    sheetDetailTable.column(0).search('');
                    sheetDetailTable.clear().draw();
                    sheet_id = '';
                    verify_flag = '';
                });

                /**
                 * 重置
                 */
                $('#btn-reset').click(function(e) {
                    $('#sheet_id').val('');
                    $('#verify_flag').val('');
                    var now = new Date();
                    now.setDate(1);
                    $('#queryTime').val(formatDateBy(now.getTime(), 'yyyy-MM-dd'));
                    $('#queryTime2').val(formatDateBy(new Date(), 'yyyy-MM-dd'));
                });
                var sourceParams=JSON.stringify({
                    depotId : deptId,
                    type:6
                })
                /**
                 * 初始化入库单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/stock/sparesToWorkShop/searchSheets',
                        type: 'GET',
                        data: function(d) {
                            d.sheetId = "%" + $('#sheet_id').val() + "%";
                            d.sendVerifyFlag = $('#verify_flag option:selected').val();
                            d.queryTime = $("#queryTime").val();
                            d.queryTime2 = ($("#queryTime2").val() == '' ? '': $("#queryTime2").val() + " 23:59:59");
                            d.sourceDepotId=deptId;
                        }
                    },
                    columns: [{
                        data: null
                    },
                        {
                            data: 'sheetId'
                        },
                        {
                            data: 'trackingNumber'
                        },{
                            data: 'sourceStorehouseName'
                        },
                        {
                            data: 'objectStorehouseName'
                        },
                        {
                            data: 'addDate',
                            render: function(data) {
                                if (data > 0) {
                                    return formatDateBy(data, 'yyyy-MM-dd');
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
                            data: 'sendVerifyFlag',
                            render: function(data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '<span style="color:red;font-weight:bold;">新建</span>';
                                } else if (data == 1) {
                                    str = '<span style="color:blue;font-weight:bold;">确认中</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:black;font-weight:bold;">已确认</span>';
                                }
                                else if (data == 3) {
                                    str = '<span style="color:red;font-weight:bold;">确认不通过</span>';
                                }
                                return str;
                            }
                        },
                        {
                            data: 'sendVerifyDate',
                            render: function(data) {
                                if (data > 0) {
                                    return formatDateBy(data, 'yyyy-MM-dd');
                                } else {
                                    return '-';
                                }
                            }
                        }
                    ],
                    ordering: false,
                    paging: true,
                    pageLength: 5,
                    serverSide: true,
                    drawCallback: function(settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function(cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
                    },
                });

                $('#sheetTable tbody').on('click', 'tr',
                    function(e) { // 获取当前单号
                        if (sheetTable.data().any()) {
                            $(this).addClass('selected').siblings().removeClass('selected');
                            var tr = $(this).closest('tr');
                            sheetTrData = sheetTable.row(tr).data();
                            sheet_id2 = sheetTrData.sheetId;
                            var params = $.param({
                                sheetId: sheet_id2
                            });
                            sheetDetailTable.column(0).search('');
                            sheetDetailTable.ajax.url(config.basePath + '/stock/sparesToWorkShop/getAllSheetDetails?' + params).load();
                        }
                    });

                /**
                 * 初始化配件详情表
                 */
                var sheetDetailTable = dataTable('sheetDetailTable', {
                    bAutoWidth: false,
                    ajax: {
                        type: 'POST',
                        data: "",
                        url: config.basePath + '/stock/sparesToWorkShop/getAllSheetDetails'
                    },
                    columns: [
                        {
                            data : null
                        },
                        {
                            data : 'partId'
                        },
                        {
                            data : 'partCode'
                        },{
                            data:'supplierName'
                        },
                        {
                            data : 'devicePartsName'
                        },
                        {
                            data : 'deviceTypeName'
                        },
                        {
                            data : 'deviceModelName'
                        },
                        {
                            data : 'assetAttributesName'
                        },
                        {
                            data:'partState',
                            render:function(data){
                                if(data==1){
                                    return "新购"
                                }else if(data==2){
                                    return "送修";
                                }else if(data==3){
                                    return "初始化在探测站";
                                }else if(data==4){
                                    return "初始化在备品库";
                                }else if(data==5){
                                    return "初始化在送修库";
                                }else {
                                    return "-";
                                }
                            }
                        }, {
                            data : 'warranty',
                            render:function(data){
                                if(data==0){
                                    return "否";
                                }else if(data==1){
                                    return "是";
                                }else{
                                    return "-";
                                }
                            }
                        },{
                            data:'purchaseOrRepairedPrice'
                        },{
                            data:'purchaseOrRepairedDate'
                        },{
                            data:'remark'
                        }],
                    ordering: false,
                    paging: true,
                    pageLength: 5,
                    drawCallback: function(settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function(cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
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
                            fileName:"班组备品到车间单据"
                        });
                        window.location.href = config.basePath + '/exportExcel/export?' + params;
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
                 * 定时隐藏alert框
                 */
                function hideTimeout(id, ms) {
                    var time = setTimeout(function() {
                            $("#" + id).hide();
                        },
                        ms)
                }

            });
    });