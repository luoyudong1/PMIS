/**
 * 备品库和送修库调拨
 */
require(['../config'],
    function (config) {

        require(['datetimepicker'],
            function () {
                var date = new Date();
                var newDate=new Date();
                date.setDate("1");
                date.setMonth(date.getMonth() - 1)
                initDatetimepicker("queryTime", date);
                initDatetimepicker("queryTime2", newDate);
                initDatetimepicker("faultDateAdd", null);
                initDatetimepicker("faultDateModify", null);

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
                var verify_flag = ''; // 查询的审核状态
                var newDate = new Date();
                var verifyFlag = '';
                var sheet_id2 = '';
                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                // var roles= window.parent.user.roles; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var sheetTrData = ''; // 保留点击的单号信息
                var partCode = '';
                var partId = '';
                var assetAttributesName='';
                var sourceStoreHouseId = ''; // 保留点击的原仓库信息
                var objectStoreHouseId = ''; // 保留点击的原仓库信息
                var sourceStoreHouseName = ''; // 保留点击的原仓库名称
                var sendVerifyFlag = ''
                var objectStoreHouseName = ''
                var faultInfoList=''
                var detectDeviceId=''
                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                    sheetDetailTable.column(0).search('');
                    sheetDetailTable.clear().draw();
                    sheet_id = '';
                    verify_flag = '';
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
                var sourceParams = JSON.stringify({
                    depotId: deptId
                })
                /**
                 *  /**
                 * 初始化源仓库下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/stock/sparesAndRepair/getAllSourceStoreHouse",
                    data: sourceParams,
                    dataType: 'json',
                    contentType: 'application/json',
                    type: 'POST',
                    success: function (result) {
                        for (var i = 0; i < result.data.length; i++) {
                            $("#sourceStoreHouseNameAdd").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                            sourceStoreHouseId = result.data[i].storehouseId;
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                /**
                 *  /**
                 * 初始化目的仓库下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/stock/repairAndSpares/getAllSourceStoreHouse",
                    data: sourceParams,
                    dataType: 'json',
                    contentType: 'application/json',
                    type: 'POST',
                    success: function (result) {
                        for (var i = 0; i < result.data.length; i++) {
                            $("#storeHouseNameAdd").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                            objectStoreHouseId = result.data[i].storehouseId
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                function getFaultInfo() {
                    $.ajax({
                        async: false,
                        url: config.basePath
                            + "/faultHandle/faultReport/getFaultInfo",
                        type: 'GET',
                        dataType: 'json',
                        data: {"detectDeviceId": detectDeviceId},
                        success: function (result) {
                            faultInfoList=result

                        },
                        error: function (result) {
                            console.log(result);
                        }
                    });

                }
                /**
                 *  /**
                 * 初始化探测站下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/stock/sparesAndRepair/getDetectDevice",
                    data: JSON.stringify({
                        depotId: deptId
                    }),
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    success: function (result) {
                        for (var i = 0; i < result.length; i++) {
                            $("#detectDeviceAdd").append('<option value="' + result[i].detectDeviceId + '">' + result[i].detectDeviceName + '</option>');
                            $("#detectDeviceModify").append('<option value="' + result[i].detectDeviceId + '">' + result[i].detectDeviceName + '</option>');
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });


                /**
                 * 初始化入库单据
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/stock/sparesAndRepair/getAllSheets',
                        type: 'GET',
                        data: function (d) {
                            d.sheetId = "%" + $('#sheet_id').val() + "%";
                            d.sendVerifyFlag = $('#verify_flag option:selected').val();
                            d.queryTime = $("#queryTime").val();
                            d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                            d.sourceDepotId = deptId;
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
                        }, {
                            data: 'sourceStorehouseName'
                        },
                        {
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
                            data: 'sendRemark'
                        },
                        {
                            data: 'sendOperatorName'
                        },
                        {
                            data: 'sendVerifyFlag',
                            render: function (data) {
                                var str = "-";
                                if (data == 0) {
                                    str = '<span style="color:red;font-weight:bold;">新建单据</span>';
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
                            render: function (data) {
                                if (data > 0) {
                                    return formatDateBy(data, 'yyyy-MM-dd');
                                } else {
                                    return '-';
                                }
                            }
                        }
                    ],
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
                 * 初始化配件表格
                 */
                var tblPartsInfo = dataTable(
                    'tblPartsInfo',
                    {
                        bAutoWidth: false,
                        ajax: {
                            url: config.basePath
                                + '/stock/sparesAndRepair/getAllPartsByStock',
                            type: 'get',
                            data: function (d) {
                                d.storeHouseId = sourceStoreHouseId;
                                d.partId = '%' + $("#query_part_id").val() + '%';
                                d.partCode = '%' + $("#query_part_code").val() + '%';

                            }
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
                            data: 'supplierName'
                        }, {
                            data: 'assetAttributesName'
                        }, {
                            data: 'faultDate'
                        }, {
                            data: 'faultInfo'
                        }, {
                            data: 'usedStationName'
                        },
                            {
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

                $('#sheetTable tbody').on('click', 'tr',
                    function (e) { // 获取当前单号
                        if (sheetTable.data().any()) {
                            $(this).addClass('selected').siblings().removeClass('selected');
                            var tr = $(this).closest('tr');
                            sheetTrData = sheetTable.row(tr).data();
                            sheet_id2 = sheetTrData.sheetId;
                            sendVerifyFlag = sheetTrData.sendVerifyFlag;
                            sourceStoreHouseName = sheetTrData.sourceStorehouseName
                            objectStoreHouseName = sheetTrData.objectStorehouseName
                            var params = $.param({
                                sheetId: sheet_id2
                            });
                            if (sendVerifyFlag == 0 || sendVerifyFlag == 3) { // 已审核不能添加
                                $("#add_sheetDetail").show();
                            } else {
                                $("#add_sheetDetail").hide();
                            }
                            tblPartsInfo.ajax.reload()
                            sheetDetailTable.ajax.url(config.basePath + '/stock/sparesAndRepair/getAllSheetDetails?' + params).load();
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
                        url: config.basePath + '/stock/sparesAndRepair/getAllSheetDetails'
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
                        }, {
                            data: 'supplierName'
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
                            data: 'assetAttrName'
                        },
                        {
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
                            data: 'faultDate'
                        }, {
                            data: 'faultInfo'
                        }, {
                            data: 'usedStationName'
                        }, {
                            data: 'remark'
                        }],
                    columnDefs: [{
                        targets: 14,
                        data: function (row) {
                            var str = '';
                            if (row.sendVerifyFlag == 0 || row.sendVerifyFlag == 3) {
                                str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改配送配件"><span class="glyphicon glyphicon-edit"></span></a>';
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
                    drawCallback: function (settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function (cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
                    }
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
                                        // //alert(msg.msg);
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
                 * 添加单据
                 */
                $('#addSheetModal').on('show.bs.modal',
                    function () {
                        // 单据ID为单据类型+部门id+序列号
                        if (deptId < 10) {
                            var sheetId = "18-0" + deptId + "-";
                        } else {
                            var sheetId = "18-" + deptId + "-";
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
                                $('#sheetIdAdd').val(result);
                            },

                        });

                        loadDate("sheetDateAdd", new Date());
                        $('#sendOperatorNameAdd').val(user_name);
                        $('#sendRemarkAdd').val('');
                    });
                /**
                 * 新增单据
                 */
                $("#btnAddSheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if ($("#sourceStoreHouseNameAdd").val() == "") {
                            $("#alertMsgSheetAdd").html("<font style='color:red'>源仓库为空</font>");
                            $("#alertMsgSheetAdd").css('display', 'inline-block')
                            timer = CMethod.hideTimeout("alertMsgSheetAdd", "alertMsgSheetAdd", 5000);
                            return false;
                        }
                        if ($("#storeHouseNameAdd").val() == "") {
                            $("#alertMsgSheetAdd").html("<font style='color:red'>目的仓库为空</font>");
                            $("#alertMsgSheetAdd").css('display', 'inline-block')
                            timer = CMethod.hideTimeout("alertMsgSheetAdd", "alertMsgSheetAdd", 5000);
                            return false;
                        }
                        var params = JSON.stringify({
                            sheetId: $('#sheetIdAdd').val(),
                            sourceStoreHouseId: sourceStoreHouseId,
                            objectStoreHouseId: objectStoreHouseId ,
                            addDate: $('#sheetDateAdd').val(),
                            sendOperatorId: user_id,
                            sendOperatorName: $('#sendOperatorNameAdd').val(),
                            sendRemark: $('#sendRemarkAdd').val(),
                            trackingNumber: $('#trackingNumber').val(),
                        });
                        $.ajax({
                            url: config.basePath + '/stock/sparesAndRepair/createSheetInfo',
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
                        $('#deviceModelNameAdd').val('');
                        $('#partCodeAdd').val('');
                        $('#faultDateAdd').val('');
                        $('#warrantyAdd option:first').prop("selected",true)
                        $('#detectDeviceAdd option:first').prop("selected",true)
                        $('#faultInfoAdd').val('');
                        $('#part_idAdd').val('');
                        $('#parts_id_select').val('');
                        $('#remarkAdd').val('');
                    });


                /**
                 * 点击当前配件 批量新增配件
                 */


                function verifySheetDetailAddParams() {
                    if ($("#part_idAdd").val() == "") {
                        $("#alertMsgAdd").html("<font style='color:red'>配件编码为空</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }
                    if ($("#partCodeAdd").val() == "") {
                        $("#alertMsgAdd").html("<font style='color:red'>配件出厂编码空</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }
                    if ($("#warrantyAdd").val() == "") {
                        $("#alertMsgAdd").html("<font style='color:red'>是否质保期为空</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }
                    if ($("#detectDeviceAdd").val() == ""&&$("#remarkAdd").val() == "") {
                        $("#alertMsgAdd").html("<font style='color:red'>探测站为空，请填写备注</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                        return false;
                    }
                    if (sourceStoreHouseName.indexOf("备品库") != -1) {
                        var date = new Date();
                        var faultDateAdd = $("#faultDateAdd").val()
                        if (faultDateAdd == '' || faultDateAdd == null) {
                            $("#alertMsgAdd").html("<font style='color:red'>故障发生时间为空</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if (Date.parse(faultDateAdd) > date) {
                            $("#alertMsgAdd").html("<font style='color:red'>故障发生时间大于当前时间</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                        if ($("#faultInfoAdd").val() == "") {
                            $("#alertMsgAdd").html("<font style='color:red'>故障现象为空</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                            return false;
                        }
                    }
                    return true;
                }

                /**
                 * 添加单据详情确认
                 */
                $("#btnAddSheetDetailOk").on('click',
                    function (e) {
                        e.preventDefault();
                        if (verifySheetDetailAddParams() == false) {//参数验证
                            return false;
                        }
                        var params = JSON.stringify({
                            sheetId: sheet_id2,
                            partCode: $('#partCodeAdd').val(),
                            partId: $('#part_idAdd').val(),
                            warranty: $('#warrantyAdd').val(),
                            assetAttributesName:assetAttributesName,
                            usedStationId: $('#detectDeviceAdd').val(),
                            usedStationName: $('#detectDeviceAdd option:selected').text(),
                            faultDate: $('#faultDateAdd').val(),
                            faultInfo: $('#faultInfoAdd').val(),
                            partState:2,
                            remark:$('#remarkAdd').val()
                        });
                        $.ajax({
                            url: config.basePath + '/stock/sparesAndRepair/SheetDetailCreate',
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
                                    sheetDetailTable.column(0).search('');
                                    sheetDetailTable.ajax.url(config.basePath + '/stock/sparesAndRepair/getAllSheetDetails?' + json).load();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);


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
                            sendRemark: $('#remarkModify').val(),

                        });
                        $.ajax({
                            url: config.basePath + '/stock/sparesAndRepair/modify',
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
                        var date = new Date();
                        if (sheetDetailTable.data().length < 1) {
                            alert("配件为空，请先添加配件！")
                            return;
                        }
                        var params = JSON.stringify({
                            sendVerifyFlag: 2,
                            sheetId: $('#btnPopSheetVerifyOk').val(),
                            sendVerifyDate: date,

                        });
                        $.ajax({
                            url: config.basePath + '/stock/sparesAndRepair/modify',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    $('#add_sheetDetail').hide()
                                    sheetTable.ajax.reload();
                                    sheetDetailTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据确认成功！</strong></span>');
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
                            url: config.basePath + '/stock/sparesAndRepair/delete',
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
                        $('#warningText').text('确定要删除该配件（' + data.partId + '）？');
                        partId = data.partId
                        partCode = data.partCode

                    });

                $('#btnPopOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            partId: partId,
                            sheetId: sheet_id2,
                            partCode: partCode
                        });
                        $.ajax({
                            url: config.basePath + '/stock/sparesAndRepair/SheetDetailDeleteByCode',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    tblPartsInfo.ajax.reload()
                                    sheetDetailTable.ajax.reload();
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
                        $("#parts_id_select").val(trData.partId);
                        $("#part_nameAdd").val(trData.devicePartsName);
                        $("#deviceModelNameAdd").val(trData.deviceModelName);
                        $("#partCodeAdd").val(trData.partCode);
                        $("#warrantyAdd").val(trData.warranty);
                        $("#part_idAdd").val(trData.partId);
                        assetAttributesName=trData.assetAttributesName
                        $("#detectDeviceAdd option:contains('" + trData.usedStationName + "')").prop("selected", true);
                        $("#remarkAdd").val(trData.remark);
                        $('#partsModal').modal('hide');
                    });
                $("#modifySheetDetailModal").on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetDetailTable.row(tr).data();
                        $("#part_nameModify").val(data.devicePartsName);
                        $("#deviceModelNameModify").val(data.deviceModelName);
                        $("#partCodeModify").val(data.partCode);
                        $("#faultDateModify").val(formatDateBy(data.faultDate,"yyyy-MM-dd"));
                        $("#faultInfoModify").val(data.faultInfo);
                        $("#partCodeModify").val(data.partCode);
                        $("#warrantyModify").val(data.warranty);
                        $("#part_idModify").val(data.partId);
                        $("#detectDeviceModify option:contains('" + data.usedStationName + "')").prop("selected", true);
                        $("#sheetDetailRemarkModify").val(data.remark);
                    });
                //修改参数验证
                function verifySheetDetailModifyParams() {
                    if ($("#part_idModify").val() == "") {
                        $("#alertMsgModify").html("<font style='color:red'>配件编码为空</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgAdd", 5000);
                        return false;
                    }
                    if ($("#partCodeModify").val() == "") {
                        $("#alertMsgModify").html("<font style='color:red'>配件出厂编码空</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                        return false;
                    }
                    if ($("#warrantyModify").val() == "") {
                        $("#alertMsgModify").html("<font style='color:red'>是否质保期为空</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                        return false;
                    }
                    if ($("#detectDeviceModify").val() == ""&&$("#sheetDetailRemarkModify").val() == "") {
                        $("#alertMsgModify").html("<font style='color:red'>探测站为空，请填写备注</font>");
                        $("#alertMsgModify").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                        return false;
                    }
                    if (sourceStoreHouseName.indexOf("备品库") != -1) {
                        var date = new Date();
                        var faultDateModify = $("#faultDateModify").val()
                        if (faultDateAdd == '' || faultDateAdd == null) {
                            $("#alertMsgModify").html("<font style='color:red'>故障发生时间为空</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                            return false;
                        }
                        if (Date.parse(faultDateModify) > date) {
                            $("#alertMsgModify").html("<font style='color:red'>故障发生时间大于当前时间</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                            return false;
                        }
                        if ($("#faultInfoModify").val() == "") {
                            $("#alertMsgModify").html("<font style='color:red'>故障现象为空</font>");
                            $("#alertMsgModify").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                            return false;
                        }
                    }
                    return true;
                }
                /***修改配件详情确认按钮事件**/
                /****************************************/
                $("#btnModifySheetDetailOk").click(function () {
                    if(verifySheetDetailModifyParams()==false){
                        return false;
                    }
                    var params = JSON.stringify({
                        sheetId: sheet_id2,
                        partCode: $("#partCodeModify").val(),
                        warranty: $('#warrantyModify').val(),
                        faultDate: $('#faultDateModify').val(),
                        faultInfo: $('#faultInfoModify').val(),
                        remark: $('#sheetDetailRemarkModify').val(),
                        usedStationId: $('#detectDeviceModify').val(),
                        usedStationName: $('#detectDeviceModify option:selected').text()
                    });
                    $.ajax({
                        url: config.basePath + '/stock/sparesAndRepair/SheetDetailModify',
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
                                sheetDetailTable.ajax.url(config.basePath + '/stock/sparesAndRepair/getAllSheetDetails?' + json).load();
                                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
                                $("#infoAlert").show();
                                hideTimeout("infoAlert", 2000);


                            }
                        }
                    });
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

                /** 单据点击事件 */
                $("#sheetTable tbody").on('click', '#exportExcel',
                    function () {
                        var tr = $(this).closest('tr');
                        sheetTrData = sheetTable.row(tr).data();
                        sheet_id2 = sheetTrData.sheetId;

                        var params = $.param({
                            sheetId: sheet_id2,
                            fileName: "备品库和送修库调拨单据"
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
                tblPartsInfo.on('draw.dt', function () {
                    //给第一列编号
                    tblPartsInfo.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
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
    });