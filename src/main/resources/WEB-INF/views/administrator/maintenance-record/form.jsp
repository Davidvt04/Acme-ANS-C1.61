<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	
	<acme:input-textbox code="administrator.maintenance-record.form.label.ticker" path="ticker" readonly="true"/>
	<acme:input-moment code="administrator.maintenance-record.form.label.moment" path="moment" readonly="true"/>
	<acme:input-textbox code="administrator.maintenance-record.form.label.status" path="status" readonly="true"/>
	<acme:input-moment code="administrator.maintenance-record.form.label.next-inspection" path="nextInspectionDueTime" readonly="true"/>
	<acme:input-textbox code="administrator.maintenance-record.form.label.aircraft" path="aircraft" readonly="true"/>
	<acme:input-money code="administrator.maintenance-record.form.label.estimated-cost" path="estimatedCost" readonly="true"/>
	<acme:input-textarea code="administrator.maintenance-record.form.label.notes" path="notes" readonly="true"/>	
	
	<acme:button code="administrator.maintenance-record.form.button.tasks" action="/administrator/task/list?masterId=${id}"/>
</acme:form>