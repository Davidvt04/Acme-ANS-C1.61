
package acme.forms.delays;

import java.util.Date;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelayDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private int					id;
	private String				departureAirport;
	private String				arrivalAirport;
	private String				airline;
	private Date				departureScheduledDateTime;
	private Date				arrivalScheduledDateTime;
	private Date				departureActualDateTime;
	private Date				arrivalActualDateTime;
	private String				status;
}
