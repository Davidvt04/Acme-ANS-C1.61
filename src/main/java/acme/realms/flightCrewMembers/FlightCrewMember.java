
package acme.realms.flightCrewMembers;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightCrewMember;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhoneNumber;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidFlightCrewMember
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique=true)
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				employeeCode;

	@Automapped
	@Mandatory
	@ValidPhoneNumber
	private String				phoneNumber;

	@Automapped
	@Mandatory
	@ValidLongText
	private String				languageSkill;

	@Mandatory
	@Automapped
	@Valid
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 120)
	private Integer				yearsOfExperience;

}
