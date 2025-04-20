<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	
	<acme:input-textbox code="administrator.task.form.label.ticker" path="ticker"/>
	<acme:input-textbox path="type" code="administrator.task.form.label.type"/>
	<acme:input-textarea code="administrator.task.form.label.description" path="description"/>
	<acme:input-integer code="administrator.task.form.label.priority" path="priority"/>
	<acme:input-integer code="administrator.task.form.label.estimated-duration" path="estimatedDuration"/>

</acme:form>