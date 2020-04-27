
/**
 * 采购入库
 */
require(['../config'], function (config) {

	require(['datetimepicker'], function () {
		var date = new Date();
		date.setDate("1")
        date.setMonth(date.getMonth()-1)
		initDatetimepicker("queryTime", date);
		initDatetimepicker("queryTime2", new Date());
		//初始化时间框
	    function initDatetimepicker(id, date){
			$.datetimepicker.setLocale('ch');
            $('#' + id).datetimepicker({
                value: date,
                format: 'Y-m-d',
                timepicker: false, //关闭时间选项
       	        todayButton: true //关闭选择今天按钮
            });
        }
	});
	
    require(['jquery', 'bootstrap', 'common/dt', 'common/slider','common/dateFormat','common/select2','common/pinyin'], function  ($, bootstrap, dataTable) {
    	
    	var supplier_name = '';
    	var part_id = '';
    	var part_code = '';
    	var part_name = '';
    	var device_model = '';
    	var device_type_name = '';
    	var unit_price = '';
    	var unit_price2 = '';
    	var queryTime = '';
    	var queryTime2 = '';
        var newDate = new Date();
    	/**
         * 查询
         */
        $('#btn-search').click(function (e) {
        	sheetDetailTable.ajax.reload();
        });
    	
        /**
         * 重置
         */
        $('#btn-reset').click(function (e) {
        	$('#supplier_name').val('');
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
        // 初始化仓库查询项
		$
				.ajax({
					async : false,
					url : config.basePath
							+ "/repairManage/repair/getStorehouse",
					data : {
						"action" : "all"
					},
					dataType : 'json',
					success : function(result) {
						for (var i = 0; i < result.data.length; i++) {
							$("#sourceStoreHouse")
									.append(
											'<option value="'
													+ result.data[i].storehouse_id
													+ '">'
													+ result.data[i].storehouse_name
													+ '</option>');
						}
					},
					error : function(result) {
						console.log(result);
					}
				});
        /**
         * 初始化供货商下拉框
         */
    	$.ajax({
            async : false,
            url : config.basePath + "/entryAndOut/purchaseParts/getAllsuppliers",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#supplier_name").append('<option value="'+result.data[i].supplier_id+'">'+result.data[i].supplier_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	
    	/**
    	 * 初始化配件型号下拉框
    	 */
    	$.ajax({
            async : false,
            url : config.basePath + "/system/partsManage/getAllDeviceModel",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#device_model").append('<option value="'+result.data[i].device_type_id+''+ result.data[i].sort_id +'">'+result.data[i].device_model+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	
    	/**
    	 * 初始化配件类型下拉框
    	 */
    	$.ajax({
            async : false,
            url : config.basePath + "/system/partsManage/getAllDeviceType",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#device_type_name").append('<option value="'+result.data[i].device_type_id+'">'+result.data[i].device_type_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	
    	var sheetDetailTable = dataTable('sheetDetailTable', {
        	bAutoWidth: false,
            ajax:{
                url: config.basePath + '/repairManage/repair/search',
                type : "POST",
                data : function (d) {
                	d.sheetId = '%' +$('#sheet_id').val()+ '%';
                	d.supplierName = $('#supplier_name option:selected').text();
                	d.partId = '%' + $("#part_id").val() + '%';
                	d.sourceStoreHouseId = $("#sourceStoreHouse option:selected").val();
                	d.devicePartsName =  $('#part_name option:selected').text();
 	                d.partCode = '%' + $("#part_code").val() + '%';
 	                d.deviceModelName = $('#device_model option:selected').text();
	                d.deviceTypeName = $('#device_type_name option:selected').text();
 	                d.unitPrice =$("#unit_price").val();
 	                d.unitPrice2 =$("#unit_price2").val();
 	                d.queryTime = $("#queryTime").val();
	                d.queryTime2 = ($("#queryTime2").val()==''? '':$("#queryTime2").val()+" 23:59:59");
                }
        },
        columns : [ {
			data : null
		}, {
			data:"sheetId"
		} ,{
			data : 'devicePartsName'
		}, {
			data : 'deviceModelName'
		}, {
			data : 'deviceTypeName'
		}, {
			data : 'supplierName'
		}, {
			data : 'partCode'
		},  {
			data : 'partId'
		}, {
			data : 'assetAttributesName'
		},{
			data : 'partState'
		},{
			data : 'faultInfo'
		},{
			data : 'prepareCheck'
		},{
			data : 'machineCheck'
		},{
			data : 'replaceComponentCheck'
		},{
			data : 'copyMachineStartTime'
		},{
			data : 'copyMachineEndTime'
		},{
			data : 'copyMachineCheck'
		},{
			data : 'repaireState',
			render : function(data) {
				var str = "-";
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
            pageLength: 15,
            serverSide: false,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });
		
        /**
         * 时间加载
         */
		function loadDate(id, date) {
			$.datetimepicker.setLocale('ch');
			$('#' + id).datetimepicker({
				value : date,
				format : 'Y-m-d',
				timepicker : false, // 关闭时间选项
				todayButton : true
			// 关闭选择今天按钮
			});
		}
		
		/**
		 * 定时隐藏alert框
		 */
		function hideTimeout(id,ms){
			var time=setTimeout(function(){
				$("#"+id).hide();
			},ms)
		}
	
    });
});