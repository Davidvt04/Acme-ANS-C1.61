<%--
- form.jsp
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

<acme:form readonly="true">
	<acme:input-double code="assistanceAgent.dashboard.form.label.claimsResolvedRatio" path="claimsResolvedRatio"/>	
	<acme:input-double code="assistanceAgent.dashboard.form.label.claimsRejectedRatio" path="claimsRejectedRatio"/>	
	<acme:input-textbox code="assistanceAgent.dashboard.form.label.topThreeMonthsByClaims" path="topThreeMonthsByClaims"/>	
	<acme:input-double code="assistanceAgent.dashboard.form.label.averageLogsPerClaim" path="averageLogsPerClaim" />
	<acme:input-integer code="assistanceAgent.dashboard.form.label.minimumLogsPerClaim" path="minimumLogsPerClaim" />
	<acme:input-integer code="assistanceAgent.dashboard.form.label.maximumLogsPerClaim" path="maximumLogsPerClaim" />
	<acme:input-double code="assistanceAgent.dashboard.form.label.standardDeviationLogsPerClaim" path="standardDeviationLogsPerClaim"/>	
	<acme:input-double code="assistanceAgent.dashboard.form.label.averageClaimsLastMonth" path="averageClaimsLastMonth"/>	
	<acme:input-integer code="assistanceAgent.dashboard.form.label.minimumClaimsLastMonth" path="minimumClaimsLastMonth"/>	
	<acme:input-integer code="assistanceAgent.dashboard.form.label.maximumClaimsLastMonth" path="maximumClaimsLastMonth" />
	<acme:input-double code="assistanceAgent.dashboard.form.label.standardDeviationClaimsLastMonth" path="standardDeviationClaimsLastMonth" />
	
		
</acme:form>