
package acme.features.assistanceAgent.flightStatusDashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.delay.Delay;
import acme.forms.delays.DelayDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class FlightStatusDashboardShowService extends AbstractGuiService<AssistanceAgent, Delay> {

	@Autowired
	private FlightStatusDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Delay delay;
		int id;

		id = super.getRequest().getData("id", int.class);
		delay = this.repository.findDelayById(id).get();

		DelayDashboard dashboard = new DelayDashboard();
		dashboard.setAirline(delay.getAirline());
		dashboard.setArrivalActualDateTime(delay.getArrivalActualDateTime());
		dashboard.setArrivalAirport(delay.getArrivalAirport());
		dashboard.setArrivalScheduledDateTime(delay.getArrivalScheduledDateTime());
		dashboard.setDepartureActualDateTime(delay.getDepartureScheduledDateTime());
		dashboard.setDepartureAirport(delay.getDepartureAirport());
		dashboard.setDepartureScheduledDateTime(delay.getDepartureScheduledDateTime());
		dashboard.setStatus(delay.getStatus());
		dashboard.setId(delay.getId());

		super.getBuffer().addData(delay);
	}

	@Override
	public void unbind(final Delay delayDashboard) {
		Dataset dataset = super.unbindObject(delayDashboard, "airline", "departureAirport", "arrivalAirport", "departureScheduledDateTime", "arrivalScheduledDateTime", "departureActualDateTime", "arrivalActualDateTime", "status", "id");

		super.getResponse().addData(dataset);
	}

}
