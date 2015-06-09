
function getParam( name )
  {
	   name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	   var regexS = "[\\?&]"+name+"=([^&#]*)";
	   var regex = new RegExp( regexS );
	   var results = regex.exec( window.location.href );
	   if( results == null )
	    return "";
	  else
	   return results[1];
  }
  
var pid = getParam( 'proj' );
		  	  
		$(document).ready(function() {
			getProjects();
//		  	$('#project_select').select2();
		});

		function getProjects(){
	      	$.ajax({
	      		type: 'GET',
	    		url: '/projects',
	    		success :function(e){
//	    			console.log(e);
//    				console.log(typeof(e));
//	    			var r = JSON.parse(e)
//	    			var data =r['data'];
    				var data = e['data'];
	    			var i=0, html = "";
	    			
	    			if(typeof(pid)=='string' && pid.length==0){
	    				html  =  '<option  selected="true" value="#"> 请选择项目</option>';
	    			}
	    			
	    			for(i=0; i<data.length; i++){
	    				var id = data[i]['id'],  name=data[i]['name'];
	    				if(data[i]['id'] == pid){
	    					html  +=  '<option  selected="true" value="/logsrc/manage?proj=' +id  + '"> ' + name + '</option>';
	    				}
	    				else{
	    					html  +=  '<option value="/logsrc/manage?proj=' +id  + '"> ' + name + '</option>';
	    				}
	    				
	    			}
	    			
	    			$("#project_select").empty().append(html);
	    			$('#project_select').select2();
	    		}
	    	})
		}