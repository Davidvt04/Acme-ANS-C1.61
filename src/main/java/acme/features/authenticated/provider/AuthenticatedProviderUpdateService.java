/*
 * AuthenticatedProviderUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.provider;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Provider;

@GuiService
public class AuthenticatedProviderUpdateService extends AbstractGuiService<Authenticated, Provider> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedProviderRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Provider.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Provider object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findProviderByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Provider object) {
		assert object != null;

		super.bindObject(object, "company", "sector");
	}

	@Override
	public void validate(final Provider object) {
		assert object != null;
	}

	@Override
	public void perform(final Provider object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Provider object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "company", "sector");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
