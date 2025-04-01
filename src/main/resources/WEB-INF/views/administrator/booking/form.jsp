<%--
- Mostrar o editar una reserva
--%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-select code="administrator.booking.list.label.flight" path="flight" choices="${flights}"/>
    <acme:input-textbox code="administrator.booking.list.label.locatorCode" path="locatorCode"/>
    <acme:input-textbox code="administrator.booking.list.label.lastNibble" path="lastNibble"/>
    <acme:input-select code="administrator.booking.list.label.travelClass" path="travelClass" choices="${travelClasses}"/>
    <acme:input-double code="administrator.booking.list.label.price" path="price"/>

	<acme:button code="administrator.booking.form.show.passengers" action="/administrator/passenger/list?bookingId=${id}"/>
	
</acme:form>