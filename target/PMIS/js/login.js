/**
 * Created by YFZX-WB on 2017/4/1.
 */
require(['config'], function (config) {

    require(['jquery','common/nav','popper','toastr','pace','bootstrap','metisMenu','slimscroll','inspinia','common/dateFormat'], function ($,nav,Popper,toastr,pace) {
    var userName=null;
    var userRole=null;
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
            $('.login-tip').text(error).show();
        },
        hideError: function () {
            $('.login-tip').hide();
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
        				    userRole=result.user_role;



                            $('#login_div').css("display","none");
                            $('#loginName').text(result.user_name);
                            $('#depotName').text(result.depotName);
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

    $('#btnQuit1').on('click', function (e) {
        e.preventDefault();
        Login.quit();

    });



    $('#repair').on('click',function (e) {
        e.preventDefault();
        if(userName!=null){
            if(userRole!=null){
                if(userRole<10){
                    window.location.href = "./pages/index.html"
                }else{
                    toastr.warning("用户："+userName+"，您未开通登录权限！");
                }

            }
        }else{
            toastr.error("请您先登录", "提示：");
        }
    });


     $('#dispatch').on('click',function (e) {
            e.preventDefault();
            if(userName!=null){
                if(userRole!=null){
                    if(userRole>9||userRole==1){
                        window.location.href = "./pages/index-dpch.html"
                    }else{
                        toastr.warning("用户："+userName+"，您未开通登录权限！");
                    }

                }
            }else{
               toastr.error("请您先登录", "提示：");
            }
     });


});

});