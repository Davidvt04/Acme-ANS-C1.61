
package acme.features.manager.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;
import acme.realms.managers.Manager;

@GuiController
public class FlightController extends AbstractGuiController<Manager, Flight> {

	@Autowired
	private FlightListService		listService;

	@Autowired
	private FlightShowService		showService;

	@Autowired
	private FlightCreateService		createService;

	@Autowired
	private FlightUpdateService		updateService;

	@Autowired
	private FlightPublishService	publishService;

	@Autowired
	private FlightDeleteService		deleteService;


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
