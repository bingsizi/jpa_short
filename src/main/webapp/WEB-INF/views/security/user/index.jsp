<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/common/base.jsp"%>
<%@ include file="/common/jqueryEasyUi.jsp"%>
<title>用户管理</title>
<script type="text/javascript">
//查询
function searchForm(){
	var param=$('#searchForm').serialize();
	var row = $('#orgTree').tree('getSelected');
	if(row)
		param+="&orgId="+row.id;
	$('#userGrid').datagrid({
		url : "${ctx}/security/user/list?"+param
	});
};
$(function(){
	  $('#orgTree').tree({  
	       url : '${ctx}/security/user/orgTree',
		singleSelect : true,
		animate:true,
		lines:true,
		onClick:function(node){
			//清空from表单
			$("#realNameSearch").val("");
			$("#usernameSearch").val("");
			$('#userGrid').datagrid({
				url : "${ctx}/security/user/list?orgId="+node.id
			});
		}
	});
	$("#userGrid").datagrid({
		url:'${ctx}/security/user/list',
		fitColumns:true,
		fit:true,
		border:false,
		nowrap:true,
		rownumbers:true,
		singleSelect:true,
		showFooter:true,
		pagination:true,
		border:false,
		pageSize:20,
		columns:[[
	        {field:"username", title:"用户名", width:10},
			{field:"realName", title:"真实姓名", width:10},
			{field:"locked", title:"锁定状态", width:10},
			{field:"orgName", title:"组织机构", width:10},
			{field:"createTime", title:"创建日期", width:10}
		]],
		toolbar : '#tb',
		footer:'#ft'
	});
});
</script>
</head>
<body class="easyui-layout">
	<!-- 西 -->
	<div id="west" data-options="region:'west',split:true,border:false" style="width:180px;padding: 10px;">
       <ul id="orgTree"></ul>  
	</div>
	<!-- 中 -->
	<div data-options="region:'center',border:false">
	    <table id="userGrid"></table>
	</div>
	<!-- toolbar -->
	<div id="tb" style="padding:5px 5px;">
	    <form id="searchForm">
			<span style="margin: 0px 2px">用户名：</span><input id="usernameSearch" type="text" name="username" style="width: 100px;"/>
			<span style="margin: 0px 2px">真实姓名：</span><input id="realNameSearch" type="text" name="realName" style="width: 100px;"/>
		    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchForm();">搜索</a>
		</form>
	</div>
	<!-- footer -->
	<div id="ft" style="padding:2px 5px;">
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="add();">新增</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit();">修改</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">删除</a>
	</div>
</body>
</html>