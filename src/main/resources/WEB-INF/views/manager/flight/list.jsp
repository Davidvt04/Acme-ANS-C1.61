<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Flight List</title>
</head>
<body>
    <h1>Flight List</h1>
    <acme:list navigable="true" show="show">
        <acme:list-column path="tag" code="manager.flight.list.label.tag" />
        <acme:list-column path="requiresSelfTransfer" code="manager.flight.list.label.requiresSelfTransfer" />
        <acme:list-column path="cost" code="manager.flight.list.label.cost" />
        <acme:list-column path="description" code="manager.flight.list.label.description" />
        <acme:list-column path="scheduledDeparture" code="manager.flight.list.label.scheduledDeparture" />
        <acme:list-column path="scheduledArrival" code="manager.flight.list.label.scheduledArrival" />
        <acme:list-column path="numberOfLayovers" code="manager.flight.list.label.numberOfLayovers" />
    </acme:list>
    <acme:button code="manager.flight.list.button.create" action="/manager/flight/create" />
</body>
</html>
