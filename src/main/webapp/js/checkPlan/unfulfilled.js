/**
 * 故障预报
 */
require(['../config'],
    function (config) {

        require(['datetimepicker2'],
            function () {
                var date = new Date();
                $.fn.datetimepicker.dates['zh-CN'] = {
                    days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
                    daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
                    daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
                    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                    monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                    today: "今天",
                    suffix: [],
                    meridiem: ["上午", "下午"]
                };
                /**
                 * 初始化时间框
                 */
                function initDatetimepicker(id, date,format) {
                    $('#' + id).datetimepicker({
                        format: format,
                        // timepicker: false,
                        // forceParse:false,
                        // // 关闭时间选项
                        autoclose: true,
                        startView: 3, //这里就设置了默认视图为年视图
                        minView: 3, //设置最小视图为年视图
                        language:'zh-CN'

                    });
                }
                function getDay() {
                    var today = new Date();
                    var oYear = today.getFullYear();
                    var oMoth = (today.getMonth() + 1).toString();
                    if (oMoth.length <= 1)
                        oMoth = '0' + oMoth;
                    console.log(oYear + '-' + oMoth)
                    return oYear + '-' + oMoth;
                }
                initDatetimepicker("queryTime", date,'yyyy-mm');
                $('#queryTime').val(getDay())
            });

        require(['jquery','bootstrap', 'common/dt', 'common/commonMethod', 'common/slider', 'common/dateFormat', 'common/select2', 'common/pinyin'],
            function ($, bootstrap, dataTable, CMethod) {

                var user_id = window.parent.user.userId; // 登录人ID
                var user_name = window.parent.user.userName; // 登录人名字
                var roleName = window.parent.user.roleName; // 登录人角色信息
                var deptId = window.parent.user.deptId // 所属部门id
                var id;
                let sheet_id = ''
                var status;
                var date=new Date()
                /**
                 * 初始化时间框
                 */
                function initDatetimepicker(id, date,format) {
                    $.datetimepicker.setLocale('ch');
                    $('#' + id).datetimepicker({
                        startDate: date.getFullYear()+'-'+date.getMonth(),
                        format: format,
                        timepicker: false,
                        // forceParse:false,
                        // // 关闭时间选项
                        autoclose: true,
                        todayButton:true,
                        // // 关闭选择今天按钮
                    });
                }
                initDatetimepicker("planTimeDelay",date,'Y-m-d');


                /**
                 * 查询
                 */
                $('#btn-search').click(function (e) {
                    sheetTable.ajax.reload();
                });

                // /**
                //  * 重置
                //  */
                // $('#btn-reset').click(function (e) {
                //     $('#sheet_id').val('');
                //     $('#verify_flag').val('');
                //     var now = new Date();
                //     now.setDate(1);
                //     $('#queryTime').val(formatDateBy(now.getTime(), 'yyyy-MM'));
                // });

                /**
                 * 初始化检修单据详情
                 */
                var sheetTable = dataTable('sheetTable', {
                    bAutoWidth: false,
                    ajax: {
                        url: config.basePath + '/checkPlan/checkPlan/listUnfulfilled',
                        type: 'GET',
                        data: function (d) {
                            d.depotId = deptId
                            d.year=$('#queryTime').val().substring(0,4)
                            d.month=$('#queryTime').val().substring(5,7)
                        }
                    },
                    columns: [{
                        data: null
                    }, {
                        data: 'id', bVisible: false
                    },
                        {
                            data: 'sheetId', bVisible: false
                        },
                        {
                            data: 'detectDeviceId', bVisible: false
                        },
                        {
                            data: 'detectDeviceName'
                        },  {
                            data: 'detectDepotName'
                        }, {
                            data: 'detectDeviceType'
                        },
                        {
                            data: 'planTime'
                        },
                        {
                            data: 'startTime'
                        }, {
                            data: 'endTime'
                        }, {
                            data: 'checkRecord'
                        }, {
                            data: 'planType'
                        }, {
                            data: 'remark'
                        },
                        {
                            data: 'status',
                            render: function (data) {
                                var str = '<span style="color:red;font-weight:bold;">未兑现</span>';
                                return str;
                            }
                        },
                    ],
                    columnDefs: [{
                        targets: 14,
                        data: function (row) {
                            var str = '-';
                            if (row.status == 2) {
                                str = '<a class="delaySheet btn btn-info btn-xs" data-toggle="modal" href="#delaySheetModal" title="延期"><span class="glyphicon glyphicon-time"></span></a>&nbsp;&nbsp;'
                            }
                            return str;
                        }
                    }],
                    ordering: false,
                    paging: true,
                    pageLength: 10,
                    serverSide: false,
                    drawCallback: function (settings) {
                        var api = this.api();
                        var startIndex = api.context[0]._iDisplayStart; // 获取到本页开始的条数
                        api.column(0).nodes().each(function (cell, i) {
                            cell.innerHTML = startIndex + i + 1;
                        });
                    },
                });
                /**
                 *延期检修计划
                 */
                $('#delaySheetModal').on('show.bs.modal',
                    function (e) {
                        var tr = $(e.relatedTarget).parents('tr');
                        var data = sheetTable.row(tr).data();
                        id = data.id;
                        $('#detectDeviceDelay').val(data.detectDeviceName)
                        $('#detectTypeDelay').val(data.detectDeviceType)
                        $('#planTypeDelay').val(data.planType)
                        $('#planTimeDelay').val(data.planTime)
                    });
                /*******************************************************
                 * 单据点击事件
                 ********************************************************/
                $('#sheetTable tbody')
                    .on(
                        'click',
                        'tr',
                        function (e) {// 获取当前单号
                            if (sheetTable.data().any()) {
                                $(this)
                                    .addClass('selected')
                                    .siblings()
                                    .removeClass('selected');
                                var tr = $(this).closest('tr');
                                var sheetTrData = sheetTable
                                    .row(tr).data();
                                status = sheetTrData.status
                                id = sheetTrData.id
                                sheet_id = sheetTrData.sheetId
                            }

                        });
                /**
                 * 延期按钮事件
                 */
                $("#btnDelaySheetOk").on('click',
                    function (e) {
                        e.preventDefault();
                        let date = new Date()
                        let planTime = $('#planTimeDelay').val()
                        if (planTime < date) {
                            $("#alertMsgDelay").html("<font style='color:red'>延期时间小于当前时间</font>");
                            $("#alertMsgDelay").css('display', 'inline-block')
                            CMethod.hideTimeout("alertMsgModify", "alertMsgDelay", 5000);
                        }
                        var params = JSON.stringify({
                            id: id,
                            sheetId: sheet_id,
                            planTime: $('#planTimeDelay').val(),
                        });
                        $.ajax({
                            url: config.basePath + '/checkPlan/checkPlan/delay',
                            type: "post",
                            data: params,
                            contentType: 'application/json',
                            dataType: "json",
                            success: function (result) {
                                if (result.code != 0) {
                                    alert(result.msg);
                                } else {
                                    sheetTable.ajax.reload();
                                    $("#alertMsg").html('<span style="color:green;text-align:center"><strong>延期成功！</strong></span>');
                                    $("#infoAlert").show();
                                    hideTimeout("infoAlert", 2000);
                                }
                            }
                        });
                    });
                /** *************** **/
                sheetTable.on('draw.dt', function () {
                    //给第一列编号
                    sheetTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                });
                /**
                 * 定时隐藏alert框
                 */
                function hideTimeout(id, ms) {
                    var time = setTimeout(function () {
                            $("#" + id).hide();
                        },
                        ms)
                }
            });
    })
;