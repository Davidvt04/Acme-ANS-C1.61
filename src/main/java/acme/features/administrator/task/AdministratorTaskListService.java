
package acme.features.administrator.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@GuiService
public class AdministratorTaskListService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		status = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> object;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findTasksInvolvedByMasterId(masterId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "ticker", "type", "priority");
		super.addPayload(dataset, task, "description", "estimatedDuration");

		super.getResponse().addData(dataset);
	}
}
