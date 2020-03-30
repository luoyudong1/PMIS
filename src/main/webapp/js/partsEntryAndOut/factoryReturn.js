/**
 * 返修入库
 */
require(['../config'],
    function (config) {

        require(['datetimepicker'],
            function () {
                var date = new Date();
                date.setDate("1");
                date.setMonth(date.getMonth() - 1)
                initDatetimepicker("queryTime", date);
                initDatetimepicker("queryTime2", new Date());
                initDatetimepicker("factoryRepairedDateAdd", new Date());
                initDatetimepicker("factoryRepairedDateModify", null);

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

                var sheet_id = ''; // 查询的单号
                var send_verify_flag = ''; // 查询的审核状态
                var newDate = new Date();
                var verifyFlag = '';
                var sheet_id2 = '';
                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                // var roles= window.parent.user.roles; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var sheetTrData = ''; // 保留点击的单号信息
                var sourceStoreHouseId = ''; // 保留点击的原仓库信息
                var objectStoreHouseId = ''; // 保留点击的原仓库信息
                var supplierName = '';// 保留点击的原仓库名称
                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                    sheetDetailTable.column(0).search('');
                    sheetDetailTable.clear().draw();
                    sheet_id = '';
                    send_verify_flag = '';
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
                 * 初始化资产属性
                 */
                // $.ajax({
                //     async: false,
                //     url: config.basePath + "/partsEntryAndOut/factoryReturn/getAttribute",
                //     data: {
                //         "action": "all"
                //     },
                //     dataType: 'json',
                //     success: function (result) {
                //         for (var i = 0; i < result.data.length; i++) {
                //             $("#supplierNameAdd").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                //         }
                //     },
                //     error: function (result) {
                //         console.log(result);
                //     }
                // });
                /**
                 * 初始化供货商下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/partsEntryAndOut/factoryReturn/getAllSourceStoreHouse",
                    data: {
                        "action": "all"
                    },
                    dataType: 'json',
                    success: function (result) {
                        for (var i = 0; i < result.data.length; i++) {
                            $("#supplierNameAdd").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                /**
                 * 初始化收货库
                 */
                var params = {
                    depotId: deptId
                }
                $.ajax({
                    async: false,
                    url: config.basePath + "/partsEntryAndOut/factoryReturn/getAllObjectStoreHouse",
                    data: params,
                    type: 'get',
                    success: function (result) {
                        if (result!=null) {
                            objectStoreHouseId = result.storehouseId
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });


                /**
                 * 初始化出库单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/partsEntryAndOut/factoryReturn/getAllSheets',
                        type: 'GET',
                        data: function (d) {
                            d.sheetId = "%" + $('#sheet_id').val() + "%";
                            d.sendVerifyFlag = $('#verify_flag').val();
                            d.queryTime = $("#queryTime").val();
                            d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
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
                        },
                        {
                            data: 'sourceStorehouseName'
                        }, {
                            data: 'objectStorehouseName'
                        },
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
                        {
                            data: 'sendOperatorName'
                        },
                        {
                            data: 'sendVerifyFlag',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '<span style="color:red;font-weight:bold;">新建</span>';
                                } else if (data == 1) {
                                    str = '<span style="color:blue;font-weight:bold;">审核中</span>';
                                } else if (data == 2) {
                                    str = '<span style="color:black;font-weight:bold;">已完成</span>';
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
                        }, {
                            data: 'sendRemark'
                        },],
                    columnDefs: [{
                        targets: 10,
                        data: function (row) {
                            var str = '';
                            if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                    + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                    + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
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
                $('#add_sheetDetail').click(function () {
                    tblPartsInfo.ajax.reload()
                })
                /**
                 * 初始化配件表格
                 */
                var tblPartsInfo = dataTable('tblPartsInfo', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/partsEntryAndOut/factoryReturn/getAllParts',
                        type: 'get',
                        data: function (d) {
                            d.storeHouseId = sourceStoreHouseId
                            d.partId = '%' + $("#query_part_id").val() + '%';
                            d.partCode = '%' + $("#query_part_code").val() + '%';
                            d.depotId=deptId;

                        }
                    },
                    columns: [{
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
                            data: 'supplierName'
                        },
                        {
                            data: 'purchasePrice'
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
                            data: 'remark'
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

                $('#sheetTable tbody').on('click', 'tr',
                    function (e) { // 获取当前单号
                        if (sheetTable.data().any()) {
                            $(this).addClass('selected').siblings().removeClass('selected');
                            var tr = $(this).closest('tr');
                            sheetTrData = sheetTable.row(tr).data();
                            sheet_id2 = sheetTrData.sheetId;
                            verifyFlag = sheetTrData.sendVerifyFlag;
                            sourceStoreHouseId = $('#supplierNameAdd option:contains("' + sheetTrData.sourceStorehouseName + '")').val()
                            var params = $.param({
                                sheetId: sheet_id2
                            });
                            if (verifyFlag == 0 || verifyFlag == 3) { // 已审核不能添加
                                $("#add_sheetDetail").show();
                            } else {
                                $("#add_sheetDetail").hide();
                            }
                            tblPartsInfo.ajax.reload()
                            sheetDetailTable.column(0).search('');
                            sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/factoryReturn/getAllSheetDetails?' + params).load();
                        }
                    });

                /**
                 * 初始化配件详情表
                 */
                var sheetDetailTable = dataTable('sheetDetailTable', {
                    bAutoWidth: false,
                    ajax: {
                        type: 'GET',
                        data: "",
                        url: config.basePath + '/partsEntryAndOut/factoryReturn/getAllSheetDetails'
                    },
                    columns: [{
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
                            data: 'supplierName'
                        },
                        {
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
                    columnDefs: [{
                        targets: 15,
                        data: function (row) {
                            var str = '';
                            if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                str += '<a class="modifySheetDetail btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改配件"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                str += '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
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
                 * 单据ID失去焦点验证是否存在此单据
                 */
                $("#sheetIdAdd").on('blur',
                    function () {
                        var part_id = $('#sheetIdAdd').val();
                        if ($.trim(part_id) != "") {
                            var params = $.param({
                                "sheet_id": sheet_id
                            });
                            $.ajax({
                                url: config.basePath + '/system/partsManage/find',
                                type: "GET",
                                data: params,
                                contentType: 'application/json',
                                dataType: "json",
                                success: function (msg) {
                                    if (msg.code != null && msg.code != 0) {
                                        alert(msg.msg);
                                        $("#sheetIdAdd")[0].focus();
                                    }
                                }
                            });
                        } else {
                            $("#warn_content").text("单据ID不能为空!");
                            $("#warningModal").modal('show');
                            $("#sheetIdAdd")[0].focus();
                        }
                    });
                /**
                 * 添加单据经办人
                 */
                $("#operator_nameAdd").val(user_name)
                /**
                 * 添加单据
                 */
                $('#addSheetModal').on('show.bs.modal',
                    function () {
                        var DateTime = new Date();
                        // 单据ID为单据类型+部门id+序列号
                        if (deptId < 10) {
                            var sheetId = "24-0" + deptId + "-";
                        } else {
                            var sheetId = "24-" + deptId + "-";
                        }
                        $.ajax({
                            url: config.basePath + '/entryAndOut/purchaseParts/getMaxSheetId',
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

                        loadDate("sheetDateAdd", new Date());
                        $('#sendOperatorNameAdd').val(user_name);
                        $('#sendRemarkAdd').val('');
                    });

                $("#btnAddSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            sheetId: $('#sheetIdAdd').val(),
                            trackingNumber: $('#trackingNumberAdd').val(),
                            sourceStoreHouseId: $('#supplierNameAdd option:selected').val(),
                            objectStoreHouseId: objectStoreHouseId,
                            addDate: $('#sheetDateAdd').val(),
                            sendOperatorId: user_id,
                            sendOperatorName: user_name,
                            sendRemark: $('#remarkAdd').val(),
                            sendVerifyFlag: 0,
                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/createSheetInfo',
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
                 * 添加入库配件
                 */
                $('#addSheetDetailModal').on('show.bs.modal',
                    function () {
                        $('#part_codeAdd').val('');
                        $('#part_nameAdd').val('');
                        $('#part_idAdd').val('');
                        $('#parts_id_select').val('');
                        $('#partUnit_priceAdd').val('');
                        $('#locationAdd').val('');
                        $('#remarkPartsAdd').val('');
                    });

                /**
                 * 添加入库配件
                 */
                $('#sheetDetailTable tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前配件信息
                            if (sheetDetailTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                var trData = sheetDetailTable
                                    .row(tr).data();
                                $('#part_codeModify').val(trData.partCode);
                                $('#part_nameModify').val(trData.devicePartsName);
                                $('#part_idModify').val(trData.partId);
                                $('#factoryReplaceComponentModify').val(trData.factoryReplaceComponent);
                                $('#factoryRepairedPriceModify').val(trData.factoryRepairedPrice);
                                $('#factoryRepairedDateModify').val(trData.factoryRepairedDate);
                                $('#warrantyModify').val(trData.warranty);
                                $('#repairStateModify').val(trData.repaireState);
                                $('#remarkPartsModify').val(trData.remark);
                            }
                        })

                /**
                 * 验证单据参数是否为空
                 */
                function verifyPartsAdd() {

                    var repaireState = $('#repairStateAdd').val();
                    var factoryRepairedPrice = $('#factoryRepairedPriceAdd').val();
                    var factoryRepairedDate = $('#factoryRepairedDateAdd').val();
                    var date=new Date()
                    if ((repaireState == '' || repaireState == null) && !(repaireState === 0)) {
                        $("#alertMsgAdd").html("<font style='color:red'>修复状态为空</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }else if ((factoryRepairedPrice == '' || factoryRepairedPrice == null)&&!(factoryRepairedPrice === 0)) {
                        $("#alertMsgAdd").html("<font style='color:red'>修复单价为空</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }
                    else if (factoryRepairedDate==''||factoryRepairedDate==null||Date.parse(factoryRepairedDate)>date) {
                        $("#alertMsgAdd").html("<font style='color:red'>修复时间为空或者大于当前时间</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }
                }

                function verifyPartsModify() {

                    var repaireState = $('#repairStateModify').val();
                    var factoryRepairedPrice = $('#factoryRepairedPriceModify').val();
                    var factoryRepairedDate= $('#factoryRepairedDateModify').val();
                    var date=new Date()
                    if ((repaireState == '' || repaireState == null) && !(repaireState === 0)) {
                        $("#alertMsgModify").html("<font style='color:red'>修复状态为空</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                        return false;
                    }
                    if ((factoryRepairedPrice == '' || factoryRepairedPrice == null)&&!(factoryRepairedPrice === 0)) {
                        $("#alertMsgModify").html("<font style='color:red'>修复单价为空</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                        return false;
                    }
                    if (factoryRepairedDate==''||factoryRepairedDate==null||Date.parse(factoryRepairedDate)>date) {
                        $("#alertMsgModify").html("<font style='color:red'>修复时间为空或者大于当前时间</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                        return false;
                    }
                }

                /**
                 * 添加单据详情确认
                 */
                $("#btnAddSheetDetailOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if (verifyPartsAdd() == false) {
                            return false;
                        }
                        var params = JSON.stringify({

                            sheetId: sheet_id2,
                            partCode: $('#part_codeAdd').val(),
                            partId: $('#part_idAdd').val(),
                            factoryReplaceComponent: $('#factoryReplaceComponentAdd').val(),
                            factoryRepairedPrice: $('#factoryRepairedPriceAdd').val(),
                            factoryRepairedDate: $('#factoryRepairedDateAdd').val(),
                            warranty: $('#warrantyAdd').val(),
                            repaireState: $('#repairStateAdd').val(),
                            remark: $('#remark').val()
                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/SheetDetailCreate',
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
                                    tblPartsInfo.ajax.reload()
                                    sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/factoryReturn/getAllSheetDetails?' + json).load();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                    $('#addSheetDetailModal').modal('hide')

                                }
                            }
                        });
                    });
                /**
                 * 修改单据详情确认
                 */
                $("#btnModifySheetDetailOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if (verifyPartsModify() == false) {
                            return false;
                        }
                        var params = JSON.stringify({
                            sheetId: sheet_id2,
                            partCode: $('#part_codeModify').val(),
                            partId: $('#part_idModify').val(),
                            factoryReplaceComponent: $('#factoryReplaceComponentModify').val(),
                            factoryRepairedPrice: $('#factoryRepairedPriceModify').val(),
                            factoryRepairedDate: $('#factoryRepairedDateModify').val(),
                            warranty: $('#warrantyModify').val(),
                            repaireState: $('#repairStateModify').val(),
                            remark: $('#remarkPartsModify').val()
                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/SheetDetailModify',
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
                                    tblPartsInfo.ajax.reload()
                                    sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/factoryReturn/getAllSheetDetails?' + json).load();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息修改成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                    $('#modifySheetDetailModal').modal('hide')

                                }
                            }
                        });
                    });
                /**
                 * 修改单据
                 */
                $('#modifySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        $('#sheetIdModify').val(data.sheetId);
                        loadDate("sheetDateModify", new Date());
                        $('#remarkModify').val(data.sendRemark);
                    });

                $("#btnModifySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            sheetId: $('#sheetIdModify').val(),
                            addDate: $('#sheetDateModify').val(),
                            sendRemark: $('#remarkModify').val()

                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/modifyVerify',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据信息修改成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });

                    });

                /**
                 * 提交入库单据
                 */
                $('#popSheetVerifyModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        $('#warningSheetVerifyText').text('确定提交单据ID为:' + data.sheetId + '的单据吗？');
                        $('#btnPopSheetVerifyOk').val(data.sheetId);
                    });

                /**
                 * 单据审核
                 */
                $("#btnPopSheetVerifyOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if (sheetDetailTable.data().length < 1) {
                            alert("配件为空，请先添加配件！")
                            return;
                        }
                        var date = new Date();
                        var params = JSON.stringify({
                            sendVerifyFlag: 2,
                            sheetId: $('#btnPopSheetVerifyOk').val(),
                            sendVerifyDate: date,
                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/modifyVerify',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    var params = $.param({
                                        sheetId: sheet_id2
                                    })
                                    $('#add_sheetDetail').hide()
                                    sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/factoryReturn/getAllSheetDetails?' + params).load();
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据已提交！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
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
                        $('#warningSheetText').text('确定要删除该单据（' + data.sheetId + '）？');
                        $('#deleteSheetId').val(data.sheetId);
                    });

                $('#btnPopSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            sheetId: $('#deleteSheetId').val()
                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/delete',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    $("#add_sheetDetail")
                                        .hide();
                                    sheetTable.ajax.reload();
                                    sheetDetailTable.ajax.reload();
                                    tblPartsInfo.ajax.reload()
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据删除成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });

                /**
                 * 删除配件
                 */
                $('#popModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        $('#warningText').text('确定要删除该配件（' + data.partCode + '）？');
                        $('#deletePartCode').val(data.partCode);

                    });

                $('#btnPopOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            sheetId: sheet_id2,
                            partCode: $('#deletePartCode').val()
                        });
                        $.ajax({
                            url: config.basePath + '/partsEntryAndOut/factoryReturn/SheetDetailDeleteByCode',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetDetailTable.ajax.reload();
                                    tblPartsInfo.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件删除成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });

                /**
                 * addSheetDetailModal点击配件 弹出配件库窗口
                 */
                $("#part_nameAdd").on('click',
                    function (e) {
                        $('#partsModal').modal('show');
                        tblPartsInfo.ajax.reload();
                    });

                /**
                 * 配件库查询
                 */
                $("#btnQueryparts").on('click',
                    function (e) {
                        tblPartsInfo.ajax.reload();
                    });

                /**
                 * 点击当前配件 获取配件
                 */
                $("#tblPartsInfo tbody").on("dblclick", "tr",
                    function () {
                        $(this).addClass('success').siblings().removeClass('success');
                        var trData = tblPartsInfo.row(this).data();
                        var tr = $(this).closest('tr');
                        var partsTrData = tblPartsInfo.row(
                            tr).data();
                        var partCode = partsTrData.factoryPartsCode;
                        var partId = partsTrData.partIdSeq
                        $("#parts_id_select").val(partsTrData.partId);
                        $("#part_nameAdd").val(partsTrData.devicePartsName);
                        $("#part_idAdd").val(partsTrData.partId);
                        $("#part_codeAdd").val(partsTrData.partCode);
                        loadDate("factory_repaired_dateAdd", new Date());
                        $("#partUnit_priceAdd").val(partsTrData.purchasePrice);
                        $("#warrantyAdd").val(partsTrData.warranty);
                        $('#partsModal').modal('hide');
                    })

                /**
                 * 时间加载
                 */
                function loadDate(id, date) {
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
                 * 添加监听input part_codeAdd值的改变事件
                 */
                $('#part_codeAdd').bind('input propertychange',
                    function () {
                        var tvalmum;
                        // 统计input输入字段的长度
                        tvalnum = $(this).val().length;
                        // 如果大于30个字直接进行字符串截取
                        if (tvalnum > 30) {
                            var tval = $(this).val();
                            tval = tval.substring(0, 30);
                            $(this).val(tval);
                            alert('长度超过限制！');
                        }
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
                tblPartsInfo.on('draw.dt', function () {
                    //给第一列编号
                    tblPartsInfo.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                })

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
    });