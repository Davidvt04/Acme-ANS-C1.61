
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignamentPlannedListService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean authorised = this.repository.existsFlightCrewMember(flightCrewMemberId);
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Collection<FlightAssignament> flightAssignaments;

		Date currentMoment;
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		currentMoment = MomentHelper.getCurrentMoment();
		flightAssignaments = this.repository.findAllFlightAssignamentByPlannedLeg(currentMoment, flightCrewMemberId);

		super.getBuffer().addData(flightAssignaments);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {
		Dataset dataset = super.unbindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks");

		super.getResponse().addData(dataset);
	}
}
