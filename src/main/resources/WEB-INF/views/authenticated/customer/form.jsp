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
	<acme:input-textbox code="authenticated.customer.form.label.identifier" path="identifier"/>
	<acme:input-textbox code="authenticated.customer.form.label.phoneNumber" path="phoneNumber"/>
	<acme:input-textarea code="authenticated.customer.form.label.address" path="address"/>
	<acme:input-textbox code="authenticated.customer.form.label.city" path="city"/>
	<acme:input-textbox code="authenticated.customer.form.label.country" path="country"/>
	<acme:input-integer code="authenticated.customer.form.label.earnedPoints" path="earnedPoints"/>
	
	<jstl:if test="${_command == 'create'}">
	<acme:submit code="authenticated.customer.form.button.create" action="/authenticated/customer/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'create'}">
	<acme:submit code="authenticated.customer.form.button.update" action="/authenticated/customer/update"/>
	</jstl:if>

</acme:form>
