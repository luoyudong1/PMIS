/**
 * 单个配件信息管理Js
 */
require(['../config'], function (config) {
    //
    require(['jquery', 'popper', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/dateFormat', 'metisMenu', 'slimscroll', 'pace', 'inspinia', 'zTree'], function ($, Popper, Bootstrap, dataTable, CMethod) {
        //入库方式（1：采购入库、2：生产入库、3：调拨入库、0:手动添加）
        const entryTypes = ['手动添加', '采购入库', '生产入库', '调拨入库'];
        let screenHeight = screen.height * 0.5 + 'px';
        let timer = 0;
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var roleId= window.parent.user.roleId; // 登录人角色信息
        var deptId = window.parent.user.deptId // 所属部门id
        var sourcePartCode = '';//源出厂编码
        var supplier_name = '';//生产厂家
        var deletePartId = '';//要删除的partId
        var deletePartCode = '';//要删除的partCode
        //初始化时间查询框
        CMethod.initDatetimepicker('queryTimeBegin', null);
        CMethod.initDatetimepicker('queryTimeEnd', null);
        //初始化备品库仓库，新增配件用
        var sourceParams = JSON.stringify({
            depotId: deptId,
        })
        /**
         *  /**
         * 初始化备品库仓库下拉框，
         */
        $.ajax({
            async: false,
            url: config.basePath + "/stock/stockInfo/getAllSourceStoreHouse",
            data: sourceParams,
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            success: function (result) {
                $("#position_storehouse_add").empty()
                for (var i = 0; i < result.data.length; i++) {

                    $("#position_storehouse_add").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
        //初始化仓库查询项
        /**
         *  /**
         * 初始化源仓库下拉框
         */
        $.ajax({
            async: false,
            url: config.basePath + "/stock/stockInfo/getAllSourceStoreHouse",
            data: sourceParams,
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            success: function (result) {
                for (var i = 0; i < result.data.length; i++) {
                    $("#storehouse_id_sel").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                }
            },
            error: function (result) {
                console.log(result);
            }
        });
        //仓库改变事件
        $('#storehouse_id_sel').change(function () {
            var storeHouseName = $('#storehouse_id_sel option:selected').text()
            var storehouseId = $('#storehouse_id_sel option:selected').val()
            $("#detect_device").empty()
            if (storeHouseName.substr(storeHouseName.length - 3) == "使用库") {

                var detectParams = JSON.stringify({
                    storehouseId: storehouseId,
                })
                $.ajax({
                    url: config.basePath
                        + "/stock/stockInfo/getAllDetectByDepot",
                    data: detectParams,
                    type: "POST",
                    dataType: 'json',
                    contentType: 'application/json',
                    success: function (result) {
                        for (var i = 0; i < result.data.length; i++) {
                            $("#detect_device")
                                .append(
                                    '<option value="'
                                    + result.data[i].detectDeviceId
                                    + '">'
                                    + result.data[i].detectDeviceName
                                    + '</option>');
                        }

                    },
                    error: function (result) {
                        console.log(result);
                    }
                })
                $('#detect_device').removeAttr("disabled");

            } else {
                $('#detect_device').attr("disabled", "disabled");
            }

        })
        //配件详情信息列表
        var stockInfoTable = dataTable('tblStockInfo', {
            ajax: {
                url: config.basePath + '/stock/stockInfo/list',
                data: {
                    //条件查询参数
                    "storehouse_id": function () {
                        return $("#storehouse_id_sel").val()
                    },
                    "detect_device_id": function () {
                        return $("#detect_device").val()
                    },
                    "part_id": function () {
                        return $("#part_id_sel").val().trim()
                    },
                    "part_code": function () {
                        return $("#part_code_sel").val().trim()
                    },
                    "begin_time": function () {
                        return $("#queryTimeBegin").val() + " 00:00:00"
                    },
                    "end_time": function () {
                        return $("#queryTimeEnd").val() + " 23:59:59"
                    },
                    "enabled": function () {
                        return $("input[id='enSelForbidPart']:checked").val() == 1 ? 1 : 0
                    }
                }
            },
            columns: [
                {data: null},
                {data: 'partIdSeq'},
                {data: 'factoryPartsCode'},
                {data: 'devicePartsName'},
                {data: 'deviceModelName'},
                {data: 'supplierName'},
                {data: 'purchasePrice'},
                {data: 'sheetId'},
                {data: 'storehouseName'},
                {
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
                    data:'assetAttributesName'
                },

            ],
            columnDefs: [
                {
                    targets: 13,
                    data: function (row) {
                        var operator ='';
                        operator = '<a class="modifyUser btn btn-info btn-xs" data-toggle="modal" href="#modifyStockInfoModal" title="修改配件"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;';
                        if(roleId==1||roleId==2||roleId==6||roleId==9) {
                            operator += '&nbsp;&nbsp;' + '<a class="deleteUser btn btn-danger btn-xs" data-toggle="modal" href="#popStockInfoModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return operator;
                    }
                }
            ],
            ordering: true,
            paging: true,
            pageLength: 30,
            serverSide: false,
            scrollX: false,
            scrollY: screenHeight,
            scrollCollapse: "true",
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function (cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });

        //配件信息列表
        var partInfoTable = dataTable('tblPartInfo', {
            bAutoWidth: false,
            ajax: {
                url: config.basePath + '/system/partsManage/getAllParts',
                data: {
                    //条件查询参数
                    "part_code": function () {
                        return $("#part_code_sel").val().trim()
                    },
                }
            },
            columns: [
                {data: null},
                {data: 'parts_code'},
                {data: 'device_parts_name'},
                {data: 'device_type_name'},
                {data: 'device_model_name'},
                {data: 'supplier_name'},
                {data: 'unit_price'}
            ],
            columnDefs: [
                {}
            ],
            ordering: false,
            paging: true,
            pageLength: 10,
            serverSide: true,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function (cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });


        /**跟据条件查询 */
        $('#btnQuery').on('click', function (e) {
            stockInfoTable.ajax.reload();
            //detectDeviceInfoTable.ajax.reload();
        });

        /** 响应刷新按钮 */
        $('#btnRefresh').click(function () {
            $("#part_id_sel").val("");
            $("#part_code_sel").val("");
            location.reload();
        });

        /**
         * 删除配件库存信息
         */
        $('#popStockInfoModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = stockInfoTable.row(tr).data();
            $('#warningText').text('确定要删除 ' + data.devicePartsName + '(' + data.factoryPartsCode + ') 的配件信息？');
           deletePartId=data.partIdSeq;
           deletePartCode=data.factoryPartsCode;
        });

        $('#btnPopOk').on('click', function (e) {
            clearTimeout(timer);
            e.preventDefault();
            var params = JSON.stringify({
                "partIdSeq": deletePartId,
                "factoryPartsCode": deletePartCode
            });
            $.ajax({
                url: config.basePath + '/stock/stockInfo/delete',
                type: 'POST',
                data: params,
                dataType: 'json',
                contentType: 'application/json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        stockInfoTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息删除成功！</strong></span>');
                        $("#infoAlert").show();
                        timer = CMethod.hideTimeout("alertMsg", "infoAlert", 2000);
                    }
                }
            });
        });

        /** 添加配件信息*/
        $('#addStockInfoModal').on('show.bs.modal', function (e) {
            $("#part_id_add").val("");
            $("#unit_price_add").val("");
            $("#part_code_add").val("");
            //CMethod.initStorehouseInfo(config,'position_storehouse_add');
        });

        /**选择配件信息键盘按下事件*/
        $("#part_name_add").on('click', function (e) {
            partInfoTable.ajax.reload();
            $("#choosePartInfoModal").modal('show');
        });

        /**选择配件信息时查询按钮*/
        $("#btnQueryPartInfo").on('click', function (e) {
            partInfoTable.ajax.reload();
        })
        /**选择配件信息时回车确认*/
        $("#part_name_add_sel").on('keydown', function (e) {
            if (e.keyCode == 13) {
                partInfoTable.ajax.reload();
            }
        });

        /** 配件信息表点击事件*/
        $("#tblPartInfo tbody").on('click', 'tr', function (e) {
            if (partInfoTable.data().any()) {
                $(this).addClass('success_query').siblings().removeClass('success_query'); // 单选
            }
        })

        /** 配件信息表双击点击事件*/
        $("#tblPartInfo tbody").on('dblclick', 'tr', function (e) {
            if (partInfoTable.data().any()) {
                let trData = partInfoTable.row(".success_query").data();
                $("#part_id_add").val(trData.parts_code);
                $("#part_name_add").val(trData.device_parts_name);
                $("#part_model_add").val(trData.device_model_name);
                $("#unit_price_add").val(trData.unit_price);
                supplier_name = trData.supplier_name;
                getMaxPartId()
                $("#choosePartInfoModal").modal("hide");
            }
        })

        function getMaxPartId() {
            $.ajax({
                url: config.basePath + '/stock/stockInfo/getMaxPartId',
                type: "GET",
                data: {
                    "part_id": $("#part_id_add").val(),
                    "supplier_name": supplier_name,
                    "device_model_name": $("#part_model_add").val()
                    ,
                    "depot_id": deptId
                },
                success: function (result) {
                    $("#part_id_add").val(result);
                },
                error: function (result) {
                    console.log(result);
                }
            });
        }

        /** 配件选择确认按钮*/
        $("#btnChoosePartOk").on('click', function (e) {
            clearTimeout(timer);
            let trData = partInfoTable.row(".success_query").data();
            if (trData) {
                $("#part_id_add").val(trData.part_name + "(" + trData.parts_code + ")");
                $("#choosePartInfoModal").modal("hide");
            } else {
                $("#alertMsgPart").html("<font style='color:red'>请选择配件</font>");
                $("#alertMsgPart").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgPart", "alertMsgPart", 3000);
            }
        });

        /**验证是否是数字类型*/
        $("#unit_price_add").on("blur", function (e) {
            clearTimeout(timer);
            let unit_price_add = $("#unit_price_add").val();
            if (CMethod.checkNumber(unit_price_add)) {
                $("#alertMsgAdd").html("");
                $("#alertMsgAdd").css('display', 'none')
            } else {
                $("#alertMsgAdd").html("<font style='color:red'>库存单价请输入正数</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 3000);
            }
        });


        /**验证出厂编号是否为空*/
        $("#part_code_add").on("blur", function (e) {
            clearTimeout(timer);
            let part_code = $.trim($("#part_code_add").val());
            if (CMethod.checkIsNotBlank(part_code)) {
                $("#alertMsgAdd").html("");
                $("#alertMsgAdd").css('display', 'none')
            } else {
                $("#alertMsgAdd").html("<font style='color:red'>出厂编号不能为空</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
            }
        });

        /**添加物资详情确认*/
        $("#btnAddOk").on('click', function (e) {
            e.preventDefault();
            clearTimeout(timer);
            let part_id_add = $.trim($("#part_id_add").val());
            if (!CMethod.checkIsNotBlank(part_id_add)) {
                $("#alertMsgAdd").html("<font style='color:red'>配件编号为空，请选择正确的配件类别</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                return false;
            } else {
                $("#alertMsgAdd").html("");
                $("#alertMsgAdd").css('display', 'none')
            }
            part_id_add = part_id_add.substring(part_id_add.indexOf('(') + 1, part_id_add.indexOf(')'));
            let part_code = $.trim($('#part_code_add').val());
            if (!CMethod.checkIsNotBlank(part_code)) {
                $("#alertMsgAdd").html("<font style='color:red'>出厂编号不能为空</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                return false;
            } else {
                $("#alertMsgAdd").html("");
                $("#alertMsgAdd").css('display', 'none')
            }
            let unit_price = $.trim($('#unit_price_add').val());
            if (!CMethod.checkIsNotBlank(unit_price)) {
                $("#alertMsgAdd").html("<font style='color:red'>配件单价不能为空</font>");
                $("#alertMsgAdd").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgAdd", "alertMsgAdd", 5000);
                return false;
            } else {
                $("#alertMsgAdd").html("");
                $("#alertMsgAdd").css('display', 'none')
            }

            let params = JSON.stringify({
                partIdSeq: $("#part_id_add").val(),
                storehouseId: $('#position_storehouse_add').val(),
                factoryPartsCode: part_code,
                enabled: 1,
                depotId:deptId

            });
            $.ajax({
                url: config.basePath + '/stock/stockInfo/create',
                type: "post",
                data: params,
                dataType: 'json',
                contentType: 'application/json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        stockInfoTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息添加成功！</strong></span>');
                        $("#infoAlert").show();
                        CMethod.hideTimeout("alertMsg", "infoAlert", 2000);
                    }
                }
            });
        });

        /**修改配件信息*/
        $('#modifyStockInfoModal').on('show.bs.modal', function (e) {
            CMethod.initStorehouseInfo(config, 'position_storehouse_modify');
            $("#unit_price_modify").val("");
            $('#remark_modify').val("");
            $('#part_code_modify').val("");
            var tr = $(e.relatedTarget).parents('tr');
            var data = stockInfoTable.row(tr).data();
            $("#part_id_modify").val(data.partIdSeq);
            $("#part_code_modify").val(data.factoryPartsCode);
            sourcePartCode = data.factoryPartsCode
            $("#part_name_modify").val(data.devicePartsName);
            $("#unit_price_modify").val(data.unitPrice);
            //$("#position_storehouse_modify").val(data.storehouseId);
            //$('#remark_modify').val(data.remark);
        });

        /**验证是否是数字类型*/
        $("#unit_price_modify").on("blur", function (e) {
            clearTimeout(timer);
            let unit_price_add = $("#unit_price_modify").val();
            if (CMethod.checkNumber(unit_price_add)) {
                $("#alertMsgModify").html("");
                $("#alertMsgModify").css('display', 'none')
            } else {
                $("#alertMsgModify").html("<font style='color:red'>库存单价请输入正数</font>");
                $("#alertMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 3000);
            }
        });

        $("#btnModifyOk").on('click', function (e) {
            e.preventDefault();
            clearTimeout(timer);
            let part_code = $.trim($('#part_code_modify').val());
            if (!CMethod.checkIsNotBlank(part_code)) {
                $("#alertMsgModify").html("<font style='color:red'>出厂编号不能为空</font>");
                $("#alertMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                return false;
            } else {
                $("#alertMsgModify").html("");
                $("#alertMsgModify").css('display', 'none')
            }
            let unit_price = $.trim($('#unit_price_modify').val());
            if (!CMethod.checkIsNotBlank(unit_price)) {
                $("#alertMsgModify").html("<font style='color:red'>配件单价不能为空</font>");
                $("#alertMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertMsgModify", "alertMsgModify", 5000);
                return false;
            } else {
                $("#alertMsgModify").html("");
                $("#alertMsgModify").css('display', 'none')
            }

            let params = JSON.stringify({
                partIdSeq: $("#part_id_modify").val(),
                //unit_price: unit_price,
                //storehouseId: $('#position_storehouse_modify').val(),
                //entry_type : 0,
                //remark : $('#remark_add').val(),
                factoryPartsCode: $("#part_code_modify").val(),
                sourcePartCode: sourcePartCode

                //enabled:1
            })


            $.ajax({
                url: config.basePath + '/stock/stockInfo/modify',
                type: 'POST',
                data: params,
                dataType: 'json',
                contentType: 'application/json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        stockInfoTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件详细信息修改成功！</strong></span>');
                        $("#infoAlert").show();
                        CMethod.hideTimeout("alertMsg", "infoAlert", 2000);
                    }
                }
            });
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
                    partInfoTable.ajax.url(config.basePath + '/system/partsManage/getAllParts?' + params).load();
                    // console.log(treeNode);
                }
            }
        };

        $.fn.zTree.init($("#funcTree"), setting, null);


        /** 导出配件详情信息*/
        $("#exportExcel").on('click', function () {
            var params = $.param({
                storehouse_id: $("#storehouse_id_sel").val(),
                part_id: $("#part_id_sel").val().trim(),
                part_code: $("#part_code_sel").val().trim(),
                begin_time: $("#queryTimeBegin").val() + " 00:00:00",
                end_time: $("#queryTimeEnd").val() + " 23:59:59",
                enabled: $("input[id='enSelForbidPart']:checked").val() == 1 ? 1 : 0
            });
            console.log(params);
            window.location.href = config.basePath + '/stock/stockInfo/exportStockInfos?' + params;
        });


    });
});