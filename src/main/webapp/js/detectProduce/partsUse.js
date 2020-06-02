/**
 * 配件使用安装
 */
require(
		[ '../config' ],
		function(config) {

			require([ 'datetimepicker' ], function() {
				var date = new Date();
				date.setDate("1");
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
						value : date,
						format : 'Y-m-d',
						timepicker : false,
						// 关闭时间选项
						todayButton : true
					// 关闭选择今天按钮
					});
				}
			});

			require(
					[ 'jquery', 'bootstrap', 'common/dt',
							'common/commonMethod', 'common/slider',
							'common/dateFormat', 'common/select2',
							'common/pinyin' ],
					function($, bootstrap, dataTable, CMethod) {
						let screenHeight=screen.height*0.5+'px';
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
						var detectDeviceId = '';// 保留点击的探测站id
						var detectDeviceName = '';// 保留点击的探测站name
						var detectPartsId = '';// 保留点击的探测站配件id
						var sourceStoreHouseId = '';// 保留源仓库id
						var objectStoreHouseId = '';// 保留点击的探测站id
						var partsCode='';// 保留点击的探测站配件编码
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
						$('#btn-reset').click(
								function(e) {
									$('#sheet_id').val('');
									$('#verify_flag').val('');
									var now = new Date();
									now.setDate(1);
									$('#queryTime').val(
											formatDateBy(now.getTime(),
													'yyyy-MM-dd'));
									$('#queryTime2').val(
											formatDateBy(new Date(),
													'yyyy-MM-dd'));
								});

        /**
		 * 获取化源仓库
		 */
						var sourceParams=JSON.stringify({
							depotId : deptId,
							type:6
						})
$.ajax({
									url : config.basePath
											+ "/detectProduce/partsUse/getStoreHouseByDepotId",
									type:'POST',	
									contentType : 'application/json',
									dataType : "json",
									data : sourceParams,
									success : function(result) {
											sourceStoreHouseId=result.storehouseId
											
									},
									error : function(result) {
										console.log(result);
									}
								});
						/**
						 * 初始化探测站下拉框
						 */
						var detectParams=JSON.stringify({
							depotId : deptId,
						})
						$
								.ajax({
									url : config.basePath
											+ "/detectProduce/partsUse/getAllDetectByDepot",
									contentType : 'application/json',
									data :detectParams,
									type:'POST',
									dataType : 'json',
									success : function(result) {
										for (var i = 0; i < result.data.length; i++) {
											$("#storeHouseNameAdd")
													.append(
															'<option value="'
																	+ result.data[i].detectDeviceId
																	+ '">'
																	+ result.data[i].detectDeviceName
																	+ '</option>');
										}
									},
									error : function(result) {
										console.log(result);
									}
								});
/**
 * 获取目的仓库
 */
var objectParams=JSON.stringify({
	depotId : deptId,
	type:8
})
$.ajax({
							async : false,
							url : config.basePath
									+ "/detectProduce/partsUse/getStoreHouseByDepotId",
							type:'POST',
							contentType : 'application/json',
							dataType : "json",
							data : objectParams,
							success : function(result) {
									objectStoreHouseId=result.storehouseId
							},
							error : function(result) {
								console.log(result);
							}
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
												+ '/detectProduce/partsUse/getAllSheets',
										type : 'GET',
										data : function(d) {
											d.sheetId = "%"
													+ $('#sheet_id').val()
													+ "%";
											// d.sendVerifyFlag = 2;
											d.receiptVerifyFlag = $(
													'#verify_flag').val();
											d.queryTime = $("#queryTime").val();
											d.queryTime2 = ($("#queryTime2")
													.val() == '' ? '' : $(
													"#queryTime2").val()
													+ " 23:59:59");
											d.sourceDepotId=deptId;
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
											},{
												data:'deviceName'
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
														str = '<span style="color:blue;font-weight:bold;">确认中</span>';
													} else if (data == 2) {
														str = '<span style="color:black;font-weight:bold;">已确认</span>';
													}else if (data == 3) {
														str = '<span style="color:red;font-weight:bold;">确认不通过</span>';
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
										targets : 11,
										data : function(row) {
											var str = '';
											if (row.sendVerifyFlag == 0||row.sendVerifyFlag ==3) {
											   str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;' + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;' + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>&nbsp;&nbsp;';
											} else {
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
										var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
										api.column(0).nodes().each(
												function(cell, i) {
													cell.innerHTML = startIndex
															+ i + 1;
												});
									},
								});

						/**
						 * 初始化探测站配件表格
						 */
						var tblPartsInfo = dataTable(
								'tblPartsInfo',
								{
									bAutoWidth : false,
									ajax :{
										url : config.basePath
										+ "/detectProduce/partsUse/getDetectParts",
								type:'GET',	
			                    data:function(d){
			                    	d.deviceId=detectDeviceId,
			                    	d.ifMoved=1
									d.enabled=1
			                    },
								contentType : 'application/json',
								dataType : "json",
						
									},
									columns : [ {
										data : 'detectPartsId'
									}, {
										data : 'detectDeviceName'
									}, {
										data : 'partId',
										render:function (data) {
											return "(空)";

                                        }
									}, {
										data : 'partsSpecial'
									},  {
										data : 'enabled',
										render:function(data){
											if(data==0){
										     
											return "不可用";
										 
										}else if(data==1){
											return "可用";
										}else{
											return "-";
										}
									}
									},  {
										data : 'ifMoved',
										render:function(data){
											str="-"
											if(data==1){
												str= '<button type="button" class="btn btn-success" id="add_sheet"data-toggle="modal" data-target="#addPartsUseModal" style="height: 30px; padding-top: 4px; margin: 10px 0 10px 5px;">新增</button>';
											    return str;}
										}
									},  {
										data : 'devicePartsName'
									}, {
										data : 'deviceTypeName'
									}, {
										data : 'deviceModelName'
									},{
										data : 'supplierName'
									} , {
										data : 'unitPrice'
									}, {
										data : 'partsCode'
									} ],
									ordering : false,
									paging:true,
									pageLength: 10,
									drawCallback : function(settings) {
										var api = this.api();
										var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
										api.column(0).nodes().each(
												function(cell, i) {
													cell.innerHTML = startIndex
															+ i + 1;
												});
									},
								});
						/**
						 * 初始化可选配件表格
						 */
						var tblPartsUseInfo = dataTable(
								'tblPartsUseInfo',
								{
									bAutoWidth : false,
									ajax : {
										url : config.basePath
												+ '/detectProduce/partsUse/getAllPartsByStock',
										type : 'get',
										data : function(d) {
											d.storeHouseId=sourceStoreHouseId
											d.partsCode =  partsCode
													+ '%';
											d.devicePartsName = '%'
													+ $("#query_part_name")
															.val() + '%';
											if (!sheetTrData.supplierName) {
											} else {
												d.supplierName = '%'
														+ sheetTrData.supplierName
														+ '%';
											}

										}
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
									},{
										data : 'purchaseOrRepairedPrice'
									}, {
										data : 'purchaseOrRepairedDate'
									}, {
										data : 'usedStationName'
									}, {
										data : 'supplierName'
									} ],
									ordering : false,
									serverSide : false,
									paging:true,
									pageLength:10,
									drawCallback : function(settings) {
										var api = this.api();
										var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
										api.column(0).nodes().each(
												function(cell, i) {
													cell.innerHTML = startIndex
															+ i + 1;
												});
									},
								});
/**
 * 单据行点击事件
 */
						$('#sheetTable tbody')
								.on(
										'click',
										'tr',
										function(e) { // 获取当前单号
											if (sheetTable.data().any()) {
												$(this)
														.addClass('selected')
														.siblings()
														.removeClass('selected');
												var tr = $(this).closest('tr');
												sheetTrData = sheetTable
														.row(tr).data();
												sheet_id2 = sheetTrData.sheetId;
												detectDeviceId = sheetTrData.deviceId;
												detectDeviceName = sheetTrData.deviceName;
												sendVerifyFlag = sheetTrData.sendVerifyFlag;
												var params = $.param({
													sheetId : sheet_id2
												});
												if (sendVerifyFlag == 0||sendVerifyFlag == 3) { // 已审核不能添加
													$("#add_sheetDetail")
															.show();
												} else {
													$("#add_sheetDetail")
															.hide();
												}
												
												tblPartsInfo.ajax.reload()
												sheetDetailTable.column(0)
														.search('');
												sheetDetailTable.ajax
														.url(
																config.basePath
																		+ '/detectProduce/partsUse/getAllSheetDetails?'
																		+ params)
														.load();
											}
										});

						/**
						 * 初始化配件详情表
						 */
						var sheetDetailTable = dataTable(
								'sheetDetailTable',
								{
									bAutoWidth : false,
									ajax : {
										type : 'POST',
										data : "",
										url : config.basePath
												+ '/detectProduce/partsUse/getAllSheetDetails'
									},
									columns : [ {
										data : null
									}, {
										data : 'partId'
									}, {
										data : 'partCode'
									}, {
										data : 'supplierName'
									}, {
										data : 'devicePartsName'
									}, {
										data : 'deviceTypeName'
									}, {
										data : 'deviceModelName'
									}, {
										data : 'assetAttributesName'
									}, {
										data : 'partState',
										render : function(data) {
											var str = "-";
											if (data == 1) {
												str = '新购';
											} else if (data == 2) {
												str = '送修';
											} else if (data == 3) {
												str = '初始化在探测站';
											}else if (data == 4) {
												str = '初始化在备品库';
											}else if (data == 5) {
												str = '初始化在送修库';
											}
											return str;
										
										}

									},  {
										data : 'purchaseOrRepairedDate'
									}, {
										data : 'usedStationId',
										render:function(data){
											return detectDeviceName;
										}
									}, {
										data : 'remark'
									} ],
									columnDefs : [ {
										targets : 12,
										data : function(row) {
											var str = '';
											if (row.sendVerifyFlag == 0) {
												str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetDetailModal" title="修改配送配件"><span class="glyphicon glyphicon-edit"></span></a>';
												str += '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
											} else {
												str += '';
											}
											return str;
										}
									} ],
									ordering : false,
									paging : true,
									pageLength : 5,
									drawCallback : function(settings) {
										var api = this.api();
										var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
										api.column(0).nodes().each(
												function(cell, i) {
													cell.innerHTML = startIndex
															+ i + 1;
												});
									}
								});

						/**
						 * 单据ID失去焦点验证是否存在此单据
						 */
						$("#sheetIdAdd")
								.on(
										'blur',
										function() {
											var part_id = $('#sheetIdAdd')
													.val();
											if ($.trim(part_id) != "") {
												var params = $.param({
													"sheet_id" : sheet_id
												});
												$
														.ajax({
															url : config.basePath
																	+ '/system/partsManage/find',
															type : "GET",
															data : params,
															contentType : 'application/json',
															dataType : "json",
															success : function(
																	msg) {
																if (msg.code != null
																		&& msg.code != 0) {
																	// //alert(msg.msg);
																	$("#sheetIdAdd")[0]
																			.focus();
																}
															}
														});
											} else {
												$("#warn_content").text(
														"单据ID不能为空!");
												$("#warningModal")
														.modal('show');
												$("#sheetIdAdd")[0].focus();
											}
										});

						/**
						 * 添加单据
						 */
						$('#addSheetModal')
								.on(
										'show.bs.modal',
										function() {
											// 单据ID为单据类型+部门id+序列号
											if (deptId < 10) {
												var sheetId = "12-0" + deptId
														+ "-";
											} else {
												var sheetId = "12-" + deptId
														+ "-";
											}
											$
													.ajax({
														url : config.basePath
																+ '/entryAndOut/purchaseParts/getMaxSheetId',
														type : "get",
														data : {
															sheet_id : sheetId
														},
														contentType : 'application/json',
														dataType : "text",
														success : function(
																result) {
															// alert(result)
															$('#sheetIdAdd')
																	.val(result);
														},

													});

											loadDate("sheetDateAdd", new Date());
											$('#sendOperatorNameAdd').val(
													user_name);
											$('#sendRemarkAdd').val('');
										});

						$("#btnAddSheetOk")
								.on(
										'click',
										function(e) {
											if($("#storeHouseNameAdd").val()==""||$("#storeHouseNameAdd").val()==null){
												$("#alertMsgSheetAdd").html("<font style='color:red'>探测站为空</font>");
								 				$("#alertMsgSheetAdd").css('display','inline-block')
								 				CMethod.hideTimeout("alertMsgSheetAdd","alertMsgSheetAdd",5000);
												return false;
											}
											e.preventDefault();
											var params = JSON
													.stringify({
														sheetId : $(
																'#sheetIdAdd')
																.val(),
														sourceStoreHouseId:
																sourceStoreHouseId,
														objectStoreHouseId:
																objectStoreHouseId,
														deviceId : $(
																'#storeHouseNameAdd')
																.val(),
														deviceName : $(
																'#storeHouseNameAdd option:selected')
																.text(),
														addDate : $(
																'#sheetDateAdd')
																.val(),
														sendOperatorId : user_id,
														sendOperatorName : $(
																'#sendOperatorNameAdd')
																.val(),
														sendRemark : $(
																'#sendRemarkAdd')
																.val(),
														trackingNumber : $(
																'#trackingNumber')
																.val(),
													});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/createSheetInfo',
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
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>单据信息添加成功！</strong></span>');
																$("#infoAlert")
																		.show();
																hideTimeout(
																		"infoAlert",
																		2000);
															}
														}
													});
										});

						/**
						 * 添加入库配件
						 */
						$('#addSheetDetailModal').on('show.bs.modal',
								function() {
									$('#deviceName').val('');
									$('#part_nameAdd').val('');
									$('#part_idAdd').val('');
									$('#parts_id_select').val('');
									$('#partUnit_priceAdd').val('');
									$('#deviceModelNameAdd').val('');
									$('#partCodeAdd').val('');
									$('#purchaseOrRepairedPriceAdd').val('');
									$('#purchaseOrRepairedDateAdd').val('');
									$('#remarkAdd').val('');
								});
						/**
						 * 验证单据参数是否为空
						 */
						function verifyPartsAdd() {
							var partCode = $('#part_codeAdd').val();
							var partName = $('#part_nameAdd').val();
							var partId = $('#part_idAdd').val();
							if ($.trim(partCode) != ""
									&& $.trim(partName) != ""
									&& $.trim(partId) != "") {
								return true;

							} else {
								return false;
							}
						}
						/**
						 * 添加单据详情确认
						 */
						$("#btnAddSheetDetailOk")
								.on(
										'click',
										function(e) {
											e.preventDefault();
											var params = JSON
													.stringify({
														sheetId : sheet_id2,
														partCode : $(
																'#partCodeAdd')
																.val(),
														partId : $(
																'#part_idAdd')
																.val(),
														purchaseOrRepairedDate : $(
																'#purchaseOrRepairedDateAdd')
																.val(),
														purchaseOrRepairedPrice : $(
																'#purchaseOrRepairedPriceAdd')
																.val(),
														warranty : $(
																'#warrantyAdd')
																.val(),
														remark : $('#remarkAdd')
																.val(),
														usedStationId:detectDeviceId,
														usedStationName:detectDeviceName,
														detectPartsId:detectPartsId
													});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/SheetDetailCreate',
														type : "post",
														data : params,
														contentType : 'application/json',
														dataType : "json",
														success : function(
																result) {
															if (result.code != 0) {
																 alert(result.msg);
															} else {
																var json = $
																		.param({
																			sheetId : sheet_id2
																		});
																sheetDetailTable.ajax
																		.url(
																				config.basePath
																						+ '/detectProduce/partsUse/getAllSheetDetails?'
																						+ json)
																		.load();
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
																$("#infoAlert")
																		.show();
																hideTimeout(
																		"infoAlert",
																		2000);

															}
														}
													});
										});

						/**
						 * 修改单据
						 */
						$('#modifySheetModal').on('show.bs.modal', function(e) {
							var tr = $(e.relatedTarget).parents('tr');
							var data = sheetTable.row(tr).data();
							$('#sheetIdModify').val(data.sheetId);
							loadDate("sheetDateModify", new Date());
							$('#remarkModify').val(data.sendRemark);
						});

						$("#btnModifySheetOk")
								.on(
										'click',
										function(e) {
											e.preventDefault();
											var params = JSON.stringify({
												sheetId : $('#sheetIdModify')
														.val(),
												receiptRemark : $(
														'#remarkModify').val(),

											});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/modify',
														type : 'POST',
														data : params,
														contentType : 'application/json',
														dataType : "json",
														success : function(
																result) {
															if (result.code != 0) {
																alert(result.msg);
															} else {
                                                                $('#add_sheetDetail').hide()
																sheetTable.ajax
																		.reload();
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>单据信息修改成功！</strong></span>');
																$("#infoAlert")
																		.show();
																hideTimeout(
																		"infoAlert",
																		2000);
															}
														}
													});

										});

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
													'确定提交单据ID为:' + data.sheetId
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
											if(sheetDetailTable.data().length<1){
								            	alert("配件为空，请先添加配件！")
								            	return;
								            }
											var params = JSON.stringify({
												sendVerifyFlag : 2,
												sheetId : $(
														'#btnPopSheetVerifyOk')
														.val(),
												sendOperatorId : user_id,
												sendOperatorName : user_name

											});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/modify',
														type : "post",
														data : params,
														contentType : 'application/json',
														dataType : "json",
														success : function(
																result) {
															if (result.code != 0) {
																 alert(result.msg);
															} else {
                                                                $('#add_sheetDetail').hide()
																sheetTable.ajax
																		.reload();
																sheetDetailTable.ajax
																		.reload();
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>单据审核中！</strong></span>');
																$("#infoAlert")
																		.show();
																hideTimeout(
																		"infoAlert",
																		2000);
															}
														}
													});

										});

						/**
						 * 删除单据
						 */
						$('#popSheetModal').on(
								'show.bs.modal',
								function(e) {
									var tr = $(e.relatedTarget).parents('tr');
									var data = sheetTable.row(tr).data();
									$('#warningSheetText').text(
											'确定要删除该单据（' + data.sheetId + '）？');
									$('#deleteSheetId').val(data.sheetId);
								});

						$('#btnPopSheetOk')
								.on(
										'click',
										function(e) {
											e.preventDefault();
											var params = JSON.stringify({
												sheetId : $('#deleteSheetId')
														.val()
											});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/delete',
														type : 'POST',
														data : params,
														contentType : 'application/json',
														dataType : 'json',
														success : function(
																result) {
															if (result.code != 0) {
																 alert(result.msg);
															} else {
																$("#add_sheetDetail")
																.hide();
																sheetTable.ajax
																		.reload();
																sheetDetailTable.ajax
																		.reload();
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
											
										});

						/**
						 * 删除配件
						 */
						$('#popModal').on(
								'show.bs.modal',
								function(e) {
									var tr = $(e.relatedTarget).parents('tr');
									var data = sheetDetailTable.row(tr).data();
									$('#warningText').text(
											'确定要删除该配件（' + data.partId + '）？');
									$('#deletePartId').val(data.partId);
									partCode=data.partCode;

								});

						$('#btnPopOk')
								.on(
										'click',
										function(e) {
											e.preventDefault();
											var params = JSON.stringify({
												partId : $('#deletePartId')
														.val(),
												partCode:partCode,
												sheetId : sheet_id2
											});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/SheetDetailDeleteByCode',
														type : 'POST',
														data : params,
														contentType : 'application/json',
														dataType : 'json',
														success : function(
																result) {
															if (result.code != 0) {
																// alert(result.msg);
															} else {
																sheetDetailTable.ajax
																		.reload();
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>配件删除成功！</strong></span>');
																$("#infoAlert")
																		.show();
																hideTimeout(
																		"infoAlert",
																		2000);
															}
														}
													});
										});

						/**
						 * addSheetDetailModal点击配件 弹出配件库窗口
						 */
						$("#part_nameAdd").on('click', function(e) {
							$('#partsModal').modal('show');
							tblPartsInfo.ajax.reload();
						});

						/**
						 * 配件库查询
						 */
						$("#btnQueryparts").on('click', function(e) {
							tblPartsInfo.ajax.reload();
						});

						/**
						 * 点击当前配件 获取配件
						 */
						$("#tblPartsInfo tbody")
								.on(
										"click",
										"tr",
										function() {
											$(this).addClass('success')
													.siblings().removeClass(
															'success');
											var trData = tblPartsInfo.row(this)
													.data();
											partsCode=trData.partsCode
											detectPartsId=trData.detectPartsId
											tblPartsUseInfo.ajax.reload()
										});
						/**
						 * 点击当前配件 获取配件
						 */
						$("#tblPartsUseInfo tbody")
								.on(
										"dblclick",
										"tr",
										function() {
											$(this).addClass('success')
													.siblings().removeClass(
															'success');
											var trData = tblPartsUseInfo.row(this)
													.data();
											partsCode=trData.partCode
											$('#part_nameAdd').val(trData.devicePartsName)
											$('#deviceModelNameAdd').val(trData.deviceModelName)
											$('#partCodeAdd').val(trData.partCode)
											$('#part_idAdd').val(trData.partId)
											$('#purchaseOrRepairedPriceAdd').val(trData.purchaseOrRepairedPrice)
											$('#purchaseOrRepairedDateAdd').val(trData.purchaseOrRepairedDate)
											$('#deviceName').val(detectDeviceName)
											$('#remarkAdd').val(trData.remark)
											$("#partsModal").modal('hide');
											$("#addPartsUseModal").modal('hide');
										});
						$("#modifySheetDetailModal").on(
								'show.bs.modal',
								function(e) {
									var tr = $(e.relatedTarget).parents('tr');
									var data = sheetDetailTable.row(tr).data();
									$("#part_nameModify").val(
											data.devicePartsName);
									$("#deviceModelNameModify").val(
											data.deviceModelName);
									$("#partCodeModify").val(data.partCode);
									$("#warrantyModify").val(data.warranty);
									$("#part_idModify").val(data.partId);
									$("#purchaseOrRepairedPriceModify").val(
											data.purchaseOrRepairedPrice);
									$("#purchaseOrRepairedDateModify").val(
											data.purchaseOrRepairedDate);
									$("#sheetDetailRemarkModify").val(
											data.remark);
								});
						$("#btnModifySheetDetailOk")
								.click(
										function() {
											var params = JSON
													.stringify({
														sheetId : sheet_id2,
														partCode : $(
																"#partCodeModify")
																.val(),
														purchaseOrRepairedDate : $(
																'#purchaseOrRepairedDateModify')
																.val(),
														purchaseOrRepairedPrice : $(
																'#purchaseOrRepairedPriceModify')
																.val(),
														warranty : $(
																'#warrantyModify')
																.val(),
														remark : $(
																'#sheetDetailRemarkModify')
																.val()
													});
											$
													.ajax({
														url : config.basePath
																+ '/detectProduce/partsUse/SheetDetailModify',
														type : "post",
														data : params,
														contentType : 'application/json',
														dataType : "json",
														success : function(
																result) {
															if (result.code != 0) {
																// alert(result.msg);
															} else {
																var json = $
																		.param({
																			sheetId : sheet_id2
																		});
																sheetDetailTable.ajax
																		.url(
																				config.basePath
																						+ '/detectProduce/partsUse/getAllSheetDetails?'
																						+ json)
																		.load();
																$("#alertMsg")
																		.html(
																				'<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
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
						 * 时间加载
						 */
						function loadDate(id, date) {
							$.datetimepicker.setLocale('ch');
							$('#' + id).datetimepicker({
								value : date,
								format : 'Y-m-d',
								timepicker : false,
								// 关闭时间选项
								todayButton : true
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
                        tblPartsInfo.on('draw.dt', function () {
                            //给第一列编号
                            tblPartsInfo.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                                cell.innerHTML = i + 1;
                            });
                        });
						/** 导出配件详情信息 */
						$("#sheetTable tbody")
								.on(
										'click',
										'#exportExcel',
										function() {
											var tr = $(this).closest('tr');
											sheetTrData = sheetTable.row(tr)
													.data();
											sheet_id2 = sheetTrData.sheetId;
											var params = $.param({
												sheetId : sheet_id2
											});
											window.location.href = config.basePath
													+ '/entryAndOut/purchaseParts/exportSheetInfo?'
													+ params;
										});

						/**
						 * 定时隐藏alert框
						 */
						function hideTimeout(id, ms) {
							var time = setTimeout(function() {
								$("#" + id).hide();
							}, ms)
						}

					});
		});