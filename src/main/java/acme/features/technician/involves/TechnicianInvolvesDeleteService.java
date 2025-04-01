
package acme.features.technician.involves;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesDeleteService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Involves involves;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);
		status = involves != null && super.getRequest().getPrincipal().hasRealm(involves.getMaintenanceRecord().getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		int id;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);

		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final Involves involves) {
		String taskTicker;
		int masterId;
		Task task;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		taskTicker = super.getRequest().getData("task", String.class);
		task = this.repository.findTaskByTicker(taskTicker);

		super.bindObject(involves);
		involves.setTask(task);
		involves.setMaintenanceRecord(maintenanceRecord);
	}

	@Override
	public void validate(final Involves involves) {
		;
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.delete(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;

		dataset = super.unbindObject(involves);
		dataset.put("task", involves.getTask().getTicker());
		dataset.put("taskTechnician", involves.getTask().getTechnician().getLicenseNumber());

		super.getResponse().addData(dataset);
	}
}
