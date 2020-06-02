/**
 * 所调拨到车间查询
 */
require(['../config'], function (config) {

    require(['datetimepicker'], function () {
        var date = new Date();
        date.setDate("1")
        date.setMonth(date.getMonth() - 1)
        initDatetimepicker("queryTime", date);
        initDatetimepicker("queryTime2", new Date());

        //初始化时间框
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

    require(['jquery', 'bootstrap', 'common/dt', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin'], function ($, bootstrap, dataTable) {
        /**
         * 查询
         */
        $('#btn-search').click(function (e) {
            sheetDetailTable.ajax.reload();
            sheetDetailInfoTable.ajax.reload()
        });

        /**
         * 重置
         */
        $('#btn-reset').click(function (e) {
            $('#part_id').val('');
            $('#part_code').val('');
            $('#part_name').val('');
            $('#device_model').val('');
            $('#device_type_name').val('');
            $('#unit_price').val('');
            $('#unit_price2').val('');
            var now = new Date();
            now.setDate(1);
            $('#queryTime').val(formatDateBy(now.getTime(), 'yyyy-MM-dd'));
            $('#queryTime2').val(formatDateBy(new Date(), 'yyyy-MM-dd'));
        });


        var sheetDetailInfoTable = dataTable('sheetDetailInfoTable', {
            bAutoWidth: false,
            ajax: {
                url: config.basePath + '/stock/partsSearch/getPartInfo',
                type: "GET",
                data: function (d) {
                    d.partId = $("#part_id").val();
                    d.partCode = $("#part_code").val();

                }
            },
            columns: [
                {data: 'devicePartsName'},
                {data: 'partIdSeq'},
                {data: 'factoryPartsCode'},
                {data: 'deviceTypeName'},
                {data: 'deviceModelName'},
                {data: 'supplierName'},
                {data: 'assetAttributesName'},
                {data: 'purchasePrice'},
                {data: 'storehouseName'},
                {data: 'detectDeviceName'},
                {
                    data: 'createTime',
                    render: function (data) {
                        var str = "-";
                        if (data > 0) {
                            str = formatDateBy(data, 'yyyy-MM-dd');
                        }
                        return str;
                    }
                },
                {
                    data: 'updateTime',
                    render: function (data) {
                        var str = "-";
                        if (data > 0) {
                            str = formatDateBy(data, 'yyyy-MM-dd');
                        }
                        return str;
                    }
                },
            ],
            ordering: false,
            paging: false
        });

        /**
         * 初始化配件详情表
         */
        var sheetDetailTable = dataTable('sheetDetailTable', {
            bAutoWidth: false,
            ajax: {
                url: config.basePath + '/stock/partsSearch/search',
                type: "GET",
                data: function (d) {
                    d.partId = $("#part_id").val().trim();
                    d.partCode = $("#part_code").val().trim();

                }
            },
            columns: [
                {data: null},
                {data: 'sheetId'},
                {
                    data: 'addDate',
                    render: function (data) {
                        if (data > 0) {
                            return formatDateBy(data, 'yyyy-MM-dd')
                        } else {
                            return "-"
                        }
                    }
                },
                {data: 'partCode'},
                {data: 'sourceStoreHouseName'},
                {data: 'sendOperatorName'},
                {data: 'sendVerifyName'},
                {data: 'objectStoreHouseName'},
                {data: 'receiptOperatorName'},
                {data: 'receiptVerifyName'},
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
                },
                {
                    data: 'repaireState',
                    render: function (data) {
                        if (data == 0) {
                            return "不合格"
                        } else if (data == 1) {
                            return "合格"
                        } else if (data == 2) {
                            return "报废"
                        } else {
                            return "-"
                        }
                    }
                },
                {data: 'purchaseOrRepairedPrice'},
                {data: 'usedStationName'},
                {data: 'remark'}
            ],
            ordering: false,
            paging: true,
            pageLength: 17,
            serverSide: false,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function (cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });


    });
});