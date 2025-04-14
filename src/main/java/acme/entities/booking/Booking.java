
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.flight.Flight;
import acme.features.authenticated.booking.BookingRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	private String				locatorCode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	@Valid
	private TravelClass			travelClass;

	@Optional
	@Automapped
	@ValidString(min = 4, max = 4)
	private String				lastNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;


	@Transient
	public Money getPrice() {
		Money res = new Money();
		if (this.getFlight() != null) {
			Money flightCost = this.getFlight().getCost();
			BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
			Integer numberOfPassengers = bookingRepository.getNumberPassengersOfBooking(this.getId());

			res.setCurrency(flightCost.getCurrency());
			res.setAmount(flightCost.getAmount() * numberOfPassengers);
			return res;
		}
		res.setAmount(0.0);
		res.setCurrency("EUR");
		return res;

	}

}
