<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

	<acme:input-textbox code="authenticated.assistanceAgent.form.label.employeeCode" path="employeeCode"/>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.spokenLanguages" path="spokenLanguages"/>
	<acme:input-moment code="authenticated.assistanceAgent.form.label.moment" path="moment"/>
	<acme:input-textarea code="authenticated.assistanceAgent.form.label.briefBio" path="briefBio"/>
	<acme:input-money code="authenticated.assistanceAgent.form.label.salary" path="salary"/>
	<acme:input-url code="authenticated.assistanceAgent.form.label.photo" path="photo"/>
	<acme:input-select code="authenticated.assistanceAgent.form.label.airline" path="airline" choices="${airlineChoices}"/>
	
	<jstl:if test="${_command == 'create'}">
	<acme:submit code="authenticated.assistanceAgent.form.button.create" action="/authenticated/assistance-agent/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'create'}">
	<acme:submit code="authenticated.assistanceAgent.form.button.update" action="/authenticated/assistance-agent/update"/>
	</jstl:if>

</acme:form>
