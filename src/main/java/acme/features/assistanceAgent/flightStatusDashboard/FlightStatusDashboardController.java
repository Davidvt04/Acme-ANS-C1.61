
package acme.features.assistanceAgent.flightStatusDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.delay.Delay;
import acme.realms.AssistanceAgent;

@GuiController
public class FlightStatusDashboardController extends AbstractGuiController<AssistanceAgent, Delay> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightStatusDashboardListService	listService;
	@Autowired
	private FlightStatusDashboardShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}

}
