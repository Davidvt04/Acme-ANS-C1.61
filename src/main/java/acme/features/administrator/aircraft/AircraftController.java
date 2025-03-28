
package acme.features.administrator.aircraft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.aircraft.Aircraft;

@GuiController
public class AircraftController extends AbstractGuiController<Administrator, Aircraft> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AircraftListService		listService;

	@Autowired
	private AircraftShowService		showService;

	@Autowired
	private AircraftCreateService	createService;

	@Autowired
	private AircraftUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

	}

}
