function formatDateYMD(date) {
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();//获得年、月、日
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var res = year + "-" + month + "-" + day;
    return res;
}

function formatDateYMDHM(date) {
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();//获得年、月、日
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var hour = date.getHours(); //获得小时、分钟、秒
    var minute = date.getMinutes();
    if (minute < 10) //如果分钟只有1位，补0显示
        minute = "0" + minute;
    var res = year + "-" + month + "-" + day + " " + hour + ":" + minute;
    return res;
}

// alert(formatDateBy(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss'))
var formatDateBy = function (time, format) {
    if (time == null || time == "" || time == undefined)
        return "";
    var t = new Date(time);
    var tf = function (i) {
        return (i < 10 ? '0' : '') + i
    };
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
        switch (a) {
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
}

//格式化时长
function calculateDuration(oldtime, newtime) {
    var diff = (newtime - oldtime) / 1000;//得到秒差
    var result = parseInt(diff / 3600) + " 小时 " + parseInt((diff % 3600) / 60) + " 分 " + parseInt((diff % 3600) % 60) + " 秒";
    return result
}

//计算日期相差几天
function getDiffDays(strDateStart, strDateEnd) {
    var strSeparator = "-"; //日期分隔符
    var oDate1;
    var oDate2;
    var iDays;
    oDate1 = strDateStart.split(strSeparator);
    oDate2 = strDateEnd.split(strSeparator);
    var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
    var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
    iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60 / 24)//把相差的毫秒数转换为天数

    return iDays;
}

function getInervalHour(startDate, endDate) {
    var ms = endDate.getTime() - startDate.getTime();
    if (ms < 0) return 0;
    return Math.round(ms / 1000 / 60 / 60);
}

//获取当前一年或者一个月或者几天
function getBeforeDate(y, m, d) {//y m d为你要传入的参数，当前为0,0,0;后一年为1,0,0
    var date = new Date();
    var year, month, day;
    date.setDate(date.getDate());
    year = date.getFullYear() + y;
    month = date.getMonth() + 1 + m;
    day = date.getDate() + d;
    s = year + '-' + (month < 10 ? ('0' + month) : month) + '-' + (day < 10 ? ('0' + day) : day);
    return s;
}
/**
 * 获取上一个月
 *
 * @date 格式为yyyy-mm-dd的日期，如：2014-01-25
 */
function getPreMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中月的天数
    var year2 = year;
    var month2 = parseInt(month) - 1;
    if (month2 == 0) {//如果是1月份，则取上一年的12月份
        year2 = parseInt(year2) - 1;
        month2 = 12;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {//如果原来日期大于上一月的日期，则取当月的最大日期。比如3月的30日，在2月中没有30
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0' + month2;//月份填补成2位。
    }
    var t2 = year2 + '-' + month2 + '-' + day2;
    return t2;
}

function stringToDate(str){

    var tempStrs = str.split(" ");

    var dateStrs = tempStrs[0].split("-");

    var year = parseInt(dateStrs[0], 10);

    var month = parseInt(dateStrs[1], 10) - 1;

    var day = parseInt(dateStrs[2], 10);

    var timeStrs = tempStrs[1].split(":");

    var hour = parseInt(timeStrs [0], 10);

    var minute = parseInt(timeStrs[1], 10);

    var second = parseInt(timeStrs[2], 10);

    var date = new Date(year, month, day, hour, minute, second);

    return date;

}