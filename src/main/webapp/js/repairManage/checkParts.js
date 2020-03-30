/**
 * 所内返修
 */
require(['../config'], function (config) {
    //
    require(['jquery', 'popper', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/dateFormat', 'metisMenu', 'slimscroll', 'pace', 'inspinia', 'zTree'], function ($, Popper, Bootstrap, dataTable, CMethod) {
        //入库方式（1：采购入库、2：生产入库、3：调拨入库、0:手动添加）
        let screenHeight = screen.height * 0.5 + 'px';
        let timer = 0;
        var sheet_id = '';// 查询的单号
        var sheetId = '';// 查询的单号
        var verify_flag = '';// 查询的审核状态
        var newDate = new Date();
        var sendVerifyFlag = '';
        var sheet_id2 = '';
        var partId = '';//保留点击的配件partId
        var partCode = '';//保留点击的配件partCode
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var deptId = window.parent.user.deptId; // 登录人部门id
        var sheetTrData = '';// 保留点击的单号信息
        var sourceStoreHouseName = '';// 保留点击的单号源仓库名称
        var sourceStoreHouseId = 7;// 保留点击的单号源仓库id
        var sheetTrData = '';// 保留点击的单号信息
        var sheetDetailTableTrData = '';//保留点击的配件检修记录信息
        var sheetDetailParams = "";
        var copyMachineStartTime = "";
        var copyMachineEndTime = "";
        var repaireState = "";
        var scrapReason = '';
        var date = new Date();
        var newDate = new Date("");
        date.setDate("1");
        date.setMonth(date.getMonth() - 1)
        //初始化时间查询框
        var format = 'Y-m-d H:i:s';
        CMethod.initDatetimepicker('queryTimeBegin', date);
        CMethod.initDatetimepicker('queryTimeEnd', newDate);
        CMethod.initDatetimepicker('queryTime', date);
        CMethod.initDatetimepicker('queryTime2', newDate);
        CMethod.initDatetimepickerWithSecond('copyMachineStartTime', newDate, format);
        CMethod.initDatetimepickerWithSecond('copyMachineEndTime', newDate, format);
        //初始化仓库查询项

        /**
         * 查询
         */
        $('#btn-search').click(function (e) {
            sheetTable.ajax.reload();
            sheetDetailTable.column(0).search('');
            sheetDetailTable.clear().draw();
            sheetId = '';
            $.fn.zTree.destroy()
            $.fn.zTree.init($("#funcTree"), setting, null);
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

        // 响应刷新按钮
        $('#btnRefresh').click(function () {
            $("#part_id_sel").val("");
            $("#part_name_sel").val("");
            location.reload();
        });
        $('#repaireState').change(function () {
            initOrClearCheckParts()
        })

        function initOrClearCheckParts() {
            var repaireState = $('#repaireState').val()
            if (repaireState == "1") {//合格
                var params = {
                    sheetId: sheet_id2,
                    partId: partId,
                    repaireState: repaireState
                }
                $.ajax({
                    url: config.basePath
                        + '/repairManage/check/getCheckPrice',
                    data: params,
                    type: 'get',
                    success: function (result) {
                        if (result != null && result > 0) {
                            $('#checkedPrice').val(result)
                        }
                    }
                })
                $('#scrapReason').attr("disabled", "disabled");
                $('#scrapReason').val('')
            } else if (repaireState == "2") {//报废
                $('#checkedPrice').val('')
                $('#scrapReason').removeAttr("disabled");
            } else {//不合格
                $('#checkedPrice').val('')
                $('#scrapReason').attr("disabled", "disabled");
                $('#scrapReason').val('')
            }
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
                        + '/repairManage/check/getAllSheets',
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
                        data: 'receiptRemark'
                    },
                    {
                        data: 'receiptOperatorName'
                    },
                    {
                        data: 'receiptVerifyName'
                    },
                    {
                        data: 'receiptVerifyFlag',
                        render: function (data) {
                            var str = "-";
                            if (data == 0 || null) {
                                str = '<span style="color:red;font-weight:bold;">待检测</span>';
                            } else if (data == 1) {
                                str = '<span style="color:blue;font-weight:bold;">检测审核中</span>';
                            } else if (data == 2) {
                                str = '<span style="color:black;font-weight:bold;">检测审核完成</span>';
                            }
                            return str;
                        }
                    },
                    {
                        data: 'receiptVerifyDate',
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
                        if (row.receiptVerifyFlag == 0) {
                            str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交审核" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
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
                    var startIndex = api.context[0]._iDisplayStart;// 获取到本页开始的条数
                    api.column(0).nodes().each(
                        function (cell, i) {
                            cell.innerHTML = startIndex
                                + i + 1;
                        });
                },
            });

        /*******************************************************
         * 单据点击事件
         *
         *
         *************************************************/
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
                        receiptVerifyFlag = sheetTrData.sendVerifyFlag;
                        sourceStoreHouseName = sheetTrData.sourceStorehouseName;
                        sheetDetailParams = $.param({
                            sheetId: sheet_id2
                        });

                        sheetDetailTable.ajax.url(config.basePath + '/repairManage/check/getAllSheetDetails?' + sheetDetailParams).load();

                    }
                });
        /**
         * 获取检修记录单
         */
        var sheetDetailTable = dataTable(
            'sheetDetailTable',
            {
                bAutoWidth: false,
                ajax: {
                    type: 'get',
                    data: "",
                    url: config.basePath
                        +
                        '/repairManage/check/getPartsZtreeDetails'

                },
                columns: [{
                    data: null
                }, {
                    data: 'sheetId'
                }, {
                    data: 'devicePartsName'
                }, {
                    data: 'partCode'
                }, {
                    data: 'partId'
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
                    targets: 17,
                    data: function (row) {
                        var str = '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改检修记录"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;';
                        str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交审核" > <span class="glyphicon glyphicon-ok"></span></a>'
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
         * 单据详情点击事件
         *
         *
         *************************************************/
        $('#sheetDetailTable tbody')
            .on(
                'click',
                'tr',
                function (e) {// 获取当前配件检修记录信息
                    if (sheetDetailTable.data().any()) {
                        $(this)
                            .addClass('selected')
                            .siblings()
                            .removeClass('selected');
                        var tr = $(this).closest('tr');

                        sheetDetailTableTrData = sheetDetailTable
                            .row(tr).data();
                        sheet_id2 = sheetDetailTableTrData.sheetId;
                        partCode = sheetDetailTableTrData.partCode;
                        partId = sheetDetailTableTrData.partId;
                        copyMachineStartTime = sheetDetailTableTrData.copyMachineStartTime;
                        copyMachineEndTime = sheetDetailTableTrData.copyMachineEndTime;
                        repaireState = sheetDetailTableTrData.repaireState;
                        scrapReason = sheetDetailTableTrData.scrapReason;
                        $("#devicePartsName").val(sheetDetailTableTrData.devicePartsName)
                        $("#deviceModelName").val(sheetDetailTableTrData.deviceModelName)
                        $("#partCode").val(sheetDetailTableTrData.partCode)
                        $("#partId").val(sheetDetailTableTrData.partId)
                        $("#faultInfo").val(sheetDetailTableTrData.faultInfo)
                        $("#prepareCheck").val(sheetDetailTableTrData.prepareCheck)
                        $("#machineCheck").val(sheetDetailTableTrData.machineCheck)
                        $("#replaceComponentCheck").val(sheetDetailTableTrData.replaceComponentCheck)
                        $("#copyMachineStartTime").val(sheetDetailTableTrData.copyMachineStartTime)
                        $("#copyMachineEndTime").val(sheetDetailTableTrData.copyMachineEndTime)
                        $("#copyMachineCheck").val(sheetDetailTableTrData.copyMachineCheck)
                        $("#checkedPrice").val(sheetDetailTableTrData.checkedPrice)
                        $("#repaireState").val(sheetDetailTableTrData.repaireState)
                        $("#scrapReason").val(sheetDetailTableTrData.scrapReason)
                        initOrClearCheckParts()
                    }
                })

        /**
         * 修改检修单据详情验证
         * @returns {boolean}
         */
        function verifyModifyParams() {
            var date = new Date()
            if (copyMachineStartTime == '' || copyMachineEndTime == '') {
                $("#alertSheetDetailMsgModify").html("<font style='color:red'>拷机开始时间或者结束时间为空</font>");
                $("#alertSheetDetailMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertSheetDetailMsgModify", "alertSheetDetailMsgModify", 5000);
                return false;
            }
            if (Date.parse(copyMachineStartTime) > Date.parse(copyMachineEndTime)) {
                $("#alertSheetDetailMsgModify").html("<font style='color:red'>拷机开始时间大于结束时间</font>");
                $("#alertSheetDetailMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertSheetDetailMsgModify", "alertSheetDetailMsgModify", 5000);
                return false;
            }
            if (Date.parse(copyMachineEndTime) > date) {
                $("#alertSheetDetailMsgModify").html("<font style='color:red'>拷机结束时间大于当前时间</font>");
                $("#alertSheetDetailMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertSheetDetailMsgModify", "alertSheetDetailMsgModify", 5000);
                return false;
            }
            if (repaireState == "2" && scrapReason == '') {
                $("#alertSheetDetailMsgModify").html("<font style='color:red'>报废原因未填写</font>");
                $("#alertSheetDetailMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertSheetDetailMsgModify", "alertSheetDetailMsgModify", 5000);
                return false;
            }
            if ((repaireState == '' || repaireState == null) && !(repaireState === 0)) {
                $("#alertSheetDetailMsgModify").html("<font style='color:red'>检测结论未填写</font>");
                $("#alertSheetDetailMsgModify").css('display', 'inline-block')
                timer = CMethod.hideTimeout("alertSheetDetailMsgModify", "alertSheetDetailMsgModify", 5000);
                return false;
            }
        }

        /*******************************************************
         * 检修记录修改事件
         *
         *
         *************************************************/
        $('#btnSheetDetailOk').click(function (e) {
            var copyMachineStartTime = $("#copyMachineStartTime").val()
            var copyMachineEndTime = $("#copyMachineEndTime").val()
            var scrapReason = $("#scrapReason").val()
            var repaireState = $("#repaireState").val()
            // if (verifyModifyParams() == false) {//验证参数正确与否
            //     return false;
            // }
            var params = JSON.stringify({
                sheetId: sheet_id2,
                partCode: $("#partCode").val(),
                prepareCheck: $("#prepareCheck").val(),
                checkedUserName: user_name,
                checkedUserId: user_id,
                checkedDate: $("#copyMachineEndTime").val(),
                faultInfo: $("#faultInfo").val(),
                machineCheck: $("#machineCheck").val(),
                replaceComponentCheck: $("#replaceComponentCheck").val(),
                copyMachineStartTime: copyMachineStartTime,
                copyMachineEndTime: copyMachineEndTime,
                copyMachineCheck: $("#copyMachineCheck").val(),
                checkedPrice: $("#checkedPrice").val(),
                repaireState: repaireState,
                scrapReason: scrapReason
            })
            $.ajax({
                url: config.basePath + '/repairManage/check/SheetDetailModify',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {

                        sheetDetailTable.ajax.reload()
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检修记录信息修改成功！</strong></span>');
                        $("#infoAlert").show();
                        hideTimeout("infoAlert", 2000);
                        $('#modifySheetDetailModal').modal('hide');
                    }
                }
            });

        })
        /**
         * 提交入库单据
         */
        $('#popSheetVerifyModal')
            .on(
                'show.bs.modal',
                function (e) {
                    var tr = $(e.relatedTarget)
                        .parents('tr');
                    var data = sheetDetailTable.row(tr)
                        .data();
                    $('#warningSheetVerifyText').text(
                        '确定提交配件编号为:' + data.partId
                        + '的记录吗？');
                    $('#btnPopSheetVerifyOk').val(
                        data.partId);
                });

        /**
         * 提交审核检修单据详情验证
         * @returns {boolean}
         */
        function verifyModify() {
            var date = new Date()
            if (copyMachineStartTime == null || copyMachineEndTime == null) {
                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>拷机开始时间或者结束时间为空！</strong></span>');
                $("#infoAlert").show();
                hideTimeout("infoAlert", 2000);
                return false;
            }
            if (Date.parse(copyMachineStartTime) > Date.parse(copyMachineEndTime)) {
                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>拷机开始时间大于结束时间！</strong></span>');
                $("#infoAlert").show();
                hideTimeout("infoAlert", 2000);
                return false;
            }
            if (Date.parse(copyMachineEndTime) > date) {
                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>拷机结束时间大于当前时间！</strong></span>');
                $("#infoAlert").show();
                hideTimeout("infoAlert", 2000);
                return false;
            }
            if (repaireState == 2 && scrapReason == '') {
                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>报废原因未填写！</strong></span>');
                $("#infoAlert").show();
                hideTimeout("infoAlert", 2000);
                return false;
            }
            if ((repaireState == '' || repaireState == null) && !(repaireState === 0)) {
                $("#alertMsg").html('<span style="color:green;text-align:center"><strong>检测结论未填写！</strong></span>');
                $("#infoAlert").show();
                hideTimeout("infoAlert", 2000);
                return false;
            }
            return true;
        }

        /**
         * 单据审核
         */
        $("#btnPopSheetVerifyOk")
            .on(
                'click',
                function (e) {
                    e.preventDefault();
                    var date = new Date();
                    if (verifyModify() == false) {//提交时审核
                        $("#popSheetVerifyModal").modal('hide');
                        return false;

                    }
                    var params = JSON.stringify({
                        // receiptVerifyFlag: 1,
                        sheetId: sheet_id2,
                        // receiptOperatorId: user_id,
                        // receiptOperatorName: user_name
                        partCode: partCode,
                        partId: partId

                    });
                    $
                        .ajax({
                            url: config.basePath
                                + '/repairManage/check/modify',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (
                                result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    // sheetTable.ajax
                                    //     .reload();
                                    sheetDetailTable.ajax.reload();
                                    $("#alertMsg")
                                        .html(
                                            '<span style="color:green;text-align:center"><strong>配件检测记录提交成功！</strong></span>');
                                    $("#infoAlert")
                                        .show();
                                    hideTimeout(
                                        "infoAlert",
                                        2000);
                                }
                            }
                        });

                });

        var setting = {
            async: {
                enable: true,
                url: config.basePath + '/repairManage/check/getPartsZtree',
                type: 'get',
                otherParam: {"storeHouseId": sourceStoreHouseId},
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
                        var params = $.param({
                            storeHouseId: sourceStoreHouseId,
                            partsZtreeId: treeNode.id,
                        })
                        sheetDetailTable.ajax.url(config.basePath + '/repairManage/check/getPartsZtreeDetails?' + params).load();
                    }
                }
            }
        };
        $.fn.zTree.init($("#funcTree"), setting, null);
        sheetDetailTable.on('draw.dt', function () {
            //给第一列编号
            sheetDetailTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });

        function hideTimeout(id, ms) {
            var time = setTimeout(function () {
                $("#" + id).hide();
            }, ms)
        }

    });
});