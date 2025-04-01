
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
public class TechnicianInvolvesCreateService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;

		id = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);
		status = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves object;
		int masterId;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);

		object = new Involves();
		object.setTask(null);
		object.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(object);
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
		this.repository.save(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;

		dataset = super.unbindObject(involves, "task");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("maintenanceRecord", involves.getMaintenanceRecord().getId());

		super.getResponse().addData(dataset);
	}
}
