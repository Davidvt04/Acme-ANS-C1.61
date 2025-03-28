
package acme.features.authenticated.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.managers.Manager;

@GuiService
public class LegShowService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		// For simplicity, we authorize all managers. You can add further restrictions if needed.
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);
		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		// Unbind basic fields; adjust field names as per your Leg entity.
		Dataset dataset = super.unbindObject(leg, "originCity", "destinationCity");

		// For date fields, wrap them in an Object array to support formatting in the JSP.
		Object departure = leg.getScheduledDeparture();
		Object arrival = leg.getScheduledArrival();
		dataset.put("scheduledDeparture", new Object[] {
			departure
		});
		dataset.put("scheduledArrival", new Object[] {
			arrival
		});

		// Unbind the status field directly.
		dataset.put("status", leg.getStatus());

		// Create SelectChoices for the LegStatus enum, marking the current status as selected.
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);

		// Optionally, mark the dataset as read-only.
		dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}
}
