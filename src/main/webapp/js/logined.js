/**
 * Created by YFZX-WB on 2017/4/1.
 */
require(['config'], function (config) {

    require(['jquery','common/nav','popper','toastr','pace','bootstrap','metisMenu','slimscroll','inspinia','common/dateFormat'], function ($,nav,Popper,toastr,pace) {
    var userName=null;
    var userRole=[];
    var userRole_code=[];

    function init() {
        var logined=0;
        var url = location.search;
        if (url.indexOf("?") !== -1) {
            var strs = url.substr(1); //substr()方法返回从参数值开始到结束的字符串；
            var logined = strs.split("=")[1];
        }

        $.ajax({
            url:  config.basePath +'/logined',
            type: 'POST',
            contentType: 'application/json',
            dataType : "json",
            data: JSON.stringify({
            }),
            success: function (result) {

                userName=result.user_name;
                for(var i=0;i<result.roles.length;i++){
                    userRole[i]=result.roles[i].role_id;
                    userRole_code[i]=result.roles[i].role_code;
                    if(userRole[i]==1||userRole[i]==3){
                        $('.admin-hide').show();
                    }
                }

                $('#loginName').text(result.user_name);
                $('#depotName').text(result.depotName);
                $('#userRole').text(result.user_role_name);
                $('#user-name').html(result.user_name);
                $('#user-dept').html(result.depotName);
                $('.hide').show();
                if(logined==0){
                    showLogin();
                }
                hint(result.depotId);

            },
            error: function(msg){

            }
        });
    }


    const Login = {

        quit:function () {

            window.location.href = config.basePath + "/logout";

        },

    };

    init();

        /**
         * 提示信息（该部门所有未完成操作信息）走马灯显示
         * @param depotName
         */
    function hint(depotId) {
        $.ajax({
            url: config.basePath + '/system/messageInfo/getHintInfoList',
            type: 'GET',
            data: {
                "depotId": depotId,
            },
            success: function (result) {
                var hintInfo="";
                if (result != null && result.data.length > 0) {
                    var HintInfos = result.data;
                    for(var i = 0; i < HintInfos.length; i++){
                            hintInfo=hintInfo+HintInfos[i].message_info+"; ";
                    }
                    $('#unViewNum').text("提示："+hintInfo);
                } else {
                    $('#unViewNum').text("");
                }

            }
        });

    }

    function showLogin() {
            toastr.options = {
                closeButton: true,
                progressBar: true,
                showMethod: 'slideDown',
                timeOut: 2000
            };
            toastr.success(userName +', 欢迎您登录5T综合管理信息系统!');
    }

    var setToastr=function(){
            toastr.options = {
                timeOut: 1000
            };
    }

    setToastr();

    $('.form-group input').keypress(function (e) {
        Login.remError($(this));
        if (e.which == 13) {
            Login.hideError();
            Login.login();
            return false;
        }
    });


    $('#btnQuit').on('click', function (e) {
        e.preventDefault();
        Login.quit();

    });

    $('#btnSet').on('click', function (e) {
        e.preventDefault();
        toOtherPage("./index-set.html","SET");
    });


    $('#repair').on('click',function (e) {
        e.preventDefault();
        toOtherPage("./index.html","PMIS");
    });


     $('#dispatch').on('click',function (e) {
         e.preventDefault();
         toOtherPage("./index-dpch.html","TMIS")
     });

     function toOtherPage(url,pageName) {
         var judge=false;
         if(userName!=null){
             $.ajax({
                 url:  config.basePath +'/loginAgain',
                 type: 'get',
                 async: false,
                 success: function (data) {
                     if (data) {
                         judge = true;
                     }
                 }
             });
             if(judge){
                 for(var i in userRole_code) {

                     if (userRole_code[i] == pageName) {
                         window.location.href = url+"? userRole="+userRole[i];
                         break;
                     }
                 }
                 toastr.warning("用户：" + userName + "，您未开通登录权限！");
             }else{
                 toastr.warning("用户：" + userName + "已超时退出，请重新登陆！");
                 window.location.href = config.basePath +'/login';
             }
         }else{
             toastr.error("请您先登录", "提示：");
         }
     }

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
                            userId: userName,
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


});

});