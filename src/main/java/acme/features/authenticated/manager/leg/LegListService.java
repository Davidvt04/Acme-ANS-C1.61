
package acme.features.authenticated.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.realms.managers.Manager;

@GuiService
public class LegListService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		// All managers are authorised to list the legs of their flights.
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		// Retrieve the current manager from the principal
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Retrieve the legs associated with flights of this manager, ordered by the leg's moment.
		Collection<Leg> legs = this.repository.findLegsByManagerIdOrderByMoment(managerId);
		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		// Unbind key fields of Leg into a Dataset.
		// Adjust the field names as necessary based on your Leg entity.
		Dataset dataset = super.unbindObject(leg, "moment", "originCity", "destinationCity");
		// If you have additional leg fields (e.g., duration) add them here:
		// super.addPayload(dataset, leg, "duration");

		super.getResponse().addData(dataset);
	}
}
