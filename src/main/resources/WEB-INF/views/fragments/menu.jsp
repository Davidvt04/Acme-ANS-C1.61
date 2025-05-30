<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.ivo" action="https://www.noticiasmontehermoso.com.ar"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.david" action="https://cadenadh.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.maria" action="https://archivoniebla.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.eloy" action="https://www.diariodesevilla.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.ivan" action="https://puginarug.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-claims" action="/administrator/claim/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-bookings" action="/administrator/booking/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-maintenance-records" action="/administrator/maintenance-record/list"/>

			<acme:menu-separator/>
			
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-recommendations" action="/administrator/recommendation/populate"/>	
			<acme:menu-suboption code="master.menu.administrator.populate-db-delays" action="/administrator/delay/populate"/>					
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.airline.list" action="/administrator/airline/list"/>
			<acme:menu-separator/>
			
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
	 <acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
		<acme:menu-suboption code="master.menu.manager.flight.list" action="/manager/flight/list"/>
    </acme:menu-option>
    

		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistanceAgent.list-claims" action="/assistance-agent/claim/list"/>			
			<acme:menu-suboption code="master.menu.assistanceAgent.list-claims-pending" action="/assistance-agent/claim/pending"/>	
			<acme:menu-suboption code="master.menu.assistanceAgent.show-dashboard" action="/assistance-agent/assistance-agent-dashboard/show"/>	
			<acme:menu-suboption code="master.menu.assistanceAgent.delay" action="/assistance-agent/delay/list"/>	
			
				
			
		</acme:menu-option>
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.list-passengers" action="/customer/passenger/list"/>
			<acme:menu-suboption code="master.menu.customer.show-dashboard" action="/customer/customer-dashboard/show"/>
			
		</acme:menu-option>
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.maintenance-records" action="/technician/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.technician.involves" action="/technician/involves/list"/>
			<acme:menu-suboption code="master.menu.technician.tasks" action="/technician/task/list"/>
			<acme:menu-suboption code="master.menu.technician.show-dashboard" action="/technician/technician-dashboard/show"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.flight-crew-member" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignament.completedlist" action="/flight-crew-member/flight-assignament/completed-list"/>
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignament.plannedlist" action="/flight-crew-member/flight-assignament/planned-list"/>
		</acme:menu-option>
	</acme:menu-left>
	


   
    
	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-assistance-agent" action="/authenticated/assistance-agent/create" access="!hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.assistance-agent-profile" action="/authenticated/assistance-agent/update" access="hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>
