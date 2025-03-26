<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Update Airline</title>
</head>
<body>
    <h1>Update Airline</h1>
    <acme:form>
        <acme:input-textbox path="name" code="label.airline.name" />
        <acme:input-textbox path="iataCode" code="label.airline.iataCode" />
        <acme:input-url path="website" code="label.airline.website" />
        <acme:input-select path="type" code="label.airline.type" choices="${choices}" />
        <acme:input-moment path="foundationMoment" code="label.airline.foundationMoment" />
        <acme:input-textbox path="email" code="label.airline.email" />
        <acme:input-textbox path="phoneNumber" code="label.airline.phoneNumber" />
        <acme:submit code="button.submit.update.airline" action="/administrator/airline/update" />
    </acme:form>
    <acme:button code="button.back" action="/administrator/airline/list" />
</body>
</html>
