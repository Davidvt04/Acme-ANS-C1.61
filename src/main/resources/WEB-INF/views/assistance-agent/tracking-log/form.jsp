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

<acme:form readonly="${!draftMode}"> 
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>	
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.step" path="step"/>	
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.resolutionPercentage" path="resolutionPercentage"/>	
	<acme:input-select code="assistanceAgent.trackingLog.form.label.status" path="status" choices="${statusChoices}"/>
	<acme:input-textbox code="assistanceAgent.trackingLog.form.label.resolution" path="resolution"/>
	
	<jstl:if test="${_command != 'create'}">
    <acme:input-integer code="assistanceAgent.trackingLog.form.label.claim" path="id" readonly="true"/>	
	</jstl:if>
	<jstl:if test="${_command == 'create'}">
	</jstl:if>

	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
		<jstl:if test="${draftMode}">
			<acme:submit code="assistanceAgent.trackingLog.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="assistanceAgent.claim.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
			<acme:submit code="assistanceAgent.claim.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
			
		</jstl:if>

		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceAgent.trackingLog.form.button.create" action="/assistance-agent/tracking-log/create?claimId=${claimId}"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>