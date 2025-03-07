
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Relación muchos Leg -> un Flight
	@ManyToOne
	@Mandatory // No sabemos si un flight tiene porque tener legs
	@Automapped
	@Valid
	private Flight				flight;

	// Relación muchos Leg -> un Aircraft
	@ManyToOne
	@Mandatory // No sabemos si un aircraft tiene porque tener legs
	@Automapped
	@Valid
	private Aircraft			aircraft;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	private String				flightNumber;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				scheduledDeparture;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	@ValidNumber(min = 1, max = 17)
	private Double				durationInHours;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory // Preguntar si está asociada a aeropuerto o esta propiedad es el nombre
	@Automapped
	@ValidString(max = 50)
	private String				departureAirport;

	@Mandatory // Preguntar si está asociada a aeropuerto o esta propiedad es el nombre
	@Automapped
	@ValidString(max = 50)
	private String				arrivalAirport;

}
