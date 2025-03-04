
package acme.entities.passenger;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.booking.Booking;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ManyToOne
	private Booking				booking;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 256)
	private String				fullName;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				email;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	private String				passportNumber;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	private Moment				dateOfBirth;

	@Mandatory
	@Automapped
	@ValidString(max = 51)
	private String				specialNeeds;

}
