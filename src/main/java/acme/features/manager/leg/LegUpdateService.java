
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.managers.Manager;

@GuiService
public class LegUpdateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		// Allow update only if the leg exists, is in draft mode, and belongs to the current manager
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
		// Bind the fields that are allowed to be updated.
		// Adjust these field names as necessary according to your Leg entity.
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours");
	}

	@Override
	public void validate(final Leg leg) {
		// Example validation: scheduledDeparture must be before scheduledArrival.
		super.state(leg.getScheduledDeparture().before(leg.getScheduledArrival()), "scheduledDeparture", "manager.leg.error.departureBeforeArrival");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		// Unbind key fields into a Dataset.
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");
		// Add the current leg status.
		dataset.put("status", leg.getStatus());
		// Create select choices for the LegStatus enum.
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);
		super.getResponse().addData(dataset);
	}
}
