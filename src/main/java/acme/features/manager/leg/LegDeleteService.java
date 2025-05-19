
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.realms.managers.Manager;

@GuiService
public class LegDeleteService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int legId = super.getRequest().getData("id", int.class);
			Leg leg = this.repository.findLegById(legId);
			Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
			// Allow deletion only if the leg exists, is in draft mode, and belongs to the current manager.
			status = leg != null && leg.isDraftMode() && leg.getFlight().getManager().getId() == manager.getId();
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		// For deletion, binding is usually minimal.
	}

	@Override
	public void validate(final Leg leg) {
		// No extra validation needed if authorisation already ensures deletion is permitted.
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(this.repository.findLegById(leg.getId()));
	}

	@Override
	public void unbind(final Leg leg) {

	}

}
