
package acme.entities.delay;

import java.util.Date;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delay extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	private String				departureAirport;
	private String				arrivalAirport;
	private String				airline;
	private Date				departureScheduledDateTime;
	private Date				arrivalScheduledDateTime;
	private Date				departureActualDateTime;
	private Date				arrivalActualDateTime;
	private String				status;

}
