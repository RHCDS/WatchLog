
// 用户输入时间格式转换为数字timestamp
function datetime2timestamp(datetime_str){
	 	//  手动转换datetime 为 timestamp		 
		 var dt_arr=  datetime_str.split(' ');
		 var date_part_arr = dt_arr[0].split('-');
		 var time_part_arr = dt_arr[1].split(':');
		 var date = new Date(date_part_arr[0],  parseInt(date_part_arr[1], 10) - 1,  date_part_arr[2], time_part_arr[0],  time_part_arr[1] );
		 return date.getTime(); //1434096660000    2014-06-12 16:11
		 // new Date(1434096660000)    Fri Jun 12 2015 16:11:00 GMT+0800 (CST)
	}
	

$(document).ready(function() {	

	//生成聚合报告 
		$('#destroy_pm_logsrc_single_form').submit(function(){ //listen for submit event
					// 日志源id  :  selected值 
					var logsrc_id=$('#pm_logsrc_select').val();  
					//console.log("logsrc id: " + logsrc_id);
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
						 var start_timestamp = datetime2timestamp(start_time);
					}
					
					// 结束时间
					var end_time =  $('#pm_end_time_id').val(); 
					if(end_time == "" ){
						 $("#pm_notice").html("<font color='color'> 结束时间不能为空</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
						 var end_timestamp = datetime2timestamp(end_time);
					}
					
					$(this).append('<input type="hidden" name="proj" value='+pid+' /> ');   
				    $(this).append('<input type="hidden" name="start_time" value='+start_timestamp+' /> ');               
				    $(this).append('<input type="hidden" name="end_time" value='+end_timestamp+' /> ');               
				   return true;
			});// end 生成聚合报告 
		
});  		// end document.ready


//  点击保存聚合分析，弹出对话框
function pm_analyse_store(report_id,pid){
	 $('#report_id').val(report_id);  //post请求参数  聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	$('#pm_analyse_store_modal').modal('show');  //弹窗modal			
}


// 点击删除当前聚合分析报告，弹出对话框
function pm_analyse_single_destroy(report_id,pid){
	 $('#report_id').val(report_id);  //post请求参数 聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	$('#destroy_pm_report_single_modal').modal('show');  //弹窗modal				
	
}




