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
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
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
    

	</acme:menu-left>


   
    
	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>
