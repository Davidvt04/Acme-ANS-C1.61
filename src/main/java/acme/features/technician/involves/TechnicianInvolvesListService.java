
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesListService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int masterId;
		Collection<Involves> involves;

		masterId = super.getRequest().getData("masterId", int.class);
		involves = this.repository.findInvolvesByMasterId(masterId);

		super.getBuffer().addData(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;

		dataset = super.unbindObject(involves);
		dataset.put("taskTicker", involves.getTask().getTicker());
		dataset.put("taskType", involves.getTask().getType());
		dataset.put("taskPriority", involves.getTask().getPriority());
		dataset.put("taskTechnician", involves.getTask().getTechnician().getLicenseNumber());
		super.addPayload(dataset, involves);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Involves> involves) {
		int masterId;
		final boolean draft;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		draft = maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("draft", draft);
	}
}
