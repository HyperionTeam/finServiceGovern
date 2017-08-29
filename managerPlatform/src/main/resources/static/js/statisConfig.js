
function getDataThreshold(){
	$.ajax({
		url:hostUri + '/data/getDataThreshold',
		type:"GET",
		cache:false,
		dataType:"json",
		error: function(){
			alert("数据加载失败!");
		},
	    success: function(data){
	    	if(data.code != 0){
	    		alert("数据加载失败!");
	    	}else{
	    		$('#app_launch_time').val(data.data.launchTimeThreshold);
	    		$('#native_page_loadtime').val(data.data.nativeLoadTimeThreshold);
	    		$('#rn_page_loadtime').val(data.data.rnLoadTimeThreshold);
	    	}
	    }
	});
}

$(document).ready(function(){
	
	getDataThreshold();
	
	$('#submit_modify').click(function(){
		var launchTimeThreshold = $('#app_launch_time').val();
		var nativeLoadTimeThreshold = $('#native_page_loadtime').val();
		var rnLoadTimeThreshold = $('#rn_page_loadtime').val();
		if(launchTimeThreshold < 0 || nativeLoadTimeThreshold < 0 || rnLoadTimeThreshold < 0){
			alert('阀值不能小于0');
		}
		$.ajax({
			url:hostUri + '/data/setDataThreshold',
			data:{
				launchTimeThreshold:launchTimeThreshold,
				nativeLoadTimeThreshold:nativeLoadTimeThreshold,
				rnLoadTimeThreshold:rnLoadTimeThreshold
			},
			type:"GET",
			cache:false,
			dataType:"json",
			error:function(){
				console.log('error');
				alert("修改失败!");
			},
			success:function(data){
				if(data.code != 0){
					console.log('code');
					alert("修改失败!");
				}else{
					alert("修改成功!");
					getDataThreshold();
				}
			}
		});
	});
});