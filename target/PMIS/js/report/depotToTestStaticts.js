/**
 * 采购入库
 */
require(['../config'], function (config) {

    require(['datetimepicker'], function () {
        var date = new Date();
        date.setDate(1);
        date.setMonth(0)
        initDatetimepicker("queryTime", date);
        initDatetimepicker("queryTime2", new Date());

        /**
         * 初始化时间框
         */
        function initDatetimepicker(id, date) {
            $.datetimepicker.setLocale('ch');
            $('#' + id).datetimepicker({
                value: date,
                format: 'Y-m-d',
                timepicker: false, //关闭时间选项
                todayButton: true //关闭选择今天按钮
            });
        }
    });

    require(['jquery','myEcharts'], function ($,echarts) {

        var sheet_id = '';//查询的单号
        var verify_flag = '';//查询的审核状态
        var names=[];
        var nums=[];
        var verifyFlag = '';
        var sheet_id2 = '';
        var user_id = window.parent.user.userId; // 登录人ID
        var user_name = window.parent.user.userName; // 登录人名字
        var deptId = window.parent.user.deptId // 所属部门id
        var sheetTrData = '';//保留点击的单号信息
        var partId = '';//保留点击的配件编码
        var partCode = '';//保留点击的配件出厂编码
        var devicePartsName = '';//保留点击的配件名称
        var deviceTypeName = '';//保留点击的配件类型
        var deviceModelName = '';//保留点击的型号
        var supplierName = '';//保留点击的厂家名称
        var sourceStoreHouseName = '';//保留点击的厂家名称
        var myChart=''
        /**
         * 查询
         */
        $('#btn-search').click(function (e) {
            loadStatictsData(myChart)
            sheet_id = '';
            verify_flag = '';
        });

        /**
         * 重置
         */
        $('#btn-reset').click(function (e) {
            $('#sheet_id').val('');
            $('#verify_flag').val('');
            var now = new Date();
            now.setDate(1);
            $('#queryTime').val(formatDateBy(now.getTime(), 'yyyy-MM-dd'));
            $('#queryTime2').val(formatDateBy(new Date(), 'yyyy-MM-dd'));
        });
        $(document).ready(function() {
            myChart = echarts.init(document.getElementById('depotToTestStatistics'));
            // 显示标题，图例和空的坐标轴
            var option = {
                title: {
                    text: '采购数据'
                },
                grid:{
                    x:100,
                    y:100,
                },

                tooltip: {},
                legend: {
                    data: ['数量']
                },
                axisLabel:{
                    interval:0,//横轴信息全部显示
                    //rotate:-30,//-30度角倾斜显示
                },
                notMerge:true,
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    name: '数量',
                    type: 'bar',
                    data: []
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            loadStatictsData(myChart)

        })
        function loadStatictsData(myChart) {
            $.ajax({
                type: "get", //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
                url: config.basePath + "/report/depotToTestStaticts/getRepairPartsCount",    //请求发送到TestServlet处
                data: function (d) {
                    d.receiptVerifyFlag = 2;
                    d.queryTime = $("#queryTime").val();
                    d.queryTime2 = ($("#queryTime2").val() == '' ? '' : $("#queryTime2").val() + " 23:59:59");
                }, //返回数据形式为json
                success: function (result) {
                    //请求成功时执行该函数内容，result即为服务器返回的json对象
                    if (result) {
                        for (var i = 0; i < result.length; i++) {
                            names.push(result[i].partId);    //挨个取出类别并填入类别数组
                        }
                        for (var i = 0; i < result.length; i++) {
                            nums.push(result[i].count);    //挨个取出销量并填入销量数组
                        }
                        myChart.hideLoading();    //隐藏加载动画

                        myChart.setOption({        //加载数据图表
                            xAxis: {
                                data: null
                            },
                            series: [{
                                // 根据名字对应到相应的系列
                                name: '数量',
                                data: null
                            }]
                        });
                        myChart.setOption({        //加载数据图表
                            xAxis: {
                                data: names
                            },
                            series: [{
                                // 根据名字对应到相应的系列
                                name: '数量',
                                data: nums
                            }]
                        });

                    }

                },
                error: function (errorMsg) {
                    //请求失败时执行该函数
                    alert("图表请求数据失败!");
                    myChart.hideLoading();
                }

            });
        }
    });
});