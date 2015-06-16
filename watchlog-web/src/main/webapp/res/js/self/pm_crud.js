

	

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
function pm_analyse_store(log_id, pid, start_time, end_time){
	 $('#log_id').val(log_id);  //post请求参数  聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	 $('#start_time').val(start_time);  //post请求参数 start_time
	 $('#end_time').val(end_time);  //post请求参数 end_time
	 
	$('#pm_analyse_store_modal').modal('show');  //弹窗modal			
}


// 点击删除当前聚合分析报告，弹出对话框
function pm_analyse_single_destroy(report_id,pid){
	 $('#report_id').val(report_id);  //post请求参数 聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	$('#destroy_pm_report_single_modal').modal('show');  //弹窗modal				
}


// 点击 saved 类型 total
function get_error_type_total(report_id, exp_id, total_count){
	console.log("get_error_type_total");
	console.log("report_id: " + report_id);
	console.log("exp_id: " + exp_id);
	console.log("total_count: " + total_count);
	
	$("#get_error_type_total_modal").modal('show');
	
	  // 表格分页设置
  	$('#error_type_total_table').bootstrapTable({
  		url : "/logsrc/saved_error_type_total_table",
  		sortName : "date_time",
  		sortOrder: "desc",
  		pageList: "[10, 25, 50, 100, All]",
  		queryParams: function(p){
  			return {
  				report_id : report_id,
  				exp_id: exp_id,
  				total_count : total_count,
  				limit: p.limit,
  				offset : p.offset,
  				sort: p.sort,
  				order: p.order
  			}
  		}
  	});//  表格end
  	
  	
  	
}




