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
						$.ajax({
							type: 'GET',
							url : '/logsrc/pm_projlevel_etc_charts',
							data: {proj: pid, report_id: report_id, start_time: start_time, end_time: end_time},
							dataType: "json",
							success : function(e){
								
							}
							
						});
						draw_charts('#pm_projlevel_etc_chats');
					}
		} );  //document
});

// table表格封装
function render_table(div_table_id){
	console.log(start_time);
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
function draw_charts(div_charts_id){
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
		            animation: Highcharts.svg, // don't animate in old IE
		            marginRight: 10,
		        },
		        title: { text:''},
		        plotOptions: {
		            series: {
		                lineWidth: 1,   //曲线粗细，默认是2
		                marker: {
		                	radius: 3,  //曲线点半径，默认是4
		                	 symbol: 'diamond' //曲线点类型："circle", "square", "diamond", "triangle","triangle-down"，默认是"circle"
		                }
		            }
		        },
		        xAxis: {
		            type: 'datetime',
		            tickPixelInterval: 150
		        },
		        yAxis: {
		            title: {
		                text: 'Value'
		            },
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		        tooltip: {  
		            formatter: function () {
		                return '<b>' + this.series.name + '</b><br/>' +
		                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
		                    Highcharts.numberFormat(this.y, 2);
		            }
		        },
		        legend: {  
		            enabled: false
		        },
		        exporting: {
		            enabled: false
		        },
		        series: [{
		            name: '日志源log1',
		            data: (function () {
		                // generate an array of random data
		                var data = [],
		                    time = (new Date()).getTime(), // 毫秒
		                    i;
		                for (i = -9; i <= 0; i += 1) {
		                	var date_time = time + i * 1000 ; 
		                	var count = Math.random() * 10;
//		                	console.log('date_time : ' + date_time);
//		                	console.log('count : ' + count);
		                    data.push({
		                        x: date_time,
		                        y: count
		                    });
		                }
		                console.log(data);
		                return data;
		            }())
		        }]
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


		