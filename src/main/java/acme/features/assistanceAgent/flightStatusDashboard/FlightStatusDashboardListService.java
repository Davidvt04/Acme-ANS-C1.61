
package acme.features.assistanceAgent.flightStatusDashboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.delay.Delay;
import acme.forms.delays.DelayDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class FlightStatusDashboardListService extends AbstractGuiService<AssistanceAgent, Delay> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightStatusDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		List<DelayDashboard> delayDashboards = new LinkedList<>();

		Collection<Delay> delays = this.repository.getDelays();

		for (Delay delay : delays) {
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
			delayDashboards.add(dashboard);
		}

		super.getBuffer().addData(delays);
	}

	@Override
	public void unbind(final Delay delayDashboard) {
		Dataset dataset = super.unbindObject(delayDashboard, "airline", "departureAirport", "arrivalAirport", "status");
		super.getResponse().addData(dataset);
	}

}
