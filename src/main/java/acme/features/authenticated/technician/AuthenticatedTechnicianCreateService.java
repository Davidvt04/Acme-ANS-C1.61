
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Technician;

@GuiService
public class AuthenticatedTechnicianCreateService extends AbstractGuiService<Authenticated, Technician> {

	@Autowired
	private AuthenticatedTechnicianRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Technician();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Technician object) {
		assert object != null;

		super.bindObject(object, "licenseNumber", "phoneNumber", "specialisation", "passedAnnualHealthTest", "experienceYears", "certifications");
	}

	@Override
	public void validate(final Technician object) {
		Technician existTechnician;
		boolean validLicenseNumber;
		boolean validPhoneNumber;

		existTechnician = this.repository.findTechnicianByLicenseNumber(object.getLicenseNumber());
		validLicenseNumber = existTechnician == null || existTechnician.getId() == object.getId();
		if (!validLicenseNumber)
			super.state(validLicenseNumber, "licenseNumber", "acme.validation.technician.license-number.duplicated.message");

		existTechnician = this.repository.findTechnicianByPhoneNumber(object.getPhoneNumber());
		validPhoneNumber = existTechnician == null || existTechnician.getId() == object.getId();
		super.state(validPhoneNumber, "phoneNumber", "acme.validation.technician.phone-number.duplicated.message");

	}

	@Override
	public void perform(final Technician object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Technician object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "licenseNumber", "phoneNumber", "specialisation", //
			"passedAnnualHealthTest", "experienceYears", "certifications");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
