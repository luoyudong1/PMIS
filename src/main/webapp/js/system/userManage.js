/**
 * 用户管理Js改造
 */
require(['../config'], function (config) {

    require([ 'jquery','popper','bootstrap', 'common/dt','metisMenu','slimscroll','pace','inspinia'], function ($,Popper,Bootstrap, dataTable) {
        var user_id = window.parent.user.userId; // 登录人ID
        //var user_name = window.parent.user.userName; // 登录人名字
        //var roles= ''; // 登录人角色信息
        //var deptId = window.parent.user.deptId // 所属部门id
       //初始化部门下拉框
    	$.ajax({
            async : false,
            url : config.basePath + "/system/userManage/getAllDepot",
            data : {
            	"action" : "all"
            },
            dataType : 'json',
            success : function(result) {
            	for(var i=0;i<result.data.length;i++){
            		$("#userDepotId").append('<option value="'+result.data[i].depot_id+'">'+result.data[i].depot_name+'</option>');
            		$("#userDepotIdAdd").append('<option value="'+result.data[i].depot_id+'">'+result.data[i].depot_name+'</option>');
            	}
            },
            error:function(result) {
            	console.log(result);
            }
        });
	Popper.Defaults.modifiers.computeStyle.gpuAcceleration = false;
	
        var userTable = dataTable('tblUserInfo', {
            ajax: {
                url: config.basePath + '/system/userManage/list',
            },
            columns: [
                {data: null},
                {data: 'user_id'},
                {data: 'user_name'},
                {data: 'user_role_name'},
                {data: 'depot_name'},
                {data: 'workshop_name'},
                {data: 'team_name'},
                {
                    data: {
                        _: 'reg_date',
                        filter: 'reg_date',
                        display: 'reg_date_display'
                    }
                },
                {
                    data: {
                        _: 'last_login',
                        filter: 'last_login',
                        display: 'last_login_display'
                    }
                },
                {
                    data: 'login_status',
                    render: function (data) {
                        return data == 0 ? '<span class="text-danger">离线</span>' : '在线';
                    }
                }
            ],
            columnDefs: [
                {
                    targets: 10,
                    data: function (row) {
                        var operator = '<a class="modifyUser btn btn-info btn-xs" data-toggle="modal" href="#modifyUserModal" title="修改用户"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;'
                            + '&nbsp;&nbsp;' + '<a class="deleteUser btn btn-danger btn-xs" data-toggle="modal" href="#popModal" title="删除用户"><span class="glyphicon glyphicon-remove"></span></a>';
                        return operator;
                    }
                }
            ],
            ordering: false,
            serverSide: true,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            },
        });

        function loadUserRoleList(dom, async) {
            $.ajax({
                url: config.basePath + '/system/userManage/getRolesList',
                dataType: 'json',
                async: async,
                success: function (result) {
                    $('#' + dom).empty();
                    $.each(result, function(idx, obj) {
                        if (idx > 0 && idx % 3 == 0) {
                            $('#' + dom).append('<br/>');
                        }
                        $('#' + dom).append('<input type="checkbox" name="role_id" value="' + obj.role_id + '" title="' + obj.role_name + '"/>' + obj.role_name);
                    });
                }
            });
        }

        $('#addUserModal').on('show.bs.modal', function () {
            // 用户类型下拉列表roleList user_priority
            $('#roleCheckIdAdd').hide();
            loadUserRoleList('roleCheckIdAdd', true);
        });

		 //跟据条件查询
		 $('#btnQuery').on('click', function (e) {
			var user_name = $('#user_name').val();
			var params = {
					user_name:'%'+user_name+'%'
		           };
			queryDataByParams(params);
		  });
		//根据条件查询数据
		 function queryDataByParams(params){
			 userTable.settings()[0].ajax.data = function (d) {
		            return $.extend({}, d, params);
				 };
				 userTable.ajax.reload();
		 };
        
        /*用户名失去焦点验证是否存在此用户 */
        $("#useridAdd").on('blur', function () {
            var userId = $('#useridAdd').val();
            if ($.trim(userId) != "") {
                var params = $.param({
                    "userId" : userId
                });
                $.ajax({
                    url: config.basePath + '/system/userManage/find',
                    type : "post",
                    data : params,
                    dataType : "json",
                    success : function(msg) {
                        if (msg.code != null && msg.code != 0)	{
                            alert(msg.msg);
                            $("#useridAdd")[0].focus();
                        }
                    }
                });
            } else {
                alert("用户ID不能为空!");
                $("#useridAdd")[0].focus();
            }
        });

        $('#rolePicAdd').on('click', function() {
            $("#roleCheckIdAdd").toggle();
        });

        $('#rolePic').on('click', function () {
            $("#roleCheckId").toggle();
        });

        $("#btnAddOk").on('click', function (e) {
            e.preventDefault();

            var userRole = $('#roleCheckIdAdd input[type="checkbox"]:checked').map(function () {
                return $(this).val();
            }).get().join(',');

            var params = JSON.stringify({
                user_id: $('#useridAdd').val(),
                user_name: $('#usernameAdd').val(),
                user_pass: $('#userpasswordAdd').val(),
                user_role_name: userRole,
                enabled: $('#userenableAdd').val(),
                idx_url: config.init_idx_url,
                depot_id : $('#userDepotIdAdd').val(),
                workshop_id : $('#userWorkshopIdAdd').val(),
                team_id : $('#userTeamIdAdd').val(),
                dispatcher : $('#dispatcherAdd').val(),
            });

            $.ajax({
                url : config.basePath + '/system/userManage/create',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	userTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>用户信息添加成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });

        /* 响应刷新按钮 */
        $('#btnRefresh').click(function() {
            location.reload();
        });
        
        $('#modifyUserModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = userTable.row(tr).data();
            var depot_id = data.depot_id;
            var workshop_id = data.workshop_id;
            firstSel(depot_id);
            secondSel(workshop_id);
            $("#userenable").val(data.enabled);
            $("#userid").val(data.user_id);
            $("#username").val(data.user_name);
            $("#userpassword").val(data.user_pass);
            $("#userDepotId").val(data.depot_id);
            $("#userWorkshopId").val(data.workshop_id);
            $("#userTeamId").val(data.team_id);
            $('#roleCheckId').hide();
            loadUserRoleList('roleCheckId', false);
            if (data.user_role_name != null) {
                var userRoleName = data.user_role_name.split(',');
                userRoleName.map(function (value) {
                    $('#roleCheckId input[type="checkbox"][title="' + value +'"]').attr('checked', true);
                });
            }
        });

        $("#btnModifyOk").on('click', function (e) {
            e.preventDefault();

            var userRole = $('#roleCheckId input[type="checkbox"]:checked').map(function () {
                return $(this).val();
            }).get().join(',');

            var params = JSON.stringify({
                user_id: $('#userid').val(),
                user_name: $('#username').val(),
                user_pass: $('#userpassword').val(),
                user_role_name: userRole,
                enabled: $("#userenable").val(),
                depot_id : $('#userDepotId').val(),
                workshop_id : $('#userWorkshopId').val(),
                team_id : $('#userTeamId').val()
            });

            $.ajax({
                url: config.basePath + '/system/userManage/modify',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	userTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>用户信息修改成功！</strong></span>');
	   					$("#infoAlert").show();
	   					hideTimeout("infoAlert",2000);
                    }
                }
            });
        });

        $('#popModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = userTable.row(tr).data();
            $('#warningText').text('确定要删除用户ID为：' + data.user_id + '的用户？');
            $('#deleteUserId').val(data.user_id);
        });

        $('#btnPopOk').on('click', function (e) {
            e.preventDefault();
            var params = $.param({
                "userId" : $('#deleteUserId').val()
            });
            $.ajax({
                url: config.basePath + '/system/userManage/delete',
                type: 'POST',
                data: params,
                dataType: 'json',
                success: function(result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                    	userTable.ajax.reload();
                    	$("#alertMsg").html('<span style="color:green;text-align:center"><strong>用户信息删除成功！</strong></span>');
	   					$("#infoAlert").show();
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
		
		$("#userDepotIdAdd").change('click', function () {
			var depot_id = $("#userDepotIdAdd").val();//得到第一个添加下拉列表的值
        	firstSel(depot_id);
        	var workshop_id = $("#userWorkshopIdAdd").val();//得到第二个添加下拉列表的值
        	secondSel(workshop_id);
        });
		
		$("#userWorkshopIdAdd").change('click', function () {
			var workshop_id = $("#userWorkshopIdAdd").val();//得到第二个添加下拉列表的值
			secondSel(workshop_id);
        });
		
		$("#userDepotId").change('click', function () {
			var depot_id = $("#userDepotId").val();//得到第一个修改下拉列表的值
        	firstSel(depot_id);
        	var workshop_id = $("#userWorkshopId").val();//得到第二个修改下拉列表的值
        	secondSel(workshop_id);
        });
		
		$("#userWorkshopId").change('click', function () {
			var workshop_id = $("#userWorkshopId").val();//得到第二个修改下拉列表的值
			secondSel(workshop_id);
        });
		
		function firstSel(depot_id) {//如果第一个下拉列表的值改变则调用此方法
        	$('#userWorkshopIdAdd').empty();
        	$('#userWorkshopId').empty();
			    $.ajax({
			        async : false,
			        url : config.basePath + "/system/userManage/getAllWorkshop",
			        data : {
			        	depot_id : depot_id
			        },
			        dataType : 'json',
			        success : function(result) {
			        	$("#userWorkshopIdAdd").append('<option value="0"></option>');
			        	$("#userWorkshopId").append('<option value="0"></option>');
			        	for(var i=0;i<result.data.length;i++){
			        		$("#userWorkshopIdAdd").append('<option value="'+result.data[i].workshop_id+'" typeClass="workshop">'+result.data[i].workshop_name+'</option>');
			        		$("#userWorkshopId").append('<option value="'+result.data[i].workshop_id+'" typeClass="workshop">'+result.data[i].workshop_name+'</option>');
			        	}
			        },
			        error:function(result) {
			        	console.log(result);
			        }
			    });
    	};
    	
    	function secondSel(workshop_id) {//如果第一个下拉列表的值改变则调用此方法
        	$('#userTeamIdAdd').empty();
        	$('#userTeamId').empty();
			    $.ajax({
			        async : false,
			        url : config.basePath + "/system/userManage/getAllTeam",
			        data : {
			        	workshop_id : workshop_id
			        },
			        dataType : 'json',
			        success : function(result) {
			        	$("#userTeamIdAdd").append('<option value="0"></option>');
			        	$("#userTeamId").append('<option value="0"></option>');
			        	for(var i=0;i<result.data.length;i++){
			        		$("#userTeamIdAdd").append('<option value="'+result.data[i].team_id+'" typeClass="team">'+result.data[i].team_name+'</option>');
			        		$("#userTeamId").append('<option value="'+result.data[i].team_id+'" typeClass="team">'+result.data[i].team_name+'</option>');
			        	}
			        },
			        error:function(result) {
			        	console.log(result);
			        }
			    });
    	};
		
    });
});