/**
 * Created by YFZX-WB on 2017/4/1.
 */
jQuery(function ($) {

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
                    success: function (msg) {
                        self.showError(msg.message);
        				if (msg.errCode == 0) {
        					window.location.href = "./pages/index.html";
        				}
                    },
        			error: function(msg){
                        self.showError("登录异常，请检查数据库");
        			}
                });
            }
        }
    };

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
});