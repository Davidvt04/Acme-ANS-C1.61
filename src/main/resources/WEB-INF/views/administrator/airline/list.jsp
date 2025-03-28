<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Airline List</title>
</head>
<body>
    <h1>Airline List</h1>
    <acme:list navigable="true" show="show">
       <acme:list-column path="name" code="administrator.airline.list.label.name" />
	   <acme:list-column path="iataCode" code="administrator.airline.list.label.iataCode" />
	   <acme:list-column path="website" code="administrator.airline.list.label.website" />
	   <acme:list-column path="type" code="administrator.airline.list.label.type" />
	   <acme:list-column path="foundationMoment" code="administrator.airline.list.label.foundationMoment"/>
    </acme:list>
    <acme:button code="button.create.airline" action="/administrator/airline/create" />
    
</body>
</html>
