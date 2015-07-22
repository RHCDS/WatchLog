
  $(document).ready(function() {
	// 聚合分析首页表格
	  if($("#pmtable" ).length != 0) {
			  	$('#pmtable').bootstrapTable({
			  		url : "/logsrc/pm_analyse/pmtable",
			  		sortName : "create_time",
			  		sortOrder: "desc",
			  		pageList: "[10, 25, 50, 100, All]",
			  		queryParams: function(p){
			  			return {
			  				proj : pid,
			  				limit: p.limit,
			  				offset : p.offset,
			  				sort: p.sort,
			  				order: p.order
			  			}
			  		}
			  	});
	  }//  表格end
	  	
	  	//  聚合分析首页 开始时间datatimepicker
        $('#pm_start_time_datetimepicker').datetimepicker({
		   // format: 'YYYY-MM-DD HH:mm'
        	  format: 'YYYY-MM-DD HH:mm:ss'   
        });
        
        //   聚合分析首页结束时间datatimepicker
        $('#pm_end_time_datetimepicker').datetimepicker({
		    format: 'YYYY-MM-DD HH:mm:ss'   
        });        
} );  //document
  
  
  
  //聚合报告名称显示   
  function pmreportnameFormatter(value, row, index) {
 	  if(row.report_id==undefined){
 		return "-";	  
 	  }
 	     var maxwidth = 20; 
 	     var value_show = value;
 	     if (value.length > maxwidth) {
 	    	 value_show = value.substring(0, maxwidth) + '...'
 	     }
 	  return  '<a title=' + value +' href="/logsrc/pm_projlevel_save?report_id=' + row.report_id + '&proj=' + pid + '" >' + value_show + '</a>';
  }

  // 操作
  function pmoperateFormatter(value, row, index){
	  if(row.report_id==undefined){
			return "-";	  
		  }
	  else{
		  return [
		          	 // 删除功能
		            '<a class="copy" href="javascript:void(0)"  title="删除"  onclick=pm_report_destroy('+row.report_id+','+ pid + ')>',
		            '<i class=" glyphicon glyphicon-trash"></i>',
		            '</a>  ',	            
		            // 查看详情
		           '<a class="pmshow" title="详情" href="/logsrc/pm_projlevel_save?report_id=' + row.report_id + '&proj=' + pid + '" >',
		            '<i class="  glyphicon glyphicon-align-left"></i>',
		            '</a>'
		        ].join('');
	  }
 }  
  
	// 详情：删除日志报告 + 二次确认 ( 单个删除)
	function pm_report_destroy(report_id, proj){
		console.log(report_id);
		console.log(pid);
		 $('#report_id').val(report_id);  //post请求参数 ids
		 $('#proj').val(proj);  //post请求参数 projj
		$('#destroy_pm_table_single_modal').modal('show');  //弹窗modal		
	}
  
 

  