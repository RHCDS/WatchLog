<#import "../layout/tableLayout.ftl" as layout>
<@layout.tbLayout>

	<div class="container">
		
					<div class="row"    style="height:25px;   border-bottom: solid 1px #ddd; margin-bottom:30px;margin-top:30px;">
							<div class="col-sm-12"> <p style="font-size: 15px;font-weight: bold;">异常类型详情</p>	</div><!-- /col-sm-12 -->		
					</div>		
					
		           <table id="pm_error_type_table"    data-toggle="toolbar"     data-height="500"     data-side-pagination="server"            data-pagination="true"      data-search="false">
		            <thead>
		            <tr>
		                <th data-field="error_type"   data-sortable="true"    > 采样时间</th>
		                <th data-field="error_example" data-sortable="true"    > Error type & count</th>
		                 <th data-field="total_count" data-sortable="true"  > Total count </th>
		            </tr>
		            </thead>
		        </table>
	</div>


</@layout.tbLayout>
