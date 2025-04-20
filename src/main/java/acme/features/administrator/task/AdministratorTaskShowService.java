
package acme.features.administrator.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;

@GuiService
public class AdministratorTaskShowService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Task task;

		masterId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(masterId);
		status = task != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "ticker", "description", "type", "priority", "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}
}
