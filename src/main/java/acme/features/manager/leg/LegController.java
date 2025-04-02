
package acme.features.manager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
// Adjust package if Leg is defined elsewhere
import acme.entities.leg.Leg;
import acme.realms.managers.Manager;

@GuiController
public class LegController extends AbstractGuiController<Manager, Leg> {

	@Autowired
	private LegListService		listService;

	@Autowired
	private LegShowService		showService;

	@Autowired
	private LegCreateService	createService;

	@Autowired
	private LegUpdateService	updateService;

	@Autowired
	private LegDeleteService	deleteService;

	@Autowired
	private LegPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
