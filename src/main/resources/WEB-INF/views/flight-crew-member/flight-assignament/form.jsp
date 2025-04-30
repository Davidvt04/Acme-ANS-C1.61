<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="flight-crew-member.flight-assignament.form.label.flight-crew-member" path="flightCrewMember" choices="${flightCrewMembers}"/>
	<acme:input-select code="flight-crew-member.flight-assignament.form.label.leg" path="leg" choices="${legs}"/>		
	<acme:input-select code="flight-crew-member.flight-assignament.form.label.duty" path="duty" choices="${duty}"/>
	<acme:input-select code="flight-crew-member.flight-assignament.form.label.current-status" path="currentStatus" choices="${currentStatus}"/>
	<acme:input-textbox code="flight-crew-member.flight-assignament.form.label.remarks" path="remarks"/>
	<acme:input-moment code="flight-crew-member.flight-assignament.form.label.moment" path="moment" readonly="true"/>
	

	<jstl:choose>	 
			
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true && isCompleted == false}">  <%-- Si está completed la leg ya no se puede publicar--%>
			<acme:submit code="flight-crew-member.flight-assignament.form.button.publish" action="/flight-crew-member/flight-assignament/publish"/>
			<acme:submit code="flight-crew-member.flight-assignament.form.button.update" action="/flight-crew-member/flight-assignament/update"/>
			<acme:submit code="flight-crew-member.flight-assignament.form.button.delete" action="/flight-crew-member/flight-assignament/delete"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')  && isCompleted==true && draftMode ==true}">
			<acme:button code="flight-crew-member.flight-assignament.form.button.activity-log" action="/flight-crew-member/activity-log/list?masterId=${id}"/>		
			<acme:submit code="flight-crew-member.flight-assignament.form.button.update" action="/flight-crew-member/flight-assignament/update"/>
			<acme:submit code="flight-crew-member.flight-assignament.form.button.delete" action="/flight-crew-member/flight-assignament/delete"/>	
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')  && isCompleted==true && draftMode ==false}">
			<acme:button code="flight-crew-member.flight-assignament.form.button.activity-log" action="/flight-crew-member/activity-log/list?masterId=${id}"/>		
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true && isCompleted==false}">
	
			<acme:submit code="flight-crew-member.flight-assignament.form.button.update" action="/flight-crew-member/flight-assignament/update"/>
			<acme:submit code="flight-crew-member.flight-assignament.form.button.delete" action="/flight-crew-member/flight-assignament/delete"/>
		</jstl:when>

		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignament.form.button.create" action="/flight-crew-member/flight-assignament/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>