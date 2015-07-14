

//  匹配规则select下拉切换响应
$(function() {
	// 过滤关键字 AND/OR切换
	$('#filter_keyword_select_id').change(function(){ 
			var p1=$(this).children('option:selected').val();  //selected值 : AND OR
			$('.filter_keyword_td_select').html(p1);  //修改table第二个元素
		});
	
	// 正则表达式  AND/OR切换
	$('#reg_regex_select_id').change(function(){ 
		var p1=$(this).children('option:selected').val();  //selected值 
		$('.reg_regex_td_select').html(p1);  //修改table第二个元素
	});
});


//删除1行： 过滤关键字 + 正则表达式
function filter_item_delete(e){
	console.log("take it ease");
	$(e).closest("tr").remove();
}


// 过滤关键字 添加1行
function add_row_filter_keyword(){
	var select_val = $("#filter_keyword_select_id option:selected").val();   //selected值 : AND OR
	var cell_1_html = '<td class="col-md-5" style="padding:0px"><input type="text" class="form-control log-item-regular-input"  id="filter_keyword_input_id "   name="filter_keyword_arr[]"     ></td>';
	var cell_2_html = '<td class="col-md-1 filter_keyword_td_select">'+select_val+'</td>';
	var cell_3_html = '<td class="col-md-1" ><a class="filter_delete" href="javascript:void(0)"  title="删除"  onclick=filter_item_delete(this)><i class="glyphicon glyphicon-trash  filter_delete_icon"></i></a>	</td>';
	var tr = '<tr>' + cell_1_html  + cell_2_html + cell_3_html  + '</tr>';	
	$('#filter_keyword_table > tbody:last-child').append(tr);
}


//正则表达式 添加1行
function add_row_reg_regex(){
	var select_val = $("#reg_regex_select_id option:selected").val();    //selected值 : AND OR
	var cell_1_html = '<td class="col-md-5" style="padding:0px"><input type="text" class="form-control log-item-regular-input"  id="reg_regex_input_id "   name="reg_regex_arr[]"  ></td>';
	var cell_2_html = '<td class="col-md-1 reg_regex_td_select">'+select_val+'</td>';
	var cell_3_html = '<td class="col-md-1" ><a class="filter_delete" href="javascript:void(0)"  title="删除"  onclick=filter_item_delete(this)><i class="glyphicon glyphicon-trash  filter_delete_icon"></i></a>	</td>';
	
	var tr = '<tr>' + cell_1_html  + cell_2_html  + cell_3_html  +'</tr>';	
	$('#reg_regex_table > tbody:last-child').append(tr);
}



//  创建日志源非空校验
function check_create_logsrc(){
	// 日志源名称验证
	$('#warn_new_logsrc_name').html("");
	var logsrc_name = $.trim($('#logsrc_name').val());
	if(logsrc_name == ""){
		$('#warn_new_logsrc_name').html("<font color='red'>日志名不能为空</font>");
		return false;
	}
	else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,100}$/.test(logsrc_name) == false){
		// 具体区分不同的规则
				if(/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/.test(logsrc_name) == false){
					$('#warn_new_logsrc_name').html("<font color='red'>日志源只能包含：中文/英文字母/数字/下划线</font>");
				}else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/.test(logsrc_name) == false){
					$('#warn_new_logsrc_name').html("<font color='red'>日志源名称不能以下划线开始和结束</font>");
				}else{
					$('#warn_new_logsrc_name').html("<font color='red'>日志名长度不能超过100</font>");
				}
				return false
	}
	else{
		$('#warn_new_logsrc_name').html("");
	}
	// 服务器地址验证
	var host_name = $.trim($('#host_name').val());
	if(host_name == ""){
		$('#warn_new_host_name').html("<font color='red'>服务器地址不能为空</font>");
		return false;
	}
	else{
		$('#warn_new_host_name').html("");
	}
	// 日志文件路径验证
	var logsrc_path = $.trim($('#logsrc_path').val());
	if(logsrc_path == ""){
		$('#warn_new_logsrc_path').html("<font color='red'>日志文件路径不能为空</font>");
		return false;
	}
	else{
		$('#warn_new_logsrc_path').html("");
	}
	// 日志文件名验证
	var logsrc_file = $.trim($('#logsrc_file').val());
	if(logsrc_file == ""){
		$('#warn_new_logsrc_file').html("<font color='red'>日志文件名不能为空</font>");
		return false;
	}
	else{
		$('#warn_new_logsrc_file').html("");
	}
	//  起始标志验证
	var start_regex = $.trim($('#start_regex').val());
	if(start_regex == ""){
		$('#warn_new_start_regex').html("<font color='red'>起始标志不能为空</font>");
		return false;
	}
	else{
		$('#warn_new_start_regex').html("");
	}	
}


//修改日志源非空校验
function check_update_logsrc(){
	// 日志源名称验证
	var logsrc_name = $.trim($('#logsrc_name').val());
	if(logsrc_name == ""){
		$('#warn_edit_logsrc_name').html("<font color='red'>日志名不能为空</font>");
		return false;
	}
	else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,100}$/.test(logsrc_name) == false){
			// 具体区分不同的规则
			if(/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/.test(logsrc_name) == false){
				$('#warn_edit_logsrc_name').html("<font color='red'>日志源只能包含：中文/英文字母/数字/下划线</font>");
			}else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/.test(logsrc_name) == false){
				$('#warn_edit_logsrc_name').html("<font color='red'>日志源名称不能以下划线开始和结束</font>");
			}else{
				$('#warn_edit_logsrc_name').html("<font color='red'>日志名长度不能超过100</font>");
			}
			return false
	}
	else{
		$('#warn_edit_logsrc_name').html("");
	}
	// 服务器地址验证
	var host_name = $.trim($('#host_name').val());
	if(host_name == ""){
		$('#warn_edit_host_name').html("<font color='red'>服务器地址不能为空</font>");
		return false;
	}
	else{
		$('#warn_edit_host_name').html("");
	}
	// 日志文件路径验证
	var logsrc_path = $.trim($('#logsrc_path').val());
	if(logsrc_path == ""){
		$('#warn_edit_logsrc_path').html("<font color='red'>日志文件路径不能为空</font>");
		return false;
	}
	else{
		$('#warn_edit_logsrc_path').html("");
	}
	// 日志文件名验证
	var logsrc_file = $.trim($('#logsrc_file').val());
	if(logsrc_file == ""){
		$('#warn_edit_logsrc_file').html("<font color='red'>日志文件名不能为空</font>");
		return false;
	}
	else{
		$('#warn_edit_logsrc_file').html("");
	}
	//  起始标志验证
	var start_regex = $.trim($('#start_regex').val());
	if(start_regex == ""){
		$('#warn_edit_start_regex').html("<font color='red'>起始标志不能为空</font>");
		return false;
	}
	else{
		$('#warn_edit_start_regex').html("");
	}	
}


// 复制日志源前端校验
function check_copy_logsrc(old_name, old_host_name, old_logsrc_path, old_logsrc_file ){
	var new_name =$.trim($('#logsrc_name').val()); 
	// 日志源名称不能重复校验 + 非空校验 + 规则校验
	if(old_name == new_name){
		$('#warn_copy_logsrc_name').html("<font color='red'>日志源名称不能重复</font>");
		return false;
	}	
	else if(new_name == ""){
		$('#warn_copy_logsrc_name').html("<font color='red'>日志名不能为空</font>");
		return false;		
	}
	else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,100}$/.test(new_name) == false){
			// 具体区分不同的规则
		if(/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/.test(new_name) == false){
			$('#warn_copy_logsrc_name').html("<font color='red'>日志源只能包含：中文/英文字母/数字/下划线</font>");
		}else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/.test(new_name) == false){
			$('#warn_copy_logsrc_name').html("<font color='red'>日志源名称不能以下划线开始和结束</font>");
		}else{
			$('#warn_copy_logsrc_name').html("<font color='red'>日志名长度不能超过100</font>");
		}
		return false
}	
	else{
		$('#warn_copy_logsrc_name').html("");
	}	
	
	// 日志源位置：至少需要修改hostname、path、filepattern三者之一，否则复制无法完成。
	var new_host_name = $.trim($('#host_name').val());
	var new_logsrc_path = $.trim($('#logsrc_path').val());
	var new_logsrc_file =  $.trim($('#logsrc_file').val());
	if(old_host_name==new_host_name && old_logsrc_path==new_logsrc_path &&  old_logsrc_file==new_logsrc_file){
		$('#warn_copy_logsrc_location').html("<font color='red'>至少修改服务器地址,日志文件路径,日志文件名三者之一</font>");
		return false;		
	}
	else{
		$('#warn_copy_host_name').html("");
	}		
	// 服务器地址非空验证
	if(new_host_name == ""){
		$('#warn_copy_host_name').html("<font color='red'>服务器地址不能为空</font>");
		return false;			
	}else{
		$('#warn_copy_host_name').html("");
	}
	// 日志文件路径非空验证
	if(new_logsrc_path == ""){
		$('#warn_copy_logsrc_path').html("<font color='red'>日志文件路径不能为空</font>");
		return false;			
	}else{
		$('#warn_copy_logsrc_path').html("");
	}	
	// 日志文件名非空验证	
	if(new_logsrc_path == ""){
		$('#warn_copy_logsrc_file').html("<font color='red'>日志文件路径不能为空</font>");
		return false;			
	}else{
		$('#warn_copy_logsrc_file').html("");
	}	
}


// 调试日志源验证功能
function start_debug_validate(proj, log_id){
	// loading 出现
	$(".ajax_content").html("");
	$("#debug_fail").css("display",  "none");
	$('#debug_loader_div').modal('show');
	
	// 校验用户输入 输入最多行
	
	
	// ajax 请求后端
  	$.ajax({
  		type: 'GET',
		url: '/logsrc/debugvalidate',
		data: {proj: proj,  id: log_id},
		dataType: "json",
		success :function(e){
					// loading消失
					$('#debug_loader_div').modal('hide');
					// 调试成功
					if(e['status'] == 0)
					{
							// 渲染debug的2个表格
							var error_tc_list = e['error_tc'];
							var unknow_list = e['unknow_list'];
							var i=0;
							// error_tc表格
							if(error_tc_list.length>0){
								// 表格第一行
								var debug_tc_body_html = "<tr><th class='col-sm-10'>异常类型</th><th class='col-sm-2'>异常总数</th></tr>";
								for(i=0; i<error_tc_list.length; i++){
									// 表格其他行
									if(error_tc_list[i]['type'] == "unknown"){
										debug_tc_body_html = debug_tc_body_html + "<tr><td class='col-sm-10' style='color:blue'>" +error_tc_list[i]['type'] + "</td>" + "<td class='col-sm-2'>"+error_tc_list[i]['count']+"</td></tr>";
									}
									else{
										debug_tc_body_html = debug_tc_body_html + "<tr><td class='col-sm-10'>" +error_tc_list[i]['type'] + "</td>" + "<td class='col-sm-2'>"+error_tc_list[i]['count']+"</td></tr>";
									}
								}				
								// 表格更新
								$("#debug_tc_body").html(debug_tc_body_html);		
								// 备注
								$("#debug_tc_comment").html("备注： 显示为unknown说明没有分析出异常类型，请修改正则表达式进一步区分.");				
							}
	
							// unknown表格
							if(unknow_list.length > 0){
								// 表格第一行
								var debug_unknow_body_html = "<tr><th class='col-sm-12' style='text-align: center;'>unknow日志信息</th></tr>";
								for(i=0; i<unknow_list.length; i++){
									// 表格其他行
									debug_unknow_body_html = debug_unknow_body_html + "<tr><td class='col-sm-12'>"+unknow_list[i]+"</td></tr>";
								}			
								// 表格更新
								$("#debug_unknow_body").html(debug_unknow_body_html);					
							}
					}
					// 调试失败
					else{
						$("#debug_fail").css("display",  "block");
						$("#debug_fail").html(e['message']);
					}
		},
		error: function(){
			//请求出错处理
		}
	});
}


	// 清空调试日志内容
	function clear_debug_validate(){
		$("#debug_log_content").val('');  //jquery
	}
