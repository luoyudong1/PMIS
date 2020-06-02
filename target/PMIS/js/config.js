/**
 * Created by YFZX-WB on 2017/4/7.
 * 说明：
 * （1）require加载的模块，必须是按照AMD规范，例如用define()函数定义的模块（Jquery），对于一些非AMD规范的模块，要采用shim的方式定义他们的特征
 * （2）符合AMD写法的define与require都能加载对应的依赖，define与require不同的就是，如果想要被别的module引用则要用define，写法上其多出来一个接口的返回，
 *     如果把define写成require后，则别的module引用不到本次定义的module。
 */

define(function () {
    // 获取当前网址，如：http://localhost:8080/TSIP/portal.html
    var currentPath = window.document.location.href;
    // 获取主机地址之后的目录，如：/TSIP/portal.html
    var pathName = window.document.location.pathname;
    var pos = currentPath.indexOf(pathName);
    // 获取主机地址，如：http://localhost:8080
    var basePath = currentPath.substring(0, pos);
    if (pathName.toUpperCase().indexOf('PMIS') != -1) {
        basePath += pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    }
    require.config({
        baseUrl: basePath + '/js',
        paths: {
            'jquery': 'jquery-3.1.1.min',
            'popper':'popper.min',
            'bootstrap': 'bootstrap',
            'inspinia':'inspinia',
            'datatables.net': 'plugins/dataTables/jquery.dataTables.min',
            'datatables': 'plugins/dataTables/dataTables.bootstrap4.min',
            'metisMenu':'plugins/metisMenu/jquery.metisMenu',
            'slimscroll':'plugins/slimscroll/jquery.slimscroll.min',
            'jquery-mousewheel': 'lib/jquery.mousewheel.min',
            'datetimepicker': 'lib/jquery.datetimepicker.full.min',
            'datetimepicker2': 'lib/bootstrap-datetimepicker.min',
            'datetimepicker2.zn-ch': 'lib/bootstrap-datetimepicker.zn-ch',
            'pace':'plugins/pace/pace.min',
            'ajaxfileupload':'ajaxfileupload',
            'toastr':'plugins/toastr/toastr.min',
            'common': './common',
            'zTree': 'jquery.ztree.all.min',
            'zTreeExhide': 'jquery.ztree.exhide.min',
            'myEcharts':'echarts',
            'wordexport':'./common/jquery.wordexport',
            'printArea':'./common/jquery.PrintArea',
        },
        shim: {
        	'bootstrap': ['jquery'],
        	'slimscroll': ['jquery'],
        	'inspinia': ['jquery','bootstrap'],
        	'zTree': ['jquery'],
        	'zTreeExhide': ['zTree'],
        	'wordexport': ['jquery'],
        	'printArea': ['jquery']
        },
        map: {
            '*': {
              'popper.js': 'popper'
            }
        }
    });

    require(['jquery'], function ($) {
        $(document).ajaxError(function (event, xhr, options, thrownError) {
            console.log('ajax error url: ' + options.url.substr(0, options.url.indexOf('?')) + '; status code: ' + xhr.status);
            if (xhr) {
                if (xhr.status === 403) {
                    top.location.href = basePath + '/error/unauth.html';
                } else if (xhr.status === 404) {
                    top.location.href = basePath + '/error/404.html';
                } else {
                    // top.location.href = basePath + '/logout';
                }
            }
        });
    });

    return {
        basePath: basePath,
        getUrlParam: function () {
            var search = window.location.search;
            var params = new Object();
            if (search.indexOf('?') != -1) {
                var arr = search.substr(1).split('&');
                for (var i = 0; i < arr.length; i++) {
                    var index = arr[i].indexOf('=');
                    params[arr[i].substring(0, index)] = decodeURIComponent(arr[i].substring(index + 1));
                }
            }
            return params;
        },
        buildConstSelect: function (dom, name, value, text) {
            dom.empty();
            for (var key in this.constant[name]) {
                dom.append('<option value="' + key + '">' + this.constant[name][key] + '</option>');
            }
            dom.prepend('<option value="' + value + '" selected>' + text + '</option>');
        }
    }
});