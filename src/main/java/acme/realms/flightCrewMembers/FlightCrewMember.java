
package acme.realms.flightCrewMembers;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLongText;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Automapped
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$") //¿Hay que validar que sean sus iniciales? decia una "\d", pero me daba error, preguntar
	@Mandatory
	private String				employeeCode;

	@Automapped
	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$") //Mismo error que antes, no se si asi está solucionado (una "\" no "\\")
	private String				phoneNumber;

	@Automapped
	@Mandatory
	@ValidLongText
	private String				languageSkill;

	@Mandatory
	@Automapped
	@Valid
	private AvailabilityStatus	availabilityStatus;

	/*
	 * @Mandatory
	 * 
	 * @Automapped
	 * 
	 * @Valid
	 * 
	 * @ManyToOne(optional = false) //preguntar
	 * private Airline airline; //todavia no esta creada xd
	 */

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 120)
	private Integer				yearsOfExperience;

}
