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


$(function () {
		$(document).ready(function() {
					// 项目级聚合分析表格
					if($("#pm_projlevel_etc_table" ).length != 0) {
						render_table('#pm_projlevel_etc_table');
					}
					// 项目级聚合分析趋势图
					if($("#pm_projlevel_etc_chats" ).length != 0) {
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
										draw_charts('#pm_projlevel_etc_chats', e['results']);
									}
									else{
										$('#pm_projlevel_notice').css('display','block'); // 失败提示文字
										$("#pm_projlevel_notice").html("<font color='red'>"+e['message']+"</font>")
									}
							} // success
						}); // ajax
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
		            dateTimeLabelFormats: {
		            	second: '%Y-%m-%d   %H:%M:%S',
		            	minute: '%Y-%m-%d   %H:%M:00',
		            	hour: '%Y-%m-%d  %H:00:00',
		            	day: '%Y-%m-%d 00:00:00 ',
		            	month: '%Y-%m-01 00:00:00',
		            },		   
		            tickPixelInterval : 50
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


// 聚合报告-项目级-异常分布  日志源显示格式
function pm_projlevel_etc_table_lognameFormatter(value, row, index){
	  	return value;
}

// 聚合报告-项目级-异常分布  日志源显示格式
function pm_projlevel_etc_table_disterrorFormatter(value,row,index){
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


		