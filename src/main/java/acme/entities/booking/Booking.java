
package acme.entities.booking;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ManyToOne
	private Customer			customer;

	@Mandatory
	@Column(unique = true)
	@Automapped
	@ValidString(min = 3, max = 3, pattern = "^[A-Z0-9]{6,8}$")
	private String				locatorCode;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	private Moment				purchaseMoment;

	@Mandatory
	@Automapped
	@Valid
	private TravelClass			travelClass;

	@Optional
	@Automapped
	@ValidMoney
	private Money				price;

	@Optional
	@Automapped
	private String				lastNibble;

}
