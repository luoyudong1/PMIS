/**
 * 所接收车间返修
 */
require(['../config'],
function(config) {

    require(['datetimepicker'],
    function() {
        var date = new Date();
        date.setDate("1");
        date.setMonth(date.getMonth()-1)
        initDatetimepicker("queryTime", date);
        initDatetimepicker("queryTime2", new Date());
        initDatetimepicker("purchaseOrRepairedDateAdd", new Date());
        initDatetimepicker("purchaseOrRepairedDateModify", new Date());
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
    function($, bootstrap, dataTable, CMethod) {

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
        /**
						 * 查询
						 */
        $('#btn-search').click(function(e) {
            sheetTable.ajax.reload();
            sheetDetailTable.column(0).search('');
            sheetDetailTable.clear().draw();
            sheet_id = '';
            verify_flag = '';
        });

        /**
						 * 重置
						 */
        $('#btn-reset').click(function(e) {
            $('#sheet_id').val('');
            $('#verify_flag').val('');
            var now = new Date();
            now.setDate(1);
            $('#queryTime').val(formatDateBy(now.getTime(), 'yyyy-MM-dd'));
            $('#queryTime2').val(formatDateBy(new Date(), 'yyyy-MM-dd'));
        });

        /**
						 * 初始化收货仓库下拉框
						 */
        $.ajax({
            async: false,
            url: config.basePath + "/stock/testReceipt/getAllReceiptStoreHouse",
            data: {
                "action": "all"
            },
            dataType: 'json',
            success: function(result) {
                for (var i = 0; i < result.data.length; i++) {
                    $("#storeHouseNameAdd").append('<option value="' + result.data[i].storehouseId + '">' + result.data[i].storehouseName + '</option>');
                }
            },
            error: function(result) {
                console.log(result);
            }
        });

        /**
						 * 初始化资产属性下拉框
						 */
        $.ajax({
            async: false,
            url: config.basePath + "/entryAndOut/purchaseParts/getAllAssetAttributes",
            data: {
                "action": "all"
            },
            dataType: 'json',
            success: function(result) {
                for (var i = 0; i < result.data.length; i++) {
                    $("#assetAttributesNameAdd").append('<option value="' + result.data[i].asset_attributes_id + '">' + result.data[i].asset_attributes_name + '</option>');
                }
            },
            error: function(result) {
                console.log(result);
            }
        });

        /**
						 * 初始化入库单据
						 */
        var sheetTable = dataTable('sheetTable', {
            bAutoWidth: false,
            ajax: {
                url: config.basePath + '/stock/testReceipt/getAllSheets',
                type: 'GET',
                data: function(d) {
                    d.sheetId = "%" + $('#sheet_id').val() + "%";
                    d.sendVerifyFlag = 2;
                    d.receiptVerifyFlag = $('#verify_flag').val();
                    d.queryTime = $("#queryTime").val();
                    d.queryTime2 = ($("#queryTime2").val() == '' ? '': $("#queryTime2").val() + " 23:59:59");
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
            },{
                data: 'sourceStorehouseName'
            },
            {
                data: 'objectStorehouseName'
            },
            {
                data: 'addDate',
                render: function(data) {
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
                data: 'sendVerifyName'
            },
            {
                data: 'sendVerifyFlag',
                render: function(data) {
                    var str = "-";
                    if (data == 0) {
                        str = '<span style="color:red;font-weight:bold;">新建单据</span>';
                    } else if (data == 1) {
                        str = '<span style="color:blue;font-weight:bold;">审核中</span>';
                    } else if (data == 2) {
                        str = '<span style="color:black;font-weight:bold;">已审核</span>';
                    }
                    else if (data == 3) {
                        str = '<span style="color:red;font-weight:bold;">审核不通过</span>';
                    }
                    return str;
                }
            },
            {
                data: 'sendVerifyDate',
                render: function(data) {
                    if (data > 0) {
                        return formatDateBy(data, 'yyyy-MM-dd');
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
                render: function(data) {
                    var str = "-";
                    if (data == 0) {
                        str = '<span style="color:red;font-weight:bold;">未接收</span>';
                    } else if (data == 1) {
                        str = '<span style="color:blue;font-weight:bold;">接收审核中</span>';
                    } else if (data == 2) {
                        str = '<span style="color:black;font-weight:bold;">接收已审核</span>';
                    }
                    else if (data == 3) {
                        str = '<span style="color:red;font-weight:bold;">接收审核不通过</span>';
                    }
                    return str;
                }
            },
            {
                data: 'receiptVerifyDate',
                render: function(data) {
                    if (data > 0) {
                        return formatDateBy(data, 'yyyy-MM-dd');
                    } else {
                        return '-';
                    }
                }
            }],
            columnDefs: [{
                targets: 16,
                data: function(row) {
                    var str = '';
                    if (row.receiptVerifyFlag == 0||row.receiptVerifyFlag == 3) {
                        str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;' 
                        	+ '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
                        	+ '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="回退单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
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
            drawCallback: function(settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });

        $('#sheetTable tbody').on('click', 'tr',
        function(e) { // 获取当前单号
            if (sheetTable.data().any()) {
                $(this).addClass('selected').siblings().removeClass('selected');
                var tr = $(this).closest('tr');
                sheetTrData = sheetTable.row(tr).data();
                sheet_id2 = sheetTrData.sheetId;
                sendVerifyFlag = sheetTrData.sendVerifyFlag;
                var params = $.param({
                    sheetId: sheet_id2
                });
                if (sendVerifyFlag == 0||sendVerifyFlag ==3) { // 已审核不能添加
                    $("#add_sheetDetail").show();
                } else {
                    $("#add_sheetDetail").hide();
                }

                sheetDetailTable.column(0).search('');
                sheetDetailTable.ajax.url(config.basePath + '/stock/testReceipt/getAllSheetDetails?' + params).load();
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
                url: config.basePath + '/stock/testReceipt/getAllSheetDetails'
            },
            columns: [
            	{
					data : null
				},
				{
					data : 'partId'
				},
				{
					data : 'partCode'
				},{
					data:'supplierName'
				},
				{
					data : 'devicePartsName'
				},
				{
					data : 'deviceTypeName'
				},
				{
					data : 'deviceModelName'
				},
				{
					data : 'assetAttributesName'
				},
				 {
					data:'partState',
	            	render:function(data){
	            		if(data==1){
	            			return "新购"
	            		}else if(data==2){
	            			return "送修";
	            		}else if(data==3){
	            			return "初始化在探测站";
	            		}else if(data==4){
	            			return "初始化在备品库";
	            		}else if(data==5){
	            			return "初始化在送修库";
	            		}else {
	            			return "-";
	            		}
	            	}
				}, {
					data : 'warranty',
					render:function(data){
						if(data==0){
							return "否";
						}else if(data==1){
							return "是";
						}else{
							return "-";
						}
					}
				},{
					data: 'usedStationName'
				},{
					data:'faultDate'
				},{
					data:'faultInfo'
				},{
					data:'remark'
				}],
            columnDefs: [{
                targets: 14,
                data: function(row) {
                    var str = '';
                    if (row.sendVerifyFlag == 0||row.sendVerifyFlag == 3) {
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
            drawCallback: function(settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            }
        });

      

        /**
						 * 修改单据
						 */
        $('#modifySheetModal').on('show.bs.modal',
        function(e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#sheetIdModify').val(data.sheetId);
            loadDate("sheetDateModify", new Date());
            $('#remarkModify').val(data.sendRemark);
        });

        $("#btnModifySheetOk").on('click',
        function(e) {
            e.preventDefault();
            var params = JSON.stringify({
                sheetId: $('#sheetIdModify').val(),
                receiptRemark: $('#remarkModify').val(),

            });
            $.ajax({
                url: config.basePath + '/stock/testReceipt/modify',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function(result) {
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
        function(e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#warningSheetVerifyText').text('确定提交单据ID为:' + data.sheetId + '的单据吗？');
            $('#btnPopSheetVerifyOk').val(data.sheetId);
        });

        /**
						 * 单据审核
						 */
        $("#btnPopSheetVerifyOk").on('click',
        function(e) {
            e.preventDefault();
            var date = new Date();
            var params = JSON.stringify({
                receiptVerifyFlag: 1,
                sheetId: $('#btnPopSheetVerifyOk').val(),
                receiptOperatorId: user_id,
                receiptOperatorName:  user_name
                
               
            });
            $.ajax({
                url: config.basePath + '/stock/testReceipt/modify',
                type: "post",
                data: params,
                contentType: 'application/json',
                dataType: "json",
                success: function(result) {
                    if (result.code != 0) {
                         alert(result.msg);
                    } else {
                        sheetTable.ajax.reload();
                        sheetDetailTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据接收审核中！</strong></span>');
                        $("#infoAlert").show();
                        hideTimeout("infoAlert", 2000);
                    }
                }
            });

        });

        /**
						 * 回退单据
						 */
        $('#popSheetModal').on('show.bs.modal',
        function(e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
            $('#warningSheetText').text('确定要回退该单据（' + data.sheetId + '）？');
            $('#deleteSheetId').val(data.sheetId);
        });

        $('#btnPopSheetOk').on('click',
        function(e) {
            e.preventDefault();
            var params = JSON.stringify({
            	sheetId :  $('#deleteSheetId').val(),
            	sendVerifyFlag : 3,
            });
            $.ajax({
                url: config.basePath + '/stock/testReceipt/modify',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: 'json',
                success: function(result) {
                    if (result.code != 0) {
                         alert(result.msg);
                    } else {
                        sheetTable.ajax.reload();
                        sheetDetailTable.ajax.reload();
                        $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据回退成功！</strong></span>');
                        $("#infoAlert").show();
                        hideTimeout("infoAlert", 2000);
                    }
                }
            });
           
        });


       

        /**
						 * 配件库查询
						 */
        $("#btnQueryparts").on('click',
        function(e) {
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
        function() {
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
            function() {
                var tr = $(this).closest('tr');
                sheetTrData = sheetTable.row(tr).data();
                sheet_id2 = sheetTrData.sheetId;
                var params = $.param({
                    sheetId: sheet_id2
                });
                window.location.href = config.basePath + '/stock/depotToTest/exportSheetInfo?' + params;
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
        /**
						 * 定时隐藏alert框
						 */
        function hideTimeout(id, ms) {
            var time = setTimeout(function() {
                $("#" + id).hide();
            },
            ms)
        }

    });
});