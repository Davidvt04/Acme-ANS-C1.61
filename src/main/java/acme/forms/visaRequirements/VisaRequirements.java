
package acme.forms.visaRequirements;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisaRequirements {

	private String			allowedStay;

	private String			citizenshipDestinationIDtype;

	private List<String>	notes;

	private Requirement		requirement;

	private String			type;

	private Textual			textual;

}
