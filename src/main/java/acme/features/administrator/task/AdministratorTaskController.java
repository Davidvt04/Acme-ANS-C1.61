
package acme.features.administrator.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.task.Task;

@GuiController
public class AdministratorTaskController extends AbstractGuiController<Administrator, Task> {

	@Autowired
	private AdministratorTaskListService	listService;

	@Autowired
	private AdministratorTaskShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
