<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
  <!-- div>
          controller:  ${controller} </br>
          action: ${action}</br>
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}</br>
		</#list>    
		
		logs[0]: ${logs[0]}   </br>
		
		logsrc_name: ${logsrc_name} </br>
		host_name : ${host_name} </br>
		logsrc_file : ${logsrc_file}  </br>
		start_regex: ${start_regex}  </br>
		filter_keyword: ${filter_keyword}  </br>
		reg_regex: ${reg_regex}  </br>
</div -->

				
  <#if RequestParameters.proj?exists >
  	<#assign pid = RequestParameters.proj>	
		  
				<!-- 左侧： 日志源名称列表-->
				<div class="col-md-2"  style="border:solid 1px red;padding-left:0px; padding-right:0px;">
						<div class=" btn-group-vertical  col-md-12 " role="group" >
								<#list logs as log_str>
										<#assign lg_arr=log_str?split("#")>
										<#if lg_arr[0]?exists><#assign id=lg_arr[0]>
												<#if lg_arr[1]?exists><a href="/logsrc/rt_analyse?proj=${pid}&log_id=${id}" class="btn btn-default  col-lg-12" >${lg_arr[1]}</a></#if>
								  		</#if>
								</#list>
						</div>
				</div><!--  col-md-2 -->
				
				
				<!-- 左侧： 实时表格-->
				<div class="col-md-6"    style="border:solid 1px yellow">
				<#list rt_table as data>
						${data['datetime']}<br>
						${data['totalcount']}<br>
						<#list data['logtc'] as x>
							${x['type']}
							${x['count']}<br>
						 </#list>	
				</#list>					

					
				</div><!--  col-md-6 -->
					
					
				<!-- 左侧： 日志源详情-->
				<div class="col-md-4"    style="border:solid 1px blue">
				
				</div><!--  col-md-4 -->

<#else>
   <div class="container"> 请选择项目，亲:)</div>
</#if>
    

</@layout.myLayout>