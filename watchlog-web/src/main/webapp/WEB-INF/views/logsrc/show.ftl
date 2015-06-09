<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
  <!-- div>
          controller:  ${controller} <br/>
          action: ${action}<br/>
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}<br/>
		</#list>    
</div-->

<div class="container">
	<div class="row" style="border-bottom: solid 1px #eee;">
			<div class="col-md-2">
				<h4> <small style="font-size: 15px;font-weight: bold;">日志源名称</small></h4>
			</div>
			<div class="col-md-10"> 
				<h4> <small>${logsrc_name}</small></h4>
			</div>
	</div>

	<div class="row" style=" border-bottom: solid 1px #eee;">
			<div class="col-md-2">
				<h4> <small style="font-size: 15px;font-weight: bold;">日志源位置</small></h4>
			</div>
	</div>
	
		<div class="row" >
			<div class="col-md-2">
				<h4> <small  style="padding-left: 60px;">服务器地址：</small></h4>
			</div>
			<div class="col-md-10"> 
				<h4> <small>${host_name}</small></h4>
			</div>			
	</div>
	
	<div class="row" >
			<div class="col-md-2">
				<h4> <small  style="padding-left: 60px;">日志文件路径：</small></h4>
			</div>
			<div class="col-md-10"> 
				<h4> <small>${logsrc_path}</small></h4>
			</div>			
	</div>
	
	
	<div class="row" >
			<div class="col-md-2">
				<h4> <small  style="padding-left: 60px;">日志文件名：</small></h4>
			</div>
			<div class="col-md-10"> 
				<h4> <small>${logsrc_file}</small></h4>
			</div>			
	</div>
		
	<div class="row" style=" border-bottom: solid 1px #eee;">
			<div class="col-md-2">
				<h4> <small style="font-size: 15px;font-weight: bold;">匹配规则配置</small></h4>
			</div>
	</div>	
	
		<div class="row" >
			<div class="col-md-2">
				<h4> <small  style="padding-left: 60px;">起始标志：
					<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4>
			</div>
			<div class="col-md-10"> 
				<h4> <small>${start_regex}</small></h4>
			</div>			
	</div>
		

			
	
	<#if  filter_keyword?contains("_AND_")>
			<#assign filter_keyword_flag = "AND">
			<#assign filter_keyword_arr=filter_keyword?split("_AND_")>
	<#elseif  filter_keyword?contains("_OR_")>				
			<#assign filter_keyword_flag = "OR">
			<#assign filter_keyword_arr=filter_keyword?split("_OR_")>
	<#else>
			<#assign filter_keyword_flag = "none">
			<#assign filter_keyword_arr=[filter_keyword]>
	</#if>
	
	<div class="row" >
			<div class="col-md-2">
					<h4> <small  style="padding-left: 60px;">Step1：</small></h4>
			</div><!-- /col-md-2 -->
			<div class="col-md-6"> 
					<div class="row" >
							<h4> <small>过滤关键字	<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4>
					</div><!-- /row -->
					<div  class="row">
							<table  class="table table-bordered">
								<tbody>
										<#list filter_keyword_arr as f>
											<tr>
												<td class="col-md-6">${f}</td>
												<td class="col-md-1">${filter_keyword_flag}</td>
											</tr>											
										</#list>
								</tbody>
							</table>
					</div><!-- /row -->
			</div><!-- /col-md-10 -->			
	</div><!-- /row -->
	
	
		
	<#if  reg_regex?contains("_AND_")>
			<#assign reg_regex_flag = "AND">
			<#assign reg_regex_arr=reg_regex?split("_AND_")>
	<#elseif  reg_regex?contains("_OR_")>				
			<#assign reg_regex_flag = "OR">
			<#assign reg_regex_arr=reg_regex?split("_OR_")>
	<#else>
			<#assign reg_regex_flag = "none">
			<#assign reg_regex_arr=[filter_keyword]>
	</#if>
		<div class="row" >
			<div class="col-md-2">
					<h4> <small  style="padding-left: 60px;">Step1：</small></h4>
			</div><!-- /col-md-2 -->
			<div class="col-md-6"> 
					<div class="row" >
							<h4> <small>正则表达式	<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4>
					</div><!-- /row -->
					<div  class="row">
							<table  class="table table-bordered">
								<tbody>
										<#list reg_regex_arr as r>
											<tr>
												<td class="col-md-6">${r}</td>
												<td class="col-md-1">${reg_regex_flag}</td>
											</tr>											
										</#list>
								</tbody>
							</table>
					</div><!-- /row -->
			</div><!-- /col-md-10 -->			
	</div><!-- /row -->
	

 

	
</div>


</@layout.myLayout>