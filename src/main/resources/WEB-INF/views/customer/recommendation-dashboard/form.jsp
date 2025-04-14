<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-textbox code="customer.recommendation-dashboard.form.label.city" path="city"/>
    <acme:input-textbox code="customer.recommendation-dashboard.form.label.country" path="country"/>
    <acme:input-textbox code="customer.recommendation-dashboard.form.label.name" path="name"/>
    <acme:input-textbox code="customer.recommendation-dashboard.form.label.recommendationType" path="recommendationType"/>
</acme:form>