$(document).ready(function () {
    var flag = false;

    function requireTestFieldData() {
        var testData = {
            "code": "0",
            "msg": "operate successfully",
            "data": {
                "sourceData":"http://10.180.185.34:8030/dmz/index/LogDetail4ai?id=597eceb50348d6c9706a8b5d&bankName=%E5%B9%B3%E9%A1%B6%E5%B1%B1&keyWord=K0LhGg2EP2FkFEqY&line=500&grepOpt=0",
                "eventList": [
                    {
                        "time": "2017-08-2915: 00",
                        "event": "esbconsumer",
                        "status": "0"
                    },
                    {
                        "time": "2017-08-2916: 00",
                        "event": "esbconsumer",
                        "status": "1"
                    },
                    {
                        "time": "2017-08-2917: 00",
                        "event": "esbconsumer",
                        "status": "2"
                    },
                    {
                        "time": "2017-08-2918: 00",
                        "event": "esbconsumer",
                        "status": "3"
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


    function loadDatas() {
        var data = {};
        data['key'] = "log";
        data['triggerName'] = "log智能策略";
        var fieldDataList = [];
        //fieldDataList = requireTestFieldData();
        $.ajax({
            type: 'POST',
            url: getLastestSummaryUrl,
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

    };

    function drawTables(seriesData) {
        var evenList = seriesData.data;
        var url=seriesData.data.sourceData;
        if (url.indexOf("http") != -1){
            $('#originUrl').html("<a href="+url+" target='_blank'>"+url+"</a>");
        }else{
            $('#originUrl').html(url);
        }
        if (evenList.eventList.length == 4) {
            clearInterval(sh);
        }
        $('#chartDiv').html('');
        $('#triggerListTmpl').tmpl(evenList).appendTo('#chartDiv');
    };

    var sh = setInterval(loadDatas, 2000);
});
