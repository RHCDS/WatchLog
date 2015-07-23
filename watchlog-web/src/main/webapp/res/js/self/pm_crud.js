
 //  手动转换datetime 为 timestamp        "2015-06-17 10:12:37" => 1434507157000
function datetime2timestamp(datetime_str){        
         var dt_arr=  datetime_str.split(' ');
         var date_part_arr = dt_arr[0].split('-');
         var time_part_arr = dt_arr[1].split(':');
         var date = new Date(date_part_arr[0],  parseInt(date_part_arr[1], 10) - 1,  date_part_arr[2], time_part_arr[0],  time_part_arr[1], time_part_arr[2] );
         return date.getTime(); 
}

// 手动转换timestamp 为 datetime    1437047489404 = > ""
function timestamp2datetime(timestamp_str){
	var year = timestamp_str.getFullYear();  // 年： 4位
	var month = timestamp_str.getMonth()+1;  // 月： (0-11, 0代表1月)
	if(month<10){	month = '0' + month;	}
	var date = timestamp_str.getDate() ;  // 日：(1-31)
	if(date<10){	date = '0' + date;	}
	var hour = timestamp_str.getHours();  // 时：(0-23)
	if(hour<10){	hour = '0' + hour;	}	
	var minute = timestamp_str.getMinutes();  // 分：(0-59)
	if(minute<10){	minute = '0' + minute;	}		
	var second = timestamp_str.getSeconds();  // 秒：(0-59)
	if(second<10){second = '0' + second;}		
	return year+ "-" +month + "-" + date  + " " +  hour + ":" + minute + ":" + second;
}


// 点击时间插件之前先清理输入框中可能被填充的内容，否则datetimepicker无法正常工作
function clear_input_start_time(){
	$('#pm_start_time_id').val(''); 
}
function clear_input_end_time(){
	$('#pm_end_time_id').val(''); 
}

//  选择时间段
function pm_time_select(duration){
	// 先清空之前的内容
	$('#pm_start_time_id').val(''); 
	$('#pm_end_time_id').val(''); 
	// 获取时间段的开始和结束时间
	var duration_ms = duration * 60 * 1000; // 将分钟转换为毫秒
	var current_time = new Date();   // 当前时间对象
	var passed_time = new Date(current_time.getTime() - duration_ms);   // duration之前时间点对象
	 //时间对象转换为可读string格式
	var current_time_str = timestamp2datetime(current_time);    // 结束时间
	var passed_time_str  = timestamp2datetime(passed_time);  // 开始时间
	// 填充input输入框
	$('#pm_start_time_id').val(passed_time_str); 
	$('#pm_end_time_id').val(current_time_str); 
}

// 查看聚合报告校验
function check_pm_analyse_view(){

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
			$('#get_pm_repost_single_form').append('<input type="hidden" name="proj" value='+pid+' /> ');   
		   return true;
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
  	$('#saved_error_type_total_table').bootstrapTable('destroy').bootstrapTable({
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
  	$('#unsave_error_type_total_table').bootstrapTable('destroy').bootstrapTable({
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




