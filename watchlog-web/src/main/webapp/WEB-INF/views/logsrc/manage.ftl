<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
  <div>
          controller:  ${controller}<br/>
          action: ${action}<br/>
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}<br/>
		</#list>    
</div>

<button id="test" class="btn btn-primary"  onclick="test()">test</button>
<div id="result">empty</div>
</br>

<div class="content">
	<table id="logsrc_table"  width="100%">
	        <thead>
	            <tr>
	                <th>日志源名称</th>
	                <th>服务器地址</th>
	                <th>日志路径</th>
	                <th>日志文件名称</th>
	                <th>监控状态</th>
	                <th>更新时间</th>
	                 <th>创建人</th>
	            </tr>

	        </thead>
	 

	    </table>
</div>
</@layout.myLayout>