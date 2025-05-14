
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
		String method;
		int taskId;
		int maintenanceRecordId;
		Task task;
		MaintenanceRecord maintenanceRecord;

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			taskId = super.getRequest().getData("task", int.class);
			maintenanceRecordId = super.getRequest().getData("maintenanceRecord", int.class);
			task = this.repository.findTaskById(taskId);
			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
			status = task != null && maintenanceRecord != null ? //
				maintenanceRecord.isDraftMode() && //
					super.getRequest().getPrincipal().getActiveRealm().getId() == maintenanceRecord.getTechnician().getId() //
				: taskId == 0 || maintenanceRecordId == 0;
		}

		//super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves object;

		object = new Involves();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Involves involves) {
		int taskId;
		int maintenanceRecordId;
		Task task;
		MaintenanceRecord maintenanceRecord;

		taskId = super.getRequest().getData("task", int.class);
		maintenanceRecordId = super.getRequest().getData("maintenanceRecord", int.class);
		task = this.repository.findTaskById(taskId);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

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
		SelectChoices taskChoices;
		SelectChoices maintenanceRecordChoices;
		Collection<Task> tasks;
		Collection<MaintenanceRecord> maintenanceRecords;

		tasks = this.repository.findAllTasks();
		taskChoices = SelectChoices.from(tasks, "ticker", involves.getTask());

		maintenanceRecords = this.repository.findAllDraftMaintenanceRecords();
		maintenanceRecordChoices = SelectChoices.from(maintenanceRecords, "ticker", involves.getMaintenanceRecord());

		dataset = super.unbindObject(involves);

		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);

		dataset.put("maintenanceRecord", maintenanceRecordChoices.getSelected().getKey());
		dataset.put("maintenanceRecords", maintenanceRecordChoices);

		super.getResponse().addData(dataset);
	}
}
