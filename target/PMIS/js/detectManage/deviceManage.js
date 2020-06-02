/**
 * 探测站管理
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

        require(['jquery', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin', 'zTree'],
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
                var deletePartCode = '';//删除的partCode
                var sourcePartCode = '';//源出厂编码
                var deviceModelName = ''
                var supplierName = ''
                var timer = '';
                var detectDeviceName = ''
                var detectDeviceId = ''
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
                /**
                 * 初始化供货商下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/deviceManage/detectManage/getDeviceModel",
                    data: {
                        "action": "all"
                    },
                    dataType: 'json',
                    success: function (result) {
                        for (var i = 0; i < result.length; i++) {
                            $("#deviceModelAdd").append('<option value="' + result[i].deviceModelId + '">' + result[i].deviceModelName + '</option>');
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                $('#operatorNameAdd').val(user_name)//初始化操作人
                /**
                 * 初始化供货商下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/entryAndOut/purchaseParts/getAllsuppliers",
                    data: {
                        "action": "all"
                    },
                    dataType: 'json',
                    success: function (result) {
                        for (var i = 0; i < result.data.length; i++) {
                            $("#supplierNameAdd").append('<option value="' + result.data[i].supplier_id + '">' + result.data[i].supplier_name + '</option>');
                        }
                    },
                    error: function (result) {
                        console.log(result);
                    }
                });
                /**
                 * 初始化班组下拉框
                 */
                $.ajax({
                    async: false,
                    url: config.basePath + "/deviceManage/detectManage/getManageDepot",
                    data: {
                        "depotId": deptId
                    },
                    dataType: 'json',
                    success: function (result) {
                        for (var i = 0; i < result.length; i++) {
                            $("#depotAdd").append('<option value="' + result[i].depotId + '">' + result[i].depotName + '</option>');
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
                        url: config.basePath + '/deviceManage/detectManage/listDetect',
                        type: 'GET',
                        data: function (d) {
                            d.depotId = deptId;
                        }
                    },
                    columns: [{
                        data: null
                    },
                        {
                            data: 'depotName'
                        },
                        {
                            data: 'detectDeviceName'
                        }, {
                            data: 'supplierName'
                        },
                        {
                            data: 'deviceModelName'
                        },
                        {
                            data: 'userName'
                        },
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
                        {
                            data: 'remark'
                        },

                    ],
                    columnDefs: [{
                        targets: 9,
                        data: function (row) {
                            var str = '';
                            str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改探测站"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;' + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除探测站"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
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
                 * 单据点击事件
                 */
                $('#sheetTable tbody').on('click', 'tr',
                    function (e) { // 获取当前单号
                        if (sheetTable.data().any()) {
                            $(this).addClass('selected').siblings().removeClass('selected');
                            var tr = $(this).closest('tr');
                            sheetTrData = sheetTable.row(tr).data();
                            detectDeviceId = sheetTrData.detectDeviceId;
                            detectDeviceName = sheetTrData.detectDeviceName;
                            deviceModelName = sheetTrData.deviceModelName;
                            supplierName = sheetTrData.supplierName;
                            $("#add_sheetDetail").show();
                            sheetDetailTable.ajax.reload();
                        }
                    });
                $("#add_sheetDetail").click(function () {
                    $('#detectDeviceAdd').val('')
                    $('#part_nameAdd').val('')
                    $('#part_idAdd').val('')
                    $('#part_specialAdd').val('')
                    $('#deviceModelNameAdd').val('')
                    $('#supplierAdd').val('')
                    $('#deviceModelNameAdd').val('')
                })
                //显示修改配件单
                $('#sheetDetailTable tbody').on('click', 'tr',
                    function (e) {
                        if (sheetDetailTable.data().any()) {
                            $(this).addClass('selected').siblings().removeClass('selected');
                            var tr = $(this).closest('tr');
                            var sheetDetailTrData = sheetDetailTable.row(tr).data();
                            $('#part_nameModify').val(sheetDetailTrData.devicePartsName)
                            $('#part_idModify').val(sheetDetailTrData.partId)
                            sourcePartCode = sheetDetailTrData.partCode
                            $('#part_codeModify').val(sourcePartCode)
                            $('#partUnit_priceModify').val(sheetDetailTrData.unitPrice)
                            $('#remarkSheetDetailModify').val(sheetDetailTrData.remark)

                        }
                    });
                /**
                 * 修改配件
                 */
                $('#btnModifySheetDetailOk').on('click', function (e) {
                        e.preventDefault();
                        if ($('#part_codeModify').val() == "") {
                            alert("出厂编码为空！")
                            return;
                        }
                        var params = JSON.stringify({
                            sheetId: sheet_id2,
                            partCode: $('#part_codeModify').val(),
                            sourcePartCode: sourcePartCode,
                            remark: $('#remarkSheetDetailModify').val()
                        });
                        $.ajax({
                            url: config.basePath + '/entryAndOut/preparePurchase/sheetDetailModify',
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
                                    });
                                    sheetDetailTable.ajax.url(config.basePath + '/entryAndOut/preparePurchase/getAllSheetDetails?' + params).load();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息修改成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);


                                }
                            }
                        });


                    }
                )
                /**
                 * 初始化配件详情表
                 */
                var sheetDetailTable = dataTable('sheetDetailTable', {
                    bAutoWidth: false,
                    ajax: {
                        type: 'POST',
                        data: function (d) {
                            d.detectDeviceId = detectDeviceId
                        },
                        url: config.basePath + '/deviceManage/detectManage/listDetectParts'
                    },
                    columns: [{
                        data: 'detectPartsId'
                    }, {
                        data: 'detectPartsId',
                        render: function (data) {
                            return detectDeviceName;
                        }
                    }, {
                        data: 'partId'
                    }, {
                        data: 'partsCode'
                    },
                        {
                            data: 'partsSpecial'
                        }, {
                            data: 'devicePartsName'
                        }, {
                            data: 'deviceTypeName'
                        }, {
                            data: 'deviceModelName'
                        }, {
                            data: 'supplierName'
                        }, {
                            data: 'enabled',
                            render: function (data) {
                                if (data == 0) {

                                    return "不可用";

                                } else if (data == 1) {
                                    return "可用";
                                } else {
                                    return "-";
                                }
                            }
                        }, {
                            data: 'ifMoved',
                            render: function (data) {
                                if (data == 0) {

                                    return "否";

                                } else if (data == 1) {
                                    return "是";
                                } else {
                                    return "-";
                                }
                            }
                        },
                    ],
                    columnDefs: [
                        {
                            targets: 11,
                            data: function (row) {
                                var str = '';
                                if (row.ifMoved == 1&&row.enabled==1) {
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
                    }
                });
                /**
                 * 初始化配件表格
                 */
                var tblPartsInfo = dataTable('tblPartsInfo', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/deviceManage/detectManage/getViewParts',
                        type: 'get',
                        dataType: 'json',
                        contentType: "application/json",
                        data: function (d) {
                            d.part_id = '%' + $("#query_part_id").val() + '%';
                            d.part_name = '%' + $("#query_part_name").val() + '%';
                            d.supplierName = supplierName;
                            d.deviceModelName = deviceModelName;
                        },
                    },
                    columns: [{
                        data: null
                    },
                        {
                            data: 'partsCode'
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
                            data: 'unitPrice'
                        },
                        {
                            data: 'supplierName'
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
                    }
                });

                /**
                 * 点击当前配件 获取配件
                 */
                $("#tblPartsInfo tbody").on("dblclick", "tr",
                    function () {
                        $(this).addClass('success').siblings().removeClass('success');
                        var trData = tblPartsInfo.row(this).data();
                        $("#parts_id_select").val(trData.partsCode);
                        $("#detectDeviceAdd").val(detectDeviceName);
                        $("#deviceModelNameAdd").val(trData.deviceModelName);
                        $("#supplierAdd").val(trData.supplierName);
                        $("#part_nameAdd").val(trData.devicePartsName);
                        $("#part_idAdd").val(trData.partsCode);
                        $("#partUnit_priceAdd").val(trData.unitPrice);
                        $('#partsModal').modal('hide');
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
                            var sheetId = "01-0" + deptId + "-";
                        } else {
                            var sheetId = "01-" + deptId + "-";
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
                            detectDeviceName: $('#detectAdd').val(),
                            depotId: $('#depotAdd').val(),
                            deviceTypeId: $('#deviceModelAdd').val(),
                            supplierId: $('#supplierNameAdd').val(),
                            deviceModelId: $('#deviceModelAdd').val(),
                            enabled: 1,
                            userName: user_name,
                            remark: $('#remarkAdd').val()
                        });
                        $.ajax({
                            url: config.basePath + '/deviceManage/detectManage/createDetect',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>探测站添加成功！</strong></span>');
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
                 * 验证单据参数是否为空
                 */
                function verifyPartsAdd() {
                    var partName = $('#part_nameAdd').val();
                    var partId = $('#part_idAdd').val();
                    if ($.trim(partName) != "" && $.trim(partId) != "") {
                        return true;

                    } else {
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
                            $("#alertMsgAdd").html("<font style='color:red'>配件不能为空</font>");
                            $("#alertMsgAdd").css('display', 'inline-block')
                            timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);

                            return false;
                        }
                        var params = JSON.stringify({
                            detectDeviceId: detectDeviceId,
                            partsId: $('#part_idAdd').val(),
                            partsSpecial: $('#part_specialAdd').val(),
                            enabled: 1,
                            ifMoved: 1
                        });
                        $.ajax({
                            url: config.basePath + '/deviceManage/detectManage/detectPartsCreate',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    $("#alertMsgAdd").html("<font style='color:red'>出厂编号已存在</font>");
                                    $("#alertMsgAdd").css('display', 'inline-block')
                                    timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                                } else {

                                    sheetDetailTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>探测站配件基本信息添加成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                    $('#addSheetDetailModal').modal('hide')

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
                            url: config.basePath + '/entryAndOut/preparePurchase/sheetInfoModify',
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
                            url: config.basePath + '/entryAndOut/preparePurchase/modify',
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
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据已审核！</strong></span>');
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
                        $('#warningSheetText').text('确定要删除该探测站（' + data.detectDeviceName + '）？');
                        $('#deleteDetectDeviceId').val(data.detectDeviceId);
                    });

                $('#btnPopSheetOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            detectDeviceId: $('#deleteDetectDeviceId').val(),
                            userName: user_id
                        });
                        $.ajax({
                            url: config.basePath + '/deviceManage/detectManage/delete',
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
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>探测站删除成功！</strong></span>');
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
                        $('#warningText').text('确定要删除该探测站配件（' + data.detectDeviceName+ '）？');
                        $('#deletePartId').val(data.detectPartsId);
                    });

                $('#btnPopOk').on('click',
                    function (e) {
                        e.preventDefault();
                        var params = JSON.stringify({
                            detectPartsId: $('#deletePartId').val(),
                        });
                        $.ajax({
                            url: config.basePath + '/deviceManage/detectManage/detectPartsDelete',
                            type: 'POST',
                            data: params,
                            contentType: 'application/json',
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
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

                /** 导出配件详情信息 */
                $("#sheetTable tbody").on('click', '#exportExcel',
                    function () {
                        var tr = $(this).closest('tr');
                        sheetTrData = sheetTable.row(tr).data();
                        sheet_id2 = sheetTrData.sheetId;
                        var params = $.param({
                            sheetId: sheet_id2
                        });
                        window.location.href = config.basePath + '/entryAndOut/preparePurchase/exportSheetInfo?' + params;
                    });
                /*******************************************************************************
                 *
                 *
                 */
                var setting = {
                    async: {
                        enable: true,
                        url: config.basePath + '/entryAndOut/preparePurchase/getPartsZtree',
                        type: 'get',
                        dataFilter: function (treeId, parentNode, childNodes) {
                            if (childNodes) {
                                for (var i = 0; i < childNodes.length; i++) {
                                    if (!childNodes[i]['parentId']) {
                                        childNodes[i]['parentId'] = 0;
                                    }
                                }
                            } else {
                                childNodes = [];
                            }
                            return childNodes;
                        }
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: 'id',
                            pIdKey: 'parentId',
                            rootPId: 0
                        },
                        key: {
                            name: 'funcName'
                        }
                    },
                    view: {
                        selectedMulti: false,
                    },
                    callback: {
                        onAsyncSuccess: function (event, treeId, treeNode, msg) {
                            var zTree = $.fn.zTree.getZTreeObj(treeId);
                            zTree.expandAll(false);
                        },
                        onClick: function (event, treeId, treeNode) {
                            var parentNode = treeNode;
                            var devicePartsName;
                            var deviceTypeName;
                            var deviceModelName;
                            var supplierName;
                            for (i = 0; i < 3; i++) {
                                var level = parentNode.funcLevel;
                                switch (level) {
                                    case 0:
                                        deviceTypeName = parentNode.funcName;
                                        break;
                                    case 1:
                                        supplierName = parentNode.funcName;
                                        break;
                                    case 2:
                                        deviceModelName = parentNode.funcName;
                                        break;
                                    case 3:
                                        devicePartsName = parentNode.funcName;
                                        break;
                                    default:
                                }
                                parentNode = parentNode.getParentNode();
                                if (parentNode == null) {
                                    break;
                                }
                            }
                            var params = $.param({
                                devicePartsName: devicePartsName,
                                deviceTypeName: deviceTypeName,
                                deviceModelName: deviceModelName,
                                supplierName: supplierName
                            })
                            // console.log(treeNode);
                        }
                    }
                };

                $.fn.zTree.init($("#funcTree"), setting, null);


                /**
                 * 定时隐藏alert框
                 */
                function hideTimeout(id, ms) {
                    var time = setTimeout(function () {
                            $("#" + id).hide();
                        },
                        ms)
                }

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
            });
    });