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
			<div class="col-md-6"> 
					  <h4><input type="text" class="form-control"  style="border-radius: 0px; height:30px;" ></h4>
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
			<div class="col-md-6"> 
				 <h4><input type="text" class="form-control"  style="border-radius: 0px; height:30px;" ></h4>
			</div>			
	</div>
	
	<div class="row" >
			<div class="col-md-2">
				<h4> <small  style="padding-left: 60px;">日志文件路径：</small></h4>
			</div>
			<div class="col-md-6"> 
				 <h4><input type="text" class="form-control"  style="border-radius: 0px; height:30px;" ></h4>
			</div>			
	</div>
	
	
	<div class="row" >
			<div class="col-md-2">
				<h4> <small  style="padding-left: 60px;">日志文件名：</small></h4>
			</div>
			<div class="col-md-6"> 
				 <h4><input type="text" class="form-control"  style="border-radius: 0px; height:30px;" ></h4>
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
			<div class="col-md-6"> 
				 <h4><input type="text" class="form-control"  style="border-radius: 0px; height:30px;" ></h4>
			</div>			
	</div>
		

			

	
	<div class="row" >
			<div class="col-md-2">
					<h4> <small  style="padding-left: 60px;">Step1：</small></h4>
			</div><!-- /col-md-2 -->
			<div class="col-md-7"> 
					<div class="row" >
							<h4 style="margin-left: 15px;"> <small>过滤关键字	<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4>
					</div><!-- /row -->
					<div  class="row">
									 <div class="col-md-10" style="margin-right: -46px;">
									 		<input type="text" class="form-control"  style="border-radius: 0px; height:30px;" placeholder="NullPointException">	
									 </div><!-- /.col-md-4 -->
									 <div class="col-md-2">
									      <select id="sel_sp1" class=" form-control" style="border-radius: 0px; height:30px;">
										      		<option>And</option>
										      		<option>Or</option>
										      </select>		
							     	 </div><!-- /.col-md-2 -->
									 <div class="col-md-10" style="margin-right: -46px;">
									 		<input type="text" class="form-control"  style="border-radius: 0px; height:30px;" placeholder="NullPointException">	
									 </div><!-- /.col-md-4 -->
									 <div class="col-md-2">
									      <select id="sel_sp1" class=" form-control" style="border-radius: 0px; height:30px;">
										      		<option>And</option>
										      		<option>Or</option>
										      </select>		
							     	 </div><!-- /.col-md-2 -->							     	 
					</div><!-- /row -->
			</div><!-- /col-md-10 -->			
	</div><!-- /row -->
	
	
		

		<div class="row" >
			<div class="col-md-2">
					<h4> <small  style="padding-left: 60px;">Step1：</small></h4>
			</div><!-- /col-md-2 -->
			<div class="col-md-6"> 
					<div class="row" >
							<h4  style="margin-left: 15px;"> <small>正则表达式	<span class="glyphicon glyphicon-question-sign"  aria-hidden="true"></small></h4>
					</div><!-- /row -->
					<div  class="row">
							<table  class="table table-bordered">
								<tbody>
								<!--  -->
								</tbody>
							</table>
					</div><!-- /row -->
			</div><!-- /col-md-10 -->			
	</div><!-- /row -->
	
		<div class="row"   style="margin-left:380px;">
	  		  <button id="remove" class="btn btn-primary"  > 保存</button>
	  		 <button id="remove" class="btn btn-default"  >取消</button>	
		</div><!-- /row -->
 

	
</div>


</@layout.myLayout>