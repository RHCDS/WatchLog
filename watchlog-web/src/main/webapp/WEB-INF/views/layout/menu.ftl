<nav class="navbar navbar-default">
  <div class="container">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
     </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
      
      <!-- 获取project.id -->
      <#if RequestParameters.proj?exists >
      	<#assign proj=RequestParameters.proj>
	      	<li <#if controller == "WlogManage">class="active"</#if>  ><a href="/logsrc/manage?proj=${proj}">日志源管理11</a></li>
	        <li <#if controller == "WlogRT">class="active"</#if>  ><a href="/logsrc/rt_analyse?proj=${proj}">实时分析</a></li>
	  		<li <#if controller == "WlogPM">class="active"</#if>  ><a href="/logsrc/pm_analyse?proj=${proj}">日志聚合分析</a></li>        
	  		<li><a href="#">报警管理</a></li>        
      	<#else>
	      	<li <#if controller == "WlogManage">class="active"</#if>  ><a href="/logsrc/manage">日志源管理11</a></li>
	        <li <#if controller == "WlogRT">class="active"</#if>  ><a href="/logsrc/rt_analyse">实时分析</a></li>
	  		<li <#if controller == "WlogPM">class="active"</#if>  ><a href="/logsrc/pm_analyse">日志聚合分析</a></li>        
	  		<li><a href="#">报警管理</a></li>              	
      	</#if>
      	

      </ul>

      
      <div class="pull-right  col-md-2">
          		<select   id="project_select"  style="width:100%" onchange="window.location.href=this.options[selectedIndex].value" >
          			
    		</select>
    	</div>
    			
      </div><!-- /.navbar-collapse -->
      
      <!-- p>
          controller:  ${controller}
          action: ${action}
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}
		</#list>    
	</p-->
      
  </div><!-- /.container-fluid -->
</nav>

