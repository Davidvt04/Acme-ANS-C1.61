
package acme.forms.recommendations;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private String				city;
	private String				country;
	private String				name;
	private String				description;
	private String				type;

}
