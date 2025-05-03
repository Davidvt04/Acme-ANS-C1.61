
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Involves;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesListService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Involves> involves;

		involves = this.repository.findAllInvolves();

		super.getBuffer().addData(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;

		dataset = super.unbindObject(involves);
		dataset.put("taskTicker", involves.getTask().getTicker());
		dataset.put("maintenanceRecordTicker", involves.getMaintenanceRecord().getTicker());
		dataset.put("taskTechnician", involves.getTask().getTechnician().getLicenseNumber());
		dataset.put("maintenanceRecordTechnician", involves.getMaintenanceRecord().getTechnician().getLicenseNumber());
		super.addPayload(dataset, involves);

		super.getResponse().addData(dataset);
	}
}
