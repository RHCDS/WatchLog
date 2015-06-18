<#import "../layout/tableLayout.ftl" as layout>
<@layout.tbLayout>

	<div class="container">
		
					<div class="row"    style="height:25px;   border-bottom: solid 1px #ddd; margin-bottom:30px;margin-top:30px;">
							<div class="col-sm-12"> <p style="font-size: 15px;font-weight: bold;">异常分布情况</p>	</div><!-- /col-sm-12 -->		
					</div>		
					
		           <table id="pm_error_dist_table"    data-toggle="toolbar"        data-side-pagination="server"            data-pagination="true"      data-search="false">
				            <thead>
						            <tr>
						                <th data-field="date_time"   data-sortable="true"    > 采样时间</th>
						                <th data-field="error_tc"  data-formatter="pmdisterrorFormatter"   > Error type & count</th>
						                 <th data-field="total_count" data-sortable="true"  > Total count </th>
						            </tr>
				            </thead>
		        </table>
		        		
	</div>


</@layout.tbLayout>
