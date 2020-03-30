/**
 * 所内返修
 */
require(['../config'], function (config) {
    //
    require([ 'jquery','popper','bootstrap', 'common/dt','common/commonMethod','common/dateFormat','metisMenu','slimscroll','pace','inspinia'], function ($,Popper,Bootstrap, dataTable,CMethod) {
    	let screenHeight = screen.height * 0.5 + 'px';
		let timer = 0;
		var sheet_id = '';// 查询的单号
		var sheetId = '';// 查询的单号
		var verify_flag = '';// 查询的审核状态
		var newDate = new Date();
		var sendVerifyFlag = '';
		var sheet_id2 = '';
		var user_id = window.parent.user.userId; // 登录人ID
		var user_name = window.parent.user.userName; // 登录人名字
		var deptId = window.parent.user.deptId; // 登录人部门id
		var sheetTrData = '';// 保留点击的单号信息
		var sourceStoreHouseName = '';// 保留点击的单号信息
		var sheetTrData = '';// 保留点击的单号信息
		var sheetDetailTable;// 保留点击的单号信息
		var sheetDetailParams="";
    	var date = new Date();
        date.setDate("1");
        date.setMonth(date.getMonth()-1)
    	//初始化时间查询框
    	CMethod.initDatetimepicker('queryTimeBegin',date);
    	CMethod.initDatetimepicker('queryTimeEnd',new Date());
    	CMethod.initDatetimepicker('queryTime',date);
    	CMethod.initDatetimepicker('queryTime2',new Date());
    	
    	//初始化仓库查询项
    	$.ajax({
            async : false,
            url : config.basePath + "/repairManage/repair/getStorehouse",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#storehouse_id_sel").append('<option value="'+result.data[i].storehouse_id+'">'+result.data[i].storehouse_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	
    	//定义一个变量用于存储选中复选框的值
        var sel_a2=[];  
        /**
		 * 查询
		 */
		$('#btn-search').click(function(e) {
			sheetTable.ajax.reload();
			sheetDetailTable.column(0).search('');
			sheetDetailTable.clear().draw();
			sheetId = '';
			verifyFlag = '';
		});
		 
		 // 响应刷新按钮 
         $('#btnRefresh').click(function() {
        	 $("#part_id_sel").val("");
             $("#part_name_sel").val("");
             location.reload();
         });
     	    /**
			 * 初始化入库单据
			 */

			var sheetTable = dataTable(
					'sheetTable',
					{
						bAutoWidth : false,
						ajax : {
							url : config.basePath
									+ '/repairManage/repair/getAllSheets',
							type : "get",
							data : function(d) {
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
						columns : [
								{
									data : null
								},
								{
									data : 'sheetId'
								},
								{
									data : 'sourceStorehouseName'
								},
								{
									data : 'objectStorehouseName'
								},
								{
									data : 'addDate',
									render : function(data) {
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
									data : 'sendRemark'
								},
								{
									data : 'sendOperatorName'
								},
								{
									data : 'sendVerifyName'
								},
								{
									data : 'sendVerifyFlag',
									render : function(data) {
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
									data : 'sendVerifyDate',
									render : function(data) {
										if (data > 0) {
											return formatDateBy(
													data,
													'yyyy-MM-dd');
										} else {
											return '-';
										}
									}
								} ],
						columnDefs : [ {
							targets : 10,
							data : function(row) {
								 var str = '';
		                         if(row.sendVerifyFlag == 1){
		                    		 str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#SheetVerifyOkModal" title="审核通过" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
		                    		 + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#SheetVerifyBackModal" title="审核不通过"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
		                    	 }else{
		                    		 str += '';
		                    	 }
		                    	 str += '<button id="exportExcel" type="button" class="btn btn-success btn-xs" title="导出"><span class="glyphicon glyphicon-download-alt"></span></button>';
		                    	 return str;
							}
						} ],
						ordering : false,
						paging : true,
						pageLength : 5,
						serverSide : false,
						drawCallback : function(settings) {
							var api = this.api();
							var startIndex = api.context[0]._iDisplayStart;// 获取到本页开始的条数
							api.column(0).nodes().each(
									function(cell, i) {
										cell.innerHTML = startIndex
												+ i + 1;
									});
						},
					});
			/*******************************************************
			 * 
			 */
			$('#sheetTable tbody')
					.on(
							'click',
							'tr',
							function(e) {// 获取当前单号
								if (sheetTable.data().any()) {
									$(this)
											.addClass('selected')
											.siblings()
											.removeClass('selected');
									var tr = $(this).closest('tr');
									sheetTrData = sheetTable
											.row(tr).data();
									sheet_id2 = sheetTrData.sheetId;
									sendVerifyFlag = sheetTrData.sendVerifyFlag;
									sourceStoreHouseName = sheetTrData.sourceStorehouseName;
					                sheetDetailParams = $.param({
					                    sheetId: sheet_id2
					                });

									if (sourceStoreHouseName =='机辆检测所新购库') {
										
										
										sheetDetailTable=sheetDetailTable1;
										 $('#sheetDetailTableDiv1').show();
										 $('#sheetDetailTableDiv2').hide();
										 $('#sheetDetailTableDiv3').hide();
										
									} else if (sourceStoreHouseName == '机辆检测所送修库') {
//										 loadSheetDetail2(params).column(0).search('');
										 sheetDetailTable=sheetDetailTable2;
										
										 $('#sheetDetailTableDiv2').show();
										 $('#sheetDetailTableDiv1').hide();
										 $('#sheetDetailTableDiv3').hide();
										 
								   } else if (sourceStoreHouseName == '机辆检测所修返库'){
//										 loadSheetDetail3(params).column(0).search('');
									   
									     sheetDetailTable=sheetDetailTable3;
										 $('#sheetDetailTableDiv3').show();
										 $('#sheetDetailTableDiv1').hide();
										 $('#sheetDetailTableDiv2').hide();
										
								}
								sheetDetailTable.ajax.url(config.basePath + '/repairManage/repair/getAllSheetDetails?' + sheetDetailParams).load();
							
								}
								});
			
			/**
			 * 获取新购库送修单据配件详情表
			 */
				var sheetDetailTable1=dataTable(
						'sheetDetailTable1',
						{
							bAutoWidth : false,
							ajax:{
								type:'POST',
								data:"",
								url:config.basePath
										 +
										 '/repairManage/repair/getAllSheetDetails'
                              
							},
							columns : [ {
								data : null
							},  {
								data : 'partId'
							}, {
								data : 'partCode'
							}, {
								data : 'devicePartsName'
							}, {
								data : 'deviceTypeName'
							}, {
								data : 'deviceModelName'
							},  {
								data : 'assetAttributesName'
							}, {
								data : 'remark'
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
			 * 获取送修库送修单据配件详情表
			 */

				var sheetDetailTable2=dataTable(
						'sheetDetailTable2',
						{   bAutoWidth : false,
							ajax:{
								type:'POST',
								data:"",
								url:config.basePath
										 +
										 '/repairManage/repair/getAllSheetDetails'
                              
							},
							columns : [ {
								data : null
							}, {
								data : 'partId'
							}, {
								data : 'partCode'
							}, {
								data : 'devicePartsName'
							}, {
								data : 'deviceTypeName'
							}, {
								data : 'deviceModelName'
							}, {
								data : 'assetAttributesName'
							}, {
								data : 'usedStationName'
							}, {
								data : 'faultDate',
								render : function(data) {
									if (data > 0) {
										return formatDateBy(
												data,
												'yyyy-MM-dd');
									} else {
										return '-';
									}
								}
							}, {
								data : 'faultInfo'
							}, {
								data : 'remark'
							} ],
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
			 * 获取修返库送修单据配件详情表
			 */
				var sheetDetailTable3=dataTable(
						'sheetDetailTable3',
						{   bAutoWidth : false,
							ajax:{
								type:'POST',
								data:"",
								url:config.basePath
										 +
										 '/repairManage/repair/getAllSheetDetails'
                              
							},
							columns : [ {
								data : null
							}, {
								data : 'partId'
							}, {
								data : 'partCode'
							}, {
								data : 'devicePartsName'
							}, {
								data : 'deviceTypeName'
							}, {
								data : 'deviceModelName'
							},
							 {
								data : 'assetAttributesName'
							}, {
								data : 'factoryReplaceComponent'
							},
							 {
								data : 'factoryReplaceCount'
							}, {
								data : 'factoryRepairedPrice'
							}
							
							, {
								data : 'factoryRepairedDate',
									render : function(data) {
										if (data > 0) {
											return formatDateBy(
													data,
													'yyyy-MM-dd');
										} else {
											return '-';
										}
									}
							},{
								data : 'repaireState'
							} ],							 
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
				 * 提交入库单据
				 */
$('#SheetVerifyOkModal').on('show.bs.modal',
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
       sendVerifyFlag: 2,//审核通过
       receiptVerifyFlag: 0,
       sheetId: $('#btnPopSheetVerifyOk').val(),
       sendVerifyId:user_id,
       sendVerifyName:user_name,
       sendVerifyDate:new Date(),
   });
   $.ajax({
       url: config.basePath + '/repairManage/repair/modify',
       type: "post",
       data: params,
       contentType: 'application/json',
       dataType: "json",
       success: function(result) {
           if (result.code != 0) {
                alert(result.msg);
           } else {
               sheetTable.ajax.reload();
           	   sheetDetailTable.ajax.url(config.basePath + '/repairManage/repair/getAllSheetDetails?' + sheetDetailParams).load();
               $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据审核通过！</strong></span>');
               $("#infoAlert").show();
               hideTimeout("infoAlert", 2000);
           }
       }
   });

});

/**
 * 删除单据
 */
$('#popBackModal').on(
		'show.bs.modal',
		function(e) {
			var tr = $(e.relatedTarget).parents('tr');
			var data = sheetTable.row(tr).data();
			$('#warningSheetText').text(
					'确定要删除该单据（' + data.sheetId + '）？');
			$('#deleteSheetId').val(data.sheetId);
		});
$('#btnBackSheetOk')
		.on(
				'click',
				function(e) {
					e.preventDefault();
					var params = JSON.stringify({
						sheetId: $('#deleteSheetId').val()
});
$.ajax({
								url : config.basePath
										+ '/repairManage/repair/delete',
								type : 'POST',
								data : params,
								contentType : 'application/json',
								dataType : 'json',
								success : function(
										result) {
									if (result.code != 0) {
										 alert(result.msg);
									} else {
										sheetTable.ajax
												.reload();
										sheetDetailTable.ajax.url(config.basePath + '/repairManage/repair/getAllSheetDetails?' + sheetDetailParams).load();
										$("#alertMsg")
												.html(
														'<span style="color:green;text-align:center"><strong>单据删除成功！</strong></span>');
										$("#infoAlert")
												.show();
										hideTimeout(
												"infoAlert",
												2000);
									}
								}
							});

				})
				   /**
         * 入库单据未通过审核提示
         */
		$('#SheetVerifyBackModal').on('show.bs.modal', function(e) {
			var tr = $(e.relatedTarget).parents('tr');
			var data = sheetTable.row(tr).data();
			$('#warningSheetText').text('确定回退入库单号为' + data.sheetId + '的单据？');
			$('#SheetVerifyId').val(data.sheetId);
		});
        
		/**
		 * 单据审核未通过
		 */
        $("#btnPopSheetVerify").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
            	sendVerifyId : user_id,
            	sendVerifyName : user_name,
            	sheetId : sheet_id2,
            	sendVerifyFlag : 3,
            	sendVerifyDate:new Date()
            });
            $.ajax({
                url : config.basePath + '/repairManage/repair/modify',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	sheetTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据审核未通过！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
            sheetDetailTable.ajax.url(config.basePath + '/repairManage/repair/getAllSheetDetails?' + sheetDetailParams).load();
        });
        /** 导出配件详情信息 */
        $("#sheetTable tbody").on('click', '#exportExcel',
            function () {
                var tr = $(this).closest('tr');
                sheetTrData = sheetTable.row(tr).data();
                sheet_id2 = sheetTrData.sheetId;
                var params = $.param({
                    sheetId: sheet_id2,
                    fileName:"所内送修出库单"
                });
                window.location.href = config.basePath + '/exportExcel/export?' + params;
            });
        sheetTable.on('draw.dt', function () {
            //给第一列编号
            sheetTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
        sheetDetailTable1.on('draw.dt', function () {
            //给第一列编号
            sheetDetailTable1.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
        sheetDetailTable2.on('draw.dt', function () {
            //给第一列编号
            sheetDetailTable2.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        }); sheetDetailTable3.on('draw.dt', function () {
            //给第一列编号
            sheetDetailTable3.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });

function hideTimeout(id, ms) {
    var time = setTimeout(function() {
        $("#" + id).hide();
    },
    ms)
}

    });
});