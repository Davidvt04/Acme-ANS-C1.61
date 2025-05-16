
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;
import java.util.Date;

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
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberMemberFlightAssignamentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {
		boolean authorised = false;
		boolean isHis = false;
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int flightAssignamentId = super.getRequest().getData("id", int.class);
		FlightAssignament flightAssignament = this.repository.findFlightAssignamentById(flightAssignamentId);
		if (flightAssignament != null) {
			boolean authorised2 = this.repository.existsFlightAssignament(flightAssignamentId);
			boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);
			authorised = authorised2 && authorised1 && this.repository.thatFlightAssignamentIsOf(flightAssignamentId, flightCrewMemberId);
			isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;
		}
		super.getResponse().setAuthorised(authorised && isHis);
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

		Dataset dataset;

		SelectChoices currentStatus;
		int flightAssignamentId;

		flightAssignamentId = super.getRequest().getData("id", int.class);
		SelectChoices duty;
		boolean isCompleted;
		legs = this.repository.findAllLegs();

		currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignament.getCurrentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());
		Date currentMoment;
		currentMoment = MomentHelper.getCurrentMoment();
		isCompleted = this.repository.areLegsCompletedByFlightAssignament(flightAssignamentId, currentMoment);
		dataset = super.unbindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks", "draftMode");
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMember.getEmployeeCode());
		dataset.put("isCompleted", isCompleted);
		super.getResponse().addData(dataset);
	}

}
