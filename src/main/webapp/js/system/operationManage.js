require([ '../config' ], function(config) {
	require([ 'jquery', 'bootstrap', 'common/dt' ], function($, Bootstrap,dataTable) {
		var roleid = "";
		/**
		 * 初始化用户列表
		 */
		var roleInfoTable = dataTable('tblRoleInfo', {
			ajax : {
				url : config.basePath + '/system/roleManage/getRoleList'
			},
			columns : [ {data:null},
			            {
				data : 'role_id'
			}, {
				data : 'role_name'
			} ],
			columnDefs : [ {
				targets : 0,
				orderable : false
			} ],
			order : [ [ 1, 'asc' ] ],
			fnDrawCallback:function () {
		        let api = this.api();
		        api.column(0).nodes().each(function(cell, i) {
		            cell.innerHTML = i + 1;
		        });
		    },
			searching : false,
			lengthChange : false,
			info : false,
			paging : false
		});
		/**
		 * 初始化功能列表
		 */
		var operInfoTable = dataTable('tblOperInfo', {
			columns : [ {
				data : 'func_id'
			}, {
				data : 'parent_name'
			}, {
				data : 'func_name'
			}, {
				data : 'func_url'
			}, {
				data : 'perm_name',
            	render:function(data){
            		if(data == null){
            			return '';
            		}else{
            			return data;
            		}
            	}
			} ],
			columnDefs : [ {
				targets : 0,
				orderable : false
			} ],
			order : [ [ 1, 'asc' ] ],
			searching : false,
			lengthChange : false,
			info : false,
			paging : false
		});

		$('#tblRoleInfo tbody').on('click', 'tr', function() {
			if (roleInfoTable.data().any()) {
				var trData = roleInfoTable.row(this).data();
				roleid = trData.role_id;
				showFcuntionInfo(trData.role_id);
			}
		});
		
		$('#tblOperInfo tbody').on('click', 'tr', function() {
			if (operInfoTable.data().any()) {
				var trData = operInfoTable.row(this).data();
				var permname = trData.perm_name;//操作代码
				$('#modifyOperModal').modal('show');
				$('.newadd').remove();//去除上次的元素
				intiipnum = 0;
				$("#addAuth input").each(function(){
					if(intiipnum > 1){
						this.value = '';
					}
					intiipnum ++;
				});	
				$("#addAuth select").each(function(){
					this.value = 1;
				});	
				$("#func_id").val(trData.func_id);//功能id
				$("#role_id").val(roleid);//角色id
				if(permname != null && permname != ''){
					var pername = permname.split(";");
					for(var i=1; i<pername.length; i++){
						var linehtml = "<div class='input-group newadd'> <label style='margin:8px;'><strong>启用：</strong></label> <select type='text' class='form-control text-center'	style='width: 30px;'> <option value=1>可用</option> <option value=0>不可用</option></select>&nbsp;&nbsp;<label style='margin:8px;'><strong>操作权限：</strong></label>	<input type='text' class='form-control' placeholder='禁止操作的按钮id或class' style='width: 120px;height:32.8px;'/></div>";
						$("#addAuth").append(linehtml);	
					}
					var ipnumit = 0;
					var stnumit = 0;
					$("#addAuth input").each(function(){
						if(ipnumit > 1){
							this.value = pername[ipnumit -2].split(':')[1];
						}
						ipnumit ++;
					});	
					$("#addAuth select").each(function(){
						this.value = pername[stnumit].split(':')[0];
						stnumit ++;
					});						
				}			
			}
		});
		
		function showFcuntionInfo(role_id) {
			operInfoTable.ajax.url(
					config.basePath
							+ '/system/roleManage/queryFuncPermByRole?role_id='+ role_id).load();
		}
		//添加一个元素的输入框
		$("#addAuthline").on('click', function (e) {
			var linehtml = "<div class='input-group newadd'> <label style='margin:8px;'><strong>启用：</strong></label> <select type='text' class='form-control text-center'	style='width: 30px;'> <option value=1>可用</option> <option value=0>不可用</option></select>&nbsp;&nbsp;<label style='margin:8px;'><strong>操作权限：</strong></label>	<input type='text' class='form-control' placeholder='禁止操作的按钮id或class' style='width: 120px;height:32.8px;'/></div>";
			$("#addAuth").append(linehtml);			
		});		
		//获得元素值
		function getAllInputInfo() {
			var ipnum = 0;
			var stnum = 0;
			var ipvue = [];
			var stvue = [];
			var optstr = "";
			$("#addAuth input").each(function(){
				if(ipnum > 1){
					ipvue.push(this.value);
				}
				ipnum ++;
			});	
			$("#addAuth select").each(function(){
				stvue.push(this.value);
			});	
			for(var i=0; i<ipvue.length; i++){
				if(ipvue[i] != null && ipvue[i].trim() != ''){
					stnum ++;
					if(stnum != ipvue.length){
						optstr = optstr + stvue[i]+":"+ipvue[i] + ";";
					}else{
						optstr = optstr + stvue[i]+":"+ipvue[i] 
					}					
				}				
        	}
			return optstr;
		};
		
		$("#btnModifyOk").on('click', function (e) {
            e.preventDefault();
            var ptstr = getAllInputInfo();
            var params = {
            	"func_id": $('#func_id').val(),
                "role_id": $('#role_id').val(),
                "perm_name": ptstr
            };          
            $.ajax({
                url : config.basePath + '/system/roleManage/modifyPermission',
                type : "post",
                data : params,
                dataType : "json",
                success : function (result) {
                    if (result.code != 1) {
                    	operInfoTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>用户权限信息添加失败！</strong></span>');
	   					$("#infoAlert").show();
	   					$("#infoAlert").css("z-index","99");
	   					hideTimeout("infoAlert",2000);
                    } else {
                    	operInfoTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>用户权限信息添加成功！</strong></span>');
	   					$("#infoAlert").show();
	   					$("#infoAlert").css("z-index","99");
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });
        //定时隐藏alert框
		function hideTimeout(id,ms){
			var time=setTimeout(function(){
				$("#"+id).hide();
			},ms)
		}
	});
});