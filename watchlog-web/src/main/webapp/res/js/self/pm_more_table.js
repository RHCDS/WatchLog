
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

  $(document).ready(function() {
	  // 异常分布情况 - 更多 表格
	  if($("#pm_error_dist_table" ).length != 0) {
					  // 聚合分析 - 异常分布情况 表格
					  	$('#pm_error_dist_table').bootstrapTable({
					  		url : "/logsrc/pm_analyse/error_dist_table",
					  		sortName : "sample_time",
					  		sortOrder: "desc",
					  		pageList: "[10,50, 100, All]",
					  		queryParams: function(p){
					  			return {
					  				proj : pid,
					  				report_id: report_id,
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
				  		sortName : "sample_time",
				  		sortOrder: "desc",
				  		pageList: "[10,50, 100, All]",
				  		queryParams: function(p){
				  			return {
				  				proj : pid,
				  				report_id: report_id,
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
		  each_val = "<a title="+value[i]['type']+ ">"+value[i]['count']+" </a>";
		  content_arr.push(each_val);
	  }
	  var  content_str = content_arr.join(' , ');
	  return content_str
  }