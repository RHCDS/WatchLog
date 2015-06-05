<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
  <!--div>
          controller:  ${controller}<br/>
          action: ${action}<br/>
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}<br/>
		</#list>    
</div>

<button id="test" class="btn btn-primary"  onclick="test()">test</button>
<div id="result">empty</div>
</br-->

<!-- select   id="result"  style="width:100%" onchange="window.location.href=this.options[selectedIndex].value" ></select-->
   

  <#if RequestParameters.proj?exists >
  	<#assign pid = RequestParameters.proj>
    <div class="container">

    <div id="toolbar">
	    <a class="btn btn-primary" href="/logsrc/new?proj=${pid}" role="button">创建日志源</a>
        <button id="remove" class="btn btn-primary"  onclick="destroyLogsrc()" > 删除日志源 </button>
    </div>
    </br>
 
    <#if message?exists>
        ${message}
    </#if>
 
    
        <table id="logtable"
               data-toggle="toolbar"
               data-height="500"
               data-side-pagination="server"
               data-pagination="true"
               data-search="false">
               
            <thead>
            <tr>
                <th data-field="state" data-checkbox="true" ></th>
                <th data-field="id"  data-sortable="true"  data-visible="false">ID</th>
                <th data-field="logsrc_name"   data-sortable="true"   data-formatter="logsrcnameFormatter"  >日志源名称</th>
                <th data-field="host_name" data-sortable="true" >服务器地址</th>
                 <th data-field="logsrc_path" data-sortable="true" >日志源地址 </th>
                 <th data-field="logsrc_file" data-sortable="true" >日志文件名称</th>
                 <th data-field="status"   data-formatter="statusFormatter"  data-sortable="true" >监控状态</th>
                 <th data-field="update_time" data-sortable="true" >更新时间</th>
                 <th data-field="creator" data-sortable="true" >创建人</th>
                  <th data-field="operate"    data-formatter="operateFormatter" ">操作</th>
                
            </tr>
            </thead>
        </table>
    </div>
    
<#else>
   <div class="container"> 请选择项目，亲:)</div>
</#if>
    

    
</@layout.myLayout>