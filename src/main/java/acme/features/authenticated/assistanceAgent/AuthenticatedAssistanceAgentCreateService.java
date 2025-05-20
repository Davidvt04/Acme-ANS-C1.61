/*
 * AuthenticatedCustomerCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.assistanceAgent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentCreateService extends AbstractGuiService<Authenticated, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAssistanceAgentRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new AssistanceAgent();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AssistanceAgent object) {
		assert object != null;

		super.bindObject(object, "employeeCode", "spokenLanguages", "moment", "briefBio", "salary", "photo", "airline");
	}

	@Override
	public void validate(final AssistanceAgent object) {
		String employeeCode = object.getEmployeeCode();
		AssistanceAgent existing = this.repository.findAssistanceAgentByEmployeeCode(employeeCode);
		boolean valid = existing == null || existing.getId() == object.getId();
		super.state(valid, "employeeCode", "acme.validation.employeeCode.invalidEmployeeCode.message");
	}

	@Override
	public void perform(final AssistanceAgent object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AssistanceAgent object) {
		Dataset dataset;
		Collection<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices airlineChoices = SelectChoices.from(airlines, "iataCode", object.getAirline());

		dataset = super.unbindObject(object, "employeeCode", "spokenLanguages", "moment", "briefBio", "salary", "photo", "airline");
		dataset.put("airlineChoices", airlineChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
