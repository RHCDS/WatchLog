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
         <button id="remove" class="btn btn-primary"  onclick="startMonitorLogsrc()" > 开始监控 </button>
         <button id="remove" class="btn btn-primary"  onclick="stopMonitorLogsrc()" > 停止监控 </button>
    </div>
    </br>
 
 <div id="js_notice" class="row"   style="padding-left: 15px; padding-right: 15px;" > </div>


 
    
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
    
<!--  复制日志源 弹窗 用rubymine去掉注释 -->    
<!--<form  id="copy_logsrc_form" class="form-horizontal" role="form"   accept-charset="UTF-8" action="/logsrc/copy" data-remote="true" method="post">-->
    <!--&lt;!&ndash; 模态框（Modal） &ndash;&gt;-->
    <!--<div class="modal fade" id="copy_logsrc_modal" tabindex="-1" role="dialog"    aria-labelledby="myModalLabel" aria-hidden="true">-->
        <!--<div class="modal-dialog">-->
            <!--<div class="modal-content">-->
                <!--<input type="hidden" id="copy_logsrc_id" name="copy_logsrc_name"  />-->
                <!--<input type="hidden"  name="proj"  value= ${pid} />-->
                <!--<div class="modal-header">-->
                    <!--<button type="button" class="close" data-dismiss="modal"   aria-hidden="true"> &times;  </button>-->
                    <!--<h4 class="modal-title" id="myModalLabel">    this is 标题   </h4>-->
                <!--</div>&lt;!&ndash; /.modal-header &ndash;&gt;-->
                <!--<div class="modal-body">     this is  主体</div>-->
                <!--<div class="modal-footer">-->
                    <!--<button type="button" class="btn btn-default"  data-dismiss="modal">    关闭       </button>-->
                    <!--<button type="submit" class="btn btn-primary">      提交更改   </button>-->
                <!--</div>&lt;!&ndash; /.modal-footer &ndash;&gt;-->
            <!--</div>&lt;!&ndash; /.modal-content &ndash;&gt;-->
        <!--</div>&lt;!&ndash; /.modal-dialog &ndash;&gt;-->
    <!--</div>&lt;!&ndash; /.modal &ndash;&gt;-->
<!--</form>	 -->
    
<#else>
   <div class="container"> 请选择项目，亲:)</div>
</#if>
    

    
</@layout.myLayout>