
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
	private TechnicianTaskListService			listService;

	@Autowired
	private TechnicianTaskShowService			showService;

	@Autowired
	private TechnicianTaskDeleteService			deleteService;

	@Autowired
	private TechnicianTaskUpdateService			updateService;

	@Autowired
	private TechnicianTaskCreateService			createService;

	@Autowired
	private TechnicianTaskPublishService		publishService;

	@Autowired
	private TechnicianTaskListFromRecordService	listFromRecordService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);

		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-from-record", "list", this.listFromRecordService);
	}
}
