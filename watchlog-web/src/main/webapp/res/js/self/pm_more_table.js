
// 获取url上特定字段值js片段
function getParam( name )
  {
	   name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	   var regexS = "[\\?&]"+name+"=([^&#]*)";
	   var regex = new RegExp( regexS );
	   var results = regex.exec( window.location.href );
	   if( results == null )
	    return "";
	  else
	   return results[1];
  }

var pid = getParam( 'proj' );
var report_id = getParam( 'report_id' );
var log_id = getParam( 'log_id' );
var start_time = getParam( 'start_time' );
var end_time = getParam( 'end_time' );
var start_time_ct = start_time.replace(/\%20/g, ' ');
var end_time_ct = end_time.replace(/\%20/g, ' ');


  $(document).ready(function() {
	  // 异常分布情况 - 更多 表格
	  if($("#pm_error_dist_table" ).length != 0) {
					  // 聚合分析 - 异常分布情况 表格
					  	$('#pm_error_dist_table').bootstrapTable({
					  		url : "/logsrc/pm_analyse/error_dist_table",
					  		sortName : "date_time",
					  		sortOrder: "desc",
					  		height: "900",
					  		pageList: "[20, 40, 80, 100]",  
					  		pageSize: "20",  // 默认展示条目个数
					  		queryParams: function(p){
					  			return {
					  				proj : pid,
					  				report_id: report_id,
					  				log_id : log_id,
					  				start_time : start_time_ct,
					  				end_time : end_time_ct,
					  				limit: p.limit,
					  				offset : p.offset,
					  				sort: p.sort,
					  				order: p.order
					  			}
					  		}
					  	});//  表格end		  
		}
	  
	  // 异常类型详情 - 更多 表格
	  if($("#pm_error_type_table" ).length != 0){
				  // 聚合分析 - 异常分布情况 表格
				  	$('#pm_error_type_table').bootstrapTable({
				  		url : "/logsrc/pm_analyse/error_type_table",
				  		sortName : "total_count",
				  		sortOrder: "desc",
				  		height: "900",
				  		pageList: "[20, 40, 80, 100]",
				  		pageSize: "20",
				  		queryParams: function(p){
				  			return {
				  				proj : pid,
				  				report_id: report_id,
				  				log_id : log_id,
				  				start_time : start_time_ct,
				  				end_time : end_time_ct,
				  				limit: p.limit,
				  				offset : p.offset,
				  				sort: p.sort,
				  				order: p.order
				  			}
				  		}
				  	});//  表格end		    
	  }
	  	
 } );  //document
  
  // 异常分布情况 (Error type & count) 显示格式 :  数字+hover信息
  function pmdisterrorFormatter(value, row, index){
	  var each_val = "";
	  var content_arr = [];

	  // 遍历数字，拼接type和count到超链接
	  for(i=0; i<value.length; i++){
		  each_val = "<a title='"+value[i]['type']+ "'>"+value[i]['count']+" </a>";
		  content_arr.push(each_val);
	  }
	  var  content_str = content_arr.join(' , ');
	  return content_str
  }
  
  
  
  // 异常类型详情 更多表格 异常总数的显示
  function totalcountFormatter(value, row, index){
 	  if(row.exp_id==undefined){
 			return "-";	  
 		  }
 	  else{
 		  // unsave  : log_id, start_time, end_time
 		  if(report_id == ""){
 			  start_time_str = "'"+start_time_ct+"'";
 			  end_time_str = "'"+end_time_ct+"'";
 	 		  return    '<a href="javascript:void(0)"   onclick="get_unsave_error_type_more_total('+row.exp_id+','+log_id+','+start_time_str+ ',' + end_time_str +')" >' + row.total_count + '</a>' ;
 		  }
 		  // saved : report_id
 		  else{
 	 		  return    '<a href="javascript:void(0)"   onclick="get_saved_error_type_more_total('+row.exp_id+','+report_id + ')" >' + row.total_count + '</a>' ;
 		  }
 	  }//if
  }
  
  
  // 异常类型详情 更多表格 点击total弹窗 unsave
  function get_unsave_error_type_more_total(exp_id, log_id, start_time_str, end_time_str){
		$("#error_type_total_more_modal").modal('show');
		  // 表格分页设置
	  	$('#error_type_total_more_table').bootstrapTable('destroy').bootstrapTable( {
	  		url : "/logsrc/pm_analyse/error_type_total_table",
	  		sortName : "date_time",
	  		sortOrder: "desc",
	  		pageList: "[10, 25, 50, 100, All]",
	  		queryParams: function(p){
	  			return {
	  				exp_id: exp_id,
	  				log_id : log_id,
	  				start_time: start_time_str,
	  				end_time: end_time_str,
	  				limit: p.limit,
	  				offset : p.offset,
	  				sort: p.sort,
	  				order: p.order
	  			}
	  		}
	  	});//  表格end
  }
  
  
  // 异常类型详情 更多表格 点击total弹窗 saved
  function get_saved_error_type_more_total(exp_id, report_id){
		$("#error_type_total_more_modal").modal('show');
		  // 表格分页设置
		// 每次弹窗需要refresh表格，但是refresh方法只能修改url，无法修改queryParams
		// 先destroy，然后在重新load
	  	$('#error_type_total_more_table').bootstrapTable('destroy').bootstrapTable( {
	  		url : "/logsrc/pm_analyse/error_type_total_table",
	  		sortName : "date_time",
	  		sortOrder: "desc",
	  		pageList: "[10, 25, 50, 100, All]",
	  		queryParams: function(p){
	  			return {
	  				exp_id: exp_id,
	  				report_id : report_id,
	  				limit: p.limit,
	  				offset : p.offset,
	  				sort: p.sort,
	  				order: p.order
	  			}
	  		}
	  	});//  表格end
	  	

  }  