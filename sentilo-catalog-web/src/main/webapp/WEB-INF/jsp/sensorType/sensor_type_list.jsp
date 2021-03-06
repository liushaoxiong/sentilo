<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/header.jsp"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<spring:url value="/admin/sensortypes/new" var="newSensorTypeURL" />
<spring:url value="/admin/sensortypes/delete" var="deleteURL" />

<spring:url value="/admin/sensortypes/list/json" var="sAjaxSource"/>
<%@include file="/WEB-INF/jsp/common/include_script_tables.jsp" %>

<spring:url value="/admin/sensortypes/" var="detailPrefix"/>

<script type="text/javascript">
$(document).ready(function() {	
	makeTableAsync('${sAjaxSource}', '#sensorTypeTable', '${detailPrefix}');
});
</script>

<div class="container-fluid">
<div class="content">
<div class="row-fluid">
<div class="span3">
	<%@include file="/WEB-INF/jsp/common/include_sidebar.jsp" %>
</div>
<div class="span9">

<div class="row-fluid">
<div class="span12">

	<%@include file="/WEB-INF/jsp/common/include_background_logo.jsp" %>
	<%@include file="/WEB-INF/jsp/common/messages.jsp" %>

	<h1 class="lead">
		<spring:message code="sensortype.list.title"/><br/>
	</h1>
	
	<form:form method="post" onkeypress="return preventEnterSubmit(event);" modelAttribute="sensorTypes" action="${deleteURL}">
	
	<table class="table table-striped" id="sensorTypeTable">
		<thead>
			<tr>
				<td>&nbsp;</th>
				<td><strong><spring:message code="sensortype.id"/></strong></td>
				<td><strong><spring:message code="sensortype.name"/></strong></td>
				<td><strong><spring:message code="sensortype.description"/></strong></td>
				<td><strong><spring:message code="sensortype.createdAt"/></strong></td>
			</tr>
		</thead>
		<tbody/>
	</table>
	<br/>
	<div class="control-group pull-right">
		<a href="#" onclick="deleteSelected('sensorTypes');" class="btn btn-danger">
			<spring:message code="sensortype.delete.title"/>
		</a>
		<a href="#" type="button" onclick="window.location.href='${newSensorTypeURL}';" class="btn">
			<spring:message code="sensortype.new.title" />
		</a>
	</div>
	</form:form>
</div>
</div>
</div>
</div>
</div>
</div>
<%@include file="/WEB-INF/jsp/common/footer.jsp"%>