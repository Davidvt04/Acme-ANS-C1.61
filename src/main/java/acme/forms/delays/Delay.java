
package acme.forms.delays;

import java.util.Date;

import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Delay {

	private Airport	departureAirport;
	private Airport	arrivalAirport;
	private Airline	airline;
	private Date	departureScheduledDateTime;
	private Date	arrivalScheduledDateTime;
	private Date	departureActualDateTime;
	private Date	arrivalActualDateTime;

}
