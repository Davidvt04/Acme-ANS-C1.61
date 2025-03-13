
package acme.forms.visaRequirements;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisaRequirementsDashboard extends AbstractForm { //Sherpa API v2

	private static final long		serialVersionUID	= 1L;

	private String					originCountry;
	private String					destinationCountry;
	private String					nationality;
	private List<VisaRequirements>	visaRequirements;

}
