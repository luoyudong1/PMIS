/**
 * 单个配件信息管理Js
 */
require(['../config'],
    function (config) {

        require(['datetimepicker'],
            function () {
                var date = new Date();
                date.setDate(1);
                date.setMonth(0)
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
        require(['../config'], function (config) {
            //
            require(['jquery', 'popper', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/dateFormat', 'metisMenu', 'slimscroll', 'pace', 'inspinia',], function ($, Popper, Bootstrap, dataTable, CMethod) {
                //入库方式（1：采购入库、2：生产入库、3：调拨入库、0:手动添加）
                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                // var roles= window.parent.user.roles; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                //隐藏探测站下拉框
                //配件详情信息列表
                var stockInfoTable = dataTable('partsInfoTable', {
                    ajax: {
                        url: config.basePath + '/report/comprehensiveStatics/getDataByDepotId',
                        type: 'GET',
                        data: function (d) {
                            d.queryTime = $("#queryTime").val();
                            d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                            d.depotId = deptId;
                        },
                    },
                    columns: [
                        {data: null},
                        {data:"depotName"},
                        {data:"purchaseCount"},
                        {data:"purchasePrice"},
                        {data:"depotToTestCount"},
                        {data:"repairToFactoryCount"},
                        {data:"repairInFactoryCount"},
                        {data:"repairOutFactoryCount"},
                        {data:"testRepairedCount"},
                        {data:"inTestCount"},
                        {data:"repairToDepotCount"},
                        {data:"repairPrice"},
                        {data:"scrapOutCount"}

                    ],
                    bAutoWidth:true,
                    ordering: false,
                    paging: false,
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
                 * 查询
                 */
                $('#btnQuery').click(function (e) {
                    stockInfoTable.ajax.reload()
                });


                stockInfoTable.on('draw.dt', function () {
                    //给第一列编号
                    stockInfoTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });
            });
        });
    })