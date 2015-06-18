
 //  手动转换datetime 为 timestamp        "2015-06-17 10:12:37" => 1434507157000
function datetime2timestamp(datetime_str){        
         var dt_arr=  datetime_str.split(' ');
         var date_part_arr = dt_arr[0].split('-');
         var time_part_arr = dt_arr[1].split(':');
         var date = new Date(date_part_arr[0],  parseInt(date_part_arr[1], 10) - 1,  date_part_arr[2], time_part_arr[0],  time_part_arr[1], time_part_arr[2] );
         return date.getTime(); 
}

	

$(document).ready(function() {	

		// 查看成聚合报告 
		$('#get_pm_repost_single_form').submit(function(){ //listen for submit event
					// 日志源id  :  selected值 
					var logsrc_id=$('#pm_logsrc_select').val(); 
					if(logsrc_id == 0){
						 $("#pm_notice").html("<font color='red'> 请选择日志源</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}
				
					// 开始时间
					var start_time = $('#pm_start_time_id').val(); 
					if(start_time == "" ){
						 $("#pm_notice").html("<font color='red'> 开始时间不能为空</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}
					
					// 结束时间
					var end_time =  $('#pm_end_time_id').val(); 
					if(end_time == "" ){
						 $("#pm_notice").html("<font color='red'> 结束时间不能为空</font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}
					
					// 开始时间不能大于结束时间
					if(  datetime2timestamp(start_time)  >   datetime2timestamp (end_time) ) {
						$("#pm_notice").html("<font color='red'>  开始时间不能大于结束时间 </font></br>");
						 return false;
					}
					else{
						 $("#pm_notice").html("");
					}					
					
					// 提交 查看聚合报告
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

//  校验保存聚合报告名称
function check_pm_report_name(){
	// 报告名
	var report_title = $.trim($('#report_title').val());
	if(report_title == "" ){
		 $("#title_notice").html("<font color='red'> 聚合报告名称不能为空</font></br>");
		 return false;
	}
	else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,255}$/.test(report_title) == false){
		$('#title_notice').html("<font color='red'>不能以下划线开始和结尾;  <br>" +
				"只能包含英文字母，数字，汉字; <br>" +
				"长度不能超过255;</font>");
		return false
	}	
	else{
		 $("#pm_notice").html("");
	}		
	
}

// 点击删除当前聚合分析报告，弹出对话框
function pm_analyse_single_destroy(report_id,pid){
	 $('#report_id').val(report_id);  //post请求参数 聚合报告id 
	 $('#proj').val(pid);  //post请求参数 proj
	$('#destroy_pm_report_single_modal').modal('show');  //弹窗modal				
}


// 点击 saved 聚合报告 异常类型 total 弹窗
function get_saved_error_type_total(report_id, exp_id){
	$("#saved_error_type_total_modal").modal('show');
	  // 表格分页设置
  	$('#saved_error_type_total_table').bootstrapTable({
  		url : "/logsrc/pm_analyse/error_type_total_table",
  		sortName : "date_time",
  		sortOrder: "desc",
  		pageList: "[10, 25, 50, 100, All]",
  		queryParams: function(p){
  			return {
  				report_id : report_id,
  				exp_id: exp_id,
  				limit: p.limit,
  				offset : p.offset,
  				sort: p.sort,
  				order: p.order
  			}
  		}
  	});//  表格end
}


//点击 unsave 聚合报告 异常类型 total 弹窗
function get_unsave_error_type_total(log_id, exp_id, start_time, end_time){
	$("#unsave_error_type_total_modal").modal('show');
	  // 表格分页设置
  	$('#unsave_error_type_total_table').bootstrapTable({
  		url : "/logsrc/pm_analyse/error_type_total_table",
  		sortName : "date_time",
  		sortOrder: "desc",
  		pageList: "[10, 25, 50, 100, All]",
  		queryParams: function(p){
  			return {
  				log_id : log_id,
  				start_time: start_time,
  				end_time : end_time,
  				exp_id: exp_id,
  				limit: p.limit,
  				offset : p.offset,
  				sort: p.sort,
  				order: p.order
  			}
  		}
  	});//  表格end
	
}




