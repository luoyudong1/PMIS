/**
 * Created by YFZX-WB on 2017/3/31.
 */
var currentModal="";  //当前模块
var currentParent="";//左侧主菜单
var curr_fun = "";//点击功能id
var Msg_Clicked=0;      // 消息已被点击查看的数量
var Msg_numb=["0","0","0","0"];
var Sub_count=["0","0","0","0"];
var auths = {};
require(['config'], function (config) {

	require(['jquery','common/nav','popper','toastr','pace','bootstrap','metisMenu','slimscroll','inspinia','common/dateFormat'], function ($,nav,Popper,toastr,pace) {
        var newWindow=''   //新窗口
        var oldWindow=''   //旧窗口


		function init () {
            //-------------------------------------------------------------------
            var user = {};
            var userRole=[];
            var url = location.search;
            if ( url.indexOf( "?" ) !== -1 ) {
                var str = url.substr( 1 ); //substr()方法返回从参数值开始到结束的字符串；
                var strs = str.split( "&" );
                for ( var i = 0; i < strs.length; i++ ) {
                       userRole[i]=  strs[ i ].split( "=" )[ 1 ];                       // theRequest[ (strs[ i ].split( "=" )[ 0 ]).toLowerCase() ] = ( strs[ i ].split( "=" )[ 1 ] );
                }
                console.log( userRole[0]);
            }

            $.ajax({
                /*
                async : false,
                url : config.basePath + "/user/getRoleIDbyUser?_=" + new Date().getTime(),
                dataType : 'json',
                */
                async : false,
                url: config.basePath + "/user/getRoleIDbyUserRole?_=" + new Date().getTime(),
                type: 'POST',
                contentType: 'application/json',
                dataType : "json",
                data: JSON.stringify({
                    userRole: userRole[0],
                }),
                success : function(result) {
                    user.deptId=result.deptId;                       //部门id
                    user.userId = result.userId;                     //用户ID
                    user.userName = result.userName;                 //用户名
                    user.roleName = result.roles.role_name;          //用户角色名                         //user.departmentId=result.departmentId; //dujun  2018-07-16
                    user.roleId = result.roles.role_id;              //用户角色ID
                    user.roleCode=result.roles.role_code;            //用户角色CODE
                    user.menus = result.menus;                       //用户菜单
                    auths = result.auths;
                    user.idxUrl = result.idxUrl;//此处的idxUrl是功能id
                    window.self.user=user;   //dujun  2018-07-16

                    var validate = false;
                    if (result.menus) {
                        $("#navTop").append('<li>|</li>');
                        $.each(result.menus, function(i, menu) {
                            //顶部一级模块
                            $("#navTop").append('<li role="' + menu.code + '"><a href="#">' + '<span style="color:black">'  + menu.name +'</span></a></li><li>|</li>');
                            $.each(menu.children || [], function (idx, item) {
                                $('#side-menu').append('<li class="memu_parent_' + menu.code + '" style="display:none">'
                                    +'<a href="#' + item.code + '" role="' + menu.code + '" ><i class="fa '+ buildIcon(item.code) +'"></i> <span class="nav-label">' + item.name + '</span>'
                                    +'<span class="fa arrow"></span></a>'
                                    +'<ul class="nav nav-second-level" id="menu_'+item.code+'">'
                                    +'</ul>'
                                    +'</li>');
                                var menuId=$('#menu_'+item.code);
                                //左侧二级菜单
                                $.each(item.children || [], function (idx3, item3) {
                                    if (parseInt(result.idxUrl) === item3.id){
                                        validate = true;
                                        $("#navTop li[role='"+menu.code+"']").css("background-color","#1ab394");
                                        $("#navTop li[role='"+menu.code+"'] a").css("color","white");
                                        currentModal = menu.code ;
                                        currentParent = item.code;
                                    }
                                    //左侧功能
                                    menuId.append('<li><a href="#fun_' + item3.id + '" role="' +menu.code + '"data-parentrole="'+item3.code+'" data-toggle="tab">' + item3.name + '</a></li>')
                                });
                            });
                        });
                    }
                    if (!validate) {
                        user.idxUrl = result.menus[0] && result.menus[0].children[0] ? result.menus[0].children[0].children[0].id : '11';
                        currentModal = result.menus[0].code;
                        currentParent = result.menus[0].children[0].code;
                        $("#navTop li[role='"+result.menus[0].code+"']").css("background-color","#1ab394");
                        $("#navTop li[role='"+result.menus[0].children[0].code+"'] a").css("color","white");
                    }
                    $("#side-menu .memu_parent_" + currentModal).show();
                    $('#userName').text(user.userName); // 显示登录用户
                    $('#roleName').text(user.roleName); // 显示登录用户角色
                    $('#quit1,#quit2').attr('href', config.basePath + '/logout');
                },
                error : function(msg) {

                    document.location.href = config.basePath;
                }
            });

            return user;
        }

        var user = init();

        Popper.Defaults.modifiers.computeStyle.gpuAcceleration = false;
        //页面加载进度条
        pace.start({
            document: false
        });


        //右上角提示信息
        var msgInfos =function(){
            console.log("user.deptId====="+user.deptId);
            console.log("user.roleId========"+user.roleId);
            console.log("user.roleCode========"+user.roleCode);
            $.ajax({
                url: config.basePath + '/system/messageInfo/getMessageInfoList',
                type: 'GET',
                data: {
                    "depotId": user.deptId,
                    "roleId": user.roleId,
                    "roleCode":user.roleCode
                },
                success: function (result) {
//	            if($('#MsgInfoNumb').text() == null || result.data.length > $('#MsgInfoNumb').text()){
//	            };

                    $('#MegInfos').empty();
                    if (result != null && result.data.length > 0) {
                        var MsgInfos = result.data;
                        var MsgNumb = 0;
                        var id=0;
                        var judge= true;
                        for(var i=0;i<MsgInfos.length;i++){
                            //		$('#MegInfos').append("<li style='background: rgba(255,250,231,0.7);'><span style='color:red'>"+MsgInfos[i].message_info+"</span>")
                            id=MsgInfos[i].message_id;
                            if(MsgInfos[i].message_state != Msg_numb[id]){
                                if(MsgInfos[i].message_state<Msg_numb[id]){
                                    judge=false;
                                }
                                Msg_numb[id] = MsgInfos[i].message_state;
                                Sub_count[id]="0";
                                //被点击后的消息数
                                Msg_Clicked= Msg_Clicked+Msg_numb[id];
                            }

                            //实际消息数量
                            MsgNumb=MsgNumb+MsgInfos[i].message_state;
                            $('#MegInfos').append("<li> <a id='"+MsgInfos[i].message_id+"' href='"+MsgInfos[i].message_state+"' class='dropdown-item'> <div><i class='fa fa-bell fa-fw'></i> "+MsgInfos[i].message_info+"</div> </a></li>");  //显示提示信息
                        }

                        $("#MsgInfoNumb").html(MsgNumb);
                        //$(".fa-bell").css("color","#f27459");
                        if(Msg_Clicked>0&&judge) {
                            $("#wifi1").attr("class", "wifi-circle first");
                            $("#wifi2").attr("class", "wifi-circle second");
                            $("#wifi3").attr("class", "wifi-circle third");    //此处喇叭图标开始动态
                            $('audio').remove();

                            var audioElementHovertree = document.createElement('audio');
                            audioElementHovertree.setAttribute('src', config.basePath+'/sound/message.mp3');
                            audioElementHovertree.setAttribute('autoplay', 'autoplay');    //打开自动播放报警声音            //audioElement.load();

                        }else {
                            $("#wifi1").attr("class", "wifi-circle first-0");
                            $("#wifi2").attr("class", "wifi-circle second-0");
                            $("#wifi3").attr("class", "wifi-circle third-0");    //此处喇叭取消动态
                        }

                    } else {
                        $("#MsgInfoNumb").html(0);
                        //$(".fa-bell").css("color","#b9b1ab");
                        $("#wifi1").attr("class","wifi-circle-0 first-0");
                        $("#wifi2").attr("class","wifi-circle-0 second-0");
                        $("#wifi3").attr("class","wifi-circle-0 third-0");    //此处喇叭动态结束，颜色复原
                    }
                }
            });
        }

        //点击上方模块切换
        $("#navTop li a").click(function(){
        	currentModal = $(this).parent().attr('role');
        	$(this).parent().css("background-color","#1ab394");
        	$(this).css("color","white");
        	$(this).parent().siblings().css("background-color","");
        	$(this).parent().siblings().find('a').css("color","");
        	$("#side-menu ").children("li:gt(0)").hide();
        	$("#side-menu .memu_parent_" + currentModal).slideDown();
        });

        //显示消息被选中
        $('#MegInfos').on('click', 'a', function (e) {
            e.preventDefault();
            //e.stopPropagation();
            var subNmb=$(this).attr("href");
            var msgId=$(this).attr("id");
            switch(true){
                case user.deptId==3:     //机辆检测所用户
                    switch (user.roleId) {
                        case 2:      //库管人员
                            if(msgId==0){    //所未接收车间返修的单据
                                $("#navTop li a").eq(2).click();
                                var idxUrl = '188';
                                currentParent = 'stock5';
                                break;
                            }
                            if(msgId==1){   //未接收所内检修出库审核
                                $("#navTop li a").eq(1).click();
                                var idxUrl = '232';
                                currentParent = 'repaireManage5';
                                break;
                            }
                            break;
                        case 3:  //审核人员
                            if(msgId==0){  //所未审核车间返修的单据
                                $("#navTop li a").eq(2).click();
                                var idxUrl = '189';
                                currentParent = 'stock5';
                                break;
                            }
                            if(msgId==1){   //所未审核所配送到车间调拨的单据
                                $("#navTop li a").eq(2).click();
                                var idxUrl = '180';
                                currentParent = 'stock2';
                                break;
                            }
                            if(msgId==2){   //所未审核所报废配送到车间调拨的单据
                                $("#navTop li a").eq(0).click();
                                var idxUrl = '152';
                                currentParent = 'entryAndOut5';
                                break;
                            }
                            break;
                        case 4:   //检修人员
                            if(msgId==0) {   //所内未审核的送修单据为
                                $("#navTop li a").eq(0).click();
                                var idxUrl = '162';
                                currentParent = 'repaireManage1';
                                break;
                            }
                            break;
                    }
                case(user.deptId>7&&user.deptId<16):  //车间用户
                    switch (user.roleId) {
                        case 6:      //车间录入人员
                            if (msgId==0) {    //车间未接收所调拨的单据
                                $("#navTop li a").eq(1).click();
                                var idxUrl = '281';
                                currentParent = 'stock8';
                                break;
                            }
                            if(msgId==1){   //车间未接收所报废调拨的单据
                                $("#navTop li a").eq(1).click();
                                var idxUrl = '271';
                                currentParent = 'stock7';
                                break;
                            }
                            break;
                        case 7:
                            if(msgId==0){  //车间未审核所调拨的单据
                                $("#navTop li a").eq(0).click();
                                var idxUrl = '282';
                                currentParent = 'stock8';
                                break;
                            }
                            if(msgId==1){   //车间未审核所报废调拨的单据
                                $("#navTop li a").eq(0).click();
                                var idxUrl = '272';
                                currentParent = 'stock7';
                                break;
                            }
                            if(msgId==2){   //车间未审核返修调拨的单据
                                $("#navTop li a").eq(0).click();
                                var idxUrl = '186';
                                currentParent = 'stock4';
                                break;
                            }
                            break;
                    }
                    break;
                case (user.deptId>15&&user.deptId<50):
                    switch (user.roleId) {
                        case 9:
                            $("#navTop li a").eq(2).click();
                            var idxUrl = '255';
                            currentParent = 'detectProduce2';

                            break;
                    }
                    break;
                default:
                    break;
            }
            $('#side-menu a[href="#'+currentParent+'"]').click();
            nav.openTab(idxUrl);
            if(Sub_count[msgId]==0){
                Msg_Clicked=Msg_Clicked-subNmb;
            }
            Sub_count[msgId]='1';
            msgInfos();
/**
            $.ajax({
                url: config.basePath + '/system/messageInfo/updateMessageState',
                type: 'POST',
                data: $.param({message_id:$(this).attr('id').split("_")[1]}),
                dataType: 'json',
                success: function (result) {
                    if(result.code == 0){msgInfos();}
                }
            });
 **/
        });

        //每10分钟执行一次
        msgInfos();
        window.setInterval(msgInfos,500000);

        //显示主界面
        $('a[data-toggle="tab"]').on('shown.bs.tab', function () {
        	curr_fun = $(this).attr('href').substr(5);
        	if($(this).attr('data-parentrole') == 'dataAnalyse/deviceBi' || $(this).attr('data-parentrole') == 'dataAnalyse/vehTXDSFaultStatistics'){
        	    window.open(config.basePath+'/pages/'+$(this).attr('data-parentrole')+'.html', '_blank');
        		$("ul[id^='menu'] li a").removeClass('active');
        	}else{
	        	//功能id,功能的最顶层父级菜单url,功能url,功能名称
	        	nav.createTab($(this).attr('href').substr(1), $(this).attr('role'), $(this).attr('data-parentrole'),$(this).text());
                if(newWindow!=''&&newWindow!=null) {
                    var hrefDom = newWindow.attr('href').substring(1)
                    nav.closeTab(hrefDom);
                }
                newWindow=$(this)

        	}
        });

        //关闭功能
        $("#navTabs").on('click', 'i', function (e) {
            e.stopPropagation();
            nav.closeTab($(this).parent().attr('href').substr(1));
        });

        $('#navTabs').on('click', 'a', function (e) {
            $('#navTabs li').removeClass('active');
            $("ul[id^='menu'] li a").removeClass('active');
            $('#navTabs a[href="' + $(this).attr('href') + '"]').parents('li').addClass('active');
        });
        //默认展开首页
        $('#side-menu a[href="#'+currentParent+'"]').parent().addClass('active');
        nav.openTab(user.idxUrl);

        $('#userInfo').on('show.bs.modal', function () {
            $("#user-name").text(user.userName);
            $("#user-role").text(user.roleName);
            $("#user-index").empty();
            $.each(user.menus, function (idx, menu) {
                $.each(menu.children, function (i, item) {
                	$.each(item.children, function (j, item3) {
                        $('#user-index').append('<option value="' + item3.id + '">' + item3.name + '</option>')
                    });
                });
            });
            $('#user-index').val(user.idxUrl);
        });

        $("#subIdx").on('click', function (e) {
            e.preventDefault();
            //判断修改首页是否与当前首页相同
            if(user.idxUrl == $('#user-index').val()){
            	$('#updateModal').modal('show');
            }
            if (user.idxUrl != $('#user-index').val()) {
                $.ajax({
                    url: config.basePath + '/user/modifyIdxUrl',
                    type: 'POST',
                    data: $.param({
                        userId: user.userId,
                        idxUrl: $('#user-index').val()
                    }),
                    dataType: 'json',
                    success: function (result) {
                        if (result.code != 0) {
                            alert(result.msg);
                        } else {
                            user.idxUrl = $('#user-index').val();
                          //首页修改完成后自动刷新页面
                            window.location.reload();
                        }
                    }
                });
            }
        });

        $("#btnModifyPsOk").on('click', function (e) {
            e.preventDefault();
            if ($('#old-ps').val() == '' || $('#new-ps').val() == "" || $('#cf-ps').val() == "") {
            	$('#noModal').modal('show');
                //清空密码输入框
                $('#old-ps').val("");
                $('#new-ps').val("");
                $('#cf-ps').val("");
                return false;
            } else {
                if ($('#new-ps').val() != $('#cf-ps').val()) {
                	$('#noSameModal').modal('show');
                    //清空密码输入框
                    $('#old-ps').val("");
                    $('#new-ps').val("");
                    $('#cf-ps').val("");
                    return false;
                } else {
                    $.ajax({
                        url: config.basePath + '/user/modifyPassword',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify({
                            userId: user.userId,
                            password: $('#old-ps').val(),
                            newPas: $('#new-ps').val()
                        }),
                        dataType: "json",
                        success: function(result) {
                            if (result.code != 0) {
                                alert(result.msg);
                                //清空密码输入框
                                $('#old-ps').val("");
                                $('#new-ps').val("");
                                $('#cf-ps').val("");
                                return false;
                            } else {
                            	$('#updatePassModal').modal('show');
                                //清空密码输入框
                                $('#old-ps').val("");
                                $('#new-ps').val("");
                                $('#cf-ps').val("");
                                //隐藏密码修改页面
                                $('#userInfo').modal('hide');
                            }
                        }
                    });
                }
            }
        });

        //构建左侧小图标，二级菜单的
        function buildIcon(role){
        	var ret="fa-th-large"; //默认
        	switch(role){
        	   case 'dispatchDirect':  //调度管理
        		   ret = 'fa-recycle'; //图表
        		   break;
        	   case 'safeManage':  //风险管控
        		   ret = 'fa-cogs'; //齿轮
        		   break;
        	   case 'safeManage2':  //隐患整改
        		   ret = 'fa-wrench'; //扳手
        		   break;
        	   case 'safeManage3':  //信息追踪
        		   ret = 'fa-mail-reply-all';
        		   break;
        	   case 'safeManage4':  //预警通知
        		   ret = 'fa-bell'; //铃铛
        		   break;
        	   case 'safeManage5':  //干部考核
        		   ret = 'fa-users'; //人员
        		   break;
        	   case 'safeManage6':  //应急处理
        		   ret = 'fa-history';
        		   break;
        	   case 'constructManage3':  //施工计划
        		   ret = 'fa-clone';
        		   break;
        	   case 'devManage':  //设备管理
        		   ret = 'fa-tachometer';
        		   break;
        	   case 'devManage5':  //统计分析
        		   ret = 'fa-bar-chart-o'; //图表
        		   break;
        	   case 'materialsManage1':  //出入库管理
        		   ret = 'fa-archive';
        		   break;
        	   case 'materialsManage2':  //材料台账
        		   ret = 'fa-film';
        		   break;
        	   case 'materialsManage3':  //统计报表
        		   ret = 'fa-bar-chart-o'; //图表
        		   break;
        	   case 'taskReport1':  //生产报表
        		   ret = 'fa-newspaper-o';
        		   break;
        	   case 'taskReport2':  //生产录入
        		   ret = 'fa-pencil-square-o';
        		   break;
        	   case 'repairVehicleManager':  //检修车管理
        		   ret = 'fa-gamepad';
        		   break;
        	   case 'focusVehTrack':  //重点车追踪
        		   ret = 'fa-random'; //车辆
        		   break;
        	   case 'devManage4':  //临修管理
        		   ret = 'fa-gavel';
        		   break;
        	   case 'devManage3':  //配件管理
        		   ret = 'fa-shopping-bag';
        		   break;
        	   case 'devManage2':  //设备履历
        		   ret = 'fa-server';
        		   break;
        	   case 'constructManage1':  //设备管理
        		   ret = 'fa-puzzle-piece';
        		   break;
        	   case 'constructManage4':  //故障管理
        		   ret = 'fa-minus-square';
        		   break;
        	   case 'constructManage2':  //天窗计划
        		   ret = 'fa-building';
        		   break;
        	   case 'constructManage6':  //统计分析
        		   ret = 'fa-bar-chart-o'; //图表
        		   break;
        	   case 'system1':  //用户权限
        		   ret = 'fa-user-times';
        		   break;
        	   case 'basicDataManage':  //基础数据管理
        		   ret = 'fa-keyboard-o'; //车辆
        		   break;
        	}
        	return ret;
        }
    });

});