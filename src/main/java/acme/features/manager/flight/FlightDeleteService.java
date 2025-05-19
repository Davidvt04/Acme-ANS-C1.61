
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.leg.ManagerLegRepository;
import acme.realms.managers.Manager;

@GuiService
public class FlightDeleteService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository		repository;

	@Autowired
	private ManagerLegRepository	managerLegRepository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int flightId;
			Flight flight;

			flightId = super.getRequest().getData("id", int.class);
			flight = this.repository.findById(flightId);

			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			status = flight != null && flight.isDraftMode() && flight.getManager().getId() == managerId;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id = super.getRequest().getData("id", int.class);
		flight = this.repository.findById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {

	}

	@Override
	public void validate(final Flight flight) {
		// No additional validation is needed for deletion if the authorisation is correct.
	}

	@Override
	public void perform(final Flight flight) {
		Integer flightId = flight.getId();
		Collection<Leg> legs = this.managerLegRepository.findLegsByflightId(flight.getId());
		if (legs == null)
			throw new IllegalStateException("managerLegRepository.findLegsByflightId(" + flight.getId() + ") returned NULL");
		for (Leg leg : legs)
			this.managerLegRepository.delete(leg);
		this.repository.deleteById(flightId);
	}

	@Override
	public void unbind(final Flight flight) {

	}
}
