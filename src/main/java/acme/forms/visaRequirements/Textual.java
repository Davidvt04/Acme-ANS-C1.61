
package acme.forms.visaRequirements;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Textual {

	private TextualClass	textualClass; //En el json solo class se llama

	private List<String>	text;
}
