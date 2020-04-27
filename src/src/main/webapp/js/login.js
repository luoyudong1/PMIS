/**
 * Created by YFZX-WB on 2017/4/1.
 */
require(['config'], function (config) {

    require(['jquery','common/nav','popper','toastr','pace','bootstrap','metisMenu','slimscroll','inspinia','common/dateFormat'], function ($,nav,Popper,toastr,pace) {
    var userName=null;
    var userRole=[];
    var userRole_code=[];
    const Login = {
        username: $('#username'),
        password: $('#password'),
        errorInfo: {
            error1: '用户名密码不正确'
        },
        hasError: function (input) {
            input.parents('.form-group').addClass('has-error');
        },
        remError: function (input) {
            input.parents('.form-group').removeClass('has-error');
        },
        showError: function (error) {
            $('.my-login-tip').text(error).show();
        },
        hideError: function () {
            $('.my-login-tip').hide();
        },
        validate: function () {
            if ($.trim(this.username.val()) == '') {
                this.hasError(this.username);
            }
            if ($.trim(this.password.val()) == '') {
                this.hasError(this.password);
            }
            return this.username.val() && this.password.val();
        },
        login: function () {
            const self = this;
            if (this.validate()) {
                $.ajax({
                    url: 'login',
                    type: 'POST',
                    contentType: 'application/json',
                    dataType : "json",
                    data: JSON.stringify({
                    	userid: self.username.val(),
                    	password: self.password.val()
                    }),
                    success: function (result) {
                        self.showError(result.message);
        				if (result.errCode == 0) {
        				    userName=result.user_name;
                            for(var i=0;i<result.roles.length;i++){
                                userRole[i]=result.roles[i].role_id;
                                userRole_code[i]=result.roles[i].role_code;
                                console.log("userRole["+i+"]============="+userRole[i]);
                                console.log("userRole_code["+i+"]============="+userRole_code[i]);
                                if(userRole[i]==1||userRole[i]==3){
                                    $('.admin-hide').show();
                                }
                            }

                            $('#login_div').css("display","none");
                            $('#loginName').text(result.user_name);
                            $('#depotName').text(result.depotName);
                            $('#userRole').text(result.user_role_name);
                            $('#user-name').html(result.user_name);
                            $('#user-dept').html(result.depotName);
                            $('.hide').show();
                            self.showLogin();

        				}
                    },
        			error: function(msg){
                        self.showError("登录异常，请检查数据库");
        			}
                });
            }
        },
        quit:function () {

            window.location.href = config.basePath + "/logout";

        },
        showLogin:function () {
            toastr.options = {
                closeButton: true,
                progressBar: true,
                showMethod: 'slideDown',
                timeOut: 2000
            };
                toastr.success(userName +', 欢迎您登录5T综合管理信息系统!');
        }
    };

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

    $('#btnLogin').on('click', function (e) {
        e.preventDefault();
        Login.hideError();
        Login.login();
    });

    $('#btnQuit').on('click', function (e) {
        e.preventDefault();
        Login.quit();

    });

    $('#btnSet').on('click', function (e) {
        e.preventDefault();
        toOtherPage("./pages/index-set.html","SET");
    });


    $('#repair').on('click',function (e) {
        e.preventDefault();
        toOtherPage("./pages/index.html","PMIS");
    });


     $('#dispatch').on('click',function (e) {
         e.preventDefault();
         toOtherPage("./pages/index-dpch.html","TMIS")
     });

     function toOtherPage(url,pageName) {
         if(userName!=null){
             for(var i in userRole_code) {

                 if (userRole_code[i] == pageName) {
                     window.location.href = url+"? userRole="+userRole[i];
                     break;
                 }
             }
             toastr.warning("用户：" + userName + "，您未开通登录权限！");
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