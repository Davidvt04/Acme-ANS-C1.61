
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.CurrentStatus;
import acme.entities.flightAssignament.Duty;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignamentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {
		int flightAssignamentId = super.getRequest().getData("id", int.class);
		FlightAssignament flightAssignament = this.repository.findFlightAssignamentById(flightAssignamentId);

		super.getResponse().setAuthorised(flightAssignament != null && flightAssignament.isDraftMode());
	}

	@Override
	public void load() {
		FlightAssignament flightAssignament = new FlightAssignament();
		flightAssignament.setDraftMode(true);
		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void bind(final FlightAssignament flightAssignament) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		int flightCrewMemberId = super.getRequest().getData("flightCrewMember", int.class);
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		super.bindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks");
		flightAssignament.setLeg(leg);
		flightAssignament.setFlightCrewMember(flightCrewMember);
	}

	@Override
	public void validate(final FlightAssignament flightAssignament) {
	}

	@Override
	public void perform(final FlightAssignament flightAssignament) {
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignamentId(flightAssignament.getId());
		this.repository.deleteAll(activityLogs);
		this.repository.delete(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {
		SelectChoices currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignament.getCurrentStatus());
		SelectChoices duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());

		Collection<Leg> legs = this.repository.findAllLegs();
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());

		Collection<FlightCrewMember> flightCrewMembers = this.repository.findFlightCrewMembersByAvailability(AvailabilityStatus.AVAILABLE);
		SelectChoices flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignament.getFlightCrewMember());

		Dataset dataset = super.unbindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getBaseMoment());
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMemberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", flightCrewMemberChoices);

		super.getResponse().addData(dataset);
	}
}
