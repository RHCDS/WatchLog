<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>


  <#if RequestParameters.proj?exists  && logs?has_content >
  	<#assign pid = RequestParameters.proj>
  	

		        
    <div class="container">

							<!-- 生成聚合报告导航表单-->
    						<form  id="destroy_pm_logsrc_single_form" action="/logsrc/pm_analyse/create_report"     method="post"   class="form-horizontal" role="form"   accept-charset="UTF-8"   data-remote="true"> 
											
											<div class="row">
													  <div class="col-sm-1">
													  				 <h5><p class="text-left" style="font-size: 15px;font-weight: bold;">日志源</p></h5>
													  	</div>
													 <div class="col-sm-2">
																	  <select   id="pm_logsrc_select"   name="log_id" class="form-control">
																	  			<option value= 0> 请选择日志源</option>
																				<#list logs as log_str>
																						<#assign lg_arr=log_str?split("#")>
																						<#if lg_arr[0]?exists><#assign id=lg_arr[0]>
																								<option value=${id}> ${lg_arr[1]}</option>
																				  		</#if>
																				</#list>													  
																	</select>    
													  </div>
													 
													  <div class="col-sm-1">
													  				 <h5><p class="text-left" style="font-size: 15px;font-weight: bold;">开始时间</p></h5>
													  	</div>									  
				 										<div class="col-sm-2">									  				
													                <div class='input-group date' id='pm_start_time_datetimepicker'>
														                    <input type='text'   id="pm_start_time_id"   name="start_time" class="form-control    input-sm"   placeholder="开始时间"/>
														                    <span class="input-group-addon">
														                        <span class="glyphicon glyphicon-calendar"></span>
														                    </span>
													                </div>
													  </div>
													  
													  <div class="col-sm-1">
													  			 <h5><p class="text-left" style="font-size: 15px;font-weight: bold;">结束时间</p></h5>  
													  	</div>											  
													  <div class="col-sm-2">
													                <div class='input-group date' id='pm_end_time_datetimepicker'>
														                    <input type='text'     id="pm_end_time_id"   name="end_time" class="form-control    input-sm"   placeholder="结束时间" />
														                    <span class="input-group-addon">
														                        <span class="glyphicon glyphicon-calendar"></span>
														                    </span>
													                </div>									    			
													  </div>
				
													  <div class="col-sm-2">
													  				  <button  class="btn btn-primary  btn-sm"  >  生成聚合报告 </button>
													  </div>  
											</div> <!-- row end-->   
											</br>
											<div id="pm_notice" class="row"   style="padding-left: 15px; padding-right: 15px;" > </div></br>
     						</form>	    
		    
							<!-- 删除聚合报告表单form-->
							<form  id="destroy_pm_logsrc_single_form" action="/logsrc/pm_analyse/destroy"  method="post"   class="form-horizontal" role="form"   accept-charset="UTF-8"   data-remote="true">     	  
								<!-- 模态框（Modal）for: 删除日志源 二次确认对话框 -->
								<div class="modal fade" id="destroy_pm_table_single_modal" tabindex="-1" role="dialog"    aria-labelledby="myModalLabel" aria-hidden="true">
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
													            	您确定要删除选中历史报告吗？
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
    
            <table id="pmtable"
               data-toggle="toolbar"
               data-height="500"
               data-side-pagination="server"
               data-pagination="true"
               data-search="false">
            <thead>
            <tr>
                <th data-field="report_id"  data-sortable="true"  data-visible="false">ID</th>
                <th data-field="logsrc_name"   data-sortable="true"    data-formatter="pmlogsrcnameFormatter"  >日志源名称</th>
                <th data-field="start_time" data-sortable="true"   >开始时间</th>
                 <th data-field="end_time" data-sortable="true"  >结束时间 </th>
                 <th data-field="create_time"    data-sortable="true" >创建时间</th>
                 <th data-field="title" data-sortable="true" >备注名</th>
                  <th data-field="operate"    data-formatter="pmoperateFormatter" >操作</th>
            </tr>
            </thead>
        </table>
        
        
<#elseif  !RequestParameters.proj?exists >
	   <div class="container  alert alert-warning"> 请先选择右上角项目</div>
<#elseif !logs?has_content>
	<div class="container  alert alert-warning">该项目没有日志源</div>
<#else>
	<div class="container  alert alert-warning">其他位置错误，请联系管理员</div>
</#if>
    

</@layout.myLayout>