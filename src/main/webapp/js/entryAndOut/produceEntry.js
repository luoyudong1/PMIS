/**
 * 生产入库
 */
require(['../config'], function (config) {

	require(['datetimepicker'], function () {
		var date = new Date();
		date.setDate("1")
        date.setMonth(date.getMonth()-1)
		initDatetimepicker("queryTime", date);
		initDatetimepicker("queryTime2", new Date());
		/**
		 * 初始化时间框
		 */
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
	
    require(['jquery', 'bootstrap', 'common/dt','common/commonMethod', 'common/slider','common/dateFormat','common/select2','common/pinyin','zTree'], function  ($, bootstrap, dataTable,CMethod) {
    	
    	var sheet_id = '';//查询的单号
    	var verify_flag = '';//查询的审核状态
        var newDate = new Date();
        var verifyFlag='';
        var sheet_id2 = '';
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var deptId = window.parent.user.deptId // 所属部门id
        var sheetTrData = '';//保留点击的单号信息
        
    	/**
         * 查询
         */
        $('#btn-search').click(function (e) {
        	sheetTable.ajax.reload();
        	sheetDetailTable.column(0).search('');
        	sheetDetailTable.clear().draw();
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
        
        /**
         * 初始化供货商下拉框
         */
    	$.ajax({
            async : false,
            url : config.basePath + "/entryAndOut/produceEntry/getAllsuppliers",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#supplierNameAdd").append('<option value="'+result.data[i].supplier_id+'">'+result.data[i].supplier_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	
    	/**
         * 初始化资产属性下拉框
         */
    	$.ajax({
            async : false,
            url : config.basePath + "/entryAndOut/produceEntry/getAllAssetAttributes",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#assetAttributesNameAdd").append('<option value="'+result.data[i].asset_attributes_id+'">'+result.data[i].asset_attributes_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	
        /**
         * 初始化入库单据
         */
        var sheetTable = dataTable('sheetTable', {
        	bAutoWidth: false,
            ajax: {
                url: config.basePath + '/entryAndOut/produceEntry/getAllSheets',
                type: "get",
                data:function(d) {
                	d.sheet_id = "%"+$('#sheet_id').val()+"%";
                	d.receipt_verify_flag = $('#verify_flag').val();
                	d.queryTime = $("#queryTime").val();
	                d.queryTime2 = ($("#queryTime2").val()==''? '':$("#queryTime2").val()+" 23:59:59");
                }
            },
            columns: [
                {data: null},
                {data: 'sheet_id'},
                {data: 'supplier_name'},
                {data: 'storehouse_name'},
                {data: 'add_date',
                	render: function (data) {
                		if(data>0){
                			return formatDateBy(data, 'yyyy-MM-dd');
                		}else{
                			return '-';
                		}
                	}
                },
                {data: 'receipt_remark'},
                {data: 'receipt_operator_name'},
                {data: 'receipt_verify_name'},
                {data: 'receipt_verify_flag',
                	render: function (data) {
                		var str = "-";
                		if (data == 0) {
                            str = '<span style="color:red;font-weight:bold;">新建</span>';
                        } else if (data == 1) {
                            str = '<span style="color:blue;font-weight:bold;">审核中</span>';
                        } else if (data == 2) {
                            str = '<span style="color:black;font-weight:bold;">已完成</span>';
                        }
                        else if (data == 3) {
                            str = '<span style="color:red;font-weight:bold;">审核不通过</span>';
                        }
                		return str;
                	}
                },
                {data: 'receipt_verify_date',
                	render: function (data) {
                		if(data>0){
                			return formatDateBy(data, 'yyyy-MM-dd');
                		}else{
                			return '-';
                		}
                	}
                }
            ],
            columnDefs: [
                {
                     targets: 10,
                     data: function (row) {
                    	 var str = '';
                    	 if (row.receipt_verify_flag == 0||row.receipt_verify_flag == 3) {
                    		 str += '<a class="modifySheet btn btn-info btn-xs" data-toggle="modal" href="#modifySheetModal" title="修改单据"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                    			 + '<a class="btn btn-primary btn-xs openCmdDetail" data-toggle="modal" href="#popSheetVerifyModal" title="提交" > <span class="glyphicon glyphicon-ok"></span></a>&nbsp;&nbsp;&nbsp;'
                    			 + '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>';
                    	 }else if(row.receipt_verify_flag == 1){
                    		 str += '<a class="deleteSheet btn btn-danger btn-xs" data-toggle="modal" href="#popSheetModal" title="删除单据"><span class="glyphicon glyphicon-remove"></span></a>';
                    	 }else{
                    		 str += '';
                    	 }
                    	 return str;
                     }
                 }
             ],
             ordering : false,
             paging:true,
             pageLength: 5,
             serverSide : false,
             drawCallback: function (settings) {
                 var api = this.api();
                 var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                 api.column(0).nodes().each(function(cell, i) {
                     cell.innerHTML = startIndex + i + 1;
                 });
             },
        });
        
        /**
         * 初始化配件表格
         */
        var tblPartsInfo = dataTable('tblPartsInfo', {
        	bAutoWidth: false,
            ajax: {
                url: config.basePath + '/system/partsManage/getAllParts',
                type:'get',
                data: function (d) {
                	 d.parts_code = '%' + $("#query_part_id").val() + '%';
 	                 d.device_parts_name = '%' + $("#query_part_name").val() + '%';
 	                 if (!sheetTrData.supplier_name) {
 	                	 
					}else{
						/*d.supplier_name = '%' + sheetTrData.supplier_name + '%';*/
					}
 	                 
	            }
            },
            columns: [
                {data: null},
                {data: 'parts_code'},
                {data: 'device_parts_name'},
                {data: 'device_model_name'},
                {data: 'unit_price'},
                {data: 'unit'},
                {data: 'supplier_name'}
            ],
            ordering : false,
            paging:true,
            pageLength: 10,
            serverSide : false,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });
		
        $('#sheetTable tbody').on('click', 'tr', function (e) {//获取当前单号
    		if (sheetTable.data().any()) {
            	$(this).addClass('selected').siblings().removeClass('selected'); 
            	var tr = $(this).closest('tr');
            	sheetTrData = sheetTable.row( tr ).data();
    		    sheet_id2 = sheetTrData.sheet_id;
    		    verifyFlag = sheetTrData.receipt_verify_flag;
                var params = $.param({
                	sheet_id : sheet_id2
                });
                if (verifyFlag == 0||verifyFlag == 3) {//已审核不能添加
                	$("#add_sheetDetail").show();
				}else{
					$("#add_sheetDetail").hide();
				}
                sheetDetailTable.column(0).search('');
                sheetDetailTable.ajax.url(config.basePath + '/entryAndOut/produceEntry/getAllSheetDetails?' + params).load();
    		}
        });
        
        /**
         * 初始化配件详情表
         */
        var sheetDetailTable = dataTable('sheetDetailTable', {
        	bAutoWidth: false,
            columns: [
                {data: null},
                {data: 'part_id'},
                {data: 'device_parts_name'},
                {data: 'device_model_name'},
                {data: 'part_code'},
                {data: 'device_type_name'},
                {data: 'unit_price'},
                {data: 'supplier_name'},
                {data: 'source',
                	render: function (data) {
                		var str = "-";
                		if (data == 1) {
                			str = '<span style="color:blue;font-weight:bold;">大修</span>';
                		} else if (data == 2) {
                			str = '<span style="color:blue;font-weight:bold;">更新</span>';
                		} else if (data == 3) {
                			str = '<span style="color:blue;font-weight:bold;">项修</span>';
                		}else if (data == 4) {
                			str = '<span style="color:blue;font-weight:bold;">新线建设</span>';
                		}
                		return str;
                	}
                },
                {data: 'asset_attributes_name'},
                {data: 'remark'}
            ],
            columnDefs: [
                 {
                      targets: 11,
                      data: function (row) {
                          var str = '';
                          if (verifyFlag == 0) {
                        	  str += '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                          }else{
                        	  str += '';
                          }
                          return str;
                      }
                  }
             ],
             ordering : false,
             paging:true,
             pageLength: 5,
             drawCallback: function (settings) {
                 var api = this.api();
                 var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                 api.column(0).nodes().each(function(cell, i) {
                     cell.innerHTML = startIndex + i + 1;
                 });
             },
        });
        
        /**
         * 单据ID失去焦点验证是否存在此单据
         */ 
        $("#sheetIdAdd").on('blur', function () {
            var part_id = $('#sheetIdAdd').val();
            if ($.trim(part_id) != "") {
                var params = $.param({
                    "sheet_id" : sheet_id
                });
                $.ajax({
                    url: config.basePath + '/system/partsManage/find',
                    type : "GET",
                    data : params,
                    contentType : 'application/json',
                    dataType : "json",
                    success : function(msg) {
                        if (msg.code != null && msg.code != 0)	{
                            alert(msg.msg);
                            $("#sheetIdAdd")[0].focus();
                        }
                    }
                });
            } else {
                $("#warn_content").text("单据ID不能为空!");
  		    	$("#warningModal").modal('show');
                $("#sheetIdAdd")[0].focus();
            }
        });
        
        /**
         * 添加单据
         */
        $('#addSheetModal').on('show.bs.modal', function () {
        	var DateTime = new Date();
            // 单据ID为单据类型+部门id+序列号
            if (deptId < 10) {
                var sheetId = "AA-0" + deptId + "-";
            } else {
                var sheetId = "AA-" + deptId + "-";
            }
            $.ajax({
                url : config.basePath + '/entryAndOut/produceEntry/getMaxSheetId',
                type : "get",
                data : {
                	sheet_id : sheetId
                	},
                contentType: 'application/json',
                dataType : "text",
                success : function (result) {
                	$('#sheetIdAdd').val(result);
                }
            });
        	
        	loadDate("sheetDateAdd", new Date());
        	$('#operator_nameAdd').val(user_name);
        	$('#remarkAdd').val('');
        });

        $("#btnAddSheetOk").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
            	sheet_id : $('#sheetIdAdd').val(),
            	supplier_name : $('#supplierNameAdd option:selected').text(),
            	add_date : $('#sheetDateAdd').val(),
            	receipt_operator_id : user_id,
            	receipt_operator_name : $('#operator_nameAdd').val(),
            	receipt_remark : $('#remarkAdd').val(),
            	source : $('#sourceAdd').val()
            });
            $.ajax({
                url : config.basePath + '/entryAndOut/produceEntry/create',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	sheetTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据信息添加成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });
        
        /**
         * 添加入库配件
         */
        $('#addSheetDetailModal').on('show.bs.modal', function () {
        	$('#part_codeAdd').val('');
        	$('#part_nameAdd').val('');
        	$('#part_idAdd').val('');
        	$('#parts_id_select').val('');
        	$('#partUnit_priceAdd').val('');
        	$('#locationAdd').val('');
        	$('#remarkPartsAdd').val('');
        });
        
        $("#btnAddSheetDetailOk").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
            	sheet_id : sheet_id2,
            	part_code : $('#part_codeAdd').val(),
            	part_id : $('#part_idAdd').val(),
            	unit_price : $('#partUnit_priceAdd').val(),
            	location : $('#locationAdd').val(),
            	asset_attributes_id : $('#assetAttributesNameAdd').val(),
            	remark : $('#remarkPartsAdd').val()
            });
            $.ajax({
                url : config.basePath + '/entryAndOut/produceEntry/SheetDetailCreate',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	var json = $.param({
                        	sheet_id : sheet_id2
                        });
                        sheetDetailTable.ajax.url(config.basePath + '/entryAndOut/produceEntry/getAllSheetDetails?' + json).load();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>入库配件信息添加成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });
        
        /**
         * 修改单据
         */
        $('#modifySheetModal').on('show.bs.modal', function (e) {
        	var tr = $(e.relatedTarget).parents('tr');
            var data = sheetTable.row(tr).data();
        	$('#sheetIdModify').val(data.sheet_id);
        	loadDate("sheetDateModify", new Date());
        	$('#remarkModify').val(data.receipt_remark);
        });

        $("#btnModifySheetOk").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
            	sheet_id : $('#sheetIdModify').val(),
            	add_date : $('#sheetDateModify').val(),
            	receipt_remark : $('#remarkModify').val()
            });
            $.ajax({
                url : config.basePath + '/entryAndOut/produceEntry/modify',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        $('#add_sheetDetail').hide()
                    	sheetTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据信息修改成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
            
        });
        
        /**
         * 提交入库单据
         */
		$('#popSheetVerifyModal').on('show.bs.modal', function(e) {
			var tr = $(e.relatedTarget).parents('tr');
			var data = sheetTable.row(tr).data();
			$('#warningSheetVerifyText').text('确定提交单据ID为:' + data.sheet_id + '的单据吗？');
			$('#btnPopSheetVerifyOk').val(data.sheet_id);
		});
        
        /**
         * 单据审核
         */
        $("#btnPopSheetVerifyOk").on('click', function (e) {
            e.preventDefault();
            if(sheetDetailTable.data().length<1){
                alert("配件为空，请先添加配件！")
                return;
            }
            var params = JSON.stringify({
            	receipt_operator_id : user_id,
            	receipt_operator_name : user_name,
            	receipt_verify_flag : 1,
            	sheet_id : sheet_id2
            });
            $.ajax({
                url : config.basePath + '/entryAndOut/produceEntry/modifyVerify',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        $('#add_sheetDetail').hide()
                    	sheetTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据审核中！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
            
        });
        
        /** 
         * 删除单据
         */
		$('#popSheetModal').on('show.bs.modal', function(e) {
			var tr = $(e.relatedTarget).parents('tr');
			var data = sheetTable.row(tr).data();
			$('#warningSheetText').text('确定要删除该单据（' + data.sheet_id + '）？');
			$('#deleteSheetId').val(data.sheet_id);
		});

		$('#btnPopSheetOk').on('click', function(e) {
			e.preventDefault();
			var params = JSON.stringify({
				sheet_id : $('#deleteSheetId').val()
			});
			$.ajax({
				url : config.basePath + '/entryAndOut/produceEntry/delete',
				type : 'POST',
				data : params,
				contentType : 'application/json',
				dataType : 'json',
				success : function(result) {
					if (result.code != 0) {
						alert(result.msg);
					} else {
						$("#add_sheetDetail")
						.hide();
						sheetTable.ajax.reload();
						sheetDetailTable.ajax.reload();
						$("#alertMsg").html('<span style="color:green;text-align:center"><strong>单据删除成功！</strong></span>');
						$("#infoAlert").show();
						hideTimeout("infoAlert", 2000);
					}
				}
			});
		});
       
        /**
         * 删除配件
         */
		$('#popModal').on('show.bs.modal', function(e) {
			var tr = $(e.relatedTarget).parents('tr');
			var data = sheetDetailTable.row(tr).data();
			$('#warningText').text('确定要删除该配件（' + data.part_code + '）？');
			$('#deletePartCode').val(data.part_code);
		});

		$('#btnPopOk').on('click', function(e) {
			e.preventDefault();
			var params = JSON.stringify({
				sheet_id : sheet_id2,
				part_code : $('#deletePartCode').val()
			});
			$.ajax({
				url : config.basePath + '/entryAndOut/produceEntry/SheetDetailDeleteByCode',
				type : 'POST',
				data : params,
				contentType : 'application/json',
				dataType : 'json',
				success : function(result) {
					if (result.code != 0) {
						alert(result.msg);
					} else {
						sheetDetailTable.ajax.reload();
						$("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件删除成功！</strong></span>');
						$("#infoAlert").show();
						hideTimeout("infoAlert", 2000);
					}
				}
			});
			
		});
		
		/**
		 * addSheetDetailModal点击配件      弹出配件库窗口
		 */ 
	    $("#part_nameAdd").on('click',function(e){
	    	$('#partsModal').modal('show');
	    	tblPartsInfo.ajax.reload();
  		});
	    
	    
        /**
         * 配件库查询
         */
	    $("#btnQueryparts").on('click',function(e){
	    	tblPartsInfo.ajax.reload();
  		});
        
	    /**
	     * 点击当前配件 获取配件
	     */
        $("#tblPartsInfo tbody").on("dblclick", "tr", function () {
        	$(this).addClass('success').siblings().removeClass('success');
            var trData = tblPartsInfo.row(this).data();
            $("#parts_id_select").val(trData.parts_code);
		    $("#part_nameAdd").val(trData.device_parts_name);
		    $("#part_idAdd").val(trData.parts_code);
		    $("#partUnit_priceAdd").val(trData.unit_price);
		    getMaxPartId()
            $('#partsModal').modal('hide');
        });
        function getMaxPartId(){
			$.ajax({
				url : config.basePath + '/entryAndOut/produceEntry/getMaxPartId',
				type:"GET",
				data:{"partId" : '%'+$("#part_idAdd").val()+'%'},
			success : function(result) {
				 $("#part_idAdd").val(result);
            },
            error:function(result) {
            	console.log(result);
            }
			});
		}
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
		 * 添加监听input  part_codeAdd值的改变事件
		 */
		$('#part_codeAdd').bind('input propertychange',function(){ 
			var tvalmum;
			//统计input输入字段的长度
			tvalnum = $(this).val().length;
			//如果大于30个字直接进行字符串截取
			if(tvalnum>30){          
				var tval = $(this).val();        
				tval = tval.substring(0,30);        
				$(this).val(tval);
				alert('长度超过限制！'); 
			}
		});
		/*******************************************************************************
		 * 
		 * 
		 */
		        var setting = {
		                async: {
		                    enable: true,
		                    url: config.basePath + '/entryAndOut/preparePurchase/getPartsZtree',
		                    type: 'get',
		                    dataFilter: function (treeId, parentNode, childNodes) {
		                        if (childNodes) {
		                            for (var i = 0; i < childNodes.length; i++) {
		                                if (!childNodes[i]['parentId']) {
		                                    childNodes[i]['parentId'] = 0;
		                                }
		                            }
		                        } else {
		                            childNodes = [];
		                        }
		                        return childNodes;
		                    }
		                },
		                data: {
		                    simpleData: {
		                        enable: true,
		                        idKey: 'id',
		                        pIdKey: 'parentId',
		                        rootPId: 0
		                    },
		                    key: {
		                        name: 'funcName'
		                    }
		                },
		                view: {
		                    selectedMulti: false,
		                },
		                callback: {
		                    onAsyncSuccess: function (event, treeId, treeNode, msg) {
		                        var zTree = $.fn.zTree.getZTreeObj(treeId);
		                        zTree.expandAll(false);
		                    },
		                   onClick: function (event, treeId, treeNode) {
		                	var parentNode=treeNode;
		                	var devicePartsName;
		                	var deviceTypeName;
		                	var deviceModelName;
		                	var supplierName;
		                	for(i=0;i<3;i++){
		                		var level=parentNode.funcLevel;
		                	 	switch(level){
		                		case 0:deviceTypeName=parentNode.funcName;
		                		       break;
		                		case 1:supplierName=parentNode.funcName;
		         		       break;
		                		case 2:deviceModelName=parentNode.funcName;
		         		       break;
		                		case 3:devicePartsName=parentNode.funcName;
		                		break;
		                		default:
		                		}
		                	 parentNode=parentNode.getParentNode();
		                    if(parentNode==null){
		                    	break;  	
		                	}}
		                    var params=$.param({
		                    	devicePartsName:devicePartsName,
		                    	deviceTypeName:deviceTypeName,
		                    	deviceModelName:deviceModelName,
		                    	supplierName:supplierName
		                    })
		                    tblPartsInfo.ajax.url(config.basePath + '/system/partsManage/getAllParts?'+params).load();
		                    // console.log(treeNode);
		                   }
		                }
		            };

		            $.fn.zTree.init($("#funcTree"), setting, null);
		            

		       
		 /**
		  * 定时隐藏alert框
		  */
		function hideTimeout(id,ms){
			var time=setTimeout(function(){
				$("#"+id).hide();
			},ms)
		}
        sheetDetailTable.on('draw.dt', function () {
            //给第一列编号
            sheetDetailTable.column(0, { search: 'applied', order: 'applied' }).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        });
    });
});