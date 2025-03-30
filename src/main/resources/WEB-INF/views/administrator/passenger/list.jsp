
<%--
- list.jsp
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

<acme:list navigable="false">
    <acme:list-column code="customer.passenger.list.label.fullName" path="fullName" />
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" />
	<acme:list-column code="customer.passenger.list.label.dateOfBirth" path="dateOfBirth" />
	<acme:list-column code="customer.passenger.list.label.draftmode" path="draftMode" />

</acme:list>