<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>

  <#if RequestParameters.proj?exists  >
  	<#assign pid = RequestParameters.proj>	
  	<div class="container">
  	
  	
							<!-- 删除当前聚合分析报告form-->
							<form  id="destroy_pm_analyse_single_form" action="/logsrc/pm_analyse/destroy"  method="post"   class="form-horizontal" role="form"   accept-charset="UTF-8"   data-remote="true">     	  
								<!-- 模态框（Modal） -->
								<div class="modal fade" id="destroy_pm_report_single_modal"  tabindex="-1" role="dialog"    aria-labelledby="myModalLabel" aria-hidden="true">
									   <div class="modal-dialog">
												      <div class="modal-content">
												      		<input type="hidden" id="report_id" name="report_id"  />
												      		<input type="hidden" id="proj" name="proj"  />
												      		 <!-- header  -->
													         <div class="modal-header">
														            <button type="button" class="close"    data-dismiss="modal" aria-hidden="true">   &times;  </button>
														            <h4 class="modal-title" id="myModalLabel">    确认删除    </h4>
													         </div>
													         <!-- body  -->
													         <div class="modal-body">
													            	您确定要删除选中当前聚合分析报告？
													         </div>
													         <!-- footer  -->
													         <div class="modal-footer">
													           <button type="submit" class="btn btn-primary">  确定  </button>
													            <button type="button" class="btn btn-default"    data-dismiss="modal"> 取消  </button>
													         </div>
												      </div><!-- /.modal-content -->
										</div><!-- /.modal-dialog -->
								</div><!-- 模态框（Modal） -->
							</form>			
				  	
				  			<!-- 保存聚合分析页面 导航条-->
							<div class="row"   style="border-bottom: solid 1px #ddd;height: 45px;" >
									<div class="col-sm-12"> 
													<table  class="table wratb  removebd">
														<tbody>
																	<tr>
																		<td class="col-sm-2"> <a href="/logsrc/pm_analyse?proj=${pid}"> 返回 &#62; </a>	&#160;&#160;<small>${title}</small></td>
																		<td class="col-sm-3">开始时间：${start_time}</td>
																		<td class="col-sm-3">结束时间：${end_time}</td>
																		<td class="col-sm-2"><button id="remove" class="btn btn-danger  btn-xs"  onclick="pm_analyse_single_destroy(${report_id}, ${pid})" > &#160;&#160;删除当前报告&#160;&#160;</button></td>
																	</tr>		
														</tbody>
													</table>
									</div><!-- /col-sm-12 -->			
							</div><!-- /row -->			
								
				  	
			<div  class="row">  <!-- row 异常分布情况 + 日志源详情-->
						<!-- 异常分布表格-->
						<div class="col-sm-7">
												<div class="row detail-head-text">
															<p style="font-size: 15px;font-weight: bold;">异常分布情况</p>
												</div><!-- row -->			
																		
												<table  class="table table-bordered">
													<tbody>
															<#if  pm_error_dist_table?has_content >
																			<tr>
																				<th class="col-sm-4">采样时间</th>
																				<th class="col-sm-6">异常类型和数量</th>
																				<th class="col-sm-2">异常总数</th>
																			</tr>		
																			<#list pm_error_dist_table as data>
																				<tr>
																					<td class="col-sm-4">${data['date_time']}</td>
																					<td class="col-sm-6">
																								<#list data['error_tc'] as dt>
																										<#if dt_has_next>
																												<a title='${dt['type']}'   href ="#" >&#160;${dt['count']}&#160;</a>,
																										<#else>
																												<a title='${dt['type']}'   href ="#" >&#160;${dt['count']}</a>
																										</#if>
																								 </#list>
																					 </td>																	
																					<td class="col-sm-2">${data['total_count']}</td>
																				</tr>																 
																		</#list>		
																</#if>																																
													</tbody>
												</table>
												
												<!-- 更多-->
												<#if  pm_error_dist_table?has_content>
														<div class="row pull-right"  style="margin-right: 0px;"><a target="_blank" href="/logsrc/pm_analyse/error_dist_more?report_id=${report_id}&proj=${pid}">更多&#62;&#62;</a></div>
												<#else>
														<div class="row   alert alert-info"  style="margin-right: 0px;">暂无数据</div>
												</#if>			
						</div><!--  col-sm-7 -->
					
					
							<!-- 右侧： 日志源详情-->
							<div class="col-sm-4 log_info_border" >
									<div class="row  detail-head-text">
												<p style="font-size: 15px;font-weight: bold;">日志源配置</p>
									</div><!-- row -->
									
										<div class="row" >
												<div class="col-sm-12"> 
																<table  class="table wratb  removebd">
																	<tbody>
																				<tr>
																					<td class="col-sm-4">日志源名称：</td>
																					<td class="col-sm-8">${logsrc_name}</td>
																				</tr>																			
																				<tr>
																					<td class="col-sm-4">服务器地址：</td>
																					<td class="col-sm-8">${host_name}</td>
																				</tr>		
																				<tr>
																					<td class="col-sm-4">日志文件路径：</td>
																					<td class="col-sm-8">${logsrc_path}</td>
																				</tr>		
																				<tr>
																					<td class="col-sm-4">日志文件名：</td>
																					<td class="col-sm-8">${logsrc_file}</td>
																				</tr>																																																											
																				<tr>
																					<td class="col-sm-4">起始地址：</td>
																					<td class="col-sm-8">${start_regex}</td>
																				</tr>											
																	</tbody>
																</table>
												</div><!-- /col-sm-12 -->			
										</div><!-- /row -->					
																		
									<#if  filter_keyword?contains("_AND_")>
											<#assign filter_keyword_flag = "AND">
											<#assign filter_keyword_arr=filter_keyword?split("_AND_")>
									<#elseif  filter_keyword?contains("_OR_")>				
											<#assign filter_keyword_flag = "OR">
											<#assign filter_keyword_arr=filter_keyword?split("_OR_")>
									<#else>
											<#assign filter_keyword_flag = "">
											<#assign filter_keyword_arr=[filter_keyword]>
									</#if>
							
									<div class="row"    style="height:25px;">
												<div class="col-sm-12"> <p style="font-size: 15px;font-weight: bold;">Step 1:  过滤关键字</p>	</div><!-- /col-sm-12 -->		
									</div><!-- row -->						
							
									<div class="row" >
											<div class="col-sm-12"> 
															<table  class="table table-bordered wratb">
																<tbody>
																		<#list filter_keyword_arr as f>
																			<tr>
																				<td class="col-sm-6">${f}</td>
																				<td class="col-sm-1">${filter_keyword_flag}</td>
																			</tr>											
																		</#list>
																</tbody>
															</table>
											</div><!-- /col-sm-12 -->			
									</div><!-- /row -->
							
									<#if  reg_regex?contains("_AND_")>
											<#assign reg_regex_flag = "AND">
											<#assign reg_regex_arr=reg_regex?split("_AND_")>
									<#elseif  reg_regex?contains("_OR_")>				
											<#assign reg_regex_flag = "OR">
											<#assign reg_regex_arr=reg_regex?split("_OR_")>
									<#else>
											<#assign reg_regex_flag = "">
											<#assign reg_regex_arr=[reg_regex]>
									</#if>				
									
									<div class="row"    style="height:25px;">
											<div class="col-sm-12"> <p style="font-size: 15px;font-weight: bold;">Step 2:  正则表达式</p>	</div><!-- /col-sm-12 -->		
									</div>						
									
									<div class="row" >
											<div class="col-sm-12"> 
															<table  class="table table-bordered wratb">
																<tbody>
																		<#list reg_regex_arr as r>
																			<tr>
																				<td class="col-sm-6">${r}</td>
																				<td class="col-sm-1">${reg_regex_flag}</td>
																			</tr>											
																		</#list>
																</tbody>
															</table>
											</div><!-- /col-sm-12 -->			
									</div><!-- /row -->				
							</div>	<!-- 右侧： 日志源详情-->
				
		</div> <!-- row 异常分布情况 + 日志源详情-->			
		
		
			<div class="row"    style="height:25px;   border-bottom: solid 1px #ddd; margin-bottom:30px;">
					<div class="col-sm-12"> <p style="font-size: 15px;font-weight: bold;">异常类型详情</p>	</div><!-- /col-sm-12 -->		
			</div>		
			
			<div  class="row">  <!-- row 异常类型详情-->
  	
								<!-- 模态框（Modal） -->
								<div class="modal fade" id="saved_error_type_total_modal"  tabindex="-1" role="dialog"    aria-labelledby="myModalLabel" aria-hidden="true"   data-backdrop="static">
									   <div class="modal-dialog">
												      <div class="modal-content">
												      		<input type="hidden" id="report_id" name="report_id"  />
												      		<input type="hidden" id="proj" name="proj"  />
												      		 <!-- header  -->
													         <div class="modal-header">
														            <button type="button" class="close"    data-dismiss="modal" aria-hidden="true" >   &times;  </button>
														            <h4 class="modal-title" id="myModalLabel">    异常类型详情 </h4>
													         </div>												      		
													         <!-- body  -->
													         <div class="modal-body">
																           <table id="saved_error_type_total_table"    data-toggle="toolbar"     data-height="500"     data-side-pagination="server"    data-pagination="true"      data-search="false">
																            <thead>
																            <tr>
																                <th data-field="date_time"   data-sortable="true"    > 采样时间</th>
																                 <th data-field="total_count" data-sortable="true"  > 异常总数</th>
																            </tr>
																            </thead>
																        </table>													            	
													         </div>
												      </div><!-- /.modal-content -->
										</div><!-- /.modal-dialog -->
								</div><!-- 模态框（Modal） -->
								
						<!-- 异常分布表格-->
						<div class="col-sm-12"    style="/*border:solid 1px yellow*/">
												<table  class="table table-bordered">
													<tbody>
													<#if  pm_error_type_table?has_content>
																<tr>
																		<th class="col-sm-4">异常类型</th>
																		<th class="col-sm-7">原始日志实例 </th>
																		<th class="col-sm-1">异常总数</th>
																</tr>		
																<#list pm_error_type_table as data>
																	<#assign error_type_str = data['error_type']?replace("(\t)+", "<br/>",'r')>
																	<#assign error_example_str = data['error_example']?replace("(\t)+", "<br/>",'r')>
																	<tr>
																			<td class="col-sm-4">${error_type_str}</td>
																			<td class="col-sm-7">${error_example_str}</td>																	
																			<td class="col-sm-1"><a  href="javascript:void(0)"  
																			onclick="get_saved_error_type_total(${report_id},  ${data['exp_id']})">${data['total_count']}</a></td>																		
																	</tr>																 
															</#list>		
													</#if>																	
													</tbody>
												</table>
												
												<!-- 更多-->
												<#if  pm_error_type_table?has_content>
														<div class="row pull-right" style="margin-right: 0px;"><a target="_blank" href="/logsrc/pm_analyse/error_type_more?report_id=${report_id}&proj=${pid}">更多&#62;&#62;</a></div>
												<#else>
														<div class="row   alert alert-info"  style="margin-right: 0px;">暂无数据</div>
												</#if>										
						</div><!--  col-sm-12 -->		
				</div> <!-- row 异常分布情况 + 日志源详情-->									
</div> 		 <!-- container-->	
				
<#elseif  !RequestParameters.proj?exists >
	   <div class="container  alert alert-warning"> 请先选择右上角项目</div>
<#else>
		<div class="container  alert alert-warning">其他位置错误，请联系管理员</div>
</#if>
    

</@layout.myLayout>