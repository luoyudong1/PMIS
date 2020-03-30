/**
 * 采购入库
 */
require(['../config'], function (config) {

    require(['datetimepicker'], function () {
        var date = new Date();
        date.setDate("1")
        date.setMonth(date.getMonth() - 1)
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

    require(['jquery', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin', 'zTree', 'zTreeExhide'], function ($, bootstrap, dataTable, CMethod) {

        var sheet_id = '';//查询的单号
        var verify_flag = '';//查询的审核状态
        var verifyFlag = '';
        var sheet_id2 = '';
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var deptId = window.parent.user.deptId // 所属部门id
        var roleName = window.parent.user.roleName // 所属部门id
        var sheetTrData = '';//保留点击的单号信息
        var partId = '';//保留点击的配件编码
        var partCode = '';//保留点击的配件出厂编码
        var devicePartsName = '';//保留点击的配件名称
        var deviceTypeName = '';//保留点击的配件类型
        var deviceModelName = '';//保留点击的型号
        var supplierName = '';//保留点击的厂家名称
        var supplier_name = '';//生产厂家
        var sourceStoreHouseName = '';//保留点击的厂家名称
        var partIdConfigList = '';//保留车间配件id配置
        var objectStoreHouseId = '';//保留点击的厂家名称
        var setting = ''
        var state=''
        var sourcePartCode = '';
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
         * 初始化新增类型
         * @returns {boolean}
         */
        function initAddType() {
    var addTypeoption1='<option value="1">新购配件</option>'
    var addTypeoption2='<option value="2">既有配件</option>'
    if(roleName.substring(0,2)=='车间'){
        $('#addType').append(addTypeoption1+addTypeoption2)
    }else if(roleName.substring(0,2)=='班组'){
        $('#addType').append(addTypeoption2)
    }else{
        return false;
    }
}


        /**
         * 初始化是否质保期
         * @returns {boolean}
         */
        function initWarrantyAdd() {
            $('#warrantyAdd').empty()
            var option='<option></option>'
            var option1='<option value="1">是</option>'
            var option2='<option value="0">否</option>'
            if(state==1){
                $('#warrantyAdd').append(option1)
            }else if(state==2){
                $('#warrantyAdd').append(option+option1+option2)
            }else{
                return false;
            }
        }
        //初始化新增类型
        initAddType()
        /**
         * 初始化备品库（目的仓库）
         * @returns {boolean}
         */
        getAllObjectStoreHouse();
        function getAllObjectStoreHouse() {
            var sourceParams='';
        if(roleName.substring(0,2)=='车间'){
             sourceParams = JSON.stringify({
                depotId: deptId,
                type: 13
            })
        }else if(roleName.substring(0,2)=='班组'){
            sourceParams = JSON.stringify({
                depotId: deptId,
                type: 6
            })
        }else{
            return false;
        }
        /**
         * 初始化源仓库下拉框
         */
        $.ajax({
            async: false,
            url: config.basePath + "/partsEntryAndOut/addParts/getAllObjectStoreHouse",
            data: sourceParams,
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            success: function (result) {
                for (var i = 0; i < result.data.length; i++) {
                   objectStoreHouseId= result.data[i].storehouseId
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
    }
        /**
         * 初始化配件编号id配置
         */
        $.ajax({
            async: false,
            url: config.basePath + "/partsEntryAndOut/addParts/getPartIpConfig",
            data: JSON.stringify({
                depotId:deptId
            }),
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            success: function (result) {
                partIdConfigList=result
            },
            error: function (result) {
                console.log(result);
            }
        });

    //执行获取备品库
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
         * 初始化入库单据
         */
        var sheetTable = dataTable('sheetTable', {
            bAutoWidth: false,
            ajax: {
                url: config.basePath + '/partsEntryAndOut/addParts/getAllSheets',
                type: "get",
                data: function (d) {
                    d.sheetId = "%" + $('#sheet_id').val() + "%";
                    d.receiptVerifyFlag = $('#verify_flag').val();
                    d.objectStoreHouseId=objectStoreHouseId
                    d.queryTime = $("#queryTime").val();
                    d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                }
            },
            columns: [
                {data: null},
                {data: 'sheetId'},
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
                {data: 'state',
                render:function (data) {
                    if(data==1){
                        return "新购"
                    }else if(data==2){
                        return "既有配件"
                    }else{
                        return "-";
                    }
                }},
                {data: 'receiptRemark'},
                {data: 'receiptOperatorName'},
                // {data: 'receiptVerifyName'},
                {
                    data: 'receiptVerifyFlag',
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
                    data: 'receiptVerifyDate',
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
                    targets: 10,
                    data: function (row) {
                        var str = '';
                        if (row.receiptVerifyFlag == 0 || row.receiptVerifyFlag == 3) {
                            str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                                + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                                + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
                        } else if (row.receipt_verify_flag == 1) {
                            str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
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
         * 初始化配件表格
         */
        var tblPartsInfo = dataTable('tblPartsInfo', {
            bAutoWidth: false,
            ajax: {
                url: config.basePath + '/partsEntryAndOut/addParts/getPartsByPartIdConfig',
                type: 'get',
                dataType: 'json',
                contentType: "application/json",
                data: function (d) {
                    d.depotId=deptId;

                }
            },
            columns: [{
                data: null
            },
                {
                    data: 'parts_code'
                },
                {
                    data: 'device_parts_name'
                },
                {
                    data: 'device_type_name'
                },
                {
                    data: 'device_model_name'
                },
                {
                    data: 'unit_price'
                },
                {
                    data: 'supplier_name'
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

        $('#sheetTable tbody').on('click', 'tr', function (e) {//获取当前单号
            if (sheetTable.data().any()) {
                $(this).addClass('selected').siblings().removeClass('selected');
                var tr = $(this).closest('tr');
                sheetTrData = sheetTable.row(tr).data();
                sheet_id2 = sheetTrData.sheetId;
                verifyFlag = sheetTrData.receiptVerifyFlag;
                sourceStoreHouseName = sheetTrData.supplierName;
                state=sheetTrData.state
                var params = $.param({
                    sheetId: sheet_id2
                });
                if (verifyFlag == 0 || verifyFlag == 3) {//已审核不能添加
                    $("#add_sheetDetail").show();
                } else {
                    $("#add_sheetDetail").hide();
                }
                sheetDetailTable.column(0).search('');
                sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/addParts/getAllSheetDetails?' + params).load();
            }
        });
        //显示修改配件单
        $('#sheetDetailTable tbody').on('click', 'tr',
            function (e) {
                if (sheetDetailTable.data().any()) {
                    $(this).addClass('selected').siblings().removeClass('selected');
                    var tr = $(this).closest('tr');
                    var sheetDetailTrData = sheetDetailTable.row(tr).data();
                    $('#part_nameModify').val(sheetDetailTrData.devicePartsName)
                    $('#part_idModify').val(sheetDetailTrData.partId)
                    $('#part_codeModify').val(sheetDetailTrData.partCode)
                    sourcePartCode = sheetDetailTrData.partCode
                    $('#remarkPartsModify').val(sheetDetailTrData.remark)

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
                    remark: $('#remarkPartsModify').val()
                });
                $.ajax({
                    url: config.basePath + '/partsEntryAndOut/addParts/sheetDetailModify',
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
                            sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/addParts/getAllSheetDetails?' + params).load();
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
            columns: [
                {data: null},
                {data: 'partId'},
                {data: 'devicePartsName'},
                {data: 'deviceModelName'},
                {data: 'partCode'},
                {data: 'deviceTypeName'},
                {data: 'unitPrice'},
                {data: 'supplierName'},
                {data: 'remark'}
            ],
            columnDefs: [
                {
                    targets: 9,
                    data: function (row) {
                        var str = '';
                        if (row.receiptVerifyFlag == 0 || row.receiptVerifyFlag == 3) {
                            str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改配件"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                        } else {
                            str += '';
                        }
                        return str;
                    }
                }
            ],
            ordering: false,
            paging: true,
            pageLength: 5,
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
        $("#sheetIdAdd").on('blur', function () {
            var part_id = $('#sheetIdAdd').val();
            if ($.trim(part_id) != "") {
                var params = $.param({
                    "sheetId": sheet_id
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
         * 添加单据
         */
        $('#addSheetModal').on('show.bs.modal', function () {
            var DateTime = new Date();
            // 单据ID为单据类型+部门id+序列号
            if (deptId < 10) {
                var sheetId = "22-0" + deptId + "-";
            } else {
                var sheetId = "22-" + deptId + "-";
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
            $('#operator_nameAdd').val(user_name);
            $('#remarkAdd').val('');
        });

        $("#btnAddSheetOk").on('click', function (e) {
            e.preventDefault();
            if($("#addType").val()==""||$("#addType").val()==null){
                $("#alertSheetMsgAdd").html("<font style='color:red'>新增类型为空</font>");
                $("#alertSheetMsgAdd").css('display','inline-block')
                CMethod.hideTimeout("alertSheetMsgAdd","alertSheetMsgAdd",5000);
                return false;
            }
            var params = JSON.stringify({
                sheetId: $('#sheetIdAdd').val(),
                supplierName: $('#supplierNameAdd option:selected').text(),
                addDate: $('#sheetDateAdd').val(),
                receiptOperatorId: user_id,
                receiptOperatorName: $('#operator_nameAdd').val(),
                receiptRemark: $('#remarkAdd').val(),
                sourceStoreHouseId:objectStoreHouseId,
                objectStoreHouseId:objectStoreHouseId,
                state:$('#addType').val(),
                depotId:deptId,
            });
            $.ajax({
                url: config.basePath + '/partsEntryAndOut/addParts/create',
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
        $('#addSheetDetailModal').on('show.bs.modal', function () {
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
            var partCode = $('#part_codeAdd').val();
            var partName = $('#part_nameAdd').val();
            var partId = $('#part_idAdd').val();
            var price=$('#partUnit_priceAdd').val()
            var warranty=$('#warrantyAdd').val()
            if ($.trim(partCode) == "" || $.trim(partName) == "" || $.trim(partId) == "") {
                $("#alertMsgAdd").html("<font style='color:red'>出厂编号不能为空</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                return false;

            }else if(state==1&&price==''){
                $("#alertMsgAdd").html("<font style='color:red'>价格不能为空</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                return false;
            } else if(warranty==''){
                $("#alertMsgAdd").html("<font style='color:red'>是否质保期不能为空</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                return false;
            } else {
                return true;
            }
        }
        $("#btnAddSheetDetailOk").on('click', function (e) {
            e.preventDefault();
            if (verifyPartsAdd() == false) {
                return false;
            }
            var params = JSON.stringify({
                sheetId: sheet_id2,
                partCode: $('#part_codeAdd').val(),
                partId: $('#part_idAdd').val(),
                unitPrice: $('#partUnit_priceAdd').val(),
                warranty:$('#warrantyAdd').val(),
                remark: $('#remarkPartsAdd').val()
            });
            $.ajax({
                url: config.basePath + '/partsEntryAndOut/addParts/SheetDetailCreate',
                type: "post",
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function (result) {
                    if (result.code != 0) {
                        $("#alertMsgAdd").html("<font style='color:red'>"+result.msg+"</font>");
                        $("#alertMsgAdd").css('display', 'inline-block')
                        CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                    } else {
                        var json = $.param({
                            sheetId: sheet_id2
                        });
                        sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/addParts/getAllSheetDetails?' + json).load();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
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
        $('#modifySheetModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#sheetIdModify').val(data.sheetId);
            loadDate("sheetDateModify", new Date());
            $('#remarkModify').val(data.remark);
        });

        $("#btnModifySheetOk").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                sheetId: $('#sheetIdModify').val(),
                addDate: $('#sheetDateModify').val(),
                receiptRemark: $('#remarkModify').val(),
            });
            $.ajax({
                url: config.basePath + '/partsEntryAndOut/addParts/modify',
                type: "post",
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
        $('#popSheetVerifyModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#warningSheetVerifyText').text('确定提交单据ID为:' + data.sheetId + '的单据吗？');
            $('#btnPopSheetVerifyOk').val(data.sheetId);
        });

        /**
         * 单据审核
         */
        $("#btnPopSheetVerifyOk").on('click', function (e) {
            e.preventDefault();
            if (sheetDetailTable.data().length < 1) {
                alert("配件为空，请先添加配件！")
                return;
            }
            var params = JSON.stringify({
                receiptOperatorId: user_id,
                receiptOperatorName: user_name,
                receiptVerifyFlag: 2,
                receiptVerifyDate: new Date(),
                sheetId: sheet_id2
            });
            $.ajax({
                url: config.basePath + '/partsEntryAndOut/addParts/modifyVerify',
                type: "post",
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        $('#add_sheetDetail').hide()
                        var json = $.param({
                            sheetId: sheet_id2
                        });
                        sheetDetailTable.ajax.url(config.basePath + '/partsEntryAndOut/addParts/getAllSheetDetails?' + json).load();
                        sheetTable.ajax.reload();
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
        $('#popSheetModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#warningSheetText').text('确定要删除该单据（' + data.sheetId + '）？');
            $('#deleteSheetId').val(data.sheetId);
        });

        $('#btnPopSheetOk').on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                sheetId: $('#deleteSheetId').val()
            });
            $.ajax({
                url: config.basePath + '/partsEntryAndOut/addParts/delete',
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
                        $.fn.zTree.destroy()
                        $.fn.zTree.init($("#funcTree"), setting, null);
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
        $('#popModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetDetailTable.row(tr).data();
            partCode = data.partCode;
            partId = data.partId;
            $('#warningText').text('确定要删除该配件（' + data.partCode + '）？');
            $('#deletePartCode').val(data.partCode);
        });

        $('#btnPopOk').on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                sheetId: sheet_id2,
                partCode: $('#deletePartCode').val(),
                partId: partId
            });
            $.ajax({
                url: config.basePath + '/partsEntryAndOut/addParts/SheetDetailDeleteByCode',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        sheetDetailTable.ajax.reload();
                        $.fn.zTree.destroy()
                        $.fn.zTree.init($("#funcTree"), setting, null);
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件删除成功！</strong></span>');
                        $("#infoAlert").show();
                        hideTimeout("infoAlert", 2000);
                    }
                }
            });

        });

        /**
         * addSheetDetailModal点击配件      弹出配件库窗口
         */
        $("#part_nameAdd").on('click', function (e) {
            $('#partsModal').modal('show');
            tblPartsInfo.ajax.reload();
        });


        /**
         * 配件库查询
         */
        $("#btnQueryparts").on('click', function (e) {
            partId = '%' + $("#query_part_id").val() + '%';
            partCode = '%' + $("#query_part_code").val() + '%';
            deviceTypeName = '';
            deviceModelName = '';
            devicePartsName = '';
            supplierName = '';
            tblPartsInfo.ajax.reload()
        });

        /**
         * 点击当前配件 获取配件
         */
        $("#tblPartsInfo tbody").on("dblclick", "tr", function () {
            $(this).addClass('success').siblings().removeClass('success');
            var trData = tblPartsInfo.row(this).data();
            $("#parts_id_select").val(trData.parts_code);
            //$("#part_codeAdd").val(trData.part_ode);
            $("#part_nameAdd").val(trData.device_parts_name);
            $("#part_idAdd").val(trData.parts_code);
            //$("#partUnit_priceAdd").val(trData.unitPrice);
            supplier_name = trData.supplier_name;
            deviceModelName = trData.device_model_name;
            getMaxPartId()
            $('#partsModal').modal('hide');
        });

        function getMaxPartId() {
            $.ajax({
                url: config.basePath + '/stock/stockInfo/getMaxPartId',
                type: "GET",
                data: {
                    "part_id": $("#part_idAdd").val(),
                    "supplier_name": supplier_name,
                    "depot_id": deptId,
                    "device_model_name": deviceModelName,
                },
                success: function (result) {
                    $("#part_idAdd").val(result);
                },
                error: function (result) {
                    console.log(result);
                }
            });
        }

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
         * 添加监听input  part_codeAdd值的改变事件
         */
        $('#part_codeAdd').bind('input propertychange', function () {
            var tvalmum;
            //统计input输入字段的长度
            tvalnum = $(this).val().length;
            //如果大于30个字直接进行字符串截取
            if (tvalnum > 30) {
                var tval = $(this).val();
                tval = tval.substring(0, 30);
                $(this).val(tval);
                alert('长度超过限制！');
            }
        });
        $('#add_sheetDetail').click(function () {
               initPartIdconfig()
               initWarrantyAdd();
        })
        /** 导出配件详情信息*/
        $("#sheetTable tbody").on('click', '#exportExcel', function () {
            var tr = $(this).closest('tr');
            sheetTrData = sheetTable.row(tr).data();
            sheet_id2 = sheetTrData.sheetId;
            var params = $.param({
                sheetId: sheet_id2
            });
            window.location.href = config.basePath + '/partsEntryAndOut/addParts/exportSheetInfo?' + params;
        });


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
                        tblPartsInfo.ajax.url(config.basePath + '/system/partsManage/getAllParts?' + params).load();
                        // console.log(treeNode);
                    }
                }
            };

            $.fn.zTree.init($("#funcTree"), setting, null);
            //initPartIdconfig();
            function initPartIdconfig() {
                var ztree = $.fn.zTree.getZTreeObj('funcTree');
                if (ztree == null || ztree == '') {
                    alert("树形列表未初始化")
                    $.fn.zTree.init($("#funcTree"), setting, null);
                }
                var list = ztree.getNodesByParam('funcLevel', 2);
                for (var i = 0; i < list.length; i++) {
                    var parentNode=list[i].getParentNode()
                    var flag=0;
                    for (var j = 0; j < partIdConfigList.length; j++) {
                        if (list[i].funcName == partIdConfigList[j].deviceModelName
                            &&parentNode.funcName==partIdConfigList[j].supplierName) {
                            ztree.showNodes(list[i].children)
                            ztree.showNode(list[i])
                            flag=1;
                            break;
                        }
                    }
                    if(flag==0) {
                        ztree.hideNodes(list[i].children)
                        ztree.hideNode(list[i])
                    }

                }
            }

        /**
         * 定时隐藏alert框
         */
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