<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>


  <#if RequestParameters.proj?exists >
  	<#assign pid = RequestParameters.proj>
  	
		<div class="container">
		

		  				<!-- 日志详情页面 导航条-->
						<div class="row"   style="border-bottom: solid 1px #ddd;height: 45px;" >
								<div class="col-sm-12"> 
												<table  class="table wratb  removebd">
													<tbody>
																<tr>
																	<td class="col-sm-9"> <a href="/logsrc/manage?proj=${pid}"> 返回 &#62; </a>	&#160;&#160;<small>${logsrc_name}</small></td>
																	<td class="col-sm-1"> <a class="btn btn-primary btn-sm"  style="margin-right: 15px;" href="/logsrc/${id}/edit?proj=${pid}" role="button">修改日志源</a></td>
																</tr>		
													</tbody>
												</table>
								</div><!-- /col-sm-12 -->			
						</div><!-- /row -->			
						  										
					<!-- 验证/清空行 -->
						<div class="row"  >
												<table  class="table wratb  removebd" style="margin-bottom: 0px;margin-left: 10px;">
													<tbody>
																<tr>
																	<td class="col-sm-10" style="vertical-align: bottom;">  <label for="debug_log_content">请输入日志信息</label></td>
																	<td class="col-sm-1"> <a class="btn btn-default  btn-sm" onclick="clear_debug_validate()"  role="button">&#160; 清空 &#160;</a></td>
																	<td class="col-sm-1"> <button class="btn btn-warning  btn-sm" onclick="start_debug_validate(${pid}, ${id})">&#160; 验证 &#160;</button></td>
																</tr>		
													</tbody>
												</table>
						</div><!-- /row -->
							
								<!-- loading -->
								<div class="modal fade" id="debug_loader_div"  tabindex="-1" role="dialog"    aria-labelledby="myModalLabel" aria-hidden="true">
									   <div class="modal-dialog" >
										 <div  class="col-sm-7 pull-right" >
                                                        <img src="/res/img/loading.gif" />
                                            </div>            
										</div><!-- /.modal-dialog -->
								</div><!-- 模态框（Modal） -->				
						
						<!-- 调试日志输入框 -->				
						<div class="row"  >				  
			   			 			<textarea class="form-control" id="debug_log_content"  rows="15"></textarea>
			   			 </div><!-- /row -->			
						   			 
						<!-- 表格: type+count -->	   			 
						<div  class='row' style="margin-top: 30px;margin-bottom: -15px;">
								<table  class='table table-bordered'>
									<tbody id='debug_tc_body'  class="ajax_content">													 
									</tbody>
								</table>
						</div><!-- /row -->		
						<div id="debug_tc_comment" class="row ajax_content" style="color: gray;"></div>		
						
						<!-- 表格: unknowlist-->	   			 
						<div  class='row' style="margin-top: 30px;">
								<table  class='table table-bordered'>
									<tbody id='debug_unknow_body' class="ajax_content">																							 
									</tbody>
								</table>
						</div><!-- /row -->				
						
						<!-- 调试失败信息-->
						<div id="debug_fail" class="row  alert alert-danger"   role="alert"   style="padding-left: 30px; padding-right: 30px; display:none">   </div>
						
</div><!--container-->
<#else>
     <div class="container  alert alert-warning"> 请先选择右上角项目</div>
</#if>

</@layout.myLayout>