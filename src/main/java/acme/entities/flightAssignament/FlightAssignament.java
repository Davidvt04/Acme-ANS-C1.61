
package acme.entities.flightAssignament;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.flightCrewMembers.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignament extends AbstractEntity {

	@Mandatory
	@Automapped
	@Valid
	@ManyToOne
	private FlightCrewMember	flightCrewMember;
	/*
	 * @Mandatory
	 * 
	 * @Automapped
	 * 
	 * @Valid
	 * 
	 * @ManyToOne
	 * private Leg leg; //todavia no está creada
	 */
	@Mandatory
	@Automapped
	@Valid
	private Duty				duty;

	@Mandatory
	@ValidMoment(past = true, min = "2000/01/01 00:00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Automapped
	@Valid
	private CurrentStatus		currentStatus;

	@Optional
	@Automapped
	@ValidString
	private String				remarks;

}
