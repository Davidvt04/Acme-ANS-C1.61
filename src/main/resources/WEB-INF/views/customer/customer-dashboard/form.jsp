<%--
- Mostrar dashboard
--%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
   
    <acme:input-textbox code="customer.dashboard.list.label.lastFiveDestinations" path="lastFiveDestinations"/>
    <acme:input-money code="customer.dashboard.list.label.spentMoney" path="spentMoney"/>
	<acme:input-integer code="customer.dashboard.list.label.economyBookings" path="economyBookings" />
	<acme:input-integer code="customer.dashboard.list.label.businessBookings" path="businessBookings" />
	<acme:input-money code="customer.dashboard.list.label.bookingTotalCost" path="bookingTotalCost" />	
	<acme:input-money code="customer.dashboard.list.label.bookingAverageCost" path="bookingAverageCost" />	
	<acme:input-money code="customer.dashboard.list.label.bookingMinimumCost" path="bookingMinimumCost" />	
	<acme:input-money code="customer.dashboard.list.label.bookingMaximumCost" path="bookingMaximumCost" />	
	<acme:input-money code="customer.dashboard.list.label.bookingDeviationCost" path="bookingDeviationCost" />	
	<acme:input-integer code="customer.dashboard.list.label.bookingTotalPassengers" path="bookingTotalPassengers" />	
	<acme:input-double code="customer.dashboard.list.label.bookingAveragePassengers" path="bookingAveragePassengers" />	
	<acme:input-integer code="customer.dashboard.list.label.bookingMinimumPassengers" path="bookingMinimumPassengers" />	
	<acme:input-integer code="customer.dashboard.list.label.bookingMaximumPassengers" path="bookingMaximumPassengers" />	
	<acme:input-double code="customer.dashboard.list.label.bookingDeviationPassengers" path="bookingDeviationPassengers" />	
		
	
</acme:form>