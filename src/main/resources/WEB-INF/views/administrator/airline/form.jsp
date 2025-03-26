<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<acme:form>
    <acme:input-textbox code="administrator.airline.list.label.name" path="name"/>
    <acme:input-textbox code="administrator.airline.list.label.iataCode" path="iataCode"/>
    <acme:input-url code="administrator.airline.list.label.website" path="website"/>
    <acme:input-select code="administrator.airline.list.label.type" path="type" choices="${airlineTypes}"/>
    <acme:input-moment code="administrator.airline.list.label.foundationMoment" path="foundationMoment"/>
    <acme:input-email code="administrator.airline.list.label.email" path="email"/>
    <acme:input-textbox code="administrator.airline.list.label.phoneNumber" path="phoneNumber"/>
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update')}">
            <acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
            <acme:submit code="administrator.airline.form.button.update" action="/administrator/airline/update"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
            <acme:submit code="administrator.airline.form.button.create" action="/administrator/airline/create"/>
        </jstl:when>
    </jstl:choose>
</acme:form>
