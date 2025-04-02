
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
		int masterId;
		final boolean draft;

		dataset = super.unbindObject(involves);
		dataset.put("taskTicker", involves.getTask().getTicker());
		dataset.put("taskType", involves.getTask().getType());
		dataset.put("taskPriority", involves.getTask().getPriority());

		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		draft = this.repository.findMaintenanceRecordById(masterId).isDraftMode();

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("draft", draft);
		super.getResponse().addData(dataset);
	}

}
