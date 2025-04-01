
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignament.CurrentStatus;
import acme.entities.flightAssignament.Duty;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberMemberFlightAssignamentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int flightAssignamentId = super.getRequest().getData("id", int.class);
		boolean authorised = this.repository.thatFlightAssignamentIsOf(flightAssignamentId, flightCrewMemberId);
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		FlightAssignament flightAssignament;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignament = this.repository.findFlightAssignamentById(id);

		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {
		Collection<Leg> legs;
		SelectChoices legChoices;

		Collection<FlightCrewMember> flightCrewMembers;
		SelectChoices flightCrewMemberChoices;

		Dataset dataset;

		SelectChoices currentStatus;
		SelectChoices duty;

		legs = this.repository.findAllLegs();
		flightCrewMembers = this.repository.findFlightCrewMembersByAvailability(AvailabilityStatus.AVAILABLE);

		currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignament.getCurrentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignament.getFlightCrewMember());

		dataset = super.unbindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks", "draftMode");
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMemberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", flightCrewMemberChoices);

		dataset.put("legIncompleted", MomentHelper.isFuture(flightAssignament.getLeg().getScheduledArrival()));

		super.getResponse().addData(dataset);
	}

}
