
/**
 * 所调拨到车间查询
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
    	var sheet_id='';
    	var supplier_name = '';
    	var part_id = '';
    	var part_code = '';
    	var device_parts_name = '';
    	var device_model_name = '';
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
    	/**初始化配件名称下拉框
    	 * 
    	 */
    	$.ajax({
            async : false,
            url : config.basePath + "/system/partsManage/getAllDeviceName",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#part_name").append('<option value="'+result.data[i].device_parts_id+'" deviceNameCode="'+result.data[i].device_parts_code+'">'+result.data[i].device_parts_name+'</option>');
            		//$("#partName").append('<option value="'+result.data[i].device_name+'" deviceNameCode="'+result.data[i].device_name_code+'">'+result.data[i].device_name+'</option>');
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
            		$("#device_model").append('<option value="'+result.data[i].device_model_id+'">'+result.data[i].device_model_name+'</option>');
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
    	
    	   
        /**
         * 初始化配件详情表
         */
        var sheetDetailTable = dataTable('sheetDetailTable', {
        	bAutoWidth: false,
            ajax:{
                url: config.basePath + '/stock/testToDepot/search',
                type : "POST",
                data : function (d) {
                	d.supplierName = $('#supplier_name option:selected').text();
                	d.partId = '%' + $("#part_id").val() + '%';
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
            columns: [
            	{data: null},
                {data: 'sheetId'},
                {data: 'partId'},
                {data: 'devicePartsName'},
                {data: 'partCode'},
                {data: 'deviceTypeName'},
                {data: 'deviceModelName'},
                {data: 'unitPrice'},
                {data: 'supplierName'},
                {data: 'assetAttributesName'},
                {data: 'remark'}
            ],
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