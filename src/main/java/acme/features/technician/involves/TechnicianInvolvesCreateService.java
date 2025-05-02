
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
		Task task;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecord = super.getRequest().getData("maintenanceRecord", MaintenanceRecord.class);
		task = super.getRequest().getData("task", Task.class);

		super.bindObject(involves);
		involves.setTask(task);
		involves.setMaintenanceRecord(maintenanceRecord);
	}

	@Override
	public void validate(final Involves involves) {
		boolean publishedTask;
		boolean publishedRecord;
		Task task;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecord = super.getRequest().getData("maintenanceRecord", MaintenanceRecord.class);
		task = super.getRequest().getData("task", Task.class);

		publishedTask = !task.isDraftMode();
		if (publishedTask)
			super.state(publishedTask, "*", "acme.validation.involves.published-task.message");

		publishedRecord = !maintenanceRecord.isDraftMode();
		super.state(publishedRecord, "", "acme.validation.involves.published-record.message");
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		SelectChoices maintenanceRecordChoices;
		Collection<Task> tasks;
		Collection<MaintenanceRecord> maintenanceRecords;
		final boolean draftTask;
		final boolean draftRecord;

		tasks = this.repository.findAllTasks();
		taskChoices = SelectChoices.from(tasks, "ticker", involves.getTask());

		maintenanceRecords = this.repository.findAllMaintenanceRecords();
		maintenanceRecordChoices = SelectChoices.from(maintenanceRecords, "ticker", involves.getMaintenanceRecord());

		dataset = super.unbindObject(involves);

		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);
		dataset.put("taskTechnician", involves.getTask().getTechnician().getLicenseNumber());
		dataset.put("taskId", involves.getTask().getId());

		dataset.put("maintenanceRecord", maintenanceRecordChoices.getSelected().getKey());
		dataset.put("maintenanceRecords", maintenanceRecordChoices);
		dataset.put("maintenanceRecordTechnician", involves.getTask().getTechnician().getLicenseNumber());

		draftTask = involves.getTask().isDraftMode();
		super.getResponse().addGlobal("draftTask", draftTask);

		draftRecord = involves.getMaintenanceRecord().isDraftMode();
		super.getResponse().addGlobal("draftRecord", draftRecord);

		super.getResponse().addData(dataset);
	}
}
