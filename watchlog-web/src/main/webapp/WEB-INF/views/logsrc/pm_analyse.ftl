<#import "../layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
   <div>
          controller:  ${controller}<br/>
          action: ${action}<br/>
		
		<#list RequestParameters?keys as key>
		    ${key} = ${RequestParameters[key]}<br/>
		</#list>    
</div>
</@layout.myLayout>