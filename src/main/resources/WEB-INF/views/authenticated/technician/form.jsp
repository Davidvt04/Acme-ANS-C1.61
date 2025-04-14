
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.technician.form.label.license-number" path="licenseNumber"/>
	<acme:input-textbox code="authenticated.technician.form.label.phone-number" path="phoneNumber"/>
	<acme:input-textbox code="authenticated.technician.form.label.specialisation" path="specialisation"/>
	<acme:input-checkbox code="authenticated.technician.form.label.passed-annual-health-test" path="passedAnnualHealthTest"/>
	<acme:input-integer code="authenticated.technician.form.label.experience-years" path="experienceYears"/>
	<acme:input-textarea code="authenticated.technician.form.label.certifications" path="certifications"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.technician.form.button.create" action="/authenticated/technician/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.technician.form.button.update" action="/authenticated/technician/update"/>
	</jstl:if>

</acme:form>