

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


// 过滤关键字 添加1行
function add_row_filter_keyword(){
	var select_val = $("#filter_keyword_select_id option:selected").val();   //selected值 : AND OR
	var cell_1_html ='<td class="col-md-6" style="padding:0px"><input type="text" class="form-control log-item-regular-input"  id="filter_keyword_input_id "   name="filter_keyword_arr[]"     ></td>';
	var cell_2_html = '<td class="col-md-1 filter_keyword_td_select">'+select_val+'</td>';
	var tr = '<tr>' + cell_1_html  + cell_2_html + '</tr>';	
	$('#filter_keyword_table > tbody:last-child').append(tr);
}


//正则表达式 添加1行
function add_row_reg_regex(){
	var select_val = $("#reg_regex_select_id option:selected").val();    //selected值 : AND OR
	var cell_1_html ='<td class="col-md-6" style="padding:0px"><input type="text" class="form-control log-item-regular-input"  id="reg_regex_input_id "   name="reg_regex_arr[]"  ></td>';
	var cell_2_html = '<td class="col-md-1 reg_regex_td_select">'+select_val+'</td>';
	var tr = '<tr>' + cell_1_html  + cell_2_html + '</tr>';	
	$('#reg_regex_table > tbody:last-child').append(tr);
}


//  创建日志源非空校验
function check_create_logsrc(){
	// 日志源名称验证
	var logsrc_name = $.trim($('#logsrc_name').val());
	if(logsrc_name == ""){
		$('#warn_new_logsrc_name').html("<font color='red'>日志名不能为空</font>");
		return false;
	}
	else if(/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,100}$/.test(logsrc_name) == false){
		$('#warn_new_logsrc_name').html("<font color='red'>日志名不符合规范</font>");
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
		$('#warn_edit_logsrc_name').html("<font color='red'>日志名不符合规范</font>");
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

