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
        // var roles= window.parent.user.roles; // 登录人角色信息
        var deptId = window.parent.user.deptId // 所属部门id
        var sourceStoreHouseId = ''//仓库id
        var detectDeviceId = ''//探测站ID
        var partId = ''//配件编号
        var partCode = ''//配件出厂编码
        var treeNodeId = ''//配件出厂编码
        //初始化时间查询框
        CMethod.initDatetimepicker('queryTimeBegin', null);
        CMethod.initDatetimepicker('queryTimeEnd', null);
        //隐藏探测站下拉框

        //初始化仓库查询项
        var sourceParams = JSON.stringify({
            depotId: deptId
        })
        /**
         *  /**
         * 初始化源仓库下拉框
         */
        $.ajax({
            async: false,
            url: config.basePath + "/stock/stockInfo/getStorehouseByDepotId",
            data: sourceParams,
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            success: function (result) {
                for (var i = 0; i < result.data.length; i++) {
                    $("#storehouse_id_sel").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                }
                sourceStoreHouseId = $("#storehouse_id_sel").val()
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
        var stockInfoTable = dataTable('partsInfoTable', {
            ajax: {
                url: config.basePath + '/stock/stockInfo/getPartsZtreeDetails',
                type: 'GET',
                data: function (d) {
                    d.storeHouseId = sourceStoreHouseId;
                    d.partsZtreeId = treeNodeId;
                    d.detectDeviceId = detectDeviceId;
                    d.partCode = partCode;
                    d.partId = partId;
                },
            },
            columns: [
                {data: null},
                {data: 'partIdSeq'},
                {data: 'devicePartsName'},
                {data: 'factoryPartsCode'},
                {data: 'deviceModelName'},
                {data: 'supplierName'},
                {data: 'purchasePrice'},
                {data: 'storehouseName'},
                {data: 'detectDeviceName'},
                {
                    data: 'createTime',
                    render: function (data) {
                        var str = '-';
                        if (data > 0) {
                            str = formatDateBy(data, 'yyyy-MM-dd')
                        }
                        return str;
                    }
                },
                {
                    data: 'updateTime',
                    render: function (data) {
                        var str = '-';
                        if (data > 0) {
                            str = formatDateBy(data, 'yyyy-MM-dd')
                        }
                        return str;
                    }
                },


            ],
            columnDefs: [
                {
                    targets: 11,
                    data: function (row) {
//                        var operator = '<a class="modifyUser btn btn-info btn-xs" data-toggle="modal" href="#modifyStockInfoModal" title="修改配件"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
//                            + '&nbsp;&nbsp;' + '<a class="deleteUser btn btn-danger btn-xs" data-toggle="modal" href="#popStockInfoModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                        return "-";
                    }
                }
            ],
            ordering: false,
            paging: true,
            pageLength: 10,
            serverSide: false,
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
            //stockInfoTable.ajax.reload();
            sourceStoreHouseId = $("#storehouse_id_sel").val()
            detectDeviceId = $("#detect_device").val()
            partId = $("#part_id_sel").val()
            partCode = $("#part_code_sel").val()
            treeNodeId = ''
            stockInfoTable.ajax.reload()
            //$('#funcTree').empty()
            $.fn.zTree.destroy()
            $.fn.zTree.init($("#funcTree"), getSetting(), null);
        });

        /** 响应刷新按钮 */
        $('#btnRefresh').click(function () {
            $("#part_id_sel").val("");
            $("#part_name_sel").val("");
            location.reload();
        });

        /**
         * 删除配件库存信息
         */
        $('#popStockInfoModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = stockInfoTable.row(tr).data();
            $('#warningText').text('确定要删除 ' + data.part_name + '(' + data.factoryPartsCode + ') 的配件信息？');
            $('#deletePartCode').val(data.partIdSeq + "-" + data.factoryPartsCode);
        });

        $('#btnPopOk').on('click', function (e) {
            clearTimeout(timer);
            e.preventDefault();
            let strs = $('#deletePartCode').val().split("-");
            var params = $.param({
                "part_id_seq": strs[0],
                "part_code": strs[1]
            });
            $.ajax({
                url: config.basePath + '/stock/stockInfo/delete',
                type: 'POST',
                data: params,
                dataType: 'json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        //stockInfoTable.ajax.reload();
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
            CMethod.initStorehouseInfo(config, 'position_storehouse_add');
        });

        /**选择配件信息键盘按下事件*/
        $("#part_id_add").on('click', function (e) {
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
                $("#part_id_add").val(trData.part_name + "(" + trData.part_id + ")");
                $("#choosePartInfoModal").modal("hide");
            }
        })

        /** 配件选择确认按钮*/
        $("#btnChoosePartOk").on('click', function (e) {
            clearTimeout(timer);
            let trData = partInfoTable.row(".success_query").data();
            if (trData) {
                $("#part_id_add").val(trData.part_name + "(" + trData.part_id + ")");
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
                $("#alertMsgAdd").html("<font style='color:red'>请选择配件类别</font>");
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

            let params = {
                part_id: part_id_add,
                unit_price: unit_price,
                storehouse_id: $('#position_storehouse_add').val(),
                entry_type: 0,
                remark: $('#remark_add').val(),
                part_code: part_code

            };
            $.ajax({
                url: config.basePath + '/stock/stockInfo/create',
                type: "post",
                data: params,
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
            $("#part_id_modify").val(data.part_id_seq);
            $("#part_code_modify").val(data.part_code);
            $("#part_name_modify").val(data.part_name);
            $("#unit_price_modify").val(data.unit_price);
            $('#part_code_modify').val(data.part_code);
            $("#position_storehouse_modify").val(data.storehouse_id);
            $('#remark_modify').val(data.remark);
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

            let params = {
                part_code: part_code,
                unit_price: $('#unit_price_modify').val(),
                storehouse_id: $('#position_storehouse_modify').val(),
                remark: $('#remark_modify').val(),
                part_id_seq: $.trim($("#part_id_modify").val())
            };

            $.ajax({
                url: config.basePath + '/stock/stockInfo/modify',
                type: 'POST',
                data: params,
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

        function getSetting() {


            var setting = {
                async: {
                    enable: true,
                    url: config.basePath + '/stock/stockInfo/getPartsZtree',
                    type: 'get',
                    otherParam: {"storeHouseId": sourceStoreHouseId, "detectDeviceId": detectDeviceId},
                    dataFilter: function (treeId, parentNode, childNodes) {
                        if (childNodes) {
                            for (var i = 0; i < childNodes.length; i++) {
                                if (!childNodes[i]['parentId']) {
                                    childNodes[i]['parentId'] = 0;
                                }
                                if (childNodes[i]['count'] != null) {
                                    console.log(childNodes[i]['count']);
                                    childNodes[i]['funcName'] = childNodes[i]['funcName'] + "(" + childNodes[i]['count'] + "个)";
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
                        partId: 'partId',
                        pIdKey: 'parentId',
                        rootPId: 0
                    },
                    key: {
                        name: "funcName"
                    }
                },
                view: {
                    selectedMulti: false,
                },
                callback: {
                    onAsyncSuccess: function (event, treeId, treeNode, msg) {
                        var zTree = $.fn.zTree.getZTreeObj(treeId);
                        zTree.expandAll(true);
                    },
                    onClick: function (event, treeId, treeNode) {
                        if (!treeNode.isParent) {
                            treeNodeId = treeNode.id
                            partId = '';
                            partCode = ''
                            stockInfoTable.ajax.reload()
                        }
                    }
                }
            };
            return setting;
        }

        $.fn.zTree.init($("#funcTree"), getSetting(), null);

        /** 导出配件详情信息*/
        $("#exportExcel").on('click', function () {
            var storeHouseId = $("#storehouse_id_sel").val()
            var detectDeviceId = $("#detect_device").val()
            var params = $.param({
                storeHouseId: storeHouseId,

                detectDeviceId: detectDeviceId
            });
            window.location.href = config.basePath + '/stock/stockInfo/exportStockInfos?' + params;
        });
        /** 导出配件详情信息*/
        $("#exportCountExcel").on('click', function () {
            var storeHouseId = $("#storehouse_id_sel").val()
            window.location.href = config.basePath + '/stockInfo/exportCount/' + storeHouseId;
        });
        stockInfoTable.on('draw.dt', function () {
            //给第一列编号
            stockInfoTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
    });
});