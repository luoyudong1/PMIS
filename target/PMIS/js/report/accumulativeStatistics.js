/**
 * 维修累计统计
 */
require(['../config'], function (config) {

    require(['datetimepicker'], function () {
        var date = new Date();
        initDatetimepicker("queryTime2", date);

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

    require(['jquery', 'popper', 'bootstrap', 'common/dt', 'common/commonMethod', 'common/dateFormat', 'metisMenu', 'slimscroll', 'pace', 'inspinia',], function ($, Popper, Bootstrap, dataTable, CMethod) {

        var sheet_id = '';//查询的单号
        var verify_flag = '';//查询的审核状态
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var deptId = window.parent.user.deptId // 所属部门id
        var myChart=''
        /**
         * 查询
         */
        $('#btn-search').click(function (e) {
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
        //配件详情信息列表
        var stockInfoTable = dataTable('partsInfoTable', {
            ajax: {
                url: config.basePath + '/report/accumulativeStatistics/getDataByDepotId',
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
                {data:"depotToTestCount"},
                {data:"detectorCount"},
                {data:"otherCount"},

                {data:"testRepairedCount"},
                {data:"testRepairWellDetectorCount"},
                {data:"testRepairWellOtherCount"},
                {data:"testRepairRatio"},
                {data:"testRepairPrice"},
                {data:"inTestCount"},
                {data:"scrapOutCount"},
                {data:"repairToFactoryCount"},
                {data:"repairToFactoryDetectorCount"},
                {data:"repairToFactoryOtherCount"},
                {data:"factoryRepairRatio"},
                {data:"repairOutFactoryCount"},
                {data:"factoryRepairWellDetectorCount"},
                {data:"factoryRepairWellOtherCount"},
                {data:"factoryRepairPrice"},
                {data:"factoryRepairDetectorPrice"},
                {data:"factoryRepairOtherPrice"},
                {data:"repairInFactoryCount"},
                {data:"inFactoryDetectorCount"},
                {data:"inFactoryOtherCount"},
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