/**
 * Created by YFZX-WB on 2017/4/1.
 */
require(['config'], function (config) {

    require(['jquery','common/nav','popper','toastr','pace','bootstrap','metisMenu','slimscroll','inspinia','common/dateFormat'], function ($,nav,Popper,toastr,pace) {
    var userName=null;

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
            var read="false";
            if (this.validate()) {
                $.ajax({
                    url: 'login',
                    type: 'POST',
                    contentType: 'application/json',
                    dataType : "json",
                    data: JSON.stringify({
                        read: read,
                    	userid: self.username.val(),
                    	password: self.password.val()
                    }),
                    success: function (result) {
                        self.showError(result.message);
        				if (result.errCode == 0) {
                            var url="./pages/logined.html";
                            window.location.href = url;
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
        init:function(){

            var read="true";
            $.ajax({
                url: 'login',
                type: 'POST',
                contentType: 'application/json',
                dataType : "json",
                data: JSON.stringify({
                    read: read,
                }),
                success: function (result) {
                    var version=result.version;
                    var update=result.update;
                    var updateTime=result.updateTime;
                    $('#unViewNum').text("提示：系统于 "+update+" "+updateTime+" 更新为 "+version+" 版本，如操作出现异常，请清除浏览器缓存、重启后使用。");
                },
                error: function(msg){

                }
            });
            console.log("haved enter!!!!!");

        },
    };

    var setToastr=function(){
        toastr.options = {
            timeOut: 1000
        };
    }

    setToastr();

    var start=function(){
        Login.init();
    }

    start();

    $('#btnLogin').on('click', function (e) {
        e.preventDefault();
        Login.hideError();
        Login.login();
    });


    $('#repair').on('click',function (e) {
        e.preventDefault();
        toastr.error("请您先登录", "提示：");
    });


     $('#dispatch').on('click',function (e) {
         e.preventDefault();
         toastr.error("请您先登录", "提示：");
     });




});

});