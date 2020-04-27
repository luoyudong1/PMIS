/**
 * 所内返修
 */
require(['../config'], function (config) {
    //
    require([ 'jquery','popper','bootstrap', 'common/dt','common/commonMethod','common/dateFormat','metisMenu','slimscroll','pace','inspinia'], function ($,Popper,Bootstrap, dataTable,CMethod) {
    	//入库方式（1：采购入库、2：生产入库、3：调拨入库、0:手动添加）
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
		var sourceStoreHouseName = '';// 保留点击的单号源仓库名称
		var sheetTrData = '';// 保留点击的单号信息
		var sheetDetailTableTrData='';//保留点击的配件检修记录信息
		var sheetDetailParams="";
    	var date = new Date();
    	var newDate=new Date("");
        date.setDate("1");
        date.setMonth(date.getMonth()-1)
    	//初始化时间查询框
        var format='Y-m-d H:i';
    	CMethod.initDatetimepicker('queryTimeBegin',date);
    	CMethod.initDatetimepicker('queryTimeEnd',newDate);
    	CMethod.initDatetimepicker('queryTime',date);
    	CMethod.initDatetimepicker('queryTime2',newDate);
    	CMethod.initDatetimepickerWithSecond('copyMachineStartTime',newDate,format);
    	CMethod.initDatetimepickerWithSecond('copyMachineEndTime',newDate,format);
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
								+ '/repairManage/check/getAllSheets',
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
								data : 'receiptRemark'
							},
							{
								data : 'receiptOperatorName'
							},
							{
								data : 'receiptVerifyName'
							},
							{
								data : 'receiptVerifyFlag',
								render : function(data) {
									var str = "-";
									if (data == 0||null) {
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
								data : 'receiptVerifyDate',
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
	                         if(row.receiptVerifyFlag == 1){
	                    		 str += '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="审核通过" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;'
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
		 * 单据点击事件
		 * 
		 * 
		 *************************************************/
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
			var sheetDetailTable=dataTable(
					'sheetDetailTable',
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
						columnDefs: [{
			                targets: 17,
			                data: function(row) {
			                    var str = '';
			                    if (row.receiptVerifyFlag == 0) {
			                        str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改检修记录"><span class="glyphicon glyphicon-edit"></span></a>';
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
			/*******************************************************
			 * 单据详情点击事件
			 * 
			 * 
			 *************************************************/
			$('#sheetDetailTable tbody')
					.on(
							'click',
							'tr',
							function(e) {// 获取当前配件检修记录信息
								if (sheetDetailTable.data().any()) {
									$(this)
											.addClass('selected')
											.siblings()
											.removeClass('selected');
									var tr = $(this).closest('tr');
									sheetDetailTableTrData = sheetDetailTable
											.row(tr).data();
									$("#devicePartsName").val(sheetDetailTableTrData.devicePartsName)
									$("#deviceModelName").val(sheetDetailTableTrData.deviceModelName)
									$("#partCode").val(sheetDetailTableTrData.partCode)
									$("#partId").val(sheetDetailTableTrData.partId)
									$("#prepareCheck").val(sheetDetailTableTrData.prepareCheck)
									$("#machineCheck").val(sheetDetailTableTrData.machineCheck)
									$("#replaceComponentCheck").val(sheetDetailTableTrData.replaceComponentCheck)
									$("#copyMachineStartTime").val(sheetDetailTableTrData.copyMachineStartTime)				
									$("#copyMachineEndTime").val(sheetDetailTableTrData.copyMachineEndTime)
									$("#copyMachineCheck").val(sheetDetailTableTrData.copyMachineCheck)
									$("#repaireState").val(sheetDetailTableTrData.repaireState)
									
								}
							})
$('#btnSheetDetailOk').click(function(e) {
	 var params = JSON.stringify({
    sheetId: sheet_id2,
    partCode:$("#partCode").val(),
    prepareCheck:$("#prepareCheck").val(),
    machineCheck:$("#machineCheck").val(),
    replaceComponentCheck:$("#replaceComponentCheck").val(),
    copyMachineStartTime:$("#copyMachineStartTime").val(),
    copyMachineEndTime:$("#copyMachineEndTime").val(),
    copyMachineCheck:$("#copyMachineCheck").val(),
    repaireState:$("#repaireState").val(),
	 })
	   $.ajax({
           url: config.basePath + '/repairManage/check/SheetDetailModify',
           type: 'POST',
           data: params,
           contentType: 'application/json',
           dataType: "json",
           success: function(result) {
               if (result.code != 0) {
                   alert(result.msg);
               } else {
            	   
            	   sheetDetailTable.ajax.url(config.basePath + '/repairManage/check/getAllSheetDetails?' + sheetDetailParams).load();
                   $("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据信息修改成功！</strong></span>');
                   $("#infoAlert").show();
                   hideTimeout("infoAlert", 2000);
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
										function(e) {
											var tr = $(e.relatedTarget)
													.parents('tr');
											var data = sheetTable.row(tr)
													.data();
											$('#warningSheetVerifyText').text(
													'确定审核通过单据ID为:' + data.sheetId
															+ '的单据吗？');
											$('#btnPopSheetVerifyOk').val(
													data.sheetId);
										});

						/**
						 * 单据审核
						 */
						$("#btnPopSheetVerifyOk")
								.on(
										'click',
										function(e) {
											e.preventDefault();
											var date = new Date();
											var params = JSON.stringify({
												receiptVerifyFlag : 2,
												sheetId : $(
														'#btnPopSheetVerifyOk')
														.val(),
														receiptVerifyId : user_id,
														receiptVerifyName : user_name,
														receiptVerifyDate : new Date(),
											});
											$
													.ajax({
														url : config.basePath
																+ '/repairManage/check/modify',
														type : "post",
														data : params,
														contentType : 'application/json',
														dataType : "json",
														success : function(
																result) {
															if (result.code != 0) {
																alert(result.msg);
															} else {
																sheetTable.ajax
																		.reload();
																sheetDetailTable.ajax
																		.url(
																				config.basePath
																						+ '/repairManage/check/getAllSheetDetails?'
																						+ sheetDetailParams)
																		.load();
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>配件检测记录单据审核通过！</strong></span>');
																$("#infoAlert")
																		.show();
																hideTimeout(
																		"infoAlert",
																		2000);
															}
														}
													});

										});

		 function hideTimeout(id,ms){
				var time=setTimeout(function(){
					$("#"+id).hide();
				},ms)
			}
    });
});