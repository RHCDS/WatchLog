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
//var start_time_ct = start_time.replace(/\+/g, ' ');
var start_time_ct = decodeURIComponent(start_time).replace(/\+/g, ' ');
var end_time_ct = decodeURIComponent(end_time).replace(/\+/g, ' ');


//  手动转换datetime 为 timestamp        "2015-06-17 10:12:37" => 1434507157000
function datetime2timestamp(datetime_str){        
         var dt_arr=  datetime_str.split(' ');
         var date_part_arr = dt_arr[0].split('-');
         var time_part_arr = dt_arr[1].split(':');
         var date = new Date(date_part_arr[0],  parseInt(date_part_arr[1], 10) - 1,  date_part_arr[2], time_part_arr[0],  time_part_arr[1], time_part_arr[2] );
         return date.getTime(); 
}


$(function () {
		$(document).ready(function() {
					// 项目级聚合分析表格 - 未保存
					if($("#pm_projlevel_unsave_etc_table" ).length != 0) {
						// 生成表格
						render_table('#pm_projlevel_unsave_etc_table');
					}
					
					// 项目级聚合分析表格 - 未保存
					if($("#pm_projlevel_saved_etc_table" ).length != 0) {
						// 生成表格
						render_table('#pm_projlevel_saved_etc_table');
					}
					
					
					// 项目级聚合分析趋势图
					if($("#pm_projlevel_etc_chats" ).length != 0) {
							// 检测时间范围
							var MAX_GRAPH_TIME_RANGE = 7*24*3600*1000; // 不能超过的7天，精确到毫秒
							if(end_time_ct !="" &&  start_time_ct!="" && datetime2timestamp(end_time_ct) -  datetime2timestamp(start_time_ct) > MAX_GRAPH_TIME_RANGE ){
									$('#pm_projlevel_notice').css('display','block'); // 失败提示文字
									$("#pm_projlevel_notice").html("<font color='red'>只支持查询7天内的数据</font>")
							}
							else{
									// ajax 请求获取chart数据
									$('#pm_projlevel_notice').css('display','none'); // 失败提示文字
									$('#rt_loading').css('display','block');
									$.ajax({
										type: 'GET',
										url : '/logsrc/pm_projlevel_etc_charts',
										data: {proj: pid, 
											report_id: report_id, 
											start_time: start_time_ct, 
											end_time: end_time_ct},
										dataType: "json",
										success : function(e){
											$('#rt_loading').css('display','none');
												if(e['status'] == 0){
													// 生成趋势图
													draw_charts('#pm_projlevel_etc_chats', e['results']);
												}
												else{
													$('#pm_projlevel_notice').css('display','block'); // 失败提示文字
													$("#pm_projlevel_notice").html("<font color='red'>"+e['message']+"</font>")
												}
										} // success
									}); // ajax							
							}// else
					}// 趋势图
		} );  //document
});

// table表格封装
function render_table(div_table_id){
	  	$(div_table_id).bootstrapTable({
	  		url : "/logsrc/pm_projlevel_etc_table",
	  		pageList: "[10, 25, 50, 100, All]",
	  		queryParams: function(p){
	  			return {
	  				proj : pid,
	  				report_id: report_id,
	  				start_time : start_time_ct,
	  				end_time : end_time_ct,
	  				limit: p.limit,
	  				offset : p.offset,
	  			}
	  		}
	  	});
}

// highcharts 趋势图封装
function draw_charts(div_charts_id, results){
			// highcharts 画图
		    Highcharts.setOptions({
		        global: {
		            useUTC: false
		        }
		    });
		    $(div_charts_id).highcharts({
		    	credits: {enabled: false}, 
		        chart: {
		            type: 'spline',
		            height: 500,
		            animation: Highcharts.svg, // don't animate in old IE
		        },
		        title: { text: null},
		        plotOptions: {
		            series: {
		              lineWidth: '1px',   //曲线粗细，默认是2
		              marker: {   // 鼠标悬浮设置
		                enabled: false,  // 是否显示point点
		                states: {
		                  hover: {
		                    enabled: true,
		                  },
		                  select: {
		                    enabled: true,
		                  },
		                },
		              },
		          }},
		        xAxis: {
		            type: 'datetime',
//		            dateTimeLabelFormats: {
//		            	second: '%Y-%m-%d   %H:%M:%S',
//		            	minute: '%Y-%m-%d   %H:%M:00',
//		            	hour: '%Y-%m-%d  %H:00:00',
//		            	day: '%Y-%m-%d 00:00:00 ',
//		            	month: '%Y-%m-01 00:00:00',
//		            },		   
//		            tickPixelInterval : 50,
//		            labels: {
//		                rotation: -60
//		            }
		        },
		        yAxis: {
		            title: {
		                text: '异常总数'
		            },
		        },
		        legend: {  
		            enabled: true
		        },
		        tooltip:{
		        	shared: true,
		        	xDateFormat: "%Y-%m-%d %H:%M:%S",
		        },
		        series: (function(){
		        	var series_list = [], i;
		        	for(i=0; i<results.length; i ++){
		        		series_list.push({name: results[i]['logsrc_name'], data: results[i]['data']});
		        	}
		        	return series_list;
		        }()),
		        lang: {
		            noData: "趋势图没有数据，请联系管理员"
		        },		        
		    });// highcharts
}


// 聚合报告-项目级-异常分布    日志源显示格式   未保存
function pm_projlevel_unsave_etc_table_lognameFormatter(value, row, index){
	  if(value!=undefined){
		     var maxwidth = 20; 
		     var value_show = value;
		     if (value.length > maxwidth-3) {
		    	 value_show = value.substring(0, maxwidth-3) + '...'
		     }
		     var link_html =  '<a title=' + value +' href="/logsrc/pm_analyse_unsave?proj=' + pid + '&log_id=' + row.log_id +'&start_time=' + start_time_ct + '&end_time=' + end_time_ct + '" >&nbsp;&gt;&gt;查看详情</a>';	
		     var display_html = '<font size=1px>'+value_show+ link_html + '</font>';
		  return  display_html;		  
	  }
}



//聚合报告-项目级-异常分布    日志源显示格式   保存
function pm_projlevel_saved_etc_table_lognameFormatter(value, row, index){
	  if(value!=undefined){
		     var maxwidth = 20; 
		     var value_show = value;
		     if (value.length > maxwidth-3) {
		    	 value_show = value.substring(0, maxwidth-3) + '...'
		     }
		     var link_html =  '<a title=' + value +' href="/logsrc/pm_analyse_unsave?proj=' + pid + '&report_id=' + row.report_id +'" >&nbsp;&gt;&gt;查看详情</a>';	
		     var display_html = '<font size=1px>'+value_show+ link_html + '</font>';
		  return  display_html;		  
	  }
}

// 聚合报告-项目级-异常分布  日志源显示格式
function pm_projlevel_etc_table_disterrorFormatter(value,row,index){
		  var each_val = "";
		  var content_arr = [];
		  // 遍历数字，拼接type和count到超链接
		  var i;
		  for(i=0; i<value.length; i++){
			  each_val = "<a  title='"+value[i]['type']+ "'>"+value[i]['count']+" </a>";
			  content_arr.push(each_val);
		  }
		  var  content_str = content_arr.join(' , ');
		  return content_str		  
}

//点击保存聚合分析，弹出对话框
function pm_analyse_store(){
	 $('#proj').val(pid);  //post请求参数 proj
	 $('#start_time').val(start_time_ct);  //post请求参数 start_time
	 $('#end_time').val(end_time_ct);  //post请求参数 end_time
	$('#pm_analyse_store_modal').modal('show');  //弹窗modal			
}


//校验保存聚合报告名称
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



		