
var triggerModifyJqObj = null

$(document).ready(function  () {
  var makeArrayForEach = function(word, size){
    var i, result = [];
    for(i = 0; i < size; i++){
      result.push(word);
    }
    return result;
  }
  //stype=1 知识策略 stype=2 应用策略。page页数，1开始
  function ajaxMainData(stype,page,limit){
	var result;
    var getMainDataUrl = hostUri;
    if (stype == 1) {
      getMainDataUrl = hostUri + "/strategy/getKnowledgeStrategyByPage"
    }else if (stype == 2) {
      getMainDataUrl = hostUri + "/strategy/getAppStrategyByPage"
    }else{
      alert("UnkownStrategyType,only 1 and 2 is available")
      return ;
    }
    $.ajax({
        type:'POST',
        url:getMainDataUrl,
        data:{
          offset : page,
          limit : limit
        },
        async:false,
        dataType:'json',
        success:function  (data) {
           var responseObj = eval(data);
           if(responseObj.code==0){
        	   result = responseObj;
        	   return ;
           }else{
              alert("get Main Data Error,Msg:" + responseObj.msg)
           }
        }
     });
    return result;

  }
  function tabActiveChange(jqObj){
    $(".main-menu-li").removeClass("active");
    jqObj.parent().addClass('active')
  }
  function changeMainHtml(jsonData){
    $('#main').html('')
    $('#strategyMainTmpl').tmpl(jsonData,{makeArrayForEach: makeArrayForEach}).appendTo('#main');
  }
  function clearKnowlegeStrategyModal(nameInputEnableFlag){
    $('#knowledgeForm')[0].reset();
    $('#knowledgeStrategyName').attr('disabled',nameInputEnableFlag);
    $('#triggerListtbody').html('')
  }
  function clearApplicationStrategyModal(nameInputEnableFlag){
    $('#applicationForm')[0].reset();
    $('#applicationStrategyName').attr('disabled',nameInputEnableFlag);
  }
  function delConfirm(msg) {
    if (confirm(msg) == true){
      return true;
    }else{
      return false;
    }
  }
  function setOperationDatas(){
    $('#triggerOpe').html('');
    $('#triggerOpe').append("<option><</option>");
    $('#triggerOpe').append("<option>=</option>");
    $('#triggerOpe').append("<option>></option>");
    $('#triggerOpe').append("<option><=</option>");
    $('#triggerOpe').append("<option>>=</option>");
  }
  function setApplicationDatas(){
    $('#triggerApp').html('');
    var getAllAppUrl = hostUri;
    getAllAppUrl = hostUri + "/strategy/getAllAppStrategy"
    $.ajax({
        type:'GET',
        url:getAllAppUrl,
        dataType:'json',
        async:false,
        success:function  (data) {
           var responseObj = eval(data);
           if(responseObj.code==0){
             strategys = responseObj['data']['strategy'];
             for(var i=0; i<strategys.length; i++)
             {
                $('#triggerApp').append("<option>" + strategys[i]['key'] + "</option>");
             }
           }else{
              alert("get All appData Error,Msg:" + responseObj.msg)
           }
        }
     });
  }
  function refreshMainData(stype){
    var firstReturnData = ajaxMainData(stype,1,20);
    if (firstReturnData != undefined) {
      firstReturnData.stype = stype
      if (stype == 1) {
        tabActiveChange($('#knowledgeManager'))
      }else{
        tabActiveChange($('#applicationManager'))
      }
      $('#main').html('');
      $('#strategyMainTmpl').tmpl(firstReturnData,{makeArrayForEach: makeArrayForEach}).appendTo('#main');
    }
  }

  refreshMainData(1);
  
  $('#knowledgeStrategyExpi').bind('input propertychange', function() {  
	    var tvalue = $(this).val();
	    if(tvalue.length != 0 && tvalue.match(/^[0-9]+$/) == null){
	      alert("只能输入数字");
	      $(this).val(tvalue.substring(0,tvalue.length - 1));
	      return ;
	    }
	    if (tvalue.length > 6){
	      $(this).val(tvalue.substring(0,tvalue.length - 1));
	    }
	    var perStat = $("input[name=persistentStatus]:checked").val();
	    if(perStat == 1){
		    if(tvalue >= 1800){
		    	$("#persistentStatusDesc").html("");
		    }else{
		    	$("#persistentStatusDesc").html("数据过期时间必须大于或等于30分钟才能持久化！");
		    }
	    }
	    
	  });
  
  $(document).on('click','#addApplicationBtn',function(){
    clearApplicationStrategyModal(false);
    $('#applicationSubType').val(0);
    $('#applicationModal').modal();
  })
  $(document).on('click','#knowledgeManager',function(){
    var returnData = ajaxMainData(1,1,20);
    if (returnData != undefined) {
      returnData.stype = 1
      changeMainHtml(returnData)
      tabActiveChange($(this))
    }
  })
  $(document).on('click','#applicationManager',function(){
    var returnData = ajaxMainData(2,1,20);
    if (returnData != undefined) {
      returnData.stype = 2
      changeMainHtml(returnData)
      tabActiveChange($(this))
    }
  })
  $(document).on('click','input[name=persistentStatus]',function(){
   var perStat = $("input[name=persistentStatus]:checked").val();
   var expiTime = $('#knowledgeStrategyExpi').val();
   if(perStat ==1 && expiTime < 1800){
	   $("#persistentStatusDesc").html("数据过期时间必须大于或等于30分钟才能持久化！");
   }else{
	   $("#persistentStatusDesc").html("");
   }
  })
  $('#triggerModal').on('hidden.bs.modal', function () {
    $('#knowledgeModal').modal('show')
   })
  $('#triggerModal').on('show.bs.modal', function () {
   $('#knowledgeModal').modal('hide')
  })
  $(document).on('click','#addKnowledgeBtn',function(){
    clearKnowlegeStrategyModal(false);
    $('#knowledgeSubType').val(0);
    $('#knowledgeModal').modal()
  })
  $(document).on('click','#addTrigger4AIBtn',function(){
    $('#triggerForm')[0].reset();
    triggerModifyJqObj = null;
    setOperationDatas();
    setApplicationDatas();
    $('#triggerModal4AI').modal();
  })
  $(document).on('click','#addTrigger4TABtn',function(){
	    $('#triggerForm')[0].reset();
	    triggerModifyJqObj = null;
	    setOperationDatas();
	    setApplicationDatas();
	    $('#triggerModal4TA').modal();
  })
  $(document).on('click','.modKnowledgeBtn',function(){
    var knowledgeStrategyName = $(this).parent().parent().children('.strategyName').html();
    $('#knowledgeSubType').val(1);
    var modKnowledgeUrl = hostUri;
    modKnowledgeUrl = hostUri + "/strategy/getKnowledgeStrategyByName"
    $.ajax({
        type:'POST',
        url:modKnowledgeUrl,
        data:{
          name : knowledgeStrategyName
        },
        dataType:'json',
        success:function  (data) {
           var responseObj = eval(data);
           if(responseObj.code==0){
             var ksName = responseObj['data']['strategy']['key'];
             var ksDesc = responseObj['data']['strategy']['description'];
             var ksExpi = responseObj['data']['strategy']['expire'];  
             var ksStat = responseObj['data']['strategy']['status'];
             clearKnowlegeStrategyModal(true);
             $('#knowledgeStrategyName').val(ksName);
             $('#knowledgeStrategyDesc').val(ksDesc);
             $('#knowledgeStrategyExpi').val(ksExpi);
             if(ksStat == 1){
               $("input[name=knowledgeStatus]:eq(1)").removeAttr("checked");
               $("input[name=knowledgeStatus]:eq(0)").attr("checked","checked");
               $("input[name=knowledgeStatus]:eq(0)").click();
             }else{
               $("input[name=knowledgeStatus]:eq(0)").removeAttr("checked");
               $("input[name=knowledgeStatus]:eq(1)").attr("checked","checked");
               $("input[name=knowledgeStatus]:eq(1)").click();
             }
             var ksTriggersJson = responseObj['data']['strategy'];
             $('#triggerListtbody').html('');
             $('#triggerListTmpl').tmpl(ksTriggersJson).appendTo('#triggerListtbody');
             $('#knowledgeModal').modal();
           }else{
              alert("get Main Data Error,Msg:" + responseObj.msg)
              return ;
           }
        }
     });
  })
  $(document).on('click','.delKnowledgeBtn',function(){
    var delKnowledgeName = $(this).parent().parent().children('.strategyName').html();
    var status = delConfirm("确认删除 " + delKnowledgeName + "么?");
    if(status){
      delKnowledgeUrl = hostUri + "/strategy/deleteKnowledgeStrategyByName"
      $.ajax({
          type:'POST',
          url:delKnowledgeUrl,
          data:{
            name : delKnowledgeName,
          },
          dataType:'json',
          success:function  (data) {
             var responseObj = eval(data);
             if(responseObj.code==0){
                alert("已成功删除");
                refreshMainData(1);
             }else{
                alert("get Main Data Error,Msg:" + responseObj.msg)
             }
          }
       });
     }
  });
  $(document).on('click','.showChartBtn',function(){
    var knowledgeName = $(this).parent().parent().children('.strategyName').html();
    var storage = window.localStorage;
    storage.setItem("strategyName",knowledgeName)
    window.location.href = chartHtmlUrl;
  });
  $(document).on('click','.modApplicationBtn',function(){
    var appStrateName = $(this).parent().parent().children('.strategyName').html();
    $('#applicationSubType').val(1);
    var modApplicationUrl = hostUri;
    modApplicationUrl = hostUri + "/strategy/getAppStrategyByName"
    $.ajax({
        type:'POST',
        url:modApplicationUrl,
        data:{
          name : appStrateName
        },
        dataType:'json',
        success:function  (data) {
          var responseObj = eval(data);
          if(responseObj.code==0){
            var asName = responseObj['data']['strategy']['key'];
            var asDesc = responseObj['data']['strategy']['description'];
            var asComm = responseObj['data']['strategy']['command'];
            clearApplicationStrategyModal(true);
            $('#applicationStrategyName').val(asName);
            $('#applicationStrategyDesc').val(asDesc);
            $('#applicationStrategyComm').val(asComm);
            $('#applicationModal').modal();
          }else{
             alert("get Main Data Error,Msg:" + responseObj.msg)
          }
        }
     });
  });
  $(document).on('click','.knowlegePaginationBtn',function(){
    pageNum = $(this).data('index')
    var returnData = ajaxMainData(1,pageNum,20);
    if (returnData != undefined) {
      returnData.stype = 1
      changeMainHtml(returnData)
      tabActiveChange($('#knowledgeManager'))
    }
  });
  $(document).on('click','.applicationPaginationBtn',function(){
    pageNum = $(this).data('index')
    var returnData = ajaxMainData(2,pageNum,20);
    if (returnData != undefined) {
      returnData.stype = 2
      changeMainHtml(returnData)
      tabActiveChange($('#applicationManager'))
    }
  })
  
  $(document).on('click','#triggerSubmitBtn4AI',function(){
	    var triKey = $('#triggerKey').val();
	    var sql = '*';
	    var triOpe = '*';
	    var triVal = '*';
	    var triApp = $("#triggerApp").val();
	    var persistent = "1";//$("input[name=persistent]:checked").val();
	    var type = 1;
	    if(triKey.trim().length == 0){
	    	alert("触发应用名不能为空或者只含空格！");
	    	return ;
	    }
	    if (triggerModifyJqObj == null) {
	      var triRow = {};
	      triRow.name = triKey;
	      triRow.sql = sql;
	      triRow.op = triOpe;
	      triRow.value = triVal;
	      triRow.appStrategyName = triApp;
	      triRow.persistent = persistent;
	      triRow.type = type;
	      var triDataJson = {appStrategyTriggers: []};
	      triDataJson.appStrategyTriggers.push(triRow);
	      $('#triggerListTmpl').tmpl(triDataJson).appendTo('#triggerListtbody');
	      // alert(triKey + triOpe + triVal + triApp)
	    }else{
	      triggerModifyJqObj.parent().parent().children('.triggerKey').html(triKey);
	      triggerModifyJqObj.parent().parent().children('.sql').html(sql);
	      triggerModifyJqObj.parent().parent().children('.triggerOpe').html(triOpe);
	      triggerModifyJqObj.parent().parent().children('.triggerOpe').data('op',triOpe);
	      triggerModifyJqObj.parent().parent().children('.triggerVal').html(triVal);
	      triggerModifyJqObj.parent().parent().children('.triggerApp').html(triApp);
	      triggerModifyJqObj.parent().parent().children('.persistent').html(persistent);
	    }
	    $('#triggerModal4AI').modal('hide');  
  })
  $(document).on('click','#triggerSubmitBtn4TA',function(){
    var triKey = $('#triggerKey').val();
    var sql = $('#sql').val();
    var triOpe = $('#triggerOpe').val();
    var triVal = $('#triggerVal').val();
    var triApp = $("#triggerApp").val();
    var persistent = $("input[name=persistent]:checked").val();
    var type = 2;
    if(triKey.trim().length == 0){
    	alert("触发应用名不能为空或者只含空格！");
    	return ;
    }
    if(triVal.trim().length == 0){
    	alert("触发应用阀值不能为空或者只含空格！");
    	return ;
    }
    if(triVal.length > 50){
    	alert("触发应用阀值长度超过限制！");
    	return ;
    }
    if (triggerModifyJqObj == null) {
      var triRow = {};
      triRow.name = triKey;
      triRow.sql = sql;
      triRow.op = triOpe;
      triRow.value = triVal;
      triRow.appStrategyName = triApp;
      triRow.persistent = persistent;
      triRow.type = type;
      var triDataJson = {appStrategyTriggers: []};
      triDataJson.appStrategyTriggers.push(triRow);
      $('#triggerListTmpl').tmpl(triDataJson).appendTo('#triggerListtbody');
      // alert(triKey + triOpe + triVal + triApp)
    }else{
      triggerModifyJqObj.parent().parent().children('.triggerKey').html(triKey);
      triggerModifyJqObj.parent().parent().children('.sql').html(sql);
      triggerModifyJqObj.parent().parent().children('.triggerOpe').html(triOpe);
      triggerModifyJqObj.parent().parent().children('.triggerOpe').data('op',triOpe);
      triggerModifyJqObj.parent().parent().children('.triggerVal').html(triVal);
      triggerModifyJqObj.parent().parent().children('.triggerApp').html(triApp);
      triggerModifyJqObj.parent().parent().children('.persistent').html(persistent);
    }
    $('#triggerModal4TA').modal('hide');
  })
	  
  
  $(document).on('click','.delTriggerBtn',function(){
    name = $(this).parent().parent().children('.triggerKey').html();
    var status = delConfirm("确认删除" + name + "?");
    if (status){
      $(this).parent().parent().remove();
    }
  })
  $(document).on('click','.modTriggerBtn',function(){
    var triKey = $(this).parent().parent().children('.triggerKey').html();
    var sql = $(this).parent().parent().children('.sql').text();
    var triOpe = $(this).parent().parent().children('.triggerOpe').data('op');
    var triVal = $(this).parent().parent().children('.triggerVal').html();
    var triApp = $(this).parent().parent().children('.triggerApp').html();
    var persistent = $(this).parent().parent().children('.persistent').html();
    var persistentObj = {};
    persistentObj.persistent = persistent;
    $('#triggerForm')[0].reset();
    triggerModifyJqObj = $(this);
    setOperationDatas();
    setApplicationDatas();
    $('#triggerKey').val(triKey);
    $('#sql').val(sql);
    $('#triggerOpe').val(triOpe);
    $('#triggerVal').val(triVal);
    $('#triggerApp').val(triApp);
    $('#persistentSelect').html('');
    $('#triggerModalPersistentSelectTmpl').tmpl(persistentObj).appendTo('#persistentSelect');
    $('#triggerModal').modal();
  })
  $(document).on('click','#knowledgeModalSubBtn',function(){
    var submitType = $('#knowledgeSubType').val();
    var knowledgeSubUrl = hostUri;
    var msg = "";
    var triggers = [];
    list = $('#triggerListtbody').children('tr');
    if (list.length >0) {
      var triggerKey;
      var triggerOpe;
      var triggerVal;
      var triggerApp;
      for(var i = 0 ; i < list.length ; i++){
        triggerKey = $(list[i]).children('.triggerKey').html()
        sql = $(list[i]).children('.sql').text()
        triggerOpe = $(list[i]).children('.triggerOpe').data('op')
        triggerVal = $(list[i]).children('.triggerVal').html()
        triggerApp = $(list[i]).children('.triggerApp').html()
        persistent = $(list[i]).children('.persistent').html()
        var triRow = {};
        triRow.name = triggerKey;
        triRow.sql = sql;//htmlEntityEncode(sql);
        triRow.op = triggerOpe;
        triRow.value = triggerVal;
        triRow.appStrategyName = triggerApp;
        triRow.persistent = persistent;
        ////////triRow.type = 
        triggers.push(triRow);
      }
    }
    if (submitType == 0) {
      knowledgeSubUrl = hostUri + "/strategy/addKnowledgeStrategy";
      msg = "添加";
    }else{
      knowledgeSubUrl = hostUri + "/strategy/modifyKnowledgeStrategyByName";
      msg = "修改";
    }
    var ksName = $('#knowledgeStrategyName').val();
    var ksDesc = $('#knowledgeStrategyDesc').val();
    var ksExpi = $('#knowledgeStrategyExpi').val();
    var ksStat = $("input[name=knowledgeStatus]:checked").val();
    var ptStat = $("input[name=persistentStatus]:checked").val();
	var knowledgeSubJson = {};
    knowledgeSubJson.key = ksName;
    knowledgeSubJson.description = ksDesc;
    knowledgeSubJson.status = ksStat;
    knowledgeSubJson.persistent = ptStat;
    knowledgeSubJson.expire = ksExpi;
    knowledgeSubJson.appStrategyTriggers = triggers;
    if(ptStat == 1 && ksExpi < 1800){
    	alert("数据过期时间必须大于或等于30分钟才能持久化！");
    	return;
    }
    if(ksName.match(/^[A-Za-z]+[A-Za-z0-9]*$/) == null){
      alert("知识策略名只能为数字和英文字母，且以英文开头");
      return ;
    }
    if(ksExpi.match(/^[0-9]+$/) == null){
      alert("只能为数字");
      return ;
    }
    $.ajax({
      type:'POST',
      url:knowledgeSubUrl,
      data:{
        subJson : JSON.stringify(knowledgeSubJson)
      },
      dataType:'json',
      success:function  (data) {
         var responseObj = eval(data);
         if(responseObj.code==0){
            alert(msg + "成功！");
            refreshMainData(1);
            $('#knowledgeModal').modal('hide');
         }else{
            alert(msg + "失败, Msg:" + responseObj.msg + ", 原因：" + responseObj.data)
         }
      }
   });
  })
  $(document).on('click','#applicationModalSubBtn',function(){
    var submitType = $('#applicationSubType').val();
    var appSubUrl = hostUri;
    var msg = "";
    if (submitType == 0) {
      appSubUrl = hostUri + "/strategy/addAppStrategy";
      msg = "添加";
    }else{
      appSubUrl = hostUri + "/strategy/modifyAppStategyByName";
      msg = "修改";
    }
    var asName = $('#applicationStrategyName').val();
    var asDesc = $('#applicationStrategyDesc').val();
    var asComm = $('#applicationStrategyComm').val();
    var applicationSubJson = {};
    applicationSubJson.key = asName;
    applicationSubJson.description = asDesc;
    applicationSubJson.command = asComm;
    if(asName.match(/^[A-Za-z0-9-_]+$/) == null){
      alert("只能为数字、英文字母、-和_");
      return ;
    }
    $.ajax({
      type:'POST',
      url:appSubUrl,
      data:{
    	  subJson : JSON.stringify(applicationSubJson)
      },
      dataType:'json',
      success:function  (data) {
         var responseObj = eval(data);
         if(responseObj.code==0){
            alert(msg + "成功！");
            refreshMainData(2);
            $('#applicationModal').modal('hide');
         }else{
            alert(msg + "失败,Msg:" + responseObj.msg)
         }
      }
   });
  })
});
