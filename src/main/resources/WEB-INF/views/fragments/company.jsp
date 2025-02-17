<%--
- company.jsp
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

<h1><acme:print code="master.company.title"/></h1>

<p><acme:print code="master.company.text"/></p>

<address>
  <strong><acme:print code="master.company.name"/></strong> <br/>
  <span class="fas fa-map-marker"> &nbsp; </span><acme:print code="master.company.address"/> <br/>
  <span class="fa fa-phone"></span> &nbsp; <acme:print code="master.company.phone"/><br/>
  <span class="fa fa-at"></span> &nbsp; <a href="mailto:<acme:print code="master.company.email"/>"><acme:print code="master.company.email"/></a> <br/>
</address>

