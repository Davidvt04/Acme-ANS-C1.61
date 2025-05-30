
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
public class FlightCrewMemberFlightAssignamentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int flightAssignamentId = super.getRequest().getData("id", int.class);
			boolean authorised = this.repository.thatFlightAssignamentIsOf(flightAssignamentId, flightCrewMemberId);
			FlightAssignament flightAssignament;
			boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);
			flightAssignament = this.repository.findFlightAssignamentById(flightAssignamentId);
			int legId = super.getRequest().getData("leg", int.class);
			boolean authorised3 = true;
			if (legId != 0)
				authorised3 = this.repository.existsLeg(legId);

			status = authorised3 && authorised1 && authorised && flightAssignament.isDraftMode() && MomentHelper.isFuture(flightAssignament.getLeg().getScheduledArrival());
			boolean isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;

			status = status && isHis;
		}
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		FlightAssignament flightAssignament;
		flightAssignament = new FlightAssignament();

		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void bind(final FlightAssignament flightAssignament) {
		Integer legId;
		Leg leg;
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		int id = super.getRequest().getData("id", int.class);
		flightAssignament.setId(id);
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(flightAssignament, "duty", "currentStatus", "remarks");
		FlightAssignament original = this.repository.findFlightAssignamentById(flightAssignament.getId());

		flightAssignament.setLeg(leg);
		flightAssignament.setFlightCrewMember(flightCrewMember);
		flightAssignament.setMoment(original.getMoment());
	}

	@Override
	public void validate(final FlightAssignament flightAssignament) {
		FlightAssignament original = this.repository.findFlightAssignamentById(flightAssignament.getId());
		Leg leg = flightAssignament.getLeg();
		boolean cambioDuty = !original.getDuty().equals(flightAssignament.getDuty());

		boolean cambioLeg = !original.getLeg().equals(flightAssignament.getLeg());

		if (leg != null && cambioLeg && !this.isLegCompatible(flightAssignament))
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignament.FlightCrewMemberIncompatibleLegs.message");

		if (leg != null && (cambioDuty || cambioLeg))
			this.checkPilotAndCopilotAssignment(flightAssignament);

		boolean legCompleted = this.isLegCompleted(leg);
		if (legCompleted)
			super.state(false, "leg", "acme.validation.FlightAssignament.LegAlreadyCompleted.message");
	}

	private boolean isLegCompleted(final Leg leg) {
		return leg != null && leg.getScheduledArrival() != null && leg.getScheduledArrival().before(MomentHelper.getCurrentMoment());
	}

	private boolean isLegCompatible(final FlightAssignament flightAssignament) {
		Collection<Leg> legsByMember = this.repository.findLegsByFlightCrewMember(flightAssignament.getFlightCrewMember().getId());
		Leg newLeg = flightAssignament.getLeg();

		return legsByMember.stream().allMatch(existingLeg -> this.areLegsCompatible(newLeg, existingLeg));
	}

	private boolean areLegsCompatible(final Leg newLeg, final Leg oldLeg) {
		return !(MomentHelper.isInRange(newLeg.getScheduledDeparture(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()) || MomentHelper.isInRange(newLeg.getScheduledArrival(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()));
	}

	private void checkPilotAndCopilotAssignment(final FlightAssignament flightAssignament) {
		boolean havePilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignament.getLeg().getId(), Duty.PILOT);
		boolean haveCopilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignament.getLeg().getId(), Duty.COPILOT);

		if (Duty.PILOT.equals(flightAssignament.getDuty()))
			super.state(!havePilot, "duty", "acme.validation.FlightAssignament.havePilot.message");
		if (Duty.COPILOT.equals(flightAssignament.getDuty()))
			super.state(!haveCopilot, "duty", "acme.validation.FlightAssignament.haveCopilot.message");
	}

	@Override
	public void perform(final FlightAssignament flightAssignament) {
		flightAssignament.setMoment(MomentHelper.getCurrentMoment());
		flightAssignament.setDraftMode(false);

		this.repository.save(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {

		SelectChoices currentStatus;
		SelectChoices duty;

		Collection<Leg> legs;
		SelectChoices legChoices;
		boolean isCompleted;
		int flightAssignamentId;

		flightAssignamentId = super.getRequest().getData("id", int.class);

		Date currentMoment;
		currentMoment = MomentHelper.getCurrentMoment();
		isCompleted = this.repository.areLegsCompletedByFlightAssignament(flightAssignamentId, currentMoment);
		Dataset dataset;
		FlightAssignament fa = this.repository.findFlightAssignamentById(flightAssignamentId);
		legs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());

		currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignament.getCurrentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		dataset = super.unbindObject(flightAssignament, "duty", "moment", "CurrentStatus", "remarks", "draftMode");
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getCurrentMoment());
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMember.getEmployeeCode());
		dataset.put("isCompleted", isCompleted);
		dataset.put("draftMode", fa.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
