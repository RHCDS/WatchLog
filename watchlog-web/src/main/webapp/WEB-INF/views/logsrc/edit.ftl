<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
  <!-- div>
          controller:  ${controller} <br/>
          action: ${action}<br/>
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}<br/>
		</#list>    
</div-->

  <#if RequestParameters.proj?exists >
  	<#assign pid = RequestParameters.proj>
  	

 
<div class="container">

<form  id="logsrc_edit_form" class="form-horizontal" role="form"   accept-charset="UTF-8" action="/logsrc/update" data-remote="true" method="post" onsubmit="return check_update_logsrc()">
		
		
		<div class="form-group"  >
				<label for="logsrc_name" class="col-sm-2 control-label"  style="text-align: left; font-size: medium; color: gray;">日志源名称</label>
		      <div class="col-sm-6">
		         <input type="text" class="form-control"  style="height: 30px;border-radius: 0;"   id="logsrc_name"      name="logsrc_name"  value=${logsrc_name}  placeholder="请输入名字">
		         <div id="warn_edit_logsrc_name"></div>
		      </div>
		</div>
	
		<div class="row" style=" border-bottom: solid 1px #eee; margin-bottom: 10px;">
					<label  class="col-sm-2 control-label"  style="text-align: left; font-size: medium; color: gray; ">日志源位置</label>
		</div>
		
		
					<div class="form-group" >
							<label for="host_name" class="col-sm-2 control-label"  style="text-align: center;color: gray;font-weight: 100;">服务器地址</label>
					      <div class="col-sm-6">
					         <input type="text" class="form-control"  style="height: 30px;border-radius: 0;"   id="host_name"     name="host_name"   value=${host_name} placeholder="请输入服务器地址">
					          <div id="warn_edit_host_name"></div>
					      </div>
					</div>		
		
					<div class="form-group" >
							<label for="logsrc_path" class="col-sm-2 control-label"  style="text-align: center;color: gray;font-weight: 100;">日志文件路径</label>
					      <div class="col-sm-6">
					         <input type="text" class="form-control"  style="height: 30px;border-radius: 0;"   id="logsrc_path"       name="logsrc_path"  value=${logsrc_path}   placeholder="请输入日志文件路径">
					         <div id="warn_edit_logsrc_path"></div>
					      </div>
					</div>		
		

					<div class="form-group" >
							<label for="logsrc_file" class="col-sm-2 control-label"  style="text-align: center;color: gray;font-weight: 100;">日志文件名</label>
					      <div class="col-sm-6">
					         <input type="text" class="form-control"  style="height: 30px;border-radius: 0;"   id="logsrc_file"  name="logsrc_file"      value=${logsrc_file} placeholder="请输入日志文件名">
					          <div id="warn_edit_logsrc_file"></div>
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
	

			
		<div class="row" style=" border-bottom: solid 1px #eee; margin-bottom: 10px;">
					<label  class="col-sm-2 control-label"  style="text-align: left; font-size: medium; color: gray;">匹配规则配置</label>
		</div>	
		
		
					<div class="form-group" >
							<label for="start_regex" class="col-sm-2 control-label"  style="text-align: center;color: gray;font-weight: 100;">
								起始标志<span class="glyphicon glyphicon-question-sign"  aria-hidden="true">
							</label>
					      <div class="col-sm-6">
					         <input type="text" class="form-control"  style="height: 30px;border-radius: 0;"   id="start_regex"  name="start_regex"   value=${start_regex}   placeholder="请输入起始标志">
					         <div id="warn_edit_start_regex"></div>
					      </div>
					</div>		
					
											
				<div class="row" >
						<div class="col-md-2">
								<h4> <small  style="padding-left: 60px;">Step1：</small></h4>
						</div><!-- /col-md-2 -->
						<div class="col-md-6"> 
								<div class="row" >
										<div class="col-md-3"> <h4 style="margin-left: 15px;"> <small>过滤关键字	<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4></div>
										<div class="col-md-2 pull-right">  <a class="btn btn-default btn-sm" href="javascript:void(0);" onclick="add_row_filter_keyword()" role="button">+ 添加行</a></div>
								</div><!-- /row -->					
							<div class="form-group"  style="margin: 0px;" >
							        <table  id="filter_keyword_table" class="table table-bordered">
											<tbody>
													<#assign i=0>
													<#list filter_keyword_arr as f>
																<tr>
																		<td class="col-md-6" style="padding:0px">
																					<input type="text" class="form-control"  id="filter_keyword_input_id"   name="filter_keyword_arr[]"   value=${f}  style="height: 30px;display: block;padding: 0px;margin: 0px;border: 0;width: 100%;border-radius: 0;line-height: 1;" >	
																		</td>
																	<#if i==0> 
																		<td class="col-md-1"  style="padding:0px"> 
																		    	  <select class=" form-control"   id="filter_keyword_select_id"   name="filter_keyword_con"     style="display: block;padding: 0px;margin: 0px;border: 0;width: 100%;border-radius: 0;line-height: 1;">
																			    	  		<#if filter_keyword_flag=="OR">
																					      		<option value="OR"   selected="true" >OR</option>
																					      		<option value="AND"   >AND</option>	
																					      	<#else>
																					      		<option value="AND"   selected="true" >AND</option>
																					      		<option value="OR"   >OR</option>				
																			    	  		</#if>
																			      </select>	<!-- /select -->															
																	</td>																		
																	<#else>
																		<td class="col-md-1 filter_keyword_td_select"  style="padding:0px">${filter_keyword_flag}</td>
																	</#if>
																</tr><!-- /tr -->
																<#assign i=1>
														</#list>																																					
											</tbody>
										</table><!-- /table -->			   
							</div><!--/ form-group -->
					</div><!-- /col-md-6 -->			
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
										<div class="col-md-3"> <h4 style="margin-left: 15px;"> <small>正则表达式		<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4></div>
										<div class="col-md-2 pull-right">  <a class="btn btn-default btn-sm" href="javascript:void(0);" onclick="add_row_reg_regex()" role="button">+ 添加行</a></div>										
								</div><!-- /row -->
								<div class="form-group"  style="margin: 0px;" >
										<table  id="reg_regex_table" class="table table-bordered">
											<tbody>
													<#assign j=0>
													<#list reg_regex_arr as r>
															<tr>
																	<td class="col-md-6" style="padding:0px">
																				<input type="text" class="form-control"  id="reg_regex_input_id"   name="reg_regex_arr[]"  value=${r}  style="display: block;padding: 0px;margin: 0px;border: 0;width: 100%;border-radius: 0;line-height: 1;" >	
																	</td>
																	<#if j==0> 
																		<td class="col-md-1"  style="padding:0px"> 
																		    	  <select class=" form-control"   id="reg_regex_select_id"   name="reg_regex_con"     style="display: block;padding: 0px;margin: 0px;border: 0;width: 100%;border-radius: 0;line-height: 1;">
																			    	  		<#if reg_regex_flag=="OR">
																					      		<option value="OR"   selected="true" >OR</option>
																					      		<option value="AND"   >AND</option>	
																					      	<#else>
																					      		<option value="AND"   selected="true" >AND</option>
																					      		<option value="OR"   >OR</option>				
																			    	  		</#if>
																			      </select>	<!-- /select -->															
																	</td>																		
																	<#else>
																		<td class="col-md-1 reg_regex_td_select"  style="padding:0px">${reg_regex_flag}</td>
																	</#if>
															</tr><!-- /tr -->		
															<#assign j=1>
													</#list>		
											</tbody>
										</table>
								</div><!-- /row -->
						</div><!-- /col-md-10 -->			
				</div><!-- /row -->
		
		<input type="hidden" name="id"   value=${id} />		
		<input type="hidden"  name="proj"   value="${pid}"  />
		
   <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
         <button type="submit" class="btn btn-primary">保存</button>
          <a class="btn btn-default" href="/logsrc/manage?proj=${pid}" role="button">取消</a>
      </div>
   </div>   
   

	 
</form>
	
</div>
<#else>
   <div class="container"> 请选择项目，亲:)</div>
</#if>
    

</@layout.myLayout>