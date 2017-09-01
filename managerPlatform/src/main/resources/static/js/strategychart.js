$(document).ready(function () {

    function requireTestFieldData(){
        var testData={
            "code": 0,
            "msg": "success",
            "data": {
                "fieldList": [
                    {
                        "field": "xxxxx",
                        "countList": [
                            {
                                "sourceData": "http://localhost:4101/chart.html",
                                "dataResult": "timeout",
                                "governStatus": 0,
                                "governResult": "重启kafka",
                                "time": "2017-08-2917: 00"
                            },
                            {
                                "sourceData": "xxxxx",
                                "dataResult": "timeout",
                                "governStatus": 0,
                                "governResult": "重启kafka",
                                "time": "2017-08-2917: 00"
                            },
                            {
                                "sourceData": "http://www.cnblogs.com/zdz8207/archive/2012/09/26/2703589.html",
                                "dataResult": "timeout",
                                "governStatus": 0,
                                "governResult": "重启kafka",
                                "time": "2017-08-2917: 00"
                            },
                            {
                                "sourceData": "xxxxx",
                                "dataResult": "timeout",
                                "governStatus": 0,
                                "governResult": "重启kafka",
                                "time": "2017-08-2917: 00"
                            }
                        ]
                    }
                ]
            }
        };
        return testData;
    }

    $(document).on('click', '.trainingbtn', function () {
        $('#triggerForms')[0].reset();
        $('#triggerModal4AI').modal();
    });

    $(document).on('click', '#triggerSubmitBtn4AI', function () {
        alert('提交成功!');
        $('#triggerModal4AI').modal('hide');
    });



    $(document).on('click', '#getStatBtn', function () {
        var strategyName = $('#knowledgeStrategyName').val();
        var appID = $('#appID').val();
        var os = $('#os').val();
        var appVersion = $('#appVersion').val();
        var triggerName = $('#triggerStrategyName').val();
        var type = $('#triggerStrategyName').find("option:selected").attr("desc");
        var data = {};
        data['key'] = strategyName;
        data['beginTime'] = $('#beginTime').val();
        data['endTime'] = $('#endTime').val();
        if (appID != "所有appID") {
            data['appID'] = appID;
        }
        if (os != "所有os") {
            data['os'] = os;
        }
        if (appVersion != "所有版本号") {
            data['appVersion'] = appVersion;
        }
        if (triggerName != "所有子策略") {
            data['triggerName'] = triggerName;
        }

        if (type == '1') {
            var chartDataList = [];
            $.ajax({
                type: 'POST',
                url: getStatByKeyAndTriggerAndBaseInfoUrl,
                data: data,
                dataType: 'json',
                success: function (data) {
                    var responseObj = eval(data);
                    if (responseObj.code == 0) {
                        var list = responseObj['data']['fieldList'];
                        for (var i = 0; i < list.length; i++) {

                            var keyAndField = list[i]['field'];

                            var countList = list[i]['countList'];
                            var dataList = new Array();
                            for (var j = 0; j < countList.length; j++) {
                                var value = countList[j]['value'];
                                var time = countList[j]['time'];
                                dataList.push([splitTimeToSeconds(time), value]);
                            }
                            var jsonObj = {
                                name: keyAndField,
                                data: dataList
                            }
                            chartDataList.push(jsonObj);
                        }
                    } else {
                        alert("get All appData Error,Msg:" + responseObj.msg)
                    }
                    drawCharts(chartDataList);
                }
            });
        } else {
            var fieldDataList = [];
            //fieldDataList = requireTestFieldData();
            $.ajax({
                type: 'POST',
                url: getDataListByKeyAndTriggerAndBaseInfoUrl,
                data: data,
                dataType: 'json',
                success: function (data) {
                    var responseObj = eval(data);
                    if (responseObj.code == 0) {
                        drawTables(responseObj);
                    } else {
                        alert("get All appData Error,Msg:" + responseObj.msg)
                    }
                }
            });
            // drawTables(fieldDataList);
        }
    });




    function requireChartData() {
        var strategysName = $('#knowledgeStrategyName').val();
        var triggerName = $('#triggerStrategyName').val();
        var beginTime = $('#beginTime').val();
        var endTime = $('#endTime').val();
        var chartDataList = [];
        if (triggerName == "所有子策略") {
            $.ajax({
                type: 'POST',
                url: getStatByKeyUrl,
                async: false,
                data: {
                    key: strategysName,
                    beginTime: beginTime,
                    endTime: endTime
                },
                dataType: 'json',
                success: function (data) {
                    var responseObj = eval(data);
                    if (responseObj.code == 0) {
                        var list = responseObj['data']['fieldList'];
                        for (var i = 0; i < list.length; i++) {

                            var keyAndField = list[i]['field'];

                            var countList = list[i]['countList'];
                            var dataList = new Array();
                            for (var j = 0; j < countList.length; j++) {
                                var value = countList[j]['value'];
                                var time = countList[j]['time'];
                                dataList.push([splitTimeToSeconds(time), value]);
                            }
                            var jsonObj = {
                                name: keyAndField,
                                data: dataList
                            }
                            chartDataList.push(jsonObj);
                        }
                    } else {
                        alert("get All appData Error,Msg:" + responseObj.msg)
                    }
                }
            });
        } else {
            $.ajax({
                type: 'POST',
                url: getStatByKeyAndFieldUrl,
                async: false,
                data: {
                    key: strategysName,
                    field: triggerName,
                    beginTime: beginTime,
                    endTime: endTime
                },
                dataType: 'json',
                success: function (data) {
                    var responseObj = eval(data);
                    if (responseObj.code == 0) {
                        var list = responseObj['data']['fieldList'];
                        for (var i = 0; i < list.length; i++) {

                            var keyAndField = list[i]['field'];

                            var countList = list[i]['countList'];
                            var dataList = new Array();
                            for (var j = 0; j < countList.length; j++) {
                                var value = countList[j]['value'];
                                var time = countList[j]['time'];
                                dataList.push([splitTimeToSeconds(time), value]);
                            }
                            var jsonObj = {
                                name: keyAndField,
                                data: dataList
                            }
                            chartDataList.push(jsonObj);
                        }
                    } else {
                        alert("get All appData Error,Msg:" + responseObj.msg)
                    }
                }
            });
        }
        return chartDataList;
    }

    function drawBaseInfoSelection(strategyName) {
        $.ajax({
            type: 'POST',
            url: getAllBaseInfoByKeyUrl,
            async: false,
            data: {
                key: strategyName,
            },
            dataType: 'json',
            success: function (data) {
                var responseObj = eval(data);
                if (responseObj.code == 0) {
                    var fields = responseObj['data']['baseInfo'];
                    if (fields != null && fields != undefined) {
                        var appID = fields['appID'];
                        if (appID != null) {
                            for (var i = 0; i < appID.length; i++) {
                                $('#appID').append("<option>" + appID[i] + "</option>");
                            }
                        }

                        var os = fields['os'];
                        if (os != null) {
                            for (var i = 0; i < os.length; i++) {
                                $('#os').append("<option>" + os[i] + "</option>");
                            }
                        }

                        var appVersion = fields['appVersion'];
                        if (appVersion != null) {
                            for (var i = 0; i < appVersion.length; i++) {
                                $('#appVersion').append("<option>" + appVersion[i] + "</option>");
                            }
                        }
                    }
                } else {
                    alert("get All appData Error,Msg:" + responseObj.msg)
                }
            }
        });
    }

    function drawTriggerSelection(strategyName) {
        $.ajax({
            type: 'POST',
            url: getAllTriggersByKeyUrl,
            async: false,
            data: {
                key: strategyName
            },
            dataType: 'json',
            success: function (data) {
                var responseObj = eval(data);
                if (responseObj.code == 0) {
                    var fields = responseObj['data'];
                    if (fields != null && fields != undefined && fields.length > 0) {
                        var fieldsize = fields.length;
                        for (var i = 0; i < fieldsize; i++) {
                            $('#triggerStrategyName').append("<option desc=" + fields[i]['chart'] + ">" + fields[i].name + "</option>");
                        }
                    }
                } else {
                    alert("get All appData Error,Msg:" + responseObj.msg)
                }
            }
        });
    }

    function drawCharts(seriesData) {
//        var seriesData = requireChartData();
        Highcharts.setOptions({
            lang: {
                months: ['1', '2', '3', '4', '5', '6', '7',
                    '8', '9', '10', '11', '12']
            }
        });
        $('#chartDiv').highcharts({
            chart: {
                type: 'spline'
            },
            title: {
                text: $('#knowledgeStrategyName').val() + "数据分析"
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: { // don't display the dummy year
                    month: '%B/%e %H:%M'

                }
            },
            yAxis: {
                title: {
                    text: '数据'
                },
                min: 0
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + timeFormat(new Date(this.x)) + '</b><br/>' +
                        '<b>触发名:' + this.series.name + '</b><br/>' +
                        '<b>' + '值:' + this.y + '</b><br/>';
                }
            },
            series: seriesData
        });
    };

    function drawTables(seriesData) {
        var countList = seriesData.data.fieldList[0];
        $('#chartDiv').html('');
        $('#triggerListTmpl').tmpl(countList).appendTo('#chartDiv');
    };

    function setClickListener(beginTimeObj, endTimeObj) {
        $('#knowledgeStrategyName').change(function () {
            $('#appID').empty();
            $('#appID').append("<option>所有appID</option>");
            $('#os').empty();
            $('#os').append("<option>所有os</option>");
            $('#appVersion').empty();
            $('#appVersion').append("<option>所有版本号</option>");
            $('#triggerStrategyName').empty();
            $('#triggerStrategyName').append("<option>所有子策略</option>");
            drawBaseInfoSelection($('#knowledgeStrategyName').val());
            drawTriggerSelection($('#knowledgeStrategyName').val());
//            drawCharts();
        });
//        $('#triggerStrategyName').change(function () {
//            drawCharts();
//        });
//        rome(beginTimeObj).on('data', function () {
//            drawCharts();
//        });
//        rome(endTimeObj).on('data', function () {
//            drawCharts();
//        });

    };

    function init() {
        var storage = window.localStorage;
        var strategyName = storage.getItem("strategyName");
        var curDate = new Date();
        var curMinute = curDate.getMinutes();
        var adjustSeconds = curDate.getTime() + 1800000;
        var endDate = new Date(adjustSeconds);
        var endTotalSeconds = endDate.getTime() - 43200000;	// beginDate相对于endDate往后推12个小时
        var beginDate = new Date(endTotalSeconds);
        var beginMinute = beginDate.getMinutes();
        var endMinute = endDate.getMinutes();
        if (beginMinute >= 30) {
            beginMinute = 30;
        } else {
            beginMinute = 0;
        }
        if (endMinute >= 30) {
            endMinute = 30;
        } else {
            endMinute = 0;
        }
        var beginTime = beginDate.getFullYear() + "-" + (beginDate.getMonth() + 1) + "-" + beginDate.getDate() + " " + beginDate.getHours() + ":" + beginMinute;
        var endTime = endDate.getFullYear() + "-" + (endDate.getMonth() + 1) + "-" + endDate.getDate() + " " + endDate.getHours() + ":" + endMinute;
        var beginTimeObj = document.getElementById("beginTime");
        var endTimeObj = document.getElementById("endTime");


        $(document).on('click', '#titleNavbar', function () {
            window.location.href = homeHtmlUrl;
        });

        //初始化时间框输入框start
        rome(beginTimeObj,
            {
                initialValue: beginTime,
                dateValidator: rome.val.beforeEq(endTimeObj)
            });

        rome(endTimeObj,
            {
                initialValue: endTime,
                dateValidator: rome.val.afterEq(beginTimeObj)
            });
        //初始化时间框输入框end

        //初始化知识策略名输入框start
        $.ajax({
            type: 'GET',
            url: getAllKnowledeNameUrl,
            async: false,
            dataType: 'json',
            success: function (data) {
                var responseObj = eval(data);
                if (responseObj.code == 0) {
                    strategys = responseObj['data'];
                    for (var i = 0; i < strategys.length; i++) {
                        $('#knowledgeStrategyName').append("<option>" + strategys[i] + "</option>");
                    }
                    $('#knowledgeStrategyName').val(strategyName);
                } else {
                    alert("get All appData Error,Msg:" + responseObj.msg)
                }
            }
        });

        //初始化触发应用名输入框start
        drawBaseInfoSelection(strategyName);
        drawTriggerSelection(strategyName);

        //初始化触发应用名输入框end
        setClickListener(beginTimeObj, endTimeObj);
    }

    init();
//    drawCharts();
});
