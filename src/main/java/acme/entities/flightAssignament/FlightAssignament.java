
package acme.entities.flightAssignament;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightAssignament
@Table(indexes = {
	@Index(columnList = "draftMode"), @Index(columnList = "moment"), @Index(columnList = "currentStatus")
})

public class FlightAssignament extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// no necesita @Valid porque es un tipo primitivo
	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

	@Mandatory
	@Automapped
	@Valid
	private Duty				duty;

	@Mandatory
	@ValidMoment(past = true)
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
