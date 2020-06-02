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
                url: config.basePath + '/repairManage/check/getCheckSheetDetails',
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
                {
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
            ordering: false,
            paging: true,
            pageLength: 15,
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