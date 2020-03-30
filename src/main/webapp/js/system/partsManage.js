/**
 * 配件类别管理
 */
require(['../config'], function (config) {

    require([ 'jquery','popper','bootstrap', 'common/dt','common/commonMethod','metisMenu','lib/ajaxfileupload','slimscroll','pace','inspinia'], function ($,Popper,Bootstrap, dataTable,CMethod) {
    
    	Popper.Defaults.modifiers.computeStyle.gpuAcceleration = false;
	
    	//初始化厂家下拉框
    	$.ajax({
            async : false,
            url : config.basePath + "/entryAndOut/purchaseParts/getAllsuppliers",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#supplierNameAdd").append('<option value="'+result.data[i].supplier_id+'" supplierCode="'+result.data[i].supplier_code+'">'+result.data[i].supplier_name+'</option>');
            		//$("#supplierName").append('<option value="'+result.data[i].supplier_id+'">'+result.data[i].supplier_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	//初始化配件名称下拉框
    	$.ajax({
            async : false,
            url : config.basePath + "/system/partsManage/getAllDeviceName",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#partNameAdd").append('<option value="'+result.data[i].device_parts_id+'" deviceNameCode="'+result.data[i].device_parts_code+'">'+result.data[i].device_parts_name+'</option>');
            		//$("#partName").append('<option value="'+result.data[i].device_name+'" deviceNameCode="'+result.data[i].device_name_code+'">'+result.data[i].device_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	//初始化配件类型下拉框
    	$.ajax({
            async : false,
            url : config.basePath + "/system/partsManage/getAllDeviceType",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#partTypeAdd").append('<option value="'+result.data[i].device_type_id+'" deviceTypeCode="'+result.data[i].device_type_code+'">'+result.data[i].device_type_name+'</option>');
            		//$("#partType").append('<option value="'+result.data[i].device_type_code +'">'+result.data[i].device_type_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	//初始化配件型号下拉框
    	$.ajax({
            async : false,
            url : config.basePath + "/system/partsManage/getAllDeviceModel",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#partModelAdd").append('<option value="'+result.data[i].device_model_id+'" deviceModelCode="'+result.data[i].device_model_code+'">'+result.data[i].device_model_name+'</option>');
            		//$("#partModel").append('<option value="'+result.data[i].device_model_code +'">'+result.data[i].device_model+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
    	   $("#partModelAdd,#partNameAdd,#partTypeAdd,#supplierNameAdd").change(function(){
    		var partModel=$('#partModelAdd option:selected').text()
    		var partName=$('#partNameAdd option:selected').text()
    		var partType=$('#partTypeAdd option:selected').text()
    		var supplierName=$('#supplierNameAdd option:selected').text()
    		if(partModel!=''&&partName!=''&&partType!=''&&supplierName!=''){
    			var partModelCode=$('#partModelAdd option:selected').attr('deviceModelCode')
    			var partNameCode=$('#partNameAdd option:selected').attr('deviceNameCode')
    			var partTypeCode=$('#partTypeAdd option:selected').attr('deviceTypeCode')
    			var supplierNameCode=$('#supplierNameAdd option:selected').attr('supplierCode')
    			
    			$('#partIdAdd').val(supplierNameCode+partTypeCode+partModelCode+partNameCode)
    		}
    		

    		            });
//    	$("#partModel,#partName,#partType,#supplierName").change(function(){
//    		var partModel=$('#partModel option:selected').text()
//    		var partName=$('#partName option:selected').text()
//    		var partType=$('#partType option:selected').text()
//    		var supplierName=$('#supplierName option:selected').text()
//    		if(partModel!=''&&partName!=''&&partType!=''&&supplierName!=''){
//    			var partModelCode=$('#partModel option:selected').val()
//    			var partNameCode=$('#partName option:selected').attr('deviceNameCode')
//    			var partTypeCode=$('#partType option:selected').val()
//    			var supplierNameCode=$('#supplierName option:selected').val()
//    			
//    			$('#partId').val(supplierNameCode+partTypeCode+partModelCode+partNameCode)
//    		}
//    	 });
		//初始化配件表格
        var partsTable = dataTable('tblPartsInfo', {
            ajax: {
                url: config.basePath + '/system/partsManage/getAllParts',
            },
            columns: [
                {data: 'parts_id'},
                {data: 'parts_code'},
                {data: 'device_parts_name'},
                {data: 'device_type_name'},
                {data: 'device_model_name'},
                {data: 'unit_price'},
//                {data: 'unit'},
                {data: 'supplier_name'},
            ],
            columnDefs: [
				{
				    targets: 7,
				    data: function (row) {
			    		if(row.pic==null || row.pic==""){
					    	return '-';
			        	}else{
			        		return '<a data-toggle="modal"  class="lookMaterialPicture" href="#showMatImage" title="查看图片"><img src="../../images/allInfo.png" height="17" width="17"/></a>'
			        	}
				    }
				},
                {
                    targets: 8,
                    data: function (row) {
                        var operator = '<a class="modifyParts btn btn-info btn-xs" data-toggle="modal" href="#modifyPartsModal" title="修改配件"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            + '&nbsp;&nbsp;' + '<a class="deleteParts btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除配件"><span class="glyphicon glyphicon-remove"></span></a>';
                        return operator;
                    }
                }
            ],
            ordering : false,
			paging:true,
			pageLength: 10,
			serverSide : true,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });
        
        //partsTable.column(7).visible(false);//将图片列的数据隐藏

		 //跟据条件查询
		 $('#btnQuery').on('click', function (e) {
			var part_name = $('#partNameQuery').val();
			var part_id = $('#partIdQuery').val();
			var params = {
					part_name:'%'+part_name+'%',
					part_id : '%'+part_id+'%'
		           };
			queryDataByParams(params);
		  });
		//根据条件查询数据
		 function queryDataByParams(params){
			 partsTable.settings()[0].ajax.data = function (d) {
		            return $.extend({}, d, params);
				 };
				 partsTable.ajax.reload();
		 };
        
        //配件编号失去焦点验证是否存在此配件 
        $("#partIdAdd").on('blur', function () {
            var part_id = $('#partIdAdd').val();
            if ($.trim(part_id) != "") {
                var params = $.param({
                    "part_id" : part_id
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
                            $("#partIdAdd")[0].focus();
                        }
                    }
                });
            } else {
            	$("#alertMsgAdd").html("<font style='color:red'>配件编号不能为空！</font>");
 				$("#alertMsgAdd").css('display','inline-block');
 				CMethod.hideTimeout("alertMsgAdd","alertMsgAdd",3000);
                $("#partIdAdd")[0].focus();
            }
        });
        
        //配件名失去焦点验证是否为空 
        $("#partNameAdd").on('blur', function () {
            var part_name = $('#partNameAdd').val();
            if ($.trim(part_name) == "") {
            	$("#alertMsgAdd").html("<font style='color:red'>配件名称不能为空！</font>");
 				$("#alertMsgAdd").css('display','inline-block');
 				CMethod.hideTimeout("alertMsgAdd","alertMsgAdd",3000);
                $("#partIdAdd")[0].focus();
            }
        });
        
        //验证是否是数字类型
 		$("#partUnit_priceAdd").on("blur",function(e){
 			let partUnit_priceAdd=$("#partUnit_priceAdd").val();
 			if(CMethod.checkNumber(partUnit_priceAdd)){
 				CMethod.hideTimeout("alertMsgAdd","alertMsgAdd",0);
 			}else{
 				$("#alertMsgAdd").html("<font style='color:red'>单价请输入正数</font>");
 				$("#alertMsgAdd").css('display','inline-block');
 				CMethod.hideTimeout("alertMsgAdd","alertMsgAdd",3000);
 			}
 		});
 		
 		$("#partUnit_price").on("blur",function(e){
 			let partUnit_priceAdd=$("#partUnit_price").val();
 			if(CMethod.checkNumber(partUnit_priceAdd)){
 				CMethod.hideTimeout("alertMsgModify","alertMsgModify",0);
 			}else{
 				$("#alertMsgModify").html("<font style='color:red'>单价请输入正数</font>");
 				$("#alertMsgModify").css('display','inline-block')
 				CMethod.hideTimeout("alertMsgModify","alertMsgModify",3000);
 			}
 		});

//        $('#addPartsModal').on('show.bs.modal', function () {
//        	$('#partIdAdd').val('');
//        	$('#partNameAdd').val('');
//        	$('#partModelAdd').val('');
//        	$('#partUnit_priceAdd').val('');
////        	$('#partUnitAdd').val('');
//        	$('#partPicAdd').val('');
//        });

        $("#btnAddOk").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                parts_code : $('#partIdAdd').val(),
                device_parts_id : $('#partNameAdd').val(),
                device_type_id: $('#partTypeAdd').val(),
                device_model_id : $('#partModelAdd').val(),
                unit_price: $('#partUnit_priceAdd').val(),
//                "unit" : $('#partUnitAdd option:selected').text(),
                supplier_id : $('#supplierNameAdd').val()
            });

            $.ajax({
                url : config.basePath + '/system/partsManage/create',
                type : "POST",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	partsTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息添加成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
            $.ajaxFileUpload({
                url: config.basePath+'/system/partsManage/upload',
                type: 'post',
                data: {"parts_id": $('#partIdAdd').val(), "partImageName": $("#partPicAdd").val()},
                secureuri: false,
                fileElementId: 'partPicAdd',
                dataType: 'json',
                success: function (result) {
                    if(result==='success'){
                    	partsTable.ajax.reload();
                    }else{
                   	 alert("图片上传失败，请刷新后重试");
                    }
                },
                error: function (data, status, e) {
                    alert('系统错误：'+e);
                }
            });
        });

        //响应刷新按钮 
        $('#btnRefresh').click(function() {
            location.reload();
        });
        
        $('#modifyPartsModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = partsTable.row(tr).data();
            $("#partId").val(data.part_id);
            $("#partName").val(data.part_name);
            $("#partModel").val(data.device_model);
            $("#partUnit_price").val(data.unit_price);
//            $("#partUnit option:contains('"+ data.unit +"')").attr("selected",true);
            $("#supplierName").val(data.supplier_name);
            $("#partPic").val(data.pic);
        });

        $("#btnModifyOk").on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                part_id: $('#partId').val(),
//                part_name: $('#partName').text(),
//                device_model: $('#partModel').text(),
                unit_price: $('#partUnit_price').val(),
//              unit: $('#partUnit option:selected').text(),
//                supplier_id: $("#supplierName").val(),
                pic: $('#partPic').val(),
            });

            $.ajax({
                url: config.basePath + '/system/partsManage/modify',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	partsTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息修改成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });

        $('#popModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = partsTable.row(tr).data();
            $('#warningText').text('确定要删除配件ID为：' + data.part_id + '的配件？');
            $('#deletePartId').val(data.part_id);
        });

        $('#btnPopOk').on('click', function (e) {
            e.preventDefault();
            var params = JSON.stringify({
                "part_id" : $('#deletePartId').val()
            });
            $.ajax({
                url: config.basePath + '/system/partsManage/delete',
                type: 'POST',
                data: params,
                contentType : 'application/json',
                dataType: 'json',
                success: function(result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	partsTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>配件信息删除成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });
        
        //查看图片
        $('#partsTable tbody').on('click', '.lookMaterialPicture', function (e) {
        	$("#jszjImage").empty();
        	$(".carousel-indicators").empty();
        	var tr = $(e.currentTarget).parents('tr');
        	var data = partsTable.row(tr[0]).data();
        	var mpId;
        	var matImageName;
            if (!data.matImage && !data.mat_picture) {
            	alert("暂未上传物资图片！");
            	return;
            }else if(data.mat_picture){
            	 $("#matImage_delete").val(data.mat_picture);
             	 matImageName= data.mat_picture.split(";");
            }else if(data.matImage && !data.mat_picture){
            	 $("#matImage_delete").val(data.matImage);
            	 matImageName= data.matImage.split(";");
            }
            for (var pic in matImageName) {
            	mpId=matImageName[pic2].substring(matImageName[pic2].indexOf('-')+1,matImageName[pic2].indexOf('.'))
            	if(mpId.length==12){
            		mpId+='0';
            	}
            	if(pic2==0){
    				$("#jszjImage").append('<div class="item active">'+
    						'<img src="'+config.basePath+"/material/materialInfo/download/"+mpId+"/"+matImageName[pic2]+'"/>'
    						+'</div>');
    				$(".carousel-indicators").append('<li data-target="#myCarousel" data-slide-to="'+pic2+'" class="active"></li>');
				}else{
					$("#jszjImage").append('<div class="item">'+
							'<img src="'+config.basePath+"/material/materialInfo/download/"+mpId+"/"+matImageName[pic2]+'"/>'
							+'</div>');	
					$(".carousel-indicators").append('<li data-target="#myCarousel" data-slide-to="'+pic2+'"></li>');
				}
            }
            $("#showMatImage").modal("show");
        });
        
        //图片格式验证
        function checkImageType(fileID, tishiId){
 			 var files=$("#"+fileID)[0].files;
        	 for(var i=0;i<files.length;i++){
        		 var type=files[i].type;
        		 var size=files[i].size;
        		 if(type.indexOf('image')==-1){
        			 $('#' + tishiId).html(files[i].name + "不是图片，请选择图片格式文件！");
                  	 return false;
        		 }
        		 if(size>20971520){
        			 $('#' + tishiId).html(files[i].name + "图片大小超过20MB，无法上传！");
                    return false;
        			 
        		 }
        	 }
   	     return true;
 		}
        
        //定时隐藏alert框
		function hideTimeout(id,ms){
			var time=setTimeout(function(){
				$("#"+id).hide();
			},ms)
		}
    });
});