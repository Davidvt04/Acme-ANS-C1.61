
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
public class TechnicianInvolvesShowService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Involves involves;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);
		status = involves != null && super.getRequest().getPrincipal().getActiveRealm().getId() == //
			involves.getMaintenanceRecord().getTechnician().getId();

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
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		SelectChoices maintenanceRecordChoices;
		Collection<Task> tasks;
		Collection<MaintenanceRecord> maintenanceRecords;
		final boolean draftRecord;

		tasks = this.repository.findAllTasks();
		taskChoices = SelectChoices.from(tasks, "ticker", involves.getTask());

		maintenanceRecords = this.repository.findAllMaintenanceRecords();
		maintenanceRecordChoices = SelectChoices.from(maintenanceRecords, "ticker", involves.getMaintenanceRecord());

		dataset = super.unbindObject(involves);

		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);
		dataset.put("taskTechnician", involves.getTask().getTechnician().getLicenseNumber());

		dataset.put("maintenanceRecord", maintenanceRecordChoices.getSelected().getKey());
		dataset.put("maintenanceRecords", maintenanceRecordChoices);
		dataset.put("maintenanceRecordTechnician", involves.getTask().getTechnician().getLicenseNumber());

		draftRecord = involves.getMaintenanceRecord().isDraftMode();
		super.getResponse().addGlobal("draftRecord", draftRecord);

		super.getResponse().addData(dataset);
	}

}
