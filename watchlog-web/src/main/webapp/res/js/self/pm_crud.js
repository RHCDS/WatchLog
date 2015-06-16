

	

$(document).ready(function() {	

	//生成聚合报告 
		$('#get_pm_repost_single_form').submit(function(){ //listen for submit event
					// 日志源id  :  selected值 
					var logsrc_id=$('#pm_logsrc_select').val();  
					if(logsrc_id == 0){
						 $("#pm_notice").html("<font color='color'> 请选择日志源</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}
				
					// 开始时间
					var start_time = $('#pm_start_time_id').val(); 
					if(start_time == "" ){
						 $("#pm_notice").html("<font color='color'> 开始时间不能为空</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}
					
					// 结束时间
					var end_time =  $('#pm_end_time_id').val(); 
					if(end_time == "" ){
						 $("#pm_notice").html("<font color='color'> 结束时间不能为空</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}
					$(this).append('<input type="hidden" name="proj" value='+pid+' /> ');   
				   return true;
			});// end 生成聚合报告 
		
});  		// end document.ready


//  点击保存聚合分析，弹出对话框
function pm_analyse_store(log_id,pid){
	 $('#log_id').val(log_id);  //post请求参数  聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	$('#pm_analyse_store_modal').modal('show');  //弹窗modal			
}


// 点击删除当前聚合分析报告，弹出对话框
function pm_analyse_single_destroy(report_id,pid){
	 $('#report_id').val(report_id);  //post请求参数 聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	$('#destroy_pm_report_single_modal').modal('show');  //弹窗modal				
	
}




