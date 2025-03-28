
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		// Allow deletion only if the leg exists, is in draft mode, and belongs to the current manager.
		boolean status = leg != null && leg.isDraftMode() && leg.getFlight().getManager().getId() == manager.getId();
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
		// Typically, for deletion, binding may not be necessary.
		// However, to follow the template, bind any relevant fields.
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours");
	}

	@Override
	public void validate(final Leg leg) {
		// No extra validation is needed if authorisation ensures that only draft legs can be deleted.
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");
		dataset.put("status", leg.getStatus());
		super.getResponse().addData(dataset);
	}
}
