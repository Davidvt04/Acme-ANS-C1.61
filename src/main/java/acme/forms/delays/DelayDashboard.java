
package acme.forms.delays;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelayDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private Airline				airline;

	private List<Delay>			delays;

}
