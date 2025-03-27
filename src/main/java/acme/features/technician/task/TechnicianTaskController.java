
package acme.features.technician.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiController
public class TechnicianTaskController extends AbstractGuiController<Technician, Task> {

	@Autowired
	private TechnicianTaskListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
