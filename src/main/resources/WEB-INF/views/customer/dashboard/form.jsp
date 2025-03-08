<%--  
- form.jsp  
-  
- Copyright (C) 2012-2025 Rafael Corchuelo.  
-  
- In keeping with the traditional purpose of furthering education and research, it is  
- the policy of the copyright owner to permit non-commercial use and redistribution of  
- this software. It has been tested carefully, but it is not guaranteed for any particular  
- purposes. The copyright owner does not offer any warranties or representations, nor do  
- they accept any liabilities with respect to them.  
--%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<h2>  
    <acme:print code="customer.dashboard.form.title"/>  
</h2>  

<table class="table table-sm">  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.last-five-destinations"/>  
        </th>  
        <td>  
            <acme:print value="lastFiveDestinations"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.spent-money"/>  
        </th>  
        <td>  
            <acme:print value="spentMoney"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.economy-bookings"/>  
        </th>  
        <td>  
            <acme:print value="economyBookings"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.business-bookings"/>  
        </th>  
        <td>  
            <acme:print value="businessBookings"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-total-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingTotalCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-average-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingAverageCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-minimum-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingMinimumCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-maximum-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingMaximumCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-deviation-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingDeviationCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-total-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingTotalPassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-average-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingAveragePassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-minimum-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingMinimumPassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-maximum-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingMaximumPassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.dashboard.form.label.booking-deviation-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingDeviationPassengers"/>  
        </td>  
    </tr>  
</table>  

<div>  
    <canvas id="canvas"></canvas>  
</div>  

<acme:return/>  
