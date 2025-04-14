<%--
- Mostrar o editar una reserva
--%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${!draftMode}">
    <acme:input-select code="customer.booking.list.label.flight" path="flight" choices="${flights}"/>
    <acme:input-textbox code="customer.booking.list.label.locatorCode" path="locatorCode"/>
    <acme:input-textbox code="customer.booking.list.label.lastNibble" path="lastNibble"/>
    <acme:input-select code="customer.booking.list.label.travelClass" path="travelClass" choices="${travelClasses}"/>
    <acme:input-double code="customer.booking.list.label.price" path="price" readonly="true"/>

	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
		<jstl:if test="${draftMode}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
			<jstl:if test="${_command != 'create'}">
				<acme:button code="customer.booking.form.add.passenger" action="/customer/booking-record/create?bookingId=${id}"/>
			</jstl:if>
		</jstl:if>

		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="customer.booking.form.show.passengers" action="/customer/passenger/list?bookingId=${id}"/>
		<acme:button code="customer.booking.form.show.recommendations" action="/customer/recommendation-dashboard/list?city=${city}&country=${country}"/>
	</jstl:if>
	


	
	
</acme:form>