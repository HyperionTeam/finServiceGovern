//dev host
//var hostUri = "http://10.20.11.225:8188/webii"
//stg host
//var hostUri = "http://ff-app-sg-stg1.paic.com.cn/webii"
//prd host
//var hostUri = "http://ff-app-sg.paic.com.cn/webii"
var hostUri = 'http://'+window.location.host;
var getStatByKeyAndTriggerAndBaseInfoUrl = hostUri + "/strategy/getStatByKeyAndTriggerAndBaseInfo";
var getDataListByKeyAndTriggerAndBaseInfoUrl = hostUri + "/strategy/getDataListByKeyAndTriggerAndBaseInfo";
var getStatByKeyUrl = hostUri + "/strategy/getStatByKey";
var getStatByKeyAndFieldUrl = hostUri + "/strategy/getStatByKeyAndField";
var getAllKnowledeNameUrl = hostUri + "/strategy/getAllPersistentedStrategy";
var getAllFieldByKeyUrl = hostUri + "/strategy/getAllFieldByKey";
var getAllTriggersByKeyUrl = hostUri + "/strategy/getAllTriggersByKey";
var getAllBaseInfoByKeyUrl = hostUri + "/strategy/getAllBaseInfoByKey";
var getLastestSummaryUrl = hostUri + "/strategy/getLastestSummary";
var chartHtmlUrl = hostUri + "/chart.html";
var homeHtmlUrl = hostUri + "/index.html";

function splitTimeToSeconds(time) {
    var year = time.split('-')[0];
    var month = time.split('-')[1] - 1;
    var day = time.split('-')[2].split(' ')[0];
    var hour = time.split(' ')[1].split(':')[0];
    var minute = time.split(' ')[1].split(':')[1];
    var targetTime = Date.UTC(year, month, day, hour, minute, 0);
    return targetTime;
}

function timeFormat(inputDate) {
    var year = inputDate.getUTCFullYear();
    var month = inputDate.getUTCMonth() + 1;
    var day = inputDate.getUTCDate();
    var hour = inputDate.getUTCHours();
    var minute = inputDate.getUTCMinutes();
    if (minute == 0){
    	minute = "00";
    }
    var formatDate = year + "/" + month + "/" + day + " " + hour + ":" + minute;
    return formatDate;
}

function htmlEntityEncode(str) {
	str = str.replace(/&lt;/g,"<");
	str = str.replace(/&gt;/g,">");
//	str = str.replace(/"/g,"\"");
//	str = str.replace(/&/g,"&");
//	str = str.replace(/ /g," ");
	return str;
}