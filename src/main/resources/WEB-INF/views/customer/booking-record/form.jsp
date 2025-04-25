<%--
- Vincular un passenger a una booking
--%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-select code="customer.bookingRecord.list.label.passenger" path="passenger" choices="${passengers}"/>
    <acme:input-textbox code="customer.bookingRecord.list.label.booking" path="locatorCode" readonly="true"/>

	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.bookingRecord.form.button.create" action="/customer/booking-record/create?bookingId=${booking.id}"/>
		</jstl:when>		
		<jstl:when test="${_command == 'delete'}">
			<acme:submit code="customer.bookingRecord.form.button.delete" action="/customer/booking-record/delete?bookingId=${booking.id}"/>	
		</jstl:when>
	</jstl:choose>	
	
	
</acme:form>