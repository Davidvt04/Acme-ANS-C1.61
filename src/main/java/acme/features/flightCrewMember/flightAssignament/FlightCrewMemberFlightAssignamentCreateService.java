
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
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignamentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);

		boolean authorised2 = true;
		if (super.getRequest().hasData("leg", int.class)) {
			int legId = super.getRequest().getData("leg", int.class);
			if (legId != 0)
				authorised2 = this.repository.existsLeg(legId);

		}

		boolean authorised = authorised1 && authorised2;

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		FlightAssignament flightAssignament;

		flightAssignament = new FlightAssignament();

		flightAssignament.setDraftMode(true);
		flightAssignament.setCurrentStatus(CurrentStatus.PENDING);
		flightAssignament.setDuty(Duty.CABIN_ATTENDANT);

		flightAssignament.setFlightCrewMember(this.repository.findFlightCrewMemberById(super.getRequest().getPrincipal().getActiveRealm().getId()));
		flightAssignament.setMoment(MomentHelper.getCurrentMoment());
		flightAssignament.setRemarks("");
		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void bind(final FlightAssignament flightAssignament) {
		Integer legId;
		Leg leg;

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(flightAssignament, "duty", "currentStatus", "remarks");
		flightAssignament.setLeg(leg);
		flightAssignament.setFlightCrewMember(flightCrewMember);
	}

	@Override
	public void validate(final FlightAssignament flightAssignament) {

		FlightCrewMember flightCrewMember = flightAssignament.getFlightCrewMember();
		Leg leg = flightAssignament.getLeg();
		if (flightCrewMember != null && leg != null && this.isLegCompatible(flightAssignament)) {
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignament.FlightCrewMemberIncompatibleLegs.message");
			return;
		}
		if (leg != null)
			this.checkPilotAndCopilotAssignment(flightAssignament);
	}

	private boolean isLegCompatible(final FlightAssignament flightAssignament) {

		Collection<Leg> legsByFlightCrewMember = this.repository.findLegsByFlightCrewMember(flightAssignament.getFlightCrewMember().getId());
		Leg newLeg = flightAssignament.getLeg();

		return legsByFlightCrewMember.stream().anyMatch(existingLeg -> !this.compatibleLegs(newLeg, existingLeg));
	}

	private void checkPilotAndCopilotAssignment(final FlightAssignament flightAssignament) {
		boolean havePilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignament.getLeg().getId(), Duty.PILOT);
		boolean haveCopilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignament.getLeg().getId(), Duty.COPILOT);

		if (Duty.PILOT.equals(flightAssignament.getDuty()))
			super.state(!havePilot, "duty", "acme.validation.FlightAssignament.havePilot.message");
		if (Duty.COPILOT.equals(flightAssignament.getDuty()))
			super.state(!haveCopilot, "duty", "acme.validation.FlightAssignament.haveCopilot.message");
	}

	private boolean compatibleLegs(final Leg newLeg, final Leg oldLeg) {
		return !(MomentHelper.isInRange(newLeg.getScheduledDeparture(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()) || MomentHelper.isInRange(newLeg.getScheduledArrival(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()));
	}

	@Override
	public void perform(final FlightAssignament flightAssignament) {
		flightAssignament.setMoment(MomentHelper.getCurrentMoment());
		this.repository.save(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {

		SelectChoices currentStatus;
		SelectChoices duty;

		Collection<Leg> legs;
		SelectChoices legChoices;

		Dataset dataset;

		legs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignament.getCurrentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());

		dataset = super.unbindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getBaseMoment());
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMember.getEmployeeCode());

		super.getResponse().addData(dataset);
	}
}
